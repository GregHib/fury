package com.fury.game.system.communication.commands.impl.owner;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.system.communication.commands.Command;
import com.fury.game.world.World;

import java.util.regex.Pattern;

public class RightsCommand implements Command {

    static Pattern pattern = Pattern.compile("^(?:rights|promote)\\s(.*)\\s(\\S+)$");

    @Override
    public Pattern pattern() {
        return pattern;
    }

    @Override
    public String prefix() {
        return "rights ";
    }

    @Override
    public String format() {
        return "rights [username] [right]";
    }

    @Override
    public void process(Player player, String... values) {
        String name = values[1];
        String right = values[2];
        PlayerRights rights = null;
        switch (right.toLowerCase()) {
            case "player":
                rights = PlayerRights.PLAYER;
                break;
            case "owner":
                rights = PlayerRights.OWNER;
                break;
            case "dev":
            case "developer":
                rights = PlayerRights.DEVELOPER;
                break;
            case "admin":
            case "administrator":
                rights = PlayerRights.ADMINISTRATOR;
                break;
            case "mod":
            case "moderator":
                rights = PlayerRights.MODERATOR;
                break;
            case "support":
                rights = PlayerRights.SUPPORT;
                break;
            case "cm":
            case "com":
            case "com_man":
            case "community_manager":
            case "manager":
                rights = PlayerRights.COMMUNITY_MANAGER;
                break;
            case "vet":
            case "veteran":
                rights = PlayerRights.VETERAN;
                break;
            case "leg":
            case "legend":
                rights = PlayerRights.LEGEND;
                break;
            case "yt":
            case "youtube":
            case "youtuber":
                rights = PlayerRights.YOUTUBER;
                break;
            case "des":
            case "designer":
                rights = PlayerRights.DESIGNER;
                break;
        }
        if(rights == null) {
            player.message("Invalid right '" + right + "'.");
            player.message("Valid rights: 'player', 'dev', 'mod', 'admin', 'support', 'owner'...");
            player.message("'manager', 'veteran', 'legend', 'youtuber', 'designer'.");
            return;
        }

        Player target = World.getPlayerByName(name);

        if (target == null) {
            player.message("Could not find player '" + name + "' online.");
            return;
        }

        target.setRights(rights);
        target.message("Your player rights have been changed.");
        target.getPacketSender().sendRights();
    }

    @Override
    public boolean rights(Player player) {
        if(player.getRights().isOrHigher(PlayerRights.OWNER))
            return true;
        return false;
    }
}
