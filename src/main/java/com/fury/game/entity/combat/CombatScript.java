package com.fury.game.entity.combat;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.content.skill.Skill;
import com.fury.game.entity.character.combat.CombatDefinitions;
import com.fury.game.entity.character.combat.CombatIcon;
import com.fury.game.entity.character.combat.Hit;
import com.fury.game.entity.character.combat.effects.Effects;
import com.fury.game.entity.character.player.content.BonusManager;
import com.fury.game.node.entity.actor.figure.player.PlayerCombat;
import com.fury.game.npc.bosses.nex.Nex;
import com.fury.game.npc.familiar.impl.SteelTitan;
import com.fury.game.system.files.loaders.npc.MobCombatDefinitions;
import com.fury.game.world.GameWorld;
import com.fury.game.world.update.flag.block.HitMask;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.util.Utils;

public abstract class CombatScript {

    public abstract Object[] getKeys();
    public abstract int attack(Mob mob, Figure target);

    public static void delayHit(Mob mob, int delay, final Figure target, final Hit... hits) {
        mob.getMobCombat().addAttackedByDelay(target);
        GameWorld.schedule(delay, () -> {
            for (Hit hit : hits) {
                Mob n = (Mob) hit.getSource();
                if (n.isDead() || n.getFinished() || target.isDead() || target.getFinished())
                    return;
                target.getCombat().applyHit(hit);
                n.getMobCombat().performDefenceAnimation(target);
                if (!(n instanceof Nex)
                        && hit.getCombatIcon() == CombatIcon.MAGIC
                        && hit.getDamage() == 0)
                    target.perform(new Graphic(85, 0, 100));
                if (target.isPlayer()) {
                    Player p2 = (Player) target;
                    if (!n.isCantSetTargetAutoRetaliate() && p2.isAutoRetaliate() && !p2.getActionManager().hasAction() && !p2.getMovement().hasWalkSteps()) {
                        p2.getPacketSender().sendInterfaceRemoval();
                        if(p2.getCombat() instanceof PlayerCombat)
                            ((PlayerCombat) p2.getCombat()).target(n);
                    }

                } else {
                    Mob np = (Mob) target;
                    if (!np.getCombat().isInCombat() || np.canBeAttackedByAutoRetaliate())
                        np.setTarget(n);
                }

                if (hit.getCombatIcon() == CombatIcon.MELEE && target.getEffects().hasActiveEffect(Effects.STAFF_OF_LIGHT))
                    target.graphic(2320);
            }
        });
    }

    public static Hit getRangeHit(Mob mob, int damage) {
        return new Hit(mob, damage, HitMask.RED, CombatIcon.RANGED);
    }

    public static Hit getMagicHit(Mob mob, int damage) {
        return new Hit(mob, damage, HitMask.RED, CombatIcon.MAGIC);
    }

    public static Hit getRegularHit(Mob mob, int damage) {
        return new Hit(mob, damage, HitMask.RED, CombatIcon.NONE);
    }

    public static Hit getMeleeHit(Mob mob, int damage) {
        return new Hit(mob, damage, HitMask.RED, CombatIcon.MELEE);
    }

    public static int getRandomMaxHit(Mob mob, int attackStyle, Figure target) {
        return getRandomMaxHit(mob, mob.getCombatDefinition().getMaxHit(), attackStyle, target);
    }

    public static int getRandomMaxHit(Mob mob, int maxHit, int attackStyle, Figure target) {
        int[] bonuses = mob.getBonuses();
        double att = bonuses == null ? 0 : attackStyle == MobCombatDefinitions.RANGE ? bonuses[CombatDefinitions.RANGE_ATTACK] : attackStyle == MobCombatDefinitions.MAGE ? bonuses[CombatDefinitions.MAGIC_ATTACK] : bonuses[CombatDefinitions.STAB_ATTACK];
        double def;
        if (target.isPlayer()) {
            Player p2 = (Player) target;
            def = p2.getSkills().getLevel(Skill.DEFENCE) + (attackStyle == MobCombatDefinitions.RANGE ? 1.25 : attackStyle == MobCombatDefinitions.MAGE ? 2.0 : 1.0) * p2.getBonusManager().getDefenceBonus()[attackStyle == MobCombatDefinitions.RANGE ? BonusManager.DEFENCE_RANGE : attackStyle == MobCombatDefinitions.MAGE ? BonusManager.DEFENCE_MAGIC : BonusManager.DEFENCE_STAB];
            def *= p2.getPrayer().getDefenceMultiplier();
            if (attackStyle == MobCombatDefinitions.MELEE)
                if (p2.getFamiliar() instanceof SteelTitan)
                    def *= 1.15;
        } else {
            Mob n = (Mob) target;
            def = n.getBonuses() == null ? 0 : n.getBonuses()[attackStyle == MobCombatDefinitions.RANGE ? CombatDefinitions.RANGE_DEF : attackStyle == MobCombatDefinitions.MAGE ? CombatDefinitions.MAGIC_DEF : CombatDefinitions.STAB_DEF];
        }
        double prob = att / def;
        if (prob > 0.90) // max, 90% prob hit so even lvl 138 can miss at lvl 3
            prob = 0.90;
        else if (prob < 0.05) // minimum 5% so even lvl 3 can hit lvl 138
            prob = 0.05;

        //Decreases chance of being hit by 2%
        if (target.getEffects().hasActiveEffect(Effects.SHADOW_FRUIT))
            prob *= 1.02;

        if (prob < Math.random())
            return 0;
        return Utils.getRandom(maxHit);
    }
}
