package com.fury.game.content.dialogue.impl.npcs;

import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.dialogue.Expressions;
import com.fury.core.model.item.Item;

public class LeprechaunD extends Dialogue {

    private int leprechaun = 418;
    private static Item kindling = new Item(14666);
    @Override
    public void start() {
        sendPlayerChat(Expressions.NORMAL, "What can I do with spare kindling?");
    }

    @Override
    public void run(int optionId) {
        if (stage == -1) {
            sendNpc(leprechaun, "I'd be 'appy to take any spare samples of ya hands. I", "got mates interested.");
            stage = 0;
        } else if (stage == 0) {
            sendNpc(leprechaun, "I'll even share some of me stash a' gold wif ya.", "5,000 coins each.");
            if(player.getInventory().contains(kindling))
                stage = 1;
            else
                stage = -2;
        } else if(stage == 1) {
            sendOptions("Sell all", "No thanks");
            stage = 2;
        } else if(stage == 2) {
            if(optionId == OPTION_1) {
                if(player.getInventory().contains(kindling)) {
                    int amount = player.getInventory().getAmount(kindling);
                    player.getInventory().delete(new Item(kindling, amount));
                    player.getInventory().addCoins(amount * 5000);
                }
            }
            end();
        } else
            end();
    }
}
