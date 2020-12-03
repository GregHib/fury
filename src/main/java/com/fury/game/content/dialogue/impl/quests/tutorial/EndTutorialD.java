package com.fury.game.content.dialogue.impl.quests.tutorial;

import com.fury.game.GameSettings;
import com.fury.game.content.controller.impl.FirstAdventureController;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.dialogue.impl.misc.StartD;
import com.fury.game.content.global.quests.Quests;
import com.fury.game.content.global.quests.impl.FirstAdventure;
import com.fury.core.model.item.Item;

public class EndTutorialD extends Dialogue {

    @Override
    public void start() {
        sendNpc(949, "If you leave now you'll have to start over", "are you sure you want to leave?");
    }

    @Override
    public void run(int optionId) {
        if(stage == -1) {
            sendOptions("Yes", "No, I want to continue");
            stage = 0;
        } else if(stage == 0) {
            end();
            if(optionId == OPTION_1) {
                FirstAdventure quest = (FirstAdventure) player.getQuestManager().getQuest(Quests.FIRST_ADVENTURE);
                player.getControllerManager().forceStop();
                quest.clean();
                player.getQuestManager().end(Quests.FIRST_ADVENTURE);
                if (quest.getStage() >= FirstAdventureController.COWS) {
                    if (player.hasItemOnThem(15596)) {
                        player.getEquipment().delete(new Item(15596));
                        player.getInventory().delete(new Item(15596));
                    }
                    if (player.hasItemOnThem(15597)) {
                        player.getEquipment().delete(new Item(15597));
                        player.getInventory().delete(new Item(15597));
                    }
                    if (player.hasItemOnThem(15598)) {
                        player.getEquipment().delete(new Item(15598));
                        player.getInventory().delete(new Item(15598));
                    }
                }
                player.getPacketSender().sendEntityHintRemoval(false);
                player.moveTo(GameSettings.DEFAULT_POSITION);
                if(player.newPlayer())
                    player.getDialogueManager().startDialogue(new StartD());
            }
        } else
            end();
    }
}