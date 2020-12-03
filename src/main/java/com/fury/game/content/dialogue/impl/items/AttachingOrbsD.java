package com.fury.game.content.dialogue.impl.items;

import com.fury.cache.Revision;
import com.fury.game.container.impl.Inventory;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.dialogue.SkillDialogue;
import com.fury.game.content.global.action.Action;
import com.fury.game.content.skill.Skill;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import com.fury.game.world.update.flag.block.Animation;

public class AttachingOrbsD extends Dialogue {

    private final static int[]
            LEVELS = { 66, 58, 54, 62, 77 },
            ORBS = { 573, 575, 571, 569, 21775 },
            STAFFS = { 1397, 1399, 1395, 1393, 21777 };
    private final static double[] EXPERIENCE = { 137.5, 112.5, 100, 125, 150 };
    public static Item BATTLESTAFF = new Item(1391);

    public int index;

    @Override
    public void start() {
        index = (int) parameters[0];
        SkillDialogue.sendSkillDialogue(player, "How many would you like to make?", new Item[] {new Item(STAFFS[index])});
    }

    @Override
    public void run(int optionId) {
        int quantity = (int) parameters[0];

        if(index == -1) {
            end();
            return;
        }

        player.getActionManager().setAction(new Action() {

            int ticks;

            @Override
            public boolean start(final Player player) {
                if (!checkAll(player))
                    return false;
                int orbs = player.getInventory().getAmount(new Item(ORBS[index]));
                int ticks = quantity;
                if (ticks > orbs)
                    ticks = orbs;
                int staffs = player.getInventory().getAmount(BATTLESTAFF);
                if (ticks > staffs)
                    ticks = staffs;
                this.ticks = ticks;
                return true;
            }

            public boolean checkAll(Player player) {
                final int levelRequired = LEVELS[index];
                if (player.getSkills().getLevel(Skill.CRAFTING) < levelRequired) {
                    player.message("You need a Magic level of " + levelRequired + " in order to enchant this type of orb.");
                    return false;
                }
                return true;
            }

            @Override
            public boolean process(Player player) {
                return checkAll(player) && ticks > 0;
            }

            @Override
            public int processWithDelay(Player player) {
                ticks--;
                player.getInventory().delete(new Item(ORBS[index], 1));
                player.getInventory().delete(new Item(BATTLESTAFF, 1));
                player.getInventory().addSafe(new Item(STAFFS[index], 1));
                player.getSkills().addExperience(Skill.CRAFTING, EXPERIENCE[index]);
                player.perform(new Animation(16446 + index, Revision.PRE_RS3));
                return 2;
            }

            @Override
            public void stop(Player player) {
                setActionDelay(player, 3);
            }
        });
        end();
    }

    public static boolean isAttachingOrb(Player player, Item item1, Item item2) {
        Item battleStaff = Inventory.contains(BATTLESTAFF.getId(), item1, item2);
        if (battleStaff == null)
            return false;
        final int index = getOrbIndex(item1.isEqual(BATTLESTAFF) ? item2.getId() : item1.getId());
        if (index == -1)
            return false;
        player.getDialogueManager().startDialogue(new AttachingOrbsD(), index);
        return true;
    }

    private static int getStaffIndex(int requestedId) {
        for (int index = 0; index < STAFFS.length; index++) {
            if (requestedId == STAFFS[index]) {
                return index;
            }
        }
        return -1;
    }

    private static int getOrbIndex(int requestedId) {
        for (int index = 0; index < ORBS.length; index++) {
            if (requestedId == ORBS[index]) {
                return index;
            }
        }
        return -1;
    }
}