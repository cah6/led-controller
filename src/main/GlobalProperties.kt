import be.tarsos.dsp.pitch.PitchProcessor

/**
 * Created by christian.henry on 12/13/16.
 */
val echoUrl = "ws://echo.websocket.org"
val opcHost = "localhost"
val opcPort = 7890

val sampleRate = 44100f
val bufferSize = 1024 * 4
val numLeds = 60

val smoothWindowSize = 3
val pitchAlgorithm = PitchProcessor.PitchEstimationAlgorithm.YIN