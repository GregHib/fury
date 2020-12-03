package com.fury.game.content.combat.magic.spell.modern.god

import com.fury.cache.Revision
import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.Projectile
import com.fury.core.model.node.entity.actor.figure.SpeedProjectile
import com.fury.core.model.node.entity.actor.figure.combat.magic.spell.CombatEffectSpell
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.container.impl.equip.Slot
import com.fury.game.content.skill.Skill
import com.fury.game.world.update.flag.block.Animation
import com.fury.game.world.update.flag.block.graphic.Graphic
import java.util.*

class StormOfArmadyl : CombatEffectSpell() {
    //both min and max hit effected by magic damage bonuses
    override fun getMaxHit(figure: Figure, target: Figure?): Int {
        return if (figure is Player) 160 + (5 * (figure.skills.getLevel(Skill.MAGIC) - 77 /* + bonus*/)) else super.getMaxHit(figure, target)
    }

    override fun getMinHit(figure: Figure, target: Figure): Int {
        return if (figure is Player) 5 * (figure.skills.getLevel(Skill.MAGIC) - 77 /* + bonus*/) else 0
    }

    override val animation = Optional.of(Animation(10546))
    override val staffAnimation = Optional.of(Animation(14221))
    override val projectile = Optional.of(arrayOf(SpeedProjectile(1019, Revision.PRE_RS3, 30, 0, 52, 0, 0) as Projectile))
    override val hitGraphic = Optional.of(Graphic(1019, Revision.PRE_RS3))
    override val id = 62044
    override val level = 77
    override val experience = 70.0
    override val items = Optional.of(arrayOf(Item(21773, Revision.PRE_RS3)))
    //speed without staff is 1/3 seconds to 1/2.4 seconds with staff
    override fun getHitDelay(figure: Figure): Int {
        return if (figure is Player && figure.equipment.get(Slot.WEAPON).id == 21777) 3 else 5
    }

    override fun spellEffect(figure: Figure, target: Figure, damage: Int) {
        (target as? Player)?.skills?.drain(Skill.DEFENCE, 1)
    }
}