package dev.coroutineslab.fundamentals

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class SequentialVsConcurrentTest {
    @Test
    fun `sequential operations have sequential structure`() = runTest {
        val events = mutableListOf<String>()

        val result = runSequentialOperations(events::add)

        assertEquals(OperationResults(20, 22), result)
        assertEquals(
            listOf("first:start", "first:end", "second:start", "second:end"),
            events,
        )
    }

    @Test
    fun `concurrent operations both produce results`() = runTest {
        val events = mutableListOf<String>()

        val result = runConcurrentOperations(events::add)

        assertEquals(OperationResults(20, 22), result)
        assertTrue(events.indexOf("first:start") < events.indexOf("first:end"))
        assertTrue(events.indexOf("second:start") < events.indexOf("second:end"))
        val firstCompletion = minOf(events.indexOf("first:end"), events.indexOf("second:end"))
        assertTrue(events.indexOf("first:start") < firstCompletion)
        assertTrue(events.indexOf("second:start") < firstCompletion)
    }

    @Test
    fun `sequential and concurrent implementations return the same result`() = runTest {
        assertEquals(runSequentialOperations(), runConcurrentOperations())
    }
}
