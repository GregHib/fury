package com.fury.game.npc.bosses.godwars.bandos

import com.fury.game.npc.bosses.godwars.GodWarMinion
import com.fury.game.npc.bosses.godwars.GodWarsBoss
import com.fury.game.world.map.Position

class GeneralGraardor(id: Int, position: Position, spawned: Boolean) : GodWarsBoss(id, position, spawned) {

    override fun respawnMinions() {
        for (minion in minions)
            if (minion != null && (minion.finished || minion.isDead))
                minion.respawn()
    }

    companion object {
        val minions = arrayOfNulls<GodWarMinion>(3)
    }
}
