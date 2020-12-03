package com.fury.game.entity.character.player.content;

import com.fury.game.content.dialogue.impl.misc.ResetSkillD;
import com.fury.game.content.skill.Skill;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import com.fury.util.Misc;

import java.util.Objects;

public class ExperienceLamps {

    public static boolean handleLamp(Player player, Item item) {
        LampData lamp = LampData.forId(item.getId());
        if (lamp == null)
            return false;

        if(player.openInterface(38000)) {
            player.getPacketSender().sendString(38059, "Choose the stat you wish to be advanced");
            Object[] arr = new Object[3];
            arr[0] = "xp";
            arr[2] = lamp;
            player.setUsableObject(arr);
        }
        return true;
    }

    public static boolean handleButton(Player player, int button) {
        if (selectingExperienceReward(player)) {
            if (button == 38055) {
                try {
                    //player.getPacketSender().sendString(38006, "Choose XP type...");
                    if (player.getUsableObject()[0] != null) {
                        player.getPacketSender().sendInterfaceRemoval();
                        Skill skill = (Skill) player.getUsableObject()[1];
                        switch (((String) player.getUsableObject()[0]).toLowerCase()) {
                            case "xp":
                                LampData lamp = (LampData) player.getUsableObject()[2];
                                if (!player.getInventory().contains(new Item(lamp.getItemId())))
                                    return true;
                                double exp = getExperienceReward(player, lamp, skill);
                                player.getInventory().delete(new Item(lamp.getItemId()));
                                if (lamp == LampData.FIRST_ADVENTURE_QUEST_LAMP)
                                    player.getSkills().addExperience(skill, exp, false);
                                else
                                    player.getSkills().addExperience(skill, exp, true, 0.1);
                                player.message("You've received some experience in " + Misc.formatText(skill.toString().toLowerCase()) + ".");
                                break;
                            case "reset":
                                if(skill != null)
                                    player.getDialogueManager().startDialogue(new ResetSkillD(), skill);
                                break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            } else {
                Interface_Buttons interfaceButton = Interface_Buttons.forButton(button);

                if (interfaceButton == null)
                    return false;
                Skill skill = Skill.forName(interfaceButton.toString());
                if (skill == null)
                    return true;
                Objects.requireNonNull(player.getUsableObject())[1] = skill;
            }
        }
        return false;
    }

    enum LampData {
        NORMAL_XP_LAMP(11137),
        FIRST_ADVENTURE_QUEST_LAMP(23714),
        DRAGONKIN_LAMP(18782);

        LampData(int itemId) {
            this.itemId = itemId;
        }

        private int itemId;

        public int getItemId() {
            return this.itemId;
        }

        public static LampData forId(int id) {
            for (LampData lampData : LampData.values()) {
                if (lampData != null && lampData.getItemId() == id)
                    return lampData;
            }
            return null;
        }
    }

    enum Interface_Buttons {

        ATTACK(38006),
        MAGIC(38012),
        MINING(38036),
        WOODCUTTING(38046),
        AGILITY(38022),
        FLETCHING(38048),
        THIEVING(38026),
        STRENGTH(38008),
        RANGED(38010),
        SMITHING(38038),
        FIREMAKING(38044),
        HERBLORE(38024),
        SLAYER(38032),
        CONSTRUCTION(38050),
        DEFENCE(38014),
        PRAYER(38020),
        FISHING(38028),
        CRAFTING(38016),
        FARMING(38034),
        HUNTER(38040),
        SUMMONING(38052),
        CONSTITUTION(38018),
        DUNGEONEERING(38054),
        COOKING(38042),
        RUNECRAFTING(38030);

        Interface_Buttons(int button) {
            this.button = button;
        }

        private int button;

        public static Interface_Buttons forButton(int button) {
            for (Interface_Buttons skill : Interface_Buttons.values()) {
                if (skill != null && skill.button == button) {
                    return skill;
                }
            }
            return null;
        }
    }

    public static double getExperienceReward(Player player, LampData lamp, Skill skill) {
        if (lamp == LampData.FIRST_ADVENTURE_QUEST_LAMP) {
            boolean combat = false;
            for (Skill cmb : Skill.combatSkills) {
                if (skill == cmb) {
                    combat = true;
                    break;
                }
            }

            switch (player.getGameMode()) {
                default:
                case REGULAR:
                    return combat ? 100000 : 75000;
                case EXTREME:
                    return combat ? 50000 : 35000;
                case LEGEND:
                    return combat ? 20000 : 10000;
                case IRONMAN:
                    return combat ? 35000 : 20000;
            }
        } else if (lamp == LampData.DRAGONKIN_LAMP) {
            int level = player.getSkills().getMaxLevel(skill);
            if (skill.isNewSkill())
                level /= 10;
            double exp = (Math.pow(level, 3) - 2 * Math.pow(level, 2) + 100 * level) / 20;
            return exp;
        } else {
            int level = player.getSkills().getMaxLevel(skill);
            if (skill.isNewSkill())
                level /= 10;
            return level;
        }
    }

    public static boolean selectingExperienceReward(Player player) {
        return player.getInterfaceId() == 38000;
    }
}
