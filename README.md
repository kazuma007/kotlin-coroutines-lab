# Kotlin Coroutines Under the Hood

## Project overview

Ten executable Kotlin/JVM experiments explain coroutine fundamentals and the
execution model through code, output, and tests. Each source file contains its
own concise explanation.

## Current status

- Phase 1: **Implemented**
- Phase 2: **In progress** (first two topics implemented)
- Phases 3–4: **Planned**

## What this repository demonstrates

1. [`runBlocking`](src/main/kotlin/dev/coroutineslab/fundamentals/E01RunBlocking.kt)
2. [`launch` and `Job`](src/main/kotlin/dev/coroutineslab/fundamentals/E02LaunchAndJob.kt)
3. [`async` and `Deferred`](src/main/kotlin/dev/coroutineslab/fundamentals/E03AsyncAndDeferred.kt)
4. [`delay` versus `Thread.sleep`](src/main/kotlin/dev/coroutineslab/fundamentals/E04DelayVsSleep.kt)
5. [Suspending functions](src/main/kotlin/dev/coroutineslab/fundamentals/E05SuspendingFunctions.kt)
6. [`suspend` is not asynchronous](src/main/kotlin/dev/coroutineslab/fundamentals/E06SuspendIsNotAsync.kt)
7. [Sequential versus concurrent execution](src/main/kotlin/dev/coroutineslab/fundamentals/E07SequentialVsConcurrent.kt)
8. [`coroutineScope`](src/main/kotlin/dev/coroutineslab/fundamentals/E08CoroutineScope.kt)
9. [`Continuation`, context, and dispatchers](src/main/kotlin/dev/coroutineslab/executionmodel/E09ContinuationContextAndDispatcher.kt)
10. [Thread switching and resumption](src/main/kotlin/dev/coroutineslab/executionmodel/E10ThreadSwitchingAndResumption.kt)

## Requirements

- Java 25

The Gradle Wrapper is included; no system Gradle installation is needed.

## Repository structure

```text
docs/                       Short roadmap and glossary
src/main/kotlin/            Ten experiments and the logger
src/test/kotlin/            Behavior-focused tests
gradle/                     Wrapper and pinned version catalog
.github/workflows/          Java 25 build
```

## Getting started

```bash
./gradlew build
```

Windows users can replace `./gradlew` with `gradlew.bat`.

## Running an experiment

```bash
./gradlew runE01
./gradlew runE10
```

Tasks `runE01` through `runE10` run experiments independently.

## Running all Phase 1 experiments

```bash
./gradlew runAllPhase1
```

The tasks run in numeric order.

## Running the implemented Phase 2 experiments

```bash
./gradlew runAllPhase2
```

## Running the tests

```bash
./gradlew test
```

Tests check values and coordinated lifecycle behavior, not exact timing or
thread names.

## Recommended experiments

- [E04: suspension versus blocking](src/main/kotlin/dev/coroutineslab/fundamentals/E04DelayVsSleep.kt)
- [E06: `suspend` is not asynchronous](src/main/kotlin/dev/coroutineslab/fundamentals/E06SuspendIsNotAsync.kt)
- [E07: sequential versus concurrent](src/main/kotlin/dev/coroutineslab/fundamentals/E07SequentialVsConcurrent.kt)
- [E08: structured work with `coroutineScope`](src/main/kotlin/dev/coroutineslab/fundamentals/E08CoroutineScope.kt)
- [E09: continuation context](src/main/kotlin/dev/coroutineslab/executionmodel/E09ContinuationContextAndDispatcher.kt)
- [E10: thread switching and resumption](src/main/kotlin/dev/coroutineslab/executionmodel/E10ThreadSwitchingAndResumption.kt)

## Learning roadmap

| Phase | Focus | Status | Planned topics |
| ----- | ----- | ------ | -------------- |
| 1 | Fundamentals | Implemented | builders, suspension, composition, concurrency |
| 2 | Execution model | In progress | contexts, dispatchers, continuations, resumption |
| 3 | Lifecycle and failure | Planned | cancellation, exceptions, supervision |
| 4 | Production concurrency | Planned | shared state, Channel, Flow, testing, performance |

See the [short roadmap](docs/learning-roadmap.md).

## Design principles

- One question per runnable experiment.
- Explanations stay beside the code.
- Tests avoid scheduler-dependent details.
- API guarantees are not inferred from one observed thread or output order.
- Future phases have no placeholder source.

## Limitations

This is an educational repository, not a benchmark or a replacement for
official documentation. Timing varies, and a coroutine is not a thread.

## References

- [Kotlin coroutines guide](https://kotlinlang.org/docs/coroutines-guide.html)
- [Coroutine basics](https://kotlinlang.org/docs/coroutines-basics.html)
- [Coroutine context and dispatchers](https://kotlinlang.org/docs/coroutine-context-and-dispatchers.html)
- [Cancellation and timeouts](https://kotlinlang.org/docs/cancellation-and-timeouts.html)
- [Exception handling](https://kotlinlang.org/docs/exception-handling.html)
- [Flow](https://kotlinlang.org/docs/flow.html)
- [`kotlinx.coroutines`](https://github.com/Kotlin/kotlinx.coroutines)
- [`kotlinx-coroutines-test`](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-test/)
