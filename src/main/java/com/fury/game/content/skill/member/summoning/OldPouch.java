package com.fury.game.content.skill.member.summoning;

import com.fury.cache.Revision;
import com.fury.core.model.item.Item;

public enum OldPouch {
	SPIRIT_WOLF(new Item(12047), new Item(12158), 7, new Item(2859, 1), 5.0, 1),
	SPIRIT_DREADFOWL(new Item(12043), new Item(12158), 8, new Item(2138, 1), 9.0, 4),
	SPIRIT_SPIDER(new Item(12059), new Item(12158), 8, new Item(6291, 1), 10.0, 8),
	THORNY_SNAIL(new Item(12019), new Item(12158), 9, new Item(3363, 1), 12.0, 13),
	GRANITE_CRAB(new Item(12009), new Item(12158), 7, new Item(440, 1), 22.0, 16),
	SPIRIT_MOSQUITO(new Item(12778), new Item(12158), 1, new Item(6319, 1), 47.0, 17),
	DESERT_WYRM(new Item(12049), new Item(12159), 45, new Item(1783, 1), 31.0, 18),
	SPIRIT_SCORPION(new Item(12055), new Item(12160), 57, new Item(3095, 1), 83.0, 19),
	SPIRIT_TZ_KIH(new Item(12808), new Item(12160), 64, new Item(12168, 1), 97.0, 22),
	ALBINO_RAT(new Item(12067), new Item(12163), 75, new Item(2134, 1), 202.0, 23),
	SPIRIT_KALPHITE(new Item(12063), new Item(12163), 51, new Item(3138, 1), 220.0, 25),
	COMPOST_MOUND(new Item(12091), new Item(12159), 47, new Item(6032, 1), 50.0, 28),
	GIANT_CHINCHOMPA(new Item(12800), new Item(12163), 84, new Item(10033, 1), 255.0, 29),
	VAMPIRE_BAT(new Item(12053), new Item(12160), 81, new Item(3325, 1), 136.0, 31),
	HONEY_BADGER(new Item(12065), new Item(12160), 84, new Item(12156, 1), 141.0, 32),
	BEAVER(new Item(12021), new Item(12159), 72, new Item(1519, 1), 58.0, 33),
	VOID_RAVAGER(new Item(12818), new Item(12159), 74, new Item(12164, 1), 60.0, 34),
	VOID_SPINNER(new Item(12780), new Item(12163), 74, new Item(12166, 1), 60.0, 34),
	VOID_TORCHER(new Item(12798), new Item(12163), 74, new Item(12167, 1), 60.0, 34),
	VOID_SHIFTER(new Item(12814), new Item(12163), 74, new Item(12165, 1), 60.0, 34),
	BRONZE_MINOTAUR(new Item(12073), new Item(12163), 102, new Item(2349, 1), 317.0, 36),
	BULL_ANT(new Item(12087), new Item(12158), 11, new Item(6010, 1), 53.0, 40),
	MACAW(new Item(12071), new Item(12159), 78, new Item(249, 1), 72.0, 41),
	EVIL_TURNIP(new Item(12051), new Item(12160), 104, new Item(12153, 1), 185.0, 42),
	SPIRIT_COCKATRICE(new Item(12095), new Item(12159), 88, new Item(12109, 1), 75.2, 43),
	SPIRIT_GUTHATRICE(new Item(12097), new Item(12159), 88, new Item(12111, 1), 75.2, 43),
	SPIRIT_SARATRICE(new Item(12099), new Item(12159), 88, new Item(12113, 1), 75.2, 43),
	SPIRIT_ZAMATRICE(new Item(12101), new Item(12159), 88, new Item(12115, 1), 75.2, 43),
	SPIRIT_PENGATRICE(new Item(12103), new Item(12159), 88, new Item(12117, 1), 75.2, 43),
	SPIRIT_CORAXATRICE(new Item(12105), new Item(12159), 88, new Item(12119, 1), 75.2, 43),
	SPIRIT_VULATRICE(new Item(12107), new Item(12159), 88, new Item(12121, 1), 75.2, 43),
	IRON_MINOTAUR(new Item(12075), new Item(12163), 125, new Item(2351, 1), 405.0, 46),
	PYRELORD(new Item(12816), new Item(12160), 111, new Item(590, 1), 202.0, 46),
	MAGPIE(new Item(12041), new Item(12159), 88, new Item(1635, 1), 83.0, 47),
	BLOATED_LEECH(new Item(12061), new Item(12160), 117, new Item(2132, 1), 215.0, 49),
	SPIRIT_TERRORBIRD(new Item(12007), new Item(12158), 12, new Item(9978, 1), 68.0, 52),
	ABYSSAL_PARASITE(new Item(12035), new Item(12159), 106, new Item(12161, 1), 95.0, 54),
	SPIRIT_JELLY(new Item(12027), new Item(12163), 151, new Item(1937, 1), 484.0, 55),
	STEEL_MINOTAUR(new Item(12077), new Item(12163), 141, new Item(2353, 1), 493.0, 56),
	IBIS(new Item(12531), new Item(12159), 109, new Item(311, 1), 99.0, 56),
	SPIRIT_GRAAHK(new Item(12810), new Item(12163), 154, new Item(10099, 1), 502.0, 57),
	SPIRIT_KYATT(new Item(12812), new Item(12163), 153, new Item(10103, 1), 502.0, 57),
	SPIRIT_LARUPIA(new Item(12784), new Item(12163), 155, new Item(10095, 1), 502.0, 57),
	KHARAMTHULHU_OVERLORD(new Item(12023), new Item(12163), 144, new Item(6667, 1), 510.0, 58),
	SMOKE_DEVIL(new Item(12085), new Item(12160), 141, new Item(9736, 1), 268.0, 61),
	ABYSSAL_LURKER(new Item(12037), new Item(12159), 119, new Item(12161, 1), 110.0, 62),
	SPIRIT_COBRA(new Item(12015), new Item(12160), 116, new Item(6287, 1), 269.0, 63),
	STRANGER_PLANT(new Item(12045), new Item(12160), 128, new Item(8431, 1), 282.0, 64),
	MITHRIL_MINOTAUR(new Item(12079), new Item(12163), 152, new Item(2359, 1), 581.0, 66),
	BARKER_TOAD(new Item(12123), new Item(12158), 11, new Item(2150, 1), 87.0, 66),
	WAR_TORTOISE(new Item(12031), new Item(12158), 1, new Item(7939, 1), 59.0, 67),
	BUNYIP(new Item(12029), new Item(12159), 110, new Item(383, 1), 120.0, 68),
	FRUIT_BAT(new Item(12033), new Item(12159), 130, new Item(1963, 1), 121.0, 69),
	RAVENOUS_LOCUST(new Item(12820), new Item(12160), 79, new Item(1933, 1), 132.0, 70),
	ARCTIC_BEAR(new Item(12057), new Item(12158), 14, new Item(10117, 1), 93.0, 71),
	PHEONIX(new Item(14623), new Item(12160), 165, new Item(14616, 1), 301.0, 72),
	OBSIDIAN_GOLEM(new Item(12792), new Item(12163), 195, new Item(12168, 1), 642.0, 73),
	GRANITE_LOBSTER(new Item(12069), new Item(12160), 166, new Item(6979, 1), 326.0, 74),
	PRAYING_MANTIS(new Item(12011), new Item(12160), 168, new Item(2460, 1), 330.0, 75),
	ADAMANT_MINOTAUR(new Item(12081), new Item(12163), 144, new Item(2361, 1), 669.0, 76),
	FORGE_REGENT(new Item(12782), new Item(12159), 141, new Item(10020, 1), 134.0, 76),
	TALON_BEAST(new Item(12794), new Item(12160), 174, new Item(12162, 1), 135.0, 77),
	GIANT_ENT(new Item(12013), new Item(12159), 124, new Item(5933, 1), 137.0, 78),
	FIRE_TITAN(new Item(12802), new Item(12163), 198, new Item(1442, 1), 695.0, 79),
	MOSS_TITAN(new Item(12804), new Item(12163), 202, new Item(1440, 1), 695.0, 79),
	ICE_TITAN(new Item(12806), new Item(12163), 198, new Item(1444, 1), 695.0, 79),
	HYDRA(new Item(12025), new Item(12159), 128, new Item(571, 1), 141.0, 80),
	SPIRIT_DAGGANOTH(new Item(12017), new Item(12160), 1, new Item(6155, 1), 365.0, 83),
	LAVA_TITAN(new Item(12788), new Item(12163), 219, new Item(12168, 1), 730.0, 83),
	SWAMP_TITAN(new Item(12776), new Item(12160), 150, new Item(10149, 1), 374.0, 85),
	RUNE_MINOTAUR(new Item(12083), new Item(12163), 1, new Item(2363, 1), 757.0, 86),
	UNICORN_STALLION(new Item(12039), new Item(12159), 140, new Item(237, 1), 154.0, 88),
	GEYSER_TITAN(new Item(12786), new Item(12163), 222, new Item(1444, 1), 783.0, 89),
	WOLPERTINGER(new Item(12089), new Item(12160), 203, new Item(3226, 1), 405.0, 92),
	ABYSSAL_TITAN(new Item(12796), new Item(12159), 113, new Item(12161, 1), 163.0, 93),
	IRON_TITAN(new Item(12822), new Item(12160), 198, new Item(1115, 1), 418.0, 95),
	PACK_YAK(new Item(12093), new Item(12160), 211, new Item(10818, 1), 422.0, 96),
	STEEL_TITAN(new Item(12790), new Item(12160), 178, new Item(1119, 1), 435.0, 99),

