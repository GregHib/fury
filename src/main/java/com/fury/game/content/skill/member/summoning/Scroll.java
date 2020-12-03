package com.fury.game.content.skill.member.summoning;

import com.fury.cache.Revision;
import com.fury.core.model.item.Item;

public enum Scroll {
    HOWL(12425, 12047, 1, 0.1, 3),
    DREADFOWL_STRIKE(12445, 12043, 4, 0.1, 3),
    EGG_SPAWN(12428, 12059, 10, 0.2, 6),
    SLIME_SPRAY(12459, 12019, 13, 0.2, 3),
    STONY_SHELL(12533, 12009, 16, 0.2, 12),
    PESTER(12838, 12778, 17, 0.2, 3),
    ELECTRIC_LASH(12460, 12049, 18, 0.4, 6),
    VENOM_SHOT(12432, 12055, 19, 0.9, 6),
    FIREBALL_ASSAULT(12839, 12808, 22, 1.1, 6),
    CHEESE_FEAST(12430, 12067, 23, 2.3, 6),
    SANDSTORM(12446, 12063, 25, 2.5, 6),
    GENERATE_COMPOST(12440, 12091, 28, 0.6, 12),
    EXPLODE(12834, 12800, 29, 2.5, 3),
    VAMPYRE_TOUCH(12447, 12053, 31, 1.6, 4),
    INSANE_FEROCITY(12433, 12065, 32, 1.6, 4),
    MULTICHOP(12429, 12021, 33, 0.7, 3),
    CALL_TO_ARMS1(12443, 12818, 34, 0.7, 3),
    CALL_TO_ARMS2(12443, 12780, 34, 0.7, 3),
    CALL_TO_ARMS3(12443, 12798, 34, 0.7, 3),
    CALL_TO_ARMS4(12443, 12814, 34, 0.7, 3),
    BRONZE_BULL_RUSH(12461, 12073, 36, 2.4, 6),
    UNBURDEN(12431, 12087, 40, 0.6, 12),
    HERBCALL(12422, 12071, 41, 0.8, 12),
    EVIL_FLAMES(12448, 12051, 42, 2.1, 6),
    PETRIFYING_GAZE1(12458, 12095, 43, 0.9, 3),
    PETRIFYING_GAZE2(12458, 12097, 43, 0.9, 3),
    PETRIFYING_GAZE3(12458, 12099, 43, 0.9, 3),
    PETRIFYING_GAZE4(12458, 12101, 43, 0.9, 3),
    PETRIFYING_GAZE5(12458, 12103, 43, 0.9, 3),
    PETRIFYING_GAZE6(12458, 12105, 43, 0.9, 3),
    PETRIFYING_GAZE7(12458, 12107, 43, 0.9, 3),
    IRON_BULL_RUSH(12462, 12075, 46, 4.6, 6),
    IMMENSE_HEAT(12829, 12816, 46, 2.3, 6),
    THIEVING_FINGERS(12426, 12041, 47, 0.9, 3),
    BLOOD_DRAIN(12444, 12061, 49, 2.4, 6),
    TIRELESS_RUN(12441, 12007, 52, 0.7, 8),
    ABYSSAL_DRAIN(12454, 12035, 54, 1.1, 6),
    DISSOLVE(12453, 12027, 55, 5.5, 6),
    STEEL_BULL_RUSH(12463, 12077, 56, 5.6, 6),
    FISH_RAIN(12424, 12531, 56, 1.1, 12),
    AMBUSH(12836, 12812, 57, 5.7, 3),
    RENDING(12840, 12784, 57, 5.7, 6),
    GOAD(12835, 12810, 57, 5.6, 3),
    DOOMSPHERE(12455, 12023, 58, 5.8, 3),
    DUST_CLOUD(12468, 12085, 61, 3.1, 6),
    ABYSSAL_STEALTH(12427, 12037, 62, 1.9, 3),
    OPHIDIAN_INCUBATION(12436, 12015, 63, 3.1, 3),
    POISONOUS_BLAST(12467, 12045, 64, 3.2, 6),
    MITH_BULL_RUSH(12464, 12079, 66, 6.6, 6),
    TOAD_BARK(12452, 12123, 66, 1.0, 6),
    TESTUDO(12439, 12031, 67, 0.7, 20),
    SWALLOW_WHOLE(12438, 12029, 68, 1.4, 3),
    FRUITFALL(12423, 12033, 69, 1.4, 6),
    FAMINE(12830, 12820, 70, 1.5, 12),
    ARCTIC_BLAST(12451, 12057, 71, 1.1, 6),
    RISE_FROM_THE_ASHES(14622, 14623, 72, 3.0, 5),
    VOLCANIC_STRENGTH(12826, 12792, 73, 7.3, 12),
    CRUSHING_CLAW(12449, 12069, 74, 3.7, 6),
    MANTIS_STRIKE(12450, 12011, 75, 3.6, 6),
    INFERNO(12465, 12782, 76, 1.5, 6),
    ADDY_BULL_RUSH(12841, 12081, 76, 8.6, 6),
    DEADLY_CLAW(12831, 12794, 77, 3.8, 6),
    ACORN_MISSILE(12457, 12013, 78, 1.6, 6),
    TITANS_CONSITUTION1(12824, 12802, 79, 7.9, 20),
    TITANS_CONSITUTION2(12824, 12804, 79, 7.9, 20),
    TITANS_CONSITUTION3(12824, 12806, 79, 7.9, 20),
    REGROWTH(12442, 12025, 80, 1.6, 8),
    SPIKE_SHOT(12456, 12017, 83, 4.1, 6),
    EBON_THUNDER(12837, 12788, 83, 8.3, 4),
    SWAMP_PLAGUE(12832, 12776, 85, 4.2, 6),
    RUNE_BULL_RUSH(12466, 12083, 86, 8.6, 6),
    HEALING_AURA(12434, 12039, 88, 1.8, 20),
    BOIL(12833, 12786, 89, 8.9, 6),
    MAGIC_FOCUS(12437, 12089, 92, 4.6, 20),
    ESSENCE_SHIPMENT(12827, 12796, 93, 1.9, 6),
    IRON_WITHIN(12828, 12822, 95, 8.6, 12),
    WINTER_STORAGE(12435, 12093, 96, 4.8, 12),
    STEEL_OF_LEGENDS(12825, 12790, 99, 4.9, 18),

