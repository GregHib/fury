package com.fury.game.entity.character.combat.prayer;

import com.fury.game.world.update.flag.block.Animation;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.util.NameUtils;

import java.util.HashMap;
import java.util.Map;

public enum CurseData {
    PROTECT_ITEM(50, 0.1, 32503, 610, new PrayerAnimation(new Animation(12567), new Graphic(2213))),
    SAP_WARRIOR(50, 0.3, 32505, 611),
    SAP_RANGER(52, 0.3, 32507, 612),
    SAP_MAGE(54, 0.3, 32509, 613),
    SAP_SPIRIT(56, 0.3, 32511, 614),
    BERSERKER(59, 0.4, 32513, 615, new PrayerAnimation(new Animation(12589), new Graphic(2266))),
    DEFLECT_SUMMONING(62, 0.5, 32515, 616),
    DEFLECT_MAGIC(65, 0.5, 32517, 617),
    DEFLECT_MISSILES(68, 0.5, 32519, 618),
    DEFLECT_MELEE(71, 0.5, 32521, 619),
    LEECH_ATTACK(74, 1, 32523, 620),
    LEECH_RANGED(76, 1, 32525, 621),
    LEECH_MAGIC(78, 1, 32527, 622),
    LEECH_DEFENCE(80, 1, 32529, 623),
    LEECH_STRENGTH(82, 1, 32531, 624),
    LEECH_ENERGY(84, 1, 32533, 625),
    LEECH_SPECIAL_ATTACK(86, 1, 32535, 626),
    WRATH(89, 1.2, 32537, 627),
    SOUL_SPLIT(92, 1.5, 32539, 628),
    TURMOIL(95, 3, 32541, 629, new PrayerAnimation(new Animation(12565), new Graphic(2226)));

    CurseData(int requirement, double drainRate, int buttonId, int configId, PrayerAnimation... animations)  {
        this.requirement = requirement;
        this.drainRate = drainRate;
        this.buttonId = buttonId;
        this.configId = configId;
        this.prayerAnimation = animations.length > 0 ? animations[0] : null;
        this.name = NameUtils.capitalizeWords(toString().toLowerCase().replaceAll("_", " "));
    }

    private final int requirement;
    private final double drainRate;
    private final int buttonId;
    public final int configId;
    private final PrayerAnimation prayerAnimation;
    private final String name;
    public static Map<Integer, CurseData> buttons = new HashMap<>();
    public static Map<Integer, CurseData> ids = new HashMap<>();

    static {
        for (CurseData data : CurseData.values()) {
            buttons.put(data.buttonId, data);
            ids.put(data.ordinal(), data);
        }
    }

    public static class PrayerAnimation {

        private PrayerAnimation(Animation animation, Graphic graphic) {
            this.animation = animation;
            this.graphic = graphic;
        }

        private final Animation animation;
        private final Graphic graphic;
    }


    public int getRequirement() {
        return requirement;
    }

    public double getDrainRate() {
        return drainRate;
    }

    public int getButtonId() {
        return buttonId;
    }

    public int getConfigId() {
        return configId;
    }

    public Animation getAnimation() {
        return prayerAnimation.animation;
    }

    public Graphic getGraphic() {
        return prayerAnimation.graphic;
    }

    public PrayerAnimation getPrayerAnimation() {
        return prayerAnimation;
    }

    public String getName() {
        return name;
    }
}
