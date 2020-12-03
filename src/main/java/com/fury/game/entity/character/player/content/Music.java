package com.fury.game.entity.character.player.content;

import com.fury.game.content.global.Achievements;
import com.fury.game.world.map.Position;
import com.fury.core.model.node.entity.actor.figure.player.Player;

import java.util.List;

public class Music {
	
	public static int INTERFACE_ID = 60000, PLAYLIST_INTERFACE_ID = 60900, nameInterfaceId = 60007, interfaceStartId = 60009;
	
	public enum Songs {
		SEVENTH_REALM("7th Realm", "at Brimhaven Dungeon.", 2624, 9472, 2687, 9599),
		ADVENTURE("Adventure", "at Varrock Palace.", 3200, 3456, 3263, 3519),
		AL_KHARID("Al Kharid", "at Al Kharid.", 3264, 3136, 3391, 3199),
		ALL_FAIRY_IN_LOVE_N_WAR("All's Fairy in Love and War", "at the fair resistance hideout in Fair Tale Part II.", -1, -1, -1, -1),
		ALONE("Alone", "at the Ice Mountain and Monastery north of Falador.", 3008, 3456, 3071, 3519),
		AMBIENT_JUNGLE("Ambient Jungle", "at Shilo Village.", 2816, 2944, 2879, 3007),
		ANYWHERE("Anywhere", "at Marim's main gate.", 2688, 2752, 2751, 2815),
		ARABIAN("Arabian", "in the Kharidian Desert.",  new int[][] {
			new int[] {3264}, new int[] {3200}, new int[] {3327}, new int[] {3263},
			new int[] {3200}, new int[] {3072}, new int[] {3263}, new int[] {3135}}),
		ARABIAN2("Arabian2", "at Al Kharid mine.", 3264, 3264, 3327, 3327),
		ARABIAN3("Arabian3", "at the desert entrance to the Kalphite Lair.", 3200, 3072, 3263, 3135),
		ARABIQUE("Arabique", "at the hellhounds in Taverley Dungeon.", 2816, 9792, 2879, 9855),
		ARMY_OF_DARKNESS("Army Of Darkness", "at the Dark Warrior's Fortress.", 3008, 3584, 3071, 3647),
		ARRIVAL("Arrival", "at the gem trader in Falador.", 2880, 3328, 2943, 3391),
		ARTISTRY("Artistry", "during the Mime random event.", 1984, 4736, 2047, 4799),
		ATTACK1("Attack", "at the Battlefield north of Tree Gnome Village.", 2496, 3200, 2559, 3263),
		ATTACK2("Attack 2", "at Elvarg's lair or during Grand Tree Quest.", 2816, 9600, 2879, 9663),
		ATTACK3("Attack 3", "to the north of the Lava Maze.", 3008, 10240, 3071, 10303),
		ATTACK4("Attack 4", "at the Fight Arena.", new int[][] {
			new int[] {2560}, new int[] {3136}, new int[] {2623}, new int[] {3199},
			new int[] {2560}, new int[] {9536}, new int[] {2623}, new int[] {9599}}),
		ATTACK5("Attack 5", "at the King Black Dragon.", 2240, 4672, 2303, 4735),
		ATTACK6("Attack 6", "at Gu'Tanoth Ogre Enclave.", 2560, 9408, 2623, 9471),
		ATTENTION("Attention", "at the coastline below Rimmington.", 2944, 3136, 3007, 3199),
		AUTUMN_VOYAGE("Autumn Voyage", "at Lumbridge farm.", 3200, 3303, 3273, 3353),
		AYE_CAR_RUM_BA("Aye Car Rum Ba", "on Braindeath Island.", 2112, 5056, 2175, 5119),
		AZTEC("Aztec", "at the Brimhaven Agility Arena.", 2752, 9536, 2815, 9599),

		BACKGROUND("Background", "on Entrana.", new int[][] {
			new int[] {2752}, new int[] {3328}, new int[] {2879}, new int[] {3391},
			new int[] {1953}, new int[] {4992}, new int[] {1983}, new int[] {5055}}),
		BALLAD_OF_ENCHANTMENT("Ballad Of Enchantment", "at the Monastery.", 2560, 3200, 2623, 3263),
		BANDIT_CAMP("Bandit Camp", "at the Bandit Camp in the Kharidian Desert.", 3136, 2944, 3199, 3007),
		BARBARIANISM("Barbarianism", "at Barbarian Village.", new int[][] {
			new int[] {3072}, new int[] {3392}, new int[] {3135}, new int[] {3455},
			new int[] {3072}, new int[] {9792}, new int[] {3135}, new int[] {9855}}),
		BARKING_MAD("Barking Mad", "at the Werewolf Agility Course.", 3520, 9856, 3583, 9919),
		BAROQUE("Baroque", "in Ardougne.", 2624, 3264, 2687, 3327),
		BEYOND("Beyond", "in the Dwarven tunnel under Wolf Mountain.", new int[][] {
			new int[] {2816}, new int[] {9856}, new int[] {2879}, new int[] {9919},
			new int[] {2816}, new int[] {9920}, new int[] {2943}, new int[] {9983}}),
		BIG_CHORDS("Big Chords", "to the west of Yanille.", 2496, 3072, 2559, 3135),
		BLISTERING_BARNACLES("Blistering Barnacles", "on Braindeath Island's mountain.", 2112, 5120, 2175, 5183),
		BODY_PARTS("Body Parts", "at the dungeon south-east of Fenkenstrain's Castle.", 3456, 9920, 3583, 9983),
		BONE_DANCE("Bone Dance", "to the west of Mort'ton.", 3392, 3264, 3455, 3327),
		BONE_DRY("Bone Dry", "in the smoky well near Pollnivneach.", 3200, 9344, 3327, 9407),
		BOOK_OF_SPELLS("Book Of Spells", "at Lumbridge Swamp.", 3136, 3136, 3199, 3199),
		BORDERLAND("Borderland", "to the east of Rellekka.", 2688, 3648, 2751, 3775),
		BREEZE("Breeze", "at Isafdar near the Underground Pass.", 2240, 3200, 2303, 3263),
		BREW_HOO_HOO("Brew Hoo Hoo", "at Port Phasmatys brewery.", -1, -1, -1, -1),
		BRIMSTAILS_SCALES("Brimstail's Scales", "at Brimstail's cave.", -1, -1, -1, -1),
		BUBBLE_AND_SQUEAK("Bubble And Squeak", "at the Keldagrim Rat Pits.", 1920, 4672, 1983, 4735),

