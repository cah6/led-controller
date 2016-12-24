package led
import opc.OpcClient
import opc.OpcDevice
import opc.PixelStrip
import rx.Observable
import java.awt.Color

/**
 * Created by christian.henry on 11/29/16.
 */
class OpcController(opcHost: String, opcPort: Int, ledStripSetup: Map<Int, Int>) {

    val server: OpcClient
    val display: OpcDevice
    val ledStrips: List<PixelStrip>

    init {
        server = OpcClient(opcHost, opcPort)
        display = server.addDevice()
        ledStrips = ledStripSetup.map { it ->
            display.addPixelStrip(it.key, it.value)
        }
    }

    fun connectToStream(colorStream: Observable<List<Color>>) {
        colorStream.subscribe { ledColors ->
            assert(ledStrips[0].pixelCount == ledColors.size)

            ledStrips.forEach {
                for (numLed in 0 until it.pixelCount) {
                    it.setPixelColor(numLed, ledColors[numLed].rgb)
                }
            }

            server.show()
        }
    }

}