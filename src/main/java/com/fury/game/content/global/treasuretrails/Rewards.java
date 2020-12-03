package com.fury.game.content.global.treasuretrails;

import com.fury.game.node.entity.actor.figure.mob.drops.DropChance;

/**
 * Created by Greg on 30/09/2016.
 */
public class Rewards {
        public enum EASY {
            COINS(995, 2500, 50000, DropChance.COMMON),
            BISCUITS(19467, 2, 27, DropChance.RARE),
            PURPLE_SWEETS(10476, 2, 27, DropChance.RARE),
            MISC(19477, 1, 15, DropChance.UNCOMMON),
            LUMBER_YARD(19480, 1, 10, DropChance.UNCOMMON),
            BANDIT_CAMP(19476, 1, 5, DropChance.UNCOMMON),
            POLLNIVNEACH(18811, 1, 10, DropChance.UNCOMMON),
            PHOENIX_LAIR(19478, 1, 4, DropChance.UNCOMMON),
            TAI_BWO_WANNAI(19479, 1, 10, DropChance.UNCOMMON),
            SARADOMIN_PAGE_1(3827, DropChance.RARE),
            SARADOMIN_PAGE_2(3828, DropChance.RARE),
            SARADOMIN_PAGE_3(3829, DropChance.RARE),
            SARADOMIN_PAGE_4(3830, DropChance.RARE),
            GUTHIX_PAGE_1(3835, DropChance.RARE),
            GUTHIX_PAGE_2(3836, DropChance.RARE),
            GUTHIX_PAGE_3(3837, DropChance.RARE),
            GUTHIX_PAGE_4(3838, DropChance.RARE),
            ZAMORAK_PAGE_1(3831, DropChance.RARE),
            ZAMORAK_PAGE_2(3832, DropChance.RARE),
            ZAMORAK_PAGE_3(3833, DropChance.RARE),
            ZAMORAK_PAGE_4(3834, DropChance.RARE),
            ARMADYL_PAGE_1(19604, DropChance.RARE),
            ARMADYL_PAGE_2(19605, DropChance.RARE),
            ARMADYL_PAGE_3(19606, DropChance.RARE),
            ARMADYL_PAGE_4(19607, DropChance.RARE),
            BANDOS_PAGE_1(19600, DropChance.RARE),
            BANDOS_PAGE_2(19601, DropChance.RARE),
            BANDOS_PAGE_3(19602, DropChance.RARE),
            BANDOS_PAGE_4(19603, DropChance.RARE),
            ANCIENT_PAGE_1(19608, DropChance.RARE),
            ANCIENT_PAGE_2(19609, DropChance.RARE),
            ANCIENT_PAGE_3(19610, DropChance.RARE),
            ANCIENT_PAGE_4(19611, DropChance.RARE),
            RED_FIRELIGHTER(7329, 5, 30, DropChance.UNCOMMON),
            GREEN_FIRELIGHTER(7330, 5, 30, DropChance.UNCOMMON),
            BLUE_FIRELIGHTER(7331, 5, 30, DropChance.UNCOMMON),
            PURPLE_FIRELIGHTER(10326, 5, 30, DropChance.UNCOMMON),
            WHITE_FIRELIGHTER(10327, 5, 30, DropChance.UNCOMMON),
            SARADOMIN_ARROWS(19152, 20, 100, DropChance.UNCOMMON),
            GUTHIX_ARROWS(19157, 20, 100, DropChance.UNCOMMON),
            ZAMORAK_ARROWS(19162, 20, 100, DropChance.UNCOMMON),
            MEERKATS_POUCH(19623, 2, 20, DropChance.RARE),
            FETCH_CASKET_SCROLLS(19621, 20, 100, DropChance.RARE),
            
