package com.bory.reactor.tutorial.generator.async

import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink
import java.lang.Thread.sleep
import java.time.Duration

class ConcurrentFluxCreator {
}

fun main() {
    val sink = CustomFluxSink()

    val flux = Flux.create(sink, FluxSink.OverflowStrategy.DROP).cache()

    val thread1 = StringEmittingThread("T200", sink, 200)
    val thread2 = StringEmittingThread("T600", sink, 600)
    val thread3 = StringEmittingThread("T800", sink, 800)

    println("Start emitting...")

    thread1.start()
    thread2.start()
    thread3.start()

    val startTime = System.nanoTime()

    flux.delayElements(Duration.ofMillis(100))
        .subscribe { println("S100 ::: $it ::: ${calculateElapsedTime(startTime)}") }
    flux.delayElements(Duration.ofMillis(500))
        .subscribe { println("S500 ::: $it ::: ${calculateElapsedTime(startTime)}") }
    flux.delayElements(Duration.ofMillis(700))
        .subscribe { println("S700 ::: $it ::: ${calculateElapsedTime(startTime)}") }

    thread1.join()
    thread2.join()
    thread3.join()

    sleep(15000)
}

fun calculateElapsedTime(startTime: Long) = "(${(System.nanoTime() - startTime) / 1_000_000}ms)"

class StringEmittingThread(
    name: String,
    private val sink: CustomFluxSink,
    private val delay: Long
) :
    Thread(name) {
    override fun run() {
        (1..10).forEach { sleep(delay); sink.publishEvent("$name - $it") }
    }
}