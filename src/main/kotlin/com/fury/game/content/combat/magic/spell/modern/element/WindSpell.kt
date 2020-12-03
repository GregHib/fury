package com.fury.game.content.combat.magic.spell.modern.element

import com.fury.core.model.node.entity.actor.figure.combat.magic.spell.CombatNormalSpell
import com.fury.game.world.update.flag.block.Animation
import com.fury.game.world.update.flag.block.graphic.Graphic
import com.fury.game.world.update.flag.block.graphic.GraphicHeight
import java.util.*

abstract class WindSpell : CombatNormalSpell() {
    override val animation = Optional.of(Animation(10546))
    override val staffAnimation = Optional.of(Animation(14221))
    override val castGraphic = Optional.of(Graphic(457, height = GraphicHeight.LOW))
}