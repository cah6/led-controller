package audio
import be.tarsos.dsp.AudioDispatcher
import be.tarsos.dsp.AudioEvent
import be.tarsos.dsp.AudioProcessor
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory
import be.tarsos.dsp.onsets.OnsetHandler
import be.tarsos.dsp.onsets.PercussionOnsetDetector
import be.tarsos.dsp.pitch.PitchDetectionResult
import be.tarsos.dsp.pitch.PitchProcessor
import bufferSize
import rx.Observable
import rx.Subscriber
import rx.subjects.PublishSubject
import rx.subjects.Subject
import rx.util.async.Async
import sampleRate
import java.io.File
import javax.sound.sampled.AudioFormat

/**
 * Created by christian.henry on 11/21/16.
 */
data class BeatResult(val isDetected: Boolean, val beatStrength: Double)
data class FrameData(var pitchResult: PitchDetectionResult, var beatResult: BeatResult, var volume: Double)
val frameData = FrameData(PitchDetectionResult(), BeatResult(false, 0.0), 0.0)

class FeatureExtractor(val audioDispatcher: AudioDispatcher, vararg val audioProcessors: AudioProcessor) {

    fun startProcessing(): Subject<FrameData, FrameData> {
        val frameAnalysis: Subject<FrameData, FrameData> = PublishSubject.create()

        audioProcessors.forEach { audioDispatcher.addAudioProcessor(it) }
        audioDispatcher.addAudioProcessor(FeatureEmitter(frameAnalysis))

        val fromRunnable: Observable<Int.Companion> = Async.fromRunnable(audioDispatcher, Int)

        fromRunnable.subscribe(object : Subscriber<Int.Companion>() {
            override fun onNext(t: Int.Companion?) {
                println("Starting audio stream!")
            }

            override fun onCompleted() {
                println("Audio stream completed, completing publish subject.")
                frameAnalysis.onCompleted()
            }

            override fun onError(e: Throwable) {
                throw e
            }

        })

        return frameAnalysis
    }

    companion object {

        fun makeDefaultDispatcher(): AudioDispatcher {
            val fromDefaultMicrophone = AudioDispatcherFactory.fromDefaultMicrophone(sampleRate.toInt(), bufferSize, bufferSize / 2)
            println("Format used is ${fromDefaultMicrophone.format}")
            return fromDefaultMicrophone
        }

        fun dispatcherFromBytes(fileLocation: String): AudioDispatcher {
            val file = File(fileLocation)
            val fileAsBytes = file.readBytes()
            val audioFormat = AudioFormat(sampleRate, 8, 2, true, true)

            return AudioDispatcherFactory.fromByteArray(fileAsBytes, audioFormat, bufferSize, bufferSize / 2)
        }

        fun dispatcherFromFile(fileLocation: String): AudioDispatcher {
            val file = File(fileLocation)
            return AudioDispatcherFactory.fromFile(file, bufferSize, bufferSize / 2)
        }

        fun makeOnsetProcessor(sampleRate: Float, bufferSize: Int): AudioProcessor {
            return PercussionOnsetDetector(sampleRate, bufferSize, OnsetHandler { time: Double, salience: Double ->
                println("Onset with salience $salience at time $time")
                // audio.getFrameData.beatResult = audio.BeatResult(true, salience)

            }, 40.0, 0.5)
        }

        fun makePitchProcessor(sampleRate: Float, bufferSize: Int, algo: PitchProcessor.PitchEstimationAlgorithm): AudioProcessor {
            return PitchProcessor(algo, sampleRate, bufferSize) {
                result: PitchDetectionResult, audioEvent: AudioEvent ->
                    if (result.isPitched) {
                        println("Detected pitch: [${result.isPitched}] of ${result.pitch} with probability ${result.probability}")
                    }
                    frameData.pitchResult = result
            }
        }
    }



}

