import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import java.util.Random

// when flow was collected -> use collect(), block code of flow{} can run -> cold flow
fun createFlows1(): Flow<Int> = flow {
  println("Flow ID: ${Random().nextInt(100)}")
  for (i in 1..3) {
    delay(1000)
    emit(i) // like return value i
  }
}

fun main(): Unit = runBlocking {
  val flow1: Flow<Int> = createFlows1()
  flow1.collect { println("value 1: $it") }

  val flow2: Flow<Int> = createFlows1()
  flow2.collect { println("value 2: $it") }

  // both flow1 and flow2 will have separate logic when collecting
}