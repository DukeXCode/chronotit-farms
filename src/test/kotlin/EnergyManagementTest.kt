import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class EnergyManagementTest {
    @Test
    fun testAllLimitsToZero() {
        runBlocking {
            val response = allLimitsToZero()

            assertEquals("success", response.kind)
        }
    }
}