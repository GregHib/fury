package com.fury.game.content.dialogue.impl.quests.tutorial;

import com.fury.game.GameSettings;
import com.fury.game.content.controller.impl.FirstAdventureController;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.global.quests.impl.FirstAdventure;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;

public class TutDeadCowD extends Dialogue {
    private FirstAdventure quest;

    @Override
    public void start() {
        quest = (FirstAdventure) parameters[0];
        player.getMovement().lock();
        player.getPacketSender().sendEntityHintRemoval(false);
        sendNpc(quest.sage, "Great work, now you know how to train", "let's show you how to make money using", "your newly acquired skills.");
    }

    @Override
    public void run(int optionId) {
        end();
    }

    @Override
    public void finish() {
        player.getMovement().unlock();
        quest.sage.getDirection().face(player);
        quest.sage.animate(1818);
        quest.sage.graphic(343);
        player.animate(1816);
        player.graphic(342);
        GameWorld.schedule(1, () -> {
            player.moveTo(GameSettings.DEFAULT_POSITION);
            quest.sage.moveTo(new Position(3092, 3503));
            quest.sage.getDirection().face(player);
            player.getDirection().face(quest.sage);
            quest.setStage(FirstAdventureController.FOLLOW_SLAYER);
            quest.sage.walkTo(3085, 3495);
            quest.sage.forceChat("Follow me!");
        });
    }
}
