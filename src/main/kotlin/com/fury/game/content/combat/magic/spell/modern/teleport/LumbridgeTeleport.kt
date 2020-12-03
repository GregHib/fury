package com.fury.game.content.combat.magic.spell.modern.teleport

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.combat.magic.Spell
import com.fury.core.model.node.entity.actor.figure.combat.magic.SpellContext
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.entity.character.player.link.transportation.TeleportHandler
import com.fury.game.world.map.Position
import java.util.*

class LumbridgeTeleport : Spell() {
    override val id = 1167
    override val level = 31
    override val experience = 41.0
    override val items = Optional.of(arrayOf(Item(563), Item(556, 3), Item(557)))

    override fun startCast(figure: Figure, context: SpellContext) {
        if (figure is Player)
            TeleportHandler.teleportPlayer(figure, Position(Position(3221, 3218), 2), figure.spellbook.teleportType)
    }
}