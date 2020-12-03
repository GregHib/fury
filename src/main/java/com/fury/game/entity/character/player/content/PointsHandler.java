package com.fury.game.entity.character.player.content;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.node.entity.actor.figure.player.Points;
import com.fury.util.FontUtils;
import com.fury.util.Colours;

public class PointsHandler {

    private Player player;

    public PointsHandler(Player player) {
        this.player = player;
    }

    public void reset() {

//        commendations = (int) (loyaltyPoints = votingPoints = slayerPoints = pkPoints = 0);
        player.getPlayerKillingAttributes().setPlayerKillStreak(0);
        player.getPlayerKillingAttributes().setPlayerKills(0);
        player.getPlayerKillingAttributes().setPlayerDeaths(0);
        player.getDueling().arenaStats[0] = player.getDueling().arenaStats[1] = 0;
    }

    public PointsHandler refreshPanel() {
        String mins = "";
        switch (player.getMinutesBonusExp()) {
            case -1:
                mins = "0 minutes";
                break;
            case 1:
                mins = "1 minute";
                break;
            default:
                mins = player.getMinutesBonusExp() + " mins";
                break;
        }
        player.getPacketSender().sendString(39182, "Bonus Exp: " + FontUtils.YELLOW + mins + FontUtils.COL_END, Colours.ORANGE_2);
        player.getPacketSender().sendString(39183, "Skill Points: " + FontUtils.YELLOW + player.getPoints().getInt(Points.STRANGE_SKILL)+ FontUtils.COL_END, Colours.ORANGE_2);
        player.getPacketSender().sendString(39184, "Commendations: " + FontUtils.YELLOW + player.getPoints().getInt(Points.COMMENDATIONS) + FontUtils.COL_END, Colours.ORANGE_2);
        player.getPacketSender().sendString(39185, "Loyalty Points: " + FontUtils.YELLOW + player.getPoints().getInt(Points.LOYALTY) + FontUtils.COL_END, Colours.ORANGE_2);
        player.getPacketSender().sendString(39186, "Dung. Tokens: " + FontUtils.YELLOW + player.getDungManager().getTokens() + FontUtils.COL_END, Colours.ORANGE_2);
        player.getPacketSender().sendString(39187, "Voting Points: " + FontUtils.YELLOW + player.getPoints().getInt(Points.VOTING) + FontUtils.COL_END, Colours.ORANGE_2);
        player.getPacketSender().sendString(39188, "Slayer Points: " + FontUtils.YELLOW + player.getPoints().getInt(Points.SLAYER) + FontUtils.COL_END, Colours.ORANGE_2);
        player.getPacketSender().sendString(39189, "Pk Points: " + FontUtils.YELLOW + player.getPoints().getInt(Points.PK) + FontUtils.COL_END, Colours.ORANGE_2);
        player.getPacketSender().sendString(39190, "Wilderness Killstreak: " + FontUtils.YELLOW + player.getPlayerKillingAttributes().getPlayerKillStreak() + FontUtils.COL_END, Colours.ORANGE_2);
        player.getPacketSender().sendString(39191, "Wilderness Kills: " + FontUtils.YELLOW + player.getPlayerKillingAttributes().getPlayerKills() + FontUtils.COL_END, Colours.ORANGE_2);
        player.getPacketSender().sendString(39192, "Wilderness Deaths: " + FontUtils.YELLOW + player.getPlayerKillingAttributes().getPlayerDeaths() + FontUtils.COL_END, Colours.ORANGE_2);
        player.getPacketSender().sendString(39193, "Arena Victories: " + FontUtils.YELLOW + player.getDueling().arenaStats[0] + FontUtils.COL_END, Colours.ORANGE_2);
        player.getPacketSender().sendString(39194, "Arena Losses: " + FontUtils.YELLOW + player.getDueling().arenaStats[1] + FontUtils.COL_END, Colours.ORANGE_2);
        return this;
    }
}
