
import opc.MyAnimation
import opc.OpcClient
import opc.OpcDevice
import opc.PixelStrip
import rx.Observable
import java.awt.Color

/**
 * Created by christian.henry on 11/29/16.
 */
class OpcController(opcHost: String, opcPort: Int, numLeds: Int) {

    val server: OpcClient
    val display: OpcDevice
    val strip: PixelStrip

    init {
        server = OpcClient(opcHost, opcPort)
        display = server.addDevice()
        strip = display.addPixelStrip(0, numLeds)
    }

    fun connectToStream(colorStream: Observable<List<Color>>) {
        colorStream.subscribe {
            assert(strip.pixelCount == it.size)

            for (numLed in 0 until numLeds) {
                strip.setPixelColor(numLed, it[numLed].rgb)
            }

            server.show()
        }
    }

    fun testLeds() {
        (0 until numLeds).forEach { strip.setPixelColor(it, Color.WHITE.rgb) }
        server.show()
        Thread.sleep(5000)
        strip.clear()
        server.show()
    }

    fun animate() {
        strip.animation = MyAnimation()
        server.animate()
    }

}