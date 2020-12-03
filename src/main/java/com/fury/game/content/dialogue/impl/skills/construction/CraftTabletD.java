package com.fury.game.content.dialogue.impl.skills.construction;

import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.skill.member.construction.CraftTablet;
import com.fury.game.content.skill.member.construction.SpellTablet;

/**
 * Created by Jon on 2/9/2017.
 */
public class CraftTabletD extends Dialogue {
    @Override
    public void start() {
    }

    @Override
    public void run(int optionId) {
        int index = optionId - 38333;
        SpellTablet[] tablets = (SpellTablet[]) player.getTemporaryAttributes().get("tablets");
        if(index < tablets.length && index > 0)
            player.getActionManager().setAction(new CraftTablet(tablets[index]));
    }

    @Override
    public void finish() {
        player.getTemporaryAttributes().remove("tablets");
    }
}
