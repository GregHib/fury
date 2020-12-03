package com.fury.game.content.combat.magic.spell.ancient.rush

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.Projectile
import com.fury.core.model.node.entity.actor.figure.SpeedProjectile
import com.fury.core.model.node.entity.actor.figure.combat.magic.SpellContext
import com.fury.core.model.node.entity.actor.figure.combat.magic.context.SpellOnItemContext
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.content.combat.magic.spell.ancient.MiasmicSpell
import com.fury.game.content.skill.free.dungeoneering.DungeonConstants
import com.fury.game.world.update.flag.block.Animation
import com.fury.game.world.update.flag.block.graphic.Graphic
import java.util.*

class MiasmicRush : MiasmicSpell(20) {//12 seconds
    override val maxHit = 180
    override val animation = Optional.of(Animation(10513))
    override val castGraphic = Optional.of(Graphic(1845))
    override val hitGraphic = Optional.of(Graphic(1847))
    override val projectile = Optional.of(arrayOf(SpeedProjectile(1846, 18, 9, 52, 15, 0) as Projectile))
    override val id = 61078
    override val level = 61
    override val experience = 36.0
    override val items = Optional.of(arrayOf(Item(566), Item(562, 2), Item(557)))

    override fun canCast(figure: Figure, context: SpellContext): Boolean {
        if(figure is Player) {
            if (!figure.equipment.contains(Item(13867))) {
                figure.message("You need a Zuriel's Staff to cast this spell!")
                return false
            }
        }
        return super.canCast(figure, context)
    }
}