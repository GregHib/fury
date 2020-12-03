package com.fury.game.npc.misc

import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.game.node.entity.mob.combat.EvilChickenCombat
import com.fury.game.world.map.Position

class EvilChicken(id: Int, position: Position, spawned: Boolean) : Mob(id, position, spawned) {
    companion object {
        var spots = arrayOf(Position(3036, 4498), Position(3025, 4495), Position(3023, 4504), Position(3021, 4511), Position(3028, 4518), Position(3034, 4514), Position(3037, 4507), Position(3052, 4492), Position(3047, 4498))
    }
}
