package com.fury.game.content.skill.free.dungeoneering;

import com.fury.cache.def.item.ItemDefinition;
import com.fury.game.GameSettings;
import com.fury.game.content.controller.impl.Daemonheim;
import com.fury.game.content.dialogue.impl.skills.dungeoneering.DungeonDifficultyD;
import com.fury.game.content.dialogue.impl.skills.dungeoneering.DungeonLeaveParty;
import com.fury.game.content.dialogue.impl.skills.dungeoneering.PrestigeReset;
import com.fury.game.content.dialogue.input.impl.EnterComplexity;
import com.fury.game.content.dialogue.input.impl.EnterFloorNumber;
import com.fury.game.content.dialogue.input.impl.EnterPlayerToInvite;
import com.fury.game.content.global.Achievements;
import com.fury.game.content.skill.Skill;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.link.transportation.TeleportHandler;
import com.fury.game.entity.character.player.link.transportation.TeleportType;
import com.fury.core.model.item.Item;
import com.fury.game.entity.object.GameObject;
import com.fury.game.world.GameWorld;
import com.fury.game.world.World;
import com.fury.game.world.map.Position;
import com.fury.util.Misc;

import java.io.Serializable;

public class DungManager implements Serializable {

	// dungeonering
	private boolean[] currentProgress;
	private int previousProgress;
	private int tokens;
	private int maxFloor;
	private int maxComplexity;
	private BindedItems bindedItems;
	private Item bindedAmmo;
	private boolean[] visitedResources;
	private boolean[] unlockedScrolls;
	private String rejoinKey;

	private Player player;
	private DungeonPartyManager party;
	private Player invitingPlayer;

	public enum UnlockScrolls {
		SCROLL_OF_LIFE(18336, "The secret is yours! You read the scroll and unlock the long lost technique of regaining seeds from dead farming patches."),
		SCROLL_OF_CLEANSING(19890, "The secret is yours! You unlock the ability to save ingredients when making potions."),
		SCROLL_OF_EFFICIENCY(19670, "The secret is yours! You unlock the ability to save bars when smithing."),
		SCROLL_OF_RENEWAL(18343, "The secret is yours! You read the scroll and unlock the ability to use Prayer Renewal."),
		SCROLL_OF_RIGOUR(18839, "The secret is yours! You read the scroll and unlock the ability to use Rigour."),
		SCROLL_OF_AUGURY(18344, "The secret is yours! You read the scroll and unlock the ability to use Augury.")
		;

		public int getItemId() {
			return itemId;
		}

		private int itemId;
		private String message;

		UnlockScrolls(int itemId, String message) {
			this.itemId = itemId;
			this.message = message;
		}
		public static UnlockScrolls forId(int itemId) {
			for(UnlockScrolls scroll : UnlockScrolls.values()) {
				if (itemId == scroll.itemId) {
					return scroll;
				}
			}
			return null;
		}
	}

	public boolean handleScrollUnlocks(Player player, Item item) {
		for(UnlockScrolls scroll : UnlockScrolls.values()) {
			if(item.getId() == scroll.itemId) {
				if(player.getInventory().contains(new Item(scroll.itemId))) {//Not necessary
					if(unlockedScrolls[scroll.ordinal()]) {
						player.message("You have already unlocked this scrolls ability.");
					} else {
						player.getInventory().delete(new Item(item, 1));
						unlockedScrolls[scroll.ordinal()] = true;
						player.message(scroll.message);
					}
					return true;
				}
			}
		}
		return false;
	}

