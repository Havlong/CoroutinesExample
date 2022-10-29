import kotlinx.coroutines.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.system.measureTimeMillis

const val waitTime: Long = 1000
const val starts: Int = 100000
val jsonDecoder = Json

fun main() = runBlocking {
    val millis = measureTimeMillis {
        val jobs: List<Deferred<String>> = List(starts) { id ->
            async {
                val result = webRequest(id)
                val s = decodeResponse(result).message
                println(s)
                s
            }
        }
        val allStrings: List<String> = jobs.awaitAll()
        println("${allStrings.size} messages collected")
    }
    println("All $starts requests done in $millis ms")
}

suspend fun webRequest(id: Int): String = withContext(Dispatchers.IO) {
    delay(waitTime)
    return@withContext "{ \"message\": \"Totally not made-up message from internet request #$id\", \"status\": 200 }"
}

suspend fun decodeResponse(response: String): Response = withContext(Dispatchers.Default) {
    return@withContext jsonDecoder.decodeFromString(response)
}

@Serializable
data class Response(val message: String, val status: Int)
