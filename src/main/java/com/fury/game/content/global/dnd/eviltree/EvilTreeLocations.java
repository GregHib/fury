package com.fury.game.content.global.dnd.eviltree;

import com.fury.game.world.map.Position;
import com.fury.util.NameUtils;

public enum EvilTreeLocations {
    SEERS_FLAX(new Position(2754, 3424)),
    BRIMHAVEN(new Position(2737, 3157)),
    SEERS_BANK(new Position(2707, 3506)),
    DRAYNOR(new Position(3096, 3227)),
    WEST_TREE_GNOME_STRONGHOLD(new Position(2401, 3431)),
    SOUTH_LEGENDS_GUILD(new Position(2723, 3330)),
    WEST_FALADOR(new Position(2917, 3366)),
    SOUTH_MONESTARY(new Position(3050, 3456)),
    NORTH_SHILO_VILLAGE(new Position(2830, 3013)),
    NORTH_LEGENDS_GUILD(new Position(2720, 3407)),
    NORTH_YANILLE(new Position(2602, 3117)),
    WEST_YANILLE(new Position(2520, 3102)),
    MC_GRUBBERS_WOOD(new Position(2664, 3486)),
    NORTH_DRAYNOR(new Position(3092, 3312)),
    LUMBRIDGE_SWAMP(new Position(3174, 3211)),
    SOUTH_CASTLE_WARS(new Position(2375, 3054)),
//    EAST_MOBILISING_ARMIES(new Position(2467, 2841)),
//    EAST_TYRAS_CAMP(new Position(2212, 3174)),
    SOUTH_LLETYA(new Position(2290, 3122)),
    EAST_VARROCK(new Position(3320, 3457)),
    NORTH_VARROCK(new Position(3220, 3511)),
    EAST_RELLEKKA(new Position(2702, 3640)),
    RANGING_GUILD(new Position(2653, 3415)),
    KHARAZI_JUNGLE(new Position(2913, 2897)),
    APE_ATOL(new Position(2760, 2697)),
    OUTPOST(new Position(2452, 3346));

    Position position;
    String name;
    EvilTreeLocations(Position position) {
        this.position = position;
        name = NameUtils.capitalizeWords(name().toLowerCase().replaceAll("_", " "));
    }

    public Position getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }
}
