package dev.coroutineslab.fundamentals

import dev.coroutineslab.common.log
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

/*
 * Explanation:
 * Suspending functions compose and return values like regular functions.
 * Direct calls remain sequential unless a coroutine builder adds concurrency.
 */
private suspend fun loadFirstName(): String {
    delay(80)
    return "Ada"
}

private suspend fun loadLastName(): String {
    delay(80)
    return "Lovelace"
}

suspend fun composeDisplayName(): String {
    val firstName = loadFirstName()
    val lastName = loadLastName()
    val separator = " "
    return firstName + separator + lastName
}

fun main() = runBlocking(CoroutineName("suspending-functions")) {
    log("Loading values sequentially")
    log("Composed value: ${composeDisplayName()}")
}
