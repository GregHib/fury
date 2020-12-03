package com.fury.game.content.skill.member.summoning.impl;

import com.fury.cache.Revision;
import com.fury.cache.def.Loader;
import com.fury.cache.def.item.ItemDefinition;
import com.fury.game.GameSettings;
import com.fury.game.content.dialogue.impl.skills.summoning.DismissD;
import com.fury.game.content.global.Achievements;
import com.fury.game.content.skill.Skill;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import com.fury.game.npc.familiar.Familiar;
import com.fury.game.world.map.Position;
import com.fury.util.NameUtils;

import java.util.HashMap;
import java.util.Map;

public class Summoning {

    public static boolean renewSummoningPoints(Player player) {
        if (!player.getSkills().isFull(Skill.SUMMONING)) {
            if (player.getFamiliar() != null) {
                player.getFamiliar().getHealth().setHitpoints(player.getFamiliar().getMaxConstitution());
                player.getFamiliar().restoreSpecialEnergy(60);
                player.getFamiliar().refreshLevels();
            }

            player.getMovement().lock(3);
            player.graphic(1517);
            player.animate(8502);
            player.getSkills().restore(Skill.SUMMONING);
            player.message("You have recharged your Summoning points.", true);
            return true;
        } else
            player.message("You already have full Summoning points.");
        return false;
    }

    public static boolean hasPouch(Player player) {
        for (Pouches pouch : Pouches.values())
            if (player.getInventory().containsAmount(new Item(pouch.getPouchId())))
                return true;
        return false;
    }

