package com.fury.game.content.skill.free.fishing;

import com.fury.core.model.item.Item;
import com.fury.game.world.update.flag.block.Animation;

import java.util.HashMap;
import java.util.Map;

public enum FishingSpots {
    CAVEFISH_SHOAL(8841, 1, 307, 313, new Animation(622), Fish.CAVE_FISH),
    ROCKTAIL_SHOAL(8842, 1, 307, 15263, new Animation(622), Fish.ROCKTAIL),
    NET(327, 1, 303, -1, new Animation(621), Fish.SHRIMP, Fish.ANCHOVIES),
    NET3(326, 1, 303, -1, new Animation(621), Fish.SHRIMP, Fish.ANCHOVIES),
    LURE(328, 1, 309, 314, new Animation(622), Fish.TROUT, Fish.SALMON),
    LURE2(309, 1, 309, 314, new Animation(622), Fish.TROUT,  Fish.SALMON),
    LURE3(310, 1, 309, 314, new Animation(622), Fish.TROUT, Fish.SALMON),
    BAIT(328, 2, 307, 313, new Animation(622), Fish.PIKE),
    BAIT2(316, 2, 307, 313, new Animation(622), Fish.PIKE),
    BAIT3(309, 2, 307, 313, new Animation(622), Fish.PIKE),
    CAGE(6267, 1, 301, -1, new Animation(619), Fish.LOBSTER),
    CAGE2(312, 1, 301, -1, new Animation(619), Fish.LOBSTER),
    HARPOON(312, 2, 311, -1, new Animation(618), Fish.TUNA, Fish.SWORDFISH),
    BIG_NET(313, 1, 305, -1, new Animation(620), Fish.MACKEREL, Fish.COD, Fish.BASS, Fish.SEAWEED, Fish.OYSTER),
    HARPOON2(313, 2, 311, -1, new Animation(618), Fish.SHARK),
    NET2(952, 1, 303, -1, new Animation(621), Fish.SHRIMP),
    MONKFISH(318, 1, 303, -1, new Animation(621), Fish.MONKFISH),
    SMALL_NET(316, 1, 303, -1, new Animation(621), Fish.SHRIMP, Fish.ANCHOVIES);
    private final Fish[] fish;
    private final int id, option;
    private final Item tool, bait;
    private final Animation animation;

    static final Map<Integer, FishingSpots> spot = new HashMap<Integer, FishingSpots>();

    public static FishingSpots forId(int id) {
        return spot.get(id);
    }

    static {
        for (FishingSpots spots : FishingSpots.values())
            spot.put(spots.id | spots.option << 24, spots);
    }

    FishingSpots(int id, int option, int tool, int bait, Animation animation, Fish... fish) {
        this.id = id;
        this.tool = tool == -1 ? null : new Item(tool);
        this.bait = bait == -1 ? null : new Item(bait);
        this.animation = animation;
        this.fish = fish;
        this.option = option;
    }

    public Fish[] getFish() {
        return fish;
    }

    public int getId() {
        return id;
    }

    public int getOption() {
        return option;
    }

    public Item getTool() {
        return tool;
    }

    public Item getBait() {
        return bait;
    }

    public Animation getAnimation() {
        return animation;
    }
}
