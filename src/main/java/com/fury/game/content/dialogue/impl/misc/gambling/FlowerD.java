package com.fury.game.content.dialogue.impl.misc.gambling;

import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.global.Gambling;
import com.fury.core.model.item.Item;
import com.fury.game.node.entity.actor.object.ObjectManager;
import com.fury.game.world.World;

/**
 * Created by Greg on 03/12/2016.
 */
public class FlowerD extends Dialogue {
    @Override
    public void start() {
        player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Pick up flowers", "Leave flowers");
    }

    @Override
    public void run(int optionId) {
        end();
        if(optionId == DialogueManager.OPTION_1) {
            if (player.getInteractingObject() != null && player.getInteractingObject().getDefinition() != null && player.getInteractingObject().getDefinition().getName().equalsIgnoreCase("flowers")) {

                if (ObjectManager.isSpawnedObject(player.getInteractingObject())) {
                    player.getInventory().addSafe(new Item(Gambling.FlowersData.forObject(player.getInteractingObject().getId()).itemId));
                    ObjectManager.removeObject(player.getInteractingObject());
                    player.setInteractingObject(null);
                }
            }
        }
    }
}