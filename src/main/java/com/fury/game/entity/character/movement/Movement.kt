package com.fury.game.entity.character.movement

import com.fury.core.engine.GameEngine
import com.fury.core.model.node.entity.Entity
import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.entity.character.player.content.EnergyHandler
import com.fury.game.node.entity.actor.figure.player.handles.Settings
import com.fury.game.world.World
import com.fury.game.world.map.Position
import com.fury.game.world.update.flag.block.Direction
import com.fury.util.Utils
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

class Movement(internal val figure: Figure) {

    private val walkSteps: ConcurrentLinkedQueue<IntArray> = ConcurrentLinkedQueue()
    private var lockDelay: Long = 0

    fun isLocked(): Boolean {
        return lockDelay > GameEngine.cycle
    }

    fun lock(ticks: Long = Long.MAX_VALUE) {
        lockDelay = if (ticks == -1L) Long.MAX_VALUE else GameEngine.cycle + ticks
    }

    fun lock() {
        lockDelay = Long.MAX_VALUE
    }

    fun unlock() {
        lockDelay = 0
    }

    val lastWalkTile: IntArray
        get() {
            val objects = walkSteps.toTypedArray()
            if (objects.isEmpty())
                return intArrayOf(figure.x, figure.y)
            val step = objects[objects.size - 1] as IntArray
            return intArrayOf(step[1], step[2])
        }

    private val nextWalkStep: Int
        get() {
            val step = walkSteps.poll() ?: return -1
            return step[0]
        }

    val isEmpty: Boolean
        get() = walkSteps.isEmpty()

    fun stepAway(): Boolean {
        if (!addWalkSteps(figure.x - figure.sizeX, figure.y, 1))
            if (!addWalkSteps(figure.x + figure.sizeX, figure.y, 1))
                if (!addWalkSteps(figure.x, figure.y + figure.sizeY, 1))
                    if (!addWalkSteps(figure.x, figure.y - figure.sizeY, 1))
                        return true
        return false
    }

    fun stepAway(from: Entity) {
        val x = figure.x
        val y = figure.y
        val fX = from.x
        val fY = from.y
        if (figure.sameAs(from))
            stepAway()

        if (!addWalkSteps(x + (x - fX), y + (y - fY), 1))
            stepAway()
    }

    fun addWalkSteps(destX: Int, destY: Int, check: Boolean): Boolean {
        return addWalkSteps(destX, destY, -1, check)
    }

    @JvmOverloads
    fun addWalkSteps(destX: Int, destY: Int, maxStepsCount: Int = -1, check: Boolean = true): Boolean {
        val lastTile = lastWalkTile
        var myX = lastTile[0]
        var myY = lastTile[1]
        var stepCount = 0
        while (true) {
            stepCount++
            if (myX < destX)
                myX++
            else if (myX > destX)
                myX--
            if (myY < destY)
                myY++
            else if (myY > destY)
                myY--
            if (!addWalkStep(myX, myY, lastTile[0], lastTile[1], check))
            // clipped here so stop
                return false
            if (stepCount == maxStepsCount)
                return true
            lastTile[0] = myX
            lastTile[1] = myY
            if (lastTile[0] == destX && lastTile[1] == destY)
                return true
        }
    }

    fun addWalkStep(nextX: Int, nextY: Int, lastX: Int, lastY: Int, check: Boolean): Boolean {
        val dir = Utils.getMoveDirection(nextX - lastX, nextY - lastY)

        if (dir == -1)
            return false

        if (check && !World.checkWalkStep(lastX, lastY, figure.z, dir, figure.sizeX))
            return false
        if (check && figure is Player) {
            if (!figure.controllerManager.checkWalkStep(lastX, lastY, nextX, nextY))
                return false
        }

        walkSteps.add(intArrayOf(dir, nextX, nextY))
        return true
    }

    fun addWalkStepsInteract(destX: Int, destY: Int, maxStepsCount: Int, size: Int, calculate: Boolean): Boolean {
        return addWalkStepsInteract(destX, destY, maxStepsCount, size, size, calculate)
    }

    fun addWalkStepsInteract(destX: Int, destY: Int, maxStepsCount: Int, sizeX: Int, sizeY: Int, calculate: Boolean): Boolean {
        val lastTile = lastWalkTile
        var myX = lastTile[0]
        var myY = lastTile[1]
        var stepCount = 0
        while (true) {
            stepCount++
            val myRealX = myX
            val myRealY = myY

            if (myX < destX)
                myX++
            else if (myX > destX)
                myX--
            if (myY < destY)
                myY++
            else if (myY > destY)
                myY--
            if (!addWalkStep(myX, myY, lastTile[0], lastTile[1], true)) {
                if (!calculate)
                    return false
                val myT = calculatedStep(myRealX, myRealY, destX, destY, lastTile[0], lastTile[1], sizeX, sizeY)
                        ?: return false
                myX = myT[0]
                myY = myT[1]
            }
            val distanceX = myX - destX
            val distanceY = myY - destY
            if (!(distanceX > sizeX || distanceX < -1 || distanceY > sizeY || distanceY < -1))
                return true
            if (stepCount == maxStepsCount)
                return true
            lastTile[0] = myX
            lastTile[1] = myY
            if (lastTile[0] == destX && lastTile[1] == destY)
                return true
        }
    }

