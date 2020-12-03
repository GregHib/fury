package com.fury.game.content.combat.magic.spell.modern.skill.charge

import com.fury.core.model.item.Item
import com.fury.game.content.misc.objects.ElementalOrb
import java.util.*

class ChargeAirOrb : ChargeOrbSpell(ElementalOrb.AIR_ORB) {
    override val id = 61955
    override val level = 66
    override val experience = 76.0
    override val items = Optional.of(arrayOf(Item(556, 30), Item(564, 3), Item(567)))
}