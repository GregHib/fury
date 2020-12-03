package com.fury.game.content.skill.member.thieving;

import com.fury.game.container.impl.equip.Equipment;
import com.fury.game.container.impl.equip.Slot;
import com.fury.game.content.global.thievingguild.ThievingGuild;
import com.fury.game.content.misc.items.StrangeRocks;
import com.fury.game.content.dialogue.impl.misc.SimpleMessageD;
import com.fury.game.content.global.Achievements;
import com.fury.game.content.global.action.Action;
import com.fury.game.content.skill.Skill;
import com.fury.game.entity.character.combat.CombatIcon;
import com.fury.game.entity.character.combat.Hit;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.game.world.update.flag.block.HitMask;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.game.world.update.flag.block.graphic.GraphicHeight;
import com.fury.util.Misc;
import com.fury.util.Utils;

/**
 * Created by Greg on 27/12/2016.
 */
public class PickPocketAction extends Action {

    /**
     * Pick pocketing npc.
     */
    private Mob mob;

    /**
     * Data of an npc.
     */
    private PickPocketableNpc npcData;

    /**
     * The npc stun animation.
     */
    private static final Animation STUN_ANIMATION = new Animation(422),

    /**
     * The pick pocketing animation.
     */
    PICKPOCKETING_ANIMATION = new Animation(881),

    /**
     * The double loot animation.
     */
    DOUBLE_LOOT_ANIMATION = new Animation(5074),

    /**
     * The triple loot animation.
     */
    TRIPLE_LOOT_ANIMATION = new Animation(5075),

    /**
     * The quadruple loot animation.
     */
    QUADRUPLE_LOOT_ANIMATION = new Animation(5078);

    /**
     * The double loot gfx.
     */
    private static final Graphic DOUBLE_LOOT_GFX = new Graphic(873),

    /**
     * The triple loot gfx.
     */
    TRIPLE_LOOT_GFX = new Graphic(874),

    /**
     * The quadruple loot gfx.
     */
    QUADRUPLE_LOOT_GFX = new Graphic(875);

    /**
     * The index to use in the levels required arrays.
     */
    private int index;

    /**
     * Constructs a new {@code PickpocketAction} {@code Object}.
     *
     * @param mob
     *            The npc to whom the player is pickpocketing.
     * @param npcData
     *            Data of an npc.
     */
    public PickPocketAction(Mob mob, PickPocketableNpc npcData) {
        this.mob = mob;
        this.npcData = npcData;
    }

    @Override
    public boolean start(Player player) {
        if (checkAll(player)) {
            int thievingLevel = player.getSkills().getLevel(Skill.THIEVING);
            int agilityLevel = player.getSkills().getLevel(Skill.AGILITY);
            if (Misc.getRandom(50) < 5) {
                for (int i = 0; i < 4; i++) {
                    if (npcData.getThievingLevels()[i] <= thievingLevel && npcData.getAgilityLevels()[i] <= agilityLevel)
                        index = i;
                }
            }
            player.getDirection().face(mob);
            if(getAnimation() != null)
                player.perform(getAnimation());
            if(getGraphics() != null)
                player.perform(getGraphics());
            player.message("You attempt to pick the " + mob.getName().toLowerCase() + "'s pocket...", true);
            setActionDelay(player, 1);
            return true;
        }
        return false;
    }

    @Override
    public boolean process(Player player) {
        return checkAll(player);
    }

    @Override
    public int processWithDelay(Player player) {
        if (mob.getId() == 11275 && ThievingGuild.canDisturb()) {
            player.moveTo(4764, 5793);
            return -1;
        }

        if (!isSuccessful(player)) {
            player.message("You fail to pick the " + mob.getName().toLowerCase() + "'s pocket.", true);
            mob.getDirection().face(player);
            player.animate(424);
            player.perform(new Graphic(80, 5, GraphicHeight.HIGH));
            player.message("You've been stunned.", true);
            player.getCombat().applyHit(new Hit(npcData.getStunDamage(), HitMask.RED, CombatIcon.NONE));
            if (npcData.equals(PickPocketableNpc.MASTER_FARMER) || npcData.equals(PickPocketableNpc.FARMER)) {
                mob.forceChat("Cor blimey mate, what are ye doing in me pockets?");
                mob.perform(STUN_ANIMATION);
            } else if (npcData.equals(PickPocketableNpc.DESERT_PHONIX)) {
                mob.forceChat("Squawk!");
            } else if (npcData.equals(PickPocketableNpc.MONKEY_KNIFE_FIGHTER)) {
            } else {
                mob.forceChat("What do you think you're doing?");
                mob.perform(STUN_ANIMATION);
            }
            player.perform(new Graphic(80, GraphicHeight.HIGH));
            player.getMovement().lock(npcData.getStunTime());
            stop(player);
        } else {
            if (ThievingGuild.isInThievingGuild(player.copyPosition()) && ThievingGuild.canDisturb() ) {
                if (!ThievingGuild.isInVault(player.copyPosition()))
                    ThievingGuild.disturbTheMaster(100);
            }
            if (ThievingGuild.canRecieveBonusAwards() && ThievingGuild.isInThievingGuild(player.copyPosition()) && ThievingGuild.canReceiveItems(player) && ThievingGuild.isInVault(player) && ThievingGuild.isEventActive() && ThievingGuild.isVaultOpen()) {
                if (mob.getId() == 11275 && Misc.random(50) == 0 && !ThievingGuild.alreadyClaimed(18648, player)) {
                    mob.forceChat("Where did my precious golden chalice go?!");
                    player.message("You have stolen a golden chalice!", true);
                    Item goldenChalice = new Item(18648, 1);
                    ThievingGuild.claim(goldenChalice, player);
                }
                if (mob.getId() == 11275 && Misc.random(75) == 0 && !ThievingGuild.alreadyClaimed(5561, player)) {
                    mob.forceChat("Where did my precious jewel go?!");
                    player.message("You have stolen a nice shiny jewel!", true);
                    Item jewel = new Item(5561, 1);
                    ThievingGuild.claim(jewel, player);
                }
            }
            player.message(getMessage(), true);
            double totalXp = npcData.getExperience();
            if (hasThievingSuit(player))
                totalXp *= 1.025;
            player.getSkills().addExperience(Skill.THIEVING, totalXp);
            for (int i = 0; i <= index; i++) {
                Item item = npcData.getLoot()[Misc.random(npcData.getLoot().length)];
                player.getInventory().add(item);
            }
            if(npcData == PickPocketableNpc.MAN)
                Achievements.finishAchievement(player, Achievements.AchievementData.PICKPOCKET_A_MAN);
            Achievements.doProgress(player, Achievements.AchievementData.PICKPOCKET_5000_PEOPLE);
            StrangeRocks.handleStrangeRocks(player, Skill.THIEVING);
        }
        return -1;
    }

