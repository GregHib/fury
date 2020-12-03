package com.fury.game.node.entity.actor.figure.player.misc

import com.fury.Stopwatch

class Stopwatches {
    val storeClaim = Stopwatch()
    val lastRunRecovery = Stopwatch()//TODO convert to some other way?
    val clickDelay = Stopwatch()
    val itemPickup = Stopwatch()
    val yell = Stopwatch()
    val login = Stopwatch()
    val lastVengeance = Stopwatch()
    val potDelay = Stopwatch()
    val foodDelay = Stopwatch()
    val chargeSpell = Stopwatch()
    val pickpocketMark = Stopwatch()
}