package com.fury.game.content.combat

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.GameSettings
import com.fury.game.entity.character.combat.CombatConstants
import com.fury.game.node.entity.actor.figure.player.PlayerCombat
import com.fury.game.world.GameWorld
import com.fury.game.world.update.flag.block.Animation
import java.util.*

interface CombatSwing {

    fun run(figure: Figure, target: Figure): Boolean

    fun doDefenceEmote(target: Figure) {
        target.performAnimationNoPriority(Animation(CombatConstants.getDefenceEmote(target)))
    }

    fun addAttackedByDelay(player: Figure, target: Figure) {
        target.combat.attackedBy = player
        target.combat.attackedByDelay = GameSettings.COMBAT_DELAY//8 seconds
        player.combat.attackingDelay = GameSettings.COMBAT_DELAY
    }

    fun autoRetaliate(figure: Figure, target: Figure) {
        if (target is Player) {
            target.packetSender.sendInterfaceRemoval()
            if (target.isAutoRetaliate && !target.movement.hasWalkSteps())
                (target.combat as? PlayerCombat)?.target(figure, true)
        } else if(target is Mob) {
            if (!target.combat.isInCombat() || target.canBeAttackedByAutoRetaliate())
                target.setTarget(figure)
        }
    }


    fun getTargets(figure: Figure, target: Figure, multiple: Boolean): Array<Figure> {
        val possibleTargets = ArrayList<Figure>()
        if(multiple)
            possibleTargets.addAll(getMultipleTargets(figure, target))
        else
            possibleTargets.add(target)
        return possibleTargets.toTypedArray()
    }

    private fun getMultipleTargets(figure: Figure, target: Figure, maxDistance: Int = 1, maxTargetCount: Int = 9): ArrayList<Figure> {
        val possibleTargets = ArrayList<Figure>()
        if (target is Player) {
            for (local in GameWorld.regions.getLocalPlayers(figure)) {
                if (local == figure || local == target || local.isDead || !local.started || local.finished || !local.isCanPvp || !local.isWithinDistance(target, maxDistance) || (figure is Player && !figure.controllerManager.canHit(local)) || !figure.combat.clippedProjectile(local, false) || local.isCanPvp && !local.inMulti())
                    continue
                possibleTargets.add(local)
                if (possibleTargets.size == maxTargetCount)
                    break
            }
        } else {
            for (mob in GameWorld.regions.getLocalNpcs(figure)) {
                if (!mob.inMulti() || (figure is Player && mob == figure.familiar) || mob.isDead || mob.finished || !mob.isWithinDistance(target, maxDistance) || !mob.definition.hasAttackOption() || (figure is Player && !figure.controllerManager.canHit(mob)) || !figure.combat.clippedProjectile(mob, false))
                    continue
                possibleTargets.add(mob)
                if (possibleTargets.size == maxTargetCount)
                    break
            }
        }
        return possibleTargets
    }
}