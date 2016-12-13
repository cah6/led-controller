
import be.tarsos.dsp.AudioEvent
import be.tarsos.dsp.AudioProcessor
import be.tarsos.dsp.pitch.PitchDetectionResult
import rx.subjects.Subject

/**
 * Created by christian.henry on 11/23/16.
 */
class FeatureEmitter(val analysisSubject: Subject<FrameData, FrameData>) : AudioProcessor {

    override fun process(audioEvent: AudioEvent): Boolean {
        frameData.volume = audioEvent.rms * 30

        // println("Processed frame, calculated $frameData")
        // println("Processed audio and sending rms level of ${audioEvent.rms}")

        analysisSubject.onNext(frameData)

        frameData.pitchResult = PitchDetectionResult()
        frameData.beatResult = BeatResult(false, 0.0)
        frameData.volume = 0.0
        return true
    }

    override fun processingFinished() {
    }

}