	private enum ResourceDungeon {
		EDGEVILLE_DUNGEON(10, 1100, 52849, new Position(3132, 9933), 52867, new Position(991, 4585)),
		DWARVEN_MINE(15, 1500, 52855, new Position(3034, 9772), 52864, new Position(1041, 4575)),
		EDGEVILLE_DUNGEON_2(20, 1600, 52853, new Position(3104, 9826), 52868, new Position(1135, 4589)),
		KARANJA_VOLCANO(25, 2100, 52850, new Position(2845, 9557), 52869, new Position(1186, 4598)),
		DAEMONHEIM_PENINSULA(30, 2400, 52861, new Position(3513, 3666), 52862, new Position(3498, 3633)),
		BAXTORIAN_FALLS(35, 3000, 52857, new Position(2578, 9898), 52873, new Position(1256, 4592)),
		MINING_GUILD(45, 4400, 52856, new Position(3022, 9741), 52866, new Position(1052, 4521)),
		TAVERLEY_DUNGEON_1(55, 6200, 52851, new Position(2854, 9841), 52870, new Position(1394, 4588)),
		TAVERLEY_DUNGEON_2(60, 7000, 52852, new Position(2912, 9810), 52865, new Position(1000, 4522)),
		VARRICK_SEWERS(65, 8500, 52863, new Position(3164, 9878), 52876, new Position(1312, 4590)),
		CHAOS_TUNNELS(70, 9600, 52858, new Position(3160, 5521), 52874, new Position(1238, 4524)),
		AL_KHARID(75, 11400, 52860, new Position(3298, 3307), 52872, new Position(1182, 4515)),
		BRIMHAVEM_DUNGEON(80, 12800, 77579, new Position(2697, 9442), 77580, new Position(1140, 4499)),
		POLYPORE_DUNGEON(82, 13500, 64291, new Position(4661, 5491, 3), 64291, new Position(4695, 5625, 3)),
		ASGARNIAN_ICE_DUNGEON(85, 15000, 52859, new Position(3033, 9599), 52875, new Position(1297, 4510))
		;

		ResourceDungeon(int level, int xp) {
			outsideId = insideId = -1;
		}

		private int level, outsideId, insideId;
		private double xp;
		private Position inside, outside;

		ResourceDungeon(int level, double xp, int outsideId, Position outside, int insideId, Position inside) {
			this.level = level;
			this.xp = xp;
			this.outsideId = outsideId;
			this.outside = outside;
			this.insideId = insideId;
			this.inside = inside;
		}
	}

	public void setCurrentProgress(boolean[] currentProgress) {
		this.currentProgress = currentProgress;
	}

	public void setPreviousProgress(int previousProgress) {
		this.previousProgress = previousProgress;
	}

	public void setTokens(int tokens) {
		this.tokens = tokens;
	}

	public void setBindedAmmo(Item bindedAmmo) {
		this.bindedAmmo = bindedAmmo;
	}

	public boolean[] getVisitedResources() {
		return visitedResources;
	}

	public void setVisitedResources(boolean[] visitedResources) {
		this.visitedResources = visitedResources;
	}

	public boolean[] getUnlockedScrolls() {
		return unlockedScrolls;
	}

	public void setUnlockedScrolls(boolean[] unlockedScrolls) {
		this.unlockedScrolls = unlockedScrolls;
	}

