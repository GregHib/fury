package com.fury.game.content.skill.member.thieving;

import com.fury.core.model.item.Item;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Greg on 27/12/2016.
 */
public enum PickPocketableNpc {
    MAN(new short[] { 1, 2, 3, 4, 5, 6, 16, 24, 170 }, new byte[] { 1, 11, 21, 31 }, new byte[] { 1, 1, 11, 21 }, 8.0, 4, 10, new Item(995, 1000)),
    DESERT_PHONIX(new short[] { 1911 }, new byte[] { 1, 127, 127, 127 }, new byte[] { 1, 1, 1, 1 }, 26, 4, 15, new Item(4621, 1)),
    FARMER(new short[] { 7, 1757, 1758, 1760 }, new byte[] { 10, 20, 30, 40 }, new byte[] { 1, 10, 20, 30 }, 14.5, 4, 10, new Item(995, 2000), new Item(5319, 3), new Item(5324, 3), new Item(5318, 3)),
    FEMALE_HAM(new short[] { 1715 }, new byte[] { 15, 25, 35, 45 }, new byte[] { 1, 15, 25, 35 }, 18.5, 3, 10, new Item(995, 3000), new Item(590), new Item(1621), new Item(1623), new Item(1625), new Item(1269), new Item(322, 5), new Item(2139, 3), new Item(4298), new Item(4300), new Item(4302), new Item(4304), new Item(4306), new Item(4308), new Item(4310), new Item(1267), new Item(1353), new Item(200, 3), new Item(202, 2), new Item(204, 2), new Item(206, 2), new Item(453), new Item(314, 10)),
    MALE_HAM(new short[] { 1714, 1716 }, new byte[] { 20, 30, 40, 50 }, new byte[] { 1, 20, 30, 40 }, 22.5, 3, 20, new Item(995, 3500), new Item(590), new Item(1621), new Item(1623), new Item(1625), new Item(1269), new Item(322, 5), new Item(2139, 3), new Item(4298), new Item(4300), new Item(4302), new Item(4304), new Item(4306), new Item(4308), new Item(4310), new Item(1273), new Item(1355), new Item(200, 3), new Item(202, 2), new Item(204, 2), new Item(206, 2), new Item(453), new Item(314, 10)),
    HAM_GUARD(new short[] { 1710, 1711, 1712 }, new byte[] { 20, 30, 40, 50 }, new byte[] { 1, 20, 30, 40 }, 22.5, 3, 30, new Item(995, 2500), new Item(590), new Item(1621), new Item(1623), new Item(1625), new Item(1269), new Item(322, 5), new Item(2139, 3), new Item(4298), new Item(4300), new Item(4302), new Item(4304), new Item(4306), new Item(4308), new Item(4310), new Item(1361), new Item(200, 3), new Item(202, 2), new Item(204, 2), new Item(206, 2), new Item(453), new Item(314, 10), new Item(8866), new Item(8867), new Item(8868), new Item(8869)),
    WARRIOR(new short[] { 15, 18 }, new byte[] { 25, 35, 45, 55 }, new byte[] { 1, 25, 35, 45 }, 26, 5, 20, new Item(995, 2500)),
    ROGUE(new short[] { 187, 2267, 2268, 2269, 8122 }, new byte[] { 32, 42, 52, 62 }, new byte[] { 1, 32, 42, 52 }, 35.5, 4, 20, new Item(995, 3000), new Item(995, 4000), new Item(1993), new Item(556, 25), new Item(1219)),
    CAVE_GOBLIN(new short[] { 5752, 5753, 5754, 5755, 5756, 5757, 5758, 5759, 5760, 5761, 5762, 5763, 5764, 5765, 5766, 5767, 5768 }, new byte[] { 36, 46, 56, 66 }, new byte[] { 1, 36, 46, 56 }, 40, 4, 10, new Item(995, 5000), new Item(590), new Item(4522), new Item(1939, 7), new Item(441, 5), new Item(441, 2)),
    MASTER_FARMER(new short[] { 2234, 2235 }, new byte[] { 38, 48, 58, 68 }, new byte[] { 1, 38, 48, 58 }, 43, 4, 30, new Item(5096, 3), new Item(5097, 3), new Item(5098, 3), new Item(5099, 2), new Item(5100, 2), new Item(5101, 3), new Item(5102, 3), new Item(5103, 3), new Item(5104, 2), new Item(5105, 2), new Item(5106, 2), new Item(5291, 3), new Item(5292, 3), new Item(5293, 3), new Item(5294, 3), new Item(5295, 2), new Item(5296, 3), new Item(5297, 3), new Item(5298), new Item(5299, 3), new Item(5300), new Item(5301, 2), new Item(5302, 2), new Item(5304), new Item(5305, 3), new Item(5306, 3), new Item(5307, 3), new Item(5308, 3), new Item(5309, 2), new Item(5310, 2), new Item(5311, 2), new Item(5312, 2), new Item(5318, 4), new Item(5319, 4), new Item(5320, 3), new Item(5321, 2), new Item(5322, 3), new Item(5323, 2), new Item(5324, 3)),
    GUARD(new short[] { 9, 32, 206, 296, 297, 298, 299, 344, 345, 346, 368, 678, 812 }, new byte[] { 40, 50, 60, 70 }, new byte[] { 1, 40, 50, 60 }, 46.5, 4, 20, new Item(995, 3000)),
    ARDOUGNE_KNIGHT(new short[] { 23, 26 }, new byte[] { 55, 65, 75, 85 }, new byte[] { 1, 55, 65, 75 }, 84.3, 4, 30, new Item(995, 3500)),
    MENAPHITE_THUG(new short[] { 1905 }, new byte[] { 65, 75, 85, 95 }, new byte[] { 1, 65, 75, 85 }, 137.5, 4, 50, new Item(995, 3750)),
    PALADIN(new short[] { 20, 2256 }, new byte[] { 70, 80, 90, 100 }, new byte[] { 1, 70, 80, 90 }, 151.75, 4, 30, new Item(995, 4000), new Item(562, 10)),
    MONKEY_KNIFE_FIGHTER(new short[] { 13212 }, new byte[] { 70, 127, 127, 127 }, new byte[] { 1, 1, 1, 1 }, 150, 4, 10, new Item(995, 1000), new Item(995, 2500), new Item(869, 4), new Item(874, 2), new Item(379), new Item(1331), new Item(1333), new Item(4587), new Item(864, 5), new Item(863, 5), new Item(865, 5), new Item(864, 5), new Item(866, 5), new Item(867, 5), new Item(868, 5)),
    GNOME(new short[] { 66, 67, 68, 168, 169, 2249, 2250, 2251, 2371, 2649, 2650, 6002, 6004 }, new byte[] { 75, 85, 95, 105 }, new byte[] { 1, 75, 85, 95 }, 198.5, 4, 10, new Item(995, 5000), new Item(557, 30), new Item(445, 2), new Item(570, 2)),
    HERO(new short[] { 21 }, new byte[] { 80, 90, 100, 110 }, new byte[] { 1, 80, 90, 100 }, 275, 5, 40, new Item(995, 200), new Item(995, 5500), new Item(560, 8), new Item(565, 4), new Item(570, 3), new Item(1601), new Item(445, 3), new Item(1993)),
    DWARF_TRADER(new short[] { 2109, 2110, 2111, 2112, 2113, 2114, 2115, 2116, 2117, 2118, 2119, 2120, 2121, 2122, 2123, 2124, 2125, 2126 }, new byte[] { 90, 100, 110, 120 }, new byte[] { 1, 90, 100, 110 }, 556.5, 6, 10, new Item(995, 4500), new Item(995, 6000), new Item(2350, 3), new Item(2352, 3), new Item(2354, 3), new Item(2360, 2), new Item(2362, 2), new Item(2364), new Item(437, 5), new Item(439, 5), new Item(441, 4), new Item(448, 3), new Item(450, 2), new Item(452), new Item(454, 4)),
    DONOR_TRADER(new short[] { 8827 }, new byte[] { 1, 50, 75, 99 }, new byte[] { 1, 50, 75, 99 }, 300, 4, 8, new Item(995, 15000), new Item(995, 25000), new Item(995, 35000), new Item(995, 45000), new Item(995, 50000), new Item(6721, 1)),
    THIEVING_GUILD_STATICS(new short[] { 11281, 11282, 11284, 11286, 11296 }, new byte[] { 50, 70, 85, 95 }, new byte[] { 50, 70, 85, 95 }, 150, 4, 0, new Item(995, 20000)),
    THIEVING_GUILD_MASTER(new short[] { 11275 }, new byte[] { 1, 40, 65, 90 }, new byte[] { 1, 40, 65, 90 }, 300, 4, 0, new Item(995, 100000));

