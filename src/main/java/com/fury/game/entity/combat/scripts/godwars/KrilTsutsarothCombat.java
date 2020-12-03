package com.fury.game.entity.combat.scripts.godwars;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.Projectile;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.content.skill.Skill;
import com.fury.game.entity.character.combat.prayer.Prayer;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.combat.CombatScript;
import com.fury.game.node.entity.actor.figure.ProjectileManager;
import com.fury.game.system.files.loaders.npc.MobCombatDefinitions;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.util.Utils;

public class KrilTsutsarothCombat extends CombatScript {
    @Override
    public Object[] getKeys() {
        return new Object[] { 6203 };
    }

    @Override
    public int attack(Mob mob, Figure target) {
        final MobCombatDefinitions defs = mob.getCombatDefinition();
        if (Utils.getRandom(4) == 0) {
            switch (Utils.getRandom(8)) {
                case 0:
                    mob.forceChat("Attack them, you dogs!");
                    break;
                case 1:
                    mob.forceChat("Forward!");
                    break;
                case 2:
                    mob.forceChat("Death to Saradomin's dogs!");
                    break;
                case 3:
                    mob.forceChat("Kill them, you cowards!");
                    break;
                case 4:
                    mob.forceChat("The Dark One will have their souls!");
//                    npc.playSound(3229, 2);
                    break;
                case 5:
                    mob.forceChat("Zamorak curse them!");
                    break;
                case 6:
                    mob.forceChat("Rend them limb from limb!");
                    break;
                case 7:
                    mob.forceChat("No retreat!");
                    break;
                case 8:
                    mob.forceChat("Flay them all!");
                    break;
            }
        }
        int attackStyle = Utils.getRandom(2);
        switch (attackStyle) {
            case 0:// magic flame attack
                mob.animate(6944);
                mob.graphic(1210);
                for (Figure t : mob.getPossibleTargets()) {
                    delayHit(mob, 1, t, getMagicHit(mob, getRandomMaxHit(mob, 300, MobCombatDefinitions.MAGE, t)));
                    ProjectileManager.send(new Projectile(mob, t, 1211, 41, 16, 41, 35, 16, 0));
                    if (Utils.getRandom(4) == 0)
                        t.getEffects().makePoisoned(168);
                }
                break;
            case 1:// main attack
            case 2:// melee attack
                int damage = 463;// normal
                if (Utils.getRandom(3) == 0 && target.isPlayer() && (((Player) target).getPrayer().usingPrayer(0, Prayer.PROTECT_FROM_MELEE) || ((Player) target).getPrayer().usingPrayer(1, Prayer.DEFLECT_MELEE))) {
                    Player player = (Player) target;
                    damage = 497;
                    mob.forceChat("YARRRRRRR!");
                    player.getSkills().drain(Skill.PRAYER, Math.round(damage / 20));
                    player.setPrayerDelay(Utils.getRandom(5) + 5);
                    player.message("K'ril Tsutsaroth slams through your protection prayer, leaving you feeling drained.");
                }
                mob.perform(new Animation(defs.getAttackAnim()));
                delayHit(mob, 0, target, getMeleeHit(mob, getRandomMaxHit(mob, damage, MobCombatDefinitions.MELEE, target)));
                break;
        }
        return defs.getAttackDelay();
    }
}
