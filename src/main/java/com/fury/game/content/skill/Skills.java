package com.fury.game.content.skill;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.task.Task;
import com.fury.game.GameSettings;
import com.fury.game.content.events.daily.DailyEventManager;
import com.fury.game.content.global.Achievements;
import com.fury.game.content.global.Achievements.AchievementData;
import com.fury.game.entity.character.player.content.Sounds;
import com.fury.game.world.GameWorld;
import com.fury.game.world.update.flag.Flag;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.util.FontUtils;
import com.fury.util.Misc;

/**
 * Handles player's skills in the game, also manages
 * calculations such as buffs, nerfs, combat level and total level.
 *
 * @author Greg
 */

public class Skills {

    private static final int MAX_EXPERIENCE = 200000000;
    private static final int EXPERIENCE_FOR_99 = 13034431;
    private static final int EXP_ARRAY[] = {
            0, 83, 174, 276, 388, 512, 650, 801, 969, 1154, 1358, 1584, 1833, 2107, 2411, 2746, 3115, 3523,
            3973, 4470, 5018, 5624, 6291, 7028, 7842, 8740, 9730, 10824, 12031, 13363, 14833, 16456, 18247,
            20224, 22406, 24815, 27473, 30408, 33648, 37224, 41171, 45529, 50339, 55649, 61512, 67983, 75127,
            83014, 91721, 101333, 111945, 123660, 136594, 150872, 166636, 184040, 203254, 224466, 247886,
            273742, 302288, 333804, 368599, 407015, 449428, 496254, 547953, 605032, 668051, 737627, 814445,
            899257, 992895, 1096278, 1210421, 1336443, 1475581, 1629200, 1798808, 1986068, 2192818, 2421087,
            2673114, 2951373, 3258594, 3597792, 3972294, 4385776, 4842295, 5346332, 5902831, 6517253, 7195629,
            7944614, 8771558, 9684577, 10692629, 11805606, 13034431
    };
    private Player player;
    private SkillData data;
    private long totalGainedExp;

    public Skills(Player player) {
        this.player = player;
        init();
    }

    /**
     * Commands
     */

    public void init() {
        this.data = new SkillData();
        for (Skill skill : Skill.values()) {
            setLevel(skill, 1, false);
            setMaxLevel(skill, 1, false);
            setExperience(skill, 0, false);
        }
        setLevel(Skill.CONSTITUTION, 100, false);
        setMaxLevel(Skill.CONSTITUTION, 100, false);
        setExperience(Skill.CONSTITUTION, 1184, false);
        setLevel(Skill.PRAYER, 10, false);
        setMaxLevel(Skill.PRAYER, 10, false);
    }

    public void reset(Skill skill) {
        setLevel(skill, skill == Skill.CONSTITUTION ? 100 :  skill == Skill.PRAYER ? 10 : 1, false);
        setMaxLevel(skill, skill == Skill.CONSTITUTION ? 100 :  skill == Skill.PRAYER ? 10 : 1, false);
        setExperience(skill, 0, false);
    }

    public boolean isMaxed() {
        for (Skill skill : Skill.values())
            if (getMaxLevel(skill) < (skill.isNewSkill() ? 990 : 99))
                return false;
        return true;
    }


    public void reset() {
        for (Skill skill : Skill.values())
            restore(skill);
    }

    /**
     * Restores current level to max level
     */

    public void restore(Skill skill) {
        setLevel(skill, skill == Skill.CONSTITUTION ? player.getMaxConstitution() : getMaxLevel(skill));
    }

    /**
     * Restores an amount up to max level
     */

    public void restore(Skill skill, int amount) {
        restore(skill, amount, 0.0);
    }

    public void restore(Skill skill, double multiplier) {
        restore(skill, 0, multiplier);
    }

    public void restore(Skill skill, int amount, double multiplier) {
        setLevel(skill, calcRestore(skill, amount, multiplier));
    }

    /**
     * Reduces current level to max level if over
     * @param skill
     */

    public void reduce(Skill skill) {
        if(getLevel(skill) > getMaxLevel(skill))
            restore(skill);
    }


    /**
     * Sets current level to max level + amount + (max * multiplier)
     * Regardless of current level
     */
    public void buff(Skill skill, int amount) {
        buff(skill, amount, 0.0);
    }

    public void buff(Skill skill, double multiplier) {
        buff(skill, 0, multiplier);
    }

