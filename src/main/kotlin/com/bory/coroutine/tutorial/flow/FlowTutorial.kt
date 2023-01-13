package com.bory.coroutine.tutorial.flow

import com.bory.coroutine.tutorial.log
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.*
import kotlin.random.Random

@OptIn(DelicateCoroutinesApi::class)
fun main(): Unit = runBlocking {
    val emitterDispatcher = newFixedThreadPoolContext(3, "EmitterDispatcher")
    val subscriberDispatcher = newFixedThreadPoolContext(5, "SubscriberDispatcher")

    val flow = flow {
        (1..20).forEach { i ->
            val message = createRandomMessage(i)
            log("==> Emitting message ::: $message")
            emit(message)
        }
    }
        .buffer()
        .flowOn(emitterDispatcher)


    launch(subscriberDispatcher) {
        flow.collect { message ->
            log("<== #1 ::: $message")
            log("-------------------------------------------------------------")
        }
    }

    launch(subscriberDispatcher) {
        flow.collect { message ->
            log("<== #2 ::: $message")
            log("-------------------------------------------------------------")
        }
    }
}


suspend fun createRandomMessage(i: Int): String {
    val delayTime = Random.nextLong(1000)
    delay(delayTime)
    val message = "Message-[$i]-[${delayTime}ms]-${UUID.randomUUID()}"

    log("==> Computed Random Message ::: $message")

    return message
}

