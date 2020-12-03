package com.fury.game.content.skill.free.mining.impl.essence;

import com.fury.cache.def.Loader;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.free.mining.Mining;
import com.fury.game.content.skill.free.mining.MiningBase;
import com.fury.game.content.skill.free.mining.data.Pickaxe;
import com.fury.core.model.item.Item;
import com.fury.game.entity.object.GameObject;
import com.fury.game.node.entity.actor.object.ObjectManager;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.util.Misc;

/**
 * Created by Greg on 08/12/2016.
 */
public class EssenceMining extends MiningBase {
    private GameObject rock;
    private Essence definitions;
    private Pickaxe axeDefinitions;

    public EssenceMining(GameObject rock, Essence definitions) {
        this.rock = rock;
        this.definitions = definitions;
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
        int mineTimer = definitions.getOreBaseTime() - player.getSkills().getLevel(Skill.MINING) - Misc.getRandom(axeDefinitions.getPickAxeTime());
        if (mineTimer < 1 + definitions.getOreRandomTime())
            mineTimer = 1 + Misc.getRandom(definitions.getOreRandomTime());
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
        return player.getSkills().hasRequirement(Skill.MINING, definitions.getLevel(), "mine this rock");
    }

    @Override
    public boolean process(Player player) {
        player.perform(new Animation(axeDefinitions.getAnimationId()));
        return checkRock();
    }

    @Override
    public int processWithDelay(Player player) {
        addOre(player);
        if (player.getInventory().getSpaces() <= 0) {
            player.animate(-1);
            player.message("Not enough space in your inventory.");
            return -1;
        }
        return getMiningDelay(player);
    }

    private void addOre(Player player) {
        Mining.addExperience(player, definitions.getXp());
        player.getInventory().add(new Item(definitions.getOreId()));
        String oreName = Loader.getItem(definitions.getOreId()).getName().toLowerCase();
        player.message("You mine some " + oreName + ".", true);
    }

    private boolean checkRock() {
        return ObjectManager.containsObjectWithId(rock, rock.getId());
    }

}
