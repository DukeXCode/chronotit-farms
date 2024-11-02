import kotlinx.coroutines.runBlocking

fun main() {
    if (System.getenv("VM_IP").isNullOrEmpty()) {
        throw IllegalStateException("Env 'VM_IP' is not set")
    }

    val energyManagement = EnergyManagement()
    val navigation = Navigation()

    runBlocking {
        energyManagement.allLimitsToZero()
        energyManagement.enableThrusters()
        navigation.goTo(-17937.0, 12727.0)
    }
}

object UrlProvider {
    val baseUrl = "http://${System.getenv("VM_IP")}"
}
