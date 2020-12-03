package com.fury.game.system.communication.commands.impl.admin;

import com.fury.game.content.events.daily.DailyEvent;
import com.fury.game.content.events.daily.DailyEventFactory;
import com.fury.game.content.events.daily.DailyEventManager;
import com.fury.game.content.events.daily.DailyEventType;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.system.communication.commands.Command;

import java.util.regex.Pattern;

public class EventStartCommand implements Command {

    private static Pattern pattern = Pattern.compile("^event start\\s(.*)\\s(\\d+)$");

    @Override
    public Pattern pattern() {
        return pattern;
    }

    @Override
    public String prefix() {
        return "event start ";
    }

    @Override
    public String format() {
        return "event start [name] [hours]";
    }

    @Override
    public void process(Player player, String... values) {
        String name = values[1];
        int hours = Integer.valueOf(values[2]);

        DailyEventType type = DailyEventType.getType(name);
        if (type != null) {
            DailyEvent event = new DailyEventFactory(type).build();
            DailyEventManager.timedEvent(event, hours);
        } else {
            player.message("Could not find event '" + name + "'");
        }
    }

    @Override
    public boolean rights(Player player) {
        return player.getRights().isOrHigher(PlayerRights.ADMINISTRATOR);
    }
}
