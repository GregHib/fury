package com.fury.game.content.dialogue.impl.skills.construction;

import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.skill.member.construction.HouseConstants;
import com.fury.game.content.skill.member.construction.TemporaryAtributtes;
import com.fury.game.entity.character.npc.impl.construction.ServantMob;
import com.fury.core.model.item.Item;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Jon on 2/9/2017.
 */
public class NewServantD extends Dialogue {
    private ServantMob servant;
    private byte page = -1;

    @Override
    public void start() {
        this.servant = (ServantMob) parameters[0];
        servant.setFollowing(true);
        int paymentStage = player.getHouse().getPaymentStage();
        if (paymentStage == 10) {
            stage = 13;
            player.getDialogueManager().sendNPCDialogue(servant.getId(), Expressions.NORMAL, "Excuse me, but before I can continue working you must pay my fee.");
        } else {
            if ((boolean) parameters[1])
                sendBeginningOption();
            else
                player.getDialogueManager().sendNPCDialogue(servant.getId(), Expressions.NORMAL, "I am at thy command, my master");
        }
    }

    private void sendBeginningOption() {
        if (servant.getServantData().isSawmill()) {
            player.getDialogueManager().sendOptionsDialogue("Select an Option", "Take something to the bank", "Bring something from the bank", "Take something to the sawmill");
        } else {
            player.getDialogueManager().sendOptionsDialogue("Select an Option", "Take something to the bank", "Bring something from the bank");
        }
        stage = 9;
    }

    @Override
    public void run(int optionId) {
        if (stage == -1) {
            player.getDialogueManager().sendOptionsDialogue("Select an Option", "Go to the bank/sawmill...", "Misc...", "Stop following me", "You're fired");
            stage = 2;
        } else if (stage == 2) {
            if (optionId == DialogueManager.OPTION_1) {
                sendBeginningOption();
            } else if (optionId == DialogueManager.OPTION_2) {
                player.getDialogueManager().sendOptionsDialogue("Select an Option", "Make tea", "Serve dinner", "Serve drinks", "Greet guests");
                stage = 4;
            } else if (optionId == DialogueManager.OPTION_3) {
                sendBeginningOption();
            } else if (optionId == DialogueManager.OPTION_4) {
                player.getDialogueManager().sendOptionsDialogue("Do you really want to fire your servant?", "Yes.", "No.");
                stage = 3;
            }
        } else if (stage == 3) {
            if (optionId == DialogueManager.OPTION_1) {
                player.getDialogueManager().sendPlayerDialogue(Expressions.NORMAL, "You are dismissed.");
                servant.fire();
            } else {
                end();
            }
            stage = 99;
        } else if (stage == 4) {
            if (optionId == DialogueManager.OPTION_1) {
                player.getDialogueManager().sendNPCDialogue(servant.getId(), Expressions.NORMAL, "Thou shall taste the very tea of the Demon Lords themselves!");
                stage = 6;
            } else if (optionId == DialogueManager.OPTION_2) {
                player.getDialogueManager().sendNPCDialogue(servant.getId(), Expressions.NORMAL, "I shall prepare thee a banquet fit for the lords of Pandemonium!");
                stage = 7;
            } else if (optionId == DialogueManager.OPTION_3) {
                player.getDialogueManager().sendPlayerDialogue(Expressions.NORMAL, "Serve drinks please.");
                stage = 8;
            } else if (optionId == DialogueManager.OPTION_4) {
                player.getDialogueManager().sendPlayerDialogue(Expressions.NORMAL, "Stay at the entrance and greet guests.");
                stage = 5;
            }
        } else if (stage == 5) {
            servant.setGreetGuests(true);
            servant.setFollowing(false);
            servant.setPosition(servant.getRespawnTile());
        } else if (stage == 6) {
            end();
            servant.makeFood(HouseConstants.TEA_BUILDS);
        } else if (stage == 7) {
            end();
            servant.makeFood(HouseConstants.DINNER_BUILDS);
        } else if (stage == 8) {
            end();
            servant.makeFood(HouseConstants.DRINKS_BUILDS);
        } else if (stage == 9) {
            if (optionId == DialogueManager.OPTION_1) {
                player.getDialogueManager().sendNPCDialogue(servant.getId(), Expressions.NORMAL, "Give any item to me and I shall take it swiftly to the bank where it will be safe from thieves and harm.");
                stage = 99;
            } else if (optionId == DialogueManager.OPTION_2) {
                player.getDialogueManager().sendOptionsDialogue("What would you like from the bank?", getPageOptions());
            } else {
                player.getDialogueManager().sendNPCDialogue(servant.getId(), Expressions.NORMAL, "Give me some logs and I will return as fast as possible.");
                stage = 99;
            }
        } else if (stage == 10 || stage == 11 || stage == 12) {
            if (optionId == (stage == 10 ? DialogueManager.OPTION_4 : DialogueManager.OPTION_5)) {
                player.getDialogueManager().sendOptionsDialogue("What would you like from the bank?", getPageOptions());
            } else {
                player.getTemporaryAttributes().put(TemporaryAtributtes.Key.SERVANT_REQUEST_TYPE, 0);
                player.getTemporaryAttributes().put(TemporaryAtributtes.Key.SERVANT_REQUEST_ITEM, HouseConstants.BANKABLE_ITEMS[page][optionId == 11 ? 0 : optionId - 12]);
                player.getPacketSender().sendEnterAmountPrompt("How many would you like?");
                end();
            }
        } else if (stage == 13) {
            player.getDialogueManager().sendOptionsDialogue("Would you you like to pay the fee of " + servant.getServantData().getCost() + "?", "Yes", "No", "Fire.");
            stage = 14;
        } else if (stage == 14) {
            if (optionId == DialogueManager.OPTION_1) {
                int cost = servant.getServantData().getCost();
                if (player.getInventory().getAmount(new Item(995)) < cost) {
                    player.getDialogueManager().sendNPCDialogue(servant.getId(), Expressions.NORMAL, "You do not have enough coins to cover up my cost.");
                    stage = 99;
                    return;
                }
                player.getMoneyPouch().setTotal(player.getMoneyPouch().getTotal() - cost);
                player.getHouse().resetPaymentStage();
                player.getDialogueManager().sendNPCDialogue(servant.getId(), Expressions.NORMAL, "Thank you!");
                stage = -1;
            } else if (optionId == DialogueManager.OPTION_2) {
                end();
            } else if (optionId == DialogueManager.OPTION_3) {
                player.getDialogueManager().sendOptionsDialogue("Do you really want to fire your servant?", "Yes.", "No.");
                stage = 3;
            }
        } else if (stage == 99) {
            end();
        }
    }

    private String[] getPageOptions() {
        List<String> options = new LinkedList<String>();
        page = (byte) (stage == 12 ? 0 : page + 1);
        Item[] items = HouseConstants.BANKABLE_ITEMS[page];
        for (int index = 0; index < items.length; index++)
            options.add(items[index].getDefinition().name);
        options.add("More...");
        stage = (byte) (page + 10);
        return options.toArray(new String[options.size()]);
    }
}
