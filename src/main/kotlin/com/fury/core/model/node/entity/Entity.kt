package com.fury.core.model.node.entity

import com.fury.core.model.node.Node
import com.fury.game.world.map.Position

abstract class Entity(position: Position) : Node(position) {

    var mapSize = 0//TODO temp, should be inside region?

}