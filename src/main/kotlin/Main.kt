import kotlinx.coroutines.runBlocking

fun main() {
    if (System.getenv("VM_IP").isNullOrEmpty()) {
        throw IllegalStateException("Env 'VM_IP' is not set")
    }

    val energyManagement = EnergyManagement()
    val navigation = Navigation()
    val laser = Laser()
    val storage = Storage()

    val x = -18737.0
    val y = 12527.0

    runBlocking {
        storage.moveDownByPriority()
    }
}

object UrlProvider {
    val baseUrl = "http://${System.getenv("VM_IP")}"
}
