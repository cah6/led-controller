import org.hamcrest.CoreMatchers.*
import org.junit.Assert.*
import org.junit.Test

/**
 * Created by christian.henry on 12/22/16.
 */
class FrequencyBinMapperTest {

    @Test
    fun testAllOnes() {
        val fftData = (1..4096).map { 1.0f }
        val leds = FrequencyBinMapper.logFftBinsToNumLeds(fftData, 60)

        assertThat(leds.size, equalTo(60))
        for (i in 0..(leds.size - 2)) {
            assertTrue(leds[i + 1] > leds[i])
        }

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
    fun testSumWithScaledBoundaries() {
        val values = listOf(2.0f, 1.0f, 2.0f, 3.0f, 1.0f)
        val sum = FrequencyBinMapper.sumWithScaledBoundaries(values, 0.25, 0.5)
        assertThat(sum, equalTo(7.0f))
    }

}