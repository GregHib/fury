package com.fury.game.system.communication.commands.impl.admin;

import com.fury.game.entity.character.combat.equipment.weapon.CombatSpecial;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.node.entity.actor.figure.player.handles.Settings;
import com.fury.game.system.communication.commands.Command;

import java.util.regex.Pattern;

public class SpecCommand implements Command {
    @Override
    public Pattern pattern() {
        return null;
    }

    @Override
    public String prefix() {
        return "spec";
    }

    @Override
    public String format() {
        return null;
    }

    @Override
    public void process(Player player, String... values) {
        if(player.isInWilderness()) {
            player.message("You can't spec restore in the wilderness!");
            return;
        }

        player.getSettings().set(Settings.SPECIAL_ENERGY, 100);
        CombatSpecial.updateBar(player);
    }

    @Override
    public boolean rights(Player player) {
        return player.getRights().isOrHigher(PlayerRights.ADMINISTRATOR);
    }
}
