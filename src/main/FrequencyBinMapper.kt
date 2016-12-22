/**
 * Created by christian.henry on 12/22/16.
 */
object FrequencyBinMapper {

    /**
     * Integral of e^(x/c) from 1 to 60 = 4096 is c = 9.96441. Plug this into equation and solve for
     * # of frequency bins averaged for each LED.
     */
    fun logFftBinsToNumLeds(fftValues: List<Float>, numLeds: Int): List<Float> {
        var currentIndex = 0
        return (1..numLeds).map {
            val index = Math.floor(Math.pow(Math.E, it / 10.0)).toInt()
            val newUpperIndex = Math.min(currentIndex + index, fftValues.size)
            val sum = fftValues.subList(currentIndex, newUpperIndex).sum()
            currentIndex = newUpperIndex
            sum
        }
    }

    fun linFFTBinsToNumLeds(fftValues: List<Float>, numLeds: Int): List<Float> {
        var currentIndex = 0
        return (1..numLeds).map {
            val index = Math.floor(fftValues.size / numLeds.toDouble()).toInt()
            val newUpperIndex = Math.min(currentIndex + index, fftValues.size)
            val sum = fftValues.subList(currentIndex, newUpperIndex).sum()
            currentIndex = newUpperIndex
            sum
        }
    }
}