package com.fury.game.entity.character.combat.range;

import com.fury.cache.Revision;
import com.fury.game.container.impl.equip.Slot;
import com.fury.core.model.node.entity.actor.figure.player.Player;

/**
 * A table of constants that hold data for all ranged ammo.
 * 
 * @author lare96
 */
public class CombatRangedAmmo {

	//TODO: Add poisonous ammo
	public enum RangedWeaponData {
		TANGLE_GUM_SHORTBOW(new int[] {15775, 16867}, new AmmunitionData[] {AmmunitionData.NOVITE_ARROW}, RangedWeaponType.SHORTBOW),
		SEEPING_ELM_SHORTBOW(new int[] {15776, 16869}, new AmmunitionData[] {AmmunitionData.BATHUS_ARROW, AmmunitionData.NOVITE_ARROW}, RangedWeaponType.SHORTBOW),
		BLOOD_SPINDLE_SHORTBOW(new int[] {15777, 16871}, new AmmunitionData[] {AmmunitionData.MARMAROS_ARROW, AmmunitionData.BATHUS_ARROW, AmmunitionData.NOVITE_ARROW}, RangedWeaponType.SHORTBOW),
		UTUKU_SHORTBOW(new int[] {15778, 16873}, new AmmunitionData[] {AmmunitionData.KRATONITE_ARROW, AmmunitionData.MARMAROS_ARROW, AmmunitionData.BATHUS_ARROW, AmmunitionData.NOVITE_ARROW}, RangedWeaponType.SHORTBOW),
		SPINEBEAM_SHORTBOW(new int[] {15779, 16875}, new AmmunitionData[] {AmmunitionData.FRACTITE_ARROW, AmmunitionData.KRATONITE_ARROW, AmmunitionData.MARMAROS_ARROW, AmmunitionData.BATHUS_ARROW, AmmunitionData.NOVITE_ARROW}, RangedWeaponType.SHORTBOW),
		BOVISTRANGLER_SHORTBOW(new int[] {15780, 16877}, new AmmunitionData[] {AmmunitionData.ZEPHYRIUM_ARROW, AmmunitionData.FRACTITE_ARROW, AmmunitionData.KRATONITE_ARROW, AmmunitionData.MARMAROS_ARROW, AmmunitionData.BATHUS_ARROW, AmmunitionData.NOVITE_ARROW}, RangedWeaponType.SHORTBOW),
		THIGAT_SHORTBOW(new int[] {15781, 16879}, new AmmunitionData[] {AmmunitionData.ARGONITE_ARROW, AmmunitionData.ZEPHYRIUM_ARROW, AmmunitionData.FRACTITE_ARROW, AmmunitionData.KRATONITE_ARROW, AmmunitionData.MARMAROS_ARROW, AmmunitionData.BATHUS_ARROW, AmmunitionData.NOVITE_ARROW}, RangedWeaponType.SHORTBOW),
		CORPSETHORN_SHORTBOW(new int[] {15782, 16881}, new AmmunitionData[] {AmmunitionData.KATAGON_ARROW, AmmunitionData.ARGONITE_ARROW, AmmunitionData.ZEPHYRIUM_ARROW, AmmunitionData.FRACTITE_ARROW, AmmunitionData.KRATONITE_ARROW, AmmunitionData.MARMAROS_ARROW, AmmunitionData.BATHUS_ARROW, AmmunitionData.NOVITE_ARROW}, RangedWeaponType.SHORTBOW),
		ENTGALLOW_SHORTBOW(new int[] {15783, 16883}, new AmmunitionData[] {AmmunitionData.GORGONITE_ARROW, AmmunitionData.KATAGON_ARROW, AmmunitionData.ARGONITE_ARROW, AmmunitionData.ZEPHYRIUM_ARROW, AmmunitionData.FRACTITE_ARROW, AmmunitionData.KRATONITE_ARROW, AmmunitionData.MARMAROS_ARROW, AmmunitionData.BATHUS_ARROW, AmmunitionData.NOVITE_ARROW}, RangedWeaponType.SHORTBOW),
		GRAVE_CREEPER_SHORTBOW(new int[] {15784, 16885}, new AmmunitionData[] {AmmunitionData.PROMETHIUM_ARROW, AmmunitionData.GORGONITE_ARROW, AmmunitionData.KATAGON_ARROW, AmmunitionData.ARGONITE_ARROW, AmmunitionData.ZEPHYRIUM_ARROW, AmmunitionData.FRACTITE_ARROW, AmmunitionData.KRATONITE_ARROW, AmmunitionData.MARMAROS_ARROW, AmmunitionData.BATHUS_ARROW, AmmunitionData.NOVITE_ARROW}, RangedWeaponType.SHORTBOW),
		SAGITTARIAN_SHORTBOW(new int[] {15785, 16887}, new AmmunitionData[] {AmmunitionData.SAGITTARIAN_ARROW, AmmunitionData.PROMETHIUM_ARROW, AmmunitionData.GORGONITE_ARROW, AmmunitionData.KATAGON_ARROW, AmmunitionData.ARGONITE_ARROW, AmmunitionData.ZEPHYRIUM_ARROW, AmmunitionData.FRACTITE_ARROW, AmmunitionData.KRATONITE_ARROW, AmmunitionData.MARMAROS_ARROW, AmmunitionData.BATHUS_ARROW, AmmunitionData.NOVITE_ARROW}, RangedWeaponType.SHORTBOW),
		
