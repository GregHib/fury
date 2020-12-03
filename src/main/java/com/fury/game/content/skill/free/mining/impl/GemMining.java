package com.fury.game.content.skill.free.mining.impl;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.free.mining.Mining;
import com.fury.game.content.skill.free.mining.MiningBase;
import com.fury.game.content.skill.free.mining.data.Pickaxe;
import com.fury.core.model.item.Item;
import com.fury.game.entity.object.GameObject;
import com.fury.game.node.entity.actor.object.ObjectManager;
import com.fury.game.node.entity.actor.object.TempObjectManager;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.util.Misc;

/**
 * Created by Greg on 10/12/2016.
 */
public class GemMining extends MiningBase {

    private GameObject rock;
    private Pickaxe axeDefinitions;

    public GemMining(GameObject rock) {
        this.rock = rock;
    }

    @Override
    public boolean start(Player player) {
        axeDefinitions = getPickaxe(player, false);
        if (!checkAll(player))
            return false;
        player.message("You swing your pickaxe at the rock.");
        setActionDelay(player, getMiningDelay(player));
        return true;
    }

    private int getMiningDelay(Player player) {
        int mineTimer = 50 - player.getSkills().getLevel(Skill.MINING) - Misc.getRandom(axeDefinitions.getPickAxeTime());
        if (mineTimer < 1 + 10)
            mineTimer = 1 + Misc.getRandom(10);
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
        if (player.getInventory().getSpaces() <= 0) {
            player.message("Not enough space in your inventory.");
            return false;
        }
        return true;
    }

    private boolean hasMiningLevel(Player player) {
        return player.getSkills().hasRequirement(Skill.MINING, 45, "mine this rock");
    }

    @Override
    public boolean process(Player player) {
        player.perform(new Animation(axeDefinitions.getAnimationId()));
        return checkRock();
    }

    @Override
    public int processWithDelay(Player player) {
        addOre(player);
        TempObjectManager.spawnObjectTemporary(new GameObject(11193, rock), 60000, false, false);
        if (player.getInventory().getSpaces() <= 0) {
            player.animate(-1);
            player.message("Not enough space in your inventory.");
            return -1;
        }
        return -1;
    }

    private void addOre(Player player) {
        Mining.addExperience(player, 65);
        double random = Misc.random(0, 100);
        player.getInventory().add(new Item(random <= 3.5 ? 1617 : random <= 3.7 ? 1619 : random <= 4.1 ? 1621 : random <= 7 ? 1623 : random <= 12.7 ? 1629 : random <= 21.9 ? 1627 : 1625, 1));
        player.message("You receive a gem.");
    }

    private boolean checkRock() {
        return ObjectManager.containsObjectWithId(rock, rock.getId());
    }
}
