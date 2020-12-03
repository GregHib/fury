package com.fury.game.container.impl.equip;

import com.fury.cache.Revision;
import com.fury.cache.def.Loader;
import com.fury.game.container.types.StackContainer;
import com.fury.game.content.controller.impl.duel.DuelController;
import com.fury.game.content.global.Achievements;
import com.fury.game.content.global.minigames.impl.OldDueling;
import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.free.firemaking.bonfire.Bonfire;
import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.game.entity.character.combat.effects.Effects;
import com.fury.game.entity.character.combat.equipment.weapon.CombatSpecial;
import com.fury.game.entity.character.combat.magic.Autocasting;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.actions.ItemMorph;
import com.fury.game.entity.character.player.content.BonusManager;
import com.fury.game.entity.character.player.content.PlayerInteractingOption;
import com.fury.game.entity.character.player.content.Sounds;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.core.model.item.Item;
import com.fury.game.system.files.loaders.item.WeaponAnimations;
import com.fury.game.system.files.loaders.item.WeaponInterfaces;
import com.fury.game.world.update.flag.Flag;
import com.fury.network.packet.impl.ItemActionPacketListener;
import com.fury.util.FontUtils;
import com.fury.util.Misc;

import java.util.HashMap;
import java.util.Objects;
import java.util.function.Consumer;

import static com.fury.game.container.impl.equip.Slot.WEAPON;

public class Equipment extends StackContainer {

    public static final int INVENTORY_INTERFACE_ID = 1688;

    public Equipment(Player player) {
        super(player, 15);
    }

    @Override
    public boolean add(Item item) {
        boolean add = isStackable(item) ? stack(item) : super.set(item, getItemIndex(item));
        if (add)
            refresh();
        return add;
    }

    @Override
    public boolean set(Item item, int index) {
        boolean set = super.set(item, index);
        if (set)
            refresh();
        return set;
    }

    @Override
    public boolean delete(int index) {
        boolean delete = super.delete(index);
        if (delete)
            refresh();
        return delete;
    }

    @Override
    public void refresh() {
        player.getPacketSender().sendItemContainer(this, 1688);
        player.getUpdateFlags().add(Flag.APPEARANCE);
        refreshConfigs();
    }

    public boolean contains(Item... items) {
        for (Item item : items)
            if (item != null && contains(item))
                return true;

        return false;
    }

    public boolean containsAll(Item... items) {
        for (Item item : items)
            if (item == null || !contains(item))
                return false;

        return true;
    }

    public int getItemIndex(Item item) {
        if (item != null && item.getId() != -1) {
            int slot = Loader.getItem(item.getId(), item.getRevision() == Revision.RS3 ? Revision.RS3 : Revision.PRE_RS3).equipSlot;
            return slot == 8 ? 0 : slot;
        }
        return -1;
    }

    public boolean handleUnequip(int id, int slot) {
        if (validate(id, slot))
            return unequip(get(slot), slot);
        return false;
    }

    public boolean unequip(Item item, int slot) {
        if (item == null)
            return false;

        move(item, player.getInventory());
        if (slot == WEAPON.ordinal())
            resetWeapon(player);
        BonusManager.update(player);
        refresh();
        return true;
    }

    public boolean exists(Slot slot) {
        return exists(slot.ordinal());
    }

    public Item get(Slot slot) {
        Item item = get(slot.ordinal());
        return item == null ? new Item(-1, 0) : item;
    }

    @Override
    public Item get(int index) {
        if (indexOutOfBounds(index))
            return null;
        return items[index] == null ? new Item(-1, 0) : items[index].copy();
    }

    public boolean equip(Item item) {
        return equip(item, Slot.values()[getItemIndex(item)]);
    }