    public static enum Pouches {
        SPIRIT_WOLF(6830, 67, 12047, 1, 0.1, 4.8, 360000, 1, 12425),
        DREADFOWL(6826, 69, 12043, 4, 0.1, 9.3, 240000, 1, 12445),
        MEERKATS(11640, -1, 19622, 4, 0, 0, 2400000, 1, 19621),
        SPIRIT_SPIDER(6842, 83, 12059, 8, 0.2, 12.6, 900000, 2, 12428),
        THORNY_SNAIL(6807, 119, 12019, 13, 0.2, 12.6, 960000, 2, 12459),
        GRANITE_CRAB(6797, 75, 12009, 16, 0.2, 21.6, 1080000, 2, 12533),
        SPIRIT_MOSQUITO(7332, 177, 12778, 17, 0.2, 46.5, 720000, 2, 12838),
        DESERT_WYRM(6832, 121, 12049, 18, 0.4, 31.2, 1140000, 1, 12460),
        SPIRIT_SCORPIAN(6838, 101, 12055, 19, 0.9, 83.2, 1020000, 2, 12432),
        SPIRIT_TZ_KIH(7362, 179, 12808, 22, 1.1, 96.8, 1080000, 3, 12839),
        ALBINO_RAT(6848, 103, 12067, 23, 2.3, 202.4, 1320000, 3, 12430),
        SPIRIT_KALPHITE(6995, 99, 12063, 25, 2.5, 220, 1320000, 3, 12446),
        COMPOST_MOUNT(6872, 137, 12091, 28, 0.6, 49.8, 1440000, 6, 12440),
        GIANT_CHINCHOMPA(7354, 165, 12800, 29, 2.5, 255.2, 1860000, 1, 12834),
        VAMPYRE_BAT(6836, 71, 12053, 31, 1.6, 136, 1980000, 4, 12447),
        HONEY_BADGER(6846, 105, 12065, 32, 1.6, 140.8, 1500000, 4, 12433),
        BEAVER(6808, 89, 12021, 33, 0.7, 57.6, 1620000, 4, 12429),
        VOID_RAVAGER(7371, 157, 12818, 34, 0.7, 59.6, 1620000, 4, 12443),
        VOID_SPINNER(7334, 157, 12780, 34, 0.7, 59.6, 1620000, 4, 12443),
        VOID_TORCHER(7352, 157, 12798, 34, 0.7, 59.6, 5640000, 4, 12443),
        VOID_SHIFTER(7369, 157, 12814, 34, 0.7, 59.6, 5640000, 4, 12443),
        BRONZE_MINOTAUR(6854, 149, 12073, 36, 2.4, 316.8, 1800000, 9, 12461),
        BULL_ANT(6868, 91, 12087, 40, 0.6, 52.8, 1800000, 5, 12431),
        MACAW(6852, 73, 12071, 41, 0.8, 72.4, 1860000, 5, 12422),
        EVIL_TURNIP(6834, 77, 12051, 42, 2.1, 184.8, 1800000, 5, 12448),
        SPIRIT_COCKATRICE(6876, 149, 12095, 43, 0.9, 75.2, 2160000, 5, 12458),
        SPIRIT_GUTHATRICE(6878, 149, 12097, 43, 0.9, 75.2, 2160000, 5, 12458),
        SPIRIT_SARATRICE(6880, 149, 12099, 43, 0.9, 75.2, 2160000, 5, 12458),
        SPIRIT_ZAMATRICE(6882, 149, 12101, 43, 0.9, 75.2, 2160000, 5, 12458),
        SPIRIT_PENGATRICE(6884, 149, 12103, 43, 0.9, 75.2, 2160000, 5, 12458),
        SPIRIT_CORAXATRICE(6886, 149, 12105, 43, 0.9, 75.2, 2160000, 5, 12458),
        SPIRIT_VULATRICE(6888, 149, 12107, 43, 0.9, 75.2, 2160000, 5, 12458),
        IRON_MINOTAUR(6856, 149, 12075, 46, 4.6, 404.8, 2220000, 9, 12462),
        PYRELORD(7378, 185, 12816, 46, 2.3, 202.4, 1920000, 5, 12829),
        MAGPIE(6824, 81, 12041, 47, 0.9, 83.2, 2040000, 5, 12426),
        BLOATED_LEECH(6844, 131, 12061, 49, 2.4, 215.2, 2040000, 5, 12444),
        SPIRIT_TERRORBIRD(6795, 129, 12007, 52, 0.7, 68.4, 2160000, 6, 12441),
        ABYSSAL_PARASITE(6819, 125, 12035, 54, 1.1, 94.8, 1800000, 6, 12454),
        SPIRIT_JELLY(6993, 123, 12027, 55, 5.5, 484, 2580000, 6, 12453),
        STEEL_MINOTAUR(6858, 149, 12077, 56, 5.6, 492.8, 2760000, 9, 12463),
        IBIS(6991, 85, 12531, 56, 1.1, 98.8, 2280000, 6, 12424),
        SPIRIT_KYATT(7364, 169, 12812, 57, 5.7, 501.6, 2940000, 6, 12836),
        SPIRIT_LARUPIA(7366, 181, 12784, 57, 5.7, 501.6, 2940000, 6, 12840),
        SPIRIT_GRAAHK(7338, 167, 12810, 57, 5.6, 501.6, 2940000, 6, 12835),
        KARAMTHULU_OVERLOAD(6810, 135, 12023, 58, 5.8, 510.4, 2640000, 6, 12455),
        SMOKE_DEVIL(6866, 133, 12085, 61, 3.1, 268, 2880000, 7, 12468),
        ABYSSAL_LURKER(6821, 87, 12037, 62, 1.9, 109.6, 2460000, 7, 12427),
        SPIRIT_COBRA(6803, 115, 12015, 63, 3.1, 276.8, 3360000, 7, 12436),
        STRANGER_PLANT(6828, 141, 12045, 64, 3.2, 281.6, 2940000, 7, 12467),
        MITHRIL_MINOTAUR(6860, 149, 12079, 66, 6.6, 580.8, 3300000, 9, 12464),
        BARKER_TOAD(6890, 107, 12123, 66, 1, 87, 480000, 7, 12452),
        WAR_TORTOISE(6816, 117, 12031, 67, 0.7, 58.6, 2580000, 7, 12439),
        BUNYIP(6814, 153, 12029, 68, 1.4, 119.2, 2640000, 7, 12438),
        FRUIT_BAT(6817, 79, 12033, 69, 1.4, 121.2, 2700000, 7, 12423),
        RAVENOUS_LOCUST(7374, 97, 12820, 70, 1.5, 132.0, 1440000, 4, 12830),
        ARCTIC_BEAR(6840, 109, 12057, 71, 1.1, 93.2, 1680000, 8, 12451),
        PHEONIX(8549, -1, 14623, 72, 3, 301, 1800000, 8, -1),
        OBSIDIAN_GOLEM(7346, 173, 12792, 73, 7.3, 642.4, 3300000, 8, 12826),
        GRANITE_LOBSTER(6850, 93, 12069, 74, 3.7, 325.6, 2920000, 8, 12449),
        PRAYING_MANTIS(6799, 95, 12011, 75, 3.6, 329.6, 4140000, 8, 12450),
        FORGE_REGENT(7336, 187, 12782, 76, 1.5, 134, 2700000, 9, 12841),
        ADAMANT_MINOTAUR(6862, 149, 12081, 76, 8.6, 668.8, 3960000, 9, 12465),
        TALON_BEAST(7348, 143, 12794, 77, 3.8, 1015.2, 2940000, 9, 12831),
        GIANT_ENT(6801, 139, 12013, 78, 1.6, 136.8, 2940000, 8, 12457),
        FIRE_TITAN(7356, 159, 12802, 79, 7.9, 695.2, 3720000, 9, 12824),
        MOSS_TITAN(7358, 159, 12804, 79, 7.9, 695.2, 3720000, 9, 12824),
        ICE_TITAN(7360, 159, 12806, 79, 7.9, 695.2, 3720000, 9, 12824),
        HYDRA(6812, 145, 12025, 80, 1.6, 140.8, 2940000, 8, 12442),
        SPIRIT_DAGANNOTH(6805, 147, 12017, 83, 4.1, 364.8, 3420000, 9, 12456),
        LAVA_TITAN(7342, 171, 12788, 83, 8.3, 730.4, 3660000, 9, 12837),
        SWAMP_TITAN(7330, 155, 12776, 85, 4.2, 373.6, 3360000, 9, 12832),
        RUNE_MINOTAUR(6864, 149, 12083, 86, 8.6, 756.8, 9060000, 9, 12466),
        UNICORN_STALLION(6823, 113, 12039, 88, 1.8, 154.4, 3240000, 9, 12434),
        GEYSER_TITAN(7340, 161, 12786, 89, 8.9, 783.2, 4140000, 10, 12833),
        WOLPERTINGER(6870, 151, 12089, 92, 4.6, 404.8, 3720000, 10, 12437),
        ABYSSAL_TITAN(7350, 175, 12796, 93, 1.9, 163.2, 1920000, 10, 12827),
        IRON_TITAN(7376, 183, 12822, 95, 8.6, 417.6, 3600000, 10, 12828),
        PACK_YAK(6874, 111, 12093, 96, 4.8, 422.2, 3480000, 10, 12435),
        STEEL_TITAN(7344, 163, 12790, 99, 4.9, 435.2, 3840000, 10, 12825),

