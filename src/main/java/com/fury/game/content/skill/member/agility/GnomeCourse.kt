package com.fury.game.content.skill.member.agility

import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.core.task.Task
import com.fury.game.content.dialogue.impl.skills.agility.CourseCompletionD
import com.fury.game.content.dialogue.impl.skills.agility.CourseFailD
import com.fury.game.content.global.Achievements
import com.fury.game.content.skill.Skill
import com.fury.game.entity.`object`.GameObject
import com.fury.game.entity.character.player.actions.ForceMovement
import com.fury.game.node.entity.actor.`object`.ObjectHandler
import com.fury.game.node.entity.actor.figure.player.handles.Settings
import com.fury.game.world.GameWorld
import com.fury.game.world.map.Position
import com.fury.game.world.map.path.RouteEvent
import com.fury.game.world.update.flag.block.Animation

object GnomeCourse {

    @JvmStatic
    fun handleObstacles(player: Player, `object`: GameObject, route: Boolean): Boolean {
        if (route) {
            when (`object`.id) {
                2295 -> {
                    walkLog(player)
                    return true
                }
                2285 -> {
                    climbObstacleNet(player)
                    return true
                }
                35970 -> {
                    climbBranch(player)
                    return true
                }
                2312 -> {
                    walkRope(player)
                    return true
                }
                4059 -> {
                    walkBackRope(player)
                    return true
                }
                2314, 2315 -> {
                    climbDownBranch(player)
                    return true
                }
                2286 -> {
                    climbObstacleNet(player, `object`)
                    return true
                }
                43544, 43543 -> {
                    if (`object`.y == 3431)
                        enterPipe(player, `object`.x, `object`.y)
                    return true
                }
            //Second floor
                43528 -> {
                    climbTree(player)
                    return true
                }
                43539 -> {
                    if (player.x != 2485 || player.y != 3433)
                        player.movement.addWalkSteps(2485, 3433)
                    jumpDown(player)
                    return true
                }
            }
        } else {
            if (`object`.id == 43581) {
                if (player.sameAs(Position(2477, 3418, 3))) {
                    runAcrossSign(player, `object`)
                    return true
                } else {
                    player.stopAll(false)
                    player.routeEvent = RouteEvent(Position(2477, 3418, 3)) { runAcrossSign(player, `object`) }
                    return true
                }
            } else if (`object`.id == 43529) {
                if (player.sameAs(Position(2486, 3418, 3))) {
                    preSwing(player, `object`)
                    return true
                } else {
                    player.stopAll(false)
                    player.routeEvent = RouteEvent(Position(2486, 3418, 3)) { preSwing(player, `object`) }
                    return true
                }
            }

        }
        return false
    }

    /**
     * Floor one
     */

    private fun walkLog(player: Player) {
        if (player.x != 2474 || player.y != 3436)
            return
        player.movement.addWalkSteps(2474, 3429, false)
        val running = player.settings.getBool(Settings.RUNNING)
        player.settings.set(Settings.RUNNING, false)
        player.message("You walk carefully across the slippery log...", true)
        player.movement.lock()
        player.skillAnimation = Animation(762)
        GameWorld.schedule(8, {
            player.skillAnimation = null
            setGnomeStage(player, 0)
            player.movement.unlock()
            player.settings.set(Settings.RUNNING, running)
            Agility.addExperience(player, 7.5)
            player.message("... and make it safely to the other side.", true)
            Achievements.finishAchievement(player, Achievements.AchievementData.CROSS_A_LOG_BALANCE)
        })
    }

    private fun climbObstacleNet(player: Player) {
        if (player.y != 3426)
            return
        player.message("You climb the netting.", true)
        ObjectHandler.useStairs(player, 828, Position(player.x, 3423, 1), 1, 2)
        GameWorld.schedule(2, {
            if (getGnomeStage(player) == 0)
                setGnomeStage(player, 1)
            Agility.addExperience(player, 7.5)
        })
    }

    private fun climbBranch(player: Player) {
        player.message("You climb the tree...", true)
        ObjectHandler.useStairs(player, 828, Position(2473, 3420, 2), 1, 2, "... to the platform above.")
        GameWorld.schedule(2, {
            if (getGnomeStage(player) == 1)
                setGnomeStage(player, 2)
            Agility.addExperience(player, 5.0)
        })
    }

    private fun walkRope(player: Player) {
        if (player.x != 2477 || player.y != 3420 || player.z != 2)
            return
        val running = player.settings.getBool(Settings.RUNNING)
        player.settings.set(Settings.RUNNING, false)
        player.movement.addWalkSteps(2483, 3420, false)
        player.movement.lock()
        player.skillAnimation = Animation(762)
        GameWorld.schedule(7, {
            player.skillAnimation = null
            player.settings.set(Settings.RUNNING, running)
            if (getGnomeStage(player) == 2)
                setGnomeStage(player, 3)
            player.movement.unlock()
            Agility.addExperience(player, 7.5)
            player.message("You passed the obstacle successfully.", true)
        })
    }

    private fun walkBackRope(player: Player) {
        if (player.x != 2483 || player.y != 3420 || player.z != 2)
            return
        val running = player.settings.getBool(Settings.RUNNING)
        player.settings.set(Settings.RUNNING, false)
        player.movement.addWalkSteps(2477, 3420, false)
        player.movement.lock()
        player.skillAnimation = Animation(762)
        GameWorld.schedule(7,  {
            player.movement.unlock()
            player.skillAnimation = null
            player.settings.set(Settings.RUNNING, running)
            Agility.addExperience(player, 7.0)
            player.message("You passed the obstacle successfully.", true)
        })
    }

