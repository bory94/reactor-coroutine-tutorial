package com.bory.reactor.tutorial.flux

import org.reactivestreams.Publisher
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription

fun main() {
    val publisher = CustomPublisher(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

    publisher.subscribe(CustomSubscriber())
}

class CustomPublisher(vararg n: Int) : Publisher<Int> {
    val elements: MutableList<Int> = mutableListOf()

    init {
        n.forEach(elements::add)
    }

    override fun subscribe(s: Subscriber<in Int>?) {
        s?.onSubscribe(CustomSubscription(this, s))
    }
}

class CustomSubscription(
    private val publisher: CustomPublisher,
    private val subscriber: Subscriber<in Int>
) : Subscription {
    private var currentIndex = 0L
    private var subscriptionCompleted = false

    private fun checkAndCompleteSubscription(): Boolean =
        if (currentIndex >= publisher.elements.size) {
            if (!subscriptionCompleted) subscriber.onComplete()
            subscriptionCompleted = true
            true
        } else false

    override fun request(n: Long) {
        for (i in currentIndex until currentIndex + n) {
            if (checkAndCompleteSubscription()) {
                break
            }

            currentIndex += 1
            subscriber.onNext(publisher.elements[i.toInt()])
        }
    }

    override fun cancel() {
        subscriber.onComplete()
    }

}