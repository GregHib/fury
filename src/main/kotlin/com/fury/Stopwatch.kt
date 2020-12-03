package com.fury

import java.util.concurrent.TimeUnit

class Stopwatch {
    companion object {
        @JvmStatic
        fun start(): Stopwatch {
            return Stopwatch().reset()
        }
    }

    private var time = 0L

    fun reset(): Stopwatch {
        time = System.nanoTime()
        return this
    }

    fun reset(t: Long): Stopwatch {
        time = t
        return this
    }

    fun reset(delay: Long, unit: TimeUnit): Stopwatch {
        time = System.nanoTime() + TimeUnit.NANOSECONDS.convert(delay, unit)
        return this
    }

    fun elapsedTime(unit: TimeUnit): Long {
        return unit.convert(System.nanoTime() - time, TimeUnit.NANOSECONDS)
    }

    fun elapsed(): Long {
        return elapsedTime(TimeUnit.MILLISECONDS)
    }

    fun elapsed(time: Long, unit: TimeUnit): Boolean {
        return elapsedTime(unit) >= time
    }

    fun elapsed(time: Long): Boolean {
        return elapsed(time, TimeUnit.MILLISECONDS)
    }

}