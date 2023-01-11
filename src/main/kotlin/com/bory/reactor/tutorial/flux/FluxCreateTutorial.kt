package com.bory.reactor.tutorial.flux

import com.bory.reactor.tutorial.coroutine.log
import reactor.core.publisher.Flux
import java.lang.Thread.sleep
import java.time.Duration

fun main() {
    val createdFlux = Flux.create { sink ->
        var initial = 1
        var accumulated = 1
        while (accumulated < 100000) {
            sink.next(initial)
            val tmp = accumulated
            accumulated += initial
            initial = tmp
        }
        sink.complete()
    }

    val disposable1 = createdFlux
        .delayElements(Duration.ofMillis(50))
        .take(50)
        .subscribe { uuid -> log(">> SUB1 ::: $uuid") }

    val disposable2 = createdFlux
        .delayElements(Duration.ofMillis(100))
        .take(25)
        .subscribe { uuid -> log(">> SUB2 ::: $uuid") }

    log("Finished....")

    createdFlux.take(10).subscribe { uuid -> log(">> SUB3 ::: $uuid") }

    while (!disposable1.isDisposed) {
        sleep(10)
    }
    while (!disposable2.isDisposed) {
        sleep(10)
    }
}
