package com.fury.game.content.dialogue.impl.npcs;

import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.character.player.link.transportation.TeleportHandler;

public class JesmonaD extends Dialogue {
    private Mob jes;

    @Override
    public void start() {
        jes = (Mob) parameters[0];
        if(player.getSlayerManager().getCurrentTask() == null) {
            player.getDialogueManager().sendNPCDialogue(jes.getId(), Expressions.PLAIN_TALKING, "Hi, come talk to me when you have a task", "I might be able to help you out getting there.");
        } else {
            if(player.getSlayerManager().getCurrentTask().getLocation() != null) {
                player.getDialogueManager().sendNPCDialogue(jes.getId(), Expressions.PLAIN_TALKING, "I can help you get to your task of " + player.getSlayerManager().getCurrentTask().getName(), "how does " + player.getSlayerManager().getCurrentMaster().getRequiredCombatLevel() + "k sound?");
                stage = 0;
            } else
                player.getDialogueManager().sendNPCDialogue(jes.getId(), Expressions.PLAIN_TALKING, "Sorry I can't teleport you to this task", "it might be in a dangerous location.");
        }
    }

    @Override
    public void run(int optionId) {
        if(stage == 0) {
            player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Yes take me to my task", "No thanks, I'll walk");
            stage = 1;
        } else if(stage == 1) {
            if(optionId == DialogueManager.OPTION_1) {
                int price = player.getSlayerManager().getCurrentMaster().getPrice();
                if(player.getInventory().removeCoins(price)) {
                    jes.animate(811);
                    player.message("Jesmona teleports you close to your task.");
                    TeleportHandler.teleportPlayer(player, player.getSlayerManager().getCurrentTask().getLocation());
                }
            }
            end();
        } else
            end();
    }
}