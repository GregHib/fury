package com.fury.game.entity.character.npc.impl.fightkiln


import com.fury.cache.Revision
import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.game.content.controller.impl.FightKiln
import com.fury.game.world.map.Position

open class FightKilnMob(id: Int, tile: Position, var controller: FightKiln) : Mob(id, tile, Revision.PRE_RS3) {

    private val deathGfx: Int
        get() {
            return when (id) {
                15201 -> 2926
                15202 -> 2927
                15203 -> 2957
                15213, 15214, 15204 -> 2928
                15205 -> 2959
                15206, 15207 -> 2929
                15208, 15211, 15212 -> 2973
                else -> 2926
            }
        }

    init {
        isNoDistanceCheck = true
        targetDistance = 40
    }

    /*@Override
	public ArrayList<Entity> getPossibleTargets() {
		ArrayList<Entity> possibleTarget = new ArrayList<Entity>(1);
		List<Integer> playerIndexes = World.getRegion(getLastRegionId()).getPlayerIndexes();
		if(playerIndexes != null) {
			for (int npcIndex : playerIndexes) {
				Player player = GameWorld.getPlayers().get(npcIndex);
				if (player == null
						|| player.getHealth().getHealth() <= 0
						|| player.getFinished()
						|| !player.isRunning())
					continue;
				possibleTarget.add(player);
			}
		}
		return possibleTarget;
	}*/


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