		TANGLE_GUM_LONGBOW(new int[] {15903, 16317}, new AmmunitionData[] {AmmunitionData.NOVITE_ARROW}, RangedWeaponType.LONGBOW),
		SEEPING_ELM_LONGBOW(new int[] {15904, 16319}, new AmmunitionData[] {AmmunitionData.BATHUS_ARROW, AmmunitionData.NOVITE_ARROW}, RangedWeaponType.LONGBOW),
		BLOOD_SPINDLE_LONGBOW(new int[] {15905, 16321}, new AmmunitionData[] {AmmunitionData.MARMAROS_ARROW, AmmunitionData.BATHUS_ARROW, AmmunitionData.NOVITE_ARROW}, RangedWeaponType.LONGBOW),
		UTUKU_LONGBOW(new int[] {15906, 16323}, new AmmunitionData[] {AmmunitionData.KRATONITE_ARROW, AmmunitionData.MARMAROS_ARROW, AmmunitionData.BATHUS_ARROW, AmmunitionData.NOVITE_ARROW}, RangedWeaponType.LONGBOW),
		SPINEBEAM_LONGBOW(new int[] {15907, 16325}, new AmmunitionData[] {AmmunitionData.FRACTITE_ARROW, AmmunitionData.KRATONITE_ARROW, AmmunitionData.MARMAROS_ARROW, AmmunitionData.BATHUS_ARROW, AmmunitionData.NOVITE_ARROW}, RangedWeaponType.LONGBOW),
		BOVISTRANGLER_LONGBOW(new int[] {15908, 16327}, new AmmunitionData[] {AmmunitionData.ZEPHYRIUM_ARROW, AmmunitionData.FRACTITE_ARROW, AmmunitionData.KRATONITE_ARROW, AmmunitionData.MARMAROS_ARROW, AmmunitionData.BATHUS_ARROW, AmmunitionData.NOVITE_ARROW}, RangedWeaponType.LONGBOW),
		THIGAT_LONGBOW(new int[] {15909, 16329}, new AmmunitionData[] {AmmunitionData.ARGONITE_ARROW, AmmunitionData.ZEPHYRIUM_ARROW, AmmunitionData.FRACTITE_ARROW, AmmunitionData.KRATONITE_ARROW, AmmunitionData.MARMAROS_ARROW, AmmunitionData.BATHUS_ARROW, AmmunitionData.NOVITE_ARROW}, RangedWeaponType.LONGBOW),
		CORPSETHORN_LONGBOW(new int[] {15910, 16331}, new AmmunitionData[] {AmmunitionData.KATAGON_ARROW, AmmunitionData.ARGONITE_ARROW, AmmunitionData.ZEPHYRIUM_ARROW, AmmunitionData.FRACTITE_ARROW, AmmunitionData.KRATONITE_ARROW, AmmunitionData.MARMAROS_ARROW, AmmunitionData.BATHUS_ARROW, AmmunitionData.NOVITE_ARROW}, RangedWeaponType.LONGBOW),
		ENTGALLOW_LONGBOW(new int[] {15911, 16333}, new AmmunitionData[] {AmmunitionData.GORGONITE_ARROW, AmmunitionData.KATAGON_ARROW, AmmunitionData.ARGONITE_ARROW, AmmunitionData.ZEPHYRIUM_ARROW, AmmunitionData.FRACTITE_ARROW, AmmunitionData.KRATONITE_ARROW, AmmunitionData.MARMAROS_ARROW, AmmunitionData.BATHUS_ARROW, AmmunitionData.NOVITE_ARROW}, RangedWeaponType.LONGBOW),
		GRAVE_CREEPER_LONGBOW(new int[] {15912, 16335}, new AmmunitionData[] {AmmunitionData.PROMETHIUM_ARROW, AmmunitionData.GORGONITE_ARROW, AmmunitionData.KATAGON_ARROW, AmmunitionData.ARGONITE_ARROW, AmmunitionData.ZEPHYRIUM_ARROW, AmmunitionData.FRACTITE_ARROW, AmmunitionData.KRATONITE_ARROW, AmmunitionData.MARMAROS_ARROW, AmmunitionData.BATHUS_ARROW, AmmunitionData.NOVITE_ARROW}, RangedWeaponType.LONGBOW),
		SAGITTARIAN_LONGBOW(new int[] {15913, 16337}, new AmmunitionData[] {AmmunitionData.SAGITTARIAN_ARROW, AmmunitionData.PROMETHIUM_ARROW, AmmunitionData.GORGONITE_ARROW, AmmunitionData.KATAGON_ARROW, AmmunitionData.ARGONITE_ARROW, AmmunitionData.ZEPHYRIUM_ARROW, AmmunitionData.FRACTITE_ARROW, AmmunitionData.KRATONITE_ARROW, AmmunitionData.MARMAROS_ARROW, AmmunitionData.BATHUS_ARROW, AmmunitionData.NOVITE_ARROW}, RangedWeaponType.LONGBOW),

		HEXHUNTER_BOW(new int[] {15836, 17295}, new AmmunitionData[] {AmmunitionData.SAGITTARIAN_ARROW, AmmunitionData.PROMETHIUM_ARROW, AmmunitionData.GORGONITE_ARROW, AmmunitionData.KATAGON_ARROW, AmmunitionData.ARGONITE_ARROW, AmmunitionData.ZEPHYRIUM_ARROW, AmmunitionData.FRACTITE_ARROW, AmmunitionData.KRATONITE_ARROW, AmmunitionData.MARMAROS_ARROW, AmmunitionData.BATHUS_ARROW, AmmunitionData.NOVITE_ARROW}, RangedWeaponType.LONGBOW),

