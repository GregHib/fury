package com.fury.game.entity.character.combat.equipment.weapon;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.SpeedProjectile;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.container.impl.equip.Slot;
import com.fury.game.content.global.Achievements;
import com.fury.game.content.global.Achievements.AchievementData;
import com.fury.game.content.skill.Skill;
import com.fury.game.content.combat.CombatType;
import com.fury.game.entity.character.combat.magic.Autocasting;
import com.fury.game.entity.character.player.actions.PlayerCombatAction;
import com.fury.game.node.entity.actor.figure.ProjectileManager;
import com.fury.game.node.entity.actor.figure.player.handles.Settings;
import com.fury.game.world.GameWorld;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.game.world.update.flag.block.graphic.GraphicHeight;
import com.fury.util.Colours;

import java.util.Arrays;

/**
 * Holds constants that hold data for all of the special attacks that can be
 * used.
 *
 * @author lare96
 */
public enum CombatSpecial {
    DRAGON_DAGGER(new int[]{1215, 1231, 5680, 5698}, 25, 1.16, 1.20, CombatType.MELEE, WeaponInterface.DAGGER) {
        @Override
        public void container(Player player, Figure target) {
            player.animate(1062);
            player.perform(new Graphic(252, GraphicHeight.HIGH));

			 /*return new void(player, target, 2, CombatType.MELEE,
                     true);*/
        }
    },
    KORASIS_SWORD(new int[]{19784}, 60, 1.55, 8, CombatType.MELEE, WeaponInterface.SWORD) {
        @Override
        public void container(Player player, Figure target) {

            player.animate(14788);
            player.graphic(1729);

			 /*return new void(player, target, 1, 1, CombatType.MAGIC, true) {
				 @Override
				 public void onHit(int damage, boolean accurate) {
					 target.graphic(2795);
				 }
			 };*/
        }
    },
    MORRIGANS_JAVELIN(new int[]{13879}, 50, 1.40, 1.30, CombatType.RANGED, WeaponInterface.JAVELIN) {
        @Override
        public void container(Player player, Figure target) {

            player.animate(10501);
            player.graphic(1836);

//			 return new void(player, target, 1, CombatType.RANGED, true);
        }
    },
    MORRIGANS_THROWNAXE(new int[]{13883}, 50, 1.38, 1.30, CombatType.RANGED, WeaponInterface.THROWNAXE) {
        @Override
        public void container(Player player, Figure target) {

            player.animate(10504);
            player.graphic(1838);

			 /*return new void(player, target, 1, CombatType.RANGED, true);*/
        }
    },
    GRANITE_MAUL(new int[]{4153, 20084}, 50, 1.21, 1, CombatType.MELEE, WeaponInterface.WARHAMMER) {
        @Override
        public void container(Player player, Figure target) {
            player.animate(1667);
            player.perform(new Graphic(340, GraphicHeight.HIGH));
			 /*player.getCombatBuilder().setAttackTimer(1);
			 return new void(player, target, 1, CombatType.MELEE,
					 true);*/
        }
    },
    ABYSSAL_WHIP(new int[]{4151, 15441, 15442, 15443, 15444, 21371, 21372, 21373, 21374, 21375}, 50, 1, 1, CombatType.MELEE, WeaponInterface.WHIP) {
        @Override
        public void container(Player player, Figure target) {
            player.animate(11971);
            target.perform(new Graphic(2108, GraphicHeight.HIGH));
            if (target.isPlayer()) {
                Player p = (Player) target;
                double totalRunEnergy = p.getSettings().getInt(Settings.RUN_ENERGY) - 25;
                if (totalRunEnergy < 0)
                    totalRunEnergy = 0;
                p.getSettings().set(Settings.RUN_ENERGY, totalRunEnergy);
            }
			 /*return new void(player, target, 1, CombatType.MELEE,
					 false);*/
        }
    },
    DRAGON_LONGSWORD(new int[]{1305}, 25, 1.15, 1.20, CombatType.MELEE, WeaponInterface.LONGSWORD) {
        @Override
        public void container(Player player, Figure target) {
            player.animate(12033);
            player.perform(new Graphic(2117, GraphicHeight.HIGH));

			 /*return new void(player, target, 1, CombatType.MELEE,
					 true);*/
        }
    },
    BARRELSCHEST_ANCHOR(new int[]{10887}, 50, 1.21, 1.30, CombatType.MELEE, WeaponInterface.WARHAMMER) {
        @Override
        public void container(Player player, Figure target) {
            player.animate(5870);
            player.perform(new Graphic(1027, GraphicHeight.MIDDLE));

			 /*return new void(player, target, 1, CombatType.MELEE,
					 true);*/
        }
    },
    SARADOMIN_SWORD(new int[]{11730}, 100, 1.35, 1.2, CombatType.MELEE, WeaponInterface.TWO_HANDED_SWORD) {
        @Override
        public void container(Player player, Figure target) {

            player.animate(11993);
            player.getDirection().setInteracting(target);

			 /*return new void(player, target, 2, CombatType.MAGIC,	true) {
				 @Override
				 public void onHit(int damage, boolean accurate) {
					 target.graphic(1194);
				 }
			 };*/
        }
    },
    VESTAS_LONGSWORD(new int[]{13899}, 25, 1.28, 1.25, CombatType.MELEE, WeaponInterface.LONGSWORD) {
        @Override
        public void container(Player player, Figure target) {
            player.animate(10502);

//			 return new void(player, target, 1, CombatType.MELEE, true);
        }
    },
    VESTAS_SPEAR(new int[]{13905}, 50, 1.26, 1, CombatType.MELEE, WeaponInterface.SPEAR) {
        @Override
        public void container(Player player, Figure target) {
            player.animate(10499);
            player.graphic(1835);
			/* player.getCombatBuilder().setAttackTimer(1);
			 return new void(player, target, 1, CombatType.MELEE,
					 true);*/
        }
    },
    STATIUS_WARHAMMER(new int[]{13902}, 30, 1.25, 1.23, CombatType.MELEE, WeaponInterface.WARHAMMER) {
        @Override
        public void container(Player player, Figure target) {
            player.animate(10505);
            player.graphic(1840);
			 /*return new void(player, target, 1, CombatType.MELEE, true) {
				 @Override
				 public void onHit(int damage, boolean accurate) {
					 if(target.isPlayer() && accurate) {
						 Player t = (Player)target;
						 int currentDef = t.getSkills().getLevel(Skill.DEFENCE);
						 int defDecrease = (int) (currentDef * 0.11);
						 if((currentDef - defDecrease) <= 0 || currentDef <= 0)
							 return;
						 t.getSkills().setLevel(Skill.DEFENCE, defDecrease);
						 t.getPacketSender().sendMessage("Your opponent has reduced your Defence level.");
						 player.getPacketSender().sendMessage("Your hammer forces some of your opponent's defences to break.");
					 }
				 }
			 };*/
        }
    },
    MAGIC_SHORTBOW(new int[]{861}, 55, 1, 1.2, CombatType.RANGED, WeaponInterface.SHORTBOW) {
        @Override
        public void container(Player player, Figure target) {

            player.animate(1074);
            player.perform(new Graphic(250, GraphicHeight.HIGH));
            ProjectileManager.send(new SpeedProjectile(player, target, 249, 44, 3, 43, 31, 0));

            GameWorld.schedule(1, () -> new SpeedProjectile(player, target, 249, 44, 3, 43, 31, 0));

//			 return new void(player, target, 2, CombatType.RANGED,
//					 true);
        }
    },
    MAGIC_LONGBOW(new int[]{859}, 35, 1, 5, CombatType.RANGED, WeaponInterface.LONGBOW) {
        @Override
        public void container(Player player, Figure target) {

            player.animate(426);
            player.perform(new Graphic(250, GraphicHeight.HIGH));
            ProjectileManager.send(new SpeedProjectile(player, target, 249, 44, 3, 43, 31, 0));

//			 return new void(player, target, 1, CombatType.RANGED,
//					 true);
        }
    },
    DARK_BOW(new int[]{11235, 15701, 15702, 15703, 15704}, 55, 1.45, 1.22, CombatType.RANGED, WeaponInterface.LONGBOW) {
        @Override
        public void container(Player player, Figure target) {
            player.animate(426);

            GameWorld.schedule(1, () -> {
                ProjectileManager.send(new SpeedProjectile(player, target, 1099, 44, 3, 43, 31, 0));
                ProjectileManager.send(new SpeedProjectile(player, target, 1099, 60, 3, 43, 31, 0));
            });

            GameWorld.schedule(2, () -> target.perform(new Graphic(1100, GraphicHeight.HIGH)));

//			 return new void(player, target, 2, CombatType.RANGED,
//					 true);
        }
    },
    HAND_CANNON(new int[]{15241}, 45, 1.45, 1.15, CombatType.RANGED, WeaponInterface.SHORTBOW) {
        @Override
        public void container(Player player, Figure target) {
            player.animate(12175);
//			 player.getCombatBuilder().setAttackTimer(8);

            GameWorld.schedule(1, () -> {
                player.graphic(2141);
                ProjectileManager.send(new SpeedProjectile(player, target, 2143, 44, 3, 43, 31, 0));
//					 new CombatHit(player.getCombatBuilder(), new void(player, target, CombatType.RANGED, true)).handleAttack();
//					 player.getCombatBuilder().setAttackTimer(2);
            });
//			 return new void(player, target, 1, 1, CombatType.RANGED,
//					 true);
        }
    },
    DRAGON_BATTLEAXE(new int[]{1377}, 100, 1, 1, CombatType.MELEE, WeaponInterface.BATTLEAXE) {
        @Override
        public void onActivation(Player player, Figure target) {
            player.perform(new Graphic(246, GraphicHeight.LOW));
            player.animate(1056);
            player.forceChat("Raarrrrrgggggghhhhhhh!");
            CombatSpecial.drain(player, DRAGON_BATTLEAXE.drainAmount);
//			 Consumables.drinkStatPotion(player, -1, -1, -1, Skill.STRENGTH.ordinal(), true);
            player.getSkills().drain(Skill.ATTACK, 7);
//			 player.getCombatBuilder().cooldown(true);
        }

        @Override
        public void container(Player player, Figure target) {
            throw new UnsupportedOperationException(
                    "Dragon battleaxe does not have a special attack!");
        }
    },
    STAFF_OF_LIGHT(new int[]{15486, 22207, 22209, 22211, 22213}, 100, 1, 1, CombatType.MELEE, WeaponInterface.LONGSWORD) {
        @Override
        public void onActivation(Player player, Figure target) {
            player.graphic(2319);
            player.animate(12804);
            CombatSpecial.drain(player, STAFF_OF_LIGHT.drainAmount);
//			 player.setStaffOfLightEffect(100);
//			 WorldTasksManager.schedule(new StaffOfLightSpecialAttackTask(player), 1, 1);
            //player.getPacketSender().sendMessage("You are shielded by the spirits of the Staff of light!");
//			 player.getCombatBuilder().cooldown(true);
        }

        @Override
        public void container(Player player, Figure target) {
            throw new UnsupportedOperationException(
                    "Dragon battleaxe does not have a special attack!");
        }
    },
    DRAGON_SPEAR(new int[]{1249, 1263, 5716, 5730}, 25, 1, 1, CombatType.MELEE, WeaponInterface.SPEAR) {
        @Override
        public void container(Player player, Figure target) {
            player.animate(12017);
            //player.graphic(253);

			 /*return new void(player, target, 1, CombatType.MELEE,
					 true) {
				 @Override
				 public void onHit(int damage, boolean accurate) {
					 if(target.isPlayer()) {
						 int moveX = target.getX() - player.getX();
						 int moveY = target.getY() - player.getY();
						 if (moveX > 0)
							 moveX = 1;
						 else if (moveX < 0)
							 moveX = -1;
						 if (moveY > 0)
							 moveY = 1;
						 else if (moveY < 0)
							 moveY = -1;
						 if(target.getMovementQueue().canWalk(moveX, moveY)) {
							 target.getDirection().setInteracting(player);
							 target.getMovementQueue().reset();
							 target.getMovementQueue().walkStep(moveX, moveY);
						 }
					 }
					 target.perform(new Graphic(80, GraphicHeight.HIGH));
					 WorldTasksManager.schedule(new WorldTask() {
						 @Override
						 public void run() {
							 target.getMovementQueue().freeze(6);
						 }
					 }, 1);
				 }
			 };*/
        }
    },
    DRAGON_MACE(new int[]{1434}, 25, 1.29, 1.25, CombatType.MELEE, WeaponInterface.MACE) {
        @Override
        public void container(Player player, Figure target) {
            player.animate(1060);
            player.perform(new Graphic(251, GraphicHeight.HIGH));

//			 return new void(player, target, 1, CombatType.MELEE,
//					 true);
        }
    },
    DRAGON_SCIMITAR(new int[]{4587}, 55, 1.1, 1.1, CombatType.MELEE, WeaponInterface.SCIMITAR) {
        @Override
        public void container(Player player, Figure target) {
            player.animate(12031);
            player.perform(new Graphic(2118, GraphicHeight.HIGH));

			 /*return new void(player, target, 1, CombatType.MELEE,
					 true);*/
        }
    },
    DRAGON_2H_SWORD(new int[]{7158}, 60, 1, 1, CombatType.MELEE, WeaponInterface.TWO_HANDED_SWORD) {
        @Override
        public void container(Player player, Figure target) {
            player.animate(7078);
            player.graphic(1225);

			 /*return new void(player, target, 1, CombatType.MELEE,
					 false) {
				 @Override
				 public void onHit(int damage, boolean accurate) {
					 if (Locations.Location.inMulti(player)) {
						 List<CharacterList> localEntities;

						 Iterator<? extends Figure> it = null;
						 if (target.isPlayer()) {
							 it = player.getLocalPlayers().iterator();
						 } else if (target.isNpc()) {
							 it = player.getLocalMobs().iterator();
						 }

						 for (Iterator<? extends Figure> $it = it; $it.hasNext(); ) {
							 Figure next = $it.next();

							 if (next == null) {
								 continue;
							 }

							 if (next.isNpc()) {
								 Npc n = (Npc) next;
								 if (!n.getDefinition().hasAttackOption() || n.isSummoningNpc()) {
									 continue;
								 }
							 } else {
								 Player p = (Player) next;
								 if (!p.isInWilderness() || !Locations.Location.inMulti(p)) {
									 continue;
								 }
							 }

							 if (next.isWithinDistance(target, 1) && !next.equals(player) && !next.equals(target) && next.getHealth().getHealth() > 0 && next.getHealth().getHealth() > 0) {
								 int calc = Misc.inclusiveRandom(0, player.getCombatBuilder().getContainer().getDamage());
								 next.applyHit(new Hit(calc, HitMask.RED, CombatIcon.RANGED));
								 next.getCombatBuilder().addDamage(player, calc);
								 damage += calc;
								 if (next.getCombatBuilder().getVictim() == null && !next.getCombatBuilder().isAttacking()) {
									 next.getCombatBuilder().setVictim(player);
									 next.getCombatBuilder().attack(player);
								 }
							 }
						 }
					 }
				 }
			 };*/
        }
    },
    DRAGON_HALBERD(new int[]{3204}, 30, 1.07, 1.08, CombatType.MELEE, WeaponInterface.HALBERD) {
        @Override
        public void container(Player player, Figure target) {
            player.animate(1203);
            player.perform(new Graphic(282, GraphicHeight.HIGH));

//			 return new void(player, target, 2, CombatType.MELEE,
//					 true);
        }
    },
    ARMADYL_GODSWORD(new int[]{11694}, 50, 1.43, 1.63, CombatType.MELEE, WeaponInterface.TWO_HANDED_SWORD) {
        @Override
        public void container(Player player, Figure target) {
            player.animate(11989);
            player.graphic(2113);

//			 return new void(player, target, 1, CombatType.MELEE, true);
        }
    },
    ZAMORAK_GODSWORD(new int[]{11700}, 50, 1.25, 1.4, CombatType.MELEE, WeaponInterface.TWO_HANDED_SWORD) {
        @Override
        public void container(Player player, Figure target) {
            player.animate(7070);
            player.graphic(1221);

			 /*return new void(player, target, 1, CombatType.MELEE, true) {
				 @Override
				 public void onHit(int damage, boolean accurate) {
					 if(accurate) {
						 target.graphic(2104);
						 if(!target.isFrozen()) {
							 if(target.getSize() == 1) {
								 target.getMovementQueue().freeze(15);
							 }
						 }
					 }
				 }
			 };*/
        }
    },
    BANDOS_GODSWORD(new int[]{11696}, 100, 1.25, 1.4, CombatType.MELEE, WeaponInterface.TWO_HANDED_SWORD) {
        @Override
        public void container(Player player, Figure target) {
            player.animate(11991);
            player.graphic(2114);

			 /*return new void(player, target, 1, CombatType.MELEE,
					 false) {
				 @Override
				 public void onHit(int damage, boolean accurate) {
					 if(target != null && target.isPlayer() && accurate) {
						 int skillDrain = 1;
						 int damageDrain = (int) (damage * 0.1);
						 if(damageDrain < 0)
							 return;
						 ((Player)target).getSkills().setLevel(Skill.forId(skillDrain), player.getSkills().getLevel(Skill.forId(skillDrain)) - damageDrain);
						 if(((Player)target).getSkills().getLevel(Skill.forId(skillDrain)) < 1)
							 ((Player)target).getSkills().setLevel(Skill.forId(skillDrain), 1);
						 player.getPacketSender().sendMessage("You've drained "+((Player)target).getUsername()+"'s "+Misc.formatText(Skill.forId(skillDrain).toString().toLowerCase())+" level by "+damageDrain+".");
						 ((Player)target).getPacketSender().sendMessage("Your "+Misc.formatText(Skill.forId(skillDrain).toString().toLowerCase())+" level has been drained.");
					 }
				 }
			 };*/
        }
    },
    SARADOMIN_GODSWORD(new int[]{11698}, 50, 1.25, 1.5, CombatType.MELEE, WeaponInterface.TWO_HANDED_SWORD) {
        @Override
        public void container(Player player, Figure target) {
            player.animate(7071);
            player.graphic(2109);

			 /*return new void(player, target, 1, CombatType.MELEE, false) {
				 @Override
				 public void onHit(int dmg, boolean accurate) {
					 if(accurate) {
						 int damageHeal = (int) (dmg * 0.5);
						 int damagePrayerHeal = (int) (dmg * 0.25);
						 if(player.getHealth().getHealth() < player.getMaxConstitution()) {
							 int level = player.getHealth().getHealth() + damageHeal > player.getMaxConstitution() ? player.getMaxConstitution() : player.getHealth().getHealth() + damageHeal;
							 player.getSkills().setLevel(Skill.CONSTITUTION, level);
						 }
						 if(player.getSkills().getLevel(Skill.PRAYER) < player.getSkills().getMaxLevel(Skill.PRAYER)) {
							 int level = player.getSkills().getLevel(Skill.PRAYER) + damagePrayerHeal > player.getSkills().getMaxLevel(Skill.PRAYER) ? player.getSkills().getMaxLevel(Skill.PRAYER) : player.getSkills().getLevel(Skill.PRAYER) + damagePrayerHeal;
							 player.getSkills().setLevel(Skill.PRAYER, level);
						 }
					 }
				 }
			 };*/
        }
    },
    DRAGON_CLAWS(new int[]{14484}, 50, 2, 1.8, CombatType.MELEE, WeaponInterface.CLAWS) {
        @Override
        public void container(Player player, Figure target) {
            player.animate(10961);
            player.graphic(1950);

//			 return new void(player, target, 4, CombatType.MELEE, true);
        }
    },
    DRAGON_PICKAXE(new int[]{15259}, 100, 1.1, 1.1, CombatType.MELEE, WeaponInterface.PICKAXE) {
        @Override
        public void container(Player player, Figure target) {
            player.animate(13691);
            player.graphic(2144);
            if (target.isPlayer()) {
                Player p = (Player) target;
                p.getSkills().drain(Skill.ATTACK, 0.5);
                p.getSkills().drain(Skill.RANGED, 0.5);
                p.getSkills().drain(Skill.MAGIC, 0.5);
            }
//			 return new void(player, target, 1, CombatType.MELEE, true);
        }
    },
    DRAGON_HATCHET(new int[]{6739}, 100, 1.1, 1.1, CombatType.MELEE, WeaponInterface.BATTLEAXE) {
        @Override
        public void container(Player player, Figure target) {
            player.animate(2876);
            player.graphic(479);
            if (target.isPlayer()) {
                Player p = (Player) target;
                p.getSkills().drain(Skill.DEFENCE, 4);
                p.getSkills().drain(Skill.MAGIC, 4);
            }
//			 return new void(player, target, 1, CombatType.MELEE, true);
        }
    };

