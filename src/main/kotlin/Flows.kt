import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun main() = runBlocking {
    val millis = measureTimeMillis {
        val webFlow: Flow<String> = channelFlow {
            repeat(starts) { id ->
                launch {
                    val json = webRequest(id)
                    send(json)
                }
            }
        }
        val responseFlow: Flow<Response> = webFlow.map(::decodeResponse)
            .flowOn(Dispatchers.Default)
            .onEach { (message) -> println(message) }

        responseFlow.collect()
    }
    println("All $starts requests done in $millis ms")

}
