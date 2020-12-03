package com.fury.game.content.misc.objects

import com.fury.core.model.node.entity.actor.figure.combat.magic.context.SpellOnObjectContext
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.content.combat.magic.spell.modern.skill.charge.ChargeOrbSpell
import com.fury.game.content.global.action.Action
import com.fury.game.entity.`object`.GameObject
import com.fury.game.node.entity.actor.`object`.ObjectManager

class OrbCharging(val spell: ChargeOrbSpell, private val obelisk: GameObject, var amount: Int) : Action() {

    val context = SpellOnObjectContext(obelisk)

    override fun start(player: Player): Boolean {
        return checkAll(player)
    }

    override fun process(player: Player): Boolean {
        return amount > 0 && checkAll(player)
    }

    override fun processWithDelay(player: Player): Int {
        if(amount > 0) {
            spell.cast(player, context)
            return 2
        }
        return -1
    }

    override fun stop(player: Player) {
        setActionDelay(player, 3)
    }

    private fun checkAll(player: Player): Boolean {
        return spell.canCast(player, context) && ObjectManager.containsObjectWithId(obelisk, obelisk.id)
    }

}