		OGRE_BOW(new int[] {2883}, new AmmunitionData[] {AmmunitionData.OGRE_ARROW}, RangedWeaponType.LONGBOW),
		OGRE_COMPOSITE_BOW(new int[] {4827}, new AmmunitionData[] {AmmunitionData.BRONZE_BRUTAL, AmmunitionData.IRON_BRUTAL, AmmunitionData.STEEL_BRUTAL, AmmunitionData.BLACK_BRUTAL, AmmunitionData.MITHRIL_BRUTAL, AmmunitionData.ADAMANT_BRUTAL, AmmunitionData.RUNE_BRUTAL}, RangedWeaponType.COMPOSITE_BOW),
		LONGBOW(new int[] {839}, new AmmunitionData[] {AmmunitionData.BRONZE_ARROW, AmmunitionData.IRON_ARROW}, RangedWeaponType.LONGBOW),
		SHORTBOW(new int[] {841}, new AmmunitionData[] {AmmunitionData.BRONZE_ARROW, AmmunitionData.IRON_ARROW}, RangedWeaponType.SHORTBOW),
		OAK_LONGBOW(new int[] {845}, new AmmunitionData[] {AmmunitionData.BRONZE_ARROW, AmmunitionData.IRON_ARROW, AmmunitionData.STEEL_ARROW}, RangedWeaponType.LONGBOW),
		OAK_SHORTBOW(new int[] {843}, new AmmunitionData[] {AmmunitionData.BRONZE_ARROW, AmmunitionData.IRON_ARROW, AmmunitionData.STEEL_ARROW}, RangedWeaponType.SHORTBOW),
		WILLOW_COMPOSITE_BOW(new int[] {10280}, new AmmunitionData[] {AmmunitionData.BRONZE_ARROW, AmmunitionData.IRON_ARROW, AmmunitionData.STEEL_ARROW, AmmunitionData.MITHRIL_ARROW}, RangedWeaponType.COMPOSITE_BOW),
		WILLOW_LONGBOW(new int[] {847}, new AmmunitionData[] {AmmunitionData.BRONZE_ARROW, AmmunitionData.IRON_ARROW, AmmunitionData.STEEL_ARROW, AmmunitionData.MITHRIL_ARROW}, RangedWeaponType.LONGBOW),
		WILLOW_SHORTBOW(new int[] {849}, new AmmunitionData[] {AmmunitionData.BRONZE_ARROW, AmmunitionData.IRON_ARROW, AmmunitionData.STEEL_ARROW, AmmunitionData.MITHRIL_ARROW}, RangedWeaponType.SHORTBOW),
		MAPLE_LONGBOW(new int[] {851}, new AmmunitionData[] {AmmunitionData.BRONZE_ARROW, AmmunitionData.IRON_ARROW, AmmunitionData.STEEL_ARROW, AmmunitionData.MITHRIL_ARROW, AmmunitionData.ADAMANT_ARROW}, RangedWeaponType.LONGBOW),
		MAPLE_SHORTBOW(new int[] {853}, new AmmunitionData[] {AmmunitionData.BRONZE_ARROW, AmmunitionData.IRON_ARROW, AmmunitionData.STEEL_ARROW, AmmunitionData.MITHRIL_ARROW, AmmunitionData.ADAMANT_ARROW}, RangedWeaponType.SHORTBOW),
		MAPLE_LONGBOW_SIGHTED(new int[] {18331}, new AmmunitionData[] {AmmunitionData.BRONZE_ARROW, AmmunitionData.IRON_ARROW, AmmunitionData.STEEL_ARROW, AmmunitionData.MITHRIL_ARROW, AmmunitionData.ADAMANT_ARROW}, RangedWeaponType.LONGBOW),
		YEW_COMPOSITE_BOW(new int[] {10282}, new AmmunitionData[] {AmmunitionData.BRONZE_ARROW, AmmunitionData.IRON_ARROW, AmmunitionData.STEEL_ARROW, AmmunitionData.MITHRIL_ARROW, AmmunitionData.ADAMANT_ARROW, AmmunitionData.RUNE_ARROW, AmmunitionData.ICE_ARROW}, RangedWeaponType.COMPOSITE_BOW),
		YEW_LONGBOW(new int[] {855}, new AmmunitionData[] {AmmunitionData.BRONZE_ARROW, AmmunitionData.IRON_ARROW, AmmunitionData.STEEL_ARROW, AmmunitionData.MITHRIL_ARROW, AmmunitionData.ADAMANT_ARROW, AmmunitionData.RUNE_ARROW, AmmunitionData.ICE_ARROW}, RangedWeaponType.LONGBOW),
		YEW_SHORTBOW(new int[] {857}, new AmmunitionData[] {AmmunitionData.BRONZE_ARROW, AmmunitionData.IRON_ARROW, AmmunitionData.STEEL_ARROW, AmmunitionData.MITHRIL_ARROW, AmmunitionData.ADAMANT_ARROW, AmmunitionData.RUNE_ARROW, AmmunitionData.ICE_ARROW}, RangedWeaponType.SHORTBOW),
		//Sacred clay bow 14121
		MAGIC_LONGBOW_SIGHTED(new int[] {18332}, new AmmunitionData[] {AmmunitionData.BRONZE_ARROW, AmmunitionData.IRON_ARROW, AmmunitionData.STEEL_ARROW, AmmunitionData.MITHRIL_ARROW, AmmunitionData.ADAMANT_ARROW, AmmunitionData.RUNE_ARROW, AmmunitionData.ICE_ARROW, AmmunitionData.BROAD_ARROW}, RangedWeaponType.LONGBOW),
		MAGIC_COMPOSITE_BOW(new int[] {10284}, new AmmunitionData[] {AmmunitionData.BRONZE_ARROW, AmmunitionData.IRON_ARROW, AmmunitionData.STEEL_ARROW, AmmunitionData.MITHRIL_ARROW, AmmunitionData.ADAMANT_ARROW, AmmunitionData.RUNE_ARROW, AmmunitionData.ICE_ARROW, AmmunitionData.BROAD_ARROW}, RangedWeaponType.COMPOSITE_BOW),
		MAGIC_LONGBOW(new int[] {859}, new AmmunitionData[] {AmmunitionData.BRONZE_ARROW, AmmunitionData.IRON_ARROW, AmmunitionData.STEEL_ARROW, AmmunitionData.MITHRIL_ARROW, AmmunitionData.ADAMANT_ARROW, AmmunitionData.RUNE_ARROW, AmmunitionData.ICE_ARROW, AmmunitionData.BROAD_ARROW}, RangedWeaponType.LONGBOW),
		MAGIC_SHORTBOW(new int[] {861}, new AmmunitionData[] {AmmunitionData.BRONZE_ARROW, AmmunitionData.IRON_ARROW, AmmunitionData.STEEL_ARROW, AmmunitionData.MITHRIL_ARROW, AmmunitionData.ADAMANT_ARROW, AmmunitionData.RUNE_ARROW, AmmunitionData.ICE_ARROW, AmmunitionData.BROAD_ARROW}, RangedWeaponType.SHORTBOW),
		SEERCUL(new int[] {6724}, new AmmunitionData[] {AmmunitionData.BRONZE_ARROW, AmmunitionData.IRON_ARROW, AmmunitionData.STEEL_ARROW, AmmunitionData.MITHRIL_ARROW, AmmunitionData.ADAMANT_ARROW, AmmunitionData.RUNE_ARROW, AmmunitionData.ICE_ARROW, AmmunitionData.BROAD_ARROW}, RangedWeaponType.SHORTBOW),
		GUTHIX_BOW(new int[] {19146}, new AmmunitionData[] {AmmunitionData.BRONZE_ARROW, AmmunitionData.IRON_ARROW, AmmunitionData.STEEL_ARROW, AmmunitionData.MITHRIL_ARROW, AmmunitionData.ADAMANT_ARROW, AmmunitionData.RUNE_ARROW, AmmunitionData.GUTHIX_ARROW}, RangedWeaponType.LONGBOW),
		SARADOMIN_BOW(new int[] {19143}, new AmmunitionData[] {AmmunitionData.BRONZE_ARROW, AmmunitionData.IRON_ARROW, AmmunitionData.STEEL_ARROW, AmmunitionData.MITHRIL_ARROW, AmmunitionData.ADAMANT_ARROW, AmmunitionData.RUNE_ARROW, AmmunitionData.SARADOMIN_ARROW}, RangedWeaponType.LONGBOW),
		ZAMORAK_BOW(new int[] {19149}, new AmmunitionData[] {AmmunitionData.BRONZE_ARROW, AmmunitionData.IRON_ARROW, AmmunitionData.STEEL_ARROW, AmmunitionData.MITHRIL_ARROW, AmmunitionData.ADAMANT_ARROW, AmmunitionData.RUNE_ARROW, AmmunitionData.ZAMORAK_ARROW}, RangedWeaponType.LONGBOW),
		ZARYTE_BOW(new int[] {20171}, new AmmunitionData[] {AmmunitionData.BRONZE_ARROW, AmmunitionData.IRON_ARROW, AmmunitionData.STEEL_ARROW, AmmunitionData.MITHRIL_ARROW, AmmunitionData.ADAMANT_ARROW, AmmunitionData.RUNE_ARROW, AmmunitionData.BROAD_ARROW, AmmunitionData.DRAGON_ARROW}, RangedWeaponType.SHORTBOW),