            WILLOW_COMP_BOW(10280, DropChance.RARE),
            YEW_COMP_BOW(10282, DropChance.RARE),
            //MAGIC_COMP_BOW(10285, DropChance.RARE),
            BLACK_FULL_HELM_T(2587, DropChance.RARE),
            BLACK_PLATEBODY_T(2583, DropChance.RARE),
            BLACK_PLATELEGS_T(2585, DropChance.RARE),
            BLACK_PLATESKIRT_T(3472, DropChance.RARE),
            BLACK_KITESHIELD_T(2589, DropChance.RARE),
            BLACK_FULL_HELM_G(2595, DropChance.RARE),
            BLACK_PLATEBODY_G(2591, DropChance.RARE),
            BLACK_PLATELEGS_G(2593, DropChance.RARE),
            BLACK_PLATESKIRT_G(3473, DropChance.RARE),
            BLACK_KITESHIELD_G(2597, DropChance.RARE),
            BLUE_BERET(2633, DropChance.RARE),
            BLACK_BERET(2635, DropChance.RARE),
            WHITE_BERET(2637, DropChance.RARE),
            HIGHWAYMAN_MASK(2631, DropChance.RARE),
            WIZARD_HAT_T(7396, DropChance.RARE),
            WIZARD_ROBE_T(7392, DropChance.RARE),
            WIZARD_SKIRT_T(7388, DropChance.RARE),
            WIZARD_HAT_G(7394, DropChance.RARE),
            WIZARD_ROBE_G(7390, DropChance.RARE),
            WIZARD_SKIRT(7386, DropChance.RARE),
            STUDDED_BODY_T(7364, DropChance.RARE),
            STUDDED_CHAPS_T(7368, DropChance.RARE),
            STUDDED_BODY_G(7362, DropChance.RARE),
            STUDDED_CHAPS_G(7366, DropChance.RARE),
            BLACK_HELM_H1(10306, DropChance.RARE),
            BLACK_HELM_H2(10308, DropChance.RARE),
            BLACK_HELM_H3(10310, DropChance.RARE),
            BLACK_HELM_H4(10312, DropChance.RARE),
            BLACK_HELM_H5(10314, DropChance.RARE),
            BLACK_PLATEBODY_H1(19167, DropChance.RARE),
            BLACK_PLATEBODY_H2(19188, DropChance.RARE),
            BLACK_PLATEBODY_H3(19209, DropChance.RARE),
            BLACK_PLATEBODY_H4(19230, DropChance.RARE),
            BLACK_PLATEBODY_H5(19251, DropChance.RARE),
            BLACK_PLATELEGS_H1(19169, DropChance.RARE),
            BLACK_PLATELEGS_H2(19190, DropChance.RARE),
            BLACK_PLATELEGS_H3(19211, DropChance.RARE),
            BLACK_PLATELEGS_H4(19232, DropChance.RARE),
            BLACK_PLATELEGS_H5(19253, DropChance.RARE),
            BLACK_SHIELD_H1(7332, DropChance.RARE),
            BLACK_SHIELD_H2(7338, DropChance.RARE),
            BLACK_SHIELD_H3(7344, DropChance.RARE),
            BLACK_SHIELD_H4(7350, DropChance.RARE),
            BLACK_SHIELD_H5(7356, DropChance.RARE),
            BLACK_PLATESKIRT_H1(19171, DropChance.RARE),
            BLACK_PLATESKIRT_H2(19192, DropChance.RARE),
            BLACK_PLATESKIRT_H3(19213, DropChance.RARE),
            BLACK_PLATESKIRT_H4(19234, DropChance.RARE),
            BLACK_PLATESKIRT_H5(19255, DropChance.RARE),
            BLUE_ELEGANT_SHIRT(10408, DropChance.RARE),
            BLUE_ELEGANT_LEGS(10410, DropChance.RARE),
            BLUE_ELEGANT_BLOUSE(10428, DropChance.RARE),
            BLUE_ELEGANT_SKIRT(10430, DropChance.RARE),
            GREEN_ELEGANT_SHIRT(10412, DropChance.RARE),
            GREEN_ELEGANT_LEGS(10414, DropChance.RARE),
            GREEN_ELEGANT_BLOUSE(10432, DropChance.RARE),
            GREEN_ELEGANT_SKIRT(10434, DropChance.RARE),
            RED_ELEGANT_SHIRT(10404, DropChance.RARE),
            RED_ELEGANT_LEGS(10406, DropChance.RARE),
            RED_ELEGANT_BLOUSE(10424, DropChance.RARE),
            RED_ELEGANT_SKIRT(10426, DropChance.RARE),
            BOB_SHIRT_RED(10316, DropChance.RARE),
            BOB_SHIRT_BLUE(10318, DropChance.RARE),
            BOB_SHIRT_GREEN(10320, DropChance.RARE),
            BOB_SHIRT_BLACK(10322, DropChance.RARE),
            BOB_SHIRT_PURPLE(10324, DropChance.RARE),
            FLARED_TROUSERS(10742, DropChance.RARE),
            SLEEPING_CAP(10746, DropChance.RARE),
            A_POWDERED_WIG(10740, DropChance.RARE),
            PANTALOONS(10744, DropChance.RARE),
            AMULET_OF_MAGIC_T(10366, DropChance.RARE),
            BLACK_CANE(13095, DropChance.RARE),
            SPIKED_HELMET(13105, DropChance.RARE),
            FIRE_RUNES(554, 150, DropChance.COMMON),
            AIR_RUNES(556, 150, DropChance.COMMON),
            EARTH_RUNES(557, 150, DropChance.COMMON),
            WATER_RUNES(555, 150, DropChance.COMMON),
            PURE_ESSENCE(7937, 150, DropChance.COMMON),
            LAW_RUNES(563, 20, DropChance.COMMON),
            NATURE_RUNES(561, 20, DropChance.COMMON),
            STAFF_OF_AIR(1381, DropChance.COMMON),
            STAFF_OF_FIRE(1387, DropChance.COMMON),
            STAFF_OF_EARTH(1385, DropChance.COMMON),
            STAFF_OF_WATER(1384, DropChance.COMMON),
            AIR_TALISMAN(1438, DropChance.COMMON),
            FIRE_TALISMAN(1442, DropChance.COMMON),
            EARTH_TALISMAN(1440, DropChance.COMMON),
            WATER_TALISMAN(1444, DropChance.COMMON),
            MITHRIL_FULL_HELM(1159, DropChance.COMMON),
            MITHRIL_PLATEBODY(1121, DropChance.COMMON),
            MITHRIL_PLATELEGS(1071, DropChance.COMMON),
            MITHRIL_KITESHIELD(1197, DropChance.COMMON),
            MITHRIL_LONGSWORD(1299, DropChance.COMMON),
            MITHRIL_2H_SWORD(1315, DropChance.COMMON),
            BLACK_CROSSBOW(13081, DropChance.COMMON),
            MITHRIL_ARROWS(888, 50, DropChance.COMMON),
            BLACK_BOLTS(13083, 33, DropChance.COMMON),
            OAK_PLANKS(8779, 20, DropChance.COMMON),
            HARD_LEATHER(1744, 10, DropChance.COMMON)
            ;

            private int itemId, minAmount, maxAmount;
            private DropChance rarity;
            EASY(int itemId, int minAmount, int maxAmount, DropChance rarity) {
                this.itemId = itemId;
                this.minAmount = minAmount;
                this.maxAmount = maxAmount;
                this.rarity = rarity;
            }
            EASY(int itemId, int amount, DropChance rarity) {
                this.itemId = itemId;
                minAmount = maxAmount = amount;
                this.rarity = rarity;
            }

            EASY(int itemId, DropChance rarity) {
                this.itemId = itemId;
                this.rarity = rarity;
                minAmount = maxAmount = 1;
            }

            public int getItemId() {
                return itemId;
            }

            public int getMinAmount() {
                return minAmount;
            }

            public int getMaxAmount() {
                return maxAmount;
            }

            public DropChance getRarity() {
                return rarity;
            }

        }
        
