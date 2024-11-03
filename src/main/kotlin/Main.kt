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
        laser.setAngle(1)
        /*
        storage.swap(CargoPosition(0, 0), CargoPosition(0, 1))
        energyManagement.allLimitsToZero()
        energyManagement.enableThrusters()
        navigation.goTo(x, y)
        navigation.awaitCoords(x, y)
         */
    }
}

object UrlProvider {
    val baseUrl = "http://${System.getenv("VM_IP")}"
}
