import kotlinx.coroutines.runBlocking

fun main() {
    if (System.getenv("VM_IP").isNullOrEmpty()) {
        throw IllegalStateException("Env 'VM_IP' is not set")
    }

    // runBlocking {
    //     Navigation().goTo(0.0, 0.0)
    //     println(Navigation().get_pos())
    // }
}

object UrlProvider {
    val baseUrl = "http://${System.getenv("VM_IP")}"
}
