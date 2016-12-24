
import audio.SingleFrameAudioData
import rx.Observable
import java.awt.Color

/**
 * Created by christian.henry on 12/13/16.
 */

private data class VolumeAndPitch(val volume: Double, val pitch: Double)

fun fftToFinalStream(input: Observable<SingleFrameAudioData>): Observable<List<Color>> {
    return input
            .map { it.frequencyMagnitudes }
            .map(::scaleMagnitudes)
            .map { FrequencyBinMapper.linFFTBinsToNumLeds(it, numLeds) }
            .map(::magnitudesToColor)
}

private fun scaleMagnitudes(fftValues: List<Float>): List<Float> {
    val max = fftValues.size
    return fftValues.map { it / 2048 }
}

private fun magnitudesToColor(magnitudes: List<Float>): List<Color> {
    val maxAmplitude = 1.0f
    return magnitudes.map {
        val safeMagnitude = Math.min(1.0, it.toDouble()).toFloat()
        Color(maxAmplitude - safeMagnitude, 0f, safeMagnitude)
    }
}

fun pitchToFinalStream(input: Observable<SingleFrameAudioData>): Observable<List<Color>> {
    return input
            .map(::preProcessFrameData)
            .map(::audioDataToColor)
            .buffer(smoothWindowSize, 1)
            .map(::averagePreviousColors)
            .map(::repeatColorToAllLeds)
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

