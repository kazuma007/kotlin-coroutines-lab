package dev.coroutineslab.fundamentals

import dev.coroutineslab.common.log
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/*
 * Explanation:
 * suspend permits suspension; it does not start a coroutine or select a
 * background thread. launch introduces concurrency explicitly.
 */
private suspend fun recordImmediateStep(events: MutableList<String>, label: String) {
    events += "$label:start"
    events += "$label:end"
}

private suspend fun recordSuspendingStep(events: MutableList<String>, label: String) {
    events += "$label:start"
    delay(50)
    events += "$label:end"
}

suspend fun recordSequentialSuspendCalls(): List<String> {
    val events = mutableListOf<String>()
    recordImmediateStep(events, "no-suspension-point")
    recordSuspendingStep(events, "suspending-call")
    recordImmediateStep(events, "final-call")
    return events
}

private suspend fun demonstrateExplicitConcurrency() {
    coroutineScope {
        launch(CoroutineName("explicit-child-a")) {
            log("Explicit child A started")
            delay(80)
            log("Explicit child A completed")
        }
        launch(CoroutineName("explicit-child-b")) {
            log("Explicit child B started")
            delay(80)
            log("Explicit child B completed")
        }
    }
}

fun main() = runBlocking(CoroutineName("sequential-suspend-calls")) {
    log("Direct suspend calls:")
    recordSequentialSuspendCalls().forEach { event ->
        log(event)
    }
    log("Concurrency requires explicit builders:")
    demonstrateExplicitConcurrency()
}