    SUNDERING_STRIKE_TIER_1(18027, 17935, 1, 0.5, 4),
    POISONOUS_SHOT_TIER_1(18037, 17985, 2, 1.0, 6),
    SNARING_WAVE_TIER_1(18047, 17945, 3, 0.6, 8),
    APTITUDE_TIER_1(18057, 17955, 5, 0.7, 10),
    SECOND_WIND_TIER_1(18067, 17975, 7, 0.9, 12),
    GLIMMER_OF_LIGHT_TIER_1(18077, 17965, 9, 0.8, 14),

    SUNDERING_STRIKE_TIER_2(18028, 17936, 11, 1.0, 2),
    POISONOUS_SHOT_TIER_2(18038, 17986, 12, 1.5, 2),
    SNARING_WAVE_TIER_2(18048, 17946, 13, 1.1, 2),
    APTITUDE_TIER_2(18058, 17956, 15, 1.2, 2),
    SECOND_WIND_TIER_2(18068, 17976, 17, 1.4, 2),
    GLIMMER_OF_LIGHT_TIER_2(18078, 17966, 19, 1.3, 2),
    SUNDERING_STRIKE_TIER_3(18029, 17937, 21, 1.5, 4),
    POISONOUS_SHOT_TIER_3(18039, 17987, 22, 2.0, 4),
    SNARING_WAVE_TIER_3(18049, 17947, 23, 1.6, 4),
    APTITUDE_TIER_3(18059, 17957, 25, 1.7, 4),
    SECOND_WIND_TIER_3(18069, 17977, 27, 1.9, 4),
    GLIMMER_OF_LIGHT_TIER_3(18079, 17967, 29, 1.8, 4),
    SUNDERING_STRIKE_TIER_4(18030, 17938, 31, 2.0, 6),
    POISONOUS_SHOT_TIER_4(18040, 17988, 32, 2.5, 6),
    SNARING_WAVE_TIER_4(18050, 17948, 33, 2.1, 6),
    APTITUDE_TIER_4(18060, 17958, 35, 2.2, 6),
    SECOND_WIND_TIER_4(18070, 17978, 37, 2.4, 6),
    GLIMMER_OF_LIGHT_TIER_4(18080, 17968, 39, 2.3, 6),
    SUNDERING_STRIKE_TIER_5(18031, 17939, 41, 2.5, 8),
    POISONOUS_SHOT_TIER_5(18041, 17989, 42, 3.0, 8),
    SNARING_WAVE_TIER_5(18051, 17949, 43, 2.6, 8),
    APTITUDE_TIER_5(18061, 17959, 45, 2.7, 8),
    SECOND_WIND_TIER_5(18071, 17979, 47, 2.9, 8),
    GLIMMER_OF_LIGHT_TIER_5(18081, 17969, 49, 2.8, 8),
    SUNDERING_STRIKE_TIER_6(18032, 17940, 51, 3.0, 10),
    POISONOUS_SHOT_TIER_6(18042, 17990, 52, 3.5, 10),
    SNARING_WAVE_TIER_6(18052, 17950, 53, 3.1, 10),
    APTITUDE_TIER_6(18062, 17960, 55, 3.2, 10),
    SECOND_WIND_TIER_6(18072, 17980, 57, 3.4, 10),
    GLIMMER_OF_LIGHT_TIER_6(18082, 17970, 59, 3.3, 10),
    SUNDERING_STRIKE_TIER_7(18033, 17941, 61, 3.5, 12),
    POISONOUS_SHOT_TIER_7(18043, 17991, 62, 4.0, 12),
    SNARING_WAVE_TIER_7(18053, 17951, 63, 3.6, 12),
    APTITUDE_TIER_7(18063, 17961, 65, 3.7, 12),
    SECOND_WIND_TIER_7(18073, 17981, 67, 3.9, 12),
    GLIMMER_OF_LIGHT_TIER_7(18083, 17971, 69, 3.8, 12),
    SUNDERING_STRIKE_TIER_8(18034, 17942, 71, 4.0, 14),
    POISONOUS_SHOT_TIER_8(18044, 17992, 72, 4.5, 14),
    SNARING_WAVE_TIER_8(18054, 17952, 73, 4.1, 14),
    APTITUDE_TIER_8(18064, 17962, 75, 4.2, 14),
    SECOND_WIND_TIER_8(18074, 17982, 77, 4.4, 14),
    GLIMMER_OF_LIGHT_TIER_8(18084, 17972, 79, 4.3, 14),
    SUNDERING_STRIKE_TIER_9(18035, 17943, 81, 4.5, 16),
    POISONOUS_SHOT_TIER_9(18045, 17993, 82, 5.0, 16),
    SNARING_WAVE_TIER_9(18055, 17953, 83, 4.6, 16),
    APTITUDE_TIER_9(18065, 17963, 85, 4.7, 16),
    SECOND_WIND_TIER_9(18075, 17983, 87, 4.9, 16),
    GLIMMER_OF_LIGHT_TIER_9(18085, 17973, 89, 4.8, 16),
    SUNDERING_STRIKE_TIER_10(18036, 17944, 91, 5.0, 18),
    POISONOUS_SHOT_TIER_10(18046, 17994, 92, 5.5, 18),
    SNARING_WAVE_TIER_10(18056, 17954, 93, 5.1, 18),
    APTITUDE_TIER_10(18066, 17964, 95, 5.2, 18),
    SECOND_WIND_TIER_10(18076, 17984, 97, 5.4, 18),
    GLIMMER_OF_LIGHT_TIER_10(18086, 17974, 99, 5.3, 18),
    CLAY_DEPOSIT_CLASS_1(14421, 14422, 3, 0.0, 3),
    CLAY_DEPOSIT_CLASS_2(14421, 14424, 20, 0.0, 3),
    CLAY_DEPOSIT_CLASS_3(14421, 14426, 40, 0.0, 3),
    CLAY_DEPOSIT_CLASS_4(14421, 14428, 60, 0.0, 3),
    CLAY_DEPOSIT_CLASS_5(14421, 14430, 80, 0.0, 3);

