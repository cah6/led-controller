import audio.AudioProcessors
import audio.Dispatchers
import audio.FeatureExtractor
import audio.SingleFrameAudioData
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
            AudioProcessors.defaultFftProcessor(bufferSize))
    val subject: Subject<SingleFrameAudioData, SingleFrameAudioData> = featureExtractor.startProcessing()

    val ledStream = fftToFinalStream(subject)

    val controller: OpcController = OpcController(opcHost, opcPort, numLeds)
    controller.connectToStream(ledStream)

    latch.await()
}