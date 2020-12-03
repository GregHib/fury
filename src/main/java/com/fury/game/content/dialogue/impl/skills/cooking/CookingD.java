package com.fury.game.content.dialogue.impl.skills.cooking;

import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.dialogue.SkillDialogue;
import com.fury.game.content.skill.free.cooking.Cookables;
import com.fury.game.content.skill.free.cooking.Cooking;
import com.fury.core.model.item.Item;
import com.fury.game.entity.object.GameObject;

/**
 * Created by Greg on 29/11/2016.
 */
public class CookingD extends Dialogue {
    private GameObject object;

    @Override
    public void start() {
        Cookables cooking = (Cookables) parameters[0];
        this.object = (GameObject) parameters[1];

        /*if(cooking == Cooking.Cookables.RAW_MEAT) {
            end();
            player.getDialogueManager().startDialogue(new MeatDrying(), object);
            return;
        }*/

        player.setSelectedSkillingItem(cooking.getRawItem());
        SkillDialogue.sendSkillDialogue(player, "How many would you like to cook?", new Item[] {new Item(cooking.getProduct().getId())});
    }

    @Override
    public void run(int optionId) {
        if(parameters[0] instanceof Integer) {
            int amount = (int) parameters[0];
            Cookables cooking = Cooking.getCookForRaw(player.getSelectedSkillingItem().getId());
            if (cooking == null)
                return;

            player.getActionManager().setAction(new Cooking(object, cooking.getRawItem(), amount, cooking));
        }
        end();
    }
}
