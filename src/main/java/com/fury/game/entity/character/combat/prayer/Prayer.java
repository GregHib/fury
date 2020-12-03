package com.fury.game.entity.character.combat.prayer;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.Projectile;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.GameSettings;
import com.fury.game.container.impl.equip.Slot;
import com.fury.game.content.global.minigames.impl.RecipeForDisaster;
import com.fury.game.content.misc.items.ItemsKeptOnDeath;
import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.free.prayer.Prayerbook;
import com.fury.game.entity.character.combat.Hit;
import com.fury.game.entity.character.player.content.BonusManager;
import com.fury.game.entity.character.player.info.DonorStatus;
import com.fury.game.node.entity.actor.figure.ProjectileManager;
import com.fury.game.world.GameWorld;
import com.fury.game.world.update.flag.Flag;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.util.Colours;
import com.fury.util.Misc;
import com.fury.util.Utils;

public class Prayer {

    public Prayer(Player player) {
        this.player = player;
        quickPrayers = new boolean[][]{new boolean[30], new boolean[20]};
        onPrayers = new boolean[][]{new boolean[30], new boolean[20]};
        cachedStates = new boolean[30];
        leechBonuses = new int[11];
        nextDrain = new long[30];
    }


    public static final int THICK_SKIN = 0, BURST_OF_STRENGTH = 1, CLARITY_OF_THOUGHT = 2, SHARP_EYE = 3, MYSTIC_WILL = 4,
            ROCK_SKIN = 5, SUPERHUMAN_STRENGTH = 6, IMPROVED_REFLEXES = 7, RAPID_RESTORE = 8, RAPID_HEAL = 9,
            PROTECT_ITEM_PRAYER = 10, HAWK_EYE = 11, MYSTIC_LORE = 12, STEEL_SKIN = 13, ULTIMATE_STRENGTH = 14,
            INCREDIBLE_REFLEXES = 15, PROTECT_FROM_SUMMONING = 16, PROTECT_FROM_MAGIC = 17, PROTECT_FROM_MISSILES = 18,
            PROTECT_FROM_MELEE = 19, EAGLE_EYE = 20, MYSTIC_MIGHT = 21, RETRIBUTION = 22, REDEMPTION = 23, SMITE = 24,
            CHIVALRY = 25, RAPID_RENEWAL = 26, PIETY = 27, RIGOUR = 28, AUGURY = 29,
            PROTECT_ITEM_CURSE = 0, SAP_WARRIOR = 1, SAP_RANGER = 2, SAP_MAGE = 3, SAP_SPIRIT = 4,
            BERSERKER = 5, DEFLECT_SUMMONING = 6, DEFLECT_MAGIC = 7, DEFLECT_MISSILES = 8,
            DEFLECT_MELEE = 9, LEECH_ATTACK = 10, LEECH_RANGED = 11, LEECH_MAGIC = 12,
            LEECH_DEFENCE = 13, LEECH_STRENGTH = 14, LEECH_ENERGY = 15, LEECH_SPECIAL_ATTACK = 16,
            WRATH = 17, SOUL_SPLIT = 18, TURMOIL = 19;

    private final static int[][][] closePrayers = {
            {//Prayers
                    {THICK_SKIN, ROCK_SKIN, STEEL_SKIN},
                    {BURST_OF_STRENGTH, SUPERHUMAN_STRENGTH, ULTIMATE_STRENGTH},
                    {CLARITY_OF_THOUGHT, IMPROVED_REFLEXES, INCREDIBLE_REFLEXES},
                    {SHARP_EYE, HAWK_EYE, EAGLE_EYE, RIGOUR},
                    {MYSTIC_WILL, MYSTIC_LORE, MYSTIC_MIGHT, AUGURY},
                    {RAPID_RESTORE, RAPID_HEAL, RAPID_RENEWAL},
                    {PROTECT_ITEM_PRAYER},
                    {PROTECT_FROM_MAGIC, PROTECT_FROM_MISSILES, PROTECT_FROM_MELEE},
                    {PROTECT_FROM_SUMMONING},
                    {RETRIBUTION, REDEMPTION, SMITE},
                    {CHIVALRY, PIETY}
            },
            {//Curses
                    {PROTECT_ITEM_CURSE},
                    {SAP_WARRIOR, SAP_RANGER, SAP_MAGE, SAP_SPIRIT},
                    {BERSERKER},
                    {DEFLECT_MAGIC, DEFLECT_MISSILES, DEFLECT_MELEE, WRATH, SOUL_SPLIT},
                    {DEFLECT_SUMMONING},
                    {LEECH_ATTACK, LEECH_RANGED, LEECH_MAGIC, LEECH_DEFENCE, LEECH_STRENGTH, LEECH_ENERGY, LEECH_SPECIAL_ATTACK}, // leech prayers 5
                    {TURMOIL},
                    {WRATH, SOUL_SPLIT}
            }
    };

    private Player player;
    private boolean[][] onPrayers;
    private boolean usingQuickPrayer;
    private int onPrayersCount;
    private long[] nextDrain;
    private boolean[] cachedStates;

    private boolean[][] quickPrayers;
    private int[] leechBonuses;
    private boolean boostedLeech;

