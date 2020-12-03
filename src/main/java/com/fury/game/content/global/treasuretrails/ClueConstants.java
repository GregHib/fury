package com.fury.game.content.global.treasuretrails;

import com.fury.game.container.impl.equip.Slot;
import com.fury.game.entity.character.player.content.emotes.EmoteData;
import com.fury.game.world.map.Position;
import com.fury.util.Misc;

import java.util.ArrayList;
import java.util.List;

public class ClueConstants {

	public static final int[] easyNpcs = new int[] { 18, 7496, 7495, 7494, 7493, 7492, 1612, 5909, 3263, 3262, 3261, 3260, 3259, 3258, 3257, 3256, 3255, 3253, 3252, 3251, 3250, 3249, 3248, 3247, 3246, 5750, 1832, 10696, 1831, 6113, 2693, 46, 7, 1757, 1758, 1760, 2685, 2686, 2687, 2688, 3264, 3265, 3266, 3267, 4261, 4262, 4263, 4264, 4265, 4266, 4267, 4268, 1712, 1711, 1710, 3406, 26, 23, 7162, 7161, 2697, 175, 1, 2, 3, 4, 5, 6, 16, 24, 25, 1267, 1265, 8, 282, 283, 2674, 2696, 5926, 2927, 5926, 5927, 5928, 5929, 186, 7106, 7107, 7108, 7109, 7110, 7111, 7112, 7113, 7114, 2044, 2045, 2046, 2047, 2048, 2049, 2051, 2052, 2053, 2054, 2055},
	mediumNpcs = new int[] {2264, 2263, 2265, 1616, 1617, 2130, 2131, 2132, 2133, 2134, 2135, 2136, 2232, 3276, 3277, 3278, 3279, 8772, 8773, 8774, 8784, 8783, 8782, 8781, 8776, 8777, 8799, 8800, 1620, 4227, 1338, 1339, 1340, 1341, 1342, 1343, 1344, 1345, 1346, 1347, 8323, 8322, 8321, 8320, 8319, 8318, 8317, 8316, 8437, 8438, 8439, 8440, 3943, 6380, 6379, 2452, 2885, 1973, 5359, 5384, 6383, 6381, 163, 164, 9, 32, 296, 297, 298, 299, 111, 3072, 4685, 4686, 4687, 125, 145, 3073, 113, 3449, 3450, 6268, 4348, 4349, 4350, 4351, 4352, 1219, 82, 4694, 4695, 4696, 4697, 6101, 1317, 2236, 2571, 9122, 20, 365, 2256, 1633, 1634, 1635, 1636, 6216, 8598, 8616, 2889, 90, 91, 92, 93, 94, 1471, 2036, 2037, 2715, 3151, 3291, 3581, 4384, 4385, 4386, 5332, 5333, 5334, 5335, 5336, 5337, 5338, 5339, 5340, 5341, 5359, 5365, 5366, 5367, 5368, 5381, 5384, 5385, 5386, 5387, 5388, 5389, 5390, 5391, 5392, 5411, 5412, 5422, 6091, 6092, 6093, 6103, 6104, 6105, 6106, 6107, 6764, 6765, 6766, 6767, 6768,
			7774, 7775, 7776, 7777, 7778, 7788, 7921, 9307, 9308, 9309, 7823, 2457, 2884, 9096, 650, 1665, 6006, 6007, 6008, 6009, 6010, 6011, 6012, 6013, 6014, 6015, 6016, 6017, 6108, 6019, 6020, 6021, 6022, 6023, 6024, 6025, 6212, 6213, 7379, 7380, 8515, 8516},
	hardNpcs = new int[] {1604, 1605, 1606, 1607, 7801, 7802, 7803, 7804, 1615, 4230, 9086, 4381, 4382, 4383, 9172, 6232, 6233, 6234, 6235, 6236, 6237, 6238, 6239, 6240, 6241, 6242, 6243, 6244, 6245, 6246, 54, 4673, 4674, 4675, 4676, 55, 4681, 4682, 5683, 4684, 5178, 7133, 7134, 6252, 1590, 5362, 4353, 4354, 4355, 4356, 4357, 3200, 6247, 8133, 116, 4291, 4292, 6078, 6079, 6080, 6081, 6269, 6270, 3478, 2882, 2883, 2881, 9465, 1183, 1184, 1201, 2359, 2360, 2361, 2362, 8324, 8325, 8326, 8327, 110, 1582, 1583, 1584, 1585, 1586, 7003, 7004, 51, 2862, 9087, 6389, 1827, 1610, 6260, 3340, 14301, 4418, 6218, 10207, 10208, 10209, 10210, 10211, 83, 4698, 4699, 4700, 4701, 6677, 941, 4677, 4678, 4679, 4680, 49, 3586, 6210, 6910, 9463, 3406, 7713, 7714, 7715, 9066, 9067, 9068, 9069, 9072, 1643, 1644, 1645, 1646, 1647, 1591, 1637, 1638, 1639, 1640, 1641, 1642, 9467, 6203, 1158, 1159, 1160, 3835, 3836, 4234, 50, 2642, 23, 26, 6258, 6259, 6222, 1608, 1609, 4229, 7797, 7805, 5363, 8424, 1613,
	13447, 13448, 13449, 13450, 38, 39, 7135, 53, 13481, 13476, 6253, 2892, 2894, 2896, 1592, 3590, 6221, 6231, 6257, 6278, 6220, 6230, 6256, 6276, 6219, 6229, 6255, 6277, 6248, 1592, 3590, 5415, 5416, 5417, 5418, 8349, 8350, 8351, 8352, 8353, 8354, 8355, 8356, 8357, 8358, 8359, 8360, 8361, 8362, 8363, 8364, 8365, 8366, 1623, 1626, 1627, 1628, 1629, 1630, 5361, 9054, 9056, 9057, 9058, 9059, 9077, 6223},
	eliteNpcs = new int [] {5477, 5133, 713, 6252, 1590, 3200, 6247, 8133, 2882, 2883, 2881, 9465, 6225, 6260, 3340, 14301, 6250, 9463, 1591, 9467, 6203, 1158, 1159, 1160, 3835, 3836, 50, 2642, 6222, 5363, 8424, 13447, 13448, 13449, 6265, 6263, 6261, 6248, 1592, 3590, 8349, 8350, 8351, 8352, 8353, 8354, 8355, 8356, 8357, 8358, 8359, 8360, 8361, 8362, 8363, 8364, 8365, 8366, 6223 };


