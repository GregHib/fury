package com.fury.game.node.entity.mob.combat

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.content.controller.impl.ZarosGodwars
import com.fury.game.entity.character.combat.Hit
import com.fury.game.node.entity.actor.figure.mob.MobCombat
import com.fury.game.npc.bosses.nex.NexMinion

class NexMinionCombat(private val mob: NexMinion): MobCombat(mob) {
    override fun handleHit(hit: Hit) {
        if (!mob.isBarrierBroken) {
            mob.graphic(1549)
            if (hit.source.isPlayer())
                (hit.source as Player).message("The avatar is not weak enough to damage this minion.")
            hit.damage = 0
        } else
            super.handleHit(hit)
    }

    override fun sendDeath(source: Figure?) {
        super.sendDeath(source)
        ZarosGodwars.moveNextStage()
    }
}