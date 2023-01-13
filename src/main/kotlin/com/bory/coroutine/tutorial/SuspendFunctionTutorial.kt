package com.bory.coroutine.tutorial

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    var who = "me before bath"
    log("Start Boiling water...")
    val deferredWater = async { boilWater(who) }

    log("Start bath...")
    who = async { bathTime(who) }.await()

    makingCoffee(deferredWater.await(), who)
}

suspend fun bathTime(who: String): String {
    log("$who is in the bath tub ...")
    delay(1000)
    val returningWho = "me after bath"
    log("Bath time finished. Who is now $returningWho")
    return returningWho
}

suspend fun boilWater(who: String): String {
    log("Coffee port has been turned on by $who ...")
    delay(200)
    log("water has been boiled.")

    return "Boiled Water"
}

suspend fun makingCoffee(water: String, who: String) {
    log("Start making coffee with $water by $who")
    delay(10)
    log("Coffee has been made.")
}