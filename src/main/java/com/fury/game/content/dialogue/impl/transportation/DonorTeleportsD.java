package com.fury.game.content.dialogue.impl.transportation;

import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.entity.character.player.info.DonorStatus;
import com.fury.game.entity.character.player.link.transportation.TeleportHandler;
import com.fury.game.world.map.Position;
import com.fury.game.world.map.instance.BossInstanceHandler;
import com.fury.util.FontUtils;

public class DonorTeleportsD extends Dialogue {
    @Override
    public void start() {
        if (DonorStatus.isDonor(player, DonorStatus.RUBY_DONOR)) {
            player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Forinthry dungeon (" + FontUtils.RED + "L17" + FontUtils.COL_END + ")", "King black dragon", "Kalphite Queen Instance", "Corporeal Beast Instance", "Dagannoth Kings Instance");
        } else if (DonorStatus.isDonor(player, DonorStatus.EMERALD_DONOR)) {
            player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Forinthry dungeon (" + FontUtils.RED + "L17" + FontUtils.COL_END + ")", "King black dragon", "Kalphite Queen Instance");
        } else if (DonorStatus.isDonor(player, DonorStatus.SAPPHIRE_DONOR)) {
            player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Forinthry dungeon (" + FontUtils.RED + "L17" + FontUtils.COL_END + ")", "King black dragon");
        } else
            end();
    }

    @Override
    public void run(int optionId) {
        if(stage == -1) {
            if(optionId == DialogueManager.OPTION_1) {
                TeleportHandler.teleportPlayer(player, new Position(3077, 10058), player.getSpellbook().getTeleportType());
            } else if(optionId == DialogueManager.OPTION_2) {
                TeleportHandler.teleportPlayer(player, new Position(3069, 10257), player.getSpellbook().getTeleportType());
            } else if(optionId == DialogueManager.OPTION_3) {
                BossInstanceHandler.enter(player, BossInstanceHandler.BossInstanceType.KALPHITE_QUEEN);
            } else if(optionId == DialogueManager.OPTION_4) {
                BossInstanceHandler.enter(player, BossInstanceHandler.BossInstanceType.CORPOREAL_BEAST);
            } else if(optionId == DialogueManager.OPTION_5) {
                BossInstanceHandler.enter(player, BossInstanceHandler.BossInstanceType.DAGANNOTH_KINGS);
            }
        } else if(stage == 0) {
        }

    }
}