import io.ktor.client.request.*

suspend fun get_pos() {
    val response = client.get("http://${BASE_URL}:2010/pos")


}
