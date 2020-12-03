package com.fury.game.content.skill.free.firemaking.bonfire;

import com.fury.cache.Revision;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.content.dialogue.impl.misc.SimpleMessageD;
import com.fury.game.content.dialogue.impl.skills.firemaking.BonfireD;
import com.fury.game.content.global.Achievements;
import com.fury.game.content.global.action.Action;
import com.fury.game.content.misc.items.StrangeRocks;
import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.free.firemaking.Firemaking;
import com.fury.game.entity.character.combat.effects.Effect;
import com.fury.game.entity.character.combat.effects.Effects;
import com.fury.core.model.item.Item;
import com.fury.game.entity.object.GameObject;
import com.fury.game.node.entity.actor.object.ObjectManager;
import com.fury.game.npc.misc.FireSpirit;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.util.Misc;

import java.util.ArrayList;

/**
 * Created by Greg on 29/11/2016.
 */
public class Bonfire extends Action {
    private BonfireLogs log;
    private GameObject object;
    private int count;

    public Bonfire(BonfireLogs log, GameObject object) {
        this.log = log;
        this.object = object;
    }

    private boolean checkAll(Player player) {
        if (!ObjectManager.containsObjectWithId(object, object.getId()))
            return false;
        if (!player.getInventory().contains(log.getLog()))
            return false;
        if (player.getSkills().getLevel(Skill.FIREMAKING) < log.getLevel()) {
            player.getDialogueManager().startDialogue(new SimpleMessageD(), "You need level " + log.getLevel() + " Firemaking to add these logs to a bonfire.");
            return false;
        }
        return true;
    }


    public static BonfireLogs getLog(int logId) {
        for (BonfireLogs log : BonfireLogs.values()) {
            if (log.getLog().getId() == logId)
                return log;
        }
        return null;
    }

    public static boolean addLog(Player player, GameObject object, Item item) {
        for (BonfireLogs log : BonfireLogs.values())
            if (log.getLog().isEqual(item)) {
                player.getActionManager().setAction(new Bonfire(log, object));
                return true;
            }
        return false;
    }

    public static void addLogs(Player player, GameObject object) {
        ArrayList<BonfireLogs> possibilities = new ArrayList<>();
        for (BonfireLogs log : BonfireLogs.values())
            if (player.getInventory().contains(log.getLog()))
                possibilities.add(log);
        BonfireLogs[] logs = possibilities.toArray(new BonfireLogs[possibilities.size()]);
        if (logs.length == 0)
            player.message("You do not have any logs to add to this fire.");
        else if (logs.length == 1)
            player.getActionManager().setAction(new Bonfire(logs[0], object));
        else
            player.getDialogueManager().startDialogue(new BonfireD(), logs, object);
    }

    @Override
    public boolean start(Player player) {
        if (checkAll(player)) {
            player.getDirection().face(object);
            return true;
        }
        return false;

    }

    @Override
    public boolean process(Player player) {
        return checkAll(player);
    }

    @Override
    public int processWithDelay(Player player) {
        player.getInventory().delete(log.getLog());
        player.getSkills().addExperience(Skill.FIREMAKING, Firemaking.increasedExperience(player, log.getXp()));
        player.perform(new Animation(16699, Revision.PRE_RS3));
        player.perform(new Graphic(log.getGfxId(), Revision.PRE_RS3));

        if (Misc.random(100) == 0) {
            new FireSpirit(object, player);
            player.message("A fire spirit emerges from the bonfire.", 0xff0000);
        }
        if (log == BonfireLogs.YEWS)
            Achievements.doProgress(player, Achievements.AchievementData.BURN_5_YEW_LOGS);
        else if (log == BonfireLogs.MAGIC)
            Achievements.doProgress(player, Achievements.AchievementData.BURN_2500_MAGIC_LOGS);
        Achievements.finishAchievement(player, Achievements.AchievementData.LIGHT_A_FIRE);

        player.message("You add a log to the fire.", true);
        StrangeRocks.handleStrangeRocks(player, Skill.FIREMAKING);

        if (count++ == 4 && !player.getEffects().hasActiveEffect(Effects.BONFIRE)) {
            player.getEffects().startEffect(new Effect(Effects.BONFIRE, log.getBoostTime() * 100));
            int percentage = (int) (getBonfireBoostMultiplier(player) * 100 - 100);
            player.message("The bonfire's warmth increases your maximum health by " + percentage + "%. This will last " + log.getBoostTime() + " minutes.", 0x00ff00);
        }
        return 4;
    }

    public static double getBonfireBoostMultiplier(Player player) {
        int fmLvl = player.getSkills().getLevel(Skill.FIREMAKING);
        if (fmLvl >= 90)
            return 1.1;
        if (fmLvl >= 80)
            return 1.09;
        if (fmLvl >= 70)
            return 1.08;
        if (fmLvl >= 60)
            return 1.07;
        if (fmLvl >= 50)
            return 1.06;
        if (fmLvl >= 40)
            return 1.05;
        if (fmLvl >= 30)
            return 1.04;
        if (fmLvl >= 20)
            return 1.03;
        if (fmLvl >= 10)
            return 1.02;
        return 1.01;

    }

    @Override
    public void stop(final Player player) {
    }

}