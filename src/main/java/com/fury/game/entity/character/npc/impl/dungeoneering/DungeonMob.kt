package com.fury.game.entity.character.npc.impl.dungeoneering

import com.fury.cache.Revision
import com.fury.cache.def.Loader
import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.content.skill.free.dungeoneering.DungeonConstants
import com.fury.game.content.skill.free.dungeoneering.DungeonManager
import com.fury.game.content.skill.free.dungeoneering.DungeonUtils
import com.fury.game.entity.character.npc.drops.Drop
import com.fury.game.world.GameWorld
import com.fury.game.world.map.Position
import com.fury.game.world.update.flag.block.Direction
import com.fury.util.Misc
import java.util.*

open class DungeonMob(id: Int, tile: Position, var manager: DungeonManager, var multiplier: Double = 0.0, revision: Revision = if (id > Loader.getTotalNpcs(Revision.RS2)) Revision.PRE_RS3 else Revision.RS2) : Mob(id, tile, revision) {

    private var customBonuses: IntArray? = null
    var marked: Boolean = false

    /*
     * they dont respawn anyway, and this way stomp will be fine
	 */

    override var respawnDirection: Direction?
        get() = this.direction.direction
        set(value: Direction?) {
            super.respawnDirection = value
        }

    override val maxConstitution: Int
        get() = (combatLevel * (if (this is DungeonBoss) 200 else 100) + 1) / 10

    val maxHit: Int
        get() = combatLevel

    override var bonuses: IntArray?
        get() = if (customBonuses == null) super.bonuses else customBonuses
        set(value) {
            super.bonuses = value
        }

    private val bones: Int
        get() = if (name.toLowerCase().contains("dragon")) 536 else if (getSize() > 1) 532 else 526

    init {
        if (definition.hasAttackOption()) {
            val level = manager.getTargetLevel(id, this is DungeonBoss || this is DungeonSkeletonBoss, multiplier)
            this.combatLevel = level
            val max = maxConstitution
            health.hitpoints = max
            resetBonuses()
        }
        targetDistance = 20//includes whole room
    }

    fun resetBonuses() {
        customBonuses = manager.getBonuses(this is DungeonBoss, combatLevel)
    }

    fun getNPC(id: Int): Mob? {
        for (mob in GameWorld.mobs.mobs) {
            if (mob.id == id) {
                return mob
            }
        }
        return null
    }

    override fun processNpc() {
        super.processNpc()
        if (combat.isInCombat()) {
            val target = mobCombat!!.target
            val thisR = manager.getCurrentRoomReference(this)
            val targetR = manager.getCurrentRoomReference(target)
            if (targetR != thisR)
                mobCombat!!.removeTarget()
        }
    }

    override fun addDrops(killer: Player): List<Drop>? {
        val items = ArrayList<Item>()
        if (id != 10831 && id != 10821)
        //nature & ghost
            items.add(Item(bones))
        for (i in 0 until 1 + Misc.random(10))
            items.add(Item(DungeonUtils.getFood(1 + Misc.random(8))))

        if (Misc.random(10) == 0)
            items.add(Item(DungeonUtils.getDagger(1 + Misc.random(5))))

        if (Misc.random(5) == 0)
            items.add(Item(DungeonConstants.RUNES[Misc.random(DungeonConstants.RUNES.size)], 90 + Misc.random(30)))

        if (manager.party.complexity >= 5 && Misc.random(5) == 0)
        //torm bag, 1
            items.add(Item(DungeonUtils.getTornBag(1 + Misc.random(10))))

        if (manager.party.complexity >= 3 && Misc.random(5) == 0)
        //ore, up to 10
            items.add(Item(DungeonUtils.getOre(1 + Misc.random(5)), 1 + Misc.random(10)))

        if (manager.party.complexity >= 2 && Misc.random(5) == 0)
        //branches, up to 10
            items.add(Item(DungeonUtils.getBranches(1 + Misc.random(5)), 1 + Misc.random(10)))

        if (manager.party.complexity >= 4 && Misc.random(5) == 0)
        //textile, up to 10
            items.add(Item(DungeonUtils.getTextile(1 + Misc.random(10)), 1 + Misc.random(10)))

        if (manager.party.complexity >= 5 && Misc.random(5) == 0)
        //herb, up to 10
            items.add(Item(DungeonUtils.getHerb(1 + Misc.random(9)), 1 + Misc.random(10)))

        if (manager.party.complexity >= 5 && Misc.random(5) == 0)
        //seed, up to 10
            items.add(Item(DungeonUtils.getSeed(1 + Misc.random(12)), 1 + Misc.random(10)))

        if (manager.party.complexity >= 5 && Misc.random(3) == 0)
        //charms, depending in mob size
            items.add(Item(DungeonConstants.CHARMS[Misc.random(DungeonConstants.CHARMS.size)], getSize()))

        if (manager.party.complexity >= 2)
        //coins, 1000 up to 11000
            items.add(Item(DungeonConstants.RUSTY_COINS, 1000 + Misc.random(10001)))

        if (manager.party.complexity >= 3 && Misc.random(5) == 0)
        //essence, 10 up to 300
            items.add(Item(DungeonConstants.RUNE_ESSENCE, 10 + Misc.random(300)))
        if (manager.party.complexity >= 2 && Misc.random(5) == 0)
        //feather, 10 up to 300
            items.add(Item(DungeonConstants.FEATHER, 10 + Misc.random(300)))
        if (manager.party.complexity >= 5 && Misc.random(10) == 0)
        //vial, 1
            items.add(Item(17490))
        if (Misc.random(10) == 0)
        //anti dragon shield
            items.add(Item(16933))
        if (manager.party.complexity >= 4 && Misc.random(10) == 0)
        //bowstring, 1
            items.add(Item(17752))
        if (manager.party.complexity >= 2 && Misc.random(10) == 0)
        //fly fishing rod, 1
            items.add(Item(17794))
        if (manager.party.complexity >= 4 && Misc.random(5) == 0)
        //thread, 10 up to 300
            items.add(Item(17447, 10 + Misc.random(300)))

        val drops = ArrayList<Drop>()
        for (item in items)
            drops.add(Drop(item.id, item.revision, 100.0, item.amount, item.amount, false))
        return drops
    }
}
