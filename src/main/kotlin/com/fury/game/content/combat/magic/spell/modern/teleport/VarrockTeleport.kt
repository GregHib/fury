package com.fury.game.content.combat.magic.spell.modern.teleport

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.combat.magic.Spell
import com.fury.core.model.node.entity.actor.figure.combat.magic.SpellContext
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.entity.character.player.link.transportation.TeleportHandler
import com.fury.game.world.map.Position
import java.util.*

class VarrockTeleport : Spell() {
    override val id = 1164
    override val level = 25
    override val experience = 35.0
    override val items = Optional.of(arrayOf(Item(563), Item(556, 3), Item(556)))

    override fun startCast(figure: Figure, context: SpellContext) {
        if (figure is Player)
            TeleportHandler.teleportPlayer(figure, Position(Position(3212, 3428), 2), figure.spellbook.teleportType)
    }
}