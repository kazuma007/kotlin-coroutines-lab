package dev.coroutineslab.lifecycle

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class JobHierarchyAndStructuredConcurrencyTest {
    @Test
    fun `scope owns its children and returns after they complete`() = runTest {
        val events = mutableListOf<String>()

        val snapshot = observeJobHierarchy(events::add)
        events += "function:return"

        assertEquals(2, snapshot.directChildCount)
        assertTrue(snapshot.firstIsDirectChild)
        assertTrue(snapshot.secondIsDirectChild)
        assertTrue(snapshot.bothChildrenActive)
        assertTrue(events.indexOf("scope:body-finished") < events.indexOf("first:complete"))
        assertTrue(events.indexOf("scope:body-finished") < events.indexOf("second:complete"))
        assertTrue(events.indexOf("first:complete") < events.indexOf("function:return"))
        assertTrue(events.indexOf("second:complete") < events.indexOf("function:return"))
        assertTrue(events.indexOf("scope:body-finished") < events.indexOf("function:return"))
    }
}
