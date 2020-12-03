package com.fury.game.content.skill.member.agility

import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.core.task.Task
import com.fury.game.content.dialogue.impl.skills.agility.CourseCompletionD
import com.fury.game.content.skill.Skill
import com.fury.game.entity.`object`.GameObject
import com.fury.game.entity.character.player.actions.ForceMovement
import com.fury.game.node.entity.actor.`object`.ObjectHandler
import com.fury.game.node.entity.actor.figure.player.handles.Settings
import com.fury.game.world.GameWorld
import com.fury.game.world.World
import com.fury.game.world.map.Position
import com.fury.game.world.update.flag.block.Animation

object BarbarianCourse {

    @JvmStatic

    fun handleObstacles(player: Player, `object`: GameObject): Boolean {
        when (`object`.id) {
            20210 -> {
                enterPipe(player, `object`)
                return true
            }
            43526 -> {
                ropeSwing(player, `object`)
                return true
            }
            43595 -> {
                crossLog(player, `object`)
                return true
            }
            20211 -> {
                climbNet(player, `object`)
                return true
            }
            2302 -> {
                walkLedge(player, `object`)
                return true
            }
            1948 -> {
                climbCrumblingWall(player, `object`)
                return true
            }
        //Advanced
            43533 -> {
                runUpWall(player)
                return true
            }
            43597 -> {
                if (player.x != 2537 || player.y != 3546) {
                    player.movement.addWalkSteps(2537, 3546)
                    GameWorld.schedule(1, {
                        player.direction.face(Position(2536, 3546))
                        climbUpWall(player)
                    })
                } else
                    climbUpWall(player)
                return true
            }
            43587 -> {
                fireSpring(player, `object`)
                return true
            }
            43527 -> {
                crossBeam(player)
                return true
            }
            43531 -> {
                jumpGap(player)
                return true
            }
            43532 -> {
                roofSlide(player)
                return true
            }
            else -> return false
        }
    }

    /**
     * Basic course
     */

    private fun enterPipe(player: Player, `object`: GameObject) {
        if (!Agility.hasLevel(player, 35))
            return
        player.movement.lock()
        player.animate(10580)
        val toTile = Position(`object`.x, if (player.y >= 3561) 3558 else 3561, `object`.z)
        player.forceMovement = ForceMovement(player, 0, toTile, 2, if (player.y >= 3561) ForceMovement.SOUTH else ForceMovement.NORTH)
        GameWorld.schedule(1) {
            player.moveTo(toTile)
            player.movement.unlock()
        }
    }

    private fun ropeSwing(player: Player, `object`: GameObject) {
        if (!Agility.hasLevel(player, 35))
            return
        if (player.y != 3556) {
            player.message("You'll need to get closer to make this jump.")
            return
        }
        player.movement.lock()
        player.animate(751)
        World.sendObjectAnimation(player, `object`, Animation(497))
        val toTile = Position(`object`.x, 3549, `object`.z)
        player.forceMovement = ForceMovement(player, 0, toTile, 3, ForceMovement.SOUTH)
        Agility.addExperience(player, 22.0)
        player.message("You skilfully swing across.", true)
        GameWorld.schedule(1) {
            player.movement.unlock()
            player.moveTo(toTile)
            setStage(player, 0)
        }
    }

    private fun crossLog(player: Player, `object`: GameObject) {
        if (!Agility.hasLevel(player, 35))
            return
        if (player.y != `object`.y) {
            player.movement.addWalkSteps(2551, 3546, false)
            player.movement.lock()
            GameWorld.schedule(1) { walkLogBalance(player, `object`) }
        } else
            walkLogBalance(player, `object`)
    }

    private fun walkLogBalance(player: Player, `object`: GameObject) {
        val running = player.settings.getBool(Settings.RUNNING)
        player.settings.set(Settings.RUNNING, false)
        player.message("You walk carefully across the slippery log...", true)
        player.movement.lock()
        val tile = Position(2541, `object`.y, `object`.z)
        player.skillAnimation = Animation(9908)
        player.movement.addWalkSteps(tile.x, tile.y, false)
        GameWorld.schedule(10) {
            player.moveTo(tile)
            player.movement.unlock()
            player.skillAnimation = null
            player.movement.reset()
            player.settings.set(Settings.RUNNING, running)
            Agility.addExperience(player, 13.0)
            player.message("... and make it safely to the other side.", true)
            if (getStage(player) == 0)
                setStage(player, 1)
        }
    }

    private fun climbNet(player: Player, `object`: GameObject) {
        if (!Agility.hasLevel(player, 35) || player.y < 3545 || player.y > 3547)
            return
        player.message("You climb the netting...", true)
        Agility.addExperience(player, 8.2)
        ObjectHandler.useStairs(player, 828, Position(`object`.x - 1, player.y, 1), 1, 2)
        if (getStage(player) == 1)
            setStage(player, 2)
    }

    fun walkLedge(player: Player, `object`: GameObject) {
        if (!Agility.hasLevel(player, 35))
            return
        player.message("You put your foot on the ledge and try to edge across...", true)
        player.movement.lock()
        player.animate(753)
        player.skillAnimation = Animation(756)
        val toTile = Position(2532, `object`.y, `object`.z)
        val running = player.settings.getBool(Settings.RUNNING)
        player.settings.set(Settings.RUNNING, false)
        player.movement.addWalkSteps(toTile.x, toTile.y, false)
        GameWorld.schedule(5) {
            player.movement.unlock()
            player.animate(759)
            player.skillAnimation = null
            player.settings.set(Settings.RUNNING, running)
            Agility.addExperience(player, 22.0)
            player.message("You skilfully edge across the gap.", true)
            if (getStage(player) == 2)
                setStage(player, 3)
        }
    }