	CUB_BLOODRAGER(new Item(17935), new Item(18017), -1, new Item(17630, 1), 5.0, 1),
	CUB_DEATHSLINGER(new Item(17985), new Item(18017), -1, new Item(17682, 2), 5.7, 2),
	CUB_STORMBRINGER(new Item(17945), new Item(18017), -1, new Item(17448, 1), 6.4, 3),
	CUB_HOARDSTALKER(new Item(17955), new Item(18017), -1, new Item(17424, 1), 7.1, 5),
	CUB_WORLDBEARER(new Item(17975), new Item(18017), -1, new Item(17995, 1), 7.8, 7),
	CUB_SKINWEAVER(new Item(17965), new Item(18017), -1, new Item(18159, 2), 8.5, 9),
	LITTLE_BLOODRAGER(new Item(17936), new Item(18017), -1, new Item(17632, 1), 19.5, 11),
	LITTLE_DEATHSLINGER(new Item(17986), new Item(18017), -1, new Item(17684, 2), 20.5, 12),
	LITTLE_STORMBRINGER(new Item(17946), new Item(18017), -1, new Item(17450, 1), 21.5, 13),
	LITTLE_HOARDSTALKER(new Item(17956), new Item(18017), -1, new Item(17426, 1), 22.5, 15),
	LITTLE_WORLDBEARER(new Item(17976), new Item(18017), -1, new Item(17997, 1), 23.5, 17),
	LITTLE_SKINWEAVER(new Item(17966), new Item(18017), -1, new Item(18161, 2), 24.5, 19),
	NAIVE_BLOODRAGER(new Item(17937), new Item(18017), -1, new Item(17634, 1), 43.0, 21),
	NAIVE_DEATHSLINGER(new Item(17987), new Item(18017), -1, new Item(17686, 2), 44.4, 22),
	NAIVE_STORMBRINGER(new Item(17947), new Item(18017), -1, new Item(17452, 1), 45.8, 23),
	NAIVE_HOARDSTALKER(new Item(17957), new Item(18017), -1, new Item(17428, 1), 47.2, 25),
	NAIVE_WORLDBEARER(new Item(17977), new Item(18017), -1, new Item(17999, 1), 48.6, 27),
	NAIVE_SKINWEAVER(new Item(17967), new Item(18017), -1, new Item(18163, 2), 50.0, 29),
	KEEN_BLOODRAGER(new Item(17938), new Item(18018), -1, new Item(17636, 1), 68.5, 31),
	KEEN_DEATHSLINGER(new Item(17988), new Item(18018), -1, new Item(17688, 2), 70.4, 32),
	KEEN_STORMBRINGER(new Item(17948), new Item(18018), -1, new Item(17454, 1), 72.3, 33),
	KEEN_HOARDSTALKER(new Item(17958), new Item(18018), -1, new Item(17430, 1), 74.2, 35),
	KEEN_WORLDBEARER(new Item(17978), new Item(18018), -1, new Item(18001, 1), 76.1, 37),
	KEEN_SKINWEAVER(new Item(17968), new Item(18018), -1, new Item(18165, 2), 78.0, 39),
	BRAVE_BLOODRAGER(new Item(17939), new Item(18018), -1, new Item(17638, 1), 99.5, 41),
	BRAVE_DEATHSLINGER(new Item(17989), new Item(18018), -1, new Item(17690, 2), 102.0, 42),
	BRAVE_STORMBRINGER(new Item(17949), new Item(18018), -1, new Item(17456, 1), 104.5, 43),
	BRAVE_HOARDSTALKER(new Item(17959), new Item(18018), -1, new Item(17432, 1), 107.0, 45),
	BRAVE_WORLDBEARER(new Item(17979), new Item(18018), -1, new Item(18003, 1), 109.5, 47),
	BRAVE_SKINWEAVER(new Item(17969), new Item(18018), -1, new Item(18167, 2), 112.0, 49),
	BRAH_BLOODRAGER(new Item(17940), new Item(18018), -1, new Item(17640, 1), 157.0, 51),
	BRAH_DEATHSLINGER(new Item(17990), new Item(18018), -1, new Item(17692, 2), 160.5, 52),
	BRAH_STORMBRINGER(new Item(17950), new Item(18018), -1, new Item(17458, 1), 164.0, 53),
	BRAH_HOARDSTALKER(new Item(17960), new Item(18018), -1, new Item(17434, 1), 167.5, 55),
	BRAH_WORLDBEARER(new Item(17980), new Item(18018), -1, new Item(18005, 1), 171.0, 57),
	BRAH_SKINWEAVER(new Item(17970), new Item(18018), -1, new Item(18169, 2), 174.5, 59),
	NAABE_BLOODRAGER(new Item(17941), new Item(18019), -1, new Item(17642, 1), 220.0, 61),
	NAABE_DEATHSLINGER(new Item(17991), new Item(18019), -1, new Item(17694, 2), 224.6, 62),
	NAABE_STORMBRINGER(new Item(17951), new Item(18019), -1, new Item(17460, 1), 229.2, 63),
	NAABE_HOARDSTALKER(new Item(17961), new Item(18019), -1, new Item(17436, 1), 233.8, 65),
	NAABE_WORLDBEARER(new Item(17981), new Item(18019), -1, new Item(18007, 1), 238.4, 67),
	NAABE_SKINWEAVER(new Item(17971), new Item(18019), -1, new Item(18171, 2), 243.0, 69),
	WISE_BLOODRAGER(new Item(17942), new Item(18019), -1, new Item(17644, 1), 325.0, 71),
	WISE_DEATHSLINGER(new Item(17992), new Item(18019), -1, new Item(17696, 2), 330.8, 72),
	WISE_STORMBRINGER(new Item(17952), new Item(18019), -1, new Item(17462, 1), 336.6, 73),
	WISE_HOARDSTALKER(new Item(17962), new Item(18019), -1, new Item(17438, 1), 342.4, 75),
	WISE_WORLDBEARER(new Item(17982), new Item(18019), -1, new Item(18009, 1), 348.2, 77),
	WISE_SKINWEAVER(new Item(17972), new Item(18019), -1, new Item(18173, 2), 354.0, 79),
	ADEPT_BLOODRAGER(new Item(17943), new Item(18020), -1, new Item(17646, 1), 517.5, 81),
	ADEPT_DEATHSLINGER(new Item(17993), new Item(18020), -1, new Item(17698, 2), 524.6, 82),
	ADEPT_STORMBRINGER(new Item(17953), new Item(18020), -1, new Item(17464, 1), 531.7, 83),
	ADEPT_HOARDSTALKER(new Item(17963), new Item(18020), -1, new Item(17440, 1), 538.8, 85),
	ADEPT_WORLDBEARER(new Item(17983), new Item(18020), -1, new Item(18011, 1), 545.9, 87),
	ADEPT_SKINWEAVER(new Item(17973), new Item(18020), -1, new Item(18175, 2), 553.0, 89),
	SACHEM_BLOODRAGER(new Item(17944), new Item(18020), -1, new Item(17648, 1), 810.0, 91),
	SACHEM_DEATHSLINGER(new Item(17994), new Item(18020), -1, new Item(17700, 2), 818.5, 92),
	SACHEM_STORMBRINGER(new Item(17954), new Item(18020), -1, new Item(17466, 1), 827.0, 93),
	SACHEM_HOARDSTALKER(new Item(17964), new Item(18020), -1, new Item(17442, 1), 835.5, 95),
	SACHEM_WORLDBEARER(new Item(17984), new Item(18020), -1, new Item(18013, 1), 844.0, 97),
	SACHEM_SKINWEAVER(new Item(17974), new Item(18020), -1, new Item(18177, 2), 852.5, 99),

