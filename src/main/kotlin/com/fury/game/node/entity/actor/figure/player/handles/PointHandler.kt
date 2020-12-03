package com.fury.game.node.entity.actor.figure.player.handles

import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.content.events.daily.DailyEventManager
import com.fury.game.node.entity.actor.figure.player.Points

class PointHandler(private val player: Player) {

    private val points = mutableMapOf<Points, Double>()

    init {
        for(point in Points.values())
            points[point] = point.value
    }

    fun get(type: Points) : Double {
        return points.getOrPut(type, { type.value })
    }

    fun getInt(type: Points) : Int {
        return points.getOrPut(type, { type.value }).toInt()
    }
    fun add(type: Points, amount: Int) {
        points[type] = DailyEventManager.getPointsAdded(type, get(type) + amount)

        if(type == Points.DONATED)//TODO better way of handling
            player.bank.refreshCapacity()
    }

    fun add(type: Points, amount: Double) {
        points[type] = DailyEventManager.getPointsAdded(type, get(type) + amount)

        if(type == Points.DONATED)//TODO better way of handling
            player.bank.refreshCapacity()
    }

    fun set(type: Points, amount: Double) {
        points[type] = amount
    }

    fun remove(type: Points, amount: Int) {
        points[type] = DailyEventManager.getPointsRemoved(type, get(type) - amount)
    }

    fun remove(type: Points, amount: Double) {
        points[type] = DailyEventManager.getPointsRemoved(type, get(type) - amount)
    }

    fun has(type: Points, amount: Double): Boolean {
        return get(type) >= DailyEventManager.getPointsCheck(type, amount)
    }

    fun reset(type: Points) {
        set(type, type.value)
    }

    fun getAll(): Map<Points, Double> {
        return points
    }
}