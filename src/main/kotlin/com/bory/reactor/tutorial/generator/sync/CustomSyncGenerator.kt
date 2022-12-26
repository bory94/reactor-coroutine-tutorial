package com.bory.reactor.tutorial.generator.sync

import reactor.core.publisher.Flux
import reactor.core.publisher.SynchronousSink
import java.util.concurrent.Callable
import java.util.function.BiFunction

interface CustomSyncGenerator<S, T> {
    fun initialStateProvider(): Callable<S>
    fun generatorFunction(): BiFunction<S, SynchronousSink<T>, S>
    fun generate(limit: Long): Flux<T> =
        Flux.generate(initialStateProvider(), generatorFunction())
            .take(limit)
}