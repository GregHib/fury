package com.fury.game.content.combat.magic.spell.modern.skill

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.combat.magic.SkillSpell
import com.fury.core.model.node.entity.actor.figure.combat.magic.SpellContext
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.container.impl.equip.Slot
import com.fury.game.entity.character.combat.effects.Effect
import com.fury.game.entity.character.combat.effects.Effects
import com.fury.game.world.update.flag.block.Animation
import java.util.*
import java.util.concurrent.TimeUnit

class Charge : SkillSpell() {
    override val animation = Optional.of(Animation(811))
    override val id = 62079
    override val level = 80
    override val experience = 180.0
    override val items = Optional.of(arrayOf(Item(554, 3), Item(556, 3), Item(565, 3)))

    override fun canCast(figure: Figure, context: SpellContext): Boolean {
        if(figure is Player) {
            val can = figure.timers.chargeSpell.elapsed(60, TimeUnit.SECONDS)
            //TODO can probably be handled better and with interface counters?
            if (!can)
                figure.message("You must wait another ${60 - figure.timers.chargeSpell.elapsedTime(TimeUnit.SECONDS)} seconds before casting this spell again.")
            return can && super.canCast(figure, context)
        }
        return super.canCast(figure, context)
    }

    override fun hasRequiredEquipment(player: Player): Boolean {
        val weapon = player.equipment.get(Slot.WEAPON).id
        return weapon in 2415..2417 || weapon == 8841
    }

    override fun spellEffect(figure: Figure, context: SpellContext) {
        if(figure is Player) {
            figure.effects.startEffect(Effect(Effects.CHARGE, 700))//7 minutes
            figure.timers.chargeSpell.reset()
        }
    }
}