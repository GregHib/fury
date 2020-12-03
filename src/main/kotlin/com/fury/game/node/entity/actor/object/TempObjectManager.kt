package com.fury.game.node.entity.actor.`object`

import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.engine.task.executor.GameExecutorManager
import com.fury.game.entity.`object`.GameObject
import com.fury.game.entity.`object`.TempObject
import com.fury.core.model.item.Item
import com.fury.game.world.FloorItemManager
import com.fury.util.Utils
import java.util.concurrent.TimeUnit

object TempObjectManager {

    fun spawnTempGroundObject(player: Player, gameObject: GameObject, replaceId: Int, time: Long) {
        ObjectManager.spawnObject(gameObject)
        GameExecutorManager.slowExecutor.schedule({
            ObjectManager.removeObject(gameObject)
            FloorItemManager.addGroundItem(Item(replaceId), gameObject, null)
        }, time, TimeUnit.MILLISECONDS)
    }

    fun removeObjectTemporary(`object`: GameObject, time: Long): Boolean {
        ObjectManager.removeObject(`object`)
        GameExecutorManager.slowExecutor.schedule({ ObjectManager.spawnObject(`object`) }, time, TimeUnit.MILLISECONDS)
        return true
    }

    @JvmStatic
    fun sendObjectTemporary(player: Player, gameObject: GameObject, time: Long, checkObjectInstance: Boolean, checkObjectBefore: Boolean) {
        val before = if (checkObjectBefore) ObjectManager.getObjectWithType(gameObject, gameObject.type) else null

        player.packetSender.sendObject(gameObject)
        GameExecutorManager.slowExecutor.schedule({
            if (checkObjectInstance && ObjectManager.getObjectWithId(gameObject.id, gameObject) !== gameObject)
                return@schedule
            player.packetSender.sendObjectRemoval(gameObject)
            if (before != null)
                player.packetSender.sendObject(before)
        }, time, TimeUnit.MILLISECONDS)
    }

    @JvmStatic
    @JvmOverloads
    fun spawnObjectTemporary(gameObject: GameObject, time: Long, checkObjectInstance: Boolean = false, checkObjectBefore: Boolean = false) {
        val before = if (checkObjectBefore) ObjectManager.getObjectWithType(gameObject, gameObject.type) else null
        ObjectManager.spawnObject(gameObject)
        GameExecutorManager.slowExecutor.schedule({
            if (checkObjectInstance && ObjectManager.getObjectWithId(gameObject.id, gameObject) !== gameObject)
                return@schedule

            ObjectManager.removeObject(gameObject) //this method allows to remove object with just tile and type actually so the removing object may be different and still gets removed
            if (before != null)
                ObjectManager.spawnObject(before)
        }, time, TimeUnit.MILLISECONDS)
    }

    @JvmStatic
    fun spawnTemporaryObject(tempObject: TempObject, checkObjectInstance: Boolean = false, checkObjectBefore: Boolean = false) {
        val before = if (checkObjectBefore) ObjectManager.getObjectWithType(tempObject, tempObject.type) else null
        ObjectManager.spawnObject(tempObject)
        scheduleTemporaryObject(tempObject, checkObjectInstance, before)
    }

    private fun scheduleTemporaryObject(tempObject: TempObject, checkObjectInstance: Boolean, before: GameObject?) {
        val start = Utils.currentTimeMillis()
        val time = tempObject.time
        GameExecutorManager.slowExecutor.schedule({
            if (checkObjectInstance && ObjectManager.getObjectWithId(tempObject.id, tempObject) !== tempObject)
                return@schedule

            val predict = start + tempObject.time
            if (predict - Utils.currentTimeMillis() > 600) {
                tempObject.addTime(-time)
                scheduleTemporaryObject(tempObject, checkObjectInstance, before)
            } else {
                ObjectManager.removeObject(tempObject) //this method allows to remove object with just tile and type actually so the removing object may be different and still gets removed
                if (before != null)
                    ObjectManager.spawnObject(before)
            }

        }, time, TimeUnit.MILLISECONDS)
    }
}