        public enum MEDIUM {
            COINS(995, 2500, 50000, DropChance.COMMON),
            BISCUITS(19467, 2, 27, DropChance.RARE),
            PURPLE_SWEETS(10476, 2, 27, DropChance.RARE),
            MISC(19477, 1, 15, DropChance.UNCOMMON),
            LUMBER_YARD(19480, 1, 10, DropChance.UNCOMMON),
            BANDIT_CAMP(19476, 1, 5, DropChance.UNCOMMON),
            POLLNIVNEACH(18811, 1, 10, DropChance.UNCOMMON),
            PHOENIX_LAIR(19478, 1, 4, DropChance.UNCOMMON),
            TAI_BWO_WANNAI(19479, 1, 10, DropChance.UNCOMMON),
            SARADOMIN_PAGE_1(3827, DropChance.RARE),
            SARADOMIN_PAGE_2(3828, DropChance.RARE),
            SARADOMIN_PAGE_3(3829, DropChance.RARE),
            SARADOMIN_PAGE_4(3830, DropChance.RARE),
            GUTHIX_PAGE_1(3835, DropChance.RARE),
            GUTHIX_PAGE_2(3836, DropChance.RARE),
            GUTHIX_PAGE_3(3837, DropChance.RARE),
            GUTHIX_PAGE_4(3838, DropChance.RARE),
            ZAMORAK_PAGE_1(3831, DropChance.RARE),
            ZAMORAK_PAGE_2(3832, DropChance.RARE),
            ZAMORAK_PAGE_3(3833, DropChance.RARE),
            ZAMORAK_PAGE_4(3834, DropChance.RARE),
            ARMADYL_PAGE_1(19604, DropChance.RARE),
            ARMADYL_PAGE_2(19605, DropChance.RARE),
            ARMADYL_PAGE_3(19606, DropChance.RARE),
            ARMADYL_PAGE_4(19607, DropChance.RARE),
            BANDOS_PAGE_1(19600, DropChance.RARE),
            BANDOS_PAGE_2(19601, DropChance.RARE),
            BANDOS_PAGE_3(19602, DropChance.RARE),
            BANDOS_PAGE_4(19603, DropChance.RARE),
            ANCIENT_PAGE_1(19608, DropChance.RARE),
            ANCIENT_PAGE_2(19609, DropChance.RARE),
            ANCIENT_PAGE_3(19610, DropChance.RARE),
            ANCIENT_PAGE_4(19611, DropChance.RARE),
            RED_FIRELIGHTER(7329, 5, 30, DropChance.UNCOMMON),
            GREEN_FIRELIGHTER(7330, 5, 30, DropChance.UNCOMMON),
            BLUE_FIRELIGHTER(7331, 5, 30, DropChance.UNCOMMON),
            PURPLE_FIRELIGHTER(10326, 5, 30, DropChance.UNCOMMON),
            WHITE_FIRELIGHTER(10327, 5, 30, DropChance.UNCOMMON),
            SARADOMIN_ARROWS(19152, 20, 100, DropChance.UNCOMMON),
            GUTHIX_ARROWS(19157, 20, 100, DropChance.UNCOMMON),
            ZAMORAK_ARROWS(19162, 20, 100, DropChance.UNCOMMON),
            MEERKATS_POUCH(19623, 2, 20, DropChance.RARE),
            FETCH_CASKET_SCROLLS(19621, 20, 100, DropChance.RARE),
            
