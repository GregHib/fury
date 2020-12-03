package com.fury.game.node.entity.mob.combat

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.game.entity.character.npc.impl.dungeoneering.DungeonSkeletonBoss

class DungeonSkeletonBossCombat(private val mob: DungeonSkeletonBoss): DungeonMobCombat(mob) {
    override fun sendDeath(source: Figure?) {
        super.sendDeath(source)
        mob.boss.removeSkeleton(mob)
    }
}