package com.fury.game.npc.familiar.impl;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.Projectile;
import com.fury.core.model.node.entity.actor.figure.player.Player;
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

public class ArcticBear extends Familiar {

    public ArcticBear(Player owner, Summoning.Pouches pouch, Position tile) {
        super(owner, pouch, tile);
    }

    @Override
    public String getSpecialName() {
        return "Arctic Blast";
    }

    @Override
    public String getSpecialDescription() {
        return "Can hit a maximum of 150 damage, with a chance of stunning the opponent.";
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
    public boolean submitSpecial(Object object) { // TODO find real animation of bear
        final Figure target = (Figure) object;
        final Familiar npc = this;
        getOwner().graphic(1316);
        getOwner().animate(7660);
        animate(4929);
        perform(new Graphic(1405));
        animate(-1);
        ProjectileManager.send(new Projectile(npc, target, 1406, 34, 16, 30, 35, 16, 0));
        GameWorld.schedule(2, () -> {
            target.getCombat().applyHit(new Hit(getOwner(), Misc.random(150), HitMask.RED, CombatIcon.MAGIC));
            target.graphic(1407);
        });
        return true;
    }
}
