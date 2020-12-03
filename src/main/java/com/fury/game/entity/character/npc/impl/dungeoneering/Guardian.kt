package com.fury.game.entity.character.npc.impl.dungeoneering

import com.fury.game.content.skill.free.dungeoneering.DungeonManager
import com.fury.game.content.skill.free.dungeoneering.RoomReference
import com.fury.game.node.entity.mob.combat.GuardianCombat
import com.fury.game.world.map.Position

open class Guardian(id: Int, tile: Position, manager: DungeonManager, internal var reference: RoomReference, multiplier: Double) : DungeonMob(id, tile, manager, multiplier) {

    init {
        forceAggressive = true
    }

}
