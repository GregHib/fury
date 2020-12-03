package com.fury.game.node.entity.actor.`object`

import com.fury.cache.Revision
import com.fury.game.content.misc.objects.DwarfMultiCannon
import com.fury.game.entity.`object`.GameObject
import com.fury.game.world.map.Position

class Cannon(id: Int, position: Position, type: Int, face: Int, revision: Revision = Revision.RS2, val cannonType: DwarfMultiCannon.CannonType) : GameObject(id, position, type, face, revision) {
    var balls = 0
    var warned = false
}