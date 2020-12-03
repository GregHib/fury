package com.fury.game.system.communication.commands.impl.donor;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.GameSettings;
import com.fury.game.entity.character.player.info.DonorStatus;
import com.fury.game.entity.character.player.info.GameMode;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.system.communication.commands.Command;
import com.fury.game.system.communication.punishment.Punishment;
import com.fury.game.world.GameWorld;
import com.fury.util.Colours;
import com.fury.util.FontUtils;
import com.fury.util.Misc;

import java.util.regex.Pattern;

public class YellCommand implements Command {

    static Pattern pattern = Pattern.compile("^yell\\s(.*)");

    @Override
    public Pattern pattern() {
        return pattern;
    }

    @Override
    public String prefix() {
        return "yell ";
    }

    @Override
    public String format() {
        return "yell [message]";
    }

    @Override
    public void process(Player player, String... values) {
        if (!player.isDonor() && !player.getRights().isStaff()) {
            player.message("Only donors can yell. To become one, claim a donor scroll.");
            player.message("Scrolls can be bought from other players or from buying items in the ::store");
            return;
        }

        if (Punishment.isMuted(player) || Punishment.isHardwareMuted(player)) {
            player.message("You are muted and cannot yell.");
            return;
        }

        if (!GameSettings.YELL_ACTIVE) {
            player.message("Yell has been temporarily disabled.");
            return;
        }

        int delay = player.getYellDelay();
        if (!player.getTimers().getYell().elapsed((delay * 1000))) {
            player.message("You must wait at least " + delay + " seconds between every yell-message you send.");
            return;
        }

        String yellMessage = values[1];

        if (Misc.blockedWord(yellMessage)) {
            player.getDialogueManager().sendStatement("A word was blocked in your sentence. Please do not repeat it!");
            return;
        }

        int icon = 535;
        if(player.getRights() == PlayerRights.PLAYER) {
            if(player.getGameMode() == GameMode.IRONMAN) {
                icon = 541;
            } else if(player.isDonor()) {
                icon = 1703 + DonorStatus.get(player).ordinal();
            }
        } else {
            icon = player.getRightsId() + 530;
        }

        boolean onyx = DonorStatus.isDonor(player, DonorStatus.ONYX_DONOR);
        String message = FontUtils.imageTags(icon) + "["  + (onyx ? "<shad>" : "") + FontUtils.colourTags(player.getYellColour()) + player.getYellName() + (onyx ? "</shad>" : "") +  "</col>" + "] " + player.getUsername() + ": " + yellMessage.trim().toUpperCase().charAt(0) + yellMessage.trim().substring(1);

        GameWorld.sendBroadcast(message, Colours.BLACK);
        player.getTimers().getYell().reset();
    }

    @Override
    public boolean rights(Player player) {
        return true;
    }
}
