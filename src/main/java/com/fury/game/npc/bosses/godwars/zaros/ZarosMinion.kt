package com.fury.game.npc.bosses.godwars.zaros

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.node.entity.mob.combat.ZarosMinionCombat
import com.fury.game.world.map.Position
import java.util.*

class ZarosMinion(id: Int, tile: Position) : Mob(id, tile) {

    override val possibleTargets: ArrayList<Figure>
        get() {
            val targets = getPossibleTargets(true, true)
            val targetsCleaned = ArrayList<Figure>()
            for (t in targets) {
                if (t is ZarosMinion || t.isFamiliar || hasSuperiourBonuses(t))
                    continue
                targetsCleaned.add(t)
            }
            return targetsCleaned
        }

    private fun hasSuperiourBonuses(t: Figure): Boolean {
        if (!t.isPlayer())
            return false
        val player = t as Player
        //        for (int bonus : BONUS_IDXS[getId() - 13456]) {
        //            if (player.getBonusManager().getAttackBonus()[bonus] >= (bonus == 9 ? 100 : CAP_BONUS))
        //                return true;
        //        }
        return false
    }

    companion object {

        private val CAP_BONUS = 200

        private val BONUS_IDXS = arrayOf(intArrayOf(5, 6, 7), intArrayOf(9), intArrayOf(), intArrayOf(8))

        fun isNexArmour(name: String): Boolean {
            return name.contains("pernix") || name.contains("torva") || name.contains("virtus") || name.contains("zaryte")
        }
    }
}
