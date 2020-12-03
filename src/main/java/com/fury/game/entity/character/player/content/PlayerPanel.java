package com.fury.game.entity.character.player.content;

import com.fury.game.content.global.dnd.eviltree.EvilTree;
import com.fury.game.content.global.dnd.star.ShootingStar;
import com.fury.game.content.global.minigames.impl.RecipeForDisaster;
import com.fury.game.content.global.quests.Quests;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.content.global.thievingguild.ThievingGuild;
import com.fury.game.node.entity.actor.figure.player.Points;
import com.fury.game.world.GameWorld;
import com.fury.util.FontUtils;
import com.fury.util.Colours;
import com.fury.util.Misc;

public class PlayerPanel {

	public static void refreshPanel(Player player) {
		/**
		 * General info
		 */
		player.getPacketSender().sendString(39159, "General Information", Colours.WHITE);


		player.getPacketSender().sendString(39162, "Crashed star: " + FontUtils.YELLOW + (ShootingStar.hasCrashed() ? ShootingStar.getLocation().getName() : "N/A") + FontUtils.COL_END, Colours.ORANGE_2);
		player.getPacketSender().sendString(39163, "Evil tree: " + FontUtils.YELLOW + (EvilTree.get().isDead() ? "N/A" : EvilTree.get().getLocation().getName()) + FontUtils.COL_END, Colours.ORANGE_2);
		player.getPacketSender().sendString(39164, "Thieving Guild Event: " +  (ThievingGuild.isEventActive() ? FontUtils.GREEN + "Active" : FontUtils.YELLOW + "Inactive") + FontUtils.COL_END, Colours.ORANGE_2);
		
		/**
		 * Account info
		 */
		player.getPacketSender().sendString(39166, "Account Information", Colours.WHITE);
		player.getPacketSender().sendString(39168, "Claimed:  " + FontUtils.YELLOW  + "$" + player.getPoints().get(Points.DONATED) + FontUtils.COL_END, Colours.ORANGE_2);
		player.getPacketSender().sendString(39169, "Rank:  " + FontUtils.YELLOW + Misc.formatText(player.getRights().toString().toLowerCase()) + FontUtils.COL_END, Colours.ORANGE_2);
		player.getPacketSender().sendString(39170, "Game Mode: " + FontUtils.YELLOW + Misc.formatText(player.getGameMode().toString().toLowerCase().replace("_", " ")) + FontUtils.COL_END, Colours.ORANGE_2);
        player.getPacketSender().sendString(39171, "", Colours.ORANGE_2);//Was email, empty now
        player.getPacketSender().sendString(39172, "Exp Lock:  " + FontUtils.YELLOW + (player.experienceLocked() ? "Locked" : "Unlocked") + FontUtils.COL_END, Colours.ORANGE_2);
		player.getPacketSender().sendString(39173, "Master: " + FontUtils.YELLOW + Misc.formatText(player.getSlayerManager().getCurrentMaster().toString().toLowerCase().replaceAll("_", " ")) + FontUtils.COL_END, Colours.ORANGE_2);
		if(player.getSlayerManager().getCurrentTask() == null)
			player.getPacketSender().sendString(39174, "Task:  None" + FontUtils.COL_END, Colours.ORANGE_2);
		else
			player.getPacketSender().sendString(39174, "Task:  " + FontUtils.YELLOW + Misc.formatText(player.getSlayerManager().getCurrentTask().toString().toLowerCase().replaceAll("_", " "))+"s", Colours.ORANGE_2);
		player.getPacketSender().sendString(39175, "Task Streak:  " + FontUtils.YELLOW + player.getSlayerManager().getCompletedTasks() + FontUtils.COL_END, Colours.ORANGE_2);
		player.getPacketSender().sendString(39176, "Task Amount:  " + FontUtils.YELLOW + player.getSlayerManager().getCount() + FontUtils.COL_END, Colours.ORANGE_2);
		if(player.getSlayerManager().getSocialPlayer() != null)
			player.getPacketSender().sendString(39177, "Duo Partner: " + FontUtils.YELLOW + player.getSlayerManager().getSocialPlayer().getUsername() + FontUtils.COL_END, Colours.ORANGE_2);
		else
			player.getPacketSender().sendString(39177, "Duo Partner: " + FontUtils.YELLOW + "-" + FontUtils.COL_END, Colours.ORANGE_2);
		player.getPacketSender().sendString(39178, "Open Kill Log", Colours.ORANGE_2);
		player.getPacketSender().sendString(39179, "Open Drop Log", Colours.ORANGE_2);

		/**
		 * Points
		 */
		player.getPacketSender().sendString(39181, "Statistics", Colours.WHITE);
		player.getPointsHandler().refreshPanel();

		/**
		 * Quests
		 */
		player.getPacketSender().sendString(39196, "Quests", Colours.WHITE);
		player.getPacketSender().sendString(39197, RecipeForDisaster.getQuestTabPrefix(player) + "Recipe For Disaster" + FontUtils.COL_END);
		player.getPacketSender().sendString(39198, player.getQuestManager().getTabString(Quests.FIRST_ADVENTURE));
	}

}
