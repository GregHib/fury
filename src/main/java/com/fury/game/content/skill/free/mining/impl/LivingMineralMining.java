package com.fury.game.content.skill.free.mining.impl;

import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.free.mining.Mining;
import com.fury.game.content.skill.free.mining.MiningBase;
import com.fury.game.content.skill.free.mining.data.Pickaxe;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import com.fury.game.npc.slayer.LivingRock;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.util.Misc;

public class LivingMineralMining extends MiningBase {

    private LivingRock rock;
    private Pickaxe axeDefinitions;

    public LivingMineralMining(LivingRock rock) {
        this.rock = rock;
    }

    @Override
    public boolean start(Player player) {
        axeDefinitions = getPickaxe(player, false);
        if (!checkAll(player))
            return false;
        setActionDelay(player, getMiningDelay(player));
        return true;
    }

    private int getMiningDelay(Player player) {
        int oreBaseTime = 50;
        int oreRandomTime = 20;
        int mineTimer = oreBaseTime - player.getSkills().getLevel(Skill.MINING) - Misc.getRandom(axeDefinitions.getPickAxeTime());
        if (mineTimer < 1 + oreRandomTime)
            mineTimer = 1 + Misc.getRandom(oreRandomTime);
        mineTimer /= player.getAuraManager().getMininingAccurayMultiplier();
        return mineTimer;
    }

    private boolean checkAll(Player player) {
        if (axeDefinitions == null) {
            player.message("You do not have a pickaxe or do not have the required level to use the pickaxe.");
            return false;
        }
        if (!hasMiningLevel(player))
            return false;
        if (!player.getInventory().hasRoom()) {
            player.message("Not enough space in your inventory.");
            return false;
        }
        if (!rock.canMine(player)) {
            player.message("You must wait at least one minute before you can mine a living rock creature that someone else defeated.");
            return false;
        }
        return true;
    }

    private boolean hasMiningLevel(Player player) {
        if (73 > player.getSkills().getLevel(Skill.MINING)) {
            player.message("You need a mining level of 73 to mine this rock.");
            return false;
        }
        return true;
    }

    @Override
    public boolean process(Player player) {
        player.perform(new Animation(axeDefinitions.getAnimationId()));
        return checkRock(player);
    }

    @Override
    public int processWithDelay(Player player) {
        addOre(player);
        rock.takeRemains();
        player.animate(-1);
        return -1;
    }

    private void addOre(Player player) {
        Mining.addExperience(player, 25);
        player.getInventory().add(new Item(15263, Misc.random(5, 25)));
        player.message("You manage to mine some living minerals.", true);
    }

    private boolean checkRock(Player player) {
        return !rock.getFinished();
    }
}
