package channel

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
  val channel: Channel<Int> = Channel<Int>(
    capacity = Channel.RENDEZVOUS, // either a positive channel capacity or one of the constants defined in [Channel.Factory].
    // Channel.RENDEZVOUS // 0 === no buffer
    // Channel.BUFFERED // default is 64 values
    // Channel.CONFLATED // capacity=1 and onBufferOverflow is DROP_OLDEST (1 latest value)
    // Channel.UNLIMITED // Int.MAX_VALUE
    onBufferOverflow = BufferOverflow.SUSPEND // configures an action on buffer overflow
  )

  launch {
    repeat(10) {
      println(">>>> sending $it to $channel")
      channel.send(it)
      delay(500)
      println("<<<< sent $it to $channel")
      println("-".repeat(20))
    }
    channel.close()
  }

  launch {
    for (value: Int in channel) {
      println("### received $value from $channel")
      delay(333)
    }
    println("DONE receiving")
  }

  Unit
}