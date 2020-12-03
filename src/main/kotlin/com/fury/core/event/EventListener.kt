package com.fury.core.event

interface EventListener {

    fun accept(event: Event) {}

}