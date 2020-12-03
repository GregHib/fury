package com.fury.game.content.dialogue.impl.skills.firemaking;

import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.dialogue.SkillDialogue;
import com.fury.game.content.skill.free.firemaking.bonfire.Bonfire;
import com.fury.game.content.skill.free.firemaking.bonfire.BonfireLogs;
import com.fury.core.model.item.Item;
import com.fury.game.entity.object.GameObject;

/**
 * Created by Greg on 29/11/2016.
 */
public class BonfireD extends Dialogue {
    private BonfireLogs[] logs;
    private GameObject object;

    @Override
    public void start() {
        this.logs = (BonfireLogs[]) parameters[0];
        this.object = (GameObject) parameters[1];
        Item[] items = new Item[logs.length];
        for(int i = 0; i < items.length; i++)
            items[i] = logs[i].getLog();

        SkillDialogue.sendSkillDialogue(player, "Which logs do you want to add to the bonfire?", items, 0, 15);
    }

    @Override
    public void run(int optionId) {
        if(optionId >= logs.length || optionId < 0)
            return;
        player.getActionManager().setAction(new Bonfire(logs[optionId], object));
        end();
    }
}