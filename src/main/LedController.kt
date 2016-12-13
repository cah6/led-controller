
import rx.Observable
import java.awt.Color
import java.net.URI
import java.util.concurrent.TimeUnit

/**
 * Created by christian.henry on 11/16/16.
 */
class LedController(observable: Observable<ByteArray>, serverURI: String) {

    val myEndpoint: MyEndpoint

    init {
        myEndpoint = MyEndpoint(observable, URI(serverURI))
        myEndpoint.continuouslyConnect()
    }

    companion object ArrayConverter {
        fun createDefaultObservable(): Observable<ByteArray> {
            val initColors: List<Color> = (1..5).map { it -> Color.WHITE }
            val writeData: ByteArray = LedController.writeData(initColors)
            return Observable.interval(2, TimeUnit.SECONDS).map { it -> writeData }
        }

        fun writeData(pixelColors: List<Color>): ByteArray {
            val bytes: List<Byte> = convertToBytes(pixelColors)
            return bytes.toByteArray()
        }

        private fun convertToBytes(pixelColors: List<Color>): List<Byte> {
            return listOf<Byte>(0, 0) + getLengthBytes(pixelColors) + getDataBytes(pixelColors)
        }

        private fun getLengthBytes(pixelColors: List<Color>): List<Byte> {
            val numBytes:Int = pixelColors.size * 3
            val firstByte = numBytes shr 8
            val secondByte = numBytes and 0xFF
            return listOf(firstByte.toByte(), secondByte.toByte())
        }

        private fun getDataBytes(pixelColors: List<Color>): List<Byte> {
            return pixelColors.map { it -> listOf(it.red.toByte(), it.green.toByte(), it.blue.toByte()) }.flatten()
        }
    }
}
