package com.fury.game.content.combat.magic.spell

import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.GameSettings
import com.fury.game.content.dialogue.impl.transportation.*
import com.fury.game.entity.character.player.link.transportation.TeleportHandler

object Teleports {
    fun handle(player: Player, button: Int): Boolean {
        when (button) {
            62252, 61402, 61001//Home
            -> {
                TeleportHandler.teleportPlayer(player, GameSettings.DEFAULT_POSITION.copyPosition(), player.spellbook.teleportType)
                return true
            }
            62380, 61486, 61038 -> {
                player.dialogueManager.startDialogue(TrainingTeleportsD())
                return true
            }
            62335, 61600, 61071 -> {
                player.dialogueManager.startDialogue(SkillTeleportsD())
                return true
            }
            62387, 61704, 61121 -> {
                player.dialogueManager.startDialogue(BossTeleportsD())
                return true
            }
            62342, 61631, 61154 -> {
                player.dialogueManager.startDialogue(CityTeleportsD())
                return true
            }
            62349, 61662, 61204 -> {
                player.dialogueManager.startDialogue(DungeonTeleportsD())
                return true
            }
            62433, 61770, 61235 -> {
                player.dialogueManager.startDialogue(MinigameTeleportD())
                return true
            }
            62453, 61823, 61285 -> {
                player.dialogueManager.startDialogue(WildernessTeleportsD())
                return true
            }
        }
        return false
    }
}
