package com.fury.game.content.dialogue.impl.npcs;

import com.fury.game.GameSettings;
import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.entity.character.player.link.transportation.TeleportHandler;
import com.fury.game.system.mysql.impl.DonationStore;
import com.fury.game.world.map.Position;
import com.fury.util.Misc;

/**
 * Created by Greg on 14/11/2016.
 */
public class KingArthurD extends Dialogue {
    @Override
    public void start() {
        player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "I'd like to claim my purchased items", "I'd like to know how many funds I have in total", "I'd like to teleport to the Member's Zone", "I'd like to learn more about Membership", "Cancel");
    }

    @Override
    public void run(int optionId) {
        if(stage == -1) {
            if(optionId == DialogueManager.OPTION_1) {
                end();
                DonationStore.checkDonation(player);
            } else if(optionId == DialogueManager.OPTION_2) {
                player.getDialogueManager().startDialogue(new KingArthurDonatedD());
            } else if(optionId == DialogueManager.OPTION_3) {
                end();
                if (!player.isDonor()) {
                    player.message("You need to be a donor to teleport to this zone.");
                    player.message("To become a donor, use the command ::store and browse our store.");
                    return;
                }
                TeleportHandler.teleportPlayer(player, new Position(3186 + Misc.random(2), 5721 + Misc.random(4)), player.getSpellbook().getTeleportType());
            } else if(optionId == DialogueManager.OPTION_4) {
                player.getPacketSender().sendUrl(GameSettings.WEBSITE + "/forums/index.php?/topic/38-how-to-donate-on-fury/");
            } else {
                end();
            }
        } else if(stage == 0) {
        }
    }
}
