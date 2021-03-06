
import audio.AudioProcessors
import audio.Dispatchers
import audio.FeatureExtractor
import audio.SingleFrameAudioData
import audio2led.PitchStreamOperators
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
            AudioProcessors.defaultPitchProcessor(sampleRate, bufferSize, pitchAlgorithm))
    val subject: Subject<SingleFrameAudioData, SingleFrameAudioData> = featureExtractor.startProcessing()

    val ledStream = PitchStreamOperators.pitchToFinalStream(subject)

    val controller: OpcController = OpcController(opcHost, opcPort, ledStripSetup)
    controller.connectToStream(ledStream)

    latch.await()
}