    public void buff(Skill skill, int amount, double multiplier) {
        setLevel(skill, calcBuff(skill, amount, multiplier));
    }

    /**
     * Boosts current level by amount
     */
    public void boost(Skill skill, int amount) {
        boost(skill, amount, 0.0);
    }

    public void boost(Skill skill, double multiplier) {
        boost(skill, 0, multiplier);
    }

    public void boost(Skill skill, int amount, double multiplier) {
        setLevel(skill, calcBoost(skill, amount, multiplier));
    }

    /**
     * Drains current level by amount and multiplier
     * limit - capped
     */

    public int drain(Skill skill, int amount) {
        return drain(skill, amount, 0.0, false);
    }

    public int drain(Skill skill, int amount, boolean limit) {
        return drain(skill, amount, 0.0, limit);
    }

    public int drain(Skill skill, double multiplier) {
        return drain(skill, 0, multiplier, false);
    }

    public int drain(Skill skill, double multiplier, boolean limit) {
        return drain(skill, 0, multiplier, limit);
    }

    public int drain(Skill skill, int amount, double multiplier, boolean limit) {
        int drain = limit ? calcDrain(skill, amount, multiplier) : calcDecrease(skill, getLevel(skill), amount, multiplier);
        drain = Math.max(drain, 0);
        setLevel(skill, drain);
        return drain;
    }

    /**
     * Checks if level is max
     */

    public boolean isFull(Skill skill) {
        return getLevel(skill) >= getMaxLevel(skill);
    }

    /**
     * Level is reduced if over cap
     */
    public boolean cap(Skill skill, int level) {
        if(getLevel(skill) > level) {
            setLevel(skill, level);
            return true;
        }
        return false;
    }

    //Auto restores current to max level then increase on top of that
    public int calcBuff(Skill skill, int amount, double multiplier) {
        return calcIncrease(skill, getMaxLevel(skill), amount, multiplier);
    }

    //Limits increase
    public int calcBoost(Skill skill, int amount, double multiplier) {
        int level = Math.min(getLevel(skill), getMaxLevel(skill));
        return calcIncrease(skill, level, amount, multiplier);
    }

    //Limits the decrease
    public int calcDrain(Skill skill, int amount, double multiplier) {
        int level = Math.max(getLevel(skill), getMaxLevel(skill));
        return calcDecrease(skill, level, amount, multiplier);
    }

    //Applies to current level, capped at max level
    public int calcRestore(Skill skill, int amount, double multiplier) {
        int level = calcIncrease(skill, getLevel(skill), amount, multiplier);
        return Math.min(level, getMaxLevel(skill));
    }

    private int calcIncrease(Skill skill, int level, int amount, double multiplier) {
        return level + amount + (int) (getMaxLevel(skill) * multiplier);
    }

    public int calcDecrease(Skill skill, int level, int amount, double multiplier) {
        return level - amount - (int) (getLevel(skill) * multiplier);
    }

    /**
     * Has level
     */

    public boolean hasLevel(Skill skill, int level) {
        return getLevel(skill) >= (skill.isNewSkill() ?  10 * level : level);
    }

    public boolean hasRequirement(Skill skill, int requirement, String end) {
        if (!player.getSkills().hasLevel(skill, requirement)) {
            player.message("You need a " + skill.getFormatName() + " level of at least " + requirement + " to " + end + ".");
            return false;
        }
        return true;
    }

    public boolean hasMaxLevel(Skill skill, int level) {
        return getMaxLevel(skill) >= (skill.isNewSkill() ?  10 * level : level);
    }

    public boolean hasMaxRequirement(Skill skill, int requirement, String end) {
        if (!player.getSkills().hasMaxLevel(skill, requirement)) {
            player.message("You need a " + skill.getFormatName() + " level of at least " + requirement + " to " + end + ".");
            return false;
        }
        return true;
    }

    public boolean hasExperience(Skill skill, long exp) {
        return getExperience(skill) >= exp;
    }

    public boolean hasExpRequirement(Skill skill, long requirement, String end) {
        if (!player.getSkills().hasExperience(skill, requirement)) {
            player.message("You need at least " + Misc.formatAmount(requirement) + " " + skill.getFormatName() + " experience to " + end + ".");
            return false;
        }
        return true;
    }

    public boolean addExperience(Skill skill, double experience) {
        return addExperience(skill, experience, true, 1);
    }

