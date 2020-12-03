package com.fury.game.npc.bosses.rfd

import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.game.world.map.Position
import com.fury.util.Misc

class GelatinnothMother(id: Int, position: Position, spawned: Boolean) : Mob(id, position, spawned) {

    private var counter = 0
    private var timer = Misc.random(15, 20)
    var type = 0

    override fun processNpc() {
        super.processNpc()

        counter++
        if (counter >= timer * 1.7) {

            type++

            if (type >= 6)
                type = 0
            setTransformation(actualId + type)
            counter = 0
            timer = Misc.random(15, 20)
        }
    }
}
