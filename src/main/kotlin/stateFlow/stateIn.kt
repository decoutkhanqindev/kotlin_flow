package stateFlow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking

// cold flow
fun getUpstreamColdFlow(): Flow<Int> = flow {
  println("getUpstreamColdFlow started emitting")
  repeat(5) {
    println("Emitting $it")
    emit(it)
    delay(1000)
  }
}.onCompletion {
  println("getUpstreamColdFlow completed emitting")
}

fun main(): Unit = runBlocking {
  val upstreamColdFlow: Flow<Int> = getUpstreamColdFlow()
  val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)

  // Converts a _cold_ [Flow] into a _hot_ [StateFlow] that is started in the given coroutine [scope],
  // sharing the most recently emitted value from a single running instance of the upstream flow with multiple
  // downstream subscribers.

  val stateFlow: StateFlow<Int> = upstreamColdFlow.stateIn(
    scope = scope, started = SharingStarted.Lazily, initialValue = -1 // initial value for MutableStateFlow
  )

  fun printCurrentValue() = println("stateFlow.value=${stateFlow.value}")

  printCurrentValue()

  println("started collecting StateFlow")

  stateFlow.onEach {
    println("[1] collect: $it")
    printCurrentValue()
  }.launchIn(scope)

  stateFlow.onEach {
    println("[2] collect: $it")
    printCurrentValue()
  }.launchIn(scope)

  printCurrentValue()

  delay(10000)
  scope.cancel()
}