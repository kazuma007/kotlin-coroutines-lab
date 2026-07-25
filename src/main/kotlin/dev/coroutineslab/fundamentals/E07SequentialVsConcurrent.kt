package dev.coroutineslab.fundamentals

import dev.coroutineslab.common.log
import kotlin.time.measureTimedValue
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

/*
 * Explanation:
 * Direct suspend calls run sequentially. async lets independent waits overlap,
 * but timing varies and concurrency is not always faster.
 */
data class OperationResults(
    val first: Int,
    val second: Int,
) {
    val total: Int = first + second
}

private suspend fun firstOperation(onEvent: suspend (String) -> Unit): Int {
    onEvent("first:start")
    delay(300)
    onEvent("first:end")
    return 20
}

private suspend fun secondOperation(onEvent: suspend (String) -> Unit): Int {
    onEvent("second:start")
    delay(300)
    onEvent("second:end")
    return 22
}

suspend fun runSequentialOperations(onEvent: suspend (String) -> Unit = {}): OperationResults {
    val first = firstOperation(onEvent)
    val second = secondOperation(onEvent)
    return OperationResults(first, second)
}

suspend fun runConcurrentOperations(onEvent: suspend (String) -> Unit = {}): OperationResults =
    coroutineScope {
        val first = async(CoroutineName("first-operation")) { firstOperation(onEvent) }
        val second = async(CoroutineName("second-operation")) { secondOperation(onEvent) }
        OperationResults(first.await(), second.await())
    }

fun main() = runBlocking(CoroutineName("execution-comparison")) {
    val sequential = measureTimedValue { runSequentialOperations() }
    log("Sequential result=${sequential.value.total}, duration=${sequential.duration}")

    val concurrent = measureTimedValue { runConcurrentOperations() }
    log("Concurrent result=${concurrent.value.total}, duration=${concurrent.duration}")
}
