package com.fury.game.npc.misc;

import com.fury.cache.Revision;
import com.fury.game.content.controller.impl.FirstAdventureController;
import com.fury.game.content.global.quests.Quest;
import com.fury.game.content.global.quests.Quests;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.world.map.Position;

public class FurySage extends Mob {

    private Quest quest;

    public FurySage(int id, Position position, Revision revision, boolean spawned, Player privateUser) {
        super(id, position, revision, spawned, privateUser);
        quest = privateUser.getQuestManager().getQuest(Quests.FIRST_ADVENTURE);
    }

    @Override
    public void processNpc() {
        if(quest.getStage() == FirstAdventureController.FOLLOW && sameAs(new Position(3089, 3498))) {
            quest.setStage(FirstAdventureController.THIEF);
            getPrivateUser().getPacketSender().sendEntityHint(this);
            getDirection().face(new Position(3090, 3498));
        } else if(quest.getStage() == FirstAdventureController.FOLLOW_MERCHANT && sameAs(new Position(3089, 3491))) {
            quest.setStage(FirstAdventureController.MERCHANT);
            getPrivateUser().getPacketSender().sendEntityHint(this);
            getDirection().face(new Position(3089, 3492));
        } else if(quest.getStage() == FirstAdventureController.FOLLOW_NURSE && sameAs(new Position(3090, 3505))) {
            quest.setStage(FirstAdventureController.NURSE);
            getPrivateUser().getPacketSender().sendEntityHint(this);
            getDirection().face(new Position(3091, 3505));
        } else if(quest.getStage() == FirstAdventureController.FOLLOW_SLAYER && sameAs(new Position(3085, 3495))) {
            quest.setStage(FirstAdventureController.SLAYER_PORTAL);
            getPrivateUser().getPacketSender().sendEntityHint(this);
            getDirection().face(new Position(3085, 3496));
        }
    }
}
