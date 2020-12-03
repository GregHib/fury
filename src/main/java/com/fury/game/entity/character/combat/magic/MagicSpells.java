package com.fury.game.entity.character.combat.magic;

import com.fury.cache.def.Loader;
import com.fury.core.model.item.Item;
import com.fury.core.model.node.entity.actor.figure.combat.magic.Spell;
import com.fury.core.model.node.entity.actor.figure.combat.magic.context.SpellOnFigureContext;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.container.impl.equip.Slot;
import com.fury.game.content.combat.magic.spell.dungeoneering.CreateGatestone;
import com.fury.game.content.combat.magic.spell.dungeoneering.GroupGatestoneTeleport;
import com.fury.game.content.combat.magic.spell.lunar.*;
import com.fury.game.content.combat.magic.spell.modern.enchant.*;
import com.fury.game.content.combat.magic.spell.modern.skill.*;
import com.fury.game.content.combat.magic.spell.modern.skill.charge.ChargeAirOrb;
import com.fury.game.content.combat.magic.spell.modern.skill.charge.ChargeEarthOrb;
import com.fury.game.content.combat.magic.spell.modern.skill.charge.ChargeFireOrb;
import com.fury.game.content.combat.magic.spell.modern.skill.charge.ChargeWaterOrb;
import com.fury.util.Utils;

/**
 * Holds data for all no-combat spells
 * @author Gabriel Hannason
 */
public enum MagicSpells {
	BONES_TO_BANANAS(new BonesToBananas()),
	LOW_ALCHEMY(new LowAlchemy()),
	TELEKINETIC_GRAB(new TelekineticGrab()),
	SUPERHEAT_ITEM(new SuperheatItem()),
	HIGH_ALCHEMY(new HighAlchemy()),
	BONES_TO_PEACHES(new BonesToPeaches()),
	BAKE_PIE(new BakePie()),
	VENGEANCE_OTHER(new VengeanceOther()),
	VENGEANCE_GROUP(new VengeanceGroup()),
	VENGEANCE(new Vengeance()),
	PLANK_MAKE(new PlankMake()),
    HUMIDIFY(new Humidify()),
    HUNTER_KIT(new HunterKit()),
    CURE_PLANT(new CurePlant()),
    CURE_OTHER(new CureOther()),
    CURE_ME(new CureMe()),
    CURE_GROUP(new CureGroup()),
	CREATE_GATESTONE(new CreateGatestone()),
    GROUP_GATESTONE_TELEPORT(new GroupGatestoneTeleport()),
	ENCHANT_SAPPHIRE(new EnchantSapphire()),
	ENCHANT_EMERALD(new EnchantEmerald()),
	ENCHANT_RUBY(new EnchantRuby()),
	ENCHANT_DIAMOND(new EnchantDiamond()),
	ENCHANT_DRAGONSTONE(new EnchantDragonstone()),
	ENCHANT_ONYX(new EnchantOnyx()),
	CHARGE(new Charge()),
	CHARGE_WATER_ORB(new ChargeWaterOrb()),
	CHARGE_EARTH_ORB(new ChargeEarthOrb()),
	CHARGE_FIRE_ORB(new ChargeFireOrb()),
	CHARGE_AIR_ORB(new ChargeAirOrb()),
	;

	private static final int AIR_RUNE = 556, WATER_RUNE = 555;
	public static final int EARTH_RUNE = 557;
	private static final int FIRE_RUNE = 554;
	private static final int BODY_RUNE = 559;
	private static final int MIND_RUNE = 558;
	private static final int NATURE_RUNE = 561;
	private static final int CHAOS_RUNE = 562;
	private static final int COSMIC_RUNE = 564;
	public static final int DEATH_RUNE = 560;
	private static final int BLOOD_RUNE = 565;
	private static final int SOUL_RUNE = 566;
	public static final int ASTRAL_RUNE = 9075;
	private static final int LAW_RUNE = 563;
	private static final int ARMADYL_RUNE = 21773;
	private static final int ELEMENTAL_RUNE = 12850;
	private static final int CATALYTIC_RUNE = 12851;

	public static boolean hasSpecialRunes(Player player, int runeId, int amountRequired) {
		if (player.getInventory().getAmount(new Item(ELEMENTAL_RUNE)) >= amountRequired) {
			if (runeId == AIR_RUNE || runeId == WATER_RUNE || runeId == EARTH_RUNE || runeId == FIRE_RUNE)
				return true;
		}
		if (player.getInventory().getAmount(new Item(CATALYTIC_RUNE)) >= amountRequired) {
			if (runeId == ARMADYL_RUNE || runeId == MIND_RUNE || runeId == CHAOS_RUNE || runeId == DEATH_RUNE || runeId == BLOOD_RUNE || runeId == BODY_RUNE || runeId == NATURE_RUNE || runeId == ASTRAL_RUNE || runeId == SOUL_RUNE || runeId == LAW_RUNE)
				return true;
		}
		return false;
	}

