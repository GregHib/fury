package com.fury.game.content.dialogue.impl.skills.slayer;

import com.fury.game.container.impl.shop.ShopManager;
import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.skill.member.slayer.SlayerMaster;

public class SlayerMasterShopD extends Dialogue {
    private int npcId;
    private SlayerMaster master;

    @Override
    public void start() {
        npcId = (Integer) parameters[0];
        master = (SlayerMaster) parameters[1];
        player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Items", "Rewards");
    }

    @Override
    public void run(int optionId) {
        if(optionId == DialogueManager.OPTION_1) {
            ShopManager.getShops().get(40).open(player);
        } else if(optionId == DialogueManager.OPTION_2) {
            if (master == SlayerMaster.DEATHWILD)
                ShopManager.getShops().get(130).open(player);
            else
                ShopManager.getShops().get(124).open(player);
        }
    }
}