package com.fury.game.content.global.dnd.star;

import com.fury.cache.Revision;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.content.dialogue.impl.misc.SimpleMessageD;
import com.fury.game.content.dialogue.impl.npcs.ShootingStarRewardD;
import com.fury.game.content.global.action.ActionManager;
import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.free.mining.Mining;
import com.fury.game.content.skill.free.mining.impl.star.ShootingStarMining;
import com.fury.core.model.item.Item;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.entity.object.GameObject;
import com.fury.game.node.entity.actor.object.ObjectManager;
import com.fury.game.world.FloorItemManager;
import com.fury.game.world.GameWorld;
import com.fury.util.Colours;
import com.fury.util.FontUtils;
import com.fury.util.Misc;
import com.fury.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Greg on 10/12/2016.
 */
public class ShootingStar {

    public static final int SHADOW = 8092, SPRITE = 8091, STAR = 38660;
    public static final Item STARDUST = new Item(13727);

    private static long crashTime = 0;
    private static volatile CrashedStar star;
    private static StarSprite sprite;
    private static Queue<BoardEntry> noticeboard = new ConcurrentLinkedQueue<>();
    private static StarLocations location;

    public static final int[] STAR_HEALTH = {600, 339, 200, 150, 100, 80, 40, 25, 15};
    private static final int SPAWN_DELAY = 2; // Hours
    private static boolean eventActive, starCrashed;
    private static int minutesTilCrash;
    private static int startHours;
    private static int expectedEndHours;
    private static int cleanMinutes = -1;

    public static final void init() {

    }

    private static void spawn() {
        location = StarLocations.values()[Misc.random(StarLocations.values().length)];
        sprite = new StarSprite(location.getPosition());
        minutesTilCrash = Misc.inclusiveRandom(2, 2);
        crashTime = Utils.currentTimeMillis() + (minutesTilCrash * 60 * 1000);
        star = new CrashedStar(location.getPosition());
        GameWorld.getPlayers().forEach(p -> {
            if (p.getRights().isOrHigher(PlayerRights.ADMINISTRATOR)) {
                p.message("Start at " + location.getPosition());
            }
        });
    }

    private static void crashStar() {
        starCrashed = true;
        ObjectManager.spawnObject(star);
        GameWorld.sendBroadcast(FontUtils.imageTags(535) + " " + FontUtils.SHAD + FontUtils.add("A shooting star has just crashed at " + location.getName() + "!", 0xff9933) + FontUtils.SHAD_END);
        GameWorld.getPlayers().forEach(p -> p.getPacketSender().sendString(39162, "Crashed star: " + FontUtils.YELLOW + location.getName() + FontUtils.COL_END, Colours.ORANGE_2));
    }

    private static void clean() {
        stopPlayersMining();
        Optional.ofNullable(star).ifPresent(ObjectManager::removeObject);
        Optional.ofNullable(sprite).ifPresent(GameWorld.getMobs()::remove);
        sprite = null;
        star = null;
        location = null;
        crashTime = 0;
        minutesTilCrash = 0;
        starCrashed = false;
        cleanMinutes = -1;
        expectedEndHours = -1;
        GameWorld.getPlayers().forEach(p -> p.getPacketSender().sendString(39162, "Crashed star:  " + FontUtils.YELLOW + "N/A" + FontUtils.COL_END, Colours.ORANGE_2));
    }

    private static void stopPlayersMining() {
        GameWorld.getPlayers().stream()
                .map(Player::getActionManager)
                .filter(manager -> manager.getAction() instanceof ShootingStarMining)
                .forEach(ActionManager::forceStop);
    }

    public static StarLocations getLocation() {
        return location;
    }

    public static void reward(Player player) {
        if (player.getInventory().delete(new Item(13727, 200))) {

            int[] items = new int[]{564, 9075, 445, 995, Mining.UNCUT_GEMS[Misc.random(Mining.UNCUT_GEMS.length - 2)] + 1};//-2 cause not onyx
            int[] maximum = new int[]{400, 200, 150, 1000000, 20};
            int[] amount = new int[items.length];
            for (int i = 0; i < items.length; i++) {
                amount[i] = Misc.randomMinusOne(maximum[i]);
                if (amount[i] > 0) {
                    if (player.getInventory().getSpaces() == 0 && !player.getInventory().contains(new Item(items[i])))
                        FloorItemManager.addGroundItem(new Item(items[i], amount[i]), player.copyPosition(), player);
                    else
                        player.getInventory().addSafe(new Item(items[i], amount[i]));
                }
            }
            List<Item> extras = new ArrayList<>();
            if (!player.hasItem(20787))
                extras.add(new Item(20787));
            if (!player.hasItem(20788))
                extras.add(new Item(20788));
            if (!player.hasItem(20789))
                extras.add(new Item(20789));
            if (!player.hasItem(20790))
                extras.add(new Item(20790));
            if (!player.hasItem(20791))
                extras.add(new Item(20791));

            if (extras.size() > 0 && Misc.random(10) == 0)
                player.getInventory().addSafe(extras.get(Misc.random(extras.size() - 1)));

            player.getDialogueManager().startDialogue(new ShootingStarRewardD(), items, amount);
        }
    }

    public static boolean hasCrashed() {
        return star != null && starCrashed;
    }

