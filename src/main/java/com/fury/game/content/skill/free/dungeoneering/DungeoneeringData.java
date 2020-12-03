package com.fury.game.content.skill.free.dungeoneering;

import com.fury.game.content.skill.Skill;

public class DungeoneeringData {
	public static final int EMPTY = 0, 
			HOME_1 = 1, HOME_2 = 2, HOME_3 = 3, HOME_4 = 4, HOME_5 = 5,
			ROOM_1 = 6, ROOM_2 = 7, ROOM_3 = 8, ROOM_4 = 9, OBELISK_1 = 10,
			OBELISK_2 = 11, LARGE_1 = 12, LARGE_2 = 13, LARGE_3 = 14,
			LARGE_4 = 15, LARGE_5 = 16, LARGE_6 = 17, LARGE_7 = 18, MEDIUM_1 = 19,
			MEDIUM_2 = 20, MEDIUM_3 = 21, MEDIUM_4 = 22, OTHER_1 = 23,
			
			EASY_ROOM = 0, MEDIUM_ROOM = 1, HARD_ROOM = 2, BOSS_ROOM = 3, BONUS_ROOM = 4,
			//64, 96
			BASE_X = 1472, BASE_Y = 3776, MIDDLE_X = BASE_X + 32, MIDDLE_Y = BASE_Y + 32, EMPTY_X = 64, EMPTY_Y = 5480;//MIDDLE_X = 120, MIDDLE_Y = 5560;

	public static final int[] DOORIDS = new int[] { 55764, 55763, 55762 };
	
	/*
	 *  49464 - Door
	 *  49463 - Door (green tinge)
	 *  49462 - Door (ice)
	 *  
	 *  //Normal, green, ice, normal looped
	 *  50344 - Gold shield door
	 *  50343 - Gold shield door
	 *  50342 - Gold shield door
	 *  50340 - Gold shield door - potions
	 *  50339 - Gold shield door - potions
	 *  50338 - Gold shield door - potions
	 *  
	 *  50334 - prayer spirit
	 *  50331 - magic
	 */
	
	/*
	 * 55764 - Door
	 * 55763 - Door with eye
	 * 55762 - key door?
	 * 55761/55760 - potion door
	 * 55759 - prayer spirit
	 * 55758 - magic
	 * 55757 - summoning
	 * 55756 - trees - farming/fletching/woodcutting?
	 * 55755 - boss door?
	 * 55754 - planks - woodcutting
	 * 55753 - firemaking - fire
	 * 55752 - purple square key door
	 * 55751 - purple square key door & hanging key?
	 * 55750 - rocks - mining
	 * 55749 - pistons - thieving?
	 * 55748 - pistons down
	 * 55747 - door no handles
	 * 55746 - combination lock
	 * 55745 - pull down circle on door
	 * 55744 - above unlocked
	 * 55743 - plank leaning against
	 * 55742 - plank fallen, door broken
	 * 55741 - blue symbols
	 * 55740/55739 - heavy beam accross
	 * 55738 - golden shield key
	 * 55737 - golden wedge
	 * 55736 - golden crest
	 * 55735 - golden corner
	 * 55734 - golden pentagon
	 * 55733 - golden rectangle
	 * 55732 - golden/orange diamond
	 * 55731 - golden/orange triangle
	 * 55730 - golden shield
	 * 55729 - golden wedge
	 * 55728 - golden crest
	 * 55727 - golden corner
	 * 55726 - golden pentagon
	 * 55725 - golden rectangle
	 * 55724 - orange diamond
	 * 55723 - orange triangle
	 * 55722 - golden crest
	 * 
	 * 50681 - brick walls?
	 * 50680 - ?
	 * 
	 * appears to loop to 55675.
	 */
	public enum RoomData {//WEST-SOUTH-EAST-NORTH
		EMPTY(79, 5505, DungeoneeringData.EMPTY, new boolean[] { false, false, false, false }),
		HOME_1(112, 5568, DungeoneeringData.HOME_1, new boolean[] { true, true, true, true }),
		HOME_2(112, 5552, DungeoneeringData.HOME_2, new boolean[] { true, true, false, true }),
		HOME_3(112, 5536, DungeoneeringData.HOME_3, new boolean[] { false, true, false, true }),
		HOME_4(112, 5520, DungeoneeringData.HOME_4, new boolean[] { true, true, false, false }),
		HOME_5(112, 5504, DungeoneeringData.HOME_5, new boolean[] { false, true, false, false }),
		ROOM_1(576, 4800, DungeoneeringData.ROOM_1, new boolean[] { true, true, true, false }),
		ROOM_2(576, 4816, DungeoneeringData.ROOM_2, new boolean[] { false, true, true, true }),
		ROOM_3(576, 4768, DungeoneeringData.ROOM_3, new boolean[] { false, true, false, true }),
		ROOM_4(576, 4736, DungeoneeringData.ROOM_4, new boolean[] { true, true, false, false }),
		OBELISK_1(592, 4768, DungeoneeringData.OBELISK_1, new boolean[] { false, true, false, true }),
		OBELISK_2(592, 4816, DungeoneeringData.OBELISK_2, new boolean[] { false, true, true, true }),
		LARGE_1(336, 4736, DungeoneeringData.LARGE_1, new boolean[] { true, true, false, false }),
		LARGE_2(336, 4752, DungeoneeringData.LARGE_2, new boolean[] { false, true, true, false }),
		LARGE_3(336, 4768, DungeoneeringData.LARGE_3, new boolean[] { false, true, false, true }),
		LARGE_4(336, 4784, DungeoneeringData.LARGE_4, new boolean[] { true, true, false, true }),
		LARGE_5(336, 4800, DungeoneeringData.LARGE_5, new boolean[] { true, true, true, false }),
		LARGE_6(336, 4816, DungeoneeringData.LARGE_6, new boolean[] { false, true, true, true }),
		LARGE_7(336, 4832, DungeoneeringData.LARGE_7, new boolean[] { true, true, true, true }),
		MEDIUM_1(144, 4800, DungeoneeringData.MEDIUM_1, new boolean[] { true, true, true, false }),
		MEDIUM_2(144, 4816, DungeoneeringData.MEDIUM_2, new boolean[] { false, true, true, true }),
		MEDIUM_3(144, 4832, DungeoneeringData.MEDIUM_3, new boolean[] { true, true, true, true }),
		OTHER_1(112, 4768, DungeoneeringData.OTHER_1, new boolean[] { false, true, false, true });
		public static RoomData forID(int id) {
			for (RoomData rd : values()) {
				if (rd.id == id)
					return rd;
			}
			return null;
		}

