package com.fury.game.content.skill.free.dungeoneering;

import com.fury.game.entity.object.GameObject;
import com.fury.core.model.item.Item;
import com.fury.game.world.map.Position;
import com.fury.game.content.skill.free.dungeoneering.rooms.BossRoom;
import com.fury.game.content.skill.free.dungeoneering.rooms.HandledRoom;
import com.fury.game.content.skill.free.dungeoneering.rooms.StartRoom;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.core.model.node.entity.actor.figure.player.Player;

import java.util.ArrayList;

public class VisibleRoom {

	private int[] musicId;
	private ArrayList<Mob> guardians;
	private int guardianCount;
	private boolean noMusic;
	private boolean loaded;
	protected RoomReference reference;
	protected DungeonManager manager;
	protected int type;

	public void init(DungeonManager manager, RoomReference ref, int type, HandledRoom room) {
		this.type = type;
		this.reference = ref;
		this.manager = manager;
		if (room instanceof StartRoom)
			musicId = new int[]
			{ DungeonConstants.START_ROOM_MUSICS[type] };
		else if (room instanceof BossRoom)
			musicId = new int[]
			{ ((BossRoom) room).getMusicId() };
		else {
			musicId = new int[]
			{ DungeonUtils.getSafeMusic(type), DungeonUtils.getDangerousMusic(type) };
			guardians = new ArrayList<Mob>();
		}
	}

	public int getMusicId() {
		return noMusic ? -2 : musicId[roomCleared() ? 0 : 1];
	}

	public boolean roomCleared() {
		if (guardians == null)
			return true;
		for (Mob n : guardians)
			if (!n.getFinished() && !n.isDead())
				return false;
		return true;
	}

	public void addGuardian(Mob n) {
		guardians.add(n);
		guardianCount++;
	}

	public boolean removeGuardians() {
		if (roomCleared()) {
			guardians = null;
			return true;
		}
		return false;
	}

	public void forceRemoveGuardians() {
		if (guardians != null) {
			for (Mob n : guardians)
				n.deregister();
			guardians.clear();
		}
	}

	public int getGuardiansCount() {
		return guardianCount;
	}

	public int getKilledGuardiansCount() {
		return guardians == null ? guardianCount : (guardianCount - guardians.size());
	}

	public void setNoMusic() {
		noMusic = true;
	}

	public boolean isLoaded() {
		return loaded;
	}

	public void setLoaded() {
		loaded = true;
	}

	public void destroy() {

	}

	public void openRoom() {

	}

	public boolean processObjectClick1(Player p, GameObject object) {
		return true;
	}

	public boolean processObjectClick2(Player p, GameObject object) {
		return true;
	}

	public boolean processObjectClick3(Player p, GameObject object) {
		return true;
	}

	public boolean processObjectClick4(Player p, GameObject object) {
		return true;
	}

	public boolean processObjectClick5(Player p, GameObject object) {
		return true;
	}

	public boolean handleItemOnObject(Player player, GameObject object, Item item) {
		return true;
	}

	public boolean processNPCClick1(Player player, Mob mob) {
		return true;
	}

	public boolean processNPCClick2(Player player, Mob mob) {
		return true;
	}

	public boolean canMove(Player player, Position to) {
		return true;
	}

	public boolean processNPCClick3(Player player, Mob mob) {
		return true;
	}

}
