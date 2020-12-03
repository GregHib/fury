package com.fury.game.content.skill.free.dungeoneering;

public class RoomReference {

	private int x, y;

	public RoomReference(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof RoomReference) {
			RoomReference rRef = (RoomReference) object;
			return x == rRef.x && y == rRef.y;
		}
		return false;
	}

	@Override
	public String toString() {
		return "[RoomReference][" + x + "][" + y + "]";
	}
}
