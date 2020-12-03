package com.fury.game.content.dialogue.impl.skills.agility;

import com.fury.game.container.impl.shop.ShopManager;
import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.dialogue.input.impl.BuyAgilityExperience;
import com.fury.util.FontUtils;

/**
 * Created by Greg on 14/11/2016.
 */
public class AgilityTicketExchangeD extends Dialogue {
    @Override
    public void start() {
        player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Experience", "Equipment");
    }

    @Override
    public void run(int optionId) {
        if(stage == -1) {
            if(optionId == DialogueManager.OPTION_1) {
                player.getDialogueManager().sendNPCDialogue(437, Expressions.NORMAL, FontUtils.BLACK + "How many tickets would you like to exchange", "for experience? One ticket currently grants", FontUtils.RED + (int) (154 * player.getGameMode().getSkillRate()) + FontUtils.COL_END + " Agility experience." + FontUtils.COL_END);
                stage = 0;
            } else {
                end();
                if(optionId == DialogueManager.OPTION_2)
                    ShopManager.getShops().get(39).open(player);
            }
        } else if(stage == 0) {
            end();
            player.setInputHandling(new BuyAgilityExperience());
            player.getPacketSender().sendEnterAmountPrompt("How many tickets would you like to exchange?");
        }
    }
}