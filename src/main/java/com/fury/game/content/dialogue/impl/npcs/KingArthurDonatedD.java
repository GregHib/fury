package com.fury.game.content.dialogue.impl.npcs;

import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.node.entity.actor.figure.player.Points;

/**
 * Created by Greg on 14/11/2016.
 */
public class KingArthurDonatedD extends Dialogue {
    int kingArthur = 251;

    @Override
    public void start() {
        String[] funds = player.getPoints().get(Points.DONATED) > 0 ?
                new String[]{ "Your account has claimed scrolls worth $" + player.getPoints().get(Points.DONATED) + " in total.", "Thank you for supporting us!" } :
                new String[]{ "Your account has claimed scrolls worth $" + player.getPoints().get(Points.DONATED) + " in total."};
        player.getDialogueManager().sendNPCDialogue(kingArthur, Expressions.NORMAL, funds);
    }

    @Override
    public void run(int optionId) {
        player.getDialogueManager().startDialogue(new KingArthurD());
        end();
    }
}