		CAMELOT("Camelot", "at Camelot Castle.", 2752, 3456, 2815, 3583),
		CASTLEWARS("Castlewars", "", 2368, 3072, 2431, 3135),
		CATCH_ME_IF_YOU_CAN("Catch Me If You Can", "at the Ardougne Rat Pits.", 2624, 9600, 2687, 9663),
		CAVE_BACKGROUND("Cave Background", "at the Dwarven Mine or the Gamers' Grotto.", new int[][] {
			new int[] {3008}, new int[] {9728}, new int[] {3071}, new int[] {9791},
			new int[] {2944}, new int[] {9792}, new int[] {3071}, new int[] {9855}}),
		CAVE_OF_BEASTS("Cave Of Beasts", "during Mountain Daughter.", 2752, 10048, 2815, 10111),
		CAVE_OF_THE_GOBLINS("Cave Of The Goblins", "at the caves beneath Lumbridge Swamp.", 3136, 9536, 3263, 9599),
		CAVERN("Cavern", "at Yanille Agility Dungeon.", 3008, 10304, 3071, 10367),
		CELLAR_SONG("Cellar Song", "at the bank vault west of Varrock.", 3136, 9792, 3199, 9855),
		CHAIN_OF_COMMAND("Chain Of Command", "during Temple of Ikov.", new int[][] {
			new int[] {2624}, new int[] {9728}, new int[] {2687}, new int[] {9920},
			new int[] {2688}, new int[] {9792}, new int[] {2751}, new int[] {9855}}),
		CHAMBER("Chamber", "on the middle floor of the Haunted Mine.", new int[][] {
			new int[] {2688}, new int[] {4416}, new int[] {2751}, new int[] {4479},
			new int[] {2752}, new int[] {4480}, new int[] {2815}, new int[] {4543}}),
		CHEF_SURPRIZE("Chef Surprize", "during Recipe for Disaster.", 1875, 5312, 1919, 5375),
		CHICKENED_OUT("Chickened Out", "at the Evil Chicken's lair.", 2432, 4352, 2495, 4415),
		CHOMPY_HUNT("Chompy Hunt", "at the chompy hunt area near Rantz the ogre.", new int[][] {
			new int[] {2624}, new int[] {2944}, new int[] {2687}, new int[] {3007},
			new int[] {2624}, new int[] {9344}, new int[] {2687}, new int[] {9407}}),
		CITY_OF_THE_DEAD("City Of The Dead", "to the north of (and at) Menaphos.", new int[][] {
			new int[] {3200}, new int[] {2752}, new int[] {3263}, new int[] {2879},
			new int[] {3264}, new int[] {2752}, new int[] {3327}, new int[] {2815}}),
		CLAUSTROPHOBIA("Claustrophobia", "during Between a Rock...", 2304, 4928, 2431, 4991),
		CLOSE_QUARTERS("Close Quarters", "to the west of the Dark Warriors' Fortress in the Wilderness.", 3136, 3712, 3199, 3775),
		COMPETITION("Competition", "at Burthorpe Games Room.", 2176, 4928, 2239, 4991),
		COMPLICATION("Complication", "at the Chaos Altar.", 2240, 4800, 2303, 4863),
		CONTEST("Contest", "in Trollheim.", 2880, 3584, 2943, 3647),
		CORPORAL_PUNISHMENT("Corporal Punishment", "during the Drill Demon random event.", 3136, 4800, 3199, 4863),
		CORRIDORS_OF_POWER("Corridors Of Power", "during Royal Trouble.", -1, -1, -1, -1),
		COURAGE("Courage", "at Taverley Dungeon.", 2880, 9792, 2943, 9919),
		CRYSTAL_CASTLE("Crystal Castle", "to the south-east of Prifddinas.", 2240, 3264, 2303, 3391),
		CRYSTAL_CAVE("Crystal Cave", "at Zanaris market.", 2432, 4416, 2495, 4479),
		CRYSTAL_SWORD("Crystal Sword", "at the Wilderness, north of Varrock.", new int[][] {
			new int[] {3200}, new int[] {3520}, new int[] {3263}, new int[] {3583},
			new int[] {2624}, new int[] {9664}, new int[] {2687}, new int[] {9727}}),
		CURSED("Cursed", "during Underground Pass.", 2368, 9664, 2495, 9727),

		DAGANNOTH_DAWN("Dagannoth Dawn", "in Waterbirth Island Dungeon.", new int[][] {
			new int[] {1792}, new int[] {4352}, new int[] {1855}, new int[] {4415},
			new int[] {1920}, new int[] {4352}, new int[] {1983}, new int[] {4415}}),
		DANCE_OF_DEATH("Dance Of Death", "at the Sepulchre of Death in the Stronghold of Security.", -1, -1, -1, -1),
		DANCE_OF_THE_UNDEAD("Dance Of The Undead", "at the Barrows.", 3520, 3264, 3583, 3327),
		DANGEROUS_ROAD("Dangerous Road", "at the caves below Crandor.", 2816, 9536, 2879, 9599),
		DANGEROUS_WAY("Dangerous Way", "beneath the Barrows.", 3520, 9664, 3583, 9727),
		DANGEROUS("Dangerous", "to the north of Edgeville.", new int[][] {
				new int[] {3072}, new int[] {3520}, new int[] {3135}, new int[] {3583},
				new int[] {3264}, new int[] {3776}, new int[] {3391}, new int[] {3839}}),
		DARK("Dark", "in the south-east of the Wilderness.", 3264, 3648, 3391, 3711),
		DAVY_JONES_LOCKER("Davy Jones's Locker", "underwater near Port Khazard.", 2944, 9472, 3007, 9535),
		DEAD_CAN_DANCE("Dead Can Dance", "in the Graveyard of Shadows in the Wilderness.", 3136, 3648, 3199, 3711),
		DEAD_QUIET("Dead Quiet", "to the north of the Mort Myre area.", new int[][] {
			new int[] {3392}, new int[] {3392}, new int[] {3455}, new int[] {3455},
			new int[] {2304}, new int[] {4992}, new int[] {2431}, new int[] {5055}}),
		DEADLANDS("Deadlands", "at the Haunted Woods of Morytania.", 3520, 3456, 3647, 3519),
		DEEP_DOWN("Deep Down", "on the bottom floor of the Haunted Mine.", new int[][] {
			new int[] {2688}, new int[] {4544}, new int[] {2815}, new int[] {4607},
			new int[] {2688}, new int[] {4480}, new int[] {2751}, new int[] {4543}}),
		DEEP_WILDY("Deep Wildy", "in the north-west of the Wilderness.", 2944, 3776, 3007, 3903),
		DESERT_HEAT("Desert Heat", "in the desert north of Nardah.", 3392, 2944, 3455, 3071),
		DESERT_VOYAGE("Desert Voyage", "around the Desert Mining Camp.", new int[][] {
			new int[] {3264}, new int[] {2944}, new int[] {3327}, new int[] {3071},
			new int[] {3328}, new int[] {3008}, new int[] {3391}, new int[] {3071}}),
		DIANGOS_LITTLE_HELPERS("Diango's Little Helpers", "at Diango's Christmas workshop.", 1984, 4416, 2047, 4479),
		DIMENSION_X("Dimension X", "on the gorak plane in Fairy Tale Part II.", -1, -1, -1, -1),
		DISTANT_LAND("Distant Land", "at Burgh de Rott.", 3456, 3161, 3583, 3263),
		DISTILLERY_HILARITY("Distillery Hilarity", "at Trouble Brewing's distillery.", -1, -1, -1, -1),
		DOGS_OF_WAR("Dogs Of War", "at the Vault of War in the Stronghold of Security.", -1, -1, -1, -1),
		DOORWAYS("Doorways", "to the north of the Grand Exchange.", 3136, 3456, 3199, 3519),
		DOWN_BELOW("Down Below", "at the dungeon below Draynor Village.", 3072, 9600, 3135, 9727),
		DOWN_TO_EARTH("Down To Earth", "at the Earth Altar.", 2624, 4800, 2687, 4863),
		DRAGONTOOTH_ISLAND("Dragontooth Island", "on Dragontooth Island.", 3776, 3520, 3839, 3583),
		DREAM("Dream", "on the path between Lumbridge and Draynor.", 3136, 3200, 3199, 3263),
		DUEL_ARENA("Duel Arena", "at the Duel Arena.", 3328, 3200, 3419, 3263),
		DUNJUN("Dunjun", "in Taverley Dungeon.", 2880, 9728, 3007, 9791),
		DYNASTY("Dynasty", "at the town of Pollnivneach.", 3328, 2944, 3391, 3007),

		EGYPT("Egypt", "at Shantay Pass.", 3264, 3072, 3391, 3135),
		ELVEN_MIST("Elven Mist", "at the exit of the Underground Pass.", 2304, 3200, 2367, 3263),
		EMOTION("Emotion", "in the Tree Gnome Village maze.", new int[][] {
			new int[] {2496}, new int[] {3136}, new int[] {2559}, new int[] {3199},
			new int[] {2560}, new int[] {4416}, new int[] {2623}, new int[] {4479},
			new int[] {2496}, new int[] {9536}, new int[] {2559}, new int[] {9599}}),
		EMPEROR("Emperor", "in Melzar's Maze.", new int[][] {
			new int[] {2880}, new int[] {3200}, new int[] {2943}, new int[] {3263},
			new int[] {2880}, new int[] {9600}, new int[] {2943}, new int[] {9663}}),
		ESCAPE("Escape", "at the perfect gold mine under Ardougne.", 2688, 9664, 2751, 9727),
		ETCETERIA("Etcetera", "was unlocked at Etceteria.", 2560, 3840, 2623, 3903),
		EVERLASTING_FIRE("Everlasting Fire", "in the north-east corner of the Wilderness.", 3328, 3904, 3391, 3967),
		EVERYWHERE("Everywhere", "to the south-west of Prifddinas.", 2112, 3264, 2239, 3327),
		EVIL_BOBS_ISLAND("Evil Bob's Island", "", -1, -1, -1, -1),

