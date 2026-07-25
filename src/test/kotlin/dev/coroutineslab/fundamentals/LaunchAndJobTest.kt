package dev.coroutineslab.fundamentals

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class LaunchAndJobTest {
    @Test
    fun `job is complete after join`() = runTest {
        val lifecycle = observeJobLifecycle()

        assertTrue(lifecycle.activeBeforeJoin)
        assertFalse(lifecycle.completedBeforeJoin)
        assertTrue(lifecycle.completedAfterJoin)
    }
}
