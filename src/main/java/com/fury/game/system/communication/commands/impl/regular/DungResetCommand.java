package com.fury.game.system.communication.commands.impl.regular;

import com.fury.game.content.dialogue.impl.skills.dungeoneering.PrestigeReset;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.system.communication.commands.Command;

import java.util.regex.Pattern;

public class DungResetCommand implements Command {
    @Override
    public Pattern pattern() {
        return null;
    }

    @Override
    public String prefix() {
        return "dungreset";
    }

    @Override
    public String format() {
        return null;
    }

    @Override
    public void process(Player player, String... values) {
        player.getDialogueManager().startDialogue(new PrestigeReset());
    }

    @Override
    public boolean rights(Player player) {
        return true;
    }
}
