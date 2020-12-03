package com.fury.game.node.entity.mob.combat

import com.fury.game.entity.character.combat.Hit
import com.fury.game.node.entity.actor.figure.mob.MobCombat
import com.fury.game.npc.bosses.bork.DagonHaiElite

class DagonHaiEliteCombat(private val mob: DagonHaiElite) : MobCombat(mob) {
    override fun applyHit(hit: Hit) {
    }
}