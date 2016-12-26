package audio2led
import audio.SingleFrameAudioData
import audio2led.FFTStreamOperators.fftToFinalStream
import be.tarsos.dsp.pitch.PitchDetectionResult
import bufferSize
import numLeds
import org.hamcrest.CoreMatchers.*
import org.junit.Assert.*
import org.junit.Test
import rx.Observable
import rx.observers.TestSubscriber
import java.awt.Color

/**
 * Created by christian.henry on 12/15/16.
 */
class AudioStreamOperatorsTest {
    val emptyFFTData = (1..bufferSize/2).map { 0f }
    val defaultFrameData = SingleFrameAudioData(PitchDetectionResult(), 0.0, emptyFFTData)

    @Test
    fun testNoDataAllBlack() {
        val numEvents = 5
        val repeatedDefaultFrameData = (1..numEvents).map { defaultFrameData }
        val defaultObservable = Observable.from(repeatedDefaultFrameData)

        val result = fftToFinalStream(defaultObservable)

        val testSubscriber = TestSubscriber<List<Color>>()

        result.subscribe(testSubscriber)

        testSubscriber.assertValueCount(numEvents)

        for (ledArray in testSubscriber.onNextEvents) {
            assertThat(ledArray.size, equalTo(numLeds))
            assertThat(ledArray, equalTo((1..numLeds).map { Color.BLACK }))
        }
    }

}