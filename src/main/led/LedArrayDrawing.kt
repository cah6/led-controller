package led

import numLeds
import java.awt.Color
import java.awt.Graphics
import javax.swing.JPanel

/**
 * Created by christian.henry on 1/9/17.
 */
class LedArrayDrawing(numLeds: Int, val windowWidth: Int) : JPanel() {

    var currentColors: List<Color> = (1..numLeds).map { Color(it, it, it) }

    fun setColors(newColors: List<Color>) {
        currentColors = newColors
    }

    override fun paintComponent(g: Graphics?) {
        g ?: throw NullPointerException()
        super.paintComponent(g)
        currentColors.mapIndexed { i, color ->
            g.color = color
            g.fillRect(i * windowWidth / numLeds, 0, windowWidth / numLeds, 100)
        }
    }

}