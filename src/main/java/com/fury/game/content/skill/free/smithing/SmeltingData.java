package com.fury.game.content.skill.free.smithing;

import com.fury.cache.def.Loader;
import com.fury.game.content.skill.Skill;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import com.fury.game.node.entity.actor.figure.player.Variables;
import com.fury.util.Misc;

public enum SmeltingData {
    BRONZE_BAR(new Item(2349), 438, 436, 1, 6.2, 2405),
    IRON_BAR(new Item(2351), 440, -1, 15, 12.5, 2406),
    SILVER_BAR(new Item(2355), 442, -1, 20, 13.7, 2407),
    STEEL_BAR(new Item(2353), 440, 453, 30, 17.5, 2409),
    GOLD_BAR(new Item(2357), 444, -1, 40, 22.5, 2410),
    MITHRIL_BAR(new Item(2359), 447, 453, 50, 30, 2411),
    ADAMANT_BAR(new Item(2361), 449, 453, 70, 37.5, 2412),
    RUNITE_BAR(new Item(2363), 451, 453, 85, 50, 2413),
    NOVITE_BAR(new Item(17650), 17630, -1, 1, 7, 2405),
    BATHUS_BAR(new Item(17652), 17632, -1, 10, 13.3, 2406),
    MARMAROS_BAR(new Item(17654), 17634, -1, 20, 19.6, 2407),
    KRATONITE_BAR(new Item(17656), 17636, -1, 30, 25.9, 2408),
    FRACTITE_BAR(new Item(17658), 17638, -1, 40, 32.2, 2409),
    ZEPHYRIUM_BAR(new Item(17660), 17640, -1, 50, 38.5, 2410),
    ARGONITE_BAR(new Item(17662), 17642, -1, 60, 44.8, 2411),
    KATAGON_BAR(new Item(17664), 17644, -1, 70, 51.1, 2412),
    GORGONITE_BAR(new Item(17666), 17646, -1, 80, 57.4, 2413),
    PROMETHIUM_BAR(new Item(17668), 17648, -1, 90, 63.7, -1);

    SmeltingData(Item bar, int ore1, int ore2, int levelReq, double experience, int frame) {
        this.bar = bar;
        this.ore1 = ore1;
        this.ore2 = ore2;
        this.levelReq = levelReq;
        this.experience = experience;
        this.frame = frame;
    }

    private Item bar;
    private int ore1, ore2, levelReq, frame;
    private double experience;

    public int getFrameId() {
        return this.frame;
    }

    public Item getBar() {
        return this.bar;
    }

    public int getOre1() {
        return this.ore1;
    }

    public int getOre2() {
        return this.ore2;
    }

    public double getExperience() {
        return experience;
    }

    public int getLevelRequirement() {
        return this.levelReq;
    }

    public int[] getOres() {
        for(SmeltingData data : SmeltingData.values()) {
            if(data.getBar().getId() == this.getBar().getId()) {
                return new int[] {  data.getOre1(), data.getOre2() };
            }
        }
        return null;
    }
    public static SmeltingData forId(int barId) {
        for(SmeltingData data : SmeltingData.values()) {
            if(data.getBar().getId() == barId) {
                return data;
            }
        }
        return null;
    }
    public static SmeltingData forOre(int oreId) {
        for(SmeltingData data : SmeltingData.values()) {
            if(data.getOre1() == oreId) {
                return data;
            }
        }
        return null;
    }
    /*
     * Checks if a player has ores required for a certain barId
     */
    public boolean hasOres(Player player) {
        player.setOres(this.getOres()); //Insert ores ids to the array
        if(player.getOres()[0] > 0 && player.getOres()[1] < 0) {
            if(player.getInventory().getAmount(new Item(player.getOres()[0])) > 0)
                return true;
        } else if(player.getOres()[1] > 0 && player.getOres()[1] != 453 && player.getOres()[0] > 0) {
            if(player.getInventory().getAmount(new Item(player.getOres()[1])) > 0 && player.getInventory().getAmount(new Item(player.getOres()[0])) > 0)
                return true;
        } else if(player.getOres()[1] > 0 && player.getOres()[1] == 453 && player.getOres()[0] > 0) {
            int total = player.getInventory().getAmount(new Item(player.getOres()[1])) + (player.getInventory().contains(new Item(18339)) ? player.getVars().getInt(Variables.COAL_BAG_STORAGE) : 0);
            if(total >= this.getCoalAmount() && player.getInventory().getAmount(new Item(player.getOres()[0])) > 0)
                return true;
        }
        return false;

    }
    public int getCoalAmount() {
        if(this.getBar().getId() == 2359)
            return 4;
        if(this.getBar().getId() == 2361)
            return 6;
        if(this.getBar().getId() == 2363)
            return 8;
        return 2;

    }
    /*
     * Checks if a player has required stats to smelt certain barId
     */
    public boolean canSmelt(Player player) {
        if (!player.getSkills().hasRequirement(Skill.SMITHING, getLevelRequirement(), "make this bar"))
            return false;

        if(!this.hasOres(player)) {
            player.message("You do not have the required ores to make this bar.");
            String requirement = null;

            if(player.getOres()[0] > 0 && player.getOres()[1] > 0 && player.getOres()[1] != 453) {
                requirement = "To make " + Misc.anOrA(bar.getName()) + " " + bar.getName() + ", you need some " + Loader.getItem(player.getOres()[0]).getName().replace(" ore", "") + " and " + Loader.getItem(player.getOres()[1]).getName() + ".";
            } else if(player.getOres()[0] > 0 && player.getOres()[1] == -1) {
                requirement = "To make " + Misc.anOrA(bar.getName()) + " " + bar.getName() + ", you need some " + Loader.getItem(player.getOres()[0]).getName() + ".";
            } else if(player.getOres()[0] > 0 && player.getOres()[1] == 453) { //The bar uses custom coal amount
                requirement = "To make " + Misc.anOrA(bar.getName()) + " " + bar.getName() + ", you need some " + Loader.getItem(player.getOres()[0]).getName().replace(" ore", "") + " and " + this.getCoalAmount() + " " + Loader.getItem(player.getOres()[1]).getName() + " ores.";
            }

            if(requirement != null)
                player.message(requirement);

            return false;
        }
        return true;
    }

}
