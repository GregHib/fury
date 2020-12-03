package com.fury.game.system.communication.commands.impl.admin;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.system.communication.commands.Command;

import java.util.regex.Pattern;

public class BankCommand implements Command {
    @Override
    public Pattern pattern() {
        return null;
    }

    @Override
    public String prefix() {
        return "bank";
    }

    @Override
    public String format() {
        return null;
    }

    @Override
    public void process(Player player, String... values) {
        if (player.getInterfaceId() > 0) {
            player.message("Please close the interface you have open before opening another one.");
            return;
        }
        if (player.getRights() != PlayerRights.DEVELOPER && (player.isInWilderness() || player.isDueling())) {
            player.message("You cannot open your bank here.");
            return;
        }
        player.setCommandBanking(true);
        player.getBank().open();
    }

    @Override
    public boolean rights(Player player) {
        if(player.getRights().isOrHigher(PlayerRights.ADMINISTRATOR))
            return true;
        return false;
    }
}
