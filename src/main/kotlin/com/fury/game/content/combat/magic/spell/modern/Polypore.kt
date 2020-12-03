package com.fury.game.content.combat.magic.spell.modern

import com.fury.cache.Revision
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

class Polypore : CombatNormalSpell() {
    override fun getMaxHit(figure: Figure, target: Figure?): Int {
        return if (figure is Player) (figure.skills.getLevel(Skill.MAGIC) /* + bonus*/) * 5 - 180 else super.getMaxHit(figure, target)
    }

    override val hitGraphic = Optional.of(Graphic(2036, 0, GraphicHeight.HIGH, Revision.PRE_RS3))
    override val projectile = Optional.of(arrayOf(SpeedProjectile(2035, Revision.PRE_RS3, 30, 26, 52, 0, 0) as Projectile))
    override val id = -1
    override val experience = 19.0
    override val animation = Optional.of(Animation(15448, Revision.PRE_RS3))
    override val castGraphic = Optional.of(Graphic(2034, Revision.PRE_RS3))
    override fun getHitDelay(figure: Figure): Int {
       return 4
    }
}