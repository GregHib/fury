package com.fury.game.content.combat.magic.spell.modern.teleport

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.combat.magic.Spell
import com.fury.core.model.node.entity.actor.figure.combat.magic.SpellContext
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.content.skill.member.construction.House
import com.fury.game.entity.character.player.link.transportation.TeleportHandler
import java.util.*

class TeleportToHouse : Spell() {
    override val id = -1
    override val level = 40
    override val experience = 30.0
    override val items = Optional.of(arrayOf(Item(563), Item(556), Item(557)))

    override fun startCast(figure: Figure, context: SpellContext) {
        if (figure is Player) {
            TeleportHandler.teleportPlayer(figure, figure.house!!.location.tile, figure.spellbook.teleportType)
            House.enterHouse(figure, true)
        }
    }
}