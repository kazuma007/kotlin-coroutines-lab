# Glossary

| Term | Meaning |
| ---- | ------- |
| Coroutine | A suspendable unit of work. It is not a thread and is not permanently tied to one. |
| Suspending function | A `suspend` function may suspend and call other suspending functions. It does not start a coroutine by itself. |
| Suspension | Pausing a coroutine while allowing its execution thread to do other work. |
| Blocking | Preventing the current thread from doing other work until an operation finishes. |
| `CoroutineScope` | A context and lifecycle boundary used by coroutine builders. |
| `runBlocking` | Bridges regular code to coroutine code while blocking the caller until completion. |
| `launch` | Starts result-free work and returns a `Job`. |
| `async` | Starts value-producing work and returns `Deferred<T>`. |
| `Job` | A handle for observing and waiting for coroutine completion. |
| `Deferred` | A `Job` with a value obtained through `await`. |
| Structured concurrency | Keeps child work bounded by the scope that created it. |
| Sequential execution | One operation starts after the previous one finishes. |
| Concurrent execution | Operations can make progress during overlapping lifetimes; they need not run simultaneously. |
| Continuation | The remaining work of a coroutine, together with the context needed to continue it. |
| `CoroutineContext` | A set of elements that describes a coroutine, such as its name, job, and dispatcher. |
| Dispatcher | A context element that selects which thread or threads run a coroutine. |
| `withContext` | Runs a suspending block with a changed context, then returns to the caller's context. |
| Resumption | Continuing a coroutine after it has suspended. |
| State machine | A model that uses a state label to decide which section of work runs next. |
| `createCoroutine` | A low-level function that creates a paused coroutine and returns the continuation used to start it. |
| `resumeWith` | Delivers a success or failure to a continuation. |

Concurrency is not the same as parallelism, and suspension is not blocking.
