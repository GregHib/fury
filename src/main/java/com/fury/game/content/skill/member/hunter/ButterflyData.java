package com.fury.game.content.skill.member.hunter;

public enum ButterflyData {
	RUBY_HARVEST("Ruby Harvest", 10020, 10, 1, 5085),
	SAPPHIRE_GLACIALIS("Sapphire Glacialis", 10018, 25, 25, 5084),
	SNOWY_KNIGHT("Snowy Knight", 10016, 75, 50, 5083),
	BLACK_WARLOCK("Black Warlock", 10014, 250, 75, 5082);

	/**
	 * Variables.
	 */
	public String name;
	public int butterflyJar, XPReward, levelReq, npcId;

	/**
	 * Creating the Butterfly.
	 * @param name
	 * @param JarAdded
	 * @param XPAdded
	 * @param LevelNeed
	 * @param Npc
	 */
	ButterflyData(String name, int JarAdded, int XPAdded, int LevelNeed, int Npc) {
		this.name = name;
		this.butterflyJar = JarAdded;
		this.XPReward = XPAdded;
		this.levelReq = LevelNeed;
		this.npcId = Npc;
	}
	
	public static ButterflyData forId(int npcId) {
		for(ButterflyData butterfly : ButterflyData.values()) {
			if(butterfly.npcId == npcId) {
				return butterfly;
			}
		}
		return null;
	}
}
