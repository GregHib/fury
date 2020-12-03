package com.fury.game.content.skill.free.fishing;

public enum Fish {
    ANCHOVIES(321, 15, 40),
    BASS(363, 46, 100),
    COD(341, 23, 45),
    CAVE_FISH(15264, 85, 300),
    HERRING(345, 10, 30),
    LOBSTER(377, 40, 90),
    MACKEREL(353, 16, 20),
    MANTA(389, 81, 46),
    MONKFISH(7944, 62, 110),
    PIKE(349, 25, 60),
    SALMON(331, 30, 70),
    SARDINES(327, 5, 20),
    SEA_TURTLE(395, 79, 38),
    SEAWEED(401, 30, 0),
    OYSTER(407, 30, 0),
    SHARK(383, 76, 140),
    SHRIMP(317, 1, 10),
    SWORDFISH(371, 50, 100),
    TROUT(335, 20, 50),
    TUNA(359, 35, 80),
    CAVEFISH(15264, 85, 300),
    ROCKTAIL(15270, 90, 385);

    private final int id, level;
    private final double xp;

    Fish(int id, int level, double xp) {
        this.id = id;
        this.level = level;
        this.xp = xp;
    }

    public int getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }

    public double getXp() {
        return xp;
    }
}
