package com.fury.game.entity.character.npc.impl.construction;


import com.fury.cache.Revision;
import com.fury.core.model.node.entity.Entity;
import com.fury.engine.task.executor.GameExecutorManager;
import com.fury.game.content.skill.member.construction.House;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.object.GameObject;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.Direction;
import com.fury.util.Logger;
import com.fury.util.Utils;

import java.util.concurrent.TimeUnit;

@SuppressWarnings("serial")
public class Guard extends Mob {

	private House house;

	public Guard(int id, House house, GameObject object) {
		super(id, object, Revision.RS2);
		getDirection().setDirection(Direction.forID(Utils.getAngle(Utils.ROTATION_DIR_Y[object.getDirection()], Utils.ROTATION_DIR_X[object.getDirection()])));
		setForceAggressive(true);
		setTargetDistance(7);
		this.house = house;
	}

	@Override
	public void processNpc() {
		Entity target = getSpawnedFor();
		if (target != null && !isWithinDistance(target, 7)) {
			getMobCombat().reset();
			Position tile = getRespawnTile();
			getMovement().addWalkSteps(tile.getX(), tile.getY());
			getDirection().setDirection(Direction.NONE);
		}
		super.processNpc();
	}


	@Override
	public void setRespawnTask() {
		GameExecutorManager.slowExecutor.schedule(() -> {
            try {
                if (!house.isLoaded() || !house.isChallengeMode())
                    return;
                spawn();
            } catch (Throwable e) {
                Logger.handle(e);
            }
        }, getCombatDefinition().getRespawnDelay() * 600, TimeUnit.MILLISECONDS);
	}

}
