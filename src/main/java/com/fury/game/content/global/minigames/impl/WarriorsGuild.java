package com.fury.game.content.global.minigames.impl;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.task.Task;
import com.fury.game.content.controller.Controller;
import com.fury.game.content.dialogue.impl.minigames.warriorsguild.WarriorsGuildD;
import com.fury.game.content.skill.Skill;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.character.npc.drops.Drop;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.core.model.item.Item;
import com.fury.game.entity.object.GameObject;
import com.fury.game.node.entity.actor.object.ObjectHandler;
import com.fury.game.npc.minigames.AnimatedArmour;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;

import java.util.ArrayList;

public class WarriorsGuild extends Controller {

	/*
	 * The armors required to animate an Npc
	 */
	private static final int[][] ARMOR_DATA = {{1075, 1117, 1155, 4278}, {1067, 1115, 1153, 4279}, {1069, 1119, 1157, 4280}, {1077, 1125, 1165, 4281}, {1071, 1121, 1159, 4282}, {1073, 1123, 1161, 4283}, {1079, 1127, 1163, 4284}};
	private static final int[] ANIMATED_ARMOUR_NPCS = {4278, 4279, 4280, 4281, 4282, 4283, 4284};
	private static final int[] TOKEN_REWARDS = {5, 10, 15, 20, 26, 32, 40};
	/*
	 * The available defenders which players can receive from this minigame.
	 */
	private static final int[] DEFENDERS = new int[]{8844, 8845, 8846, 8847, 8848, 8849, 8850, 20072};

/*
	@Override
	public boolean canAttack(Figure target) {
		if (target instanceof AnimatedArmour) {
			AnimatedArmour npc = (AnimatedArmour) target;
			if (player != npc.getMobCombat().getTarget())
				return false;
		}
		return true;
	}*/

	/**
	// * Handles what happens when a player uses an item on the animator.
	// * @param player	The player
	// * @param item		The item the player is using
	// * @param object	That animator object which the player is using an item on
	 **/
/*	public static boolean itemOnAnimator(final Player player, final Item item, final GameObject object) {
		if(player.getMinigameAttributes().getWarriorsGuildAttributes().hasSpawnedArmour() && player.getRights() != PlayerRights.DEVELOPER) {
			player.message("You have already spawned some animated armour.");
			return true;
		} else {
			for(int i = 0; i < ARMOR_DATA.length; i++) {
				for(int f = 0; f < ARMOR_DATA[0].length; f++) {
					if(item.getId() == ARMOR_DATA[i][f]) {
						if(player.getInventory().contains(new Item(ARMOR_DATA[i][0])) && player.getInventory().contains(new Item(ARMOR_DATA[i][1])) && player.getInventory().contains(new Item(ARMOR_DATA[i][2]))) {
							final int items[] = new int[] {ARMOR_DATA[i][0], ARMOR_DATA[i][1], ARMOR_DATA[i][2]};
							if(items != null) {
								for(int a = 0; a < items.length; a++)
									player.getInventory().delete(new Item(items[a]));
								player.getMinigameAttributes().getWarriorsGuildAttributes().setSpawnedArmour(true);
								player.animate(827);
								player.message("You place some armor on the animator..", true);
								object.graphic(1930);
								final int index = i;
								GameWorld.schedule(2, () -> {
									Mob mob = GameWorld.getMobs().spawn(ARMOR_DATA[index][3], new Position(player.getX(), player.getY() - 1), true);
									mob.forceChat("I'M ALIVE!!!!");
									mob.getDirection().setInteracting(player);
									mob.getMobCombat().setTarget(player);
									mob.setSpawnedFor(player);
									player.getDirection().face(mob);
								});
							}
							return true;
						}
					}
				}
			}
		}
		return false;
	}
*/
/*	public static Drop[] getDrops(Player player, Mob mob) {
		ArrayList<Drop> drops = new ArrayList<>();
		if(mob.getId() >= 4278 && mob.getId() <= 4284) {
			int[] armour = null;
			for (int[] a : ARMOR_DATA) {
				if (a[3] == mob.getId()) {
					armour = new int[]{a[0], a[1], a[2]};
					break;
				}
			}
			if (armour != null) {
				for (int i : armour)
					drops.add(new Drop(i, 100.0, 1, 1, false));
				player.getMinigameAttributes().getWarriorsGuildAttributes().setSpawnedArmour(false);
				int amount = getTokenAmount(mob.getId());
				drops.add(new Drop(8851, 100.0, amount, amount, false));
			}
		}
		return drops.toArray(new Drop[drops.size()]);
	}
*/
	/**
	 * Gets the player's best defender
	 * @param player	The player to search items for
	 * @return			The best defender's item id
	 */
	public static int getDefender(Player player) {
		return DEFENDERS[getCurrentDefenderIndex(player)];
	}

