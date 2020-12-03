package com.fury.game.content.dialogue.impl.skills.crafting;

import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.dialogue.SkillDialogue;
import com.fury.game.content.skill.free.crafting.spinning.SpinData;
import com.fury.game.content.skill.free.crafting.spinning.Spinning;
import com.fury.core.model.item.Item;

/**
 * Created by Greg on 29/12/2016.
 */
public class SpinningD extends Dialogue {
    Item[] items;

    @Override
    public void start() {
        items = (Item[]) parameters;
        SkillDialogue.sendSkillDialogue(player, "How many do you wish to make?", items);
    }

    @Override
    public void run(int optionId) {
        int amount = (int) parameters[0];
        SpinData data = SpinData.forId(items[optionId].getId());
        if(data != null)
            player.getActionManager().setAction(new Spinning(data, amount));
        end();
    }
}
