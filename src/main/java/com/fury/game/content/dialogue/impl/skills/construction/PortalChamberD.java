package com.fury.game.content.dialogue.impl.skills.construction;

import com.fury.cache.def.Loader;
import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.member.construction.House;
import com.fury.game.content.skill.member.construction.HouseConstants;
import com.fury.core.model.item.Item;

/**
 * Created by Jon on 2/9/2017.
 */
public class PortalChamberD extends Dialogue {

    private static final int[] LEVELS =
            {25, 31, 37, 45, 51, 58, 66};
    private static final double[] EXPERIENCE =
            {19, 41, 48, 55.5, 61, 68, 76};
    private static final String[] OPTION_NAMES =
            {"Nowhere", "Varrock", "Lumbridge", "Falador", "Camelot", "Ardougne", "Yanille", "Kharyll"};

    private House.RoomReference rRef;
    private boolean[] existingPortals;
    private int redirectSlot;

    @Override
    public void start() {
        existingPortals = new boolean[3];
        this.rRef = (House.RoomReference) this.parameters[0];
        for (House.ObjectReference oRef : rRef.getObjects()) {
            if (oRef.getBuild() == HouseConstants.Builds.PORTAL_1)
                existingPortals[0] = true;
            if (oRef.getBuild() == HouseConstants.Builds.PORTAL_2)
                existingPortals[1] = true;
            if (oRef.getBuild() == HouseConstants.Builds.PORTAL_3)
                existingPortals[2] = true;
        }
        player.getDialogueManager().sendDialogue("To direct a portal you need enough", "runes for 100 castings of that teleport spell.", "(Combination runes and staffs cannot be used.)");
    }

    private static final int[][] RUNES =
            {
                    {563, 100, 556, 300, 554, 100},
                    {563, 100, 556, 300, 557, 100},
                    {563, 100, 556, 300, 555, 100},
                    {563, 100, 556, 500},
                    {563, 200, 555, 200},
                    {563, 200, 557, 200},
                    {563, 200, 565, 200}
            };

    @Override
    public void run(int optionId) {
        if (stage == -1) {
            if (rRef.getDirectedPortals() == null)
                rRef.setDirectedPortals(new byte[3]);
            byte primaryFrame = rRef.getDirectedPortals()[0];
            byte secondaryFrame = rRef.getDirectedPortals()[1];
            byte tertiaryFrame = rRef.getDirectedPortals()[2];
            player.getDialogueManager().sendOptionsDialogue("Redirect which portal?", existingPortals[0] ? OPTION_NAMES[primaryFrame] : "No portal frame", existingPortals[1] ? OPTION_NAMES[secondaryFrame] : "No portal frame", existingPortals[2] ? OPTION_NAMES[tertiaryFrame] : "No portal frame", "Cancel");
            stage = 0;
        } else if (stage == 0) {
            if (optionId >= DialogueManager.OPTION_1 && optionId <= DialogueManager.OPTION_3) {
                redirectSlot = optionId == 0 ? 0 : optionId;
                if (!existingPortals[redirectSlot]) {
                    player.getDialogueManager().sendDialogue("There must be a portal frame in order for the centerpiece to specify a location.");
                    stage = 10;
                    return;
                }
                player.getDialogueManager().sendOptionsDialogue("Select a destination", "Varrock", "Lumbridge", "Falador", "Camelot", "More");
                stage = 1;
            } else if (optionId == DialogueManager.OPTION_4) {
                end();
            }
        } else if (stage == 1) {
            if (optionId >= DialogueManager.OPTION_1 && optionId <= DialogueManager.OPTION_4) {
                placePortal(optionId == 0 ? 0 : optionId);
            } else if (optionId == DialogueManager.OPTION_5) {
                player.getDialogueManager().sendOptionsDialogue("Select a destination", "Ardougne", "Yanille", "Kharyll", "Previous");
                stage = 2;
            }
        } else if (stage == 2) {
            if (optionId >= DialogueManager.OPTION_1 && optionId <= DialogueManager.OPTION_3) {
                placePortal(4 + (optionId == 0 ? 0 : optionId));
            } else if (optionId == DialogueManager.OPTION_4) {
                player.getDialogueManager().sendOptionsDialogue("Select a destination", "Varrock", "Lumbridge", "Falador", "Camelot", "More");
                stage = 1;
            }
        } else {
            end();
        }
    }

    private void placePortal(int index) {
        stage = 10;
        if (!player.getSkills().hasRequirement(Skill.MAGIC, LEVELS[index], "focus the portal to this location"))
            return;
        int runesCount = 0;
        int[] runes = RUNES[index];
        while (runesCount < runes.length) {
            int runeId = runes[runesCount++];
            int amount = runes[runesCount++];
            if (!player.getInventory().contains(new Item(runeId, amount))) {
                player.getDialogueManager().sendDialogue("You do not have enough " + Loader.getItem(runeId).name.replace("rune", "Rune") + "s to create this portal.");
                return;
            }
        }
        runesCount = 0;
        while (runesCount < runes.length) {
            int runeId = runes[runesCount++];
            int amount = runes[runesCount++];
            player.getInventory().delete(new Item(runeId, amount));
        }
        player.getSkills().addExperience(Skill.MAGIC, 5 * EXPERIENCE[index]);
        player.message("You redirected the portal. Turn build mode off to see it.", true);
        rRef.getDirectedPortals()[redirectSlot] = (byte) (index + 1);
        end();
    }
}
