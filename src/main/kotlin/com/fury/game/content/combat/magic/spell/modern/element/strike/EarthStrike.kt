package com.fury.game.content.combat.magic.spell.modern.element.strike

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.Projectile
import com.fury.core.model.node.entity.actor.figure.SpeedProjectile
import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.game.content.combat.magic.spell.modern.element.EarthSpell
import com.fury.game.world.update.flag.block.graphic.Graphic
import com.fury.game.world.update.flag.block.graphic.GraphicHeight
import java.util.*

class EarthStrike : EarthSpell() {
    override val maxHit = 60
    override fun getMaxHit(figure: Figure, target: Figure?): Int {
        if(target is Mob && target.name == "Salarin the Twisted")
            return 110
        return super.getMaxHit(figure, target)
    }
    override val castGraphic = Optional.of(Graphic(2713))
    override val hitGraphic = Optional.of(Graphic(2723, height = GraphicHeight.HIGH))
    override val projectile = Optional.of(arrayOf(SpeedProjectile(2718, 30, 26, 52, 0, 0) as Projectile))
    override val id = 61473
    override val level = 9
    override val experience = 9.5
    override val items = Optional.of(arrayOf(Item(556), Item(558), Item(557, 2)))
}