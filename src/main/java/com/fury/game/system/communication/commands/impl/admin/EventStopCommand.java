package com.fury.game.system.communication.commands.impl.admin;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.content.events.daily.DailyEvent;
import com.fury.game.content.events.daily.DailyEventManager;
import com.fury.game.content.events.daily.DailyEventType;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.system.communication.commands.Command;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Pattern;

public class EventStopCommand implements Command {

    private static Pattern pattern = Pattern.compile("^event stop\\s(.*)$");

    @Override
    public Pattern pattern() {
        return pattern;
    }

    @Override
    public String prefix() {
        return "event stop ";
    }

    @Override
    public String format() {
        return "event start [name]";
    }

    @Override
    public void process(Player player, String... values) {
        String name = values[1];

        DailyEventType type = DailyEventType.getType(name.toLowerCase());

        if (type != null) {
            ListIterator<DailyEvent> it = DailyEventManager.events().listIterator();

            List<DailyEvent> toRemove = new ArrayList<>();

            while(it.hasNext()) {
                DailyEvent event = it.next();
                if (event.getEventType() == type)
                    toRemove.add(event);
            }

            toRemove.forEach(DailyEventManager::removeEvent);
        } else {
            player.message("Could not find event '" + name + "'");
        }
    }

    @Override
    public boolean rights(Player player) {
        return player.getRights().isOrHigher(PlayerRights.ADMINISTRATOR);
    }
}