    public static void sendSoulSplit(Figure attacker, Figure target, Hit hit) {
        if (hit.getDamage() > 0)
            ProjectileManager.send(new Projectile(attacker, target, 2263, 11, 11, 20, 5, 0, 0));
        attacker.getHealth().heal((int) (target.isPlayer() ? hit.getDamage() / 2.5 : hit.getDamage() / 5));
        if (target.isPlayer())
            ((Player) target).getSkills().drain(Skill.PRAYER, (int) (hit.getDamage() / 2.5));
        GameWorld.schedule(1, () -> {
            target.graphic(2264);
            if (hit.getDamage() > 0)
                ProjectileManager.send(new Projectile(target, attacker, 2263, 11, 11, 20, 5, 0, 0));
        });
    }

    public double getMageMultiplier() {
        if (onPrayersCount == 0)
            return 1.0;
        double value = 1.0;

        if (usingPrayer(0, MYSTIC_WILL))
            value += 0.05;
        else if (usingPrayer(0, MYSTIC_LORE))
            value += 0.10;
        else if (usingPrayer(0, MYSTIC_MIGHT))
            value += 0.15;
        else if (usingPrayer(0, AUGURY))
            value += 0.22;
        else if (usingPrayer(1, SAP_MAGE)) {
            double d = (leechBonuses[2]);
            value += d / 100;
        } else if (usingPrayer(1, LEECH_MAGIC)) {
            double d = (5 + leechBonuses[5]);
            value += d / 100;
        }
        return value;
    }

    public double getRangeMultiplier() {
        if (onPrayersCount == 0)
            return 1.0;
        double value = 1.0;

        if (usingPrayer(0, SHARP_EYE))
            value += 0.05;
        else if (usingPrayer(0, HAWK_EYE))
            value += 0.10;
        else if (usingPrayer(0, EAGLE_EYE))
            value += 0.15;
        else if (usingPrayer(0, RIGOUR))
            value += 0.22;
        else if (usingPrayer(1, SAP_RANGER)) {
            double d = (leechBonuses[1]);
            value += d / 100;
        } else if (usingPrayer(1, LEECH_RANGED)) {
            double d = (5 + leechBonuses[4]);
            value += d / 100;
        }
        return value;
    }

    public double getAttackMultiplier() {
        if (onPrayersCount == 0)
            return 1.0;
        double value = 1.0;

        if (usingPrayer(0, CLARITY_OF_THOUGHT))
            value += 0.05;
        else if (usingPrayer(0, IMPROVED_REFLEXES))
            value += 0.10;
        else if (usingPrayer(0, INCREDIBLE_REFLEXES))
            value += 0.15;
        else if (usingPrayer(0, CHIVALRY))
            value += 0.15;
        else if (usingPrayer(0, PIETY))
            value += 0.20;
        else if (usingPrayer(1, SAP_WARRIOR)) {
            double d = (leechBonuses[0]);
            value += d / 100;
        } else if (usingPrayer(1, LEECH_ATTACK)) {
            double d = (5 + leechBonuses[3]);
            value += d / 100;
        } else if (usingPrayer(1, TURMOIL)) {
            double d = (15 + leechBonuses[8]);
            value += d / 100;
        }
        return value;
    }

    public double getStrengthMultiplier() {
        if (onPrayersCount == 0)
            return 1.0;
        double value = 1.0;

        if (usingPrayer(0, BURST_OF_STRENGTH))
            value += 0.05;
        else if (usingPrayer(0, SUPERHUMAN_STRENGTH))
            value += 0.10;
        else if (usingPrayer(0, ULTIMATE_STRENGTH))
            value += 0.15;
        else if (usingPrayer(0, CHIVALRY))
            value += 0.18;
        else if (usingPrayer(0, PIETY))
            value += 0.23;
        else if (usingPrayer(1, SAP_WARRIOR)) {
            double d = (leechBonuses[0]);
            value += d / 100;
        } else if (usingPrayer(1, LEECH_STRENGTH)) {
            double d = (5 + leechBonuses[7]);
            value += d / 100;
        } else if (usingPrayer(1, TURMOIL)) {
            double d = (23 + leechBonuses[10]);
            value += d / 100;
        }
        return value;
    }

    public double getDefenceMultiplier() {
        if (onPrayersCount == 0)
            return 1.0;
        double value = 1.0;

        if (usingPrayer(0, THICK_SKIN))
            value += 0.05;
        else if (usingPrayer(0, ROCK_SKIN))
            value += 0.10;
        else if (usingPrayer(0, STEEL_SKIN))
            value += 0.15;
        else if (usingPrayer(0, CHIVALRY))
            value += 0.20;
        else if (usingPrayer(0, PIETY))
            value += 0.25;
        else if (usingPrayer(0, RIGOUR))
            value += 0.25;
        else if (usingPrayer(0, AUGURY))
            value += 0.25;
        else if (usingPrayer(1, SAP_WARRIOR)) {
            double d = (leechBonuses[0]);
            value += d / 100;
        } else if (usingPrayer(1, LEECH_DEFENCE)) {
            double d = (6 + leechBonuses[6]);
            value += d / 100;
        } else if (usingPrayer(1, TURMOIL)) {
            double d = (15 + leechBonuses[9]);
            value += d / 100;
        }
        return value;
    }