    private fun climbDownBranch(player: Player) {
        ObjectHandler.useStairs(player, 828, Position(2487, 3421), 1, 2, "You climbed the tree branch successfully.")
        GameWorld.schedule(2,  {
            if (getGnomeStage(player) == 3)
                setGnomeStage(player, 4)
            Agility.addExperience(player, 5.0)
        })
    }

    private fun climbObstacleNet(player: Player, `object`: GameObject) {
        if (player.y != 3425)
            return
        val toTile = Position(player.x, `object`.y + 1, 0)
        player.movement.lock()
        player.direction.face(toTile)
        player.message("You climb the netting.", true)
        player.animate(16356)
        GameWorld.schedule(3,  {
            player.moveTo(toTile)
            if (getGnomeStage(player) == 4)
                setGnomeStage(player, 5)
            Agility.addExperience(player, 7.5)
            player.movement.unlock()
        })
    }

    private fun enterPipe(player: Player, objectX: Int, objectY: Int) {
        player.movement.lock()
        player.movement.reset()
        player.movement.addWalkSteps(objectX, objectY - 1, false)
        player.message("You pull yourself through the pipes.", true)
        GameWorld.schedule(object : Task(true, 1) {
            internal var tick: Int = 0

            override fun run() {
                when (tick) {
                    0 -> player.forceMovement = ForceMovement(player, 1, Position(objectX, player.y + 3), 4, ForceMovement.NORTH)
                    1 -> player.animate(10580)
                    3 -> player.setPosition(Position(objectX, player.y + 3, 0))
                    4 -> player.forceMovement = ForceMovement(player, 1, Position(objectX, player.y + 4), 5, ForceMovement.NORTH)
                    7 -> {
                        player.animate(10580)
                        player.setPosition(Position(objectX, player.y + 4, 0))
                    }
                    8 -> {
                        if (getGnomeStage(player) == 5) {
                            removeGnomeStage(player)
                            player.skills.addExperience(Skill.AGILITY, 39.5)
                            Agility.giveTickets(player, 3)
                            player.dialogueManager.startDialogue(CourseCompletionD())
                        } else {
                            player.dialogueManager.startDialogue(CourseFailD())
                        }
                        player.moveTo(player)
                        Agility.addExperience(player, 7.5)
                        player.movement.unlock()
                        stop()
                    }
                }
                tick++
            }
        })
    }


    /**
     * Floor two
     */
    private fun climbTree(player: Player) {
        if (!Agility.hasLevel(player, 85))
            return
        ObjectHandler.useStairs(player, 828, Position(2472, 3419, 3), 1, 2, "You climbed the tree successfully.")
        GameWorld.schedule(2,  {
            removeGnomeStage(player)
            Agility.addExperience(player, 25.0)
        })
    }

    private fun runAcrossSign(player: Player, `object`: GameObject) {
        player.movement.lock()
        player.direction.face(Position(2478, 3418, 3))
        player.animate(2922)
        val toTile = Position(2484, 3418, `object`.z)
        player.forceMovement = ForceMovement(Position(2477, 3418, 3), 1, toTile, 3, ForceMovement.EAST)
        player.message("You skilfully run across the Board", true)
        GameWorld.schedule(2,  {
            player.moveTo(toTile)
            player.movement.unlock()
            Agility.addExperience(player, 25.0)
        })
    }

    private fun preSwing(player: Player, `object`: GameObject) {
        player.movement.lock()
        player.direction.face(Position(2486, 3432, 3))
        player.resetPosition = Position(2486, 3421, 3)
        val firstTile = Position(player.x, 3421, `object`.z)
        val secondTile = Position(player.x, 3425, `object`.z)
        val thirdTile = Position(player.x, 3429, `object`.z)
        val fourthTile = Position(player.x, 3432, `object`.z)
        GameWorld.schedule(object : Task(true, 1) {
            internal var stage: Int = 0

            override fun run() {
                when (stage) {
                    0 -> {
                        player.animate(11784)
                        player.forceMovement = ForceMovement(player, 0, firstTile, 2, ForceMovement.NORTH)
                    }
                    2 -> {
                        player.moveTo(firstTile)
                        player.animate(11785)
                        player.forceMovement = ForceMovement(player, 0, secondTile, 1, ForceMovement.NORTH)
                    }
                    3 -> {
                        player.animate(11789)
                        player.setPosition(secondTile)
                    }
                    5 -> player.forceMovement = ForceMovement(player, 2, thirdTile, 3, ForceMovement.NORTH)
                    8 -> player.setPosition(thirdTile)
                    12 -> player.forceMovement = ForceMovement(player, 0, fourthTile, 1, ForceMovement.NORTH)
                    14 -> {
                        Agility.addExperience(player, 25.0)
                        player.moveTo(fourthTile)
                        player.resetPosition = null
                        player.movement.unlock()
                    }
                }
                stage++
            }
        })
    }

    private fun jumpDown(player: Player) {
        player.movement.lock(2)
        val tile = Position(2485, 3434, 3)
        player.animate(2923)
        player.forceMovement = ForceMovement(player, 0, tile, 1, ForceMovement.NORTH)
        GameWorld.schedule(2, {
            player.moveTo(2485, 3436)
            player.animate(2924)
            player.dialogueManager.startDialogue(CourseCompletionD())
            Agility.giveTickets(player, 6)
            player.skills.addExperience(Skill.AGILITY, 630.0)
        })
    }

    private const val KEY = "GnomeCourse"

    fun removeGnomeStage(player: Player) {
        player.temporaryAttributes.remove(KEY)
    }

    fun setGnomeStage(player: Player, stage: Int) {
        player.temporaryAttributes[KEY] = stage
    }

    fun getGnomeStage(player: Player): Int {
        return if(player.temporaryAttributes[KEY] == null) 0 else player.temporaryAttributes[KEY] as Int
    }
}
