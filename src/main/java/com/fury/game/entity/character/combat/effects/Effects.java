package com.fury.game.entity.character.combat.effects;

import com.fury.cache.Revision;
import com.fury.game.container.impl.equip.Slot;
import com.fury.game.content.skill.member.herblore.Drinkables;
import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.game.entity.character.combat.CombatIcon;
import com.fury.game.entity.character.combat.Hit;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.npc.bosses.nex.Nex;
import com.fury.game.world.update.flag.Flag;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.game.world.update.flag.block.HitMask;
import com.fury.util.Utils;

/**
 * Created by Greg on 16/01/2017.
 */
public enum Effects {
    POISON(EffectType.HIT_MARK) {
        @Override
        public boolean canStartEffect(Effect effect, Figure figure) {
            if (figure.getEffects().hasActiveEffect(Effects.ANTIPOISON))
                return false;
            if (figure.isPlayer()) {
                Player p = (Player) figure;
                if (p.getEquipment().get(Slot.SHIELD).getId() == 18340)
                    return false;
                p.message("You have been poisoned.", 0x00ff00);
            }
            return !figure.isPoisonImmune();
        }
    },

    ANTIPOISON(EffectType.BUFF) {
        @Override
        public boolean canStartEffect(Effect effect, Figure figure) {
            figure.getEffects().removeEffect(Effects.POISON);
            return true;
        }
    },

    MORRIGAN_AXE(EffectType.DEBUFF),

    STAFF_OF_LIGHT(EffectType.BUFF) {
        @Override
        public void onRemoval(Figure figure) {
            if (figure.isPlayer())
                ((Player) figure).message("The power of the light fades. Your resistance to melee attacks return to normal.");
        }
    },

    SHADOW_EFFECT(EffectType.DEBUFF),

    SMOKE_EFFECT(EffectType.DEBUFF),

    MIASMIC_EFFECT(EffectType.DEBUFF),

    CHARGE(EffectType.BUFF){
        @Override
        public void onStart(Figure figure) {
            if(figure instanceof Player)
                ((Player) figure).message("You feel charged with magic power.");
        }

        @Override
        public void onRemoval(Figure figure) {
            if(figure instanceof Player)
                ((Player) figure).message("Your magical charge fades away.");
        }
    },

    CONFUSE_EFFECT(EffectType.DEBUFF) {
        @Override
        public boolean canStartEffect(Effect effect, Figure figure) {
            return !figure.getEffects().hasActiveEffect(STAGGER_EFFECT);
        }
    },

    WEAKEN_EFFECT(EffectType.DEBUFF) {
        @Override
        public boolean canStartEffect(Effect effect, Figure figure) {
            return !figure.getEffects().hasActiveEffect(ENFEEBLE_EFFECT);
        }
    },

    CURSE_EFFECT(EffectType.DEBUFF) {
        @Override
        public boolean canStartEffect(Effect effect, Figure figure) {
            return !figure.getEffects().hasActiveEffect(VULNERABILITY_EFFECT);
        }
    },

    VULNERABILITY_EFFECT(EffectType.DEBUFF){
        @Override
        public boolean canStartEffect(Effect effect, Figure figure) {
            figure.getEffects().removeEffect(CURSE_EFFECT);
            return true;
        }
    },

    ENFEEBLE_EFFECT(EffectType.DEBUFF){
        @Override
        public boolean canStartEffect(Effect effect, Figure figure) {
            figure.getEffects().removeEffect(WEAKEN_EFFECT);
            return true;
        }
    },

    STAGGER_EFFECT(EffectType.DEBUFF){
        @Override
        public boolean canStartEffect(Effect effect, Figure figure) {
            figure.getEffects().removeEffect(CONFUSE_EFFECT);
            return true;
        }
    },

    INCREASE_CRIT_CHANCE(EffectType.BUFF),

    CASTLE_WARS_BRACE(EffectType.BUFF),

    MODIFY_ACCURACY(EffectType.BUFF),

    DRAGON_BATTLEAXE(EffectType.BUFF),

    BONFIRE(EffectType.BUFF) {
        @Override
        public void onRemoval(Figure figure) {
            if (figure.isPlayer())
                ((Player) figure).message("The health boost you received from stoking a bonfire has run out.", 0xff0000);
        }
    },

    CONSTITUTION_CRYSTAL(EffectType.BUFF) {
        @Override
        public void onRemoval(Figure figure) {
            if (figure.isPlayer()) {
                figure.getTemporaryAttributes().remove("FightKilnCrystal");
                ((Player) figure).message("The power of the crystal dwindles and your hitpoints prowess returns to normal.", 0x7e2217);
            }
        }
    },

