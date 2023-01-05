package com.bory.reactor.tutorial.coroutine

import kotlinx.coroutines.*

fun main() = runBlocking {
    println("==> Starting Looping Job")

    val job = launch { loopWithDelay(1_000_000, 50) }

    println("==> Looping Job Launched.")
    delay(10)

//    job.cancelAndJoin()
    job.cancel(CancellationException("Cancel Cancel!"))
    job.join()
    println()

    println("<== Looping Job Canceled.")
}

suspend fun loopWithDelay(num: Int, delayedTime: Long = 100) {
    print("\t==> Loop Job Started. ==> ")
    try {
        (0 until num).forEach { i ->
            print("$i,")
            if (i != 0 && i % 10 == 0) {
                println()
            }
//        delay(delayedTime)
            yield()
        }
    } catch (e: CancellationException) {
        println(" ==> Job Canceled. <== ${e.message}")
    } finally {
        withContext(NonCancellable) {
            print("==> Some delay....")
            delay(1000)
            println(" ... then job finished successfully.")
        }
    }

    println(" <== Loop Job Finished.")
}