    /**
     * The weapon ID's that perform this special when activated.
     */
    private int[] identifiers;

    /**
     * The amount of special energy this attack will drain.
     */
    private int drainAmount;

    /**
     * The strength bonus when performing this special attack.
     */
    private double strengthBonus;

    /**
     * The accuracy bonus when performing this special attack.
     */
    private double accuracyBonus;

    /**
     * The combat type used when performing this special attack.
     */
    private CombatType combatType;

    /**
     * The weapon interface used by the identifiers.
     */
    private WeaponInterface weaponType;

    /**
     * Create a new {@link CombatSpecial}.
     *
     * @param identifiers   the weapon ID's that perform this special when activated.
     * @param drainAmount   the amount of special energy this attack will drain.
     * @param strengthBonus the strength bonus when performing this special attack.
     * @param accuracyBonus the accuracy bonus when performing this special attack.
     * @param combatType    the combat type used when performing this special attack.
     * @param weaponType    the weapon interface used by the identifiers.
     */
    CombatSpecial(int[] identifiers, int drainAmount,
                  double strengthBonus, double accuracyBonus, CombatType combatType,
                  WeaponInterface weaponType) {
        this.identifiers = identifiers;
        this.drainAmount = drainAmount;
        this.strengthBonus = strengthBonus;
        this.accuracyBonus = accuracyBonus;
        this.combatType = combatType;
        this.weaponType = weaponType;
    }

