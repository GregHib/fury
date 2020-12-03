package com.fury.game.content.skill.member.fletching

import com.fury.core.model.item.Item
import com.fury.game.world.update.flag.block.Animation
import com.fury.game.world.update.flag.block.graphic.Graphic

enum class FletchingData(val item: Item, val selected: Item, val products: Array<Item>, val levels: Array<Int>, val xp: Array<Double>, val animation: Animation? = null, val graphic: Graphic? = null) {
    /**
     * Crossbows
     */
    UNSTRUNG_BRONZE_CROSSBOW(Item(9440), Item(9420), Item(9454), 9, 6.0),
    UNSTRUNG_IRON_CROSSBOW(Item(9444), Item(9423), Item(9457), 39, 22.0),
    UNSTRUNG_BLURITE_CROSSBOW(Item(9442), Item(9422), Item(9456), 24, 16.0),
    UNSTRUNG_STEEL_CROSSBOW(Item(9446), Item(9425), Item(9459), 46, 27.0),
    UNSTRUNG_MITHRIL_CROSSBOW(Item(9448), Item(9427), Item(9461), 54, 32.0),
    UNSTRUNG_ADAMANT_CROSSBOW(Item(9450), Item(9429), Item(9463), 61, 41.0),
    UNSTRUNG_RUNITE_CROSSBOW(Item(9452), Item(9431), Item(9465), 69, 50.0),
    BRONZE_CROSSBOW(Item(9454), Item(9438), Item(9174), 9, 6.0, Animation(6671)),
    IRON_CROSSBOW(Item(9457), Item(9438), Item(9177), 39, 22.0, Animation(6673)),
    STEEL_CROSSBOW(Item(9459), Item(9438), Item(9465), 46, 27.0, Animation(6674)),
    BLURITE_CROSSBOW(Item(9456), Item(9438), Item(9176), 24, 16.0, Animation(6672)),
    MITHRIL_CROSSBOW(Item(9461), Item(9438), Item(9181), 52, 32.0, Animation(6675)),
    ADAMANT_CROSSBOW(Item(9463), Item(9438), Item(9183), 61, 41.0, Animation(6676)),
    RUNITE_CROSSBOW(Item(9465), Item(9438), Item(9185), 69, 50.0, Animation(6677)),

    /**
     * Arrows
     */
    HEADLESS_ARROWS(Item(52, 15), Item(314, 15), Item(53, 15), 1, 1.0),
    BRONZE_ARROWS(Item(39, 15), Item(53, 15), Item(882, 15), 1, 0.4),
    IRON_ARROWS(Item(40, 15), Item(53, 15), Item(884, 15), 15, 3.8),
    STEEL_ARROWS(Item(41, 15), Item(53, 15), Item(886, 15), 30, 6.3),
    MITHRIL_ARROWS(Item(42, 15), Item(53, 15), Item(888, 15), 45, 8.8),
    ADAMANT_ARROWS(Item(43, 15), Item(53, 15), Item(890, 15), 60, 11.3),
    RUNITE_ARROWS(Item(44, 15), Item(53, 15), Item(892, 15), 75, 13.8),
    DRAGON_ARROWS(Item(11237, 15), Item(53, 15), Item(11212, 15), 90, 16.3),
    BROAD_ARROWS(Item(13278, 15), Item(53, 15), Item(4160, 15), 52, 15.0),

