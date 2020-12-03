package com.fury.game.entity.character.player.link.transportation;

import com.fury.game.world.update.flag.block.Animation;
import com.fury.game.world.update.flag.block.graphic.Graphic;
public enum TeleportType {

	NORMAL(1, new Animation(8939), new Animation(8941), new Graphic(1576), new Graphic(1577)),
	ANCIENT(5, new Animation(9599), null, new Graphic(1681), null),
	LUNAR(4, new Animation(9606), new Animation(9013), new Graphic(1685), null),
	TELE_TAB(1, new Animation(9597), Animations.DEFAULT_RESET_ANIMATION, new Graphic(1680), null),
	RING_TELE(2, new Animation(9603), Animations.DEFAULT_RESET_ANIMATION, new Graphic(1684), null),
	LEVER(2, new Animation(8939), new Animation(8941), new Graphic(1576), new Graphic(1577)),
	PURO_PURO(7, new Animation(6601), Animations.DEFAULT_RESET_ANIMATION, new Graphic(1118), null),
	KINSHIP_TELE(8, new Animation(13652), new Animation(13654), new Graphic(2602), new Graphic(2603)),
	DUNGEONEERING_TELE(1, new Animation(13288), new Animation(13285), new Graphic(2516), new Graphic(2517)),
	SPIRIT_TREE(2, new Animation(7082), new Animation(7084), new Graphic(1229, 10), new Graphic(1229)),
	FAIRY_RING_TELE(0, new Animation(3254, 3), new Animation(3255, 3), new Graphic(2670), new Graphic(2671)),
	ARDOUGNE_CLOAK(4, new Animation(9606), null, new Graphic(2172), new Graphic(2173)),
	CABBAGE_TELEPORT(1, new Animation(804), new Animation(804), new Graphic(1731), new Graphic(1732)),
	SCROLL_TELEPORT(4, new Animation(14293), null, null, null),
	BATS_TELEPORT(1, new Animation(8939), new Animation(8941), new Graphic(1864), new Graphic(1864)),
	BROOMSTICK_TELEPORT(2, new Animation(10538), new Animation(10537), null, null),
	IRON_MAN(2, new Animation(1816), null, new Graphic(342), null),
	SNOW_TELEPORT(2, new Animation(7534), null, new Graphic(1285), null),
	DUNGEONEERING_GATESTONE_PORTAL(0, new Animation(14279), new Animation(13285), null, new Graphic(2517));
	
	TeleportType(int startTick, Animation startAnim, Animation endAnim, Graphic startGraphic, Graphic endGraphic) {
		this.startTick = startTick;
		this.startAnim = startAnim;
		this.endAnim = endAnim;
		this.startGraphic = startGraphic;
		this.endGraphic = endGraphic;
	}
	
	private Animation startAnim, endAnim;
	private Graphic startGraphic, endGraphic;
	private int startTick;

	public Animation getStartAnimation() {
		return startAnim;
	}

	public Animation getEndAnimation() {
		return endAnim;
	}

	public Graphic getStartGraphic() {
		return startGraphic;
	}

	public Graphic getEndGraphic() {
		return endGraphic;
	}

	public int getStartTick() {
		return startTick;
	}
	
	static class Animations {
		static Animation DEFAULT_RESET_ANIMATION = new Animation(-1);
	}
}
