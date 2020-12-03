package com.fury.game.system.communication;

import com.fury.util.FontUtils;

/**
 * Enumeration holding all possible notification types
 * @author Stan
 *
 */
public enum MessageType {
	OVERLOAD_WARNING(FontUtils.SHAD + FontUtils.colourTags(0x490101), FontUtils.COL_END + FontUtils.SHAD_END),
	RENEWAL_WARNING(FontUtils.colourTags(0x790000), FontUtils.COL_END),
	NPC_ALERT(FontUtils.colourTags(0x008fb2) + "-" + FontUtils.RED, FontUtils.COL_END),
	SERVER_ALERT(FontUtils.imageTags(535) + FontUtils.colourTags(0x008fb2), FontUtils.COL_END),
	PLAYER_ALERT(FontUtils.imageTags(535) + FontUtils.colourTags(0x008fb2), FontUtils.COL_END),
	LOOT_ALERT(FontUtils.colourTags(0x008fb2) + "[LOOT]", FontUtils.COL_END);

	private String prefix;
	private String suffix;
	
	MessageType(String prefix, String suffix){
		this.prefix = prefix;
		this.suffix = suffix;
	}

	public String getPrefix() {
		return prefix;
	}

	public String getSuffix() {
		return suffix;
	}
}
