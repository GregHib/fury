package com.fury.game.content.dialogue.impl.misc;

import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.dialogue.input.impl.BuyShards;
import com.fury.game.content.dialogue.input.impl.SellShards;

/**
 * Created by Greg on 04/12/2016.
 */
public class ShardExchange extends Dialogue {
    @Override
    public void start() {
        player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Buy Shards (25 coins each)", "Sell Shards (25 coins each)", "Cancel");
    }

    @Override
    public void run(int optionId) {
        end();
        if(optionId == DialogueManager.OPTION_1) {
            player.getPacketSender().sendEnterAmountPrompt("How many shards would you like to buy? (You can use K, M, B prefixes)");
            player.setInputHandling(new BuyShards());
        } else if(optionId == DialogueManager.OPTION_2) {
            player.getPacketSender().sendEnterAmountPrompt("How many shards would you like to sell? (You can use K, M, B prefixes)");
            player.setInputHandling(new SellShards());
        }
    }
}