	public static int getCurrentDefenderIndex(Player player) {
		int foundIndex = -1;
		for(int i = 0; i < DEFENDERS.length; i++) {
			if(player.hasItem(DEFENDERS[i])) {
				foundIndex = i;
			}
		}

		if(foundIndex != 7) {
			foundIndex++;
		}
		return foundIndex;
	}

	/**
	 * The warriors guild task
	 */
	public static void handleTokenRemoval(final Player player) {
		if(player.getMinigameAttributes().getWarriorsGuildAttributes().enteredTokenRoom())
			return;
		player.getMinigameAttributes().getWarriorsGuildAttributes().setEnteredTokenRoom(true);
		GameWorld.schedule(new Task(false, 160) {
			@Override
			public void run() {
				if(!player.getMinigameAttributes().getWarriorsGuildAttributes().enteredTokenRoom()) {
					this.stop();
					return;
				}
				if(!(player.getControllerManager().getController() instanceof WarriorsGuild) || player.getZ() != 2) {
					player.getMinigameAttributes().getWarriorsGuildAttributes().setEnteredTokenRoom(false);
					this.stop();
					return;
				}
				Item token = new Item(8851);
				if(player.getInventory().contains(token)) {
					int amount = player.getInventory().getAmount(token);
					player.getInventory().delete(new Item(token, Math.min(10, amount)));
					player.message("Some of your tokens crumble to dust..", true);
				} else {
					player.getMinigameAttributes().getWarriorsGuildAttributes().setEnteredTokenRoom(false);
					player.getCombat().resetCombat();
					player.getMovement().reset();
					player.moveTo(2844, 3536, 2);
					player.message("You have run out of tokens.");
					resetCyclopsCombat(player);
					this.stop();
				}
			}
		});
	}

	/**
	 * Gets the amount of tokens to receive from an npc
	 * @param npc	The npc to check how many tokens to receive from
	 * @return		The amount of tokens to receive as a drop
	 */
	private static int getTokenAmount(int npc) {
		for(int f = 0; f < ANIMATED_ARMOUR_NPCS.length; f++) {
			if (npc == ANIMATED_ARMOUR_NPCS[f]) {
				return TOKEN_REWARDS[f];
			}
		}
		return 5;
	}

	/**
	 * Resets any cyclops's combat who are in combat with player
	 * @param player	The player to check if cyclop is in combat with
	 */
	public static void resetCyclopsCombat(Player player) {
		for(Mob n : GameWorld.getRegions().getLocalNpcs(player)) {
			if(n == null)
				continue;
			if(n.getId() == 4291 && n.getCombat().isInCombat()) {
				n.getCombat().resetCombat();
				n.getMovement().reset();
			}
		}
	}

	public static boolean hasRequirements(Player player) {
		int total = player.getSkills().getMaxLevel(Skill.ATTACK) + player.getSkills().getMaxLevel(Skill.STRENGTH);
		boolean has99 = player.getSkills().hasLevel(Skill.ATTACK, 99) || player.getSkills().hasLevel(Skill.STRENGTH, 99);
		if (total < 140 && !has99) {
			player.message("");
			player.message("You do not meet the requirements of a Warrior.");
			player.message("You need to have a total Attack and Strength level of at least 140.");
			player.message("Having level 99 in either is fine as well.");
			return false;
		}
		return true;
	}

	@Override
	public boolean processObjectClick1(GameObject object) {
		switch (object.getId()) {
			case 2213://Bank
				return true;
			case 15653:
				player.moveTo(2877, 3542);
				removeController();
				return false;
			case 15638:
				player.moveTo(2840, 3535);
				break;
			case 1738:
				player.moveTo(2840, 3535, 2);
				break;
			case 15644:
			case 15641:
				switch (player.getZ()) {
					case 2:
						if (player.getX() == 2846) {
							if (player.getInventory().getAmount(new Item(8851)) < 70) {
								player.message("You need at least 70 tokens to enter this area.");
								return false;
							}
							player.getDialogueManager().startDialogue(new WarriorsGuildD());
							player.moveTo(new Position(2847, player.getY(), 2));
							WarriorsGuild.handleTokenRemoval(player);
						} else if (player.getX() == 2847) {
							WarriorsGuild.resetCyclopsCombat(player);
							player.moveTo(new Position(2846, player.getY(), 2));
							player.getMinigameAttributes().getWarriorsGuildAttributes().setEnteredTokenRoom(false);
						}
						return false;
				}
				if (object.getDefinition().containsOption(0, "Open"))
					ObjectHandler.handleDoor(object);
				return false;
		}
		return false;
	}

    @Override
    public boolean processObjectClick2(GameObject object) {
        return object.getId() == 2213 || super.processObjectClick2(object);
    }

    @Override
	public void start() {

	}

	@Override
	public void magicTeleported(int type) {
		removeController();
	}

	@Override
	public boolean login() {
		start();
		return false;
	}

	@Override
	public boolean logout() {
		return false;
	}
}
