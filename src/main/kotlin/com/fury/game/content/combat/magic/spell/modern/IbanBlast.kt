package com.fury.game.content.combat.magic.spell.modern

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Projectile
import com.fury.core.model.node.entity.actor.figure.SpeedProjectile
import com.fury.core.model.node.entity.actor.figure.combat.magic.spell.CombatNormalSpell
import com.fury.game.world.update.flag.block.Animation
import com.fury.game.world.update.flag.block.graphic.Graphic
import com.fury.game.world.update.flag.block.graphic.GraphicHeight
import java.util.*

class IbanBlast : CombatNormalSpell() {
    override val maxHit = 250
    override val animation = Optional.of(Animation(708))
    override val castGraphic = Optional.of(Graphic(87, height = GraphicHeight.HIGH))
    override val hitGraphic = Optional.of(Graphic(89, height = GraphicHeight.HIGH))
    override val projectile = Optional.of(arrayOf(SpeedProjectile(88, 54, 32, 43, 0, 1) as Projectile))
    override val id = 61735
    override val level = 50
    override val experience = 30.0
    override val items = Optional.of(arrayOf(Item(560), Item(554, 5)))
    override val equipment = Optional.of(arrayOf(Item(1409)))
}