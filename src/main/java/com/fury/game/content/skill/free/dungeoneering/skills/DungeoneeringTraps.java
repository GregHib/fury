package com.fury.game.content.skill.free.dungeoneering.skills;

import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.free.dungeoneering.DungeonManager;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.character.npc.impl.dungeoneering.MastyxTrap;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import com.fury.util.Misc;

public class DungeoneeringTraps {

	public static final Item[] ITEM_TRAPS =
	{ new Item(17756), new Item(17758), new Item(17760), new Item(17762), new Item(17764), new Item(17766), new Item(17768), new Item(17770), new Item(17772), new Item(17774 )};
	private static final int[] MASTRYX_HIDES =
	{ 17424, 17426, 17428, 17430, 17432, 17434, 17436, 17438, 17440, 17442 };
	private static final int[] HUNTER_LEVELS =
	{ 1, 10, 20, 30, 40, 50, 60, 70, 80, 90 };

	public static void placeTrap(final Player player, final DungeonManager manager, final int index) {
		int levelRequired = HUNTER_LEVELS[index];
		if (manager.getMastyxTraps().size() > 5) {
			player.message("Your party has already placed the maximum amount of traps allowed.");
			return;
		} else if (player.getSkills().getLevel(Skill.HUNTER) < levelRequired) {
			player.message("You need a Hunter level of " + levelRequired + " in order to place this trap.");
			return;
		}
		player.getMovement().lock(2);
		player.animate(827);
		/*WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				manager.addMastyxTrap(new MastyxTrap(player.getUsername(), 11076 + index, player, -1, false));
				player.getInventory().delete(new Item(ITEM_TRAPS[index], 1));
				player.message("You lay the trap onto the floor.");
			}
		}, 2);*/
	}

	public static void removeTrap(Player player, MastyxTrap trap, DungeonManager manager) {
		if (player.getUsername().equals(trap.getPlayerName())) {
			player.animate(827);
			player.message("You dismantle the trap.", true);
			player.getInventory().add(ITEM_TRAPS[trap.getTier()]);
			manager.removeMastyxTrap(trap);
		} else
			player.message("This trap is not yours to remove!");
	}

	public static void skinMastyx(Player player, Mob mob) {
		player.animate(827);
		player.getInventory().add(new Item(MASTRYX_HIDES[getNPCTier(mob.getId() - 10)], Misc.random(2, 5)));
		mob.deregister();
	}

	public static int getNPCTier(int id) {
		return id - 11086;
	}
}
