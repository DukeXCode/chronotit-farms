import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable

private const val CARGO_BOT_DELAY: Long = 1000

class Storage {
    suspend fun moveDownByPriorityForever() {
        while (true) {
            val hold = getHold()

            for (row in 0..10) {
                for (col in 0..11) {
                    if (hold[row][col] != null && hold[row + 1][col] == null) {
                        try {
                            swap(CargoPosition(col, row), CargoPosition(col, row + 1))
                        } catch (e: ServerResponseException) {
                            println("Swap request failed")
                        }
                        delay(CARGO_BOT_DELAY)
                        moveDownByPriorityForever()
                    }
                }
            }
        }
    }

    private suspend fun getFreeSpace(): Int {
        return ClientProvider.client.get("${UrlProvider.baseUrl}:2012/hold").body<HoldResponse>().hold.hold_free
    }

    suspend fun awaitFullStorage() {
        do {
            delay(5_000)
            val freeStorage = getFreeSpace()
        } while (freeStorage > 0)
    }

    private suspend fun swap(a: CargoPosition, b: CargoPosition) {
        ClientProvider.client.post("${UrlProvider.baseUrl}:2012/swap_adjacent") {
            contentType(ContentType.Application.Json)
            setBody(SwapBody(a, b))
        }
    }

    private suspend fun getHold(): List<List<String?>> {
        return ClientProvider.client.get("${UrlProvider.baseUrl}:2012/structure").body<StructureResponse>().hold
    }
}

@Serializable
data class SwapBody(val a: CargoPosition, val b: CargoPosition)

@Serializable
data class CargoPosition(val x: Int, val y: Int)

@Serializable
data class StructureResponse(val hold: List<List<String?>>)

@Serializable
data class HoldResponse(val hold: Hold)

@Serializable
data class Hold(val hold_free: Int)
