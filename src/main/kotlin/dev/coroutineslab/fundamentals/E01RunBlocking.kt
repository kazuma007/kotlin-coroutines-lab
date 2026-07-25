package dev.coroutineslab.fundamentals

import dev.coroutineslab.common.log
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

/*
 * Explanation:
 * runBlocking bridges regular code to suspending code. The calling thread
 * does not continue past it until its coroutine body completes.
 */
private suspend fun loadGreeting(): String {
    delay(100)
    return "Hello from suspending code"
}

fun main() {
    println("Before runBlocking")

    runBlocking(CoroutineName("command-line-bridge")) {
        log("Inside runBlocking: ${loadGreeting()}")
    }

    println("After runBlocking")
}
