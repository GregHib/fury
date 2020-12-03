package com.fury.game.world.update.flag.block.graphic;

public enum GraphicHeight {
	LOW,
	MIDDLE,
	HIGH,
	SALAMANDER_FIRE;

	public int toInt() {
		return ordinal() >= 3 ? 65 : (ordinal() * 50);
	}
}
