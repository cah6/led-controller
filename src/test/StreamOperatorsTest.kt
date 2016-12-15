
import audio.FrameData
import be.tarsos.dsp.pitch.PitchDetectionResult
import org.hamcrest.CoreMatchers.*
import org.junit.Assert.*
import org.junit.Test
import rx.Observable
import rx.observers.TestSubscriber
import java.awt.Color

/**
 * Created by christian.henry on 12/15/16.
 */
class StreamOperatorsTest {

    val defaultFrameData = FrameData(PitchDetectionResult(), 0.0)

    @Test
    fun deleteMe() {
        assertTrue(true)
        assertEquals(1, 1)
        assertThat(1, equalTo(1))
    }

    @Test
    fun testNoDataAllBlack() {
        val numEvents = 5
        val repeatedDefaultFrameData = (1..numEvents).map { defaultFrameData }
        val defaultObservable = Observable.from(repeatedDefaultFrameData)

        val result = subjectToFinalStream(defaultObservable)

        val testSubscriber = TestSubscriber<List<Color>>()

        result.subscribe(testSubscriber)

        testSubscriber.assertValueCount(numEvents)

        for (ledArray in testSubscriber.onNextEvents) {
            assertThat(ledArray.size, equalTo(numLeds))
            assertThat(ledArray, equalTo((1..numLeds).map { Color.BLACK }))
        }
    }

}