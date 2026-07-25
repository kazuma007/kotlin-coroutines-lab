package dev.coroutineslab.common

import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.currentCoroutineContext

private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS")

suspend fun log(message: String) {
    val coroutineName = currentCoroutineContext()[CoroutineName]?.name ?: "unnamed"
    val time = LocalTime.now().format(timeFormatter)
    println("$time [${Thread.currentThread().name}] [$coroutineName] $message")
}
