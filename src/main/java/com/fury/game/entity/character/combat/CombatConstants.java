package com.fury.game.entity.character.combat;

import com.fury.core.model.item.Item;
import com.fury.core.model.node.entity.Entity;
import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.container.impl.equip.Slot;
import com.fury.game.content.combat.CombatType;
import com.fury.game.content.skill.Skill;
import com.fury.game.entity.character.combat.effects.Effect;
import com.fury.game.entity.character.combat.effects.Effects;
import com.fury.game.entity.character.player.content.BonusManager;
import com.fury.game.world.map.Position;
import com.fury.util.Colours;
import com.fury.util.Utils;

public class CombatConstants {

    public static Skill[] NON_COMBAT_SKILLS = {Skill.COOKING, Skill.WOODCUTTING, Skill.FLETCHING, Skill.FISHING, Skill.FIREMAKING, Skill.CRAFTING, Skill.SMITHING, Skill.MINING, Skill.HERBLORE, Skill.AGILITY, Skill.THIEVING, Skill.FARMING, Skill.RUNECRAFTING, Skill.CONSTRUCTION, Skill.HUNTER};


    public static double getDragonFireMultiplier(Figure target, boolean black, boolean metal) {
        boolean superPot = target.getEffects().hasActiveEffect(Effects.SUPER_FIRE_IMMUNITY);
        boolean regularPot = target.getEffects().hasActiveEffect(Effects.FIRE_IMMUNITY);
        boolean antiShield = CombatConstants.hasAntiDragProtection(target);

        double multiplier = 1.0;

        if(target.isPlayer()) {
            Player player = (Player) target;
            if (antiShield) {
                multiplier -= metal ? 0.6 : 0.95;
                player.message("Your shield absorbs most of the dragon's fiery breath!", true);
                if (player.getEquipment().get(Slot.SHIELD).getId() == 11283)
                    CombatConstants.chargeDragonFireShield(player);
            }

            if (regularPot || superPot) {
                multiplier -= superPot ? 1.0 : 0.5;
                player.message("Your potion protects you from the heat of the dragon's breath", true);
            }

            if (multiplier > 0.0) {
                if (player.getPrayer().isMageProtecting() || (!black && !metal)) {
                    multiplier -= 0.1;
                    player.message("You manage to resist some of the dragon fire!", true);
                } else if(multiplier > 0.5) {
                    player.message("You're horribly burnt by the dragon fire!", true);
                }
            }
        } else if(target.isFamiliar())
            multiplier -= 0.3;

        return multiplier > 1.0 ? 1.0 : multiplier;
    }

    /*
    Cannon shooting speed is really fast (just projectile)
Gold ores/stocks don't deplete in shops?
    Runecrossbow sinew
    size/sizeXY and Utils & Misc duplicates
Can make more money off general stall than magic stall
Some npcs not agro on some players? If walk to region not tele?
Achivemeents!
Stocks don't restock as fast
     */
    public static boolean hasAntiDragProtection(Entity target) {
        if (target.isNpc())
            return false;
        Player p2 = (Player) target;
        int shieldId = p2.getEquipment().get(Slot.SHIELD).getId();
        return shieldId == 1540 || shieldId == 11283 || shieldId == 11284 || shieldId == 16079 || shieldId == 16933;
    }

