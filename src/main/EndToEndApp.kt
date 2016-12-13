
import be.tarsos.dsp.pitch.PitchProcessor
import rx.Observable
import rx.subjects.Subject
import java.awt.Color
import java.util.concurrent.CountDownLatch

/**
 * Created by christian.henry on 11/18/16.
 */

val echoUrl = "ws://echo.websocket.org"
val opcHost = "localhost"
val opcPort = 7890

val sampleRate = 44100f
val bufferSize = 1024 * 4
val numLeds = 60

val testFileDispatcher = FeatureExtractor.dispatcherFromFile("/Users/christian.henry/Documents/coding/led/kotlin-led-controller/resources/snap-whistle.wav")
val microphoneDispatcher = FeatureExtractor.makeDefaultDispatcher()

fun main(args: Array<String>) {
    val latch: CountDownLatch = CountDownLatch(1)

    val featureExtractor: FeatureExtractor = FeatureExtractor(
            microphoneDispatcher,
            // good algos: MPM or YIN
            FeatureExtractor.makePitchProcessor(sampleRate, bufferSize, PitchProcessor.PitchEstimationAlgorithm.YIN))
    val subject: Subject<FrameData, FrameData> = featureExtractor.startProcessing()

    data class VolumeAndPitch(val volume: Double, val pitch: Double)

    val volumeAndPitch: Observable<VolumeAndPitch> = subject.map { frameData ->
        if (frameData.pitchResult.isPitched && frameData.pitchResult.probability > 0.85f) {
            VolumeAndPitch(frameData.volume, frameData.pitchResult.pitch.toDouble())
        } else {
            VolumeAndPitch(0.0, 0.0)
        }
    }

    val colorStream: Observable<Color> = volumeAndPitch.map {
        val maxVolume = 5.0
        val scaledVolume = Math.min(255.0, it.volume / maxVolume * 255)

        val maxPitch = 1500.0
        val correctedPitch = Math.min(maxPitch, it.pitch)
        val percentBlue = correctedPitch / maxPitch

        Color((scaledVolume * (1 - percentBlue)).toInt(), 0, (scaledVolume * percentBlue).toInt())
    }

    val windowSize = 3
    val timeSmoothedColor: Observable<Color> = colorStream.buffer(windowSize, 1).map {
        var redAgg = 0
        var blueAgg = 0
        for (color in it) {
            redAgg += color.red
            blueAgg += color.blue
        }
        Color(redAgg / windowSize, 0, blueAgg / windowSize)
    }

    val allLeds: Observable<List<Color>> = timeSmoothedColor.map { colorToRepeat ->
        (1..numLeds).map { colorToRepeat }
    }


    val controller: OpcController = OpcController(opcHost, opcPort, numLeds)
    controller.connectToStream(allLeds)

    latch.await()
}

fun intToByteArray(value: Int): ByteArray {
    val list: List<Color> = (1..numLeds).map { it -> Color(value, value, value)}
    return LedController.writeData(list)
}