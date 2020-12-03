package com.fury.game.content.dialogue.impl.quests.tutorial;

import com.fury.game.content.controller.impl.FirstAdventureController;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.global.quests.impl.FirstAdventure;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.node.entity.actor.figure.player.handles.Settings;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;

public class TutTeleD extends Dialogue {
    private FirstAdventure quest;
    private Mob musican;

    @Override
    public void start() {
        quest = (FirstAdventure) parameters[0];
        musican = GameWorld.getMobs().get(8705, new Position(3091, 3500));
        player.getMovement().lock();
        player.getPacketSender().sendEntityHintRemoval(false);
        sendNpc(quest.sage, "You can use teleport to get to anywhere you want", "so make sure to explore them all later on.");
    }

    @Override
    public void run(int optionId) {
        if(stage == -1) {
            sendNpc(quest.sage, "You must be tired from all that running.", "Let's go rest by the musician.");
            if(player.getSettings().getInt(Settings.RUN_ENERGY) > 90)
                player.getSettings().set(Settings.RUN_ENERGY, 90);
            stage = 0;
        } else
            end();
    }

    @Override
    public void finish() {
        if(musican != null) {
            quest.setStage(FirstAdventureController.MUSICIAN);
            quest.sage.walkTo(3090, 3500);
            quest.sage.getDirection().face(musican);
            player.getPacketSender().sendEntityHint(musican);
            player.getMovement().unlock();
        }
    }
}