	public static final int[] globalDrops = new int[] {995, 19467, 4561};

	public static final int[] MIN_NUMB_CLUES =
			{ 1, 1, 1, 1 }, MAX_NUMB_CLUES =
			{ 5, 15, 8, 11 };

	public static final int DOUBLE_AGENT_HIGH = 5145, DOUBLE_AGENT_LOW = 5144, URI = 5143;
	public static final int CLUE_REWARD_INTERFACE = 6960, CLUE_TEXT_INTERFACE = 6965;

	public static final String[] uriWisdom = new String[] {
			"Once I was a poor man, but then I found a party hat.",
			"There were three goblins in a bar, which one left first?",
			"Would you like to buy a pewter spoon?",
			"In the end, only the three-legged survive.",
			"I heard that the tall man fears only strong winds.",
			"In Canifis the men are known for eating much spam.",
			"I am the egg man, are you one of the egg men?",
			"The sudden appearance of a deaf squirrel is most puzzling, Comrade.",
			"I believe that it is very rainy in Varrock.",
			"The slowest of fishermen catch the swiftest of fish.",
			"It is quite easy being green.",
			"Don't forget to find the jade monkey."
	};

	public enum Maps {
		CRAFTING_GUILD(4305, new Position(2906, 3293)),
		VARROCK_MINING(7045, new Position(3289, 3374)),
		DRAYNOR_VILLAGE(7113, new Position(3091, 3227)),
		RANGING_GUILD(7162, new Position(2702, 3429)),
		FALADOR(7271, new Position(3043, 3398)),
		YANILLE(9043, new Position(2616, 3077)),
		COAL_TRUCKS(9108, new Position(2612, 3482)),
		//SEERS_VILLAGE(9196, new Position(2658, 3488), 24202),//Broken interface
		WIZARDS_TOWER(9275, new Position(3110, 3152)),
		WEST_ARDOUGNE(9359, new Position(2488, 3308)),
		//UNKNOWN(9454, null),
		FORTRESS(9507, new Position(3026, 3628), 46331),
		TOWER_OF_LIFE(9632, new Position(2651, 3232)),
		CLOCK_TOWER(9720, new Position(2565, 3248), 46331),
		RIMMINGTON(9839, new Position(2924, 3209)),
		FALADOR_CROSSROADS(17537, new Position(2970, 3414)),
		WILDERNESS(17620, new Position(3021, 3912)),
		EAST_ARDY(17634, new Position(2722, 3338)),
		//MISCELLANIA(17687, new Position(2536, 3865)),
		MORTTON(17774, new Position(3433, 3267)),
		KHAZARD(17888, new Position(2454, 3230)),
		RELLEKKA_LIGHTHOUSE(17907, new Position(2579, 3597)),
		RELLEKKA_PATH(18055, new Position(2665, 3562))
		;
		Maps(int interfaceId, Position location) {
			this.interfaceId = interfaceId;
			this.location = location;
		}

		Maps(int interfaceId, Position location, int objectId) {
			this.interfaceId = interfaceId;
			this.location = location;
			this.objectId = objectId;
		}

		private int interfaceId, objectId;
		private Position location;

		public int getObjectId() {
			return objectId;
		}

		public int getInterfaceId() {
			return interfaceId;
		}

		public Position getLocation() {
			return location;
		}

		public ClueTypes getType() {
			return ClueTypes.MAP;
		}

		public static int getIndex(String name) {
			for(Maps map : values())
				if(name.equals(map.name()))
					return map.ordinal();
			return 0;
		}
	}