    /**
     * Bolts
     */
    BRONZE_BOLT(Item(9375, 10), Item(314, 10), Item(877, 10), 9, 0.5),
    IRON_BOLT(Item(9377, 10), Item(314, 10), Item(9140, 10), 39, 1.5),
    STEEL_BOLT(Item(9378, 10), Item(314, 10), Item(9141, 10), 46, 3.5),
    MITHRIL_BOLT(Item(9379, 10), Item(314, 10), Item(9142, 10), 54, 5.0),
    ADAMANT_BOLT(Item(9380, 10), Item(314, 10), Item(9143, 10), 61, 7.0),
    RUNITE_BOLT(Item(9381, 10), Item(314, 10), Item(9144, 10), 69, 10.0),
    OPAL_BOLTS(Item(45, 10), Item(877, 10), Item(879, 10), 11, 1.6),
    BLURITE_BOLTS(Item(9376, 10), Item(314, 10), Item(9139, 10), 24, 1.0),
    JADE_BOLTS(Item(9139, 10), Item(9187, 10), Item(9335, 10), 26, 2.4),
    PEARL_BOLTS(Item(9140, 10), Item(46, 10), Item(880, 10), 41, 3.2),
    SILVER_BOLTS(Item(9382, 10), Item(314, 10), Item(9145, 10), 43, 2.5),
    RED_TOPAZ_BOLTS(Item(9188, 10), Item(9141, 10), Item(9336, 10), 48, 3.9),
    BARBED_BOLTS(Item(47, 10), Item(877, 10), Item(881, 10), 51, 9.5),
    SAPPHIRE_BOLTS(Item(9189, 10), Item(9142, 10), Item(9337, 10), 56, 2.4),
    EMERALD_BOLTS(Item(9142, 10), Item(9187, 10), Item(9335, 10), 26, 4.7),
    RUBY_BOLTS(Item(9191, 10), Item(9143, 10), Item(9339, 10), 63, 6.3),
    DIAMOND_BOLTS(Item(9143, 10), Item(9192, 10), Item(9340, 10), 65, 7.0),
    DRAGON_BOLTS(Item(9193, 10), Item(9144, 10), Item(9341, 10), 71, 8.2),
    ONYX_BOLTS(Item(9194, 10), Item(9144, 10), Item(9342, 10), 73, 9.4),
    BROAD_BOLTS(Item(13279, 10), Item(314, 10), Item(13280, 10), 55, 3.0),

    /**
     * Darts
     */
    BRONZE_DART(Item(819, 10), Item(314, 10), Item(806, 10), 1, 0.8),
    IRON_DART(Item(820, 10), Item(314, 10), Item(807, 10), 22, 1.0),
    STEEL_DART(Item(821, 10), Item(314, 10), Item(808, 10), 37, 1.7),
    MITHRIL_DART(Item(822, 10), Item(314, 10), Item(809, 10), 52, 4.0),
    ADAMANT_DART(Item(823, 10), Item(314, 10), Item(810, 10), 67, 7.6),
    RUNITE_DART(Item(824, 10), Item(314, 10), Item(811, 10), 81, 12.2),
    DRAGON_DART(Item(11232, 10), Item(314, 10), Item(11230, 10), 95, 18.4),

    /**
     * Bolt tips
     */
    OPAL_BOLTS_TIPS(Item(1609), Fletching.CHISLE, Item(45, 12), 11, 1.5, Animation(886)),
    JADE_BOLT_TIPS(Item(1611), Fletching.CHISLE, Item(9187, 12), 26, 2.0, Animation(886)),
    PEARL_BOLT_TIPS(Item(411), Fletching.CHISLE, Item(46, 12), 41, 2.0, Animation(886)),
    TOPAZ_BOLT_TIPS(Item(1613), Fletching.CHISLE, Item(9188, 12), 48, 3.9, Animation(887)),
    SAPPHIRE_BOLT_TIPS(Item(1607), Fletching.CHISLE, Item(9189, 12), 56, 4.0, Animation(888)),
    EMERALD_BOLT_TIPS(Item(1605), Fletching.CHISLE, Item(9190, 12), 58, 5.5, Animation(889)),
    RUBY_BOLT_TIPS(Item(1603), Fletching.CHISLE, Item(9191, 12), 63, 6.3, Animation(887)),
    DIAMOND_BOLT_TIPS(Item(1601), Fletching.CHISLE, Item(9192, 12), 65, 7.0, Animation(890)),
    DRAGON_BOLT_TIPS(Item(1615), Fletching.CHISLE, Item(9193, 12), 71, 8.2, Animation(885)),
    ONYX_BOLT_TIPS(Item(6573), Fletching.CHISLE, Item(9194, 12), 73, 9.4, Animation(2717)),

    /**
     * Misc
     */
    DRAMEN_BRANCH(Item(771), Fletching.KNIFE, Item(772, 12), 1, 0.0, Animation(6702)),
    SAGIE_SHAFTS(Item(21351), Fletching.KNIFE, Item(21353, 5), 83, 0.0, Animation(3007)),
    SAGIE(Item(21353, 5), Item(21358, 5), Item(21364, 5), 83, 40.0, Animation(3113), Graphic(450)),
    BOLAS(Item(21359, 2), Item(21358), Item(21365), 87, 50.0, Animation(3114), Graphic(467)),


