package com.fury.game.content.controller

import com.fury.game.content.global.Achievements
import com.fury.game.content.global.action.Action
import com.fury.game.content.global.config.ConfigConstants
import com.fury.game.content.skill.Skill
import com.fury.game.content.skill.free.mining.MiningBase
import com.fury.game.content.skill.free.woodcutting.Woodcutting
import com.fury.game.entity.`object`.GameObject
import com.fury.game.entity.character.combat.effects.Effect
import com.fury.game.entity.character.combat.effects.Effects
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.entity.character.player.link.transportation.TeleportHandler
import com.fury.game.entity.character.player.link.transportation.TeleportType
import com.fury.core.model.item.Item
import com.fury.game.world.map.Position
import com.fury.util.Colours
import com.fury.util.RandomUtils

class Abyss : Controller() {

    //TODO monster anims, combat, drops, multi-combat
    enum class Obstacles(val ids: IntArray, val level: Int, val skill: Skill?, val animation: Int, val delay: Int, val start: String, val success: String, val fail: String) {
        BOIL(intArrayOf(7145, 7151), 30, Skill.FIREMAKING, 733, 5, "You attempt to burn away the boil...", "...and you manage to burn down the boil.", "...but fail to burn the boil."),
        EYES(intArrayOf(7146, 7150), 40, Skill.THIEVING, 3, 3, "You use your thieving skills to misdirect the eyes", "...and you manage to distract the eyes.", "...but fail to distract them enough to get past."),
        TENDRILS(intArrayOf(7144, 7152), 50, Skill.WOODCUTTING, -1, 2, "You attempt to chop your way through...", "...and manage to chop through the tendrils.", "...but fail to chop up the tendrils."),
        ROCK(intArrayOf(7143, 7153), 60, Skill.MINING, -1, 8, "You attempt to mine your way through...", "...and manage to break through the rock.", "...but fail to break-up the rock."),
        GAP(intArrayOf(7147, 7149), 70, Skill.AGILITY, 845, 1, "You attempt to squeeze through the narrow gap...", "...and you manage to crawl through.", "...but fail to crawl through."),
        PASSAGE(intArrayOf(7148), -1, null, -1, 2, "You make your way through the passage...", "...and make it to the other side.", ""),
        BLOCKAGE(intArrayOf(7156), -1, null, -1, -1, "", "", "");
    }

    companion object {
        //Couldn't figure out a pattern
        private val oneToFive = arrayOf(Obstacles.BLOCKAGE, Obstacles.ROCK, Obstacles.TENDRILS, Obstacles.BOIL, Obstacles.EYES, Obstacles.GAP, Obstacles.PASSAGE, Obstacles.GAP, Obstacles.EYES, Obstacles.BOIL, Obstacles.TENDRILS, Obstacles.ROCK)
        private val six = arrayOf(Obstacles.BLOCKAGE, Obstacles.GAP, Obstacles.EYES, Obstacles.BOIL, Obstacles.TENDRILS, Obstacles.ROCK, Obstacles.PASSAGE, Obstacles.ROCK, Obstacles.TENDRILS, Obstacles.BOIL, Obstacles.EYES, Obstacles.GAP)
        private val seven = arrayOf(Obstacles.BLOCKAGE, Obstacles.GAP, Obstacles.EYES, Obstacles.BOIL, Obstacles.TENDRILS, Obstacles.GAP, Obstacles.PASSAGE, Obstacles.ROCK, Obstacles.TENDRILS, Obstacles.BOIL, Obstacles.EYES, Obstacles.GAP)
        private val eight = arrayOf(Obstacles.BLOCKAGE, Obstacles.GAP, Obstacles.EYES, Obstacles.BOIL, Obstacles.EYES, Obstacles.ROCK, Obstacles.PASSAGE, Obstacles.ROCK, Obstacles.TENDRILS, Obstacles.BOIL, Obstacles.EYES, Obstacles.GAP)
        private val nine = arrayOf(Obstacles.BLOCKAGE, Obstacles.GAP, Obstacles.EYES, Obstacles.BOIL, Obstacles.EYES, Obstacles.ROCK, Obstacles.PASSAGE, Obstacles.ROCK, Obstacles.TENDRILS, Obstacles.BOIL, Obstacles.EYES, Obstacles.GAP)
        private val ten = arrayOf(Obstacles.BLOCKAGE, Obstacles.GAP, Obstacles.TENDRILS, Obstacles.BOIL, Obstacles.TENDRILS, Obstacles.ROCK, Obstacles.PASSAGE, Obstacles.ROCK, Obstacles.TENDRILS, Obstacles.BOIL, Obstacles.EYES, Obstacles.GAP)
        private val eleven = arrayOf(Obstacles.BLOCKAGE, Obstacles.ROCK, Obstacles.TENDRILS, Obstacles.BOIL, Obstacles.TENDRILS, Obstacles.ROCK, Obstacles.PASSAGE, Obstacles.ROCK, Obstacles.TENDRILS, Obstacles.BOIL, Obstacles.EYES, Obstacles.GAP)

        private val outerPositions = arrayOf(Position(3041, 4809), Position(3025, 4811), Position(3016, 4823), Position(3015, 4835), Position(3018, 4844), Position(3029, 4853), Position(3039, 4856), Position(3051, 4853), Position(3061, 4841), Position(3064, 4831), Position(3060, 4820), Position(3051, 4810))
        private val innerPositions = arrayOf(Position(3041, 4818), Position(3029, 4823), Position(3028, 4825), Position(3025, 4833), Position(3028, 4840), Position(3031, 4842), Position(3038, 4845), Position(3048, 4842), Position(3051, 4839), Position(3053, 4830), Position(3050, 4823), Position(3048, 4822))
    }

