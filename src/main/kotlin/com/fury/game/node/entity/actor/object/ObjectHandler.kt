package com.fury.game.node.entity.actor.`object`

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.engine.task.executor.GameExecutorManager
import com.fury.game.content.dialogue.impl.objects.ClimbEmoteStairsD
import com.fury.game.entity.`object`.GameObject
import com.fury.game.entity.character.player.link.transportation.TeleportHandler
import com.fury.game.world.FloorItemManager
import com.fury.game.world.GameWorld
import com.fury.game.world.map.Position
import com.fury.util.Misc
import java.util.concurrent.TimeUnit

/**
 * Created by Greg on 27/12/2016.
 */
object ObjectHandler {

    @JvmOverloads
    @JvmStatic
    fun useStairs(player: Player, emoteId: Int, dest: Position, useDelay: Int, totalDelay: Int, message: String? = null, resetAnimation: Boolean = false) {
        player.stopAll()
        player.movement.lock(totalDelay.toLong())
        if (emoteId != -1)
            player.animate(emoteId)
        if (useDelay == 0)
            player.moveTo(dest)
        else {
            GameWorld.schedule(if (useDelay <= 0) 1 else useDelay) {
                if (player.isDead)
                    return@schedule
                if (resetAnimation)
                    player.animate(-1)
                player.moveTo(dest)
                if (message != null)
                    player.message(message, true)
                TeleportHandler.checkControllers(player, dest)
                player.movement.unlock()
            }
        }
    }

    @JvmStatic
    fun slashWeb(player: Player, gameObject: GameObject) {
        if (Misc.random(2) == 0) {
            TempObjectManager.spawnObjectTemporary(GameObject(gameObject.id + 1, gameObject, gameObject.type, gameObject.direction), 60000)
            player.message("You slash through the web!", true)
        } else
            player.message("You fail to cut through the web.", true)
    }

    @JvmStatic
    @JvmOverloads
    fun handleDoor(gameObject: GameObject, timer: Long = 60000) {
        if (ObjectManager.isSpawnedObject(gameObject)) {
            // World.removeObject(gameObject);
            return
        }
        if (gameObject.sameAs(Position(2847, 3541, 2)) || gameObject.sameAs(Position(2847, 3540, 2)))
            return
        //Jail doors
        if (gameObject.id == 15653 || (gameObject.id == 15644 || gameObject.id == 15641) && gameObject.z == 2 || gameObject.id == 31539 || gameObject.id == 31454 || gameObject.id == 31534 || gameObject.id == 31455)
            return
        val openedDoor = GameObject(gameObject.id, gameObject.copyPosition(), gameObject.type, gameObject.direction + 1 and 0x3)
        TempObjectManager.spawnObjectTemporary(openedDoor, timer)
        return
    }

    fun handleGate(player: Player, gameObject: GameObject): Boolean {
        return handleGate(player, gameObject, 60000)
    }

