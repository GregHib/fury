package com.fury.game.node.entity.actor.figure.mob.drops

import com.fury.cache.Revision
import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.container.impl.equip.Equipment
import com.fury.game.content.global.Achievements
import com.fury.game.content.global.events.christmas.ChristmasEvent
import com.fury.game.content.skill.Skill
import com.fury.game.content.skill.free.prayer.Bone
import com.fury.game.content.skill.free.prayer.Bone.handleDemonHornNecklace
import com.fury.game.content.skill.member.herblore.Herbs
import com.fury.game.content.skill.member.summoning.CharmingImp
import com.fury.game.entity.character.npc.drops.Drop
import com.fury.game.node.entity.actor.figure.player.misc.redo.DropLog
import com.fury.game.system.communication.clans.ClanChatManager
import com.fury.game.system.files.world.increment.timer.impl.DailySnowflake
import com.fury.game.world.GameWorld
import com.fury.util.FontUtils
import com.fury.util.RandomUtils
import com.fury.util.Utils
import java.util.*

object MobDropHandler {
    private val CHARMS = intArrayOf(12158, 12159, 12160, 12163)
    val ANNOUNCE = intArrayOf(14484, 4224,
            11702, 11704, 11706, 11708, 11704, 11724, 11726, 11728, 11718,
            11720, 11722, 11730, 11716, 14876, 11286, 13427, 6731, 6737,
            6735, 4151, 2513, 15259, 13902, 13890, 13884, 13861, 13858,
            13864, 13905, 13887, 13893, 13899, 13873, 13879, 13876, 13870,
            6571, 13750, 13748, 13746, 13752, 11335, 15486, 13870, 13873,
            13876, 13884, 13890, 13896, 13902, 13858, 13861, 13864, 13867,
            11235, 20135, 20139, 20143, 20147, 20151, 20155, 20159, 20163,
            20167, 20171, 15488, 15490, 6739)

    private fun getAdditions(mob: Mob, player: Player): Collection<Drop> {
        val drops = mutableListOf<Drop>()

        val additions = mob.addDrops(player)
        if (additions != null)
            drops.addAll(additions)

        //Charms
        if (RandomUtils.success(0.125 * mob.getSize()))
            drops.add(Drop(RandomUtils.random(*CHARMS), 100.0, 1, mob.getSize() * 2, false))

        //Christmas event
        if (ChristmasEvent.active && DailySnowflake.get().get(player.logger.hardwareId) < 750) {
            //Snowmen
            if (mob.id == 14209 || mob.id == 14207 || mob.id == 14208) {
                if (ChristmasEvent.active && DailySnowflake.get().get(player.logger.hardwareId) < 750) {
                    DailySnowflake.get().record(player.logger.hardwareId)
                    drops.add(Drop(33596, Revision.RS3, 100.0, 1, 8, false))
                }
            } else {
                //Snowflakes
                DailySnowflake.get().record(player.logger.hardwareId)
                drops.add(Drop(33596, Revision.RS3, 100.0, 1, 1, false))
            }
        }

        //Runecrafting pouches
        if (mob.id in 2263..2265) {
            if (!player.hasItem(5509))
                drops.add(Drop(5509, Revision.RS2, 50.0, 1, 1, false))
            else if (!player.hasItem(5510))
                drops.add(Drop(5510, Revision.RS2, 50.0, 1, 1, false))
            else if (!player.hasItem(5512))
                drops.add(Drop(5512, Revision.RS2, 50.0, 1, 1, false))
            else if (!player.hasItem(5514))
                drops.add(Drop(5514, Revision.RS2, 50.0, 1, 1, false))
        }

        //Rare drop table
        if (RareDropTable.hitTable(player)) {
            if (Equipment.wearingRingOfWealth(player))
                player.message("Your ring of wealth shines more brightly", 0xff7000)
            val item = RareDropTable.getDrop(player)
            drops.add(Drop(item.id, item.revision, 100.0, item.amount, item.amount, true))
        }
        return drops
    }

