package channel

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
  val channel: Channel<Int> = Channel<Int>() // no buffer

  // SendChannel
  // isClosedForSend
  println("isCloseForSend=${channel.isClosedForSend}") // false
  channel.close();
  println("isCloseForSend=${channel.isClosedForSend}") // true

  // send() and trySend()
  channel.send(1) // suspend until received value
  channel.trySend(2) // non-suspend

  // ReceiveChannel
  channel.receive() // suspend until sending value
  channel.tryReceive() // non-suspend and try catch
  channel.receiveCatching() // suspend and and try { receive() } catch {...}
}