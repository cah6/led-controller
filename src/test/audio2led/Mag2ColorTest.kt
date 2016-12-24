package audio2led
import audio2led.Mag2Color.magnitudeToColor
import org.hamcrest.CoreMatchers.*
import org.junit.Assert.*
import org.junit.Test
import java.awt.Color

/**
 * Created by christian.henry on 12/23/16.
 */
class Mag2ColorTest {

    @Test
    fun testMagnitudeToColor_Primaries() {
        val black = magnitudeToColor(0.0f)
        val red = magnitudeToColor(1.0f)
        val blue = magnitudeToColor(1f / 3f)
        val green = magnitudeToColor(2f / 3f)

        assertThat(black, equalTo(Color.BLACK))
        assertThat(red, equalTo(Color.RED))
        assertThat(blue, equalTo(Color.BLUE))
        assertThat(green, equalTo(Color.GREEN))
    }

    @Test
    fun testMagnitudeToColor_Mix() {
        val yellow = magnitudeToColor(0.8f)
        assertTrue(yellow.red > 0)
        assertTrue(yellow.green > 0)

        val cyan = magnitudeToColor(0.5f)
        assertTrue(cyan.blue > 0)
        assertTrue(cyan.green > 0)

        val lightBlue = magnitudeToColor(0.2f)
        assertTrue(lightBlue.blue > 0)
        assertTrue(lightBlue.blue < 255)
    }

}