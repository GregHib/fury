package com.fury.game.content.dialogue.impl.quests.tutorial;

import com.fury.game.content.controller.impl.FirstAdventureController;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.global.quests.impl.FirstAdventure;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;

public class TutRestD extends Dialogue {
    private FirstAdventure quest;

    @Override
    public void start() {
        quest = (FirstAdventure) parameters[0];
        player.getMovement().lock();
        player.getPacketSender().sendEntityHintRemoval(false);
        sendNpc(quest.sage, "Resting and listening to musicians is a quick way", "of restoring health and run energy.", "Now you're all rested up let's go run it off", "at my favourite agility course.");
    }

    @Override
    public void run(int optionId) {
        end();
    }

    @Override
    public void finish() {
        quest.setStage(FirstAdventureController.AGILITY);
        quest.sage.getDirection().face(player);
        quest.sage.animate(1818);
        quest.sage.graphic(343);
        player.animate(1816);
        player.graphic(342);
        GameWorld.schedule(1, () -> {
            quest.sage.animate(8939);
            quest.sage.graphic(1576);
            player.moveTo(2474, 3437);
            quest.sage.moveTo(new Position(2475, 3437));
            quest.sage.getDirection().face(player);
            player.getDirection().face(new Position(2474, 3436));
            player.getDialogueManager().startDialogue(new TutAgilityD(), quest);
        });
        player.getMovement().unlock();
    }
}
