package com.fury.game.entity.combat.scripts;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.Projectile;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.combat.CombatScript;
import com.fury.game.node.entity.actor.figure.ProjectileManager;
import com.fury.game.system.files.loaders.npc.MobCombatDefinitions;
import com.fury.game.world.GameWorld;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.util.Misc;
import com.fury.util.Utils;

public class RevenantCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[] { 13465, 13466, 13467, 13468, 13469, 13470, 13471, 13472, 13473, 13474, 13475, 13476, 13477, 13478, 13479, 13480, 13481 };
    }

    public int getMagicAnimation(Mob mob) {
        switch (mob.getId()) {
            case 13465:
                return 7500;
            case 13466:
            case 13467:
            case 13468:
            case 13469:
                return 7499;
            case 13470:
            case 13471:
                return 7506;
            case 13472:
                return 7503;
            case 13473:
                return 7507;
            case 13474:
                return 7496;
            case 13475:
                return 7497;
            case 13476:
                return 7515;
            case 13477:
                return 7498;
            case 13478:
                return 7505;
            case 13479:
                return 7515;
            case 13480:
                return 7508;
            case 13481:
            default:
                // melee emote, better than 0
                return mob.getCombatDefinition().getAttackAnim();
        }
    }

    public int getRangeAnimation(Mob mob) {
        switch (mob.getId()) {
            case 13465:
                return 7501;
            case 13466:
            case 13467:
            case 13468:
            case 13469:
                return 7513;
            case 13470:
            case 13471:
                return 7519;
            case 13472:
                return 7516;
            case 13473:
                return 7520;
            case 13474:
                return 7521;
            case 13475:
                return 7510;
            case 13476:
                return 7501;
            case 13477:
                return 7512;
            case 13478:
                return 7518;
            case 13479:
                return 7514;
            case 13480:
                return 7522;
            case 13481:
            default:
                // melee emote, better than 0
                return mob.getCombatDefinition().getAttackAnim();
        }
    }

    @Override
    public int attack(final Mob mob, final Figure target) {
        final MobCombatDefinitions defs = mob.getCombatDefinition();
        if (mob.getHealth().getHitpoints() < mob.getMaxConstitution() / 2 && Misc.random(5) == 0) // if lower than 50% hp, 1/5 prob of healing 10%
            mob.getHealth().heal(100);
        int attackStyle = Misc.random(3);
        if (attackStyle == 2) { // checks if can melee
            if (!Utils.isOnRange(mob, target, 0))
                attackStyle = Misc.random(2);
        }
        boolean noDamage = false;
        /*if (target.isPlayer()) {
            Player targetPlayer = (Player) target;
            Long ivulnerability = (Long) targetPlayer.getTemporaryAttributes().get(Key.REVENEANT_IVULNERABILITY);
            if (ivulnerability != null && ivulnerability + 100 > Misc.currentWorldCycle())
                noDamage = true;
        }*/
        switch (attackStyle) {
            case 0: // magic
                int damage = noDamage ? 0 : getRandomMaxHit(mob, MobCombatDefinitions.MAGE, target);
                Projectile projectile = new Projectile(mob, target, 1276, 34, 16, 35, 2, 10, 0);
                ProjectileManager.send(projectile);
                delayHit(mob, projectile.getTickDelay(), target, getMagicHit(mob, damage));
                if (damage > 0) {
                    GameWorld.schedule(projectile.getTickDelay(), () -> {
                        target.perform(new Graphic(1277, 0, 100));
                        if (Misc.random(5) == 0) { // 1/5 prob freezing while maging
                            target.graphic(363);
                            target.getCombat().addFreezeDelay(8000);
                        }
                    });
                }
                mob.perform(new Animation(getMagicAnimation(mob)));
                break;
            case 1: // range
                mob.perform(new Animation(getRangeAnimation(mob)));
                projectile = new Projectile(mob, target, 1278, 34, 16, 35, 2, 10, 0);
                ProjectileManager.send(projectile);
                delayHit(mob, projectile.getTickDelay(), target, getRangeHit(mob, noDamage ? 0 : getRandomMaxHit(mob, MobCombatDefinitions.RANGE, target)));
                break;
            case 2: // melee
                mob.perform(new Animation(defs.getAttackAnim()));
                delayHit(mob, 0, target, getMeleeHit(mob, noDamage ? 0 : getRandomMaxHit(mob, MobCombatDefinitions.MELEE, target)));
                break;
        }
        return mob.getCombat().getAttackSpeed();
    }
}
