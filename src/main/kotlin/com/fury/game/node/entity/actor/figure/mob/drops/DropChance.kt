package com.fury.game.node.entity.actor.figure.mob.drops

import com.fury.core.model.node.entity.actor.figure.player.Player

enum class DropChance private constructor(private val random: Int) {
    ALWAYS(0),
    ALMOST_ALWAYS(2),
    VERY_COMMON(5),
    COMMON(10),
    SEMI_COMMON(20),
    UNCOMMON(50),
    SEMI_RARE(75),
    QUITE_RARE(100),
    RARE(250),
    RARER(425),
    RATHER_RARE(500),
    VERY_RARE(750),
    EXTREMELY_RARE(1000),
    QUITE_LEGENDARY(1250),
    ALMOST_LEGENDARY(1500),
    LEGENDARY(3500),
    INSANE(7500);

    fun getRandom(player: Player): Int {
        return Math.round(this.random * (2.0 - player.dropRate)).toInt()
    }
}