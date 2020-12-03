package com.fury.game.content.dialogue.impl.skills.crafting;

import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.dialogue.SkillDialogue;
import com.fury.game.content.skill.free.crafting.gems.Gem;
import com.fury.game.content.skill.free.crafting.gems.GemCutting;
import com.fury.core.model.item.Item;

/**
 * Created by Greg on 17/11/2016.
 */
public class GemCuttingD extends Dialogue {
    Gem gem;
    @Override
    public void start() {
        gem = (Gem) parameters[0];
        Item[] items = new Item[] {gem.getCut()};
        SkillDialogue.sendSkillDialogue(player, "How many do you wish to cut?", items, SkillDialogue.getNames(items), 0, 10, 180);
    }

    @Override
    public void run(int optionId) {
        if(parameters.length > 1) {
            int amount = (int) parameters[0];
            end();
            player.getActionManager().setAction(new GemCutting(gem, amount));
        } else
            end();
    }
}
