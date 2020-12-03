package com.fury.game.system.communication.commands.impl.admin;

import com.fury.game.content.skill.Skill;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.system.communication.commands.Command;

import java.util.regex.Pattern;

public class PrayerCommand implements Command {

    @Override
    public Pattern pattern() {
        return null;
    }

    @Override
    public String prefix() {
        return "prayer";
    }

    @Override
    public String format() {
        return null;
    }

    @Override
    public void process(Player player, String... values) {
        if(player.isInWilderness()) {
            player.message("You can't restore prayer in the wilderness!");
            return;
        }
        player.getSkills().restore(Skill.PRAYER);
    }

    @Override
    public boolean rights(Player player) {
        return player.getRights().isOrHigher(PlayerRights.ADMINISTRATOR);
    }
}
