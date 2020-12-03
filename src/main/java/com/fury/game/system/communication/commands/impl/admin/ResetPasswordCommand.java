package com.fury.game.system.communication.commands.impl.admin;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.system.communication.commands.Command;
import com.fury.game.system.files.logs.PlayerLogs;
import com.fury.game.world.World;
import com.fury.network.security.PBKDF2;
import com.fury.tools.accounts.Utils.SearchUtils;
import com.fury.util.Misc;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Random;
import java.util.regex.Pattern;

public class ResetPasswordCommand implements Command {

    private static Pattern pattern = Pattern.compile("^reset\\spassword\\s(.*)");

    @Override
    public Pattern pattern() {
        return pattern;
    }

    @Override
    public String prefix() {
        return "reset password ";
    }

    @Override
    public String format() {
        return "reset password [username]";
    }

    @Override
    public void process(Player player, String... values) {
        String name = values[1];
        Player target = World.getPlayerByName(name);

        if(target == null) {
            target = SearchUtils.getPlayerFromName(name);
        } else {
            player.message("You cannot change a users password while they are online.");
            return;
        }

        if (target == null) {
            player.message("Error loading players file.");
            return;
        }

        String password = "pass" + generate(Misc.random(3, 5));
        String passwordHash = null;
        try {
            passwordHash = PBKDF2.generatePasswordHash(password);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }

        if(passwordHash != null) {
            target.setPasswordHash(passwordHash);
            player.message(name + "'s password has been reset to '" + password + "'.");
            player.message("Don't forget to tell the player to change it using the guide at home.");
            target.save(true);
            PlayerLogs.log(player.getUsername(), "Reset " + name + "'s password.");
        } else {
            player.message("An error occurred. Please try again.");
        }
    }

    @Override
    public boolean rights(Player player) {
        return player.getRights().isOrHigher(PlayerRights.ADMINISTRATOR);
    }


    private static String generate(int len) {
        char[] password = new char[len];
        Random rand = new Random(System.nanoTime());
        for (int i = 0; i < len; i++) {
            password[i] = chars[rand.nextInt(chars.length)];
        }
        return new String(password);
    }

    private static char[] chars = "0123456789abcdefghijklmnopqrstuvwxyz".toCharArray();
}
