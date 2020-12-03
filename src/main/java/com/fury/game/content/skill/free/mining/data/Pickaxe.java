package com.fury.game.content.skill.free.mining.data;

import com.fury.core.model.item.Item;

public enum Pickaxe {
    NOVITE(16295, 1, 13074, 1),
    BATHUS(16297, 10, 13075, 3),
    MARMAROS(16299, 20, 13076, 5),
    KRATONITE(16301, 30, 13077, 7),
    FRACTITE(16303, 40, 13078, 10),
    ZEPHYRIUM(16305, 50, 13079, 12),
    ARGONITE(16307, 60, 13080, 13),
    KATAGON(16309, 70, 13081, 15),
    GORGONITE(16311, 80, 13082, 16),
    PROMETHIUM(16313, 90, 13083, 17),
    PRIMAL(16315, 99, 13084, 20),
    BRONZE(1265, 1, 625, 1),
    IRON(1267, 1, 626, 2),
    STEEL(1269, 6, 627, 3),
    MITHRIL(1273, 21, 629, 5),
    ADAMANT(1271, 31, 628, 7),
    RUNE(1275, 41, 624, 10),
    ADZ(13661, 41, 10222, 14),
    DRAGON(15259, 61, 12189, 14),
    GUILDED_DRAGON(20786, 61, 250, 14);

    private int levelRequired, animationId, pickAxeTime;
    private Item pickAxe;

    Pickaxe(int pickAxeId, int levelRequired, int animationId, int pickAxeTime) {
        this.pickAxe = new Item(pickAxeId);
        this.levelRequired = levelRequired;
        this.animationId = animationId;
        this.pickAxeTime = pickAxeTime;
    }

    public Item getPickAxe() {
        return pickAxe;
    }

    public int getLevelRequired() {
        return levelRequired;
    }

    public int getAnimationId() {
        return animationId;
    }

    public int getPickAxeTime() {
        return pickAxeTime;
    }
}
