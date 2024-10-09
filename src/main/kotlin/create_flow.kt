import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
  // flowOf() is like flow() but it contains many values ~ iterable
  val flow1: Flow<Int> = flowOf(1, 2, 3, 4, 5)
  flow1.collect { value: Int -> print("$value ") }

  println()

  // asFlow() can cast from iterable to flow
  val flow2: Flow<String> = listOf("A", "B", "C").asFlow()
  flow2.collect { value: String -> print("$value ") }

  println()

  // emitAll() is used to emit another flow in flow
  val flow3: Flow<Int> = flow {
    emit(200)
    emitAll(flow1)
  }
  flow3.collect { value: Int -> print("$value ") }
}