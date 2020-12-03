package com.fury.game.system.communication.commands.impl.admin;

import com.fury.engine.task.executor.GameExecutorManager;
import com.fury.game.content.global.dnd.eviltree.EvilTree;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.system.communication.commands.Command;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class TreeCommand implements Command {
    @Override
    public Pattern pattern() {
        return null;
    }

    @Override
    public String prefix() {
        return "tree";
    }

    @Override
    public String format() {
        return null;
    }

    @Override
    public void process(Player player, String... values) {
        EvilTree.get().spawn();
    }

    @Override
    public boolean rights(Player player) {
        return player.getRights().isOrHigher(PlayerRights.ADMINISTRATOR);
    }
}