            WILLOW_COMP_BOW(10280, DropChance.RARE),
            YEW_COMP_BOW(10282, DropChance.RARE),
            //MAGIC_COMP_BOW(10285, DropChance.RARE),
            ADAMANT_FULL_HELM_T(2605, DropChance.RARE),
            ADAMANT_PLATEBODY_T(2599, DropChance.RARE),
            ADAMANT_PLATELEGS_T(2601, DropChance.RARE),
            ADAMANT_PLATESKIRT_T(3474, DropChance.RARE),
            ADAMANT_KITESHIELD_T(2603, DropChance.RARE),
            ADAMANT_FULL_HELM_G(2613, DropChance.RARE),
            ADAMANT_PLATEBODY_G(2607, DropChance.RARE),
            ADAMANT_PLATELEGS_G(2609, DropChance.RARE),
            ADAMANT_PLATESKIRT_G(3475, DropChance.RARE),
            ADAMANT_KITESHIELD_G(2611, DropChance.RARE),
            RANGER_BOOTS(2577, DropChance.RARE),
            WIZARD_BOOTS(2579, DropChance.RARE),
            BLACK_HEADBAND(2647, DropChance.RARE),
            RED_HEADBAND(2645, DropChance.RARE),
            BROWN_HEADBAND(2649, DropChance.RARE),
            RED_BOATER(7319, DropChance.RARE),
            ORANGE_BOATER(7321, DropChance.RARE),
            GREEN_BOATER(7323, DropChance.RARE),
            BLUE_BOATER(7325, DropChance.RARE),
            BLACK_BOATER(7327, DropChance.RARE),
            DRAGONHIDE_BODY_T(7372, DropChance.RARE),
            DRAGONHIDE_CHAPS_T(7380, DropChance.RARE),
            DRAGONHIDE_BODY_G(7370, DropChance.RARE),
            DRAGONHIDE_CHAPS_G(7378, DropChance.RARE),
            ADAMANT_HELM_H1(10296, DropChance.RARE),
            ADAMANT_HELM_H2(10298, DropChance.RARE),
            ADAMANT_HELM_H3(10300, DropChance.RARE),
            ADAMANT_HELM_H4(10302, DropChance.RARE),
            ADAMANT_HELM_H5(10304, DropChance.RARE),
            ADAMANT_PLATEBODY_H1(19173, DropChance.RARE),
            ADAMANT_PLATEBODY_H2(19194, DropChance.RARE),
            ADAMANT_PLATEBODY_H3(19215, DropChance.RARE),
            ADAMANT_PLATEBODY_H4(19236, DropChance.RARE),
            ADAMANT_PLATEBODY_H5(19257, DropChance.RARE),
            ADAMANT_PLATELEGS_H1(19175, DropChance.RARE),
            ADAMANT_PLATELEGS_H2(19196, DropChance.RARE),
            ADAMANT_PLATELEGS_H3(19217, DropChance.RARE),
            ADAMANT_PLATELEGS_H4(19238, DropChance.RARE),
            ADAMANT_PLATELEGS_H5(19259, DropChance.RARE),
            ADAMANT_SHIELD_H1(7334, DropChance.RARE),
            ADAMANT_SHIELD_H2(7340, DropChance.RARE),
            ADAMANT_SHIELD_H3(7346, DropChance.RARE),
            ADAMANT_SHIELD_H4(7352, DropChance.RARE),
            ADAMANT_SHIELD_H5(7358, DropChance.RARE),
            ADAMANT_PLATESKIRT_H1(19177, DropChance.RARE),
            ADAMANT_PLATESKIRT_H2(19198, DropChance.RARE),
            ADAMANT_PLATESKIRT_H3(19219, DropChance.RARE),
            ADAMANT_PLATESKIRT_H4(19240, DropChance.RARE),
            ADAMANT_PLATESKIRT_H5(19261, DropChance.RARE),
            BLACK_ELEGANT_SHIRT(10400, DropChance.RARE),
            BLACK_ELEGANT_LEGS(10402, DropChance.RARE),
            WHITE_ELEGANT_BLOUSE(10420, DropChance.RARE),
            WHITE_ELEGANT_SKIRT(10422, DropChance.RARE),
            PURPLE_ELEGANT_SHIRT(10416, DropChance.RARE),
            PURPLE_ELEGANT_LEGS(10418, DropChance.RARE),
            PURPLE_ELEGANT_BLOUSE(10436, DropChance.RARE),
            PURPLE_ELEGANT_SKIRT(10438, DropChance.RARE),
            SARADOMIN_MITRE(10452, DropChance.RARE),
            GUTHIX_MITRE(10454, DropChance.RARE),
            ZAMORAK_MITRE(10456, DropChance.RARE),
            ARMADYL_ROBE_TOP(19380, DropChance.RARE),
            ARMADYL_ROBE_LEGS(19386, DropChance.RARE),
            BANDOS_ROBE_TOP(19384, DropChance.RARE),
            BANDOS_ROBE_LEGS(19388, DropChance.RARE),
            ANCIENT_ROBE_TOP(19382, DropChance.RARE),
            ANCIENT_ROBE_LEGS(19390, DropChance.RARE),
            PENGUIN_MASK(13109, DropChance.RARE),
            SHEEP_MASK(13107, DropChance.RARE),
            BAT_MASK(13111, DropChance.RARE),
            CAT_MASK(13113, DropChance.RARE),
            WOLF_MASK(13115, DropChance.RARE),
            STRENGTH_AMULET_T(10364, DropChance.RARE),
            ADAMANT_CANE(13097, DropChance.RARE),
            PITH_HELMET(13103, DropChance.RARE),
            FIRE_RUNES(554, 300, DropChance.COMMON),
            AIR_RUNES(556, 300, DropChance.COMMON),
            EARTH_RUNES(557, 300, DropChance.COMMON),
            WATER_RUNES(555, 300, DropChance.COMMON),
            MIND_RUNES(558, 300, DropChance.COMMON),
            PURE_ESSENCE(7937, 300, DropChance.COMMON),
            LAW_RUNES(563, 20, 75, DropChance.COMMON),
            NATURE_RUNES(561, 20, 75, DropChance.COMMON),
            AIR_BATTLESTAFF(1397, DropChance.COMMON),
            FIRE_BATTLESTAFF(1393, DropChance.COMMON),
            EARTH_BATTLESTAFF(1399, DropChance.COMMON),
            WATER_BATTLESTAFF(1395, DropChance.COMMON),
            ADAMANT_FULL_HELM(1161, DropChance.COMMON),
            ADAMANT_PLATEBODY(1123, DropChance.COMMON),
            ADAMANT_PLATELEGS(1073, DropChance.COMMON),
            ADAMANT_KITESHIELD(1199, DropChance.COMMON),
            ADAMANT_LONGSWORD(1301, DropChance.COMMON),
            ADAMANT_2H_SWORD(1317, DropChance.COMMON),
            ADAMANT_CROSSBOW(9183, DropChance.COMMON),
            ADAMANT_ARROWS(890, 100, DropChance.COMMON),
            ADAMANT_BOLTS(9143, 66, DropChance.COMMON),
            TEAK_PLANKS(8781, 4, DropChance.COMMON),
            GREEN_DRAGONHIDE(1754, 5, DropChance.COMMON),
            GREEN_DHIDE_BODY(1135, DropChance.COMMON),
            GREEN_DHIDE_CHAPS(1099, DropChance.COMMON)
            ;

            private int itemId, minAmount, maxAmount;
            private DropChance rarity;
            MEDIUM(int itemId, int minAmount, int maxAmount, DropChance rarity) {
                this.itemId = itemId;
                this.minAmount = minAmount;
                this.maxAmount = maxAmount;
                this.rarity = rarity;
            }
            MEDIUM(int itemId, int amount, DropChance rarity) {
                this.itemId = itemId;
                minAmount = maxAmount = amount;
                this.rarity = rarity;
            }

            MEDIUM(int itemId, DropChance rarity) {
                this.itemId = itemId;
                this.rarity = rarity;
                minAmount = maxAmount = 1;
            }

            public int getItemId() {
                return itemId;
            }

            public int getMinAmount() {
                return minAmount;
            }

            public int getMaxAmount() {
                return maxAmount;
            }

