package com.fury.game.content.skill.member.agility;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.container.impl.equip.Slot;
import com.fury.game.content.global.events.christmas.ChristmasEvent;
import com.fury.game.content.misc.items.StrangeRocks;
import com.fury.game.content.skill.Skill;
import com.fury.game.entity.character.player.actions.ForceMovement;
import com.fury.game.entity.character.player.info.DonorStatus;
import com.fury.core.model.item.Item;
import com.fury.game.entity.object.GameObject;
import com.fury.game.node.entity.actor.figure.player.handles.Settings;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.Animation;

public class Agility {

    public static void cross(Player player, int levelReq, Position tile, Animation animation) {
        if (!player.getSkills().hasRequirement(Skill.AGILITY, levelReq, "use this shortcut"))
            return;

        player.getMovement().lock();
        final boolean running = player.getSettings().getBool(Settings.RUNNING);
        player.getSettings().set(Settings.RUNNING, false);
        player.setSkillAnimation(animation);
        player.getMovement().addWalkSteps(tile.getX(), tile.getY(), false);
        player.message("You attempt to walk across the log..");
        System.out.println("Abs: " + Math.abs(player.getX() - tile.getX()));
        GameWorld.schedule(Math.abs(player.getX() - tile.getX()), () -> {
            player.moveTo(tile);
            player.getMovement().unlock();
            player.setSkillAnimation(null);
            player.getSettings().set(Settings.RUNNING, running);
            player.message("... and make it safely to the other side.", true);
        });
    }

    public static void crossLog(Player player, int levelReq, Position tile) {
        if (player.getSkills().getLevel(Skill.AGILITY) < levelReq) {
            player.message("You need an Agility level of at least " + levelReq + " or higher to use this shortcut.");
            return;
        }

        player.getMovement().lock();
        final boolean running = player.getSettings().getBool(Settings.RUNNING);
        player.getSettings().set(Settings.RUNNING, false);
        player.setSkillAnimation(new Animation(9908));
        player.getMovement().addWalkSteps(tile.getX(), tile.getY(), false);
        player.message("You attempt to walk across the log..");
        GameWorld.schedule(Math.abs(player.getX() - tile.getX()), () -> {
            player.moveTo(tile);
            player.getMovement().unlock();
            player.setSkillAnimation(null);
            player.getSettings().set(Settings.RUNNING, running);
            player.message("... and make it safely to the other side.", true);
        });
    }

    public static boolean hasLevel(Player player, int level) {
        if (player.getSkills().getLevel(Skill.AGILITY) < level) {
            player.message("You need an Agility level of " + level + " to use this obstacle.");
            return false;
        }
        return true;
    }

    public static boolean handleObject(Player player, GameObject object) {
        if (GnomeCourse.handleObstacles(player, object, true))
            return true;

        if (BarbarianCourse.handleObstacles(player, object))
            return true;

        return WildernessCourse.handleObstacles(player, object, true);
    }

	public static void walkLedge(final Player player, final GameObject object, boolean left, int level, int targetX, int targetY, int delay) {
		if (!Agility.hasLevel(player, level))
			return;
		player.message("You put your foot on the ledge and try to edge across...", true);
		player.getMovement().lock();
		player.animate(left ? 753 : 752);
		player.setSkillAnimation(new Animation(left ? 756 : 754));
		final Position toTile = new Position(targetX, targetY, object.getZ());
		boolean running = player.getSettings().getBool(Settings.RUNNING);
		player.getSettings().set(Settings.RUNNING, false);
		player.getMovement().addWalkSteps(toTile.getX(), toTile.getY(), false);
		GameWorld.schedule(delay, () -> {
			player.getMovement().unlock();
			player.animate(left ? 759 : 658);
			player.setSkillAnimation(null);
			player.getSettings().set(Settings.RUNNING, running);
			player.message("You skilfully edge across the gap.", true);
		});
	}

	public static void enterPipe(final Player player, GameObject object, int level, int targetX, int targetY, int direction) {
		if (!Agility.hasLevel(player, level))
			return;
		player.getMovement().lock();
		player.animate(10580);
		final Position toTile = new Position(targetX, targetY, object.getZ());
		player.setForceMovement(new ForceMovement(player, 0, toTile, 2, direction));
		GameWorld.schedule(1, () -> {
			player.moveTo(toTile);
			player.getMovement().unlock();
		});
	}

	public static void giveTickets(Player player, int amount) {
		if(DonorStatus.isDonor(player, DonorStatus.DIAMOND_DONOR))
			amount += 2;
		else if(DonorStatus.isDonor(player, DonorStatus.EMERALD_DONOR))
			amount += 1;
		player.getInventory().add(new Item(2996, amount));
		ChristmasEvent.giveSnowflake(player);
		StrangeRocks.handleStrangeRocks(player, Skill.AGILITY);
	}
	
	public static void addExperience(Player player, double experience) {
		double modifier = 1;
		if(player.getEquipment().get(Slot.BODY).getId() == 14936)modifier += .25;
		if(player.getEquipment().get(Slot.LEGS).getId() == 14938)modifier += .25;
		if(player.getEquipment().get(Slot.SHIELD).getId() == 15439)modifier += .005;
		player.getSkills().addExperience(Skill.AGILITY, experience, modifier);
		StrangeRocks.handleStrangeRocks(player, Skill.AGILITY);
	}
}
