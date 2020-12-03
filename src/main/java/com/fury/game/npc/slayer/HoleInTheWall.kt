package com.fury.game.npc.slayer

import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.content.skill.member.slayer.Slayer
import com.fury.game.entity.character.combat.CombatIcon
import com.fury.game.entity.character.combat.Hit
import com.fury.game.node.entity.mob.combat.HoleInTheWallCombat
import com.fury.game.world.GameWorld
import com.fury.game.world.map.Position
import com.fury.game.world.update.flag.block.HitMask
import com.fury.util.Misc

class HoleInTheWall(id: Int, position: Position, spawned: Boolean) : Mob(id, position, spawned) {

    var hasGrabbed: Boolean = false

    init {
        isCantFollowUnderCombat = true
        setCantInteract(true)
        forceAggressive = true
    }

    override fun processNpc() {
        super.processNpc()
        if (id == 2058) {
            if (!hasGrabbed) {
                for (figure in possibleTargets) {
                    if (figure == null || figure.isDead || !isWithinDistance(figure, 1))
                        continue
                    if (figure.isPlayer()) {
                        val player = figure as Player
                        player.movement.reset()
                        hasGrabbed = true
                        if (Slayer.hasSpinyHelmet(player)) {
                            setTransformation(7823)
                            animate(1805)
                            setCantInteract(false)
                            setTarget(player)
                            player.message("The spines on your helmet repel the beast's hand.")
                            return
                        }
                        animate(1802)
                        player.movement.lock(4)
                        player.animate(425)
                        player.message("A giant hand appears and grabs your head.")

                        GameWorld.schedule(5) {
                            player.combat.applyHit(Hit(player, Misc.getRandom(44), HitMask.RED, CombatIcon.NONE))
                            animate(-1)
                        }
                        GameWorld.schedule(25) { hasGrabbed = false }
                    }
                }
            }
        } else {
            if (!mobCombat!!.process()) {
                setCantInteract(true)
                setTransformation(2058)
            }
        }
    }
}
