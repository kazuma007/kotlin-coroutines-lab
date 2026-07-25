package dev.coroutineslab.fundamentals

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SuspendingFunctionsTest {
    @Test
    fun `composed suspending functions return the expected value`() = runTest {
        assertEquals("Ada Lovelace", composeDisplayName())
    }

    @Test
    fun `sequential suspend calls preserve logical event order`() = runTest {
        assertEquals(
            listOf(
                "no-suspension-point:start",
                "no-suspension-point:end",
                "suspending-call:start",
                "suspending-call:end",
                "final-call:start",
                "final-call:end",
            ),
            recordSequentialSuspendCalls(),
        )
    }
}