	CLAY_BEAST1(new Item(14422), new Item(-1), -1, new Item(14182, 1), 0.0, 3),
	CLAY_BEAST2(new Item(14424), new Item(-1), -1, new Item(14184, 1), 0.0, 20),
	CLAY_BEAST3(new Item(14426), new Item(-1), -1, new Item(14186, 1), 0.0, 40),
	CLAY_BEAST4(new Item(14428), new Item(-1), -1, new Item(14188, 1), 0.0, 60),
	CLAY_BEAST5(new Item(14430), new Item(-1), -1, new Item(14190, 1), 0.0, 80);

	/**
	 * Variables
	 */

	private Item pouch;
	private Item charm;
	private int shardsRequired;
	private Item secondIngredient;
	private int levelRequired;
	private double experience;

	/**
	 * Sets the enumeration data format
	 *
	 * @param pouch returns the value of the pouch
	 * @param charmId returns the value of the charmId
	 * @param shardsRequired returns the value of the shardsRequired
	 * @param levelRequired returns the value of the levelRequired
	 * @param experience returns the value of the experience given
	 */
	private OldPouch(final Item pouch, final Item charm, final int shardsRequired, Item secondIngredient, final double experience, final int levelRequired)
	{
		this.pouch = pouch;
		this.charm = charm;
		this.shardsRequired = shardsRequired;
		this.secondIngredient = secondIngredient;
		this.levelRequired = levelRequired;
		this.experience = experience;
	}