    public boolean equip(Item item, Slot slot) {
        if (!player.getControllerManager().canEquip(item, slot))
            return false;
        boolean weaponChanged = false;
        Slot toRemove = null;
        switch (slot) {
            case HEAD:
                if (item.getId() == 6583)
                    player.getActionManager().setAction(new ItemMorph(2626));
                else if (item.getId() == 7927)
                    player.getActionManager().setAction(new ItemMorph(3689 + Misc.random(5)));
                break;
            case WEAPON:
                if (isTwoHandedWeapon(item)) {
                    if (exists(Slot.SHIELD) && exists(WEAPON) && player.getInventory().getSpaces() == 0) {
                        player.getInventory().full();
                        return false;
                    }
                    toRemove = Slot.SHIELD;
                }
                weaponChanged = true;
                break;
            case SHIELD:
                Item weapon = get(WEAPON);
                if (isTwoHandedWeapon(weapon) && weapon != null) {
                    toRemove = WEAPON;
                    weaponChanged = true;
                }
                break;
        }

        //Equip
        boolean move = !exists(slot);
        int index = move ? player.getInventory().indexOf(item) : player.getInventory().getFreeIndex();

        if (item.getDefinition().isStackable() && player.getEquipment().get(slot).isEqual(item))
            player.getInventory().move(item, this);
        else if (move)
            player.getInventory().move(index, this, slot.ordinal());
        else if (player.getInventory().swap(item, this, get(slot)))
            player.getInventory().refresh();

        if (toRemove != null)
            move(toRemove.ordinal(), player.getInventory(), index);

        //Update
        if (weaponChanged)
            resetWeapon(player);

        BonusManager.update(player);
        return true;
    }

    public static boolean isTwoHandedWeapon(Item item) {
        if (item != null)
            return Loader.getItem(item.getId(), item.getRevision() == Revision.RS2 ? Revision.PRE_RS3 : item.getRevision()).equipType == 5;
        return false;
    }

    public static void resetWeapon(Player player) {
        Item weapon = player.getEquipment().get(WEAPON);

        if (player.getEffects().hasActiveEffect(Effects.STAFF_OF_LIGHT) && !weapon.getName().equalsIgnoreCase("staff of light"))
            player.getEffects().removeEffect(Effects.STAFF_OF_LIGHT);

        //Snowball
        if (weapon.getId() == 11951) {
            player.getPacketSender().sendInteractionOption("Pelt", 2, false);
        } else if (player.getPlayerInteractingOption() == PlayerInteractingOption.PELT)
            if (player.isCanPvp())
                player.getPacketSender().sendInteractionOption("Attack", 2, true);
            else
                player.getPacketSender().sendInteractionOption("null", 2, false);

        WeaponInterfaces.assign(player, weapon);
        WeaponAnimations.update(player);
        if (player.getAutoCastSpell() != null || player.isAutoCast()) {
            Autocasting.resetAutoCast(player, true);
            player.message("Autocast spell cleared.", true);
        }
        player.getSettings().setSpecialToggled(false);
        CombatSpecial.updateBar(player);
    }

