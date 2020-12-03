package com.fury.core.model.node.entity.actor.figure.combat.magic

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.Projectile
import com.fury.core.model.node.entity.actor.figure.combat.magic.context.SpellOnFigureContext
import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.container.impl.equip.Slot
import com.fury.game.entity.character.combat.magic.CombatSpells
import com.fury.game.node.entity.actor.figure.ProjectileManager
import com.fury.game.world.update.flag.block.Animation
import com.fury.game.world.update.flag.block.graphic.Graphic
import java.util.*

/**
 * A [Spell] implementation used for combat related spells.
 *
 * @author lare96
 * @author Greg
 */
abstract class CombatSpell : Spell() {

    val name: String
        get() = CombatSpells.getName(this)

    open val maxHit = 0

    open val animation = Optional.empty<Animation>()

    open val staffAnimation = Optional.empty<Animation>()

    open val castGraphic = Optional.empty<Graphic>()

    open val castStaffGraphic = Optional.empty<Graphic>()

    open val hitGraphic = Optional.empty<Graphic>()

    open val splashGraphic = Optional.of(Graphic(85, 0, 100))

    open val projectile = Optional.empty<Array<Projectile>>()

    open val attackDelay = 5

    open fun canCastProjectile(figure: Figure, target: Figure): Boolean {
        return projectile.isPresent
    }

    open fun getMinHit(figure: Figure, target: Figure): Int {
        return 0
    }

    open fun getMaxHit(figure: Figure, target: Figure?): Int {
        return maxHit
    }

    open fun getHitDelay(figure: Figure): Int {
        return attackDelay
    }

    override fun finishCast(figure: Figure, context: SpellContext) {
        //TODO temp
        if(context is SpellOnFigureContext)
            finishCast(figure, context.target, context.accurate, context.damage)
    }

    protected abstract fun finishCast(figure: Figure, target: Figure, accurate: Boolean, damage: Int)

    override fun startCast(figure: Figure, context: SpellContext) {
        if(context is SpellOnFigureContext) {
            val target = context.target

            //Animate
            if (figure is Mob)
                figure.animate(figure.combatDefinition.attackAnim)
            else if (figure is Player) {
                if (staffAnimation.isPresent && figure.equipment.get(Slot.WEAPON).name.contains("staff"))
                    staffAnimation.ifPresent(figure::perform)
                else
                    animation.ifPresent(figure::perform)
            }

            //Graphic
            if (figure is Mob)
                castGraphic.ifPresent(figure::perform)//figure.combatDefinition.attackGraphic?
            else if (figure is Player) {
                if (castStaffGraphic.isPresent && figure.equipment.get(Slot.WEAPON).name.contains("staff"))
                    castStaffGraphic.ifPresent(figure::perform)
                else
                    castGraphic.ifPresent(figure::perform)
            }
            //Projectiles
            if (canCastProjectile(figure, target)) {
                projectile.ifPresent {
                    it.forEach { projectile -> ProjectileManager.send(projectile.setPositions(figure, target)) }
                }
            }
        }
    }

    override fun canCast(figure: Figure, context: SpellContext): Boolean {
        if(context is SpellOnFigureContext) {
            val target = context.target
            if(target is Mob)
                return target.definition.hasAttackOption()
        }
        return super.canCast(figure, context)
    }
}