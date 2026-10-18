package dev.coroutineslab.lifecycle

import dev.coroutineslab.common.log
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull

/*
 * Explanation:
 * Cancellation is cooperative. cancel asks a coroutine to stop, and
 * cancellable suspending functions such as delay notice that request.
 *
 * finally performs cleanup when the worker is cancelled. cancelAndJoin waits
 * until that cleanup finishes. withTimeoutOrNull cancels a slow block when its
 * time limit is reached and returns null instead of a value.
 */
data class CancellationSummary(
    val workerWasCancelled: Boolean,
    val timeoutValue: String?,
)

suspend fun demonstrateCancellationAndTimeout(
    onEvent: (String) -> Unit = {},
): CancellationSummary = coroutineScope {
    val workerStarted = CompletableDeferred<Unit>()
    val worker = launch(CoroutineName("cancellable-worker")) {
        try {
            onEvent("worker:start")
            workerStarted.complete(Unit)

            while (true) {
                delay(100)
                onEvent("worker:step")
            }
        } finally {
            onEvent("worker:cleanup")
        }
    }

    workerStarted.await()
    worker.cancelAndJoin()
    onEvent("worker:joined")

    val timeoutValue = withTimeoutOrNull(50) {
        try {
            onEvent("timeout:start")
            delay(100)
            "finished"
        } finally {
            onEvent("timeout:cleanup")
        }
    }
    onEvent("timeout:returned")

    CancellationSummary(
        workerWasCancelled = worker.isCancelled,
        timeoutValue = timeoutValue,
    )
}

fun main() = runBlocking(CoroutineName("cancellation-example")) {
    val summary = demonstrateCancellationAndTimeout(::println)

    log("Worker was cancelled: ${summary.workerWasCancelled}")
    log("Timed block returned: ${summary.timeoutValue}")
}
