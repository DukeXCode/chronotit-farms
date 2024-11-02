import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializable

class EnergyManagement {
    private val ports = listOf(2032, 2033)

    public suspend fun allLimitsToZero(): LimitsResponse {
        return sendLimitsRequestsUntilSuccess(body = LimitsBody())
    }

    public suspend fun enableThrusters() {
        val body = LimitsBody(
            thruster_back = 1.0,
            thruster_front = 1.0,
            thruster_front_left = 1.0,
            thruster_front_right = 1.0,
            thruster_bottom_left = 1.0,
            thruster_bottom_right = 1.0,
        )
        sendLimitsRequestsUntilSuccess(body = body)
    }

    private suspend fun sendLimitsRequestsUntilSuccess(port: Int = ports[0], body: LimitsBody): LimitsResponse {
        val response: LimitsResponse = sendLimitsRequest(ports[0], body)
        if (response.kind == "success") {
            return response
        } else {
            sendLimitsRequestsUntilSuccess(ports.stream().filter { it != port }.toList()[0], body)
        }
        throw IllegalStateException("Should never happen")
    }

    private suspend fun sendLimitsRequest(port: Int, body: LimitsBody): LimitsResponse {
        return ClientProvider.client.put("${UrlProvider.baseUrl}:${port}/limits") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()
    }
}

@Serializable
data class LimitsBody(
    val thruster_back: Double = 0.0,
    val thruster_front: Double = 0.0,
    val thruster_front_left: Double = 0.0,
    val thruster_front_right: Double = 0.0,
    val thruster_bottom_left: Double = 0.0,
    val thruster_bottom_right: Double = 0.0,
    val laser: Double = 0.0,
    val cargo_bot: Double = 0.0,
    val scanner: Double = 0.0,
    val jumpdrive: Double = 0.0,
    val sensor_void_energy: Double = 0.0,
    val shield_generator: Double = 0.0,
    val sensor_atmoic_field: Double = 0.0,
    val matter_stablizer: Double = 0.0,
    val analyser_alpha: Double = 0.0,
)

@Serializable
data class LimitsResponse(val kind: String)
