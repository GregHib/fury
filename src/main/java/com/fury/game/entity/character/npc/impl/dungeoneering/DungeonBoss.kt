package com.fury.game.entity.character.npc.impl.dungeoneering

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.content.skill.free.dungeoneering.DungeonConstants
import com.fury.game.content.skill.free.dungeoneering.DungeonManager
import com.fury.game.content.skill.free.dungeoneering.RoomReference
import com.fury.game.entity.character.npc.drops.Drop
import com.fury.game.node.entity.actor.figure.mob.drops.MobDrops
import com.fury.game.world.FloorItemManager
import com.fury.game.world.map.Position
import com.fury.util.Misc
import com.fury.util.RandomUtils
import java.util.*

open class DungeonBoss @JvmOverloads constructor(id: Int, tile: Position, manager: DungeonManager, reference: RoomReference, multiplier: Double = 1.0) : DungeonMob(id, tile, manager, multiplier) {

    var reference: RoomReference? = null
    var nextForceTalk: String? = null

    override val isPoisonImmune: Boolean
        get() = true

    init {
        this.reference = reference
        forceAggressive = true
        isIntelligentRouteFinder = true
        lureDelay = 0
    }

    override fun addDrops(killer: Player): List<Drop>? {
        val drops = ArrayList<Drop>()
        val originalDrops = MobDrops.getDrops(id)
        if (originalDrops != null)
            Collections.addAll(drops, *originalDrops)

        var drop: Drop? = null
        if (manager.party.size == DungeonConstants.LARGE_DUNGEON)
            drop = drops[if (Misc.random(100) < 90) drops.size - 1 else Misc.random(drops.size)]
        else if (manager.party.size == DungeonConstants.LARGE_DUNGEON)
            drop = drops[if (Misc.random(100) < 60) drops.size - 1 else Misc.random(drops.size)]
        else if (drops.size > 0)
            drop = RandomUtils.random(drops)

        val players = manager.party.team
        if (players.size == 0 || drop == null)
            return null
        val luckyPlayer = RandomUtils.random(players)
        val item = sendDrop(luckyPlayer, drop)
        if (item != null) {
            luckyPlayer.message("You received: " + item.amount + " " + item.name + ".")
            for (p2 in players) {
                if (p2 === luckyPlayer)
                    continue
                p2.message("" + luckyPlayer.username + " received: " + item.amount + " " + item.name + ".")
            }
            drop(luckyPlayer, item)
        }

        return null
    }


    fun sendDrop(player: Player, drop: Drop): Item {
        val item = Item(drop.itemId)
        player.inventory.add(item)
        return item
    }

    fun sendDrop(player: Player, drop: Item): Item {
        player.inventory.add(drop)
        return drop
    }

    companion object {

        fun drop(item: Item, mob: Mob, pos: Position, goGlobal: Boolean) {
            val itemId = item.id
            val amount = item.amount

            FloorItemManager.addGroundItem(item, pos, null)
            //		GroundItemManager.add(new GroundItem(item, pos, null, true, 150, true, 0), true);
            //DropLog.submit(toGive, new DropLog.DropLogEntry(itemId, item.getAmount()));
        }
    }
}