            public DropChance getRarity() {
                return rarity;
            }
        }
        //4-6 items
        public enum HARD {
            COINS(995, 2500, 50000, DropChance.COMMON),
            BISCUITS(19467, 2, 27, DropChance.RARE),
            PURPLE_SWEETS(10476, 2, 27, DropChance.RARE),
            MISC(19477, 1, 15, DropChance.UNCOMMON),
            LUMBER_YARD(19480, 1, 10, DropChance.UNCOMMON),
            BANDIT_CAMP(19476, 1, 5, DropChance.UNCOMMON),
            POLLNIVNEACH(18811, 1, 10, DropChance.UNCOMMON),
            PHOENIX_LAIR(19478, 1, 4, DropChance.UNCOMMON),
            TAI_BWO_WANNAI(19479, 1, 10, DropChance.UNCOMMON),
            SARADOMIN_PAGE_1(3827, DropChance.RARE),
            SARADOMIN_PAGE_2(3828, DropChance.RARE),
            SARADOMIN_PAGE_3(3829, DropChance.RARE),
            SARADOMIN_PAGE_4(3830, DropChance.RARE),
            GUTHIX_PAGE_1(3835, DropChance.RARE),
            GUTHIX_PAGE_2(3836, DropChance.RARE),
            GUTHIX_PAGE_3(3837, DropChance.RARE),
            GUTHIX_PAGE_4(3838, DropChance.RARE),
            ZAMORAK_PAGE_1(3831, DropChance.RARE),
            ZAMORAK_PAGE_2(3832, DropChance.RARE),
            ZAMORAK_PAGE_3(3833, DropChance.RARE),
            ZAMORAK_PAGE_4(3834, DropChance.RARE),
            ARMADYL_PAGE_1(19604, DropChance.RARE),
            ARMADYL_PAGE_2(19605, DropChance.RARE),
            ARMADYL_PAGE_3(19606, DropChance.RARE),
            ARMADYL_PAGE_4(19607, DropChance.RARE),
            BANDOS_PAGE_1(19600, DropChance.RARE),
            BANDOS_PAGE_2(19601, DropChance.RARE),
            BANDOS_PAGE_3(19602, DropChance.RARE),
            BANDOS_PAGE_4(19603, DropChance.RARE),
            ANCIENT_PAGE_1(19608, DropChance.RARE),
            ANCIENT_PAGE_2(19609, DropChance.RARE),
            ANCIENT_PAGE_3(19610, DropChance.RARE),
            ANCIENT_PAGE_4(19611, DropChance.RARE),
            RED_FIRELIGHTER(7329, 5, 30, DropChance.UNCOMMON),
            GREEN_FIRELIGHTER(7330, 5, 30, DropChance.UNCOMMON),
            BLUE_FIRELIGHTER(7331, 5, 30, DropChance.UNCOMMON),
            PURPLE_FIRELIGHTER(10326, 5, 30, DropChance.UNCOMMON),
            WHITE_FIRELIGHTER(10327, 5, 30, DropChance.UNCOMMON),
            SARADOMIN_ARROWS(19152, 20, 100, DropChance.UNCOMMON),
            GUTHIX_ARROWS(19157, 20, 100, DropChance.UNCOMMON),
            ZAMORAK_ARROWS(19162, 20, 100, DropChance.UNCOMMON),
            MEERKATS_POUCH(19623, 2, 20, DropChance.RARE),
            FETCH_CASKET_SCROLLS(19621, 20, 100, DropChance.RARE),
            
