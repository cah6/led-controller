
import audio.AudioProcessors
import audio.Dispatchers
import audio.FeatureExtractor
import audio.SingleFrameAudioData
import audio2led.PitchStreamOperators.pitchToFinalStream
import led.OpcController
import org.hamcrest.CoreMatchers.*
import org.junit.Assert.*
import org.junit.Test
import rx.observers.TestSubscriber
import rx.subjects.Subject
import java.awt.Color
import java.util.concurrent.TimeUnit

/**
 * Created by christian.henry on 12/14/16.
 */
class EndToEndTest {

    val testFileDispatcher = Dispatchers.fromFile("/Users/christian.henry/Documents/coding/led/kotlin-led-controller/resources/snap-whistle.wav")

    @Test
    fun deleteMe() {
        assertTrue(true)
        assertEquals(1, 1)
        assertThat(1, equalTo(1))
    }

    @Test
    fun test1() {
        val featureExtractor: FeatureExtractor = FeatureExtractor(
                testFileDispatcher,
                AudioProcessors.defaultPitchProcessor(sampleRate, bufferSize, pitchAlgorithm))
        val subject: Subject<SingleFrameAudioData, SingleFrameAudioData> = featureExtractor.startProcessing()
        val ledStream = pitchToFinalStream(subject)
        val controller: OpcController = OpcController(opcHost, opcPort, ledStripSetup)
        controller.connectToStream(ledStream)

        val testSubscriber = TestSubscriber<List<Color>>()
        ledStream.subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent(5000, TimeUnit.MILLISECONDS)
    }

}