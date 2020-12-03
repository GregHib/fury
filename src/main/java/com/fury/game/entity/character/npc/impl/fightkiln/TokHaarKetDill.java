package com.fury.game.entity.character.npc.impl.fightkiln;


import com.fury.game.content.controller.impl.FightKiln;
import com.fury.game.world.map.Position;

@SuppressWarnings("serial")
public class TokHaarKetDill extends FightKilnMob {

	public int receivedHits = 0;

	private FightKiln controller;
	
	public TokHaarKetDill(int id, Position tile, FightKiln controller) {
		super(id, tile, controller);
		this.controller = controller;
	}
}
