package com.fury.game.content.combat.magic.spell.modern.skill.charge

import com.fury.core.model.item.Item
import com.fury.game.content.misc.objects.ElementalOrb
import java.util.*

class ChargeEarthOrb : ChargeOrbSpell(ElementalOrb.EARTH_ORB) {
    override val id = 61843
    override val level = 60
    override val experience = 70.0
    override val items = Optional.of(arrayOf(Item(557, 30), Item(564, 3), Item(567)))
}