        /**
         * Dungeoneering
         */

        CUB_BLOODRAGER(11107, -1, 17935, 1, 0.5, 5.0, 2700000, 1, 18027),
        CUB_DEATHSLINGER(11207, -1, 17985, 2, 1, 5.7, 2700000, 1, 18037),
        CUB_STORMBRINGER(11127, -1, 17945, 3, .6, 6.4, 2700000, 1, 18047),
        CUB_HOARDSTALKER(11147, -1, 17955, 5, 0.7, 7.1, 2700000, 1, 18057),
        CUB_WORLDBEARER(11187, -1, 17975, 7, 0.9, 7.8, 2700000, 1, 18067),
        CUB_SKINWEAVER(11167, -1, 17965, 9, 0.8, 8.5, 2700000, 1, 18077),

        LITTLE_BLOODRAGER(11109, -1, 17936, 11, 1, 19.5, 2700000, 2, 18028),
        LITTLE_DEATHSLINGER(11209, -1, 17986, 12, 1.5, 20.5, 2700000, 2, 18038),
        LITTLE_STORMBRINGER(11129, -1, 17946, 13, 1.1, 21.5, 2700000, 2, 18048),
        LITTLE_HOARDSTALKER(11149, -1, 17956, 15, 1.2, 22.5, 2700000, 2, 18058),
        LITTLE_WORLDBEARER(11189, -1, 17976, 17, 1.4, 23.5, 2700000, 2, 18068),
        LITTLE_SKINWEAVER(11169, -1, 17966, 19, 1.3, 24.5, 2700000, 2, 18078),