	/**
	 * Loops through all the pouch enumeration
	 * @param buttonId parses the value of the buttonId
	 * @return the pouchId
	 */
	public final static OldPouch get(final int buttonId)
	{
		for (OldPouch p: OldPouch.values())
		{
			if (p.getButtonId() == buttonId) 
			{
				return p;
			}
		}
		return null;
	}

	public final static OldPouch forId(int pouchId) {
		for (OldPouch p: OldPouch.values()) {
			if (p.getPouchId() == pouchId) {
				return p;
			}
		}
		return null;
	}
	/**
	 * Accessor for the action button id pouch creation interface
	 * @return buttonId
	 */

	public int getButtonId()
	{
		return -1;
	}

	/**
	 * Accessor for the Pouch Id it gives the player
	 * @return pouchId
	 */

	public int getPouchId() {
		return this.pouch.getId();
	}

	public Item getPouch() {
		return this.pouch;
	}

	/**
	 * Accessor for the charm id required
	 * @return charmId
	 */

	public int getCharmId() {
		return this.charm.getId();
	}

	public Item getCharm() {
		return this.charm;
	}

	/**
	 * Accessor for the shards required to create the pouch
	 * @return shardsRequired
	 */

	public int getShardsRequired() 
	{
		return this.shardsRequired;
	}

