package com.fury.game.content.skill.free.prayer;

import com.fury.cache.def.Loader;
import com.fury.cache.def.item.ItemDefinition;
import com.fury.game.content.global.Achievements;
import com.fury.game.content.global.events.christmas.ChristmasEvent;
import com.fury.game.content.skill.Skill;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import com.fury.game.world.GameWorld;
import com.fury.game.world.update.flag.block.Animation;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Greg on 29/12/2016.
 */
public enum Bone {
    NORMAL(526, 4.5),
    BURNT(528, 4.5),
    WOLF(2859, 4.5),
    MONKEY(3183, 5),
    BAT(530, 5),
    BIG(532, 15),
    LONG(10976, 15),
    CURVED(10977, 15),
    FEMUR(15182, 15),
    JOGRE(3125, 15),
    ZOGRE(4812, 22.5),
    SHAIKAHAN(3123, 25),
    BABY(534, 30),
    WYVERN(6812, 50),
    DRAGON(536, 72),
    FAYRG(4830, 84),
    RAURG(4832, 96),
    DAGANNOTH(6729, 125),
    OURG(4834, 140),
    OURG_2(14793, 140),
    //dung ones
    FROST_DRAGON(18830, 180),
    //real ones
    FROST_DRAGON_2(18832, 180),
    IMPIOUS(20264, 4, true),
    ACCURSED(20266, 12.5, true),
    INFERNAL(20268, 62.5, true);

    //Alter = * 3.5
    //Ecto = * 4

    private int id;
    private double experience;
    private boolean ash;

    private static Map<Integer, Bone> bones = new HashMap<>();

    static {
        for (Bone bone : Bone.values()) {
            bones.put(bone.getId(), bone);
        }
    }

    public static Bone forId(int id) {
        return bones.get(id);
    }

    Bone(int id, double experience, boolean ash) {
        this.id = id;
        this.experience = experience;
        this.ash = ash;
    }

    Bone(int id, double experience) {
        this.id = id;
        this.experience = experience;
    }

    public int getId() {
        return id;
    }

    public boolean isAsh() {
        return ash;
    }

    public double getExperience() {
        return experience;
    }

    public static final Animation BURY_ANIMATION = new Animation(827);

    public static void bury(final Player player, Item item) {
        if(!player.getTimers().getClickDelay().elapsed(2000))
            return;
        final Bone bone = Bone.forId(item.getId());
        if (bone == null)
            return;
        final ItemDefinition itemDef = Loader.getItem(item.getId());
        player.getMovement().lock(2);
        switch (item.getId()) {
            case 20264:
                player.animate(445);
                player.graphic(56);
                break;
            case 20266:
                player.animate(445);
                player.graphic(47);
                break;
            case 20268:
                player.animate(445);
                player.graphic(40);
                break;
            default:
                player.perform(BURY_ANIMATION);
                break;
        }

        ChristmasEvent.giveSnowflake(player);

        player.getPacketSender().sendMessage(bone.ash ? "You scatter the ashes in the wind." : "You dig a hole in the ground...", true);
        GameWorld.schedule(1, () -> {
            if (!bone.ash)
                player.message("You bury the " + itemDef.getName().toLowerCase(), true);
            player.getInventory().delete(new Item(item, 1));
            double xp = bone.getExperience() * player.getAuraManager().getPrayerMultiplier();
            player.getSkills().addExperience(Skill.PRAYER, xp);
            if(bone.ash) {
                Achievements.doProgress(player, Achievements.AchievementData.SCATTER_500_ASHES);
            } else {
                buryAchievements(player, bone);
            }
            Double lastPrayer = (Double) player.getTemporaryAttributes().get("current_prayer_xp");
            if (lastPrayer == null) {
                lastPrayer = 0.0;
            }
            double total = xp + lastPrayer;
            int amount = (int) (total / 500);
            if (amount != 0) {
                double restore = player.getAuraManager().getPrayerRestoration() * (player.getSkills().getMaxLevel(Skill.PRAYER) * 10);
                player.getSkills().restore(Skill.PRAYER, (int) (amount * restore));
                total -= amount * 500;
            }
            player.getTemporaryAttributes().put("current_prayer_xp", total);
        });
        int demonHornNecklace = handleDemonHornNecklace(player, bone);
        if (demonHornNecklace > 0) {
            player.getSkills().restore(Skill.PRAYER, demonHornNecklace);
        }
        player.getTimers().getClickDelay().reset();
    }

    public static void buryAchievements(Player player, Bone bone) {
        Achievements.finishAchievement(player, Achievements.AchievementData.BURY_A_BONE);
        if (bone == Bone.DRAGON)
            Achievements.finishAchievement(player, Achievements.AchievementData.BURY_A_DRAGON_BONE);
        if(bone == Bone.FROST_DRAGON)
            Achievements.doProgress(player, Achievements.AchievementData.BURY_1000_FROST_DRAGON_BONES);
        Achievements.doProgress(player, Achievements.AchievementData.BURY_5000_BONES);
    }

    public static int handleDemonHornNecklace(Player player, Bone bone) {
        int normalBones = bone.getId() == 526 || bone.getId() == 528 || bone.getId() == 3183 ? 10 : 0;
        int mediumBones = bone.getId() == 532 || bone.getId() == 10976 || bone.getId() == 10977 || bone.getId() == 15182 || bone.getId() == 3125 || bone.getId() == 4812 || bone.getId() == 3123 || bone.getId() == 534 || bone.getId() == 6812  ? 20 : 0;
        int largeBones = bone.getId() == 536 || bone.getId() == 4830 || bone.getId() == 4832 || bone.getId() == 4834 || bone.getId() == 14793 || bone.getId() == 18830 || bone.getId() == 18832 || bone.getId() == 20264 || bone.getId() == 20266 || bone.getId() == 20268 ? 30 : 0;
        int amountToRecover = normalBones + mediumBones + largeBones;
        if (player.getEquipment().contains(new Item(19888))) {
                return amountToRecover;
        }
        return 0;
    }
}
