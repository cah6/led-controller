package audio2led

import audio.SingleFrameAudioData
import numLeds
import rx.Observable
import smoothWindowSize
import java.awt.Color

/**
 * Created by christian.henry on 12/24/16.
 */
object PitchStreamOperators {
    private data class VolumeAndPitch(val volume: Double, val pitch: Double)

    fun pitchToFinalStream(input: Observable<SingleFrameAudioData>): Observable<List<Color>> {
        return input
                .map { preProcessFrameData(it) }
                .map { audioDataToColor(it) }
                .buffer(smoothWindowSize, 1)
                .map { averagePreviousColors(it) }
                .map { repeatColorToAllLeds(it) }
    }

    private fun preProcessFrameData(singleFrameAudioData: SingleFrameAudioData): VolumeAndPitch {
        if (singleFrameAudioData.pitchResult.isPitched && singleFrameAudioData.pitchResult.probability > 0.85f) {
            return VolumeAndPitch(singleFrameAudioData.volume, singleFrameAudioData.pitchResult.pitch.toDouble())
        } else {
            return VolumeAndPitch(0.0, 0.0)
        }
    }

    private fun audioDataToColor(volumeWithPitch: VolumeAndPitch): Color {
        val maxVolume = 1.0
        val scaledVolume = Math.min(255.0, volumeWithPitch.volume / maxVolume * 255)

        val maxPitch = 1500.0
        val correctedPitch = Math.min(maxPitch, volumeWithPitch.pitch)
        val percentBlue = correctedPitch / maxPitch

        return Color((scaledVolume * (1 - percentBlue)).toInt(), 0, (scaledVolume * percentBlue).toInt())
    }

    private fun averagePreviousColors(previousFrameColors: Collection<Color>): Color {
        var redAgg = 0
        var blueAgg = 0
        for (frame in previousFrameColors) {
            redAgg += frame.red
            blueAgg += frame.blue
        }
        return Color(redAgg / smoothWindowSize, 0, blueAgg / smoothWindowSize)
    }

    private fun repeatColorToAllLeds(colorToRepeat: Color) =
            (1..numLeds).map { colorToRepeat }
}