package com.bory.reactor.tutorial.generator.sync

import reactor.core.publisher.SynchronousSink
import reactor.util.function.Tuple2
import reactor.util.function.Tuples
import java.util.concurrent.Callable
import java.util.function.BiFunction

class FibonacciSyncGenerator : CustomSyncGenerator<Tuple2<Int, Int>, Int> {
    override fun initialStateProvider() = Callable { Tuples.of(1, 1) }

    override fun generatorFunction() =
        BiFunction<Tuple2<Int, Int>, SynchronousSink<Int>, Tuple2<Int, Int>> { state, sink ->
            sink.next(state.t1)
            Tuples.of(state.t2, state.t1 + state.t2)
        }
}
