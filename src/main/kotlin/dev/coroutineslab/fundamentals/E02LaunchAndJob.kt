package dev.coroutineslab.fundamentals

import dev.coroutineslab.common.log
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/*
 * Explanation:
 * launch starts result-free work and returns a Job. join waits for that job,
 * after which isCompleted is guaranteed to be true.
 */
data class JobLifecycle(
    val activeBeforeJoin: Boolean,
    val completedBeforeJoin: Boolean,
    val completedAfterJoin: Boolean,
)

suspend fun observeJobLifecycle(): JobLifecycle = coroutineScope {
    val job: Job = launch(CoroutineName("background-task")) {
        delay(100)
    }

    val activeBeforeJoin = job.isActive
    val completedBeforeJoin = job.isCompleted
    job.join()

    JobLifecycle(
        activeBeforeJoin = activeBeforeJoin,
        completedBeforeJoin = completedBeforeJoin,
        completedAfterJoin = job.isCompleted,
    )
}

fun main() = runBlocking(CoroutineName("launch-example")) {
    val job: Job = launch(CoroutineName("worker")) {
        log("Work started")
        delay(150)
        log("Work completed")
    }

    log("launch returned Job: active=${job.isActive}, completed=${job.isCompleted}")
    job.join()
    log("After join: active=${job.isActive}, completed=${job.isCompleted}")
}
