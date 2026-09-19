package dev.coroutineslab.executionmodel

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class LowLevelCoroutineApisTest {
    @Test
    fun `low-level continuations start suspend resume and complete in order`() {
        val run = runLowLevelCoroutine()

        assertTrue(run.result.isSuccess)
        assertEquals("Hello, coroutine", run.result.getOrThrow())
        assertEquals(
            listOf(
                "caller:created",
                "body:start",
                "body:suspended",
                "caller:after-start",
                "body:resumed",
                "completion:success",
                "caller:after-resume",
            ),
            run.events,
        )
    }
}
