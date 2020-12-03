package com.fury.game.entity.character.combat.effects;

import com.fury.core.model.item.Item;
import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.content.combat.Swing;
import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.member.herblore.Drink;
import com.fury.game.content.skill.member.herblore.Drinkables;
import com.fury.game.entity.character.combat.CombatIcon;
import com.fury.game.entity.character.combat.Hit;
import com.fury.game.entity.character.player.actions.ItemMorph;
import com.fury.game.world.update.flag.block.HitMask;
import com.fury.game.world.update.flag.block.graphic.Graphic;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Greg on 18/11/2016.
 */
public class EffectManager {
    private List<Effect> effects = new CopyOnWriteArrayList<>();
    private Figure figure;

    public EffectManager() {
    }

    public void setFigure(Figure figure) {
        this.figure = figure;
    }

    public void startEffect(Effect effect) {
        Effects type = effect.getType();
        if (!type.canStartEffect(effect, figure))
            return;
        Effect currentEffect = getEffectForType(type);
        if (currentEffect != null)
            effects.set(effects.indexOf(currentEffect), effect);
        else
            effects.add(effect);
        refreshBuffEffect(effect, false);
        type.onStart(figure);
    }

    public void process() {
        if (figure.isDead() || figure.getFinished() || effects.isEmpty())
            return;
        for (Effect effect : effects) {
            EffectType action = effect.getType().getAction();
            if (effect.getCycle() != -1) {
                effect.setCycle(effect.getCycle() - 1);
                if (effect.getCycle() <= 0) {
                    removeEffect(effect.getType());
                    continue;
                }
            }
            //already refreshes at both remove and add
            if (figure.isPlayer())
                processBuffTimer(effect, false);
            if (action == EffectType.BUFF) {
                if (figure.isPlayer()) {
                    Player player = (Player) figure;
                    if (effect.getType() == Effects.OVERLOAD) {
                        if (effect.getCycle() % 25 == 0)//15 Seconds
                            Drinkables.applyOverLoadEffect(player);
                    } else if (effect.getType() == Effects.PRAYER_RENEWAL) {
                        if (effect.getCycle() == 50)
                            player.message("Your prayer renewal will wear off in 30 seconds.", 0x0000FF);
                        if (!player.getSkills().isFull(Skill.PRAYER)) {
                            if (getRenewalTime(player) != 0 && effect.getCycle() % getRenewalTime(player) == 0)
                                player.getSkills().restore(Skill.PRAYER, 1);
                            if (effect.getCycle() % 25 == 0)
                                player.graphic(1295);
                        }
                    } else if (effect.getType() == Effects.BONFIRE) {
                        if (effect.getCycle() == 500)
                            player.message("The health boost you received from stoking a bonfire will run out in 5 minutes.", 0xffff00);
                    } else if (effect.getType() == Effects.FIRE_IMMUNITY || effect.getType() == Effects.SUPER_FIRE_IMMUNITY) {
                        if (effect.getCycle() == (effect.getType() == Effects.FIRE_IMMUNITY ? 10 : 20))
                            player.message("Your resistance to dragonfire is about to run out.", 0x480000);
                    }
                }
            } else if (action == EffectType.DEBUFF) {
            } else if (action == EffectType.HIT_MARK) {
                //Not a very good way of doing it, serialization would be better
                HitMask look = (HitMask) effect.getArguments()[0];
                Graphic graphics = (Graphic) effect.getArguments()[1];
                int damage = (int) effect.getArguments()[2];
                int effectDelay = (int) effect.getArguments()[3];
                if (effect.getCycle() % effectDelay == 0) {
                    if (effect.getType() == Effects.POISON) {
                        if (figure.isPlayer()) {
                            Player player = (Player) figure;
                            if (player.getInterfaceId() > 0 || player.getActionManager().getAction() instanceof ItemMorph) {
                                effect.setCycle(effect.getCycle() + 1);
                                return;
                            }
                            if (player.getAuraManager().hasPoisonPurge())
                                look = HitMask.PURPLE;
                        }
                    }
                    if (look == HitMask.PURPLE)
                        figure.getHealth().heal(damage, 0, 0, true);
                    else {
                        Hit hit = new Hit(figure, damage, look, CombatIcon.NONE);
                        if (effect.getArguments().length >= 5) {
                            hit.setSource((Figure) effect.getArguments()[4]);
                        }
                        figure.getCombat().applyHit(hit);
                        if (hit.getSource() != null && hit.getSource().isPlayer() && effect.getArguments().length >= 5)
                            Swing.autoRetaliate((Player) effect.getArguments()[4], figure);
                    }
                    if (graphics != null)
                        figure.perform(graphics);
                }
            }
        }
    }

