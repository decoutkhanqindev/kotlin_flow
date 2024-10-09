import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Random

fun main(): Unit = runBlocking {
//  val flow: Flow<Int> = flow {
//    println("Flow ID: ${Random().nextInt(100)}")
//    launch {
//      launch {
//        repeat(3) { value: Int ->
//          delay(100)
//          emit(value)
//        }
//      }
//    }
//  }

  // emit() doesn't know in which coroutine it will emit the value -> none thread safe
  // to fix -> use channelFlow() to communicate between multiple coroutines -> use send() or trySend() to emit value -> thread safe

  val flow: Flow<Int> = channelFlow {
    println("Flow ID: ${Random().nextInt(100)}")
    launch {
      launch {
        repeat(3) { value: Int ->
          delay(100)
          send(value) // suspend until the collector receives complete data
          // trySend(value) // Automatically send data to the collector
        }
      }
    }
  }

  flow.collect { value: Int -> println(value) }
}