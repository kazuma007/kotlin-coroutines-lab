package dev.coroutineslab.executionmodel

import dev.coroutineslab.common.log
import java.util.concurrent.Executors
import kotlin.coroutines.ContinuationInterceptor
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

/*
 * Explanation:
 * A dispatcher selects threads for a coroutine. withContext temporarily uses
 * another dispatcher. delay suspends the coroutine, and the selected
 * dispatcher later resumes it.
 *
 * Suspension does not itself promise a thread switch. In this example each
 * dispatcher has one thread, so work resumes on that dispatcher's thread.
 * After the nested withContext finishes, execution returns to the first
 * dispatcher.
 */
data class ExecutionPoint(
    val label: String,
    val threadName: String,
    val dispatcher: CoroutineDispatcher?,
)

private suspend fun executionPoint(label: String): ExecutionPoint {
    val dispatcher =
        currentCoroutineContext()[ContinuationInterceptor] as? CoroutineDispatcher

    return ExecutionPoint(
        label = label,
        threadName = Thread.currentThread().name,
        dispatcher = dispatcher,
    )
}

suspend fun observeThreadSwitching(
    firstDispatcher: CoroutineDispatcher,
    secondDispatcher: CoroutineDispatcher,
    onPoint: suspend (ExecutionPoint) -> Unit = {},
): List<ExecutionPoint> {
    val points = mutableListOf<ExecutionPoint>()

    suspend fun record(label: String) {
        val point = executionPoint(label)
        points += point
        onPoint(point)
    }

    withContext(firstDispatcher + CoroutineName("first-dispatcher-work")) {
        record("first:before-delay")
        delay(50)
        record("first:after-delay")

        withContext(secondDispatcher + CoroutineName("second-dispatcher-work")) {
            record("second:before-delay")
            delay(50)
            record("second:after-delay")
        }

        record("first:after-withContext")
    }

    return points
}

fun main() {
    val firstExecutor = Executors.newSingleThreadExecutor { runnable ->
        Thread(runnable, "first-demo-thread")
    }
    val secondExecutor = Executors.newSingleThreadExecutor { runnable ->
        Thread(runnable, "second-demo-thread")
    }

    firstExecutor.asCoroutineDispatcher().use { firstDispatcher ->
        secondExecutor.asCoroutineDispatcher().use { secondDispatcher ->
            runBlocking(CoroutineName("thread-switching-example")) {
                observeThreadSwitching(firstDispatcher, secondDispatcher) { point ->
                    log(point.label)
                }
            }
        }
    }
}
