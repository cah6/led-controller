import audio.SingleFrameAudioData
import be.tarsos.dsp.pitch.PitchDetectionResult
import be.tarsos.dsp.pitch.PitchProcessor

/**
 * Created by christian.henry on 12/13/16.
 */
val opcHost = "localhost"
val opcPort = 7890

val sampleRate = 44100f
val bufferSize = 1024 * 4
val numLeds = 60

val smoothWindowSize = 3
val pitchAlgorithm = PitchProcessor.PitchEstimationAlgorithm.YIN

// Currently global so that various audio processors can share. Should lock down more.
val frameData = SingleFrameAudioData(PitchDetectionResult(), 0.0, listOf())