            YEW_COMP_BOW(10282, DropChance.RARE),
            //MAGIC_COMP_BOW(10285, DropChance.RARE),
            RUNE_FULL_HELM_T(2627, DropChance.RARE),
            RUNE_PLATEBODY_T(2623, DropChance.RARE),
            RUNE_PLATELEGS_T(2625, DropChance.RARE),
            RUNE_PLATESKIRT_T(3477, DropChance.RARE),
            RUNE_KITESHIELD_T(2629, DropChance.RARE),
            RUNE_FULL_HELM_G(2619, DropChance.RARE),
            RUNE_PLATEBODY_G(2615, DropChance.RARE),
            RUNE_PLATELEGS_G(2617, DropChance.RARE),
            RUNE_PLATESKIRT_G(3476, DropChance.RARE),
            RUNE_KITESHIELD_G(2621, DropChance.RARE),
            SARADOMIN_FULL_HELM(2665, DropChance.RARE),
            SARADOMIN_PLATEBODY(2661, DropChance.RARE),
            SARADOMIN_PLATELEGS(2663, DropChance.RARE),
            SARADOMIN_KITESHIELD(2667, DropChance.RARE),
            GUTHIX_FULL_HELM(2673, DropChance.RARE),
            GUTHIX_PLATEBODY(2669, DropChance.RARE),
            GUTHIX_PLATELEGS(2671, DropChance.RARE),
            GUTHIX_KITESHIELD(2675, DropChance.RARE),
            ZAMORAK_FULL_HELM(2657, DropChance.RARE),
            ZAMORAK_PLATEBODY(2653, DropChance.RARE),
            ZAMORAK_PLATELEGS(2655, DropChance.RARE),
            ZAMORAK_KITESHIELD(2675, DropChance.RARE),
            DRAGONHIDE_BODY_T(7376, DropChance.RARE),
            DRAGONHIDE_CHAPS_T(7384, DropChance.RARE),
            DRAGONHIDE_BODY_G(7374, DropChance.RARE),
            DRAGONHIDE_CHAPS_G(7382, DropChance.RARE),
            PIRATE_HAT(8950, DropChance.RARE),
            ROBIN_HOOD_HAT(2581, DropChance.RARE),
            ENCHANTED_HAT(7400, DropChance.RARE),
            ENCHANTED_TOP(7399, DropChance.RARE),
            ENCHANTED_ROBE(7398, DropChance.RARE),
            TAN_CAVALIERS(2639, DropChance.RARE),
            DARK_CAVALIERS(2641, DropChance.RARE),
            BLACK_CAVALIERS(2643, DropChance.RARE),
            RUNE_HELM_H1(10286, DropChance.RARE),
            RUNE_HELM_H2(10288, DropChance.RARE),
            RUNE_HELM_H3(10290, DropChance.RARE),
            RUNE_HELM_H4(10292, DropChance.RARE),
            RUNE_HELM_H5(10294, DropChance.RARE),
            RUNE_PLATEBODY_H1(19179, DropChance.RARE),
            RUNE_PLATEBODY_H2(19200, DropChance.RARE),
            RUNE_PLATEBODY_H3(19221, DropChance.RARE),
            RUNE_PLATEBODY_H4(19242, DropChance.RARE),
            RUNE_PLATEBODY_H5(19263, DropChance.RARE),
            RUNE_PLATELEGS_H1(19182, DropChance.RARE),
            RUNE_PLATELEGS_H2(19203, DropChance.RARE),
            RUNE_PLATELEGS_H3(19224, DropChance.RARE),
            RUNE_PLATELEGS_H4(19245, DropChance.RARE),
            RUNE_PLATELEGS_H5(19266, DropChance.RARE),
            RUNE_SHIELD_H1(7336, DropChance.RARE),
            RUNE_SHIELD_H2(7342, DropChance.RARE),
            RUNE_SHIELD_H3(7348, DropChance.RARE),
            RUNE_SHIELD_H4(7354, DropChance.RARE),
            RUNE_SHIELD_H5(7360, DropChance.RARE),
            RUNE_PLATESKIRT_H1(19185, DropChance.RARE),
            RUNE_PLATESKIRT_H2(19206, DropChance.RARE),
            RUNE_PLATESKIRT_H3(19227, DropChance.RARE),
            RUNE_PLATESKIRT_H4(19248, DropChance.RARE),
            RUNE_PLATESKIRT_H5(19269, DropChance.RARE),
            AMULET_OF_GLORY_T(10362, DropChance.RARE),
            SARADOMIN_STOLE(10470, DropChance.RARE),
            GUTHIX_STOLE(10472, DropChance.RARE),
            ZAMORAK_STOLE(10474, DropChance.RARE),
            SARADOMIN_CROZIER(10440, DropChance.RARE),
            GUTHIX_CROZIER(10442, DropChance.RARE),
            ZAMORAK_CROZIER(1444, DropChance.RARE),
            ARMADYL_MITRE(19374, DropChance.RARE),
            BANDOS_MITRE(19376, DropChance.RARE),
            ANCIENT_MITRE(19378, DropChance.RARE),
            ARMADYL_CLOAK(19368, DropChance.RARE),
            BANDOS_CLOAK(19370, DropChance.RARE),
            ANCIENT_CLOAK(19372, DropChance.RARE),
            SARADOMIN_COIF(10390, DropChance.RARE),
            SARADOMIN_BODY(10386, DropChance.RARE),
            SARADOMIN_CHAPS(10388, DropChance.RARE),
            SARADOMIN_VAMBRACES(10384, DropChance.RARE),
            GUTHIX_COIF(10382, DropChance.RARE),
            GUTHIX_BODY(10378, DropChance.RARE),
            GUTHIX_CHAPS(10380, DropChance.RARE),
            GUTHIX_VAMBRACES(10376, DropChance.RARE),
            ZAMORAK_COIF(10374, DropChance.RARE),
            ZAMORAK_BODY(10370, DropChance.RARE),
            ZAMORAK_CHAPS(10372, DropChance.RARE),
            ZAMORAK_VAMBRACES(10368, DropChance.RARE),
            FOX_MASK(19272, DropChance.RARE),
            WHITE_UNICORN_MASK(19275, DropChance.RARE),
            BLACK_UNICORN_MASK(19278, DropChance.RARE),
            GREEN_DRAGON_MASK(19281, DropChance.RARE),
            BLUE_DRAGON_MASK(19284, DropChance.RARE),
            RED_DRAGON_MASK(19287, DropChance.RARE),
            TOP_HAT(13101, DropChance.RARE),
            RUNE_CANE(13099, DropChance.RARE),
            RUNE_LONGSWORD(1303, DropChance.COMMON),
            RUNE_2H_SWORD(1319, DropChance.COMMON),
            RUNE_PICKAXE(1275, DropChance.COMMON),
            BLACK_DHIDE_BODY(2503, DropChance.COMMON),
            BLACK_DHIDE_CHAPS(2497, DropChance.COMMON),
            MYSTIC_AIR_STAFF(1405, DropChance.COMMON),
            MYSTIC_FIRE_STAFF(1401, DropChance.COMMON),
            MYSTIC_EARTH_STAFF(1407, DropChance.COMMON),
            MYSTIC_WATER_STAFF(1403, DropChance.COMMON),
            MAHOGANY_PLANK(8783, 24, DropChance.COMMON),
            //COURT_SUMMONS(18757, DropChance.COMMON),
            RUNE_FULL_HELM(1163, DropChance.COMMON),
            RUNE_PLATEBODY(1127, DropChance.COMMON),
            RUNE_PLATELEGS(1079, DropChance.COMMON),
            RUNE_KITESHIELD(1201, DropChance.COMMON),
            RUNE_ARROWS(892, 150, DropChance.COMMON),
            LAW_RUNES(563, 75, DropChance.COMMON),
            NATURE_RUNES(561, 75, DropChance.COMMON),
            ASTRAL_RUNES(9075, 75, DropChance.COMMON),
            BLOOD_RUNES(565, 75, DropChance.COMMON),
            BLACK_DRAGONHIDE(1748, 5, DropChance.COMMON),
            GUILDED_FULL_HELM(3486, DropChance.EXTREMELY_RARE),
            GUILDED_PLATEBODY(3481, DropChance.EXTREMELY_RARE),
            GUILDED_PLATELEGS(3483, DropChance.EXTREMELY_RARE),
            GUILDED_PLATESKIRT(3485, DropChance.EXTREMELY_RARE),
            GUILDED_KITESHIELD(3488, DropChance.EXTREMELY_RARE),
            SUPER_ATTACK_POTIONS(2441, 5, DropChance.EXTREMELY_RARE),
            SUPER_STRENGTH_POTIONS(2437, 5, DropChance.EXTREMELY_RARE),
            SUPER_DEFENCE_POTIONS(2443, 5, DropChance.EXTREMELY_RARE),
            SUPER_ENERGY_POTION(3017, 15, DropChance.EXTREMELY_RARE),
            SUPER_RESTORE_POTION(3025, 15, DropChance.EXTREMELY_RARE),
            ANTIFIRE(2453, 15, DropChance.EXTREMELY_RARE),
            STARVED_ANCIENT_EFFIGY(18778, DropChance.EXTREMELY_RARE),

