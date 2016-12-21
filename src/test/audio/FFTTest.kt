package audio

import be.tarsos.dsp.util.fft.FFT
import org.hamcrest.CoreMatchers.*
import org.junit.Assert.*
import org.junit.Test

/**
 * Created by christian.henry on 12/20/16.
 */
class FFTTest {

    @Test
    fun testSine() {
        val target: FFT = FFT(4)

        val data = listOf(0f, 1f, 0f, -1f).toFloatArray()
        val result = data.copyOf()

        target.forwardTransform(result)

        assertThat(Math.abs(result[3]), equalTo(2.0f))
    }

    @Test
    fun testConstant() {
        val target: FFT = FFT(4)

        val data = listOf(1f, 1f, 1f, 1f).toFloatArray()
        val result = data.copyOf()

        target.forwardTransform(result)

        assertThat(result[0], equalTo(4.0f))
    }

}