		EXPANSE("Expanse", "at the stone circle in Varrock.", new int[][] {
			new int[] {3136}, new int[] {3904}, new int[] {3199}, new int[] {3967},
			new int[] {3200}, new int[] {3328}, new int[] {3263}, new int[] {3391},
			new int[] {3200}, new int[] {9728}, new int[] {3263}, new int[] {9791}}),
		EXPECTING("Expecting", "at the unholy altar north of the Observatory.", new int[][] {
			new int[] {2432}, new int[] {3200}, new int[] {2495}, new int[] {3263},
			new int[] {2432}, new int[] {9600}, new int[] {2495}, new int[] {9663}}),
		EXPEDITION("Expedition", "in the caverns below the Observatory.", 2880, 9984, 2943, 10047),
		EXPOSED("Exposed", "to the south of Tyras Camp in Isafdar.", 2176, 3072, 2239, 3135),

		FAERIE("Faerie", "at Zanaris.", 2368, 4352, 2431, 4479),
		FAITHLESS("Faithless", "at the Chaos Temple in the Wilderness.", 3200, 3584, 3327, 3647),
		FANFARE("Fanfare", "at Falador Castle.", 2944, 3328, 3007, 3391),
		FANFARE2("Fanfare2", "at the Shipyard on Karamja.", 2944, 3008, 3007, 3071),
		FANFARE3("Fanfare3", "at Port Khazard.", 2624, 3136, 2687, 3199),
		FANGS_FOR_THE_MEMORY("Fangs For The Memory", "during In Aid of the Myreque.", -1, -1, -1, -1),
		FAR_AWAY("Far Away", "at Lletya.", 2304, 3136, 2367, 3199),
		FEAR_AND_LOATHING("Fear And Loathing", "at the fear room of Tolna's rift area.", -1, -1, -1, -1),
		FENKENSTRAINS_REFRAIN("Fenkenstrain's Refrain", "at Fenkenstrain's Castle.", 3456, 3520, 3583, 3583),
		FIGHT_OR_FLIGHT("Fight Or Flight", "at the slave mine under West Ardougne.", 1920, 4608, 2047, 4671),
		FIND_MY_WAY("Find My Way", "at the gnome tunnel in Monkey Madness.", 2688, 9088, 2815, 9151),
		FIRE_AND_BRIMSTONE("Fire And Brimstone", "in the TzHaar Fight Pits.", 2368, 5120, 2431, 5183),
		FISHING("Fishing", "at Catherby beach.", 2816, 3392, 2879, 3455),
		FLUTE_SALAD("Flute Salad", "at the Lumbridge windmill area.", 3136, 3264, 3199, 3327),
		FOOD_FOR_THOUGHT("Food For Thought", "at the Catacomb of Famine in the Stronghold of Security.", -1, -1, -1, -1),
		FORBIDDEN("Forbidden", "to the north of the Lumber Yard.", 3264, 3520, 3391, 3583),
		FOREST("Forest", "to the south-east of Isafdar.", 2240, 3136, 2303, 3199),
		FOREVER("Forever", "at Edgeville.", 3072, 3456, 3135, 3519),
		FORGETTABLE_MELODY("Forgettable Melody", "during Forgettable Tale...", -1, -1, -1, -1),
		FORGOTTEN("Forgotten", "during the Golem.", -1, -1, -1, -1),
		FROGLAND("Frogland", "during the Frog random event.", 2432, 4736, 2495, 4799),
		FROSTBITE("Frostbite", "at the Ice Path north of Trollheim.", 2816, 3776, 2879, 3839),
		FRUITS_DE_MER("Fruits De Mer", "at the Fishing Platform.", 2752, 3264, 2815, 3327),
		FUNNY_BUNNIES("Funny Bunnies", "in an Easter holiday event (2006 onwards).", 2432, 5248, 2559, 5311),

		GAOL("Gaol", "at Gu'Tanoth.", new int[][] {
			new int[] {3008}, new int[] {3712}, new int[] {3071}, new int[] {3775},
			new int[] {2496}, new int[] {3008}, new int[] {2559}, new int[] {3071},
			new int[] {2496}, new int[] {9408}, new int[] {2559}, new int[] {9471}}),
		GARDEN("Garden", "at Varrock city centre.", 3200, 3392, 3263, 3455),
		GNOME_KING("Gnome King", "at the Tree Gnome Stronghold.", 2432, 3456, 2495, 3583),
		GNOME_THEME("Gnome Theme", "", 3008, 3392, 3071, 3455),//Renamed Dwarf Theme
		GNOME_VILLAGE("Gnome Village I", "at the Tree Gnome Stronghold's Agility Training Area.", 2432, 3392, 2495, 3455),
		GNOME_VILLAGE2("Gnome Village II", "to the south-west of the Tree Gnome Stronghold.", 2304, 3392, 2431, 3455),
		GNOME("Gnome", "south of Goblin Village.", 2944, 3456, 3007, 3519),
		GNOMEBALL("Gnomeball", "at the Gnome Ball Field.", 2304, 3456, 2431, 3583),
		GOBLIN_GAME("Goblin Game", "at the goblin cave near the Fishing Guild.", 2560, 9792, 2623, 9855),
		GOLDEN_TOUCH("Golden Touch", "at the Mage Training Arena's Alchemists' Playground.", 3328, 9600, 3391, 9663),
		GREATNESS("Greatness", "at the Champions' Guild.", 3136, 3328, 3199, 3391),
		GRIP_OF_THE_TALON("Grip Of The Talon", "during Shadow of the Storm.", -1, -1, -1, -1),
		GROTTO("Grotto", "at the nature spirit's grotto in Mort Myre Swamp.", 3392, 9728, 3455, 9791),
		GROUND_SCAPE("Ground Scape", "automatically.", -1, -1, -1, -1),
		GRUMPY("Grumpy", "at the swamp toad pond in the Feldip Hills.", 2560, 2944, 2623, 3007),
		HAM_AND_SEEK("Ham and Seek", "during Another Slice of H.A.M.", 2560, 2944, 2623, 3007),
		HARMONY("Harmony", "at Lumbridge Castle.", new int[][] {
			new int[] {3200}, new int[] {3199}, new int[] {3273}, new int[] {3302},
			new int[] {1856}, new int[] {5312}, new int[] {1874}, new int[] {5334}}),
		HARMONY2( "Harmony2", "in Lumbridge Castle's basement.", 3200, 9600, 3220, 9663),
		HAUNTED_MINE("Haunted Mine", "at the boss area of the Haunted Mine.", 2752, 4416, 2815, 4479),
		HAVE_A_BLAST("Have A Blast", "during Blast Furnace.", 1920, 4928, 1983, 4991),
		HEAD_TO_HEAD("Head To Head", "during the Evil Twin random event.", -1, -1, -1, -1),

		HEART_AND_MIND("Heart And Mind", "at the Body Altar.", 2496, 4800, 2559, 4863),