        NAIVE_BLOODRAGER(11111, -1, 17937, 21, 1.5, 43, 2700000, 3, 18029),
        NAIVE_DEATHSLINGER(11211, -1, 17987, 22, 2, 44.4, 2700000, 3, 18039),
        NAIVE_STORMBRINGER(11131, -1, 17947, 23, 1.6, 45.8, 2700000, 3, 18049),
        NAIVE_HOARDSTALKER(11151, -1, 17957, 25, 1.7, 47.2, 2700000, 3, 18059),
        NAIVE_WORLDBEARER(11191, -1, 17977, 27, 1.9, 48.6, 2700000, 3, 18069),
        NAIVE_SKINWEAVER(11171, -1, 17967, 29, 1.8, 50, 2700000, 3, 18079),

        KEEN_BLOODRAGER(11113, -1, 17938, 31, 2, 68.5, 2700000, 4, 18030),
        KEEN_DEATHSLINGER(11213, -1, 17988, 32, 2.5, 70.4, 2700000, 4, 18040),
        KEEN_STORMBRINGER(11133, -1, 17948, 33, 2.1, 72.3, 2700000, 4, 18050),
        KEEN_HOARDSTALKER(11153, -1, 17958, 35, 2.2, 74.2, 2700000, 4, 18060),
        KEEN_WORLDBEARER(11193, -1, 17978, 37, 2.4, 76.1, 2700000, 4, 18070),
        KEEN_SKINWEAVER(11173, -1, 17968, 39, 2.3, 78, 2700000, 4, 18080),

        BRAVE_BLOODRAGER(11115, -1, 17939, 41, 2.5, 99.5, 2700000, 5, 18031),
        BRAVE_DEATHSLINGER(11215, -1, 17989, 42, 3, 102, 2700000, 5, 18041),
        BRAVE_STORMBRINGER(11135, -1, 17949, 43, 2.6, 104.5, 2700000, 5, 18051),
        BRAVE_HOARDSTALKER(11155, -1, 17959, 45, 2.7, 107, 2700000, 5, 18061),
        BRAVE_WORLDBEARER(11195, -1, 17979, 47, 2.9, 109.5, 2700000, 5, 18071),
        BRAVE_SKINWEAVER(11175, -1, 17969, 49, 2.8, 112, 2700000, 5, 18081),

        BRAH_BLOODRAGER(11117, -1, 17940, 51, 3, 157, 2700000, 6, 18032),
        BRAH_DEATHSLINGER(11217, -1, 17990, 52, 3.5, 160.5, 2700000, 6, 18042),
        BRAH_STORMBRINGER(11137, -1, 17950, 53, 3.1, 164, 2700000, 6, 18052),
        BRAH_HOARDSTALKER(11157, -1, 17960, 55, 3.2, 167.5, 2700000, 6, 18062),
        BRAH_WORLDBEARER(11197, -1, 17980, 57, 3.4, 171, 2700000, 6, 18072),
        BRAH_SKINWEAVER(11177, -1, 17970, 59, 3.3, 174.5, 2700000, 6, 18082),

        NAABE_BLOODRAGER(11119, -1, 17941, 61, 3.5, 220, 2700000, 7, 18033),
        NAABE_DEATHSLINGER(11219, -1, 17991, 62, 4, 224.6, 2700000, 7, 18043),
        NAABE_STORMBRINGER(11139, -1, 17951, 63, 3.6, 229.2, 2700000, 7, 18053),
        NAABE_HOARDSTALKER(11159, -1, 17961, 65, 3.7, 233.8, 2700000, 7, 18063),
        NAABE_WORLDBEARER(11199, -1, 17981, 67, 3.9, 238.4, 2700000, 7, 18073),
        NAABE_SKINWEAVER(11179, -1, 17971, 69, 3.8, 243, 2700000, 7, 18083),

        WISE_BLOODRAGER(11121, -1, 17942, 71, 4, 325, 2700000, 8, 18034),
        WISE_DEATHSLINGER(11221, -1, 17992, 72, 4.5, 330.8, 2700000, 8, 18044),
        WISE_STORMBRINGER(11141, -1, 17952, 73, 4.1, 336.6, 2700000, 8, 18054),
        WISE_HOARDSTALKER(11161, -1, 17962, 75, 4.2, 342.4, 2700000, 8, 18064),
        WISE_WORLDBEARER(11201, -1, 17982, 77, 4.4, 348.2, 2700000, 8, 18074),
        WISE_SKINWEAVER(11181, -1, 17972, 79, 4.3, 354, 2700000, 8, 18084),

