package com.fury.game.world

import com.fury.game.entity.character.player.info.PlayerRights
import com.fury.game.system.communication.MessageType

object MessageManager {
    fun sendMessage(message: String, colour: Int) {
        GameWorld.players.forEach { player -> player.message(message, colour) }
    }

    fun sendMessage(message: String) {
        GameWorld.players.forEach { player -> player.message(message) }
    }

    fun sendMessage(type: MessageType, message: String) {
        GameWorld.players.forEach { player -> player.packetSender.sendMessage(type, message) }
    }

    fun sendStaffMessage(message: String, colour: Int) {
        GameWorld.players.stream().filter({ player -> player != null && (player.rights == PlayerRights.OWNER || player.rights == PlayerRights.DEVELOPER || player.rights == PlayerRights.ADMINISTRATOR || player.rights == PlayerRights.MODERATOR || player.rights == PlayerRights.SUPPORT) }).forEach { p -> p.message(message, colour) }
    }

    fun sendStaffMessage(message: String) {
        GameWorld.players.stream().filter({ player -> player != null && (player.rights == PlayerRights.OWNER || player.rights == PlayerRights.DEVELOPER || player.rights == PlayerRights.ADMINISTRATOR || player.rights == PlayerRights.MODERATOR || player.rights == PlayerRights.SUPPORT) }).forEach { p -> p.message(message) }
    }
}