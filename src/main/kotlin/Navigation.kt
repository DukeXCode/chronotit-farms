import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializable

class Navigation {
    suspend fun get_pos(): GetPosResponse {
        return ClientProvider.client.get("${UrlProvider.baseUrl}:2010/pos").body<GetPosResponse>()
    }

    suspend fun goTo(x: Double, y: Double) {
        try {
            ClientProvider.client.post("${UrlProvider.baseUrl}:2009/set_target") {
                contentType(ContentType.Application.Json)
                setBody(GoToBody(Position(x, y)))
            }
        } catch (e: Exception) {
            println("Request failed: ${e.message}")
            throw e
        }
    }
}

@Serializable
data class Position(val x: Double, val y: Double)


@Serializable
data class GetPosResponse(val kind: String, val pos: Position)

@Serializable
data class GoToBody(val target: Position)
