package com.fury.game.content.skill.free.crafting.glass;

/**
 * Created by Jon on 10/2/2016.
 */
public enum Glass {
    BEER_GLASS(new int[][]{{12400, 1}, {12399, 5}, {12398, 10}, {12397, 28}}, 1919, 1, 1, 19.5, "Beer Glass"),
    CANDLE_LANTERN(new int[][]{{12404, 1}, {12403, 5}, {12402, 10}, {12401, 28}}, 4527, 1, 4, 19.0, "Candle Lantern"),
    OIL_LAMP(new int[][]{{12408, 1}, {12407, 5}, {12406, 10}, {12405, 28}}, 4522, 1, 12, 25.0, "Oil Lamp"),
    VIAL(new int[][]{{11474, 1}, {11473, 5}, {11472, 10}, {11471, 28}}, 229, 1, 33, 35, "Vial"),
    FISHBOWL(new int[][]{{6203, 1}, {6202, 5}, {6201, 10}, {6200, 28}}, 6667, 1, 42, 42.5, "Fishbowl"),
    GLASS_ORB(new int[][]{{12396, 1}, {12395, 5}, {12394, 10}, {11475, 28}}, 567, 1, 46, 52.5, "Glass Orb"),
    BULLSEYE_LANTERN_LENS(new int[][]{{12412, 1}, {12411, 5}, {12410, 10}, {12409, 28}}, 4542, 1, 49, 55.0, "Lantern Lens");

    private int[][] buttonId;
    private int newId, needed, levelReq;
    private double xp;
    private String name;

    Glass(final int[][] buttonId, final int newId, final int needed, final int levelReq, final double xp, String name) {
        this.buttonId = buttonId;
        this.newId = newId;
        this.needed = needed;
        this.levelReq = levelReq;
        this.xp = xp;
        this.name = name;
    }

    public int getButtonId(final int button) {
        for (int i = 0; i < buttonId.length; i++) {
            if (button == buttonId[i][0]) {
                return buttonId[i][0];
            }
        }
        return -1;
    }

    public int getAmount(final int button) {
        for (int i = 0; i < buttonId.length; i++) {
            if (button == buttonId[i][0]) {
                return buttonId[i][1];
            }
        }
        return -1;
    }

    public int getNewId() {
        return newId;
    }

    public int getNeeded() {
        return needed;
    }

    public int getLevelReq() {
        return levelReq;
    }

    public double getXP() {
        return xp;
    }

    public String getName() {
        return name;
    }
}