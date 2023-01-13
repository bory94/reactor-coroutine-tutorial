package com.bory.coroutine.tutorial.cancel

import kotlinx.coroutines.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import reactor.util.function.Tuples
import java.lang.Thread.sleep
import java.util.*
import java.util.concurrent.Executors
import kotlin.concurrent.thread


@OptIn(DelicateCoroutinesApi::class)
suspend fun main() {
    val publishExecutor = Executors.newFixedThreadPool(3)
    val subscribeExecutor = Executors.newFixedThreadPool(3)

    runBlocking {
        println("Main program starts in [${Thread.currentThread().name}] Thread")

        thread {
            println("\t==> Forked Thread starts in [${Thread.currentThread().name}] Thread")
            sleep(1000)
            println("\t<== Forked Thread finishes in [${Thread.currentThread().name}] Thread")
        }

        println("\t==> Starting Coroutines with Array...")
        arrayOf(
            Tuples.of("C1", 2000L),
            Tuples.of("C2", 1500L),
            Tuples.of("C3", 1000L),
        ).map {
            async { executeCoroutineFunc(it.t1, it.t2) }
        }.forEach {
            println("\t==> Array RESULT ::: ${it.await()}")
        }
        println("\t==> Waiting for array of coroutines to finish in [${Thread.currentThread().name}] Thread")

        println("\t==> Starting Coroutines with Flux...")

        val subscription = Flux.just(
            Tuples.of("C1", 2000L),
            Tuples.of("C2", 1500L),
            Tuples.of("C3", 1000L)
        )
            .subscribeOn(Schedulers.fromExecutor(subscribeExecutor))
            .map {
                println("\t\t==> executing coroutine $it in [${Thread.currentThread().name}] Thread")
                (async { executeCoroutineFunc(it.t1, it.t2) })
            }
            .flatMap {
                println("\t\t==> Calling Mono for waiting DeferredJob $it in [${Thread.currentThread().name}] Thread")
                Mono.fromCallable {
                    println("\t\t\t==> Waiting DeferredJob $it in [${Thread.currentThread().name}] Thread")
                    val result = runBlocking { it.await() }
                    println("\t\t\t==> Finished Waiting DeferredJob $it in [${Thread.currentThread().name}] Thread")
                    result
                }.publishOn(Schedulers.fromExecutor(publishExecutor))
            }
            .subscribe {
                println("\t==> subscribe RESULT ::: $it in [${Thread.currentThread().name}] Thread")
            }

        while (!subscription.isDisposed) {
            delay(10)
        }
        publishExecutor.shutdown()
        subscribeExecutor.shutdown()
        println("\t==> Waiting for Flux of coroutines to finish in [${Thread.currentThread().name}] Thread")

        println("Main program finishes in [${Thread.currentThread().name}] Thread")
    }
}

suspend fun executeCoroutineFunc(name: String = "C0", delayedTime: Long = 1000): String {
    println("\t==> $name Coroutine starts in [${Thread.currentThread().name}] Thread")
    delay(delayedTime)
    println("\t<== $name Coroutine finishes in [${Thread.currentThread().name}] Thread")

    return "Coroutine $name Result::: ${UUID.randomUUID()}"
}