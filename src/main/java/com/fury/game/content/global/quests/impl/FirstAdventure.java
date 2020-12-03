package com.fury.game.content.global.quests.impl;

import com.fury.cache.Revision;
import com.fury.game.content.controller.impl.FirstAdventureController;
import com.fury.game.content.global.quests.*;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import com.fury.game.npc.misc.FurySage;
import com.fury.game.world.map.Position;

public class FirstAdventure extends Quest {

    public FurySage sage;
    public Mob hint;
    public Mob trainer;

    @Override
    public boolean hasRequirements(Player player) {
        if (player.getInventory().getSpaces() < 6) {
            player.getMovement().unlock();
            player.getPacketSender().sendInterfaceRemoval();
            player.message("You need at least 6 free inventory spots to start this quest.");
            return false;
        }
        if(player.getFamiliar() != null) {
            player.message("You cannot have a familiar with you on this adventure.");
            return false;
        }
        return true;
    }

    @Override
    public Quests getQuest() {
        return Quests.FIRST_ADVENTURE;
    }

    @Override
    public QuestDifficulty getQuestDifficulty() {
        return QuestDifficulty.NOVICE;
    }

    @Override
    public QuestLength getQuestLength() {
        return QuestLength.SHORT;
    }

    @Override
    public int getStages() {
        return 33;
    }

    @Override
    public QuestReward getRewards() {
        return new QuestReward() {

            @Override
            public Item[] items() {
                return new Item[]{new Item(23714, Revision.PRE_RS3)};
            }

            @Override
            public String[] lines() {
                return new String[]{
                        "Experience lamp",
                        "Reese's sword",
                        "Kayle's sling",
                        "Caitlin's staff",
                        "2 Quest points"
                };
            }

            @Override
            public int questPoints() {
                return 2;
            }

            @Override
            public void giveReward() {
            }

            @Override
            public Item questItem() {
                return new Item(23714, Revision.PRE_RS3);
            }
        };
    }

    public void setup(Player player) {
        if (sage == null) {
            sage = new FurySage(949, new Position(3094, 3503), Revision.RS2, true, player);
            hint = new Mob(13728, new Position(3090, 3498), Revision.RS2, true, player);
            trainer = new Mob(162, new Position(2473, 3437), Revision.RS2, true, player);
            sage.setWalkType(0);
            trainer.setWalkType(0);
            sage.getDirection().face(player);
        }
    }

    public void clean() {
        if(sage != null) {
            sage.deregister();
            hint.deregister();
            trainer.deregister();

            sage = null;
            hint = trainer = null;
        }
    }


    @Override
    public void start(Player player) {
        FirstAdventureController controller = new FirstAdventureController();
        player.getControllerManager().startController(controller);
        controller.beginAt(FirstAdventureController.INTRO);
    }

    public void finish(Player player) {
        player.getInventory().addSafe(getRewards().items());
        player.getPacketSender().sendInterface(12140);
        player.getPacketSender().sendString(12144, "You have completed " + getQuest().getName() + "!");
        player.getPacketSender().sendString(12147, player.getQuestManager().getQuestPoints() + "");
        for (int i = 0; i < 6; i++) {
            String value = i >= getRewards().lines().length ? "" : getRewards().lines()[i];
            player.getPacketSender().sendString(12150 + i, value);
        }
        player.getPacketSender().sendString(39198, player.getQuestManager().getTabString(Quests.FIRST_ADVENTURE));
        player.getControllerManager().forceStop();
        clean();
    }
}