	public enum SimpleClues {
		GRAND_TREE(new String[] { "Dig near some giant mushrooms,", "behind the Grand Tree." }, new Position(2460, 3505)),
//		FALADOR(new String[] { "Look in the ground floor", "crates of house in Falador." }, new Position(3029, 3355), 28625),
		PORT_SARIM(new String[] { "Search chests found in the", "upstairs of shops in Port Sarim." }, new Position(3016, 3205, 1), 40093),
		HEMENSTER(new String[] { "Search for a crate in a", "building in Hemenster." }, new Position(2636, 3453), 48998),
		VARROCK_PALACE(new String[] { "Search for a crate", "in Varrock Palace." }, new Position(3224, 3492), 46266),
		SEERS_VILLAGE(new String[] { "Search for a crate on", "the ground floor of a", "house in Seers' Village." }, new Position(2699, 3470), 25775),
		TAVERLEY(new String[] { "Search the boxes in", "a shop in Taverley" }, new Position(2886, 3449), 28625),
		AL_KHARID(new String[] { "Search the boxes in one", "of the tents in Al Kharid." }, new Position(3308, 3206), 46239),
		LUMBRIDGE_GOLBINS(new String[] { "Search the boxes in the goblin", "house near Lumbridge." }, new Position(3247, 3244), 46237),
		SOUTH_VARROCK(new String[] { "Search the boxes in", "the house near the", "south entrance of Varrock." }, new Position(3203, 3384), 46236),
		LUMBRIDGE_DUKE(new String[] { "Search the chest in the", "Duke of Lumbridge's bedroom." }, new Position(3209, 3218, 1), 37009),
		CAMELOT_CASTLE(new String[] { "Search the chest in the left-", "hand tower of Camelot Castle." }, new Position(2748, 3495, 2), 25592),
		AL_KHARID_PALACE(new String[] { "Search the chests in", "Al Kharid palace." }, new Position(3301, 3167), 35470),
		DWARVEN_MINE(new String[] { "Search the chests in", "the Dwarven Mine." }, new Position(3000, 9798), 30928),
		LUMBRIDGE_TOWER(new String[] { "Search the crate in the left-", "hand tower of Lumbridge castle." }, new Position(3228, 3212, 1), 21806),
		PORT_KHAZARD(new String[] { "Search the crate near", "a cart in Port Khazard." }, new Position(2660, 3149), 46270),
		YANILLE_PIANO(new String[] { "Search the crates in", "a house in Yanille that has a piano." }, new Position(2598, 3105), 24202),
		BARBARIAN_VILLAGE(new String[] { "Search the creates in", "Barbarian Village helmet shop." }, new Position(3073, 3430), 24202),
		CANIFIS(new String[] { "Search the crates", "in Canifis." }, new Position(3509, 3497), 24911),//Fix object walls
		DRAYNOR_MANOR(new String[] { "Search the crates", "in Draynor Manor." }, new Position(3106, 3369, 2), 47560),//Fix roof
		EAST_ARDOUGNE(new String[] { "Search the crates in East", "Ardougne's general store." }, new Position(2615, 3291), 34585),
		VARROCK_ARMOURY(new String[] { "Search the crates", "in Horvik's armoury." }, new Position(3228, 3433), 46269),
		ARDOGUNE_EAST_GATE(new String[] { "Search the crates in the", "guardhouse of the northern", "gate of East Ardogune." }, new Position(2645, 3338), 34585),
		AL_KHARID_NORTH_WESTERN(new String[] { "Search the crates in the", "northernmost house in Al Kharid." }, new Position(3289, 3202), 46238),
		PORT_SARIM_FISHING(new String[] { "Search the crates in the", "Port Sarim fishing shop." }, new Position(3012, 3222), 40021),
		ARDOUGNE_SHED(new String[] { "Search the crates in the shed", "just north of East Ardougne." }, new Position(2617, 3347), 34586),
		VARROCK_CART(new String[] { "Search the crates near", "a cart in Varrock." }, new Position(3226, 3452), 46269),
		EAST_ARDOUGNE_ARMOUR(new String[] { "Search the crates just", "outside the armour shop", "in East Ardougne." }, new Position(2654, 3299), 34586),
		FALADOR_GENERAL_STORE(new String[] { "Search the crates of", "Falador's general store." }, new Position(2955, 3390), 11744),//Fix falador rooves
		VARROCK_CLOTHES(new String[] { "Search the drawers", "above Varrock's shops." }, new Position(3206, 3419, 1), 24294),
		ARDOUGNE_PUB(new String[] { "Search the drawers found upstairs", "in the houses of East Ardougne." }, new Position(2575, 3326, 1), 34482),
		DRAYNOR_VILLAGE_HOUSE(new String[] { "Search the drawers in a", "house in Draynor Village." }, new Position(3097, 3277), 2631),
		CATHERBY_ARCHERY(new String[] { "Search the drawers", "in Catherby's archery shop." }, new Position(2825, 3442), 33931),
		FALADOR_CHAINMAIL(new String[] { "Search the drawers in", "Falador's chainmail shop." }, new Position(2969, 3311), 348),//Add falador bankers - fix falador wall
		TAVERLEY_HOUSE(new String[] { "Search the drawers in Taverley." }, new Position(2884, 3417), 28632),
		GERTRUDES_HOUSE(new String[] { "Search the drawers in", "one of Gertrude's bedrooms." }, new Position(3156, 3406), 24294),
		YANILLE_HUNTER(new String[] { "Search the drawers in", "the ground floor of a", "shop in Yanille." }, new Position(2570, 3085), 350),
		CATHERBY(new String[] { "Search the drawers", "in the upstairs of a", "house in Catherby." }, new Position(2809, 3452), 33931),
		BURTHORPE(new String[] { "Search the drawers of", "houses in Burthorpe." }, new Position(2929, 3570), 33461),
		ARDOUGNE_MARKET(new String[] { "Search the drawers on", "the first floor of a", "building overlooking", "Ardougne Market." }, new Position(2655, 3323, 1), 34482),
		FALADOR_SHIELD(new String[] { "Search the drawers upstairs", "in Falador's shield shop." }, new Position(2971, 3386, 1), 348),
		VARROCK_EAST_BANK(new String[] { "Search the drawers upstairs", "in the bank to", "the East of Varrock." }, new Position(3250, 3420, 1), 24294),
//		FALADOR_EASTERN(new String[] { "Search the drawers upstairs", "of houses in eastern", "part of Falador." }, new Position(3035, 3347, 1), 350),
		BURTHORPE_BOXES(new String[] { "Search the tents in the", "imperial guard camp in", "Burthorpe for some boxes." }, new Position(2885, 3540), 3686),
		FALADOR_CHEST(new String[] { "Search through chests found", "in the upstairs of houses", "in eastern Falador." }, new Position(3041, 3364, 1), 375),
		TAVERLEY_HOUSES(new String[] { "Search through some drawers", "found in Taverley's houses." }, new Position(2894, 3418), 28632),
		RIMMINGTON_HOUSE(new String[] { "Search through some drawers", "in the upstairs of a", "house in Rimmington." }, new Position(2970, 3214, 1), 352),
		SEERS_VILLAGE_HOUSE(new String[] { "Search upstairs in the", "houses of Seers' Village", "for some drawers." }, new Position(2716, 3471, 1), 25766),
		;

