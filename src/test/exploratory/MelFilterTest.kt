package exploratory

import edu.cmu.sphinx.frontend.frequencywarp.MelFilter
import edu.cmu.sphinx.frontend.frequencywarp.MelFrequencyFilterBank2
import org.hamcrest.CoreMatchers.*
import org.junit.Assert.*
import org.junit.Test

/**
 * Created by christian.henry on 1/6/17.
 */
class MelFilterTest {

    @Test
    fun deleteMe() {
        assertTrue(true)
        assertEquals(1, 1)
        assertThat(1, equalTo(1))
    }

    @Test
    fun test1() {
        val melFrequencyFilterBank2 = MelFrequencyFilterBank2(20.0, 20000.0, 20)

        val melFilter = MelFilter(20.0, 3000.0, 20000.0, 20.0, 200.0)
    }

}