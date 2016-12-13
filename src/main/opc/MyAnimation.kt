package opc

/**
 * Created by christian.henry on 11/29/16.
 */
class MyAnimation : Animation() {

    var rand: java.util.Random = java.util.Random()

    override fun reset(strip: PixelStrip) {
        rand = java.util.Random()
    }

    override fun draw(strip: PixelStrip): Boolean {
        val randomPixel = rand.nextInt(strip.pixelCount)
        val randomColor = Animation.makeColor(rand.nextInt(255),
                rand.nextInt(255), rand.nextInt(255))
        strip.setPixelColor(randomPixel, randomColor)
        return true
    }
}
