package com.fury.game.content.combat.magic.spell

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.Projectile
import com.fury.core.model.node.entity.actor.figure.SpeedProjectile
import com.fury.core.model.node.entity.actor.figure.combat.magic.SpellContext
import com.fury.core.model.node.entity.actor.figure.combat.magic.context.SpellOnFigureContext
import com.fury.core.model.node.entity.actor.figure.combat.magic.spell.CombatEffectSpell
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.entity.character.combat.effects.Effect
import com.fury.game.entity.character.combat.effects.Effects
import com.fury.game.world.update.flag.block.Animation
import com.fury.game.world.update.flag.block.graphic.Graphic
import java.util.*

class Teleblock : CombatEffectSpell() {
    override val maxHit = 30
    override val animation = Optional.of(Animation(10503))
    override val castGraphic = Optional.of(Graphic(1841))
    override val hitGraphic = Optional.of(Graphic(1843))
    override val projectile = Optional.of(arrayOf(SpeedProjectile(1842, 30, 26, 52, 0, 0) as Projectile))
    override val id = 62118
    override val level = 85
    override val experience = 80.0
    override val items = Optional.of(arrayOf(Item(563, 1), Item(562, 1), Item(560, 1)))

    override fun canCast(figure: Figure, context: SpellContext): Boolean {
        if(figure is Player && context is SpellOnFigureContext) {
            val target = context.target
            if(target is Player && target.effects.hasActiveEffect(Effects.TELEPORT_BLOCK)) {
                figure.message("This player is already effected by this spell.", true)
                return false
            }
        }
        return super.canCast(figure, context)
    }

    override fun spellEffect(figure: Figure, target: Figure, damage: Int) {
        if (target is Player && !target.effects.hasActiveEffect(Effects.TELEPORT_BLOCK)) {
            target.effects.startEffect(Effect(Effects.TELEPORT_BLOCK, if (target.prayer.isMageProtecting) 166 else 500))
            target.message("You have been teleblocked.")
        }
    }
}