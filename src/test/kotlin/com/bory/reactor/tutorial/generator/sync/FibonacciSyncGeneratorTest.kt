package com.bory.reactor.tutorial.generator.sync

import org.junit.jupiter.api.Test
import reactor.test.StepVerifier
import java.time.Duration

class FibonacciSyncGeneratorTest {
    @Test
    fun testFibonacciGenerator() {
        val fibonacciFlux =
            FibonacciSyncGenerator().generate(5)
                .delayElements(Duration.ofMillis(50))

        StepVerifier.withVirtualTime { fibonacciFlux }
            .expectSubscription()
            .expectNext(1)
            .expectNoEvent(Duration.ofMillis(50))
            .expectNext(1)
            .expectNoEvent(Duration.ofMillis(50))
            .expectNext(2)
            .expectNoEvent(Duration.ofMillis(50))
            .expectNext(3)
            .expectNoEvent(Duration.ofMillis(50))
            .expectNext(5)
            .expectComplete().verify()
    }
}