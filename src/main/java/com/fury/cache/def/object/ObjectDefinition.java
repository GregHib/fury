package com.fury.cache.def.object;

import com.fury.cache.ByteBuffer;
import com.fury.cache.Revision;
import com.fury.cache.def.Definition;
import com.fury.game.content.skill.free.dungeoneering.DungeonUtils;

import java.util.HashMap;


// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

/**
 * Rewritten by Greg on 18/08/2016.
 */

public abstract class ObjectDefinition extends Definition {

	public ObjectDefinition(int id) {
		this.id = id;
		setDefaults();
	}

	@Override
	public void changeValues() {
		setObjectClipping();
		switch (id) {

			case 588:
			case 48627:
			case 48628:
			case 48629:
			case 48630:
			case 48631:
			case 48632:
			case 48633:
			case 48634:
			case 48635:
			case 48636:
			case 48637:
			case 48638:
			case 48639:
			case 48640:
			case 48641:
			case 48642://Statue
				options = new String[] {"Add-to", "Check", "Rewards", null, null};
				break;
			case 55804:
			case 52271:
			case 56184:
			case 56187:
			case 56188:
			case 56189:
			case 56195://Dungeoneering 2 model anims
			case 43099:
			case 43096://Falador well's
				animationId = -1;
				break;
			case 68444:
				interactive = 1;
				name = "Kiln";
				options = new String[]{"Enter", "QuickEnter", null, null, null};
				break;
			case 57183://Lava flow mine, main source of mem usage
				setDefaults();
				break;
			/*case 1444:
			case 1443:
			case 1442:
			case 1441:
			case 1440://Wilderness wall
				setDefaults();
				break;*/
			case 1308://Broken willow trees
				setDefaults();
				break;
			case 74630:
				name = "Slayer portal";
				break;
		}

		if(name.contains("habitat") && modelTypes[0] == 22)
			solid = 0;

		if (name.equalsIgnoreCase("bank booth")) {
			boolean hasOptions = false;
			if(options != null) {
				for (int i = 0; i < options.length; i++) {
					if (options[i] != null) {
						hasOptions = true;
						break;
					}
				}
				if (hasOptions) {
					options = new String[]{"Use", "Exchange", null, null, null};
				}
			}
		}

		if (name.equalsIgnoreCase("bank booth") || name.equalsIgnoreCase("counter")) {
			ignoreClipOnAlternativeRoute = false;
			projectileClipped = true;
			if (solid == 0)
				solid = 1;
		} else if (DungeonUtils.isDoor(id)) {
			ignoreClipOnAlternativeRoute = false;
			projectileClipped = true;
			if (solid == 0)
				solid = 1;
		}

		if (ignoreClipOnAlternativeRoute) {
			projectileClipped = false;
			solid = 0;
		}
	}

	public final void setObjectClipping() {
		if (interactive == -1) {
			interactive = 0;
			if (modelTypes != null && modelTypes.length == 1 && modelTypes[0] == 10)
				interactive = 1;
			if (options != null)
				interactive = 1;
		}
		if (supportItems == -1)
			supportItems = solid != 0 ? 1 : 0;
	}

	public boolean isInteractive() {
		return interactive == 1;
	}

	public boolean obstructsGround;
	public byte brightness;
	public int offsetX;
	String name;
	public int modelSizeY;
	public byte contrast;
	public int sizeX;
	public int offsetH;
	public int mapFunctionID;
	public int[] originalColours;
	public int modelSizeX;
	public int configId;
	public boolean rotated;
	public int id;
	public boolean impenetrable;
	public int mapSceneID;
	public int configObjectIds[];
	public int sizeY;
	public boolean contouredGround;
	public boolean needsCulling;
	public boolean ignoreClipOnAlternativeRoute;
	public int plane;
	public boolean delayShading;
	public int modelSizeH;
	public int[][] modelIds;
	public int varbitIndex;
	public int offsetMultiplier;
	public int[] modelTypes;
	public String description;
	public boolean castsShadow;
	public int animationId;
	public int offsetY;
	public int[] replacementColours;
	public String[] options;
	public int[] animationArray;
	public HashMap<Integer, Object> parameters;
	public Revision revision;
	public boolean animateImmediately;
	public int solid;
	public int supportItems;
	public int interactive;
	public boolean projectileClipped;

	public boolean isProjectileClipped() {
		return projectileClipped;
	}

	public void setDefaults() {
		projectileClipped = true;
		animateImmediately = true;
		interactive = -1;
		supportItems = -1;
		solid = 2;
		modelIds = null;
		modelTypes = null;
		name = "null";
		description = null;
		replacementColours = null;
		originalColours = null;
		sizeX = 1;
		sizeY = 1;
		impenetrable = true;
		contouredGround = false;
		delayShading = false;
		needsCulling = false;
		animationId = -1;
		offsetMultiplier = 16;
		brightness = 0;
		contrast = 0;
		options = null;
		mapFunctionID = -1;
		mapSceneID = -1;
		rotated = false;
		castsShadow = true;
		modelSizeX = 128;
		modelSizeH = 128;
		modelSizeY = 128;
		plane = 0;
		offsetX = 0;
		offsetH = 0;
		offsetY = 0;
		obstructsGround = false;
		ignoreClipOnAlternativeRoute = false;
		varbitIndex = -1;
		configId = -1;
		configObjectIds = null;
		animationArray = null;
	}

	public static void skip(ByteBuffer buffer) {
		int length = buffer.readUnsignedByte();
		for(int index = 0; index < length; ++index) {
			buffer.position += 1;
			int length2 = buffer.readUnsignedByte();

			for(int i = 0; i < length2; ++i) {
				buffer.readBigSmart();
			}
		}
	}

	public String getOption(int option) {
		if (options == null || options.length < option || option == 0)
			return "";
		return options[option - 1];
	}

	public boolean containsOption(int index, String option) {
		if (options == null || options[index] == null || options.length <= index)
			return false;
		return options[index].equals(option);
	}

    public boolean containsOption(String option) {
        if (options == null || options.length == 0)
            return false;
        for(int i = 0; i < options.length; i++) {
            if(options[i] == null)
                continue;
            if(options[i].equalsIgnoreCase(option)) {
                return true;
            }
        }
        return false;
    }

	public int xLength() {
		return sizeX;
	}

	public int yLength() {
		return sizeY;
	}

	public String getName() {
		return name;
	}
	
	public int getSizeX() {
		return sizeX;
	}
	
	public int getSizeY() {
		return sizeY;
	}

	public int getSolid() {
		return solid;
	}

	public boolean isImpenetrable() {
	    return projectileClipped;
    }

	public boolean hasActions() {
		return options != null;
	}
}
