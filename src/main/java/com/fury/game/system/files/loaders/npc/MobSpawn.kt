package com.fury.game.system.files.loaders.npc

import com.fury.cache.Revision
import com.fury.game.world.map.Position
import com.fury.game.world.update.flag.block.Direction

class MobSpawn internal constructor(val id: Int, val tile: Position, val isWalk: Boolean, val direction: Direction, val revision: Revision)