            THIRDAGE_FULL_HELMET(10350, DropChance.LEGENDARY),
            THIRDAGE_PLATEBODY(10348, DropChance.LEGENDARY),
            THIRDAGE_PLATELEGS(10346, DropChance.LEGENDARY),
            THIRDAGE_KITESHIELD(10352, DropChance.LEGENDARY),

            THIRDAGE_MAGE_HAT(10342, DropChance.LEGENDARY),
            THIRDAGE_ROBE_TOP(10338, DropChance.LEGENDARY),
            THIRDAGE_ROBE(10340, DropChance.LEGENDARY),
            THIRDAGE_AMULET(10344, DropChance.LEGENDARY),

            THIRDAGE_RANGE_COIF(10344, DropChance.LEGENDARY),
            THIRDAGE_RANGE_TOP(10330, DropChance.LEGENDARY),
            THIRDAGE_RANGE_LEGS(10332, DropChance.LEGENDARY),
            THIRDAGE_VAMBRACES(10336, DropChance.LEGENDARY),
            ;

            private int itemId, minAmount, maxAmount;
            private DropChance rarity;
            HARD(int itemId, int minAmount, int maxAmount, DropChance rarity) {
                this.itemId = itemId;
                this.minAmount = minAmount;
                this.maxAmount = maxAmount;
                this.rarity = rarity;
            }
            HARD(int itemId, int amount, DropChance rarity) {
                this.itemId = itemId;
                minAmount = maxAmount = amount;
                this.rarity = rarity;
            }

            HARD(int itemId, DropChance rarity) {
                this.itemId = itemId;
                this.rarity = rarity;
                minAmount = maxAmount = 1;
            }

            public int getItemId() {
                return itemId;
            }

            public int getMinAmount() {
                return minAmount;
            }

            public int getMaxAmount() {
                return maxAmount;
            }

            public DropChance getRarity() {
                return rarity;
            }
        }

        //4-6 items
        public enum ELITE {
            COINS(995, 2500, 50000, DropChance.COMMON),
            BISCUITS(19467, 2, 27, DropChance.RARE),
            PURPLE_SWEETS(10476, 2, 27, DropChance.RARE),
            MISC(19477, 1, 15, DropChance.UNCOMMON),
            LUMBER_YARD(19480, 1, 10, DropChance.UNCOMMON),
            BANDIT_CAMP(19476, 1, 5, DropChance.UNCOMMON),
            POLLNIVNEACH(18811, 1, 10, DropChance.UNCOMMON),
            PHOENIX_LAIR(19478, 1, 4, DropChance.UNCOMMON),
            TAI_BWO_WANNAI(19479, 1, 10, DropChance.UNCOMMON),
            SARADOMIN_PAGE_1(3827, DropChance.RARE),
            SARADOMIN_PAGE_2(3828, DropChance.RARE),
            SARADOMIN_PAGE_3(3829, DropChance.RARE),
            SARADOMIN_PAGE_4(3830, DropChance.RARE),
            GUTHIX_PAGE_1(3835, DropChance.RARE),
            GUTHIX_PAGE_2(3836, DropChance.RARE),
            GUTHIX_PAGE_3(3837, DropChance.RARE),
            GUTHIX_PAGE_4(3838, DropChance.RARE),
            ZAMORAK_PAGE_1(3831, DropChance.RARE),
            ZAMORAK_PAGE_2(3832, DropChance.RARE),
            ZAMORAK_PAGE_3(3833, DropChance.RARE),
            ZAMORAK_PAGE_4(3834, DropChance.RARE),
            ARMADYL_PAGE_1(19604, DropChance.RARE),
            ARMADYL_PAGE_2(19605, DropChance.RARE),
            ARMADYL_PAGE_3(19606, DropChance.RARE),
            ARMADYL_PAGE_4(19607, DropChance.RARE),
            BANDOS_PAGE_1(19600, DropChance.RARE),
            BANDOS_PAGE_2(19601, DropChance.RARE),
            BANDOS_PAGE_3(19602, DropChance.RARE),
            BANDOS_PAGE_4(19603, DropChance.RARE),
            ANCIENT_PAGE_1(19608, DropChance.RARE),
            ANCIENT_PAGE_2(19609, DropChance.RARE),
            ANCIENT_PAGE_3(19610, DropChance.RARE),
            ANCIENT_PAGE_4(19611, DropChance.RARE),
            RED_FIRELIGHTER(7329, 5, 30, DropChance.UNCOMMON),
            GREEN_FIRELIGHTER(7330, 5, 30, DropChance.UNCOMMON),
            BLUE_FIRELIGHTER(7331, 5, 30, DropChance.UNCOMMON),
            PURPLE_FIRELIGHTER(10326, 5, 30, DropChance.UNCOMMON),
            WHITE_FIRELIGHTER(10327, 5, 30, DropChance.UNCOMMON),
            SARADOMIN_ARROWS(19152, 20, 100, DropChance.UNCOMMON),
            GUTHIX_ARROWS(19157, 20, 100, DropChance.UNCOMMON),
            ZAMORAK_ARROWS(19162, 20, 100, DropChance.UNCOMMON),
            MEERKATS_POUCH(19623, 2, 20, DropChance.RARE),
            FETCH_CASKET_SCROLLS(19621, 20, 100, DropChance.RARE),
            
