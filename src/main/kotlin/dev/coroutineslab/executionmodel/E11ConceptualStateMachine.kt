package dev.coroutineslab.executionmodel

import dev.coroutineslab.common.log
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

/*
 * Explanation:
 * A suspending function can be understood as a state machine. A state label
 * says which section should run next. Values needed after suspension are kept
 * in fields so they are still available when execution resumes.
 *
 * ConceptualGreetingStateMachine is a teaching model. It is not Kotlin
 * compiler output, and real compiler details may change between versions.
 */
suspend fun composeGreetingWithSuspensions(
    loadWord: suspend (String) -> String,
    onEvent: (String) -> Unit = {},
): String {
    onEvent("request:first")
    val firstWord = loadWord("first")
    onEvent("resumed:first")

    onEvent("request:second")
    val secondWord = loadWord("second")
    onEvent("resumed:second")

    onEvent("completed")
    return "$firstWord $secondWord"
}

sealed interface ConceptualStep {
    data class Suspended(val request: String) : ConceptualStep

    data class Completed(val value: String) : ConceptualStep
}

class ConceptualGreetingStateMachine(
    private val onEvent: (String) -> Unit = {},
) {
    private enum class Label {
        START,
        AFTER_FIRST_SUSPENSION,
        AFTER_SECOND_SUSPENSION,
        DONE,
    }

    private var label = Label.START
    private var firstWord: String? = null

    fun start(): ConceptualStep {
        check(label == Label.START) { "The state machine has already started" }

        onEvent("request:first")
        label = Label.AFTER_FIRST_SUSPENSION
        return ConceptualStep.Suspended("first")
    }

    fun resume(value: String): ConceptualStep =
        when (label) {
            Label.START -> error("Call start before resume")
            Label.AFTER_FIRST_SUSPENSION -> {
                firstWord = value
                onEvent("resumed:first")
                onEvent("request:second")
                label = Label.AFTER_SECOND_SUSPENSION
                ConceptualStep.Suspended("second")
            }

            Label.AFTER_SECOND_SUSPENSION -> {
                val result = "${checkNotNull(firstWord)} $value"
                onEvent("resumed:second")
                onEvent("completed")
                label = Label.DONE
                ConceptualStep.Completed(result)
            }

            Label.DONE -> error("The state machine has already completed")
        }
}

fun runConceptualGreetingStateMachine(
    loadWord: (String) -> String,
    onEvent: (String) -> Unit = {},
): String {
    val machine = ConceptualGreetingStateMachine(onEvent)
    var step = machine.start()

    while (step is ConceptualStep.Suspended) {
        step = machine.resume(loadWord(step.request))
    }

    return (step as ConceptualStep.Completed).value
}

fun main() = runBlocking(CoroutineName("state-machine-example")) {
    val words = mapOf("first" to "Hello", "second" to "coroutines")

    val suspendingEvents = mutableListOf<String>()
    val suspendingResult = composeGreetingWithSuspensions(
        loadWord = { request ->
            delay(50)
            words.getValue(request)
        },
        onEvent = suspendingEvents::add,
    )
    log("Suspending function: $suspendingResult")
    log("Events: ${suspendingEvents.joinToString()}")

    val conceptualEvents = mutableListOf<String>()
    val conceptualResult = runConceptualGreetingStateMachine(
        loadWord = words::getValue,
        onEvent = conceptualEvents::add,
    )
    log("Conceptual state machine: $conceptualResult")
    log("Events: ${conceptualEvents.joinToString()}")
}
