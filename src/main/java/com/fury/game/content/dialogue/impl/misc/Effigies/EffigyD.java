package com.fury.game.content.dialogue.impl.misc.Effigies;

import com.fury.game.entity.character.player.content.Effigies;
import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.util.Misc;

/**
 * Created by Greg on 04/12/2016.
 */
public class EffigyD extends Dialogue {
    private int effigy;

    @Override
    public void start() {
        effigy = (int) parameters[0];
        player.getDialogueManager().sendItemDialogue(effigy, "You clean off the dust off of the Ancient effigy.",
                "The relic begins to make some sort of weird noises.",
                "I think there may be something inside here.");
    }

    @Override
    public void run(int optionId) {
        if (stage == -1) {
            String[] lines = new String[3];
            switch (effigy) {
                case 18778:
                    lines = new String[]{"This will require at least a level of 91 in one of the two", "skills to investigate. After investigation it becomes nourished,", "rewarding 15,000 experience in the skill used."};
                    break;
                case 18779:
                    lines = new String[]{"This will require at least a level of 93 in one of the two", "skills to investigate. After investigation it becomes sated,", "rewarding 30,000 experience in the skill used."};
                    break;
                case 18780:
                    lines = new String[]{"This will require at least a level of 95 in one of the two", "skills to investigate. After investigation it becomes gordged,", "rewarding 45,000 experience in the skill used."};
                    break;
                case 18781:
                    lines = new String[]{"This will require at least a level of 97 in one of the two", "skills to investigate. After investigation it provides 60,000 ", "experience in the skill used and, then crumbles to dust,", "leaving behind a dragonkin lamp."};
                    break;
            }
            player.getDialogueManager().sendItemDialogue(effigy, lines);
            stage = 0;
        } else if (stage == 0) {
            int r = 1 + Misc.getRandom(6);
            boolean newEffigy = player.getEffigy() == 0;
            if (!newEffigy)
                r = player.getEffigy();
            else
                player.setEffigy(r);
            switch (r) {
                case 1:
                    player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Crafting", "Agility");
                    stage = 1;
                    break;
                case 2:
                    player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Runecrafting", "Thieving");
                    stage = 2;
                    player.setDialogueActionId(50);
                    break;
                case 3:
                    player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Cooking", "Firemaking");
                    stage = 3;
                    player.setDialogueActionId(51);
                    break;
                case 4:
                    player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Farming", "Fishing");
                    stage = 4;
                    player.setDialogueActionId(52);
                    break;
                case 5:
                    player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Fletching", "Woodcutting");
                    stage = 5;
                    player.setDialogueActionId(53);
                    break;
                case 6:
                    player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Herblore", "Prayer");
                    stage = 6;
                    player.setDialogueActionId(54);
                    break;
                case 7:
                    player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Smithing", "Mining");
                    stage = 7;
                    player.setDialogueActionId(55);
                    break;
            }
        } else if (stage == 1) {
            end();
            if (optionId == DialogueManager.OPTION_1)
                Effigies.openEffigy(player, 12);
            else if (optionId == DialogueManager.OPTION_2)
                Effigies.openEffigy(player, 16);
        } else if (stage == 2) {
            end();
            if (optionId == DialogueManager.OPTION_1)
                Effigies.openEffigy(player, 20);
            else if (optionId == DialogueManager.OPTION_2)
                Effigies.openEffigy(player, 17);
        } else if (stage == 3) {
            end();
            if (optionId == DialogueManager.OPTION_1)
                Effigies.openEffigy(player, 7);
            else if (optionId == DialogueManager.OPTION_2)
                Effigies.openEffigy(player, 11);
        } else if (stage == 4) {
            end();
            if (optionId == DialogueManager.OPTION_1)
                Effigies.openEffigy(player, 19);
            else if (optionId == DialogueManager.OPTION_2)
                Effigies.openEffigy(player, 10);
        } else if (stage == 5) {
            end();
            if (optionId == DialogueManager.OPTION_1)
                Effigies.openEffigy(player, 9);
            else if (optionId == DialogueManager.OPTION_2)
                Effigies.openEffigy(player, 8);
        } else if (stage == 6) {
            end();
            if (optionId == DialogueManager.OPTION_1)
                Effigies.openEffigy(player, 15);
            else if (optionId == DialogueManager.OPTION_2)
                Effigies.openEffigy(player, 5);
        } else if (stage == 7) {
            end();
            if (optionId == DialogueManager.OPTION_1)
                Effigies.openEffigy(player, 13);
            else if (optionId == DialogueManager.OPTION_2)
                Effigies.openEffigy(player, 14);
        }
    }
}