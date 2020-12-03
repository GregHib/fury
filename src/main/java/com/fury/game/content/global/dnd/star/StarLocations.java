package com.fury.game.content.global.dnd.star;

import com.fury.game.world.map.Position;

public enum StarLocations {
    CRAFTING_GUILD_MINING_SITE("Crafting guild", new Position(2940, 3278)),
    MINING_GUILD("East Falador", new Position(3027, 3349)),
    RIMMINGTON_MINING_SITE("Rimmington mine", new Position(2975, 3237)),
    FALADOR_MINING_SITE("West Falador", new Position(2926, 3340)),

    KARAMJA_NORTH_WEST_MINING_SITE("North Brimhaven", new Position(2732, 3219)),
    BRIMHAVEN_MINING_SITE("South Brimhaven", new Position(2743, 3143)),
//    SOUTH_CRANDOR_MINING_SITE("Crandor or Karamja", new Position(2822, 3237)),
    KARAMJA_MINING_SITE("Karamja", new Position(2847, 3028)),
    SHILO_VILLAGE_SITE("Shilo Village", new Position(2826, 2996)),

    KELDAGRIM_ENTRANCE_MINING_SITE("Rellekka", new Position(2724, 3698)),
//    JATIZSO_MINE("Fremennik lands or Lunar Isle", new Position(2397, 3815)),
    LUNAR_ISLE_MINE("Lunar Isle", new Position(2139, 3938)),
    MISCELLANIA_MINING_SITE("Miscellania", new Position(2530, 3887)),
    FREMENNIK_ISLES_MINING_SITE("Fremennik Isle", new Position(2375, 3834)),
    RELLEKKA_MINNING_SITE("Rellekka", new Position(2683, 3699)),

    LEGENDS_GUILD_MINNING_SITE("Legends guild", new Position(2702, 3331)),
    SOUTH_EAST_ARDOUGNE_MINNING_SITE("Ardougne Monastery", new Position(2610, 3220)),
    COAL_TRUCKS("Coal Trucks", new Position(2585, 3477)),
    YANNILLE_BANK("Yannille", new Position(2603, 3086)),
    FIGHT_ARENA_MINING_SITE("Fight Arena", new Position(2628, 3134)),

    ALKHARID_BANK("Al Kharid", new Position(3285, 3181)),
    ALKHARID_MINING_SITE("Al Kharid", new Position(3297, 3297)),
    DUEL_ARENA_BANK_CHEST("Duel Arena", new Position(3341, 3266)),
//    UZER_MINING_SITE("Uzer", new Position(3456, 3136)),
    NARDAH_BANK("Nardah", new Position(3434, 2888)),
    WESTERN_DESERT_MINING_SITE("Kharidian Desert", new Position(3169, 2911)),

    SOUTH_EAST_VARROCK_MINING_SITE("Varrock", new Position(3293, 3352)),
//    LUMBRIDGE_SWAMP_TRAINING_MINING_SITE("Misthalin", new Position(3231, 3155)),//Map is ugly
    SOUTH_WEST_VARROCK_MINING_SITE("Varrock", new Position(3174, 3361)),
    VARROCK_EAST_BANK("Varrock", new Position(3257, 3408)),

//    BURGH_DE_ROTT_BANK("Morytania or Mos Le'Harmless", new Position(3501, 3215)),
    CANIFIS_BANK("Canifis", new Position(3505, 3485)),
//    MOS_LE_HARMLESS_BANK("Mos Le'Harmless", new Position(3685, 2967)),

    GNOME_STRONGHOLD_BANK("Gnome stronghold", new Position(2449, 3436)),
    LTETYA_BANK("Letya", new Position(2329, 3163)),
//    PISCATORIS_MINING_SITE("Piscatoris", new Position(2336, 3636)),

    NORTH_EDGEVILLE_MINING_SITE("Edgeville", new Position(3108, 3569)),
    SOUTHERN_WILDERNESS_MINING_SITE("Wilderness", new Position(3019, 3594)),
    WILDERNESS_VOLCANO("Wilderness", new Position(3188, 3690)),
    BANDIT_CAMP_MINING_SITE("Bandit Camp", new Position(3031, 3795)),
    LAVA_MAZE_RUNITE_MINING_SITE("Lava Maze", new Position(3059, 3888)),
    PIRATES_HIDEOUT_MINING_SITE("Pirates Hideout", new Position(3048, 3944)),
    MAGE_ARENA_BANK("Mage Arena", new Position(3091, 3962));

    private String name;
    private Position location;

    StarLocations(String name, Position location) {
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public Position getPosition() {
        return location;
    }
}