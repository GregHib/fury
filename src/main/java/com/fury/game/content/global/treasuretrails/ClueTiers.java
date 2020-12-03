package com.fury.game.content.global.treasuretrails;

/**
 * Created by Greg on 27/09/2016.
 */
public enum ClueTiers {
    EASY(2677, 19005, 2714, new ClueTypes[] {ClueTypes.SIMPLE, ClueTypes.EMOTE, ClueTypes.MAP}),
    MEDIUM(2801, 19065, 2802, new ClueTypes[] {ClueTypes.EMOTE, ClueTypes.MAP, ClueTypes.SIMPLE}),
    HARD(2722, 18937, 2724, new ClueTypes[]{ClueTypes.EMOTE, ClueTypes.MAP}),
    ELITE(19064, 19041, 19039, new ClueTypes[] {ClueTypes.SIMPLE, ClueTypes.EMOTE, ClueTypes.MAP}),//19042 - elite puzzle scroll box, 19040 - elite puzzle reward casket
    ;

    private int scrollId, boxId, casketId;
    private ClueTypes[] availableTypes;

    ClueTiers(int scrollId, int boxId, int casketId, ClueTypes[] availableTypes) {
        this.scrollId = scrollId;
        this.boxId = boxId;
        this.casketId = casketId;
        this.availableTypes = availableTypes;
    }

    public ClueTypes[] getAvailableTypes() {
        return availableTypes;
    }

    public final int getScrollId() {
        return scrollId;
    }

    public final int getBoxId() {
        return boxId;
    }

    public final int getCasketId() {
        return casketId;
    }

    public static ClueTiers get(int itemId) {
        for(ClueTiers tier : values())
            if(itemId == tier.getScrollId() || itemId == tier.getBoxId() || itemId == tier.getCasketId())
                return tier;
        return null;
    }
}
