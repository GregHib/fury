package com.fury.game.entity.character.player.content.emotes;

import com.fury.game.container.impl.equip.Slot;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.util.Misc;

public enum EmoteData {
    YES(168, new Animation(855), null, 20),
    NO(169, new Animation(856), null, 20),
    BOW(164, new Animation(858), null, 15),
    ENHANCED_BOW(164, new Animation(5312), null, 35),
    ANGRY(165, new Animation(859), null, 20),
    ENHANCED_ANGRY(165, new Animation(5315), null, 50),
    THINK(162, new Animation(857), null, 20),
    WAVE(163, new Animation(863), null, 25),
    SHRUG(13370, new Animation(2113), null, 20),
    CHEER(171, new Animation(862), null, 30),
    BECKON(167, new Animation(864), null, 25),
    LAUGH(170, new Animation(861), null, 30),
    JUMP_FOR_JOY(13366, new Animation(2109), null, 20),
    YAWN(13368, new Animation(2111), null, 30),
    ENHANCED_YAWN(13368, null, null, 80),
    DANCE(166, new Animation(866), null, 50),
    ENHANCED_DANCE(166, new Animation(5316), null, 65),
    JIG(13363, new Animation(2106), null, 40),
    SPIN(13364, new Animation(2107), null, 20),
    HEADBANG(13365, new Animation(2108), null, 35),
    CRY(161, new Animation(860), null, 30),
    KISS(11100, new Animation(1374), new Graphic(1702), 20),
    PANIC(13362, new Animation(2105), null, 20),
    RASPBERRY(13367, new Animation(2110), null, 25),
    CLAP(172, new Animation(865), null, 35),
    SALUTE(13369, new Animation(2112), null, 15),
    GOBLIN_BOW(13383, new Animation(2127), null, 15),
    GOBLIN_SALUTE(13384, new Animation(2128), null, 30),
    GLASS_BOX(667, new Animation(1131), null, 30),
    CLIMB_ROPE(6503, new Animation(1130), null, 40),
    LEAN(6506, new Animation(1129), null, 45),
    GLASS_WALL(666, new Animation(1128), null, 50),
    IDEA(18460, new Animation(4276), new Graphic(712), 15),
    STOMP(18461, new Animation(4278), null, 40),
    FLAP(18462, new Animation(4280), null, 20),
    SLAP_HEAD(18463, new Animation(4275), null, 20),
    ZOMBIE_WALK(18464, new Animation(3544), null, 65),
    ZOMBIE_DANCE(18465, new Animation(3543), null, 50),
    ZOMBIE_HAND(15166, new Animation(7272), new Graphic(1244), 25),
    SCARED(15167, new Animation(2836), null, 35),
    RABBIT_HOP(18686, new Animation(6111), null, 50),
    SNOWMAN_DANCE(18687, new Animation(7531), null, 35),
    AIR_GUITAR(18688, new Animation(2414), new Graphic(1537), 50),
    SAFETY_FIRST(18689, new Animation(8770), new Graphic(1553), 40),
    EXPLORE(18690, new Animation(9990), new Graphic(1734), 60),
    TRICK(18691, new Animation(10530), new Graphic(1864), 40),
    FREEZE(18692, new Animation(11044), new Graphic(1973), 50),
    GIVE_THANKS(18693, null, null, 34),
    AROUND_THE_WORLD_IN_EGGTY_DAYS(18694, new Animation(11542), new Graphic(2037), 65),
    DRAMATIC_POINT(18695, new Animation(12658), new Graphic(780), 40),
    FAINT(18696, new Animation(14165), null, 50),
    PUPPET_MASTER(18697, new Animation(14869), new Graphic(2837), 55),
    TASKMASTER(18698, new Animation(15033), new Graphic(2930), 110),
    SEAL_OF_APPROVAL(18699, null, null, 55);

    EmoteData(int button, Animation animation, Graphic graphic, int delay) {
        this.button = button;
        this.animation = animation;
        this.graphic = graphic;
        this.delay = delay;
    }

    private int button;
    public Animation animation;
    public Graphic graphic;
    private int delay;

    public static EmoteData forButton(int button) {
        for (EmoteData data : EmoteData.values()) {
            if (data != null && data.button == button)
                return data;
        }
        return null;
    }

    public static EmoteData getRandomEmote() {
        int randomEmote = Misc.getRandom(EmoteData.values().length - 1);
        return EmoteData.values()[randomEmote];
    }

    public int getDelay() {
        return delay;
    }

    public EmoteData transform(Player player) {
        switch (this) {
            case ANGRY:
                if (player.getEquipment().get(Slot.HEAD).getId() == 10740)
                    return ENHANCED_ANGRY;
            case YAWN:
                if (player.getEquipment().get(Slot.HEAD).getId() == 10746)
                    return ENHANCED_YAWN;
            case BOW:
                if (player.getEquipment().get(Slot.LEGS).getId() == 10744)
                    return ENHANCED_BOW;
            case DANCE:
                if (player.getEquipment().get(Slot.LEGS).getId() == 10742)
                    return ENHANCED_DANCE;
        }
        return this;
    }
}