    public boolean addExperience(Skill skill, double experience, double bonus) {
        return addExperience(skill, experience, true, bonus);
    }

    public boolean addExperience(Skill skill, double experience, boolean modifier) {
        return addExperience(skill, experience, modifier, 1);
    }

    public boolean addExperience(Skill skill, double experience, boolean modifier, double additionalBonus) {
        if (player.experienceLocked())
            return false;

        //Bonus experience
        experience = handleBonuses(skill, experience, modifier, additionalBonus);

        //Start
        int startingLevel = skill.isNewSkill() ? getMaxLevel(skill) / 10 : getMaxLevel(skill);
        double startingExperience = getExperience(skill);

        //Add experience
        setExperience(skill, getExperience(skill) + experience, false);

        if (startingExperience < MAX_EXPERIENCE) {

            int newLevel = getLevelForExperience(getExperience(skill), skill == Skill.DUNGEONEERING);

            //200m Announcement
            if (startingExperience + experience >= MAX_EXPERIENCE)
                maxExperience(skill);


            //Level up
            if (newLevel > startingLevel) {

                setMaxLevel(skill, skill.isNewSkill() ? newLevel * 10 : newLevel, false);
                if (!skill.isNewSkill() && skill != Skill.SUMMONING && getLevel(skill) < getMaxLevel(skill))
                    setLevel(skill, getMaxLevel(skill), false);

                levelUp(skill, newLevel);
            }
        }

        refresh(skill);
        if (totalGainedExp + experience < Long.MAX_VALUE)
            totalGainedExp += experience;
        return true;
    }

    public void refresh(Skill skill) {
        int maxLevel = getMaxLevel(skill), currentLevel = getLevel(skill);
        if (skill == Skill.PRAYER)
            player.getPacketSender().sendString(687, currentLevel / 10 + " / " + maxLevel / 10);
        player.getPacketSender().sendString(31199, "Total level: " + getTotalLevel());
        player.getPacketSender().sendString(19000, "Combat level: " + getCombatLevel());
        player.getPacketSender().sendSkill(skill, (int) (getExperience(skill) > Integer.MAX_VALUE ? Integer.MAX_VALUE : getExperience(skill)));
    }


    /**
     * Getters
     */

    public int getLevel(Skill skill) {
        return data.getLevel(skill);
    }

    public int getMaxLevel(Skill skill) {
        return data.getMaxLevel(skill);
    }

    public double getExperience(Skill skill) {
        return data.getExperience(skill);
    }

    public SkillData getData() {
        return data;
    }

    public long getTotalGainedExp() {
        return totalGainedExp;
    }

    public static int getExperienceForLevel(int level) {
        if (level <= 99) {
            return EXP_ARRAY[--level > 98 ? 98 : level];
        } else {
            int points = 0;
            int output = 0;
            for (int lvl = 1; lvl <= level; lvl++) {
                points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
                if (lvl >= level) {
                    return output;
                }
                output = (int) Math.floor(points / 4);
            }
        }
        return 0;
    }

    public static int getLevelForExperience(double experience, boolean dungeoneering) {
        if (experience <= EXPERIENCE_FOR_99) {
            for (int j = 98; j >= 0; j--) {
                if (EXP_ARRAY[j] <= experience) {
                    return j + 1;
                }
            }
        } else {
            int points = 0, output = 0;
            for (int lvl = 1; lvl <= (dungeoneering ? 120 : 99); lvl++) {
                points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
                output = (int) Math.floor(points / 4);
                if (output >= experience) {
                    return lvl;
                }
            }
        }
        return 99;
    }

    public int getTotalLevel() {
        int total = 0;
        for (Skill skill : Skill.values())
            total += skill.isNewSkill() ? getMaxLevel(skill) / 10 : getMaxLevel(skill);
        return total;
    }

    public long getTotalExp() {
        long xp = 0;
        for (Skill skill : Skill.values())
            xp += getExperience(skill);
        return xp;
    }

    public static int getMaxAchievingLevel(Skill skill) {
        int level = 99;

        if (skill.isNewSkill())
            level *= 10;
        else if (skill == Skill.DUNGEONEERING)
            level = 120;

        return level;
    }

