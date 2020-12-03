package com.fury.game.entity.character.npc.impl.dungeoneering;

import com.fury.core.model.node.entity.Entity;
import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.content.dialogue.impl.skills.dungeoneering.DivineSkinweaverD;
import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.free.dungeoneering.DungeonConstants;
import com.fury.game.content.skill.free.dungeoneering.DungeonManager;
import com.fury.game.content.skill.free.dungeoneering.RoomReference;
import com.fury.game.entity.character.combat.CombatIcon;
import com.fury.game.entity.character.combat.Hit;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.object.GameObject;
import com.fury.game.node.entity.actor.object.ObjectManager;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.HitMask;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.game.world.update.flag.block.graphic.GraphicHeight;
import com.fury.util.FontUtils;
import com.fury.util.Misc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("serial")
public final class DivineSkinweaver extends DungeonBoss {

	private static final int[] SKELETONS =
	{ 10630, 10680, 10693 };

	private static final int[][] HOLES =
	{
	{ 0, 10 },
	{ 5, 15 },
	{ 11, 15 },
	{ 15, 10 },
	{ 15, 5 } };

	private static final String[] CLOSE_HOLE_MESSAGES =
	{ "Ride the wind and smite the tunnel.", "We have little time, tear down the tunnel.", "Churra! Bring down the tunnel while you can." };

	private final boolean[] holeClosed;
	private int count;
	private boolean requestedClose;
	private int healDelay;
	private int respawnDelay;
	private final List<DungeonSkeletonBoss> skeletons;
	private int killedCount;

	public DivineSkinweaver(int id, Position tile, DungeonManager manager, RoomReference reference) {
		super(id, tile, manager, reference);
		holeClosed = new boolean[5];
		skeletons = Collections.synchronizedList(new ArrayList<DungeonSkeletonBoss>());
	}

	public void removeSkeleton(DungeonSkeletonBoss skeleton) {
		skeletons.remove(skeleton);
		if (!requestedClose && count < holeClosed.length) {
			killedCount++;
			if (killedCount == 3) {
				requestedClose = true;
				killedCount = 0;
				setNextForceTalk(CLOSE_HOLE_MESSAGES[Misc.random(CLOSE_HOLE_MESSAGES.length)]);
				for (Player p2 : getManager().getParty().getTeam()) {
					if (!getManager().isAtBossRoom(p2))
						continue;
					p2.message("Divine skinweaver: " + FontUtils.add(getNextForceTalk(), 0x99cc66));
				}
			}
		}
	}

	private int[] getOpenHole() {
		List<int[]> holes = new ArrayList<int[]>();
		for (int[] hole : HOLES) {
			GameObject object = getManager().getObjectWithType(getReference(), 49289, 0, hole[0], hole[1]);
			if (object != null && object.getId() != 49289)
				holes.add(new int[]
				{ object.getX() + Misc.ROTATION_DIR_X[object.getDirection()], object.getY() + Misc.ROTATION_DIR_Y[object.getDirection()] });
		}
		if (holes.size() == 0)
			return null;
		return holes.get(Misc.random(holes.size()));
	}

	@Override
	public void processNpc() {
		ArrayList<Figure> targets = getPossibleTargets(true, true);
		if (respawnDelay > 0) {
			respawnDelay--;
		} else if (count < holeClosed.length && targets.size() != 0 && skeletons.size() < 5) { //blablala spawn skeletons
			int[] coords = getOpenHole();
			if (coords != null) {
				skeletons.add((DungeonSkeletonBoss) getManager().spawnNPC(SKELETONS[Misc.random(SKELETONS.length)], 0, new Position(coords[0], coords[1], 0), getReference(), DungeonConstants.BOSS_NPC, 0.4D));
				respawnDelay = 10;
			}
		}
		if (healDelay > 0) {
			healDelay--;
			return;
		}
		Entity healTarget = null;
		for (Entity target : targets) {
			if(target.isNpc())
				if(((Mob) target).getHealth().getHitpoints() >= ((Mob) target).getCombatDefinition().getHitpoints())
					continue;
			if(target.isPlayer())
				if(((Player) target).getHealth().getHitpoints() >= ((Player) target).getSkills().getMaxLevel(Skill.CONSTITUTION))
					continue;
			if (healTarget == null || Misc.getDistance(this, healTarget) > Misc.getDistance(this, target))
				healTarget = target;
		}
		if (healTarget == null)
			return;
		int distance = (4 - Misc.getDistance(this, healTarget));
		if (distance == 4 || distance < 0)
			return;
		if(healTarget.isNpc()) {
			Mob healTarg = (Mob) healTarget;
			int maxHeal = healTarg.getCombatDefinition().getHitpoints();
			healTarg.getHealth().heal((distance + 1) * maxHeal / 4);
			animate(13678);
			perform(new Graphic(2445));
			healTarg.perform(new Graphic(2443, 60, GraphicHeight.HIGH));
			getDirection().face(healTarget);
			healDelay = 4;
		} else if(healTarget.isPlayer()) {
			Player healTarg = (Player) healTarget;
			int maxHeal = healTarg.getSkills().getMaxLevel(Skill.CONSTITUTION);
			healTarg.getHealth().heal((distance + 1) * maxHeal / 4);
			animate(13678);
			perform(new Graphic(2445));
			healTarg.perform(new Graphic(2443, 60, GraphicHeight.HIGH));
			getDirection().face(healTarg);
			healDelay = 4;
		}
	}

	public void talkTo(Player player) {
		if (count < holeClosed.length || skeletons.size() > 0) {
			player.getDialogueManager().startDialogue(new DivineSkinweaverD());
			return;
		}
		if (killedCount == Integer.MAX_VALUE)
			return;
		forceChat("I see little danger in this room so move on to the next with my thanks.");
		for (Player p2 : getManager().getParty().getTeam()) {
			if (!getManager().isAtBossRoom(p2))
				continue;
			p2.message("Divine skinweaver: " + FontUtils.add(getNextForceTalk(), 0x99cc66));
		}
		getManager().openStairs(getReference());
		drop();
		killedCount = Integer.MAX_VALUE;
	}

	public void blockHole(Player player, GameObject object) {
		if (count >= holeClosed.length)
			return;
		player.animate(833);
		if (!requestedClose) {
			player.message("The portal is fully powered and shocks you with a large burst of energy.");
			player.getCombat().applyHit(new Hit((int) (player.getSkills().getMaxLevel(Skill.CONSTITUTION) * 0.2), HitMask.RED, CombatIcon.NONE));
			return;
		}
		holeClosed[count++] = true;
		requestedClose = false;
		GameObject closedHole = new GameObject(object);
		closedHole.setId(49289);
		ObjectManager.spawnObject(closedHole);
	}
}
