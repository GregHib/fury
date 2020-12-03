package com.fury.game.content.dialogue.impl.skills.fletching;

import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.dialogue.SkillDialogue;
import com.fury.game.content.skill.member.fletching.Fletching;
import com.fury.game.content.skill.member.fletching.FletchingData;
import com.fury.util.Misc;

/**
 * Created by Greg on 18/11/2016.
 */
public class FletchingD extends Dialogue {
    private FletchingData items;

    @Override
    public void start() {
        items = (FletchingData) parameters[0];
        String[] names = SkillDialogue.getNames(items.getProducts());
        String[] dungNames = {"Tangle gum", "Seeping elm", "Blood spindle", "Utuku", "Spinebeam", "Bovistrangler", "Thigat", "Corpsethorn", "Entgallow", "Grave creeper"};
        for(int i = 0; i < names.length; i++) {
            names[i] = names[i].replace(" (u)", "");
            for(String dung : dungNames)
                if(names[i].contains(dung))
                    names[i] = Misc.uppercaseFirst(names[i].replace(dung, "").trim());
        }

        SkillDialogue.sendSkillDialogue(player, SkillDialogue.DEFAULT_SKILLS_TITLE, items.getProducts(), names);
    }

    @Override
    public void run(int optionId) {
        if(parameters[0] instanceof Integer) {
            int amount = (int) parameters[0];
            if (optionId > items.getProducts().length) {
                end();
                return;
            }

            player.getActionManager().setAction(new Fletching(items, optionId, amount));
        }
        end();
    }
}