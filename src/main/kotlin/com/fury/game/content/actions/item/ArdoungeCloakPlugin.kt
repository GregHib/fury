package com.fury.game.content.actions.item

import com.fury.core.action.ItemActionEvent
import com.fury.game.content.dialogue.impl.misc.ArdougneCloakD
import com.fury.game.node.entity.actor.figure.player.misc.HourlyDelay
import com.fury.game.content.skill.member.summoning.impl.Summoning
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.entity.character.player.link.transportation.TeleportHandler
import com.fury.game.entity.character.player.link.transportation.TeleportType
import com.fury.core.model.item.Item
import com.fury.game.node.entity.actor.figure.player.Variables
import com.fury.game.world.map.Position

class ArdoungeCloakPlugin : ItemActionEvent() {

    override fun secondOption(player: Player, item: Item, slot: Int): Boolean {
        if(item.isEqual(15349) || item.isEqual(19748))
            player.dialogueManager.startDialogue(ArdougneCloakD())
        else if(item.isEqual(15347) || item.isEqual(15345))
            TeleportHandler.teleportPlayer(player, Position(2606, 3210), TeleportType.ARDOUGNE_CLOAK)
        return super.secondOption(player, item, slot)
    }

    override fun thirdOption(player: Player, item: Item, slot: Int): Boolean {
        if(item.isEqual(19748)) {
            HourlyDelay.fireOnce(player, Variables.ARDOUGNE_CLOAK_RECHARGE, 24) {
                Summoning.renewSummoningPoints(player)
            }
            return true
        }
        return false
    }
}