    public int getCombatLevel() {
        final int attack = getMaxLevel(Skill.ATTACK);
        final int defence = getMaxLevel(Skill.DEFENCE);
        final int strength = getMaxLevel(Skill.STRENGTH);
        final int hp = getMaxLevel(Skill.CONSTITUTION) / 10;
        final int prayer = getMaxLevel(Skill.PRAYER) / 10;
        final int ranged = getMaxLevel(Skill.RANGED);
        final int magic = getMaxLevel(Skill.MAGIC);
        final int summoning = getMaxLevel(Skill.SUMMONING);

        int combatLevel = (int) ((defence + hp + Math.floor(prayer / 2)) * 0.2535) + 1;
        final double melee = (attack + strength) * 0.325;
        final double ranger = Math.floor(ranged * 1.5) * 0.325;
        final double mage = Math.floor(magic * 1.5) * 0.325;

        if (melee >= ranger && melee >= mage)
            combatLevel += melee;
        else if (ranger >= melee && ranger >= mage)
            combatLevel += ranger;
        else if (mage >= melee && mage >= ranger)
            combatLevel += mage;

        if (!player.isInWilderness())
            combatLevel += summoning * 0.125;
        else if (combatLevel > 126)
            return 126;

        if (combatLevel > 138)
            return 138;
        else if (combatLevel < 3)
            return 3;

        return combatLevel;
    }


    /**
     * Setters
     */

    public void setLevel(Skill skill, int level) {
        setLevel(skill, level, true);
    }

    public void setLevel(Skill skill, int level, boolean refresh) {
        data.setLevel(skill, level < 0 ? 0 : level);
        if (refresh)
            refresh(skill);
    }

    public void setMaxLevel(Skill skill, int level) {
        setMaxLevel(skill, level, true);
    }

    public void setMaxLevel(Skill skill, int level, boolean refresh) {
        data.setMaxLevel(skill, level);
        if (refresh)
            refresh(skill);
    }

    public void setExperience(Skill skill, int experience) {
        setExperience(skill, experience, true);
    }

    public void setExperience(Skill skill, double experience, boolean refresh) {
        data.setExperience(skill, experience < 0 ? 0 : experience > Long.MAX_VALUE ? Long.MAX_VALUE : experience);
        if (refresh)
            refresh(skill);
    }

    public void setData(SkillData data) {
        this.data = data;
    }

    public void setTotalGainedExp(long totalGainedExp) {
        this.totalGainedExp = totalGainedExp;
    }

    public static int getColour(Skill skill) {
        switch (skill) {
            case ATTACK:
                return 0x8e1d11;
            case STRENGTH:
                return 0x118e5d;
            case DEFENCE:
                return 0x0000ff;
            case CONSTITUTION:
                return 0xbf2818;
            case PRAYER:
                return 0xfbe264;
            case MAGIC:
                return 0x090d64;
            case RANGED:
                return 0x6c8822;
            case RUNECRAFTING:
                return 0xdabf1a;
            case CONSTRUCTION:
                return 0x72552d;
            case DUNGEONEERING:
                return 0xba6215;
            case AGILITY:
                return 0x9b3026;
            case HERBLORE:
                return 0x0d8011;
            case THIEVING:
                return 0x91527b;
            case CRAFTING:
                return 0xbd8118;
            case FLETCHING:
                return 0x119398;
            case SLAYER:
                return 0x961d0f;
            case HUNTER:
                return 0x4a433a;
            case MINING:
                return 0x6fabc3;
            case SMITHING:
                return 0x87877d;
            case FISHING:
                return 0x008FB2;
            case COOKING:
                return 0x6b2c7c;
            case FIREMAKING:
                return 0xf8841d;
            case WOODCUTTING:
                return 0x39a755;
            case FARMING:
                return 0xcbef7d;
            case SUMMONING:
                return 0xffffff;
            default:
                return 0xffff00;
        }
    }

