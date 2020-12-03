package com.fury.game.entity.character.combat.magic;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.combat.magic.CombatSpell;
import com.fury.game.content.combat.magic.spell.Teleblock;
import com.fury.game.content.combat.magic.spell.ancient.barrage.*;
import com.fury.game.content.combat.magic.spell.ancient.blitz.*;
import com.fury.game.content.combat.magic.spell.ancient.burst.*;
import com.fury.game.content.combat.magic.spell.ancient.rush.*;
import com.fury.game.content.combat.magic.spell.modern.*;
import com.fury.game.content.combat.magic.spell.modern.curse.*;
import com.fury.game.content.combat.magic.spell.modern.element.blast.EarthBlast;
import com.fury.game.content.combat.magic.spell.modern.element.blast.FireBlast;
import com.fury.game.content.combat.magic.spell.modern.element.blast.WaterBlast;
import com.fury.game.content.combat.magic.spell.modern.element.blast.WindBlast;
import com.fury.game.content.combat.magic.spell.modern.element.bolt.EarthBolt;
import com.fury.game.content.combat.magic.spell.modern.element.bolt.FireBolt;
import com.fury.game.content.combat.magic.spell.modern.element.bolt.WaterBolt;
import com.fury.game.content.combat.magic.spell.modern.element.bolt.WindBolt;
import com.fury.game.content.combat.magic.spell.modern.element.strike.EarthStrike;
import com.fury.game.content.combat.magic.spell.modern.element.strike.FireStrike;
import com.fury.game.content.combat.magic.spell.modern.element.strike.WaterStrike;
import com.fury.game.content.combat.magic.spell.modern.element.strike.WindStrike;
import com.fury.game.content.combat.magic.spell.modern.element.surge.EarthSurge;
import com.fury.game.content.combat.magic.spell.modern.element.surge.FireSurge;
import com.fury.game.content.combat.magic.spell.modern.element.surge.WaterSurge;
import com.fury.game.content.combat.magic.spell.modern.element.surge.WindSurge;
import com.fury.game.content.combat.magic.spell.modern.element.wave.EarthWave;
import com.fury.game.content.combat.magic.spell.modern.element.wave.FireWave;
import com.fury.game.content.combat.magic.spell.modern.element.wave.WaterWave;
import com.fury.game.content.combat.magic.spell.modern.element.wave.WindWave;
import com.fury.game.content.combat.magic.spell.modern.god.ClawsOfGuthix;
import com.fury.game.content.combat.magic.spell.modern.god.FlamesOfZamorak;
import com.fury.game.content.combat.magic.spell.modern.god.SaradominStrike;
import com.fury.game.content.combat.magic.spell.modern.god.StormOfArmadyl;

/**
 * Holds all of the {@link CombatSpell}s that can be cast by an {@link Figure}.
 *
 * @author lare96
 */
public enum CombatSpells {

    /**
     * Normal spellbook spells.
     */
    WIND_RUSH(new WindRush()),
    WIND_STRIKE(new WindStrike()),
    CONFUSE(new Confuse()),
    WATER_STRIKE(new WaterStrike()),
    EARTH_STRIKE(new EarthStrike()),
    WEAKEN(new Weaken()),
    FIRE_STRIKE(new FireStrike()),
    WIND_BOLT(new WindBolt()),
    CURSE(new Curse()),
    BIND(new Bind()),
    WATER_BOLT(new WaterBolt()),
    EARTH_BOLT(new EarthBolt()),
    FIRE_BOLT(new FireBolt()),
    CRUMBLE_UNDEAD(new CrumbleUndead()),
    WIND_BLAST(new WindBlast()),
    WATER_BLAST(new WaterBlast()),
    IBAN_BLAST(new IbanBlast()),
    SNARE(new Snare()),
    SLAYER_DART(new MagicDart()),
    EARTH_BLAST(new EarthBlast()),
    FIRE_BLAST(new FireBlast()),
    SARADOMIN_STRIKE(new SaradominStrike()),
    CLAWS_OF_GUTHIX(new ClawsOfGuthix()),
    FLAMES_OF_ZAMORAK(new FlamesOfZamorak()),
    WIND_WAVE(new WindWave()),
    WATER_WAVE(new WaterWave()),
    VULNERABILITY(new Vulnerability()),
    EARTH_WAVE(new EarthWave()),
    ENFEEBLE(new Enfeeble()),
    FIRE_WAVE(new FireWave()),
    STORM_OF_ARMADYL(new StormOfArmadyl()),
    ENTANGLE(new Entangle()),
    STUN(new Stun()),
    WIND_SURGE(new WindSurge()),
    TELEBLOCK(new Teleblock()),
    WATER_SURGE(new WaterSurge()),
    EARTH_SURGE(new EarthSurge()),
    FIRE_SURGE(new FireSurge()),
    POLYPORE(new Polypore()),
    DRAGON_FIRE(new DragonFire()),

