package com.fury.game.content.skill.member.agility

import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.core.task.Task
import com.fury.game.content.dialogue.impl.skills.agility.CourseCompletionD
import com.fury.game.content.skill.Skill
import com.fury.game.entity.`object`.GameObject
import com.fury.game.entity.character.player.actions.ForceMovement
import com.fury.game.node.entity.actor.figure.player.handles.Settings
import com.fury.game.world.GameWorld
import com.fury.game.world.World
import com.fury.game.world.map.Position
import com.fury.game.world.map.path.RouteEvent
import com.fury.game.world.update.flag.block.Animation

object WildernessCourse {

    @JvmStatic
    fun handleObstacles(player: Player, `object`: GameObject, route: Boolean): Boolean {
        when {
            route -> when (`object`.id) {
                2309 -> {
                    enterGate(player, `object`)
                    return true
                }
                2307, 2308 -> {
                    exitGate(player, `object`)
                    return true
                }
                2288 -> {
                    enterPipe(player)
                    return true
                }
                37704 -> {
                    jumpStones(player, `object`)
                    return true
                }
                2297 -> {
                    crossLog(player, `object`)
                    return true
                }
                2328 -> {
                    climbRocks(player)
                    return true
                }
                else -> return false
            }
            `object`.id == 2283 -> {
                player.stopAll(false)
                player.routeEvent = RouteEvent(Position(3005, 3953)) { swingRope(player, `object`) }
                return true
            }
            else -> return false
        }
    }

    private fun enterGate(player: Player, `object`: GameObject) {
        if (player.sameAs(Position(2998, 3916)) && !player.movement.isLocked()) {
            if (!Agility.hasLevel(player, 52))
                return

            val secondGate = Position(2998, 3931)
            player.moveTo(Position(`object`.x, `object`.y + 1, 0))
            //            player.setForceMovement(new ForceMovement(secondGate, 7, ForceMovement.NORTH));
            //            player.animate(9908);
            player.movement.lock(1)
            player.message("You go through the gate and try to edge over the ridge...")
            player.moveTo(secondGate)
            /*WorldTasksManager.schedule(new WorldTask() {

            @Override
            public void run() {
                player.setForceMovement(null);
                player.moveTo(secondGate);
                player.animate(-1);
                WorldTasksManager.schedule(new WorldTask() {

                    @Override
                    public void run() {
                        player.movement.unlock();
                        player.moveTo(secondGate);
                    }
                });
            }
        }, 8);*/
        }
    }

    private fun exitGate(player: Player, `object`: GameObject) {
        if ((player.sameAs(Position(2998, 3931)) || player.sameAs(Position(2997, 3931))) && !player.movement.isLocked()) {
            if (!Agility.hasLevel(player, 52))
                return
            val firstGate = Position(2998, 3916)
            player.moveTo(Position(`object`.x + if (`object`.id == 2308) 1 else 0, `object`.y, 0))
            //            player.setForceMovement(new ForceMovement(new Position(firstGate.getX(), firstGate.getY(), 0), 7, ForceMovement.SOUTH));
            //            player.animate(9908);
            player.movement.lock(1)
            player.moveTo(firstGate)
            /*WorldTasksManager.schedule(new WorldTask() {

            @Override
            public void run() {
                player.setForceMovement(null);
                player.animate(-1);
                player.movement.unlock();
                player.moveTo(firstGate);
                WorldTasksManager.schedule(new WorldTask() {

                    @Override
                    public void run() {
                        if(!player.sameAs(firstGate))
                            player.moveTo(firstGate);
                    }
                }, 2);
            }
        }, 7);*/
        }
    }

    private fun enterPipe(player: Player) {
        player.movement.lock()
        player.movement.reset()
        player.movement.addWalkSteps(3004, 3937, false)
        GameWorld.schedule(object : Task(true, 1) {
            private var tick: Int = 0

            override fun run() {
                when (tick) {
                    0 -> {
                        player.forceMovement = ForceMovement(player, 0, Position(3004, 3940), 4, ForceMovement.NORTH)
                        player.animate(10580)
                    }
                    2 -> player.moveTo(3004, 3940)
                    6 -> player.moveTo(3004, 3947)
                    7 -> {
                        player.forceMovement = ForceMovement(player, 0, Position(3004, 3950), 2, ForceMovement.NORTH)
                        player.animate(10580)
                    }
                    9 -> player.moveTo(3004, 3950)
                    11 -> {
                        Agility.addExperience(player, 12.5)
                        player.movement.unlock()
                        setStage(player, 1)
                        stop()
                    }
                    else -> {
                    }
                }
                tick++
            }
        })
    }

