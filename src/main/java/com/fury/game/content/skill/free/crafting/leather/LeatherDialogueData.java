package com.fury.game.content.skill.free.crafting.leather;

public enum LeatherDialogueData {
	
	GREEN_LEATHER(1745, 1065, 1099, 1135),
	BLUE_LEATHER(2505, 2487, 2493, 2499),
	RED_LEATHER(2507, 2489, 2495, 2501),
	BLACK_LEATHER(2509, 2491, 2497, 2503),
	ROYAL_HIDE(2509, 2491, 2497, 2503),
	FUNGAL(22449, 22458, 22462, 22466),
	GRIFOLIC(22450, 22470, 22474, 22478),
	GANODERMIC(22451, 22482, 22486, 22490);

	private int leather, vambraces, chaps, body, coif;
	
	LeatherDialogueData(final int leather, final int vambraces, final int chaps, final int body, final int coif) {
		this.leather = leather;
		this.vambraces = vambraces;
		this.chaps = chaps;
		this.body = body;
		this.coif = coif;
	}
	LeatherDialogueData(final int leather, final int vambraces, final int chaps, final int body) {
		this(leather, vambraces, chaps, body, -1);
	}
	public int getLeather() {
		return leather;
	}
	
	public int getVamb() {
		return vambraces;
	}
	
	public int getChaps() {
		return chaps;
	}
	
	public int getBody() {
		return body;
	}

	public int getCoif() {
		return coif;
	}
}
