import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializable

class Storage {
    suspend fun swap(a: CargoPosition, b: CargoPosition) {
        ClientProvider.client.post("${UrlProvider.baseUrl}:2012/swap_adjacent") {
            contentType(ContentType.Application.Json)
            setBody(SwapBody(a, b))
        }
    }
}

@Serializable
data class SwapBody(val a: CargoPosition, val b: CargoPosition)

@Serializable
data class CargoPosition(val x: Int, val y: Int)
