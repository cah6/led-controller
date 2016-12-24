package audio

import org.hamcrest.CoreMatchers.*
import org.junit.Assert.*
import org.junit.Test

/**
 * Created by christian.henry on 12/23/16.
 */
class Lin2LogConverterTest {

    @Test
    fun test1() {
        val numLeds = 60
        val lowBound = 20.0
        val highBound = 20000.0
        val increment = Math.round((highBound - lowBound) / numLeds).toInt()

        val target = Lin2LogConverter(lowBound, lowBound, highBound, highBound)

        val increments = lowBound.toInt()..highBound.toInt() step increment
        for (i in increments) {
            println("Linear value: $i; log value: ${target.lin2Log(i.toDouble())}")
        }
        assertEquals(increments.count(), numLeds + 1)
    }

}