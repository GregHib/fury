package com.fury.game.content.skill.member.herblore;

import com.fury.cache.def.Loader;
import com.fury.game.content.skill.Skill;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;

import java.util.LinkedList;
import java.util.List;

public enum Herbs {
    GUAM(199, 2.5, 1, 249),
    MARRENTILL(201, 3.8, 5, 251),
    TARROMIN(203, 5, 11, 253),
    HARRALANDER(205, 6.3, 20, 255),
    RANARR(207, 7.5, 25, 257),
    TOADFLAX(3049, 8, 30, 2998),
    SPIRIT_WEED(12174, 7.8, 35, 12172),
    IRIT(209, 8.8, 40, 259),
    WERGALI(14836, 9.5, 41, 14854),
    AVANTOE(211, 10, 48, 261),
    KWUARM(213, 11.3, 54, 263),
    SNAPDRAGON(3051, 11.8, 59, 3000),
    CADANTINE(215, 12.5, 65, 265),
    LANTADYME(2485, 13.1, 67, 2481),
    DWARF_WEED(217, 13.8, 70, 267),
    TORSTOL(219, 15, 75, 269),
    FELLSTALK(21626, 190, 91, 21624),
    SAGEWORT(17494, 2.1, 3, 17512),
    VALERIAN(17496, 3.2, 4, 17514),
    ALOE(17498, 4, 8, 17516),
    WORMWOOD_LEAF(17500, 7.2, 34, 17518),
    MAGEBANE(17502, 7.7, 37, 17520),
    FEATHERFOIL(17504, 8.6, 41, 17522),
    GRIMY_WINTERS_GRIP(17506, 12.7, 67, 17524),
    LYCOPUS(17508, 13.1, 70, 17526),
    BUCKTHORN(17510, 13.8, 74, 17528);

    private int herbId;
    private int level;
    private int cleanId;
    private double xp;

    Herbs(int herbId, double xp, int level, int cleanId) {
        this.herbId = herbId;
        this.xp = xp;
        this.level = level;
        this.cleanId = cleanId;
    }

    public int getHerbId() {
        return herbId;
    }

    public double getExperience() {
        return xp;
    }

    public int getLevel() {
        return level;
    }

    public int getCleanId() {
        return cleanId;
    }

    public static List<Herbs> getHerbs() {
        List<Herbs> herbs = new LinkedList<>();
        for (Herbs herb : Herbs.values()) {
            if (herb.ordinal() < 17) //Felstalks
                herbs.add(herb);
        }
        return herbs;
    }

    public static Herbs getHerb(int id) {
        for (final Herbs herb : values())
            if (herb.getHerbId() == id)
                return herb;
        return null;
    }

    public static boolean clean(final Player player, Item item) {
        final Herbs herb = getHerb(item.getId());
        if (herb == null)
            return false;
        if (!player.getSkills().hasRequirement(Skill.HERBLORE, herb.getLevel(), "clean this leaf"))
            return true;


        if (item.getId() != herb.getHerbId())//Is this needed?
            return false;
        player.getInventory().delete(new Item(item, 1));
        player.getInventory().add(new Item(herb.getCleanId()));
        player.getSkills().addExperience(Skill.HERBLORE, herb.getExperience());
        player.getPacketSender().sendMessage("You clean the dirt from the " + Loader.getItem(herb.cleanId).getName().replaceAll("Clean", "").toLowerCase().trim() + ".", true);
        return true;
    }
}
