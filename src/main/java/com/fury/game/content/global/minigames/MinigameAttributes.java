package com.fury.game.content.global.minigames;

/**
 * Holds different minigame attributes for a player
 * @author Gabriel Hannason
 */
public class MinigameAttributes {

	private final BarrowsMinigameAttributes barrowsMinigameAttributes = new BarrowsMinigameAttributes();
	private final WarriorsGuildAttributes warriorsGuildAttributes = new WarriorsGuildAttributes();
	private final RecipeForDisasterAttributes rfdAttributes = new RecipeForDisasterAttributes();
	private final AllFiredUpAttributes allFiredUpAttributes = new AllFiredUpAttributes();

	public class AllFiredUpAttributes {

		public final int[] acceptableLogs = {
				1511, 1513, 1515, 1517, 1519, 1521
		};

		public boolean allBeaconsLit = false;

		public int[][] beaconData = {//beaconID, beaconState, var Container ID, first shift, second shift
				{0, 0, 1283, 0, 1},//38448
				{1, 0, 1283, 3, 4},//38449
				{2, 0, 1283, 6, 7},//38450
				{3, 0, 1283, 9, 10},//38451
				{4, 0, 1283, 12, 13},//38452
				{5, 0, 1283, 15, 16},//38453
				{6, 0, 1283, 18, 19},//38454
				{7, 0, 1283, 21, 22},//38455
				{8, 0, 1283, 24, 25},//38456
				{9, 0, 1288, 0, 1},//38457
				{10, 0, 1288, 3, 4},//38458
				{11, 0, 1288, 6, 7},//38459
				{12, 0, 1288, 9, 10},//38460
				{13, 0, 1288, 12, 13},//38461
		};

		public boolean started = false;
		public  double timer = 540.00;
		public  int beaconsLeft = beaconData.length;

	}

	public class WarriorsGuildAttributes {

		private boolean hasSpawnedArmour;
		private boolean enteredTokenRoom;

		public boolean hasSpawnedArmour() {
			return hasSpawnedArmour;
		}

		public void setSpawnedArmour(boolean hasSpawnedArmour) {
			this.hasSpawnedArmour = hasSpawnedArmour;
		}

		public boolean enteredTokenRoom() {
			return enteredTokenRoom;
		}

		public void setEnteredTokenRoom(boolean enteredTokenRoom) {
			this.enteredTokenRoom = enteredTokenRoom;
		}

	}

	public class BarrowsMinigameAttributes {

		private int killcount, randomCoffin, riddleAnswer = -1;

		public int getKillcount() {
			return killcount;
		}

		public void setKillcount(int killcount) {
			this.killcount = killcount;
		}

		public int getRandomCoffin() {
			return randomCoffin;
		}

		public void setRandomCoffin(int randomCoffin) {
			this.randomCoffin = randomCoffin;
		}

		public int getRiddleAnswer() {
			return riddleAnswer;
		}

		public void setRiddleAnswer(int riddleAnswer) {
			this.riddleAnswer = riddleAnswer;
		}

		private int[][] barrowsData = { //NPCID, state
				{ 2030, 0}, // verac
				{ 2029, 0 }, // toarg
				{ 2028, 0 }, // karil
				{ 2027, 0 }, // guthan
				{ 2026, 0 }, // dharok
				{ 2025, 0 } // ahrim
		};

		public int[][] getBarrowsData() {
			return barrowsData;
		}

		public void setBarrowsData(int[][] barrowsData) {
			this.barrowsData = barrowsData;
		}
	}

	public class RecipeForDisasterAttributes {
		private int wavesCompleted;
		private boolean[] questParts = new boolean[9];

		public int getWavesCompleted() {
			return wavesCompleted;
		}

		public void setWavesCompleted(int wavesCompleted) {
			this.wavesCompleted = wavesCompleted;
		}

		public boolean hasFinishedPart(int index) {
			return questParts[index];
		}

		public void setPartFinished(int index, boolean finished) {
			questParts[index] = finished;
		}

		public boolean[] getQuestParts() {
			return questParts;
		}

		public void setQuestParts(boolean[] questParts) {
			this.questParts = questParts;
		}

		public void reset() {
			questParts = new boolean[9];
			wavesCompleted = 0;
		}
	}

	public BarrowsMinigameAttributes getBarrowsMinigameAttributes() {
		return barrowsMinigameAttributes;
	}

	public WarriorsGuildAttributes getWarriorsGuildAttributes() {
		return warriorsGuildAttributes;
	}

	public RecipeForDisasterAttributes getRecipeForDisasterAttributes() {
		return rfdAttributes;
	}

	public AllFiredUpAttributes getAllFiredUpAttributes() { return allFiredUpAttributes; }

	public MinigameAttributes() {}

}
