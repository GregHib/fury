package com.fury.game.npc.minigames.pest

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.game.content.global.minigames.impl.PestControl
import com.fury.game.node.entity.actor.figure.player.handles.Settings
import com.fury.game.node.entity.mob.combat.PestMonstersCombat
import com.fury.game.world.map.Position
import com.fury.util.Misc
import java.util.*

open class PestMonsters(id: Int, tile: Position, spawned: Boolean, var portalIndex: Int, var manager: PestControl) : Mob(id, tile, spawned) {

    override val possibleTargets: ArrayList<Figure>
        get() {
            val possibleTarget = ArrayList<Figure>()
            val players = getRegion().getPlayers(z)
            for (player in players) {
                if (player == null || player.isDead || player.finished || !player.settings.getBool(Settings.RUNNING)
                        || !player.isWithinDistance(this, 10))
                    continue
                possibleTarget.add(player)
            }
            if (possibleTarget.isEmpty() || Misc.random(3) == 0) {
                possibleTarget.clear()
                possibleTarget.add(manager.knight)
            }
            return possibleTarget
        }

    init {
        forceAggressive = true
        forceTargetDistance = 70
    }

    override fun processNpc() {
        super.processNpc()
        if (!mobCombat!!.underCombat())
            checkAggression()
    }
}
