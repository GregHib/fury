package com.fury.game.content.skill.free.fishing;

import com.fury.game.container.impl.equip.Slot;
import com.fury.game.content.misc.items.StrangeRocks;
import com.fury.game.content.dialogue.impl.misc.SimpleMessageD;
import com.fury.game.content.global.Achievements;
import com.fury.game.content.global.action.Action;
import com.fury.game.content.global.events.christmas.ChristmasEvent;
import com.fury.game.content.skill.Skill;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.util.Misc;

public class Fishing extends Action {

	private FishingSpots spot;
	private Mob mob;
	private Position tile;
	private int fishId;

	private final int[] BONUS_FISH = { 341, 349, 401, 407 };

	private boolean multipleCatch;

	public Fishing(FishingSpots spot, Mob mob) {
		this.spot = spot;
		this.mob = mob;
		tile = mob.copyPosition();
	}

	@Override
	public boolean start(Player player) {
		if (!checkAll(player))
			return false;
		player.message("You attempt to capture a fish...", true);
		if(spot.getAnimation().getId() == 622)
			player.setSkillAnimation(new Animation(623));

		player.perform(spot.getAnimation());
		setActionDelay(player, getFishingDelay(player));
		return true;
	}

	@Override
	public boolean process(Player player) {
		return checkAll(player);
	}

	private int getFishingDelay(Player player) {
		int playerLevel = player.getSkills().getLevel(Skill.FISHING);
		int fishLevel = spot.getFish()[fishId].getLevel();
		int modifier = spot.getFish()[fishId].getLevel();
		int randomAmt = Misc.random(4);
		double cycleCount = 1, otherBonus = 0;
		if (player.getFamiliar() != null)
			otherBonus = getSpecialFamiliarBonus(player.getFamiliar().getId());
		cycleCount = Math.ceil(((fishLevel + otherBonus) * 50 - playerLevel * 10) / modifier * 0.25 - randomAmt * 4);
		if (cycleCount < 1)
			cycleCount = 1;
		int delay = (int) cycleCount + 1;
		delay /= player.getAuraManager().getFishingAccurayMultiplier();
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

	private int getRandomFish(Player player) {
		int random = Misc.random(spot.getFish().length);
		int difference = player.getSkills().getLevel(Skill.FISHING) - spot.getFish()[random].getLevel();
		if (difference <= -1)
			random = 0;
		return random;
	}

	@Override
	public int processWithDelay(Player player) {
		player.perform(spot.getAnimation());
		addFish(player);
		return getFishingDelay(player);
	}

	private void addFish(Player player) {
		if (spot.getFish()[fishId] == Fish.TUNA || spot.getFish()[fishId] == Fish.SHARK
				|| spot.getFish()[fishId] == Fish.SWORDFISH) {
			if (Misc.random(50) <= 5) {
				if (player.getSkills().getLevel(Skill.AGILITY) >= spot.getFish()[fishId].getLevel())
					multipleCatch = true;
			} else
				multipleCatch = false;
		}

		Item fish = new Item(spot.getFish()[fishId].getId(), multipleCatch ? 2 : 1);
		player.getPacketSender().sendMessage(getMessage(fish), true);
		player.getInventory().delete(spot.getBait());
		double totalXp = spot.getFish()[fishId].getXp();
		if (hasFishingSuit(player))
			totalXp *= 1.025;
		player.getSkills().addExperience(Skill.FISHING, totalXp);
		player.getInventory().add(fish);
		StrangeRocks.handleStrangeRocks(player, Skill.FISHING);

		if(spot.getFish()[fishId] == Fish.TROUT)
			Achievements.finishAchievement(player, Achievements.AchievementData.CATCH_A_TROUT);
		else if(spot.getFish()[fishId] == Fish.LOBSTER)
			Achievements.doProgress(player, Achievements.AchievementData.CATCH_20_LOBSTERS);

		if (player.getFamiliar() != null) {
			if (Misc.random(50) == 0 && getSpecialFamiliarBonus(player.getFamiliar().getId()) > 0) {
				player.getInventory().add(new Item(BONUS_FISH[Misc.random(BONUS_FISH.length)]));
				player.getSkills().addExperience(Skill.FISHING, 5.5);
			}
		}
		fishId = getRandomFish(player);
		if (Misc.random(50) == 0 && FishingSpotsHandler.moveSpot(mob))
			player.animate(-1);
		ChristmasEvent.giveSnowflake(player);
	}

	private boolean hasFishingSuit(Player player) {
        return player.getEquipment().get(Slot.HEAD).getId() == 24427 && player.getEquipment().get(Slot.BODY).getId() == 24428
                && player.getEquipment().get(Slot.LEGS).getId() == 24429 && player.getEquipment().get(Slot.FEET).getId() == 24430;
    }

	private String getMessage(Item fish) {
		if (spot.getFish()[fishId] == Fish.ANCHOVIES || spot.getFish()[fishId] == Fish.SHRIMP)
			return "You manage to catch some " + fish.getName().toLowerCase() + ".";
		else if (multipleCatch)
			return "Your quick reactions allow you to catch two " + fish.getName().toLowerCase() + ".";
		else
			return "You manage to catch a " + fish.getName().toLowerCase() + ".";
	}

	private boolean checkAll(Player player) {
		if (player.getSkills().getLevel(Skill.FISHING) < spot.getFish()[fishId].getLevel()) {
			player.getDialogueManager().startDialogue(new SimpleMessageD(), "You need a fishing level of " + spot.getFish()[fishId].getLevel() + " to fish here.");
			return false;
		}
		if (!player.getInventory().contains(spot.getTool())) {
			player.message("You need a " + spot.getTool().getDefinition().getName().toLowerCase() + " to fish here.");
			return false;
		}

		if (spot.getBait() != null && player.getInventory().getAmount(spot.getBait()) < 1) {
			player.message("You don't have " + spot.getBait().getDefinition().getName().toLowerCase() + " to fish here.");
			return false;
		}
		if (player.getInventory().getSpaces() <= 0) {
			player.animate(-1);
			player.getDialogueManager().startDialogue(new SimpleMessageD(), "You don't have enough inventory space.");
			return false;
		}
        return !(tile.getX() != mob.getX() || tile.getY() != mob.getY());
    }

	@Override
	public void stop(final Player player) {
		player.setSkillAnimation(null);
		setActionDelay(player, 3);
	}
}