		HELLS_BELLS("Hell's Bells", "during the Troll Romance quest.", 2752, 3712, 2815, 3839),
		HERMIT("Hermit", "at the hermit's cave north-west of Burthorpe.", 2240, 4736, 2303, 4799),
		HIGH_SEAS("High Seas", "at Brimhaven.", 2752, 3136, 2815, 3199),
		HORIZON("Horizon", "in Taverley.", 2880, 3392, 2943, 3455),
		HYPNOTIZED("Hypnotized", "during Icthlarin's Little Helper.", -1, -1, -1, -1),
		IBAN("Iban", "at Iban's lair in the Underground Pass.", 2112, 4544, 2175, 4735),
		ICE_MELODY("Ice Melody", "at White Wolf Mountain.", 2816, 3456, 2879, 3519),
		IN_BETWEEN("In Between", "during Between a Rock...", 2496, 4928, 2623, 4991),
		IN_THE_BRINE("In The Brine", "at The Other Inn on Mos Le'Harmless.", 3648, 2944, 3711, 3007),
		IN_THE_CLINK("In The Clink", "during the Prison Pete random event.", 2048, 4416, 2111, 4479),
		IN_THE_MANOR("In The Manor", "at the ogre island, west of Yanille.", 2560, 3008, 2623, 3071),
		IN_THE_PITS("In The Pits", "in TzHaar.", 2432, 5120, 2495, 5183),
		INCANTATION("Incantation", "during Shadow of the Storm.", -1, -1, -1, -1),
		INSECT_QUEEN("Insect Queen", "at the Kalphite Queen.", 3456, 9472, 3583, 9535),
		INSPIRATION("Inspiration", "at the area north of the Black Knights' Fortress.", 3008, 3520, 3071, 3583),
		INTO_THE_ABYSS("Into The Abyss", "in abyssal space.", 3008, 4800, 3071, 4927),
		INTREPID("Intrepid", "in the Underground Pass caverns.", 2304, 9792, 2367, 9919),
		ISLAND_LIFE("Island Life", "at the southern part of Ape Atoll.", 2688, 2688, 2815, 2751),
		ISLE_OF_EVERYWHERE("Isle Of Everywhere", "on the east coast of Lunar Isle.", -1, -1, -1, -1),

		JOLLY_R("Jolly R", "at the north dock of Brimhaven.", 2752, 3200, 2815, 3263),
		JUNGLE_ISLAND("Jungle Island", "at Karamja", new int[][] {
				new int[] {2816}, new int[] {3136}, new int[] {2879}, new int[] {3199},
				new int[] {2816}, new int[] {2880}, new int[] {2879}, new int[] {2943}}),
		JUNGLE_TROUBLES("Jungle Troubles", "in the jungle of north-east Tai Bwo Wannai.", 2880, 3072, 2943, 3135),
		JUNGLY1("Jungly1", "at Cairn Isle.", new int[][] {
			new int[] {2752}, new int[] {2944}, new int[] {2815}, new int[] {3007},
			new int[] {2752}, new int[] {9344}, new int[] {2815}, new int[] {9407}}),
		JUNGLY2("Jungly2", "to the north-west of Brimhaven.", 2688, 3200, 2751, 3263),
		JUNGLY3("Jungly3", "at Tai Bwo Wannai Village.", 2752, 3008, 2815, 3071),

		KARAMJA_JAM("Karamja Jam", "at the dragon area of Brimhaven Dungeon.", 2688, 9408, 2751, 9535),
		KINGDOM("Kingdom", "on the way to Death Plateau.", 2816, 3520, 2879, 3583),
		KNIGHTLY("Knightly", "at Ardougne Castle.", 2560, 3264, 2623, 3327),

		LA_MORT("La Mort", "at the Death Altar.", 2176, 4800, 2239, 4863),
		LAIR("Lair", "at the shade catacombs below Mort'ton.", 3456, 9664, 3519, 9727),
		LAMENT_OF_MEIYERDITCH("Lament Of Meiyerditch", "during the Darkness of Hallowvale.", -1, -1, -1, -1),
		LAMENT("Lament", "during Enakhra's Lament.", 3072, 9280, 3135, 9343),
		LAND_DOWN_UNDER("Land Down Under", "during Royal Trouble.", -1, -1, -1, -1),
		LAND_OF_THE_DWARVES("Land Of The Dwarves", "on the west side of Keldagrim.", 2816, 10176, 2879, 10239),
		LANDLUBBER("Landlubber", "to the west of Brimhaven.", 2688, 3136, 2751, 3199),
		LAST_STAND("Last Stand", "during Swan Song.", -1, -1, -1, -1),
		LASTING("Lasting", "at Hemenster.", 2624, 3392, 2687, 3455),
		LEGEND("Legend", "to the south-east of Rellekka.", 2688, 3584, 2815, 3647),
		LEGION("Legion", "at the Barbarian Outpost.", new int[][] {
			new int[] {3008}, new int[] {3648}, new int[] {3071}, new int[] {3711},
			new int[] {2496}, new int[] {3520}, new int[] {2559}, new int[] {3583}}),
		LIFES_A_BEACH("Life's A Beach!", "at the coastal area of Mos Le'Harmless.", -1, -1, -1, -1),
		LIGHTHOUSE("Lighthouse", "at the Lighthouse.", 2496, 3584, 2559, 3647),
		LIGHTNESS("Lightness", "to the north of Edgeville.", 3136, 3520, 3199, 3583),
		LIGHTWALK("Lightwalk", "at Keep Le Faye.", 2752, 3392, 2815, 3455),
		LITTLE_CAVE_OF_HORRORS("Little Cave Of Horrors", "in the cave horror dungeon beneath Mos Le'Harmless.", -1, -1, -1, -1),
		LONESOME("Lonesome", "at the Desert Mining Camp.", 3264, 9408, 3327, 9471),
		LONG_AGO("Long Ago", "at Hazelmere's island East of Yanille", 2624, 3072, 2687, 3135),
		LONG_WAY_HOME("Long Way Home", "at Rimmington.", 2944, 3200, 3007, 3263),
		LOST_SOUL("Lost Soul", "at the Poison Waste in south Isafdar.", 2240, 3072, 2367, 3135),
		LULLABY("Lullaby", "at the area south-west of Rellekka.", new int[][] {
			new int[] {3328}, new int[] {3392}, new int[] {3391}, new int[] {3455},
			new int[] {2624}, new int[] {3520}, new int[] {2687}, new int[] {3583}}),