    fun climbCrumblingWall(player: Player, `object`: GameObject) {
        if (!Agility.hasLevel(player, 35))
            return
        if (player.x >= `object`.x) {
            player.message("You cannot climb that from this side.")
            return
        }
        player.message("You climb the low wall...", true)
        player.movement.lock()
        player.animate(4853)
        val toTile = Position(`object`.x + 1, `object`.y, `object`.z)
        player.forceMovement = ForceMovement(player, 0, toTile, 2, ForceMovement.EAST)
        GameWorld.schedule(1) {
            player.moveTo(toTile)
            player.movement.unlock()
            Agility.addExperience(player, 13.7)
            val stage = getStage(player)
            if (stage == 3)
                setStage(player, 4)
            else if (stage == 4) {
                removeStage(player)
                player.dialogueManager.startDialogue(CourseCompletionD())
                Agility.giveTickets(player, 4)
                player.skills.addExperience(Skill.AGILITY, 46.2)
            }
        }
    }


    /**
     * Advanced course
     */

    private fun runUpWall(player: Player) {
        if (!Agility.hasLevel(player, 90))
            return
        player.movement.lock()
        val toTile = Position(2538, 3545, 2)
        player.forceMovement = ForceMovement(player, 7, toTile, 8, ForceMovement.NORTH)
        player.animate(10492)
        GameWorld.schedule(object : Task(7) {
            override fun run() {
                player.movement.unlock()
                player.animate(10493)
                player.moveTo(toTile)
                Agility.addExperience(player, 15.0)
                stop()
            }
        })
    }

    private fun climbUpWall(player: Player) {
        if (!Agility.hasLevel(player, 90))
            return
        ObjectHandler.useStairs(player, 10023, Position(2536, 3546, 3), 2, 3)
        GameWorld.schedule(2) {
            player.animate(11794)
            Agility.addExperience(player, 15.0)
        }
    }

    private fun fireSpring(player: Player, `object`: GameObject) {
        if (!Agility.hasLevel(player, 90))
            return
        player.movement.lock()
        player.movement.addWalkSteps(2533, 3547, false)
        val toTile = Position(2532, 3553, 3)
        GameWorld.schedule(object : Task(false, 1) {
            internal var second: Boolean = false
            override fun run() {
                if (!second) {
                    player.forceMovement = ForceMovement(player, 1, toTile, 3, ForceMovement.NORTH)
                    player.animate(4189)
                    World.sendObjectAnimation(player, `object`, Animation(11819))
                    second = true
                } else {
                    player.moveTo(toTile)
                    player.movement.unlock()
                    Agility.addExperience(player, 15.0)
                    stop()
                }
            }
        })
    }

    private fun crossBeam(player: Player) {
        if (!Agility.hasLevel(player, 90))
            return
        player.movement.lock()
        val toTile = Position(2536, 3553, 3)
        player.forceMovement = ForceMovement(player, 1, toTile, 3, ForceMovement.EAST)
        player.animate(16079)
        GameWorld.schedule(2) {
            player.movement.unlock()
            player.moveTo(toTile)
            Agility.addExperience(player, 15.0)
        }
    }

    private fun jumpGap(player: Player) {
        if (!Agility.hasLevel(player, 90))
            return
        player.movement.lock()
        player.animate(2586)
        GameWorld.schedule(1) {
            player.movement.unlock()
            player.moveTo(2538, 3553, 2)
            player.animate(2588)
            Agility.addExperience(player, 15.0)
        }
    }

    private fun roofSlide(player: Player) {
        if (!Agility.hasLevel(player, 90))
            return
        val running = player.settings.getBool(Settings.RUNNING)
        player.settings.set(Settings.RUNNING, false)
        player.movement.lock()
        player.animate(11792)
        val tile = Position(2544, player.y, 0)
        player.forceMovement = ForceMovement(player, 0, tile, 5, ForceMovement.EAST)

        GameWorld.schedule(object : Task(false, 1) {
            internal var tick: Int = 0
            override fun run() {
                when (tick) {
                    0 -> {
                        player.moveTo(2541, player.y, 1)
                        player.animate(11790)
                    }
                    1 -> player.animate(11790)
                    2 -> player.animate(11791)
                    3 -> {
                        player.forceMovement = null
                        player.moveTo(tile)
                        player.animate(2588)
                        Agility.addExperience(player, 15.0)
                        player.movement.unlock()
                        player.settings.set(Settings.RUNNING, running)
                        if (getStage(player) == 1) {
                            removeStage(player)
                            player.dialogueManager.startDialogue(CourseCompletionD())
                            Agility.giveTickets(player, 7)
                            player.skills.addExperience(Skill.AGILITY, 615.0)
                        }
                    }
                    else -> stop()
                }
                tick++
            }
        })
    }

    private const val KEY = "BarbarianOutpostCourse"

    private fun removeStage(player: Player) {
        player.temporaryAttributes.remove(KEY)
    }

    fun setStage(player: Player, stage: Int) {
        player.temporaryAttributes[KEY] = stage
    }

    fun getStage(player: Player): Int {
        return if(player.temporaryAttributes[KEY] == null) 0 else player.temporaryAttributes[KEY] as Int
    }
}