    fun calculatedStep(startX: Int, startY: Int, destX: Int, destY: Int, lastX: Int, lastY: Int, sizeX: Int, sizeY: Int): IntArray? {
        var myX = startX
        var myY = startY
        if (myX < destX) {
            myX++
            if (!addWalkStep(myX, myY, lastX, lastY, true))
                myX--
            else if (!(myX - destX > sizeX || myX - destX < -1
                            || myY - destY > sizeY || myY - destY < -1)) {
                return if (myX == lastX || myY == lastY) null else intArrayOf(myX, myY)
            }
        } else if (myX > destX) {
            myX--
            if (!addWalkStep(myX, myY, lastX, lastY, true))
                myX++
            else if (!(myX - destX > sizeX || myX - destX < -1
                            || myY - destY > sizeY || myY - destY < -1)) {
                return if (myX == lastX || myY == lastY) null else intArrayOf(myX, myY)
            }
        }
        if (myY < destY) {
            myY++
            if (!addWalkStep(myX, myY, lastX, lastY, true))
                myY--
            else if (!(myX - destX > sizeX || myX - destX < -1
                            || myY - destY > sizeY || myY - destY < -1)) {
                return if (myX == lastX || myY == lastY) null else intArrayOf(myX, myY)
            }
        } else if (myY > destY) {
            myY--
            if (!addWalkStep(myX, myY, lastX, lastY, true)) {
                myY++
            } else if (!(myX - destX > sizeX || myX - destX < -1
                            || myY - destY > sizeY || myY - destY < -1)) {
                return if (myX == lastX || myY == lastY) null else intArrayOf(myX, myY)
            }
        }
        return if (myX == lastX || myY == lastY) null else intArrayOf(myX, myY)
    }

    fun checkWalkStep(nextX: Int, nextY: Int, lastX: Int, lastY: Int, check: Boolean): Boolean {
        val dir = Utils.getMoveDirection(nextX - lastX, nextY - lastY)
        if (dir == -1)
            return false

        return !(check && !World.checkWalkStep(lastX, lastY, figure.z, dir, figure.sizeX, figure.sizeY))
    }

    fun reset() {
        walkSteps.clear()
    }

    fun hasWalkSteps(): Boolean {
        return !walkSteps.isEmpty()
    }

    var teleporting: Boolean = false
    var walkingDirection = Direction.NONE
    var runningDirection = Direction.NONE
    var lastDirection = Direction.NONE

    var lastPosition: Position = figure.copyPosition()
    var nextPosition: Optional<Position> = Optional.empty()

    fun process() {
        lastPosition = figure.copyPosition()

        /*val entity = figure.direction.interacting
        if(entity != null) {
            figure.direction.direction = Direction.fromDeltas(entity.getCoordFaceX(entity.size) - figure.x, entity.getCoordFaceY(sizeY = entity.size) - figure.y)
            figure.updateFlags.remove(Flag.FACE_POSITION)
        }*/

        //Move to
        if (nextPosition.isPresent) {
            val lastPlane = figure.z
            figure.moveTo(nextPosition.get())
            nextPosition = Optional.empty()
            teleporting = true
            World.updateEntityRegion(figure)
            if (figure.needMapUpdate())
                figure.loadMapRegions()
            else if (figure is Player && lastPlane != figure.z)
                figure.clientLoadedMapRegion = false
            figure.movement.reset()
            return
        }

        teleporting = false
        if (figure.movement.isEmpty)
            return
        if (figure is Player && figure.emotesManager.isDoingEmote)
            return
        val walkDir = figure.movement.nextWalkStep
        if (walkDir != -1) {
            if (figure is Player && !figure.controllerManager.canMove(walkDir)) {
                figure.movement.reset()
                return
            }

            var xOffset = Utils.DIRECTION_DELTA_X[walkDir].toInt()
            var yOffset = Utils.DIRECTION_DELTA_Y[walkDir].toInt()
            var direction = Direction.fromDeltas(xOffset, yOffset)
            figure.add(xOffset, yOffset)
            walkingDirection = direction
            lastDirection = direction

            if (figure.direction.getPositionToFace() != null)
                figure.direction.face(null)

            if (figure.run) {
                if (figure is Player && figure.settings.getInt(Settings.RUN_ENERGY) <= 0)
                    figure.settings.set(Settings.RUNNING, false)
                else {
                    val runDir = figure.movement.nextWalkStep
                    if (runDir != -1) {
                        if (figure is Player) {
                            if (!figure.controllerManager.canMove(runDir)) {
                                figure.movement.reset()
                                return
                            }
                            EnergyHandler.processEnergyDepletion(figure)
                        }

                        xOffset = Utils.DIRECTION_DELTA_X[runDir].toInt()
                        yOffset = Utils.DIRECTION_DELTA_Y[runDir].toInt()
                        direction = Direction.fromDeltas(xOffset, yOffset)
                        figure.add(xOffset, yOffset)
                        runningDirection = direction
                        lastDirection = direction
                    }
                }
            }
        }
        World.updateEntityRegion(figure)
        if (figure.needMapUpdate())
            figure.loadMapRegions()
    }
}
