package dev.coroutineslab.executionmodel

import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ConceptualStateMachineTest {
    @Test
    fun `conceptual state machine matches the suspending function`() = runTest {
        val words = mapOf("first" to "Hello", "second" to "coroutines")
        val suspendingEvents = mutableListOf<String>()
        val conceptualEvents = mutableListOf<String>()

        val suspendingResult = composeGreetingWithSuspensions(
            loadWord = { request ->
                delay(1)
                words.getValue(request)
            },
            onEvent = suspendingEvents::add,
        )
        val conceptualResult = runConceptualGreetingStateMachine(
            loadWord = words::getValue,
            onEvent = conceptualEvents::add,
        )

        assertEquals("Hello coroutines", suspendingResult)
        assertEquals(suspendingResult, conceptualResult)
        assertEquals(suspendingEvents, conceptualEvents)
        assertEquals(
            listOf(
                "request:first",
                "resumed:first",
                "request:second",
                "resumed:second",
                "completed",
            ),
            conceptualEvents,
        )
    }
}
