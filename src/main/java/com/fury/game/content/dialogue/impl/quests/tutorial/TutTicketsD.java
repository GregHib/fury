package com.fury.game.content.dialogue.impl.quests.tutorial;

import com.fury.game.content.controller.impl.FirstAdventureController;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.global.quests.impl.FirstAdventure;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;

public class TutTicketsD extends Dialogue {
    private FirstAdventure quest;

    @Override
    public void start() {
        quest = (FirstAdventure) parameters[0];
        player.getMovement().lock();
        player.getPacketSender().sendEntityHintRemoval(false);
        sendNpc(quest.sage, "Great you got some agility tickets, did you feel the rush!",
                "There are tons of agility courses to explore,", "but that's for later.");
    }

    @Override
    public void run(int optionId) {
        if(stage == -1) {
            sendNpc(quest.sage, "I think it's about time we toughened you up a bit.", "That gnome gave you a rough time.");
            stage = 0;
        } else if(stage == 0) {
            quest.sage.getDirection().face(player);
            quest.sage.animate(1818);
            quest.sage.graphic(343);
            player.animate(1816);
            player.graphic(342);
            player.getMovement().unlock();
            GameWorld.schedule(1, () -> {
                player.moveTo(3257, 3255);
                quest.sage.moveTo(new Position(3258, 3255));
                quest.sage.getDirection().face(player);
                player.getDirection().face(quest.sage);
                quest.setStage(FirstAdventureController.COWS);
                player.getDialogueManager().startDialogue(new TutCowsD(), quest);
            });
        } else
            end();
    }
}
