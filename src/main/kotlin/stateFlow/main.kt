package stateFlow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ViewModel {
  private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
  private val _stateFLow: MutableStateFlow<Int> = MutableStateFlow(0)
  val stateFlow: StateFlow<Int> = _stateFLow.asStateFlow()

  fun increment1() {
    scope.launch {
      _stateFLow.emit(_stateFLow.value + 1)
    }
  }

  fun increment2() {
    _stateFLow.tryEmit(_stateFLow.value + 1)
  }

  fun increment3() {
    // Read-Modify-Write
    // read: val read = _stateFlow.value (read=0)
    //
    // modify: val updated =  read + 1 (updated=1)
    //
    // thread2: _stateFlow.value = 100
    //
    // thread 1: write: _stateFlow.value = updated (1)
    _stateFLow.value = _stateFLow.value + 1
  }

  fun increment4() {
    // for multi threading
    _stateFLow.update { it + 1 }
  }

  fun doSomething() {
    scope.launch {
      // call api or update
      val data = 100
      delay(1000) // delay by network
      _stateFLow.value = data
    }
  }
}

fun main(): Unit = runBlocking {
  // StateFlow is a SharedFlow that represents a read-only state with a single updatable data [value] that emits updates
  // to the value to its collectors. A state flow is a _hot_ flow because its active instance exists independently
  // of the presence of collectors. Its current value can be retrieved via the [value] property.

  // A MutableStateFlow is created using `MutableStateFlow(value)` constructor function with
  // the initial value. The value of mutable state flow can be updated by setting its [value] property.
  // Updates to the [value] are always [conflated][Flow.conflate]. So a slow collector skips fast updates,
  // but always collects the most recently emitted value.

  // val stateFlow: StateFlow<Int> = TODO()
  // always have current value
  // conflated -> DROP_OLDEST
  // replay 1
  // distinctUntilChanged

  val vm = ViewModel()

  vm.stateFlow
    .onEach { println("[1] collect: $it stateFlow.value=${vm.stateFlow.value}") }
    .launchIn(this)

  delay(1000)

  vm.increment1()
  vm.increment2()
  vm.increment3()
  vm.increment4()

  delay(3000)

  vm.stateFlow
    .onEach { println("[2] collect: $it stateFlow.value=${vm.stateFlow.value}") }
    .launchIn(this)

  delay(3000)
  cancel()
}