package com.fury.game.entity.item.content;

import com.fury.game.world.update.flag.block.Animation;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.core.model.item.Item;
import com.fury.game.content.skill.Skill;
import com.fury.util.Misc;
import com.fury.core.model.node.entity.actor.figure.player.Player;

/**
 * Created by Greg on 12/07/2016.
 */
public enum ItemCreation {
    CHOCOLATE_CAKE(new Item[] { new Item(1891), new Item(1975) }, new Item(1897), null),
    LEMON(new Item(2102), new Item[] { new Item(2104), new Item(2106) }, new Item(946)),
    LIME(new Item(2120), new Item[] { new Item(2122), new Item(2124) }, new Item(946)),
    ORANGE(new Item(2108), new Item[] { new Item(2110), new Item(2112) }, new Item(946)),
    PINEAPPLE(new Item(2114), new Item[] { new Item(2116), new Item(2118) }, new Item(946)),
    WATERMELON(new Item(5982), new Item(5984), new Item(946)),
    POTATO_WITH_BUTTER(new Item[] { new Item(6697), new Item(6701) }, new Item(6703), null, 39, Skill.COOKING, 40),
    CHILLI_POTATO(new Item[] { new Item(7062), new Item(6703) }, new Item(7054), null, 41, Skill.COOKING, 15),
    POTATO_WITH_CHEESE(new Item[] { new Item(1985), new Item(6703) }, new Item(6705), null, 47, Skill.COOKING, 40),
    EGG_POTATO(new Item[] { new Item(7064), new Item(6703) }, new Item(7056), null, 54, Skill.COOKING, 45),
    MUSHROOM_POTATO(new Item[] { new Item(7066), new Item(6703) }, new Item(7058), null, 64, Skill.COOKING, 55),
    TUNA_POTATO(new Item[] { new Item(7068), new Item(6703) }, new Item(7060), null, 68, Skill.COOKING, 10),
    PIZZA_BASE(new Item[] { new Item(1929), new Item(1933) }, new Item[] { new Item(2283), new Item(1925)}, null, 35, Skill.COOKING, 1),
    UNCOOKED_PIZZA(new Item[] { new Item(2283), new Item(1985), new Item(1982) }, new Item(2287), null, 35, Skill.COOKING, 0),
    MEAT_PIZZA(new Item[] { new Item(2289), new Item(2142) }, new Item(2293), null, 45, Skill.COOKING, 26),
    CHICKEN_PIZZA(new Item[] { new Item(2289), new Item(2140) }, new Item(2293), null, 45, Skill.COOKING, 26),
    ANCHOVY_PIZZA(new Item[] { new Item(2289), new Item(319) }, new Item(2297), null, 55, Skill.COOKING, 40),
    PINEAPPLE_PIZZA_CHUNKS(new Item[] { new Item(2289), new Item(2116) }, new Item(2301), null, 65, Skill.COOKING, 52),
    PINEAPPLE_PIZZA_RINGS(new Item[] { new Item(2289), new Item(2118) }, new Item(2301), null, 65, Skill.COOKING, 52),
    UNCOOKED_CAKE(new Item[] { new Item(1944), new Item(1927), new Item(1933), new Item(1887) }, new Item[] { new Item(1889), new Item(1925), new Item(1931) }, null, 40, Skill.COOKING, 180),
    CANDLE(new Item(36), new Item(34), new Item(590)),
    BLACK_CANDLE(new Item(38), new Item(32), new Item(590)),
    TORCH(new Item(596), new Item(594), new Item(590)),
    BUG_LANTERN(new Item(7051), new Item(7053), new Item(590)),
    DRAMEN_STAFF(new Item(771), new Item(772), new Item(946)),
    ROYAL_CROSSBOW(new Item[] {new Item(24303), new Item(24342), new Item(24346), new Item(24340), new Item(24344)}, new Item(24337), null),
    VINE_WHIP(new Item[] {new Item(4151), new Item(21369)}, new Item[] {new Item(21371)}, null),
    MITHRIL_GRAPLE(new Item[] {new Item(9418), new Item(954)}, new Item[] {new Item(9419)}, null),
    ;