    public static void handleTimedEvent(int hours, int minutes) {
        if (!eventActive)
            handleNonActiveEvent(hours, minutes);
        else
            handleActiveEvent(hours, minutes);
    }

    private static void handleNonActiveEvent(int hours, int minutes) {
        if (minutes == 0 && hours % SPAWN_DELAY != 0) { // odd hours
            startEvent();
            startHours = hours;

            expectedEndHours = startHours + 1;
            if (expectedEndHours == 24)
                expectedEndHours = 0;
        }
    }

    private static void handleActiveEvent(int hours, int minutes) {
        if(star.hasBeenMined() && cleanMinutes == -1)
            cleanMinutes = minutes + 10;

        if (hours == expectedEndHours || (star.hasBeenMined() && minutes == cleanMinutes)) {
            endEvent();
        } else if (!starCrashed && minutes == minutesTilCrash)
            crashStar();
    }

    private static void startEvent() {
        eventActive = true;
        clean();
        spawn();

    }

    private static void endEvent() {
        eventActive = false;
        clean();
    }

    private static class BoardEntry {
        String name;
        long time;

        public BoardEntry(String name, long time) {
            this.name = name;
            this.time = time;
        }
    }

    public static void mine(Player player, GameObject object) {
        if (!eventActive || star == null)
            return;

        if (!star.isFirstClick()) {
            star.setFirstClick();
            int xp = player.getSkills().getMaxLevel(Skill.MINING) * 75;
            Mining.addExperience(player, xp);
            int actualXp = (int) (xp * player.getGameMode().getSkillRate());
            player.getDialogueManager().startDialogue(new SimpleMessageD(), "You were the first person to find this star and so you", "receive " + (actualXp > 1000 ? (actualXp / 1000 + "k") : actualXp) + " mining experience.");
            if (noticeboard.size() >= 5)
                noticeboard.poll();
            noticeboard.add(new BoardEntry(player.getUsername(), Utils.currentTimeMillis()));
            return;
        }
        player.getActionManager().setAction(new ShootingStarMining(object));
    }

    public static int getLevel() {
        return star == null ? 1 : star.getRequiredMiningLevel();
    }

    public static int getStarSize() {
        return star == null ? Integer.MAX_VALUE : star.getStarSize();
    }

    public static int getXP() {
        return star == null ? 1 : star.getXP();
    }

    public static void reduceStarLife() {
        if (!eventActive)
            return;

        star.reduceStarLife();

        if (star.getHealth() <= 0) {
            star.increaseStage();

            if (!star.hasBeenMined()) {
                ObjectManager.removeObject(star);
                star.setId(STAR + (8 - star.getStarSize()) + star.getCurrentStarSize());
                star.resetHealth();
                ObjectManager.spawnObject(star);
            } else {
                ObjectManager.removeObject(star);
                sprite.setTransformation(SPRITE, Revision.RS2);

                GameWorld.getPlayers().forEach(player -> {
                    if (player.getActionManager().getAction() instanceof ShootingStarMining)
                        player.getActionManager().forceStop();
                });
            }
        }
    }

    public static int getPercentage() {
        return star == null ? 0 : star.getPercentage();
    }

    public static void prospect(Player player) {
        if (star == null)
            return;

        player.getDialogueManager().startDialogue(new SimpleMessageD(), "This is a size " + star.getCurrentStarSize() + " star.",
                "A Mining level of at least " + star.getRequiredMiningLevel() + " is required to mine this layer.",
                "It has been mined about " + star.getLayerPercentage() + " percent of the way",
                "to the " + (star.getCurrentStarSize() == 1 ? "core" : "next layer") + ".");
    }

    public static void openNoticeboard(Player player) {
        int c = 0;
        long time = Utils.currentTimeMillis();
        //player.getInterfaceManager().sendCentralInterface(787);
        for (BoardEntry entry : noticeboard.toArray(new BoardEntry[noticeboard.size()])) {
            //player.getPacketSender().sendIComponentText(787, 6 + c, ((time - entry.time) / 60000)+" minutes ago");
            //player.getPacketSender().sendIComponentText(787, 11 + c, entry.name);
            c++;
        }
    }

    private static final String[][] NO_STAR_MESSAGES = {
            {"Hmm... are the stars really small,", "or are they just very far away?"},
            {"One of these stars has... little stars moving around it.", "Interesting..."},
            {"Oh no! A giant space spider is eating the moon!", "Oh, it was just a spider crawling across the lens."},
            {"It's overcast; I can't see anything."},
            {"My goodness... it's full of stars!"}
    };

    public static void openTelescope(Player player) {
        if (Utils.currentTimeMillis() > crashTime - 10 * 60 * 1000 && Utils.currentTimeMillis() < crashTime) {
            int time = (int) (crashTime - Utils.currentTimeMillis()) / (60 * 1000);
            player.getDialogueManager().startDialogue(new SimpleMessageD(), "You see a shooting star!", "The star looks like it will land in " + location.getName(), " in the next " + (time - 2) + " to " + (time + 2) + " minutes.");
        } else
            player.getDialogueManager().startDialogue(new SimpleMessageD(), (Object) NO_STAR_MESSAGES[Misc.random(NO_STAR_MESSAGES.length)]);

    }

}
