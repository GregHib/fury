package com.fury.game.system.communication.commands.impl.admin;

import com.fury.game.content.events.daily.DailyEventType;
import com.fury.game.content.global.ListWidget;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.system.communication.commands.Command;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class EventListCommand implements Command {

    @Override
    public Pattern pattern() {
        return null;
    }

    @Override
    public String prefix() {
        return "event list";
    }

    @Override
    public String format() {
        return null;
    }

    @Override
    public void process(Player player, String... values) {
        List<String> list = new ArrayList<>();
        for(DailyEventType type : DailyEventType.values())
            list.add(type.getTypeName() + " - " + type.getDescription());
        ListWidget.display(player, "Daily Events List", "Event name - Description", list.toArray(new String[list.size()]));
    }

    @Override
    public boolean rights(Player player) {
        return player.getRights().isOrHigher(PlayerRights.ADMINISTRATOR);
    }
}
