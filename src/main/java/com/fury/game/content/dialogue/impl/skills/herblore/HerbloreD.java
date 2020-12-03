package com.fury.game.content.dialogue.impl.skills.herblore;

import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.dialogue.SkillDialogue;
import com.fury.game.content.skill.member.herblore.Herblore;
import com.fury.core.model.item.Item;

/**
 * Created by Greg on 30/12/2016.
 */
public class HerbloreD extends Dialogue {
    private Item item;
    private Item first;
    private Item second;

    @Override
    public void start() {
        item = (Item) parameters[0];
        first = (Item) parameters[1];
        second = (Item) parameters[2];
        boolean raw = (boolean) parameters[3];

        if(!raw && item.getName().contains("tar"))
            raw = true;

        Item[] items = new Item[] { item };
        String[] names = SkillDialogue.getNames(items);
        for(int i = 0; i < names.length; i++)
            if(names[i].contains("(")) {
                int start = names[i].indexOf("(") - 1;
                names[i] = names[i].substring(0, start);
            }
        SkillDialogue.sendSkillDialogue(player, "How many would you like to make?", items, names, 0, raw ? 0 : 20, 180);
    }

    @Override
    public void run(int optionId) {
        if(parameters[0] instanceof Integer) {
            int quantity = (int) parameters[0];
            player.getActionManager().setAction(new Herblore(first, second, quantity));
        }
        end();
    }
}
