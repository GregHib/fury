package com.fury.game.node.entity.actor.figure.mob.extension.misc

import com.fury.core.model.item.FloorItem
import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.engine.task.executor.GameExecutorManager
import com.fury.game.content.misc.objects.GravestoneSelection
import com.fury.game.content.skill.Skill
import com.fury.game.network.packet.out.EntityHint
import com.fury.game.network.packet.out.EntityHintRemoval
import com.fury.game.network.packet.out.GravestoneTimer
import com.fury.game.world.FloorItemManager
import com.fury.game.world.World
import com.fury.game.world.map.Position
import com.fury.game.world.update.flag.block.Animation
import com.fury.game.world.update.flag.block.Direction
import com.fury.util.Misc
import java.util.*
import java.util.concurrent.TimeUnit

class Gravestone(player: Player, deathTile: Position, items: Array<Item>) : Mob(if (player.gravestone == 13) 13296 else 6565 + player.gravestone * 3, deathTile, true) {
    private val username: String
    private var ticks: Int = 0
    private val inscription: Array<String>
    private val floorItems: MutableList<FloorItem>
    private val graveStone = player.gravestone
    private var blessed: Boolean = false

    companion object {
        private val gravestones = ArrayList<Gravestone>()

        fun linkPlayer(player: Player) {
            synchronized(gravestones) {
                val gravestone = getGravestoneByUsername(player.username) ?: return
                gravestone.setPlayer(player)
            }
        }

        private fun getGravestoneByUsername(username: String): Gravestone? {
            return gravestones.firstOrNull { it.username == username }
        }
    }

    private fun setPlayer(player: Player) {
        player.send(GravestoneTimer((ticks * 600) / 1000))
        player.send(EntityHint(this))
    }

    override fun processNpc() {
        ticks--
        when (ticks) {
            0 -> {
                decrementGrave(-1, "Your grave has collapsed!")
                return
            }
            50 -> decrementGrave(2, "Your grave is collapsing.")
            100 -> decrementGrave(1, "Your grave is about to collapse.")
        }
    }

    private fun addLeftTime(clean: Boolean) {
        if (clean) {
            for (item in floorItems)
                FloorItemManager.turnPublic(item, 60, false)
        } else {
            GameExecutorManager.slowExecutor.schedule({
                for (item in floorItems)
                    FloorItemManager.turnPublic(item, 60, false)
            }, (ticks * 0.6).toLong(), TimeUnit.SECONDS)
        }
    }

    override fun deregister() {
        synchronized(gravestones) {
            gravestones.remove(this)
        }
        getPlayer()?.send(EntityHintRemoval())
        super.deregister()
    }

    private fun getPlayer(): Player? {
        return World.getPlayerByName(username)
    }

    private fun decrementGrave(stage: Int, message: String) {
        getPlayer()?.message(message, 0x7e2217)
        if (stage == -1) {
            addLeftTime(true)
            deregister()
        } else
            setTransformation(getNpcId(graveStone) + stage)
    }

    fun sendGraveInscription(reader: Player) {
        when {
            ticks <= 50 -> reader.dialogueFactory.sendStatement("The inscription is too unclear to read.")
            reader === getPlayer() -> reader.dialogueFactory.sendStatement("It looks like it'll survive another " + ticks / 100 + " minutes.", "Isn't there something a bit odd about reading your own gravestone?")
            else -> reader.dialogueFactory.sendStatement(*inscription)
        }
    }

    fun repair(friend: Player, bless: Boolean) {
        if (friend.skills.getLevel(Skill.PRAYER) < (if (bless) 70 else 2)) {
            friend.message("You need " + (if (bless) 70 else 2) + " prayer to " + (if (bless) "bless" else "repair") + " a gravestone.")
            return
        }
        if (friend.username == username) {
            friend.message("The gods don't seem to approve of people attempting to " + (if (bless) "bless" else "repair") + " their own gravestones.")
            return
        }
        if (bless && blessed) {
            friend.message("This gravestone has already been blessed.")
            return
        } else if (!bless && ticks > 100) {
            friend.message("This gravestone doesn't seem to need repair.")
            return
        }
        ticks += if (bless) 1000 else 500 // 5minutes, 1hour
        blessed = true
        decrementGrave(0, friend.username + " has " + (if (bless) "blessed" else "repaired") + " your gravestone. It should survive another " + ticks / 100 + " minutes.")
        friend.message("You " + (if (bless) "bless" else "repair") + " the grave.")
        friend.movement.lock(2)
        friend.perform(Animation(645))
    }

    fun demolish(demolisher: Player) {
        if (demolisher.username != username) {
            demolisher.message("It would be impolite to demolish someone else's gravestone.")
            return
        }
        addLeftTime(true)
        deregister()
        demolisher.send(GravestoneTimer(0))
        demolisher.message("It looks like it'll survive another " + ticks / 100 + " minutes. You demolish it anyway.")
    }


    private fun getNpcId(currentGrave: Int): Int {
        return if (currentGrave == 13) 13296 else 6565 + currentGrave * 3
    }

    private fun getInscription(username: String): Array<String> {
        val name = Misc.formatPlayerNameForDisplay(username)
        when (graveStone) {
            0, 1 -> return arrayOf("In memory of <i>$name</i>,", "who died here.")
            2, 3 -> return arrayOf("In loving memory of our dear friend <i>$name</i>, who", "died in this place several minutes ago.")
            4, 5 -> return arrayOf("In your travels, pause awhile to remember <i>$name</i>,", "who passed away at this spot.")
            6 -> return arrayOf("<i>$name</i>, ", "an enlightened servant of Saradomin,", "perished in this place.")
            7 -> return arrayOf("<i>$name</i>, ", "a most bloodthirsty follower of Zamorak,", "perished in this place.")
            8 -> return arrayOf("<i>$name</i>, ", "who walked with the Balance of Guthix,", "perished in this place.")
            9 -> return arrayOf("<i>$name</i>, ", "a vicious warrior dedicated to Bandos,", "perished in this place.")
            10 -> return arrayOf("<i>$name</i>, ", "a follower of the Law of Armadyl,", "perished in this place.")
            11 -> return arrayOf("<i>$name</i>, ", "servant of the Unknown Power,", "perished in this place.")
            12 -> return arrayOf("Ye frail mortals who gaze upon this sight, forget not", "the fate of <i>$name</i>, once mighty,", "now surrendered to the inescapable grasp of destiny.", "<i>Rest in peace.</i>")
            13 -> return arrayOf("Here lies <i>$name</i>, friend of dwarves. Great in", "life, glorious in death. His/Her name lives on in", "song and story.")
        }
        return arrayOf("Cabbage")
    }

    init {
        direction.direction = Direction.SOUTH
        perform(Animation(if (graveStone == 1) 7396 else 7394))
        username = player.username
        ticks = GravestoneSelection.getSeconds(graveStone) * 1000 / 600
        inscription = getInscription(player.username)
        floorItems = items
                .mapNotNull { FloorItemManager.addGroundItem(it, deathTile, player, true, -1, true, -1) }
                .toMutableList()
        synchronized(gravestones) {
            val oldStone = getGravestoneByUsername(username)
            if (oldStone != null) {
                oldStone.addLeftTime(false)
                oldStone.deregister()
            }
            gravestones.add(this)
        }
        setPlayer(player)
        player.message("Your items have been dropped at your gravestone, where they'll remain until it crumbles.");
        player.message("It looks like it'll survive another " + ticks / 100 + " minutes.")
    }
}