package com.fury.game.content.dialogue.impl.skills.dungeoneering;

import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.dialogue.SkillDialogue;
import com.fury.game.content.skill.free.dungeoneering.skills.DungeoneeringRunecrafting;
import com.fury.game.content.skill.free.runecrafting.Runecrafting;
import com.fury.game.content.skill.free.runecrafting.StaffImbuing;
import com.fury.core.model.item.Item;

/**
 * Created by Greg on 16/11/2016.
 */
public class DungRunecraftingD extends Dialogue {
    public static final Item[][] RUNES = {
            {new Item(17780), new Item(17781), new Item(17782), new Item(17783)},
            {new Item(17784), new Item(17785), new Item(17786), new Item(17787)},
            {new Item(17788), new Item(17789), new Item(17790), new Item(17791), new Item(17792)},
            {new Item(16997), new Item(17001), new Item(17005), new Item(17009), new Item(17013)},
            {new Item(16999), new Item(17003), new Item(17007), new Item(17011), new Item(17015)}
    };

    @Override
    public void start() {
        int type = (int) parameters[0];
        sendRCOptions(type);
    }

    private void sendRCOptions(int type) {
        if (type == 0) {
            player.getDialogueManager().sendOptionsDialogue(SkillDialogue.DEFAULT_SKILLS_TITLE, "Elemental runes.", "Combat runes.", "Other runes.", "Staves.");
        } else if (type == 4) {
            player.getDialogueManager().sendOptionsDialogue(SkillDialogue.DEFAULT_SKILLS_TITLE, "Elemental staves.", "Empowered staves.");
        } else {
            SkillDialogue.sendSkillDialogue(player, SkillDialogue.DEFAULT_SKILLS_TITLE, RUNES[type - 1]);
        }
        stage = (byte) (type + 1);
    }

    @Override
    public void run(int optionId) {
        if (stage == 1) {
            if (optionId == DialogueManager.OPTION_1) {
                sendRCOptions(1);
            } else if (optionId == DialogueManager.OPTION_2) {
                sendRCOptions(2);
            } else if (optionId == DialogueManager.OPTION_3) {
                sendRCOptions(3);
            } else if (optionId == DialogueManager.OPTION_4) {
                sendRCOptions(4);
            }
        } else if (stage >= 2 && stage <= 7) {
            if (!player.getTimers().getClickDelay().elapsed(4500))
                return;
            int quantity = (int) parameters[0];
            if (stage == 2) {
                if (optionId == 0)
                    player.getActionManager().setAction(new DungeoneeringRunecrafting(quantity, RUNES[0][optionId], 1, .10, 11, 2, 22, 3, 34, 4, 44, 5, 55, 6, 66, 7, 77, 88, 9, 99, 10));
                else if (optionId == 1)
                    player.getActionManager().setAction(new DungeoneeringRunecrafting(quantity, RUNES[0][optionId], 5, .12, 19, 2, 38, 3, 57, 4, 76, 5, 95, 6));
                else if (optionId == 2)
                    player.getActionManager().setAction(new DungeoneeringRunecrafting(quantity, RUNES[0][optionId], 9, .13, 26, 2, 52, 3, 78, 4));
                else if (optionId == 3)
                    player.getActionManager().setAction(new DungeoneeringRunecrafting(quantity, RUNES[0][optionId], 14, .14, 35, 2, 70, 3));
            } else if (stage == 3) {
                if (optionId == 0)
                    player.getActionManager().setAction(new DungeoneeringRunecrafting(quantity, RUNES[1][optionId], 2, .11, 14, 2, 28, 3, 42, 4, 56, 5, 70, 6, 84, 7, 98, 8));
                else if (optionId == 1)
                    player.getActionManager().setAction(new DungeoneeringRunecrafting(quantity, RUNES[1][optionId], 35, .17, 74, 2));
                else if (optionId == 2)
                    player.getActionManager().setAction(new DungeoneeringRunecrafting(quantity, RUNES[1][optionId], 35, .20, 74, 2));
                else if (optionId == 3)
                    player.getActionManager().setAction(new DungeoneeringRunecrafting(quantity, RUNES[1][optionId], 77, .21));
            } else if (stage == 4) {
                if (optionId == 0)
                    player.getActionManager().setAction(new DungeoneeringRunecrafting(quantity, RUNES[2][optionId], 20, .15, 46, 2, 92, 3));
                else if (optionId == 1)
                    player.getActionManager().setAction(new DungeoneeringRunecrafting(quantity, RUNES[2][optionId], 27, .16, 59, 2));
                else if (optionId == 2)
                    player.getActionManager().setAction(new DungeoneeringRunecrafting(quantity, RUNES[2][optionId], 40, .174, 82, 2));
                else if (optionId == 3)
                    player.getActionManager().setAction(new DungeoneeringRunecrafting(quantity, RUNES[2][optionId], 45, .18, 91, 2));
                else
                    player.getActionManager().setAction(new DungeoneeringRunecrafting(quantity, RUNES[2][optionId], 50, .19));
                end();
            } else if (stage == 5) {
                if (optionId == DialogueManager.OPTION_1) {
                    SkillDialogue.sendSkillDialogue(player, SkillDialogue.DEFAULT_SKILLS_TITLE, RUNES[3]);
                    stage = 6;
                } else if (optionId == DialogueManager.OPTION_2) {
                    Item[] items = RUNES[4];
                    String[] names = SkillDialogue.getNames(items);
                    for (int i = 0; i < names.length; i++)
                        names[i] = names[i].replaceAll("Empowered ", "Empowered\\\\n");

                    SkillDialogue.sendSkillDialogue(player, SkillDialogue.DEFAULT_SKILLS_TITLE, items, names);
                    stage = 7;
                } else {
                    end();
                }
            } else if (stage == 6 || stage == 7) {
                StaffImbuing staff = StaffImbuing.values()[stage == 7 ? optionId + 3 : optionId];
                Runecrafting.imbueStaff(player, staff, quantity);
                end();
            }
        }
    }
}