    public void refreshBuffEffect(Effect effect, boolean remove) {
        if (!figure.isPlayer())
            return;
        Player player = (Player) figure;
        Effects type = effect.getType();
        if (type == Effects.DRAGON_BATTLEAXE) {
//            player.refreshMeleeAttackRating();
//            player.refreshMeleeStrengthRating();
//            player.refreshDefenceRating();
        } else if (type == Effects.BONFIRE) {
            player.getEquipment().refreshConfigs();
        } else if (type == Effects.CONSTITUTION_CRYSTAL) {
            player.getEquipment().refreshConfigs();
        } else if (type == Effects.POISON) {
            player.getPacketSender().sendOrb(0, !remove);
        } else if (type == Effects.ANTIPOISON) {
            player.getPacketSender().sendOrb(0, false);
        }/* else {
            if (type.var != -1 || type.varbit != -1) {
                boolean isVar = type.var != -1 && player.getVarsManager().sendVar(type.var, type == Effects.ANTIPOISON ? -effect.cycle : effect.cycle);
                boolean isVarbit = type.varbit != -1 && player.getVarsManager().sendVarBit(type.varbit, effects.contains(effect) ? 1 : 0);
                if (type == Effects.OVERLOAD) // Required to display.
                    player.getVarsManager().sendVar(4910, 2048);
                if (isVar || isVarbit)
                    processBuffTimer(effect, true);
            }
        }*/
    }

    public void processBuffTimer(Effect effect, boolean refresh) {
        if (!figure.isPlayer())
            return;
        Player player = (Player) figure;
        /*Effects type = effect.type;
        if (type.var == -1 && type.varbit == -1)
            return;
        if (refresh || effect.cycle % 25 == 0) {
            if (type.grMap != -1) {
                *//*if (effect.type == Effects.DEVOTION) {
                    player.getPacketSender().sendCSVarInteger(4098, type.grMap);
                    player.getPacketSender().sendExecuteScript(9379, type.grMap, 0, effect.cycle);
                } else
                    player.getPacketSender().sendExecuteScript(4252, type.grMap, effect.cycle);*//*
            }
            player.updateBuffs();
        }*/
    }

    public void resetEffects() {
        Effect[] e = effects.toArray(new Effect[effects.size()]);
        for (Effect effect : e) {
            effect.setCycle(0);
            refreshBuffEffect(effect, true);
        }
        effects.clear();
    }

    public boolean removeEffect(Effects type) {
        Effect effect = getEffectForType(type);
        if (effect == null)
            return false;
        if (effect.getCycle() > 0)
            effect.setCycle(0);
        type.onRemoval(figure);
        boolean removedEffect = effects.remove(effect);
        refreshBuffEffect(effect, true);
        return removedEffect;
    }

    public void removeEffectsWithAction(EffectType action) {
        for (Effect effect : effects) {
            Effects type = effect.getType();
            if (type.getAction() == action)
                removeEffect(type);
        }
    }

    public boolean hasActiveEffect(Effects type) {
        Effect effect = getEffectForType(type);
        if (effect == null)
            return false;
        return effects.contains(effect);
    }

    public boolean hasActiveEffect(EffectType action) {
        return getEffectForAction(action) != null;
    }

    public Effect getEffectForType(Effects type) {
        for (Effect effect : effects) {
            if (effect.getType() == type)
                return effect;
        }
        return null;
    }

    public Effect getEffectForAction(EffectType action) {
        for (Effect effect : effects) {
            Effects type = effect.getType();
            if (type.getAction() == action)
                return effect;
        }
        return null;
    }

    public static final int getRenewalTime(Player player) {
        int lvl = player.getSkills().getMaxLevel(Skill.PRAYER);
        if (lvl < 200)
            return 0;
        else if (lvl < 500)
            return 1;
        //TODO + 30 extra in the 5mins 10 sec if holy wrench
        return 2;
    }

    public void makePoisoned(int damage) {
        makePoisoned(damage, -1);
    }

    public void makePoisoned(int damage, int cycles) {
        startEffect(new Effect(Effects.POISON, cycles == -1 ? (1000 - damage) / 10 : cycles, HitMask.GREEN, null, damage, 17));
    }

    public static boolean healPoison(Figure figure) {
        if (!figure.isPlayer())
            return false;
        Player player = (Player) figure;
        if (!player.getEffects().hasActiveEffect(Effects.POISON)) {
            player.message("You're not currently in need of curing.");
            return false;
        }

        for (int slot = 0; slot < 28; slot++) {
            Item item = player.getInventory().get(slot);
            if (item == null || (!Drink.ANTIPOISON_POTION.contains(item.getId()) && !Drink.SUPER_ANTIPOISON.contains(item.getId()) && !Drink.ANTIPOISON_FLASK.contains(item.getId()) && !Drink.SUPER_ANTIPOISON_FLASK.contains(item.getId())))
                continue;
            Drinkables.drink(player, item, slot);
            return true;
        }

        if (player.getInventory().contains(new Item(10890))) {
            if (player.getEmotesManager().handleItem(10890))
                return true;
        } else
            player.message("You don't have anything to cure the poison.");
        return false;
    }

    public boolean isEmpty() {
        return effects.isEmpty();
    }

    public List<Effect> getEffects() {
        return effects;
    }
}
