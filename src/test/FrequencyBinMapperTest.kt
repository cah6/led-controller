import org.hamcrest.CoreMatchers.*
import org.junit.Assert.*
import org.junit.Test

/**
 * Created by christian.henry on 12/22/16.
 */
class FrequencyBinMapperTest {

    @Test
    fun deleteMe() {
        assertTrue(true)
        assertEquals(1, 1)
        assertThat(1, equalTo(1))
    }

    @Test
    fun testAllOnes() {
        val fftData = (1..4096).map { 1.0f }
        val leds = FrequencyBinMapper.logFftBinsToNumLeds(fftData, 60)

        assertThat(leds.size, equalTo(60))
        println(leds)
    }

    @Test
    fun testDecreasing() {
        val fftData = (4096 downTo 1).map(Int::toFloat)
        val leds = FrequencyBinMapper.logFftBinsToNumLeds(fftData, 60)

        assertThat(leds.size, equalTo(60))
        println(leds)
    }

    @Test
    fun testLinDistAllOnes() {
        val fftData = (1..4096).map { 1.0f }
        val leds = FrequencyBinMapper.linFFTBinsToNumLeds(fftData, 60)

        assertThat(leds.size, equalTo(60))
        println(leds.sum())
    }

    @Test
    fun testLinDistDecreasing() {
        val fftData = (4096 downTo 1).map(Int::toFloat)
        val leds = FrequencyBinMapper.linFFTBinsToNumLeds(fftData, 60)

        assertThat(leds.size, equalTo(60))
    }

}