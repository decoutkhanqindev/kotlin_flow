package sharedFlow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
  val mutableSharedFlow: MutableSharedFlow<Int> = MutableSharedFlow(
    replay = 2, // the number of values replayed to new subscribers (cannot be negative, defaults to zero).
    extraBufferCapacity = 0, // the number of values buffered in addition to `replay`.
    onBufferOverflow = BufferOverflow.SUSPEND //  configures an emit action on buffer overflow.
  )

//  Buffer overflow can happen only when there is at least one subscriber that is not ready to accept
//  the new value. In the absence of subscribers only the most recent replay values are stored and
//  the buffer overflow behavior is never triggered and has no effect.

  val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)

  scope.launch {
    repeat(20) {
      mutableSharedFlow.emit(it)
    }
    // emit first values -> all values will be in replay buffer, in case replay buffer = 2

    // observe later, that flow will collect 2 values 18, 19
    mutableSharedFlow.collect {
      println(">>> [1]: $it")
    }
  }

  delay(5)
  scope.cancel()
  println("DONE")
}