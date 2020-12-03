package com.fury.game.content.global.dnd.eviltree;

public enum EvilTreeClues {
    YEW_CLUE("Close to a collection of yew trees", EvilTreeLocations.SEERS_FLAX, EvilTreeLocations.LUMBRIDGE_SWAMP),
    DUNGEON_CLUE(new String[] {"Close to a dungeon, entrance", "and within a tropical jungle"}, EvilTreeLocations.APE_ATOL, EvilTreeLocations.BRIMHAVEN),
    WILLOW_CLUE(new String[] {"Close to a large collection of", "willow trees"}, EvilTreeLocations.SEERS_BANK, EvilTreeLocations.DRAYNOR, EvilTreeLocations.SOUTH_LEGENDS_GUILD, EvilTreeLocations.WEST_TREE_GNOME_STRONGHOLD),
    MINE_CLUE(new String[] {"Close to a mine on the outskirts", "of a city"}, EvilTreeLocations.SOUTH_LEGENDS_GUILD, EvilTreeLocations.WEST_FALADOR),
    RUNECRAFTING_CLUE("Close to a Runecrafting altar", EvilTreeLocations.SOUTH_MONESTARY, EvilTreeLocations.NORTH_SHILO_VILLAGE),
    LEGENDS_CLUE("Close to the home of 'Legends'", EvilTreeLocations.NORTH_LEGENDS_GUILD, EvilTreeLocations.SOUTH_LEGENDS_GUILD),
    YANILLE_CLUE("Close to the town you call Yanille", EvilTreeLocations.NORTH_YANILLE, EvilTreeLocations.WEST_YANILLE),
    SEERS_CLUE("Close to the village you call 'Seers'", EvilTreeLocations.MC_GRUBBERS_WOOD, EvilTreeLocations.SEERS_BANK, EvilTreeLocations.SEERS_FLAX),
    DRAYNOR_CLUE("Close to the village you humans call 'Draynor'", EvilTreeLocations.NORTH_DRAYNOR, EvilTreeLocations.DRAYNOR),
    LUMBRIDGE_CLUE("Due west of the town you call Lumbridge", EvilTreeLocations.DRAYNOR, EvilTreeLocations.LUMBRIDGE_SWAMP),
    OGRE_CLUE(new String[] {"In a location with rare trees", "and ogres nearby"}, EvilTreeLocations.SOUTH_CASTLE_WARS/*, EvilTreeLocations.EAST_MOBILISING_ARMIES*/),
    ELVES_CLUE("In the lands inhabited by elves"/*, EvilTreeLocations.EAST_TYRAS_CAMP*/, EvilTreeLocations.SOUTH_LLETYA),
    VARROCK_CLUE(new String[] {"Just outside of the city", "you call Varrock"}, EvilTreeLocations.EAST_VARROCK, EvilTreeLocations.NORTH_VARROCK),
    RELLEKKA_CLUE(new String[] {"North as the crow flies", "from Seers' Village"}, EvilTreeLocations.EAST_RELLEKKA, EvilTreeLocations.SEERS_BANK),
    ARDOUGNE_CLUE(new String[] {"North as the crow flies", "from the market of Ardougne"}, EvilTreeLocations.MC_GRUBBERS_WOOD, EvilTreeLocations.RANGING_GUILD),
    KARAMJA_CLUE("On the island known as Karamja", EvilTreeLocations.KHARAZI_JUNGLE, EvilTreeLocations.NORTH_SHILO_VILLAGE),
    ISLAND_CLUE("On the southern coast of a tropical island", EvilTreeLocations.APE_ATOL, EvilTreeLocations.KHARAZI_JUNGLE),
    GNOME_CLUE(new String[] {"To the south of a tree", "gnome settlement"}, EvilTreeLocations.OUTPOST, EvilTreeLocations.WEST_YANILLE);

    String[] clue;
    EvilTreeLocations[] locations;

    EvilTreeClues(String clue, EvilTreeLocations... locations) {
        this(new String[] {clue}, locations);
    }

    EvilTreeClues(String[] clue, EvilTreeLocations... locations) {
        this.clue = clue;
        this.locations = locations;
    }
}
