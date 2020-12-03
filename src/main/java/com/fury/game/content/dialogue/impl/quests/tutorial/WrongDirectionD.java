package com.fury.game.content.dialogue.impl.quests.tutorial;

import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.content.dialogue.Dialogue;

public class WrongDirectionD extends Dialogue {
    @Override
    public void start() {
        if(parameters[0] instanceof Integer) {
            int id = (int) parameters[0];
            sendNpc(id, "Not that way, over here!");
        } else if(parameters[0] instanceof Mob) {
            Mob mob = (Mob) parameters[0];
            sendNpc(mob, "Not that way, over here!");
            player.getPacketSender().sendEntityHint(mob);
        }
    }

    @Override
    public void run(int optionId) {
        if(player.getMovement().isLocked())
            player.getMovement().unlock();
        end();
    }
}
