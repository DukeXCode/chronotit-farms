package ch.dukex.chronotit_farm

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

private const val CHRON_PARK_X = -18737.0
private const val CHRON_PARK_Y = 12527.0
private const val CHRON_X = -18937.0
private const val CHRON_Y = 12727.0
private const val ARAK_X = 2490.0
private const val ARAK_Y = 4368.0

fun main() {
    if (System.getenv("VM_IP").isNullOrEmpty()) {
        throw IllegalStateException("Env 'VM_IP' is not set")
    }

    runBlocking { runFarm() }
}

suspend fun runFarm() = coroutineScope {
    val energyManagement = EnergyManagement()
    val navigation = Navigation()
    val laser = Laser()
    val storage = Storage()

    launch { energyManagement.enableThrusters() }.join()

    launch { navigation.goTo(CHRON_PARK_X, CHRON_PARK_Y) }.join()
    launch { navigation.awaitCoords(CHRON_PARK_X, CHRON_PARK_Y) }.join()

    launch {
        energyManagement.set(
            LimitsBody(
                laser = 1.0,
                cargo_bot = 1.0,
                sensor_void_energy = 1.0,
                matter_stabilizer = 1.0
            )
        )
    }.join()

    launch { delay(15_000); laser.aimLaserAt(CHRON_X, CHRON_Y) }.join()
    val laserJob = launch { laser.activateForever() }
    val storageJob = launch { storage.moveDownByPriorityForever() }
    launch { storage.awaitFullStorage() }.join()
    laserJob.cancel()
    storageJob.cancel()

    launch { energyManagement.enableThrusters() }.join()

    launch { navigation.goTo(ARAK_X, ARAK_Y) }.join()
}

object UrlProvider {
    val baseUrl = "http://${System.getenv("VM_IP")}"
}