    /**
     * Shortbow stringing
     */
    STRUNG_SHORT_BOW(Item(50), Fletching.BOW_STRING, Item(841), 5, 5.0, Animation(6678)),
    STRUNG_OAK_SHORT_BOW(Item(54), Fletching.BOW_STRING, Item(843), 20, 16.5, Animation(6679)),
    STRUNG_WILLOW_SHORT_BOW(Item(60), Fletching.BOW_STRING, Item(849), 35, 33.3, Animation(6680)),
    STRUNG_MAPLE_SHORT_BOW(Item(64), Fletching.BOW_STRING, Item(853), 50, 50.0, Animation(6681)),
    STRUNG_YEW_SHORT_BOW(Item(68), Fletching.BOW_STRING, Item(857), 65, 67.5, Animation(6682)),
    STRUNG_MAGIC_SHORT_BOW(Item(72), Fletching.BOW_STRING, Item(861), 80, 83.25, Animation(6683)),

    /**
     * Longbow stringing
     */
    STRUNG_LONG_BOW(Item(48), Fletching.BOW_STRING, Item(839), 10, 10.0, Animation(6684)),
    STRUNG_OAK_LONG_BOW(Item(56), Fletching.BOW_STRING, Item(845), 25, 25.0, Animation(6685)),
    STRUNG_WILLOW_LONG_BOW(Item(58), Fletching.BOW_STRING, Item(847), 40, 41.5, Animation(6686)),
    STRUNG_MAPLE_LONG_BOW(Item(62), Fletching.BOW_STRING, Item(851), 55, 58.3, Animation(6687)),
    STRUNG_YEW_LONG_BOW(Item(66), Fletching.BOW_STRING, Item(855), 70, 75.0, Animation(6688)),
    STRUNG_MAGIC_LONG_BOW(Item(70), Fletching.BOW_STRING, Item(859), 85, 91.5, Animation(6689)),

    /**
     * Dungeoneering
     */

    HEADLESS_ARROW(Item(17742, 15), Item(17796, 15), Item(17747, 15), 1, 0.3),

    NOVITE_ARROW(Item(17885, 15), Item(Fletching.DUNGEONEERING_HEADLESS, 15), Item(16427, 15), 1, 1.3, Animation(1248)),
    BATHUS_ARROW(Item(17890, 15), Item(Fletching.DUNGEONEERING_HEADLESS, 15), Item(16432, 15), 11, 2.5, Animation(1248)),
    MARMAROS_ARROW(Item(17895, 15), Item(Fletching.DUNGEONEERING_HEADLESS, 15), Item(16437, 15), 22, 5.0, Animation(1248)),
    KRATONITE_ARROW(Item(17900, 15), Item(Fletching.DUNGEONEERING_HEADLESS, 15), Item(16442, 15), 33, 7.5, Animation(1248)),
    FRACTITE_ARROW(Item(17905, 15), Item(Fletching.DUNGEONEERING_HEADLESS, 15), Item(16447, 15), 44, 10.0, Animation(1248)),
    ZEPHYRIUM_ARROW(Item(17910, 15), Item(Fletching.DUNGEONEERING_HEADLESS, 15), Item(16452, 15), 55, 12.5, Animation(1248)),
    AGRONITE_ARROWS(Item(17915, 15), Item(Fletching.DUNGEONEERING_HEADLESS, 15), Item(16457, 15), 66, 15.0, Animation(1248)),
    KATAGON_ARROWS(Item(17920, 15), Item(Fletching.DUNGEONEERING_HEADLESS, 15), Item(16462, 15), 77, 17.5, Animation(1248)),
    GORGONITE_ARROWS(Item(17925, 15), Item(Fletching.DUNGEONEERING_HEADLESS, 15), Item(16467, 15), 88, 20.0, Animation(1248)),
    PROMETHIUM_ARROWS(Item(17930, 15), Item(Fletching.DUNGEONEERING_HEADLESS, 15), Item(16472, 15), 99, 22.5, Animation(1248)),