        ADEPT_BLOODRAGER(11123, -1, 17943, 81, 4.5, 517.5, 2700000, 9, 18035),
        ADEPT_DEATHSLINGER(11223, -1, 17993, 82, 5, 524.6, 2700000, 9, 18045),
        ADEPT_STORMBRINGER(11143, -1, 17953, 83, 4.6, 531.7, 2700000, 9, 18055),
        ADEPT_HOARDSTALKER(11163, -1, 17963, 85, 4.7, 538.8, 2700000, 9, 18065),
        ADEPT_WORLDBEARER(11203, -1, 17983, 87, 4.9, 545.9, 2700000, 9, 18075),
        ADEPT_SKINWEAVER(11183, -1, 17973, 89, 4.8, 553, 2700000, 9, 18085),

        SACHEM_BLOODRAGER(11125, -1, 17944, 91, 5, 810, 2700000, 10, 18036),
        SACHEM_DEATHSLINGER(11225, -1, 17994, 92, 5.5, 818.5, 2700000, 10, 18046),
        SACHEM_STORMBRINGER(11145, -1, 17954, 93, 5.1, 827, 2700000, 10, 18056),
        SACHEM_HOARDSTALKER(11165, -1, 17964, 95, 5.2, 835.5, 2700000, 10, 18066),
        SACHEM_WORLDBEARER(11205, -1, 17984, 97, 5.4, 844, 2700000, 10, 18076),
        SACHEM_SKINWEAVER(11185, -1, 17974, 99, 5.3, 852.5, 2700000, 10, 18086),

        CLAY_FAMILIAR_CLASS_1(8241, -1, 14422, 1, 0, 0, 1800000, 1, 14421),
        CLAY_FAMILIAR_CLASS_2(8243, -1, 14424, 20, 0, 0, 1800000, 3, 14421),
        CLAY_FAMILIAR_CLASS_3(8245, -1, 14426, 40, 0, 0, 1800000, 5, 14421),
        CLAY_FAMILIAR_CLASS_4(8247, -1, 14428, 60, 0, 0, 1800000, 7, 14421),
        CLAY_FAMILIAR_CLASS_5(8249, -1, 14430, 80, 0, 0, 1800000, 10, 14421);

        private static final Map<Integer, Pouches> pouches = new HashMap<>();

        static {
            for (Pouches pouch : Pouches.values()) {
                pouches.put(pouch.pouchId, pouch);
            }
        }

        public static Pouches forId(int id) {
            return pouches.get(id);
        }

        private int npcId;
        private int pouchId;
        private int level;
        private int spawnCost;
        private double useExp;
        private double creationExp;
        private int configId;
        private long time;
        private int scrollId;

        Pouches(int npcId, int configId, int pouchId, int level, double useExp, double creationExp, long time, int spawnCost, int scrollId) {
            this.npcId = npcId;
            this.pouchId = pouchId;
            this.level = level;
            this.spawnCost = spawnCost;
            this.useExp = useExp;
            this.creationExp = creationExp;
            this.time = time;
            this.scrollId = scrollId;
        }

        public int getScrollId() {
            return scrollId;
        }

        public int getNpcId() {
            return npcId;
        }

        public int getConfigId() {
            return configId;
        }

        public int getPouchId() {
            return pouchId;
        }

        public int getLevel() {
            return level;
        }

        public double getUseExp() {
            return useExp;
        }

        public double getCreationExp() {
            return creationExp;
        }

        public long getTime() {
            return time;
        }

        public int getSpawnCost() {
            return spawnCost;
        }

        public static Map<Integer, Pouches> getPouches() {
            return pouches;
        }
    }

    private Pouches pouches;

    public Summoning(Pouches pouches) {
        this.setPouches(pouches);
    }

    public static int calculateScrolls(Player player) {
        if (player.getFamiliar() != null) {
            int scrollId = player.getFamiliar().getPouch().getScrollId();
            return player.getInventory().getAmount(new Item(scrollId));
        }
        return 0;
    }

