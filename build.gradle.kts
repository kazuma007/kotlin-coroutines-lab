import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.jvm)
}

group = "dev.coroutineslab"
version = "1.0.0"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_25
    }
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform.launcher)
}

tasks.test {
    useJUnitPlatform()
}

val experiments = (1..8).associate { number ->
    val id = number.toString().padStart(2, '0')
    val taskName = "runE$id"
    val title = listOf(
        "RunBlocking",
        "LaunchAndJob",
        "AsyncAndDeferred",
        "DelayVsSleep",
        "SuspendingFunctions",
        "SuspendIsNotAsync",
        "SequentialVsConcurrent",
        "CoroutineScope",
    )[number - 1]
    taskName to "dev.coroutineslab.fundamentals.E${id}${title}Kt"
}

val experimentTasks = experiments.map { (taskName, generatedMainClass) ->
    tasks.register<JavaExec>(taskName) {
        group = "coroutine experiments"
        description = "Runs Phase 1 experiment ${taskName.removePrefix("runE")}."
        classpath = sourceSets.main.get().runtimeClasspath
        mainClass = generatedMainClass
    }
}

experimentTasks.zipWithNext().forEach { (earlier, later) ->
    later.configure {
        mustRunAfter(earlier)
    }
}

tasks.register("runAllPhase1") {
    group = "coroutine experiments"
    description = "Runs all eight Phase 1 experiments in numeric order."
    dependsOn(experimentTasks)
}

val phase2Experiments = mapOf(
    "runE09" to "dev.coroutineslab.executionmodel.E09ContinuationContextAndDispatcherKt",
    "runE10" to "dev.coroutineslab.executionmodel.E10ThreadSwitchingAndResumptionKt",
)

val phase2ExperimentTasks = phase2Experiments.map { (taskName, generatedMainClass) ->
    tasks.register<JavaExec>(taskName) {
        group = "coroutine experiments"
        description = "Runs Phase 2 experiment ${taskName.removePrefix("runE")}."
        classpath = sourceSets.main.get().runtimeClasspath
        mainClass = generatedMainClass
    }
}

phase2ExperimentTasks.zipWithNext().forEach { (earlier, later) ->
    later.configure {
        mustRunAfter(earlier)
    }
}

tasks.register("runAllPhase2") {
    group = "coroutine experiments"
    description = "Runs the implemented Phase 2 experiments in numeric order."
    dependsOn(phase2ExperimentTasks)
}
