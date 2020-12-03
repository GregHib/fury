package com.fury.game.content.global.dnd.eviltree;

import com.fury.cache.def.Loader;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.engine.task.executor.GameExecutorManager;
import com.fury.game.content.dialogue.impl.misc.SimpleMessageD;
import com.fury.game.content.global.action.Action;
import com.fury.game.content.global.treasuretrails.ClueTiers;
import com.fury.game.content.global.treasuretrails.TreasureTrails;
import com.fury.game.content.skill.free.woodcutting.BirdNests;
import com.fury.game.entity.character.combat.effects.Effect;
import com.fury.game.entity.character.combat.effects.Effects;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.character.player.content.objects.PrivateObjectManager;
import com.fury.core.model.item.Item;
import com.fury.game.entity.object.GameObject;
import com.fury.game.node.entity.actor.object.ObjectManager;
import com.fury.game.world.GameWorld;
import com.fury.game.world.World;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.util.Colours;
import com.fury.util.FontUtils;
import com.fury.util.Misc;
import com.fury.util.NameUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class EvilTree {
    private static final EvilTree evilTree = new EvilTree();

    private EvilTreeClues clue;
    private EvilTreeLocations location;
    private EvilTreeTypes type;
    private int health;
    private Mob leprechaun;
    private GameObject tree;
    private EvilRoot[] roots;
    private EvilTreeFire[] fires;
    private int stage = 0;
    private Map<Player, Integer> players = new HashMap<>();
    private boolean eventActive;
    private int lightningStrikePower;
    private int startHours, startMinutes;
    private int cleanMinutes;
    private int expectedEndMinutes;
    private int nextLightning;
    private int lightning;

    private static final int SEEDLING = 11391;
    private static final int SPAWN_DELAY = 2;//Hours
    public static final Item KINDLING = new Item(14666);

    public static EvilTree get() {
        return evilTree;
    }

    public void spawn() {
        clue = EvilTreeClues.values()[Misc.random(EvilTreeClues.values().length - 1)];
        location = clue.locations[Misc.random(clue.locations.length - 1)];
        type = EvilTreeTypes.values()[Misc.random(EvilTreeTypes.values().length - 4)];
        health = type.getSeedHeath();
        leprechaun = new Mob(418, location.position.copyPosition().add(-1, -1));
        tree = new GameObject(SEEDLING, location.position.copyPosition().add(1, 1));

        ObjectManager.spawnObject(tree);
    }

    public void handleTimedEvent(int hours, int minutes) {
        if(!eventActive) {
            handleNonActiveEvent(hours, minutes);
        } else {
            handleActiveEvent(hours, minutes);
        }
    }

    private void handleNonActiveEvent(int hours, int minutes) {
        if(isDead()) {
            if(cleanMinutes == -1)
                cleanMinutes = minutes + 10;
            else if (minutes == cleanMinutes)
                clean();
        }
        else if (minutes == 0 && hours % SPAWN_DELAY == 0) { // even hours
            startEvent();
            startHours = hours;
            startMinutes = minutes;

            expectedEndMinutes = 30;
        }
    }

    private void handleActiveEvent(int hours, int minutes) {
        if(isDead() && cleanMinutes == -1) {
            cleanMinutes = minutes + 10;
        }

        if(hours == startHours && minutes == expectedEndMinutes) {
            endEvent(hours, minutes);
        } else if(minutes == nextLightning) {
            performLightning();
            nextLightning = minutes + 10;
            lightning++;
        }
    }

    private void startEvent() {
        eventActive = true;
        clean();
        spawn();
        GameWorld.sendBroadcast(FontUtils.imageTags(535) + " The taint of an evil tree has been felt across the land.", 0x4a6212);
        GameWorld.getPlayers().forEach(p -> p.getPacketSender().sendString(39163, "Evil tree: " + FontUtils.YELLOW +  location.getName() + FontUtils.COL_END, Colours.ORANGE_2));

    }

    private void endEvent(int hours, int minutes) {
        eventActive = false;
        if(!isDead()) {
            death(true);
            cleanMinutes = minutes + 10;
        }
    }

    private boolean isSeedling(int id) {
        return id >= 11391 && id <= 11395;
    }

    private void death(boolean lightning) {
        despawnSurroundings();

        ObjectManager.removeObject(tree);
        if (lightning) {
            tree.setId(type.getDeath());
            ObjectManager.spawnObject(tree);
            World.sendObjectAnimation(tree, new Animation(1707));
        } else {
            if (Misc.random(1) == 0)
                leprechaun.forceChat("Hurrah! The Evil Tree is dead!");

            tree.setId(type.getFallen());
            ObjectManager.spawnObject(tree);
        }


        //Spawn rewards tree
        for (Player player : players.keySet())
            if (player != null) {
                Action action = player.getActionManager().getAction();
                if (action instanceof NurtureSapling || action instanceof ChopEvilTree || player.getActionManager().getAction() instanceof LightKindling)
                    player.getActionManager().forceStop();
                PrivateObjectManager.addPrivateObject(player, new GameObject(type.getReward(), tree), 300000);
            }
        GameWorld.getPlayers().forEach(p -> p.getPacketSender().sendString(39163, "Evil tree: "+ FontUtils.YELLOW + "N/A" + FontUtils.COL_END, Colours.ORANGE_2));
    }

    public void clean() {
        Optional.ofNullable(tree).ifPresent(ObjectManager::removeObject);
        Optional.ofNullable(leprechaun).ifPresent(GameWorld.getMobs()::remove);

        tree = null;
        leprechaun = null;
        clue = null;
        location = null;
        type = null;
        health = -1;
        stage = 0;
        lightningStrikePower = 0;
        players.clear();
        despawnSurroundings();
    }

    private void despawnSurroundings() {
        if (roots != null)
            for (int i = 0; i < roots.length; i++) {
                EvilRoot root = roots[i];
                if (root != null) {
                    root.despawn();
                    root.kill(tree);
                    roots[i] = null;
                }
            }
        if (fires != null)
            for (int i = 0; i < fires.length; i++) {
                EvilTreeFire fire = fires[i];
                if (fire != null) {
                    fire.despawn();
                    fires[i] = null;
                }
            }

        roots = null;
        fires = null;
    }

    public void depleteHealth(Player player, int amount) {
        if (!players.containsKey(player))
            players.put(player, amount);
        else
            players.put(player, players.get(player) + amount);

        setHealth(getHealth() - amount);
    }

    private void setHealth(int value) {
        health = value;
        if (health <= 0)
            update();
        else
            change();
    }

    public int getHealth() {
        return health;
    }

    private void performLightning() {
        if(lightning == 3) {
            if (isSeedling(tree.getId())) {
                clean();
            } else {
                death(true);
            }
        }

        lightningStrikePower += 2;

        Optional.ofNullable(tree).ifPresent(tree -> {
            tree.graphic(313);
            if (health > type.getHeath() / lightningStrikePower)
                setHealth(type.getHeath() / lightningStrikePower);
        });
    }

    private void update() {
        if (tree.getId() >= SEEDLING && tree.getId() < 11395) {
            health = type.getSeedHeath();
            ObjectManager.removeObject(tree);
            tree.setId(tree.getId() + 1);

            if (tree.getId() == 11394)//Compensate for size
                tree.add(-1, -1);

            ObjectManager.spawnObject(tree);
        } else if (tree.getId() == 11395) { //Spawn evil tree
            ObjectManager.removeObject(tree);
            tree.setId(type.getHealthy());
            health = type.getHeath();
            ObjectManager.spawnObject(tree);
            spawnRoots();
        } else if (health <= 0) {
            death(false);
        }
        stage++;
    }

    private void change() {
        if (stage > 5) {
            if (health < type.getHeath() / 4 && tree.getId() != type.getQuaterHealth()) {
                ObjectManager.removeObject(tree);
                tree.setId(type.getQuaterHealth());
                ObjectManager.spawnObject(tree);
            } else if (health > type.getHeath() / 4 && health < type.getHeath() / 2 && tree.getId() != type.getHalfHealth()) {
                ObjectManager.removeObject(tree);
                tree.setId(type.getHalfHealth());
                ObjectManager.spawnObject(tree);
                if (Misc.random(1) == 0)
                    leprechaun.forceChat(Misc.random(1) == 0 ? "Take 'im down!" : "It's getting weaker!");
            }
        }
    }

    public EvilTreeFire getFire(Position position) {
        for (EvilTreeFire fire : fires)
            if (fire != null && fire.sameAs(position))
                return fire;
        return null;
    }

    private void spawnRoots() {
        roots = new EvilRoot[4];
        roots[0] = new EvilRoot(11427, location.position.copyPosition().add(-1, 1), 0);
        roots[1] = new EvilRoot(11429, location.position.copyPosition().add(1, -1), 3);
        roots[2] = new EvilRoot(11431, location.position.copyPosition().add(3, 1), 2);
        roots[3] = new EvilRoot(11433, location.position.copyPosition().add(1, 3), 1);

        fires = new EvilTreeFire[12];
        fires[0] = new EvilTreeFire(11425, location.position.copyPosition().add(-1, 1), 2);//West
        fires[1] = new EvilTreeFire(11425, location.position.copyPosition().add(-1, 0), 2);//South-West
        fires[2] = new EvilTreeFire(11425, location.position.copyPosition().add(0, -1), 1);//South-West
        fires[3] = new EvilTreeFire(11425, location.position.copyPosition().add(1, -1), 1);//South
        fires[4] = new EvilTreeFire(11425, location.position.copyPosition().add(2, -1), 1);//South-East
        fires[5] = new EvilTreeFire(11425, location.position.copyPosition().add(3, 0), 0);//South-East
        fires[6] = new EvilTreeFire(11425, location.position.copyPosition().add(3, 1), 0);//East
        fires[7] = new EvilTreeFire(11425, location.position.copyPosition().add(3, 2), 0);//North-East
        fires[8] = new EvilTreeFire(11425, location.position.copyPosition().add(2, 3), 3);//North-East
        fires[9] = new EvilTreeFire(11425, location.position.copyPosition().add(1, 3), 3);//North
        fires[10] = new EvilTreeFire(11425, location.position.copyPosition().add(0, 3), 3);//North-West
        fires[11] = new EvilTreeFire(11425, location.position.copyPosition().add(-1, 2), 2);//North-West

        Arrays.stream(roots).forEach(EvilRoot::spawn);

        GameExecutorManager.slowExecutor.schedule(() -> {
            if (roots != null) {
                for (EvilRoot root : roots) {
                    if (root != null) {
                        root.despawn();
                        root.setId(root.getId() - 1);
                        root.spawn();
                    }
                }
            }
        }, 1, TimeUnit.SECONDS);
    }

    public static boolean handleObjectClick(Player player, GameObject object) {
        if (object.getId() >= SEEDLING && object.getId() <= SEEDLING + 4) {
            player.getActionManager().setAction(new NurtureSapling());
            return false;
        } else if (EvilTree.isRoot(object.getId())) {
            player.getActionManager().setAction(new ChopEvilTree(object, true));
            return true;
        } else {
            EvilTreeTypes type = EvilTreeTypes.getTree(object.getId());
            if (type != null) {
                if (object.getId() == type.getReward())
                    EvilTree.get().reward(player);
                else
                    player.getActionManager().setAction(new ChopEvilTree(object, false));
                return true;
            }

        }
        return false;
    }

    public static int getPercentage() {
        EvilTree tree = EvilTree.get();
        double totalHealth = 5 * tree.getType().getSeedHeath();
        double depletedHealth = (tree.getStage() * tree.getType().getSeedHeath()) + (tree.getType().getSeedHeath() - tree.getHealth());
        double percentage = depletedHealth / totalHealth * 100;
        return (int) percentage;
    }

    public static boolean handleSecondObjectClick(Player player, GameObject object) {
        if (object.getId() >= SEEDLING && object.getId() <= SEEDLING + 4) {
            player.getDialogueManager().startDialogue(new SimpleMessageD(), "This " + object.getDefinition().getName().toLowerCase() + " appears to be about " + getPercentage() + "% grown.");
            return true;
        }

        EvilTreeTypes type = EvilTreeTypes.getTree(object.getId());
        if (type != null) {
            player.getActionManager().setAction(new LightKindling());
            return true;
        }

        return false;
    }

    public static boolean handleThirdObjectClick(Player player, GameObject object) {
        EvilTreeTypes type = EvilTreeTypes.getTree(object.getId());
        if (type != null) {
            player.getDialogueManager().startDialogue(new SimpleMessageD(), "This is an Evil" + (type == EvilTreeTypes.NORMAL ? "" : " " + NameUtils.capitalize(type.name().toLowerCase())) + " tree.",
                    "A Woodcutting / Firemaking level of at least " + type.getRequiredLevel() + " is",
                    "required to interact with this tree and the surrounding roots.");
            return true;
        }

        return false;
    }

    private void reward(Player player) {
        if (players.containsKey(player)) {
            int damage = players.get(player);
            int log;
            int amount = Misc.random(damage < 10 ? 1 : 10, damage < 10 ? 10 : damage);
            List<Item> extras = new ArrayList<>();
            switch (type) {
                case NORMAL:
                default:
                    log = 1512;
                    extras.add(new Item(5312, Misc.random(1, 10)));
                    break;
                case OAK:
                    log = 1522;
                    extras.add(new Item(5312, Misc.random(1, 20)));
                    break;
                case WILLOW:
                    log = 1520;
                    extras.add(new Item(5313, Misc.random(1, 8)));
                    break;
                case MAPLE:
                    log = 1518;
                    extras.add(new Item(5314, Misc.random(1, 6)));
                    break;
                case YEW:
                    log = 1516;
                    extras.add(new Item(5315, Misc.random(1, 3)));
                    break;
                case MAGIC:
                    log = 1514;
                    extras.add(new Item(5316, Misc.random(1, 2)));
                    break;
                case ELDER:
                    log = 6334;
                    extras.add(new Item(5316, Misc.random(1, 4)));
                    break;
            }
            //Clue scrolls
            boolean[] clues = TreasureTrails.getActiveClues(player);
            for (int i = 0; i < 3; i++)
                if (!clues[i])
                    extras.add(new Item(ClueTiers.values()[i].getScrollId()));
            //Birds nests
            for (int nest : BirdNests.BIRD_NEST_IDS)
                extras.add(new Item(nest));
            extras.add(new Item(21621, Misc.random(1, 11)));//Fellstalks
            extras.add(new Item(21620, Misc.random(1, 10)));//Morchella mushroom spores
            //Key halves
            extras.add(new Item(985));
            extras.add(new Item(987));
            //Lumberjack

            if (damage > 50) {
                if (!player.hasItem(10941))
                    extras.add(new Item(10941));
                if (!player.hasItem(10939))
                    extras.add(new Item(10939));
                if (!player.hasItem(10940))
                    extras.add(new Item(10940));
                if (!player.hasItem(10933))
                    extras.add(new Item(10933));

                //Dragon hatchet
                extras.add(new Item(6739));
            }

            int coins = (type.ordinal() + 1) * 20000;
            player.getInventory().addSafe(new Item(log, amount));
            player.getInventory().addCoins(Misc.random(coins));

            int random = Misc.random(2);
            String extra = "";

            Item first = extras.get(Misc.random(extras.size() - 1));
            extras.remove(first);
            giveItem(player, first);
            if (random == 1) {
                Item second = extras.get(Misc.random(extras.size() - 1));
                extra = " and " + (second.getAmount() > 1 ? "some" : Misc.anOrA(second.getName())) + " " + second.getName() + "!";
                giveItem(player, second);
            }
            player.getDialogueManager().sendItemDialogue(log - 1,
                    "Inside the hollow stump you found: " + amount + " " + Loader.getItem(log).getName() + ",",
                    coins + " gold coins.", "You also found " + (first.getAmount() > 1 ? "some" : Misc.anOrA(first.getName())) + " " + first.getName() + (random == 1 ? "!" : ""),
                    (random == 1 ? extra : ""));

            player.getEffects().startEffect(new Effect(Effects.EVIL_TREE_BUFF, (type.getBuff() * 60 * 1000) / 600));//Minutes to ticks
            player.message("The leprechaun snaps its fingers, and you are granted a magical log-banking ability", 0xffffff);
            player.message("for the next " + type.getBuff() + " minutes.", 0xffffff);
            players.remove(player);
        }

        if (tree != null)
            PrivateObjectManager.replaceObject(player, tree, tree);
    }

    private void giveItem(Player player, Item first) {
        ClueTiers clue = null;
        for (int i = 0; i < ClueTiers.values().length; i++) {
            if (first.getId() == ClueTiers.values()[i].getScrollId()) {
                clue = ClueTiers.values()[i];
                break;
            }
        }
        if (clue != null)
            TreasureTrails.giveClue(player, clue);
        else
            player.getInventory().addSafe(first);
    }

    private static boolean isRoot(int id) {
        if (id >= 11426 && id <= 11433)
            return true;
        return false;
    }

    public EvilTreeTypes getType() {
        return type;
    }

    public EvilTreeLocations getLocation() {
        return location;
    }

    public int getStage() {
        return stage;
    }

    public boolean isDead() {
        return tree == null || tree.getId() == type.getDeath() || tree.getId() == type.getReward() || tree.getId() == type.getFallen();
    }

    public void performAnimation() {
        if (evilTree.tree != null && evilTree.tree.getDefinition() != null)
            evilTree.tree.perform(new Animation(evilTree.tree.getDefinition().animationId + (evilTree.getHealth() > evilTree.getType().getHeath() / 4 ? 1 : 2)));
    }
}
