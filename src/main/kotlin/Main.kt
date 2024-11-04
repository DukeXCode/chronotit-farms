import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

private const val CHRON_X = -18737.0
private const val CHRON_Y = 12527.0
private const val ARAK_X = 0.0
private const val ARAK_Y = 0.0

fun main() {
    if (System.getenv("VM_IP").isNullOrEmpty()) {
        throw IllegalStateException("Env 'VM_IP' is not set")
    }

    runBlocking { runFarm() }
}

suspend fun runFarm() = coroutineScope {
    val energyManagement = EnergyManagement() // TODO: Add energy management
    val navigation = Navigation()
    val laser = Laser()
    val storage = Storage()

    launch { navigation.goTo(CHRON_X, CHRON_Y) }.join()
    launch { navigation.awaitCoords(CHRON_X, CHRON_Y) }.join()

    val laserJob = launch { laser.activateForever() }
    val storageJob = launch { storage.moveDownByPriorityForever() }
    launch { storage.awaitFullStorage() }.join()
    laserJob.cancel()
    storageJob.cancel()

    launch { navigation.goTo(ARAK_X, ARAK_Y) }.join()
}

object UrlProvider {
    val baseUrl = "http://${System.getenv("VM_IP")}"
}
