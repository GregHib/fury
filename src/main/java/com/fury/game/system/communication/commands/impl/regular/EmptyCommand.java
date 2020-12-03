package com.fury.game.system.communication.commands.impl.regular;

import com.fury.game.content.dialogue.impl.misc.EmptyInventoryD;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.system.communication.commands.Command;

import java.util.regex.Pattern;

public class EmptyCommand implements Command {
    @Override
    public Pattern pattern() {
        return null;
    }

    @Override
    public String prefix() {
        return "empty";
    }

    @Override
    public String format() {
        return null;
    }

    @Override
    public void process(Player player, String... values) {
        player.getDialogueManager().startDialogue(new EmptyInventoryD());
    }

    @Override
    public boolean rights(Player player) {
        return true;
    }
}