		DARK_BOW(new int[] {11235, 13405, 15701, 15702, 15703, 15704}, new AmmunitionData[] {AmmunitionData.BRONZE_ARROW, AmmunitionData.IRON_ARROW, AmmunitionData.STEEL_ARROW, AmmunitionData.MITHRIL_ARROW, AmmunitionData.ADAMANT_ARROW, AmmunitionData.RUNE_ARROW, AmmunitionData.DRAGON_ARROW}, RangedWeaponType.DARK_BOW),

		DORGESHUUN_CROSSBOW(new int[] {8880}, new AmmunitionData[] {AmmunitionData.BRONZE_BOLT, AmmunitionData.IRON_BOLT, AmmunitionData.BONE_BOLT}, RangedWeaponType.CROSSBOW),
		BRONZE_CROSSBOW(new int[] {9174, 837, 767}, new AmmunitionData[] {AmmunitionData.BRONZE_BOLT, AmmunitionData.OPAL_BOLT}, RangedWeaponType.CROSSBOW),
		IRON_CROSSBOW(new int[] {9177}, new AmmunitionData[] {AmmunitionData.BRONZE_BOLT, AmmunitionData.OPAL_BOLT, AmmunitionData.IRON_BOLT, AmmunitionData.PEARL_BOLT}, RangedWeaponType.CROSSBOW),
		BLURITE_CROSSBOW(new int[] {9176}, new AmmunitionData[] {AmmunitionData.BRONZE_BOLT, AmmunitionData.OPAL_BOLT, AmmunitionData.IRON_BOLT, AmmunitionData.PEARL_BOLT, AmmunitionData.BLURITE_BOLT, AmmunitionData.JADE_BOLT}, RangedWeaponType.CROSSBOW),
		STEEL_CROSSBOW(new int[] {9179}, new AmmunitionData[] {AmmunitionData.BRONZE_BOLT, AmmunitionData.OPAL_BOLT, AmmunitionData.IRON_BOLT, AmmunitionData.PEARL_BOLT, AmmunitionData.STEEL_BOLT, AmmunitionData.TOPAZ_BOLT}, RangedWeaponType.CROSSBOW),
		BLACK_CROSSBOW(new int[] {13081}, new AmmunitionData[] {AmmunitionData.BRONZE_BOLT, AmmunitionData.OPAL_BOLT, AmmunitionData.IRON_BOLT, AmmunitionData.PEARL_BOLT, AmmunitionData.STEEL_BOLT, AmmunitionData.TOPAZ_BOLT, AmmunitionData.BLACK_BOLT}, RangedWeaponType.CROSSBOW),
		MITHRIL_CROSSBOW(new int[] {9181}, new AmmunitionData[] {AmmunitionData.BRONZE_BOLT, AmmunitionData.OPAL_BOLT, AmmunitionData.IRON_BOLT, AmmunitionData.PEARL_BOLT, AmmunitionData.STEEL_BOLT, AmmunitionData.TOPAZ_BOLT, AmmunitionData.BLACK_BOLT, AmmunitionData.MITHRIL_BOLT, AmmunitionData.SAPPHIRE_BOLT, AmmunitionData.EMERALD_BOLT}, RangedWeaponType.CROSSBOW),
		ADAMANT_CROSSBOW(new int[] {9183}, new AmmunitionData[] {AmmunitionData.BRONZE_BOLT, AmmunitionData.OPAL_BOLT, AmmunitionData.IRON_BOLT, AmmunitionData.PEARL_BOLT, AmmunitionData.STEEL_BOLT, AmmunitionData.TOPAZ_BOLT, AmmunitionData.BLACK_BOLT, AmmunitionData.MITHRIL_BOLT, AmmunitionData.SAPPHIRE_BOLT, AmmunitionData.EMERALD_BOLT, AmmunitionData.ADAMANT_BOLT, AmmunitionData.RUBY_BOLT, AmmunitionData.DIAMOND_BOLT}, RangedWeaponType.CROSSBOW),
		RUNE_CROSSBOW(new int[] {9185}, new AmmunitionData[] {AmmunitionData.BRONZE_BOLT, AmmunitionData.OPAL_BOLT, AmmunitionData.IRON_BOLT, AmmunitionData.PEARL_BOLT, AmmunitionData.STEEL_BOLT, AmmunitionData.TOPAZ_BOLT, AmmunitionData.BLACK_BOLT, AmmunitionData.MITHRIL_BOLT, AmmunitionData.SAPPHIRE_BOLT, AmmunitionData.EMERALD_BOLT, AmmunitionData.ADAMANT_BOLT, AmmunitionData.RUBY_BOLT, AmmunitionData.DIAMOND_BOLT, AmmunitionData.RUNITE_BOLT, AmmunitionData.DRAGONSTONE_BOLT, AmmunitionData.ONYX_BOLT, AmmunitionData.BROAD_BOLT}, RangedWeaponType.CROSSBOW),
		CHAOTIC_CROSSBOW(new int[] {18357}, new AmmunitionData[] {AmmunitionData.BRONZE_BOLT, AmmunitionData.OPAL_BOLT, AmmunitionData.IRON_BOLT, AmmunitionData.PEARL_BOLT, AmmunitionData.STEEL_BOLT, AmmunitionData.TOPAZ_BOLT, AmmunitionData.BLACK_BOLT, AmmunitionData.MITHRIL_BOLT, AmmunitionData.SAPPHIRE_BOLT, AmmunitionData.EMERALD_BOLT, AmmunitionData.ADAMANT_BOLT, AmmunitionData.RUBY_BOLT, AmmunitionData.DIAMOND_BOLT, AmmunitionData.RUNITE_BOLT, AmmunitionData.DRAGONSTONE_BOLT, AmmunitionData.ONYX_BOLT, AmmunitionData.BROAD_BOLT}, RangedWeaponType.CROSSBOW),
		ROYAL_CROSSBOW(new int[] {24338}, new AmmunitionData[] {AmmunitionData.BRONZE_BOLT, AmmunitionData.OPAL_BOLT, AmmunitionData.IRON_BOLT, AmmunitionData.PEARL_BOLT, AmmunitionData.STEEL_BOLT, AmmunitionData.TOPAZ_BOLT, AmmunitionData.BLACK_BOLT, AmmunitionData.MITHRIL_BOLT, AmmunitionData.SAPPHIRE_BOLT, AmmunitionData.EMERALD_BOLT, AmmunitionData.ADAMANT_BOLT, AmmunitionData.RUBY_BOLT, AmmunitionData.DIAMOND_BOLT, AmmunitionData.RUNITE_BOLT, AmmunitionData.DRAGONSTONE_BOLT, AmmunitionData.ONYX_BOLT, AmmunitionData.BROAD_BOLT, AmmunitionData.ROYAL_BOLT}, RangedWeaponType.CROSSBOW),
        DOMINION_CROSSBOW(new int[] {22348}, new AmmunitionData[] {AmmunitionData.BRONZE_BOLT, AmmunitionData.OPAL_BOLT, AmmunitionData.IRON_BOLT, AmmunitionData.PEARL_BOLT, AmmunitionData.STEEL_BOLT, AmmunitionData.TOPAZ_BOLT, AmmunitionData.BLACK_BOLT, AmmunitionData.MITHRIL_BOLT, AmmunitionData.SAPPHIRE_BOLT, AmmunitionData.EMERALD_BOLT, AmmunitionData.ADAMANT_BOLT, AmmunitionData.RUBY_BOLT, AmmunitionData.DIAMOND_BOLT, AmmunitionData.RUNITE_BOLT, AmmunitionData.DRAGONSTONE_BOLT, AmmunitionData.ONYX_BOLT, AmmunitionData.BROAD_BOLT, AmmunitionData.ROYAL_BOLT}, RangedWeaponType.CROSSBOW),
		ZANIK_CROSSBOW(new int[] {14684}, new AmmunitionData[] {AmmunitionData.BRONZE_BOLT, AmmunitionData.OPAL_BOLT, AmmunitionData.IRON_BOLT, AmmunitionData.PEARL_BOLT, AmmunitionData.STEEL_BOLT, AmmunitionData.TOPAZ_BOLT, AmmunitionData.BLACK_BOLT, AmmunitionData.MITHRIL_BOLT, AmmunitionData.SAPPHIRE_BOLT, AmmunitionData.EMERALD_BOLT, AmmunitionData.ADAMANT_BOLT, AmmunitionData.RUBY_BOLT, AmmunitionData.DIAMOND_BOLT, AmmunitionData.RUNITE_BOLT, AmmunitionData.DRAGONSTONE_BOLT, AmmunitionData.ONYX_BOLT, AmmunitionData.BONE_BOLT}, RangedWeaponType.CROSSBOW),
		CORAL_CROSSBOW(new int[] {24303}, new AmmunitionData[] {AmmunitionData.CORAL_BOLT}, RangedWeaponType.CROSSBOW),
        //Hunters crossbow 10156 (Kebbit bolts 10158 & long kebbit bolts 10159)

