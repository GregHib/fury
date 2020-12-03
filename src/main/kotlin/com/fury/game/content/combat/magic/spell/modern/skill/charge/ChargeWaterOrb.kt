package com.fury.game.content.combat.magic.spell.modern.skill.charge

import com.fury.core.model.item.Item
import com.fury.game.content.misc.objects.ElementalOrb
import java.util.*

class ChargeWaterOrb : ChargeOrbSpell(ElementalOrb.WATER_ORB) {
    override val id = 61801
    override val level = 56
    override val experience = 66.0
    override val items = Optional.of(arrayOf(Item(555, 30), Item(564, 3), Item(567)))
}