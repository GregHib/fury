package com.fury.game.node.entity.actor.figure.player

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.GameSettings
import com.fury.game.content.misc.items.ItemsKeptOnDeath
import com.fury.game.content.skill.free.dungeoneering.DungeonController
import com.fury.game.network.packet.out.MinimapOrb
import com.fury.game.node.entity.EntityDeath
import com.fury.game.node.entity.actor.figure.mob.extension.misc.Death
import com.fury.game.node.entity.actor.figure.mob.extension.misc.Gravestone
import com.fury.game.world.FloorItemManager
import com.fury.game.world.map.Position
import java.util.*
import java.util.stream.Collectors

class PlayerDeath @JvmOverloads constructor(player: Player, private val dropTile: Position = player.copyPosition(), private val respawnTile: Position = GameSettings.DEFAULT_POSITION, private val drop: Boolean = true, val runnable: Runnable? = null) : EntityDeath<Player>(player, 2) {

    private var death: Mob? = null

    override fun preDeath(killer: Figure?) {
        entity.animate(836)

        if (Calendar.getInstance().get(Calendar.MONTH) == Calendar.OCTOBER)
            this.death = Death(entity)
    }

    override fun death(killer: Figure?) {
        if (!entity.rights.isPriviledged && drop) {
            entity.familiar?.sendDeath(entity)
            calculateDropItems(entity, killer)
        }
    }

    override fun postDeath(killer: Figure?) {
        runnable?.run()

        if (entity.isInDungeoneering) {
            if (entity.dungManager.party.team.contains(entity))//Not sure if necessary
                if (entity.dungManager.party.dungeon.isAtBossRoom(entity, 26, 672, true))//Why is this not handled in a dung class?
                    DungeonController.getNPC(entity, 11872)?.forceChat("Another kill for the Thunderous!")

            val startRoom = entity.dungManager.party.dungeon.homeTile
            entity.moveTo(startRoom)
            entity.dungManager.party.sendMessage("${entity.username} has died.")
        } else {
            entity.moveTo(respawnTile)
        }
        entity.message("Oh dear, you have died.")

        entity.controllerManager.forceStop()
        entity.reset()
        entity.stopAll()
        entity.send(MinimapOrb(1, false))
        entity.combat.resetCombat()

        entity.equipment.refresh()
        entity.inventory.refresh()

        death?.deregister()

        //Junk to sort out
        if (!entity.isInDungeoneering && killer is Player) {
            killer.playerKillingAttributes.add(entity)
            entity.playerKillingAttributes.playerDeaths = entity.playerKillingAttributes.playerDeaths + 1
            entity.playerKillingAttributes.playerKillStreak = 0
            entity.pointsHandler.refreshPanel()
        }
    }

    private fun calculateDropItems(player: Player, highestDealer: Figure?) {
        val killer = highestDealer ?: player

        //Get all items
        val unsorted = linkedSetOf<Item>()
        val keep = mutableListOf<Item>()
        val items = mutableListOf<Item>()

        items.addAll(player.equipment.notNullItems)
        items.addAll(player.inventory.notNullItems)

        //Clear inventory
        player.equipment.clear()
        player.inventory.clear()

        //Sort items by value
        unsorted.addAll(items)

        val toDrop: MutableList<Item> = unsorted
                .stream()
                .sorted { first, second -> second.definitions.value.compareTo(first.definitions.value) }
                .collect(Collectors.toList())


        //Add items kept because
        for (i in 0 until ItemsKeptOnDeath.getAmountToKeep(player)) {
            if(toDrop.size > 0) {
                keep.add(toDrop.first())
                toDrop.remove(toDrop.first())
            }
        }

        //Remove any extra items that shouldn't be kept
        keep.forEach { item ->
            player.inventory.add(Item(item.id, item.revision))
            if (item.getDefinition().stackable && item.amount > 1)
                toDrop.add(item.createAndDecrement(1))
        }

        val owner = if (killer is Player && !killer.gameMode.isIronMan) killer else player

        if (entity.isInWilderness) {
            //Drop immediately
            var coins = 0
            toDrop.forEach { item ->
                if (killer is Player && killer !== player && !item.tradeable()) {
                    coins += item.definitions.value
                    return@forEach
                }
                FloorItemManager.addGroundItem(item, dropTile, owner)
            }
            if(coins > 0)
                FloorItemManager.addGroundItem(Item(995, coins), dropTile, player)
        } else if(toDrop.size > 0) {
            Gravestone(entity, dropTile, toDrop.toTypedArray())
        }

        FloorItemManager.addGroundItem(Item(526), dropTile, owner)
    }

}