    override fun start() {
        val rotation = RandomUtils.inclusive(0, 11)
        player.config.send(ConfigConstants.ABYSS_OBSTACLES, rotation, true)
        TeleportHandler.teleportPlayer(player, outerPositions[rotation], TeleportType.NORMAL)
        player.effects.startEffect(Effect(Effects.SKULL, -1))
        player.skills.setLevel(Skill.PRAYER, 0)
        if(!player.effects.hasActiveEffect(Effects.SKULL))
            player.message("You have been skulled!", Colours.RED)
    }

    override fun magicTeleported(type: Int) {
        leave()
    }

    override fun forceClose() {
        leave()
    }

    private fun leave() {
        player.effects.startEffect(Effect(Effects.SKULL, 300))
        removeController()
    }

    private fun wrapIndex(index: Int): Int {
        return if (index < 0) oneToFive.size + index else if (index > oneToFive.size - 1) oneToFive.size - 1 - index.rem(oneToFive.size - 1) else index
    }

    private fun getArray(rotation: Int): Array<Obstacles> {
        return when (rotation) {
            6 -> six
            7 -> seven
            8 -> eight
            9 -> nine
            10 -> ten
            11 -> eleven
            else -> oneToFive
        }
    }

    private fun getType(index: Int, rotation: Int): Obstacles {
        return if (rotation >= 6)
            getArray(rotation)[wrapIndex(index - rotation).rem(oneToFive.size)]
        else
            oneToFive[wrapIndex(index - rotation)]
    }

    override fun processObjectClick1(gameObject: GameObject): Boolean {
        if (gameObject.id in 7142..7153) {
            val rotation = player.config.get(ConfigConstants.ABYSS_OBSTACLES)
            val index = gameObject.id - 7142
            val obstacle = getType(index, rotation)
            player.actionManager.action = Navigate(obstacle, index)
            return false
        }

        if(gameObject.id in 7129..7141) {
            when (gameObject.id) {
                7129 -> player.moveTo(2591, 4838)//Fire
                7130 -> player.moveTo(2658, 4847)//Earth
                7131 -> player.moveTo(2523, 4846)//Body
                7132 -> player.moveTo(2136, 4833)//Cosmic
                7133 -> player.moveTo(2400, 4847)//Nature
                7134 -> player.moveTo(2281, 4837)//Chaos
                7135 -> {
                    Achievements.finishAchievement(player, Achievements.AchievementData.TRAVEL_TO_THE_LAW_ALTAR_BY_ABYSS)
                    player.moveTo(2464, 4839)//Law
                }
                7136 -> player.moveTo(2205, 4841)//Death
                7137 -> player.moveTo(3478, 4836)//Water
                7138 -> player.message("A strange power blocks your entrance.")//Soul
                7139 -> player.moveTo(2844, 4840)//Air
                7140 -> player.moveTo(2786, 4847)//Mind
                7141 -> player.moveTo(2460, 4901, 1)//Blood
            }
            if(gameObject.id != 7138)
                leave()
            return false
        }
        return true
    }

    class Navigate(private val obstacle: Obstacles, private val index: Int) : Action() {
        private var animation = obstacle.animation

        override fun start(player: Player): Boolean {
            if (!hasReq(player))
                return false

            if (animation != -1)
                player.animate(animation)
            if (obstacle.start.isNotEmpty())
                player.message(obstacle.start, true)

            if(obstacle.delay != -1)
                setActionDelay(player, obstacle.delay)
            return true
        }

        override fun process(player: Player): Boolean {
            return hasReq(player)
        }

        private fun hasReq(player: Player): Boolean {

            if (obstacle.level != -1 && !player.skills.hasRequirement(obstacle.skill, obstacle.level, "pass this obstacle"))
                return false

            if(obstacle == Obstacles.BOIL) {
                if(!player.inventory.contains(Item(590))) {
                    player.message("You do not have a tinderbox or the required level to burn this boil.")
                    return false
                }
            }

            if (obstacle == Obstacles.ROCK) {
                val definition = MiningBase.getPickaxe(player, false)

                if (definition == null) {
                    player.message("You do not have a pickaxe or do not have the required level to use the pickaxe.")
                    return false
                } else {
                    animation = definition.animationId
                }
            }

            if (obstacle == Obstacles.TENDRILS) {
                val definition = Woodcutting.getHatchet(player, false)

                if (definition == null) {
                    player.message("You do not have a hatchet that you can use.")
                    return false
                } else {
                    animation = definition.emoteId
                }
            }

            return true
        }

        override fun processWithDelay(player: Player): Int {
            val level = if (obstacle.skill == null) 100 else player.skills.getLevel(obstacle.skill)
            if (RandomUtils.success((level + 1.0) / 100.0)) {

                if (obstacle.skill != null)
                    player.skills.addExperience(obstacle.skill, 25.0)

                player.moveTo(innerPositions[index])

                if (obstacle.success.isNotEmpty())
                    player.message(obstacle.success, true)
                return -1
            } else {
                if (obstacle.fail.isNotEmpty())
                    player.message(obstacle.fail, true)

                if (animation != -1)
                    player.animate(animation)

                if (obstacle.start.isNotEmpty())
                    player.message(obstacle.start, true)
            }
            return obstacle.delay
        }

        override fun stop(player: Player) {
            player.animate(-1)
        }

    }

    override fun login(): Boolean {
        return false//To not remove the controller
    }

    override fun logout(): Boolean {
        return false//To not remove the controller
    }
}