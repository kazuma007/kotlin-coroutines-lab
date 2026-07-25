package dev.coroutineslab.fundamentals

import dev.coroutineslab.common.log
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

/*
 * Explanation:
 * coroutineScope groups child work into one structured suspending operation.
 * It returns only after its children finish; it does not create a thread.
 */
suspend fun buildSummary(onEvent: suspend (String) -> Unit = {}): String = coroutineScope {
    val title = async(CoroutineName("title-child")) {
        delay(100)
        onEvent("title:complete")
        "Coroutine"
    }
    val detail = async(CoroutineName("detail-child")) {
        delay(150)
        onEvent("detail:complete")
        "scope"
    }

    val summary = "${title.await()} ${detail.await()}"
    onEvent("scope:return")
    summary
}

fun main() = runBlocking(CoroutineName("scope-caller")) {
    log("Calling structured suspending function")
    val summary = buildSummary { event -> log(event) }
    log("Function returned: $summary")
}
