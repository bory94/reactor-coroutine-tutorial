package com.bory.reactor.tutorial.coroutine

import kotlinx.coroutines.*

fun main() = runBlocking {
    val job = launch(Dispatchers.Default) { loopWithIsActive(1_000_000, this) }
    delay(1)

    job.cancelAndJoin()
}

fun loopWithIsActive(loopCount: Int, coroutineScope: CoroutineScope) {
    (0 until loopCount).forEach { i ->
        print("$i,")
        if (!coroutineScope.isActive) {
            return
        }
    }
}