	public DungManager() {
		reset();
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public boolean enterResourceDungeon(GameObject object) {
		int i = 0;
		for (ResourceDungeon dung : ResourceDungeon.values()) {
			if (object.getId() == dung.outsideId || object.getId() == dung.insideId) {
				if (player.getSkills().getLevel(Skill.DUNGEONEERING) < dung.level) {
					player.message("You need a dungeoneering level of " + dung.level + " to enter this dungeoneering resource.");
					return true;
				}
				TeleportHandler.teleportPlayer(player, object.getId() == dung.insideId ? dung.outside : dung.inside, TeleportType.DUNGEONEERING_TELE);
				if (!visitedResources[i]) {
					visitedResources[i] = true;
					player.getSkills().addExperience(Skill.DUNGEONEERING, dung.xp, true);
				}
				return true;
			}
			i++;
		}
		return false;
	}

	public void bind(Item item, int slot) {
		ItemDefinition defs = item.getDefinition();
		int bindId = DungeonUtils.getBindedId(item);
		if (bindId == -1)
			return;
		if (DungeonUtils.isBindAmmo(item)) {
			if (bindedAmmo != null && bindedAmmo.getId() != -1 && (!defs.isStackable() || bindedAmmo.getId() != bindId)) {
				player.message("A currently bound item must be destroyed before another item may be bound.");
				return;
			}
			if(bindedAmmo != null)
				player.getInventory().delete(bindedAmmo);
			int total = (bindedAmmo == null) ?  item.getAmount() : bindedAmmo.getAmount() + item.getAmount();
			bindedAmmo = new Item(bindId, total > 255 ? 255 : total);
			int itemId = item.getId();
			player.getInventory().delete(slot);
			player.getInventory().add(bindedAmmo);
			player.getInventory().add(new Item(itemId, total - 255));
		} else {
			if (bindedItems.getSpaces() <= 5 - DungeonUtils.getMaxBindItems(player.getSkills().getLevel(Skill.DUNGEONEERING))) {
				player.message("A currently bound item must be destroyed before another item may be bound.");
				return;
			}
			item.setId(bindId);
			player.getInventory().refresh();
			bindedItems.add(item);
		}
		Achievements.finishAchievement(player, Achievements.AchievementData.BIND_AN_ITEM);
		player.message("You bind the " + defs.getName() + " to you.");//.sendMessage("Check in the smuggler to manage your bound items.");
	}

	public void unbind(Item item) {
		if (bindedAmmo != null && bindedAmmo.getId() == item.getId())
			bindedAmmo = null;
		else
			bindedItems.delete(item);
	}

	public Item getBindedAmmo() {
		return bindedAmmo;
	}

	public boolean isInside() {
		return party != null && party.getDungeon() != null;
	}

	public BindedItems getBindedItems() {
		return bindedItems;
	}

	public void reset() {
		currentProgress = new boolean[DungeonConstants.FLOORS_COUNT];
		previousProgress = 0;
		bindedItems = new BindedItems(player, 5);
		bindedAmmo = null;
		maxFloor = maxComplexity = 1;
		visitedResources = new boolean[ResourceDungeon.values().length];
		unlockedScrolls = new boolean[UnlockScrolls.values().length];
	}

	public boolean isTickedOff(int floor) {
		return currentProgress[floor - 1];
	}

	public boolean[] getCurrentProgressData() {
		return currentProgress;
	}

	public int getCurrentProgress() {
		int count = 0;
		for (boolean b : currentProgress)
			if (b)
				count++;
		return count;
	}

	public int getPreviousProgress() {
		return previousProgress;
	}

	public int getPrestige() {
		int currentProgress = getCurrentProgress();
		return currentProgress > previousProgress ? currentProgress : previousProgress;
	}

	public void tickOff(int floor) {
		currentProgress[floor - 1] = true;
		refreshCurrentProgress();
	}

	public void resetProgress() {
		previousProgress = getCurrentProgress();
		currentProgress = new boolean[DungeonConstants.FLOORS_COUNT];
		refreshCurrentProgress();
		refreshPreviousProgress();
	}

	public void addTokens(int tokens) {
		this.tokens += tokens;
	}

	public int getTokens() {
		return tokens;
	}

	public String getRejoinKey() {
		return rejoinKey;
	}

	public void setRejoinKey(String rejoinKey) {
		this.rejoinKey = rejoinKey;
	}

	public int getMaxFloor() {
		return maxFloor;
	}

	public void setMaxFloor(int maxFloor) {
		this.maxFloor = maxFloor;
	}

	public void increaseMaxFloor() {
		if (maxFloor == 60)
			return;
		maxFloor++;
	}

	public void increaseMaxComplexity() {
		maxComplexity++;
	}

	public int getMaxComplexity() {
		return maxComplexity;
	}

	public void setMaxComplexity(int maxComplexity) {
		this.maxComplexity = maxComplexity;
	}

	public void openPartyInterface() {
		player.getPacketSender().sendDungeoneeringTabIcon(true).sendTab(GameSettings.QUESTS_TAB);
		refreshFloor();
		refreshCurrentProgress();
		refreshPreviousProgress();
		refreshComplexity();
		refreshPartyDetailsComponents();
		refreshPartyGuideModeComponent();
		refreshNames();
	}

	public void refreshPartyGuideModeComponent() {
		/*if (!player.getInterfaceManager().containsInterface(939))
			return;
		player.getPacketSender().sendHideIComponent(939, 66, party == null || !party.getGuideMode());*/
	}

	/*
	 * called aswell when player added/removed to party
	 */
	public void refreshPartyDetailsComponents() {
		player.getPacketSender().sendTabInterface(GameSettings.QUESTS_TAB, party == null ? DungeonConstants.FORM_PARTY_INTERFACE : DungeonConstants.PARTY_INTERFACE);
		player.getPacketSender().sendDungeoneeringTabIcon(true);
		/*if (!player.getInterfaceManager().containsInterface(939))
			return;
		player.getPacketSender().sendHideIComponent(939, 18, party != null);//Form party button
		player.getPacketSender().sendHideIComponent(939, 14, party == null || !party.isLeader(player));//Leave Group
		player.getPacketSender().sendHideIComponent(939, 20, party == null || !party.isLeader(player));//Invite Button
		player.getPacketSender().sendHideIComponent(939, 77, party != null && party.isLeader(player));//Complexity change
		player.getPacketSender().sendHideIComponent(939, 82, party != null && party.isLeader(player));//Floor change
		player.getPacketSender().sendHideIComponent(939, 68, party != null && party.isLeader(player));//Guide mode
		
		*//*
		player.getPacketSender().sendHideIComponent(939, 34, party == null || party.isLeader(player));
		for (int i = 0; i < 5; i++) {
			Player p2 = party == null || i >= party.getTeam().size() ? null : party.getTeam().forId(i);
			for (int i2 = 59 + i * 3; i2 < 62 + i * 3; i2++)
				player.getPacketSender().sendHideIComponent(939, i2, p2 == null);
		}*//*
		
		for (int i = 0; i < 5; i++) {
			Player p2 = party == null || i >= party.getTeam().size() ? null : party.getTeam().forId(i);
			for (int i2 = 35 + i * 3; i2 < 38 + i * 3; i2++)
				player.getPacketSender().sendHideIComponent(939, i2, p2 == null);
		}
		
		for (int component = 29; component < 35; component++)//Lines on the interface
			player.getPacketSender().sendHideIComponent(939, component, false);*/
	}

	public void pressOption(int playerIndex, int option) {
		player.stopAll();
		if (party == null || playerIndex >= party.getTeam().size())
			return;
		Player player = party.getTeam().get(playerIndex);
		if (player == null)
			return;
		DungeonManager dungeon = party.getDungeon();
		if (option == 0) {
			if (dungeon == null) {
				this.player.message("You must be in a dungeon to do that.");
				return;
			}
			if (player == this.player) {
				this.player.message("Why don't you just use your inventory and stat interfaces?");
				return;
			}
		} else if (option == 1) {
			if (player == this.player) {
				this.player.message("You can't kick yourself!");
				return;
			}
			if (!party.isLeader(this.player)) {
				this.player.message("Only your party's leader can kick a party member!");
				return;
			}
			if (player.getMovement().isLocked() || dungeon != null && dungeon.isBossOpen()) {
				this.player.message("You can't kick this player right now.");
				return;
			}
			leaveParty();
		} else if (option == 2) {
			if (party.isLeader(player)) {
				this.player.message("You can't promote the party leader.");
				return;
			}
			if (!party.isLeader(this.player)) {
				this.player.message("Only your party's leader can promote a leader!");
				return;
			}
			party.setLeader(player);
			for (Player p2 : party.getTeam())
				party.refreshPartyDetails(p2);
		} else if (option == 3) {
			if (player != this.player) {
				this.player.message("You can't switch another player shared-xp.");
				return;
			}
			player.message("Shared xp is currently disabled.");
		}
	}

	public void invite() {
		if (party == null || !party.isLeader(player))
			return;
		player.stopAll();
		if (party.getDungeon() != null) {
			player.message("You can't do that right now.");
			return;
		}
		GameWorld.schedule(1, () -> {
			player.setInputHandling(new EnterPlayerToInvite());
			player.getPacketSender().sendEnterInputPrompt("Enter name of player to invite:");
				/*player.getPacketSender().sendInputNameScript("Enter name:");
				player.getTemporaryAttributes().put(Key.DUNGEON_INVITE, Boolean.TRUE);*/
		});
	}

	public void acceptInvite(Player invitedBy) {
		if (invitedBy == null)
			return;
		DungeonPartyManager party = invitedBy.getDungManager().getParty();
		if (invitedBy.getDungManager().invitingPlayer != player || party == null || !party.isLeader(invitedBy)) {
			player.getPacketSender().sendInterfaceRemoval();
			player.message("You can't do that right now.");
			return;
		}
		if (party.getTeam().size() >= 5) {
			player.getPacketSender().sendInterfaceRemoval();
			player.message("The party is full.");
			return;
		}
		if (party.getComplexity() > maxComplexity) {
			player.getPacketSender().sendInterfaceRemoval();
			player.message("You can't do this complexity.");
			return;
		}
		if (party.getFloor() > maxFloor) {
			player.getPacketSender().sendInterfaceRemoval();
			player.message("You can't do this floor.");
			return;
		}
		invitedBy.getDungManager().resetInvitation();
		invitedBy.getDungManager().getParty().setDifficulty(0);
		invitedBy.getDungManager().getParty().add(player);
		player.stopAll();
		invitedBy.stopAll();
	}

	public void invite(Player friend) {
		if (party == null) {
			DungeonPartyManager party = friend.getDungManager().getParty();
			if (friend.getDungManager().invitingPlayer != player || player.getZ() != 0 || party == null || !party.isLeader(friend)) {
				player.message("You need to be in a party to invite someone.");
				return;
			}
			friend.getDungManager().expireInvitation();
		} else {
			if (!party.isLeader(player) || party.getDungeon() != null) {
				player.message("You can't do that right now.");
				return;
			}
			if (party.getSize() >= 5) {
				player.message("Your party is full.");
				return;
			}
			if (friend == null) {
				player.message("That player is offline, or has privacy mode enabled.");
				return;
			}
			if (!(friend.getControllerManager().getController() instanceof Daemonheim)) {
				player.message("You can only invite a player in or around Daemonheim.");
				return;
			}
			if (friend.getDungManager().party != null) {
				player.message(friend.getUsername() + " is already in a party.");
				return;
			}
			if (friend.busy()) {
				player.message("The other player is busy.");
				return;
			}
			expireInvitation();
			invitingPlayer = friend;
			player.message("Sending party invitation to " + friend.getUsername() + "...");
			friend.getPacketSender().sendDungeonneringRequestMessage(player);
		}
	}

	public void invite(String name) {
		player.stopAll();

		if(name == null || name.length() <= 0)
			return;
		name = Misc.formatText(name);
		Player friend = World.getPlayerByName(name);

		if (friend == null) {
			player.message("Unable to find " + name);
			return;
		}

		invite(friend);
	}

	public void openResetProgress() {
		player.stopAll();
		player.getDialogueManager().startDialogue(new PrestigeReset());
	}

	public void switchGuideMode() {
		/*if (party == null) {
			player.message("You must be in a party to do that.");
			return;
		}
		if (party.getDungeon() != null) {
			player.message("You cannot change the guide mode once the dungeon has started.");
			return;
		}
		if (!party.isLeader(this.player)) {
			this.player.message("Only your party's leader can switch guide mode!");
			return;
		}
		player.stopAll();
		party.setGuideMode(!party.getGuideMode());
		if (party.getGuideMode())
			player.message("Guide mode enabled. Your map will show you the critical path, but you will receive an xp penalty.");
		else
			player.message("Guide mode disabled. Your map will no longer show the critical path.");
		for (Player p2 : party.getTeam())
			p2.getDungManager().refreshPartyGuideModeComponent();*/
	}

	public void changeFloor() {
		if (party == null) {
			player.message("You must be in a party to do that.");
			return;
		}
		if (party.getDungeon() != null) {
			player.message("You cannot change these settings while in a dungeon.");
			return;
		}
		if (!party.isLeader(this.player)) {
			this.player.message("Only your party's leader can change floor!");
			return;
		}
		player.stopAll();
		player.setInputHandling(new EnterFloorNumber());
		player.getPacketSender().sendEnterAmountPrompt("Select floor (1-60):");
	}

	public void selectFloor(int selectedFloor) {
		player.stopAll();
		if (party == null) {
			player.message("You must be in a party to do that.");
			return;
		}
		if (selectedFloor == -1)
			selectedFloor = party.getMaxFloor();
		if (party.getMaxFloor() < party.getMaxFloor()) {
			player.message("A member in your party can't do this floor.");
			return;
		}
		if (party.getDungeon() != null) {
			player.message("You cannot change these settings while in a dungeon.");
			return;
		}
		if (!party.isLeader(player)) {
			player.message("Only your party's leader can change floor!");
			return;
		}
		party.setFloor(selectedFloor);
	}

	public void changeComplexity() {
		if (party == null) {
			player.message("You must be in a party to do that.");
			return;
		}
		if (party.getDungeon() != null) {
			player.message("You cannot change these settings while in a dungeon.");
			return;
		}
		if (!party.isLeader(player)) {
			player.message("Only your party's leader can change complexity!");
			return;
		}
		player.stopAll();
		player.setInputHandling(new EnterComplexity());
		player.getPacketSender().sendEnterAmountPrompt("Select complexity (1-6):");
	}

	public void selectComplexity(int selectedComplexity) {
		player.stopAll();
		if (selectedComplexity == -1)
			return;
		if (party == null) {
			player.message("You must be in a party to do that.");
			return;
		}
		if (party.getMaxComplexity() < selectedComplexity) {
			player.message("A member in your party can't do this complexity.");
			return;
		}
		if (party.getDungeon() != null) {
			player.message("You cannot change these settings while in a dungeon.");
			return;
		}
		if (!party.isLeader(player)) {
			player.message("Only your party's leader can change complexity!");
			return;
		}

		party.setComplexity(selectedComplexity);
	}

	private void markComplexity(int complexity, boolean mark) {
		//player.getPacketSender().sendHideIComponent(938, 14 + ((complexity - 1) * 5), !mark);
	}

	private static final String[] COMPLEXITY_SKILLS =
		{
		"Combat",
		"Cooking",
		"Firemaking",
		"Woodcutting",
		"Fishing",
		"Creating Weapons",
		"Mining",
		"Runecrafting",
		"Farming Textiles",
		"Hunting",
		"Creating Armour",
		"Farming Seeds",
		"Herblore",
		"Thieving",
		"Summoning",
		"Construction",
		"Divination" };

	private void hideSkills(int complexity) {
		int count = 0;
		if (complexity >= 1)
			count += 1;
		if (complexity >= 2)
			count += 4;
		if (complexity >= 3)
			count += 3;
		if (complexity >= 4)
			count += 3;
		if (complexity >= 5)
			count += 5;
		if (complexity >= 6)
			count += 1;
		for (int i = 0; i < COMPLEXITY_SKILLS.length; i++) {
			//player.getPacketSender().sendIComponentText(938, 45 + i * 2, (i >= count ? "<col=383838>" : "") + COMPLEXITY_SKILLS[i]);
		}
	}

	public void expireInvitation() {
		if (invitingPlayer == null)
			return;
		player.message("Your dungeon party invitation to " + invitingPlayer.getUsername() + " has expired.");
		invitingPlayer.message("A dungeon party invitation from " + player.getUsername() + " has expired.");
		invitingPlayer = null;
	}

	public void enterDungeon(boolean selectSize) {
		player.stopAll();
		expireInvitation();
		if (party == null) {
			player.message("You must be in a party to start a dungeon.");
			//player.getDialogueManager().startDialogue(new DungeonPartyStart");
			return;
		}
		if (party.getDungeon() != null) //cant happen
			return;
		if (!party.isLeader(player)) {
			player.message("Only your party's leader can start a dungeon!");
			return;
		}
		if (party.getFloor() == 0) {
			changeFloor();
			return;
		}

		if(party.getFloor() >= 12 && party.getFloor() <= 14) {
            player.message("This floor is currently disabled.");
            player.getDungManager().tickOff(party.getFloor());
            return;
        }

		if (party.getComplexity() == 0) {
			changeComplexity();
			return;
		}
		if (party.getDifficulty() == 0) {
			if (party.getTeam().size() == 1)
				party.setDifficulty(1);
			else {
				player.getDialogueManager().startDialogue(new DungeonDifficultyD(), party.getTeam().size());
				return;
			}
		}

		boolean solo = party.getTeam().size() == 1;
		if (solo)
			party.setKeyShare(true);
		else if (party.getKeyType() == 0) {
			party.setKeyShare(true);
            //System.out.println("pre share d");
            //player.getDialogueManager().startDialogue(new PreShareD());
			//return;
		}
		if (selectSize) {
			/*if (party.getComplexity() == 6) {
                System.out.println("dungeon size");
                //player.getDialogueManager().startDialogue(new DungeonSize");
				return;
			} else*/
				party.setSize(DungeonConstants.SMALL_DUNGEON);
		}
		for (Player p2 : party.getTeam()) {
			for (Item item : p2.getInventory().getItems()) {
				if (item != null && item.getId() != -1 && item.getId() != 15707 && item.getId() != 5733) {
					player.message((player == p2 ? "You are" : p2.getUsername() + " is") + " carrying items that cannot be taken into Daemonheim.");
					return;
				}
			}
			for (Item item : p2.getEquipment().getItems()) {
				if (item != null && item.getId() != -1 && item.getId() != 15707 && item.getId() != 5733) {
					player.message((player == p2 ? "You are" : p2.getUsername() + " is") + " carrying items that cannot be taken into Daemonheim.");
					return;
				}
			}
			if (p2.getFamiliar() != null) {
				player.message((player == p2 ? "You have " : p2.getUsername() + " has ") + " a familiar that cannot be taken into Daemonheim.");
				return;
			}
			if (p2.getZ() != 0 /*|| p2.getInterfaceId() <= 0*/ || p2.getMovement().isLocked()) {
				player.message(p2.getUsername() + " is busy.");
				return;
			}
		}
		party.start();
	}

	public void setSize(int size) {
		if (party == null || !party.isLeader(player) || party.getComplexity() != 6)
			return;
		party.setSize(size);
	}

	public void setDificulty(int dificulty) {
		if (party == null || !party.isLeader(player))
			return;
		party.setDifficulty(dificulty);
	}

	public void setKeyShare(boolean isKeyShare) {
		if (party == null || !party.isLeader(player))
			return;
		party.setKeyShare(isKeyShare);
	}

	public void resetInvitation() {
		if (invitingPlayer == null)
			return;
		invitingPlayer = null;
	}

	public void refreshNames() {
		if (party == null)
			return;
		for(int i = 0; i < 5; i++) {
			if(i < party.getTeam().size()) {
				player.getPacketSender().sendString(26235 + i, party.getTeam().get(i).getUsername());
			} else {
				player.getPacketSender().sendString(26235 + i, "");
			}
		}
	}

	public void refreshFloor() {
		player.getPacketSender().sendString(26240, party == null ? "0" : party.getFloor() + "");
		//player.getPacketSender().sendCSVarInteger(1180, party == null ? 0 : party.getFloor());
	}

	public void refreshComplexity() {
		player.getPacketSender().sendString(26241, party == null ? "0" : party.getComplexity() + "");
		//player.getPacketSender().sendCSVarInteger(1183, party == null ? 0 : party.getComplexity());
	}

	public void refreshCurrentProgress() {
		player.getPacketSender().sendString(26242, getCurrentProgress() + "");
		//player.getPacketSender().sendCSVarInteger(1181, getCurrentProgress());
	}

	public void refreshPreviousProgress() {
		player.getPacketSender().sendString(26243, previousProgress + "");
		//player.getPacketSender().sendCSVarInteger(1182, previousProgress);
	}

	public DungeonPartyManager getParty() {
		return party;
	}

	public void setParty(DungeonPartyManager party) {
		this.party = party;
	}

	public void formParty() {
		if (party != null)
			return;
		if (!(player.getControllerManager().getController() instanceof Daemonheim) || player.getZ() != 0) {
			player.message("You can only form a party in or around Daemonheim.");
			return;
		}
		player.getPacketSender().sendTabInterface(GameSettings.QUESTS_TAB, DungeonConstants.PARTY_INTERFACE);
		player.stopAll();
		DungeonPartyManager party = new DungeonPartyManager();
		party.add(player);
		party.setDefaults();
		party.refreshInterface();
	}

	public void finish() {
		if (party != null)
			party.leaveParty(player, true, false);
	}

	public void checkLeaveParty() {
		if (party == null)
			return;
		if (party.getDungeon() != null) {
			player.getDialogueManager().startDialogue(new DungeonLeaveParty());
		} else
			leaveParty();
	}

	public void leaveParty() {
		if (party != null)
			party.leaveParty(player, false, true);
	}

	public Item[] handleDungRunes(Player player, Item[] runes) {
		int[] REGULAR_RUNES = new int[] {556, 555, 557, 554, 558, 562, 560, 565, 559, 564, 9075, 561, 563, 566};
		for(Item rune : runes) {
			if(rune == null)
				continue;

			for (int i = 0; i < REGULAR_RUNES.length; i++)
				if (rune.getId() == REGULAR_RUNES[i]) {
					rune.setId(DungeonConstants.RUNES[i]);
					if(player.getDungManager().getBindedAmmo() != null && player.getDungManager().getBindedAmmo().getId() == DungeonUtils.getBindedId(rune))
						rune.setId(player.getDungManager().getBindedAmmo().getId());
				}
		}
		return runes;
	}
}
