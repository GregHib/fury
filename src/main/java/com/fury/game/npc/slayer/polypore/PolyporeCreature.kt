package com.fury.game.npc.slayer.polypore

import com.fury.cache.Revision
import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.content.dialogue.impl.items.NeemDrupeSquish
import com.fury.game.node.entity.actor.`object`.ObjectHandler
import com.fury.game.node.entity.mob.combat.PolyporeCreatureCombat
import com.fury.game.world.GameWorld
import com.fury.game.world.map.Position
import com.fury.game.world.map.path.RouteEvent
import com.fury.game.world.update.flag.block.Animation
import com.fury.game.world.update.flag.block.graphic.Graphic

class PolyporeCreature(private val realId: Int, tile: Position, revision: Revision, spawned: Boolean) : Mob(realId, tile, revision, spawned) {

    val infectEmote: Int
        get() {
            return when (realId) {
                14688 -> 15484
                14690 -> 15507
                14692 -> 15514
                14696 -> 15466
                14698 -> 15477
                14700 -> 15492
                else -> -1
            }
        }

    init {
        walkType = if (realId == 14694) Mob.Companion.NORMAL_WALK or Mob.Companion.FLY_WALK else Mob.Companion.NORMAL_WALK
    }

    fun useOil(player: Player) {
        if (id != realId)
            return
        player.movement.lock(1)
        player.perform(Graphic(2014, Revision.PRE_RS3))
        player.animate(9954)
        setTransformation(realId + 1, Revision.PRE_RS3)
        forceChat("Ssss!")
        if (mobCombat!!.target == null)
            setTarget(player)
        NeemDrupeSquish.removeCharge(player)
        player.message("You throw some of the oil at the creature weakening it slightly.")
    }

    override fun reset() {
        setTransformation(realId)
        super.reset()
    }

    fun canInfect(): Boolean {
        return realId == id
    }

    companion object {

        fun useStairs(player: Player, tile: Position, down: Boolean) {
            ObjectHandler.useStairs(player, if (down) 15458 else 15456, tile, 2, 3) //TODO find correct emote
            GameWorld.schedule(1) { player.perform(Animation(if (down) 15459 else 15457, Revision.PRE_RS3)) }
        }

        fun sprinkleOil(player: Player, target: Mob?) {
            if (target == null) {
                for (n in GameWorld.regions.getLocalNpcs(player)) {
                    if (n == null || n.isDead || n.finished || n.z != player.z || n.mobCombat!!.target !== player || n !is PolyporeCreature || n.realId != n.id)
                        continue
                    player.routeEvent = RouteEvent(n, {
                        player.direction.face(n)
                        sprinkleOil(player, n)
                    }, false)
                    return
                }
                player.message("There are no suitable targets nearby.")
                return
            } else {
                if (target !is PolyporeCreature || target.isDead || target.finished)
                    return
                if (target.realId != target.id) {
                    player.message("That creature has already been weakened.")
                    return
                }
            }
            target.useOil(player)
        }
    }
}