    Item[] itemsRemoved, itemsAdded, itemsRequired;
    int levelRequired;
    Skill skill;
    double experience;
    Animation emote;
    Graphic graphic;
    ItemCreation(Item itemsRemoved, Item itemsAdded, Item itemsRequired) {
        this.itemsRemoved = new Item[] { itemsRemoved };
        this.itemsAdded = new Item[] { itemsAdded };
        this.itemsRequired = new Item[] { itemsRequired };
        this.levelRequired = 1;
        this.skill = null;
        this.experience = 0;
        this.emote = null;
        this.graphic = null;
    }
    ItemCreation(Item[] itemsRemoved, Item itemsAdded, Item[] itemsRequired) {
        this.itemsRemoved = itemsRemoved;
        this.itemsAdded = new Item[] { itemsAdded };
        this.itemsRequired = itemsRequired;
        this.levelRequired = 1;
        this.skill = null;
        this.experience = 0;
        this.emote = null;
        this.graphic = null;
    }
    ItemCreation(Item itemsRemoved, Item itemsAdded, Item[] itemsRequired) {
        this.itemsRemoved = new Item[] { itemsRemoved };
        this.itemsAdded = new Item[] { itemsAdded };
        this.itemsRequired = itemsRequired;
        this.levelRequired = 1;
        this.skill = null;
        this.experience = 0;
        this.emote = null;
        this.graphic = null;
    }
    ItemCreation(Item itemsRemoved, Item[] itemsAdded, Item itemsRequired) {
        this.itemsRemoved = new Item[] { itemsRemoved };
        this.itemsAdded = itemsAdded;
        this.itemsRequired = new Item[] { itemsRequired };
        this.levelRequired = 1;
        this.skill = null;
        this.experience = 0;
        this.emote = null;
        this.graphic = null;
    }
    ItemCreation(Item[] itemsRemoved, Item[] itemsAdded, Item[] itemsRequired) {
        this.itemsRemoved = itemsRemoved;
        this.itemsAdded = itemsAdded;
        this.itemsRequired = itemsRequired;
        this.levelRequired = 1;
        this.skill = null;
        this.experience = 0;
        this.emote = null;
        this.graphic = null;
    }
    ItemCreation(Item[] itemsRemoved, Item[] itemsAdded, Item[] itemsRequired, int levelRequired) {
        this.itemsRemoved = itemsRemoved;
        this.itemsAdded = itemsAdded;
        this.itemsRequired = itemsRequired;
        this.levelRequired = levelRequired;
        this.skill = null;
        this.experience = 0;
        this.emote = null;
        this.graphic = null;
    }

    ItemCreation(Item[] itemsRemoved, Item itemsAdded, Item[] itemsRequired, int levelRequired, Skill skill, double experience) {
        this.itemsRemoved = itemsRemoved;
        this.itemsAdded = new Item[] {itemsAdded};
        this.itemsRequired = itemsRequired;
        this.levelRequired = levelRequired;
        this.skill = skill;
        this.experience = experience;
        this.emote = null;
        this.graphic = null;
    }

    ItemCreation(Item[] itemsRemoved, Item[] itemsAdded, Item[] itemsRequired, int levelRequired, Skill skill, double experience) {
        this.itemsRemoved = itemsRemoved;
        this.itemsAdded = itemsAdded;
        this.itemsRequired = itemsRequired;
        this.levelRequired = levelRequired;
        this.skill = skill;
        this.experience = experience;
        this.emote = null;
        this.graphic = null;
    }

