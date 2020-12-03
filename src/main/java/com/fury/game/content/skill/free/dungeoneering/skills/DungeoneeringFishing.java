package com.fury.game.content.skill.free.dungeoneering.skills;

import com.fury.game.entity.character.combat.CombatIcon;
import com.fury.game.entity.character.combat.Hit;
import com.fury.game.system.communication.MessageType;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.game.world.update.flag.block.HitMask;
import com.fury.game.content.skill.Skill;
import com.fury.core.model.item.Item;
import com.fury.util.Misc;
import com.fury.game.entity.character.npc.impl.dungeoneering.DungeonFishSpot;
import com.fury.core.model.node.entity.actor.figure.player.Player;

public class DungeoneeringFishing {

	public enum Fish {

		HEIM_CRAB(17797, 1, 9),

		RED_EYE(17799, 10, 27),

		DUSK_EEL(17801, 20, 45),

		GIANT_FLATFISH(17803, 30, 63),

		SHORTFINNED_EEL(17805, 40, 81),

		WEB_SNIPPER(17807, 50, 99),

		BOULDABASS(17809, 60, 117),

		SALVE_EEL(17811, 70, 135),

		BLUE_CRAB(17813, 80, 153),

		CAVE_MORAY(17815, 90, 171),

		VILE_FISH(17374, 1, 0);

		private final int id, level;
		private final double xp;

		Fish(int id, int level, double xp) {
			this.id = id;
			this.level = level;
			this.xp = xp;
		}

		public int getId() {
			return id;
		}

		public int getLevel() {
			return level;
		}

		public double getXp() {
			return xp;
		}
	}

	public static final int FLY_FISHING_ROAD_EMOTE = 622;
	public static final Item FLY_FISHING_ROAD = new Item(17794), FEATHER = new Item(17796);

	private DungeonFishSpot spot;

	public DungeoneeringFishing(DungeonFishSpot spot) {
		this.spot = spot;
	}

	//@Override
	public boolean start(Player player) {
		if (!checkAll(player))
			return false;
		player.getPacketSender().sendMessage(MessageType.NPC_ALERT, "You attempt to capture a fish...");
		//setActionDelay(player, getFishingDelay(player));
		return true;
	}

	//@Override
	public boolean process(Player player) {
		player.perform(new Animation(FLY_FISHING_ROAD_EMOTE));
		return checkAll(player);
	}

	private int getFishingDelay(Player player) {
		int playerLevel = player.getSkills().getLevel(Skill.FISHING);
		int fishLevel = spot.getFish().getLevel();
		int modifier = spot.getFish().getLevel();
		int randomAmt = Misc.random(4);
		double cycleCount = 1, otherBonus = 0;
		if (player.getFamiliar() != null)
			otherBonus = getSpecialFamiliarBonus(player.getFamiliar().getId());
		cycleCount = Math.ceil(((fishLevel + otherBonus) * 50 - playerLevel * 10) / modifier * 0.25 - randomAmt * 4);
		if (cycleCount < 1)
			cycleCount = 1;
		int delay = (int) cycleCount + 1;
		//delay /= player.getAuraManager().getFishingAccurayMultiplier();
		return delay;

	}

	private int getSpecialFamiliarBonus(int id) {
		switch (id) {
		case 6796:
		case 6795:// rock crab
			return 1;
		}
		return -1;
	}

	//@Override
	public int processWithDelay(Player player) {
		addFish(player);
		return getFishingDelay(player);
	}

	private void addFish(Player player) {
		player.getPacketSender().sendMessage(MessageType.NPC_ALERT, "You manage to catch a " + new Item(spot.getFish().id).getName().toLowerCase() + ".");
		player.getInventory().delete(FEATHER);
		player.getSkills().addExperience(Skill.FISHING, spot.getFish().xp);
		player.getInventory().add(new Item(spot.getFish().id));
		if (spot.decreaseFish() <= 1) {
			if (spot.getFish() == Fish.VILE_FISH) {
				spot.addFishes();
				player.getCombat().applyHit(new Hit((int) (player.getSkills().getMaxLevel(Skill.CONSTITUTION) * .3), HitMask.RED, CombatIcon.NONE));
				player.message("You have a hilarious fishing accident that one day you'll tell your grandchildren about.");
				return;
			}
			spot.deregister();
			player.animate(-1);
			player.message("You have depleted this resource.");
		}
	}

	private boolean checkAll(Player player) {
		if (player.getSkills().getLevel(Skill.FISHING) < spot.getFish().getLevel()) {
			player.getDialogueManager().sendStatement("You need a fishing level of " + spot.getFish().getLevel() + " to fish here.");
			return false;
		}
		if (!player.getInventory().contains(FLY_FISHING_ROAD)) {
			player.message("You need a " + FLY_FISHING_ROAD.getDefinition().getName().toLowerCase() + " to fish here.");
			return false;
		}
		if (!player.getInventory().contains(FEATHER)) {
			player.message("You don't have " + FEATHER.getDefinition().getName().toLowerCase() + " to fish here.");
			return false;
		}
		if (player.getInventory().getSpaces() < 1) {
			player.animate(-1);
			player.getDialogueManager().sendStatement("You don't have enough inventory space.");
			return false;
		}
		return !spot.getFinished();
	}

	//@Override
	public void stop(final Player player) {
		//setActionDelay(player, 3);
	}
}
