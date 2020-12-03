package com.fury.game.content.combat.magic.spell.ancient

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.combat.magic.spell.CombatAncientSpell
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.entity.character.combat.effects.Effect
import com.fury.game.entity.character.combat.effects.Effects
import java.util.*

abstract class MiasmicSpell(val time: Int) : CombatAncientSpell() {
    override val equipment: Optional<Array<Item>> = Optional.of(arrayOf(Item(13867), Item(13941)))

    override fun hasRequiredEquipment(player: Player): Boolean {
        return !(equipment.isPresent && !player.equipment.contains(*equipment.get()))//Contains one of, not all
    }

    override fun spellEffect(figure: Figure, target: Figure, damage: Int) {
        target.effects.startEffect(Effect(Effects.MIASMIC_EFFECT, time))
    }
}