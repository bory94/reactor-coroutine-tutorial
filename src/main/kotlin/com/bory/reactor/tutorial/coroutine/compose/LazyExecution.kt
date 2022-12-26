package com.bory.reactor.tutorial.coroutine.compose

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun main() = runBlocking {
    println("Main Program started in [${Thread.currentThread().name}] Thread.")

    val time = measureTimeMillis {
        val msgOne = async(start = CoroutineStart.LAZY) { message1() }
        val msgTwo = async(start = CoroutineStart.LAZY) { message2() }
        println("\t==> messages = ${msgOne.await()} ${msgTwo.await()}")
    }

    println("Main Program finished in [${Thread.currentThread().name}] Thread. Elapsed Time = [$time]ms")
}

suspend fun message1(): String {
    delay(1000)
    println("After message1")
    return "Hello"
}

suspend fun message2(): String {
    delay(1000)
    println("After message2")
    return "World"
}