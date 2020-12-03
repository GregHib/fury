package com.fury.game.content.dialogue.impl.items;

import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.entity.character.player.info.PlayerRights;

public class ModPotatoExtraD extends Dialogue {
    @Override
    public void start() {
        player.getDialogueManager().sendOptionsDialogue("Mod Potato", "Invulnerable", "One hit punch");
    }

    @Override
    public void run(int optionId) {
        if(optionId == DialogueManager.OPTION_1) {
            if(player.getRights() == PlayerRights.ADMINISTRATOR && (player.isInWilderness() || player.isDueling())) {
                player.message("Invulnerability cannot be used here.");
            } else {
                player.setInvulnerable(!player.isInvulnerable());
                player.message("Invulnerability: " + (player.isInvulnerable() ? "Enabled" : "Disabled"));
            }
        } else if(optionId == DialogueManager.OPTION_2) {
            if(player.getRights() == PlayerRights.ADMINISTRATOR && (player.isInWilderness() || player.isDueling())) {
                player.message("Invulnerability cannot be used here.");
            } else {
                boolean insta = (Boolean) player.getTemporaryAttributes().getOrDefault("instakill", false);
                player.getTemporaryAttributes().put("instakill", !insta);
                player.message("Insta kill: " + (!insta ? "Enabled" : "Disabled"));
            }
        }
        end();
    }
}