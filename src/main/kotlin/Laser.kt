import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable

class Laser {
    suspend fun setAngle(angle: Int) {
        ClientProvider.client.put("${UrlProvider.baseUrl}:2018/angle") {
            contentType(ContentType.Application.Json)
            setBody(AngleBody(angle))
        }
    }

    suspend fun activateForever() {
        while (true) {
            activate()
            delay(10_000)
        }
    }

    suspend fun activate() {
        ClientProvider.client.post("${UrlProvider.baseUrl}:2018/activate")
    }
}

@Serializable
data class AngleBody(val angle: Int)
