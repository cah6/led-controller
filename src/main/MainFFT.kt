
import audio.Dispatchers
import audio.FFTProcessor
import audio.FeatureExtractor
import audio.SingleFrameAudioData
import audio2led.FFTStreamOperators
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

    val controller: OpcController = OpcController(opcHost, opcPort, ledStripSetup)
    controller.connectToStream(ledStream)

    latch.await()
}