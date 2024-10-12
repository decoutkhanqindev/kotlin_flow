package sharedFlow

import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class EventBus<Event> {
  // SharedFlow is a hot flow that shares emitted values among all its collectors in a broadcast fashion,
  // so that all collectors get all emitted values

  // private mutable
  // MutableLiveData
  // Subject
  // MutableList
  private val _events: MutableSharedFlow<Event> = MutableSharedFlow<Event>()

  // public readonly/immutable
  // LiveData
  // Observable
  // List
  val events: SharedFlow<Event> = _events.asSharedFlow() // Observable.hide(), mutable.toList()

  suspend fun send(event: Event) {
    _events.emit(event)
  }
}

fun main(): Unit = runBlocking {
  // Subject:           Observable (subscribe) & Observer (onNext)
  // MutableSharedFlow: SharedFlow (collect)   & FlowCollector (emit)

  // [0 1 2], capacity = 3
  // DROP_OLDEST: emits 5 : [1 2 5] ==> non suspend
  // DROP_LATEST: emits 5 : [0 1 2] ==> non suspend

  val bus = EventBus<Int>()

  launch {
    bus.events.collect { event: Int -> println("A: $event") }
  }

  launch {
    bus.events.collect { event: Int -> println("B: $event") }
  }

  delay(100)
  repeat(10) { it: Int ->
    bus.send(it)
    delay(100)
  }

  delay(2_000)
  this.cancel()
}