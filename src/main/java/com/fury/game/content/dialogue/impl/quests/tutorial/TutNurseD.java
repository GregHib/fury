package com.fury.game.content.dialogue.impl.quests.tutorial;

import com.fury.game.content.controller.impl.FirstAdventureController;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.global.quests.impl.FirstAdventure;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;

public class TutNurseD extends Dialogue {
    private FirstAdventure quest;
    private Mob nurse;

    @Override
    public void start() {
        quest = (FirstAdventure) parameters[0];
        quest.sage.setRun(false);
        player.getMovement().lock();
        player.getPacketSender().sendEntityHintRemoval(false);
        nurse = GameWorld.getMobs().get(961, new Position(3091, 3506));
        sendNpc(quest.sage, "Talk to " + (nurse == null ? "null" : nurse.getName()), "she will help you out.");
    }

    @Override
    public void run(int optionId) {
        if(quest.getStage() == FirstAdventureController.NURSE) {
            quest.setStage(FirstAdventureController.NURSE_HEALING);
            player.getPacketSender().sendEntityHint(nurse);
        }
        player.getMovement().unlock();
        end();
    }
}