            ARMADYL_FULL_HELM(19422, DropChance.RARE),
            ARMADYL_PLATEBODY(19413, DropChance.RARE),
            ARMADYL_PLATELEGS(19416, DropChance.RARE),
            ARMADYL_KITESHIELD(19425, DropChance.RARE),
            BANDOS_FULL_HELM(19437, DropChance.RARE),
            BANDOS_PLATEBODY(19428, DropChance.RARE),
            BANDOS_PLATELEGS(19431, DropChance.RARE),
            BANDOS_KITESHIELD(19440, DropChance.RARE),
            ANCIENT_FULL_HELM(19407, DropChance.RARE),
            ANCIENT_PLATEBODY(19398, DropChance.RARE),
            ANCIENT_PLATELEGS(19401, DropChance.RARE),
            ANCIENT_KITESHIELD(19410, DropChance.RARE),
            ARMADYL_COIF(19465, DropChance.RARE),
            ARMADYL_BODY(19461, DropChance.RARE),
            ARMADYL_CHAPS(19463, DropChance.RARE),
            ARMADYL_VAMBRACES(19459, DropChance.RARE),
            BANDOS_COIF(19457, DropChance.RARE),
            BANDOS_BODY(19453, DropChance.RARE),
            BANDOS_CHAPS(19455, DropChance.RARE),
            BANDOS_VAMBRACES(19451, DropChance.RARE),
            ANCIENT_COIF(19449, DropChance.RARE),
            ANCIENT_BODY(19445, DropChance.RARE),
            ANCIENT_CHAPS(19447, DropChance.RARE),
            ANCIENT_VAMBRACES(19443, DropChance.RARE),
            ARMADYL_STOLE(19392, DropChance.RARE),
            BANDOS_STOLE(19394, DropChance.RARE),
            ANCIENT_STOLE(19396, DropChance.RARE),
            ARMADYL_CROZIER(19362, DropChance.RARE),
            BANDOS_CROZIER(19364, DropChance.RARE),
            ANCIENT_CROZIER(19366, DropChance.RARE),
            DRAGON_FULL_HELM_ORNAMENT_KIT_OR(19346, DropChance.RARE),
            DRAGON_FULL_HELM_ORNAMENT_KIT_SP(19354, DropChance.RARE),
            DRAGON_PLATEBODY_ORNAMENT_KIT_OR(19350, DropChance.RARE),
            DRAGON_PLATEBODY_ORNAMENT_KIT_SP(19358, DropChance.RARE),
            DRAGON_PLATELEGS_ORNAMENT_KIT_OR(19348, DropChance.RARE),
            DRAGON_PLATELEGS_ORNAMENT_KIT_SP(19356, DropChance.RARE),
            DRAGON_SQ_SHIELD_ORNAMENT_KIT_OR(19352, DropChance.RARE),
            DRAGON_SQ_SHIELD_ORNAMENT_KIT_SP(19360, DropChance.RARE),
            FURY_ORNAMENT_KIT(19333, DropChance.RARE),
            BAT_STAFF(19327, DropChance.RARE),
            WOLF_STAFF(19329, DropChance.RARE),
            DRAGON_STAFF(19323, DropChance.RARE),
            CAT_STAFF(19331, DropChance.RARE),
            PENGUIN_STAFF(19325, DropChance.RARE),
            STARVED_ANCIENT_EFFIGY(18778, DropChance.EXTREMELY_RARE),
            BLACK_DRAGON_MASK(19290, DropChance.RARE),
            BRONZE_DRAGON_MASK(19296, DropChance.RARE),
            IRON_DRAGON_MASK(19299, DropChance.RARE),
            STEEL_DRAGON_MASK(19302, DropChance.RARE),
            MITHRIL_DRAGON_MASK(19305, DropChance.RARE),
            FROST_DRAGON_MASK(19293, DropChance.RARE),
            SARADOMIN_BOW(19143, DropChance.RARE),
            GUTHIX_BOW(19146, DropChance.RARE),
            ZAMORAK_BOW(19149, DropChance.RARE),
            THIRDAGE_DRUIDIC_STAFF(19308, DropChance.INSANE),
            THIRDAGE_DRUIDIC_WREATH(19314, DropChance.INSANE),
            THIRDAGE_DRUIDIC_ROBE_TOP(19317, DropChance.INSANE),
            THIRDAGE_DRUIDIC_ROBE(19320, DropChance.INSANE),
            THIRDAGE_DRUIDIC_CLOAK(19311, DropChance.INSANE),
            DWARF_WEED_SEEDS(5303, 2, DropChance.COMMON),
            LANTADYME_SEEDS(5302, 2, DropChance.COMMON),
            WATER_TALISMAN(1445, 8, DropChance.COMMON),
            RUNE_BARS(2364, 5, DropChance.COMMON),
            UNCUT_DRAGONSTONES(1632, 2, DropChance.COMMON),
            MAHOGANY_PLANKS(8783, 40, DropChance.COMMON),
            RUNE_PLATEBODY(1127, DropChance.COMMON),
            PALM_TREE_SEED(5289, DropChance.COMMON),
            PAPAYA_TREE_SEED(5289, DropChance.COMMON),
            YEW_SEED(5315, DropChance.COMMON),
            SUPER_RESTORE(3025, 9, DropChance.COMMON),
            ANTIFIRE(2453, 9, DropChance.COMMON),
            PRAYER_POTION(2435, 9, DropChance.COMMON),
            SWAMP_LIZARDS(10163, 15, DropChance.COMMON),
            UNICORN_HORNS(238, 10, DropChance.COMMON),
            ONYX_BOLT_TIPS(9194, 12, DropChance.COMMON),
            BATTLESTAVES(1392, 8, DropChance.COMMON),
            DRAGON_MED_HELM(1149, DropChance.COMMON)
            ;

            private int itemId, minAmount, maxAmount;
            private DropChance rarity;
            ELITE(int itemId, int minAmount, int maxAmount, DropChance rarity) {
                this.itemId = itemId;
                this.minAmount = minAmount;
                this.maxAmount = maxAmount;
                this.rarity = rarity;
            }
            ELITE(int itemId, int amount, DropChance rarity) {
                this.itemId = itemId;
                minAmount = maxAmount = amount;
                this.rarity = rarity;
            }

            ELITE(int itemId, DropChance rarity) {
                this.itemId = itemId;
                this.rarity = rarity;
                minAmount = maxAmount = 1;
            }

            public int getItemId() {
                return itemId;
            }

            public int getMinAmount() {
                return minAmount;
            }

            public int getMaxAmount() {
                return maxAmount;
            }

            public DropChance getRarity() {
                return rarity;
            }
        }
}