		MAD_EADGAR("Mad Eadgar", "at Eadgar's cave atop Trollheim.", 2880, 10048, 2943, 10111),
		MAGE_ARENA("Mage Arena", "at the Mage Arena.", new int[][] {
			new int[] {3072}, new int[] {3904}, new int[] {3135}, new int[] {3967},
			new int[] {2496}, new int[] {4672}, new int[] {2559}, new int[] {4735}}),
		MAGIC_DANCE("Magic Dance", "in the east of Yanille.", 2560, 3072, 2623, 3135),
		MAGICAL_JOURNEY("Magical Journey", "at the Sorcerer's Tower.", 2688, 3392, 2751, 3455),
		MAKING_WAVES("Making Waves", "during Swan Song.", new int[][] {
			new int[] {2304}, new int[] {3648}, new int[] {2367}, new int[] {3711},
			new int[] {2304}, new int[] {3584}, new int[] {2431}, new int[] {3647}}),
		MALADY("Malady", "at the Pit of Pestilence in the Stronghold of Security.", -1, -1, -1, -1),
		MARCH("March", "at King Lathas's Combat Training Camp.", 2496, 3328, 2559, 3391),
		MAROONED("Marooned", "on Crash Island during Monkey Madness.", new int[][] {
			new int[] {2880}, new int[] {2688}, new int[] {2943}, new int[] {2751},
			new int[] {3008}, new int[] {5440}, new int[] {3071}, new int[] {5503}}),
		MARZIPAN("Marzipan", "at the Trollheim cave.", new int[][] {
			new int[] {2752}, new int[] {10112}, new int[] {2815}, new int[] {10239},
			new int[] {2816}, new int[] {10048}, new int[] {2879}, new int[] {10111}}),
		MASQUERADE("Masquerade", "on the west side of the Fremennik Slayer Dungeon.", 2688, 9984, 2751, 10047),
		MASTERMINDLESS("Mastermindless", "during Recipe for Disaster.", 2560, 4608, 2623, 4671),
		MAUSOLEUM("Mausoleum", "under Paterdomus.", 3392, 9856, 3455, 9919),
		MEDDLING_KIDS("Meddling Kids", "during Royal Trouble.", -1, -1, -1, -1),
		MEDIEVAL("Medieval", "to the west of the Dig Site.", 3264, 3392, 3327, 3455),
		MELLOW("Mellow", "at the Fishing Guild.", 2560, 3392, 2623, 3455),
		MELODRAMA("Melodrama", "during Castle Wars.", 2432, 3072, 2495, 3135),
		MERIDIAN("Meridian", "at the area around Tyras Camp in Isafdar.", 2112, 3136, 2239, 3199),
		METHOD_OF_MADNESS("Method Of Madness", "at the confusion room of Tolna's rift area.", 3041, 5184, 3071, 5214),
		MILES_AWAY("Miles Away", "at the Crafting Guild.", new int[][] {
			new int[] {2880}, new int[] {3264}, new int[] {2943}, new int[] {3327},
			new int[] {2624}, new int[] {4672}, new int[] {2687}, new int[] {4735}}),
		MIND_OVER_MATTER("Mind Over Matter", "at the Mage Training Arena's Telekinetic Theatre.", 3328, 9664, 3391, 9727),
		MIRACLE_DANCE("Miracle Dance", "at the Mind Altar.", 2752, 4800, 2815, 4863),
		MIRAGE("Mirage", "during Icthlarin's Little Helper.", 3264, 9152, 3327, 9215),
		MISCELLANIA("Miscellania", "on Miscellania.", 2496, 3840, 2559, 3903),
		MONARCH_WALTZ("Monarch Waltz", "at the Sinclair Mansion.", 2688, 3520, 2751, 3583),
		MONKEY_MADNESS("Monkey Madness", "on Ape Atoll.", 2752, 2752, 2815, 2815),
		MONSTER_MELEE("Monster Melee", "at the H.A.M. cave.", 3136, 9600, 3199, 9663),
		MOODY("Moody", "at the entrance to the Underground Pass.", new int[][] {
			new int[] {3136}, new int[] {3584}, new int[] {3199}, new int[] {3647},
			new int[] {2368}, new int[] {3264}, new int[] {2495}, new int[] {3327}}),
		MORYTANIA("Morytania", "to the east of Paterdomus.", 3392, 3456, 3455, 3519),
		MUDSKIPPER_MELODY("Mudskipper Melody", "at Mudskipper Point.", 2944, 3072, 3007, 3135),
		NARNODES_THEME("Narnode's Theme", "", 2432, 9856, 2495, 9919),
		NATURAL("Natural", "at the nature spirit's island in Mort Myre Swamp.", new int[][] {
			new int[] {3392}, new int[] {3328}, new int[] {3455}, new int[] {3391},
			new int[] {2240}, new int[] {4992}, new int[] {2303}, new int[] {5055}}),
		NEVERLAND("Neverland", "to the south of Tree Gnome Village.", 2432, 3328, 2495, 3391),
		NEWBIE_MELODY("Newbie Melody", "automatically.", new int[][] {
			new int[] {3008}, new int[] {3008}, new int[] {3135}, new int[] {3135},
			new int[] {3136}, new int[] {3072}, new int[] {3199}, new int[] {3135}}),
		NIGHT_OF_THE_VAMPYRE("Night of the Vampyre", "during the Darkness of Hallowvale.", -1, -1, -1, -1),
		NIGHTFALL("Nightfall", "at the north of Rimmington.", new int[][] {
			new int[] {3200}, new int[] {3904}, new int[] {3263}, new int[] {3967},
			new int[] {2944}, new int[] {3264}, new int[] {3007}, new int[] {3327}}),
		NIGHTWALK("Nightwalk", "", -1, -1, -1, -1),
		NO_WAY_OUT("No Way Out", "at the hopelessness room of Tolna's rift area.", new int[][] {
			new int[] {3264}, new int[] {9792}, new int[] {3327}, new int[] {9855},
			new int[] {3072}, new int[] {5184}, new int[] {3135}, new int[] {5247},
			new int[] {3008}, new int[] {5184}, new int[] {3040}, new int[] {5214}}),
		NOMAD("Nomad", "to the east of the Bedabin Camp.", 2752, 3072, 2815, 3135),
		NULL_AND_VOID("Null And Void", "at the Pest Control landing area.", 2624, 2624, 2687, 2687),
		ON_THE_WING( "On The Wing", "at the Falconer Area, north-east of Eagles' Peak.", -1, -1, -1, -1),
		ORIENTAL("Oriental", "in the Waterfall Dungeon, East of Shilo Village.", 2880, 9344, 2943, 9407),
		OUT_OF_THE_DEEP("Out Of The Deep", "at the dagannoth cave under the Lighthouse.", 2496, 9984, 2559, 10047),
		OVER_TO_NARDAH("Over To Nardah", "at Nardah.", 3392, 2880, 3455, 2943),
		OVERPASS("Overpass", "at Arandar, east of Tirannwn.", 2304, 3264, 2367, 3327),
		OVERTURE("Overture", "at Seers' Village.", 2688, 3456, 2751, 3519),
		PARADE("Parade", "at the Jolly Boar Inn.", 3264, 3456, 3391, 3519),
		PATH_OF_PERIL("Path Of Peril", "in Damis's Shadow Dungeon.", 2624, 5056, 2751, 5119),
		PATHWAYS("Pathways", "at Brimhaven Dungeon's entrance.", 2688, 9536, 2751, 9599),
		PEST_CONTROL("Pest Control", "during Pest Control.", 2624, 2560, 2687, 2623),
		PHAROAHS_TOMB("Pharaoh's Tomb", "at the Agility Pyramid.", 3648, 9856, 3711, 9919),
		PHASMATYS("Phasmatys", "at the pool of ectoplasm underneath the Ectofuntus.", 3648, 9856, 3711, 9919),
		PHEASANT_PEASANT("Pheasant Peasant", "during the Freaky Forester random event.", 2560, 4736, 2623, 4800),
		PINBALL_WIZARD("Pinball Wizard", "during the Pinball random event.", -1, -1, -1, -1),
		PRINCIPALITY("Principality", "at Burthorpe's training ground.", 2880, 3520, 2943, 3583),
		PIRATES_OF_PERIL("Pirates Of Peril", "at the Pirates' Hideout in the Wilderness.", 3008, 3904, 3071, 3967),

		QUEST("Quest", "at the Fire Altar.", 2560, 4800, 2623, 4863),

		RAT_A_TAT_TAT("Rat A Tat Tat", "at the Varrock Rat Pits.", 2880, 5056, 2943, 5119),
		RAT_HUNT("Rat Hunt", "during Rat Catchers.", 2816, 5056, 2879, 5119),
		READY_FOR_BATTLE("Ready For Battle", "during Castle Wars.", 2368, 9472, 2431, 9535),
		REGAL("Regal", "at the Rogues' Castle.", 3264, 3904, 3327, 3967),
		REGGAE("Reggae", "to the south-east of the Kharazi Jungle.", 2880, 2880, 3007, 2943),
		REGGAE2("Reggae2", "to the east of Karamja Jungle.", 2880, 3008, 2943, 3071),
		RELLEKKA("Rellekka", "at Rellekka.", 2560, 3648, 2687, 3775),
		RIGHT_ON_TRACK("Right On Track", "during Forgettable Tale...", -1, -1, -1, -1),
		RIGHTEOUSNESS("Righteousness", "at the Law Altar.", 2432, 4800, 2495, 4863),
		RIVERSIDE("Riverside", "to the west of Tyras Camp.", new int[][] {
			new int[] {2688}, new int[] {3264}, new int[] {2751}, new int[] {3327},
			new int[] {2112}, new int[] {3072}, new int[] {2175}, new int[] {3135}}),
		ROC_AND_ROLL("Roc And Roll", "during My Arm's Big Adventure.", -1, -1, -1, -1),
		ROLL_THE_BONES("Roll The Bones", "at the Mage Training Arena's Creature Graveyard.", 3328, 9600, 3391, 9663),
		ROMANCING_THE_CRONE("Romancing The Crone", "during the Troll Romance quest.", 2752, 3840, 2815, 3903),
		ROMPER_CHOMPER("Romper Chomper", "at the ogre area south of Castle Wars.", 2304, 3008, 2431, 3071),
		ROYALE("Royale", "at the Black Knight base in Taverley Dungeon.", 2880, 9664, 2943, 9727),
		RUNE_ESSENCE("Rune Essence", "at the rune essence mine.", 2880, 4800, 2943, 4863),