		BRONZE_DART(new int[] {806}, new AmmunitionData[] {AmmunitionData.BRONZE_DART}, RangedWeaponType.THROW),
		IRON_DART(new int[] {807}, new AmmunitionData[] {AmmunitionData.IRON_DART}, RangedWeaponType.THROW),
		STEEL_DART(new int[] {808}, new AmmunitionData[] {AmmunitionData.STEEL_DART}, RangedWeaponType.THROW),
		MITHRIL_DART(new int[] {809}, new AmmunitionData[] {AmmunitionData.MITHRIL_DART}, RangedWeaponType.THROW),
		ADAMANT_DART(new int[] {810}, new AmmunitionData[] {AmmunitionData.ADAMANT_DART}, RangedWeaponType.THROW),
		RUNE_DART(new int[] {811}, new AmmunitionData[] {AmmunitionData.RUNE_DART}, RangedWeaponType.THROW),
		DRAGON_DART(new int[] {11230}, new AmmunitionData[] {AmmunitionData.DRAGON_DART}, RangedWeaponType.THROW),
		DEATHTOUCHED_DART(new int[] {25202}, new AmmunitionData[] {AmmunitionData.DEATHTOUCHED_DART}, RangedWeaponType.THROW),

		BRONZE_KNIFE(new int[] {864, 870, 5654}, new AmmunitionData[] {AmmunitionData.BRONZE_KNIFE}, RangedWeaponType.THROW),
		IRON_KNIFE(new int[] {863, 871, 5655}, new AmmunitionData[] {AmmunitionData.IRON_KNIFE}, RangedWeaponType.THROW),
		STEEL_KNIFE(new int[] {865, 872, 5656}, new AmmunitionData[] {AmmunitionData.STEEL_KNIFE}, RangedWeaponType.THROW),
		BLACK_KNIFE(new int[] {869, 874, 5658}, new AmmunitionData[] {AmmunitionData.BLACK_KNIFE}, RangedWeaponType.THROW),
		MITHRIL_KNIFE(new int[] {866, 873, 5657}, new AmmunitionData[] {AmmunitionData.MITHRIL_KNIFE}, RangedWeaponType.THROW),
		ADAMANT_KNIFE(new int[] {867, 875, 5659}, new AmmunitionData[] {AmmunitionData.ADAMANT_KNIFE}, RangedWeaponType.THROW),
		RUNE_KNIFE(new int[] {868, 876, 5660, 5667}, new AmmunitionData[] {AmmunitionData.RUNE_KNIFE}, RangedWeaponType.THROW),

