package com.fury.game.content.combat.magic.spell.modern

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.Projectile
import com.fury.core.model.node.entity.actor.figure.SpeedProjectile
import com.fury.core.model.node.entity.actor.figure.combat.magic.spell.CombatNormalSpell
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.content.skill.Skill
import com.fury.game.world.update.flag.block.Animation
import com.fury.game.world.update.flag.block.graphic.Graphic
import com.fury.game.world.update.flag.block.graphic.GraphicHeight
import java.util.*

class MagicDart : CombatNormalSpell() {

    override fun getMaxHit(figure: Figure, target: Figure?): Int {
        /*
        100 + level + (wizards mind bomb ? (level > 49 ? 3 : 2) : mature mind bomb ? (level > 49 ? 4 : 3) : magic essence ? 3 : magic ? 5 : extreme magic/overload/wolpertinger/magic focus ? 7 : magic cape/cape ? 1 : 0)
         */
        return if(figure is Player) 100 + figure.skills.getLevel(Skill.MAGIC) else super.getMaxHit(figure, target)
    }

    override fun hasRequiredLevel(figure: Figure): Boolean {
        return (figure as? Player)?.skills?.hasRequirement(Skill.SLAYER, 55, "cast this spell")?: true && super.hasRequiredLevel(figure)
    }

    override val animation = Optional.of(Animation(811))
    override val castGraphic = Optional.of(Graphic(327, height = GraphicHeight.HIGH))
    override val hitGraphic = Optional.of(Graphic(329))
    override val projectile = Optional.of(arrayOf(SpeedProjectile(328, 44, 3, 43, 31, 0) as Projectile))
    override val id = 61759
    override val level = 50//Also requires 55 slayer to cast
    override val experience = 30.0
    override val items = Optional.of(arrayOf(Item(558, 4), Item(560)))
    override val equipment = Optional.of(arrayOf(Item(4170)))//Slayer staff or staff of light
}