    ItemCreation(Item itemsRemoved, Item itemsAdded, Item itemsRequired, int levelRequired, Skill skill, double experience, Animation emote, Graphic graphic) {
        this.itemsRemoved = new Item[] { itemsRemoved };
        this.itemsAdded = new Item[] { itemsAdded };
        this.itemsRequired = new Item[] { itemsRequired };
        this.levelRequired = levelRequired;
        this.skill = skill;
        this.experience = experience;
        this.emote = emote;
        this.graphic = graphic;
    }

    ItemCreation(Item[] itemsRemoved, Item[] itemsAdded, Item[] itemsRequired, int levelRequired, Skill skill, double experience, Animation emote, Graphic graphic) {
        this.itemsRemoved = itemsRemoved;
        this.itemsAdded = itemsAdded;
        this.itemsRequired = itemsRequired;
        this.levelRequired = levelRequired;
        this.skill = skill;
        this.experience = experience;
        this.emote = emote;
        this.graphic = graphic;
    }

    public Item[] getItemsRemoved() {
        return itemsRemoved;
    }

    public Item[] getItemsAdded() {
        return itemsAdded;
    }

    public Item[] getItemsRequired() {
        return itemsRequired;
    }

    public int getLevelRequired() {
        return levelRequired;
    }

    public Skill getSkill() {
        return skill;
    }

    public double getExperience() {
        return experience;
    }

    public Animation getEmote() {
        return emote;
    }

    public Graphic getGraphic() {
        return graphic;
    }

    public static ItemCreation forItem(int item1, int item2) {
        for (ItemCreation creation : ItemCreation.values()) {
            boolean has1 = false, has2 = false;
            if(creation.getItemsRemoved() != null) {
                for (Item item : creation.getItemsRemoved()) {
                    if (item.getId() == item1)
                        has1 = true;
                    if(item.getId() == item2)
                        has2 = true;
                }
            }
            if(creation.getItemsRequired() != null) {
                for (Item item : creation.getItemsRequired()) {
                    if (item.getId() == item1)
                        has1 = true;
                    if(item.getId() == item2)
                        has2 = true;
                }
            }
            if(has1 && has2)
                return creation;
        }
        return null;
    }

    public static boolean handleItemOnItem(Player player, int item1, int item2) {
        ItemCreation creation = forItem(item1, item2);
        if(creation == null)
            return false;

        //Run checks
        if(creation.getItemsRemoved() != null)
        for (Item item : creation.getItemsRemoved()) {
            if(!player.getInventory().contains(item)) {
                String name = item.getName();
                player.message("You need to have " +  Misc.anOrA(name) + " " + name + " to do this.");
                return true;
            }

        }
        if(creation.getItemsRequired() != null)
        for (Item item : creation.getItemsRequired()) {
            if(!player.getInventory().contains(item)) {
                String name = item.getName();
                player.message("You must have " + Misc.anOrA(name) + " " + name + " to do this.");
                return true;
            }
        }
        if(creation.getSkill() != null && player.getSkills().getLevel(creation.getSkill()) < creation.getLevelRequired()) {
            player.message("You need a " + creation.getSkill().getName() + " level of at least " + creation.getLevelRequired() + " to do this.");
            return true;
        }
        int spaceRequired = 0;
        if(creation.getItemsAdded() != null)
            spaceRequired += creation.getItemsAdded().length;

        if(creation.getItemsRemoved() != null)
            spaceRequired -= creation.getItemsRemoved().length;

        if(player.getInventory().getSpaces() < spaceRequired) {
            player.message("You don't have enough inventory space to be able to do this.");
            return true;
        }

        //Create items
        for(Item item : creation.getItemsRemoved())
            player.getInventory().delete(item);

        for(Item item : creation.getItemsAdded())
            player.getInventory().add(item);

        if(creation.getSkill() != null && creation.getExperience() != 0) {
            player.getSkills().addExperience(creation.getSkill(), creation.getExperience());
        }
        if(creation.getEmote() != null)player.perform(creation.getEmote());
        if(creation.getGraphic() != null)player.perform(creation.getGraphic());
        return true;
    }
}
