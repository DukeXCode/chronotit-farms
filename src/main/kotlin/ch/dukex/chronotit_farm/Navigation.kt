package ch.dukex.chronotit_farm

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable

class Navigation {
    suspend fun getPos(): GetPosResponse {
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

    suspend fun awaitCoords(x: Double, y: Double, tolerance: Double = 200.0) {
        var atCoords = false
        while (!atCoords) {
            val response = getPos()
            if (response.pos.x - x <= tolerance && response.pos.y - y <= tolerance) {
                atCoords = true
            }
            delay(3000)
        }
    }
}

@Serializable
data class Position(val x: Double, val y: Double)

@Serializable
data class GetPosResponse(val kind: String, val pos: PositionResponse)

@Serializable
data class GoToBody(val target: Position)

@Serializable
data class PositionResponse(val x: Double, val y: Double, val angle: Double)
