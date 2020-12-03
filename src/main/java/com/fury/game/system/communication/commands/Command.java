package com.fury.game.system.communication.commands;

import com.fury.core.model.node.entity.actor.figure.player.Player;

import java.util.regex.Pattern;

public interface Command {

//    boolean matches(String text);

    Pattern pattern();

    String prefix();

    String format();

    void process(Player player, String... values);

    boolean rights(Player player);
}
