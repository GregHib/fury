package com.fury.game.system.communication.commands.impl.regular;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.system.communication.commands.Command;
import com.fury.game.system.mysql.EmailPromo;
import com.fury.util.Colours;

import java.util.regex.Pattern;

public class PromoCommand implements Command {

    static Pattern pattern = Pattern.compile("^promo\\s(.*)$");

    @Override
    public Pattern pattern() {
        return pattern;
    }

    @Override
    public String prefix() {
        return "promo ";
    }

    @Override
    public String format() {
        return "promo [code]";
    }

    @Override
    public void process(Player player, String... values) {
        String code = values[1];
        player.message("Checking promo code " + code + "...");
        try {
            if(code.contains("mb-") && player.getTotalPlayTime() <= 3600 * 1000){ //checks play time 1 hour
                player.message("You must play at least 1 hour before using this promo :)", Colours.RED);
                return;
            }

            if(code.contains("xp-") && player.getTotalPlayTime() <= 10800 * 1000){ //checks playtime 3 hours
                player.message("You must play at least 3 hours before using this promo :)", Colours.RED);
                return;
            }
            EmailPromo.check(player, code);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean rights(Player player) {
        return true;
    }
}
