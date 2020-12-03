package com.fury.game.content.combat.magic

import com.fury.game.entity.character.player.link.transportation.TeleportType

/**
 * Represents a player's magic spellbook.
 *
 * @author relex lawl
 * @author Greg
 */

enum class MagicSpellBook( val interfaceId: Int, val teleportType: TeleportType) {
    NORMAL(11000, TeleportType.NORMAL),
    ANCIENT(11500, TeleportType.ANCIENT),
    LUNAR(11800, TeleportType.LUNAR),
    DUNGEONEERING(19000, TeleportType.DUNGEONEERING_TELE);
}
