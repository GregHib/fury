package com.fury.game.content.skill.member.slayer;

import com.fury.game.content.dialogue.impl.skills.slayer.DuoSlayerD;
import com.fury.game.content.global.Achievements;
import com.fury.game.content.global.Achievements.AchievementData;
import com.fury.game.content.skill.Skill;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.content.PlayerPanel;
import com.fury.game.entity.character.player.link.transportation.TeleportHandler;
import com.fury.core.model.item.Item;
import com.fury.game.node.entity.actor.figure.player.Points;
import com.fury.util.Colours;
import com.fury.util.Misc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class SlayerManager {

	public static int BUY_INTERFACE = 36000, LEARN_INTERFACE = 36035, ASSIGN_INTERFACE = 36100;
	private Player player;
	private Player socialPlayer;
	private int canceledTasksCount;
	private int difficultyWildernessSlayer = -1;
	private SlayerTask[] canceledTasks;
	private SlayerTask currentTask;
	private SlayerMaster currentMaster;
	protected int completedTasks;
	protected int maximumTaskCount;
	protected int currentTaskCount;
	protected int completedWildernessTasks;
	private boolean[] learnt;
	private static int FLETCH_BROAD = 0, CRAFT_RINGS = 1, CRAFT_HELMS = 2, AQUANITES = 3, QUICK_BLOW = 4, ICE_STRYKE = 5;

	public SlayerManager(Player p) {
		this.player = p;
		learnt = new boolean[6];
		canceledTasks = new SlayerTask[6];
		setSlayerMaster(SlayerMaster.TURAEL);
	}

	private void addPoints() {
		double pointsIncreased = 3;
		if (getCurrentMaster().equals(SlayerMaster.DEATHWILD)) {
			pointsIncreased = difficultyWildernessSlayer == 0 ? 4 : 6;
			completedWildernessTasks++;
			final int PVP_BOX = 1;
			if (completedWildernessTasks % 10 == 0) {
				player.getInventory().add(new Item(PVP_BOX));
				player.message("You have been awarded a pvp box for your 10th task!");
			}
			int pvpBox = Misc.random(difficultyWildernessSlayer == 0 ? 600 : 450);
			if (pvpBox == 0) {
				player.getInventory().add(new Item(PVP_BOX));
				player.message("You have randomly been awarded a pvp box!");
			}
		}
		if (completedTasks == 50) {
			pointsIncreased += currentMaster.getPointsRange()[2];
			resetCompletedTasks();
		} else if (completedTasks == 10)
			pointsIncreased += currentMaster.getPointsRange()[1];
		else
			pointsIncreased += currentMaster.getPointsRange()[0];
		pointsIncreased *= (currentTaskCount / maximumTaskCount);
        player.getPoints().add(Points.SLAYER, (int) pointsIncreased);
		player.message("You received " + (int) pointsIncreased + " Slayer points for completing this task.");
		player.getPointsHandler().refreshPanel();
	}

	private boolean removePoints(int pointsValue) {
		if(!player.getPoints().has(Points.SLAYER, pointsValue)) {
			player.message("You don't have enough points to complete this transaction.");
			return false;
		}
		player.getPoints().remove(Points.SLAYER, pointsValue);
		return true;
	}

	private void cancelCurrentTask() {
		if (currentTask == null) {
			player.message("You don't have an active task to cancel.");
		} else {
			if (removePoints(10)) {
				skipCurrentTask(true);
				refresh();
				player.message("Your slayer task has been re-assigned as requested,");
				player.message("as a result, your slayer-streak has been reset to 0.");
			}
		}
	}

	private void addRemovedTask(int slot) {
		SlayerTask task = canceledTasks[slot];
		if (task == null)
			return;
		canceledTasks[slot] = null;
		sendSlayerInterface(ASSIGN_INTERFACE);
		player.message("You have re-added " + task.getName().toLowerCase() + " to the assignment list.");
	}

	private void removeCurrentTask() {
		if (canceledTasksCount != canceledTasks.length) {
			if (currentTask == null) {
				player.message("You don't have an active task to remove.");
				return;
			} else {
				if (removePoints(50)) {
					for (int index = 0; index < 6; index++) {
						SlayerTask task = canceledTasks[index];
						if (task == null) {
							canceledTasks[index] = currentTask;
							player.message("You have canceled the task " + currentTask.getName() + " permanently.");
							skipCurrentTask(true);
							sendSlayerInterface(ASSIGN_INTERFACE);
							return;
						}
					}
				}
			}
		} else
			player.message("You have reached the maximum limit of cancelable tasks, please remove one before continuing.");
	}

	public Object[] calculateTask(SlayerMaster master) {
		List<SlayerTask> tasks = new LinkedList<>(Arrays.asList(master.getTask()));
		for (SlayerTask task : canceledTasks) {
			if (tasks.contains(task))
				tasks.remove(task);
		}
		while (true) {
			SlayerTask task = tasks.get(Misc.random(tasks.size()));
			if (!hasRequirement(player, task) || (socialPlayer != null && !hasRequirement(socialPlayer, task)))
				continue;

			return new Object[] {task, (int) Misc.random(master.getTasksRange()[0] * task.getTaskFactor(), master.getTasksRange()[1] * task.getTaskFactor())};
		}
	}

	/**
	 * This method is only called for wilderness npc
	 */
	public Object[] calculateTask(SlayerMaster master, String difficulty) {
		List<SlayerTask> tasks = new LinkedList<>(Arrays.asList(master.getTask()));
		for (SlayerTask task : canceledTasks) {
			if (tasks.contains(task))
				tasks.remove(task);
		}
		List<SlayerTask> toRemove = new LinkedList<>(Arrays.asList(master.getTask()));
		for (SlayerTask task : tasks) {
			if (task.getWildernessDifficulty().equals("N/A")) {
				continue;
			}
			if (task.getWildernessDifficulty().equalsIgnoreCase("hard") && difficulty.equalsIgnoreCase("easy")) {
				continue;
			}
			toRemove.add(task);
		}
		tasks = toRemove;
		while (true) {
			SlayerTask task = tasks.get(Misc.random(tasks.size()));
			if (!hasRequirement(player, task) || (socialPlayer != null && !hasRequirement(socialPlayer, task)))
				continue;

			return new Object[] {task, (int) Misc.random(master.getTasksRange()[0] * task.getTaskFactor(), master.getTasksRange()[1] * task.getTaskFactor())};
		}
	}

	private static boolean hasRequirement(Player player, SlayerTask task) {
		/*if (task == SlayerTask.SHADOW_WARRIOR || task == SlayerTask.SKELETAL_WYVERN)
			return false;*/
		if((task == SlayerTask.BASILISK || task == SlayerTask.COCKATRICE) && !player.getSkills().hasMaxLevel(Skill.DEFENCE, 20))
			return false;
		if((task == SlayerTask.TUROTH || task == SlayerTask.KURASK) && !player.getSkills().hasMaxLevel(Skill.ATTACK, 50))
			return false;
		if ((task == SlayerTask.ICE_STRYKEWYRM /*&& !player.isCompletedFightCaves()*/ && !player.getSlayerManager().hasLearnedStrykes()) || (task == SlayerTask.AQUANITE && !player.getSlayerManager().hasLearnedAquanites()))
			return false;
		if (player.getSkills().getMaxLevel(Skill.SLAYER) < task.getLevelRequired())
			return false;
		return true;
	}

	public boolean isValidTask(String name) {
		if (currentTask == null)
			return false;
		List<SlayerTask> tasks = new LinkedList<>(Arrays.asList(currentTask.getAlternatives()));
		tasks.add(currentTask);
		for (SlayerTask task : tasks) {
			if (name.toLowerCase().replace("'", "").contains(task.toString().replace("_", " ").replace("'", "").toLowerCase()))
				return true;
		}
		return false;
	}

	public boolean isCurrentTask(String name) {
		if (currentTask == null)
			return false;
		List<SlayerTask> tasks = new LinkedList<>(Arrays.asList(currentTask.getAlternatives()));
		tasks.add(currentTask);
		for (SlayerTask task : tasks) {
            if (name.toLowerCase().replace("'", "").contains(task.toString().replace("_", " ").replace("'", "").toLowerCase()))
			    if (currentTask == task)
					return true;
		}
		return false;
	}

	public void checkCompletedTask(int damageAdmitted, int otherDamageAdmitted) {
		if (getCurrentMaster().equals(SlayerMaster.DEATHWILD) && !player.isInWilderness()) {
			player.message("This kill did not count as it is not in the wilderness.");
			return;
		}
		currentTaskCount++;
		int otherSocialCount = 0;
		if (socialPlayer != null) {
			if (socialPlayer.isWithinDistance(player, 16))
				socialPlayer.getSkills().addExperience(Skill.SLAYER, otherDamageAdmitted / 5);
			otherSocialCount = socialPlayer.getSlayerManager().getCurrentTaskCount();
		}
		player.getSkills().addExperience(Skill.SLAYER, damageAdmitted / 5);
		if (currentTaskCount + otherSocialCount == maximumTaskCount) {
			if (socialPlayer != null)
				socialPlayer.message("You have finished your slayer task, talk to a slayer master for a new one.");
			player.message("You have finished your slayer task, talk to a slayer master for a new one.");
			Achievements.finishAchievement(player, AchievementData.COMPLETE_A_SLAYER_TASK);
			Achievements.doProgress(player, AchievementData.COMPLETE_35_SLAYER_TASKS);
			resetTask(true, true);
			return;
		} else if (currentTaskCount % 10 == 0)
			checkKillsLeft();
		PlayerPanel.refreshPanel(player);
	}

	public void checkKillsLeft() {
		if (currentTask == null) {
			player.message("You currently have no slayer task assigned.");
			return;
		}
		player.message("Your current assignment is: " + currentTask.getName() + "; only " + getCount() + " more to go.");
		if (socialPlayer != null) {
			player.message("Your partner's current assignment is: " + currentTask.getName() + "; only " + player.getSlayerManager().getCount() + " more to go.");
			int combinedTasksCount = currentTaskCount + socialPlayer.getSlayerManager().getCurrentTaskCount();
			player.message("In total you both have killed " + combinedTasksCount + " out of " + maximumTaskCount + " of the task, only " + (maximumTaskCount - combinedTasksCount) + " more to go.");
		}
	}

	public int getCount() {
		return maximumTaskCount - currentTaskCount;
	}

	/**
	 * Social slayer
	 */

	public void invitePlayer(Player otherPlayer) {
		if (currentTask != null) {
			player.message("You need to complete your current task before starting a social slayer group.");
			return;
		} else if (otherPlayer == null || !otherPlayer.isWithinDistance(player, 7) || player.getFinished() || otherPlayer.getFinished()) {
			player.message("That player is no-where to be found.");
			return;
		} else if (otherPlayer.getSlayerManager().getCurrentTask() != null) {
			player.message("That player needs to complete their current task before joining a social slayer group.");
			return;
		} else if (otherPlayer.getSlayerManager().getSocialPlayer() != null) {
			player.message("Your target is already in a social slayer group.");
			return;
		} else if (socialPlayer != null) {
			player.message("You are already in a social slayer group, leave it in order to start a new one.");
			return;
		}

		player.getTemporaryAttributes().put("social_request", otherPlayer);
		otherPlayer.getTemporaryAttributes().put("social_request", player);
		player.message("Sending " + otherPlayer.getUsername() + " an invitation...");
		openSocialInvitation(otherPlayer);
	}

	private void openSocialInvitation(final Player otherPlayer) {
		otherPlayer.getDialogueManager().startDialogue(new DuoSlayerD(), "You have received an invitation to join " + player.getUsername() + "'s social slayer group.", player);
		otherPlayer.setCloseInterfacesEvent(() -> {
			player.message("Your invitation has been declined.");
			otherPlayer.message("You have declined the invitation.");
			player.getTemporaryAttributes().remove("social_request");
			otherPlayer.getTemporaryAttributes().remove("social_request");
		});
	}

	public void createSocialGroup(boolean initial) {
		Player socialPlayer = (Player) player.getTemporaryAttributes().remove("social_request");
		if (socialPlayer == null)
			return;
		if (initial) {
			socialPlayer.getSlayerManager().createSocialGroup(false);
			player.message("You have created a social group.");
		} else {
			player.message("You have just joined " + socialPlayer.getUsername() + "'s social group.");
		}
		this.socialPlayer = socialPlayer;
		PlayerPanel.refreshPanel(player);
		player.message("Leaving or logging out will require you to get another task.", Colours.RED);
	}

	public void resetSocialGroup(boolean initial) {
		if (socialPlayer != null) {
			if (initial) {
				socialPlayer.getSlayerManager().resetSocialGroup(false);
				player.message("You have left the social slayer group.", true);
			} else
				player.message("Your social slayer member has left your group.", true);
			socialPlayer = null;
		}
	}

	public void skipCurrentTask(boolean resetCompletedTasks) {
		resetTask(false, true);
		if (resetCompletedTasks)
			resetCompletedTasks();
	}

	private void resetTask(boolean completed, boolean initial) {
		if (completed) {
            completedTasks++;
			addPoints();
		}
		if (initial) {
			if (socialPlayer != null) {
				socialPlayer.getSlayerManager().resetTask(completed, false);
				if (!completed)
					resetSocialGroup(true);
			}
		}
		setCurrentTask(null, 0);
	}

	public boolean hasLearnedBroad() {
		return learnt[FLETCH_BROAD];
	}

	public boolean hasLearnedQuickBlows() {
		return learnt[QUICK_BLOW];
	}

	public boolean hasLearnedRing() {
		return learnt[CRAFT_RINGS];
	}

	public boolean hasLearnedSlayerHelmet() {
		return learnt[CRAFT_HELMS];
	}

	public boolean hasLearnedStrykes() {
		return learnt[ICE_STRYKE];
	}

	public boolean hasLearnedAquanites() {
		return learnt[AQUANITES];
	}

	private void resetCompletedTasks() {
		this.completedTasks = 0;
	}

	public void setCurrentTask(SlayerTask task) {
		this.currentTask = task;
	}

	public void setCurrentTask(boolean initial, SlayerMaster master) {
		Object[] futureTask = calculateTask(master);
		setCurrentTask((SlayerTask) futureTask[0], (int) futureTask[1]);
		checkKillsLeft();
		if (master != null)
			setCurrentMaster(master);
		if (initial) {
			if (socialPlayer != null)
				socialPlayer.getSlayerManager().setCurrentTask((SlayerTask) futureTask[0], (int) futureTask[1]);
		}

		refresh();
	}

	public void setCurrentTask(boolean initial, SlayerMaster master, String difficulty) {
		Object[] futureTask = calculateTask(master, difficulty);
		setCurrentTask((SlayerTask) futureTask[0], (int) futureTask[1]);
		checkKillsLeft();
		if (master != null)
			setCurrentMaster(master);
		if (initial) {
			if (socialPlayer != null)
				socialPlayer.getSlayerManager().setCurrentTask((SlayerTask) futureTask[0], (int) futureTask[1]);
		}

		refresh();
	}

	public void refresh() {
		PlayerPanel.refreshPanel(player);
		updateShopPoints(player);
	}

	public void setCurrentTask(SlayerTask task, int maximumTaskCount) {
		if (task == null)
			this.currentTaskCount = 0;
		this.currentTask = task;
		this.maximumTaskCount = maximumTaskCount;
	}

	public SlayerTask getCurrentTask() {
		return currentTask;
	}

	public int getCurrentTaskCount() {
		return currentTaskCount;
	}

	public void setSlayerMaster(SlayerMaster currentMaster) {
		this.currentMaster = currentMaster;
	}

	public void setCurrentMaster(SlayerMaster currentMaster) {
//		if(currentMaster != this.currentMaster)
//			resetCompletedTasks();
		setSlayerMaster(currentMaster);
	}

	public SlayerMaster getCurrentMaster() {
		return currentMaster;
	}

	public Player getSocialPlayer() {
		return socialPlayer;
	}

	/**
	 * Old stuff
	 */

	public void killedNpc(Mob mob) {
		//Rockslug
		if (mob.getId() == 1632 && player.getSlayerManager().getLearnt()[QUICK_BLOW] && player.getInventory().contains(new Item(4161))) {
			player.getInventory().delete(new Item(4161));
		}
		//Desert Lizard
		if ((mob.getId() == 2806 || mob.getId() == 2805 || mob.getId() == 2804) && player.getSlayerManager().getLearnt()[QUICK_BLOW] && player.getInventory().contains(new Item(6696))) {
			player.getInventory().delete(new Item(6696));
		}
	}

	public boolean[] getLearnt() {
		return learnt;
	}

	public void setLearnt(int i, boolean l) {
		learnt[i] = l;
	}

	public void setLearnt(boolean[] l) {
		learnt = l;
	}

	public boolean doubleSlayerXP = false;

	public static void updateShopPoints(Player player) {
		//Points
		player.getPacketSender().sendString(36018, "Current points = " + player.getPoints().getInt(Points.SLAYER));
		player.getPacketSender().sendString(36053, "Current points = " + player.getPoints().getInt(Points.SLAYER));
		player.getPacketSender().sendString(36118, "Current points = " + player.getPoints().getInt(Points.SLAYER));

		//Buy Tab
		for (int i = 0; i < SlayerRewards.Buy.values().length; i++) {
			SlayerRewards.Buy reward = SlayerRewards.Buy.forId(i);
			if (reward == null)
				continue;
			if (player.getPoints().getInt(Points.SLAYER) < reward.getCost()) {
				player.getPacketSender().sendString(reward.getButtonId() + 5, reward.getCost() + " points", Colours.RED);
			} else {
				player.getPacketSender().sendString(reward.getButtonId() + 5, reward.getCost() + " points");
			}
		}

		//Learn Tab
		boolean[] learnt = player.getSlayerManager().getLearnt();
		for (int i = 0; i < learnt.length; i++) {
			SlayerRewards.Learn reward = SlayerRewards.Learn.forId(i);
			if (reward == null)
				continue;

			if (learnt[i]) {
				player.getPacketSender().sendString(reward.getButtonId() + 4, "Already unlocked.");
				player.getPacketSender().sendString(reward.getButtonId() + 5, "");
				player.getPacketSender().sendString(reward.getButtonId() + 6, "");
			} else {
				player.getPacketSender().sendString(reward.getButtonId() + 4, reward.getLineOne());
				player.getPacketSender().sendString(reward.getButtonId() + 5, reward.getLineTwo());
				if (player.getPoints().getInt(Points.SLAYER) < reward.getCost()) {
					player.getPacketSender().sendString(reward.getButtonId() + 6, reward.getCost() + " points", Colours.RED);
				} else {
					player.getPacketSender().sendString(reward.getButtonId() + 6, reward.getCost() + " points");
				}
			}
		}

//        Assignment Tab
		String name = "nothing";
		if(player.getSlayerManager().getCurrentTask() != null)
			name = player.getSlayerManager().getCurrentTask().name().toLowerCase().replaceAll("_", " ");

		player.getPacketSender().sendString(36119, "Cancel task of " + name + ".");
		player.getPacketSender().sendString(36120, "Never assign " + name + " again.");
		player.getSlayerManager().canceledTasksCount = 0;
		for (int i = 0; i < 5; i++) {
			if (player.getSlayerManager().getCanceled()[i] == null) {
				player.getPacketSender().sendString(36127 + (i * 4), "nothing");
			} else {
				player.getSlayerManager().canceledTasksCount++;
				player.getPacketSender().sendString(36127 + (i * 4), player.getSlayerManager().getCanceled()[i].name().toLowerCase().replaceAll("_", " "));
			}

		}

	}

	public static boolean handleRewardsInterface(Player player, int button) {
		if (player.getInterfaceId() == 36000 || player.getInterfaceId() == 36035 || player.getInterfaceId() == 36100) {
			for (SlayerRewards.Buy reward : SlayerRewards.Buy.values()) {
				if (reward.getButtonId() == button) {

					if (player.getPoints().getInt(Points.SLAYER) < reward.getCost()) {
						player.message("You need " + reward.getCost() + " Slayer points in order to buy this.");
						return true;
					}
					if (reward.getItems() != null) {
						if (player.getInventory().getSpaces() < reward.getItems().length) {
							player.message("You need at least " + reward.getItems().length + " free inventory slot" + (reward.getItems().length > 1 ? "s" : "") + " to buy this.");
							return true;
						}
					}
					player.getPointsHandler().refreshPanel();
                    player.getPoints().remove(Points.SLAYER, reward.getCost());
					updateShopPoints(player);
					if (reward.getItems() != null) {
						for (Item item : reward.getItems()) {
							player.getInventory().add(item);
						}
					}
					if (reward.getSkill() != null && reward.getExp() != -1) {
						player.getSkills().addExperience(reward.getSkill(), reward.getExp());
					}

					if (reward.getSkill() != null && reward.getExp() != -1) {
						player.message("You've bought " + reward.getExp() + " Slayer XP for " + reward.getCost() + " Slayer points.");
					}

					if (reward.getItems() != null) {
						player.message("You've bought " + reward.getName() + " for " + reward.getCost() + " Slayer points.");
					}

				}
			}
			for (SlayerRewards.Learn reward : SlayerRewards.Learn.values()) {
				if (reward.getButtonId() == button) {
					if (player.getSlayerManager().getLearnt()[reward.ordinal()]) {
						player.message("You have already unlocked this.");
						return true;
					}
					if (player.getPoints().getInt(Points.SLAYER) < reward.getCost()) {
						player.message("You need " + reward.getCost() + " Slayer points in order to buy this.");
						return true;
					}
					player.getPointsHandler().refreshPanel();
                    player.getPoints().remove(Points.SLAYER, reward.getCost());
					player.getSlayerManager().setLearnt(reward.ordinal(), true);
					updateShopPoints(player);
					if (reward == SlayerRewards.Learn.CRAFT_HELMETS)
						player.message("The secret is yours. You can now combine a black mask, face mask, spiny helm, nosepeg and earmuffs into one useful item.");
					else
						player.message("You've unlock the ability in exchange for " + reward.getCost() + " Slayer points.");
				}
			}
			switch (button) {
				case 36128:
					player.getSlayerManager().addRemovedTask(0);
					break;
				case 36132:
					player.getSlayerManager().addRemovedTask(1);
					break;
				case 36136:
					player.getSlayerManager().addRemovedTask(2);
					break;
				case 36140:
					player.getSlayerManager().addRemovedTask(3);
					break;
				case 36144:
					player.getSlayerManager().addRemovedTask(4);
					break;
				case 36119:
					player.getSlayerManager().cancelCurrentTask();
					break;
				case 36120:
					player.getSlayerManager().removeCurrentTask();
					break;
				case 36105://Buy
				case 36040:
					player.getPacketSender().sendInterface(BUY_INTERFACE);
					break;
				case 36008://Learn
				case 36108:
					player.getPacketSender().sendInterface(LEARN_INTERFACE);
					break;
				case 36011://Assignment
				case 36046:
					player.getPacketSender().sendInterface(ASSIGN_INTERFACE);
					break;
			}
			return true;
		}
		return false;
	}

	public void sendSlayerInterface(int interfaceId) {
		SlayerManager.updateShopPoints(player);
		player.getPacketSender().sendInterface(interfaceId);
	}

	public SlayerTask[] getCanceled() {
		return canceledTasks;
	}

	public int getMaximumTaskCount() {
		return maximumTaskCount;
	}

	public String[] getCanceledNames() {
		List<String> names = new ArrayList<>();
		for (int i = 0; i < canceledTasks.length; i++) {
			SlayerTask canceledTask = canceledTasks[i];
			names.add(canceledTask == null ? null : canceledTask.name());
		}
		return names.toArray(new String[names.size()]);
	}

	public void seMaximumTaskCount(int value) {
		this.maximumTaskCount = value;
	}

	public void setCurrentTaskCount(int taskCount) {
		this.currentTaskCount = taskCount;
	}

	public void setCanceled(SlayerTask[] tasks) {
		this.canceledTasks = tasks;
	}

	public void slayerRingTeleport(Item item) {

		if(getCurrentTask() == null) {
			player.message("You don't currently have a task to teleport to.");
			return;
		}

		if(getCurrentTask().getLocation() == null) {
			player.message("This task does not have a direct teleport.");
			return;
		}

		if(player.getEquipment().contains(item)) {
			player.getEquipment().delete(item);
			if(item.getId() == 13288)
				player.message("Your ring crumbles to dust...");
			else
				player.getEquipment().add(new Item(item.getId() + 1, item.getAmount(), item.getRevision()));
		} else if(player.getInventory().contains(item)) {
			player.getInventory().delete(item);
			if(item.getId() == 13288)
				player.message("Your ring crumbles to dust...");
			else
				player.getInventory().add(new Item(item.getId() + 1, item.getAmount(), item.getRevision()));
		}

		TeleportHandler.teleportPlayer(player, getCurrentTask().getLocation(), player.getSpellbook().getTeleportType());
	}

	public void setCompletedTasks(int tasksCompleted) {
		this.completedTasks = tasksCompleted;
	}

	public int getCompletedTasks() {
		return completedTasks;
	}

	public int getDifficultyWildernessSlayer() {
		return difficultyWildernessSlayer;
	}

	public void setDifficultyWildernessSlayer(int difficultyWildernessSlayer) {
		this.difficultyWildernessSlayer = difficultyWildernessSlayer;
	}

	public int getCompletedWildernessTasks() {
		return completedWildernessTasks;
	}

	public void setCompletedWildernessTasks(int completedWildernessTasks) {
		this.completedWildernessTasks = completedWildernessTasks;
	}
}