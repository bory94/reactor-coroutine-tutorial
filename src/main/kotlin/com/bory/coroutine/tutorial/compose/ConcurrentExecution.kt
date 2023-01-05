package com.bory.coroutine.tutorial.compose

import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis


fun main() = runBlocking {
    println("Main Program started in [${Thread.currentThread().name}] Thread.")

    val time = measureTimeMillis {
        val msgOne = async { one() }
        val msgTwo = async { two() }
        println("\t==> messages = ${msgOne.await()} ${msgTwo.await()}")
    }

    println("Main Program finished in [${Thread.currentThread().name}] Thread. Elapsed Time = $time ms")
}