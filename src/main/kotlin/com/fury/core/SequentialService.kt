package com.fury.core

interface SequentialService {

    fun start()

    fun execute()

    fun end()

    fun run() {
        start()
        execute()
        end()
    }

}