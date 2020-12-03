package com.fury.game.content.dialogue.impl.skills.crafting;

import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.dialogue.SkillDialogue;
import com.fury.game.content.skill.free.crafting.leather.LeatherCrafting;
import com.fury.game.content.skill.free.crafting.leather.LeatherData;
import com.fury.game.content.skill.free.crafting.leather.LeatherDialogueData;
import com.fury.core.model.item.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Greg on 17/11/2016.
 */
public class MakeLeatherD extends Dialogue {
    private Item[] otherValues = null;

    @Override
    public void start() {
        int leather = (int) parameters[0];
        if(leather == 1743) {
            SkillDialogue.sendSkillDialogue(player, SkillDialogue.DEFAULT_SKILLS_TITLE, new Item[]{new Item(1131)});
            stage = 0;
        } else if(leather == 6289) {
            String[] names = (String[]) parameters[1];
            SkillDialogue.sendSkillDialogue(player, SkillDialogue.DEFAULT_SKILLS_TITLE, new Item[]{new Item(6322), new Item(6324), new Item(6326), new Item(6328), new Item(6330)}, names);
            stage = 1;
        } else if(leather == 24372) {
            String[] names = (String[]) parameters[1];
            SkillDialogue.sendSkillDialogue(player, SkillDialogue.DEFAULT_SKILLS_TITLE, new Item[]{new Item(24382), new Item(24379), new Item(24388), new Item(24376)}, names);
            stage = -1;
        } else {
            LeatherDialogueData d = (LeatherDialogueData) parameters[1];
            String[] names = (String[]) parameters[2];
            otherValues = new Item[]{new Item(d.getVamb()), new Item(d.getChaps()), new Item(d.getBody())};

            SkillDialogue.sendSkillDialogue(player, SkillDialogue.DEFAULT_SKILLS_TITLE, otherValues, names);
            stage = 2;
        }
    }

    @Override
    public void run(int optionId) {
        int amount = (int) parameters[0];
        LeatherData data = null;
        if(stage == -1) {
            int[] royalDhide = new int[]{24382, 24379, 24388, 24376};
            data = LeatherData.forId(royalDhide[optionId]);
        } else if(stage == 0) {
            data = LeatherData.forId(1131);
        } else if(stage == 1) {
            int[] snakeskin = new int[]{6322, 6324, 6326, 6328, 6330};
            data = LeatherData.forId(snakeskin[optionId]);
        } else if(stage == 2 && otherValues != null) {
            data = LeatherData.forId(otherValues[optionId].getId());
        }
        if(data != null)
            if (player.getSelectedSkillingItem().isEqual(data.getLeather())) {
                player.getActionManager().setAction(new LeatherCrafting(data, amount, false));
                end();
            }

    }
}