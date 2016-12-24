package audio

import be.tarsos.dsp.pitch.PitchDetectionResult

/**
 * Created by christian.henry on 12/15/16.
 */
data class SingleFrameAudioData(
        var pitchResult: PitchDetectionResult,
        var volume: Double,
        var frequencyMagnitudes: List<Float>)