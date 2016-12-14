
import audio.FrameData
import rx.Observable
import rx.subjects.Subject
import java.awt.Color

/**
 * Created by christian.henry on 12/13/16.
 */

private data class VolumeAndPitch(val volume: Double, val pitch: Double)

fun subjectToFinalStream(input: Subject<FrameData, FrameData>): Observable<List<Color>> {
    return input
            .map { frameData ->
                if (frameData.pitchResult.isPitched && frameData.pitchResult.probability > 0.85f) {
                    VolumeAndPitch(frameData.volume, frameData.pitchResult.pitch.toDouble())
                } else {
                    VolumeAndPitch(0.0, 0.0)
                }
            }
            .map { volumeWithPitch ->
                val maxVolume = 5.0
                val scaledVolume = Math.min(255.0, volumeWithPitch.volume / maxVolume * 255)

                val maxPitch = 1500.0
                val correctedPitch = Math.min(maxPitch, volumeWithPitch.pitch)
                val percentBlue = correctedPitch / maxPitch

                Color((scaledVolume * (1 - percentBlue)).toInt(), 0, (scaledVolume * percentBlue).toInt())
            }
            .buffer(smoothWindowSize, 1)
            .map { previousFrameColors ->
                var redAgg = 0
                var blueAgg = 0
                for (frame in previousFrameColors) {
                    redAgg += frame.red
                    blueAgg += frame.blue
                }
                Color(redAgg / smoothWindowSize, 0, blueAgg / smoothWindowSize)
            }
            .map { colorToRepeat ->
                (1..numLeds).map { colorToRepeat }
            }
}
