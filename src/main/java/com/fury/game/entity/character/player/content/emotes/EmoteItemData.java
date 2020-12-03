package com.fury.game.entity.character.player.content.emotes;

import com.fury.game.world.update.flag.block.Animation;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.util.Misc;

public enum EmoteItemData {
    FLY_KITE(12844, new Animation[]{new Animation(8990)}, null, 35),
    REINDEER(10507, new Animation[]{new Animation(5059)}, new Graphic[]{new Graphic(859)}, 25),
    PRAYER_BOOK(10890, new Animation[]{new Animation(5864)}, null, 70),
    CHICKEN_DANCE(4566, new Animation[]{new Animation(6400)}, null, 65),
    SPIN_PLATE(4613, new Animation[]{new Animation(1902)}, null, 20),
    YOYO(4079, new Animation[]{new Animation(1457), new Animation(1458), new Animation(1459), new Animation(1460)}, null, new int[]{55, 20, 20, 35}),
    SNOW_GLOBE(11949, new Animation[]{new Animation(7528)}, new Graphic[] {new Graphic(1284)}, 20);

    EmoteItemData(int itemId, Animation[] animations, Graphic[] graphics, int delay) {
        this.itemId = itemId;
        this.animation = animations;
        this.graphic = graphics;
        this.delay = new int[]{delay};
    }

    EmoteItemData(int itemId, Animation[] animations, Graphic[] graphics, int[] delay) {
        this.itemId = itemId;
        this.animation = animations;
        this.graphic = graphics;
        this.delay = delay;
    }

    private int itemId;
    public Animation[] animation;
    public Graphic[] graphic;
    private int[] delay;

    public int[] getDelay() {
        return delay;
    }

    public static EmoteItemData forItem(int item) {
        for (EmoteItemData data : EmoteItemData.values()) {
            if (data != null && data.itemId == item)
                return data;
        }
        return null;
    }

    public static EmoteItemData getRandomEmote() {
        int randomEmote = Misc.getRandom(EmoteItemData.values().length - 1);
        return EmoteItemData.values()[randomEmote];
    }
}
