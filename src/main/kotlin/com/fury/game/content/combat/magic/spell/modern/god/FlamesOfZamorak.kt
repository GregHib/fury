package com.fury.game.content.combat.magic.spell.modern.god

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.combat.magic.spell.CombatEffectSpell
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.container.impl.equip.Slot
import com.fury.game.content.skill.Skill
import com.fury.game.entity.character.combat.effects.Effects
import com.fury.game.world.update.flag.block.Animation
import com.fury.game.world.update.flag.block.graphic.Graphic
import java.util.*

class FlamesOfZamorak : CombatEffectSpell() {
    override val maxHit = 200
    override val animation = Optional.of(Animation(811))
    override val hitGraphic = Optional.of(Graphic(78))
    override val id = 61893
    override val level = 60
    override val experience = 35.0
    override val items = Optional.of(arrayOf(Item(554, 4), Item(565, 2), Item(556)))
    override val equipment = Optional.of(arrayOf(Item(2417)))

    override fun getMaxHit(figure: Figure, target: Figure?): Int {
        return if(figure is Player && figure.effects.hasActiveEffect(Effects.CHARGE) && figure.equipment.get(Slot.CAPE).id == 2414) maxHit + 100 else super.getMaxHit(figure, target)
    }

    override fun spellEffect(figure: Figure, target: Figure, damage: Int) {
        if(target is Player && target.skills.isFull(Skill.MAGIC))
            target.skills.drain(Skill.MAGIC, 5, true)
    }
}