import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {

  //  (1..3).asFlow()
//    .flowOn(Dispatchers.IO)
//    .collect {
//      println("Collecting $it in thread ${currentCoroutineContext()}")
//    }
//
//  // collect is a suspending function, meaning it will suspend the current coroutine and only resume
//  // when all the values of Flow are collected.
//
//  println("Flow started") // -> this line will be suspended by collect

//  uiScope.launch {
//  (1..3).asFlow()
//    .flowOn(Dispatchers.IO)
//    .collect {
//      println("Collecting $it in thread ${currentCoroutineContext()}")
//    }
//  }

  // or

  (1..3).asFlow()
    .flowOn(Dispatchers.IO)
    .onEach {
      println("Collecting $it in thread ${currentCoroutineContext()}")
    }
    .launchIn(this)

  // launchIn is not a suspend function. It returns a Job, which allows you to launch the Flow in a
  // separate coroutine, typically in viewModelScope vs lifecycleScope.

  println("Flow started") // -> this line will be not suspended by launchIn
}