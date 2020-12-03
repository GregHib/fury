package com.fury.game.npc.bosses.glacors

import com.fury.cache.Revision
import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.game.entity.character.combat.CombatIcon
import com.fury.game.entity.character.combat.Hit
import com.fury.game.world.map.Position
import com.fury.game.world.update.flag.block.HitMask
import com.fury.game.world.update.flag.block.graphic.Graphic

open class Glacyte(var glacor: Glacor?, id: Int, tile: Position) : Mob(id, tile, Revision.PRE_RS3, true) {
    var effect: Byte = 0
    private var explosionTicks: Byte = 0
    private val isGlacor: Boolean
    var targetIndex: Int = 0
    var mainTarget: Int = 0

    init {
        capDamage = 900
        isForceMultiAttacked = true
        isGlacor = id == 14301
        if (!isGlacor) {
            effect = (id - 14302).toByte()
            if (combatDefinition.isAggressive)
                mobCombat!!.target = glacor!!.mobCombat!!.target
        }
        explosionTicks = 25
        targetIndex = -1
        mainTarget = -1
    }

    /**
     * Effects go as this, 0 - explosion, 1 - sap prayer , 2 - endurance
     */
    override fun processNpc() {
        super.processNpc()

        if (isGlacor)
            return

        val target = mobCombat!!.target ?: return

        if (effect.toInt() == 0) {
            explosionTicks--
            if (explosionTicks <= 0) {
                explosionTicks = 25
                val tile = this
                for (e in possibleTargets) {
                    if (e == null || e.isDead || !e.isWithinDistance(tile, if (isGlacor) 3 else 1))
                        continue
                    e.combat.applyHit(Hit(target, e.health.hitpoints / 3, HitMask.RED, CombatIcon.NONE))
                }
                combat.applyHit(Hit(target, (health.hitpoints * .9).toInt(), HitMask.RED, CombatIcon.DEFLECT))
                perform(Graphic(956, Revision.PRE_RS3))
            } else {
                if (explosionTicks >= 20) {
                    // just so it can delay healing a little
                } else if (explosionTicks >= 13) {
                    health.heal((maxConstitution * .05).toInt())
                } //else if (explosionTicks == 12)
                //                    setNextSecondaryBar(new SecondaryBar(0, 350, 1, false));
            }
        }
    }


    override fun equals(other: Any?): Boolean {
        return other is Glacyte && other.id == id && super.equals(other);
    }
}
