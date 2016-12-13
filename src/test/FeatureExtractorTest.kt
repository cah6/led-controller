
import be.tarsos.dsp.AudioDispatcher
import be.tarsos.dsp.AudioEvent
import be.tarsos.dsp.AudioProcessor
import be.tarsos.dsp.io.TarsosDSPAudioFormat
import be.tarsos.dsp.io.TarsosDSPAudioInputStream
import be.tarsos.dsp.io.UniversalAudioInputStream
import be.tarsos.dsp.pitch.PitchProcessor
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.timeout
import java.io.ByteArrayInputStream

/**
 * Created by christian.henry on 11/21/16.
 */
class FeatureExtractorTest {

    val sampleRate = 44100f
    val bufferSize = 2048

    val oneFrameInput: ByteArray = ((1..bufferSize).map(Int::toByte)).toByteArray()
    val twoFrameInput: ByteArray = ((1..bufferSize + 2).map(Int::toByte)).toByteArray()

    @Test
    fun testMockProcessor1() {
        val processor: AudioProcessor = Mockito.mock(AudioProcessor::class.java)

        val target: FeatureExtractor = FeatureExtractor(makeTestDispatcher(oneFrameInput), processor)
        target.startProcessing()
        Mockito.verify(processor, Mockito.times(1)).process(Mockito.any(AudioEvent::class.java))
        Mockito.reset(processor)
    }

    @Test
    fun testMockProcessor2() {
        val processor: AudioProcessor = Mockito.mock(AudioProcessor::class.java)

        val target: FeatureExtractor = FeatureExtractor(makeTestDispatcher(twoFrameInput), processor)
        val subject = target.startProcessing()
        Mockito.verify(processor, timeout(5000).times(2)).process(Mockito.any(AudioEvent::class.java))
        Mockito.reset(processor)
    }

    @Test
    fun testRealProcessors() {
        val onsetProcessor = FeatureExtractor.makeOnsetProcessor(sampleRate, bufferSize)
        val pitchProcessor = FeatureExtractor.makePitchProcessor(sampleRate, bufferSize, PitchProcessor.PitchEstimationAlgorithm.FFT_YIN)

        var target: FeatureExtractor = FeatureExtractor(makeTestDispatcher(oneFrameInput), onsetProcessor, pitchProcessor)
        val subject = target.startProcessing()
    }

    fun makeTestDispatcher(data: ByteArray): AudioDispatcher {
        val format: TarsosDSPAudioFormat = TarsosDSPAudioFormat(sampleRate, 8, 1, true, true)
        val audioInputStream: TarsosDSPAudioInputStream = UniversalAudioInputStream(ByteArrayInputStream(data), format)
        val testDispatcher = AudioDispatcher(audioInputStream, bufferSize, bufferSize / 2)
        return testDispatcher
    }


}