    public boolean reachedMax(int bonus) {
        if (bonus != 8 && bonus != 9 && bonus != 10)
            return leechBonuses[bonus] >= 20;
        else
            return false;
    }

    public void increaseLeechBonus(int bonus) {
        leechBonuses[bonus]++;
        if (bonus == 0) {//sap warrior
            refreshAttack();
            refreshStrength();
            refreshDefence();
        } else if (bonus == 1) {//sap range
            refreshDefence();
            refreshRange();
        } else if (bonus == 2) {//sap mage
            refreshDefence();
            refreshMagic();
        } else if (bonus == 3)//leech attack
            refreshAttack();
        else if (bonus == 4)//leech ranged
            refreshRange();
        else if (bonus == 5)//leech mage
            refreshMagic();
        else if (bonus == 6)//leech def
            refreshDefence();
        else if (bonus == 7)//leech strength
            refreshStrength();
    }

    public void increaseTurmoilBonus(Player p2) {
        leechBonuses[8] = (int) ((100 * Math.floor(0.15 * p2.getSkills().getMaxLevel(Skill.ATTACK))) / p2.getSkills().getMaxLevel(Skill.ATTACK));
        leechBonuses[9] = (int) ((100 * Math.floor(0.15 * p2.getSkills().getMaxLevel(Skill.DEFENCE))) / p2.getSkills().getMaxLevel(Skill.DEFENCE));
        leechBonuses[10] = (int) ((100 * Math.floor(0.1 * p2.getSkills().getMaxLevel(Skill.STRENGTH))) / p2.getSkills().getMaxLevel(Skill.STRENGTH));
        refreshAttack();
        refreshDefence();
        refreshStrength();
    }

    public void adjustStat(int stat, int percentage) {
        switch (stat) {
            case 0:
                refreshAttack();
                return;
            case 1:
                refreshStrength();
                break;
            case 2:
                refreshDefence();
                break;
            case 3:
                refreshRange();
                break;
            case 4:
                refreshMagic();
                break;
        }
    }

    public void refreshStats() {
        refreshAttack();
        refreshStrength();
        refreshDefence();
        refreshRange();
        refreshMagic();
    }

    private void refreshAttack() {
        sendStat(0, (int) Math.round((getAttackMultiplier() - 1.0) * 100));
    }

    private void refreshStrength() {
        sendStat(1, (int) Math.round((getStrengthMultiplier() - 1.0) * 100));
    }

    private void refreshDefence() {
        sendStat(2, (int) Math.round((getDefenceMultiplier() - 1.0) * 100));
    }

    private void refreshRange() {
        sendStat(3, (int) Math.round((getRangeMultiplier() - 1.0) * 100));
    }

    private void refreshMagic() {
        sendStat(4, (int) Math.round((getMageMultiplier() - 1.0) * 100));
    }

    private void sendStat(int stat, int bonus) {
        player.getPacketSender().sendString(690 + stat, (bonus > 0 ? "+" : "") + bonus + "%", bonus > 0 ? Colours.GREEN : bonus < 0 ? Colours.RED : Colours.ORANGE);
    }

    public void closePrayers(int prayerId) {
        if (getPrayerBook() == 1) {
            if (prayerId == 1) {
                if (leechBonuses[0] > 0)
                    player.message("Your Attack is now unaffected by sap and leech curses.", true);
                refreshAttack();
                refreshStrength();
                refreshDefence();
                leechBonuses[0] = 0;
            } else if (prayerId == 2) {
                if (leechBonuses[1] > 0)
                    player.message("Your Range is now unaffected by sap and leech curses.", true);
                refreshDefence();
                refreshRange();
                leechBonuses[1] = 0;
            } else if (prayerId == 3) {
                if (leechBonuses[2] > 0)
                    player.message("Your Magic is now unaffected by sap and leech curses.", true);
                refreshDefence();
                refreshMagic();
                leechBonuses[2] = 0;
            } else if (prayerId == 10) {
                if (leechBonuses[3] > 0)
                    player.message("Your Attack is now unaffected by sap and leech curses.", true);
                refreshAttack();
                leechBonuses[3] = 0;
            } else if (prayerId == 11) {
                if (leechBonuses[4] > 0)
                    player.message("Your Ranged is now unaffected by sap and leech curses.", true);
                refreshRange();
                leechBonuses[4] = 0;
            } else if (prayerId == 12) {
                if (leechBonuses[5] > 0)
                    player.message("Your Magic is now unaffected by sap and leech curses.", true);
                refreshMagic();
                leechBonuses[5] = 0;
            } else if (prayerId == 13) {
                if (leechBonuses[6] > 0)
                    player.message("Your Defence is now unaffected by sap and leech curses.", true);
                refreshDefence();
                leechBonuses[6] = 0;
            } else if (prayerId == 14) {
                if (leechBonuses[7] > 0)
                    player.message("Your Strength is now unaffected by sap and leech curses.", true);
                refreshStrength();
                leechBonuses[7] = 0;
            } else if (prayerId == 19) {
                leechBonuses[8] = 0;
                leechBonuses[9] = 0;
                leechBonuses[10] = 0;
                refreshAttack();
                refreshStrength();
                refreshDefence();
            }
        }
    }

