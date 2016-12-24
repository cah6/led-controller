package audio2led

import audio.SingleFrameAudioData
import numLeds
import rx.Observable
import java.awt.Color

/**
 * Created by christian.henry on 12/24/16.
 */
object AudioStreamOperators {

    fun fftToFinalStream(input: Observable<SingleFrameAudioData>): Observable<List<Color>> {
        return input
                .map { it.frequencyMagnitudes }
                .map { scaleMagnitudes(it) }
                .map { FrequencyBinMapper.logFftBinsToNumLeds(it, numLeds) }
                .map{ it.map { Mag2Color.magnitudeToColor(it) } }
    }

    // todo: before or after doing log conversion?
    private fun scaleMagnitudes(fftValues: List<Float>): List<Float> {
        return fftValues.map { it / 16 }
    }
}