    /**
     * Short bow stringing
     */
    TANGLE_SHORT_BOW(Item(17702), Fletching.DUNGEEONEERING_BOW_STRING, Item(16867), 1, 5.0, Animation(13225)),
    SEEPING_SHORT_BOW(Item(17704), Fletching.DUNGEEONEERING_BOW_STRING, Item(16869), 11, 9.0, Animation(13226)),
    BLOOD_SHORT_BOW(Item(17706), Fletching.DUNGEEONEERING_BOW_STRING, Item(16871), 21, 15.0, Animation(13227)),
    UTUKU_SHORT_BOW(Item(17708), Fletching.DUNGEEONEERING_BOW_STRING, Item(16873), 31, 23.0, Animation(13228)),
    SPINEBEAM_SHORT_BOW(Item(17710), Fletching.DUNGEEONEERING_BOW_STRING, Item(16875), 41, 33.0, Animation(13229)),
    BOVISTRANGLER_SHORT_BOW(Item(17712), Fletching.DUNGEEONEERING_BOW_STRING, Item(16877), 51, 45.0, Animation(13230)),
    THIGAT_SHORT_BOW(Item(17714), Fletching.DUNGEEONEERING_BOW_STRING, Item(16879), 61, 59.0, Animation(13231)),
    CORPSETHORN_SHORT_BOW(Item(17716), Fletching.DUNGEEONEERING_BOW_STRING, Item(16881), 71, 75.0, Animation(13232)),
    ENTGALLOW_SHORT_BOW(Item(17718), Fletching.DUNGEEONEERING_BOW_STRING, Item(16883), 81, 93.0, Animation(13233)),
    GRAVE_CREEPER_SHORT_BOW(Item(17720), Fletching.DUNGEEONEERING_BOW_STRING, Item(16885), 91, 113.0, Animation(13234)),

    /**
     * Long bow stringing
     */
    TANGLE_LONG_BOW(Item(17722), Fletching.DUNGEEONEERING_BOW_STRING, Item(16317), 6, 5.7, Animation(13235)),
    SEEPING_LONG_BOW(Item(17724), Fletching.DUNGEEONEERING_BOW_STRING, Item(16319), 16, 10.3, Animation(13236)),
    BLOOD_LONG_BOW(Item(17726), Fletching.DUNGEEONEERING_BOW_STRING, Item(16321), 26, 17.2, Animation(13237)),
    UTUKU_LONG_BOW(Item(17728), Fletching.DUNGEEONEERING_BOW_STRING, Item(16323), 36, 26.4, Animation(13238)),
    SPINEBEAM_LONG_BOW(Item(17730), Fletching.DUNGEEONEERING_BOW_STRING, Item(16325), 46, 37.9, Animation(13239)),
    BOVISTRANGLER_LONG_BOW(Item(17732), Fletching.DUNGEEONEERING_BOW_STRING, Item(16327), 56, 51.7, Animation(13240)),
    THIGAT_LONG_BOW(Item(17734), Fletching.DUNGEEONEERING_BOW_STRING, Item(16329), 66, 67.8, Animation(13241)),
    CORPSETHORN_LONG_BOW(Item(17736), Fletching.DUNGEEONEERING_BOW_STRING, Item(16331), 76, 86.2, Animation(13242)),
    ENTGALLOW_LONG_BOW(Item(17738), Fletching.DUNGEEONEERING_BOW_STRING, Item(16333), 86, 106.9, Animation(13243)),
    GRAVE_CREEPER_LONG_BOW(Item(17740), Fletching.DUNGEEONEERING_BOW_STRING, Item(16335), 96, 129.9, Animation(13244)),

    /**
     * Sets
     */

    REGULAR_BOW(Item(1511), Item(946), arrayOf<Item>(Item(52, 15), Item(50), Item(48), Item(9440)), arrayOf(1, 5, 10, 9), arrayOf(5.0, 5.0, 10.0, 6.0), Animation(6702)),
    OAK_BOW(Item(1521), Fletching.KNIFE, arrayOf<Item>(Item(54), Item(56), Item(9442)), arrayOf(20, 25, 24), arrayOf(16.5, 25.0, 16.0), Animation(6702)),
    WILLOW_BOW(Item(1519), Fletching.KNIFE, arrayOf<Item>(Item(60), Item(58), Item(9444)), arrayOf(35, 40, 39), arrayOf(33.3, 41.5, 22.0), Animation(6702)),
    MAPLE_BOW(Item(1517), Fletching.KNIFE, arrayOf<Item>(Item(64), Item(62), Item(9448)), arrayOf(50, 55, 54), arrayOf(50.0, 58.3, 32.0), Animation(6702)),
    YEW_BOW(Item(1515), Fletching.KNIFE, arrayOf<Item>(Item(68), Item(66), Item(9452)), arrayOf(65, 70, 69), arrayOf(67.5, 75.0, 50.0), Animation(6702)),
    MAGIC_BOW(Item(1513), Fletching.KNIFE, arrayOf<Item>(Item(72), Item(70)), arrayOf(80, 85), arrayOf(83.25, 91.5), Animation(6702)),

