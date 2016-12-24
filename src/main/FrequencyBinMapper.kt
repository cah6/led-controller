import audio.Lin2LogConverter

/**
 * Created by christian.henry on 12/22/16.
 */
object FrequencyBinMapper {

    /**
     * Start with bufferSize / 2 number of FFT values. Need to map this to 60 LEDs,
     * doing this on a log scale will make sure lower frequency ranges get adequate
     * resolution as well.
     */
    fun logFftBinsToNumLeds(fftValues: List<Float>, numLeds: Int): List<Float> {
        val logFrequencyIncrements = getLogIncrements(20.0, 20000.0, numLeds)

        val ledMagnitudes = mutableListOf<Float>()
        val fftResolution = sampleRate / bufferSize
        for (i in 0..(logFrequencyIncrements.size - 2)) {
            val lowBoundAsDouble = logFrequencyIncrements[i] / fftResolution
            val highBoundAsDouble = logFrequencyIncrements[i + 1] / fftResolution
            ledMagnitudes.add(getMagnitude(fftValues, lowBoundAsDouble, highBoundAsDouble))
        }
        return ledMagnitudes
    }

    private fun getLogIncrements(lowBound: Double, highBound: Double, numIncrements: Int): List<Double> {
        val increment = Math.round((highBound - lowBound) / numIncrements).toInt()
        val converter = Lin2LogConverter(lowBound, lowBound, highBound, highBound)
        return (lowBound.toInt()..highBound.toInt() step increment).map { converter.lin2Log(it.toDouble()) }
    }

    /**
     * Given the entire FFT value list, extract a single magnitude from a range, scaling the endpoints.
     */
    private fun getMagnitude(fftValues: List<Float>, lowBound: Double, highBound: Double): Float {
        val lowIndex = Math.floor(lowBound).toInt()
        val highIndex = Math.floor(lowBound).toInt()

        // handle special case where both fall in the same frequency bin
        val subList = fftValues.subList(lowIndex, highIndex).plus(fftValues[highIndex])
        if (subList.size == 1) {
            val percentToExtract = highBound - lowBound
            val ledMagnitude = fftValues[lowIndex] * percentToExtract
            return ledMagnitude.toFloat()
        }

        // Scale the boundaries, then sum the entire list.
        val ledMagnitude = sumWithScaledBoundaries(subList, 1 - (lowBound - lowIndex), highBound - highIndex)
        return ledMagnitude
    }

    fun sumWithScaledBoundaries(values: List<Float>, lowBoundPercent: Double, highBoundPercent: Double): Float {
        assert(values.size > 1) {
            "Size was actually: ${values.size}, with values: $values"
        }
        return values.mapIndexed { index, value ->
            // scale the boundaries
            var returnVal = value
            if (index == 0) {
                returnVal = (value * lowBoundPercent).toFloat()
            }
            if (index == values.lastIndex) {
                returnVal = (value * highBoundPercent).toFloat()
            }
            // if not boundary, return value as is
            returnVal
        }.sum()
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