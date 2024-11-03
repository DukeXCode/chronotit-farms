import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.Serializable

class Laser {
    suspend fun setAngle(angle: Int) {
        ClientProvider.client.put("${UrlProvider.baseUrl}:2018/angle") {
            setBody(AngleBody(angle))
        }
    }

    suspend fun activate() {
        ClientProvider.client.post("${UrlProvider.baseUrl}:2018/activate")
    }
}

@Serializable
data class AngleBody(val angle: Int)
