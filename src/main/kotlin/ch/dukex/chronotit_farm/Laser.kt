package ch.dukex.chronotit_farm

import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable
import java.lang.Math.toDegrees
import java.lang.Math.toRadians
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

const val LASER_SHIPCENTER_SPACING = 65

class Laser {
    suspend fun aimLaserAt(x: Double, y: Double) {
        val shipPos = Navigation().getPos().pos
        val shipAngle = shipPos.angle
        val shipPitchAngleRad = toRadians((450 - shipAngle) % 360)
        val pitchAngleRad = atan2(
            y - shipPos.y - LASER_SHIPCENTER_SPACING * sin(shipPitchAngleRad),
            x - shipPos.x - LASER_SHIPCENTER_SPACING * cos(shipPitchAngleRad)
        )
        setAngle(90 - shipAngle - toDegrees(if (pitchAngleRad >= 0) pitchAngleRad else 2 * PI + pitchAngleRad))
    }

    private suspend fun setAngle(angle: Double) {
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
        try {
            ClientProvider.client.post("${UrlProvider.baseUrl}:2018/activate")
        } catch (e: ServerResponseException) {
            println("Activate laser request failed")
        }
    }
}

@Serializable
data class AngleBody(val angle: Double)
