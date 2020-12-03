package com.fury.game.content.dialogue.impl.quests.tutorial;

import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.global.quests.impl.FirstAdventure;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;

public class TutPortalD extends Dialogue {
    private FirstAdventure quest;

    @Override
    public void start() {
        quest = (FirstAdventure) parameters[0];
        player.getMovement().lock();
        player.getPacketSender().sendEntityHintRemoval(false);
        sendNpc(quest.sage, "This is the slayer portal,", "within it lie the realm of the slayer masters.");
    }

    @Override
    public void run(int optionId) {
        if(stage == -1) {
            sendNpc(quest.sage, "Let's go take a look!");
            stage = 0;
        } else if(stage == 0) {
            quest.sage.getDirection().face(new Position(3083, 3495));
            quest.sage.animate(14279);

            GameWorld.schedule(1, () -> {
                quest.sage.moveTo(new Position(1375, 5911));
                quest.sage.getDirection().face(new Position(1375, 5912));

                player.getDirection().face(new Position(3083, 3496));
                player.animate(14279);
            });

            GameWorld.schedule(2, () -> {
                player.moveTo(1376, 5911);
                quest.sage.getDirection().face(new Position(1376, 5912));
                player.getDialogueManager().startDialogue(new TutSlayerD(), quest);
            });
            end();
        } else
            end();
    }
}