    public boolean handleEquip(Item item) {
        if (item == null)
            return false;

        int equipIndex = player.getEquipment().getItemIndex(item);

        if (equipIndex == -1 || equipIndex >= Slot.values().length)
            return false;

        Slot slot = Slot.values()[equipIndex];

        HashMap<Integer, Integer> requirements = item.getDefinition().getSkillRequirements();
        if (requirements != null) {
            for (int index : requirements.keySet()) {

                //RS3 items with new skills
                if (index >= Skill.values().length)
                    continue;

                Skill skill = Skill.values()[index];
                int requirement = requirements.get(index);

                if(requirement == 99 && item.getRevision() == Revision.RS3) {//Master capes
                    if (!player.getSkills().hasExpRequirement(skill, 200000000, "wear this"))
                        return false;
                } else {
                    if(!player.getSkills().hasMaxRequirement(skill, requirement, "wear this"))
                        return false;
                }
            }
        }

        if (!item.getDefinition().equipable()) {
            ItemActionPacketListener.fourthAction(player, item);
            return false;
        }

        switch (item.getId()) {
            case 6583:
            case 7927://Ring of stone + easter ring
                if (player.getCombat().isUnderAttack()) {
                    player.message("You can't do this while in combat.");
                    return false;
                }
                break;
            case 286:
            case 287:
            case 288:
            case 9054:
            case 9055:
            case 9056:
            case 9057:
            case 9058:
            case 9059:
                player.message("This looks too small for you to wear.");
                return false;
            case 1061:
                Achievements.finishAchievement(player, Achievements.AchievementData.EQUIP_LEATHER_BOOTS);
                break;
            case 15345:
            case 11136:
            case 11756:
            case 13560:
            case 14577:
            case 14631:
            case 14571:
            case 24134:
                if (!Achievements.hasFinishedAll(player, Achievements.Difficulty.EASY) && !player.getRights().isOrHigher(PlayerRights.OWNER)) {
                    player.message("You need to have completed all easy achievements in order to equip this.");
                    return false;
                }
                break;
            case 15347:
            case 11138:
            case 11757:
            case 13561:
            case 14578:
            case 14662:
            case 14572:
            case 24135:
                if (!Achievements.hasFinishedAll(player, Achievements.Difficulty.MEDIUM) && !player.getRights().isOrHigher(PlayerRights.OWNER)) {
                    player.message("You need to have completed all medium achievements in order to equip this.");
                    return false;
                }
                break;
            case 15349:
            case 11140:
            case 11758:
            case 13562:
            case 14579:
            case 14663:
            case 14573:
            case 24136:
                if (!Achievements.hasFinishedAll(player, Achievements.Difficulty.HARD) && !player.getRights().isOrHigher(PlayerRights.OWNER)) {
                    player.message("You need to have completed all hard achievements in order to equip this.");
                    return false;
                }
                break;
            case 19748:
            case 19754:
            case 19757:
            case 19760:
            case 19749:
            case 19763:
            case 19766:
            case 24137:
                if (!Achievements.hasFinishedAll(player, Achievements.Difficulty.ELITE) && !player.getRights().isOrHigher(PlayerRights.OWNER)) {
                    player.message("You need to have completed all elite achievements in order to equip this.");
                    return false;
                }
                break;
        }

        if (player.getControllerManager().getController() instanceof DuelController) {
            for (int i = 10; i < player.getDueling().selectedDuelRules.length; i++) {
                if (player.getDueling().selectedDuelRules[i]) {
                    OldDueling.DuelRule duelRule = OldDueling.DuelRule.forId(i);
                    if (slot.ordinal() == duelRule.getEquipmentSlot() || duelRule == OldDueling.DuelRule.NO_SHIELD && isTwoHandedWeapon(item)) {
                        player.message("The rules that were set do not allow this item to be equipped.");
                        return false;
                    }
                }
            }
            if (player.getDueling().selectedDuelRules[OldDueling.DuelRule.LOCK_WEAPON.ordinal()]) {
                if (slot == WEAPON || isTwoHandedWeapon(item)) {
                    player.message("Weapons have been locked during this duel!");
                    return false;
                }
            }
        }

        switch (slot) {
            case RING:
                if (item.getId() == 2570)
                    player.getPacketSender().sendMessage(FontUtils.imageTags(535) + " Warning! The Ring of Life special effect does not work in the Wilderness or", 0x996633).sendMessage("Duel Arena.", 0x996633);
                break;
        }

        player.setCastSpell(null);
        Sounds.sendSound(player, Sounds.Sound.EQUIP_ITEM);

        equip(item, slot);
        return true;
    }

    public int getAuraId() {
        Item item = get(Slot.AURA);
        if (item == null)
            return -1;
        return item.getId();
    }

    public boolean hasRevision(Revision revision) {
        for (Item item : items)
            if (item != null && item.getRevision() == revision)
                return true;
        return false;
    }


    /**
     * Junk to sort out
     */

