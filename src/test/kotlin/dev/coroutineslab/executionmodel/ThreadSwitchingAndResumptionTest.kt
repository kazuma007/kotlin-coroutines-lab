package dev.coroutineslab.executionmodel

import java.util.concurrent.Executors
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test

class ThreadSwitchingAndResumptionTest {
    @Test
    fun `withContext selects a dispatcher and returns to the previous one`() = runTest {
        val firstExecutor = Executors.newSingleThreadExecutor()
        val secondExecutor = Executors.newSingleThreadExecutor()

        firstExecutor.asCoroutineDispatcher().use { firstDispatcher ->
            secondExecutor.asCoroutineDispatcher().use { secondDispatcher ->
                val points = observeThreadSwitching(firstDispatcher, secondDispatcher)

                assertEquals(
                    listOf(
                        "first:before-delay",
                        "first:after-delay",
                        "second:before-delay",
                        "second:after-delay",
                        "first:after-withContext",
                    ),
                    points.map { it.label },
                )

                val firstPoints = points.filter { it.label.startsWith("first:") }
                val secondPoints = points.filter { it.label.startsWith("second:") }

                firstPoints.forEach { assertSame(firstDispatcher, it.dispatcher) }
                secondPoints.forEach { assertSame(secondDispatcher, it.dispatcher) }

                assertEquals(1, firstPoints.map { it.threadName }.distinct().size)
                assertEquals(1, secondPoints.map { it.threadName }.distinct().size)
                assertNotEquals(firstPoints.first().threadName, secondPoints.first().threadName)
            }
        }
    }
}
