package com.fury.game.content.combat.magic.spell.modern.skill

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.content.global.Achievements
import com.fury.util.RandomUtils
import java.util.*

class BonesToBananas : BonesToSpell(true) {
    override val id = 61519
    override val level = 15
    override val experience = 25.0
    override val items = Optional.of(arrayOf(Item(561), Item(555, 2), Item(557, 2)))

    override fun removeRunes(player: Player): Boolean {
        return if(Achievements.hasFinishedAll(player, Achievements.Difficulty.HARD)) RandomUtils.success(0.5) else true
    }
}