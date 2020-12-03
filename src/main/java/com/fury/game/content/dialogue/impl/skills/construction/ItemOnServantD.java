package com.fury.game.content.dialogue.impl.skills.construction;

import com.fury.cache.def.item.ItemDefinition;
import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.skill.member.construction.HouseConstants;
import com.fury.game.content.skill.member.construction.Sawmill;
import com.fury.game.entity.character.npc.impl.construction.ServantMob;
import com.fury.core.model.item.Item;

/**
 * Created by Jon on 2/9/2017.
 */
public class ItemOnServantD extends Dialogue {
    private ServantMob servant;
    private Item item;

    @Override
    public void start() {
        this.servant = (ServantMob) parameters[0];
        item = (Item) this.parameters[1];
        boolean procceed = false;
        for (int index = 0; index < HouseConstants.BANKABLE_ITEMS.length; index++) {
            for (Item bankable : HouseConstants.BANKABLE_ITEMS[index]) {
                if (item.isEqual(bankable)) {
                    procceed = true;
                    break;
                }
            }
        }
        ItemDefinition definition = item.getDefinition();
        final Sawmill.Plank plank = Sawmill.getPlankForLog(item.getId());
        if (plank != null || definition.isNoted())
            procceed = true;
        if (!procceed) {
            end();
            return;
        }
        int paymentStage = player.getHouse().getPaymentStage();
        if (paymentStage == 1) {
            player.getDialogueManager().sendNPCDialogue(servant.getId(), Expressions.NORMAL, "Excuse me, but before I can continue working you must pay my fee.");
            stage = 3;
        }
        String name = definition.name.toLowerCase();

        if (definition.isNoted()) {
            player.getDialogueManager().sendOptionsDialogue("Un-cert this item?", "Un-cert " + name + ".", "Fetch another " + name + ".", "Bank", "Cancel");
            stage = 0;
        } else if ((boolean) this.parameters[2] && plank != null) {
            player.getDialogueManager().sendOptionsDialogue("Take this to the sawmill?", "Take it to the sawmill.", "Bank", "Cancel");
            stage = 2;
        } else {
            player.getDialogueManager().sendOptionsDialogue("Take this item to the bank?", "Fetch another " + name + ".", "Bank", "Cancel");
            stage = 1;
        }
    }

    @Override
    public void run(int optionId) {
        end();
    }
}