    TANGLE_GUM(Item(17682), Fletching.DUNGEONEERING_KNIFE, arrayOf<Item>(Item(17702), Item(17722), Item(17742), Item(16977), Item(17756)), arrayOf(1, 6, 1, 8, 3), arrayOf(5.0, 5.7, 4.5, 8.0, 12.0), Animation(1248)),
    SEEPING_ELM(Item(17684), Fletching.DUNGEONEERING_KNIFE, arrayOf<Item>(Item(17704), Item(17724), Item(17742), Item(16979), Item(17758)), arrayOf(11, 16, 10, 18, 13), arrayOf(9.0, 10.3, 6.3, 12.0, 21.6), Animation(1248)),
    BLOOD_SPINDLE(Item(17686), Fletching.DUNGEONEERING_KNIFE, arrayOf<Item>(Item(17706), Item(17726), Item(17742), Item(16981), Item(17760)), arrayOf(21, 26, 20, 28, 23), arrayOf(15.0, 17.2, 7.8, 32.0, 36.0), Animation(1248)),
    UTUKU(Item(17688), Fletching.DUNGEONEERING_KNIFE, arrayOf<Item>(Item(17708), Item(17728), Item(17742), Item(16983), Item(17762)), arrayOf(31, 36, 30, 38, 33), arrayOf(23.0, 26.4, 9.6, 43.2, 55.2), Animation(1248)),
    SPINEBEAM(Item(17690), Fletching.DUNGEONEERING_KNIFE, arrayOf<Item>(Item(17710), Item(17730), Item(17742), Item(16985), Item(17764)), arrayOf(41, 46, 40, 48, 43), arrayOf(33.0, 37.9, 11.1, 76.8, 79.2), Animation(1248)),
    BOVISTRANGLER(Item(17692), Fletching.DUNGEONEERING_KNIFE, arrayOf<Item>(Item(17712), Item(17732), Item(17742), Item(16987), Item(17766)), arrayOf(51, 56, 50, 58, 53), arrayOf(45.0, 51.7, 12.9, 93.3, 108.0), Animation(1248)),
    THIGAT(Item(17694), Fletching.DUNGEONEERING_KNIFE, arrayOf<Item>(Item(17714), Item(17734), Item(17742), Item(16989), Item(17768)), arrayOf(61, 66, 1, 68, 63), arrayOf(59.0, 67.8, 14.4, 113.4, 141.6), Animation(1248)),
    CORPSETHORN(Item(17696), Fletching.DUNGEONEERING_KNIFE, arrayOf<Item>(Item(17716), Item(17736), Item(17742), Item(16991), Item(17770)), arrayOf(71, 76, 70, 78, 73), arrayOf(75.0, 86.2, 16.2, 137.7, 180.0), Animation(1248)),
    ENTGALLOW(Item(17698), Fletching.DUNGEONEERING_KNIFE, arrayOf<Item>(Item(17718), Item(17738), Item(17742), Item(16993), Item(17772)), arrayOf(81, 86, 80, 88, 83), arrayOf(93.0, 106.9, 17.7, 167.4, 223.2), Animation(1248)),
    GRAVE_CREEPER(Item(17700), Fletching.DUNGEONEERING_KNIFE, arrayOf<Item>(Item(17720), Item(17740), Item(17742), Item(16995), Item(17774)), arrayOf(91, 96, 90, 98, 93), arrayOf(113.0, 129.9, 19.5, 203.4, 271.2), Animation(1248));

    constructor(item: Item, selected: Item, product: Item, level: Int, xp: Double, animation: Animation? = null, graphic: Graphic? = null) : this(item, selected, arrayOf(product), arrayOf(level), arrayOf(xp), animation, graphic)

}