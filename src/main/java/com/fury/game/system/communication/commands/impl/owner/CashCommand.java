package com.fury.game.system.communication.commands.impl.owner;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.system.communication.commands.Command;
import com.fury.util.Misc;

import java.util.regex.Pattern;

public class CashCommand implements Command {

    static Pattern pattern = Pattern.compile("^(?:cash|money)\\s(\\S+)$");

    @Override
    public Pattern pattern() {
        return pattern;
    }

    @Override
    public String prefix() {
        return null;
    }

    @Override
    public String format() {
        return "cash [amount]";
    }

    @Override
    public void process(Player player, String... values) {
        long amount = Misc.stringToAmount(values[1]);
        player.getMoneyPouch().setTotal(player.getMoneyPouch().getTotal() + amount);
        player.getPacketSender().sendString(8135, "" + player.getMoneyPouch().getTotal());
    }

    @Override
    public boolean rights(Player player) {
        return player.getRights().isOrHigher(PlayerRights.OWNER);
    }
}
