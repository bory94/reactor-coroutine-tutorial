package com.bory.coroutine.tutorial.channel

import com.bory.reactor.tutorial.coroutine.log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

const val A_FINISHED = "A-Finished"
const val B_FINISHED = "B-Finished"

fun main(): Unit = runBlocking {
    val channel = Channel<String>()

    launch(Dispatchers.Default) {
        send(channel, "A1")
        delay(200)
        send(channel, "A2")
        send(channel, A_FINISHED)
        log("A done")
    }

    launch(Dispatchers.Default) {
        send(channel, "B1")
        delay(100)
        send(channel, "B2")
        send(channel, B_FINISHED)
        log("B done")
    }

    launch(Dispatchers.Default) {

        var aFinished = false
        var bFinished = false

        while (!(aFinished && bFinished)) {
            delay(200) // <-- All Producers wait for subscriber starting for 200 ms
            val received = channel.receive()
            log("<== Received::: $received")
            if (received == A_FINISHED) aFinished = true
            if (received == B_FINISHED) bFinished = true
        }

        // if you receive more message from channel, this line will be blocked and waiting
//        val isThereAnyRemainedMessage = channel.receive()
//        log(isThereAnyRemainedMessage)

        log("C done")
    }

}

suspend fun send(channel: Channel<String>, message: String) {
    log("==> Sending message: $message")

    channel.send(message)

    log("==> Message: $message sent")
}
