package com.fury.game.content.dialogue.impl.skills.prayer;

import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.dialogue.SkillDialogue;
import com.fury.game.content.skill.free.prayer.Bone;
import com.fury.game.content.skill.free.prayer.BoneOffering;
import com.fury.core.model.item.Item;
import com.fury.game.entity.object.GameObject;

/**
 * Created by Greg on 17/11/2016.
 */
public class BonesOnAlterD extends Dialogue {
    Bone bone;
    int burners;
    GameObject object;

    @Override
    public void start() {
        object = (GameObject) parameters[0];
        bone = (Bone) parameters[1];
        burners = (int) parameters[2];
        SkillDialogue.sendSkillDialogue(player, "How many would you like to offer?", new Item[] {new Item(bone.getId())});
    }

    @Override
    public void run(int optionId) {
        if(parameters[0] instanceof Integer) {
            int amount = (int) parameters[0];
            end();
            int actualAmount = player.getInventory().getAmount(new Item(bone.getId()));
            player.getActionManager().setAction(new BoneOffering(object, bone, amount > actualAmount ? actualAmount : amount, burners));
        } else
            end();
    }
}