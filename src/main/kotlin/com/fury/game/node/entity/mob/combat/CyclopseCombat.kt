package com.fury.game.node.entity.mob.combat

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.content.global.minigames.impl.WarriorsGuild
import com.fury.game.node.entity.actor.figure.mob.MobCombat
import com.fury.game.npc.minigames.Cyclopse
import com.fury.game.world.FloorItemManager
import com.fury.game.world.map.Position
import com.fury.util.Misc

class CyclopseCombat(private val mob: Cyclopse) : MobCombat(mob) {

    override fun sendDeath(source: Figure?) {
        super.sendDeath(source)
        if (source is Player) {
            if (Misc.getRandom(if (WarriorsGuild.getCurrentDefenderIndex(source) >= 6) 20 else 15) == 0)
                FloorItemManager.addGroundItem(Item(WarriorsGuild.getDefender(source)), Position(mob.getCoordFaceX(mob.sizeX), mob.getCoordFaceY(sizeY = mob.sizeY), mob.z), source)
        }
    }
}