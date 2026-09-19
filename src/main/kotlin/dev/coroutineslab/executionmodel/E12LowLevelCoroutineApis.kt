package dev.coroutineslab.executionmodel

import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.createCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/*
 * Explanation:
 * createCoroutine creates a coroutine but does not start it. It returns a
 * Continuation<Unit>; resume(Unit) starts the body. The completion
 * continuation receives the final Result through resumeWith.
 *
 * suspendCoroutine exposes the continuation at one suspension point. This
 * example saves it and resumes it manually. These APIs explain the mechanism,
 * but normal application code should prefer builders such as launch and async
 * because they also provide a Job and structured lifecycle management.
 */
data class LowLevelCoroutineRun(
    val events: List<String>,
    val result: Result<String>,
)

fun runLowLevelCoroutine(
    context: CoroutineContext = EmptyCoroutineContext,
): LowLevelCoroutineRun {
    val events = mutableListOf<String>()
    var pausedContinuation: Continuation<String>? = null
    var completionResult: Result<String>? = null

    val completion = object : Continuation<String> {
        override val context: CoroutineContext = context

        override fun resumeWith(result: Result<String>) {
            events += if (result.isSuccess) "completion:success" else "completion:failure"
            completionResult = result
        }
    }

    val body: suspend () -> String = {
        events += "body:start"
        val word = suspendCoroutine { continuation ->
            pausedContinuation = continuation
            events += "body:suspended"
        }
        events += "body:resumed"
        "Hello, $word"
    }

    val starter: Continuation<Unit> = body.createCoroutine(completion)
    events += "caller:created"

    starter.resume(Unit)
    events += "caller:after-start"
    check(completionResult == null) { "The body should still be suspended" }

    checkNotNull(pausedContinuation).resume("coroutine")
    events += "caller:after-resume"

    return LowLevelCoroutineRun(
        events = events,
        result = checkNotNull(completionResult),
    )
}

fun main() {
    val run = runLowLevelCoroutine()

    run.events.forEach(::println)
    println("result=${run.result.getOrThrow()}")
}
