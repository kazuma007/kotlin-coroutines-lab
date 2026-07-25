package dev.coroutineslab.fundamentals

import dev.coroutineslab.common.log
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

/*
 * Explanation:
 * async starts value-producing work and returns Deferred<T>, which is also a
 * Job. await suspends until the value is available.
 */
private suspend fun loadSubtotal(): Int {
    delay(150)
    return 42
}

private suspend fun loadDiscount(): Int {
    delay(100)
    return 7
}

suspend fun calculateTotal(): Int = coroutineScope {
    val subtotal: Deferred<Int> = async { loadSubtotal() }
    val discount: Deferred<Int> = async { loadDiscount() }
    subtotal.await() - discount.await()
}

fun main() = runBlocking(CoroutineName("async-example")) {
    val subtotal: Deferred<Int> = async(CoroutineName("subtotal")) {
        log("Loading subtotal")
        loadSubtotal()
    }
    val discount: Deferred<Int> = async(CoroutineName("discount")) {
        log("Loading discount")
        loadDiscount()
    }
    val subtotalJob: Job = subtotal

    log("Deferred can be used as a Job: active=${subtotalJob.isActive}")
    val total = subtotal.await() - discount.await()
    log("Combined total: $total")
}
