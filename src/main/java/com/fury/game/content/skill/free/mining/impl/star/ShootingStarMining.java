package com.fury.game.content.skill.free.mining.impl.star;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.content.global.Achievements;
import com.fury.game.content.global.dnd.star.ShootingStar;
import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.free.mining.Mining;
import com.fury.game.content.skill.free.mining.MiningBase;
import com.fury.game.content.skill.free.mining.data.Pickaxe;
import com.fury.game.entity.object.GameObject;
import com.fury.game.node.entity.actor.object.ObjectManager;
import com.fury.game.world.update.flag.block.Animation;

/**
 * Created by Greg on 10/12/2016.
 */
public class ShootingStarMining extends MiningBase {

    private GameObject rock;
    private Pickaxe axeDefinitions;

    public ShootingStarMining(GameObject rock) {
        this.rock = rock;
    }

    @Override
    public boolean start(Player player) {
        axeDefinitions = getPickaxe(player, false);
        if (!checkAll(player))
            return false;
        player.message("You swing your pickaxe at the rock.");
        setActionDelay(player, getMiningDelay());
        return true;
    }

    private int getMiningDelay() {
        return ShootingStar.getStarSize()*2;
    }

    private boolean checkAll(Player player) {
        if (axeDefinitions == null) {
            player.message("You do not have a pickaxe or do not have the required level to use the pickaxe.");
            return false;
        }
        if (!hasMiningLevel(player))
            return false;
        if (player.getInventory().getSpaces() <= 0) {
            player.message("Not enough space in your inventory.");
            return false;
        }
        return true;
    }

    private boolean hasMiningLevel(Player player) {
        return player.getSkills().hasRequirement(Skill.MINING, ShootingStar.getLevel(), "mine this rock");
    }

    @Override
    public boolean process(Player player) {
        player.perform(new Animation(axeDefinitions.getAnimationId()));
        return checkRock();
    }

    @Override
    public int processWithDelay(Player player) {
        addOre(player);
        if (player.getInventory().getSpaces() <= 0 && player.getInventory().getAmount(ShootingStar.STARDUST) >= 0) {
            player.animate(-1);
            player.message("Not enough space in your inventory.");
            return -1;
        }
        return getMiningDelay();
    }

    private void addOre(Player player) {
        Mining.addExperience(player, ShootingStar.getXP());
        if(player.getInventory().getAmount(ShootingStar.STARDUST) < 500)
            player.getInventory().add(ShootingStar.STARDUST);
        player.message("You mine some stardust.", true);
        ShootingStar.reduceStarLife();
        Achievements.finishAchievement(player, Achievements.AchievementData.MINE_SHOOTING_STAR);
    }

    private boolean checkRock() {
        return ObjectManager.containsObjectWithId(rock, rock.getId());
    }
}
