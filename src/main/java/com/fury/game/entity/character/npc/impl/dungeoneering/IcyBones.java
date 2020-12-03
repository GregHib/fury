package com.fury.game.entity.character.npc.impl.dungeoneering;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.content.skill.free.dungeoneering.DungeonManager;
import com.fury.game.content.skill.free.dungeoneering.RoomReference;
import com.fury.game.entity.character.combat.CombatIcon;
import com.fury.game.entity.character.combat.Hit;
import com.fury.game.entity.object.GameObject;
import com.fury.game.node.entity.actor.object.ObjectManager;
import com.fury.game.world.GameWorld;
import com.fury.game.world.World;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.HitMask;
import com.fury.util.Misc;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public final class IcyBones extends DungeonBoss {

	public IcyBones(int id, Position tile, DungeonManager manager, RoomReference reference) {
		super(id, tile, manager, reference);
		spikes = new ArrayList<>();
	}

	//@Override
	public double getMeleePrayerMultiplier() {
		return 0.6;
	}

	//@Override
	public double getMagePrayerMultiplier() {
		return 0.6;
	}

	//@Override
	public double getRangePrayerMultiplier() {
		return 0.6;
	}

	private List<GameObject> spikes;

	public void removeSpikes() {
		if (spikes.isEmpty())
			return;
		for (GameObject object : spikes)
			ObjectManager.removeObject(object);
		spikes.clear();
	}

	public boolean sendSpikes() {
		if (!spikes.isEmpty())
			return false;
		int sizeX = getSizeX();
		int sizeY = getSizeY();
		for (int x = -1; x < 7; x++) {
			for (int y = -1; y < 7; y++) {
				if (((x != -1 && x != 6) && (y != -1 && y != 6)) || Misc.random(2) != 0)
					continue;
				Position tile = add(x - sizeX, y - sizeY, 0);
				RoomReference current = getManager().getCurrentRoomReference(tile);
				if (current.getX() != getReference().getX() || current.getY() != getReference().getY() || (World.getMask(tile.getZ(), tile.getX(), tile.getY()) & 0x1280120) != 0)
					continue;
				GameObject object = new GameObject(52285 + Misc.random(3), tile, 10, Misc.random(4));
				spikes.add(object);
				ObjectManager.spawnObject(object);
				for (Player player : getManager().getParty().getTeam()) {
					if (player.getX() == object.getX() && player.getY() == object.getY())
						player.getCombat().applyHit(new Hit(1 + Misc.random(getMaxHit()), HitMask.RED, CombatIcon.NONE));
				}
			}
		}
		GameWorld.schedule(10, this::removeSpikes);
		return true;
	}

}