    private fun handleGate(player: Player, gameObject: GameObject, delay: Long): Boolean {
        if (ObjectManager.isSpawnedObject(gameObject))
            return false
        if (gameObject.direction == 0) {
            var south = true
            var otherDoor: GameObject? = ObjectManager.getObjectWithType(Position(gameObject.x, gameObject.y + 1, gameObject.z), gameObject.type)
            if (otherDoor == null || otherDoor.direction != gameObject.direction || otherDoor.type != gameObject.type || !otherDoor.definition.name.equals(gameObject.definition.name, ignoreCase = true)) {
                otherDoor = ObjectManager.getObjectWithType(Position(gameObject.x, gameObject.y - 1, gameObject.z), gameObject.type)
                if (otherDoor == null || otherDoor.direction != gameObject.direction || otherDoor.type != gameObject.type || !otherDoor.definition.name.equals(gameObject.definition.name, ignoreCase = true))
                    return false
                south = false
            }
            val openedDoor1 = GameObject(gameObject.id, gameObject.copyPosition(), gameObject.type, gameObject.direction + 1)
            val openedDoor2 = GameObject(otherDoor.id, gameObject.copyPosition(), otherDoor.type, otherDoor.direction + 1)
            if (south) {
                openedDoor1.setPosition(-1, 0, 0)
                openedDoor1.direction = 3
                openedDoor2.setPosition(-1, 0, 0)
            } else {
                openedDoor1.setPosition(-1, 0, 0)
                openedDoor2.setPosition(-1, 0, 0)
                openedDoor2.direction = 3
            }

            if (removeObjectTemporary(gameObject, delay) && removeObjectTemporary(otherDoor, delay)) {
                player.direction.face(openedDoor1)
                TempObjectManager.spawnObjectTemporary(openedDoor1, delay)
                TempObjectManager.spawnObjectTemporary(openedDoor2, delay)
                return true
            }
        } else if (gameObject.direction == 2) {
            var south = true
            var otherDoor: GameObject? = ObjectManager.getObjectWithType(Position(gameObject.x, gameObject.y + 1, gameObject.z), gameObject.type)
            if (otherDoor == null || otherDoor.direction != gameObject.direction || otherDoor.type != gameObject.type || !otherDoor.definition.name.equals(gameObject.definition.name, ignoreCase = true)) {
                otherDoor = ObjectManager.getObjectWithType(Position(gameObject.x, gameObject.y - 1, gameObject.z), gameObject.type)
                if (otherDoor == null || otherDoor.direction != gameObject.direction || otherDoor.type != gameObject.type || !otherDoor.definition.name.equals(gameObject.definition.name, ignoreCase = true))
                    return false
                south = false
            }
            val openedDoor1 = GameObject(gameObject.id, gameObject.copyPosition(), gameObject.type, gameObject.direction + 1)
            val openedDoor2 = GameObject(otherDoor.id, gameObject.copyPosition(), otherDoor.type, otherDoor.direction + 1)
            if (south) {
                openedDoor1.setPosition(1, 0, 0)
                openedDoor2.direction = 1
                openedDoor2.setPosition(1, 0, 0)
            } else {
                openedDoor1.setPosition(1, 0, 0)
                openedDoor1.direction = 1
                openedDoor2.setPosition(1, 0, 0)
            }
            if (removeObjectTemporary(gameObject, delay) && removeObjectTemporary(otherDoor, delay)) {
                player.direction.face(openedDoor1)
                TempObjectManager.spawnObjectTemporary(openedDoor1, delay)
                TempObjectManager.spawnObjectTemporary(openedDoor2, delay)
                return true
            }
        } else if (gameObject.direction == 3) {

            var right = true
            var otherDoor: GameObject? = ObjectManager.getObjectWithType(Position(gameObject.x - 1, gameObject.y, gameObject.z), gameObject.type)
            if (otherDoor == null || otherDoor.direction != gameObject.direction || otherDoor.type != gameObject.type || !otherDoor.definition.name.equals(gameObject.definition.name, ignoreCase = true)) {
                otherDoor = ObjectManager.getObjectWithType(Position(gameObject.x + 1, gameObject.y, gameObject.z), gameObject.type)
                if (otherDoor == null || otherDoor.direction != gameObject.direction || otherDoor.type != gameObject.type || !otherDoor.definition.name.equals(gameObject.definition.name, ignoreCase = true))
                    return false
                right = false
            }
            val openedDoor1 = GameObject(gameObject.id, gameObject.copyPosition(), gameObject.type, gameObject.direction + 1)
            val openedDoor2 = GameObject(otherDoor.id, gameObject.copyPosition(), otherDoor.type, otherDoor.direction + 1)
            if (right) {
                openedDoor1.setPosition(0, -1, 0)
                openedDoor2.direction = 0
                openedDoor1.direction = 2
                openedDoor2.setPosition(0, -1, 0)
            } else {
                openedDoor1.setPosition(0, -1, 0)
                openedDoor1.direction = 0
                openedDoor2.direction = 2
                openedDoor2.setPosition(0, -1, 0)
            }
            if (removeObjectTemporary(gameObject, delay) && removeObjectTemporary(otherDoor, delay)) {
                player.direction.face(openedDoor1)
                TempObjectManager.spawnObjectTemporary(openedDoor1, delay)
                TempObjectManager.spawnObjectTemporary(openedDoor2, delay)
                return true
            }
        } else if (gameObject.direction == 1) {

            var right = true
            var otherDoor: GameObject? = ObjectManager.getObjectWithType(Position(gameObject.x - 1, gameObject.y, gameObject.z), gameObject.type)
            if (otherDoor == null || otherDoor.direction != gameObject.direction || otherDoor.type != gameObject.type || !otherDoor.definition.name.equals(gameObject.definition.name, ignoreCase = true)) {
                otherDoor = ObjectManager.getObjectWithType(Position(gameObject.x + 1, gameObject.y, gameObject.z), gameObject.type)
                if (otherDoor == null || otherDoor.direction != gameObject.direction || otherDoor.type != gameObject.type || !otherDoor.definition.name.equals(gameObject.definition.name, ignoreCase = true))
                    return false
                right = false
            }
            val openedDoor1 = GameObject(gameObject.id, otherDoor.copyPosition(), gameObject.type, gameObject.direction + 1)
            val openedDoor2 = GameObject(otherDoor.id, otherDoor.copyPosition(), otherDoor.type, otherDoor.direction + 1)
            if (right) {
                openedDoor1.setPosition(0, 1, 0)
                openedDoor1.direction = 0
                openedDoor2.setPosition(0, 1, 0)
            } else {
                openedDoor1.setPosition(0, 1, 0)
                openedDoor2.direction = 0
                openedDoor2.setPosition(0, 1, 0)
            }
            if (removeObjectTemporary(gameObject, delay) && removeObjectTemporary(otherDoor, delay)) {
                player.direction.face(openedDoor1)
                TempObjectManager.spawnObjectTemporary(openedDoor1, delay)
                TempObjectManager.spawnObjectTemporary(openedDoor2, delay)
                return true
            }
        }
        return false
    }

