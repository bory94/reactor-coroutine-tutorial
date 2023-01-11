package com.bory.reactor.tutorial.flux

import com.bory.reactor.tutorial.coroutine.log
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers
import java.lang.Thread.sleep
import java.time.Duration

fun main() {
    val scheduler = Schedulers.boundedElastic()

    val flux = Flux.just(1, 2, 3, 4, 5, 6, 7, 8)

    // doOnNext is invoked first, then subscribe is invoked
    flux.doOnNext { log("doOnNext ::: $it") }
        .subscribe { log("subscribe ::: $it") }

    log("---------------------------------------------")
    flux
        .publishOn(scheduler)
        .delayElements(Duration.ofMillis(100))
        .subscribeOn(scheduler)
        .subscribe(CustomSubscriber<Int>("SUB-1"))

    flux
        .publishOn(scheduler)
        .delayElements(Duration.ofMillis(100))
        .subscribeOn(scheduler)
        .subscribe(CustomSubscriber<Int>("SUB-2"))

    scheduler.disposeGracefully().subscribe()

    sleep(1000)
}

/* do not self-implement Subscribers */
class CustomSubscriber<T>(private val name: String = "Subscriber") : Subscriber<T> {
    private var subscription: Subscription? = null

    override fun onSubscribe(s: Subscription?) {
        log("$name:::CustomSubscriber#onSubscribe ::: $s")
        subscription = s
        subscription?.request(20)
    }

    override fun onError(t: Throwable?) {
        log("$name:::CustomSubscriber#onError ::: $t")
    }

    override fun onComplete() {
        log("$name:::CustomSubscriber#onComplete")
    }

    override fun onNext(t: T) {
        log("$name:::CustomSubscriber#onNext ::: $t")
        subscription?.request(3)
    }

}