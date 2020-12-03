package com.fury.game.entity.character.npc.impl.queenblackdragon


import com.fury.cache.Revision
import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.core.task.Task
import com.fury.game.entity.character.combat.CombatIcon
import com.fury.game.entity.character.combat.Hit
import com.fury.game.node.entity.mob.combat.TorturedSoulCombat
import com.fury.game.world.GameWorld
import com.fury.game.world.map.Position
import com.fury.game.world.update.flag.block.Animation
import com.fury.game.world.update.flag.block.HitMask
import com.fury.game.world.update.flag.block.graphic.Graphic
import com.fury.util.Misc

/**
 * Represents a Tortured soul.
 *
 * @author Emperor
 */
class TorturedSoul
/**
 * Constructs a new `TorturedSoul` `Object`.
 *
 * @param dragon The queen black dragon reference.
 * @param victim The player victim.
 * @param spawn  The spawn location.
 */
(
        /**
         * The queen black dragon reference.
         */
        private val dragon: QueenBlackDragon,
        /**
         * The player victim.
         */
        private val victim: Player, spawn: Position) : Mob(15510, spawn, Revision.PRE_RS3, false) {

    /**
     * If the Npc should skip a walk step.
     */
    private var skipWalkStep = true

    init {
        health.hitpoints = 2000
        combatDefinition.hitpoints = 2000
        walkType = 0
        mobCombat!!.target = victim
    }

    /**
     * Switches the walk step value and returns it.
     *
     * @return `True` if the npc should skip one movement tick.
     */
    fun switchWalkStep(): Boolean {
        skipWalkStep = !skipWalkStep
        return skipWalkStep
    }

    /**
     * Sends the special attack.
     */
    fun specialAttack(teleport: Position) {
        mobCombat!!.addCombatDelay(10)
        moveTo(teleport)
        perform(TELEPORT_GRAPHIC)
        perform(TELEPORT_ANIMATION)
        mobCombat!!.reset()
        GameWorld.schedule(1) {
            var diffX = x - victim.x
            var diffY = y - victim.y
            if (diffX < 0) {
                diffX = -diffX
            }
            if (diffY < 0) {
                diffY = -diffY
            }
            var offsetX = 0
            var offsetY = 0
            if (diffX > diffY) {
                offsetX = if (x - victim.x < 0) -1 else 1
            } else {
                offsetY = if (y - victim.y < 0) -1 else 1
            }
            if (victim.copyPosition().transform(offsetX, offsetY, 0).sameAs(this@TorturedSoul)) {
                offsetX = -offsetX
                offsetY = -offsetY
            }
            val currentX = offsetX + victim.x
            val currentY = offsetY + victim.y
            forceChat(FORCE_MESSAGES[Misc.random(FORCE_MESSAGES.size)])
            perform(SPECIAL_ATT_GFX)
            perform(SPECIAL_ATT_ANIM)
            mobCombat!!.target = victim
            GameWorld.schedule(object : Task(true) {
                internal var x = currentX
                internal var y = currentY
                override fun run() {
                    val current = Position(x, y, 1)
                    victim.packetSender.sendGraphic(SPECIAL_GRAPHIC, current)
                    var target: Figure? = null
                    for (soul in dragon.souls) {
                        if (soul.sameAs(current)) {
                            target = soul
                            break
                        }
                    }
                    if (target == null) {
                        for (worm in dragon.getWorms()) {
                            if (worm.sameAs(current)) {
                                target = worm
                                break
                            }
                        }
                    }
                    if (target == null && victim.sameAs(current)) {
                        target = victim
                    }
                    if (target != null) {
                        stop()
                        target.combat.applyHit(Hit(dragon, Misc.random(1000, 1500), HitMask.RED, CombatIcon.NONE))
                        return
                    }
                    if (x > victim.x) {
                        x--
                    } else if (x < victim.x) {
                        x++
                    }
                    if (y > victim.y) {
                        y--
                    } else if (y < victim.y) {
                        y++
                    }
                }
            })
        }
    }

    companion object {

        /**
         * The messages the Npc can say.
         */
        private val FORCE_MESSAGES = arrayOf("NO MORE! RELEASE ME, MY QUEEN! I BEG YOU!", "We lost our free will long ago...", "How long has it been since I was taken...", "The cycle is never ending, mortal...")

        /**
         * The teleport graphic.
         */
        val TELEPORT_GRAPHIC = Graphic(3147, Revision.PRE_RS3)

        /**
         * The teleport animation.
         */
        val TELEPORT_ANIMATION = Animation(16861, Revision.PRE_RS3)

        /**
         * The special attack graphic.
         */
        private val SPECIAL_GRAPHIC = Graphic(3146, Revision.PRE_RS3)

        /**
         * The special attack graphic.
         */
        private val SPECIAL_ATT_GFX = Graphic(3145, Revision.PRE_RS3)

        /**
         * The special attack animation.
         */
        private val SPECIAL_ATT_ANIM = Animation(16864, Revision.PRE_RS3)
    }
}