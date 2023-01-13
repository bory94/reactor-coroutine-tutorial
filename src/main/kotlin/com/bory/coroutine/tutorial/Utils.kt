package com.bory.coroutine.tutorial

import java.time.LocalDateTime

fun log(message: String) {
    println("[${LocalDateTime.now()} ${Thread.currentThread().name}] ::: $message")
}