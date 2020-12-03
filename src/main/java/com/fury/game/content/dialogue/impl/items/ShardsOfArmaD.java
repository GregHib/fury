package com.fury.game.content.dialogue.impl.items;

import com.fury.cache.Revision;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.skill.Skill;
import com.fury.core.model.item.Item;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.game.world.update.flag.block.graphic.Graphic;

public class ShardsOfArmaD extends Dialogue {
    @Override
    public void start() {
        if (player.getSkills().getLevel(Skill.CRAFTING) < 77) {
            player.message("You need a Crafting level of at least 77 in order to combine the shards.");
            end();
            return;
        }

        if(player.getInventory().getAmount(new Item(21776)) >= 100) {
            player.animate(713);
            player.perform(new Graphic(1383, Revision.PRE_RS3));
            player.getInventory().delete(new Item(21776, 100));
            player.getInventory().addSafe(new Item(21775, 1));
            player.getSkills().addExperience(Skill.CRAFTING, 150);
            player.getDialogueManager().sendItemDialogue(21775, Revision.PRE_RS3, "You combine the shards into an orb");
        } else
            player.getDialogueManager().sendItemDialogue(21776, Revision.PRE_RS3, "You need at least 100 shards in order", "to create an orb of armadyl.");
    }

    @Override
    public void run(int optionId) {
        end();
    }
}