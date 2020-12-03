package com.fury.game.content.dialogue.impl.misc;

import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.entity.character.player.content.LoyaltyProgramme;
import com.fury.game.entity.character.player.info.GameMode;

import java.text.DecimalFormat;

/**
 * Created by Greg on 15/11/2016.
 */
public class GamemodeD extends Dialogue {
    @Override
    public void start() {
        DecimalFormat oneDigit = new DecimalFormat("#,##0.0");
        player.getDialogueManager().sendOptionsDialogue("Select a game mode", "Regular (cmb x" + GameMode.REGULAR.getCombatRate() + " skill x" + GameMode.REGULAR.getSkillRate() + " drop +" +  oneDigit.format((GameMode.REGULAR.getDropRate() - 1) * 100) + "%)",
                "Ironman (cmb x" + GameMode.IRONMAN.getCombatRate() + " skill x" + GameMode.IRONMAN.getSkillRate() + " drop +" + oneDigit.format((GameMode.IRONMAN.getDropRate() - 1) * 100) + "%)",
                "Extreme (cmb x" + GameMode.EXTREME.getCombatRate() + " skill x" + GameMode.EXTREME.getSkillRate() + " drop +" +  oneDigit.format((GameMode.EXTREME.getDropRate() - 1) * 100) + "%)",
                "Legend (cmb x" + GameMode.LEGEND.getCombatRate() + " skill x" + GameMode.LEGEND.getSkillRate() + " drop +" +  oneDigit.format((GameMode.LEGEND.getDropRate() - 1) * 100) + "%)");
    }

    @Override
    public void run(int optionId) {
        if(stage == -1) {
            if (optionId == DialogueManager.OPTION_1) {
                player.setGameMode(GameMode.REGULAR);
                LoyaltyProgramme.unlock(player, LoyaltyProgramme.LoyaltyTitles.SIR);
                LoyaltyProgramme.unlock(player, LoyaltyProgramme.LoyaltyTitles.LADY);
                LoyaltyProgramme.unlock(player, LoyaltyProgramme.LoyaltyTitles.DUKE);
                LoyaltyProgramme.unlock(player, LoyaltyProgramme.LoyaltyTitles.DUCHESS);
            } else if (optionId == DialogueManager.OPTION_2) {
                player.setGameMode(GameMode.IRONMAN);
                LoyaltyProgramme.unlock(player, LoyaltyProgramme.LoyaltyTitles.LORD);
            } else if (optionId == DialogueManager.OPTION_3) {
                player.setGameMode(GameMode.EXTREME);
                LoyaltyProgramme.unlock(player, LoyaltyProgramme.LoyaltyTitles.BARON);
                LoyaltyProgramme.unlock(player, LoyaltyProgramme.LoyaltyTitles.BARONESS);
            } else if (optionId == DialogueManager.OPTION_4) {
                player.setGameMode(GameMode.LEGEND);
                LoyaltyProgramme.unlock(player, LoyaltyProgramme.LoyaltyTitles.BARON);
                LoyaltyProgramme.unlock(player, LoyaltyProgramme.LoyaltyTitles.BARONESS);
            }
            end();
            player.getAppearance().openInterface();
        }
    }

}