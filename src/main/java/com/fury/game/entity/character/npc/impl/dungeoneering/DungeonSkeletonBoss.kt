package com.fury.game.entity.character.npc.impl.dungeoneering

import com.fury.game.content.skill.free.dungeoneering.DungeonManager
import com.fury.game.content.skill.free.dungeoneering.RoomReference
import com.fury.game.world.map.Position

class DungeonSkeletonBoss(id: Int, tile: Position, manager: DungeonManager, reference: RoomReference, multiplier: Double) : DungeonMob(id, tile, manager, multiplier) {

    var boss: DivineSkinweaver

    init {
        forceAggressive = true
        //setIntelligentRouteFinder(true);
        lureDelay = 0
        boss = getNPC(10058) as DivineSkinweaver
    }

    override fun drop() {

    }

}