	public static final boolean checkRunes(Player player, boolean delete, int... runes) {
		return checkRunes(player, delete, false, runes);
	}

	public static final boolean checkRunes(Player player, boolean delete, boolean dungeoneering, int... values) {
		int weaponId = player.getEquipment().get(Slot.WEAPON).getId();
		int shieldId = player.getEquipment().get(Slot.SHIELD).getId();
		int runesCount = 0;
		while (runesCount < values.length) {
			int runeId = values[runesCount++];
			int amount = values[runesCount++];
			if (hasSpecialRunes(player, runeId, amount))
				continue;
			else if (dungeoneering) {
				if (player.getInventory().getAmount(new Item(runeId - 1689)) >= amount)
					continue;
			}
			if (!(player.getInventory().getAmount(new Item(runeId)) >= amount)) {
				player.message("You do not have enough " + Loader.getItem(runeId).name.replace("rune", "Rune") + "s to cast this spell.");
				return false;
			}

		}
		if (delete) {
			if (hasStaffOfLight(weaponId) && !containsRune(LAW_RUNE, values) && !containsRune(NATURE_RUNE, values) && Utils.random(8) == 0) {
				player.message("The power of your staff of light saves some runes from being drained.", true);
				return true;
			}
			runesCount = 0;
			while (runesCount < values.length) {
				int runeId = values[runesCount++];
				int amount = values[runesCount++];
				if (hasSpecialRunes(player, runeId, amount))
					runeId = getRuneForId(runeId);
				else if (dungeoneering) {
					int bindedRune = runeId - 1689;
					if (player.getInventory().getAmount(new Item(bindedRune)) >= amount) {
						player.getInventory().delete(new Item(bindedRune, amount));
						continue; // won't delete the extra rune anyways.
					}
				}
				player.getInventory().delete(new Item(runeId, amount));
			}
		}
		return true;
	}

	public static int getRuneForId(int runeId) {
		if (runeId == AIR_RUNE || runeId == WATER_RUNE || runeId == EARTH_RUNE || runeId == FIRE_RUNE)
			return ELEMENTAL_RUNE;
		else if (runeId == ARMADYL_RUNE || runeId == DEATH_RUNE || runeId == MIND_RUNE || runeId == CHAOS_RUNE || runeId == BLOOD_RUNE || runeId == BODY_RUNE || runeId == NATURE_RUNE || runeId == ASTRAL_RUNE || runeId == SOUL_RUNE || runeId == LAW_RUNE)
			return CATALYTIC_RUNE;
		return -1;
	}

	public static boolean hasStaffOfLight(int weaponId) {
        return weaponId == 15486 || weaponId == 22207 || weaponId == 22209 || weaponId == 22211 || weaponId == 22213;
    }

	private static boolean containsRune(int rune, int[] integer) {
		for (int id : integer) {
			if (rune == id)
				return true;
		}
		return false;
	}

    public int getDungButton() {
        switch (this) {
            case BONES_TO_BANANAS:
                return 62984;
            case HIGH_ALCHEMY:
                return 63150;
            case LOW_ALCHEMY:
                return 63034;
            case HUMIDIFY:
                return 63246;
            case CURE_OTHER:
                return 63233;
            case CURE_ME:
                return 63272;
            case CURE_GROUP:
                return 63298;
            case VENGEANCE:
                return 63406;
            case VENGEANCE_OTHER:
                return 63393;
            case VENGEANCE_GROUP:
                return 63419;
                default:return -1;
        }
    }
	
	MagicSpells(Spell spell) {
		this.spell = spell;
	}
	
	private Spell spell;
	
	public Spell getSpell() {
		return spell;
	}
	
	public static MagicSpells forSpellId(int spellId) {
		for(MagicSpells spells : MagicSpells.values()) {
			if(spells.getSpell().getId() == spellId || (spells.getDungButton() != -1 && spells.getDungButton() == spellId))
				return spells;
		}
		return null;
	}

	public static boolean handle(Player player, int button) {
        for(MagicSpells spell : MagicSpells.values()) {
            if(spell.getSpell().getId() == button || (spell.getDungButton() != -1 && spell.getDungButton() == button)) {
                handleMagicSpells(player, spell);
                return true;
            }
        }

        return false;
    }

	public static void handleMagicSpells(Player player, MagicSpells magicSpell) {
		Spell spell = magicSpell.getSpell();

        if(spell == null)
            return;

        if(player.getControllerManager().handleMagicSpells(magicSpell))
        	return;

        if(spell.cast(player))
            player.getTimers().getClickDelay().reset();
	}

	public static boolean handleMagicSpells(Player player, int buttonId, Player target) {
		MagicSpells spell = forSpellId(buttonId);
        if(spell == null)
            return false;

		if(spell.spell.cast(player, new SpellOnFigureContext(target)))
            player.getTimers().getClickDelay().reset();
		return true;
	}
}
