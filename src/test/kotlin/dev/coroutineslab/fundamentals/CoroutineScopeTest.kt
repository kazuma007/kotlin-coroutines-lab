package dev.coroutineslab.fundamentals

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CoroutineScopeTest {
    @Test
    fun `coroutineScope function waits for child work before returning`() = runTest {
        val events = mutableListOf<String>()

        val result = buildSummary(events::add)

        assertEquals("Coroutine scope", result)
        assertTrue(events.indexOf("title:complete") < events.indexOf("scope:return"))
        assertTrue(events.indexOf("detail:complete") < events.indexOf("scope:return"))
    }
}