    public int getPrayerHeadIcon() {
        if (onPrayersCount == 0)
            return -1;
        int value = -1;
        if (usingPrayer(0, PROTECT_FROM_SUMMONING))
            value += 8;
        if (usingPrayer(0, PROTECT_FROM_MAGIC))
            value += 3;
        else if (usingPrayer(0, PROTECT_FROM_MISSILES))
            value += 2;
        else if (usingPrayer(0, PROTECT_FROM_MELEE))
            value += 1;
        else if (usingPrayer(0, RETRIBUTION))
            value += 4;
        else if (usingPrayer(0, REDEMPTION))
            value += 6;
        else if (usingPrayer(0, SMITE))
            value += 5;
        else if (usingPrayer(1, DEFLECT_SUMMONING)) {
            value += 16;
            if (usingPrayer(1, DEFLECT_MISSILES))
                value += 2;
            else if (usingPrayer(1, DEFLECT_MAGIC))
                value += 3;
            else if (usingPrayer(1, DEFLECT_MELEE))
                value += 1;
        } else if (usingPrayer(1, DEFLECT_MAGIC))
            value += 14;
        else if (usingPrayer(1, DEFLECT_MISSILES))
            value += 15;
        else if (usingPrayer(1, DEFLECT_MELEE))
            value += 13;
        else if (usingPrayer(1, WRATH))
            value += 20;
        else if (usingPrayer(1, SOUL_SPLIT))
            value += 21;
        return value;
    }

    public void toggleQuickPrayerWidget() {
        if (player.getTabInterface(GameSettings.PRAYER_TAB) == player.getPrayerbook().getInterfaceId()) {
            for (int i = 0; i < quickPrayers[getPrayerBook()].length; i++)
                player.getPacketSender().sendConfig(630 + i, quickPrayers[getPrayerBook()][i] ? 1 : 0);
            player.getPacketSender().sendTabInterface(GameSettings.PRAYER_TAB, getPrayerBook() == 0 ? 17200 : 17234);

            player.getPacketSender().sendTab(GameSettings.PRAYER_TAB);
        } else
            player.getPacketSender().sendTabInterface(GameSettings.PRAYER_TAB, player.getPrayerbook().getInterfaceId());
    }

    public void toggleQuickPrayer(int id) {
        if (getPrayerBook() == 0) {
            PrayerData data = PrayerData.prayerData.get(id);
            if (data == null)
                return;

            if (!hasRequirement(data)) {
                player.getPacketSender().sendConfig(630 + id, 0);
                return;
            }
        } else {
            CurseData data = CurseData.ids.get(id);
            if (data == null)
                return;

            if (!hasCurseRequirement(data)) {
                player.getPacketSender().sendConfig(630 + id, 0);
                return;
            }
        }

        int[] overlaps = getOverlaps(id);
        boolean[] cached = quickPrayers[getPrayerBook()].clone();//TODO cache can be replaced with inteface/config caching?
        closeQuickPrayers(id, overlaps);
        quickPrayers[getPrayerBook()][id] = !quickPrayers[getPrayerBook()][id];

        for (int i = 0; i < cached.length; i++)
            if (quickPrayers[getPrayerBook()][i] != cached[i])
                player.getPacketSender().sendConfig(630 + i, quickPrayers[getPrayerBook()][i] ? 1 : 0);
    }

    private void closeQuickPrayers(int toActive, int[]... prayers) {
        for (int[] prayer : prayers)
            for (int prayerId : prayer)
                if (prayerId != toActive)
                    quickPrayers[getPrayerBook()][prayerId] = false;
    }

    public void switchSettingQuickPrayer() {
        usingQuickPrayer = !usingQuickPrayer;
        player.getPacketSender().sendOrb(1, usingQuickPrayer);

        unlockPrayerBookButtons();
    }

    public void switchQuickPrayers() {
        if (!checkPrayer())
            return;
        if (hasPrayersOn())
            closeAllPrayers();
        else {
            boolean hasOn = false;
            int index = 0;
            for (boolean prayer : quickPrayers[getPrayerBook()]) {
                if (prayer) {
                    if (usePrayer(index))
                        hasOn = true;
                }
                index++;
            }
            if (hasOn) {
                player.getPacketSender().sendOrb(1, true);
                refreshStats();
            }
        }
    }

    private void closePrayers(int toActive, int[]... prayers) {
        for (int[] prayer : prayers)
            for (int prayerId : prayer)
                if (usingQuickPrayer)
                    quickPrayers[getPrayerBook()][prayerId] = false;
                else if (prayerId != toActive) {
                    if (onPrayers[getPrayerBook()][prayerId])
                        onPrayersCount--;
                    onPrayers[getPrayerBook()][prayerId] = false;
                    closePrayers(prayerId);
                }
    }


    public void closeProtectionPrayers() {
        if (getPrayerBook() == 1)
            closePrayers(-1, Misc.concat(closePrayers[getPrayerBook()][3], closePrayers[getPrayerBook()][4]));
        else
            closePrayers(-1, Misc.concat(closePrayers[getPrayerBook()][7], closePrayers[getPrayerBook()][8]));
        refreshToggles(false);
    }

