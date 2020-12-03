package com.fury.game.content.dialogue.impl.quests.tutorial;

import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.global.quests.impl.FirstAdventure;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;

public class TutSellD extends Dialogue {
    private FirstAdventure quest;

    @Override
    public void start() {
        quest = (FirstAdventure) parameters[0];
        player.getMovement().lock();
        player.getPacketSender().sendEntityHintRemoval(false);
        sendNpc(quest.sage, "Perfect, just what we were looking for.", "Trade with the merchant and sell the ring", "to his shop by right clicking it in your inventory.");
    }

    @Override
    public void run(int optionId) {
        player.getMovement().unlock();
        Mob merchant = GameWorld.getMobs().get(2292, new Position(3090, 3492));
        if(merchant != null)
            player.getPacketSender().sendEntityHint(merchant);
        end();
    }
}
