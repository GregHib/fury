package com.fury.game.content.skill.member.hunter;

public enum ImpData {
    BABY("Baby Impling", 11238, 20, 1, 6055),
    YOUNG("Young Impling", 11240, 48, 17, 6056),
    GOURMET("Gourmet Impling", 11244, 82, 34, 6057),
    EARTH("Earth Impling", 11244, 126, 34, 6058),
    ESSENCE("Essence Impling", 11246, 160, 40, 6059),
    ELECTIC("Electic Impling", 11248, 205, 50, 6060),
    NATURE("Nature Impling", 11250, 250, 58, 6061),
    MAGPIE("Magpie Impling", 11252, 289, 65, 6062),
    NINJA("Ninja Impling", 11254, 339, 74, 6063),
    DRAGON("Dragon Impling", 11256, 390, 83, 6064),
    KINGLY("Kingly Impling", 15517, 434, 91, 7903);

    /**
     * Variables.
     */
    public String name;
    public int impJar, XPReward, levelReq, npcId;

    /**
     * Creating the Impling.
     *
     * @param name
     * @param JarAdded
     * @param XPAdded
     * @param LevelNeed
     * @param Npc
     */
    ImpData(String name, int JarAdded, int XPAdded, int LevelNeed, int Npc) {
        this.name = name;
        this.impJar = JarAdded;
        this.XPReward = XPAdded;
        this.levelReq = LevelNeed;
        this.npcId = Npc;
    }

    public static ImpData forId(int npcId) {
        for (ImpData imps : ImpData.values()) {
            if (imps.npcId == npcId) {
                return imps;
            }
        }
        return null;
    }
}
