
import audio.FrameData
import rx.Observable
import java.awt.Color

/**
 * Created by christian.henry on 12/13/16.
 */

private data class VolumeAndPitch(val volume: Double, val pitch: Double)

fun subjectToFinalStream(input: Observable<FrameData>): Observable<List<Color>> {
    return input
            .map(::preProcessFrameData)
            .map(::audioDataToColor)
            .buffer(smoothWindowSize, 1)
            .map(::averagePreviousColors)
            .map(::repeatColorToAllLeds)
}

private fun preProcessFrameData(frameData: FrameData): VolumeAndPitch {
    if (frameData.pitchResult.isPitched && frameData.pitchResult.probability > 0.85f) {
        return VolumeAndPitch(frameData.volume, frameData.pitchResult.pitch.toDouble())
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

