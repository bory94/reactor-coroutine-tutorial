package com.bory.reactor.tutorial.flatmap

import com.bory.reactor.tutorial.generator.sync.FibonacciSyncGenerator
import reactor.core.publisher.Mono
import java.lang.Thread.sleep
import java.time.Duration

fun main() {
    // flatMap preserves sequence of launches and emissions
    val subscription = FibonacciSyncGenerator().generate(10)
        .concatMap {
            Mono.just("FM$it")
                .delayElement(Duration.ofMillis(if (it % 2 == 0) 20 else 10))
                .doOnNext { s -> println("Emitting $s") }
        }
        .subscribe { println(it) }

    while (!subscription.isDisposed) {
        sleep(10)
    }
}