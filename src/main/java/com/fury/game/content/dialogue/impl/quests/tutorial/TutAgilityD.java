package com.fury.game.content.dialogue.impl.quests.tutorial;

import com.fury.game.content.controller.impl.FirstAdventureController;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.global.quests.impl.FirstAdventure;
import com.fury.game.world.map.Position;

public class TutAgilityD extends Dialogue {
    private FirstAdventure quest;

    @Override
    public void start() {
        quest = (FirstAdventure) parameters[0];
        player.getMovement().lock();
        player.getPacketSender().sendEntityHintRemoval(false);
        sendNpc(quest.sage, "Follow the gnomes instructions and I will", "meet you at the end of course!");
    }

    @Override
    public void run(int optionId) {
        end();
    }

    @Override
    public void finish() {
        player.getMovement().unlock();
        quest.setStage(FirstAdventureController.AGILITY_START);
        quest.hint.moveTo(new Position(2474, 3435));
        player.getPacketSender().sendEntityHint(quest.hint);
        quest.sage.walkTo(2482, 3438);
        quest.trainer.getDirection().face(player);
        quest.trainer.forceChat("Right, across the log!");
    }
}