		private String[] clue;
		private Position location;
		private int objectId;

		SimpleClues(String[] clue, Position location) {
			this.clue = clue;
			this.location = location;
			this.objectId = -1;
		}

		SimpleClues(String[] clue, Position location, int objectId) {
			this.clue = clue;
			this.location = location;
			this.objectId = objectId;
		}

		public String[] getClue() {
			return clue;
		}

		public Position getLocation() {
			return location;
		}

		public ClueTypes getType() {
			return ClueTypes.SIMPLE;
		}

		public int getObjectId() {
			return objectId;
		}

		public static int getIndex(String name) {
			for(SimpleClues clue : values())
				if(name.equals(clue.name()))
					return clue.ordinal();
			return 0;
		}
	}

	public enum Emotes {
		TAI_BWO_WANNAI(new String[] { "Beckon in Tai Bwo Wannai.", "Clap before you talk to me.", "Equip green d'hide chaps, a", "ring of duelling (8) and a mithril", "med helm." }, EmoteData.BECKON, new int[] {2772, 2815}, new int[] {3052, 3078}, 0, EmoteData.CLAP, new int[][] {{Slot.LEGS.ordinal(), 1099}, {Slot.RING.ordinal(), 2552}, {Slot.HEAD.ordinal(), 1143}}, false),
		//DIGSITE(new String[] { "Beckon in the Digsite, near the", "eastern winch. Bow or curts", "before you talk to me.", "Equip a pointed red and black snelm,", "snakeskin boots, and an", "iron pickaxe." }, EmoteData.BECKON, new int[] {3367, 3372}, new int[] {3425, 3430}, 0, EmoteData.BOW, new int[][] {{Slot.HEAD.ordinal(), 3329}, {Slot.FEET.ordinal(), 6328}, {Slot.WEAPON.ordinal(), 1267}}, false),
		SHILO_VILLAGE(new String[] { "Blow a kiss between the tables in", "Shilo Village bank.", "Beware of double agents!", "Equip a splitbark helm,", "mud pie and rune platebody." }, EmoteData.KISS, new int[] {2851, 2853}, new int[] {2952, 2954}, 0, null, new int[][] {{Slot.HEAD.ordinal(), 3385}, {Slot.WEAPON.ordinal(), 7170}, {Slot.BODY.ordinal(), 1127}}, true),
		ARDOUGNE(new String[] { "Blow a raspberry at the", "monkey cage in Ardougne Zoo.", "Equip a studded leather body, bronze", "platelegs and a mud pie." }, EmoteData.RASPBERRY, new int[] {2598, 2607}, new int[] {3273, 3283}, 0, null, new int[][] {{Slot.BODY.ordinal(), 1133}, {Slot.LEGS.ordinal(), 1075}, {Slot.WEAPON.ordinal(), 7170}}, false),
		FISHING_GUILD_BANK(new String[] { "Blow a raspberry in the Fishing Guild", "bank. Beware of double agents!", "Equip an elemental shield, blue", "dragonhide chaps, and a rune", "warhammer." }, EmoteData.RASPBERRY, new int[] {2583, 2587}, new int[] {3420, 3424}, 0, null, new int[][] {{Slot.SHIELD.ordinal(), 2890}, {Slot.LEGS.ordinal(), 2493}, {Slot.WEAPON.ordinal(), 1347}}, true),
		KEEP_LE_FAYE(new String[] { "Blow raspberries outside the", "entrance to Keep le Faye.", "Equip a coif, iron platebody", "and leather gloves." }, EmoteData.RASPBERRY, new int[] {2762, 2763}, new int[] {3400, 3403}, 0, null, new int[][] {{Slot.HEAD.ordinal(), 1169}, {Slot.BODY.ordinal(), 1067}, {Slot.HANDS.ordinal(), 1059}}, false), //2762, 3402 - fix walls
//		LIGHTHOUSE(new String[] { "Bow or curtsy at the top of the", "Lighthouse.", "Beware of double agents!", "Equip a blue d'hide body,", "blue d'hide vambraces and no", "jewellery." }, EmoteData.BOW, new int[] {2440, 2449}, new int[] {4596, 4605}, 2, null, new int[][] {{Slot.BODY.ordinal(), 2499}, {Slot.HANDS.ordinal(), 2487}, {Slot.RING.ordinal(), -1}, {Slot.AMULET.ordinal(), -1}}, true),
		DUEL_ARENA(new String[] { "Bow or curtsy in the ticket office of the", "Duel Arena. Equip an iron chainbody,", "leather chaps and coif." }, EmoteData.BOW, new int[] {3311, 3316}, new int[] {3240, 3244}, 0, null, new int[][] {{Slot.BODY.ordinal(), 1101}, {Slot.LEGS.ordinal(), 1095}, {Slot.HEAD.ordinal(), 1169}}, false),
		LEGENDS_GUILD(new String[] { "Bow or curtsy outside the entrance to", "the Legends' Guild.", "Equip iron platelegs, an", "emerald amulet and an oak", "shortbow." }, EmoteData.BOW, new int[] {2726, 2731}, new int[] {3347, 3349}, 0, null, new int[][] {{Slot.LEGS.ordinal(), 1067}, {Slot.AMULET.ordinal(), 1696}, {Slot.WEAPON.ordinal(), 843}}, false),
		BURTHORPE(new String[] { "Cheer at the Burthorpe Games Room.", "Have nothing equipped at all when", "you do." }, EmoteData.CHEER, new int[] {2893, 2904}, new int[] {3558, 3569}, 0, null, new int[][] {{Slot.HEAD.ordinal(), -1}, {Slot.CAPE.ordinal(), -1}, {Slot.AMULET.ordinal(), -1}, {Slot.ARROWS.ordinal(), -1}, {Slot.WEAPON.ordinal(), -1}, {Slot.BODY.ordinal(), -1}, {Slot.SHIELD.ordinal(), -1}, {Slot.LEGS.ordinal(), -1}, {Slot.HANDS.ordinal(), -1}, {Slot.FEET.ordinal(), -1}, {Slot.RING.ordinal(), -1}}, false),
		DRUIDS_CIRCLE(new String[] { "Cheer at the Druids' Circle.", "Equip an air tiara, a bronze", "two-handed sword, and gold amulet." }, EmoteData.CHEER, new int[] {2920, 2931}, new int[] {3478, 3490}, 0, null, new int[][] {{Slot.HEAD.ordinal(), 5527}, {Slot.WEAPON.ordinal(), 1307}, {Slot.AMULET.ordinal(), 1692}}, false),
		PORT_SARIM_MONKS(new String[] { "Cheer for the monks at", "Port Sarim.", "Equip a coif, steel", "plateskirt and a sapphire", "necklace." }, EmoteData.CHEER, new int[] {3043, 3050}, new int[] {3234, 3237}, 0, null, new int[][] {{Slot.HEAD.ordinal(), 1169}, {Slot.LEGS.ordinal(), 1083}, {Slot.AMULET.ordinal(), 1656}}, false),
		BARBARIAN_AGILITY(new String[] { "Cheer in the Barbarian Agility Arena.", "Headbang before you talk to me.", "Equip a steel platebody,", "maple shortbow and bronze boots." }, EmoteData.CHEER, new int[] {2528, 2555}, new int[] {3542, 3559}, 0, EmoteData.HEADBANG, new int[][] {{Slot.BODY.ordinal(), 1069}, {Slot.WEAPON.ordinal(), 853}, {Slot.FEET.ordinal(), 4119}}, false),
		OGRE_PEN(new String[] { "Cheer in the Ogre Pen in the", "Training Camp. Show you are angry", "before you talk to me.", "Equip a green dragonhide body and", "chaps, and a steel squareshield." }, EmoteData.CHEER, new int[] {2523, 2533}, new int[] {3373, 3377}, 0, EmoteData.ANGRY, new int[][] {{Slot.BODY.ordinal(), 1135}, {Slot.LEGS.ordinal(), 1099}, {Slot.SHIELD.ordinal(), 1177}}, false),
		EXAM_CENTRE(new String[] { "Clap in the main exam room", "in the Exam Centre.", "Equip a ruby amulet, blue flowers", "and leather gloves." }, EmoteData.CLAP, new int[] {3357, 3367}, new int[] {3332, 3348}, 0, null, new int[][] {{Slot.AMULET.ordinal(), 1698}, {Slot.WEAPON.ordinal(), 2464}, {Slot.HANDS.ordinal(), 1059}}, false),
		WIZARDS_TOWER(new String[] { "Clap on the causeway to", "the Wizards' Tower.", "Equip an iron med helm,", "emerald ring and leather gloves." }, EmoteData.CLAP, new int[] {3112, 3116}, new int[] {3173, 3206}, 0, null, new int[][] {{Slot.HEAD.ordinal(), 1137}, {Slot.RING.ordinal(), 1639}, {Slot.HANDS.ordinal(), 1059}}, false),
		//EAST_ARDOUGNE_MILL(new String[] { "Clap on the top level of", "the mill, north of east Ardougne.", "Equip an Emerald ring, blue", "gnome robe top and unenchanted", "tiara." }, EmoteData.CLAP, new int[] {2630, 2635}, new int[] {3383, 3388}, 2, null, new int[][] {{Slot.RING.ordinal(), 1639}, {Slot.BODY.ordinal(), -1}, {Slot.HEAD.ordinal(), -1}}, false),
		//CATHERBY_ARCHERY_SHOP(new String[] { "Cry in Catherby archery shop.", "Bow or curtsy before you talk to me.", "Equip a round red and black snelm, a hard", "leather body and an unblessed silver", "sickle." }, EmoteData.CRY, new int[] {2821, 2825}, new int[] {3441, 3445}, 0, EmoteData.BOW, new int[][] {{Slot.HEAD.ordinal(), 3329}, {Slot.BODY.ordinal(), 1131}, {Slot.WEAPON.ordinal(), 2961}}, false),
		GNOME_AGILITY_ARENA(new String[] { "Cry on the platform of the", "south-west tree of the Gnome", "Agility Arena.", "Indicate 'no' before you talk to me.", "Equip a steel kiteshield, ring of", "forging and green dragonhide chaps." }, EmoteData.CRY, new int[] {2472, 2477}, new int[] {3418, 3421}, 2, EmoteData.NO, new int[][] {{Slot.SHIELD.ordinal(), 1193}, {Slot.RING.ordinal(), 2568}, {Slot.LEGS.ordinal(), 1099}}, false),
		FISHING_GUILD(new String[] { "Dance a jig by the entrance to", "the Fishing Guild.", "Equip an emerald ring, sapphire", "amulet and bronze chainbody." }, EmoteData.JIG, new int[] {2612, 2615}, new int[] {3384, 3386}, 0, null, new int[][] {{Slot.RING.ordinal(), 1639}, {Slot.AMULET.ordinal(), 1694}, {Slot.BODY.ordinal(), 1103}}, false),
		//SHANTAY_PASS(new String[] { "Dance a jig under Shantay's Awning.", "Bow or curtsy before you talk to me.", "Equip a pointed blue snail helmet", "and an air staff." }, EmoteData.JIG, new int[] {3302, 3305}, new int[] {3122, 3125}, 0, EmoteData.BOW, new int[][] {{Slot.HEAD.ordinal(), 3343}, {Slot.WEAPON.ordinal(), 1381}, {Slot.SHIELD.ordinal(), 1173}}, false),
		SOPANEM(new String[] { "Dance at the cat-doored pyramid in", "Sophanem. Beware of double agents!", "Equip a ring of life,", "uncharged amulet of glory, and", "adamant two-handed sword." }, EmoteData.DANCE, new int[] {3293, 3296}, new int[] {2781, 2782}, 0, null, new int[][] {{Slot.RING.ordinal(), 2570}, {Slot.AMULET.ordinal(), 1704}, {Slot.WEAPON.ordinal(), 1317}}, true),
		DRAYNOR(new String[] { "Dance at the crossroads north", "of Draynor.", "Equip an iron chainbody,", "sapphire ring and longbow." }, EmoteData.DANCE, new int[] {3108, 3111}, new int[] {3293, 3296}, 0, null, new int[][] {{Slot.BODY.ordinal(), 1101}, {Slot.RING.ordinal(), 1637}, {Slot.WEAPON.ordinal(), 839}}, false),
		CANIFIS(new String[] { "Dance in the centre of Canifis.", "Bow or curtsy before you talk to me.", "Equip a spiny helmet,", "mithril platelegs and an iron", "two-handed sword." }, EmoteData.DANCE, new int[] {3488, 3499}, new int[] {3485, 3494}, 0, EmoteData.BOW, new int[][] {{Slot.HEAD.ordinal(), 4551}, {Slot.LEGS.ordinal(), 1071}, {Slot.WEAPON.ordinal(), 1309}}, false),
		PARTY_ROOM(new String[] { "Dance in the Party Room.", "Equip a steel full helm, steel", "platebody and iron plateskirt." }, EmoteData.DANCE, new int[] {3036, 3055}, new int[] {3371, 3385}, 0, null, new int[][] {{Slot.HEAD.ordinal(), 1157}, {Slot.BODY.ordinal(), 1119}, {Slot.LEGS.ordinal(), 1081}}, false),
		LUMBRIDGE_SHACK(new String[] { "Dance in the shack in Lumbridge", "Swamp. Equip a bronze dagger, iron full", "helm and a gold ring." }, EmoteData.DANCE, new int[] {3202, 3205}, new int[] {3170, 3167}, 0, null, new int[][] {{Slot.WEAPON.ordinal(), 1205}, {Slot.HEAD.ordinal(), 1153}, {Slot.RING.ordinal(), 1635}}, false),
		AL_KHARID_MINE(new String[] { "Headbang in the mine", "north of Al-kharid.", "Equip a desert shirt,", "leather gloves and leather boots." }, EmoteData.HEADBANG, new int[] {3291, 3306}, new int[] {3281, 3317}, 0, null, new int[][] {{Slot.BODY.ordinal(), 1833}, {Slot.HANDS.ordinal(), 1059}, {Slot.FEET.ordinal(), 1061}}, false),
		BEEHIVES(new String[] { "Jump for joy at the beehives.", "Equip iron boots,", "an unholy symbol and", "a steel hatchet." }, EmoteData.JUMP_FOR_JOY, new int[] {2752, 2766}, new int[] {3436, 3451}, 0, null, new int[][] {{Slot.FEET.ordinal(), 4121}, {Slot.AMULET.ordinal(), 1724}, {Slot.WEAPON.ordinal(), 1353}}, false),
		YANILLE(new String[] { "Jump for joy in Yanille bank.", "Dance a jig before you talk to me.", "Equip an iron crossbow, adamant", "med helm and snakeskin chaps." }, EmoteData.JUMP_FOR_JOY, new int[] {2609, 2613}, new int[] {3088, 3097}, 0, EmoteData.JIG, new int[][] {{Slot.WEAPON.ordinal(), 9177}, {Slot.HEAD.ordinal(), 1145}, {Slot.LEGS.ordinal(), 6324}}, false),
		SINCLAIR_MANSION(new String[] { "Laugh at the crossroads south", "of Sinclair Mansion.", "Equip a cowl, strength amulet", "and iron scimitar." }, EmoteData.LAUGH, new int[] {2737, 2743}, new int[] {3534, 3538}, 0, null, new int[][] {{Slot.HEAD.ordinal(), 1167}, {Slot.AMULET.ordinal(), 1725}, {Slot.WEAPON.ordinal(), 1323}}, false),
		MOUNTAIN_CAMP(new String[] { "Laugh in the Jokul's tent in the", "Mountain Camp.", "Beware of double agents! Equip", "a rune full helmet, blue dragonhide", "chaps and a fire battlestaff." }, EmoteData.LAUGH, new int[] {2794, 2796}, new int[] {3670, 3671}, 0, null, new int[][] {{Slot.HEAD.ordinal(), 1163}, {Slot.LEGS.ordinal(), 2493}, {Slot.WEAPON.ordinal(), 1393}}, true),
		HAUNTED_WOODS(new String[] { "Panic in the heart of the Haunted", "Woods.", "Beware of double agents!", "Have no items equipped when you do." }, EmoteData.PANIC, new int[] {3549, 3615}, new int[] {3463, 3507}, 0, null, new int[][] {{Slot.HEAD.ordinal(), -1}, {Slot.CAPE.ordinal(), -1}, {Slot.AMULET.ordinal(), -1}, {Slot.ARROWS.ordinal(), -1}, {Slot.WEAPON.ordinal(), -1}, {Slot.BODY.ordinal(), -1}, {Slot.SHIELD.ordinal(), -1}, {Slot.LEGS.ordinal(), -1}, {Slot.HANDS.ordinal(), -1}, {Slot.FEET.ordinal(), -1}, {Slot.RING.ordinal(), -1}}, true),
		LIMESTONE_MINE(new String[] { "Panic in the limestone mine.", "Equip bronze platelegs, a", "steel pickaxe and a steel", "medium helmet." }, EmoteData.PANIC, new int[] {3368, 3376}, new int[] {3496, 3504}, 0, null, new int[][] {{Slot.LEGS.ordinal(), 1075}, {Slot.WEAPON.ordinal(), 1269}, {Slot.HEAD.ordinal(), 1141}}, false),
		WHITE_WOLF_MOUNTAIN(new String[] { "Panic by the pilot on White Wolf", "Mountain.", "Beware of double agents!", "Equip mithril platelegs, a ring of life", "and a rune hatchet." }, EmoteData.PANIC, new int[] {2847, 2852}, new int[] {3498, 3501}, 0, null, new int[][] {{Slot.LEGS.ordinal(), 1071}, {Slot.RING.ordinal(), 2570}, {Slot.WEAPON.ordinal(), 1359}}, true),
		FISHING_TRAWLER(new String[] { "Panic on the pier where", "you catch the Fishing Trawler.", "Have nothing equipped at all when", "you do." }, EmoteData.PANIC, new int[] {2665, 2671}, new int[] {3160, 3166}, 0, null, new int[][] {{Slot.HEAD.ordinal(), -1}, {Slot.CAPE.ordinal(), -1}, {Slot.AMULET.ordinal(), -1}, {Slot.ARROWS.ordinal(), -1}, {Slot.WEAPON.ordinal(), -1}, {Slot.BODY.ordinal(), -1}, {Slot.SHIELD.ordinal(), -1}, {Slot.LEGS.ordinal(), -1}, {Slot.HANDS.ordinal(), -1}, {Slot.FEET.ordinal(), -1}, {Slot.RING.ordinal(), -1}}, false),
		MORYTANIA(new String[] { "Panic by the mausoleum in", "Morytania.", "Wave before you speak to me.", "Equip a mithril plateskirt,", "maple longbow and no boots." }, EmoteData.PANIC, new int[] {3491, 3512}, new int[] {3562, 3579}, 0, EmoteData.WAVE, new int[][] {{Slot.LEGS.ordinal(), 1085}, {Slot.WEAPON.ordinal(), 851}, {Slot.FEET.ordinal(), -1}}, false),
		BANANA(new String[] { "Salute in the banana plantation.", "Beware of double agents!", "Equip a diamond ring, amulet of", "power, and nothing on your chest", "and legs." }, EmoteData.SALUTE, new int[] {2906, 2932}, new int[] {3153, 3175}, 0, null, new int[][] {{Slot.RING.ordinal(), 1643}, {Slot.AMULET.ordinal(), 1731}, {Slot.BODY.ordinal(), -1}, {Slot.LEGS.ordinal(), -1}}, true),
		RIMMINGTON(new String[] { "Shrug in the mine near Rimmington.", "Equip a gold necklace,", "a gold ring and a bronze", "spear." }, EmoteData.SHRUG, new int[] {2963, 2990}, new int[] {3227, 3252}, 0, null, new int[][] {{Slot.AMULET.ordinal(), 1654}, {Slot.RING.ordinal(), 1635}, {Slot.WEAPON.ordinal(), 1237}}, false),
		ZAMORAK_TEMPLE(new String[] { "Shrug in the Zamorak temple found", "in the Eastern Wilderness.", "Beware of double agents!", "Equip bronze platelegs, an iron", "platebody, and blue d'hide vambraces." }, EmoteData.SHRUG, new int[] {3233, 3245}, new int[] {3603, 3616}, 0, null, new int[][] {{Slot.LEGS.ordinal(), 1075}, {Slot.BODY.ordinal(), 1115}, {Slot.HANDS.ordinal(), 2487}}, true),
		LUMBRIDGE_MILL(new String[] { "Think in the middle of the wheat", "field by the Lumbridge mill.", "Equip a sapphire necklace, kyatt legs and an oak shortbow." }, EmoteData.THINK, new int[] {3156, 3163}, new int[] {3291, 3306}, 0, null, new int[][] {{Slot.AMULET.ordinal(), 1656}, {Slot.LEGS.ordinal(), 10035}, {Slot.WEAPON.ordinal(), 843}}, false),
		//OBSERVATORY(new String[] { "Think under the lens in the", "Observatory.", "Spin before you talk to me.", "Equip a mithril chainbody, green", "dragonhide chaps and ruby amulet." }, EmoteData.THINK, new int[] {2439, 2442}, new int[] {3164, 3166}, 1, EmoteData.SPIN, new int[][] {{Slot.BODY.ordinal(), 1109}, {Slot.LEGS.ordinal(), 1099}, {Slot.AMULET.ordinal(), 1698}}, false),
		RIMMINGTON_CROSSROADS(new String[] { "Spin at the crossroads north", "of Rimmington.", "Equip a Sapphire ring, yellow", "flowers and leather chaps." }, EmoteData.SPIN, new int[] {2979, 2985}, new int[] {3274, 3278}, 0, null, new int[][] {{Slot.RING.ordinal(), 1637}, {Slot.WEAPON.ordinal(), 2466}, {Slot.LEGS.ordinal(), 1095}}, false),
		DRAYNOR_MANOR_FOUNTAIN(new String[] { "Spin in Draynor Manor by", "the fountain.", "Equip an iron platebody, studded", "leather chaps and a bronze", "full helmet." }, EmoteData.SPIN, new int[] {3086, 3091}, new int[] {3332, 3337}, 0, null, new int[][] {{Slot.BODY.ordinal(), 1115}, {Slot.LEGS.ordinal(), 1097}, {Slot.HEAD.ordinal(), 1155}}, false),
		BARBARIAN_VILLAGE(new String[] { "Spin on the bridge by Barbarian", "Village. Salute before you talk to me.", "Equip an iron hatchet,", "steel kiteshield and mithril", "full helm." }, EmoteData.SPIN, new int[] {3103, 3107}, new int[] {3420, 3421}, 0, EmoteData.SALUTE, new int[][] {{Slot.WEAPON.ordinal(), 1349}, {Slot.SHIELD.ordinal(), 1193}, {Slot.HEAD.ordinal(), 1159}}, false),
		MUDSKIPPER_POINT(new String[] { "Wave on Mudskipper Point.", "Equip a gold ring, leather", "chaps and a steel mace." }, EmoteData.WAVE, new int[] {2981, 3005}, new int[] {3108, 3126}, 0, null, new int[][] {{Slot.RING.ordinal(), 1635}, {Slot.LEGS.ordinal(), 1095}, {Slot.WEAPON.ordinal(), 1424}}, false),
		LUMBERYARD(new String[] { "Wave along the south fence", "of the Lumber Yard.", "Equip a hard leather body,", "leather chaps and a bronze hatchet." }, EmoteData.WAVE, new int[] {3305, 3311}, new int[] {3492, 3493}, 0, null, new int[][] {{Slot.BODY.ordinal(), 1129}, {Slot.LEGS.ordinal(), 1095}, {Slot.WEAPON.ordinal(), 1351}}, false),
		CASTLE_WARS(new String[] { "Yawn in the Castle Wars lobby.", "Shrug before you talk to me.", "Equip a ruby amulet, mithril", "scimitar and iron square shield." }, EmoteData.YAWN, new int[] {2437, 2447}, new int[] {3081, 3098}, 0, EmoteData.SHRUG, new int[][] {{Slot.AMULET.ordinal(), 1698}, {Slot.WEAPON.ordinal(), 1329}, {Slot.SHIELD.ordinal(), 1175}}, false),
		DRAYNOR_MARKET(new String[] { "Yawn in Draynor Marketplace.", "Equip an iron kiteshield, steel", "longsword and studded leather chaps." }, EmoteData.YAWN, new int[] {3074, 3086}, new int[] {3245, 3256}, 0, null, new int[][] {{Slot.SHIELD.ordinal(), 1191}, {Slot.WEAPON.ordinal(), 1295}, {Slot.LEGS.ordinal(), 1097}}, false),
		VARROCK_LIBRARY(new String[] { "Yawn in Varrock Palace library.", "Equip a holy symbol, leather", "vambraces and an iron warhammer." }, EmoteData.YAWN, new int[] {3207, 3214}, new int[] {3490, 3497}, 0, null, new int[][] {{Slot.AMULET.ordinal(), 1718}, {Slot.HANDS.ordinal(), 1063}, {Slot.WEAPON.ordinal(), 1335}}, false),
		ROGUES_GENERAL_STORE(new String[] { "Yawn in the rogues' general store.", "Beware of double agents!", "Equip an iron square shield,", "blue dragonhide vambraces, and", "an iron pickaxe." }, EmoteData.YAWN, new int[] {3024, 3027}, new int[] {3699, 3704}, 0, null, new int[][] {{Slot.SHIELD.ordinal(), 1175}, {Slot.HANDS.ordinal(), 2487}, {Slot.WEAPON.ordinal(), 1267}}, true),
		;

