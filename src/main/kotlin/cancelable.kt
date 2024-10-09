import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.cancellation.CancellationException

fun main(): Unit = runBlocking() {
  (1..3).asFlow()
    .onCompletion { it: Throwable? ->
      if (it is CancellationException) {
        println("Cancelled")
      }
    }
    .cancellable()
    .collect { it: Int ->
      println("Collected $it")
      if (it % 2 == 0) cancel()
    }

  // cancellable() is an operator in Flow that is used to ensure that the Flow can be cancelled during execution of
  // some code that does not check the cancellation status automatically.
}