    /**
     * A hashmap containing all the npc pickpocketing data.
     */
    private static final Map<Short, PickPocketableNpc> NPCS = new HashMap<Short, PickPocketableNpc>();

    /**
     * Gets the pickpocketing data from the mapping, depending on the Npc id.
     *
     * @param id
     *            The npc id.
     * @return The {@code PickpocketableNPC} {@code Object}, or {@code null} if
     *         the data was non-existant.
     */
    public static PickPocketableNpc get(int id) {
        return NPCS.get((short) id);
    }

    /**
     * Populate the mapping.
     */
    static {
        for (PickPocketableNpc data : PickPocketableNpc.values()) {
            for (short id : data.npcIds) {
                NPCS.put(id, data);
            }
        }
    }

    /**
     * The npc ids.
     */
    private final short[] npcIds;

    /**
     * The thieving levels required (slot 0 = normal loot, 1 = double, 2 = 3x
     * loot, 4 = 4x loot).
     */
    private final byte[] thievingLevels;

    /**
     * The agility levels required (see thievingLevels[] comment for slots).
     */
    private final byte[] agilityLevels;

    /**
     * The experience gained.
     */
    private final double experience;

    /**
     * The stun time.
     */
    private final byte stunTime;

    /**
     * The stun damage to deal.
     */
    private final byte stunDamage;