    public void switchPrayer(int prayerId) {
        if (!usingQuickPrayer)
            if (!checkPrayer(prayerId))
                return;
        usePrayer(prayerId);
        refreshStats();
    }

    private boolean hasRequired(int level) {
        return player.getSkills().hasMaxRequirement(Skill.PRAYER, level, "use this prayer");
    }

    private boolean needsRefresh(int prayerId) {
        if (getPrayerBook() == 0) {
            switch (prayerId) {
                case PROTECT_FROM_MAGIC:
                case PROTECT_FROM_MISSILES:
                case PROTECT_FROM_MELEE:
                case PROTECT_FROM_SUMMONING:
                case RETRIBUTION:
                case REDEMPTION:
                case SMITE:
                    return true;
            }
        } else {
            switch (prayerId) {
                case DEFLECT_MAGIC:
                case DEFLECT_MISSILES:
                case DEFLECT_MELEE:
                case WRATH:
                case SOUL_SPLIT:
                case DEFLECT_SUMMONING:
                    return true;
            }
        }
        return false;
    }

    private Animation getAnimation(int prayerId) {
        if (getPrayerBook() == 1 && !usingQuickPrayer) {
            switch (prayerId) {
                case PROTECT_ITEM_CURSE:
                    return new Animation(12567);
                case BERSERKER:
                    return new Animation(12589);
                case TURMOIL:
                    return new Animation(12565);
            }
        }
        return null;
    }

    private Graphic getGraphic(int prayerId) {
        if (getPrayerBook() == 1 && !usingQuickPrayer) {
            switch (prayerId) {
                case PROTECT_ITEM_CURSE:
                    return new Graphic(2213);
                case BERSERKER:
                    return new Graphic(2266);
                case TURMOIL:
                    return new Graphic(2226);
            }
        }
        return null;
    }

    private int[] getOverlaps(int prayerId) {
        if (getPrayerBook() == 0) {
            switch (prayerId) {
                case THICK_SKIN:
                case ROCK_SKIN:
                case STEEL_SKIN:
                    return Misc.concat(closePrayers[getPrayerBook()][0], closePrayers[getPrayerBook()][10]);
                case BURST_OF_STRENGTH:
                case SUPERHUMAN_STRENGTH:
                case ULTIMATE_STRENGTH:
                    return Misc.concat(closePrayers[getPrayerBook()][1], closePrayers[getPrayerBook()][3], closePrayers[getPrayerBook()][4], closePrayers[getPrayerBook()][10]);
                case CLARITY_OF_THOUGHT:
                case IMPROVED_REFLEXES:
                case INCREDIBLE_REFLEXES:
                    return Misc.concat(closePrayers[getPrayerBook()][2], closePrayers[getPrayerBook()][3], closePrayers[getPrayerBook()][4], closePrayers[getPrayerBook()][10]);
                case SHARP_EYE:
                case HAWK_EYE:
                case EAGLE_EYE:
                    return Misc.concat(closePrayers[getPrayerBook()][1], closePrayers[getPrayerBook()][2], closePrayers[getPrayerBook()][3], closePrayers[getPrayerBook()][10]);
                case MYSTIC_WILL:
                case MYSTIC_LORE:
                case MYSTIC_MIGHT:
                    return Misc.concat(closePrayers[getPrayerBook()][1], closePrayers[getPrayerBook()][2], closePrayers[getPrayerBook()][4], closePrayers[getPrayerBook()][10]);
                case RAPID_RESTORE:
                case RAPID_HEAL:
                case RAPID_RENEWAL:
                    return Misc.concat(closePrayers[getPrayerBook()][5]);
                case PROTECT_ITEM_PRAYER:
                    return Misc.concat(closePrayers[getPrayerBook()][6]);
                case PROTECT_FROM_MAGIC:
                case PROTECT_FROM_MISSILES:
                case PROTECT_FROM_MELEE:
                    return Misc.concat(closePrayers[getPrayerBook()][7], closePrayers[getPrayerBook()][9]);
                case PROTECT_FROM_SUMMONING:
                    return Misc.concat(closePrayers[getPrayerBook()][8], closePrayers[getPrayerBook()][9]);
                case RETRIBUTION:
                case REDEMPTION:
                case SMITE:
                    return Misc.concat(closePrayers[getPrayerBook()][7], closePrayers[getPrayerBook()][8], closePrayers[getPrayerBook()][9]);
                case CHIVALRY:
                case PIETY:
                case RIGOUR:
                case AUGURY:
                    return Misc.concat(closePrayers[getPrayerBook()][0], closePrayers[getPrayerBook()][1], closePrayers[getPrayerBook()][2], closePrayers[getPrayerBook()][3], closePrayers[getPrayerBook()][4], closePrayers[getPrayerBook()][10]);
            }
        } else {
            switch (prayerId) {
                case PROTECT_ITEM_CURSE:
                    return Misc.concat(closePrayers[getPrayerBook()][0]);
                case SAP_WARRIOR:
                case SAP_RANGER:
                case SAP_MAGE:
                case SAP_SPIRIT:
                    return Misc.concat(closePrayers[getPrayerBook()][5], closePrayers[getPrayerBook()][6]);
                case BERSERKER:
                    return Misc.concat(closePrayers[getPrayerBook()][2]);
                case DEFLECT_MAGIC:
                case DEFLECT_MISSILES:
                case DEFLECT_MELEE:
                    return Misc.concat(closePrayers[getPrayerBook()][3]);
                case WRATH:
                case SOUL_SPLIT:
                    return Misc.concat(closePrayers[getPrayerBook()][3], closePrayers[getPrayerBook()][4]);
                case DEFLECT_SUMMONING:
                    return Misc.concat(closePrayers[getPrayerBook()][4], closePrayers[getPrayerBook()][7]);
                case LEECH_ATTACK:
                case LEECH_RANGED:
                case LEECH_MAGIC:
                case LEECH_DEFENCE:
                case LEECH_STRENGTH:
                case LEECH_ENERGY:
                case LEECH_SPECIAL_ATTACK:
                    return Misc.concat(closePrayers[getPrayerBook()][1], closePrayers[getPrayerBook()][6]);
                case TURMOIL:
                    return Misc.concat(closePrayers[getPrayerBook()][1], closePrayers[getPrayerBook()][5], closePrayers[getPrayerBook()][6]);
            }
        }
        return new int[0];
    }

