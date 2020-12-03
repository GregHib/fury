package com.fury.game.system.communication;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.content.PlayerPanel;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.core.model.item.Item;
import com.fury.game.node.entity.actor.figure.player.Points;
import com.fury.game.system.files.logs.PlayerLogs;
import com.fury.game.world.GameWorld;
import com.fury.game.world.World;
import com.fury.util.Colours;
import com.fury.util.FontUtils;
import com.fury.util.Misc;

public class ReferAFriend {

    private String referral = null;
    private String[] friends = new String[100];
    private boolean receivedReward = false;
    private Player player;

    public ReferAFriend(Player player) {
        this.player = player;
    }

    public void init() {
        refreshOnline();
        checkReward();
    }

    public void refreshOnline() {
        if(referralOnline()) {
            Player refer = getReferrer();
            refer.getReferAFriend().checkFriend(player);
        }
    }

    public String getReferral() {
        return referral;
    }

    public void checkReward() {
        if(hasReceivedReward() || !hasReferral())
            return;

        long timeSince = Misc.currentTimeMillis() - player.getCreationTime();

        long hour = 1000 * 60 * 60;

        if(timeSince < hour * 2)
            return;

        if(player.getSkills().getTotalExp() < 2000000)
            return;

        GameWorld.sendBroadcast(FontUtils.imageTags(535) + FontUtils.GREEN_3 + " " + player.getUsername() + " has just received their referral rewards!" + FontUtils.COL_END);
        player.message("Congratulations you just met your referral requirements!", Colours.GREEN_3);
        player.message("Your mystery box and money awaits you in your bank!", Colours.GREEN_3);

        player.getBank().tab().add(new Item(6199));
        player.getBank().tab().add(new Item(995, 2000000));
        receivedReward = true;
    }

    public void checkFriend(Player friend) {
        boolean contains = false;
        for (String friendName : friends) {
            if(friendName != null) {
                if(friendName.equals(friend.getUsername())) {
                    contains = true;
                    break;
                }
            }
        }

        if(!contains) {
            for (int i = 0; i < friends.length; i++) {
                String friendName = friends[i];
                if(friendName == null) {
                    if(!player.getRights().isOrHigher(PlayerRights.OWNER) && friend.getLogger().getHardwareId().equals(player.getLogger().getHardwareId())) {
                        player.message(friend.getUsername() + " tried to refer you but we detected foul play!", Colours.RED);
                        player.message("If you believe this to be incorrect please contact a member of staff.", Colours.RED);
                        friend.message("Your referral was found to be invalid!", Colours.RED);
                        friend.message("If you believe this to be incorrect please contact a member of staff.", Colours.RED);
                        friend.getReferAFriend().setReferral(null);
                        PlayerLogs.log(player.getUsername(), "Invalid referral: " + player.getLogger().getHardwareId() + " " + friend.getLogger().getHardwareId() + " " + player.getLogger().getIpAddress() + " " + friend.getLogger().getIpAddress() + " " + player.getLogger().getMacAddress() + " " + friend.getLogger().getMacAddress());
                    } else {
                        friends[i] = friend.getUsername();
                        player.message(friend.getUsername() + " has been added to your refers list!", Colours.GREEN);
                        if(referredFriendsCount() <= 10) {
                            player.getPoints().add(Points.DONATED, 1);
                            player.getPoints().add(Points.DONOR, 100);
                            PlayerPanel.refreshPanel(player);
                        }
                        PlayerLogs.log(player.getUsername(), "Referred friend: " + friend.getUsername());
                    }
                    return;
                }
            }
            friend.getReferAFriend().setReferral(null);
            player.message(friend.getUsername() + " tried to refer you but your refers list is full!", Colours.ORANGE);
        }
    }

    public boolean hasReferral() {
        return referral != null;
    }

    public boolean hasReceivedReward() {
        return receivedReward;
    }

    private Player getReferrer() {
        return World.getPlayerByName(referral);
    }

    public boolean referralOnline() {
        return hasReferral() && getReferrer() != null;

    }

    public int referredFriendsCount() {
        int count = 0;
        for (String friendName : friends) {
            if (friendName != null)
                count++;
        }
        return count;
    }

    public int referredFriendsOnline() {
        int count = 0;
        for (String friendName : friends) {
            if (friendName != null) {
                Player friend = World.getPlayerByName(friendName);
                if (friend != null && friend.getReferAFriend().hasReceivedReward())
                    count++;
            }
        }
        return count >= 10 ? 10 : count;
    }


    public double getReferralBonusExperience() {
        int friends = referredFriendsOnline();

        if(friends != 0) {
            return 1.0 + (friends * 0.05);
        } else if(referralOnline()) {
            return 1.20;
        }

        return 1.0;
    }

    public void setReferral(String referral) {
        this.referral = referral;
    }

    public void setReceivedReward(boolean reward) {
        this.receivedReward = reward;
    }

    public String[] getFriends() {
        return friends;
    }

    public void setFriends(String[] friends) {
        this.friends = friends;
    }
}