    public static Familiar createFamiliar(Player player, Summoning.Pouches pouch) {
        try {
            String name = "com.fury.game.npc.familiar.impl." + NameUtils.capitalizeWords(Loader.getNpc(pouch.getNpcId(), Revision.RS2).getName()).replace(" ", "").replace("-", "");
            return (Familiar) Class.forName(name).getConstructor(Player.class, Summoning.Pouches.class, Position.class).newInstance(player, pouch, player);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void spawnFamiliar(Player player, Pouches pouch) {
        if (player.getFamiliar() != null || player.getPet() != null) {
            player.message("You already have a follower.");
            return;
        }

        if (!player.getControllerManager().canSummonFamiliar())
            return;

        if (!player.getSkills().hasMaxRequirement(Skill.SUMMONING, pouch.getLevel(), "order to use this pouch"))
            return;

        if (player.getSkills().getLevel(Skill.SUMMONING) < pouch.getSpawnCost()) {
            player.message("You don't have enough summoning energy to summon this familiar.");
            return;
        }

        ItemDefinition def = Loader.getItem(pouch.getPouchId(), Revision.RS2);
        if (def == null)
            return;
        HashMap<Integer, Integer> skillReq = def.getSkillRequirements();
        boolean hasRequirements = true;
        if (skillReq != null) {
            for (int skillId : skillReq.keySet()) {
                Skill skill = Skill.values()[skillId];
                if (skillId > 24 || skillId < 0)
                    continue;
                int level = skillReq.get(skillId);
                if (level < 0 || level > 120)
                    continue;
                if (!player.getSkills().hasRequirement(skill, level, "summon this")) {
                    if (hasRequirements)
                        player.message("You are not high enough level to use this pouch.");
                    hasRequirements = false;
                }
            }
        }
        if (!hasRequirements || player.getSkills().getLevel(Skill.SUMMONING) < pouch.getSpawnCost())
            return;

        final Familiar npc = createFamiliar(player, pouch);
        if (npc == null) {
            player.message("This familiar is not added yet.");
            return;
        }

        if(pouch == Pouches.DREADFOWL)
            Achievements.finishAchievement(player, Achievements.AchievementData.SUMMON_A_DREADFOWL);

        handleSpawn(player, npc, false);
    }

    public static void handleSpawn(Player player, Familiar npc, boolean login) {
        Pouches pouch = npc.getPouch();
        if (!login) {
            player.getInventory().delete(new Item(pouch.getPouchId(), 1));
            player.getSkills().drain(Skill.SUMMONING, pouch.getSpawnCost());
        }

        player.setFamiliar(npc);

        player.getPacketSender().sendSummoningInfo(true, pouch.getPouchId());
        player.getPacketSender().sendTabInterface(GameSettings.SUMMONING_TAB, 54017);

        String name = npc.getDefinition().getName().replaceAll("_", " ").toLowerCase();
        player.getPacketSender().sendString(54028, name.substring(0, 1).toUpperCase() + name.substring(1));
        player.getFamiliar().refreshLevels();
        player.getPacketSender().sendNpcHeadOnInterface(npc.getId(), npc.getRevision(), 54021); // 60 = invisible head to remove it
        player.getFamiliar().refreshSpecialScrolls();
        player.getPacketSender().sendString(0, "[SUMMOtrue");//TODO wtf is this?
        player.getFamiliar().sendMainConfigs();
    }

    public Pouches getPouches() {
        return pouches;
    }

    public void setPouches(Pouches pouches) {
        this.pouches = pouches;
    }

    public static boolean handleButtons(Player player, int id) {
        switch (id) {
            case 54047:
            case 1022://Special Move
                if (player.getFamiliar() != null) {
                    player.getFamiliar().setSpecial(true);
                    if (player.getFamiliar().getSpecialAttack() == Familiar.SpecialAttack.CLICK && player.getFamiliar().hasSpecialOn())
                        player.getFamiliar().submitSpecial(player);
                }
                return true;
            case 1021://attack
                return true;
            case 1020://interact
                return true;
            case 54032:
            case 1019://Renew familiar
                if (player.getFamiliar() != null)
                    player.getFamiliar().renewFamiliar();
                return true;
            case 1018://Take BoB
            case 54029://Store BoB
                if(player.getFamiliar() != null)
                    player.getFamiliar().takeBob();
                return true;
            case 54227:
            case 54038:
            case 1017://Dismiss
                player.getDialogueManager().startDialogue(new DismissD());
                return true;
            case 54224:
            case 54035:
            case 1016://Call
                if (player.getPet() != null)
                    player.getPet().call();
                else if (player.getFamiliar() != null)
                    player.getFamiliar().call();
                return true;
            case 1015://Left Click
                return true;
        }
        return false;
    }
}
