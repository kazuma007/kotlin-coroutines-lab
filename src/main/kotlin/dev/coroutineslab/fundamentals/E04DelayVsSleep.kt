package dev.coroutineslab.fundamentals

import dev.coroutineslab.common.log
import java.util.concurrent.Executors
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/*
 * Explanation:
 * On one execution thread, delay lets another coroutine run while suspended.
 * Thread.sleep blocks that thread, so no other coroutine can use it.
 */
private suspend fun demonstrateDelay() = coroutineScope {
    val firstStarted = CompletableDeferred<Unit>()

    launch(CoroutineName("delaying-coroutine")) {
        log("Before delay")
        firstStarted.complete(Unit)
        delay(200)
        log("After delay")
    }
    launch(CoroutineName("other-coroutine")) {
        firstStarted.await()
        log("Progress while the first coroutine is suspended")
    }
}

private suspend fun demonstrateSleep() = coroutineScope {
    val firstStarted = CompletableDeferred<Unit>()

    launch(CoroutineName("sleeping-coroutine")) {
        log("Before Thread.sleep")
        firstStarted.complete(Unit)
        Thread.sleep(200)
        log("After Thread.sleep")
    }
    launch(CoroutineName("other-coroutine")) {
        firstStarted.await()
        log("Progress only after the single thread is available")
    }
}

fun main() {
    val executor = Executors.newSingleThreadExecutor { runnable ->
        Thread(runnable, "single-demo-thread")
    }

    executor.asCoroutineDispatcher().use { dispatcher ->
        runBlocking(dispatcher + CoroutineName("delay-vs-sleep")) {
            log("Delay demonstration")
            demonstrateDelay()
            log("Thread.sleep demonstration")
            demonstrateSleep()
        }
    }
}