    public boolean wearingHalberd() {
        return player.getEquipment().get(WEAPON).getName().toLowerCase().endsWith("halberd");
    }

    public static boolean wearingRingOfWealth(Player player) {
        return player.getEquipment().get(Slot.RING) != null && player.getEquipment().get(Slot.RING).getName().toLowerCase().startsWith("ring of wealth");
    }

    public static boolean wearingArdyCloak(Player player, int tier) {
        if (player.getEquipment().get(Slot.CAPE) != null) {
            if (tier >= 1 && player.getEquipment().get(Slot.CAPE).getId() == 15345)
                return true;
            if (tier >= 2 && player.getEquipment().get(Slot.CAPE).getId() == 15347)
                return true;
            if (tier >= 3 && player.getEquipment().get(Slot.CAPE).getId() == 15349)
                return true;
            if (tier >= 4 && player.getEquipment().get(Slot.CAPE).getId() == 19748)
                return true;
        }
        return false;
    }

    public static boolean wearingKaramjaGloves(Player player, int tier) {
        if (player.getEquipment().get(Slot.HANDS) != null) {
            if (tier >= 1 && player.getEquipment().get(Slot.HANDS).getId() == 11136)
                return true;
            if (tier >= 2 && player.getEquipment().get(Slot.HANDS).getId() == 11138)
                return true;
            if (tier >= 3 && player.getEquipment().get(Slot.HANDS).getId() == 11140)
                return true;
            if (tier >= 4 && player.getEquipment().get(Slot.HANDS).getId() == 19754)
                return true;
        }
        return false;
    }

    public static boolean wearingExplorersRing(Player player, int tier) {
        if (player.getEquipment().get(Slot.RING) != null) {
            if (tier >= 1 && player.getEquipment().get(Slot.RING).getId() == 13560)
                return true;
            if (tier >= 2 && player.getEquipment().get(Slot.RING).getId() == 13561)
                return true;
            if (tier >= 3 && player.getEquipment().get(Slot.RING).getId() == 13562)
                return true;
            if (tier >= 4 && player.getEquipment().get(Slot.RING).getId() == 19760)
                return true;
        }
        return false;
    }

    public static boolean wearingVarrockArmour(Player player, int tier) {
        if (player.getEquipment().get(Slot.BODY) != null) {
            if (tier >= 1 && player.getEquipment().get(Slot.BODY).getId() == 11756)
                return true;
            if (tier >= 2 && player.getEquipment().get(Slot.BODY).getId() == 11757)
                return true;
            if (tier >= 3 && player.getEquipment().get(Slot.BODY).getId() == 11758)
                return true;
            if (tier >= 4 && player.getEquipment().get(Slot.BODY).getId() == 19757)
                return true;
        }
        return false;
    }

    public static boolean wearingSeersHeadband(Player player, int tier) {
        if (player.getEquipment().get(Slot.HEAD) != null) {
            if (tier >= 1 && player.getEquipment().get(Slot.HEAD).getId() == 14631)
                return true;
            if (tier >= 2 && player.getEquipment().get(Slot.HEAD).getId() == 14662)
                return true;
            if (tier >= 3 && player.getEquipment().get(Slot.HEAD).getId() == 14663)
                return true;
            if (tier >= 4 && player.getEquipment().get(Slot.HEAD).getId() == 19763)
                return true;
        }
        return false;
    }

    public static boolean wearingSeaBoots(Player player, int tier) {
        if (player.getEquipment().get(Slot.FEET) != null) {
            if (tier >= 1 && player.getEquipment().get(Slot.FEET).getId() == 14571)
                return true;
            if (tier >= 2 && player.getEquipment().get(Slot.FEET).getId() == 14572)
                return true;
            if (tier >= 3 && player.getEquipment().get(Slot.FEET).getId() == 14573)
                return true;
            if (tier >= 4 && player.getEquipment().get(Slot.FEET).getId() == 19766)
                return true;
        }
        return false;
    }