		BRONZE_THROWNAXE(new int[] {800}, new AmmunitionData[] {AmmunitionData.BRONZE_THROWNAXE}, RangedWeaponType.THROW),
		IRON_THROWNAXE(new int[] {801}, new AmmunitionData[] {AmmunitionData.IRON_THROWNAXE}, RangedWeaponType.THROW),
		STEEL_THROWNAXE(new int[] {802}, new AmmunitionData[] {AmmunitionData.STEEL_THROWNAXE}, RangedWeaponType.THROW),
		MITHRIL_THROWNAXE(new int[] {803}, new AmmunitionData[] {AmmunitionData.MITHRIL_THROWNAXE}, RangedWeaponType.THROW),
		ADAMANT_THROWNAXE(new int[] {804}, new AmmunitionData[] {AmmunitionData.ADAMANT_THROWNAXE}, RangedWeaponType.THROW),
		RUNE_THROWNAXE(new int[] {805}, new AmmunitionData[] {AmmunitionData.RUNE_THROWNAXE}, RangedWeaponType.THROW),
		MORRIGANS_THROWNAXE(new int[] {13883, 13957}, new AmmunitionData[] {AmmunitionData.MORRIGANS_THROWNAXE}, RangedWeaponType.THROW),
		
		TOKTZ_XIL_UL(new int[] {6522}, new AmmunitionData[] {AmmunitionData.TOKTZ_XIL_UL}, RangedWeaponType.THROW),
		
		BRONZE_JAVELIN(new int[] {825}, new AmmunitionData[] {AmmunitionData.BRONZE_JAVELIN}, RangedWeaponType.THROW),
		IRON_JAVELIN(new int[] {826}, new AmmunitionData[] {AmmunitionData.IRON_JAVELIN}, RangedWeaponType.THROW),
		STEEL_JAVELIN(new int[] {827}, new AmmunitionData[] {AmmunitionData.STEEL_JAVELIN}, RangedWeaponType.THROW),
		MITHRIL_JAVELIN(new int[] {828}, new AmmunitionData[] {AmmunitionData.MITHRIL_JAVELIN}, RangedWeaponType.THROW),
		ADAMANT_JAVELIN(new int[] {829}, new AmmunitionData[] {AmmunitionData.ADAMANT_JAVELIN}, RangedWeaponType.THROW),
		RUNE_JAVELIN(new int[] {830}, new AmmunitionData[] {AmmunitionData.RUNE_JAVELIN}, RangedWeaponType.THROW),
		MORRIGANS_JAVELIN(new int[] {13879, 13953}, new AmmunitionData[] {AmmunitionData.MORRIGANS_JAVELIN}, RangedWeaponType.THROW),
		SAGAIE(new int[] {21364}, new AmmunitionData[] {AmmunitionData.SAGAIE}, RangedWeaponType.THROW),
		BOLAS(new int[] {21365}, new AmmunitionData[] {AmmunitionData.BOLAS}, RangedWeaponType.THROW),

		CHINCHOMPA(new int[] {10033}, new AmmunitionData[] {AmmunitionData.CHINCHOMPA}, RangedWeaponType.THROW),
		RED_CHINCHOMPA(new int[] {10034}, new AmmunitionData[] {AmmunitionData.RED_CHINCHOMPA}, RangedWeaponType.THROW),

		HAND_CANNON(new int[] {15241}, new AmmunitionData[] {AmmunitionData.HAND_CANNON_SHOT}, RangedWeaponType.HAND_CANNON),

		KARILS_CROSSBOW(new int[]{4734}, new AmmunitionData[] {AmmunitionData.BOLT_RACK}, RangedWeaponType.CROSSBOW),

		TOXIC_BLOWPIPE(new int[]{12926}, new AmmunitionData[] {AmmunitionData.ZULRAH_SCALES}, RangedWeaponType.BLOWPIPE);

		RangedWeaponData(int[] weaponIds, AmmunitionData[] ammunitionData, RangedWeaponType type) {
			this.weaponIds = weaponIds;
			this.ammunitionData = ammunitionData;
			this.type = type;
		}

		private int[] weaponIds;
		private AmmunitionData[] ammunitionData;
		private RangedWeaponType type;

		public int[] getWeaponIds() {
			return weaponIds;
		}

		public AmmunitionData[] getAmmunitionData() {
			return ammunitionData;
		}

		public RangedWeaponType getType() {
			return type;
		}

		public static RangedWeaponData getData(Player p) {
			int weapon = p.getEquipment().get(Slot.WEAPON).getId();
			for(RangedWeaponData data : RangedWeaponData.values()) {
				for(int i : data.getWeaponIds()) {
					if(i == weapon)
						return data;
				}
			}
			return null;
		}

		public static AmmunitionData getAmmunitionData(Player player) {
			RangedWeaponData data = player.getRangedWeaponData();
			if(data != null) {
				int ammunition = player.getEquipment().get(data.getType() == RangedWeaponType.THROW ? Slot.WEAPON : Slot.ARROWS).getId();
				for(AmmunitionData ammoData : AmmunitionData.values()) {
					for(int i : ammoData.getItemIds()) {
						if(i == ammunition)
							return ammoData;
					}
				}
			}
			return AmmunitionData.BRONZE_ARROW;
		}
	}

	public enum AmmunitionData {
		//int[] itemIds, int startGfxId, int projectileId, int projectileSpeed, int projectileDelay, int strength, int startHeight, int endHeight
		NOVITE_ARROW(new int[] {16427, 15947}, 2466, 2467, 3, 44, 7, 43, 31),
		BATHUS_ARROW(new int[] {15948, 16432}, 2468, 2469, 3, 44, 7, 43, 31),
		MARMAROS_ARROW(new int[] {15949, 16437}, 2470, 2471, 3, 44, 7, 43, 31),
		KRATONITE_ARROW(new int[] {15950, 16442}, 2472, 2473, 3, 44, 7, 43, 31),
		FRACTITE_ARROW(new int[] {15951, 16447}, 2474, 2475, 3, 44, 7, 43, 31),
		ZEPHYRIUM_ARROW(new int[] {15952, 16452}, 2476, 2477, 3, 44, 7, 43, 31),
		ARGONITE_ARROW(new int[] {15953, 16457}, 2478, 2479, 3, 44, 7, 43, 31),
		KATAGON_ARROW(new int[] {15954, 16462}, 2480, 2481, 3, 44, 7, 43, 31),
		GORGONITE_ARROW(new int[] {15955, 16467}, 2482, 2483, 3, 44, 7, 43, 31),
		PROMETHIUM_ARROW(new int[] {15956, 16472}, 2484, 2485, 3, 44, 7, 43, 31),
		SAGITTARIAN_ARROW(new int[] {15957, 16477}, 2486, 2487, 3, 44, 7, 43, 31),

