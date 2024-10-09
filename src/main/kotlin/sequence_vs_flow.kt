import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

// sequences is a synchronous -> block main thread
fun createSequences(): Sequence<Int> = sequence {
  for (i in 1..3) {
    Thread.sleep(1000)
    yield(i)
  }
}

// flow is an asynchronous -> non block main thread
fun createFlows(): Flow<Int> = flow {
  for (i in 1..3) {
    delay(1000)
    emit(i)
  }
}

fun main(): Unit = runBlocking {
  launch {
    println("Current thread: ${Thread.currentThread().name}")
    for (i in 1..3) {
      println("I am not blocked $i")
    }
  }

//  createSequences().forEach {
//    println("Sequences: $it")
//  }

  // when flow was collected, block code of flow{} can run -> cold observable
  createFlows().collect { value: Int ->
    println("Flow: $value")
  }
}