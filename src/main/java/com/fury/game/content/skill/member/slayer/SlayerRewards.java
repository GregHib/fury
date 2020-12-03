package com.fury.game.content.skill.member.slayer;

import com.fury.core.model.item.Item;
import com.fury.game.content.skill.Skill;

/**
 * Created by Greg on 21/06/2016.
 */
public class SlayerRewards {
    public static enum Buy {
        SLAYER_XP("Slayer XP", 36024, Skill.SLAYER, 10000, null, 400),
        RING_OF_SLAYING("Ring of Slaying", 36025, null, -1, new Item[] {new Item(13281, 1) }, 75),
        SLAYER_DART("Slayer Dart", 36026, null, -1, new Item[] { new Item(560, 250), new Item(556, 750) }, 35),
        BROAD_BOLT("Broad-Tipped Bolts", 36027, null, -1, new Item[] { new Item(13280, 250) }, 35),
        BROAD_ARROWS("Broad Arrows", 36028, null, -1, new Item[] { new Item(4160, 250) }, 35);

        private int buttonId, experience, cost;
        private String name;
        private Skill skill;
        private Item[] items;

        private Buy(String name, int buttonId, Skill skill, int experience, Item[] items, int cost) {
            this.name = name;
            this.buttonId = buttonId;
            this.skill = skill;
            this.experience = experience;
            this.items = items;
            this.cost = cost;
        }

        public String getName() {
            return name;
        }

        public int getButtonId() {
            return buttonId;
        }

        public Skill getSkill() {
            return skill;
        }

        public int getExp() {
            return experience;
        }

        public Item[] getItems() {
            return items;
        }

        public int getCost() {
            return cost;
        }

        public static Buy forId(int i) {
            for(Buy reward : values()) {
                if(reward.ordinal() == i) {
                    return reward;
                }
            }
            return null;
        }
    }
    public static enum Learn {
        FLETCH_BROAD(36054, "Learn how to fletch broad", "arrows/bolts", 300),
        CRAFT_RINGS(36061, "Learn how to craft rings", "of slaying", 300),
        CRAFT_HELMETS(36068, "Learn how to craft Slayer", "helmets", 400),
        AQUANITES(36075, "Persuade Kuradal to", "assign aquanites", 50),
        KILLING_BLOWS(36082, "Learn how to deliver", "killing blows quicker", 400),
        ICE_STRYKEWYRMS(36089, "Learn new technique to", "attack ice strykewyrms", 2000);

        private int buttonId, cost;
        private String lineOne, lineTwo;

        private Learn(int buttonId, String descriptionLine1, String descriptionLine2, int cost) {
            this.buttonId = buttonId;
            this.lineOne = descriptionLine1;
            this.lineTwo = descriptionLine2;
            this.cost = cost;
        }
        public int getButtonId() {
            return buttonId;
        }

        public String getLineOne() {
            return lineOne;
        }

        public String getLineTwo() {
            return lineTwo;
        }

        public int getCost() {
            return cost;
        }

        public static Learn forId(int i) {
            for(Learn reward : values()) {
                if(reward.ordinal() == i) {
                    return reward;
                }
            }
            return null;
        }
    }
}
