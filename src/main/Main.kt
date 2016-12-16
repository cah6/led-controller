
import audio.Dispatchers
import audio.FeatureExtractor
import audio.FrameData
import led.OpcController
import rx.subjects.Subject
import java.util.concurrent.CountDownLatch

/**
 * Created by christian.henry on 12/13/16.
 */

fun main(args: Array<String>) {
    val latch: CountDownLatch = CountDownLatch(1)

    val featureExtractor: FeatureExtractor = FeatureExtractor(
            Dispatchers.fromDefaultMicrophone(),
            FeatureExtractor.makePitchProcessor(sampleRate, bufferSize, pitchAlgorithm))
    val subject: Subject<FrameData, FrameData> = featureExtractor.startProcessing()

    val ledStream = subjectToFinalStream(subject)

    val controller: OpcController = OpcController(opcHost, opcPort, numLeds)
    controller.connectToStream(ledStream)

    latch.await()
}