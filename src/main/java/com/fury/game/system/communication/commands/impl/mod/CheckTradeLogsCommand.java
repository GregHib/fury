package com.fury.game.system.communication.commands.impl.mod;

import com.fury.game.content.global.ListWidget;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.system.communication.commands.Command;
import com.fury.game.system.files.logs.LoggedPlayerItem;
import com.fury.game.world.World;
import com.fury.tools.accounts.Utils.SearchUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class CheckTradeLogsCommand implements Command {

    private static Pattern pattern = Pattern.compile("^check\\strade\\slogs\\s(.*)");

    @Override
    public Pattern pattern() {
        return pattern;
    }

    @Override
    public String prefix() {
        return "check trade logs ";
    }

    @Override
    public String format() {
        return "check trade logs [username]";
    }

    @Override
    public void process(Player player, String... values) {
        String name = values[1];
        Player target = World.getPlayerByName(name);

        if (target == null) {
            player.message("'" + name + "' is not currently online.");
            target = SearchUtils.getPlayerFromName(name);
        }

        if(target == null) {
            player.message("Error loading that players file.");
            return;
        }

        List<String> list = new ArrayList<>();
        for(LoggedPlayerItem item : target.getLogger().getTradedItems())
            list.add(item.getTime() + " " + (item.wasReceived() ? "Received" : "Gave") + " " + item.getName() + (player.getRights().isOrHigher(PlayerRights.DEVELOPER) ? " (" + item.getId() + ")" : "") + " " + item.getAmount() + " to " + item.getUsername());

        ListWidget.display(player, "Item Trade Logs: " + target.getUsername(), "", list.toArray(new String[list.size()]));
    }

    @Override
    public boolean rights(Player player) {
        return player.getRights().isOrHigher(PlayerRights.MODERATOR);
    }
}
