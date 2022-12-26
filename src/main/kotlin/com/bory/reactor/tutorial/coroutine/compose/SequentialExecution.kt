package com.bory.reactor.tutorial.coroutine.compose

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun main() = runBlocking {
    println("Main Program started in [${Thread.currentThread().name}] Thread.")

    val time = measureTimeMillis {
        val msgOne = one()
        val msgTwo = two()
        println("\t==> messages = $msgOne $msgTwo")
    }

    println("Main Program finished in [${Thread.currentThread().name}] Thread. Elapsed Time = $time ms")
}

suspend fun one(): String {
    delay(1000)
    return "Hello"
}

suspend fun two(): String {
    delay(1000)
    return "World"
}