package dev.coroutineslab.lifecycle

import dev.coroutineslab.common.log
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield

/*
 * Explanation:
 * A coroutine started inside coroutineScope becomes a child of the scope's
 * Job. This parent-child link creates a Job hierarchy.
 *
 * Reaching the last line of the scope body is not enough for the scope to
 * return. Structured concurrency makes it wait until both child jobs finish.
 */
data class JobHierarchySnapshot(
    val directChildCount: Int,
    val firstIsDirectChild: Boolean,
    val secondIsDirectChild: Boolean,
    val bothChildrenActive: Boolean,
)

suspend fun observeJobHierarchy(
    onEvent: suspend (String) -> Unit = {},
): JobHierarchySnapshot = coroutineScope {
    val scopeJob = checkNotNull(currentCoroutineContext()[Job])

    val firstChild = launch(CoroutineName("first-child")) {
        onEvent("first:start")
        delay(50)
        onEvent("first:complete")
    }
    val secondChild = launch(CoroutineName("second-child")) {
        onEvent("second:start")
        delay(50)
        onEvent("second:complete")
    }

    // Let both children start and reach delay before inspecting the hierarchy.
    yield()
    val directChildren = scopeJob.children.toSet()
    val snapshot = JobHierarchySnapshot(
        directChildCount = directChildren.size,
        firstIsDirectChild = firstChild in directChildren,
        secondIsDirectChild = secondChild in directChildren,
        bothChildrenActive = firstChild.isActive && secondChild.isActive,
    )

    onEvent("scope:body-finished")
    snapshot
}

fun main() = runBlocking(CoroutineName("job-hierarchy-example")) {
    val snapshot = observeJobHierarchy { event -> log(event) }

    log("Direct children while running: ${snapshot.directChildCount}")
    log("The function returned after both children completed")
}