		Emotes(String[] clue, EmoteData emoteOne, int[] x, int[] y, int plane, EmoteData emoteTwo, int[][] equipment, boolean doubleAgent) {
			this.clue = clue;
			this.emoteOne = emoteOne;
			this.x = x;
			this.y = y;
			this.plane = plane;
			this.emoteTwo = emoteTwo;
			this.equipment = equipment;
			this.doubleAgent = doubleAgent;
		}

		private String[] clue;
		private EmoteData emoteOne, emoteTwo;
		private int plane;
		private int[] x, y;
		private int[][] equipment;
		private boolean doubleAgent;

		public int[] getX() {
			return x;
		}

		public int[] getY() {
			return y;
		}

		public int getPlane() {
			return plane;
		}

		public String[] getClue() {
			return clue;
		}

		public EmoteData getEmoteOne() {
			return emoteOne;
		}

		public EmoteData getEmoteTwo() {
			return emoteTwo;
		}

		public int[][] getEquipment() {
			return equipment;
		}

		public boolean isDoubleAgent() {
			return doubleAgent;
		}

		public ClueTypes getType() {
			return ClueTypes.EMOTE;
		}

		public static int getIndex(String name) {
			for(Emotes emote : values())
				if(name.equals(emote.name()))
					return emote.ordinal();
			return 0;
		}

		public static Emotes getRandom(ClueTiers clueTier) {
			List<Emotes> available = new ArrayList<>();
			for(Emotes clue : values()) {
				switch (clueTier) {
					case EASY:
						if(clue.getEmoteTwo() == null && !clue.isDoubleAgent())
							available.add(clue);
						break;
					case MEDIUM:
						if(clue.getEmoteTwo() != null && !clue.isDoubleAgent())
							available.add(clue);
						break;
					case HARD:
						if(clue.getEmoteTwo() == null && clue.isDoubleAgent())
							available.add(clue);
						break;
				}
			}
			if(available.size() == 0)
				return null;

			return available.get(Misc.random(available.size() - 1));
		}
	}
}
