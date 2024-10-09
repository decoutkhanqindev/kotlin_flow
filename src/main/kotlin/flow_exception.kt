import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking

fun createFlow3(): Flow<Int> = flow {
  for (i in 5 downTo -5) {
    emit(i)
  }
}

fun main(): Unit = runBlocking {
  createFlow3()
    .onEach { println("Processing: ${10 / it}") } // processing logic
    .catch { println("Caught exception $it") } // catch exception if processing is error
    .collect { println("Collecting $it") }
}