    OVERLOAD(EffectType.BUFF) {
        @Override
        public void onRemoval(Figure figure) {
            if (figure.isPlayer())
                Drinkables.resetOverLoadEffect((Player) figure);
        }
    },

    PRAYER_RENEWAL(EffectType.BUFF) {
        @Override
        public void onRemoval(Figure figure) {
            if (figure.isPlayer())
                ((Player) figure).message("Your prayer renewal has ended.", 0x0000FF);
        }
    },

    HEAL(EffectType.HIT_MARK),

    BOUND_IMMUNITY(EffectType.DEBUFF),

    STUN_IMMUNITY(EffectType.DEBUFF),

    STUNNED(EffectType.DEBUFF) {

        @Override
        public boolean canStartEffect(Effect effect, Figure figure) {
            //if (e.getSize() == 1)//TODO not sure if this is used (old stun gfx)
            //	e.perform(new Graphics(254, 0, 92));
            return !figure.isStunImmune();
        }
    },

    PROTECTION_DISABLED(EffectType.DEBUFF) {
        @Override
        public boolean canStartEffect(Effect effect, Figure figure) {
            if (figure.isNpc())
                return true;
            ((Player) figure).getPrayer().closeProtectionPrayers();
            return true;
        }
    },

    TELEPORT_BLOCK(EffectType.DEBUFF),

    SKULL(EffectType.DEBUFF) {

        @Override
        public void onStart(Figure figure) {
            figure.getUpdateFlags().add(Flag.APPEARANCE);
        }

        @Override
        public void onRemoval(Figure figure) {
            figure.getUpdateFlags().add(Flag.APPEARANCE);
        }
    },

    VIRUS(EffectType.DEBUFF) {
        @Override
        public boolean canStartEffect(Effect effect, Figure figure) {
            figure.forceChat("*Cough*");
            return true;
        }

        @Override
        public void onRemoval(Figure figure) {
            if (figure.isNpc())
                return;
            if (figure.isPlayer())
                ((Player) figure).message("The smoke clouds around you dissipate.");
        }
    },

    BLOOD_SACRIFICE(EffectType.DEBUFF) {
        @Override
        public boolean canStartEffect(Effect effect, Figure figure) {
            if (figure.isNpc())
                return true;
            Player player = (Player) figure;
            player.message("Nex has marked you as a sacrifice, RUN!", 0x480000);
//                player.getAppearence().setGlowRed(true);
            return true;
        }

        @Override
        public void onRemoval(Figure figure) {
            if (figure.isNpc())
                return;
            Effect currentEffect = figure.getEffects().getEffectForType(BLOOD_SACRIFICE);
            if (currentEffect != null) {
                Player player = (Player) figure;
                Nex nex = (Nex) currentEffect.getArguments()[0];
                if (Utils.isOnRange(nex, player, 3)) {
                    nex.perform(new Animation(17414, Revision.PRE_RS3));
                    nex.graphic(3375);
                    nex.getHealth().heal(player.getHealth().getHitpoints());

                    player.message("You didn't make it far enough in time - Nex fires a punishing attack!");
                    player.getCombat().applyHit(new Hit(nex, (int) (player.getMaxConstitution() * 0.1), HitMask.RED, CombatIcon.NONE));
                }
            }
        }
    },

    FIRE_IMMUNITY(EffectType.BUFF) {
        @Override
        public boolean canStartEffect(Effect effect, Figure figure) {
            Effect currentEffect = figure.getEffects().getEffectForType(SUPER_FIRE_IMMUNITY);
            if (currentEffect != null)
                return false;
            return true;
        }
    },

    SUPER_FIRE_IMMUNITY(EffectType.BUFF) {
        @Override
        public boolean canStartEffect(Effect effect, Figure figure) {
            Effect currentEffect = figure.getEffects().getEffectForType(FIRE_IMMUNITY);
            if (currentEffect != null)
                figure.getEffects().removeEffect(Effects.FIRE_IMMUNITY);
            return true;
        }

        @Override
        public void onRemoval(Figure figure) {
            if (figure.isPlayer())
                ((Player) figure).message("Your resistance to dragonfire has run out.", 0x480000);
        }
    },

    EVIL_TREE_BUFF(EffectType.BUFF) {
        @Override
        public void onRemoval(Figure figure) {
            if (figure.isPlayer())
                ((Player) figure).message("Your magical log-banking ability has warn off.", 0xffffff);
        }
    },

    WEAPON_POISON(EffectType.BUFF) {
        @Override
        public boolean canStartEffect(Effect effect, Figure figure) {
            Effect currentEffect = figure.getEffects().getEffectForType(WEAPON_POISON);
            if (currentEffect != null && currentEffect.getCycle() > effect.getCycle())
                return false;
            return true;
        }
    },

