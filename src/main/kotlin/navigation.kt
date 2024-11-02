import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializable

@Serializable
data class Position(val x: Int, val y: Int)

suspend fun get_pos(): GetPosResponse {
    return client.get("${BASE_URL}:2010/pos").body<GetPosResponse>()
}

@Serializable
data class GetPosResponse(val kind: String, val pos: Position)

suspend fun goTo(x: Int, y: Int): Boolean {
    val pos = Position(x, y)

    return try {
        val response = client.post("https://api.example.com/endpoint") {
            contentType(ContentType.Application.Json)
            setBody(pos)
        }
        response.status == HttpStatusCode.Created
    } catch (e: Exception) {
        println("Request failed: ${e.message}")
        false
    }
}