    @Override
    public void stop(Player player) {
        mob.getDirection().face(null);
        setActionDelay(player, 3);
    }

    private boolean hasThievingSuit(Player player) {
        return player.getEquipment().get(Slot.HEAD).getId() == 21482 && player.getEquipment().get(Slot.BODY).getId() == 21480 && player.getEquipment().get(Slot.LEGS).getId() == 21481 && player.getEquipment().get(Slot.FEET).getId() == 21483;
    }

    /**
     * Checks if the player is succesfull to thiev or not.
     *
     * @param player
     *            The player.
     * @return {@code True} if succesfull, {@code false} if not.
     */
    private boolean isSuccessful(Player player) {
		int thievingLevel = player.getSkills().getLevel(Skill.THIEVING) + (player.getSkills().getLevel(Skill.THIEVING) == 1 ? 5 : 0);
		int increasedChance = getIncreasedChance(player);
		int level = Misc.getRandom(thievingLevel + increasedChance);
		double ratio = level / (Utils.random(npcData.getThievingLevels()[0] + 5) + 1);
		if (Math.round(ratio * thievingLevel) < npcData.getThievingLevels()[0] / player.getAuraManager().getThievingAccurayMultiplier())
		    return false;
        return true;
    }

    /**
     * Gets the increased chance for successfully pickpocketing.
     *
     * @param player
     *            The player.
     * @return The amount of increased chance.
     */
    private int getIncreasedChance(Player player) {
        int chance = 0;
        if (player.getEquipment().get(Slot.HANDS).getId() == 10075)
            chance += 12;
        if (Equipment.wearingArdyCloak(player, 2))
            chance += 15;
        if (mob.getName().contains("H.A.M")) {
            for (Item item : player.getEquipment().getItems()) {
                if (item != null && item.getDefinition().name.contains("H.A.M")) {
                    chance += 3;
                }
            }
        }
        return chance;
    }

    /**
     * Gets the message to send when finishing.
     *
     * @return The message.
     */
    private String getMessage() {
        switch (index) {
            case 0:
                return "You successfully pick the " + mob.getName().toLowerCase() + "'s pocket.";
            case 1:
                return "Your lighting-fast reactions allow you to steal double loot.";
            case 2:
                return "Your lighting-fast reactions allow you to steal triple loot.";
            case 3:
                return "Your lighting-fast reactions allow you to steal quadruple loot.";
        }
        return null;
    }

    /**
     * Checks everything before starting.
     *
     * @param player
     *            The player.
     * @return
     */
    private boolean checkAll(Player player) {
        if (player.getSkills().getLevel(Skill.THIEVING) < npcData.getThievingLevels()[0]) {
            player.getDialogueManager().startDialogue(new SimpleMessageD(), "You need a thieving level of " + npcData.getThievingLevels()[0] + " to steal from this npc.");
            return false;
        }
        if (player.getInventory().getSpaces() < 1 && !player.getInventory().contains(new Item(995))) {
            player.message("You don't have enough space in your inventory.");
            return false;
        }
        if (player.getCombat().getAttackedBy() != null && player.getCombat().getAttackedByDelay() > Utils.currentTimeMillis()) {
            player.message("You can't do this while you're in combat.");
            return false;
        }
        if (mob.getCombat().getAttackedBy() != null && mob.getCombat().getAttackedByDelay() > Utils.currentTimeMillis()) {
            player.message("The npc is currently in combat.");
            return false;
        }
        if (mob.getFinished()) {
            player.message("Too late, the npc is dead.");
            return false;
        }
        return true;

    }

    /**
     * Gets the animation to perform.
     *
     * @return The animation.
     */
    private Animation getAnimation() {
        switch (index) {
            case 0:
                return PICKPOCKETING_ANIMATION;
            case 1:
                return DOUBLE_LOOT_ANIMATION;
            case 2:
                return TRIPLE_LOOT_ANIMATION;
            case 3:
                return QUADRUPLE_LOOT_ANIMATION;
        }
        return null;
    }

    /**
     * Gets the graphic to perform.
     *
     * @return The graphic.
     */
    private Graphic getGraphics() {
        switch (index) {
            case 0:
                return null;
            case 1:
                return DOUBLE_LOOT_GFX;
            case 2:
                return TRIPLE_LOOT_GFX;
            case 3:
                return QUADRUPLE_LOOT_GFX;
        }
        return null;
    }
}
