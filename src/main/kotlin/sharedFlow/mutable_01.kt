package sharedFlow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
  val mutableSharedFlow: MutableSharedFlow<Int> = MutableSharedFlow(
    replay = 0, // the number of values replayed to new subscribers (cannot be negative, defaults to zero).
    extraBufferCapacity = 8, // the number of values buffered in addition to `replay`.
    onBufferOverflow = BufferOverflow.SUSPEND //  configures an emit action on buffer overflow.
  )

//  Buffer overflow can happen only when there is at least one subscriber that is not ready to accept
//  the new value. In the absence of subscribers only the most recent replay values are stored and
//  the buffer overflow behavior is never triggered and has no effect.

  val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)

  // fast collector
  mutableSharedFlow
    .onEach { println(">>> [1]: $it") }
    .launchIn(scope)

  // slow collector
  mutableSharedFlow
    .onEach {
      println(">>> [2]: $it")
      delay(5_000)
    }
    .launchIn(scope)

  scope.launch {
    repeat(20) {
      println("### Emitting $it")
      mutableSharedFlow.emit(it)
      println("<<< Emitted $it")
    }
  }

  delay(30_000)
  scope.cancel()
  println("DONE")
}