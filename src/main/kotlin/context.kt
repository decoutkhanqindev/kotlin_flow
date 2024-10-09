import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

fun wrongSwitchContextInFlow(): Flow<Int> = flow {
  withContext(Dispatchers.Default) {
    println("Current thread in flow is ${Thread.currentThread().name}")
    for (i in 1..3) {
      delay(100)
      emit(i)
    }
  }
}

fun correctSwitchContextInFlow(): Flow<Int> = flow {
  println("Current thread in flow is ${Thread.currentThread().name}")
  for (i in 1..3) {
    delay(1000)
    emit(i)
  }
}.flowOn(Dispatchers.Default)

fun main(): Unit = runBlocking {
  println("Current thread is ${Thread.currentThread().name}")

//  val flow1: Flow<Int> = wrongSwitchContextInFlow()
//  flow1.collect { value: Int -> println(value) }

//  // it will throw an exception, because the block code in flow{} must preserve context
//  // to fix -> use flowOn()

  val flow2: Flow<Int> = correctSwitchContextInFlow()
  flow2.collect { value: Int -> println(value) }

  launch(Dispatchers.Default) {
    (1..10).asFlow()
      .filter { it % 2 == 0 }
      .onEach {
        println("Processing $it in thread ${currentCoroutineContext()}")
      }
      .flowOn(Dispatchers.IO) // all processes on flowOn() are in Dispatchers.IO
      .collect {
        println("Collecting $it in thread ${currentCoroutineContext()}")
      }
  }
}