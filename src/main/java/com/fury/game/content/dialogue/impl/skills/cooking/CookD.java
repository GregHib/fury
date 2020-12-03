package com.fury.game.content.dialogue.impl.skills.cooking;

import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.dialogue.SkillDialogue;
import com.fury.game.content.skill.free.cooking.Cooking;
import com.fury.core.model.item.Item;

/**
 * Created by Greg on 17/11/2016.
 */
public class CookD extends Dialogue {
    @Override
    public void start() {
        int itemId = (int) parameters[0];
        SkillDialogue.sendSkillDialogue(player, "How many would you like to cook?", new Item[] {new Item(itemId)});
    }

    @Override
    public void run(int optionId) {
        int amount = (int) parameters[0];
        if(player.getSelectedSkillingItem() != null && player.getSelectedSkillingItem().getId() > 0)
            Cooking.cook(player, player.getSelectedSkillingItem().getId(), amount);
        end();
    }
}