    @JvmStatic
    fun handleLadder(player: Player, gameObject: GameObject, optionId: Int): Boolean {
        val option = gameObject.definition.getOption(optionId) ?: return true

        when {
            option.equals("Climb-up", ignoreCase = true) -> {
                if (player.z == 3)
                    return false
                useStairs(player, 828, Position(player.x, player.y, player.z + 1), 1, 2)
            }
            option.equals("Climb-down", ignoreCase = true) -> {
                if (player.z == 0)
                    return false
                useStairs(player, 828, Position(player.x, player.y, player.z - 1), 1, 2)
            }
            option.equals("Climb", ignoreCase = true) -> {
                if (player.z == 3 || player.z == 0)
                    return false
                player.dialogueManager.startDialogue(ClimbEmoteStairsD(), Position(player.x, player.y, player.z + 1), Position(player.x, player.y, player.z - 1), "Climb up the ladder.", "Climb down the ladder.", 828)
            }
            else -> return false
        }
        return true
    }

    @JvmStatic
    fun spawnTempGroundObject(gameObject: GameObject, replaceId: Int, time: Long) {
        ObjectManager.spawnObject(gameObject)
        GameExecutorManager.slowExecutor.schedule({
            ObjectManager.removeObject(gameObject)
            FloorItemManager.addGroundItem(Item(replaceId), gameObject, null)
        }, time, TimeUnit.MILLISECONDS)
    }

    @JvmStatic
    fun removeObjectTemporary(gameObject: GameObject, time: Long): Boolean {
        ObjectManager.removeObject(gameObject)
        GameExecutorManager.slowExecutor.schedule({ ObjectManager.spawnObject(gameObject) }, time, TimeUnit.MILLISECONDS)
        return TempObjectManager.removeObjectTemporary(gameObject, time)
    }
}
