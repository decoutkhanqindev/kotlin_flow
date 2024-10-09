import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

interface WebSocketCallback {
  fun receiveMessage(message: String)
}

class ServiceTesting {
  private var webSocket: WebSocketCallback? = null

  fun registerWebSocketCallback(callback: WebSocketCallback) {
    this.webSocket = callback
  }

  fun unregisterWebSocketCallback() {
    this.webSocket = null
  }

  fun sendMessage(value: String) {
    webSocket?.receiveMessage(value)
  }
}

private fun createWebSocketCallback(): Flow<String> = callbackFlow {
  val service = ServiceTesting()

  val webSocketCallback: WebSocketCallback = object : WebSocketCallback {
    override fun receiveMessage(message: String) {
      trySend(message)
    }
  }

  println("service is register")
  service.registerWebSocketCallback(webSocketCallback)

  service.sendMessage("A1")
  service.sendMessage("A2")
  service.sendMessage("A3")
  service.sendMessage("A4")

  awaitClose { // if flow is cancel -> auto unregister
    println("service is unregister")
    service.unregisterWebSocketCallback()
  }
}

fun main(): Unit = runBlocking {
  launch {
    createWebSocketCallback()
      .cancellable()
      .collect { value: String ->
        println("value: $value")
        if (value == "A3") {
          cancel()
        }
      }
  }

  // callbackFlow() allows you to create a Flow from any callback-based API, such as events or persistent changes
  // from a data source.

  // With callbackFlow, you can send values into the Flow using offer() or trySend().

  // It provides a safe and cancelable channel to receive values sent from callbacks, and then turns those values
  // into a Flow stream.

  // When using callbackFlow, you typically register the callback with a data source, then emit the values
  // received from the callback into the Flow. When the callback is no longer needed, you need to unregister it.
}