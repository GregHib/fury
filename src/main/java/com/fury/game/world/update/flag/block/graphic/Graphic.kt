package com.fury.game.world.update.flag.block.graphic

import com.fury.cache.Revision
import com.fury.core.model.node.entity.Entity
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.network.packet.out.WorldGraphic
import com.fury.game.world.GameWorld
import com.fury.game.world.map.Position

/**
 * Represents a graphic an figure might perform.
 *
 * @author relex lawl
 */

class Graphic @JvmOverloads constructor(val id: Int, val delay: Int = 0, val height: Int = GraphicHeight.LOW.toInt(), val revision: Revision = Revision.RS2) {

    @JvmOverloads constructor(id: Int, delay: Int = 0, height: GraphicHeight, revision: Revision = Revision.RS2) : this(id, delay, height.toInt(), revision)

    constructor(id: Int, revision: Revision) : this(id, 0, revision = revision)

    companion object {
        @JvmStatic
        fun sendGlobal(creator: Entity?, graphic: Graphic, tile: Position) {
            if (creator == null) {
                GameWorld.players
                        .filter { it.started && !it.finished && it.isViewableFrom(tile) }
                        .forEach { it.send(WorldGraphic(graphic, tile)) }
            } else if (creator is Player) {
                creator.send(WorldGraphic(graphic, tile))
                GameWorld.regions.getLocalPlayers(creator)
                        .filter { it.started && !it.finished && it.isViewableFrom(tile) }
                        .forEach { it.send(WorldGraphic(graphic, tile)) }
            }
        }
    }

    override fun toString(): String {
        return "Graphic [$id, $revision, $delay, $height]"
    }
}