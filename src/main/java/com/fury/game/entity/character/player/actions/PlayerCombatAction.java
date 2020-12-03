package com.fury.game.entity.character.player.actions;

import com.fury.core.model.item.Item;
import com.fury.core.model.node.entity.Entity;
import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.Projectile;
import com.fury.core.model.node.entity.actor.figure.combat.magic.CombatSpell;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.task.Task;
import com.fury.game.container.impl.equip.Equipment;
import com.fury.game.container.impl.equip.Slot;
import com.fury.game.content.combat.CombatType;
import com.fury.game.content.combat.Swing;
import com.fury.game.content.combat.magic.Magic;
import com.fury.game.content.controller.impl.Wilderness;
import com.fury.game.content.global.Achievements;
import com.fury.game.content.global.action.Action;
import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.member.slayer.Slayer;
import com.fury.game.content.skill.member.slayer.SlayerManager;
import com.fury.game.entity.character.combat.CombatConstants;
import com.fury.game.entity.character.combat.CombatDefinitions;
import com.fury.game.entity.character.combat.CombatIcon;
import com.fury.game.entity.character.combat.Hit;
import com.fury.game.entity.character.combat.effects.Effect;
import com.fury.game.entity.character.combat.effects.Effects;
import com.fury.game.entity.character.combat.equipment.weapon.CombatSpecial;
import com.fury.game.entity.character.combat.equipment.weapon.FightStyle;
import com.fury.game.entity.character.combat.equipment.weapon.FightType;
import com.fury.game.entity.character.combat.magic.CombatSpells;
import com.fury.game.entity.character.combat.range.CombatRangedAmmo;
import com.fury.game.entity.character.npc.impl.queenblackdragon.QueenBlackDragon;
import com.fury.game.entity.character.player.content.BonusManager;
import com.fury.game.node.entity.actor.figure.ProjectileManager;
import com.fury.game.node.entity.actor.figure.player.handles.Settings;
import com.fury.game.npc.bosses.glacors.Glacyte;
import com.fury.game.npc.bosses.nex.NexMinion;
import com.fury.game.npc.familiar.Familiar;
import com.fury.game.npc.familiar.impl.SteelTitan;
import com.fury.game.npc.minigames.pest.PestPortal;
import com.fury.game.system.files.loaders.item.WeaponAnimations;
import com.fury.game.world.FloorItemManager;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.game.world.update.flag.block.HitMask;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.game.world.update.flag.block.graphic.GraphicHeight;
import com.fury.util.Misc;
import com.fury.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class PlayerCombatAction extends Action {

    private Figure target;
    private CombatType combatType;

    public PlayerCombatAction(Figure target) {
        this.target = target;
    }

    @Override
    public boolean start(Player player) {
        player.getDirection().setInteracting(target);
        if (checkAll(player))
            return true;
        player.getDirection().setInteracting(null);
        return false;
    }

    @Override
    public boolean process(Player player) {
        combatType = isRanging(player) == 0 ? player.getCastSpell() != null || player.getAutoCastSpell() != null || player.getEquipment().get(Slot.WEAPON).getId() == 22494 ? CombatType.MAGIC : CombatType.MELEE : CombatType.RANGED;
        return checkAll(player);
    }

    @Override
    public int processWithDelay(Player player) {
        if (target.isNpc()) {
            switch (((Mob) target).getId()) {
                case 1880:
                    Achievements.finishAchievement(player, Achievements.AchievementData.ATTACK_A_BANDIT);
                    break;
            }
        }

        int isRanging = isRanging(player);
        CombatSpell spell = player.getCastSpell();
        CombatSpell autoSpell = player.getAutoCastSpell();
        int maxDistance = isRanging != 0 || spell != null || autoSpell != null || player.getEquipment().get(Slot.WEAPON).getId() == 22494 ? 7 : 0;
        int distanceX = player.getX() - target.getX();
        int distanceY = player.getY() - target.getY();
        int sizeX = target.getSizeX();
        int sizeY = target.getSizeY();
        if (!player.getCombat().clippedProjectile(target, !(target instanceof NexMinion) && !(target instanceof PestPortal) && !(target instanceof QueenBlackDragon) && maxDistance == 0))
            return 0;
        player.setRouteEvent(null);
        if (player.getMovement().hasWalkSteps())
            maxDistance += 1;
        //TODO colides/isOnRange?
        if (distanceX > sizeX + maxDistance || distanceX < -1 - maxDistance || distanceY > sizeY + maxDistance || distanceY < -1 - maxDistance)
            return 0;

        if (!player.getControllerManager().keepCombating(target) || target.isDead())
            return -1;
        CombatRangedAmmo.RangedWeaponData data = CombatRangedAmmo.RangedWeaponData.getData(player);
        if (data != null)
            player.setRangedWeaponData(data);
//        addAttackedByDelay(player);
        if (spell != null || autoSpell != null || player.getEquipment().get(Slot.WEAPON).getId() == 22494) {
            return mageAttack(player);
        } else {
            if (isRanging == 0) {
                return meleeAttack(player);
            } else if (isRanging == 1) {
                String ammo = player.getEquipment().get(Slot.ARROWS).getName();
                String weapon = player.getEquipment().get(Slot.WEAPON).getName();
                player.message("You can't use " + ammo + (ammo.endsWith("s") ? "" : "s") + " with " + Misc.anOrA(weapon) + " " + weapon + ".");
                return -1;
            } else if (isRanging == 3) {
                player.message("You don't have any ammunition to fire.");
                return -1;
            } else {
                return rangeAttack(player);
            }
        }
    }

    private int getRangeCombatDelay(Item weapon, int attackStyle) {
        int delay = 6;
        if (weapon.getId() != -1) {
            String weaponName = weapon.getName().toLowerCase();
            if (weaponName.contains("shortbow") || weaponName.contains("karil's crossbow"))
                delay = 3;
            else if (weaponName.contains("crossbow") || weaponName.equalsIgnoreCase("bolas"))
                delay = 5;
            else if (weaponName.contains("chinchompa"))
                delay = 4;
            else if (weaponName.contains("dart") || weaponName.contains("knife"))
                delay = 2;
            else {
                switch (weapon.getId()) {
                    case 15241:
                    case 24338:
                        delay = 7;
                        break;
                    case 11235: // dark bows
                    case 15701:
                    case 15702:
                    case 15703:
                    case 15704:
                        delay = 8;
                        break;
                    default:
                        delay = 6;
                        break;
                }
            }
        }
        if (attackStyle == 1)
            delay--;
        else if (attackStyle == 2)
            delay++;
        return delay;
    }

    public int mageAttack(Player player) {
        CombatSpell spell = player.getCastSpell() != null ? player.getCastSpell() : player.getAutoCastSpell() != null ? player.getAutoCastSpell() : player.getEquipment().get(Slot.WEAPON).getId() == 22494 ? CombatSpells.POLYPORE.spell : null;
        if(spell == null)
            return -1;

        if(spell == CombatSpells.POLYPORE.spell)
            player.setAutoCast(true);

        int delay = spell.getHitDelay(player);

        if(!CombatType.MAGIC.getSwing().run(player, target))
            return -1;

        if (player.getEquipment().get(Slot.SHIELD).getId() == 11283 && player.getTemporaryAttributes().remove("dfs_shield_active") != null) {
            CombatSpells.DRAGON_FIRE.spell.cast(player);
            delayMagicHit(player, target, 1, CombatSpells.DRAGON_FIRE.spell, getMagicHit(player, Magic.getMaxHit(player, target, CombatSpells.DRAGON_FIRE.spell)));
        }
        return delay;
    }

    private int rangeAttack(final Player player) {
        final Item weapon = player.getEquipment().get(Slot.WEAPON);
        FightType fightType = player.getFightType();
        int attackStyle = player.getCombatDefinitions().getAttackStyle();
        int combatDelay = getRangeCombatDelay(weapon, attackStyle);
        if(player.getEffects().hasActiveEffect(Effects.MIASMIC_EFFECT))
            combatDelay *= 2;
        int soundId = getSoundId(weapon);
        CombatRangedAmmo.RangedWeaponData data = CombatRangedAmmo.RangedWeaponData.getData(player);
        if (data != null)
            player.setRangedWeaponData(data);
        CombatRangedAmmo.AmmunitionData ammo = CombatRangedAmmo.RangedWeaponData.getAmmunitionData(player);
//        mage_hit_gfx = 0;
        player.getTemporaryAttributes().remove("range_hit_gfx");
        player.getTemporaryAttributes().remove("mage_hit_gfx");
//        range_hit_gfx = 0;

        if (player.getSettings().isSpecialToggled()) {
            int specAmt = getSpecialAmount(weapon);
            if (specAmt == 0) {
                player.message("This weapon has no special Attack, if you still see special bar please relog.");
                CombatSpecial.drain(player, 0);
                return combatDelay;
            }
            if (player.getCombatDefinitions().hasRingOfVigour())
                specAmt *= 0.9;
            if (player.getSettings().getInt(Settings.SPECIAL_ENERGY) < specAmt) {
                player.message("You don't have enough power left.");
                CombatSpecial.drain(player, 0);
                return combatDelay;
            }
            CombatSpecial.drain(player, specAmt);
            switch (weapon.getId()) {
                case 19149:// zamorak bow
                case 19151:
                    player.animate(426);
                    player.perform(new Graphic(97));
                    ProjectileManager.send(new Projectile(player, target, 100, 41, 16, 25, 35, 16, 0));
                    delayHit(1, weapon, fightType, getRangeHit(player, getRandomMaxHit(player, weapon, fightType, true, true, 1.0, true)));
                    dropAmmo(player, 1);
                    break;
                case 19146:
                case 19148:// guthix bow
                    player.animate(426);
                    player.perform(new Graphic(95));
                    ProjectileManager.send(new Projectile(player, target, 98, 41, 16, 25, 35, 16, 0));
                    delayHit(1, weapon, fightType, getRangeHit(player, getRandomMaxHit(player, weapon, fightType, true, true, 1.0, true)));
                    dropAmmo(player, 1);
                    break;
                case 19143:// saradomin bow
                case 19145:
                    player.animate(426);
                    player.perform(new Graphic(96));
                    ProjectileManager.send(new Projectile(player, target, 99, 41, 16, 25, 35, 16, 0));
                    delayHit(1, weapon, fightType, getRangeHit(player, getRandomMaxHit(player, weapon, fightType, true, true, 1.0, true)));
                    dropAmmo(player, 1);
                    break;
                case 859: // magic longbow
                case 861: // magic shortbow
                case 10284: // Magic composite bow
                case 18332: // Magic longbow (sighted)
                    player.animate(1074);
                    player.perform(new Graphic(250, 0, 100));
                    ProjectileManager.send(new Projectile(player, target, 249, 41, 16, 31, 35, 16, 0));
                    ProjectileManager.send(new Projectile(player, target, 249, 41, 16, 25, 35, 21, 0));
                    delayHit(2, weapon, fightType, getRangeHit(player, getRandomMaxHit(player, weapon, fightType, true, true, 1.0, true)));
                    delayHit(3, weapon, fightType, getRangeHit(player, getRandomMaxHit(player, weapon, fightType, true, true, 1.0, true)));
                    dropAmmo(player, 2);
                    break;
                case 15241: // Hand cannon
                    GameWorld.schedule(new Task(true, (int) 0.25) {
                        private int loop = 0;

                        @Override
                        public void run() {
                            if ((target.isDead() || player.isDead() || loop > 1) && !target.isRegistered()) {
                                stop();
                                return;
                            }
                            if (loop == 0) {
                                player.animate(12174);
                                player.perform(new Graphic(2138));
                                ProjectileManager.send(new Projectile(player, target, 2143, 18, 18, 50, 50, 0, 0));
                                delayHit(1, weapon, fightType, getRangeHit(player, getRandomMaxHit(player, weapon, fightType, true, true, 1.0, true)));
                            } else if (loop == 1) {
                                player.animate(12174);
                                player.perform(new Graphic(2138));
                                ProjectileManager.send(new Projectile(player, target, 2143, 18, 18, 50, 50, 0, 0));
                                delayHit(1, weapon, fightType, getRangeHit(player, getRandomMaxHit(player, weapon, fightType, true, true, 1.0, true)));
                                stop();
                            }
                            loop++;
                        }
                    });
                    combatDelay = 9;
                    break;
                case 11235: // dark bows
                case 15701:
                case 15702:
                case 15703:
                case 15704:
                    int ammoId = player.getEquipment().get(Slot.ARROWS).getId();
                    player.perform(new Animation(WeaponAnimations.getAttackAnimation(weapon, attackStyle)));
                    player.perform(new Graphic(ammo.getStartGfxId(), 0, 100));
                    if (ammoId == 11212) {
                        int damage = getRandomMaxHit(player, weapon, fightType, true, true, 1.5, true);
                        if (damage < 80)
                            damage = 80;
                        int damage2 = getRandomMaxHit(player, weapon, fightType, true, true, 1.5, true);
                        if (damage2 < 80)
                            damage2 = 80;
                        ProjectileManager.send(new Projectile(player, target, 1099, 41, 16, 31, 35, 16, 0));
                        ProjectileManager.send(new Projectile(player, target, 1099, 41, 16, 25, 35, 21, 0));
                        delayHit(2, weapon, fightType, getRangeHit(player, damage));
                        delayHit(3, weapon, fightType, getRangeHit(player, damage2));
                        GameWorld.schedule(2, () -> target.perform(new Graphic(1100, 0, 100)));
                    } else {
                        int damage = getRandomMaxHit(player, weapon, fightType, true, true, 1.3, true);
                        if (damage < 50)
                            damage = 50;
                        int damage2 = getRandomMaxHit(player, weapon, fightType, true, true, 1.3, true);
                        if (damage2 < 50)
                            damage2 = 50;
                        ProjectileManager.send(new Projectile(player, target, 1101, 41, 16, 31, 35, 16, 0));
                        ProjectileManager.send(new Projectile(player, target, 1101, 41, 16, 25, 35, 21, 0));
                        delayHit(2, weapon, fightType, getRangeHit(player, damage));
                        delayHit(3, weapon, fightType, getRangeHit(player, damage2));
                    }
                    dropAmmo(player, 2);
                    break;
                case 14684: // zanik cbow
                    player.perform(new Animation(WeaponAnimations.getAttackAnimation(weapon, attackStyle)));
                    player.perform(new Graphic(1714));
                    ProjectileManager.send(new Projectile(player, target, 2001, 41, 41, 41, 35, 0, 0));
                    delayHit(2, weapon, fightType, getRangeHit(player, getRandomMaxHit(player, weapon, fightType, true, true, 1.0, true) + 30 + Utils.getRandom(120)));
                    dropAmmo(player);
                    break;
                case 13954:// morrigan javelin
                case 12955:
                case 13956:
                case 13879:
                case 13880:
                case 13881:
                case 13882:
                    player.perform(new Graphic(1836));
                    player.animate(10501);
                    ProjectileManager.send(new Projectile(player, target, 1837, 41, 41, 41, 35, 0, 0));
                    final int hit = getRandomMaxHit(player, weapon, fightType, true, true, 1.0, true);
                    delayHit(2, weapon, fightType, getRangeHit(player, hit));
                    if (hit > 0) {
                        final Figure finalTarget = target;
                        GameWorld.schedule(4, () -> GameWorld.schedule(new Task(true, 2) {
                            int damage = hit;

                            @Override
                            public void run() {
                                if (finalTarget.isDead() || finalTarget.getFinished()) {
                                    stop();
                                    return;
                                }
                                if (damage > 50) {
                                    damage -= 50;
                                    finalTarget.getCombat().applyHit(new Hit(player, 50, HitMask.RED, CombatIcon.RANGED));
                                } else {
                                    finalTarget.getCombat().applyHit(new Hit(player, damage, HitMask.RED, CombatIcon.RANGED));
                                    stop();
                                }
                            }
                        }));
                    }
                    dropAmmo(player, -1);
                    break;
                case 13883:
                case 13957:// morigan thrown axe
                    player.perform(new Graphic(1838));
                    player.animate(10504);
                    ProjectileManager.send(new Projectile(player, target, 1839, 41, 41, 41, 35, 0, 0));
                    delayHit(2, weapon, fightType, getRangeHit(player, getRandomMaxHit(player, weapon, fightType, true, true, 1.0, true)));
                    dropAmmo(player, -1);
                    break;
                default:
                    player.message("This weapon has no special Attack, if you still see special bar please relogin.");
                    return combatDelay;
            }
        } else {
            if (player.getEquipment().get(Slot.SHIELD).getId() == 11283 && player.getTemporaryAttributes().remove("dfs_shield_active") != null) {
                CombatSpells.DRAGON_FIRE.spell.cast(player);
                delayMagicHit(player, target, 1, CombatSpells.DRAGON_FIRE.spell, getMagicHit(player, Magic.getMaxHit(player, target, CombatSpells.DRAGON_FIRE.spell)));
            } else
            if (weapon.getId() != -1) {
                String weaponName = weapon.getName().toLowerCase();
                if (weapon.getId() == 25202) {
                    if (target.isNpc()) {
                        ProjectileManager.send(new Projectile(player, target, ammo.getProjectileId(), ammo.getRevision(), 41, 16, 60, 30, 16, 0));
                        Hit hit = getRangeHit(player, target.getHealth().getHitpoints());
                        hit.setWeapon(weapon.copy());
                        delayHit(1, weapon, fightType, hit);
                        dropAmmo(player, -3);
                    } else {
                        player.message("Deathtouch darts can only be used on npcs.");
                        return -1;
                    }
                } else if (weaponName.contains("throwing axe") || weaponName.contains("knife") || weaponName.contains("dart") || weaponName.contains("javelin") || weaponName.contains("thrownaxe") || weaponName.equalsIgnoreCase("sagaie") || weaponName.equalsIgnoreCase("bolas")) {
                    if (!weaponName.equalsIgnoreCase("bolas") && !weaponName.equalsIgnoreCase("sagaie") && !weaponName.contains("javelin") && !weaponName.contains("thrownaxe"))
                        player.perform(new Graphic(ammo.getStartGfxId(), 0, 100, ammo.getRevision()));
                    ProjectileManager.send(new Projectile(player, target, ammo.getProjectileId(), ammo.getRevision(), 41, 16, 60, 30, 16, 0));
                    delayHit(1, weapon, fightType, getRangeHit(player, getRandomMaxHit(player, weapon, fightType, true)));
                    if (!weaponName.equalsIgnoreCase("bolas") && !weaponName.equalsIgnoreCase("sagaie"))
                        dropAmmo(player, -1);
                } else if (weaponName.contains("crossbow")) {
                    int damage;
                    int ammoId = player.getEquipment().get(Slot.ARROWS).getId();
                    if (ammoId != -1 && Utils.getRandom(10) == 5) {
                        switch (ammoId) {
                            case 9237:
                                damage = getRandomMaxHit(player, weapon, fightType, true);
                                target.perform(new Graphic(755));
                                if (target.isPlayer()) {
                                    Player p2 = (Player) target;
                                    p2.stopAll();
                                } else {
                                    Mob n = (Mob) target;
                                    n.setTarget(null);
                                }
                                soundId = 2914;
                                break;
                            case 9242:
                                player.temporaryAttributes.put("max_hit", (int) Short.MAX_VALUE);
//                                max_hit = Short.MAX_VALUE;
                                damage = (int) (target.getHealth().getHitpoints() * 0.2);
                                if (damage > 4000) {
                                    damage = 4000;
                                }
                                target.perform(new Graphic(754));
                                player.getCombat().applyHit(new Hit(target, player.getHealth().getHitpoints() > 20 ? (int) (player.getHealth().getHitpoints() * 0.1) : 1, HitMask.NONE, CombatIcon.DEFLECT));
                                soundId = 2912;
                                break;
                            case 9243:
                                damage = getRandomMaxHit(player, weapon, fightType, true, false, 1.15, true);
                                target.perform(new Graphic(751));
                                soundId = 2913;
                                break;
                            case 9244:
                                damage = getRandomMaxHit(player, weapon, fightType, true, false, !Equipment.hasAntiDragonProtection(target) ? 1.45 : 1.0, true);
                                target.perform(new Graphic(756));
                                soundId = 2915;
                                break;
                            case 9245:
                                damage = getRandomMaxHit(player, weapon, fightType, true, false, 1.15, true);
                                target.perform(new Graphic(753));
                                player.getHealth().heal((int) (player.getMaxConstitution() * 0.25));
                                soundId = 2917;
                                break;
                            default:
                                damage = getRandomMaxHit(player, weapon, fightType, true);
                                break;
                        }
                    } else
                        damage = getRandomMaxHit(player, weapon, fightType, true);
                    delayHit(2, weapon, fightType, getRangeHit(player, damage));
                    if (ammo != CombatRangedAmmo.AmmunitionData.BOLT_RACK)
                        dropAmmo(player);
                    else
                        player.getEquipment().delete(new Item(ammoId, 1));
                    player.perform(new Graphic(ammo.getStartGfxId(), 0, 100, ammo.getRevision()));
                    ProjectileManager.send(new Projectile(player, target, ammo.getProjectileId(), ammo.getRevision(), 41, 16, 20, 35, 16, 0));
                } else if (weapon.getId() == 15241) {// hand cannon
                    if (Utils.getRandom(player.getSkills().getLevel(Skill.FIREMAKING) * 5) == 0) {// explode
                        player.perform(new Graphic(2140));
                        player.getEquipment().delete(3);
                        player.getEquipment().refresh();
                        Equipment.resetWeapon(player);
                        player.getCombat().applyHit(new Hit(player, Utils.getRandom(150) + 10, HitMask.RED, CombatIcon.RANGED));
                        player.animate(12175);
                        player.message("Your hand cannon explodes!");
                        return combatDelay;
                    } else {
                        player.perform(new Graphic(2138));
                        ProjectileManager.send(new Projectile(player, target, 2143, 18, 18, 60, 30, 0, 0));
                        delayHit(1, weapon, fightType, getRangeHit(player, getRandomMaxHit(player, weapon, fightType, true)));
                        dropAmmo(player, -2);
                    }
                } else if (weaponName.contains("crystal bow")) {
                    player.perform(new Animation(WeaponAnimations.getAttackAnimation(weapon, attackStyle)));
                    player.perform(new Graphic(250, 0, 100));
                    ProjectileManager.send(new Projectile(player, target, 249, 41, 41, 41, 35, 0, 0));
                    delayHit(2, weapon, fightType, getRangeHit(player, getRandomMaxHit(player, weapon, fightType, true)));
                } else if (weaponName.contains("sling")) {
                    player.perform(new Animation(WeaponAnimations.getAttackAnimation(weapon, attackStyle)));
                    player.perform(new Graphic(33, 0, 100));
                    ProjectileManager.send(new Projectile(player, target, 32, 41, 41, 41, 35, 0, 0));
                    delayHit(2, weapon, fightType, getRangeHit(player, getRandomMaxHit(player, weapon, fightType, true)));
                } else if (weaponName.contains("chinchompa")) {
                    ProjectileManager.send(new Projectile(player, target, ammo.getProjectileId(), ammo.getRevision(), 41, 16, 60, 30, 16, 0));
                    if (weapon.getId() == 10034) {
                        player.getTemporaryAttributes().put("range_hit_gfx", 2739);
//                        range_hit_gfx = 2739;
//                        range_hit_gfx_revision = Revision.RS2;
                    }
                    for (Figure target : getMultiAttackTargets(player))
                        Swing.delayHit(target, 1, weapon, fightType, getRangeHit(player, getRandomMaxHit(player, weapon, fightType, true)));
                    dropAmmo(player, -3);
                } else { // bow/default
                    player.perform(new Graphic(ammo.getStartGfxId(), 0, 100, ammo.getRevision()));
                    ProjectileManager.send(new Projectile(player, target, ammo.getProjectileId(), ammo.getRevision(), 41, 16, 20, 35, 16, 0));
                    delayHit(2, weapon, fightType, getRangeHit(player, getRandomMaxHit(player, weapon, fightType, true)));
                    if (weapon.getId() == 11235 || weapon.getId() == 15701 || weapon.getId() == 15702 || weapon.getId() == 15703 || weapon.getId() == 15704) { // dbows
                        ProjectileManager.send(new Projectile(player, target, ammo.getProjectileId(), ammo.getRevision(), 41, 35, 26, 35, 21, 0));
                        delayHit(3, weapon, fightType, getRangeHit(player, getRandomMaxHit(player, weapon, fightType, true)));
                        dropAmmo(player, 2);
                    } else {
                        if (weapon.getId() != -1) {
                            if (!weaponName.endsWith("bow full") && !weaponName.equals("zaryte bow"))
                                dropAmmo(player);
                        }
                    }
                }
                player.perform(new Animation(WeaponAnimations.getAttackAnimation(weapon, attackStyle)));
            }
        }
        playSound(soundId, player, target);
        return combatDelay;
    }

    public void dropAmmo(Player player, int quantity) {
        if (quantity == -3) {
            final Item weapon = player.getEquipment().get(Slot.WEAPON);
            if (weapon.getId() != -1)
                player.getEquipment().delete(new Item(weapon, 1));
        } else if (quantity == -2) {
            final Item ammo = player.getEquipment().get(Slot.ARROWS);
            player.getEquipment().delete(new Item(ammo, 1));
        } else if (quantity == -1) {
            final Item weapon = player.getEquipment().get(Slot.WEAPON);
            if (weapon.getId() != -1) {
                if (Utils.getRandom(3) > 0) {
                    Item cape = player.getEquipment().get(Slot.CAPE);
                    if (cape.getId() != -1 && (cape.getName().contains("Ava's") || cape.getName().contains("Completionist")))
                        return;
                } else {
                    player.getEquipment().delete(new Item(weapon, 1));
                    return;
                }
                player.getEquipment().delete(new Item(weapon, 1));
                FloorItemManager.updateGroundItem(new Item(weapon, 1), new Position(target.getCoordFaceX(target.getSizeX()), target.getCoordFaceY(target.getSizeY()), target.getZ()), player);
            }
        } else {
            final Item ammo = player.getEquipment().get(Slot.ARROWS);
            if (Utils.getRandom(3) > 0) {
                Item cape = player.getEquipment().get(Slot.CAPE);
                if (cape.getId() != -1 && (cape.getName().contains("Ava's") || cape.getName().contains("Completionist")))
                    return; // nothing happens
            } else {
                player.getEquipment().delete(new Item(ammo, quantity));
                return;
            }
            if (ammo.getId() != -1) {
                player.getEquipment().delete(new Item(ammo, quantity));
                FloorItemManager.updateGroundItem(new Item(ammo, quantity), new Position(target.getCoordFaceX(target.getSizeX()), target.getCoordFaceY(target.getSizeY()), target.getZ()), player);
            }
        }
    }

    public void dropAmmo(Player player) {
        dropAmmo(player, 1);
    }

    private int getRangeHitDelay(Player player) {
        return Utils.getDistance(player.getX(), player.getY(), target.getX(), target.getY()) >= 5 ? 2 : 1;
    }

    public Figure[] getMultiAttackTargets(Player player) {
        return getMultiAttackTargets(player, target, 1, 9, false);
    }

    public static Figure[] getMultiAttackTargets(Player player, Figure target) {
        return getMultiAttackTargets(player, target, 1, 9, false);
    }

    public static Figure[] getMultiAttackTargets(Player player, Figure target, int maxDistance, int maxAmtTargets) {
        return getMultiAttackTargets(player, target, maxDistance, maxAmtTargets, false);
    }

    public static Figure[] getMultiAttackTargets(Player player, Figure target, int maxDistance, int maxAmtTargets, boolean usePlayerLoc) {
        List<Figure> possibleTargets = new ArrayList<>();
        possibleTargets.add(target);
        if (target.isPlayer()) {
            for (Player p2 : GameWorld.getRegions().getLocalPlayers(player)) {
                if (p2 == null || p2 == player || p2 == target || p2.isDead() || !p2.hasStarted() || p2.getFinished() || !p2.isCanPvp() || !p2.isWithinDistance(usePlayerLoc ? player : target, maxDistance) || !player.getControllerManager().canHit(p2) || !player.getCombat().clippedProjectile(p2, false) || (p2.getControllerManager().getController() instanceof Wilderness && !p2.inMulti()))
                    continue;
                possibleTargets.add(p2);
                if (possibleTargets.size() == maxAmtTargets)
                    break;
            }
        } else {
            for (Mob n : GameWorld.getRegions().getLocalNpcs(player)) {
                if (n == null || n == target || !n.inMulti() || n == player.getFamiliar() || n.isDead() || n.getFinished() || !n.isWithinDistance(usePlayerLoc ? player : target, maxDistance) || !n.getDefinition().hasAttackOption() || !player.getControllerManager().canHit(n) || !player.getCombat().clippedProjectile(n, false))
                    continue;
                possibleTargets.add(n);
                if (possibleTargets.size() == maxAmtTargets)
                    break;
            }
        }
        return possibleTargets.toArray(new Figure[possibleTargets.size()]);
    }

    public static boolean hasInstantSpecial(Player player, final Item weapon) {
        int drainAmount = getSpecialAmount(weapon);
        if (player.getCombatDefinitions().hasRingOfVigour())
            drainAmount *= 0.9;
        if (player.getSettings().getInt(Settings.SPECIAL_ENERGY) < drainAmount) {
            player.message("You don't have enough power left.");
            player.getSettings().setSpecialToggled(false);
            return false;
        }
        switch (weapon.getId()) {
            case 4153:
                if (player.getTemporaryAttributes().get("InstantSpecial") == null)
                    player.getTemporaryAttributes().put("InstantSpecial", 4153);
                else
                    player.getTemporaryAttributes().remove("InstantSpecial");
                player.getSettings().setSpecialToggled(!player.getSettings().isSpecialToggled());
                return true;
            case 15486:
            case 22207:
            case 22209:
            case 22211:
            case 22213:
                player.animate(12804);
                player.perform(new Graphic(2319));
                player.getEffects().startEffect(new Effect(Effects.STAFF_OF_LIGHT, 100));
                CombatSpecial.drain(player, drainAmount);
                return true;
            case 1377:
            case 13472:
                player.animate(1056);
                player.perform(new Graphic(246, GraphicHeight.LOW));
                player.forceChat("Raarrrrrgggggghhhhhhh!");
                player.getSkills().drain(Skill.DEFENCE, 0.10);
                player.getSkills().drain(Skill.ATTACK, 0.10);
                player.getSkills().drain(Skill.RANGED, 0.10);
                player.getSkills().drain(Skill.MAGIC, 0.10);
                player.getSkills().boost(Skill.STRENGTH, 5, 0.15);
                CombatSpecial.drain(player, drainAmount);
                return true;
            case 35:// Excalibur
            case 14632:
                player.animate(1168);
                player.perform(new Graphic(247));
                final boolean enhanced = weapon.getId() == 14632;
                if (enhanced)
                    player.getSkills().boost(Skill.DEFENCE, 0.15);
                else
                    player.getSkills().boost(Skill.DEFENCE, 8);
                GameWorld.schedule(4, () -> GameWorld.schedule(new Task(true, 2) {
                    private int count = 5;

                    @Override
                    public void run() {
                        if (player.isDead() || player.getFinished() || player.getHealth().getHitpoints() >= player.getMaxConstitution()) {
                            stop();
                            return;
                        }
                        player.getHealth().heal(enhanced ? 80 : 40);
                        if (count-- == 0) {
                            stop();
                            return;
                        }
                    }
                }));
                CombatSpecial.drain(player, drainAmount);
                return true;
        }
        return false;
    }

    private int meleeAttack(final Player player) {
        Item weapon = player.getEquipment().get(Slot.WEAPON);
        FightType fightType = player.getFightType();
        int attackStyle = player.getCombatDefinitions().getAttackStyle();
        int combatDelay = getMeleeCombatDelay(weapon);
        if(player.getEffects().hasActiveEffect(Effects.MIASMIC_EFFECT))
            combatDelay *= 2;
        int soundId = getSoundId(weapon);

        if (player.getSettings().isSpecialToggled()) {
            player.getSettings().setSpecialToggled(false);
            int specAmt = getSpecialAmount(weapon);
            if (specAmt == 0) {
                player.message("This weapon has no special Attack, if you still see special bar please relog.");
                CombatSpecial.drain(player, 0);
                return combatDelay;
            }
            if (player.getCombatDefinitions().hasRingOfVigour())
                specAmt *= 0.9;
            if (player.getSettings().getInt(Settings.SPECIAL_ENERGY) < specAmt) {
                player.message("You don't have enough power left.");
                CombatSpecial.drain(player, 0);
                return combatDelay;
            }
            CombatSpecial.drain(player, specAmt);
            switch (weapon.getId()) {
                case 4153:// gmaul
                    player.animate(1667);
                    player.perform(new Graphic(340, 0, 96 << 16));
                    delayNormalHit(weapon, fightType, getMeleeHit(player, getRandomMaxHit(player, weapon, fightType, false, true, 1.1, true)));
                    break;
                case 15442:// whip start
                case 15443:
                case 15444:
                case 15441:
                case 4151:
                    player.animate(11971);
                    target.perform(new Graphic(2108, 0, 100));
                    if (target.isPlayer()) {
                        Player p2 = (Player) target;
                        p2.getSettings().set(Settings.RUN_ENERGY, p2.getSettings().getInt(Settings.RUN_ENERGY) > 25 ? p2.getSettings().getInt(Settings.RUN_ENERGY) - 25 : 0);
                    }
                    delayNormalHit(weapon, fightType, getMeleeHit(player, getRandomMaxHit(player, weapon, fightType, false, true, 1.2, true)));
                    break;
                case 11730: // sara sword
                    player.animate(11993);
                    target.perform(new Graphic(1194));
                    delayNormalHit(weapon, fightType, getMeleeHit(player, 50 + Utils.getRandom(100)), getMeleeHit(player, getRandomMaxHit(player, weapon, fightType, false, true, 1.1, true)));
                    soundId = 3853;
                    break;
                case 1249:// d spear
                case 1263:
                case 3176:
                case 5716:
                case 5730:
                case 13770:
                case 13772:
                case 13774:
                case 13776:
                    player.animate(12017);
                    player.stopAll();
                    target.perform(new Graphic(80, 5, 60));
                    if (!target.getMovement().addWalkSteps(target.getX() - player.getX() + target.getX(), target.getY() - player.getY() + target.getY(), 1))
                        player.getDirection().setInteracting(target);
                    target.getDirection().setInteracting(player);
                    GameWorld.schedule(1, () -> {
                        target.getDirection().setInteracting(null);
                        player.getDirection().setInteracting(null);
                    });
                    if (target.isPlayer()) {
                        final Player other = (Player) target;
                        other.setStopAttackDelay(Long.MAX_VALUE);
//                        other.addFoodDelay(3000);
//                        other.setDisableEquip(true);
                        GameWorld.schedule(5, () -> {
//                                other.setDisableEquip(false);
                            other.setStopAttackDelay(0);
                        });
                    } else {
                        Mob n = (Mob) target;
                        n.getCombat().setFreezeDelay(3000);
                        n.getCombat().resetCombat();
                        n.setWalkType(0);
                    }
                    break;
                case 11698: // sgs
                    player.animate(12019);
                    player.perform(new Graphic(2109));
                    int sgsdamage = getRandomMaxHit(player, weapon, fightType, false, true, 1.1, true);
                    player.getHealth().heal(sgsdamage / 2);
                    player.getSkills().restore(Skill.PRAYER, (int) (sgsdamage / 4) * 10);
                    delayNormalHit(weapon, fightType, getMeleeHit(player, sgsdamage));
                    break;
                case 11696: // bgs
                    player.animate(11991);
                    player.perform(new Graphic(2114));
                    int damage = getRandomMaxHit(player, weapon, fightType, false, true, 1.2, true);
                    delayNormalHit(weapon, fightType, getMeleeHit(player, damage));
                    if (target.isPlayer()) {
                        Player targetPlayer = ((Player) target);
                        int amountLeft;
                        if ((amountLeft = targetPlayer.getSkills().drain(Skill.DEFENCE, damage / 10)) > 0) {
                            if ((amountLeft = targetPlayer.getSkills().drain(Skill.STRENGTH, amountLeft)) > 0) {
                                if ((amountLeft = targetPlayer.getSkills().drain(Skill.PRAYER, amountLeft)) > 0) {
                                    if ((amountLeft = targetPlayer.getSkills().drain(Skill.ATTACK, amountLeft)) > 0) {
                                        if ((amountLeft = targetPlayer.getSkills().drain(Skill.MAGIC, amountLeft)) > 0) {
                                            if (targetPlayer.getSkills().drain(Skill.RANGED, amountLeft) > 0) {
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    break;
                case 11694: // ags
                    player.animate(11989);
                    player.perform(new Graphic(2113));
                    delayNormalHit(weapon, fightType, getMeleeHit(player, getRandomMaxHit(player, weapon, fightType, false, true, 1.50, true)));
                    break;
                case 13899: // vls
                case 13901:
                    player.animate(10502);
                    delayNormalHit(weapon, fightType, getMeleeHit(player, getRandomMaxHit(player, weapon, fightType, false, true, 1.20, true)));
                    break;
                case 13902: // statius hammer
                case 13904:
                    player.animate(10505);
                    player.perform(new Graphic(1840));
                    delayNormalHit(weapon, fightType, getMeleeHit(player, getRandomMaxHit(player, weapon, fightType, false, true, 1.25, true)));
                    break;
                case 13905: // vesta spear
                case 13907:
                    player.animate(10499);
                    player.perform(new Graphic(1835));
                    delayNormalHit(weapon, fightType, getMeleeHit(player, getRandomMaxHit(player, weapon, fightType, false, true, 1.1, true)));
                    break;
                case 19784: // korasi sword
                    player.animate(14788);
                    player.perform(new Graphic(1729));
                    int korasiDamage = getMaxHit(player, fightType, false, 1);
                    double multiplier = 0.5;
                    multiplier += Math.random();
                    korasiDamage *= multiplier;
                    delayNormalHit(weapon, fightType, getMagicHit(player, korasiDamage));
                    break;
                case 11700:
                    int zgsdamage = getRandomMaxHit(player, weapon, fightType, false, true, 1.0, true);
                    player.animate(7070);
                    player.perform(new Graphic(1221));
                    if (zgsdamage != 0 && target.getSize() <= 1) { // freezes small npcs
                        target.perform(new Graphic(2104));
                        target.getCombat().addFreezeDelay(20000); // 20 seconds
                    }
                    delayNormalHit(weapon, fightType, getMeleeHit(player, zgsdamage));
                    break;
                case 14484: // d claws
                    player.animate(10961);
                    player.perform(new Graphic(1950));
                    int hit1 = getRandomMaxHit(player, weapon, fightType, false, true, 1.0, true);
                    int hit2 = hit1 == 0 ? getRandomMaxHit(player, weapon, fightType, false, true, 1.0, true) : hit1;
                    if (hit1 == 0 && hit2 == 0) {
                        int hit3 = getRandomMaxHit(player, weapon, fightType, false, true, 1.0, true);
                        if (hit3 == 0) {
                            int hit4 = getRandomMaxHit(player, weapon, fightType, false, true, 1.0, true);
                            if (hit4 == 0) {
                                delayNormalHit(weapon, fightType, getMeleeHit(player, hit1), getMeleeHit(player, hit2));
                                delayHit(1, weapon, fightType, getMeleeHit(player, hit3), getMeleeHit(player, 1));
                            } else {
                                delayNormalHit(weapon, fightType, getMeleeHit(player, hit1), getMeleeHit(player, hit2));
                                delayHit(1, weapon, fightType, getMeleeHit(player, hit3), getMeleeHit(player, (int) (hit4 * 1.5)));
                            }
                        } else {
                            delayNormalHit(weapon, fightType, getMeleeHit(player, hit1), getMeleeHit(player, hit2));
                            delayHit(1, weapon, fightType, getMeleeHit(player, hit3), getMeleeHit(player, hit3));
                        }
                    } else {
                        delayNormalHit(weapon, fightType, getMeleeHit(player, hit1), getMeleeHit(player, hit1 == 0 ? hit2 : hit2 / 2));
                        delayHit(1, weapon, fightType, getMeleeHit(player, hit1 == 0 ? hit2 / 2 : hit2 / 4), getMeleeHit(player, hit2 / 4));
                    }
                    break;
                case 10887: // anchor
                    player.animate(5870);
                    player.perform(new Graphic(1027));
                    delayNormalHit(weapon, fightType, getMeleeHit(player, getRandomMaxHit(player, weapon, fightType, false, false, 1.0, true)));
                    break;
                case 1305: // dragon long
                    player.animate(12033);
                    player.perform(new Graphic(2117));
                    delayNormalHit(weapon, fightType, getMeleeHit(player, getRandomMaxHit(player, weapon, fightType, false, true, 1.25, true)));
                    break;
                case 3204: // d hally
                    player.animate(1665);
                    player.perform(new Graphic(282));
                    if (target.getSize() < 3) {// giant npcs wont get stuned cuz of
                        // a stupid hit
                        target.perform(new Graphic(254, 0, 100));
                        target.perform(new Graphic(80));
                    }
                    delayNormalHit(weapon, fightType, getMeleeHit(player, getRandomMaxHit(player, weapon, fightType, false, true, 1.1, true)));
                    if (target.getSize() > 1)
                        delayHit(1, weapon, fightType, getMeleeHit(player, getRandomMaxHit(player, weapon, fightType, false, true, 1.1, true)));
                    break;
                case 4587: // dragon sci
                    player.animate(12031);
                    player.perform(new Graphic(2118));
                    if (target.isPlayer()) {
                        Player p2 = (Player) target;
                        p2.setPrayerDelay(5000);// 5 seconds
                    }
                    delayNormalHit(weapon, fightType, getMeleeHit(player, getRandomMaxHit(player, weapon, fightType, false, true, 1.0, true)));
                    soundId = 2540;
                    break;
                case 1215: // dragon dagger
                case 1231: // dragon dagger
                case 5680: // dragon dagger
                case 5698: // dds
                    player.animate(1062);
                    player.perform(new Graphic(252, 0, 100));
                    delayNormalHit(weapon, fightType, getMeleeHit(player, getRandomMaxHit(player, weapon, fightType, false, true, 1.15, true)), getMeleeHit(player, getRandomMaxHit(player, weapon, fightType, false, true, 1.15, true)));
                    soundId = 2537;
                    break;
                case 1434: // dragon mace
                    player.animate(1060);
                    player.perform(new Graphic(251));
                    delayNormalHit(weapon, fightType, getMeleeHit(player, getRandomMaxHit(player, weapon, fightType, false, true, 1.45, true)));
                    soundId = 2541;
                    break;
                default:
                    player.message("This weapon has no special Attack, if you still see special bar please relog.");
                    return combatDelay;
            }
        } else {
            if (player.getEquipment().get(Slot.SHIELD).getId() == 11283 && player.getTemporaryAttributes().remove("dfs_shield_active") != null) {
                CombatSpells.DRAGON_FIRE.spell.cast(player);
                delayMagicHit(player, target, 1, CombatSpells.DRAGON_FIRE.spell, getMagicHit(player, Magic.getMaxHit(player, target, CombatSpells.DRAGON_FIRE.spell)));
            } else
            if (weapon.getId() == 4755 && hasVeracsSet(player)) {
                boolean effect = Misc.random(4) == 0;//25%
                if (effect)
                    player.getTemporaryAttributes().put("verac_effect", Boolean.TRUE);
                delayNormalHit(weapon, fightType, getMeleeHit(player, getRandomMaxHit(player, weapon, fightType, false, effect ? false : true, 1.0D, false)));
                player.perform(new Animation(WeaponAnimations.getAttackAnimation(weapon, attackStyle)));
            } else {
                delayNormalHit(weapon, fightType, getMeleeHit(player, getRandomMaxHit(player, weapon, fightType, false)));
                player.perform(new Animation(WeaponAnimations.getAttackAnimation(weapon, attackStyle)));
            }
        }
        //playSound(soundId, player, target);
        return combatDelay;
    }

    public static boolean hasVeracsSet(Player player) {
        return player.getEquipment().containsAll(new Item(4753), new Item(4757), new Item(4759), new Item(4755));
    }

    public static boolean hasGuthansSet(Player player) {
        return player.getEquipment().containsAll(new Item(4724), new Item(4728), new Item(4730), new Item(4726));
    }

    public static boolean hasToragsSet(Player player) {
        return player.getEquipment().containsAll(new Item(4745), new Item(4749), new Item(4751), new Item(4747));
    }

    public static boolean hasKarilsSet(Player player) {
        return player.getEquipment().containsAll(new Item(4732), new Item(4736), new Item(4738), new Item(4734));
    }

    public static boolean hasAhrimsSet(Player player) {
        return player.getEquipment().containsAll(new Item(4708), new Item(4712), new Item(4714), new Item(4710));
    }

    public static void playSound(int soundId, Player player, Entity target) {
        if (soundId == -1)
            return;
        player.getPacketSender().sendSound(soundId, 0, 1);
        if (target.isPlayer()) {
            Player p2 = (Player) target;
            p2.getPacketSender().sendSound(soundId, 0, 1);
        }
    }

    public static int getSpecialAmount(Item weapon) {
        switch (weapon.getId()) {
            case 4587: // dragon sci
            case 859: // magic longbow
            case 861: // magic shortbow
            case 10284: // Magic composite bow
            case 18332: // Magic longbow (sighted)
            case 19149:// zamorak bow
            case 19151:
            case 19143:// saradomin bow
            case 19145:
            case 19146:
            case 19148:// guthix bow
                return 55;
            case 11235: // dark bows
            case 15701:
            case 15702:
            case 15703:
            case 15704:
                return 65;
            case 13899: // vls
            case 13901:
            case 1305: // dragon long
            case 1215: // dragon dagger
            case 1231: // dds
            case 5680: // dds
            case 5698: // dds
            case 1434: // dragon mace
            case 1249:// d spear
            case 1263:
            case 3176:
            case 5716:
            case 5730:
            case 13770:
            case 13772:
            case 13774:
            case 13776:
                return 25;
            case 15442:// whip start
            case 15443:
            case 15444:
            case 15441:
            case 4151:
            case 11698: // sgs
            case 11694: // ags
            case 13902: // statius hammer
            case 13904:
            case 13905: // vesta spear
            case 13907:
            case 14484: // d claws
            case 10887: // anchor
            case 3204: // d hally
            case 4153: // granite maul
            case 14684: // zanik cbow
            case 15241: // hand cannon
            case 13908:
            case 13954:// morrigan javelin
            case 13955:
            case 13956:
            case 13879:
            case 13880:
            case 13881:
            case 13882:
            case 13883:// morigan thrown axe
            case 13957:
                return 50;
            case 11730: // ss
            case 11696: // bgs
            case 11700: // zgs
            case 35:// Excalibur
            case 8280:
            case 14632:
            case 1377:// dragon battle axe
            case 13472:
            case 15486:// staff of lights
            case 22207:
            case 22209:
            case 22211:
            case 22213:
                return 100;
            case 19784: // korasi sword
                return 60;
            default:
                return 0;
        }
    }

    public int getRandomMaxHit(Player player, Item weapon, FightType attackStyle, boolean ranging) {
        return getRandomMaxHit(player, weapon, attackStyle, ranging, true, 1.0D, false);
    }

    public int getRandomMaxHit(Player player, Item weapon, FightType fightType, boolean ranging, boolean defenceAffects, double specMultiplier, boolean usingSpec) {
//        max_hit = getMaxHit(player, fightType, ranging, specMultiplier);
        player.getTemporaryAttributes().put("max_hit", getMaxHit(player, fightType, ranging, specMultiplier));
        if (defenceAffects) {
            double att = player.getSkills().getLevel(ranging ? Skill.RANGED : Skill.ATTACK) + player.getBonusManager().getAttackBonus()[ranging ? BonusManager.ATTACK_RANGE : player.getFightType().getBonusType()];
            att *= ranging ? player.getPrayer().getRangeMultiplier() : player.getPrayer().getAttackMultiplier();
            if (fullVoidEquipped(player, ranging ? (new int[]{11664, 11675}) : (new int[]{11665, 11676})))
                att *= 1.1;
            if (ranging)
                att *= player.getAuraManager().getRangeAccurayMultiplier();
            double def;
            if (target.isPlayer()) {
                Player p2 = (Player) target;
                def = (double) p2.getSkills().getLevel(Skill.DEFENCE) + (2 * p2.getBonusManager().getDefenceBonus()[ranging ? BonusManager.DEFENCE_RANGE : player.getFightType().getCorrespondingBonus()]);
                def *= p2.getPrayer().getDefenceMultiplier();
                if (!ranging && p2.getFamiliar() instanceof SteelTitan)
                    def *= 1.15;
            } else {
                Mob n = (Mob) target;
                def = n.getBonuses() != null ? n.getBonuses()[ranging ? CombatDefinitions.RANGE_DEF : CombatDefinitions.getMeleeDefenceBonus(CombatDefinitions.getMeleeBonusStyle(weapon, player.getCombatDefinitions().getAttackStyle()))] : 0;
            }
            if (usingSpec) {
                double multiplier = /* 0.25 + */specMultiplier;
                att *= multiplier;
            }
            double prob = att / def;
            if (prob > 0.90) // max, 90% prob hit so even lvl 138 can miss at lvl 3
                prob = 0.90;
            else if (prob < 0.05) //minimum 5% so even lvl 3 can hit lvl 138
                prob = 0.05;

            //Increases chances of hitting by 2%
            if (target.getEffects().hasActiveEffect(Effects.CANNIBAL_FRUIT))
                prob *= 0.98;

            if (prob < Math.random())
                return 0;
        }
        int hit = Utils.getRandom((int) player.getTemporaryAttributes().get("max_hit"));
        if (target.isNpc()) {
            Mob n = (Mob) target;
            if (n.getId() == 9463 && hasFireCape(player))
                hit += 40;
        }
        if (player.getAuraManager().usingEquilibrium()) {
            int perc25MaxHit = (int) ((int) player.getTemporaryAttributes().get("max_hit") * 0.25);
            hit -= perc25MaxHit;
            player.getTemporaryAttributes().put("max_hit", (int) player.getTemporaryAttributes().get("max_hit") - perc25MaxHit);
//            max_hit -= perc25MaxHit;
            if (hit < 0)
                hit = 0;
            if (hit < perc25MaxHit)
                hit += perc25MaxHit;
        }
        return hit;
    }

    public boolean hasFireCape(Player player) {
        int capeId = player.getEquipment().get(Slot.CAPE).getId();
        return capeId == 6570 || capeId == 20769 || capeId == 20771;
    }

    public static final int getMaxHit(Player player, FightType fightType, boolean ranging, double specMultiplier) {
        if (!ranging) {
            int level = player.getSkills().getLevel(Skill.STRENGTH);
            double styleBonus = fightType.getStyle() == FightStyle.AGGRESSIVE ? 3 : fightType.getStyle() == FightStyle.CONTROLLED ? 1 : 0;
            double otherBonus = 1;
            if (fullDharokEquipped(player)) {
                double current = player.getHealth().getHitpoints();
                double max = player.getMaxConstitution();
                double d = current / max;
                otherBonus = 2 - d;
            }
            double effectiveStrength = 8 + level * player.getPrayer().getStrengthMultiplier() + styleBonus;
            if (fullVoidEquipped(player, 11665, 11676))
                effectiveStrength *= 1.1;
            double strengthBonus = player.getBonusManager().getOtherBonus()[BonusManager.BONUS_STRENGTH];
            double baseDamage = 5 + effectiveStrength * (1 + (strengthBonus / 64));
            double multiplier = 1.0;
            if (player.getEffects().hasActiveEffect(Effects.DRACONIC_FRUIT))
                multiplier = 1.02;

            return (int) (baseDamage * specMultiplier * otherBonus * multiplier);
        } else {
            double level = player.getSkills().getLevel(Skill.RANGED);
            int attackStyle = player.getCombatDefinitions().getAttackStyle();
            double styleBonus = attackStyle == 0 ? 3 : attackStyle == 1 ? 0 : 1;
            double otherBonus = 1;
            double effectiveStrength = (level * player.getPrayer().getRangeMultiplier() * otherBonus) + styleBonus;
            if (fullVoidEquipped(player, 11664, 11675))
                effectiveStrength += (player.getSkills().getMaxLevel(Skill.RANGED) / 5) + 1.6;
            double strengthBonus = player.getBonusManager().getOtherBonus()[BonusManager.RANGED_STRENGTH];
            double baseDamage = 5 + (((effectiveStrength + 8) * (strengthBonus + 64)) / 64);
            double multiplier = 1.0;
            if (player.getEffects().hasActiveEffect(Effects.DRACONIC_FRUIT))
                multiplier = 1.02;

            return (int) (baseDamage * specMultiplier * multiplier);
        }
    }

    public static final boolean fullVanguardEquipped(Player player) {
        Item helm = player.getEquipment().get(Slot.HEAD);
        Item chest = player.getEquipment().get(Slot.BODY);
        Item legs = player.getEquipment().get(Slot.LEGS);
        Item weapon = player.getEquipment().get(Slot.WEAPON);
        Item boots = player.getEquipment().get(Slot.FEET);
        Item gloves = player.getEquipment().get(Slot.HANDS);
        if (helm.getId() == -1 || chest.getId() == -1 || legs.getId() == -1 || weapon.getId() == -1
                || boots.getId() == -1 || gloves.getId() == -1)
            return false;
        return helm.getName().contains("Vanguard")
                && chest.getName().contains("Vanguard")
                && legs.getName().contains("Vanguard")
                && weapon.getName().contains("Vanguard")
                && boots.getName().contains("Vanguard")
                && gloves.getName().contains("Vanguard");
    }

    public static final boolean usingGoliathGloves(Player player) {
        String name = player.getEquipment().get(Slot.SHIELD).getId() != -1 ? player.getEquipment().get(Slot.SHIELD).getName().toLowerCase() : "";
        if (player.getEquipment().get(Slot.HANDS).getId() != -1) {
            if (player.getEquipment().get(Slot.HANDS).getName().toLowerCase().contains("goliath") && player.getEquipment().get(Slot.WEAPON).getId() == -1) {
                if (name.contains("defender") && name.contains("dragonfire shield"))
                    return true;
                return true;
            }
        }
        return false;
    }

    public static final boolean fullDharokEquipped(Player player) {
        Item helm = player.getEquipment().get(Slot.HEAD);
        Item chest = player.getEquipment().get(Slot.BODY);
        Item legs = player.getEquipment().get(Slot.LEGS);
        Item weapon = player.getEquipment().get(Slot.WEAPON);
        if (helm.getId() == -1 || chest.getId() == -1 || legs.getId() == -1 || weapon.getId() == -1)
            return false;
        return helm.getName().contains("Dharok's")
                && chest.getName().contains("Dharok's")
                && legs.getName().contains("Dharok's")
                && weapon.getName().contains("Dharok's");
    }

    public static final boolean fullVoidEquipped(Player player, int... helmid) {
        boolean hasDeflector = player.getEquipment().get(Slot.SHIELD).getId() == 19712;
        if (player.getEquipment().get(Slot.HANDS).getId() != 8842) {
            if (hasDeflector)
                hasDeflector = false;
            else
                return false;
        }
        int legsId = player.getEquipment().get(Slot.LEGS).getId();
        boolean hasLegs = legsId != -1
                && (legsId == 8840 || legsId == 19786 || legsId == 19788 || legsId == 19790);
        if (!hasLegs) {
            if (hasDeflector)
                hasDeflector = false;
            else
                return false;
        }
        int torsoId = player.getEquipment().get(Slot.BODY).getId();
        boolean hasTorso = torsoId != -1
                && (torsoId == 8839 || torsoId == 10611 || torsoId == 19785
                || torsoId == 19787 || torsoId == 19789);
        if (!hasTorso) {
            if (hasDeflector)
                hasDeflector = false;
            else
                return false;
        }
        if (hasDeflector)
            return true;
        int helmId = player.getEquipment().get(Slot.HEAD).getId();
        if (helmId == -1)
            return false;
        boolean hasHelm = false;
        for (int id : helmid) {
            if (helmId == id) {
                hasHelm = true;
                break;
            }
        }
        if (!hasHelm)
            return false;
        return true;
    }

    public void delayNormalHit(Item weapon, FightType attackStyle, Hit... hits) {
        delayHit(0, weapon, attackStyle, hits);
    }

    public Hit getMeleeHit(Player player, int damage) {
        return new Hit(player, damage, HitMask.RED, CombatIcon.MELEE);
    }

    public Hit getRangeHit(Player player, int damage) {
        return new Hit(player, damage, HitMask.RED, CombatIcon.RANGED);
    }

    public Hit getMagicHit(Player player, int damage) {
        return new Hit(player, damage, HitMask.RED, CombatIcon.MAGIC);
    }

    private void delayMagicHit(int delay, final Hit... hits) {
        delayHit(delay, new Item(-1, 0), FightType.UNARMED_PUNCH, hits);
    }

    private void delayMagicHit(Player player, Figure target, int delay, CombatSpell spell, final Hit hit) {
//        Swing.sendSpell(player, target, delay, spell, hit);
    }

    private void delayHit(int delay, final Item weapon, final FightType type, final Hit... hits) {
        Swing.delayHit(this.target, delay, weapon, type, hits);
    }


    private int getSoundId(Item weapon) {
        if (weapon.getId() != -1) {
            String weaponName = weapon.getName().toLowerCase();
            if (weaponName.contains("dart") || weaponName.contains("knife"))
                return 2707;
        }
        return -1;
    }

    private int getMeleeCombatDelay(Item weapon) {
        if (weapon.getId() != -1) {
            String weaponName = weapon.getName().toLowerCase();

            // Interval 2.4
            if (weaponName.contains("zamorakian spear"))// Works dont edit this
                // ^^
                return 3;
            // Interval 3.0
            if (weaponName.contains("spear")
                    || weaponName.contains("longsword")
                    || weaponName.contains("light")
                    || weaponName.contains("hatchet")
                    || weaponName.contains("pickaxe")
                    || weaponName.contains("mace")
                    || weaponName.contains("hasta")
                    || weaponName.contains("warspear")
                    || weaponName.contains("flail")
                    || weaponName.contains("hammers"))
                return 4;
            // Interval 3.6
            if (weaponName.contains("godsword")
                    || weaponName.contains("warhammer")
                    || weaponName.contains("battleaxe")
                    || weaponName.contains("maul"))
                return 5;
            // Interval 4.2
            if (weaponName.contains("greataxe")
                    || weaponName.contains("halberd")
                    || weaponName.contains("2h sword")
                    || weaponName.contains("two handed sword"))
                return 6;
        }
        switch (weapon.getId()) {
            case 6527:// tzhaar-ket-em
                return 4;
            case 10887:// barrelchest anchor
                return 5;
            case 15403:// balmung
            case 6528:// tzhaar-ket-om
                return 6;
            default:
                return 3;
        }
    }

    @Override
    public void stop(Player player) {
        if (player.getCastSpell() == null)
            player.getDirection().setInteracting(null);
    }

    private boolean checkAll(Player player) {
        if (player.isDead() || player.getFinished() || target.isDead() || target.getFinished())
            return false;
        int distanceX = player.getX() - target.getX();
        int distanceY = player.getY() - target.getY();
        int sizeX = target.getSizeX();
        int sizeY = target.getSizeY();
        int maxDistance = 16;
        if (player.getZ() != target.getZ() || distanceX > sizeX + maxDistance || distanceX < -1 - maxDistance || distanceY > sizeY + maxDistance || distanceY < -1 - maxDistance)
            return false;
        if (player.getCombat().getFreezeDelay() >= Utils.currentTimeMillis())
            return !player.isWithinDistance(target, 0);

        if (target.isPlayer()) {
            Player p2 = (Player) target;
            if (!player.isCanPvp() || !p2.isCanPvp())
                return false;
            if (player.getTemporaryAttributes().get("InstantSpecial") != null) {
                int specialId = (int) player.getTemporaryAttributes().get("InstantSpecial");
                if (specialId != player.getEquipment().get(Slot.WEAPON).getId())
                    return true;
                if (player.isAutoCast())
                    return true;
                player.getActionManager().setActionDelay(0);
                player.getTemporaryAttributes().remove("InstantSpecial");
                return true;
            }
            if (p2.getTemporaryAttributes().get("InstantSpecial") != null) {
                int specialId = (int) p2.getTemporaryAttributes().get("InstantSpecial");
                if (specialId != p2.getEquipment().get(Slot.WEAPON).getId())
                    return true;
                if (p2.isAutoCast())
                    return true;
                p2.getActionManager().setActionDelay(0);
                p2.getTemporaryAttributes().remove("InstantSpecial");
                return true;
            }
        } else {
            if (player.getTemporaryAttributes().get("InstantSpecial") != null) {
                int specialId = (int) player.getTemporaryAttributes().get("InstantSpecial");
                if (specialId != player.getEquipment().get(Slot.WEAPON).getId())
                    return true;
                if (player.isAutoCast())
                    return true;
                player.getActionManager().setActionDelay(0);
                player.getTemporaryAttributes().remove("InstantSpecial");
                return true;
            }
            Mob n = (Mob) target;
            SlayerManager manager = player.getSlayerManager();
            if (n.getName().contains("strykewyrm") && !manager.isCurrentTask(n.getName())) {
                player.message("You need to have strykewyrm assigned as a task in order to fight them.");
                return false;
            }
            int slayerLevel = Slayer.getLevelRequirement(n.getName());
            if (slayerLevel > player.getSkills().getLevel(Skill.SLAYER)) {
                player.message("You need a slayer level of " + slayerLevel + " to know how to wound this monster.");
                return false;
            }

            if (n.isCantInteract())
                return false;
            if (n.isFamiliar()) {
                Familiar familiar = (Familiar) n;
                if (!familiar.canAttack(player))
                    return false;

                if (player.getControllerManager().getController() instanceof Wilderness)
                    CombatConstants.skullPlayer(player);
            } else {
                /*if (!n.canBeAttackFromOutOfArea()
                        && !MapAreas.isAtArea(n.getMapAreaNameHash(), player))
                    return false;*/

                if (isAttackException(player, n))
                    return false;
            }
        }
        if (!(target instanceof Mob && ((Mob) target).isForceMultiAttacked())) {
            if (!target.inMulti() || !player.inMulti()) {

                if (player.getCombat().getAttackedBy() != target && player.getCombat().getAttackedByDelay() > Utils.currentTimeMillis()) {
                    player.message("You are already in combat.");
                    return false;
                }
                if (target.getCombat().getAttackedBy() != player && target.getCombat().getAttackedByDelay() > Utils.currentTimeMillis()) {
                    player.message("That " + (target.isPlayer() ? "player" : "npc") + " is already in combat.");
                    return false;
                }
            }
        }
        boolean isMagic = player.getCastSpell() != null || player.getAutoCastSpell() != null || player.getEquipment().get(Slot.WEAPON).getId() == 22494;
        int isRanging = isRanging(player);
        if (player.getX() == target.getX() && player.getY() == target.getY() && target.getSize() == 1 && !target.getMovement().hasWalkSteps()) {
            player.getMovement().reset();
            return !player.getMovement().stepAway();
        } else if (isRanging == 0 && target.getSize() == 1 && !isMagic
                && Math.abs(player.getX() - target.getX()) == 1
                && Math.abs(player.getY() - target.getY()) == 1
                && !target.getMovement().hasWalkSteps()) {
            if (!player.getMovement().addWalkSteps(target.getX(), player.getY(), 1)) {
                player.getMovement().reset();
                player.getMovement().addWalkSteps(player.getX(), target.getY(), 1);
            }
            return true;
        }
        maxDistance = isRanging != 0 || isMagic ? 7 : 0;
        if ((!player.getCombat().clippedProjectile(target, !(target instanceof NexMinion) && !(target instanceof PestPortal) && maxDistance == 0))
                || !Utils.isOnRange(player, target, maxDistance)) {
            if (!player.getMovement().hasWalkSteps()) {
                player.getMovement().reset();
                player.getMovement().addWalkStepsInteract(target.getX(), target.getY(), player.getSettings().getBool(Settings.RUNNING) ? 2 : 1, sizeX, sizeY, true);
            }
            return true;
        } else {
            player.getMovement().reset();
        }
        if (player.getEffects().hasActiveEffect(Effects.STAFF_OF_LIGHT) && !player.getEquipment().get(Slot.WEAPON).getName().equalsIgnoreCase("staff of light"))
            player.getEffects().removeEffect(Effects.STAFF_OF_LIGHT);
        return true;
    }

    public static boolean isAttackException(Player player, Mob n) {
        if (n.getId() == 14578) {
            if (player.getEquipment().get(Slot.WEAPON).getId() != 2402 && player.getAutoCastSpell() == null) {
                player.message("I'd better wield Silverlight first.");
                return true;
            } else {
                if (Slayer.getLevelRequirement(n.getName()) > player.getSkills().getLevel(Skill.SLAYER))
                    return true;
            }
        } else if (n.getId() == 6222 || n.getId() == 6223 || n.getId() == 6225 || n.getId() == 6227) {
            if (isRanging(player) == 0) {
                player.message("I can't reach that!");
                return true;
            }
        } else if (n.getId() == 14301 || n.getId() == 14302 || n.getId() == 14303 || n.getId() == 14304) {
            Glacyte glacyte = (Glacyte) n;
            if (glacyte.getGlacor().getTargetIndex() != -1 && player.getIndex() != glacyte.getGlacor().getTargetIndex()) {
                player.message("This isn't your target.");
                return true;
            }
        }
        return false;
    }

    /*
     * 0 not ranging, 1 invalid ammo so stops att, 2 can range, 3 no ammo
     */

    public static boolean isWieldingThrown(Player player) {
        Item weapon = player.getEquipment().get(Slot.WEAPON);
        if (weapon.getId() == -1)
            return false;
        String name = weapon.getName();
        //Those that don't need arrows
        return name.contains("knife") || name.contains("dart")
                || name.contains("javelin") || name.contains("thrownaxe")
                || name.contains("throwing axe")
                || name.toLowerCase().contains("crystal bow")
                || name.toLowerCase().contains("chinchompa")
                || name.contains("Zaryte bow")
                || name.toLowerCase().contains("sling")
                || name.equals("Sagaie")
                || name.equals("Bolas");
    }

    public static int isRanging(Player player) {
        //0 not ranging, 1 invalid ammo, 2 range, 3 no ammo
        Item weapon = player.getEquipment().get(Slot.WEAPON);
        if (weapon.getId() == -1)
            return 0;

        if (isWieldingThrown(player))
            return 2;

        CombatRangedAmmo.RangedWeaponData data = CombatRangedAmmo.RangedWeaponData.getData(player);

        if (data == null)
            return 0;

        Item ammo = player.getEquipment().get(Slot.ARROWS);
        if (ammo.getId() == -1)
            return 3;

        for (CombatRangedAmmo.AmmunitionData a : data.getAmmunitionData()) {
            for (int ammoId : a.getItemIds()) {
                if (ammo.getId() == ammoId) {
                    if (data == CombatRangedAmmo.RangedWeaponData.DARK_BOW && ammo.getAmount() < 2)
                        return 3;
                    return 2;
                }
            }
        }

        return 1;
    }
}