    public int getColourString(Skill skill) {
        switch (skill) {
            case ATTACK:
                return 0x8e1d11;
            case STRENGTH:
                return 0x118e5d;
            case DEFENCE:
                return 0x0000ff;
            case CONSTITUTION:
                return 0xbf2818;
            case PRAYER:
                return 0xfbe264;
            case MAGIC:
                return 0x090d64;
            case RANGED:
                return 0x6c8822;
            case RUNECRAFTING:
                return 0xdabf1a;
            case CONSTRUCTION:
                return 0x72552d;
            case DUNGEONEERING:
                return 0xba6215;
            case AGILITY:
                return 0x9b3026;
            case HERBLORE:
                return 0x0d8011;
            case THIEVING:
                return 0x91527b;
            case CRAFTING:
                return 0xbd8118;
            case FLETCHING:
                return 0x119398;
            case SLAYER:
                return 0x961d0f;
            case HUNTER:
                return 0x4a433a;
            case MINING:
                return 0x6fabc3;
            case SMITHING:
                return 0x87877d;
            case FISHING:
                return 0x008FB2;
            case COOKING:
                return 0x6b2c7c;
            case FIREMAKING:
                return 0xf8841d;
            case WOODCUTTING:
                return 0x39a755;
            case FARMING:
                return 0xcbef7d;
            case SUMMONING:
                return 0xffffff;
            default:
                return 0xffff00;
        }
    }
    private void levelUp(Skill skill, int level) {
        String skillName = Misc.formatText(skill.toString().toLowerCase());

        //Chat box dialogue
        player.getDialogueManager().finishDialogue();
        player.getPacketSender().sendString(4268, "Congratulations! You've just advanced a " + skillName + " level!");
        player.getPacketSender().sendString(4269, "You have now reached level " + level + "!");
        player.getPacketSender().sendString(358, "Click here to continue");
        player.getPacketSender().sendChatboxInterface(skill.getChatboxInterface());

        //Chat box message
        player.message("You've just advanced a " + skillName + " level! You have reached level " + level);

        //Celebration
        player.graphic(312);
        Sounds.sendSound(player, Sounds.Sound.LEVELUP);

        if (getMaxLevel(skill) == getMaxAchievingLevel(skill)) {
            player.message("Well done! You've achieved the highest possible level in this skill!");

            if (isMaxed() && !player.getAchievementAttributes().getCompletion()[AchievementData.REACH_MAX_LEVEL_IN_ALL_SKILLS.ordinal()]) {
                Achievements.finishAchievement(player, AchievementData.REACH_MAX_LEVEL_IN_ALL_SKILLS);
                GameWorld.sendBroadcast("News: " + player.getUsername() + " (" + player.getGameMode().name().toLowerCase() + ") just achieved the highest possible level in all skills!", 0xffff00);
            } else {
                int colour = getColourString(skill);
                GameWorld.sendBroadcast(FontUtils.add("News:", 0x0000ff) + " " + player.getUsername() + " (" + player.getGameMode().name().toLowerCase() + ") just achieved the highest possible level in " + FontUtils.add(skillName, colour) + "!");
            }

            GameWorld.schedule(new Task(true, 2) {
                int localGFX = 1634;
                @Override
                public void run() {
                    player.perform(new Graphic(localGFX));
                    if (localGFX == 1637) {
                        stop();
                        return;
                    }
                    localGFX++;
                    player.perform(new Graphic(localGFX));
                }
            });

        } else {
            GameWorld.schedule(2, () -> player.graphic(199));
        }

        player.getUpdateFlags().add(Flag.APPEARANCE);
    }

    private void maxExperience(Skill skill) {
        Achievements.finishAchievement(player, AchievementData.REACH_MAX_EXP_IN_A_SKILL);
        String skillName = Misc.formatText(skill.toString().toLowerCase());
        int colour = getColourString(skill);
        GameWorld.sendBroadcast(FontUtils.add("News:", 0x0000ff) + " " + FontUtils.colourTags(colour) + player.getUsername() + " (" + player.getGameMode().name().toLowerCase() + ") just achieved 200m experience in " + skillName + FontUtils.COL_END + "!");
    }

    private double handleBonuses(Skill skill, double experience, boolean modifier, double bonus) {
        //Base game mode rate
        if (modifier)
            experience *= skill.isCombat() ? player.getGameMode().getCombatRate() : player.getGameMode().getSkillRate();

        //Additional bonus (items etc..)
        experience *= bonus;

        //Global bonus experience
        if (GameSettings.BONUS_EXP)
            experience *= 1.5;
        else
            experience = DailyEventManager.getExperienceAdded(skill, experience);

        //Vote book bonus
        if (player.getMinutesBonusExp() != -1)
            experience *= player.getGameMode().isIronMan() ? 1.20 : 1.30;

        //Refer a friend bonus
        experience *= player.getReferAFriend().getReferralBonusExperience();
        player.getReferAFriend().checkReward();

        return experience;
    }
}