    public static boolean wearingMorytaniaLegs(Player player, int tier) {
        if (player.getEquipment().get(Slot.LEGS) != null) {
            if (tier >= 1 && player.getEquipment().get(Slot.LEGS).getId() == 24134)
                return true;
            if (tier >= 2 && player.getEquipment().get(Slot.LEGS).getId() == 24135)
                return true;
            if (tier >= 3 && player.getEquipment().get(Slot.LEGS).getId() == 24136)
                return true;
            if (tier >= 4 && player.getEquipment().get(Slot.LEGS).getId() == 24137)
                return true;
        }
        return false;
    }

    public static boolean wearingFaladorShield(Player player, int tier) {
        if (player.getEquipment().get(Slot.SHIELD) != null) {
            if (tier >= 1 && player.getEquipment().get(Slot.SHIELD).getId() == 14577)
                return true;
            if (tier >= 2 && player.getEquipment().get(Slot.SHIELD).getId() == 14578)
                return true;
            if (tier >= 3 && player.getEquipment().get(Slot.SHIELD).getId() == 14579)
                return true;
            if (tier >= 4 && player.getEquipment().get(Slot.SHIELD).getId() == 19749)
                return true;
        }
        return false;
    }

    public static boolean hasAntiDragonProtection(Figure target) {
        if (target.isNpc())
            return false;
        Player player = (Player) target;
        int shieldId = player.getEquipment().get(Slot.SHIELD).getId();
        return shieldId == 1540 || shieldId == 11283 || shieldId == 11284 || shieldId == 11285 || shieldId == 16933;
    }

    public static boolean isPvpArmour(Item item) {
        if (item.getDefinition() != null && item.getDefinition().getName() != null) {
            String name = item.getDefinition().getName().toLowerCase();
            if (name.contains("morrigan's") || name.contains("zuriel's") || name.contains("statius's") || name.contains("vesta's"))
                return true;
        }
        return false;
    }

    private int hpIncrease;

    public void refreshConfigs() {
        double hpIncrease = 0;
        if (player.getEffects().hasActiveEffect(Effects.BONFIRE)) {
            int maxhp = player.getSkills().getLevel(Skill.CONSTITUTION);
            hpIncrease += (maxhp * Bonfire.getBonfireBoostMultiplier(player)) - maxhp;
        }

        if (player.getEffects().hasActiveEffect(Effects.CONSTITUTION_CRYSTAL)) {
            int maxhp = player.getSkills().getLevel(Skill.CONSTITUTION);
            hpIncrease += maxhp * 0.5;
        }

        for (Slot slot : Slot.values()) {
            Item item = get(slot.ordinal());

            if (slot == Slot.HEAD) {
                if (item.getId() == 20135 || item.getId() == 20137
                        || item.getId() == 20147 || item.getId() == 20149
                        || item.getId() == 20159 || item.getId() == 20161)
                    hpIncrease += 66;
            } else if (slot == Slot.BODY) {
                if (item.getId() == 20139 || item.getId() == 20141
                        || item.getId() == 20151 || item.getId() == 20153
                        || item.getId() == 20163 || item.getId() == 20165)
                    hpIncrease += 200;
            } else if (slot == Slot.LEGS) {
                if (item.getId() == 20143 || item.getId() == 20145
                        || item.getId() == 20155 || item.getId() == 20157
                        || item.getId() == 20167 || item.getId() == 20169)
                    hpIncrease += 134;
            }
        }

        if ((int) hpIncrease != this.hpIncrease) {
            this.hpIncrease = (int) hpIncrease;
            int maxHP = player.getMaxConstitution();
            if (player.getHealth().getHitpoints() > maxHP)
                player.getHealth().setHitpoints(maxHP);
        }
    }

    public int getHpIncrease() {
        return hpIncrease;
    }

    public void forEach(Consumer<? super Item> action) {
        Objects.requireNonNull(action);
        for (Item item : items) {
            if (item != null) {
                action.accept(item);
            }
        }
    }
}
