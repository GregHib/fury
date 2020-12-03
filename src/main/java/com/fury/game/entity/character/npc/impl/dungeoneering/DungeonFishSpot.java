package com.fury.game.entity.character.npc.impl.dungeoneering;

import com.fury.cache.Revision;
import com.fury.game.world.map.Position;
import com.fury.util.Misc;
import com.fury.game.content.skill.free.dungeoneering.DungeonManager;
import com.fury.game.content.skill.free.dungeoneering.skills.DungeoneeringFishing;

@SuppressWarnings("serial")
public class DungeonFishSpot extends DungeonMob {

	private DungeoneeringFishing.Fish fish;
	private int fishes;

	public DungeonFishSpot(int id, Position tile, DungeonManager manager, DungeoneeringFishing.Fish fish) {
		super(id, tile, manager, 1, Revision.PRE_RS3);
		this.fish = fish;
		setName(Misc.formatPlayerNameForDisplay(fish.toString()));
		fishes = 14;
	}

	@Override
	public void processNpc() {

	}

	public DungeoneeringFishing.Fish getFish() {
		return fish;
	}

	public int decreaseFish() {
		return fishes--;
	}

	public void addFishes() {
		fishes += Misc.random(5, 10);
	}
}