    private boolean hasRequirement(PrayerData data) {
        if (!hasRequired(data.getRequirement()))
            return false;

        if (data == PrayerData.CHIVALRY || data == PrayerData.PIETY) {
            if(!player.getSkills().hasRequirement(Skill.DEFENCE, 60, "use this prayer"))
                return false;
        }

        if (player.getPrayerDelay() >= Utils.currentTimeMillis()) {
            if (data.getPrayerName().startsWith("Protect from")) {
                player.message("You are currently injured and cannot use protection prayers!");
                return false;
            }
        }

        return true;
    }

    private boolean hasCurseRequirement(CurseData data) {
        if (!hasRequired(data.getRequirement())) {
            player.getPacketSender().sendConfig(data.getConfigId(), 0);
            return false;
        }

        if (!player.getSkills().hasRequirement(Skill.DEFENCE, 30, "use this prayer"))
            return false;

        if (player.getPrayerDelay() >= Utils.currentTimeMillis()) {
            if (data.getName().startsWith("Deflect ")) {
                player.message("You are currently injured and cannot use protection prayers!");
                return false;
            }
        }

        return true;
    }

    private boolean usePrayer(int prayerId) {
        if (prayerId < 0)
            return false;


        if (getPrayerBook() == 0) {
            PrayerData data = PrayerData.prayerData.get(prayerId);
            if (data == null)
                return false;

            if (!hasRequirement(data)) {
                player.getPacketSender().sendConfig(data.getConfigId(), 0);
                return false;
            }

            if(player.getControllerManager().getController() instanceof RecipeForDisaster) {
                player.message("You cannot use prayers here.");
                player.getPacketSender().sendConfig(data.getConfigId(), 0);
                return false;
            }
        } else {
            CurseData data = CurseData.ids.get(prayerId);
            if (data == null)
                return false;

            if (!hasCurseRequirement(data)) {
                player.getPacketSender().sendConfig(data.getConfigId(), 0);
                return false;
            }

            if(player.getControllerManager().getController() instanceof RecipeForDisaster) {
                player.message("You cannot use curses here.");
                player.getPacketSender().sendConfig(data.getConfigId(), 0);
                return false;
            }
        }


        cachedStates = onPrayers[getPrayerBook()].clone();
        if (!usingQuickPrayer) {

            if (onPrayers[getPrayerBook()][prayerId]) {
                onPrayers[getPrayerBook()][prayerId] = false;
                closePrayers(prayerId);
                onPrayersCount--;
                player.getUpdateFlags().add(Flag.APPEARANCE);
                if (getPrayerBook() == 0) {
                    PrayerData data = PrayerData.prayerData.get(prayerId);
                    if(data != null && data == PrayerData.PROTECT_ITEM && player.getInterfaceId() == 17100)
                        ItemsKeptOnDeath.open(player);
                } else {
                    CurseData data = CurseData.ids.get(prayerId);
                    if (data != null && data == CurseData.PROTECT_ITEM && player.getInterfaceId() == 17100)
                        ItemsKeptOnDeath.open(player);
                }
                return true;
            }
        } else {
            if (quickPrayers[getPrayerBook()][prayerId]) {
                quickPrayers[getPrayerBook()][prayerId] = false;
                return true;
            }
        }
        boolean refreshHeadIcon = needsRefresh(prayerId);
        int[] overlaps = getOverlaps(prayerId);
        closePrayers(prayerId, overlaps);

        Animation animation = getAnimation(prayerId);
        Graphic graphic = getGraphic(prayerId);

        if (animation != null)
            player.perform(animation);
        if (graphic != null)
            player.perform(graphic);

        if (!usingQuickPrayer) {
            onPrayers[getPrayerBook()][prayerId] = true;
            resetDrainPrayer(prayerId);
            onPrayersCount++;
            if (refreshHeadIcon)
                player.getUpdateFlags().add(Flag.APPEARANCE);
        } else {
            quickPrayers[getPrayerBook()][prayerId] = true;
        }


        if (getPrayerBook() == 0) {
            PrayerData data = PrayerData.prayerData.get(prayerId);
            if(data != null && data == PrayerData.PROTECT_ITEM && player.getInterfaceId() == 17100)
                ItemsKeptOnDeath.open(player);
        } else {
            CurseData data = CurseData.ids.get(prayerId);
            if (data != null && data == CurseData.PROTECT_ITEM && player.getInterfaceId() == 17100)
                ItemsKeptOnDeath.open(player);
        }
        refreshToggles(false);
        return true;
    }

