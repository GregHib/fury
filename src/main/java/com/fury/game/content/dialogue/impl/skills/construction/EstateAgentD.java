package com.fury.game.content.dialogue.impl.skills.construction;

import com.fury.game.GameSettings;
import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.dialogue.impl.npcs.SimpleNpcMessageD;
import com.fury.game.content.skill.member.construction.HouseConstants;
import com.fury.util.Misc;

public class EstateAgentD extends Dialogue {

    private static final HouseConstants.POHLocation[] LOCATIONS = {
            HouseConstants.POHLocation.EDGEVILLE,
            HouseConstants.POHLocation.RIMMINGTON,
            HouseConstants.POHLocation.TAVERLEY,
            HouseConstants.POHLocation.POLLNIVNEACH,
            HouseConstants.POHLocation.RELLEKKE,
            HouseConstants.POHLocation.BRIMHAVEN,
            HouseConstants.POHLocation.YANILLE
    };
    private static final int[] REDECORATE_PRICE = {50000, 70000, 100000, 150000, 500000};
    private static final int[] REDECORATE_BUILDS = {
            3, 0,//basic wood
            1, 2,//whitewash
            2, 3,//fremmy wood
            3, 4,//tropical wood
            3, 6//zenviva dark
    };
    private int npcId;

    @Override
    public void start() {
        npcId = (int) parameters[0];
        player.getDialogueManager().sendNPCDialogue(npcId, Expressions.PLAIN_TALKING, "Hello welcome to " + GameSettings.NAME + "'s Housing Agency!", "What can I do you for?");
    }

    @Override
    public void run(int optionId) {
        if (stage == -1) {
            player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Can you move my house please?", "Can you redecorate my house please?");
            stage = 0;
        } else if (stage == 0) {
            if (optionId == DialogueManager.OPTION_1) {
                player.getDialogueManager().sendPlayerDialogue(Expressions.PLAIN_TALKING, "Can you move my house please?");
                stage = 1;
            } else if (optionId == DialogueManager.OPTION_2) {
                player.getDialogueManager().sendPlayerDialogue(Expressions.PLAIN_TALKING, "Can you redecorate my house please?");
                stage = 5;
            }
        } else if (stage == 1) {
            player.getDialogueManager().sendNPCDialogue(npcId, Expressions.PLAIN_TALKING, "Certainly. Where would you like it moved to?");
            stage = 2;
        } else if (stage == 2) {
            player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Edgeville (free)", "Rimmington (50k)", "Taverly (50k)", "Pollnivneach (75k)", "More");
            stage = 3;
        } else if (stage == 3) {
            if (optionId >= DialogueManager.OPTION_1 && optionId <= DialogueManager.OPTION_4) {
                moveHouse(LOCATIONS[optionId]);
            } else if (optionId == DialogueManager.OPTION_5) {
                player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Relleka (100k)", "Brimhaven (150k)", "Yannile (250k)", "Previous");
                stage = 4;
            }
        } else if (stage == 4) {
            if (optionId == DialogueManager.OPTION_1 || optionId == DialogueManager.OPTION_2 || optionId == DialogueManager.OPTION_3) {
                moveHouse(LOCATIONS[optionId == DialogueManager.OPTION_1 ? 4 : optionId == DialogueManager.OPTION_2 ? 5 : 6]);
            } else if (optionId == DialogueManager.OPTION_4) {
                player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Edgeville (free)", "Rimmington (50k)", "Taverly (50k)", "Pollnivneach (75k)", "More");
                stage = 3;
            }
        } else if (stage == 5) {
            player.getDialogueManager().sendNPCDialogue(npcId, Expressions.PLAIN_TALKING, "Certainly.", "I can redecorate the house in a completely new style!", "What style would you like?");
            stage = 6;
        } else if (stage == 6) {
            player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Basic wood (50k)", "Whitewashed stone (75k)", "Fremennik-style wood (100k)", "Tropical Wood (150k)", "Zeneviva's dark stone (500k)");
            stage = 7;
        } else if (stage == 7) {
            redecorateHouse(optionId);
        } else
            end();
    }


    private void redecorateHouse(int index) {
        int cost = REDECORATE_PRICE[index];
        if (!player.getInventory().hasCoins(cost)) {
            player.getDialogueManager().startDialogue(new SimpleNpcMessageD(), npcId, "You don't have enough capital to cover the costs. Please return when you do!");
            return;
        } else if (player.getHouse().getLook() == index) {
            player.getDialogueManager().startDialogue(new SimpleNpcMessageD(), npcId, "Your house is already in that style!");
            return;
        }
        if (player.getInventory().removeCoins(cost)) {
            player.getDialogueManager().startDialogue(new SimpleNpcMessageD(), npcId, "Your house has been redecorated.");
            player.getHouse().redecorateHouse(REDECORATE_BUILDS[(index * 2) + 1]);
        }
    }

    private void moveHouse(HouseConstants.POHLocation location) {
        int cost = location.getCost();
        String regionalName = Misc.formatPlayerNameForDisplay(location.toString().toLowerCase());

        if (!player.getInventory().hasCoins(cost)) {
            player.getDialogueManager().startDialogue(new SimpleNpcMessageD(), npcId, "You don't have enough capital to cover the costs. Please return when you do!");
            return;
        } else if (player.getHouse().getLocation() == location) {
            player.getDialogueManager().startDialogue(new SimpleNpcMessageD(), npcId, "Your home is already located in " + regionalName + "!");
            return;
        }
        if (player.getInventory().removeCoins(cost)) {
            player.getHouse().setLocation(location);
            player.getDialogueManager().startDialogue(new SimpleNpcMessageD(), npcId, "Your home has been moved to " + regionalName + "!");
        }
    }
}