    SHADOW_FRUIT(EffectType.FRUIT) {
        @Override
        public void onStart(Figure figure) {
            if(figure.isPlayer())
                ((Player) figure).message("The fruit magically decreases the chance of being hit by enemies.", 0x3f3f45);
        }

        @Override
        public void onRemoval(Figure figure) {
            if(figure.isPlayer())
                ((Player) figure).message("The effects of your shadow fruit have warn off.", 0x3f3f45);
        }
    },
    IGNEOUS_FRUIT(EffectType.FRUIT) {
        @Override
        public void onStart(Figure figure) {
            if(figure.isPlayer())
                ((Player) figure).message("The fruit will decrease damage taken by 2% for a minute.", 0x6d6d64);
        }

        @Override
        public void onRemoval(Figure figure) {
            if(figure.isPlayer())
                ((Player) figure).message("The effects of your igneous fruit have warn off.", 0x6d6d64);
        }
    },
    CANNIBAL_FRUIT(EffectType.FRUIT) {
        @Override
        public void onStart(Figure figure) {
            if(figure.isPlayer())
                ((Player) figure).message("Your chances of hitting have been increased by 2%", 0x784d09);
        }

        @Override
        public void onRemoval(Figure figure) {
            if(figure.isPlayer())
                ((Player) figure).message("The effects of your cannibal fruit have warn off.", 0x784d09);
        }
    },
    AQUATIC_FRUIT(EffectType.FRUIT) {
        @Override
        public void onStart(Figure figure) {
            if(figure.isPlayer())
                ((Player) figure).message("All fires fuelled will receive double favour points for the next minute.", 0x666f6f);
        }

        @Override
        public void onRemoval(Figure figure) {
            if(figure.isPlayer())
                ((Player) figure).message("The effects of your aquatic fruit have warn off.", 0x666f6f);
        }
    },
    AMPHIBIOUS_FRUIT(EffectType.FRUIT) {
        @Override
        public void onStart(Figure figure) {
            if(figure.isPlayer())
                ((Player) figure).message("You will receive double favour points for cutting mutated roots.", 0x88ac58);
        }

        @Override
        public void onRemoval(Figure figure) {
            if(figure.isPlayer())
                ((Player) figure).message("The effects of your amphibious fruit have warn off.", 0x88ac58);
        }
    },
    CARRION_FRUIT(EffectType.FRUIT) {
        @Override
        public void onStart(Figure figure) {
            if(figure.isPlayer())
                ((Player) figure).message("Mutated Jadinkoes will start dropping more vines.", 0x784430);
        }

        @Override
        public void onRemoval(Figure figure) {
            if(figure.isPlayer())
                ((Player) figure).message("The effects of your carrion fruit have warn off.", 0x784430);
        }
    },
    DISEASED_FRUIT(EffectType.FRUIT) {
        @Override
        public void onStart(Figure figure) {
            if(figure.isPlayer())
                ((Player) figure).message("Mutated Jadinkoes will drop double excrescence for the next minute.", 0x7b724d);
        }

        @Override
        public void onRemoval(Figure figure) {
            if(figure.isPlayer())
                ((Player) figure).message("The effects of your diseased fruit have warn off.", 0x7b724d);
        }
    },
    CAMOUFLAGED_FRUIT(EffectType.FRUIT) {
        @Override
        public void onStart(Figure figure) {
            if(figure.isPlayer())
                ((Player) figure).message("You will be immune from damage from roots for 60 seconds.", 0x3c411a);
        }

        @Override
        public void onRemoval(Figure figure) {
            if(figure.isPlayer())
                ((Player) figure).message("The effects of your camouflaged fruit have warn off.", 0x3c411a);
        }
    },
    DRACONIC_FRUIT(EffectType.FRUIT) {
        @Override
        public void onStart(Figure figure) {
            if(figure.isPlayer())
                ((Player) figure).message("You max hit has been increased temporarily by 2%", 0x7d2a25);
        }

        @Override
        public void onRemoval(Figure figure) {
            if(figure.isPlayer())
                ((Player) figure).message("The effects of your draconic fruit have warn off.", 0x7d2a25);
        }
    },

    MODIFY_DAMAGE(EffectType.BUFF);

    public boolean canStartEffect(Effect effect, Figure figure) {
        return true;
    }

    public void onStart(Figure figure) {

    }

    public void onRemoval(Figure figure) {

    }

    private EffectType action;

    Effects(EffectType action) {
        this.action = action;
    }

    public EffectType getAction() {
        return action;
    }
}