		private int x, y, id;
		private boolean[] doors;

		private RoomData(int x, int y, int id, boolean[] doors) {
			this.x = x;
			this.y = y;
			this.id = id;
			this.doors = doors;
		}

		public boolean[] getDoors() {
			return doors;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public int getId() {
			return id;
		}

		public static int getFirstElegibleRotation(RoomData rd, int from) {
			for (int rot = 0; rot < 4; rot++) {
				boolean[] door = rd.getRotatedDoors(rot);
				if (from == 0 && door[2])
					return rot;
				if (from == 1 && door[3])
					return rot;
				if (from == 2 && door[0])
					return rot;
				if (from == 3 && door[1])
					return rot;
			}
			return -1;
		}

		public static int getNextEligibleRotationClockWise(RoomData rd,
				int from, int currentRot) {
			for (int rot = currentRot + 1; rot < currentRot + 4; rot++) {
				int rawt = (rot > 3 ? (rot - 4) : rot);
				boolean[] door = rd.getRotatedDoors(rawt);
				if (from == 0 && door[2])
					return rawt;
				if (from == 1 && door[3])
					return rawt;
				if (from == 2 && door[0])
					return rawt;
				if (from == 3 && door[1])
					return rawt;
			}
			return currentRot;
		}

		public static int getNextEligibleRotationCounterClockWise(RoomData rd,
				int from, int currentRot) {
			for (int rot = currentRot - 1; rot > currentRot - 4; rot--) {
				int rawt = (rot < 0 ? (rot + 4) : rot);
				boolean[] door = rd.getRotatedDoors(rawt);
				if (from == 0 && door[2])
					return rawt;
				if (from == 1 && door[3])
					return rawt;
				if (from == 2 && door[0])
					return rawt;
				if (from == 3 && door[1])
					return rawt;
			}
			return -1;
		}

		public boolean[] getRotatedDoors(int rotation) {
			if (rotation == 0)
				return doors;
			if (rotation == 1) {
				boolean[] newDoors = new boolean[4];
				if (doors[0])
					newDoors[3] = true;
				if (doors[1])
					newDoors[0] = true;
				if (doors[2])
					newDoors[1] = true;
				if (doors[3])
					newDoors[2] = true;
				return newDoors;
			}
			if (rotation == 2) {
				boolean[] newDoors = new boolean[4];
				if (doors[0])
					newDoors[2] = true;
				if (doors[1])
					newDoors[3] = true;
				if (doors[2])
					newDoors[0] = true;
				if (doors[3])
					newDoors[1] = true;
				return newDoors;
			}
			if (rotation == 3) {
				boolean[] newDoors = new boolean[4];
				if (doors[0])
					newDoors[1] = true;
				if (doors[1])
					newDoors[2] = true;
				if (doors[2])
					newDoors[3] = true;
				if (doors[3])
					newDoors[0] = true;
				return newDoors;
			}
			return null;
		}
	}
	
