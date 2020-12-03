package com.fury.game.content.combat.magic.spell.modern.element

import com.fury.core.model.node.entity.actor.figure.combat.magic.spell.CombatNormalSpell
import com.fury.game.world.update.flag.block.Animation
import com.fury.game.world.update.flag.block.graphic.Graphic
import com.fury.game.world.update.flag.block.graphic.GraphicHeight
import java.util.*

abstract class WaterSpell : CombatNormalSpell() {
    override val animation = Optional.of(Animation(10542))
    override val staffAnimation = Optional.of(Animation(14220))
    override val castGraphic = Optional.of(Graphic(2701, height = GraphicHeight.LOW))
    override val castStaffGraphic = Optional.of(Graphic(2702, height = GraphicHeight.LOW))
}