    public void onActivation(Player player, Figure target) {
    }

    public abstract void container(Player player, Figure target);

    /**
     * Drains the special bar for the argued {@link Player}.
     *
     * @param player the player who's special bar will be drained.
     * @param amount the amount of energy to drain from the special bar.
     */
    public static void drain(Player player, int amount) {
        int spec = player.getSettings().getInt(Settings.SPECIAL_ENERGY);
        player.getSettings().set(Settings.SPECIAL_ENERGY, spec - amount < 0 ? 0 : spec - amount);
        player.getSettings().setSpecialToggled(false);
        CombatSpecial.updateBar(player);
        Achievements.finishAchievement(player, AchievementData.PERFORM_SPECIAL_ATTACK);
    }

    /**
     * Restores the special bar for the argued {@link Player}.
     *
     * @param player the player who's special bar will be restored.
     * @param amount the amount of energy to restore to the special bar.
     */
    public static void restore(Player player, int amount, boolean message) {
        player.getSettings().restore(Settings.SPECIAL_ENERGY, amount);
        CombatSpecial.updateBar(player);
        if (message && player.getSettings().getInt(Settings.SPECIAL_ENERGY) % 50 == 0)
            player.message("Your special attack energy is now " + player.getSettings().getInt(Settings.SPECIAL_ENERGY) + "%.");
    }

