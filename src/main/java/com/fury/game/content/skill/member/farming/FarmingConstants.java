package com.fury.game.content.skill.member.farming;

import com.fury.core.model.item.Item;
import com.fury.game.world.update.flag.block.Animation;

public class FarmingConstants {
    static final int REGENERATION_CONSTANT = 120000;
    public static final int ALLOTMENT = 0, TREES = 1, HOPS = 2, FLOWERS = 3, FRUIT_TREES = 4, BUSHES = 5, HERBS = 6, COMPOST = 7, MUSHROOMS = 8, BELLADONNA = 9;

    static final Item RAKE = new Item(5341);
    static final Item EMPTY_BUCKET = new Item(1925);
    static final Item SPADE = new Item(952);
    static final Item SEED_DIBBER = new Item(5343);
    static final Item SECATEURS = new Item(5329);
    static final Item MAGIC_SECATEURS = new Item(7409);
    static final Item TROWEL = new Item(5325);
    static final String[] PATCH_NAMES = {"allotment", "tree", "hops", "flower", "fruit tree", "bush", "herb", "compost", "mushroom", "belladonna"};

    static final int[][] HARVEST_AMOUNTS = {
            {3, 53},
            {1, 1},
            {3, 41},
            {1, 3},
            {3, 5},
            {3, 5},
            {3, 18},
            {0, 0},
            {6, 9},
            {1, 1}
    };

    static final int[] COMPOST_ORGANIC = {6055, 1942, 1957, 1965, 5986, 5504, 5982, 249, 251, 253, 255, 257, 2998, 259, 261, 263, 3000, 265, 2481, 267, 269, 1951, 753, 2126, 247, 239, 6018};
    static final int[] SUPER_COMPOST_ORGANIC = {2114, 5978, 5980, 5982, 6004, 247, 6469};
    static final Animation
            RAKING_ANIMATION = new Animation(2273),
            WATERING_ANIMATION = new Animation(2293),
            SEED_DIPPING_ANIMATION = new Animation(2291),
            SPADE_ANIMATION = new Animation(830),
            HERB_PICKING_ANIMATION = new Animation(2282),
            MAGIC_PICKING_ANIMATION = new Animation(2286),
            CURE_PLANT_ANIMATION = new Animation(2288),
            CHECK_TREE_ANIMATION = new Animation(832),
            PRUNING_ANIMATION = new Animation(2275),
            FLOWER_PICKING_ANIMATION = new Animation(2292),
            FRUIT_PICKING_ANIMATION = new Animation(2280),
            COMPOST_ANIMATION = new Animation(2283),
            BUSH_PICKING_ANIMATION = new Animation(2281),
            FILL_COMPOST_ANIMATION = new Animation(832);
}
