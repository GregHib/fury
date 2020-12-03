package com.fury.game.content.combat.magic.spell.modern.skill.charge

import com.fury.core.model.item.Item
import com.fury.game.content.misc.objects.ElementalOrb
import java.util.*

class ChargeFireOrb : ChargeOrbSpell(ElementalOrb.FIRE_ORB) {
    override val id = 61924
    override val level = 63
    override val experience = 73.0
    override val items = Optional.of(arrayOf(Item(554, 30), Item(564, 3), Item(567)))
}