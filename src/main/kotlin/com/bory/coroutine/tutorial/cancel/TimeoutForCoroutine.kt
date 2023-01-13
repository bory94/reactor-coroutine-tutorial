package com.bory.coroutine.tutorial.cancel

import kotlinx.coroutines.*
import kotlin.time.Duration.Companion.milliseconds

fun main() = runBlocking {
    try {
        withTimeout(50.milliseconds) {
            try {
                loop()
            } catch (e: TimeoutCancellationException) {
                println("\n Inside Loop Exception: ${e.message}")
            } finally {
                println("Finally...")
                withContext(NonCancellable) { delay(200) }
                println("Finished.")
            }
        }
    } catch (e: TimeoutCancellationException) {
        println("\n Timeout Exception:: ${e.message}")
    }


    println("run withTimeoutOrNull")
    val result = withTimeoutOrNull(2000.milliseconds) {
        loop()

        "Hello World"
    }

    println("\nResult of Timeout Coroutine ::: $result")
}

suspend fun loop() {
    var printed = 0
    (0 until 1_000_000).forEach { i ->
        printed++

        if (printed % 100 == 0) {
            print(".")
        }
        if (printed % 10000 == 0) {
            println(" <== $printed")
        }
        yield()
    }
}