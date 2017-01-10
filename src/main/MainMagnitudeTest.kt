
import audio2led.Mag2Color
import led.OpcController
import rx.Observable
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Created by christian.henry on 1/5/17.
 */
fun main(args: Array<String>) {
    val latch: CountDownLatch = CountDownLatch(1)

    val timer: Observable<Long> = Observable.interval(150, TimeUnit.MILLISECONDS)

    val ledStream = timer
            .map { magnitude -> (1..numLeds).map { (magnitude % 100) / 100f } }
            .map { it.map { Mag2Color.magnitudeToColor(it) } }

    val controller: OpcController = OpcController(opcHost, opcPort, ledStripSetup)
    controller.connectToStream(ledStream)

    latch.await()
}