package com.fury.game.system.communication.commands.impl.owner;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.combat.equipment.ItemBonuses;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.system.communication.commands.Command;

import java.util.regex.Pattern;

public class ReloadItemBonusesCommand implements Command {

    @Override
    public Pattern pattern() {
        return null;
    }

    @Override
    public String prefix() {
        return "reload item bonuses";
    }

    @Override
    public String format() {
        return null;
    }

    @Override
    public void process(Player player, String... values) {
        ItemBonuses.reload();
        player.message("Item bonuses reloaded.");
    }

    @Override
    public boolean rights(Player player) {
        return player.getRights().isOrHigher(PlayerRights.OWNER);
    }
}
