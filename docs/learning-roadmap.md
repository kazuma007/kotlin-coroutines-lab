# Learning Roadmap

Only Phase 1 has source code. Later phases remain plans.

## Phase 1: Coroutine Fundamentals

**Status:** Implemented

- [x] `runBlocking`, `launch`, `async`, `Job`, and `Deferred`
- [x] Suspension versus blocking
- [x] Suspending function composition
- [x] Sequential versus concurrent work
- [x] Introductory `coroutineScope`

Implemented experiments:
[E01](../src/main/kotlin/dev/coroutineslab/fundamentals/E01RunBlocking.kt),
[E02](../src/main/kotlin/dev/coroutineslab/fundamentals/E02LaunchAndJob.kt),
[E03](../src/main/kotlin/dev/coroutineslab/fundamentals/E03AsyncAndDeferred.kt),
[E04](../src/main/kotlin/dev/coroutineslab/fundamentals/E04DelayVsSleep.kt),
[E05](../src/main/kotlin/dev/coroutineslab/fundamentals/E05SuspendingFunctions.kt),
[E06](../src/main/kotlin/dev/coroutineslab/fundamentals/E06SuspendIsNotAsync.kt),
[E07](../src/main/kotlin/dev/coroutineslab/fundamentals/E07SequentialVsConcurrent.kt), and
[E08](../src/main/kotlin/dev/coroutineslab/fundamentals/E08CoroutineScope.kt).

**Boundary:** no cancellation, failure propagation, Flow, or low-level
continuation details.

## Phase 2: Execution Model

**Status:** Planned

- [ ] `Continuation`, context, and dispatchers
- [ ] Thread selection, switching, suspension, and resumption
- [ ] Conceptual state-machine transformation
- [ ] Low-level coroutine APIs

**Boundary:** conceptual models will be separated from compiler details.

## Phase 3: Lifecycle, Cancellation, and Failure

**Status:** Planned

- [ ] Job hierarchy and structured concurrency
- [ ] Cooperative cancellation, cleanup, and timeouts
- [ ] Exception propagation and handling
- [ ] Supervision

**Boundary:** no Channels, Flow, or shared-state experiments.

## Phase 4: Production-Oriented Concurrency

**Status:** Planned

- [ ] Shared state, atomics, `Mutex`, and confinement
- [ ] Channel and Flow
- [ ] StateFlow and SharedFlow
- [ ] Advanced testing, debugging, and performance measurement

**Boundary:** no Android, server framework, database, or external-service
examples.