    private void refreshToggles(boolean ignoreCache) {
        for (int i = 0; i < cachedStates.length; i++) {
            if (onPrayers[getPrayerBook()][i] != cachedStates[i] || ignoreCache) {
                if (getPrayerBook() == 0) {
                    PrayerData pd = PrayerData.prayerData.get(i);
                    player.getPacketSender().sendConfig(pd.getConfigId(), onPrayers[getPrayerBook()][i] ? 1 : 0);
                } else {
                    CurseData cd = CurseData.ids.get(i);
                    player.getPacketSender().sendConfig(cd.getConfigId(), onPrayers[getPrayerBook()][i] ? 1 : 0);
                }
            }
        }
    }

    //TODO use these to replace in PrayerData & CurseData
    private final static double[][] newDrainRates =
            {
                    {
                            12, // Thick Skin
                            12, // Burst of Strength
                            12, // Clarity of Thought
                            12, // Sharp Eye
                            12, // Mystic Will
                            6, // Rock Skin
                            6, // SuperHuman Strength
                            6, // Improved Reflexes
                            36, // Rapid restore
                            18, // Rapid Heal
                            18, // Protect Item
                            6, // Hawk eye
                            6, // Mystic Lore
                            3, // Steel Skin
                            3, // Ultimate Strength
                            3, // Incredible Reflexes
                            3, // Protect from Summoning
                            3, // Protect from Magic
                            3, // Protect from Missiles
                            3, // Protect from Melee
                            3, // Eagle Eye
                            3, // Mystic Might
                            12, // Retribution
                            6, // Redemption
                            2, // Smite
                            1.5, // Chivalry
                            2.57, // Rapid renewal
                            1.5, // Piety
                            1.8, // Rigour
                            1.8, // Augury
                    },
                    {
                            18, //Protect Item
                            2.57, //Sap Warrior
                            2.57, //Sap Ranger
                            2.57, //Sap Mage
                            2.57, //Sap Spirit
                            18, //Berserker
                            3, //Deflect Summoning
                            3, //Deflect Magic
                            3, //Deflect Missiles
                            3, //Deflect Melee
                            3.6, //Leech Attack
                            3.6, //Leech Ranged
                            3.6, //Leech Magic
                            3.6, //Leech Defence
                            3.6, //Leech Strength
                            3.6, //Leech Energy
                            3.6, //Leech Special
                            12, //Wrath
                            2, //SS
                            2 //Turmoil
                    }};

    public int calcDrainMilliseconds(int index, double bonus) {
        double newRate = newDrainRates[getPrayerBook()][index] + (0.067 * bonus);
        if(!player.isCanPvp())
            newRate *= DonorStatus.get(player).getPrayerDrain();
        return (int) Math.ceil(Math.ceil(newRate * 1000) / 10);
    }

    public void processPrayer() {
        if (!hasPrayersOn())
            return;
        int prayerBook = getPrayerBook();
        long currentTime = Utils.currentTimeMillis();
        double bonus = player.getBonusManager().getOtherBonus()[BonusManager.BONUS_PRAYER];
        int drain = 0;
        int hatId = player.getEquipment().get(Slot.HEAD).getId();
        if (hatId >= 18744 && hatId <= 18746) //halo's give hidden effect 15 pray bonus
            bonus += 15;
        for (int index = 0; index < onPrayers[prayerBook].length; index++) {
            if (onPrayers[prayerBook][index]) {
                long drainTimer = nextDrain[index];
                if (drainTimer != 0 && drainTimer <= currentTime) {
                    int rate = calcDrainMilliseconds(index, bonus);
                    int passedTime = (int) (currentTime - drainTimer);
                    drain++;
                    int count = 0;
                    while (passedTime >= rate && count++ < 10) {
                        drain++;
                        passedTime -= rate;
                    }
                    nextDrain[index] = (currentTime + rate) - passedTime;
                }
            }
        }

        if (drain > 0) {
            player.getSkills().drain(Skill.PRAYER, drain);
            if (!checkPrayer())
                closeAllPrayers();
        }
    }

    public boolean handleButton(int id) {

        if (PrayerData.actionButton.containsKey(id)) {
            player.getPrayer().switchPrayer((id - 25000) / 2);
            return true;
        }

        if (CurseData.buttons.containsKey(id)) {
            player.getPrayer().switchPrayer((id - 32503) / 2);
            return true;
        }

        if (id >= 18950 && id <= 18980) {
            player.getPrayer().toggleQuickPrayer(id - 18950);
            return true;
        }

        switch (id) {
            case 1045:
                player.getPrayer().switchQuickPrayers();
                return true;
            case 1046:
            case 17231:
                player.getPrayer().toggleQuickPrayerWidget();
                return true;
        }
        return false;
    }

