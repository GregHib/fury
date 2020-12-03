package com.fury.game.content.skill.free.woodcutting;

public enum Hatchet {
    NOVITE(16361, 1, 1, 13118),
    BATHUS(16363, 10, 4, 13119),
    MARMAROS(16365, 20, 5, 13120),
    KRATONITE(16367, 30, 7, 13121),
    FRACTITE(16369, 40, 10, 13122),
    ZEPHYRIUM(16371, 50, 12, 13123),
    ARGONITE(16373, 60, 13, 13124),
    KATAGON(16373, 70, 15, 13125),
    GORGONITE(16375, 80, 17, 13126),
    PROMETHIUM(16379, 90, 19, 13127),
    PRIMAL(16381, 99, 21, 13128),
    BRONZE(1351, 1, 1, 879, 3324, 3324),
    IRON(1349, 1, 2, 877, 2847, 3323),
    STEEL(1353, 5, 3, 875, 880, 3292),
    BLACK(1361, 11, 4, 873, 878, 3284),
    MITHRIL(1355, 21, 5, 871, 876, 3263),
    ADAMANT(1357, 31, 7, 869, 874, 3262),
    RUNE(1359, 41, 10, 867, 872, 3261),
    DRAGON(6739, 61, 13, 2846, 870, 3260),
    INFERNO(13661, 61, 13, 10251, 12323, 3325);
    //Stealing creation 3326

    private int itemId, levelRequired, axeTime, emoteId, ivyEmote, rootEmote;

    Hatchet(int itemId, int levelRequired, int axeTime, int emoteId) {
        this.itemId = itemId;
        this.levelRequired = levelRequired;
        this.axeTime = axeTime;
        this.emoteId = emoteId;
    }

    Hatchet(int itemId, int levelRequired, int axeTime, int emoteId, int ivyEmote, int rootEmote) {
        this(itemId, levelRequired, axeTime, emoteId);
        this.ivyEmote = ivyEmote;
        this.rootEmote = rootEmote;
    }

    public int getItemId() {
        return itemId;
    }

    public int getLevelRequired() {
        return levelRequired;
    }

    public int getAxeTime() {
        return axeTime;
    }

    public int getEmoteId() {
        return emoteId;
    }

    public int getIvyEmoteId() {
        return ivyEmote;
    }

    public int getRootEmoteId() {
        return rootEmote;
    }
}