	/**
	 * Accessor for the second Ingredient required to create the pouch
	 * @return sacrificeId
	 */
	public Item getSecondIngredient()
	{
		return this.secondIngredient;
	}

	/**
	 * Accessor for the level required to create the pouch
	 * @return levelRequired
	 */

	public int getLevelRequired()
	{
		return this.levelRequired;
	}

	/**
	 * Experience it gives the player for creating the pouch
	 * @return experience
	 */

	public double getExp()
	{
		return this.experience;
	}


	public static Item[] getPouches(SummoningType type) {
		Item[] pouches = new Item[type.getEnd() - type.getStart()];
		for(int i = type.getStart(); i < type.getEnd(); i++) {
			pouches[i - type.getStart()] = new Item(values()[i].getPouchId(), 1);
		}
		return pouches;
	}

	public static int[] getRequiredLevels(SummoningType type) {
		int[] levels = new int[type.getEnd() - type.getStart()];
		for(int i = type.getStart(); i < type.getEnd(); i++) {
			levels[i - type.getStart()] = values()[i].getLevelRequired();
		}
		return levels;
	}

	public static int[] getShardRequired(SummoningType type) {
		int[] shards = new int[type.getEnd() - type.getStart()];
		for(int i = type.getStart(); i < type.getEnd(); i++) {
			shards[i - type.getStart()] = values()[i].getShardsRequired();
		}
		return shards;
	}