    public void resetDrainPrayer(int index) {
        nextDrain[index] = (long) (Misc.currentTimeMillis() + calcDrainMilliseconds(index, player.getBonusManager().getOtherBonus()[BonusManager.BONUS_PRAYER]));
    }


    public int getOnPrayersCount() {
        return onPrayersCount;
    }

    public void closeAllPrayers() {
        onPrayers = new boolean[][]{new boolean[30], new boolean[20]};
        cachedStates = onPrayers[getPrayerBook()].clone();
        leechBonuses = new int[11];
        onPrayersCount = 0;
        player.getPacketSender().sendOrb(1, false);
        player.getUpdateFlags().add(Flag.APPEARANCE);
        refreshStats();
        refreshToggles(true);
    }

    public boolean hasPrayersOn() {
        return onPrayersCount > 0;
    }

    private boolean checkPrayer() {
        return checkPrayer(-1);
    }

    private boolean checkPrayer(int id) {
        if (player.getSkills().getLevel(Skill.PRAYER) <= 0) {
            if (id != -1) {
                if (getPrayerBook() == 0) {
                    PrayerData data = PrayerData.prayerData.get(id);
                    if (data != null)
                        player.getPacketSender().sendConfig(data.getConfigId(), 0);
                } else {
                    CurseData data = CurseData.ids.get(id);
                    if (data != null)
                        player.getPacketSender().sendConfig(data.getConfigId(), 0);
                }
            }
//            player.getPacketSender().sendSound(2672, 0, 1);
            //You have run out of Prayer points, you can recharge at an altar.
            player.message("You do not have enough Prayer points. You can recharge your points at an altar.");
            return false;
        }
        return true;
    }

    private int getPrayerBook() {
        return player.getPrayerbook() == Prayerbook.NORMAL ? 0 : 1;
    }

    public void refresh() {
        player.getPacketSender().sendOrb(1, usingQuickPrayer);
        unlockPrayerBookButtons();
        refreshStats();
    }

    public void init() {
        player.getPacketSender().sendOrb(1, usingQuickPrayer);
        refreshStats();
    }

    public void unlockPrayerBookButtons() {
//        player.getPacketSender().sendUnlockIComponentOptionSlots(271, usingQuickPrayer ? 42 : 8, 0, 29, 0);
    }

    public void setPlayer(Player player) {
        this.player = player;
        onPrayers = new boolean[][]{new boolean[30], new boolean[20]};
        leechBonuses = new int[11];
    }

    public boolean usingPrayer(int book, int prayerId) {
        return onPrayers[book][prayerId];
    }

    public boolean isUsingQuickPrayer() {
        return usingQuickPrayer;
    }

    public boolean isBoostedLeech() {
        return boostedLeech;
    }

    public void setBoostedLeech(boolean boostedLeech) {
        this.boostedLeech = boostedLeech;
    }

    public void reset() {
        closeAllPrayers();
    }

    /**
     * Utility methods
     */

    public boolean isUsingProtectionPrayer() {
        return isMageProtecting() || isRangeProtecting() || isMeleeProtecting();
    }

    public boolean isProtectingItem() {
        return getPrayerBook() == 1 ? usingPrayer(1, PROTECT_ITEM_CURSE) : usingPrayer(0, PROTECT_ITEM_PRAYER);
    }

    public boolean isSummoningProtecting() {
        return getPrayerBook() == 1 ? usingPrayer(1, DEFLECT_SUMMONING) : usingPrayer(0, PROTECT_FROM_SUMMONING);
    }

    public boolean isMageProtecting() {
        return getPrayerBook() == 1 ? usingPrayer(1, DEFLECT_MAGIC) : usingPrayer(0, PROTECT_FROM_MAGIC);
    }

    public boolean isRangeProtecting() {
        return getPrayerBook() == 1 ? usingPrayer(1, DEFLECT_MISSILES) : usingPrayer(0, PROTECT_FROM_MISSILES);
    }

    public boolean isMeleeProtecting() {
        return getPrayerBook() == 1 ? usingPrayer(1, DEFLECT_MELEE) : usingPrayer(0, PROTECT_FROM_MELEE);
    }

    public void setQuickPrayers(boolean[][] quickPrayers) {
        this.quickPrayers = quickPrayers;
    }

    public void setQuickPrayers(boolean[] quickPrayers) {
        this.quickPrayers[getPrayerBook()] = quickPrayers;
    }

    public boolean[][] getQuickPrayers() {
        return quickPrayers;
    }

    /*public boolean canReflect(Figure entity) {
        if (entity instanceof DungeonBoss || entity instanceof WildyWyrm || entity instanceof CombatEventNPC || entity instanceof Nex || entity instanceof QueenBlackDragon) {
            player.message("You are unable to reflect damage back to this creature.", true);
            return false;
        }
        return entity.getMaxHitpoints() > 1;
    }*/
}
