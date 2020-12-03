package com.fury.game.content.global.events.christmas;

import com.fury.game.world.map.Position;

public enum ChristmasPartyCharacters {
    BARBARIAN(6742, new Position(3082, 3416), "Boo! Haha scared you.", "I heard someone say something about a barbarian."),
    DWARF(6744, new Position(3032, 3341), "There's always light at the end of the tunnel", "I recall a dwarf roaming Falador,", "he said he was interested"),
    PIRATE(6745, new Position(3042, 3248), "Aye, the sea isn't frozen enough for me yet", "Someone who doesn't see land too often", "would love a party."),
    LUMBRIDGE(6746, new Position(3235, 3218), "Happy Holidays!", "I overheard someone saying they saw a snowman in lumbridge", "snowmen love my parties!"),
    AL_KHARID(9393, new Position(3276, 3175), "Where am I?", "Someone mentioned a lost party goer by the desert."),
    VARROCK(6743, new Position(3225, 3426), "To battle!", "I hear there's a well armoured snow man", "around varrock."),
    TAVERLEY(9387, new Position(2918, 3437), "Do you like my hat?", "I did hear about a party lover who was working", "on some kind of love potion."),
    RIMMINGTON(9384, new Position(2956, 3220), "I know a Nick, plump fella", "I have a friend in rimmington", "who always loves a party!");

    private int id;
    private Position position;
    private String[] clue;
    private String talk;

    ChristmasPartyCharacters(int id, Position position, String talk, String... clue) {
        this.id = id;
        this.position = position;
        this.talk = talk;
        this.clue = clue;
    }

    public static ChristmasPartyCharacters getCharacter(int id) {
        for (ChristmasPartyCharacters character : values())
            if (id == character.getId())
                return character;

        return null;
    }

    public int getId() {
        return id;
    }

    public Position getPosition() {
        return position;
    }

    public String getTalk() {
        return talk;
    }

    public String[] getClue() {
        return clue;
    }


    public static final String[] randomCatch = new String[]{
            "Oh no! How could I forget Santa!",
            "Right of course, I'll meet you there!",
            "I'll see you there!",
            "Meet you there!",
            "Can't wait!"
    };

    public static final String[] randomParty = new String[]{
            "What a great party!",
            "Haha! You got me!",
            "Nooo! I've been hit!",
            "Darn, I'll get you back!",
            "Merry Christmas!"
    };
}
