package com.fury.game.system.communication.commands.impl.owner;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.system.communication.commands.Command;

import java.util.regex.Pattern;

public class FlyCommand implements Command {


    @Override
    public Pattern pattern() {
        return null;
    }

    @Override
    public String prefix() {
        return "fly";
    }

    @Override
    public String format() {
        return "fly";
    }

    @Override
    public void process(Player player, String... values) {
//        player.animate(3672);
//        player.animate(3673);
//        player.animate(3614);
//        player.animate(3482);
//        player.animate(3610);
//        player.animate(3480);
//        player.animate(3422);
//        player.animate(3421);
//        player.animate(3420);
//        player.animate(3419);
//        player.animate(3417);
//        player.animate(3416);
//        player.animate(3364);
//        player.animate(2829);
//        player.animate(2665);
//        player.animate(2664);
//        player.animate(2638);
//        player.animate(2633);
//        player.animate(3651);
    }

    @Override
    public boolean rights(Player player) {
        return player.getRights().isOrHigher(PlayerRights.OWNER);
    }
}
