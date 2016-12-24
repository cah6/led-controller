package audio

import be.tarsos.dsp.AudioEvent
import be.tarsos.dsp.AudioProcessor
import be.tarsos.dsp.pitch.PitchDetectionResult
import be.tarsos.dsp.pitch.PitchProcessor
import frameData

/**
 * Created by christian.henry on 12/15/16.
 */
object AudioProcessors {

    fun defaultPitchProcessor(sampleRate: Float, bufferSize: Int, algo: PitchProcessor.PitchEstimationAlgorithm): AudioProcessor {
        return PitchProcessor(algo, sampleRate, bufferSize) {
            result: PitchDetectionResult, audioEvent: AudioEvent ->
            if (result.isPitched) {
                println("Detected pitch: [${result.isPitched}] of ${result.pitch} with probability ${result.probability}")
            }
            frameData.pitchResult = result
        }
    }

}