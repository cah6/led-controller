
import org.junit.Assert
import org.junit.Test
import java.awt.Color

/**
 * Created by christian.henry on 11/16/16.
 */
class LedControllerTest {

    val target = LedController(LedController.createDefaultObservable(), "ws://echo.websocket.org")

    @Test
    fun writeData() {
        val colorMix = listOf<Color>(Color.WHITE, Color.BLACK, Color.WHITE)
        val result = LedController.writeData(colorMix)

        println(result)

        Assert.assertArrayEquals(byteArrayOf(0, 0, 0, 9,
                255.toByte(), 255.toByte(), 255.toByte(),
                0, 0, 0,
                255.toByte(), 255.toByte(), 255.toByte()), result)
    }

}