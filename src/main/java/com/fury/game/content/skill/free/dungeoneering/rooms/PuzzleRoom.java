package com.fury.game.content.skill.free.dungeoneering.rooms;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.free.dungeoneering.VisibleRoom;
import com.fury.game.entity.object.GameObject;
import com.fury.game.node.entity.actor.object.ObjectManager;
import com.fury.util.Misc;

public abstract class PuzzleRoom extends VisibleRoom {

	private boolean complete;
	private int[] requirements = new int[25];
	private int[] giveXPCount = new int[25];

	public final boolean hasRequirement(Player p, Skill skill) {
		return p.getSkills().getLevel(skill) >= getRequirement(skill);
	}

	public final int getRequirement(Skill skill) {
		setLevel(skill);
		return requirements[skill.ordinal()];
	}

	public final void giveXP(Player player, Skill skill) {
		if (giveXPCount[skill.ordinal()] < 4) {
			//You only gain xp for the first 4 times you do an action
			giveXPCount[skill.ordinal()]++;
			player.getSkills().addExperience(skill, getRequirement(skill) * 5 +10);
		}
	}

	private void setLevel(Skill skill) {
		if (requirements[skill.ordinal()] == 0)
			requirements[skill.ordinal()] = !manager.getRoom(reference).isCritPath() ? Misc.exclusiveRandom(30, skill == Skill.SUMMONING || skill == Skill.PRAYER ? 100 : 106) : Math.max(1, (manager.getParty().getMaxLevel(skill) - Misc.getRandom(10)));
	}

	public void replaceObject(GameObject object, int newId) {
		if(object == null)
			return;
		GameObject newObject = new GameObject(newId, object, object.getType(), object.getDirection());
		ObjectManager.spawnObject(newObject);
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete() {
		this.complete = true;
		if (getCompleteMessage() != null)
			manager.message(reference, getCompleteMessage());
		manager.getRoom(reference).removeChallengeDoors();
	}

	public String getCompleteMessage() {
		return "You hear a clunk as the doors unlock.";
	}

	public String getLockMessage() {
		return "The door is locked. You can't see any obvious keyhole or mechanism.";
	}

}
