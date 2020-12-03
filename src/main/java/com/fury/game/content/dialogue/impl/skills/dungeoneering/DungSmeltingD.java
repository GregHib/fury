package com.fury.game.content.dialogue.impl.skills.dungeoneering;

import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.dialogue.SkillDialogue;
import com.fury.game.content.skill.free.smithing.Smelting;
import com.fury.game.content.skill.free.smithing.SmeltingData;
import com.fury.core.model.item.Item;
import com.fury.util.Misc;

/**
 * Created by Greg on 18/11/2016.
 */
public class DungSmeltingD extends Dialogue {
    Item[] bars = null;

    @Override
    public void start() {
        if(parameters.length > 0) {
            SmeltingData bar = (SmeltingData) parameters[0];
            stage = 2;
            bars = new Item[] { bar.getBar() };
            SkillDialogue.sendSkillDialogue(player, "What would you like to smith?", bars, new String[] { Misc.uppercaseFirst(bar.name().split("_")[0]) }, -30, -10, 150);
        } else
            player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Low level bars.", "High level bars.");
    }

    @Override
    public void run(int optionId) {
        if(stage == -1) {
            if(optionId == DialogueManager.OPTION_1 || optionId == DialogueManager.OPTION_2) {
                int start = optionId == DialogueManager.OPTION_2 ? 13 : 8;
                stage = optionId == DialogueManager.OPTION_2 ? 1 : 0;
                String[] names = new String[5];
                bars = new Item[5];
                for(int i = 0; i < 5; i++) {
                    SmeltingData data = SmeltingData.values()[start + i];
                    bars[i] = data.getBar();
                    names[i] = Misc.uppercaseFirst(data.name().split("_")[0]);
                }
                SkillDialogue.sendSkillDialogue(player, "What would you like to smith?", bars, names, -30, -10, 150);
            }
        } else if(stage == 0 || stage == 1 || stage == 2) {
            if(parameters.length > 0 && parameters[0] instanceof Integer) {
                int amount = (int) parameters[0];
                Smelting.smeltBar(player, bars[optionId], amount);
            }
            end();
        }
    }
}