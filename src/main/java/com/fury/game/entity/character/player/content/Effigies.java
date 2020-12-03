package com.fury.game.entity.character.player.content;

import com.fury.cache.Revision;
import com.fury.game.content.dialogue.impl.misc.Effigies.EffigyD;
import com.fury.game.content.skill.Skill;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import com.fury.util.Misc;

public class Effigies {

    public static void handleEffigy(Player player, Item effigy) {
        if (player == null)
            return;
        if (player.getInterfaceId() > 0) {
            player.message("Please close the interface you have open before doing this.");
            return;
        } else
            player.getDialogueManager().startDialogue(new EffigyD(), effigy.getId());
    }

    public static boolean checkRequirement(Player player, int skillId, int req) {
        if (player.getSkills().getLevel(Skill.forId(skillId)) >= req) {
            return true;
        } else {
            String skill = Misc.formatText(Skill.forId(skillId).name().toLowerCase());
            player.animate(4067);
            player.getPacketSender().sendInterfaceRemoval();
            player.message("You need " + Misc.anOrA(skill) + " " + skill + " level of at least " + req + " to do that.");
            return false;
        }
    }

    public static void openEffigy(Player player, int skillId) {
        int[] levelReq = {91, 93, 95, 97};
        if (player.getInteractingItem() == null)
            return;
        if (!player.getTimers().getClickDelay().elapsed(4000)) {
            player.getPacketSender().sendInterfaceRemoval();
            return;
        }
        if (player.getInteractingItem().getId() == 18778) {
            if (checkRequirement(player, skillId, levelReq[0]) && player.getInventory().contains(new Item(18778))) {
                player.getInventory().delete(new Item(18778));
                player.getInventory().add(new Item(18779));
                player.getSkills().addExperience(Skill.forId(skillId), 15000, false);
                player.getTimers().getClickDelay().reset();
                player.animate(4068);
                player.getPacketSender().sendInterfaceRemoval();
                player.setEffigy(0);
                player.setInteractingItem(null);
                return;
            }
        }
        if (player.getInteractingItem().getId() == 18779) {
            if (checkRequirement(player, skillId, levelReq[1]) && player.getInventory().contains(new Item(18779))) {
                player.getInventory().delete(new Item(18779));
                player.getInventory().add(new Item(18780));
                player.getSkills().addExperience(Skill.forId(skillId), 30000, false);
                player.getTimers().getClickDelay().reset();
                player.animate(4068);
                player.getPacketSender().sendInterfaceRemoval();
                player.setEffigy(0);
                player.setInteractingItem(null);
                return;
            }
        }
        if (player.getInteractingItem().getId() == 18780) {
            if (checkRequirement(player, skillId, levelReq[2]) && player.getInventory().contains(new Item(18780))) {
                player.getInventory().delete(new Item(18780));
                player.getInventory().add(new Item(18781));
                player.getSkills().addExperience(Skill.forId(skillId), 45000, false);
                player.getTimers().getClickDelay().reset();
                player.animate(4068);
                player.getPacketSender().sendInterfaceRemoval();
                player.setEffigy(0);
                player.setInteractingItem(null);
                return;
            }
        }
        if (player.getInteractingItem().getId() == 18781) {
            if (checkRequirement(player, skillId, levelReq[3]) && player.getInventory().contains(new Item(18781))) {
                player.getInventory().delete(new Item(18781));
                player.getInventory().add(new Item(18782));
                player.getSkills().addExperience(Skill.forId(skillId), 60000, false);
                player.getTimers().getClickDelay().reset();
                player.animate(4068);
                player.getPacketSender().sendInterfaceRemoval();
                player.setEffigy(0);
                player.setInteractingItem(null);
                return;
            }
        }
    }

    public static boolean isEffigy(Item item) {
        return item.getRevision() == Revision.RS2 && item.getId() >= 18778 && item.getId() <= 18781;
    }
}
