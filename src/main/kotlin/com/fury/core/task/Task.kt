package com.fury.core.task

import java.util.*

abstract class Task {

    /** If the task fires immediately after schedule **/
    val instant: Boolean

    /** Game tick delay between cycles **/
    private val delay: Int

    /** Task is running **/
    var running: Boolean = false

    /** Counter for first delay **/
    private var counter: Int = 0

    /** Optional identifier **/
    var key = Optional.empty<Any>()

    constructor(delay: Int) : this(false, delay)

    @JvmOverloads
    constructor(instant: Boolean = false, delay: Int = 1) {
        this.instant = if (delay <= 0) true else instant
        this.delay = if (delay <= 0) 1 else delay
    }


    internal fun process() {
        if (++counter >= delay && running) {
            run()
            counter = 0
        }
    }

    abstract fun run()

    @JvmOverloads
    fun stop(logout: Boolean = false) {
        if (running) {
            onCancel(logout)
            onStop()
            running = false
        }
    }

    /** start  */
    open fun onSchedule() {}

    /** Can start  */
    fun canSchedule(): Boolean {
        return true
    }

    /** Executed on cancel  */
    open fun onCancel(logout: Boolean) {

    }

    open fun onStop() {
        //TODO temp remove/replace onCancel
    }


    /** Executed on exceptions.  */
    internal open fun onException(e: Exception) {}

    open fun canRun(): Boolean {
        return true
    }

    /** Attaches a new key.  */
    fun attach(newKey: Any): Task {
        key = Optional.ofNullable(newKey)
        return this
    }
}