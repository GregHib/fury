package com.fury.game.content.skill.free.crafting.spinning;

import com.fury.core.model.item.Item;

public enum SpinData {
    BALL_OF_WOOL(1737, 1759, 1, 2.5),
    BALL_OF_BLACK_WOOL(15415, 15416, 1, 2.5),
    BOW_STRING(1779, 1777, 1, 15),
    CROSSBOW_STRING(9436, 9438, 10, 15),
    MAGIC_STRING(6051, 6038, 19, 30),
    ROPE(10814, 954, 30, 25),
    SALVE_CLOTH(17448, 17468, 1, 2.5),
    WILDERCRESS_CLOTH(17450, 17470, 10, 3.0),
    BLIGHTLEAF_CLOTH(17452, 17472, 20, 3.5),
    ROSEBLOOD_CLOTH(17454, 17474, 30, 3.0),
    BRYLL_CLOTH(17456, 17476, 40, 3.5),
    DUSKWEED_CLOTH(17458, 17478, 50, 6.5),
    SOULBELL_CLOTH(17460, 17480, 60, 7.5),
    ECTOCLOTH(17462, 17482, 70, 9.0),
    RUNIC_CLOTH(17464, 17484, 80, 11),
    SPIRITBLOOM_CLOTH(17466, 17486, 90, 12)
    ;

    public Item getIngredient() {
        return ingredient;
    }

    public Item getProduct() {
        return product;
    }

    public int getLevel() {
        return level;
    }

    public double getExperience() {
        return experience;
    }
    Item ingredient, product;
    int level;
    double experience;

    SpinData(int ingredient, int product, int level, double experience) {
        this.ingredient = new Item(ingredient);
        this.product = new Item(product);
        this.level = level;
        this.experience = experience;
    }

    public static SpinData forId(int id) {
        for(SpinData s : values()) {
            if(id == s.getProduct().getId()) {
                return s;
            }
        }
        return null;
    }
}