	public static int[][] getRequiredItems(SummoningType type) {
		int[][] items = new int[type.getEnd() - type.getStart()][];
		for(int i = type.getStart(); i < type.getEnd(); i++) {
			if(type == SummoningType.DUNGEONEERING)
				items[i - type.getStart()] = new int[] { 18015, values()[i].getCharmId(), values()[i].getSecondIngredient().getId() };
			else
				items[i - type.getStart()] = new int[] { 12155, 12183, values()[i].getCharmId(), values()[i].getSecondIngredient().getId() };
		}
		return items;
	}

	public static int[][] getRequiredAmounts(SummoningType type) {
		int[][] amounts = new int[type.getEnd() - type.getStart()][];
		for(int i = type.getStart(); i < type.getEnd(); i++) {
			if(type == SummoningType.DUNGEONEERING)
				amounts[i - type.getStart()] = new int[] { 1, 1, values()[i].getSecondIngredient().getAmount() };
			else
				amounts[i - type.getStart()] = new int[] { 1, values()[i].getShardsRequired(), 1, values()[i].getSecondIngredient().getAmount() };
		}
		return amounts;
	}

	public static Revision[][] getRequiredRevisions(SummoningType type) {
		Revision[][] revisions = new Revision[type.getEnd() - type.getStart()][];
		for(int i = type.getStart(); i < type.getEnd(); i++) {
			if(type == SummoningType.DUNGEONEERING)
				revisions[i - type.getStart()] = new Revision[] { Revision.RS2, values()[i].getCharm().getRevision(), values()[i].getSecondIngredient().getRevision() };
			else
				revisions[i - type.getStart()] = new Revision[] { Revision.RS2, Revision.RS2, values()[i].getCharm().getRevision(), values()[i].getSecondIngredient().getRevision() };
		}
		return revisions;
	}
}