		TRAINING_ARROW(new int[] {9706}, 806, 805, 3, 44, 7, 43, 31),
		OGRE_ARROW(new int[] {2866}, 243, 242, 3, 44, 7, 43, 31),

		BRONZE_BRUTAL(new int[] {4773}, 403, 404, 3, 44, 7, 43, 31),
		IRON_BRUTAL(new int[] {4778}, 403, 404, 3, 44, 10, 43, 31),
		STEEL_BRUTAL(new int[] {4783}, 403, 404, 3, 44, 16, 43, 31),
		BLACK_BRUTAL(new int[] {4788}, 403, 404, 3, 44, 16, 43, 31),
		MITHRIL_BRUTAL(new int[] {4793}, 403, 404, 3, 44, 22, 43, 31),
		ADAMANT_BRUTAL(new int[] {4798}, 403, 404, 3, 44, 31, 43, 31),
		RUNE_BRUTAL(new int[] {4803}, 403, 404, 3, 44, 50, 43, 31),

		BRONZE_ARROW(new int[] {882}, 19, 10, 3, 44, 7, 43, 31),
		IRON_ARROW(new int[] {884}, 18, 9, 3, 44, 10, 43, 31),
		STEEL_ARROW(new int[] {886}, 20, 11, 3, 44, 16, 43, 31),
		MITHRIL_ARROW(new int[] {888}, 21, 12, 3, 44, 22, 43, 31),
		ADAMANT_ARROW(new int[] {890}, 22, 13, 3, 44, 31, 43, 31),
		RUNE_ARROW(new int[] {892}, 24, 15, 3, 44, 50, 43, 31),
		ICE_ARROW(new int[]{78}, 25, 16, 3, 44, 58, 34, 31),
		BROAD_ARROW(new int[] {4160}, 325, 326, 3, 44, 58, 43, 31),
		DRAGON_ARROW(new int[] {11212}, 1111, 1120, 3, 44, 65, 43, 31),

		ABYSSALBANE_ARROW(new int[] {21655}, 22, 13, 3, 44, 50, 43, 31),
		BASILISKBANE_ARROW(new int[] {21650}, 22, 13, 3, 44, 50, 43, 31),
		DRAGONBANE_ARROW(new int[] {21640}, 22, 13, 3, 44, 50, 43, 31),
		WALLASALKIBANE_ARROW(new int[] {21645}, 22, 13, 3, 44, 50, 43, 31),

		GUTHIX_ARROW(new int[] {19162}, 95, 98, 3, 44, 55, 43, 31),
		SARADOMIN_ARROW(new int[] {19152}, 96, 99, 3, 44, 55, 43, 31),
		ZAMORAK_ARROW(new int[] {19162}, 97, 100, 3, 44, 55, 43, 31),

		BONE_BOLT(new int[] {8882}, -1, 696, 3, 44, 13, 43, 31),
		BARBED_BOLT(new int[] {881}, -1, 696, 3, 44, 13, 43, 31),
		BRONZE_BOLT(new int[] {877}, -1, 27, 3, 44, 13, 43, 31),
		BLURITE_BOLT(new int[] {9139}, -1, 27, 3, 44, 40, 43, 31),
		SILVER_BOLT(new int[] {9145}, -1, 27, 3, 44, 40, 43, 31),
		IRON_BOLT(new int[] {9140}, -1, 27, 3, 44, 28, 43, 31),
		STEEL_BOLT(new int[] {9141}, -1, 27, 3, 44, 35, 43, 31),
		BLACK_BOLT(new int[] {13083}, -1, 27, 3, 44, 40, 43, 31),
		MITHRIL_BOLT(new int[] {9142}, -1, 27, 3, 44, 40, 43, 31),
		ADAMANT_BOLT(new int[] {9143}, -1, 27, 3, 44, 60, 43, 31),
		RUNITE_BOLT(new int[] {9144}, -1, 27, 3, 44, 84, 43, 31),
		BROAD_BOLT(new int[] {13280}, -1, 27, 3, 44, 88, 43, 31),

		OPAL_BOLT(new int [] {879, 9236}, -1, 27, 3, 44, 20, 43, 31),//Bronze
		JADE_BOLT(new int[] {9335, 9237}, -1, 27, 3, 44, 31, 43, 31),//Blurite
		PEARL_BOLT(new int[] {880, 9238}, -1, 27, 3, 44, 38, 43, 31),//Iron
		TOPAZ_BOLT(new int[] {9336, 9239}, -1, 27, 3, 44, 50, 43, 31),//Steel
		SAPPHIRE_BOLT(new int[] {9337, 9240}, -1, 27, 3, 44, 65, 43, 31),//Mithril
		EMERALD_BOLT(new int[] {9338, 9241}, -1, 27, 3, 44, 70, 43, 31),//Mithril
		RUBY_BOLT(new int[] {9339, 9242}, -1, 27, 3, 44, 75, 43, 31),//Adamant
		DIAMOND_BOLT(new int[] {9340, 9243}, -1, 27, 3, 44, 88, 43, 31),//Adamant
        DRAGONSTONE_BOLT(new int[] {9341, 9244}, -1, 27, 3, 44, 90, 43, 31),//Runite
        ONYX_BOLT(new int[] {9342, 9245}, -1, 27, 3, 44, 90, 43, 31),//Runite

        KEBBIT_BOLT(new int[] {10158}, -1, 27, 3, 44, 84, 43, 31),
        LONG_KEBBIT_BOLT(new int[] {10159}, -1, 27, 3, 44, 84, 43, 31),
		CORAL_BOLT(new int[] {24304}, -1, 3172, Revision.PRE_RS3, 3, 44, 90, 43, 31),
		ROYAL_BOLT(new int[] {24336}, -1, 3173, Revision.PRE_RS3, 3, 44, 90, 43, 31),
		BAKRIMINEL_BOLT(new int[] {24116}, -1, 3023, Revision.PRE_RS3, 3, 44, 90, 43, 31),
		ABYSSALBANE_BOLT(new int[] {21675}, -1, 27, 3, 44, 90, 43, 31),
		BASILISKBANE_BOLT(new int[] {21670}, -1, 27, 3, 44, 90, 43, 31),
		DRAGONBANE_BOLT(new int[] {21660}, -1, 27, 3, 44, 90, 43, 31),
		WALLASALKIBANE_BOLT(new int[] {21665}, -1, 27, 3, 44, 90, 43, 31),

