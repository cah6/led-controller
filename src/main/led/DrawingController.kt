package led

import numLeds
import rx.Observable
import java.awt.Color
import java.awt.Dimension
import java.awt.Toolkit
import javax.swing.JFrame

/**
 * Created by christian.henry on 1/8/17.
 */
class DrawingController(ledStrips: Map<Int, Int>): JFrame() {

    val windowWidth = Toolkit.getDefaultToolkit().screenSize.width / 2
    val leds = LedArrayDrawing(ledStrips[0]!!, windowWidth)

    init {
        title = "LED Preview"
        defaultCloseOperation = EXIT_ON_CLOSE
        isVisible = true
        size = Dimension(windowWidth, 100)
        setLocationRelativeTo(null)

        contentPane.add(leds)
    }

    fun connectToStream(colorStream: Observable<List<Color>>) {
        colorStream.subscribe { ledColors ->
            assert(numLeds == ledColors.size)
            leds.setColors(ledColors)
            repaint()
        }
    }

}
