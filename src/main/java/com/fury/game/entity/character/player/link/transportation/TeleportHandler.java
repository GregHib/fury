package com.fury.game.entity.character.player.link.transportation;

import com.fury.game.container.impl.equip.Equipment;
import com.fury.game.content.controller.impl.Daemonheim;
import com.fury.game.content.controller.impl.Wilderness;
import com.fury.game.content.controller.impl.duel.DuelController;
import com.fury.game.content.global.Achievements;
import com.fury.game.content.skill.member.construction.House;
import com.fury.game.entity.character.combat.effects.Effects;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.content.Sounds;
import com.fury.core.model.item.Item;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.Direction;
import com.fury.util.Misc;


public class TeleportHandler {

	public static boolean teleportPlayer(final Player player, final Position targetLocation) {
		return teleportPlayer(player, targetLocation, player.getSpellbook().getTeleportType());
	}
	public static boolean teleportPlayer(final Player player, final Position targetLocation, final TeleportType teleportType) {
		return teleportPlayer(player, targetLocation, teleportType, false);
	}

	public static boolean teleportPlayer(final Player player, final Position targetLocation, final TeleportType teleportType, boolean dungeoneering) {
		if(teleportType != TeleportType.LEVER) {
			if(!checkReqs(player, targetLocation)) {
				return false;
			}
		}
		if(!player.getControllerManager().processTeleport(teleportType, targetLocation))
			return false;
		if((!dungeoneering && !player.getTimers().getClickDelay().elapsed(2000)) || player.getMovement().isLocked() || player.getEmotesManager().isDoingEmote())
			return false;
		if(player.getDungManager().isInside() && !dungeoneering) {
			player.message("You cannot teleport out of dungeoneering.");
			player.getPacketSender().sendInterfaceRemoval();
			return false;
		}
		if(player.getControllerManager() != null)
			player.getControllerManager().magicTeleported(teleportType.ordinal());
		player.getMovement().setTeleporting(true);
		player.getMovement().lock();
		player.getMovement().reset();
		player.stopAll();
		if(teleportType.getStartAnimation() != null)
			player.perform(teleportType.getStartAnimation());
		if(teleportType.getStartGraphic() != null)
			player.perform(teleportType.getStartGraphic());
		if(teleportType == TeleportType.NORMAL)
			Sounds.sendSound(player, 202);
		GameWorld.schedule(teleportType.getStartTick(), () -> {
			switch(teleportType) {
				default:
					player.stopAll();
					if(teleportType.getEndAnimation() != null)
						player.perform(teleportType.getEndAnimation());
					if(teleportType.getEndGraphic() != null)
						player.perform(teleportType.getEndGraphic());
					player.moveTo(targetLocation);
					player.getDirection().setDirection(Direction.SOUTH);
					if (teleportType == TeleportType.TELE_TAB) {
						removeTab(player, targetLocation);
					}
					Sounds.sendSound(player, 201);
					player.getMovement().unlock();
					player.getMovement().reset();
					player.getMovement().setTeleporting(false);
					player.getTimers().getClickDelay().reset();
					Achievements.doProgress(player, Achievements.AchievementData.TELEPORT_10000_TIMES);
					onArrival(player, targetLocation);
					break;
			}
		});
		return true;
	}
	
	public static void onArrival(Player player, Position targetLocation) {
		checkControllers(player, targetLocation);
	}

	public static void checkControllers(Player player, Position position) {
		if (Daemonheim.isAtDaemonheim(position))
			player.getControllerManager().startController(new Daemonheim());
		else if (DuelController.isAtDuelArena(position))
			player.getControllerManager().startController(new DuelController());
		else if (Wilderness.isAtWild(position) && !(player.getControllerManager().getController() instanceof Wilderness))
			player.getControllerManager().startController(new Wilderness());
	}

	public static boolean interfaceOpen(Player player) {
		if(player.getInterfaceId() > 0 && player.getInterfaceId() != 50100) {
			player.message("Please close the interface you have open before opening another.");
			return true;
		}
		return false;
	}

	public static boolean checkReqs(Player player, Position targetLocation) {
		if(player.getHealth().getHitpoints() <= 0)
			return false;
		if(player.getEffects().hasActiveEffect(Effects.TELEPORT_BLOCK)) {
			player.message("A magical spell is blocking you from teleporting.");
			return false;
		}
		if(player.getSettings().isResting())
			player.getSettings().setRestingState(0);

		if(player.getMovement().isLocked() || player.isCrossingObstacle()) {
			player.message("You cannot teleport right now.");
			return false;
		}

		if(player.getEmotesManager().isDoingEmote()) {
			player.message("Please wait till you've finished performing your emote.");
			return false;
		}

		return true;
	}

	private static void removeTab(Player player, Position targetLocation) {
		for(TeleportTabs tab : TeleportTabs.values()) {
			if(targetLocation.getX() == tab.getDestination().getX() && targetLocation.getY() == tab.getDestination().getY()) {
				if(tab == TeleportTabs.HOME) {
					Achievements.finishAchievement(player, Achievements.AchievementData.USE_A_TELEPORT_TAB);
					player.getInventory().delete(new Item(tab.getItemId()));
					House.enterHouse(player, true);
					break;
				} else {
					Achievements.finishAchievement(player, Achievements.AchievementData.USE_A_TELEPORT_TAB);
					boolean remove = true;
					if (tab == TeleportTabs.BLOOD && Equipment.wearingMorytaniaLegs(player, 3))
						remove = Misc.random(100) < 50;

					if (remove)
						player.getInventory().delete(new Item(tab.getItemId()));
					break;
				}
			}
		}
	}

	public static void pullLever(Player player, Position target) {
		player.animate(2140);
		player.message("You pull the lever..", true);
		GameWorld.schedule(1, () -> TeleportHandler.teleportPlayer(player, target, TeleportType.LEVER));
	}
}
