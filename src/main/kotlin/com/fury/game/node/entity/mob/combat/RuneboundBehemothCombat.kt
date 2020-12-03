package com.fury.game.node.entity.mob.combat

import com.fury.game.entity.character.combat.Hit
import com.fury.game.entity.character.npc.impl.dungeoneering.RuneboundBehemoth

class RuneboundBehemothCombat(private val mob: RuneboundBehemoth) : DungeonBossCombat(mob) {
    override fun applyHit(hit: Hit) {
        if (hit.damage > 0)
            mob.reduceHit(hit)
        super.applyHit(hit)
    }
}