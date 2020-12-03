package com.fury.game.content.skill.free.prayer;

import com.fury.core.model.item.Item;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.content.global.Achievements;
import com.fury.game.content.global.action.Action;
import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.member.construction.HouseConstants;
import com.fury.game.entity.object.GameObject;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.game.world.update.flag.block.graphic.Graphic;

/**
 * Created by Greg on 29/12/2016.
 */
public class BoneOffering extends Action {

    private static final double[] BASE_ALTAR_PERCENT_BOOST = { 1.0, 1.1, 1.25, 1.5, 1.75, 2.0, 2.5 };
    private static final Animation OFFERING_ANIMATION = new Animation(3705);
    private static final Graphic OFFERING_GRAPHICS = new Graphic(624);

    private double totalExperience;
    private final int litBurners;
    private final Bone bone;
    private final GameObject altar;
    private Position position;
    private int ticks;

    public BoneOffering(GameObject altar, Bone bone, int amount, int litBurners) {
        this.altar = altar;
        this.bone = bone;
        this.ticks = amount;
        this.litBurners = litBurners;
    }

    @Override
    public boolean start(Player player) {
        position = calcPosition(player, altar);
        player.getDirection().face(position);
        int house = HouseConstants.Builds.ALTAR.getSingleHObjectSlot(altar.getId());
        if(house == -1)
            totalExperience = bone.getExperience() * 3.5;
        else
            totalExperience = bone.getExperience() * (BASE_ALTAR_PERCENT_BOOST[house] + 1.0 + (litBurners > 2 ? 2 : litBurners * 0.5));
        return true;
    }

    @Override
    public boolean process(Player player) {
        return ticks > 0;
    }

    @Override
    public int processWithDelay(Player player) {
        if (ticks > 0) {
            player.perform(OFFERING_ANIMATION);
            Graphic.sendGlobal(player, OFFERING_GRAPHICS, position);
            player.getSkills().addExperience(Skill.PRAYER, totalExperience);
            player.getInventory().delete(new Item(bone.getId()));
            Achievements.finishAchievement(player, Achievements.AchievementData.OFFER_BONES_ON_ALTAR);
            Achievements.doProgress(player, Achievements.AchievementData.OFFER_5000_BONES);
            player.message("The gods " + (bone.getExperience() <= 25 ? "accept" : bone.getExperience() <= 100 ? "are pleased with" : "are very pleased with") + " your offering.", true);
            ticks--;
            return 2;
        }
        return -1;
    }

    public static Position calcPosition(Player player, GameObject altar) {
        if((altar.getDirection() == 0 || altar.getDirection() == 2) && player.getX() > altar.getX())
            return altar.copyPosition().add(1, 0);
        else if((altar.getDirection() == 1 || altar.getDirection() == 3) && player.getY() > altar.getY())
            return altar.copyPosition().add(0, 1);
        return altar.copyPosition();
    }

    @Override
    public void stop(Player player) {
        setActionDelay(player, 3);
    }
}
