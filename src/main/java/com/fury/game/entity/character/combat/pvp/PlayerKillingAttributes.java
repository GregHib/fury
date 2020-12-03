package com.fury.game.entity.character.combat.pvp;

import com.fury.game.content.global.Achievements;
import com.fury.game.content.global.Achievements.AchievementData;
import com.fury.game.content.global.Artifacts;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.content.LoyaltyProgramme;
import com.fury.game.entity.character.player.content.LoyaltyProgramme.LoyaltyTitles;
import com.fury.game.node.entity.actor.figure.player.Points;
import com.fury.util.Misc;

import java.util.ArrayList;
import java.util.List;

public class PlayerKillingAttributes {

	private final Player player;
	private Player target;
	private int playerKills;
	private int playerKillStreak;
	private int playerDeaths;
	private int targetPercentage;
	private long lastPercentageIncrease;
	private int safeTimer;

	private final int WAIT_LIMIT = 2;
	private List<String> killedPlayers = new ArrayList<>();

	public PlayerKillingAttributes(Player player) {
		this.player = player;
	}

	public void add(Player other) {

		if(other.getAppearance().getBountyHunterSkull() >= 0)
			other.getAppearance().setBountyHunterSkull(-1);

		boolean target = player.getPlayerKillingAttributes().getTarget() != null && player.getPlayerKillingAttributes().getTarget().getIndex() == other.getIndex() || other.getPlayerKillingAttributes().getTarget() != null && other.getPlayerKillingAttributes().getTarget().getIndex() == player.getIndex();
		if(target)
			killedPlayers.clear();

		if (killedPlayers.size() >= WAIT_LIMIT) {
			killedPlayers.clear();
			handleReward(other, target);
		} else {
			if (!killedPlayers.contains(other.getUsername()))
				handleReward(other, target);
			else
				player.message("You were not given points because you have recently defeated " + other.getUsername() + ".");
		}

		if(target)
			BountyHunter.resetTargets(player, other, true, "You have defeated your target!");
	}

	/**
	 * Gives the player a reward for defeating his opponent
	 * @param other
	 */
	private void handleReward(Player other, boolean targetKilled) {
		if (!other.getLogger().getHardwareId().equals(player.getLogger().getHardwareId()) && player.isInWilderness()) {
			if(!killedPlayers.contains(other.getUsername()))
				killedPlayers.add(other.getUsername());
			player.message(getRandomKillMessage(other.getUsername()));
			player.getPoints().add(Points.PK, 5);
			this.playerKills += 1;
			this.playerKillStreak +=1;
			player.message("You've received five Pk points.");
			Artifacts.handleDrops(player, other, targetKilled);
			if(player.getAppearance().getBountyHunterSkull() < 4)
				player.getAppearance().setBountyHunterSkull(player.getAppearance().getBountyHunterSkull() + 1);
			player.getPointsHandler().refreshPanel();
			
			/** ACHIEVEMENTS AND LOYALTY TITLES **/
			LoyaltyProgramme.unlock(player, LoyaltyTitles.KILLER);
			Achievements.finishAchievement(player, AchievementData.KILL_A_PLAYER_WILDERNESS);
			if(this.playerKills >= 15) {
				LoyaltyProgramme.unlock(player, LoyaltyTitles.SLAUGHTERER);
			}
			if(this.playerKills >= 50) {
				LoyaltyProgramme.unlock(player, LoyaltyTitles.GENOCIDAL);
			}
			/*if(this.playerKillStreak >= 5) {
				Achievements.finishAchievement(player, AchievementData.KILL_100_FROST_DRAGONS);
			}*/
			if(this.playerKillStreak >= 15) {
				LoyaltyProgramme.unlock(player, LoyaltyTitles.IMMORTAL);
			}
		}
	}

	public List<String> getKilledPlayers() {
		return killedPlayers;
	}

	public void setKilledPlayers(List<String> list) {
		killedPlayers = list;
	}

	/**
	 * Gets a random message after killing a player
	 * @param killedPlayer 		The player that was killed
	 */
	public static String getRandomKillMessage(String killedPlayer){
		int deathMsgs = Misc.getRandom(8);
		switch(deathMsgs) {
		case 0: return "With a crushing blow, you defeat "+killedPlayer+".";
		case 1: return "It's humiliating defeat for "+killedPlayer+".";
		case 2: return ""+killedPlayer+" didn't stand a chance against you.";
		case 3: return "You've defeated "+killedPlayer+".";
		case 4: return ""+killedPlayer+" regrets the day they met you in combat.";
		case 5: return "It's all over for "+killedPlayer+".";
		case 6: return ""+killedPlayer+" falls before you might.";
		case 7: return "Can anyone defeat you? Certainly not "+killedPlayer+".";
		case 8: return "You were clearly a better fighter than "+killedPlayer+".";
		}
		return null;
	}

	public int getPlayerKills() {
		return playerKills;
	}

	public void setPlayerKills(int playerKills) {
		this.playerKills = playerKills;
	}

	public int getPlayerKillStreak() {
		return playerKillStreak;
	}

	public void setPlayerKillStreak(int playerKillStreak) {
		this.playerKillStreak = playerKillStreak;
	}

	public int getPlayerDeaths() {
		return playerDeaths;
	}

	public void setPlayerDeaths(int playerDeaths) {
		this.playerDeaths = playerDeaths;
	}

	public Player getTarget() {
		return target;
	}

	public void setTarget(Player target) {
		this.target = target;
	}

	public int getTargetPercentage() {
		return targetPercentage;
	}

	public void setTargetPercentage(int targetPercentage) {
		this.targetPercentage = targetPercentage;
	}

	public long getLastTargetPercentageIncrease() {
		return lastPercentageIncrease;
	}

	public void setLastTargetPercentageIncrease(long lastPercentageIncrease) {
		this.lastPercentageIncrease = lastPercentageIncrease;
	}

	public int getSafeTimer() {
		return safeTimer;
	}

	public void setSafeTimer(int safeTimer) {
		this.safeTimer = safeTimer;
	}
}
