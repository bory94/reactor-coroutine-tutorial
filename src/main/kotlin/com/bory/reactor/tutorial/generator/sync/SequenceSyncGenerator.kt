package com.bory.reactor.tutorial.generator.sync

import reactor.core.publisher.SynchronousSink
import java.util.concurrent.Callable
import java.util.function.BiFunction

class SequenceSyncGenerator : CustomSyncGenerator<Int, Int> {
    override fun initialStateProvider() = Callable { 0 };

    override fun generatorFunction() = BiFunction<Int, SynchronousSink<Int>, Int> { state, sink ->
        sink.next(state)
        state + 1
    }
}