    private int buttonId;
    private int scrollId;
    private int pouchId;
    private int levelRequired;
    private double experience;
    private int specialDepletion;

    private Scroll(final int scrollId, final int pouchId, final int levelRequired, final double experience, final int specialDepletion) {
//        this.buttonId = buttonId;
        this.scrollId = scrollId;
        this.pouchId = pouchId;
        this.levelRequired = levelRequired;
        this.experience = experience;
        this.specialDepletion = specialDepletion;
    }

    public static boolean isScroll(final int itemId) {
        for (Scroll p : values())
            if (p.getScrollId() == itemId)
                return true;
        return false;
    }

    public final static Scroll get(final int buttonId) {
        for (Scroll p : values()) {
            if (p.getButtonId() == buttonId) {
                return p;
            }
        }
        return null;
    }

    public static Scroll forScroll(int scrollId) {
        for (Scroll scroll : values()) {
            if (scroll.getScrollId() == scrollId) {
                return scroll;
            }
        }
        return null;
    }

    public static Scroll forId(int pouchId) {
        for (Scroll scroll : values()) {
            if (scroll.getPouchId() == pouchId) {
                return scroll;
            }
        }
        return null;
    }

    public int getButtonId() {
        return this.buttonId;
    }

    public int getScrollId() {
        return this.scrollId;
    }

    public int getPouchId() {
        return this.pouchId;
    }

    public int getLevelRequired() {
        return this.levelRequired;
    }

    public double getExp() {
        return this.experience;
    }

    public int getSpecialDepletion() {
        return this.specialDepletion;
    }

    public static Item[] getAllScrolls(SummoningType type) {
        Item[] scrolls = new Item[type.getEnd() - type.getStart()];
        for (int i = type.getStart(); i < type.getEnd(); i++) {
            scrolls[i - type.getStart()] = new Item(values()[i].getScrollId(), 10);
        }
        return scrolls;
    }

    public static int[][] getRequiredItems(SummoningType type) {
        int[][] items = new int[type.getEnd() - type.getStart()][];
        for (int i = type.getStart(); i < type.getEnd(); i++) {
            items[i - type.getStart()] = new int[]{OldPouch.values()[i].getPouchId()};
        }
        return items;
    }

    public static Revision[][] getRequiredRevisions(SummoningType type) {
        Revision[][] revisions = new Revision[type.getEnd() - type.getStart()][];
        for (int i = type.getStart(); i < type.getEnd(); i++) {
            revisions[i - type.getStart()] = new Revision[]{OldPouch.values()[i].getPouch().getRevision()};
        }
        return revisions;
    }

}
