package com.fury.game.content.combat.magic.spell.modern.element.strike

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.Projectile
import com.fury.core.model.node.entity.actor.figure.SpeedProjectile
import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.game.content.combat.magic.spell.modern.element.WaterSpell
import com.fury.game.world.update.flag.block.graphic.Graphic
import com.fury.game.world.update.flag.block.graphic.GraphicHeight
import java.util.*

class WaterStrike : WaterSpell() {
    override val maxHit = 40

    override fun getMaxHit(figure: Figure, target: Figure?): Int {
        if(target is Mob && target.name == "Salarin the Twisted")
            return 100
        return super.getMaxHit(figure, target)
    }

    override val hitGraphic = Optional.of(Graphic(2708, height = GraphicHeight.HIGH))
    override val projectile = Optional.of(arrayOf(SpeedProjectile(2703, 30, 26, 52, 0, 0) as Projectile))
    override val id = 61449
    override val level = 5
    override val experience = 7.5
    override val items = Optional.of(arrayOf(Item(555), Item(556), Item(558)))
}