    /**
     * Updates the special bar with the amount of special energy the argued
     * {@link Player} has.
     *
     * @param player the player who's special bar will be updated.
     */
    public static void updateBar(Player player) {
        if (player.getWeapon().getSpecialBar() == -1 || player.getWeapon().getSpecialMeter() == -1)
            return;

        int specialCheck = 10;
        int specialBar = player.getWeapon().getSpecialMeter();
        int specialAmount = player.getSettings().getInt(Settings.SPECIAL_ENERGY) / 10;

        for (int i = 0; i < 10; i++) {
            player.getPacketSender().sendInterfaceComponentOffset(specialAmount >= specialCheck ? 500 : 0, 0, --specialBar);
            specialCheck--;
        }
        player.getPacketSender().sendString(player.getWeapon().getSpecialMeter(), "Special Attack (" + player.getSettings().getInt(Settings.SPECIAL_ENERGY) + "%)", player.getSettings().isSpecialToggled() ? Colours.YELLOW : Colours.BLACK);

    }

    /**
     * Assigns special bars to the attack style interface if needed.
     *
     * @param player the player to assign the special bar for.
     */
    public static void assign(Player player) {
        if (player.getWeapon().getSpecialBar() == -1) {
            //if(!player.isPerformingSpecialAttack()) {
            player.getSettings().setSpecialToggled(false);
            player.setCombatSpecial(null);
            CombatSpecial.updateBar(player);
            //}

            return;
        }

        for (CombatSpecial c : CombatSpecial.values()) {
            if (player.getWeapon() == c.getWeaponType()) {
                if (Arrays.stream(c.getIdentifiers()).anyMatch(
                        id -> player.getEquipment().get(Slot.WEAPON).getId() == id)) {
                    player.getPacketSender().sendInterfaceDisplayState(player.getWeapon().getSpecialBar(), false);
                    player.setCombatSpecial(c);
                    return;
                }
            }
        }

        player.getPacketSender().sendInterfaceDisplayState(player.getWeapon().getSpecialBar(), true);
        player.setCombatSpecial(null);
    }