		SAD_MEADOW("Sad Meadow", "at West Ardougne.", new int[][] {
			new int[] {2496}, new int[] {3264}, new int[] {2559}, new int[] {3327},
			new int[] {2752}, new int[] {4672}, new int[] {2815}, new int[] {4735}}),
		SAGA("Saga", "to the south of Rellekka.", 2560, 3584, 2687, 3647),
		SARCOPHAGUS("Sarcophagus", "inside Jaldraocht, the pyramid visited in Desert Treasure.", 3200, 9280, 3263, 9343),
		SARIMS_VERMIN("Sarim's Vermin", "at Port Sarim Rat Pits.", 2944, 9600, 3007, 9663),
		SCAPE_CAVE("Scape Cave", "in Varrock Sewers.", new int[][] {
			new int[] {3136}, new int[] {9856}, new int[] {3327}, new int[] {9919},
			new int[] {3072}, new int[] {9572}, new int[] {3135}, new int[] {9535}}),
		SCAPE_MAIN("Scape Main", "automatically.", -1, -1, -1, -1),
		SCAPE_ORIGINAL("Scape Original", "automatically.", -1, -1, -1, -1),
		SCAPE_SAD("Scape Sad", "at the Demonic Ruins in the Wilderness.", 3264, 3840, 3391, 3903),
		SCAPE_SANTA("Scape Santa", "automatically.", -1, -1, -1, -1),
		SCAPE_SCARED("Scape Scared", "automatically.", -1, -1, -1, -1),
		SCAPE_SOFT("Scape Soft", "to the north of Falador.", 2944, 3392, 3007, 3455),
		SCAPE_WILD("Scape Wild", "in the Wilderness.", new int[][] {
			new int[] {3200}, new int[] {3648}, new int[] {3263}, new int[] {3711},
			new int[] {3136}, new int[] {3840}, new int[] {3263}, new int[] {3903}}),
		SCARAB("Scarab", "at Jaldraocht, the pyramid visited in Desert Treasure.", 3136, 2880, 3327, 2943),
		SEA_SHANTY2("Sea Shanty2", "at Port Sarim.", 3008, 3200, 3071, 3263),
		SEA_SHANTY("Sea Shanty", "at Karamja port.", 2880, 3136, 2943, 3199),
		SERENADE("Serenade", "at the Observatory.", 2368, 3136, 2495, 3199),
		SERENE("Serene", "at the Air Altar.", new int[][] {
			new int[] {2944}, new int[] {3904}, new int[] {3007}, new int[] {3967},
			new int[] {2944}, new int[] {10303}, new int[] {3007}, new int[] {10367},
			new int[] {2816}, new int[] {4800}, new int[] {2879}, new int[] {4863}}),
		SETTLEMENT("Settlement", "at the Mountain Camp.", 2752, 3648, 2815, 3711),
		SHADOWLAND("Shadowland", "to the east of Mort'ton.", new int[][] {
			new int[] {3420}, new int[] {3200}, new int[] {3455}, new int[] {3263},
			new int[] {3456}, new int[] {3264}, new int[] {3519}, new int[] {3327},
			new int[] {2112}, new int[] {4992}, new int[] {2175}, new int[] {5055}}),
		SHINE("Shine", "at the Duel Arena hospital.", 3328, 3264, 3391, 3327),
		SHINING("Shining", "in the Wilderness north of the Graveyard.", 3200, 3712, 3263, 3775),
		SHIPWRECKED("Shipwrecked", "at the shipwreck on the north coast of Morytania.", 3584, 3520, 3647, 3583),
		SHOWDOWN("Showdown", "during the boss fight in Monkey Madness.", 2688, 9152, 2751, 9215),
		SIGMUNDS_SHOWDOWN("Sigmund's Showdown", "during Death to the Dorgeshuun.", -1, -1, -1, -1),
		SOJOURN("Sojourn", "at the troll champion arena.", 2816, 3648, 2943, 3711),
		SOUNDSCAPE("Soundscape", "at the Feldip Hills glider area.", 2432, 2944, 2559, 3007),
		SPHINX("Sphinx", "to the north of Sophanem.", 3264, 2816, 3327, 2879),
		SPIRIT("Spirit", "at the Cooks' Guild.", 3136, 3392, 3199, 3455),
		SPIRITS_OF_ELID("Spirits Of Elid", "during the Spirits of Elid.", 3328, 9536, 3391, 9599),
		SPLENDOUR("Splendour", "at the Heroes' Guild.", 2880, 3456, 2943, 3519),
		SPOOKY_JUNGLE("Spooky Jungle", "", new int[][] {
				new int[] {2752}, new int[] {2880}, new int[] {2815}, new int[] {2943},
				new int[] {2880}, new int[] {9472}, new int[] {2943}, new int[] {9535}}),
		SPOOKY("Spooky", "at Draynor Manor.", new int[][] {
			new int[] {3072}, new int[] {3328}, new int[] {3135}, new int[] {3391},
			new int[] {1920}, new int[] {4992}, new int[] {1952}, new int[] {5055}}),
		SPOOKY2("Spooky2", "at the entrance to the Haunted Mine.", 3392, 9600, 3519, 9663),
		STAGNANT("Stagnant", "at the Hollows in the Mort Myre Swamp.", new int[][] {
			new int[] {3456}, new int[] {3328}, new int[] {3519}, new int[] {3391},
			new int[] {2176}, new int[] {4992}, new int[] {2239}, new int[] {5055}}),
		STARLIGHT("Starlight", "at the Asgarnian Ice Dungeon north of Mudskipper Point.", new int[][] {
			new int[] {2944}, new int[] {9536}, new int[] {3020}, new int[] {9599},
			new int[] {3221}, new int[] {9560}, new int[] {3071}, new int[] {9599}}),
		START("Start", "at Draynor Village.", 3072, 3264, 3135, 3327),
		STILL_NIGHT("Still Night", "at the Mining area south-east of Varrock.", 3264, 3328, 3327, 3391),
		STILLNESS("Stillness", "at the Myreque area under Mort Myre.", 3456, 9792, 3519, 9855),
		STORM_BREW("Storm Brew", "at the killerwatt plane.", 2624, 5184, 2687, 5247),
		STRANDED("Stranded", "at the gate to the Ice Path, north of Trollheim.", 2816, 3712, 2943, 3775),
		STRANGE_PLACE("Strange Place", "during A Tail of Two Cats.", -1, -1, -1, -1),
		STRATOSPHERE("Stratosphere", "at the Cosmic Altar.", 2112, 4800, 2175, 4863),
		SUBTERRANEA("Subterranea", "in Waterbirth Island Dungeon.", 2496, 10112, 2560, 10175),
		SUNBURN("Sunburn", "in the desert north of Jaldraocht Pyramid.", new int[][] {
			new int[] {3200}, new int[] {2944}, new int[] {3263}, new int[] {3007},
			new int[] {3328}, new int[] {2880}, new int[] {3391}, new int[] {2943}}),
		SUPERSTITION("Superstition", "at the cave below the Kharazi Jungle.", 2752, 9280, 2815, 9343),
		SUSPICIOUS("Suspicious", "during Monkey Madness.", 2560, 4544, 2687, 4607),
		TALE_OF_KELDAGRIM("Tale Of Keldagrim", "on the east side of Keldagrim.", 2880, 10112, 2943, 10239),
		TALKING_FOREST("Talking Forest", "at McGrubor's Wood.", 2624, 3456, 2687, 3519),
		TEARS_OF_GUTHIX("Tears Of Guthix", "during Tears of Guthix.", 3200, 9472, 3263, 9535),
		TECHNOLOGY("Technology", "at the gnome glider hangar.", 2560, 4480, 2687, 4543),
		TEMPLE_OF_LIGHT("Temple Of Light", "during Mourning's Ends Part II.", 1856, 4608, 1919, 4671),
		TEMPLE("Temple", "on Ape Atoll.", 2752, 9152, 2815, 9215),
		THE_CELLAR_DWELLARS("The Cellar Dwellers", "was unlocked during Hazeel Cult.", 2496, 9664, 2623, 9727),
		THE_CHOSEN("The Chosen", "during the Recruitment Drive quest.", 2432, 4928, 2495, 4991),
		THE_DESERT("The Desert", "north of Bedabin Camp.", 3136, 3008, 3263, 3071),
		THE_DESOLATE_ISLE("The Desolate Isle", "is unlocked in Waterbirth Island.", 2496, 3712, 2559, 3775),
		THE_ENCHANTER("The Enchanter", "in the Mage Training Arena.", 3328, 9600, 3391, 9663),
		THE_FAR_SIDE("The Far Side", "in the Rogue's Den.", 3008, 5056, 3071, 5119),
		BLANK("", "", -1, -1, -1, -1),
		THE_GALLEON("The Galleon", "during Lunar Diplomacy.", -1, -1, -1, -1),
		THE_GENIE("The Genie", "at the genie's cave west of Nardah.", 3328, 9280, 3391, 9343),
		THE_GOLEM("The Golem", "near Uzer.", new int[][] {
				new int[] {3392}, new int[] {3072}, new int[] {3455}, new int[] {3135},
				new int[] {3456}, new int[] {3072}, new int[] {3519}, new int[] {3160}}),
		THE_LAST_SHANTY("The Last Shanty", "during the Darkness of Hallowvale.", -1, -1, -1, -1),
		THE_LOST_MELODY("The Lost Melody", "at the Dorgesh-Kaan mine.", 3264, 9600, 3327, 9663),
		THE_LOST_TRIBE("The Lost Tribe", "at the goblin mines under Lumbridge.", 3221, 9600, 3327, 9663),
		THE_LUNAR_ISLE("The Lunar Isle", "at Lunar Isle.", -1, -1, -1, -1),
		THE_MAD_MOLE("The Mad Mole", "at Falador Mole Lair.", 1728, 5120, 1791, 5247),
		THE_MONSTERS_BELOW("The Monsters Below", "in Waterbirth Island Dungeon.", 2432, 10112, 2495, 10175),
		THE_NAVIGATOR("The Navigator", "during Fremennik Trials.", 2624, 9984, 2687, 10047),
		THE_NOBLE_RODENT("The Noble Rodent", "during Rat Catchers.", -1, -1, -1, -1),
		THE_OTHER_SIDE("The Other Side", "at Port Phasmatys.", 3648, 3456, 3711, 3583),
		THE_POWER_OF_TEARS("The Power of Tears", "during the Tears of Guthix quest.", -1, -1, -1, -1),
		THE_QUIZMASTER("The Quiz Master", "", 1920, 4736, 1983, 4799),
		THE_ROGUES_DEN("The Rogues' Den", "at the Rogues' Den.", new int[][] {
				new int[] {2944}, new int[] {4928}, new int[] {3007}, new int[] {5119},
				new int[] {3008}, new int[] {4928}, new int[] {3071}, new int[] {5055}}),
		THE_SHADOW("The Shadow", "on Crandor.", 2816, 3200, 2879, 3327),
		THE_TERRIBLE_TOWER("The Terrible Tower", "at the Slayer Tower.", 3392, 3520, 3455, 3583),
		THE_TOWER("The Tower", "north of Ardougne.", new int[][] {
				new int[] {2560}, new int[] {3328}, new int[] {2623}, new int[] {3391},
				new int[] {2496}, new int[] {9728}, new int[] {2623}, new int[] {9791}}),
		THEME("Theme", "at the Coal Trucks.", 2560, 3456, 2623, 3519),
		THRONE_OF_THE_DEMON("Throne Of The Demon", "during Shadow of the Storm.", 2688, 4864, 2751, 4927),
		TIME_OUT("Time Out", "during the Maze random event.", 2880, 4544, 2943, 4607),
		TIME_TO_MINE("Time To Mine", "during Between a Rock...", 2816, 10112, 2879, 10175),
		TIPTOE("Tiptoe", "at Draynor Manor's cellar.", 3072, 9728, 3135, 9791),
		TITLE_FIGHT("Title Fight", "at the Champion's Challenge underground area.", new int[][] {
			new int[] {3182}, new int[] {9728}, new int[] {3199}, new int[] {9791},
			new int[] {3136}, new int[] {9728}, new int[] {3157}, new int[] {9791},
			new int[] {3158}, new int[] {9728}, new int[] {3181}, new int[] {9751},
			new int[] {3158}, new int[] {9765}, new int[] {3181}, new int[] {9791}}),
		TOMB_RAIDER("Tomb Raider", "during Pyramid Plunder.", -1, -1, -1, -1),
		TOMORROW("Tomorrow", "at the coastline south of Port Sarim.", 3008, 3136, 3071, 3199),
		TOO_MANY_COOKS("Too Many Cooks...", "during Recipe for Disaster.", 2944, 9856, 3007, 9919),
		TRAWLER_MINOR("Trawler Minor", "during Fishing Trawler.", 1920, 4800, 2047, 4863),
		TRAWLER("Trawler", "during Fishing Trawler.", 1856, 4800, 1919, 4863),
		TREE_SPIRITS("Tree Spirits", "to the west of the Tree Gnome Stronghold.", 2304, 3328, 2431, 3391),
		TREMBLE("Tremble", "during Death Plateau.", 2816, 3584, 2879, 3647),
		TRIBAL2("Tribal2", "at the gnome glider on Karamja.", 2880, 2944, 3007, 3007),
		TRIBAL_BACKGROUND("Tribal Background", "in the south-east Kharazi Jungle.", new int[][] {
				new int[] {2816}, new int[] {3072}, new int[] {2879}, new int[] {3135},
				new int[] {2816}, new int[] {9472}, new int[] {2879}, new int[] {9535}}),
		TRIBAL("Tribal", "to the east of Tai Bwo Wannai Village.", 2816, 3008, 2879, 3071),
		TRINITY("Trinity", "at the Legends' Guild.", new int[][] {
			new int[] {2688}, new int[] {3328}, new int[] {2751}, new int[] {3391},
			new int[] {2688}, new int[] {9728}, new int[] {2751}, new int[] {9791}}),
		TROUBLE_BREWING("Trouble Brewing", "during Trouble Brewing.", -1, -1, -1, -1),
		TROUBLED("Troubled", "to the west of the Bandit Camp in the Wilderness.", 2944, 3648, 3007, 3711),
		TWILIGHT("Twilight", "during Elemental Workshop.", 2688, 9856, 2751, 9919),
		TZHAAR("Tzhaar!", "in the TzHaar Fight Caves.", 2368, 5056, 2431, 5119),
		UNDERCURRENT("Undercurrent", "in the Wilderness south-west of Daemonheim.", 3072, 3648, 3135, 3711),
		UNDERGROUND("Underground", "at the dramen tree in Entrana Dungeon.", new int[][] {
			new int[] {3328}, new int[] {3584}, new int[] {3391}, new int[] {3647},
			new int[] {2816}, new int[] {9728}, new int[] {2879}, new int[] {9791}}),
		UNDERGROUND_PASS("Underground Pass", "during Underground Pass.", 2368, 9536, 2431, 9663),
		UNDERSTANDING("Understanding", "at the Nature Altar.", 2368, 4800, 2431, 4863),
		UNKNOWN_LAND("Unknown Land", "at Draynor Market.", 3072, 3200, 3135, 3263),
		UPCOMING("Upcoming", "at the tower of the Necromancer.", 2624, 3200, 2687, 3263),

