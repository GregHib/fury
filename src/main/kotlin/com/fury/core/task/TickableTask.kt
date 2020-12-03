package com.fury.core.task

abstract class TickableTask @JvmOverloads constructor(instant: Boolean = true, delay: Int = 1) : Task(instant, delay) {
    var tick = 0

    abstract fun tick()

    override fun run() {
        tick()
        tick++
    }
}