package com.fury.game.system.communication.clans;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.GameSettings;
import com.fury.game.content.global.Achievements;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.character.player.info.DonorStatus;
import com.fury.game.entity.character.player.info.GameMode;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.core.model.item.Item;
import com.fury.game.system.files.Resources;
import com.fury.game.world.World;
import com.fury.util.Colours;
import com.fury.util.FontUtils;
import com.fury.util.Misc;
import com.fury.util.NameUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;

/**
 * 100% Runescape clanchat system.
 *
 * @author Gabriel Hannason
 */
public class ClanChatManager {

    private static List<ClanChat> clans = new ArrayList<>();

    public static List<ClanChat> getClans() {
        return clans;
    }

    public static ClanChat getClanChat(int index) {
        return clans.get(index);
    }

    public static ClanChat getClanChatChannel(Player player) {
        for (ClanChat clan : clans) {
            if (clan == null || clan.getOwnerName() == null)
                continue;

            if (clan.getOwnerName().equals(player.getUsername())) {
                return clan;
            }
        }
        return null;
    }

    public static void init() {
        long startup = System.currentTimeMillis();
        for (File file : (new File(Resources.getSaveDirectory("clans"))).listFiles()) {
            if (!file.exists() || file.length() <= 0)
                continue;
            try {
                DataInputStream input = new DataInputStream(new FileInputStream(file));
                String name = input.readUTF();
                String owner = input.readUTF();
                int index = input.readShort();
                ClanChat clan = new ClanChat(owner, name);
                clan.setRankRequirements(ClanChat.RANK_REQUIRED_TO_ENTER, ClanChatRank.forId(input.read()));
                clan.setRankRequirements(ClanChat.RANK_REQUIRED_TO_KICK, ClanChatRank.forId(input.read()));
                clan.setRankRequirements(ClanChat.RANK_REQUIRED_TO_TALK, ClanChatRank.forId(input.read()));
                clan.setRankRequirements(ClanChat.RANK_REQUIRED_TO_SHARE_LOOT, ClanChatRank.forId(input.read()));
                clan.setCoinShare(input.readBoolean());
                int totalRanks = input.readShort();
                for (int i = 0; i < totalRanks; i++) {
                    clan.getRankedNames().put(input.readUTF(), ClanChatRank.forId(input.read()));
                }
                int totalBans = input.readShort();
                for (int i = 0; i < totalBans; i++) {
                    clan.addBannedName(input.readUTF());
                }
                clans.add(clan);
                clan.setIndex(clans.indexOf(clan));
                input.close();
            } catch (IOException exception) {
                System.err.println("Error loading clan chat file: " + file.getName());
                exception.printStackTrace();
            }
        }
        if (GameSettings.DEBUG)
            System.out.println("Loaded clan chat manager " + (System.currentTimeMillis() - startup) + "ms");
    }

