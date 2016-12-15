import audio.FeatureExtractor
import audio.FrameData
import led.OpcController
import org.hamcrest.CoreMatchers.*
import org.junit.Assert.*
import org.junit.Test
import rx.subjects.Subject

/**
 * Created by christian.henry on 12/14/16.
 */
class EndToEndTest {

    val testFileDispatcher = FeatureExtractor.dispatcherFromFile("/Users/christian.henry/Documents/coding/led/kotlin-led-controller/resources/snap-whistle.wav")

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
                FeatureExtractor.makePitchProcessor(sampleRate, bufferSize, pitchAlgorithm))
        val subject: Subject<FrameData, FrameData> = featureExtractor.startProcessing()

        val ledStream = subjectToFinalStream(subject)

        val controller: OpcController = OpcController(opcHost, opcPort, numLeds)
        controller.connectToStream(ledStream)


        Thread.sleep(5000)
    }

}