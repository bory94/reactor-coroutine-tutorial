package com.bory.reactor.tutorial.coroutine.scope

import kotlinx.coroutines.*

fun main() = runBlocking {
    println("$this started in ${Thread.currentThread().name}")

    launch {
        println("launch::: ${this@launch} in ${Thread.currentThread().name}")

        launch {
            println("==> launch in launch ::: $this in ${Thread.currentThread().name}")
        }
    }

    val result = async {
        println("async ${this@async} in ${Thread.currentThread().name}")
        launch {
            println("==> withContext in async How About this?? in ${Thread.currentThread().name}")
        }
        CoroutineScopeWrapper("Hello World", this)
    }.await()

    println("Result of Async = $result in ${Thread.currentThread().name}")
    println("CoroutineScope context: ${result.coroutineScope.coroutineContext}")
    println("CoroutineScope context: ${result.coroutineScope.isActive}")

    // this is not executed because coroutineScope is not active (isActive == false)
    val job = result.coroutineScope.launch {
        println("Hey? what's this? in ${Thread.currentThread().name}")
    }

    job.join()

    println("$this finished in ${Thread.currentThread().name}")
}

data class CoroutineScopeWrapper(
    val message: String,
    val coroutineScope: CoroutineScope
)