    /**
     * Ancient spellbook spells.
     */
    SMOKE_RUSH(new SmokeRush()),
    SHADOW_RUSH(new ShadowRush()),
    BLOOD_RUSH(new BloodRush()),
    ICE_RUSH(new IceRush()),
    MIASMIC_RUSH(new MiasmicRush()),
    SMOKE_BURST(new SmokeBurst()),
    SHADOW_BURST(new ShadowBurst()),
    BLOOD_BURST(new BloodBurst()),
    ICE_BURST(new IceBurst()),
    MIASMIC_BURST(new MiasmicBurst()),
    SMOKE_BLITZ(new SmokeBlitz()),
    SHADOW_BLITZ(new ShadowBlitz()),
    BLOOD_BLITZ(new BloodBlitz()),
    ICE_BLITZ(new IceBlitz()),
    MIASMIC_BLITZ(new MiasmicBlitz()),
    SMOKE_BARRAGE(new SmokeBarrage()),
    SHADOW_BARRAGE(new ShadowBarrage()),
    BLOOD_BARRAGE(new BloodBarrage()),
    ICE_BARRAGE(new IceBarrage()),
    MIASMIC_BARRAGE(new MiasmicBarrage());

    /**
     * The combat spell that can be casted.
     */
    public CombatSpell spell;

    /**
     * Create a new {@link CombatSpells}.
     *
     * @param spell the combat spell that can be casted.
     */
    CombatSpells(CombatSpell spell) {
        this.spell = spell;
    }

    /**
     * Gets the combat spell that can be casted.
     *
     * @return the combat spell that can be casted.
     */
    public CombatSpell getSpell() {
        return spell;
    }

    /**
     * Gets the spell constant by its spell id.
     *
     * @param spellId the spell to retrieve.
     * @return the spell constant with that spell id.
     */
    public static CombatSpell getSpell(int spellId) {
        for (CombatSpells spell : CombatSpells.values()) {
            if (spell.getSpell() == null)
                continue;

            if (spell.getSpell().getId() == spellId || (spell.getDungButton() != -1 && spell.getDungButton() == spellId)) {
                return spell.getSpell();
            }
        }
        return null;
    }

    public int getDungButton() {
        switch (this) {
            case WIND_STRIKE:
                return 62908;
            case CONFUSE:
                return 62919;
            case WATER_STRIKE:
                return 62932;
            case EARTH_STRIKE:
                return 62945;
            case WEAKEN:
                return 62958;
            case FIRE_STRIKE:
                return 62971;
            case WIND_BOLT:
                return 62997;
            case CURSE:
                return 63008;
            case BIND:
                return 63021;
            case WATER_BOLT:
                return 63045;
            case EARTH_BOLT:
                return 63058;
            case FIRE_BOLT:
                return 63087;
            case WIND_BLAST:
                return 63100;
            case WATER_BLAST:
                return 63111;
            case SNARE:
                return 63124;
            case EARTH_BLAST:
                return 63137;
            case FIRE_BLAST:
                return 63161;
            case WIND_WAVE:
                return 63174;
            case WATER_WAVE:
                return 63194;
            case VULNERABILITY:
                return 63207;
            case EARTH_WAVE:
                return 63259;
            case ENFEEBLE:
                return 63285;
            case FIRE_WAVE:
                return 63311;
            case ENTANGLE:
                return 63324;
            case STUN:
                return 63337;
            case WIND_SURGE:
                return 63350;
            case WATER_SURGE:
                return 63363;
            case EARTH_SURGE:
                return 63378;
            case FIRE_SURGE:
                return 63432;
            default:
                return -1;
        }
    }

    public static String getName(CombatSpell spell) {
        for (CombatSpells s : CombatSpells.values()) {
            if (s.getSpell() == spell)
                return s.name();
        }
        return null;
    }
}
