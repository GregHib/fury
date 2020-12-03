package com.fury.game.system.communication.commands.impl.admin;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.content.events.daily.DailyEventManager;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.system.communication.commands.Command;

import java.util.regex.Pattern;

public class EventClearCommand implements Command {

    @Override
    public Pattern pattern() {
        return null;
    }

    @Override
    public String prefix() {
        return "event clear";
    }

    @Override
    public String format() {
        return "event clear";
    }

    @Override
    public void process(Player player, String... values) {
        DailyEventManager.clear();
    }

    @Override
    public boolean rights(Player player) {
        return player.getRights().isOrHigher(PlayerRights.ADMINISTRATOR);
    }
}
