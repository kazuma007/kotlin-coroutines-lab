package dev.coroutineslab.executionmodel

import kotlin.coroutines.ContinuationInterceptor
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Job
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test

class ContinuationContextAndDispatcherTest {
    @Test
    fun `continuation carries the current coroutine context`() = runTest {
        withContext(CoroutineName("context-test")) {
            val context = currentCoroutineContext()
            val snapshot = inspectContinuationContext()

            assertEquals("context-test", snapshot.coroutineName)
            assertEquals(context[Job] != null, snapshot.hasJob)
            assertNotNull(snapshot.dispatcher)
            assertSame(
                context[ContinuationInterceptor] as? CoroutineDispatcher,
                snapshot.dispatcher,
            )
        }
    }
}
