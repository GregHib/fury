package com.fury.game.content.tasks

import com.fury.core.task.Task
import com.fury.game.entity.character.combat.equipment.weapon.CombatSpecial
import com.fury.game.node.entity.actor.figure.player.handles.Settings
import com.fury.game.world.GameWorld

class SpecialEnergyRestoreTask : Task(false, 50) {//30 seconds

    override fun run() {
        GameWorld.players.forEach { player ->
            if (player.familiar != null)
                player.familiar!!.restoreSpecialEnergy(15)

            if (!player.isDead && player.settings.getInt(Settings.SPECIAL_ENERGY) != 100)
                CombatSpecial.restore(player, if (player.isInWilderness || player.isCanPvp) 10 else player.gameMode.specRestore, true)
        }
    }

}