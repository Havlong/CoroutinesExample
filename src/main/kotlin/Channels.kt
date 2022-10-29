import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun main() = runBlocking {
    val responseChannel = Channel<String>(Channel.BUFFERED)

    val millis = measureTimeMillis {
        repeat(starts) { id ->
            launch {
                val jsonString = webRequest(id)
                responseChannel.send(jsonString)
            }
        }
        val decodeJobs: List<Job> = List(starts) {
            launch {
                val acquiredResponse = responseChannel.receive()
                val decodedResponse = decodeResponse(acquiredResponse)
                println(decodedResponse.message)
            }
        }
        decodeJobs.joinAll()
    }
    println("All $starts requests done in $millis ms")
}
