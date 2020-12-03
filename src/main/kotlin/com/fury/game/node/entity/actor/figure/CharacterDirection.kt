package com.fury.game.node.entity.actor.figure

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.game.entity.`object`.GameObject
import com.fury.game.world.map.Position
import com.fury.game.world.map.path.Rectangle
import com.fury.game.world.update.flag.Flag
import com.fury.game.world.update.flag.block.Direction

class CharacterDirection(private val figure: Figure) {
    //TODO combine direction & positionToFace
    var direction: Direction = Direction.NONE
        set(value) {
            field = value
            face(if(value == Direction.NONE) null else figure.copyPosition().add(direction.deltaX, direction.deltaY))
        }
    var interacting: Figure? = null
        set(value) {
            if(value == null && interacting != null || value != null)
                figure.updateFlags.add(Flag.ENTITY_INTERACTION)
            field = value
        }

    private var positionToFace: Rectangle? = null

    fun getPositionToFace(): Rectangle? {
        return positionToFace
    }

    @JvmOverloads fun face(base: Position?, sizeX: Int = 1, sizeY: Int = 1) {
        if(base != null)
            figure.updateFlags.add(Flag.FACE_POSITION)
        positionToFace = if (base != null) Rectangle(base.x, base.y, sizeX, sizeY) else null
    }

    fun faceObject(gameObject: GameObject) {
        val def = gameObject.definition
        var x = -1
        var y = -1
        var sizeX = 1
        var sizeY = 1
        if (gameObject.type == 0) { // wall
            when {
                gameObject.direction == 0 -> { // west
                    x = gameObject.x - 1
                    y = gameObject.y
                }
                gameObject.direction == 1 -> { // north
                    x = gameObject.x
                    y = gameObject.y + 1
                }
                gameObject.direction == 2 -> { // east
                    x = gameObject.x + 1
                    y = gameObject.y
                }
                gameObject.direction == 3 -> { // south
                    x = gameObject.x
                    y = gameObject.y - 1
                }
            }
        } else if (gameObject.type == 1 || gameObject.type == 2) { // corner and cornerwall
            when {
                gameObject.direction == 0 -> { // nw
                    x = gameObject.x - 1
                    y = gameObject.y + 1
                }
                gameObject.direction == 1 -> { // ne
                    x = gameObject.x + 1
                    y = gameObject.y + 1
                }
                gameObject.direction == 2 -> { // se
                    x = gameObject.x + 1
                    y = gameObject.y - 1
                }
                gameObject.direction == 3 -> { // sw
                    x = gameObject.x - 1
                    y = gameObject.y - 1
                }
            }
        } else if (gameObject.type == 3) { // inverted corner
            when {
                gameObject.direction == 0 -> { // se
                    x = gameObject.x + 1
                    y = gameObject.y - 1
                }
                gameObject.direction == 1 -> { // sw
                    x = gameObject.x - 1
                    y = gameObject.y - 1
                }
                gameObject.direction == 2 -> { // nw
                    x = gameObject.x - 1
                    y = gameObject.y + 1
                }
                gameObject.direction == 3 -> { // ne
                    x = gameObject.x + 1
                    y = gameObject.y + 1
                }
            }
        } else if (gameObject.type < 10) { // walldeco's
            when {
                gameObject.direction == 0 -> { // west
                    x = gameObject.x - 1
                    y = gameObject.y
                }
                gameObject.direction == 1 -> { // north
                    x = gameObject.x
                    y = gameObject.y + 1
                }
                gameObject.direction == 2 -> { // east
                    x = gameObject.x + 1
                    y = gameObject.y
                }
                gameObject.direction == 3 -> { // south
                    x = gameObject.x
                    y = gameObject.y - 1
                }
            }
        } else if (gameObject.type == 10 || gameObject.type == 11 || gameObject.type == 22) { // multisized rect objs
            if (gameObject.direction == 0 || gameObject.direction == 2) {
                x = gameObject.x
                y = gameObject.y
                sizeX = def.getSizeX()
                sizeY = def.getSizeY()
            } else {
                x = gameObject.x
                y = gameObject.y
                sizeX = def.getSizeY()
                sizeY = def.getSizeX()
            }
        } else {
            // rest
            x = gameObject.x
            y = gameObject.y
        }

        face(Position(x, y, figure.z), sizeX, sizeY)
    }
}