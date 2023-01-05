package com.bory.coroutine.tutorial.scope

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * CoroutineContext is consisted with THREE components
 * - CoroutineName: the NAME of coroutine
 * - Dispatcher : decides which thread will execute this coroutine
 * - Job : instance of coroutine for controlling it
 */
fun main() = runBlocking {
    // this = CoroutineScope instance

    println(coroutineContext)

    // Without Parameter: CONFINED [CONFINED DISPATCHER]
    launch {
        launch(coroutineContext) {
            println("C1-C1::: $coroutineContext ::: ${Thread.currentThread().name}") // Thread: main
            delay(500)
            println("C1-C1 after delay::: $coroutineContext ::: ${Thread.currentThread().name}") // Thread: main
        }

        // inherits parent context (this(coroutineScope) is not inherited)
        println("C1::: $coroutineContext ::: ${Thread.currentThread().name}") // Thread: main
        delay(500)
        println("C1 after delay::: $coroutineContext ::: ${Thread.currentThread().name}") // Thread: main
    }

    // With Parameter: Dispatchers.Default [similar to GlobalScope.launch]
    launch(Dispatchers.Default) {
        launch(coroutineContext) {
            println("C2-C1::: $coroutineContext ::: ${Thread.currentThread().name}") // Thread: T1
            delay(500)
            println("C2-C1 after delay::: $coroutineContext ::: ${Thread.currentThread().name}") // Thread: T1 or some other
        }

        println("C2::: $coroutineContext ::: ${Thread.currentThread().name}") // Thread: T1
        delay(500)
        println("C2 after delay::: $coroutineContext ::: ${Thread.currentThread().name}") // Thread: T1 or some other
    }

    // With Parameter: Dispatchers.Unconfined [UNCONFINED DISPATCHER]
    launch(Dispatchers.Unconfined) {
        launch(coroutineContext) {
            println("C3-C1::: $coroutineContext ::: ${Thread.currentThread().name}") // Thread: main
            delay(500)
            println("C3-C1 after delay::: $coroutineContext ::: ${Thread.currentThread().name}") // Thread: some other
        }

        println("C3::: $coroutineContext ::: ${Thread.currentThread().name}") // Thread: main
        delay(500)
        println("C3 after delay::: $coroutineContext ::: ${Thread.currentThread().name}") // Thread: T2

        launch(coroutineContext) {
            println("C3-C2::: $coroutineContext ::: ${Thread.currentThread().name}") // Thread: T2
            delay(500)
            println("C3-C2 after delay::: $coroutineContext ::: ${Thread.currentThread().name}") // Thread: T2 or some other
        }
    }

    // With Parameter: Dispatchers.IO [IO DISPATCHER]
    launch(Dispatchers.IO) {
        launch(coroutineContext) {
            println("C4-C1::: $coroutineContext ::: ${Thread.currentThread().name}") // Thread: T2
            delay(500)
            println("C4-C1 after delay::: $coroutineContext ::: ${Thread.currentThread().name}") // Thread: T2 or some other
        }

        println("C4::: $coroutineContext ::: ${Thread.currentThread().name}") // Thread: T2
        delay(500)
        println("C4 after delay::: $coroutineContext ::: ${Thread.currentThread().name}") // Thread: T2 or some other

        launch(coroutineContext) {
            println("C4-C2::: $coroutineContext ::: ${Thread.currentThread().name}") // Thread: T2
            delay(500)
            println("C4-C2 after delay::: $coroutineContext ::: ${Thread.currentThread().name}") // Thread: T2 or some other
        }
    }

    // from parent coroutineContext <== inherited by runBlocking
    launch(coroutineContext) {
        println("C5::: $coroutineContext ::: ${Thread.currentThread().name}") // Thread: main
        delay(500)
        println("C5 after delay::: $coroutineContext ::: ${Thread.currentThread().name}") // Thread: main
    }

    Unit
}