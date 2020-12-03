package com.fury.game.content.combat.magic.spell.modern.element.strike

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.Projectile
import com.fury.core.model.node.entity.actor.figure.SpeedProjectile
import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.game.content.combat.magic.spell.modern.element.FireSpell
import com.fury.game.world.update.flag.block.graphic.Graphic
import com.fury.game.world.update.flag.block.graphic.GraphicHeight
import java.util.*

class FireStrike : FireSpell() {
    override val maxHit = 80

    override fun getMaxHit(figure: Figure, target: Figure?): Int {
        if(target is Mob && target.name == "Salarin the Twisted")
            return 120
        return super.getMaxHit(figure, target)
    }

    override val hitGraphic = Optional.of(Graphic(2737, height = GraphicHeight.HIGH))
    override val projectile = Optional.of(arrayOf(SpeedProjectile(2729, 30, 26, 52, 0, 0) as Projectile))
    override val id = 61506
    override val level = 13
    override val experience = 11.5
    override val items = Optional.of(arrayOf(Item(556, 2), Item(558), Item(554, 3)))
}