    public static void writeFile(ClanChat clan) {
        try {
            File file = new File(Resources.getSaveDirectory("clans") + clan.getName());
            if (file.exists())
                file.createNewFile();
            DataOutputStream output = new DataOutputStream(new FileOutputStream(file));
            output.writeUTF(clan.getName());
            output.writeUTF(clan.getOwnerName());
            output.writeShort(clan.getIndex());
            output.write(clan.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_ENTER] != null ? clan.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_ENTER].ordinal() : -1);
            output.write(clan.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_KICK] != null ? clan.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_KICK].ordinal() : -1);
            output.write(clan.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_TALK] != null ? clan.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_TALK].ordinal() : -1);
            output.write(clan.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_SHARE_LOOT] != null ? clan.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_SHARE_LOOT].ordinal() : -1);
            output.writeBoolean(clan.getCoinShare());
            output.writeShort(clan.getRankedNames().size());
            for (Entry<String, ClanChatRank> iterator : clan.getRankedNames().entrySet()) {
                String name = iterator.getKey();
                int rank = iterator.getValue() == null ? 0 : iterator.getValue().ordinal();
                output.writeUTF(name);
                output.write(rank);
            }
            output.writeShort(clan.getBannedNames().size());
            for (String ban : clan.getBannedNames()) {
                output.writeUTF(ban);
            }
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        for (ClanChat clan : clans) {
            if (clan != null) {
                writeFile(clan);
            }
        }
    }

    public static void createClan(Player player) {
        player.getPacketSender().sendInterfaceRemoval();
        if (getClanChatChannel(player) != null) {
            player.message("You have already created a clanchat channel.");
            return;
        }
        File file = new File(Resources.getSaveDirectory("clans") + player.getUsername());
        if (file.exists())
            file.delete();
        ClanChat createdCc = create(player);
        if (createdCc != null) {
            if (player.getCurrentClanChat() == null) {
                join(player, createdCc);
            }
            player.message("You now have a clanchat channel.");
        }
    }

    public static void deleteClan(Player player) {
        player.getPacketSender().sendInterfaceRemoval();
        if (player.getCurrentClanChat() == null) {
            player.message("Please enter you clanchat channel before doing this.");
            return;
        }
        if (getClanChatChannel(player) == null) {
            player.message("You have not created a clanchat channel yet.");
            return;
        }
        delete(player);
    }

    public static ClanChat create(Player player) {
        File file = new File(Resources.getSaveDirectory("clans") + player.getUsername());
        if (file.exists()) {
            player.message("Your clan channel is already public!");
            return null;
        }

        ClanChat clan = new ClanChat(player, player.getUsername());
        clans.add(clan);

        int index = clans.indexOf(clan);

        clan.setIndex(index);
        clans.get(index).getRankedNames().put(player.getUsername(), ClanChatRank.OWNER);
        writeFile(clans.get(index));
        return clans.get(index);
    }

    public static void join(Player player, String channel) {
        if (channel == null || channel.equals("null"))
            return;
        if (player.getCurrentClanChat() != null) {
            player.message("You are already in a clan channel.");
            return;
        }
        channel = channel.toLowerCase();

        for (ClanChat clan : clans) {
            if (clan != null) {
                if (clan.getName().toLowerCase().equals(channel)) {
                    if(clan.getMembers().contains(player))
                        clan.removeMember(player.getUsername());

                    join(player, clan);
                    return;
                }
            }
        }

        for (ClanChat clan : clans) {
            if (clan != null) {
                if (clan.getOwnerName().toLowerCase().equals(channel)) {
                    if(clan.getMembers().contains(player))
                        clan.removeMember(player.getUsername());

                    join(player, clan);
                    return;
                }
            }
        }
        if (player.getUsername().equalsIgnoreCase(channel))
            ClanChatManager.createClan(player);
        else
            player.message("That channel does not exist.");
    }

    public static void updateList(ClanChat clan) {
        Collections.sort(clan.getMembers(), new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                ClanChatRank rank1 = clan.getRank(o1);
                ClanChatRank rank2 = clan.getRank(o2);
                if (rank1 == null && rank2 == null) {
                    return 1;
                }
                if (rank1 == null && rank2 != null) {
                    return 1;
                } else if (rank1 != null && rank2 == null) {
                    return -1;
                }
                if (rank1.ordinal() == rank2.ordinal()) {
                    return 1;
                }
                if (rank1 == ClanChatRank.OWNER) {
                    return -1;
                } else if (rank2 == ClanChatRank.OWNER) {
                    return 1;
                }
                if (rank1.ordinal() > rank2.ordinal()) {
                    return -1;
                }
                return 1;
            }
        });
        for (Player member : clan.getMembers()) {
            if (member != null) {
                int childId = 29344;
                for (Player others : clan.getMembers()) {
                    if (others != null) {
                        ClanChatRank rank = clan.getRank(others);
                        int image = -1;
                        if (rank != null) {
                            image = 93 + rank.ordinal();
                        }
                        String prefix = image >= 0 ? FontUtils.imageTags(image) + " " : "";
                        member.getPacketSender().sendString(childId, prefix + others.getUsername());
                        childId++;
                    }
                }
                for (int i = childId; i < 29444; i++) {
                    member.getPacketSender().sendString(i, "");
                }
                ClanChatRank rank = clan.getRank(member);
                if (rank != null) {
                    if (rank == ClanChatRank.OWNER && member.getInterfaceId() == 40172)
                        ClanChatManager.sendFriendsList(member);
                    if (rank == ClanChatRank.OWNER || rank == ClanChatRank.STAFF) {
                        member.getPacketSender().sendClanChatListOptionsVisible(2); //Kick/demote/promote options
                    } else if (clan.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_KICK] != null && rank.ordinal() >= clan.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_KICK].ordinal()) {
                        member.getPacketSender().sendClanChatListOptionsVisible(1); //only kick option
                    } else {
                        member.getPacketSender().sendClanChatListOptionsVisible(0); //no options
                    }
                }
            }
        }
    }

    public static void sendMessage(Player player, String message) {
        ClanChat clan = player.getCurrentClanChat();
        if (clan == null) {
            player.message("You're not in a clanchat channel.");
            return;
        }
        ClanChatRank rank = clan.getRank(player);
        if (clan.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_TALK] != null) {
            if (rank == null || rank.ordinal() < clan.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_TALK].ordinal()) {
                player.message("You do not have the required rank to speak in this channel.");
                return;
            }
        }
        player.getLogger().addClanMessage(message, clan.getOwnerName());
        for (Player memberPlayer : clan.getMembers()) {
            if (memberPlayer != null) {
                if (memberPlayer.getRelations().getIgnoreList().contains(player.getLongUsername()))
                    continue;

                int icon = -1;
                if (player.getRights() == PlayerRights.PLAYER) {
                    if (player.getGameMode() == GameMode.IRONMAN)
                        icon = 541;
                    else if (player.isDonor())
                        icon = 1703 + DonorStatus.get(player).ordinal();
                } else
                    icon = player.getRightsId() + 530;

                String rankImg = icon != -1 ? " " + FontUtils.imageTags(icon) + " " : " ";
                memberPlayer.getPacketSender().sendClanChatMessage(FontUtils.BLACK + "[" + FontUtils.BLUE + clan.getName() + FontUtils.COL_END + "]" + "" + rankImg + "" +
                        NameUtils.capitalizeWords(player.getUsername()) + ":" + FontUtils.COL_END + " " + FontUtils.add(NameUtils.capitalize(message), 0x800000));
            }
        }
    }

    public static void sendMessage(ClanChat clan, String message) {
        for (Player member : clan.getMembers())
            if (member != null)
                member.message(message, Colours.BLACK);
    }

    public static void sendLootshare(Player player, String received) {
        for (Player member : player.getCurrentClanChat().getMembers()) {
            if (member != null) {
                if (member == player) {
                    member.getPacketSender().sendClanChatMessage(FontUtils.add("You received: " + received + ".", 0x26661c));
                } else {
                    member.getPacketSender().sendClanChatMessage(player.getUsername() + " received: " + received + ".", Colours.BLACK);
                }
            }
        }
    }

    public static void leave(Player player, boolean kicked) {
        final ClanChat clan = player.getCurrentClanChat();
        if (clan == null) {
            player.message("You are not in a clan chat channel.");
            return;
        }
        player.getPacketSender().sendString(29340, "Talking in: Not in chat");
        player.getPacketSender().sendString(29450, "Owner: None");
        player.getPacketSender().sendButtonToggle(29455, false);
        player.getPacketSender().sendClanChatStatus(false);
        player.setCurrentClanChat(null);
        clan.removeMember(player.getUsername());
        for (int i = 29344; i < 29444; i++) {
            player.getPacketSender().sendString(i, "");
        }
        player.getPacketSender().sendClanChatListOptionsVisible(0);
        updateList(clan);
        player.message(kicked ? "You have been kicked from the channel." : "You have left the channel.");
        player.getPacketSender().sendButtonToggle(29329, false);
        player.getPacketSender().sendTooltip(29329, "Join Clan");
    }

    private static void join(Player player, ClanChat clan) {
        if (clan.getOwnerName().equals(player.getUsername())) {
            if (clan.getOwner() == null) {
                clan.setOwner(player);
            }
            clan.giveRank(player, ClanChatRank.OWNER);
            Achievements.finishAchievement(player, Achievements.AchievementData.JOIN_A_CLAN_CHAT);
        }
        player.message("Attempting to join channel...");
        if (clan.getMembers().size() >= 100) {
            player.message("This clan channel is currently full.");
            return;
        }
        if (clan.isBanned(player.getUsername())) {
            player.message("You're currently banned from using this channel. Bans expire every 20 minutes.");
            return;
        }
        checkFriendsRank(player, clan, false);
        ClanChatRank rank = clan.getRank(player);
        if(clan.getName().equalsIgnoreCase("help") && player.getRights().isStaff()) {
            switch (player.getRights()) {
                case SUPPORT:
                    clan.giveRank(player, ClanChatRank.SERGEANT);
                    break;
                case COMMUNITY_MANAGER:
                    clan.giveRank(player, ClanChatRank.LIEUTENANT);
                    break;
                case MODERATOR:
                    clan.giveRank(player, ClanChatRank.CAPTAIN);
                    break;
                case ADMINISTRATOR:
                    clan.giveRank(player, ClanChatRank.GENERAL);
                    break;
                case OWNER:
                    clan.giveRank(player, ClanChatRank.STAFF);
                    break;
                case DEVELOPER:
                    clan.giveRank(player, ClanChatRank.OWNER);
                    break;
            }
        } else if (player.getRights().isUpperStaff()) {
            if (rank == null || rank != ClanChatRank.OWNER) {
                rank = ClanChatRank.STAFF;
                clan.giveRank(player, ClanChatRank.STAFF);
            }
        } else {
            if (rank != null && rank == ClanChatRank.STAFF) {
                clan.giveRank(player, null);
            }
        }
        if (clan.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_ENTER] != null) {
            if (rank == null || clan.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_ENTER].ordinal() > rank.ordinal()) {
                player.message("Your rank is not high enough to enter this channel.");
                return;
            }
        }
        player.setCurrentClanChat(clan);
        player.setClanChatName(clan.getName());
        String clanName = NameUtils.capitalizeWords(clan.getName());
        clan.addMember(player);
        player.getPacketSender().sendClanChatStatus(true);
        player.getPacketSender().sendString(29340, "Talking in: " + FontUtils.YELLOW + clanName + FontUtils.COL_END, Colours.ORANGE);
        player.getPacketSender().sendString(29450, "Owner: " + FontUtils.YELLOW + Misc.uppercaseFirst(clan.getOwnerName()) + FontUtils.COL_END, Colours.ORANGE);
        player.message("Now talking in clan channel " + clan.getName());
        player.message("To talk, start each line of chat with the / symbol.");
        player.getPacketSender().sendButtonToggle(29329, true);
        player.getPacketSender().sendTooltip(29329, "Leave Clan");
        updateList(clan);
    }

    public static void checkFriendsRank(Player player, ClanChat chat, boolean update) {
        ClanChatRank rank = chat.getRank(player);
        if (rank == null) {
            if (chat.getOwner() != null && chat.getOwner().getRelations().isFriendWith(player.getUsername())) {
                chat.giveRank(player, ClanChatRank.FRIEND);
                if (update) {
                    updateList(chat);
                }
            }
        } else {
            if (rank == ClanChatRank.FRIEND && chat.getOwner() != null && !chat.getOwner().getRelations().isFriendWith(player.getUsername())) {
                chat.giveRank(player, null);
                if (update) {
                    updateList(chat);
                }
            }
        }
    }

    public static void delete(Player player) {
        ClanChat clan = getClanChatChannel(player);
        File file = new File(Resources.getSaveDirectory("clans") + clan.getName());
        for (Player member : clan.getMembers()) {
            if (member != null) {
                leave(member, true);
                member.setClanChatName("");
            }
        }
        if (player.getClanChatName().equalsIgnoreCase(clan.getName())) {
            player.setClanChatName("");
        }
        player.message("Your clan chat channel was successfully deleted.");
        clans.remove(clan.getIndex());
        file.delete();
    }

    public static void setName(Player player, String newName) {
        final ClanChat clan = getClanChatChannel(player);
        if (clan == null) {
            player.message("You need to have a clan channel to do this.");
            return;
        }
        if (newName.length() == 0)
            return;
        if (newName.length() > 12)
            newName = newName.substring(0, 11);

        if (newName.equalsIgnoreCase("help")) {
            player.message("You cannot use that clan chat name.");
            return;
        }
        if (new File(Resources.getSaveDirectory("clans") + newName).exists()) {
            player.message("That clanchat name is already taken.");
            return;
        }
        if (clan.getLastAction().elapsed(5000) || player.getRights().isStaff()) {
            new File(Resources.getSaveDirectory("clans") + clan.getName()).delete();
            clan.setName(NameUtils.capitalizeWords(newName));
            for (Player member : clan.getMembers()) {
                if (member == null)
                    continue;
                member.setClanChatName(clan.getName());
                member.getPacketSender().sendString(29340, "Talking in: " + FontUtils.YELLOW + clan.getName() + FontUtils.COL_END, Colours.ORANGE);
            }
            clanChatSetupInterface(player, false);
            writeFile(clan);
            clan.getLastAction().reset();
            Achievements.finishAchievement(player, Achievements.AchievementData.NAME_YOUR_CLAN);
        } else {
            player.message("You need to wait a few seconds between every clan chat action.");
        }
    }

    public static void kick(Player player, Player target) {
        ClanChat clan = player.getCurrentClanChat();
        if (clan == null) {
            player.message("You're not in a clan channel.");
            return;
        }
        final ClanChatRank rank = clan.getRank(player);
        if (rank == null || rank != ClanChatRank.STAFF && clan.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_KICK] != null && rank.ordinal() < clan.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_KICK].ordinal()) {
            player.message("You do not have the required rank to kick this player.");
            return;
        }
        for (Player member : clan.getMembers()) {
            if (member != null && member.equals(target)) {
                ClanChatRank memberRank = clan.getRank(member);
                if (memberRank != null) {
                    if (memberRank == ClanChatRank.STAFF) {
                        player.message("That player cannot be kicked.");
                        break;
                    }
                    if (rank.ordinal() < memberRank.ordinal()) {
                        player.message("You cannot kick a player who has a higher rank than you!");
                        break;
                    }
                }
                clan.addBannedName(member.getUsername());
                leave(member, true);
                sendMessage(player.getCurrentClanChat(), "[" + FontUtils.add(clan.getName(), 0xff) + "] " + member.getUsername() + " has been kicked from the channel by " + player.getUsername() + ".");
                break;
            }
        }
    }

    public static void handleMemberOption(Player player, int index, int menuId) {
        if ((player.getCurrentClanChat() == null || !player.getCurrentClanChat().getOwnerName().equals(player.getUsername())) && menuId != 1) {
            player.message("Only the clan chat owner can do that.");
            return;
        }
        Player target = getPlayer(index, player.getCurrentClanChat());
        if (target == null || target.equals(player)) {
            return;
        }
        switch (menuId) {
            case 8:
            case 7:
            case 6:
            case 5:
            case 4:
            case 3:
                ClanChatRank rank = ClanChatRank.forMenuId(menuId);
                ClanChatRank targetRank = player.getCurrentClanChat().getRank(target);
                if (targetRank != null) {
                    if (targetRank == rank) {
                        player.message("That player already has that rank.");
                        return;
                    }
                    if (targetRank == ClanChatRank.STAFF) {
                        player.message("That player cannot be promoted or demoted.");
                        return;
                    }
                }
                if (player.getCurrentClanChat().getLastAction().elapsed(5000) || player.getRights().isStaff()) {
                    player.getCurrentClanChat().giveRank(target, rank);
                    updateList(player.getCurrentClanChat());
                    if (rank != ClanChatRank.FRIEND)
                        Achievements.finishAchievement(player, Achievements.AchievementData.PROMOTE_A_FRIEND);
                    //sendMessage(player.getCurrentClanChat(), "<col=16777215>[<col=255>"+player.getCurrentClanChat().getName() +"<col=16777215>]<col=3300CC> "+target.getUsername()+" has been given the rank: "+Misc.formatText(rank.name().toLowerCase())+".");
                    player.getCurrentClanChat().getLastAction().reset();
                } else {
                    player.message("You need to wait a few seconds between every clanchat action.");
                }
                break;
            case 2:
                targetRank = player.getCurrentClanChat().getRank(target);
                if (targetRank == null) {
                    player.message("That player has no rank.");
                    return;
                }
                if (targetRank == ClanChatRank.STAFF) {
                    player.message("That player cannot be promoted or demoted.");
                    return;
                }
                if (player.getCurrentClanChat().getLastAction().elapsed(5000) || player.getRights().isStaff()) {
                    player.getCurrentClanChat().getRankedNames().remove(target.getUsername());
                    checkFriendsRank(target, player.getCurrentClanChat(), false);
                    updateList(player.getCurrentClanChat());
                    //sendMessage(player.getCurrentClanChat(), "[<col=255>"+player.getCurrentClanChat().getName() +"</col>]<col=3300CC> "+target.getUsername()+" has been demoted from his rank.");
                    player.getCurrentClanChat().getLastAction().reset();
                } else {
                    player.message("You need to wait a few seconds between every clan chat action.");
                }
                break;
            case 1:
                kick(player, target);
                break;
        }
    }

    public static boolean dropShareLoot(Player player, Mob mob, Item itemDropped) {
        /*	ClanChat clan = player.getFields().getClanChat();
		if (clan != null) {
			boolean received = false;
			Cache<Player> players = getPlayersWithinPosition(clan, npc);
			String green = "<col=" + ClanChatMessageColor.GREEN.getRGB()[player.getFields().rgbIndex] + ">";
			if (clan.isItemSharing() && itemDropped.getScrollId() != 995) {
				Player rewarded = players.size() > 0 ? players.get(MathUtils.random(players.size() - 1)) : null;
				if (rewarded != null) {
					rewarded.message(green + "You have received " + itemDropped.getAmount() + "worldX " + itemDropped.getCombatDefinition().getName() + ".");
					received = true;
				}
			}
			if (clan.isCoinSharing() && itemDropped.getScrollId() == 995) {
				for (Item drop : npc.getDrops()) {
					if ((drop.getCombatDefinition().getValue() * drop.getAmount()) < 50000) {
						GroundItem groundItem = new GroundItem(drop, npc.copyPosition());
						GameServer.getWorld().register(groundItem, player);
						continue;
					}
					int amount = (int) (ItemDefinition.forId(drop.getScrollId()).getValue() / players.size());
					Item split = new Item(995, amount);
					for (Player member : players) {
						GroundItem groundItem = new GroundItem(split.copyPosition(), npc.copyPosition());
						GameServer.getWorld().register(groundItem, member);
						member.message(green + "You have received " + amount + "worldX " + split.getCombatDefinition().getName() + " as part of a split drop.");
					}
				}
			} else if(!clan.isItemSharing() && !clan.isCoinSharing() || !received)
				return false;
		} else
			return false;*/
        return false;
    }

    public static void toggleLootShare(Player player) {
        final ClanChat clan = player.getCurrentClanChat();
        if (clan == null) {
            player.getPacketSender().sendButtonToggle(29455, false);
            player.message("You're not in a clan channel.");
            return;
        }
        player.getPacketSender().sendButtonToggle(29455, clan.getLootShare());
        if (!player.getRights().isStaff()) {
            if (clan.getOwner() == null)
                return;
            if (!clan.getOwner().equals(player)) {
                player.message("Only the owner of the channel has the power to do this.");
                return;
            }
        }
        if (clan.getLastAction().elapsed(5000) || player.getRights().isStaff()) {
            clan.setLootShare(!clan.getLootShare());
            for (Player memeber : clan.getMembers())
                memeber.getPacketSender().sendButtonToggle(29455, clan.getLootShare());
            sendMessage(clan, "[" + FontUtils.add(clan.getName(), 0xff) + "] Lootshare has been " + (clan.getLootShare() ? "enabled" : "disabled") + ".");
            clan.getLastAction().reset();
            if (clan.getLootShare())
                Achievements.finishAchievement(player, Achievements.AchievementData.ACTIVATE_LOOTSHARE);
        } else {
            player.message("You need to wait a few seconds between every clanchat action.");
        }
    }

    public static void toggleCoinShare(Player player) {
        final ClanChat clan = player.getCurrentClanChat();
        if (clan == null) {
            player.getPacketSender().sendButtonToggle(48042, false);
            player.message("You're not in a clan channel.");
            return;
        }
        if (!player.getRights().isStaff()) {
            if (clan.getOwner() == null)
                return;
            if (!clan.getOwner().equals(player)) {
                player.message("Only the owner of the channel has the power to do this.");
                return;
            }
        }
        if (clan.getLastAction().elapsed(5000) || player.getRights().isStaff()) {
            clan.setCoinShare(!clan.getCoinShare());
            player.getPacketSender().sendButtonToggle(48042, !clan.getCoinShare());
            if (clan.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_SHARE_LOOT] != null)
                sendMessage(clan, "[" + FontUtils.add(clan.getName(), 0xff) + "] Coinshare has been " + (clan.getCoinShare() ? "enabled for " + Misc.uppercaseFirst(clan.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_SHARE_LOOT].name()) + "'s and over" : "disabled") + ".");
            else
                sendMessage(clan, "[" + FontUtils.add(clan.getName(), 0xff) + "] Coinshare has been " + (clan.getCoinShare() ? "enabled" : "disabled") + ".");
            clan.getLastAction().reset();
        } else {
            player.getPacketSender().sendButtonToggle(48042, !clan.getCoinShare());
            player.message("You need to wait a few seconds between every clanchat action.");
        }
    }

    public static boolean handleClanChatSetupButton(Player player, int interfaceId) {
        if (player.getInterfaceId() == 40172) {
            final ClanChat clan = getClanChatChannel(player);
            if (clan == null) {
                return true;
            }
            switch (interfaceId) {
                case 47255:
                    clan.setRankRequirements(ClanChat.RANK_REQUIRED_TO_ENTER, null);
                    player.message("You have changed your clanchat channel's settings.");
                    refreshRanks(player, clan);
                    writeFile(clan);
                    return true;
                case 47258:
                    clan.setRankRequirements(ClanChat.RANK_REQUIRED_TO_TALK, null);
                    player.message("You have changed your clanchat channel's settings.");
                    clanChatSetupInterface(player, false);
                    writeFile(clan);
                    return true;
                case 47261:
                    clan.setRankRequirements(ClanChat.RANK_REQUIRED_TO_KICK, null);
                    player.message("You have changed your clanchat channel's settings.");
                    clanChatSetupInterface(player, false);
                    updateList(clan);
                    writeFile(clan);
                    return true;
                case 48029:
                    clan.setRankRequirements(ClanChat.RANK_REQUIRED_TO_SHARE_LOOT, null);
                    player.message("You have changed your clanchat channel's settings.");
                    clanChatSetupInterface(player, false);
                    updateList(clan);
                    writeFile(clan);
                    return true;
            }
        }
        return false;
    }

    public static void refreshRanks(Player player, ClanChat clan) {
        if (clan.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_ENTER] != null) {
            for (Player member : clan.getMembers()) {
                if (member == null)
                    continue;
                ClanChatRank rank = clan.getRank(member);
                if (rank == null || clan.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_ENTER].ordinal() > rank.ordinal() && rank != ClanChatRank.STAFF) {
                    member.message("Your rank is not high enough to be in this channel.");
                    leave(member, false);
                    player.message("Warning! Changing that setting kicked the player " + member.getUsername() + " from the chat because", Colours.RED);
                    player.message(" they do not have the required rank to be in the chat.", Colours.RED);
                }
            }
        }
        clanChatSetupInterface(player, false);
    }

    public static boolean handleClanChatSetupButton(Player player, int interfaceId, int menuId) {
        if (player.getInterfaceId() == 40172) {
            final ClanChat clan = getClanChatChannel(player);
            if (clan == null) {
                return true;
            }
            if (interfaceId >= 44801 && interfaceId <= 44936) {
                int row = interfaceId - 44801;
                if (row >= player.getRelations().getFriendList().size())
                    return true;//Invalid Id
                String friendName = Misc.uppercaseFirst(NameUtils.longToString(player.getRelations().getFriendList().get(row)));

                ClanChatRank rank = clan.getRank(friendName);

                if (rank == null && ClanChatRank.forId(menuId - 1) != ClanChatRank.FRIEND)//Not in clan
                    return true;

                if (rank != ClanChatRank.STAFF) {
                    clan.giveRank(friendName, ClanChatRank.forId(menuId - 1));
                    clanChatSetupInterface(player, false);
                    writeFile(clan);
                    updateList(clan);
                }
                return true;
            }

            switch (interfaceId) {
                case 47255:
                    int l = menuId - 1;
                    clan.setRankRequirements(ClanChat.RANK_REQUIRED_TO_ENTER, ClanChatRank.forId(l));
                    player.message("You have changed your clanchat channel's settings.");
                    refreshRanks(player, clan);
                    writeFile(clan);
                    return true;
                case 47258:
                    l = menuId - 1;
                    clan.setRankRequirements(ClanChat.RANK_REQUIRED_TO_TALK, ClanChatRank.forId(l));
                    player.message("You have changed your clanchat channel's settings.");
                    clanChatSetupInterface(player, false);
                    writeFile(clan);
                    return true;
                case 47261:
                    l = menuId;
                    clan.setRankRequirements(ClanChat.RANK_REQUIRED_TO_KICK, ClanChatRank.forId(l));
                    player.message("You have changed your clanchat channel's settings.");
                    clanChatSetupInterface(player, false);
                    updateList(clan);
                    writeFile(clan);
                    return true;
                case 48029:
                    l = menuId - 1;
                    clan.setRankRequirements(ClanChat.RANK_REQUIRED_TO_SHARE_LOOT, ClanChatRank.forId(l));
                    player.message("You have changed your clanchat channel's settings.");
                    clanChatSetupInterface(player, false);
                    updateList(clan);
                    writeFile(clan);
                    return true;
            }
        }
        return false;
    }

    public static void clanChatSetupInterface(Player player, boolean check) {
        if (player.getInterfaceId() != 40172)
            player.getPacketSender().sendInterfaceRemoval();
        ClanChat channel = getClanChatChannel(player);
        if (check) {
            if (channel == null) {
                player.message("You have not created a clanchat channel yet.");
                return;
            }
        }
        player.getPacketSender().sendString(47814, channel.getName());
        if (channel.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_ENTER] == null) {
            player.getPacketSender().sendString(47815, "Anyone");
        } else {
            player.getPacketSender().sendString(47815, Misc.formatText(channel.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_ENTER].name().toLowerCase()) + "+");
        }

        if (channel.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_TALK] == null) {
            player.getPacketSender().sendString(47816, "Anyone");
        } else {
            player.getPacketSender().sendString(47816, Misc.formatText(channel.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_TALK].name().toLowerCase()) + "+");
        }

        if (channel.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_KICK] == null) {
            player.getPacketSender().sendString(47817, "Only me");
        } else {
            player.getPacketSender().sendString(47817, Misc.formatText(channel.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_KICK].name().toLowerCase()) + "+");
        }

        if (channel.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_SHARE_LOOT] == null) {
            player.getPacketSender().sendString(48028, "Anyone");
        } else {
            player.getPacketSender().sendString(48028, Misc.formatText(channel.getRankRequirement()[ClanChat.RANK_REQUIRED_TO_SHARE_LOOT].name().toLowerCase()) + "+");
        }
        player.getPacketSender().sendButtonToggle(48042, !channel.getCoinShare());

        sendFriendsList(player);

        player.getPacketSender().sendInterface(40172);
    }

    public static void sendFriendsList(Player player) {
        ClanChat clan = player.getCurrentClanChat();
        if (clan == null)
            return;
        List<Long> friendsList = player.getRelations().getFriendList();
        for (int i = 0; i < 200; i++) {
            if (i < friendsList.size()) {
                String friendName = Misc.uppercaseFirst(NameUtils.longToString(friendsList.get(i)));
                player.getPacketSender().sendString(44001 + i, friendName);

                Player friend = World.getPlayerByName(friendName);
                if (friend != null)
                    ClanChatManager.checkFriendsRank(friend, clan, true);

                ClanChatRank rank = clan.getRank(friendName);
                if (rank != null)
                    player.getPacketSender().sendString(44801 + i, Misc.uppercaseFirst(rank.name().toLowerCase()));
                else
                    player.getPacketSender().sendString(44801 + i, "Not in clan");
            } else {
                player.getPacketSender().sendString(44001 + i, "");
                player.getPacketSender().sendString(44801 + i, "");
            }
        }
    }

    public static void handleLogin(Player player) {
        resetInterface(player);
        ClanChatManager.join(player, player.getClanChatName());
    }

    public static void resetInterface(Player player) {
        player.getPacketSender().sendString(29340, "Talking in: Not in chat");
        player.getPacketSender().sendString(29450, "Owner: None");
        for (int i = 29344; i < 29444; i++) {
            player.getPacketSender().sendString(i, "");
        }
    }

    public static Player getPlayer(int index, ClanChat clan) {
        int clanIndex = 0;
        for (Player members : clan.getMembers()) {
            if (members != null) {
                if (clanIndex == index) {
                    return members;
                }
                clanIndex++;
            }
        }
        return null;
    }
}
