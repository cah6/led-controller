
import audio.Dispatchers
import audio.FFTProcessor
import audio.FeatureExtractor
import audio.SingleFrameAudioData
import audio2led.FFTStreamOperators
import led.DrawingController
import led.OpcController
import rx.subjects.Subject
import java.util.concurrent.CountDownLatch

/**
 * Created by christian.henry on 12/21/16.
 */

fun main(args: Array<String>) {
    val latch: CountDownLatch = CountDownLatch(1)

    val featureExtractor: FeatureExtractor = FeatureExtractor(
            Dispatchers.fromDefaultMicrophone(),
            FFTProcessor(bufferSize))
    val subject: Subject<SingleFrameAudioData, SingleFrameAudioData> = featureExtractor.startProcessing()

    val ledStream = FFTStreamOperators.fftToFinalStream(subject)

    val multiLedStream = ledStream.publish()
    val controller: OpcController = OpcController(opcHost, opcPort, ledStripSetup)
    controller.connectToStream(multiLedStream)

    val drawingController: DrawingController = DrawingController(ledStripSetup)
    drawingController.connectToStream(multiLedStream)

    println("All streams connected, waiting until program close.")
    multiLedStream.connect()
}