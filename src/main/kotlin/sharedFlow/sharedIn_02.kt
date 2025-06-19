package sharedFlow

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

fun main(): Unit = runBlocking {
  val upstreamColdFlow: Flow<Int> = getUpstreamColdFlow()
  val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)

  //  shareIn will convert a _cold_ [Flow] into a _hot_ [SharedFlow] that is started in the given coroutine [scope],
  //  sharing emissions from a single running instance of the upstream flow with multiple downstream subscribers,
  //  replaying a specified number of [replay] values to new subscribers.

  val sharedFlow: SharedFlow<Int> = upstreamColdFlow.shareIn(
    scope = scope, // the coroutine scope in which sharing is started.
    started = SharingStarted.Lazily, // the strategy that controls when sharing is started and stopped.
    // Lazily -> Sharing is started when the first subscriber appears and never stops.
    // in case upstreamColdFlow will share when first collector [1] or [2] collect
    replay = 2, // the number of values replayed to new subscribers (cannot be negative, defaults to zero).
  )

  println("before delay 2s")
  delay(2000)
  println("after delay 2s")

  println("started collecting SharedFlow")

  sharedFlow
    .onEach { println("[1] collect: $it") }
    .launchIn(scope)

  sharedFlow
    .onEach { println("[2] collect: $it") }
    .launchIn(scope)

  delay(10000)
  scope.cancel()
}