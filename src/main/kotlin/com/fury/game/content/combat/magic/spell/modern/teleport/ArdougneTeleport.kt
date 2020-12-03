package com.fury.game.content.combat.magic.spell.modern.teleport

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.combat.magic.Spell
import com.fury.core.model.node.entity.actor.figure.combat.magic.SpellContext
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.entity.character.player.link.transportation.TeleportHandler
import com.fury.game.world.map.Position
import java.util.*

class ArdougneTeleport : Spell() {
    override val id = 1540
    override val level = 51
    override val experience = 61.0
    override val items = Optional.of(arrayOf(Item(563, 2), Item(555, 2)))

    override fun startCast(figure: Figure, context: SpellContext) {
        if (figure is Player)
            TeleportHandler.teleportPlayer(figure, Position(Position(2661, 3303), 2), figure.spellbook.teleportType)
    }
}