	//Object position fix system.
	public enum RoomObjectData {
		//		Room ID, 		Room Rotation			OffsetX, OffsetY				ObjectID, OffsetX, OffsetY, Rotation
		HOME_90(new int[] { DungeoneeringData.HOME_1, DungeoneeringData.HOME_2, DungeoneeringData.HOME_3, DungeoneeringData.HOME_4, DungeoneeringData.HOME_5 }, 1, new int[][] {{14, 12}, {14, 10}}, new int[][] {{55811, 14, 9, 0}, {55813, 14, 11, 0}}),
		HOME_180(new int[] { DungeoneeringData.HOME_1, DungeoneeringData.HOME_2, DungeoneeringData.HOME_3, DungeoneeringData.HOME_4, DungeoneeringData.HOME_5 }, 2, new int[][] {{10, 0}, {12, 0}}, new int[][] {{55811, 9, 1, 1}, {55813, 11, 1, 1}}),
		HOME_270(new int[] { DungeoneeringData.HOME_1, DungeoneeringData.HOME_2, DungeoneeringData.HOME_3, DungeoneeringData.HOME_4, DungeoneeringData.HOME_5 }, 3, new int[][] {{0, 3}, {0, 5}}, new int[][] {{55811, 1, 5, 2}, {55813, 1, 3, 2}});
		
		private int rotation;
		private int[] roomType;
		private int[][] removeOffset, addOffset;

		private RoomObjectData(int[] roomType, int rotation, int[][] removeOffset, int[][] addOffset) {
			this.roomType = roomType;
			this.rotation = rotation;
			this.removeOffset = removeOffset;
			this.addOffset = addOffset;
		}
		
		public int[][] getAdditions() {
			return addOffset;
		}
		
		public int[][] getRemoval() {
			return removeOffset;
		}

		public int[] getRoomType() {
			return roomType;
		}
		
		public int getRotation() {
			return rotation;
		}
		
		public static RoomObjectData forID(int roomId, int rotation) {
			for (RoomObjectData rd : values()) {
				for(int rt : rd.getRoomType()) {
					if (rt == roomId && rd.getRotation() == rotation)
						return rd;
				}
			}
			return null;
		}
	}
	public enum SkillDoors {
		AGILITY(Skill.AGILITY, 55744, false, "You miss the chain, and set off the trap."),
		CONSTRUCTION(Skill.CONSTRUCTION, 55742, false, "You dislodge some debris while attempting to fix the door, and it falls on you."),
		CRAFTING(Skill.CRAFTING, 55748, false, "The rope snaps again as you attempt to fix it, and you crush your hands in the mechanism."),
		FARMING(Skill.FARMING, 55756, true, "You hurt your hands on the vicious thorns covering the vines."),
		FIREMAKING(Skill.FIREMAKING, 55753, true, "The pile of debris fails to ignite. The same cannot be said for your clothes."),
		HERBLORE(Skill.HERBLORE, 55760, false, "You incorrectly mix the ingredients, making it explode."),
		MAGIC(Skill.MAGIC, 55758, true, "You fail to dispel the barrier and take a surge of magical energy to the face."),
		MINING(Skill.MINING, 55750, true, "You fail to mine the obstruction, and are harmed by falling debris."),
		PRAYER(Skill.PRAYER, 55759, true, "The gods snub your prayer, and the dark spirit attacks you."),
		RUNECRAFTING(Skill.RUNECRAFTING, 55741, true, "You imbue the door with the wrong type of rune energy, and it reacts explosively."),
		SMITHING(Skill.SMITHING, 55751, false, "You hit your hand with the hammer. Needless to say, the key is still broken."),
		STRENGTH(Skill.STRENGTH, 55739, false, "You pull a muscle while attempting to move the plank."),
		SUMMONING(Skill.SUMMONING, 55757, true, "You fail to dismiss the rogue familiar, and it punches you in anger."),
		THIEVING(Skill.THIEVING, 55746, false, "You set off a booby trap inside the lock, and fail to pick it."),
		WOODCUTTING(Skill.WOODCUTTING, 55754, false, "You swing the axe against the grain and are showered with sharp splinters of wood.");
		
		private Skill skill;
		private int doorId;
		private boolean requiresDoor;
		private String failedMessage;

		private SkillDoors(Skill skill, int doorId, boolean requiresDoor, String failedMessage) {
			this.skill = skill;
			this.doorId = doorId;
			this.requiresDoor = requiresDoor;
			this.failedMessage = failedMessage;
		}
		
		public Skill getSkill() {
			return skill;
		}
		
		public int getDoorId() {
			return doorId;
		}

		public int getReplacementDoorId() {
			if(!requiresDoor)
				return doorId + 1;
			return -1;
		}
		
		public boolean requiresDoor() {
			return requiresDoor;
		}
		
		public String getFailedMessage() {
			return failedMessage;
		}
		
		public static SkillDoors forID(Skill skill) {
			for (SkillDoors sd : values()) {
				if (sd.getSkill() == skill)
					return sd;
			}
			return null;
		}
	}
}
