package com.fury.game.content.dialogue.impl.quests.tutorial;

import com.fury.game.GameSettings;
import com.fury.game.content.controller.impl.FirstAdventureController;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.global.quests.impl.FirstAdventure;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;

public class TutSlayerD extends Dialogue {
    private FirstAdventure quest;

    @Override
    public void start() {
        quest = (FirstAdventure) parameters[0];
        player.getMovement().lock();
        player.getPacketSender().sendEntityHintRemoval(false);
        sendNpc(quest.sage, "These master are all highly skilled killers.", "Each specialise at killing different types of monsters.");
    }

    @Override
    public void run(int optionId) {
        if(stage == -1) {
            sendNpc(quest.sage, "You can ask them for a task and they will assign", "a number of monsters to kill in exchange for rewards.");
            stage = 0;
        } else if(stage == 0) {
            sendNpc(quest.sage, "And you to, can learn how to be a master of slaying.");
            stage = 1;
        } else if(stage == 1) {
            sendNpc(quest.sage, "The masters are standing in level order so if you're just", "starting off you should get your tasks from Turael.");
            Mob turael = GameWorld.getMobs().get(8461, new Position(1371, 5912));
            if(turael != null)
                player.getPacketSender().sendEntityHint(turael);
            stage = 2;
        } else if(stage == 2) {
            sendNpc(quest.sage, "And work your way around until you reach the maximum", "level and can get tasks for bosses from Death himself.");
            Mob death = GameWorld.getMobs().get(14386, new Position(1378, 5913));
            if(death != null)
                player.getPacketSender().sendEntityHint(death);
            stage = 3;
        } else if(stage == 3) {
            sendNpc(quest.sage, "Once you have a task; if you are too lazy to walk you", "can pay to teleport nearby to your task unless it is", "in a dangerous location.");
            Mob teleporter = GameWorld.getMobs().get(7781, new Position(1374, 5911));
            if (teleporter != null)
                player.getPacketSender().sendEntityHint(teleporter);
            stage = 4;
        } else if(stage == 4) {
            quest.setStage(FirstAdventureController.SLAYER_TASK);
            player.getMovement().unlock();
            quest.sage.getDirection().face(player);
            quest.sage.animate(1818);
            quest.sage.graphic(343);
            player.animate(1816);
            player.graphic(342);
            GameWorld.schedule(1, () -> {
                player.moveTo(GameSettings.DEFAULT_POSITION);
                quest.sage.moveTo(new Position(3094, 3503));
                quest.sage.getDirection().face(player);
                player.getDirection().face(quest.sage);
                quest.setStage(FirstAdventureController.HOME);
                player.getDialogueManager().startDialogue(new TutEndD(), quest);
            });
            end();
        } else
            end();
    }
}
