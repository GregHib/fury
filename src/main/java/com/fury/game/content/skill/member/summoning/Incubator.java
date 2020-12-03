package com.fury.game.content.skill.member.summoning;

import com.fury.game.content.skill.Skill;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;

public class Incubator {

    private enum Egg {
        PENGUIN_EGG(30, 12483, 12481),
        RAVEN_EGG(50, 11964, 12484),
        ZAMORAK_BIRD_EGG(70, 5076, 12506),
        SARADOMIN_BIRD_EGG(70, 5077, 12503),
        GUTHIX_BIRD_EGG(70, 5078, 12509),
        VULTURE_EGG(85, 11965, 12498),
        CHAMELEON_EGG(90, 12494, 12492),
        RED_DRAGON_EGG(99, 12477, 12469),
        BLUE_DRAGON_EGG(99, 12478, 12471),
        GREEN_DRAGON_EGG(99, 12479, 12473),
        BLACK_DRAGON_EGG(99, 12480, 12475);
        private int summoningLevel, eggId, petId;

        Egg(int summoningLevel, int eggId, int petId) {
            this.summoningLevel = summoningLevel;
            this.eggId = eggId;
            this.petId = petId;
        }
    }

    public static Egg getEgg(int itemId) {
        for (Egg egg : Egg.values())
            if (egg.eggId == itemId)
                return egg;
        return null;
    }

    public static boolean useEgg(Player player, int itemId) {
        Egg egg = getEgg(itemId);
        if (egg == null)
            return false;
        if (!player.getSkills().hasRequirement(Skill.SUMMONING, egg.summoningLevel, "hatch this egg"))
            return true;

        player.getMovement().lock(1);
        player.animate(833);
        player.getInventory().delete(new Item(itemId));
        player.getInventory().add(new Item(egg.petId));
        player.message("You put the egg in the incubator and it hatches.");
        return true;
    }
}
