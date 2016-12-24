package audio

/**
 * Log scale conversion functions. See for more details:
 * http://stackoverflow.com/questions/19472747/convert-linear-scale-to-logarithmic
 *
 * Created by christian.henry on 12/23/16.
 */
class Lin2LogConverter(x1: Double, y1: Double, x2: Double, y2: Double) {

    val b = Math.log(y1 / y2) / (x1 - x2)
    val a = y1 / Math.exp(this.b * x1)

    fun lin2Log(x: Double): Double {
        return this.a * Math.exp(this.b * x)
    }

    fun log2lin(y: Double): Double {
        return Math.log(y / this.a) / this.b
    }

}
