package com.fury.game.system.communication.commands.impl.regular;

import com.fury.game.content.global.ListWidget;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.system.communication.commands.Command;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ReferralsCommand implements Command {
    @Override
    public Pattern pattern() {
        return null;
    }

    @Override
    public String prefix() {
        return "referrals";
    }

    @Override
    public String format() {
        return null;
    }

    @Override
    public void process(Player player, String... values) {

        List<String> lines = new ArrayList<>();
        lines.add("You were referred by: " + (player.getReferAFriend().hasReferral() ? player.getReferAFriend().getReferral() : "No one"));
        lines.add("Friends you've referred:");
        for(String friend : player.getReferAFriend().getFriends())
            if(friend != null)
                lines.add(friend);
        lines.add("");
        int exp = (int) Math.round((player.getReferAFriend().getReferralBonusExperience() - 1) * 100);
        lines.add("Current bonus experience: + " + exp + "%");
        ListWidget.display(player, "Refer A Friend", "", lines.toArray(new String[lines.size()]));
    }

    @Override
    public boolean rights(Player player) {
        return true;
    }
}
