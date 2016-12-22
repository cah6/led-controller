package audio

import be.tarsos.dsp.AudioEvent
import be.tarsos.dsp.AudioProcessor
import be.tarsos.dsp.util.fft.FFT
import frameData

/**
 * Created by christian.henry on 12/20/16.
 */
class FFTProcessor(size: Int) : AudioProcessor {

    private val fft: FFT = FFT(size)

    override fun process(p0: AudioEvent): Boolean {
        val result: FloatArray = p0.floatBuffer.copyOf()
        fft.forwardTransform(result)
        frameData.frequencyData = result.toList()
        return true
    }

    override fun processingFinished() {
    }

}