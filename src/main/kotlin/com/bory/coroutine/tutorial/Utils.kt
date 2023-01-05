package com.bory.reactor.tutorial.coroutine

import java.time.LocalDateTime

fun log(message: String) {
    println("[${LocalDateTime.now()} ${Thread.currentThread().name}] ::: $message")
}