    /**
     * The possible loot.
     */
    private final Item[] loot;

    /**
     * Constructs a new {@code PickpocketableNPC} {@code Object}.
     *
     * @param npcIds
     *            The array containing all the npc ids of the npcs using this
     *            pickpocket data.
     * @param thievingLevel
     *            The thieving levels required (slot 0 = normal loot, 1 =
     *            double, 2 = 3x loot, 4 = 4x loot).
     * @param agilityLevel
     *            The agility levels required (see slots above).
     * @param experience
     *            The experience gained.
     * @param stunTime
     *            The stun time (per sec).
     * @param stunDamage
     *            The stun damage to deal when stunned.
     * @param loot
     *            The possible loot.
     */
    private PickPocketableNpc(short[] npcIds, byte[] thievingLevel, byte[] agilityLevel, double experience, int stunTime, int stunDamage, Item... loot) {
        this.npcIds = npcIds;
        this.thievingLevels = thievingLevel;
        this.agilityLevels = agilityLevel;
        this.experience = experience;
        this.stunTime = (byte) stunTime;
        this.stunDamage = (byte) stunDamage;
        this.loot = loot;
    }

    /**
     * @return the npcIds
     */
    public short[] getNpcIds() {
        return npcIds;
    }

    /**
     * @return the thievingLevels
     */
    public byte[] getThievingLevels() {
        return thievingLevels;
    }

    /**
     * @return the agilityLevels
     */
    public byte[] getAgilityLevels() {
        return agilityLevels;
    }

    /**
     * @return the experience
     */
    public double getExperience() {
        return experience;
    }

    /**
     * @return the stunTime
     */
    public byte getStunTime() {
        return stunTime;
    }

    /**
     * @return the stunDamage
     */
    public byte getStunDamage() {
        return stunDamage;
    }

    /**
     * @return the loot
     */
    public Item[] getLoot() {
        return loot;
    }
}
