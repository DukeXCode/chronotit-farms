import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class EnergyManagementTest {
    @Test
    fun testAllLimitsToZero() {
        runBlocking {
            val response = EnergyManagement().allLimitsToZero()

            assertEquals("success", response.kind)
        }
    }
}