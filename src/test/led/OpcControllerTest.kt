package led

import org.hamcrest.CoreMatchers.*
import org.junit.Assert.*
import org.junit.Test

/**
 * Created by christian.henry on 12/24/16.
 */
class OpcControllerTest {

    @Test
    fun deleteMe() {
        assertTrue(true)
        assertEquals(1, 1)
        assertThat(1, equalTo(1))
    }

    @Test
    fun testLedStripSetup() {
        OpcController("localhost", 1234, mapOf<Int, Int>())
    }

}