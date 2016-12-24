package audio2led

import java.awt.Color

/**
 * Created by christian.henry on 12/23/16.
 */
object Mag2Color {

    val third = 1f / 3f

    fun magnitudeToColor(magnitude: Float): Color {
        val safeMagnitude = Math.min(1.0, magnitude.toDouble()).toFloat()
        return safeMagToColor(safeMagnitude)
    }

    private fun safeMagToColor(magnitude: Float): Color {
        val red = getRed(magnitude)
        val blue = getBlue(magnitude)
        val green = getGreen(magnitude)

        return Color(red, green, blue)
    }

    private fun getRed(magnitude: Float): Float {
        if (magnitude > 2 * third) {
            return Math.min((magnitude - 2 * third) * 3, 1f)
        } else {
            return 0f
        }
    }

    private fun getBlue(magnitude: Float): Float {
        if (magnitude < 2 * third) {
            val inverseBlue = Math.abs(magnitude - third) * 3
            return Math.max(1f - inverseBlue, 0f)
        } else {
            return 0f
        }
    }

    private fun getGreen(magnitude: Float): Float {
        if (magnitude >= third) {
            val inverseGreen = Math.abs(magnitude - 2 * third) * 3
            return Math.max(1f - inverseGreen, 0f)
        } else {
            return 0f
        }
    }

}