    public static int getDefenceEmote(Entity target) {
        if (target.isNpc()) {
            Mob n = (Mob) target;
            return n.getCombatDefinition().getDefenceAnim();
        } else {
            Player p = (Player) target;
            Item shield = p.getEquipment().get(Slot.SHIELD);
            String shieldName = shield.getName().toLowerCase();
            if (shield.getId() == -1 || (shieldName.contains("book") && shield.getId() != 18346)) {
                Item weapon = p.getEquipment().get(Slot.WEAPON);
                if (weapon.getId() == -1)
                    return 424;
                String weaponName = weapon.getName().toLowerCase();
                if (weaponName != null && !weaponName.equals("null")) {
                    if (weaponName.contains("scimitar")
                            || weaponName.contains("korasi sword"))
                        return 15074;
                    if (weaponName.contains("whip"))
                        return 11974;
                    if (weaponName.contains("staff of light"))
                        return 12806;
                    if (weaponName.contains("longsword")
                            || weaponName.contains("darklight")
                            || weaponName.contains("silverlight")
                            || weaponName.contains("excalibur"))
                        return 388;
                    if (weaponName.contains("dagger"))
                        return 378;
                    if (weaponName.contains("rapier"))
                        return 13038;
                    if (weaponName.contains("pickaxe"))
                        return 397;
                    if (weaponName.contains("mace"))
                        return 403;
                    if (weaponName.contains("claws"))
                        return 404;
                    if (weaponName.contains("hatchet"))
                        return 397;
                    if (weaponName.contains("greataxe"))
                        return 12004;
                    if (weaponName.contains("wand"))
                        return 415;
                    if (weaponName.contains("staff"))
                        return 420;
                    if (weaponName.contains("warhammer")
                            || weaponName.contains("tzhaar-ket-em"))
                        return 403;
                    if (weaponName.contains("maul")
                            || weaponName.contains("tzhaar-ket-om"))
                        return 1666;
                    if (weaponName.contains("zamorakian spear"))
                        return 12008;
                    if (weaponName.contains("spear")
                            || weaponName.contains("halberd")
                            || weaponName.contains("hasta"))
                        return 430;
                    if (weaponName.contains("2h sword")
                            || weaponName.contains("godsword")
                            || weaponName.equals("saradomin sword"))
                        return 7050;
                }
                return 424;
            }
            if (shieldName != null) {
                if (shieldName.contains("shield"))
                    return 1156;
                if (shieldName.contains("defender"))
                    return 4177;
            }
            switch (shield.getId()) {
                case -1:
                default:
                    return 424;
            }
        }
    }

    /**
     * Attempts to put the skull icon on the argued player, including the effect
     * where the player loses all item upon death. This method will have no
     * effect if the argued player is already skulled.
     *
     * @param player the player to attempt to skull to.
     */
    public static void skullPlayer(Player player) {

        // We are already skulled, return.
        if (player.getEffects().hasActiveEffect(Effects.SKULL)) {
            return;
        }

        // Otherwise skull the player as normal.
        player.getEffects().startEffect(new Effect(Effects.SKULL, 300));
        player.message("You have been skulled!", Colours.RED);
    }

    public static void handleDragonFireShield(final Player player) {
        if (player == null || player.getHealth().getHitpoints() <= 0)
            return;
        if (!player.getTimers().getClickDelay().elapsed(4000))
            return;
        player.getTimers().getClickDelay().reset();

        player.getTemporaryAttributes().put("dfs_shield_active", true);
    }

    /**
     * Calculates the combat level difference for wilderness player vs. player
     * combat.
     *
     * @param combatLevel      the combat level of the first person.
     * @param otherCombatLevel the combat level of the other person.
     * @return the combat level difference.
     */
    public static int combatLevelDifference(int combatLevel,
                                            int otherCombatLevel) {
        if (combatLevel > otherCombatLevel) {
            return (combatLevel - otherCombatLevel);
        } else if (otherCombatLevel > combatLevel) {
            return (otherCombatLevel - combatLevel);
        } else {
            return 0;
        }
    }

    public static void chargeDragonFireShield(Player player) {
        if (player.getDfsCharges() >= 20) {
            player.message("Your Dragonfire shield is fully charged and can be operated.");
            return;
        }
        player.animate(6695);
        player.graphic(1164);
        player.incrementDfsCharges(1);
        BonusManager.update(player);
        player.message("Your shield absorbs some of the Dragon's fire..", true);
    }
    public static final int[] MAGIC_DELAYS = { 2, 2, 3, 3, 3, 4, 4, 4, 5, 5, 5 };
    public static final int[] RANGED_DELAYS = { 2, 2, 2, 3, 3, 3, 3, 3, 3, 4, 4 };

    public static int getHitDelay(Player player, Position position, CombatType type) {
        if(type != CombatType.MELEE) {
            int distance = Utils.getDistance(player, position);
            if (distance > 10)
                distance = 10;

            if (type == CombatType.RANGED)
                return RANGED_DELAYS[distance];

            if (type == CombatType.MAGIC)
                return MAGIC_DELAYS[distance];
        }
        return 0;
    }
}
