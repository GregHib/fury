package com.fury.game.entity.character.combat;

import com.fury.game.container.impl.equip.Slot;
import com.fury.game.content.skill.Skill;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.actions.PlayerCombatAction;
import com.fury.game.entity.character.player.content.BonusManager;
import com.fury.core.model.item.Item;

public class CombatDefinitions {

    public static final int STAB_ATTACK = 0, SLASH_ATTACK = 1,
            CRUSH_ATTACK = 2, RANGE_ATTACK = 4, MAGIC_ATTACK = 3;
    public static final int STAB_DEF = 5, SLASH_DEF = 6, CRUSH_DEF = 7,
            RANGE_DEF = 9, MAGIC_DEF = 8, SUMMONING_DEF = 10;

    public static final int SHARED = -1;
    private Player player;
    private byte attackStyle;

    public CombatDefinitions(Player player) {
        this.player = player;
    }

    public static final int getMeleeDefenceBonus(int bonusId) {
        if (bonusId == BonusManager.ATTACK_STAB)
            return BonusManager.DEFENCE_STAB;
        if (bonusId == BonusManager.DEFENCE_SLASH)
            return BonusManager.DEFENCE_SLASH;
        return BonusManager.DEFENCE_CRUSH;
    }

    public int getAttackStyle() {
        return attackStyle;
    }

    public void setAttackStyle(int style) {
        int maxSize = 3;
        Item weapon = player.getEquipment().get(Slot.WEAPON);
        String name = weapon.getName().toLowerCase();
        if (weapon.getId() == -1 || PlayerCombatAction.isRanging(player) != 0 || name.contains("whip") || name.contains("halberd"))
            maxSize = 2;
        if (style > maxSize)
            style = maxSize;
        if (style != attackStyle) {
            attackStyle = (byte) style;
//            if (autoCastSpell > 1)
//                resetSpells(true);
//            else
//                refreshAttackStyle();
        }/* else if (autoCastSpell > 1)
            resetSpells(true);*/
    }

    public static final int getMeleeBonusStyle(Item weapon, int attackStyle) {
        if (weapon.getId() != -1) {
            String weaponName = weapon.getName().toLowerCase();
            if (weaponName.contains("whip"))
                return BonusManager.ATTACK_SLASH;
            if (weaponName.contains("staff of light")) {
                switch (attackStyle) {
                    case 0:
                        return BonusManager.ATTACK_STAB;
                    case 1:
                        return BonusManager.ATTACK_SLASH;
                    default:
                        return BonusManager.ATTACK_CRUSH;
                }
            }
            if (weaponName.contains("staff")
                    || weaponName.contains("granite mace")
                    || weaponName.contains("warhammer")
                    || weaponName.contains("tzhaar-ket-em")
                    || weaponName.contains("tzhaar-ket-om")
                    || weaponName.contains("maul"))
                return BonusManager.ATTACK_CRUSH;
            if (weaponName.contains("scimitar")
                    || weaponName.contains("korasi's sword")
                    || weaponName.contains("hatchet")
                    || weaponName.contains("claws")
                    || weaponName.contains("longsword")) {
                switch (attackStyle) {
                    case 2:
                        return BonusManager.ATTACK_STAB;
                    default:
                        return BonusManager.ATTACK_SLASH;
                }
            }
            if (weaponName.contains("mace") || weaponName.contains("anchor")) {
                switch (attackStyle) {
                    case 2:
                        return BonusManager.ATTACK_STAB;
                    default:
                        return BonusManager.ATTACK_CRUSH;
                }
            }
            if (weaponName.contains("halberd")) {
                switch (attackStyle) {
                    case 1:
                        return BonusManager.ATTACK_SLASH;
                    default:
                        return BonusManager.ATTACK_STAB;
                }
            }
            if (weaponName.contains("spear")) {
                switch (attackStyle) {
                    case 1:
                        return BonusManager.ATTACK_SLASH;
                    case 2:
                        return BonusManager.ATTACK_CRUSH;
                    default:
                        return BonusManager.ATTACK_STAB;
                }
            }
            if (weaponName.contains("pickaxe")) {
                switch (attackStyle) {
                    case 2:
                        return BonusManager.ATTACK_CRUSH;
                    default:
                        return BonusManager.ATTACK_STAB;
                }
            }

            if (weaponName.contains("dagger") || weaponName.contains("rapier")) {
                switch (attackStyle) {
                    case 2:
                        return BonusManager.ATTACK_SLASH;
                    default:
                        return BonusManager.ATTACK_STAB;
                }
            }

            if (weaponName.contains("godsword")
                    || weaponName.contains("greataxe")
                    || weaponName.contains("2h sword")
                    || weaponName.equals("saradomin sword")) {
                switch (attackStyle) {
                    case 2:
                        return BonusManager.ATTACK_CRUSH;
                    default:
                        return BonusManager.ATTACK_SLASH;
                }
            }

        }
        switch (weapon.getId()) {
            default:
                return BonusManager.ATTACK_CRUSH;
        }
    }

    public static final Skill getXpStyle(Item weapon, int attackStyle) {
        if (weapon.getId() != -1) {
            String weaponName = weapon.getName().toLowerCase();
            if (weaponName.contains("whip")) {
                switch (attackStyle) {
                    case 0:
                        return Skill.ATTACK;
                    case 1:
                        return null;
                    case 2:
                    default:
                        return Skill.DEFENCE;
                }
            }
            if (weaponName.contains("halberd")) {
                switch (attackStyle) {
                    case 0:
                        return null;
                    case 1:
                        return Skill.STRENGTH;
                    case 2:
                    default:
                        return Skill.DEFENCE;
                }
            }
            if (weaponName.contains("staff")) {
                switch (attackStyle) {
                    case 0:
                        return Skill.ATTACK;
                    case 1:
                        return Skill.STRENGTH;
                    case 2:
                    default:
                        return Skill.DEFENCE;
                }
            }
            if (weaponName.contains("godsword") || weaponName.contains("sword")
                    || weaponName.contains("2h")) {
                switch (attackStyle) {
                    case 0:
                        return Skill.ATTACK;
                    case 1:
                        return Skill.STRENGTH;
                    case 2:
                        return Skill.STRENGTH;
                    case 3:
                    default:
                        return Skill.DEFENCE;
                }
            }
        }
        switch (weapon.getId()) {
            case -1:
                switch (attackStyle) {
                    case 0:
                        return Skill.ATTACK;
                    case 1:
                        return Skill.STRENGTH;
                    case 2:
                    default:
                        return Skill.DEFENCE;
                }
            default:
                switch (attackStyle) {
                    case 0:
                        return Skill.ATTACK;
                    case 1:
                        return Skill.STRENGTH;
                    case 2:
                        return null;
                    case 3:
                    default:
                        return Skill.DEFENCE;
                }
        }
    }

    public boolean hasRingOfVigour() {
        return player.getEquipment().get(Slot.RING).getId() == 19669;
    }

}
