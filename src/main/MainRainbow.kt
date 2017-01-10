
import led.OpcController
import rx.Observable
import java.awt.Color
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Created by christian.henry on 1/5/17.
 */
fun main(args: Array<String>) {
    val latch: CountDownLatch = CountDownLatch(1)

    val timer: Observable<Long> = Observable.interval(50, TimeUnit.MILLISECONDS)

    val ledStream = timer
            .map { (it..(it + numLeds - 1)).toList() }
            .map { it.map { colorWheel(it) }
            }

    val controller: OpcController = OpcController(opcHost, opcPort, ledStripSetup)
    controller.connectToStream(ledStream)

    latch.await()
}

/**
 * Input a value 0 to 1 to get a color value.
 * The colors are a transition r - g - b - back to r.
 */
private fun colorWheel(n: Long): Color {
    val percentageOfSpectrumVisible = 0.25f
    val scalingValue = numLeds / percentageOfSpectrumVisible
    val rgb = Color.HSBtoRGB((n / scalingValue) % numLeds, 1f, 1f)
    return Color(rgb)
}