# 📚 Learning Kotlin Flow 

This repository is for learning and practicing the key concepts of Kotlin Flow. Each file and folder in the project is a practical example, demonstrating how to use different types of Flow, Channel, StateFlow, SharedFlow, as well as common operators and patterns in asynchronous programming with Kotlin.

## 🏆 Main Topics

- **Basic Flow:** Creating, collecting, and processing asynchronous data.
- **Channel:** Communication between coroutines using Channel.
- **StateFlow & SharedFlow:** State management and broadcasting data.
- **Operators:** Using operators to transform, combine, and process data streams.
- **Comparison with Sequence, Callback, Exception handling, Context, ...**

## 📝 Descriptions

- **Basic Flow**
  - `create_flow.kt, cold_flow.kt`: Creating and collecting data from Flow, distinguishing cold flow.
  - `collect_vs_launchIn.kt`: Comparison between `collect` (suspend) and `launchIn` (non-suspend, returns a Job).

- **Channel**
    - `channel/main.kt`: Communication between coroutines using Channel.
    - `buffered_channel.kt, 0_buffered_channel.kt`: Buffered Channel examples.
    - `send_channel_receive_channel.kt`: Sending and receiving data via Channel.

- **StateFlow**
    - `stateFlow/main.kt`: State management with StateFlow, updating and collecting the latest value.
    - `stateIn.kt`: Using the `stateIn` operator to convert Flow to StateFlow.

- **SharedFlow**
    - `sharedFlow/main.kt`: Broadcasting data to multiple collectors.
    - `mutable_01.kt ~ mutable_04.kt`: Working with MutableSharedFlow.
    - `sharedIn_01.kt ~ sharedIn_03.kt`: Using the `sharedIn` operator.

- **Others**
    - `callback_flow.kt`: Converting callbacks to flow.
    - `flow_exception.kt`: Exception handling in flow.
    - `context.kt`: Changing the execution context of flow.
    - `sequence_vs_flow.kt`: Comparison between Sequence (sequential generator) and Flow (asynchronous).

## 🧑‍🏫 References

- [Kotlin Flow (official)](https://kotlinlang.org/docs/flow.html)
- [Kotlin Coroutines Guide](https://kotlinlang.org/docs/coroutines-guide.html)
- [Kotlinx Coroutines GitHub](https://github.com/Kotlin/kotlinx.coroutines)

## 📝 License
This project is developed for educational and personal purposes.
