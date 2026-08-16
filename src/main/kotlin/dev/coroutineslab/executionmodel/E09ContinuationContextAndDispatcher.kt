package dev.coroutineslab.executionmodel

import dev.coroutineslab.common.log
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.resume
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext

/*
 * Explanation:
 * A Continuation represents what a suspended coroutine should do next. It
 * also carries the CoroutineContext. The context is a set of elements, such
 * as CoroutineName, Job, and CoroutineDispatcher.
 *
 * A dispatcher is stored under the ContinuationInterceptor key because it
 * intercepts continuations and decides where they run. suspendCoroutine gives
 * this example its current continuation. Calling resume continues it exactly
 * once. Later experiments will cover the low-level APIs in more detail.
 */
data class ContinuationContextSnapshot(
    val coroutineName: String?,
    val hasJob: Boolean,
    val dispatcher: CoroutineDispatcher?,
)

suspend fun inspectContinuationContext(): ContinuationContextSnapshot =
    suspendCancellableCoroutine { continuation ->
        val context = continuation.context
        val snapshot = ContinuationContextSnapshot(
            coroutineName = context[CoroutineName]?.name,
            hasJob = context[Job] != null,
            dispatcher = context[ContinuationInterceptor] as? CoroutineDispatcher,
        )

        continuation.resume(snapshot)
    }

fun main() = runBlocking(CoroutineName("context-example")) {
    val outer = inspectContinuationContext()
    log("Outer context: name=${outer.coroutineName}, has Job=${outer.hasJob}")
    log("Outer dispatcher: ${outer.dispatcher}")

    withContext(Dispatchers.Default + CoroutineName("default-worker")) {
        val worker = inspectContinuationContext()
        log("Worker context: name=${worker.coroutineName}, has Job=${worker.hasJob}")
        log("Worker dispatcher: ${worker.dispatcher}")
    }
}
