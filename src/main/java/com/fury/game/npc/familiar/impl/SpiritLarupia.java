package com.fury.game.npc.familiar.impl;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.Projectile;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.member.summoning.impl.Summoning;
import com.fury.game.entity.character.combat.CombatIcon;
import com.fury.game.entity.character.combat.Hit;
import com.fury.game.node.entity.actor.figure.ProjectileManager;
import com.fury.game.npc.familiar.Familiar;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.HitMask;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.util.Misc;

public class SpiritLarupia extends Familiar {

    public SpiritLarupia(Player owner, Summoning.Pouches pouch, Position tile) {
        super(owner, pouch, tile);
    }

    @Override
    public String getSpecialName() {
        return "Rending";
    }

    @Override
    public String getSpecialDescription() {
        return "Attacks the player's opponent with a magic attack, and also drains opponent's Strength. ";
    }

    @Override
    public int getStoreSize() {
        return 0;
    }

    @Override
    public int getSpecialAttackEnergy() {
        return 6;
    }

    @Override
    public SpecialAttack getSpecialAttack() {
        return SpecialAttack.ENTITY;
    }

    @Override
    public boolean submitSpecial(Object object) {
        final Figure target = (Figure) object;
        Player player = getOwner();
        final int damage = Misc.getRandom(107);
        graphic(1370);
        animate(7919);
        player.animate(7660);
        player.perform(new Graphic(1316));
        Projectile projectile = new Projectile(this, target, 1371, 34, 16, 30, 35, 16, 0);
        ProjectileManager.send(projectile);
        GameWorld.schedule(projectile.getTickDelay(), () -> {
            if (damage > 20)
                if (target.isPlayer())
                    ((Player) target).getSkills().drain(Skill.STRENGTH, damage / 20);
            target.getCombat().applyHit(new Hit(getOwner(), damage, HitMask.RED, CombatIcon.MAGIC));
        });
        return true;
    }
}
