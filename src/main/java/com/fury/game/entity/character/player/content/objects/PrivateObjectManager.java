package com.fury.game.entity.character.player.content.objects;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.object.GameObject;
import com.fury.game.node.entity.actor.object.ObjectManager;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;
import com.fury.util.Misc;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class PrivateObjectManager {
    public static final AtomicLong keyMaker = new AtomicLong();

    private static final Map<String, PrivateObjectManager> ownedObjects = new ConcurrentHashMap<>();

    private Player player;
    private GameObject[] objects;
    private int count;
    private boolean spawn;
    private long[] cycleTimes;
    private long lifeTime;
    private String managerKey;
    private PrivateObjectProcessEvent event;

    public static void sequence() {
        for (PrivateObjectManager object : ownedObjects.values())
            object.process();
    }

    public static void updateLogin(Player player) {
        for (Iterator<String> it = player.getOwnedObjectManagerKeys().iterator(); it.hasNext(); ) {
            final PrivateObjectManager manager = ownedObjects.get(it.next());
            if (manager == null) {
                it.remove();
                continue;
            }
            manager.player = player;
            if(player.getRegionId() == manager.objects[manager.count].getRegionId() && !manager.spawn)
                player.getPacketSender().sendObject(manager.objects[manager.count]);
        }
    }

    public static void refreshObjects(Player player) {
        for (Iterator<String> it = player.getOwnedObjectManagerKeys().iterator(); it.hasNext(); ) {
            final PrivateObjectManager manager = ownedObjects.get(it.next());
            if (manager == null) {
                it.remove();
                continue;
            }

            if(player.getRegionId() == manager.objects[manager.count].getRegionId() && !manager.spawn)
                player.getPacketSender().sendObject(manager.objects[manager.count]);
        }
    }

    public static Player getObjectPlayer(GameObject object) {
        for (PrivateObjectManager manager : ownedObjects.values()) {
            if (manager.getCurrentObject().getX() == object.getX() && manager.getCurrentObject().getY() == object.getY() && manager.getCurrentObject().getZ() == object.getZ() && manager.getCurrentObject().getId() == object.getId())
                return manager.player;
        }
        return null;
    }

    public static GameObject getObjectWithCoords(Player player, Position position) {
        for (Iterator<String> it = player.getOwnedObjectManagerKeys().iterator(); it.hasNext(); ) {
            final PrivateObjectManager manager = ownedObjects.get(it.next());
            if (manager == null) {
                it.remove();
                continue;
            }
            if (manager.getCurrentObject().getX() == position.getX() && manager.getCurrentObject().getY() == position.getY() && manager.getCurrentObject().getZ() == position.getZ()) {
                return manager.getCurrentObject();
            }
        }
        return null;
    }

    public static boolean isPlayerObject(Player player, GameObject object) {
        for (Iterator<String> it = player.getOwnedObjectManagerKeys().iterator(); it.hasNext(); ) {
            PrivateObjectManager manager = ownedObjects.get(it.next());
            if (manager == null) {
                it.remove();
                continue;
            }
            if (manager.getCurrentObject().getX() == object.getX() && manager.getCurrentObject().getY() == object.getY() && manager.getCurrentObject().getZ() == object.getZ() && manager.getCurrentObject().getId() == object.getId())
                return true;
        }
        return false;
    }

    public static boolean convertIntoObject(GameObject object, GameObject toObject, PrivateObjectConvertEvent event) {
        for (PrivateObjectManager manager : ownedObjects.values()) {
            if (manager.getCurrentObject().getX() == toObject.getX() && manager.getCurrentObject().getY() == toObject.getY() && manager.getCurrentObject().getZ() == toObject.getZ() && manager.getCurrentObject().getId() == object.getId()) {
                if (event != null && !event.canConvert(manager.player))
                    return false;
                manager.convertIntoObject(toObject);
                return true;
            }
        }
        return false;
    }

    public static boolean deleteObject(Player player, GameObject object) {
        for (Iterator<String> it = player.getOwnedObjectManagerKeys().iterator(); it.hasNext(); ) {
            final PrivateObjectManager manager = ownedObjects.get(it.next());
            if (manager == null) {
                it.remove();
                continue;
            }
            if (manager.getCurrentObject().getX() == object.getX() && manager.getCurrentObject().getY() == object.getY() && manager.getCurrentObject().getZ() == object.getZ() && manager.getCurrentObject().getId() == object.getId()) {
                GameWorld.schedule(1, manager::delete);
                return true;
            }
        }
        return false;
    }

    public static boolean deleteObject(Player player, Position position) {
        for (Iterator<String> it = player.getOwnedObjectManagerKeys().iterator(); it.hasNext(); ) {
            final PrivateObjectManager manager = ownedObjects.get(it.next());
            if (manager == null) {
                it.remove();
                continue;
            }
            if (manager.getCurrentObject().getX() == position.getX() && manager.getCurrentObject().getY() == position.getY() && manager.getCurrentObject().getZ() == position.getZ()) {
                GameWorld.schedule(1, manager::delete);
                return true;
            }
        }
        return false;
    }

    public static boolean replaceObject(Player player, Position position, GameObject object) {
        for (Iterator<String> it = player.getOwnedObjectManagerKeys().iterator(); it.hasNext(); ) {
            final PrivateObjectManager manager = ownedObjects.get(it.next());
            if (manager == null) {
                it.remove();
                continue;
            }
            if (manager.getCurrentObject().getX() == position.getX() && manager.getCurrentObject().getY() == position.getY() && manager.getCurrentObject().getZ() == position.getZ()) {
                GameWorld.schedule(1, () -> {
                    manager.delete();
                    if(!manager.spawn)
                        player.getPacketSender().sendObject(object);
                });
                return true;
            }
        }
        return false;
    }

    public static boolean cancel(String key) {
        for (PrivateObjectManager manager : ownedObjects.values()) {
            if (manager.managerKey.equals(key)) {
                manager.delete();
                return true;
            }
        }
        return false;
    }

    public static void linkKeys(Player player) {
        for (Iterator<String> it = player.getOwnedObjectManagerKeys().iterator(); it.hasNext(); ) {
            PrivateObjectManager manager = ownedObjects.get(it.next());
            if (manager == null) {
                it.remove();
                continue;
            }
            manager.player = player;
        }
    }

    public static String spawnPrivateObject(Player player, GameObject object, long cycleTime) {
        return spawnPrivateObject(player, new GameObject[]
                {object}, new long[]
                {cycleTime});
    }

    public static String spawnPrivateObject(Player player, GameObject[] object, long[] cycleTimes) {
        return spawnPrivateObject(player, object, cycleTimes, null);
    }

    public static String spawnPrivateObject(Player player, GameObject[] object, long[] cycleTimes, PrivateObjectProcessEvent event) {
        return new PrivateObjectManager(player, object, cycleTimes, event, true).managerKey;
    }

    public static String addPrivateObject(Player player, GameObject object, long cycleTime) {
        return addPrivateObject(player, new GameObject[]
                {object}, new long[]
                {cycleTime});
    }

    public static String addPrivateObject(Player player, GameObject[] object, long[] cycleTimes) {
        return addPrivateObject(player, object, cycleTimes, null);
    }

    public static String addPrivateObject(Player player, GameObject[] object, long[] cycleTimes, PrivateObjectProcessEvent event) {
        return new PrivateObjectManager(player, object, cycleTimes, event, false).managerKey;
    }

    private PrivateObjectManager(Player player, GameObject[] objects, long[] cycleTimes, PrivateObjectProcessEvent event, boolean spawn) {
        managerKey = player.getUsername() + "_" + keyMaker.getAndIncrement();
        this.cycleTimes = cycleTimes;
        this.objects = objects;
        this.player = player;
        this.event = event;
        this.spawn = spawn;
        spawnObject();
        player.getOwnedObjectManagerKeys().add(managerKey);
        ownedObjects.put(managerKey, this);
    }

    public static int getObjects(Player player, int objectId) {
        int count = 0;
        for (Iterator<String> it = player.getOwnedObjectManagerKeys().iterator(); it.hasNext(); ) {
            PrivateObjectManager manager = ownedObjects.get(it.next());
            if (manager == null) {
                it.remove();
                continue;
            }
            if (manager.getCurrentObject().getId() == objectId)
                count++;
        }
        return count;
    }

    public static GameObject getObject(Player player, int objectId) {
        for (Iterator<String> it = player.getOwnedObjectManagerKeys().iterator(); it.hasNext(); ) {
            PrivateObjectManager manager = ownedObjects.get(it.next());
            if (manager == null) {
                it.remove();
                continue;
            }
            if (manager.getCurrentObject().getId() == objectId)
                return manager.getCurrentObject();
        }
        return null;
    }

    public static boolean isPrivateObject(Player player, int objectId) {
        for (Iterator<String> it = player.getOwnedObjectManagerKeys().iterator(); it.hasNext(); ) {
            PrivateObjectManager manager = ownedObjects.get(it.next());
            if (manager == null) {
                it.remove();
                continue;
            }
            if (manager.getCurrentObject().getId() == objectId)
                return true;
        }
        return false;
    }

    public static boolean containsObjectValue(Player player, int... objectIds) {
        for (Iterator<String> it = player.getOwnedObjectManagerKeys().iterator(); it.hasNext(); ) {
            PrivateObjectManager manager = ownedObjects.get(it.next());
            if (manager == null) {
                it.remove();
                continue;
            }
            for (int objectId : objectIds)
                if (manager.getCurrentObject().getId() == objectId)
                    return true;
        }
        return false;
    }

    public void reset() {
        for (PrivateObjectManager object : ownedObjects.values())
            object.delete();
    }

    public void resetLifeTime() {
        this.lifeTime = Misc.currentTimeMillis() + cycleTimes[count];
    }

    public boolean forceMoveNextStage() {
        if (count != -1)
            destroyObject(objects[count]);
        count++;
        if (count == objects.length) {
            remove();
            return false;
        }
        spawnObject();
        return true;
    }

    private void spawn() {
        if (spawn)
            ObjectManager.spawnObject(objects[count]);
        else
            player.getPacketSender().sendObject(objects[count]);
    }
    private void spawnObject() {
        spawn();
        if (event != null)
            event.spawnObject(player, getCurrentObject());
        resetLifeTime();
    }

    public void convertIntoObject(GameObject object) {
        destroyObject(objects[count]);
        objects[count] = object;
        spawnObject();
    }

    private void remove() {
        ownedObjects.remove(managerKey);
        if (player != null)
            player.getOwnedObjectManagerKeys().remove(managerKey);
    }

    public void delete() {
        destroyObject(objects[count]);
        remove();
    }

    public void process() {
        if (Misc.currentTimeMillis() > lifeTime)
            forceMoveNextStage();
        else if (event != null)
            event.process(player, getCurrentObject());
    }

    public GameObject getCurrentObject() {
        return objects[count];
    }

    public void destroyObject(GameObject object) {
        if (spawn)
            ObjectManager.removeObject(object);
        else
            player.getPacketSender().sendObjectRemoval(object);

        if (event != null)
            event.destroyObject(player, getCurrentObject());
    }
}
