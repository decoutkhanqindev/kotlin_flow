package channel

import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


fun main(): Unit = runBlocking {
  // Channel provide a convenient way to transfer a single value between coroutines.
  // Channels provide a way to transfer a stream of values.

  val channel: Channel<Int> = Channel<Int>()

  // sending
  launch {
    for (value: Int in 1..5) {
      println("sending ${value * value}")
      channel.send(value * value)
      println("sent ${value * value}")
      delay(1000)
    }
    channel.close() // close channel (done) -> will automatically notify the channel it has completed sending
  }

  // receiving

  // manual receive
//  launch {
//    repeat(5) {
//      val value: Int = channel.receive();
//      println("received $value")
//    }
//  }

  // automatic receive -> use close()
  launch {
    for (value: Int in channel) {
      println("received $value")
    }
    println("DONE receiving")
  }
}