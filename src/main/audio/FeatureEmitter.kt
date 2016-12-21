package audio
import be.tarsos.dsp.AudioEvent
import be.tarsos.dsp.AudioProcessor
import be.tarsos.dsp.pitch.PitchDetectionResult
import frameData
import rx.subjects.Subject

/**
 * Created by christian.henry on 11/23/16.
 */
class FeatureEmitter(val analysisSubject: Subject<SingleFrameAudioData, SingleFrameAudioData>) : AudioProcessor {

    override fun process(audioEvent: AudioEvent): Boolean {
        // push volume up to be between 0 and 1 normally
        frameData.volume = audioEvent.rms * 6

        analysisSubject.onNext(frameData)

        // reset global frameData
        frameData.pitchResult = PitchDetectionResult()
        frameData.volume = 0.0
        return true
    }

    override fun processingFinished() {
    }

}