package com.fury.game.system.communication.commands.impl.dev;

import com.fury.core.model.item.Item;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.container.impl.bank.BankTab;
import com.fury.game.content.dialogue.impl.misc.EmptyInventoryD;
import com.fury.game.system.communication.commands.Command;

import java.util.regex.Pattern;

public class EmptyBankCommand implements Command {
    @Override
    public Pattern pattern() {
        return null;
    }

    @Override
    public String prefix() {
        return "emptybank";
    }

    @Override
    public String format() {
        return null;
    }

    @Override
    public void process(Player player, String... values) {
        BankTab tab = player.getBank().tab();
        for(Item item : tab.getItems()) {
            tab.delete(item);
        }
    }

    @Override
    public boolean rights(Player player) {
        return true;
    }
}