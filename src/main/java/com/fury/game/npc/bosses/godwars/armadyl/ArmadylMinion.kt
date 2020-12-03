package com.fury.game.npc.bosses.godwars.armadyl

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.npc.bosses.godwars.zaros.ZarosMinion
import com.fury.game.world.map.Position
import java.util.*

class ArmadylMinion(id: Int, position: Position, spawned: Boolean) : Mob(id, position, spawned) {

    override val possibleTargets: ArrayList<Figure>
        get() {
            return if (!isWithinDistance(Position(2881, 5310, 2), 200))
                super.possibleTargets
            else {
                val targets = getPossibleTargets(true, true)
                val targetsCleaned = ArrayList<Figure>()
                for (t in targets) {
                    if (t is ArmadylMinion || t.isPlayer() && hasGodItem(t as Player) || t.isFamiliar)
                        continue
                    targetsCleaned.add(t)
                }
                targetsCleaned
            }
        }


    private fun hasGodItem(player: Player): Boolean {
        for (item in player.equipment.items) {
            if (item == null)
                continue
            val name = item.name.toLowerCase()
            if (name.contains("armadyl") || name.contains("book of law") || ZarosMinion.isNexArmour(name))
                return true
        }
        return false
    }
}
