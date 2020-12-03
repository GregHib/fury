package com.fury.game.content.skill.member.herblore;

public enum Drink {

    ATTACK_POTION(new int[] { 2428, 121, 123, 125 }, PotionEffects.ATTACK_POTION),
    STRENGTH_POTION(new int[] { 113, 115, 117, 119 }, PotionEffects.STRENGTH_POTION),
    DEFENCE_POTION(new int[] { 2432, 133, 135, 137 }, PotionEffects.DEFENCE_POTION),
    RANGING_POTION(new int[] { 2444, 169, 171, 173 }, PotionEffects.RANGE_POTION),
    MAGIC_POTION(new int[] { 3040, 3042, 3044, 3046 }, PotionEffects.MAGIC_POTION),
    MAGIC_FLASK(new int[] { 23423, 23425, 23427, 23429, 23431, 23433 }, PotionEffects.MAGIC_POTION),
    ANTIPOISON_POTION(new int[] { 2446, 175, 177, 179 }, PotionEffects.ANTIPOISON),
    SUPER_ANTIPOISON(new int[] { 2448, 181, 183, 185 }, PotionEffects.SUPER_ANTIPOISON),
    ANTIPOISION_PLUS(new int[] { 5943, 5945, 5947, 5949 }, PotionEffects.ANTIPOISON_PLUS),
    ANTIPOISON_DOUBLE_PLUS(new int[] { 5952, 5954, 5956, 5958 }, PotionEffects.ANTIPOISON_DOUBLE_PLUS),
    ANTIPOISON_PLUS_FLASK(new int[] { 23579, 23581, 23583, 23585, 23587, 23589 }, PotionEffects.ANTIPOISON_PLUS),
    ANTIPOISON_DOUBLE_PLUS_FLASK(new int[] { 23591, 23593, 23595, 23597, 23599, 23601 }, PotionEffects.ANTIPOISON_DOUBLE_PLUS),
    ZAMORAK_BREW(new int[] { 2450, 189, 191, 193 }, PotionEffects.ZAMORAK_BREW),
    ZAMORAK_BREW_FLASK(new int[] { 23339, 23341, 23343, 23345, 23347, 23349 }, PotionEffects.ZAMORAK_BREW),
    PRAYER_POTION(new int[] { 2434, 139, 141, 143 }, PotionEffects.PRAYER_POTION),
    PRAYER_MIX(new int[] { 11465, 11467 }, PotionEffects.PRAYER_MIX),
    //SUPER_STRENGTH_MIX(new int[] { 11443, 11441 }, PotionEffects.SUPER_STR_MIX),
    SUPER_ATTACK_POTION(new int[] { 2436, 145, 147, 149 }, PotionEffects.SUPER_ATT_POTION),
    SUPER_STRENGTH_POTION(new int[] { 2440, 157, 159, 161 }, PotionEffects.SUPER_STR_POTION),
    SUPER_DEFENCE_POTION(new int[] { 2442, 163, 165, 167 }, PotionEffects.SUPER_DEF_POTION),
    SUPER_RANGING_POTION(new int[] { 2444, 169, 171, 173 }, PotionEffects.SUPER_RANGE_POTION),
    SUPER_MAGIC_POTION(new int[] { 3040, 3042, 3044, 3046 }, PotionEffects.SUPER_DEF_POTION),
    ENERGY_POTION(new int[] { 3008, 3010, 3012, 3014 }, PotionEffects.ENERGY_POTION),
    SUPER_ENERGY_POTION(new int[] { 3016, 3018, 3020, 3022 }, PotionEffects.SUPER_ENERGY),
    EXTREME_ATTACK_POTION(new int[] { 15308, 15309, 15310, 15311 }, PotionEffects.EXTREME_ATT_POTION),
    EXTREME_STRENGTH_POTION(new int[] { 15312, 15313, 15314, 15315 }, PotionEffects.EXTREME_STR_POTION),
    EXTREME_DEFENCE_POTION(new int[] { 15316, 15317, 15318, 15319 }, PotionEffects.EXTREME_DEF_POTION),
    EXTREME_MAGIC_POTION(new int[] { 15320, 15321, 15322, 15323 }, PotionEffects.EXTREME_MAG_POTION),
    EXTREME_RANGING_POTION(new int[] { 15324, 15325, 15326, 15327 }, PotionEffects.EXTREME_RAN_POTION),
    RESTORE_POTION(new int[] { 2430, 127, 129, 131 }, PotionEffects.RESTORE_POTION),
    SUPER_RESTORE_POTION(new int[] { 3024, 3026, 3028, 3030 }, PotionEffects.SUPER_RESTORE),
    SARADOMIN_BREW_POTION(new int[] { 6685, 6687, 6689, 6691 }, PotionEffects.SARADOMIN_BREW),
    RECOVER_SPECIAL_POTION(new int[] { 15300, 15301, 15302, 15303 }, PotionEffects.RECOVER_SPECIAL),
    SUPER_PRAYER_POTION(new int[] { 15328, 15329, 15330, 15331 }, PotionEffects.SUPER_PRAYER),
    OVERLOAD_POTION(new int[] { 15332, 15333, 15334, 15335 }, PotionEffects.OVERLOAD),
    ANTIFIRE_POTION(new int[] { 2452, 2454, 2456, 2458 }, PotionEffects.ANTI_FIRE),
    SUPER_ANTIFIRE_POTION(new int[] { 15304, 15305, 15306, 15307 }, PotionEffects.SUPER_ANTI_FIRE),
    SUMMONING_POTION(new int[] { 12140, 12142, 12144, 12146 }, PotionEffects.SUMMONING_POT),
    SUMMONING_FLASK(new int[] { 23621, 23623, 23625, 23627, 23629, 23631 }, PotionEffects.SUMMONING_POT),
    SANFEW_SERUM_POTION(new int[] { 10925, 10927, 10929, 10931 }, PotionEffects.SANFEW_SERUM),
    PRAYER_RENEWAL_POTION(new int[] { 21630, 21632, 21634, 21636 }, PotionEffects.PRAYER_RENEWAL),
    PRAYER_RENEWAL_FLASK(new int[] { 23609, 23611, 23613, 23615, 23617, 23619 }, PotionEffects.PRAYER_RENEWAL),
    ATTACK_FLASK(new int[] { 23195, 23197, 23199, 23201, 23203, 23205 }, PotionEffects.ATTACK_POTION),
    STRENGTH_FLASK(new int[] { 23207, 23209, 23211, 23213, 23215, 23217 }, PotionEffects.STRENGTH_POTION),
    RESTORE_FLASK(new int[] { 23219, 23221, 23223, 23225, 23227, 23229 }, PotionEffects.RESTORE_POTION),
    DEFENCE_FLASK(new int[] { 23231, 23233, 23235, 23237, 23239, 23241 }, PotionEffects.DEFENCE_POTION),
    PRAYER_FLASK(new int[] { 23243, 23245, 23247, 23249, 23251, 23253 }, PotionEffects.PRAYER_POTION),
    SUPER_ATTACK_FLASK(new int[] { 23255, 23257, 23259, 23261, 23263, 23265 }, PotionEffects.SUPER_ATT_POTION),
    FISHING_FLASK(new int[] { 23267, 23269, 23271, 23273, 23275, 23277 }, PotionEffects.FISHING_POTION),
    FISHING_POTION(new int[] { 2438, 151, 153, 155 }, PotionEffects.FISHING_POTION),
    AGILITY_POTION(new int[] { 3032, 3034, 3036, 3038 }, PotionEffects.AGILITY_POTION),
    AGILITY_FLASK(new int[] { 23411, 23413, 23415, 23417, 23419, 23421 }, PotionEffects.AGILITY_POTION),
    COMBAT_POTION(new int[] { 9740, 9742, 9744, 9746 }, PotionEffects.COMBAT_POTION),
    COMBAT_FLASK(new int[] { 23447, 23449, 23451, 23453, 23455, 23457 }, PotionEffects.COMBAT_POTION),
    GUTHIX_REST_POTION(new int[] { 4417, 4419, 4421, 4423 }, PotionEffects.GUTHIX_REST),
    GUTHIX_REST_FLASK(new int[] { 29448, 29450, 29452, 29454, 29456, 29458 }, PotionEffects.GUTHIX_REST),
    MAGIC_ESSENCE_POTION(new int[] { 4417, 4419, 4421, 4423 }, PotionEffects.MAGIC_ESSENCE),
    MAGIC_ESSENCE_FLASK(new int[] { 23447, 23449, 23451, 23453, 23455, 23457 }, PotionEffects.MAGIC_ESSENCE),
    CRAFTING_POTION(new int[] { 14838, 14840, 14842, 14844 }, PotionEffects.CRAFTING_POTION),
    CRAFTING_FLASK(new int[] { 23459, 23461, 23463, 23465, 23467, 23469 }, PotionEffects.CRAFTING_POTION),
    HUNTER_POTION(new int[] { 9998, 10000, 10002, 10004 }, PotionEffects.HUNTER_POTION),
    HUNTER_FLASK(new int[] { 23435, 23437, 23439, 23441, 23443, 23435 }, PotionEffects.HUNTER_POTION),
    FLETCHING_POTION(new int[] { 14846, 14848, 14850, 14852 }, PotionEffects.FLETCHING_POTION),
    FLETCHING_FLASK(new int[] { 23471, 23473, 23475, 23477, 23479, 23481 }, PotionEffects.FLETCHING_POTION),
    SUPER_STRENGTH_FLASK(new int[] { 23279, 23281, 23283, 23285, 23287, 23289 }, PotionEffects.SUPER_STR_POTION),
    SUPER_DEFENCE_FLASK(new int[] { 23291, 23293, 23295, 23297, 23299, 23301 }, PotionEffects.SUPER_DEF_POTION),
    SUPER_RANGING_FLASK(new int[] { 23303, 23305, 23307, 23309, 23311, 23313 }, PotionEffects.SUPER_RANGE_POTION),
    SUPER_MAGIC_FLASK(new int[] { 23423, 23425, 23427, 23429, 23431, 23433 }, PotionEffects.SUPER_MAGIC_POTION),
    RANGING_FLASK(new int[] { 23303, 23305, 23307, 23309, 23311, 23313 }, PotionEffects.RANGE_POTION),
    ANTIPOISON_FLASK(new int[] { 23315, 23317, 23319, 23321, 23323, 23325 }, PotionEffects.ANTIPOISON),
    SUPER_ANTIPOISON_FLASK(new int[] { 23327, 23329, 23331, 23333, 23335, 23337 }, PotionEffects.SUPER_ANTIPOISON),
    SARADOMIN_BREW_FLASK(new int[] { 23351, 23353, 23355, 23357, 23359, 23361 }, PotionEffects.SARADOMIN_BREW),
    ANTIFIRE_FLASK(new int[] { 23363, 23365, 23367, 23369, 23371, 23373 }, PotionEffects.ANTI_FIRE),
    SUPER_ANTIFIRE_FLASK(new int[] { 23489, 23490, 23491, 23492, 23493, 23494 }, PotionEffects.SUPER_ANTI_FIRE),
    ENERGY_FLASK(new int[] { 23375, 23377, 23379, 23381, 23383, 23385 }, PotionEffects.ENERGY_POTION),
    SUPER_ENERGY_FLASK(new int[] { 23387, 23389, 23391, 23393, 23395, 23397 }, PotionEffects.SUPER_ENERGY),
    SUPER_RESTORE_FLASK(new int[] { 23399, 23401, 23403, 23405, 23407, 23409 }, PotionEffects.SUPER_RESTORE),
    RECOVER_SPECIAL_FLASK(new int[] { 23483, 23484, 23485, 23486, 23487, 23488 }, PotionEffects.RECOVER_SPECIAL),
    EXTREME_ATTACK_FLASK(new int[] { 23495, 23496, 23497, 23498, 23499, 23500 }, PotionEffects.EXTREME_ATT_POTION),
    EXTREME_STRENGTH_FLASK(new int[] { 23501, 23502, 23503, 23504, 23505, 23506 }, PotionEffects.EXTREME_STR_POTION),
    EXTREME_DEFENCE_FLASK(new int[] { 23507, 23508, 23509, 23510, 23511, 23512 }, PotionEffects.EXTREME_DEF_POTION),
    EXTREME_MAGIC_FLASK(new int[] { 23513, 23514, 23515, 23516, 23517, 23518 }, PotionEffects.EXTREME_MAG_POTION),
    EXTREME_RANGING_FLASK(new int[] { 23519, 23520, 23521, 23522, 23523, 23524 }, PotionEffects.EXTREME_RAN_POTION),
    SUPER_PRAYER_FLASK(new int[] { 23525, 23526, 23527, 23528, 23529, 23530 }, PotionEffects.SUPER_PRAYER),
    OVERLOAD_FLASK(new int[] { 23531, 23532, 23533, 23534, 23535, 23536 }, PotionEffects.OVERLOAD),
    JUG(new int[] { 1993, 1935 }, PotionEffects.WINE),
    TEA(new int[] { 712, 1978 }, PotionEffects.TEA),
    NETTLE_TEA(new int[] { 4239, 4240, 4242, 4243}, PotionEffects.NETTLE_TEA),
    FRUIT_BLAST(new int[] { 2084, 9514 }, PotionEffects.FRUIT_BLAST),
    PINEAPPLE_PUNCH(new int[] { 2048, 9512 }, PotionEffects.PINEAPPLE_PUNCH),
    WIZARD_BLIZZARD(new int[] { 2054, 9508 }, PotionEffects.WIZARD_BLIZZARD),
    SHORT_GREEN_GUY(new int[] { 2080, 9510 }, PotionEffects.SHORT_GREEN_GUY),
    DRUNK_DRAGON(new int[] { 2092, 9516 }, PotionEffects.DRUNK_DRAGON),
    CHOCOLATE_SATURDAY(new int[] { 2074, 9518 }, PotionEffects.CHOCOLATE_SATURDAY),
    BLURBERRY_SPECIAL(new int[] { 2064, 9520 }, PotionEffects.BLURBERRY_SPECIAL),
    SANFEW_SERUM_FLASK(new int[] { 23567, 23569, 23571, 23573, 23575, 23577 }, PotionEffects.SANFEW_SERUM),
    BEER(new int[] { 1917, 1919 }, PotionEffects.BEER),
    TANKARD(new int[] { 3803 }, PotionEffects.TANKARD),
    GREENMANS_ALE(new int[] { 1909 }, PotionEffects.GREENMANS_ALE),
    GREENMANS_ALE_KEG(new int[] { 5793, 5791, 5789, 5787 }, PotionEffects.GREENMANS_ALE),
    AXEMANS_ALE(new int[] { 5751 }, PotionEffects.AXEMANS),
    AXEMANS_ALE_KEG(new int[] { 5825, 5823, 5821, 5819 }, PotionEffects.AXEMANS),
    SLAYER_RESPITE(new int[] { 5759 }, PotionEffects.SLAYER_RESPITE),
    SLAYER_RESPITE_KEG(new int[] { 5841, 5839, 5837, 5835 }, PotionEffects.SLAYER_RESPITE),
    RANGERS_AID(new int[] { 15119 }, PotionEffects.RANGERS_AID),
    MOONLIGHT_MEAD(new int[] { 2955 }, PotionEffects.MOONLIGHT_MEAD),
    MOONLIGHT_MEAD_KEG(new int[] { 5817, 5815, 5813, 5811 }, PotionEffects.MOONLIGHT_MEAD),
    DRAGON_BITTER(new int[] { 1911 }, PotionEffects.DRAGON_BITTER),
    DRAGON_BITTER_KEG(new int[] { 5809, 5807, 5805, 5803 }, PotionEffects.DRAGON_BITTER),
    ASGARNIAN_ALE(new int[] { 1905 }, PotionEffects.ASGARNIAN_ALE),
    ASGARNIAN_ALE_KEG(new int[] { 5785, 5783, 5781, 5779 }, PotionEffects.ASGARNIAN_ALE),
    CHEF_DELIGHT(new int[] { 5755 }, PotionEffects.CHEF_DELIGHT),
    CHEF_DELIGHT_KEG(new int[] { 5833, 5831, 5829, 5827 }, PotionEffects.CHEF_DELIGHT),
    CIDER(new int[] { 5763 }, PotionEffects.CIDER),
    CIDER_KEG(new int[] { 5849, 5847, 5845, 5843 }, PotionEffects.CIDER),
    WIZARD_MIND_BOMB(new int[] { 1907 }, PotionEffects.WIZARD_MIND_BOMB),
    DWARVEN_STOUT(new int[] { 1913 }, PotionEffects.DWARVEN_STOUT),
    DWARVEN_STOUT_KEG(new int[] { 5777, 5775, 5773, 5771 }, PotionEffects.DWARVEN_STOUT),
    GROG(new int[] { 1915 }, PotionEffects.GROG),
    KEG_OF_BEER(new int[] { 3801 }, PotionEffects.KEG_OF_BEER),
    BANDIT_BREW(new int[] { 4627 }, PotionEffects.BANDIT_BREW),
    WEAK_MELEE_POTION(new int[] { 17560 }, PotionEffects.WEAK_MELEE_POTION),
    WEAK_MAGIC_POTION(new int[] { 17556 }, PotionEffects.WEAK_MAGIC_POTION),
    WEAK_RANGE_POTION(new int[] { 17558 }, PotionEffects.WEAK_RANGE_POTION),
    WEAK_DEFENCE_POTION(new int[] { 17562 }, PotionEffects.WEAK_DEFENCE_POTION),
    WEAK_STAT_RESTORE(new int[] { 17564 }, PotionEffects.WEAK_STAT_RESTORE_POTION),
    WEAK_CURE_POTION(new int[] { 17568 }, PotionEffects.WEAK_STAT_CURE_POTION),
    WEAK_REJUVINATION_POTION(new int[] { 17570 }, PotionEffects.WEAK_REJUVINATION_POTION),
    WEAK_GATHERS_POTION(new int[] { 17574 }, PotionEffects.WEAK_GATHERER_POTION),
    WEAK_ARTISTAN_POTION(new int[] { 17576 }, PotionEffects.WEAK_ARTISTIANS_POTION),
    WEAK_NATURALIST_POTION(new int[] { 17578 }, PotionEffects.WEAK_NATURALISTS_POTION),
    WEAK_SURVIVALIST_POTION(new int[] { 17580 }, PotionEffects.WEAK_SURVIVALISTS_POTION),
    REGULAR_MELEE_POTION(new int[] { 17586 }, PotionEffects.REGULAR_MELEE_POTION),
    REGULAR_MAGIC_POTION(new int[] { 17582 }, PotionEffects.REGULAR_MAGIC_POTION),
    REGULAR_RANGE_POTION(new int[] { 17584 }, PotionEffects.REGULAR_RANGE_POTION),
    REGULAR_DEFENCE_POTION(new int[] { 17588 }, PotionEffects.REGULAR_DEFENCE_POTION),
    REGULAR_STAT_RESTORE(new int[] { 17590 }, PotionEffects.REGULAR_STAT_RESTORE_POTION),
    REGULAR_CURE_POTION(new int[] { 17592 }, PotionEffects.REGULAR_STAT_CURE_POTION),
    REGULAR_REJUVINATION_POTION(new int[] { 17594 }, PotionEffects.REGULAR_REJUVINATION_POTION),
    REGULAR_GATHERS_POTION(new int[] { 17598 }, PotionEffects.REGULAR_GATHERER_POTION),
    REGULAR_ARTISTAN_POTION(new int[] { 17600 }, PotionEffects.REGULAR_ARTISTIANS_POTION),
    REGULAR_NATURALIST_POTION(new int[] { 17602 }, PotionEffects.REGULAR_NATURALISTS_POTION),
    REGULAR_SURVIVALIST_POTION(new int[] { 17604 }, PotionEffects.REGULAR_SURVIVALISTS_POTION),
    STRONG_MELEE_POTION(new int[] { 17610 }, PotionEffects.STRONG_MELEE_POTION),
    STRONG_MAGIC_POTION(new int[] { 17606 }, PotionEffects.STRONG_MAGIC_POTION),
    STRONG_RANGE_POTION(new int[] { 17608 }, PotionEffects.STRONG_RANGE_POTION),
    STRONG_DEFENCE_POTION(new int[] { 17612 }, PotionEffects.STRONG_DEFENCE_POTION),
    STRONG_STAT_RESTORE(new int[] { 17614 }, PotionEffects.STRONG_STAT_RESTORE_POTION),
    STRONG_CURE_POTION(new int[] { 17616 }, PotionEffects.STRONG_STAT_CURE_POTION),
    STRONG_REJUVINATION_POTION(new int[] { 17618 }, PotionEffects.STRONG_REJUVINATION_POTION),
    STRONG_GATHERS_POTION(new int[] { 17622 }, PotionEffects.STRONG_GATHERER_POTION),
    STRONG_ARTISTAN_POTION(new int[] { 17624 }, PotionEffects.STRONG_ARTISTIANS_POTION),
    STRONG_NATURALIST_POTION(new int[] { 17626 }, PotionEffects.STRONG_NATURALISTS_POTION),
    STRONG_SURVIVALIST_POTION(new int[] { 17628 }, PotionEffects.STRONG_SURVIVALISTS_POTION),
    ;

