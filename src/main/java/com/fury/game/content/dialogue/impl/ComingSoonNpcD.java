package com.fury.game.content.dialogue.impl;

import com.fury.cache.Revision;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.skill.member.slayer.SlayerMaster;

public class ComingSoonNpcD extends Dialogue {


    @Override
    public void start() {
        int npcId = (Integer) parameters[0];
        SlayerMaster master = (SlayerMaster) parameters[1];
        Revision revision = npcId < 13930 ? Revision.RS2 : Revision.PRE_RS3;
        player.getDialogueManager().sendNPCDialogue(npcId, revision, Expressions.NORMAL, "Coming soon");
    }

    @Override
    public void run(int optionId) {
        end();
    }
}