		BRONZE_DART(new int[] {806}, 1234, 226, 4, 33, 2, 45, 37),
		IRON_DART(new int[] {807}, 1235, 227, 4, 33, 5, 45, 37),
		STEEL_DART(new int[] {808}, 1236, 228, 4, 33, 8, 45, 37),
		MITHRIL_DART(new int[] {809}, 1238, 229, 4, 33, 10, 45, 37),
		ADAMANT_DART(new int[] {810}, 1239, 230, 4, 33, 15, 45, 37),
		RUNE_DART(new int[] {811}, 1240, 231, 4, 33, 20, 45, 37),
		DRAGON_DART(new int[] {11230}, 1123, 226, 4, 33, 25, 49, 37),
		BLISTERWOOD_STAKE(new int[] {21580}, -1, -1, 4, 33, 25, 49, 37),
        DEATHTOUCHED_DART(new int[] {25202}, -1, -1, 4, 33, 25, 49, 37),

		BRONZE_KNIFE(new int[] {864, 870, 5654}, 219, 212, 4, 33, 8, 45, 37),
		IRON_KNIFE(new int[] {863, 871, 5655}, 220, 213, 4, 33, 12, 45, 37),
		STEEL_KNIFE(new int[] {865, 872, 5656}, 221, 214, 4, 33, 15, 45, 37),
		BLACK_KNIFE(new int[] {869, 874, 5658}, 222, 215, 4, 33, 17, 45, 37),
		MITHRIL_KNIFE(new int[] {866, 873, 5657}, 223, 215, 4, 33, 19, 45, 37),
		ADAMANT_KNIFE(new int[] {867, 875, 5659}, 224, 217, 4, 33, 24, 45, 37),
		RUNE_KNIFE(new int[] {868, 876, 5660, 5667}, 225, 218, 4, 33, 30, 48, 37),
		
		BRONZE_THROWNAXE(new int[] {800}, 43, 36, 3, 44, 7, 43, 31),
		IRON_THROWNAXE(new int[] {801}, 42, 35, 3, 44, 9, 43, 31),
		STEEL_THROWNAXE(new int[] {802}, 44, 37, 3, 44, 11, 43, 31),
		MITHRIL_THROWNAXE(new int[] {803}, 45, 38, 3, 44, 13, 43, 31),
		ADAMANT_THROWNAXE(new int[] {804}, 46, 39, 3, 44, 15, 43, 31),
		RUNE_THROWNAXE(new int[] {805}, 48, 41, 3, 44, 17, 43, 31),
		MORRIGANS_THROWNAXE(new int[] {13883, 13957}, 1856, 1839, 3, 44, 100, 43, 31),

		BRONZE_JAVELIN(new int[] {825}, 206, 200, 2, 40, 7, 45, 37),
		IRON_JAVELIN(new int[] {826}, 207, 201, 2, 40, 9, 45, 37),
		STEEL_JAVELIN(new int[] {827}, 208, 202, 2, 40, 11, 45, 37),
		MITHRIL_JAVELIN(new int[] {828}, 209, 203, 2, 40, 13, 45, 37),
		ADAMANT_JAVELIN(new int[] {829}, 210, 204, 2, 40, 15, 45, 37),
		RUNE_JAVELIN(new int[] {830}, 211, 205, 2, 40, 17, 45, 37),
		MORRIGANS_JAVELIN(new int[] {13879, 13953}, 1855, 1837, 2, 40, 100, 45, 37),
		SAGAIE(new int[] {21364}, -1, 466, 2, 40, 100, 45, 37),
		BOLAS(new int[] {21365}, -1, 468, 2, 40, 100, 45, 37),

		TOKTZ_XIL_UL(new int[]{6522}, -1, 442, 2, 40, 58, 51, 37),
		
		CHINCHOMPA(new int[] {10033}, -1, 908, 2, 8, 50, 40, 35),
		RED_CHINCHOMPA(new int[] {10034}, -1, 909, 2, 8, 80, 40, 37),

		HAND_CANNON_SHOT(new int[] {15243}, 2138, 2143, 3, 8, 115, 43, 31),
		BOLT_RACK(new int[] {4740}, -1, 27, 3, 33, 70, 43, 31),
		
		ZULRAH_SCALES(new int[] {12934}, -1, 27, 3, 33, 115, 43, 31);

		AmmunitionData(int[] itemIds, int startGfxId, int projectileId, int projectileSpeed, int projectileDelay, int strength, int startHeight, int endHeight) {
			this(itemIds, startGfxId, projectileId, Revision.RS2, projectileSpeed, projectileDelay, strength, startHeight, endHeight);
		}

		AmmunitionData(int[] itemIds, int startGfxId, int projectileId, Revision revision, int projectileSpeed, int projectileDelay, int strength, int startHeight, int endHeight) {
			this.itemIds = itemIds;
			this.startGfxId = startGfxId;
			this.projectileId = projectileId;
			this.revision = revision;
			this.projectileSpeed = projectileSpeed;
			this.projectileDelay = projectileDelay;
			this.strength = strength;
			this.startHeight = startHeight;
			this.endHeight = endHeight;		
		}

		private int[] itemIds;
		private int startGfxId;
		private int projectileId;
		private int projectileSpeed;
		private int projectileDelay;
		private int strength;
		private int startHeight;
		private int endHeight;
		private Revision revision;
		
		public int[] getItemIds() {
			return itemIds;
		}

		public boolean hasSpecialEffect() {
			return getItemIds().length >= 2;
		}

		public int getStartGfxId() {
			return startGfxId;
		}

		public int getProjectileId() {
			return projectileId;
		}

		public Revision getRevision() {
			return revision;
		}

		public int getProjectileSpeed() {
			return projectileSpeed;
		}

		public int getProjectileDelay() {
			return projectileDelay;
		}

		public int getStrength() {
			return strength;
		}
		
		public int getStartHeight() {
			return startHeight;
		}
		
		public int getEndHeight() {
			return endHeight;
		}
	}

	public enum RangedWeaponType {

		COMPOSITE_BOW(5, 5),
		LONGBOW(5, 5),
		SHORTBOW(5, 4),
		CROSSBOW(5, 5),
		THROW(4, 3),
		DARK_BOW(5, 5),
		HAND_CANNON(5, 4),
		BLOWPIPE(5, 3);

		RangedWeaponType(int distanceRequired, int attackDelay) {
			this.distanceRequired = distanceRequired;
			this.attackDelay = attackDelay;
		}

		private int distanceRequired;
		private int attackDelay;

		public int getDistanceRequired() {
			return distanceRequired;
		}

		public int getAttackDelay() {
			return attackDelay;
		}
	}
}
