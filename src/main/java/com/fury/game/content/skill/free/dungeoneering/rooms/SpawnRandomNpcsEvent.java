package com.fury.game.content.skill.free.dungeoneering.rooms;

import com.fury.game.content.skill.free.dungeoneering.DungeonManager;
import com.fury.game.content.skill.free.dungeoneering.RoomReference;

public class SpawnRandomNpcsEvent implements RoomEvent {

	@Override
	public void openRoom(DungeonManager dungeon, RoomReference reference) {
		dungeon.spawnRandomNPCS(reference);
	}

}