    private fun handleRemovals(item: Item, player: Player): Boolean {
        //Bonecrusher
        val bone = Bone.forId(item.id)
        if (bone != null && !bone.isAsh) {
            if (player.inventory.contains(Item(18337))) {
                Bone.buryAchievements(player, bone)
                player.skills.addExperience(Skill.PRAYER, bone.experience)
                val demonHornNecklace = handleDemonHornNecklace(player, bone)
                if (demonHornNecklace > 0) {
                    player.skills.restore(Skill.PRAYER, demonHornNecklace)
                }
                return true
            }
        }

        //Herbicide
        val herb = Herbs.getHerb(item.id)
        if (herb != null) {
            if (player.inventory.contains(Item(19675))) {
                if (player.skills.hasLevel(Skill.HERBLORE, herb.level)) {
                    player.skills.addExperience(Skill.HERBLORE, herb.experience * 2.0)
                    return true
                }
            }
        }

        //Charming imp
        if (player.inventory.contains(Item(27996, Revision.RS3)) && CharmingImp.handleCharmDrop(player, item.id, item.amount))
            return true

        return false
    }

    private fun handleReplacements(itemId: Int, player: Player, mob: Mob): Int {
        return when (itemId) {
            618 -> {//TODO remove (temporary fix)
                println("Npc dropped 618 ${mob.id}")
                995
            }
            15241 -> {
                Achievements.finishAchievement(player, Achievements.AchievementData.RECEIVE_HAND_CANNON_DROP)
                itemId
            }
            6729 -> if (Equipment.wearingSeaBoots(player, 4)) 6730 else itemId
            else -> {
                itemId
            }
        }
    }

    @JvmStatic
    fun drop(killer: Player, mob: Mob) {
        //Slayer
        val otherPlayer = killer.slayerManager.socialPlayer
        val manager = killer.slayerManager
        if (manager.isValidTask(mob.name))
            manager.checkCompletedTask(mob.combat.hits.getDamageReceived(killer), if (otherPlayer != null) mob.combat.hits.getDamageReceived(otherPlayer) else 0)

        try {
            var originalDrops: Array<Drop>? = MobDrops.getDrops(mob.actualId, mob.revision)

            if (originalDrops == null)
                originalDrops = MobDrops.getDrops(mob.id, mob.revision)

            val drops = ArrayList<Drop>()

            if (originalDrops != null)
                drops.addAll(Arrays.asList(*originalDrops))

            drops.addAll(getAdditions(mob, killer))


            val ls = Lootshare.isActive(killer)

            drops
                    .filter { it.rate == 100.0 }
                    .forEach { sendDrop(killer, it, mob, ls) }

            val drop = getDrop(drops.toTypedArray(), killer)
            if (drop != null)
                sendDrop(killer, drop, mob, ls)

        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    @JvmStatic
    fun getDrop(drops: Array<Drop>?, player: Player): Drop? {
        if (drops == null)
            return null

        val possibilities = drops
                .filter { it.rate != 100.0 && Utils.randomDouble(99.0) + 1 <= it.rate * 1.5 * player.dropRate }
                .toTypedArray()
        return if (possibilities.isNotEmpty())
            RandomUtils.random(possibilities)
        else
            null
    }

    @JvmOverloads
    fun sendDrop(player: Player, drop: Drop, mob: Mob, lootshare: Boolean = false): Item? {
        drop.itemId = handleReplacements(drop.itemId, player, mob)

        val item = Item(drop.itemId, drop.minAmount + Utils.getRandom(drop.extraAmount), drop.revision)

        if (handleRemovals(item, player))
            return null

        if (ANNOUNCE.contains(item.id))
            GameWorld.sendBroadcast("${FontUtils.imageTags(535)}${player.username} just received ${item.name} from ${mob.name}!", 0x009966)

        val rewardee = if (lootshare) Lootshare.getRewardee(player, mob) else player

        if (lootshare && rewardee !== player) {
            ClanChatManager.sendLootshare(rewardee, "${item.amount} ${item.name}")
            Achievements.finishAchievement(rewardee, Achievements.AchievementData.RECEIVE_LOOTSHARE_DROP)
        }

        rewardee.logger.addNpcDrop(item)

        DropLog.submit(rewardee, DropLogEntry(item.id, item.amount))

        mob.drop(rewardee, item)

        return item
    }
}