		VENTURE("Venture", "at the Dig Site.", 3328, 3328, 3391, 3391),
		VENTURE2("Venture2", "during Dig Site.", 3328, 9728, 3391, 9855),
		VICTORY_IS_MINE("Victory Is Mine", "at the Champion's Challenge arena.", 3158, 9755, 3181, 9761),
		VILLAGE("Village", "at Canifis in Morytania.", 3456, 3456, 3519, 3519),
		VISION("Vision", "at the Wizards' Tower.", new int[][] {
			new int[] {3072}, new int[] {3136}, new int[] {3135}, new int[] {3199},
			new int[] {3072}, new int[] {9535}, new int[] {3135}, new int[] {9603}}),
		VOODOO_CULT("Voodoo Cult", "at the cave under the Kharazi Jungle.", new int[][] {
			new int[] {2368}, new int[] {4672}, new int[] {2431}, new int[] {4735},
			new int[] {2880}, new int[] {9280}, new int[] {2943}, new int[] {9343}}),
		VOYAGE("Voyage", "at the top of Baxtorian Falls.", 2496, 3456, 2559, 3519),

		WAKING_DREAM("Waking Dream", "during your dream in Lunar Diplomacy.", -1, -1, -1, -1),
		WANDER("Wander", "at the farm area south of Falador.", 3008, 3264, 3071, 3327),
		WARRIOR("Warrior", "during Fremennik Trials.", 2624, 10048, 2687, 10111),
		WARRIORS_GUILD("Warrior's Guild", "in the Warriors' Guild.", 2838, 3534, 2875, 3555),
		WATERFALL("Waterfall", "at Baxtorian Falls.", new int[][] {
			new int[] {2496}, new int[] {3391}, new int[] {2559}, new int[] {3455},
			new int[] {2496}, new int[] {9792}, new int[] {2559}, new int[] {9855}}),
		WATERLOGGED("Waterlogged", "to the south of Canifis.", new int[][] {
			new int[] {3456}, new int[] {3392}, new int[] {3583}, new int[] {3455},
			new int[] {1984}, new int[] {4992}, new int[] {2111}, new int[] {5055}}),
		WAY_OF_THE_ENCHANTER("Way Of The Enchanter", "in the dungeon beneath Lunar Isle.", -1, -1, -1, -1),
		WAYWARD("Wayward", "at the zogre Dungeon.", 2432, 9408, 2495, 9471),
		WE_ARE_THE_FAIRIES("We Are The Fairies", "on the cosmic character plane during Fairy Tale Part II.", -1, -1, -1, -1),
		WELL_OF_VOYAGE("Well Of Voyage", "in the passage between Iban's Temple and Isafdar.", 2304, 9600, 2367, 9663),
		WILD_SIDE("Wild Side", "to the north of the Lava Maze in the Wilderness.", 3008, 3840, 3135, 3903),
		WILDERNESS("Wilderness I", "in the Wilderness.", new int[][] {
			new int[] {2944}, new int[] {3584}, new int[] {3007}, new int[] {3647},
			new int[] {3072}, new int[] {3712}, new int[] {3135}, new int[] {3775}}),
		WILDERNESS2("Wilderness II", "to the south of the Lava Maze in the Wilderness.", 3008, 3776, 3135, 3839),
		WILDERNESS3("Wilderness III", "at the Forgotten Cemetery in the Wilderness.", 2944, 3712, 3007, 3775),
		WILDWOOD("Wildwood", "to the west of Stealing Creation in the Wilderness.", 3072, 3584, 3135, 3647),
		WITCHING("Witching", "on the east side of the Wilderness.", 3264, 3712, 3391, 3775),
		WOE_OF_THE_WYVERN("Woe Of The Wyvern", "at the Asgarnian Ice Dungeon's wyvern area.", 3021, 9536, 3071, 9559),
		WONDER("Wonder", "to the north-west of the Black Knights' Fortress.", 2944, 3520, 3007, 3583),
		WONDEROUS("Wonderous", "near the Legends' Guild.", 2624, 3328, 2687, 3391),
		WOODLAND("Woodland", "at the Elf Camp in Tirannwn.", 2112, 3200, 2239, 3263),
		WORKSHOP("Workshop", "at the Mining Guild in Falador.", 3008, 3328, 3071, 3391),
		WRATH_AND_RUIN("Wrath And Ruin", "at the anger room of Tolna's rift.",new int[][] {
				new int[] {3008}, new int[] {5215}, new int[] {3040}, new int[] {5247},
				new int[] {2944}, new int[] {5284}, new int[] {3007}, new int[] {5247}}),
		XENOPHOBE("Xenophobe", "in Waterbirth Island Dungeon.", new int[][] {
			new int[] {1856}, new int[] {4352}, new int[] {1919}, new int[] {4415},
			new int[] {2880}, new int[] {4416}, new int[] {2943}, new int[] {4479}}),
		YESTERYEAR("Yesteryear", "to the east of Lumbridge Swamp.", 3200, 3136, 3263, 3199),
		ZEALOT("Zealot", "at the Water Altar.", 2688, 4800, 2751, 4863),
		ZOGRE_DANCE("Zogre Dance", "at the zogre area.", 2432, 3008, 2495, 3071);
		//502 - scat
		//503 - x-file
		//504 - pulp fict
		//505 - miss impos

