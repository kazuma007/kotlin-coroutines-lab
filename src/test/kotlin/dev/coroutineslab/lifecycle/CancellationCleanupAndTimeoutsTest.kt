package dev.coroutineslab.lifecycle

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CancellationCleanupAndTimeoutsTest {
    @Test
    fun `cancellation and timeout run cleanup before returning`() = runTest {
        val events = mutableListOf<String>()

        val summary = demonstrateCancellationAndTimeout(events::add)

        assertTrue(summary.workerWasCancelled)
        assertNull(summary.timeoutValue)
        assertEquals(
            listOf(
                "worker:start",
                "worker:cleanup",
                "worker:joined",
                "timeout:start",
                "timeout:cleanup",
                "timeout:returned",
            ),
            events,
        )
    }
}
