package com.fury.game.entity.character.npc.impl.fightkiln


import com.fury.cache.Revision
import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.game.node.entity.actor.figure.player.handles.Settings
import com.fury.game.node.entity.mob.combat.HarAkenTentacleCombat
import com.fury.game.world.GameWorld
import com.fury.game.world.map.Position
import com.fury.game.world.update.flag.block.Animation
import java.util.*

class HarAkenTentacle(id: Int, tile: Position, var aken: HarAken) : Mob(id, tile, Revision.PRE_RS3) {

    override val possibleTargets: ArrayList<Figure>
        get() {
            val possibleTarget = ArrayList<Figure>(1)
            val players = GameWorld.regions.get(lastRegionId).getPlayers(z)
            for (player in players) {
                if (player == null
                        || player.health.hitpoints <= 0
                        || player.finished
                        || !player.settings.getBool(Settings.RUNNING))
                    continue
                possibleTarget.add(player)
            }
            return possibleTarget
        }

    init {
        //setForceMultiArea(true);
        movement.lock()
        perform(Animation(if (id == 15209) 16238 else 16241, Revision.PRE_RS3))
    }

    /*@Override
	public double getMagePrayerMultiplier() {
		return 0.1;
	}
	
	@Override
	public double getRangePrayerMultiplier() {
		return 0.1;
	}
	
	@Override
	public double getMeleePrayerMultiplier() {
		return 0.1;
	}*/
}
