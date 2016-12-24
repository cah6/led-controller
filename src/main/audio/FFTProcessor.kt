package audio

import be.tarsos.dsp.AudioEvent
import be.tarsos.dsp.AudioProcessor
import be.tarsos.dsp.util.fft.BlackmanWindow
import be.tarsos.dsp.util.fft.FFT
import frameData

/**
 * Created by christian.henry on 12/20/16.
 */
class FFTProcessor(size: Int) : AudioProcessor {

    private val fft: FFT = FFT(size, BlackmanWindow())

    override fun process(p0: AudioEvent): Boolean {
        val result: FloatArray = p0.floatBuffer.copyOf()
        if (result.any{ it > 1.0 }) {
            throw Exception("Did not expect any data value larger than 1.0, needs to be scaled!")
        }
        fft.forwardTransform(result)
        frameData.frequencyMagnitudes = complexToMagnitude(result)
        return true
    }

    /**
     * Convert complex frequency array to magnitudes. This means that output list will be half as long.
     */
    private fun complexToMagnitude(complexValues: FloatArray): List<Float> {
        val amplitudes = FloatArray(complexValues.size / 2)
        fft.modulus(complexValues, amplitudes)
        return amplitudes.toList()
    }

    override fun processingFinished() {
    }

}