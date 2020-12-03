package com.fury.game.content.dialogue.impl.skills.construction;

import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.skill.member.construction.HouseConstants;
import com.fury.core.model.item.Item;

/**
 * Created by Jon on 2/9/2017.
 */
public class WeaponsRackD extends Dialogue {
    private static final String[] RACK_NAMES = { "Red Boxing Gloves", "Blue Boxing Gloves", "Wooden Sword", "Wooden Shield", "Pugel Stick" };

    @Override
    public void start() {
        int length = (int) this.parameters[0];
        String[] name = new String[length];
        System.arraycopy(RACK_NAMES, 0, name, 0, length);
        player.getDialogueManager().sendOptionsDialogue("Select a weapon of your choice.", name);
    }

    @Override
    public void run(int optionId) {
        player.getInventory().add(new Item(HouseConstants.WEAPON_RACK[optionId]));
        end();
    }
}