    private fun swingRope(player: Player, `object`: GameObject) {
        if (!Agility.hasLevel(player, 52))
            return
        else if (player.y != 3953) {
            player.movement.addWalkSteps(player.x, 3953)
            player.message("You'll need to get closer to make this jump.")
            return
        }
        player.movement.lock()
        player.animate(751)
        World.sendObjectAnimation(player, `object`, Animation(497))
        val toTile = Position(`object`.x, 3958, `object`.z)
        player.forceMovement = ForceMovement(player, 1, toTile, 3, ForceMovement.NORTH)
        Agility.addExperience(player, 20.0)
        player.message("You skilfully swing across.", true)
        GameWorld.schedule(1, {
            player.moveTo(toTile)
            if (getStage(player) != 1)
                removeStage(player)
            else
                setStage(player, 2)
            player.movement.unlock()
        })
    }

    private fun jumpStones(player: Player, `object`: GameObject) {
        if (player.y != `object`.y)
            return
        player.movement.lock()
        player.message("You carefully start crossing the stepping stones...")
        GameWorld.schedule(object : Task(true, 1) {
            internal var x: Int = 0
            internal var alternate: Boolean = false

            override fun run() {
                if (!alternate && x++ == 6) {
                    player.message("... and reach the other side safely.", true)
                    player.movement.unlock()
                    Agility.addExperience(player, 20.0)
                    if (getStage(player) != 2)
                        removeStage(player)
                    else
                        setStage(player, 3)
                    stop()
                    return
                }
                val tile = Position(3002 - x, player.y, player.z)
                if (alternate) {
                    player.moveTo(tile)
                } else {
                    player.forceMovement = ForceMovement(tile, 1, ForceMovement.WEST)
                    player.animate(741)
                }
                alternate = !alternate
            }
        })
    }

    private fun crossLog(player: Player, `object`: GameObject) {
        if (!Agility.hasLevel(player, 52))
            return
        if (player.y != `object`.y) {
            player.movement.addWalkSteps(3001, 3945, false)
            player.movement.lock()
            GameWorld.schedule(1, { walkAcrossLog(player, `object`) })
        } else
            walkAcrossLog(player, `object`)
    }

    private fun walkAcrossLog(player: Player, `object`: GameObject) {
        player.message("You walk carefully across the slippery log...", true)
        player.movement.lock()
        player.skillAnimation = Animation(9908)
        val running = player.settings.getBool(Settings.RUNNING)
        player.settings.set(Settings.RUNNING, false)
        val tile = Position(2994, `object`.y, `object`.z)
        player.movement.addWalkSteps(tile.x, tile.y, false)
        GameWorld.schedule(9, {
            player.skillAnimation = null
            player.moveTo(tile)
            player.movement.unlock()
            Agility.addExperience(player, 20.0)
            player.message("... and make it safely to the other side.", true)
            player.settings.set(Settings.RUNNING, running)
            if (getStage(player) != 3)
                removeStage(player)
            else
                setStage(player, 4)
        })
    }

    private fun climbRocks(player: Player) {
        if (!Agility.hasLevel(player, 52))
            return
        player.movement.reset()
        val tile = Position(2994, 3933)
        player.direction.face(tile)
        player.movement.addWalkSteps(2994, 3937, false)
        GameWorld.schedule(object : Task(1) {
            internal var tick: Int = 0

            override fun run() {
                when (tick) {
                    0 -> player.animate(3378)
                    4 -> player.moveTo(tile)
                    7 -> {
                        if (getStage(player) != 4)
                            removeStage(player)
                        else {
                            Agility.giveTickets(player, 6)
                            player.dialogueManager.startDialogue(CourseCompletionD())
                            player.skills.addExperience(Skill.AGILITY, 498.9)
                            setStage(player, 0)
                        }
                        stop()
                    }
                    else -> {
                    }
                }
                tick++
            }
        })
    }

    private const val KEY = "WildernessCourse"

    private fun removeStage(player: Player) {
        player.temporaryAttributes.remove(KEY)
    }

    fun setStage(player: Player, stage: Int) {
        player.temporaryAttributes[KEY] = stage
    }

    fun getStage(player: Player): Int {
        return if (player.temporaryAttributes[KEY] != null) player.temporaryAttributes[KEY] as Int else 0
    }
}
