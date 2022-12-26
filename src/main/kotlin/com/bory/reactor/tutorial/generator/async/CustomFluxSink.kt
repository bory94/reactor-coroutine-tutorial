package com.bory.reactor.tutorial.generator.async

import reactor.core.publisher.FluxSink
import java.util.function.Consumer

class CustomFluxSink : Consumer<FluxSink<String>> {
    lateinit var fluxSink: FluxSink<String>

    override fun accept(t: FluxSink<String>) {
        fluxSink = t
    }

    fun publishEvent(message: String) {
        fluxSink.next(message)
    }
}