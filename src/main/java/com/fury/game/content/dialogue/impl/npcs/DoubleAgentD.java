package com.fury.game.content.dialogue.impl.npcs;

import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.global.treasuretrails.ClueConstants;
import com.fury.game.content.global.treasuretrails.DoubleAgent;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.core.model.node.entity.actor.figure.player.Player;

/**
 * Created by Greg on 27/09/2016.
 */
public class DoubleAgentD extends Dialogue {
    public static boolean handleDoubleAgentAttack(Player player, Mob interact) {
        if(interact instanceof DoubleAgent) {
            DoubleAgent doubleAgent = (DoubleAgent) interact;
            if (doubleAgent.getTarget() == player)
                return true;
            else {
                player.getDialogueManager().startDialogue(new DoubleAgentD(), doubleAgent.getTarget());
                return false;
            }
        }
        return false;
    }

    @Override
    public void start() {
        Player target = (Player) parameters[0];
        player.getDialogueManager().sendNPCDialogue(ClueConstants.DOUBLE_AGENT_HIGH, Expressions.PLAIN_TALKING, "I have no business with you.", "I'm here for " + target.getUsername() + ".");
    }

    @Override
    public void run(int optionId) {
        end();
    }
}
