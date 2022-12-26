package com.bory.reactor.tutorial.generator.sync

import org.junit.jupiter.api.Test
import reactor.test.StepVerifier

class SequenceSyncGeneratorTest {
    @Test
    fun testSequenceGenerator() {
        val expectedValues = Array(10) { it }
        val sequenceFlux = SequenceSyncGenerator().generate(10)

        StepVerifier.create(sequenceFlux)
            .expectSubscription()
            .expectNext(*expectedValues)
            .expectComplete()
            .verify()
    }
}