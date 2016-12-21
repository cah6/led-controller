package audio
import be.tarsos.dsp.AudioDispatcher
import be.tarsos.dsp.AudioProcessor
import rx.Observable
import rx.Subscriber
import rx.subjects.PublishSubject
import rx.subjects.Subject
import rx.util.async.Async

/**
 * Created by christian.henry on 11/21/16.
 */

class FeatureExtractor(val audioDispatcher: AudioDispatcher, vararg val audioProcessors: AudioProcessor) {

    fun startProcessing(): Subject<SingleFrameAudioData, SingleFrameAudioData> {
        val singleFrameAudioAnalysis: Subject<SingleFrameAudioData, SingleFrameAudioData> = PublishSubject.create()

        audioProcessors.forEach { audioDispatcher.addAudioProcessor(it) }
        audioDispatcher.addAudioProcessor(FeatureEmitter(singleFrameAudioAnalysis))

        val fromRunnable: Observable<Int.Companion> = Async.fromRunnable(audioDispatcher, Int)

        fromRunnable.subscribe(object : Subscriber<Int.Companion>() {
            override fun onNext(t: Int.Companion?) {
                println("Starting audio stream!")
            }

            override fun onCompleted() {
                println("Audio stream completed, completing publish subject.")
                singleFrameAudioAnalysis.onCompleted()
            }

            override fun onError(e: Throwable) {
                throw e
            }

        })

        return singleFrameAudioAnalysis
    }

}

