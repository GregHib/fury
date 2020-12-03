package com.fury.game.content.controller.impl;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.engine.task.executor.FadingScreen;
import com.fury.game.content.controller.Controller;
import com.fury.game.content.skill.Skill;
import com.fury.game.entity.character.combat.CombatIcon;
import com.fury.game.entity.character.combat.Hit;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.character.player.info.DonorStatus;
import com.fury.game.entity.object.GameObject;
import com.fury.game.node.entity.actor.figure.player.Variables;
import com.fury.game.node.entity.actor.object.ObjectHandler;
import com.fury.game.npc.bosses.bork.Bork;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;
import com.fury.game.world.map.instance.MapInstance;
import com.fury.game.world.update.flag.block.HitMask;
import com.fury.util.Misc;

public class BorkController extends Controller {

    private static final Position OUTSIDE = new Position(3143, 5545);

    public static void enterBork(Player player) {
        long hour = 60 * 60 * 1000;
        long wait = DonorStatus.get(player).getBorkTime() * hour;
        if(Misc.currentTimeMillis() - player.getVars().getLong(Variables.LAST_BORK_DEFEAT) <= wait) {
            player.message("The portal appears to have stopped working for now.");
            long difference = Misc.currentTimeMillis() - player.getVars().getLong(Variables.LAST_BORK_DEFEAT);
            int hours = (int) ((wait - difference) / hour);
            player.message("Perhaps you should return " + (hours == 0 ? "later" : " in " + hours + " hours") + "?");
            return;
        }
        player.getControllerManager().startController(new BorkController());
    }

    private MapInstance instance;
    private Mob surokMagis;
    private Bork bork;
    private boolean earthquake;
    private int timer;

    @Override
    public void start() {
        enter();
    }

    @Override
    public boolean processObjectClick1(GameObject object) {
        if(object.getId() == 29537) {
            leave(2);
            return false;
        }
        return true;
    }

    @Override
    public boolean processNPCClick1(Mob mob) {
        if(mob.getId() == 7136 || mob.getId() == 7137) {
            player.message("Your attack has no effect.");
            return false;
        }
        return true;
    }

    public void enter() {
        instance = new MapInstance(385, 690);
        player.getMovement().lock();
        player.animate(17803);
        final long time = FadingScreen.fade(player);
        instance.load(() -> {
            player.moveTo(instance.getTile(25, 17));
            surokMagis = GameWorld.getMobs().spawn(7137, instance.getTile(22, 20), true);
            surokMagis.setCantSetTargetAutoRetaliate(true);
            bork = new Bork(instance.getTile(12, 15), BorkController.this);
            startFight();
        });
    }

    //0 - logout
    //1 - teleport / death
    //2 - leave
    public void leave(int type) {
        player.stopAll();
        if(type != 0) {
            if(type == 1)
                player.getMovement().lock(3);
            else
                ObjectHandler.useStairs(player, 17803, OUTSIDE, 2, 3);
            removeController();
        } else
            player.moveTo(OUTSIDE);
        instance.destroy(null);
    }

    @Override
    public void forceClose() {
        leave(1);
    }

    @Override
    public boolean logout() {
        leave(0);
        return true;
    }

    @Override
    public boolean login() {
        player.moveTo(OUTSIDE);
        return true;
    }

    @Override
    public void magicTeleported(int type) {
        leave(1);
    }

    @Override
    public boolean sendDeath() {
        player.getMovement().lock(8);
        player.stopAll();
        player.appendDeath(OUTSIDE);
        return false;

    }

    public void startFight() {
        player.getMovement().unlock();
        bork.setCantInteract(false);
        surokMagis.setTarget(player);
    }

    public void startEarthquake() {
        player.getMovement().unlock();
        player.message("Something is shaking the whole cavern! You should get out of here quick!");
        earthquake = true;
        timer = 30;
//        player.getPacketSender().sendCameraShake(3, 12, 25, 12, 25);
    }

    @Override
    public void process() {
        if(!earthquake)
            return;
        if (timer > 0) {
            timer--;
            return;
        }
        player.getCombat().applyHit(new Hit(player, Misc.random(499) + 1, HitMask.RED, CombatIcon.NONE));
        timer = 30;
    }

    public void spawnMinions() {
        player.message("Bork strikes the ground with his axe.");
        bork.setMinions();
    }

    public void killBork() {
        bork = null;
        surokMagis.sendDeath(player);
        player.getSkills().addExperience(Skill.SLAYER, player.getVars().getLong(Variables.LAST_BORK_DEFEAT) == 0 ? 5000 : 3000);
        player.getVars().set(Variables.LAST_BORK_DEFEAT, Misc.currentTimeMillis());
        startEarthquake();
    }

    public MapInstance.Stages getStage() {
        return instance.getStage();
    }

}
