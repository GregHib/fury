package com.fury.game.npc.familiar.impl;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.Projectile;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.free.fishing.Fishing;
import com.fury.game.content.skill.member.summoning.impl.Summoning;
import com.fury.game.entity.character.combat.CombatIcon;
import com.fury.game.entity.character.combat.Hit;
import com.fury.core.model.item.Item;
import com.fury.game.node.entity.actor.figure.ProjectileManager;
import com.fury.game.npc.familiar.Familiar;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.HitMask;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.util.Misc;

public class GraniteLobster extends Familiar {

    private int forageTicks;

    public GraniteLobster(Player owner, Summoning.Pouches pouch, Position tile) {
        super(owner, pouch, tile);
    }

    @Override
    public void processNpc() {
        super.processNpc();
        boolean isFishing = getOwner().getActionManager().getAction() != null && getOwner().getActionManager().getAction() instanceof Fishing;
        if (isFishing) {
            forageTicks++;
            if (forageTicks == 300)
                giveReward();
        }
    }

    private void giveReward() {
        boolean isShark = Misc.random(3) == 0;
        int foragedItem = isShark ? 383 : 371;
        if (!isShark)
            getOwner().getSkills().addExperience(Skill.FISHING, 30);
        getBeastOfBurden().add(new Item(foragedItem, 1));
        forageTicks = 0;
    }

    @Override
    public String getSpecialName() {
        return "Crushing Claw";
    }

    @Override
    public String getSpecialDescription() {
        return "May inflict up to 140 life points of magic damage and temporarily decrease an opponent's Defence by five levels.";
    }

    @Override
    public int getStoreSize() {
        return 30;
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
        final Familiar npc = this;
        getOwner().graphic(1316);
        getOwner().animate(7660);
        animate(8118);
        perform(new Graphic(1351));
        Projectile projectile = new Projectile(npc, target, 1352, 34, 16, 30, 35, 16, 0);
        ProjectileManager.send(projectile);
        GameWorld.schedule(projectile.getTickDelay(), () -> {
            if (Misc.getRandom(5) == 0)
                if (target.isPlayer())
                    ((Player) target).getSkills().drain(Skill.DEFENCE, 5);
            target.getCombat().applyHit(new Hit(getOwner(), Misc.random(140), HitMask.RED, CombatIcon.MELEE));
            target.perform(new Graphic(1353));
        });
        return true;
    }
}