		private String name, hint;
		private int[][] location;
		Songs(String name, String hint, int pos1, int pos2, int pos3, int pos4) {
			this.name = name;
			this.hint = hint;
			this.location = new int[][] {new int[] {pos1}, new int[] {pos2}, new int[] {pos3}, new int[] {pos4}};
		}
		
		Songs(String name, String hint, int[][] location) {
			this.name = name;
			this.hint = hint;
			this.location = location;
		}
		
		public int getId() {
			return ordinal() + 1;
		}

		public String getName() {
			return name;
		}
		
		public String getHint() {
			return hint;
		}
		
		public int[][] getLocation() {
			return location;
		}

		public boolean locationIsEmpty() {
			return location[0][0] == -1 && location[1][0] == -1 && location[2][0] == -1 && location[3][0] == -1;
		}
		
		public static Songs get(int index) {
			for(Songs s : values()) {
				if(s.ordinal() == index)
					return s;
			}
			return null;
		}
	}

	public static boolean handleButton(Player player, int interfaceId) {
		if(interfaceId >= interfaceStartId && interfaceId <= interfaceStartId + Songs.values().length-1) {
			int songId = interfaceId - interfaceStartId;
			Songs song = Songs.get(songId);
			/*if(player.hasSongUnlocked(songId)) {
				//player.getPacketSender().sendString(nameInterfaceId, song.getName());
				player.getPacketSender().sendSongList(song.getScrollId());
			} else {*/
				player.message("This track can be unlocked " + song.getHint());
			//}
			return true;
		}
		return false;
	}
	
	public static void refreshUnlocked(Player player) {
		int unlocked = 0;
		List<Boolean> songs = player.getSongs();
		for(int i = 0; i < songs.size(); i++) {
			if(songs.get(i) == true)
				unlocked++;
		}
		if(unlocked >= 150)
			Achievements.finishAchievement(player, Achievements.AchievementData.UNLOCK_150_SONGS);
		player.getPacketSender().sendString(60006, unlocked + " / " + songs.size());
	}
	
	public static void createList(Player player) {
		for(Songs s : Songs.values()) {
			player.addSong(s.locationIsEmpty());
		}
	}

	public static void login(Player player) {
		List<Boolean> songs = player.getSongs();
		if(songs.size() != 0) {
			player.getPacketSender().sendSongList(player.getSongs(), player.getPlaylist());
		}
		Songs song = Songs.get(getAreaID(player));
		if(song != null && player.musicActive())
			player.getPacketSender().sendSong(song.getId());
		refreshUnlocked(player);
	}
	
	public static int getAreaID(Player player) {
		final Position location = player.copyPosition();
		Songs[] songs = Songs.values();
		for(Songs song : songs) {
			if(song.locationIsEmpty())
				continue;
			int[][] loc = song.getLocation();
			for(int i = 0; i < loc[0].length; i++) {
				if (location.getX() >= loc[0][i] && location.getX() <= loc[2][i] && location.getY() >= loc[1][i] && location.getY() <= loc[3][i])
					return song.ordinal();
			}
		}
		return -1;
	}

	public static void handleRegionChange(Player player) {
		Songs song = Songs.get(getAreaID(player));
		if(song != null) {
			if(!player.hasSongUnlocked(song.ordinal())) {
				player.message("You have unlocked a new music track: " + song.getName(), 0xff0000);
				player.setSong(song.ordinal(), true);
				refreshUnlocked(player);
                player.getPacketSender().sendSongList(player.getSongs());
			}

            if(player.musicActive())
                player.getPacketSender().sendSong(song.getId());
		}
	}
}
