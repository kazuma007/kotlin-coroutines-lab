package dev.coroutineslab.fundamentals

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AsyncAndDeferredTest {
    @Test
    fun `deferred returns the expected value`() = runTest {
        val result: Deferred<Int> = async { calculateTotal() }
        val lifecycle: Job = result

        assertEquals(false, lifecycle.isCompleted)
        assertEquals(35, result.await())
        assertEquals(true, lifecycle.isCompleted)
    }
}
