package sharedFlow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
  val upstreamColdFlow: Flow<Int> = getUpstreamColdFlow()
  val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)

  //  shareIn will convert a _cold_ [Flow] into a _hot_ [SharedFlow] that is started in the given coroutine [scope],
  //  sharing emissions from a single running instance of the upstream flow with multiple downstream subscribers,
  //  replaying a specified number of [replay] values to new subscribers.

  // 0 -> 1: collect upstreamColdFlow
  // 1 -> 2, 2 -> 3, ....
  // 1 -> 0: cancel upstreamColdFlow
  val sharedFlow: SharedFlow<Int> = upstreamColdFlow.shareIn(
    scope = scope, // the coroutine scope in which sharing is started.
    started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000), // the strategy that controls when sharing is started and stopped.
    // WhileSubscribed -> Sharing is started when the first subscriber appears, immediately stops when the last
    // subscriber disappears (by default), keeping the replay cache forever (by default).
    replay = 2, // the number of values replayed to new subscribers (cannot be negative, defaults to zero).
  )

  println("before delay 2s")
  delay(2000)
  println("after delay 2s")

  println("started collecting SharedFlow")


  // RefCount: 0 -> 1 (collecting upstreamColdFlow)
  val job1: Job = sharedFlow
    .onEach { println("[1] collect: $it") }
    .launchIn(scope)

  delay(100)

  // RefCount: 1 -> 2
  val job2: Job = sharedFlow
    .onEach { println("[2] collect: $it") }
    .launchIn(scope)

  delay(2000)
  // RefCount: 2 -> 1
  job1.cancelAndJoin()

  delay(1000)
  // RefCount: 1 -> 0 (cancel upstreamColdFlow)
  job2.cancelAndJoin()

  println("-----------------CANCEL----------------")

  delay(6000)

  println("-----------------COLLECT---------------")

  // RefCount: 0 -> 1 (collecting upstreamColdFlow)
  val job3: Job = sharedFlow
    .onEach { println("[3] collect: $it") }
    .launchIn(scope)

  delay(10000)
  scope.cancel()
}