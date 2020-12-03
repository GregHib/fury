package com.fury.game.node.entity.actor.figure.player.handles

import com.fury.game.node.entity.actor.figure.player.Variables

abstract class VariableMap<T> {

    private val vars = mutableMapOf<T, Any>()

    private fun getValue(type: T): Any {
        return when(type) {
            is Variables -> type.value
            is Settings -> type.value
            else -> 0
        }
    }

    fun getInt(type: T): Int {
        return vars.getOrPut(type, { getValue(type) }) as? Int ?: (vars[type] as Double).toInt()
    }

    fun getLong(type: T): Long {
        return vars.getOrPut(type, { getValue(type) }) as? Long ?: (vars[type] as Double).toLong()
    }

    fun getDouble(type: T): Double {
        return vars.getOrPut(type, { getValue(type) }) as Double
    }

    fun getBool(type: T): Boolean {
        return vars.getOrPut(type, { getValue(type) }) as Boolean
    }

    fun add(type: T, amount: Double) {
        vars[type] = getDouble(type) + amount
    }

    fun add(type: T, amount: Int) {
        vars[type] = getInt(type) + amount
    }

    fun remove(type: T, amount: Double) {
        vars[type] = getDouble(type) - amount
    }

    fun remove(type: T, amount: Int) {
        vars[type] = getInt(type) - amount
    }

    open fun set(type: T, value: Any) {
        vars[type] = value
    }

    fun reset(type: T) {
        set(type, getValue(type))
    }

    fun getAll(): Map<T, Any> {
        return vars
    }
}