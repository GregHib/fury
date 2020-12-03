package com.fury.game.content.combat.magic.spell.modern.element

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.combat.magic.spell.CombatNormalSpell
import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.game.world.update.flag.block.Animation
import com.fury.game.world.update.flag.block.graphic.Graphic
import java.util.*

abstract class FireSpell : CombatNormalSpell() {
    override val animation = Optional.of(Animation(2791))
    override val staffAnimation = Optional.of(Animation(14223))
    override val castGraphic = Optional.of(Graphic(2728))

    override fun getMaxHit(figure: Figure, target: Figure?): Int {
        return if(target is Mob && target.name.contains("glac")) maxHit * 2 else maxHit
    }
}