    public static void activate(Player player) {
        /*if (Dueling.checkRule(player, DuelRule.NO_SPECIAL_ATTACKS)) {
            player.getPacketSender().sendMessage("Special Attacks have been turned off in this duel.");
            return;
        }*/
        if (player.getCombatSpecial() == null) {
            return;
        }
        if (player.getSettings().isSpecialToggled()) {
            player.getSettings().setSpecialToggled(false);
            CombatSpecial.updateBar(player);
        } else {
			if (player.getSettings().getInt(Settings.SPECIAL_ENERGY) < player.getCombatSpecial().getDrainAmount()) {
				player.message("You do not have enough special attack energy left!");
				return;
			}

            final CombatSpecial spec = player.getCombatSpecial();
			boolean instantSpecial = spec == CombatSpecial.GRANITE_MAUL || spec == CombatSpecial.DRAGON_BATTLEAXE || spec == CombatSpecial.STAFF_OF_LIGHT;
			
			if(spec != CombatSpecial.STAFF_OF_LIGHT && player.isAutoCast()) {
				Autocasting.resetAutoCast(player, true);
			}
            if (PlayerCombatAction.hasInstantSpecial(player, player.getEquipment().get(Slot.WEAPON)))
                return;

			player.getSettings().setSpecialToggled(true);
            CombatSpecial.updateBar(player);
        }
    }

    public int[] getIdentifiers() {
        return identifiers;
    }

    public int getDrainAmount() {
        return drainAmount;
    }

    public WeaponInterface getWeaponType() {
        return weaponType;
    }
}