    private int[] id;
    private PotionEffects effect;

    Drink(int[] id, PotionEffects effect) {
        this.id = id;
        this.effect = effect;
    }

    public boolean isFlask() {
        return getMaxDoses() == 6;
    }

    public boolean isMix() {
        return getMaxDoses() == 2;
    }

    public boolean isPotion() {
        return getMaxDoses() == 4;
    }

    public boolean isExtreme() {
        return this == EXTREME_ATTACK_FLASK
                || this == EXTREME_DEFENCE_FLASK
                || this == EXTREME_MAGIC_FLASK
                || this == EXTREME_RANGING_FLASK
                || this == EXTREME_STRENGTH_FLASK
                || this == EXTREME_ATTACK_POTION
                || this == EXTREME_DEFENCE_POTION
                || this == EXTREME_MAGIC_POTION
                || this == EXTREME_RANGING_POTION
                || this == EXTREME_STRENGTH_POTION;
    }

    public boolean isOverload() {
        return this == OVERLOAD_POTION || this == OVERLOAD_FLASK;
    }

    public boolean isRecoverSpecial() {
        return this == RECOVER_SPECIAL_POTION || this == RECOVER_SPECIAL_FLASK;
    }

    public PotionEffects getEffect() {
        return effect;
    }

    public int getMaxDoses() {
        return id.length;
    }

    public int[] getId() {
        return id;
    }

    public int getIdForDoses(int doses) {
        return id[getMaxDoses() - doses];
    }

    public boolean contains(int i) {
        for (int i2 : id)
            if (i2 == i)
                return true;
        return false;
    }
}
