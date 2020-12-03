package com.fury.game.system.communication.clans;

import com.fury.Stopwatch;
import com.fury.core.task.Task;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.world.GameWorld;
import com.fury.game.world.World;
import com.fury.game.world.map.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * An instance of a clanchat channel, holding all fields.
 * @author Gabriel Hannason
 */
public class ClanChat {

	public ClanChat(Player owner, String name) {
		this.owner = owner;
		this.name = name;
		this.ownerName = owner.getUsername();
	}

	public ClanChat(String ownerName, String name) {
		this.owner = World.getPlayerByName(ownerName);
		this.ownerName = ownerName;
		this.name = name;
	}

	private String name;
	private Player owner;
	private String ownerName;

	public void setIndex(int index) {
		this.index = index;
	}

	private int index;
	private boolean lootShare, coinShare;
	private Stopwatch lastAction = new Stopwatch();

	private ClanChatRank[] rankRequirement = new ClanChatRank[4];
	private CopyOnWriteArrayList<Player> members = new CopyOnWriteArrayList<>();
	private CopyOnWriteArrayList<String> bannedNames = new CopyOnWriteArrayList<>();
	private Map<String, ClanChatRank> rankedNames = new HashMap<>();
	
	public Player getOwner() {
		return owner;
	}

	public ClanChat setOwner(Player owner) {
		this.owner = owner;
		return this;
	}

	public String getOwnerName() {
//		if(name.equalsIgnoreCase("help"))
//			return "";

		return ownerName;
	}

	public int getIndex() {
		return index;
	}

	public String getName() {
		return name;
	}

	public ClanChat setName(String name) {
		this.name = name;
		return this;
	}

	public boolean getLootShare() {
		return lootShare;
	}

	public void setLootShare(boolean lootShare) {
		this.lootShare = lootShare;
	}

	public boolean getCoinShare() {
		return coinShare;
	}

	public void setCoinShare(boolean coinShare) {
		this.coinShare = coinShare;
	}

	public Stopwatch getLastAction() {
		return lastAction;
	}

	public ClanChat addMember(Player member) {
		members.add(member);
		return this;
	}

	public ClanChat removeMember(String name) {
		for(int i = 0; i < members.size(); i++) {
			Player member = members.get(i);
			if(member == null)
				continue;
			if(member.getUsername().equals(name)) {
				members.remove(i);
				break;
			}
		}
		return this;
	}
	public ClanChatRank getRank(String username) {
		return rankedNames.get(username);
	}
	public ClanChatRank getRank(Player player) {
		return rankedNames.get(player.getUsername());
	}

	public ClanChat giveRank(String username, ClanChatRank rank) {
		rankedNames.put(username, rank);
		return this;
	}

	public ClanChat giveRank(Player player, ClanChatRank rank) {
		rankedNames.put(player.getUsername(), rank);
		return this;
	}

	public CopyOnWriteArrayList<Player> getMembers() {
		return members;
	}

	public Map<String, ClanChatRank> getRankedNames() {
		return rankedNames;
	}

	public CopyOnWriteArrayList<String> getBannedNames() {
		return bannedNames;
	}

	public void addBannedName(String name) {
		if(!bannedNames.contains(name)) {
			bannedNames.add(name);
			GameWorld.schedule(new Task(false, 100) {
				int tick = 0;
				@Override
				public void run() {
					if(tick == 20) { // 20 minutes
						bannedNames.remove(name);
						return;
					}
					tick++;
				}
			});
		}
	}
	public ArrayList<Player> getClosePlayers(Position t) {
		ArrayList<Player> list = new ArrayList<>();
		
		for(Player p : getMembers()) {
			if(p == null || !p.isWithinDistance(t, 7)) {
				continue;
			}
			list.add(p);
		}
		
		return list;
	}

	public boolean isBanned(String name) {
		return bannedNames.contains(name);
	}

	public ClanChatRank[] getRankRequirement() {
		return rankRequirement;
	}

	public ClanChat setRankRequirements(int index, ClanChatRank rankRequirement) {
		this.rankRequirement[index] = rankRequirement;
		return this;
	}

	public static final int RANK_REQUIRED_TO_ENTER = 0, RANK_REQUIRED_TO_KICK = 1, RANK_REQUIRED_TO_TALK = 2, RANK_REQUIRED_TO_SHARE_LOOT = 3;
}