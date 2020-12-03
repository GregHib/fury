package com.fury.game.content.skill.free.dungeoneering;

import com.fury.cache.Revision;
import com.fury.cache.def.Loader;
import com.fury.cache.def.npc.NpcDefinition;
import com.fury.cache.def.object.ObjectDefinition;
import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.engine.task.executor.GameExecutorManager;
import com.fury.game.GameSettings;
import com.fury.game.container.impl.equip.Slot;
import com.fury.game.container.impl.shop.ShopManager;
import com.fury.game.content.controller.impl.Daemonheim;
import com.fury.game.content.dialogue.impl.misc.SimpleMessageD;
import com.fury.game.content.global.Achievements;
import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.free.dungeoneering.DungeonConstants.KeyDoors;
import com.fury.game.content.skill.free.dungeoneering.rooms.BossRoom;
import com.fury.game.content.skill.free.dungeoneering.skills.DungeoneeringFishing;
import com.fury.game.content.skill.free.dungeoneering.skills.DungeoneeringMining;
import com.fury.game.entity.character.npc.impl.dungeoneering.*;
import com.fury.game.entity.character.player.content.BonusManager;
import com.fury.game.content.combat.magic.MagicSpellBook;
import com.fury.core.model.item.Item;
import com.fury.game.entity.item.content.ItemDegrading;
import com.fury.game.entity.object.GameObject;
import com.fury.game.node.entity.actor.figure.player.handles.Settings;
import com.fury.game.node.entity.actor.object.ObjectManager;
import com.fury.game.node.entity.actor.object.TempObjectManager;
import com.fury.game.system.files.loaders.item.WeaponAnimations;
import com.fury.game.system.files.loaders.item.WeaponInterfaces;
import com.fury.game.world.FloorItemManager;
import com.fury.game.world.GameWorld;
import com.fury.game.world.World;
import com.fury.game.world.map.Position;
import com.fury.game.world.map.build.MapBuilder;
import com.fury.game.world.map.region.Region;
import com.fury.game.world.update.flag.Flag;
import com.fury.game.world.update.flag.block.Direction;
import com.fury.game.world.update.flag.block.Gender;
import com.fury.util.FontUtils;
import com.fury.util.Logger;
import com.fury.util.Misc;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;


/**
 * Created by Greg on 03/07/2016.
 */
public class DungeonManager {

    private static final Map<Object, DungeonManager> cachedDungeons = Collections.synchronizedMap(new HashMap<Object, DungeonManager>());
    public static final AtomicLong keyMaker = new AtomicLong();

    private DungeonPartyManager party;
    public Dungeon dungeon;
    private VisibleRoom[][] visibleMap;
    private int[] boundChunks;
    private int stage; //0 - not loaded. 1 - loaded. 2 - new one not loaded, old one loaded(rewards screen)
    private RewardsTimer rewardsTimer;
    private DestroyTimer destroyTimer;
    private long time;
    private List<DungeonConstants.KeyDoors> keyList;
    private List<String> farmKeyList;
    private String key;
    private HashMap<String, MagicSpellBook> previousSpellbook = new HashMap<>();

    private Position groupGatestone;
    private List<MastyxTrap> mastyxTraps;

    //force saving deaths
    private Map<String, Integer> partyDeaths;

    private boolean tutorialMode;

    private DungeonBoss temporaryBoss; //must for gravecreeper, cuz... it gets removed from npc list :/

    // 7554
    public DungeonManager(DungeonPartyManager party) {
        this.party = party;
        tutorialMode = party.getMaxComplexity() < 6;
        load();
        keyList = new CopyOnWriteArrayList<>();
        farmKeyList = new CopyOnWriteArrayList<>();
        mastyxTraps = new CopyOnWriteArrayList<>();
        partyDeaths = new ConcurrentHashMap<>();
    }

    public void clearKeyList() {
        for (Player player : party.getTeam()) {
            //player.getPacketSender().sendExecuteScriptReverse(6072);
            //player.getPacketSender().sendCSVarInteger(1875, 0); //forces refresh
        }
    }

    public void setKey(KeyDoors key, boolean add) {
        if (add) {
            keyList.add(key);
            for (Player player : party.getTeam())
                player.message("Your party found a key: " + new Item(key.getKeyId()).getName(), 0xd2691e);
        } else {
            keyList.remove(key);
            for (Player player : party.getTeam())
                player.message("Your party used a key: " + new Item(key.getKeyId()).getName(), 0xd2691e);
        }
        /*for (Player player : party.getTeam()) {
            player.getPacketSender().sendCSVarInteger(1812 + key.getIndex(), add ? 1 : 0);
            if (key.getIndex() != 64)
                player.getPacketSender().sendCSVarInteger(1875, keyList.contains(KeyDoors.GOLD_SHIELD) ? 1 : 0);
        }*/
    }

    public boolean isAtBossRoom(Position tile) {
        return isAtBossRoom(tile, -1, -1, false);
    }

    public boolean isAtBossRoom(Position tile, int x, int y, boolean check) {
        Room room = getRoom(getCurrentRoomReference(tile));
        if (room == null || !(room.getRoom() instanceof BossRoom))
            return false;
        if (check) {
            BossRoom bRoom = (BossRoom) room.getRoom();
            if (x != bRoom.getChunkX() || y != bRoom.getChunkY())
                return false;
        }
        return true;
    }

    public boolean isBossOpen() {
        for (int x = 0; x < visibleMap.length; x++) {
            for (int y = 0; y < visibleMap[x].length; y++) {
                VisibleRoom room = visibleMap[x][y];
                if (room == null || !room.isLoaded())
                    continue;
                if (isAtBossRoom(getRoomCenterTile(room.reference)))
                    return true;
            }
        }
        return false;
    }

    /*
     * dont use
     */
    public void refreshKeys(Player player) {
       /* for (KeyDoors key : keyList)
            player.getPacketSender().sendCSVarInteger(1812 + key.getIndex(), 1);
        player.getPacketSender().sendCSVarInteger(1875, keyList.contains(KeyDoors.GOLD_SHIELD) ? 1 : 0);*/
    }

    public boolean hasKey(KeyDoors key) {
        return keyList.contains(key);
    }

    public boolean isKeyShare() {
        return party.isKeyShare();
    }

    /*
     * when dung ends to make sure no1 dies lo, well they can die but still..
     */
    public void clearGuardians() {
        for (int x = 0; x < visibleMap.length; x++)
            for (int y = 0; y < visibleMap[x].length; y++)
                if (visibleMap[x][y] != null)
                    visibleMap[x][y].forceRemoveGuardians();
    }

    public int getVisibleRoomsCount() {
        int count = 0;
        for (int x = 0; x < visibleMap.length; x++)
            for (int y = 0; y < visibleMap[x].length; y++)
                if (visibleMap[x][y] != null)
                    count++;
        return count;
    }

    public int getVisibleBonusRoomsCount() {
        int count = 0;
        for (int x = 0; x < visibleMap.length; x++)
            for (int y = 0; y < visibleMap[x].length; y++)
                if (visibleMap[x][y] != null && !dungeon.getRoom(new RoomReference(x, y)).isCritPath())
                    count++;
        return count;
    }

    public int getLevelModPerc() {
        int totalGuardians = 0;
        int killedGuardians = 0;

        for (int x = 0; x < visibleMap.length; x++)
            for (int y = 0; y < visibleMap[x].length; y++)
                if (visibleMap[x][y] != null) {
                    totalGuardians += visibleMap[x][y].getGuardiansCount();
                    killedGuardians += visibleMap[x][y].getKilledGuardiansCount();
                }

        return totalGuardians == 0 ? 100 : killedGuardians * 100 / totalGuardians;
    }

    public boolean enterRoom(Player player, GameObject object, int x, int y) {
        if (x < 0 || y < 0 || x >= visibleMap.length || y >= visibleMap[0].length) {
            if (GameSettings.DEBUG) System.out.println("Invalid dungeoneering room");
            return false;
        }
        RoomReference roomReference = getCurrentRoomReference(player);
        //player.getMovement().lock(0);
        if (visibleMap[x][y] != null) {
            if (!visibleMap[x][y].isLoaded())
                return false;
            int xOffset = x - roomReference.getX();
            int yOffset = y - roomReference.getY();
            player.moveTo(new Position(object.getX() + xOffset * 2, object.getY() + yOffset * 2, 0));
            playMusic(player, new RoomReference(x, y));
            return true;
        } else {
            loadRoom(x, y);
            return false;
        }
    }

    public void loadRoom(int x, int y) {
        loadRoom(new RoomReference(x, y));
    }

    public void loadRoom(final RoomReference reference) {
        final Room room = dungeon.getRoom(reference);
        if (room == null)
            return;
        VisibleRoom vr = new VisibleRoom();
        visibleMap[reference.getX()][reference.getY()] = vr;
        vr.init(this, reference, party.getFloorType(), room.getRoom());
        GameExecutorManager.slowExecutor.execute(() -> openRoom(room, reference, visibleMap[reference.getX()][reference.getY()]));
    }

    public boolean isDestroyed() {
        return dungeon == null;
    }

    public void openRoom(final Room room, final RoomReference reference, final VisibleRoom visibleRoom) {
        final int toChunkX = boundChunks[0] + reference.getX() * 2;
        final int toChunkY = boundChunks[1] + reference.getY() * 2;
        final int fromChunkX = room.getChunkX(party.getComplexity());
        final int fromChunkY = room.getChunkY(party.getFloorType());

        MapBuilder.copy2RatioSquare(fromChunkX, fromChunkY, toChunkX, toChunkY, room.getRotation(), 0);

        Set<Integer> regionIds = new HashSet<>();
        for (Player player : party.getTeam())
            if (player.getDungManager().isInside())
                regionIds.add(player.getRegionId());

        for (int regionId : regionIds)
            MapBuilder.reloadDynamicRegion(regionId);

        int regionId = (((toChunkX / 8) << 8) + (toChunkY / 8));

        World.executeAfterLoadRegion(regionId, () -> {
            if (isDestroyed())
                return;

            //Doors
            room.openRoom(DungeonManager.this, reference);
            visibleRoom.openRoom();
            for (int i = 0; i < room.getRoom().getDoorDirections().length; i++) {
                Door door = room.getDoor(i);
                if (door == null)
                    continue;
                int rotation = (room.getRoom().getDoorDirections()[i] + room.getRotation()) & 0x3;
                if (door.getType() == DungeonConstants.KEY_DOOR) {
                    KeyDoors keyDoor = KeyDoors.values()[door.getId()];
                    setDoor(reference, keyDoor.getObjectId(), keyDoor.getDoorId(party.getFloorType()), rotation);
                } else if (door.getType() == DungeonConstants.GUARDIAN_DOOR) {
                    setDoor(reference, -1, DungeonConstants.DUNGEON_GUARDIAN_DOORS[party.getFloorType()], rotation);
                    if (visibleRoom.roomCleared())  //remove reference since done
                        room.setDoor(i, null);
                } else if (door.getType() == DungeonConstants.SKILL_DOOR) {
                    DungeonConstants.SkillDoors skillDoor = DungeonConstants.SkillDoors.values()[door.getId()];
                    int type = party.getFloorType();
                    int closedId = skillDoor.getClosedObject(type);
                    int openId = skillDoor.getOpenObject(type);
                    setDoor(reference, openId == -1 ? closedId : -1, openId != -1 ? closedId : -1, rotation);
                }
            }
            if (room.getRoom().allowResources())
                setResources(room, reference, toChunkX, toChunkY);

            if (room.getDropId() != -1)
                setKey(room, reference, toChunkX, toChunkY);
            visibleRoom.setLoaded();
        });
    }

    public void setDoor(RoomReference reference, int lockObjectId, int doorObjectId, int rotation) {
        if (lockObjectId != -1) {
            int[] xy = DungeonManager.translate(1, 7, rotation, 1, 2, 0);
            ObjectManager.spawnObject(new GameObject(lockObjectId, new Position(((boundChunks[0] * 8) + reference.getX() * 16) + xy[0], ((boundChunks[1] * 8) + reference.getY() * 16) + xy[1], 0), 10, rotation));
        }
        if (doorObjectId != -1) {
            int[] xy = DungeonManager.translate(0, 7, rotation, 1, 2, 0);
            ObjectManager.spawnObject(new GameObject(doorObjectId, new Position(((boundChunks[0] * 8) + reference.getX() * 16) + xy[0], ((boundChunks[1] * 8) + reference.getY() * 16) + xy[1], 0), 10, rotation));
        }
    }

    public void setKey(Room room, RoomReference reference, int toChunkX, int toChunkY) {

        int[] loc = room.getRoom().getKeySpot();
        if (loc != null) {
            spawnItem(reference, new Item(room.getDropId()), loc[0], loc[1]);
            return;
        }

        spawnItem(reference, new Item(room.getDropId()), 7, 1); //spawn it on the entrance door

		/*
        Cache<int[]> keySpots = new ArrayList<int[]>();
		if (keySpots.isEmpty()) {

			for (int worldX = 2; worldX < 15; worldX++) {
				for (int worldY = 2; worldY < 15; worldY++) {
					if (!World.isTileFree(0, toChunkX * 8 + worldX, toChunkY * 8 + worldY, 2))
						continue;
					keySpots.add(new int[]
					{ worldX, worldY });
				}
			}
		}
		loc = keySpots.isEmpty() ? new int[]
		{ 8, 8 } : keySpots.forId(Misc.random(keySpots.size()));
		World.addGroundItem(new Item(room.getDropId()), new Position(toChunkX * 8 + loc[0], toChunkY * 8 + loc[1], 0));
		*/
    }

    public void setResources(Room room, RoomReference reference, int toChunkX, int toChunkY) {
        if (party.getComplexity() >= 5 && Misc.random(3) == 0) { //sets thief chest
            for (int i = 0; i < DungeonConstants.SET_RESOURCES_MAX_TRY; i++) {
                int rotation = Misc.random(DungeonConstants.WALL_BASE_X_Y.length);
                int[] b = DungeonConstants.WALL_BASE_X_Y[rotation];
                int x = b[0] + Misc.random(b[2]);
                int y = b[1] + Misc.random(b[3]);
                if (((x >= 6 && x <= 8) && b[2] != 0) || ((y >= 6 && y <= 8) && b[3] != 0))
                    continue;
                if ((World.getMask(toChunkX * 8 + x, toChunkY * 8 + y, 0) & 0x1280120) != 0 || (World.getMask(toChunkX * 8 + x - Misc.ROTATION_DIR_X[((rotation + 3) & 0x3)], toChunkY * 8 + y - Misc.ROTATION_DIR_Y[((rotation + 3) & 0x3)], 0) & 0x1280120) != 0)
                    continue;
                room.setThiefChest(Misc.random(10));
                ObjectManager.spawnObject(new GameObject(DungeonConstants.THIEF_CHEST_LOCKED[party.getFloorType()], new Position(toChunkX * 8 + x, toChunkY * 8 + y, (rotation + 3) & 0x3), 10, 0));
                break;
            }
        }
        if (party.getComplexity() >= 4 && Misc.random(3) == 0) { //sets flower
            for (int i = 0; i < DungeonConstants.SET_RESOURCES_MAX_TRY; i++) {
                int rotation = Misc.random(DungeonConstants.WALL_BASE_X_Y.length);
                int[] b = DungeonConstants.WALL_BASE_X_Y[rotation];
                int x = b[0] + Misc.random(b[2]);
                int y = b[1] + Misc.random(b[3]);
                if (((x >= 6 && x <= 8) && b[2] != 0) || ((y >= 6 && y <= 8) && b[3] != 0))
                    continue;
                if ((World.getMask(toChunkX * 8 + x, toChunkY * 8 + y, 0) & 0x1280120) != 0)
                    continue;
                ObjectManager.spawnObject(new GameObject(DungeonUtils.getFarmingResource(Misc.random(10), party.getFloorType()), new Position(toChunkX * 8 + x, toChunkY * 8 + y, 0), 10, rotation));
                break;
            }
        }
        if (party.getComplexity() >= 3 && Misc.random(3) == 0) { //sets rock
            for (int i = 0; i < DungeonConstants.SET_RESOURCES_MAX_TRY; i++) {
                int rotation = Misc.random(DungeonConstants.WALL_BASE_X_Y.length);
                int[] b = DungeonConstants.WALL_BASE_X_Y[rotation];
                int x = b[0] + Misc.random(b[2]);
                int y = b[1] + Misc.random(b[3]);
                if (((x >= 6 && x <= 8) && b[2] != 0) || ((y >= 6 && y <= 8) && b[3] != 0))
                    continue;
                if ((World.getMask(toChunkX * 8 + x, toChunkY * 8 + y, 0) & 0x1280120) != 0)
                    continue;
                ObjectManager.spawnObject(new GameObject(DungeonUtils.getMiningResource(Misc.random(DungeoneeringMining.DungeoneeringRocks.values().length), party.getFloorType()), new Position(toChunkX * 8 + x, toChunkY * 8 + y, 0), 10, rotation));
                break;
            }
        }
        if (party.getComplexity() >= 2 && Misc.random(3) == 0) { //sets tree
            for (int i = 0; i < DungeonConstants.SET_RESOURCES_MAX_TRY; i++) {
                int rotation = Misc.random(DungeonConstants.WALL_BASE_X_Y.length);
                int[] b = DungeonConstants.WALL_BASE_X_Y[rotation];
                int x = b[0] + Misc.random(b[2]);
                int y = b[1] + Misc.random(b[3]);
                if (((x >= 6 && x <= 8) && b[2] != 0) || ((y >= 6 && y <= 8) && b[3] != 0))
                    continue;
                Position pos = new Position(toChunkX * 8 + x, toChunkY * 8 + y, 0);
                if (!World.blocked(pos) || Region.getGameObject(pos) != null)
                    continue;
                x -= Misc.ROTATION_DIR_X[rotation];
                y -= Misc.ROTATION_DIR_Y[rotation];
                ObjectManager.spawnObject(new GameObject(DungeonUtils.getWoodcuttingResource(Misc.random(10), party.getFloorType()), new Position(toChunkX * 8 + x, toChunkY * 8 + y, 0), 10, rotation));
                break;
            }
        }
        if (party.getComplexity() >= 2) { //sets fish spot
            List<int[]> fishSpots = new ArrayList<>();
            for (int x = 0; x < 16; x++) {
                for (int y = 0; y < 16; y++) {
                    GameObject o = ObjectManager.getObjectWithType(new Position(toChunkX * 8 + x, toChunkY * 8 + y, 0), 10);
                    if (o == null || o.getId() != DungeonConstants.FISH_SPOT_OBJECT_ID)
                        continue;
                    fishSpots.add(new int[]
                            {x, y});
                }
            }
            if (!fishSpots.isEmpty()) {
                int[] spot = fishSpots.get(Misc.random(fishSpots.size()));
                spawnNPC(DungeonConstants.FISH_SPOT_NPC_ID, room.getRotation(), new Position(toChunkX * 8 + spot[0], toChunkY * 8 + spot[1], 0), reference, DungeonConstants.FISH_SPOT_NPC, 1);
            }
        }
    }

    public Position getRoomCenterTile(RoomReference reference) {
        return getRoomBaseTile(reference).add(8, 8, 0);
    }

    public Position getRoomBaseTile(RoomReference reference) {
        return new Position(((boundChunks[0] << 3) + reference.getX() * 16), ((boundChunks[1] << 3) + reference.getY() * 16), 0);
    }

    public RoomReference getCurrentRoomReference(Position tile) {
        return new RoomReference((tile.getX() - (boundChunks[0] * 8)) / 16, ((tile.getY() - (boundChunks[1] * 8)) / 16));
        //return new RoomReference((tile.getChunkX() - boundChunksX[0]) / 2, ((tile.getChunkY() - boundChunksY[0]) / 2));
    }

    public Position getDungeonTile() {
        return new Position(boundChunks[0] * 8, boundChunks[1] * 8);
    }

    public Room getRoom(RoomReference reference) {
        return dungeon == null ? null : dungeon.getRoom(reference);
    }

    public VisibleRoom getVisibleRoom(RoomReference reference) {
        if (reference.getX() >= visibleMap.length || reference.getY() >= visibleMap[0].length)
            return null;
        return visibleMap[reference.getX()][reference.getY()];
    }

    public Position getHomeTile() {
        return getRoomCenterTile(dungeon.getStartRoomReference());
    }

    public void telePartyToRoom(RoomReference reference) {
        Position tile = getRoomCenterTile(reference);
        //System.out.println("Tele party to: " + tile);
        for (Player player : party.getTeam()) {
            player.moveTo(tile, 0, Direction.SOUTH);
            //playMusic(player, reference);
        }
    }

    public void playMusic(Player player, RoomReference reference) {
        if (reference.getX() >= visibleMap.length || reference.getY() >= visibleMap[reference.getX()].length)
            return;
        //player.getMusicsManager().forcePlayMusic(visibleMap[reference.getChunkX()][reference.getChunkY()].getMusicId());
    }

    public void linkPartyToDungeon() {
        GameWorld.schedule(1, () -> {
            for (Player player : party.getTeam()) {
                player.setDungeoneeringRing(player.hasItemOnThem(15707));
                if (player.hasItemOnThem(5733))
                    player.getTemporaryAttributes().put("CarryModPotato", true);

                player.getPrayer().closeAllPrayers();
                resetItems(player, player, false, false);
                sendSettings(player);

                if (party.getComplexity() >= 5 && party.isLeader(player))
                    player.getInventory().add(DungeonConstants.GROUP_GATESTONE);
                removeMark(player);
                sendStartItems(player);
                player.message("");
                player.message("-Welcome to Daemonheim-");
                player.message("Floor " + FontUtils.add("" + party.getFloor(), 0x641d9e) + "    " + FontUtils.add("Complexity", 0xffffff) + " " + FontUtils.add(party.getComplexity() + "", 0x641d9e));
                String[] sizeNames = new String[]{"Small", "Medium", "Large", "Test"};
                player.message("Dungeon Size: " + FontUtils.add(sizeNames[party.getSize()] + "", 0x641d9e));
                player.message("Party Size:Difficulty " + FontUtils.add(party.getTeam().size() + ":" + party.getDifficulty(), 0x641d93));
                player.message("Pre-Share: " + FontUtils.add(isKeyShare() ? "OFF" : "ON", 0x641d9e));

                if (party.isGuideMode())
                    player.message("Guide Mode ON", 0x641d9e);
                player.message("");
                player.message("Warning: Dungeoneering is still work in progress, bugs will occur.", 0x641d9e);
                player.getMovement().unlock();
            }
            resetGatestone();
        });
    }

    public void setTableItems(RoomReference room) {
        addItemToTable(room, new Item(16295)); //novite pickaxe, cuz of boss aswell so 1+
        if (party.getComplexity() >= 2) {
            addItemToTable(room, new Item(DungeonConstants.RUSTY_COINS, 5000 + Misc.random(10000)));
            addItemToTable(room, new Item(17678)); //tinderbox
            addItemToTable(room, new Item(16361)); //novite hatcher
            addItemToTable(room, new Item(17794)); //fish rods
        }
        if (party.getComplexity() >= 3) { //set weap/gear in table
            int rangeTier = DungeonUtils.getTier(party.getMaxLevel(Skill.RANGED));
            if (rangeTier > 8)
                rangeTier = 8;
            addItemToTable(room, new Item(DungeonUtils.getArrows(1 + Misc.random(rangeTier)), 90 + Misc.random(30))); //arround 100 arrows, type random up to best u can, limited to tier 8
            addItemToTable(room, new Item(DungeonConstants.RUNES[0], 90 + Misc.random(30))); //arround 100 air runes
            addItemToTable(room, new Item(DungeonConstants.RUNE_ESSENCE, 90 + Misc.random(30))); //arround 100 essence
            addItemToTable(room, new Item(17754)); //knife
            addItemToTable(room, new Item(17883)); //hammer
            if (party.getComplexity() >= 4)
                addItemToTable(room, new Item(17446)); //needle
        }
        for (@SuppressWarnings("unused")
                Player player : party.getTeam()) {
            for (int i = 0; i < 7 + Misc.random(4); i++)
                //9 food
                addItemToTable(room, new Item(DungeonUtils.getFood(1 + Misc.random(8))));
            if (party.getComplexity() >= 3) { //set weap/gear in table
                for (int i = 0; i < 1 + Misc.random(3); i++)
                    //1 up to 3 pieces of gear or weap
                    addItemToTable(room, new Item(DungeonUtils.getRandomGear(1 + Misc.random(8)))); //arround 100 essence
            }
        }
    }

    public void addItemToTable(RoomReference room, Item item) {
        int slot = Misc.random(10); //10 spaces for items
        if (slot < 6)
            spawnItem(room, item, 9 + Misc.random(3), 10 + Misc.random(2));
        else if (slot < 8)
            spawnItem(room, item, 10 + Misc.random(2), 14);
        else
            spawnItem(room, item, 14, 10 + Misc.random(2));
    }

    public void sendStartItems(Player player) {
        if (party.getComplexity() == 1) {
            player.getDialogueManager().startDialogue(new SimpleMessageD(), "Complexity 1", "Combat only", "Armour and weapons allocated", "No shop stock");
        } else if (party.getComplexity() == 2) {
            player.getDialogueManager().startDialogue(new SimpleMessageD(), "Complexity 2", "+ Fishing, Woodcutting, Firemaking, Cooking", "Armour and weapons allocated", "Minimal shop stock");
        } else if (party.getComplexity() == 3) {
            player.getDialogueManager().startDialogue(new SimpleMessageD(), "Complexity 3", "+ Mining, Smithing weapons, Fletching, Runecrafting", "Armour allocated", "Increased shop stock");
        } else if (party.getComplexity() == 4) {
            player.getDialogueManager().startDialogue(new SimpleMessageD(), "Complexity 4", "+ Smithing armour, Hunter, Farming textiles, Crafting", "Increased shop stock");
        } else if (party.getComplexity() == 5) {
            player.getDialogueManager().startDialogue(new SimpleMessageD(), "Complexity 5", "All skills included", "+ Farming seeds, Herblore, Thieving, Summoning", "Complete shop stock", "Challenge rooms + skill doors");
        }
        if (party.getComplexity() <= 3) {
            int defenceTier = DungeonUtils.getTier(player.getSkills().getMaxLevel(Skill.DEFENCE));
            if (defenceTier > 8)
                defenceTier = 8;
            player.getInventory().add(new Item(DungeonUtils.getPlatebody(defenceTier)));
            player.getInventory().add(new Item(DungeonUtils.getPlatelegs(defenceTier, player.getAppearance().getGender() == Gender.MALE)));
            if (party.getComplexity() <= 2) {
                int attackTier = DungeonUtils.getTier(player.getSkills().getMaxLevel(Skill.ATTACK));
                if (attackTier > 8)
                    attackTier = 8;
                player.getInventory().add(new Item(DungeonUtils.getRapier(attackTier)));
                player.getInventory().add(new Item(DungeonUtils.getBattleaxe(attackTier)));
            }
            int magicTier = DungeonUtils.getTier(player.getSkills().getMaxLevel(Skill.MAGIC));
            if (magicTier > 8)
                magicTier = 8;
            player.getInventory().add(new Item(DungeonUtils.getRobeTop(defenceTier < magicTier ? defenceTier : magicTier)));
            player.getInventory().add(new Item(DungeonUtils.getRobeBottom(defenceTier < magicTier ? defenceTier : magicTier)));
            if (party.getComplexity() <= 2) {
                player.getInventory().add(new Item(DungeonConstants.RUNES[0], 90 + Misc.random(30)));
                player.getInventory().add(new Item(DungeonUtils.getStartRunes(player.getSkills().getMaxLevel(Skill.MAGIC)), 90 + Misc.random(30)));
                player.getInventory().add(new Item(DungeonUtils.getElementalStaff(magicTier)));
            }
            int rangeTier = DungeonUtils.getTier(player.getSkills().getMaxLevel(Skill.RANGED));
            if (rangeTier > 8)
                rangeTier = 8;
            player.getInventory().add(new Item(DungeonUtils.getLeatherBody(defenceTier < rangeTier ? defenceTier : rangeTier)));
            player.getInventory().add(new Item(DungeonUtils.getChaps(defenceTier < rangeTier ? defenceTier : rangeTier)));
            if (party.getComplexity() <= 2) {
                player.getInventory().add(new Item(DungeonUtils.getShortbow(rangeTier)));
                player.getInventory().add(new Item(DungeonUtils.getArrows(rangeTier), 90 + Misc.random(30)));
            }
        }
    }

    public void sendSettings(Player player) {
        /*Effect previousOvlEffect = player.getEffects().getEffectForType(Effects.OVERLOAD);
        Effect nextOvlEffect = previousOvlEffect == null ? null : new Effect(Effects.OVERLOAD, previousOvlEffect.getCycle());
        Effect previousRenewalEffect = player.getEffects().getEffectForType(Effects.OVERLOAD);
        Effect nextRenewalEffect = previousRenewalEffect == null ? null : new Effect(Effects.OVERLOAD, previousRenewalEffect.getCycle());
        player.reset();
        if(nextOvlEffect != null)
            player.getEffects().startEffect(nextOvlEffect);
        if(nextRenewalEffect != null)
            player.getEffects().startEffect(nextRenewalEffect);
        if (player.getControlerManager().getControler() instanceof DungeonController)
            ((DungeonController) player.getControlerManager().getControler()).reset();
        else {
            player.getControlerManager().startControler("DungeonControler", DungeonManager.this);
            player.setLargeSceneView(true);
            player.getCombatDefinitions().setSpellBook(3);
            player.getToolbelt().switchDungeonneringToolbelt();
            setWorldMap(player, true);
        }*/
        player.getControllerManager().startController(new DungeonController(), DungeonManager.this);
        previousSpellbook.put(player.getUsername(), player.getSpellbook());
        player.setSpellBook(MagicSpellBook.DUNGEONEERING, false);
        player.getPacketSender().sendTabInterface(GameSettings.MAGIC_TAB, player.getSpellbook().getInterfaceId());
        sendRing(player, true);
        sendBindItems(player);
        wearInventory(player);

        player.getPrayer().reset();

        player.getSkills().reset();

        player.getSettings().set(Settings.RUN_ENERGY, 100);
    }

    public void rejoinParty(Player player) {
        player.stopAll();
        player.getMovement().lock(2);
        party.add(player);
        sendSettings(player);
        refreshKeys(player);
        player.moveTo(getHomeTile(), 0, Direction.SOUTH);
        playMusic(player, dungeon.getStartRoomReference());
    }

    public void sendBindItems(Player player) {
        Item ammo = player.getDungManager().getBindedAmmo();
        if (ammo != null)
            player.getInventory().add(ammo);
        for (Item item : player.getDungManager().getBindedItems().getItems()) {
            if (item == null)
                continue;
            player.getInventory().add(item);
        }
    }

    public void resetItems(Player player, Position position, boolean drop, boolean logout) {
        if (drop)
            dropItems(player, position);

        player.getEquipment().clear();
        player.getEquipment().refresh();
        player.getInventory().clear();
        player.getInventory().refresh();
        if (!logout) {
            player.getUpdateFlags().add(Flag.APPEARANCE);
            WeaponInterfaces.assign(player, player.getEquipment().get(Slot.WEAPON));
            WeaponAnimations.update(player);
            BonusManager.update(player);
        }
    }

    public void dropItem(Player player, Item item, Position position) {
        if (hasLoadedNoRewardScreen() && item.isEqual(DungeonConstants.GROUP_GATESTONE))
            setGroupGatestone(player.copyPosition());

        FloorItemManager.addGroundItem(item, position);
        if (item != null && item.getId() > 0 && item.getAmount() > 0) {
            if (ItemDegrading.degradesOnDrop(item.getId()))
                item.setId(ItemDegrading.getDegradedItemId(item.getId()));
        }
    }

    public void dropItems(Player player, Position position) {
        for (Item item : player.getEquipment().getItems()) {
            if (item == null || item.getId() == -1 || !item.tradeable())
                continue;

            FloorItemManager.addGroundItem(item, position);
        }
        for (Item item : player.getInventory().getItems()) {
            if (item == null || item.getId() <= 0 || !item.tradeable() || item.getAmount() <= 0)
                continue;

            dropItem(player, item, position);
        }
    }

    public void sendRing(Player player, boolean entrance) {
        Item ring = new Item(15707);
        if (player.getInventory().contains(ring))//deletes current normal ring
            player.getInventory().delete(ring);
        if (player.getEquipment().contains(ring))//deletes current normal ring
            player.getEquipment().delete(ring);
        if (entrance)
            player.getInventory().add(ring);
        else if (player.startedDungeoneeringWithRing()) {
            player.getInventory().add(ring);
            player.setDungeoneeringRing(false);
        }

        Item potato = new Item(5733);
        if (player.getInventory().contains(potato))
            player.getInventory().delete(potato);
        if (entrance && player.getTemporaryAttributes().get("CarryModPotato") != null)
            player.getInventory().add(potato);
        else if (player.getTemporaryAttributes().get("CarryModPotato") != null) {
            player.getInventory().add(potato);
            player.getTemporaryAttributes().remove("CarryModPotato");
        }
    }

    public void wearInventory(Player player) {
        boolean worn = false;
        for (Item item : player.getInventory().getItems()) {
            if (item == null)
                continue;
            //if (ButtonHandler.wear(player, item.getScrollId(), slotId, false))
            //worn = true;
        }
        if (worn) {
            player.getUpdateFlags().add(Flag.APPEARANCE);
            WeaponInterfaces.assign(player, player.getEquipment().get(Slot.WEAPON));
            WeaponAnimations.update(player);
            BonusManager.update(player);
            //player.getInventory().getItems().shift();
            player.getInventory().refresh();
        }

    }

    @SuppressWarnings("deprecation")
    public void spawnRandomNPCS(RoomReference reference) {
        int floorType = party.getFloorType();
        for (int i = 0; i < 3; i++) { //all floors creatures
            spawnNPC(reference, DungeonUtils.getGuardianCreature(), 2 + Misc.getRandom(13), 2 + Misc.getRandom(13), true, DungeonConstants.GUARDIAN_DOOR, 0.5);
        }

        for (int i = 0; i < 2; i++) { //this kind of floor creatures
            spawnNPC(reference, DungeonUtils.getGuardianCreature(floorType), 2 + Misc.getRandom(13), 2 + Misc.getRandom(13), true, DungeonConstants.GUARDIAN_DOOR, Misc.random(2) == 0 ? 0.8 : 1);
        }
        //forgotten warrior
        if (Misc.random(2) == 0)
            spawnNPC(reference, DungeonUtils.getForgottenWarrior(), 2 + Misc.getRandom(13), 2 + Misc.getRandom(13), true, DungeonConstants.FORGOTTEN_WARRIOR);
        //hunter creature
        spawnNPC(reference, DungeonUtils.getHunterCreature(), 2 + Misc.getRandom(13), 2 + Misc.getRandom(13), true, DungeonConstants.HUNTER_NPC, 0.5);
        if (Misc.random(5) == 0) //slayer creature
            spawnNPC(reference, DungeonUtils.getSlayerCreature(), 2 + Misc.getRandom(13), 2 + Misc.getRandom(13), true, DungeonConstants.SLAYER_NPC);
    }

    public int[] getRoomPos(Position tile) {
        int chunkX = tile.getX() / 16 * 2;
        int chunkY = tile.getY() / 16 * 2;
        int x = tile.getX() - chunkX * 8;
        int y = tile.getY() - chunkY * 8;
        Room room = getRoom(getCurrentRoomReference(tile));
        if (room == null)
            return null;
        return DungeonManager.translate(x, y, (4 - room.getRotation()) & 0x3, 1, 1, 0);
    }

    public static int[] translate(int x, int y, int mapRotation, int sizeX, int sizeY, int objectRotation) {
        int[] coords = new int[2];
        if ((objectRotation & 0x1) == 1) {
            int prevSizeX = sizeX;
            sizeX = sizeY;
            sizeY = prevSizeX;
        }
        if (mapRotation == 0) {
            coords[0] = x;
            coords[1] = y;
        } else if (mapRotation == 1) {
            coords[0] = y;
            coords[1] = 15 - x - (sizeX - 1);
        } else if (mapRotation == 2) {
            coords[0] = 15 - x - (sizeX - 1);
            coords[1] = 15 - y - (sizeY - 1);
        } else if (mapRotation == 3) {
            coords[0] = 15 - y - (sizeY - 1);
            coords[1] = x;
        }
        return coords;
    }

    public DungeonMob spawnNPC(RoomReference reference, int id, int x, int y) {
        return spawnNPC(reference, id, x, y, false, DungeonConstants.NORMAL_NPC);
    }

    public DungeonMob spawnNPC(RoomReference reference, int id, int x, int y, boolean check, int type) {
        return spawnNPC(reference, id, x, y, check, type, 1);
    }

    /*
     * worldX 0-15, worldY 0-15
     */

    public DungeonMob spawnNPC(final RoomReference reference, final int id, int x, int y, boolean check, final int type, final double multiplier) {
        final int rotation = dungeon.getRoom(reference).getRotation();
        NpcDefinition def = Loader.forId(id);
        final int size = def == null ? 1 : def.getSize();
        int[] coords = translate(x, y, rotation, size, size, 0);
        final Position tile = new Position(((boundChunks[0] * 8) + reference.getX() * 16) + coords[0], ((boundChunks[1] * 8) + reference.getY() * 16) + coords[1]);
        if (check && !World.isTileFree(tile.getX(), tile.getY(), 0, size))
            return null;
        return spawnNPC(id, rotation, tile, reference, type, multiplier);
    }

    public GameObject spawnObject(RoomReference reference, int id, int type, int rotation, int x, int y) {
        final int mapRotation = dungeon.getRoom(reference).getRotation();
        ObjectDefinition defs = Loader.forId(id, ((boundChunks[0] * 8) + reference.getX() * 16), ((boundChunks[1] * 8) + reference.getY() * 16));
        int[] coords = translate(x, y, mapRotation, defs.getSizeX(), defs.getSizeY(), rotation);
        GameObject object = new GameObject(id, new Position(((boundChunks[0] * 8) + reference.getX() * 16) + coords[0], ((boundChunks[1] * 8) + reference.getY() * 16) + coords[1]), type, (rotation + mapRotation) & 0x3);
        ObjectManager.spawnObject(object);
        return object;
    }

    public GameObject spawnObjectForMapRotation(RoomReference reference, int id, int type, int rotation, int x, int y, int mapRotation) {
        ObjectDefinition defs = Loader.forId(id, ((boundChunks[0] * 8) + reference.getX() * 16), ((boundChunks[1] * 8) + reference.getY() * 16));
        int[] coords = translate(x, y, mapRotation, defs.getSizeX(), defs.getSizeY(), rotation);
        GameObject object = new GameObject(id, new Position(((boundChunks[0] * 8) + reference.getX() * 16) + coords[0], ((boundChunks[1] * 8) + reference.getY() * 16) + coords[1]), type, (rotation + mapRotation) & 0x3);
        ObjectManager.spawnObject(object);
        return object;
    }

    public GameObject spawnObjectTemporary(RoomReference reference, int id, int type, int rotation, int x, int y, long time) {
        final int mapRotation = dungeon.getRoom(reference).getRotation();
        ObjectDefinition defs = Loader.forId(id, ((boundChunks[0] * 8) + reference.getX() * 16), ((boundChunks[1] * 8) + reference.getY() * 16));
        int[] coords = translate(x, y, mapRotation, defs.getSizeX(), defs.getSizeY(), rotation);
        GameObject object = new GameObject(id, new Position(((boundChunks[0] * 8) + reference.getX() * 16) + coords[0], ((boundChunks[1] * 8) + reference.getY() * 16) + coords[1]), type, (rotation + mapRotation) & 0x3);
        TempObjectManager.spawnObjectTemporary(object, time, false, true);
        return object;
    }

    public void removeObject(RoomReference reference, int id, int type, int rotation, int x, int y) {
        final int mapRotation = dungeon.getRoom(reference).getRotation();
        ObjectDefinition defs = Loader.forId(id, ((boundChunks[0] * 8) + reference.getX() * 16), ((boundChunks[1] * 8) + reference.getY() * 16));
        int[] coords = translate(x, y, mapRotation, defs.getSizeX(), defs.getSizeY(), rotation);
        ObjectManager.removeObject(new GameObject(id, new Position(((boundChunks[0] * 8) + reference.getX() * 16) + coords[0], ((boundChunks[1] * 8) + reference.getY() * 16) + coords[1]), type, (rotation + mapRotation) & 0x3));
    }

    public GameObject getObject(RoomReference reference, int id, int x, int y) {
        final int mapRotation = dungeon.getRoom(reference).getRotation();
        ObjectDefinition defs = Loader.forId(id, ((boundChunks[0] * 8) + reference.getX() * 16), ((boundChunks[1] * 8) + reference.getY() * 16));
        int[] coords = translate(x, y, mapRotation, defs.getSizeX(), defs.getSizeY(), 0);
        return ObjectManager.getObjectWithId(id, new Position(((boundChunks[0] * 8) + reference.getX() * 16) + coords[0], ((boundChunks[1] * 8) + reference.getY() * 16) + coords[1], 0));
    }

    public GameObject getObjectWithType(RoomReference reference, int id, int type, int x, int y) {
        final int mapRotation = dungeon.getRoom(reference).getRotation();
        ObjectDefinition defs = Loader.forId(id, ((boundChunks[0] * 8) + reference.getX() * 16), ((boundChunks[1] * 8) + reference.getY() * 16));
        int[] coords = translate(x, y, mapRotation, defs.getSizeX(), defs.getSizeY(), 0);
        return ObjectManager.getObjectWithType(new Position(((boundChunks[0] * 8) + reference.getX() * 16) + coords[0], ((boundChunks[1] * 8) + reference.getY() * 16) + coords[1], 0), type);
    }

    public GameObject getObjectWithType(RoomReference reference, int type, int x, int y) {
        Room room = dungeon.getRoom(reference);
        if (room == null)
            return null;
        final int mapRotation = room.getRotation();
        int[] coords = translate(x, y, mapRotation, 1, 1, 0);
        return ObjectManager.getObjectWithType(new Position(((boundChunks[0] * 8) + reference.getX() * 16) + coords[0], ((boundChunks[1] * 8) + reference.getY() * 16) + coords[1], 0), type);
    }

    public Position getTile(RoomReference reference, int x, int y, int sizeX, int sizeY) {
        Room room = dungeon.getRoom(reference);
        if (room == null)
            return null;
        final int mapRotation = room.getRotation();
        int[] coords = translate(x, y, mapRotation, sizeX, sizeY, 0);
        return new Position(((boundChunks[0] * 8) + reference.getX() * 16) + coords[0], ((boundChunks[1] * 8) + reference.getY() * 16) + coords[1], 0);
    }

    public Position getTile(RoomReference reference, int x, int y) {
        return getTile(reference, x, y, 1, 1);
    }

    public Position getRotatedTile(RoomReference reference, int x, int y) {
        final int mapRotation = dungeon.getRoom(reference).getRotation();
        int[] coords = translate(x, y, mapRotation, 1, 1, 0);
        return new Position(((boundChunks[0] * 8) + reference.getX() * 16) + coords[0], ((boundChunks[1] * 8) + reference.getY() * 16) + coords[1], 0);
    }

    public void spawnItem(RoomReference reference, Item item, int x, int y) {
        final int mapRotation = dungeon.getRoom(reference).getRotation();
        int[] coords = translate(x, y, mapRotation, 1, 1, 0);
        FloorItemManager.addGroundItem(item, new Position(((boundChunks[0] * 8) + reference.getX() * 16) + coords[0], ((boundChunks[1] * 8) + reference.getY() * 16) + coords[1]));
    }

    public boolean isFloorFree(RoomReference reference, int x, int y) {
        final int mapRotation = dungeon.getRoom(reference).getRotation();
        int[] coords = translate(x, y, mapRotation, 1, 1, 0);
        return World.isFloorFree(((boundChunks[0] * 8) + reference.getX() * 16) + coords[0], ((boundChunks[1] * 8) + reference.getY() * 16) + coords[1], 0);
    }

    public Position getRoomTile(RoomReference reference) {
        final int mapRotation = dungeon.getRoom(reference).getRotation();
        int[] coords = translate(0, 0, mapRotation, 1, 1, 0);
        return new Position(((boundChunks[0] * 8) + reference.getX() * 16) + coords[0], ((boundChunks[1] * 8) + reference.getY() * 16) + coords[1], 0);
    }

    public DungeonMob spawnNPC(int id, int rotation, Position tile, RoomReference reference, int type, double multiplier) {
        /*
        Unusable until 16k-24k or fixed anims
        10168
        10480
        10178

        Broken anims
        9898
        10128
        10143
        11872
        12865
        12752
         */
        DungeonMob n = null;
        if (type == DungeonConstants.BOSS_NPC) {
            if (id == 10040)
                n = new IcyBones(id, tile, this, reference);
            else if (id == 10058)
                n = new DivineSkinweaver(id, tile, this, reference);
            else if (id == 10630 || id == 10680 || id == 10693)
                n = new DungeonSkeletonBoss(id, tile, this, reference, multiplier);
            else if (id == 9767)
                n = new Rammernaut(id, tile, this, reference);
            else if (id == 11812)
                n = new RuneboundBehemoth(id, tile, this, reference);
            else if (id == 12848)
                n = new Dreadnaut(id, tile, this, reference);
            else
                n = new DungeonBoss(id, tile, this, reference);
        } else if (type == DungeonConstants.GUARDIAN_NPC) {
            n = new Guardian(id, tile, this, reference, multiplier);
            visibleMap[reference.getX()][reference.getY()].addGuardian(n);
        } else if (type == DungeonConstants.FORGOTTEN_WARRIOR) {
            n = new ForgottenWarrior(id, tile, this, reference, multiplier);
            visibleMap[reference.getX()][reference.getY()].addGuardian(n);
        } else if (type == DungeonConstants.FISH_SPOT_NPC)
            n = new DungeonFishSpot(id, tile, this, DungeoneeringFishing.Fish.values()[Misc.random(DungeoneeringFishing.Fish.values().length - 1)]);
        else if (type == DungeonConstants.SLAYER_NPC)
            n = new DungeonSlayerMob(id, tile, this, multiplier);
        else if (type == DungeonConstants.HUNTER_NPC)
            n = new DungeonHunterMob(id, tile, this, multiplier);
        else {
            n = new DungeonMob(id, tile, this, multiplier, id > Loader.getTotalNpcs(Revision.RS2) ? Revision.PRE_RS3 : Revision.RS2); // remove revision argument once converted to Kotlin
            if (!DungeonConstants.isDungeoneeringCreature(id))
                n.getMovement().lock();
        }
        n.getDirection().setDirection(Direction.fromDeltas(Misc.ROTATION_DIR_X[(rotation + 3) & 0x3], Misc.ROTATION_DIR_Y[(rotation + 3) & 0x3]));
        return n;
    }

    public int getTargetLevel(int id, boolean boss, double multiplier) {
        double lvl = boss ? party.getCombatLevel() : party.getAverageCombatLevel();
        int randomize = party.getComplexity() * 2 * party.getTeam().size();
        lvl -= randomize;
        lvl += Misc.random(randomize * 2);
        lvl *= party.getDificultyRatio();
        lvl *= multiplier;
        lvl *= 1D - ((6D - party.getComplexity()) * 0.07D);
        if (party.getTeam().size() > 2 && id != 12752 && id != 11872 && id != 11708 && id != 12865) //blink
            lvl *= 0.7;
        return (int) (lvl < 1 ? 1 : lvl);
    }

    private int RANGE_ATTACK = 0, STAB_ATTACK = 1, MAGIC_ATTACK = 2, STAB_DEF = 3, CRUSH_DEF = 4, SLASH_DEF = 5, RANGE_DEF = 6, MAGIC_DEF = 7;

    public int[] getBonuses(boolean boss, int level) {
        int[] bonuses = new int[10];

        // less 50% than defence
        bonuses[RANGE_ATTACK] = party.getRangeLevel() + (level / 3);
        bonuses[STAB_ATTACK] = (int) (party.getAttackLevel() + (level / 3) / 1.2);
        bonuses[MAGIC_ATTACK] = (int) (party.getAttackLevel() + (level / 3) / 1.5);
        int npcDefenceLevel = /*party.getDefenceLevel()*/party.getAverageCombatLevel() / (boss ? 1 : 2);
        //bonuses[CombatDefinitions.RANGE_DEF] = npcDefenceLevel * + (level / (boss ? 2 : 3));
        bonuses[STAB_DEF] = bonuses[CRUSH_DEF] = bonuses[SLASH_DEF] = (int) ((npcDefenceLevel + (level / (boss ? 2 : 3))) / 1.2); // 20%

        //usualy range def > melee def for mobs but bows in dung suck, and even range gear doesnt help that much
        bonuses[RANGE_DEF] = (int) ((npcDefenceLevel + (level / (boss ? 2 : 3))) / 1.6); // 60% nerf less than range
        bonuses[MAGIC_DEF] = (int) ((npcDefenceLevel + (level / (boss ? 2 : 3))) / 1.5); // 50% less than range
        return bonuses;
    }

    public void updateGuardian(RoomReference reference) {
        if (visibleMap[reference.getX()][reference.getY()].removeGuardians()) {
            getRoom(reference).removeGuardianDoors();
            /*for (Player player : party.getTeam()) {
                RoomReference playerReference = getCurrentRoomReference(player);
                if (playerReference.getX() == reference.getX() && playerReference.getY() == reference.getY())
                    playMusic(player, reference);
            }*/
        }
    }

    public void exitDungeon(Player player, final boolean logout, boolean animate) {
        Position position = player.copyPosition();
        player.getControllerManager().removeControllerWithoutCheck();
        player.getControllerManager().startController(new Daemonheim());

        player.getPrayer().closeAllPrayers();

        resetItems(player, position, true, logout);

        party.remove(player, logout);
        player.stopAll();

        player.getMovement().reset();
        player.getPacketSender().sendInterfaceRemoval();

        resetTraps(player);
        sendRing(player, false);

        player.getInventory().refresh();

        player.getSkills().reset();

        player.getSettings().set(Settings.RUN_ENERGY, 100);

        if (player.getFamiliar() != null)
            player.getFamiliar().sendDeath(player);

        if (previousSpellbook == null || previousSpellbook.get(player.getUsername()) == null) {
            player.setSpellBook(MagicSpellBook.NORMAL, false);
        } else {
            player.setSpellBook(previousSpellbook.get(player.getUsername()) == MagicSpellBook.DUNGEONEERING ? MagicSpellBook.NORMAL : previousSpellbook.get(player.getUsername()), false);
        }

        if (logout)
            player.moveTo(new Position(3460 + Misc.random(2), 3720 + Misc.random(2), 1));
        else {
            player.getDungManager().setRejoinKey(null);

//            if (animate)
//                player.animate(828);
            player.moveTo(new Position(3460 + Misc.random(2), 3720 + Misc.random(2), 1), 0, Direction.NORTH);
            player.getPacketSender().sendTabInterface(GameSettings.MAGIC_TAB, player.getSpellbook().getInterfaceId());
            setWorldMap(player, false);
            removeMark(player);
//            player.setLargeSceneView(false);
//            player.getInterfaceManager().removeMinigameInterface();
//            player.getMusicsManager().reset();
//            player.getAppearence().setRenderEmote(-1);
        }

    }

    public void setWorldMap(Player player, boolean dungIcon) {
        //player.getVarsManager().sendVarBit(11297, dungIcon ? 1 : 0);
    }

    public void endFarming() {
        //for (String key : farmKeyList)
        //  PrivateObjectManager.cancel(key);
        farmKeyList.clear();
    }

    private void resetTraps(Player player) {
        for (MastyxTrap trap : mastyxTraps) {
            if (!trap.getPlayerName().equals(player.getUsername()))
                continue;
            trap.deregister();
        }
    }

    public void endMastyxTraps() {
        for (MastyxTrap trap : mastyxTraps) {
            trap.deregister();
        }
        mastyxTraps.clear();
    }

    public void removeDungeon() {
        cachedDungeons.remove(key);
    }

    public void destroy() {
        synchronized (this) {
            if (isDestroyed()) //to prevent issues when shutting down forcing
                return;
            final int[] oldBoundChunks = boundChunks;
            endRewardsTimer();
            endDestroyTimer();
            endFarming();
            endMastyxTraps();
            endShops();
            removeDungeon();
            partyDeaths.clear();
            for (int x = 0; x < visibleMap.length; x++) {
                for (int y = 0; y < visibleMap[x].length; y++) {
                    if (visibleMap[x][y] != null) {
                        visibleMap[x][y].destroy();
                    }
                }
            }
            final Dungeon oldDungeon = dungeon;
            dungeon = null;
            GameExecutorManager.slowExecutor.schedule(() -> {
                try {
                    MapBuilder.destroyMap(oldBoundChunks[0], oldBoundChunks[1], (oldDungeon.getMapWidth() * 2), (oldDungeon.getMapHeight() * 2));
                } catch (Exception e) {
                    System.err.println("Dungeon destruction error: " + Arrays.toString(oldBoundChunks) + " " + Arrays.toString(boundChunks) + " " + oldDungeon);
                }
            }, 5, TimeUnit.SECONDS);
        }
    }

    private void endShops() {
        if (party.getShop() > 0)
            ShopManager.getShops().remove(party.getShop());
        party.setShop(-1);
    }

    public void nextFloor() {
        for (Player member : party.getTeam())
            member.getDungManager().leaveParty();
        /*int maxFloor = DungeonUtils.getMaxFloor(party.getDungeoneeringLevel());
        if (party.getMaxFloor() > party.getFloor())
            party.setFloor(party.getFloor() + 1);
        if (tutorialMode) {
            int complexity = party.getComplexity();
            if (party.getMaxComplexity() > complexity)
                party.setComplexity(complexity + 1);
        }
        destroy();
        load();*/
    }

    public Integer[] getAchievements(Player player) {
        List<Integer> achievements = new ArrayList<>();

        DungeonController controller = (DungeonController) player.getControllerManager().getController();

        //solo achievements
        if (controller.isKilledBossWithLessThan10HP())
            achievements.add(6);
        if (controller.getDeaths(player) == 8)
            achievements.add(8);
        else if (controller.getDeaths(player) == 0)
            achievements.add(14);
        if (controller.getDamage() != 0 && controller.getDamageReceived() == 0)
            achievements.add(25);

        if (party.getTeam().size() > 1) { //party achievements
            boolean mostMeleeDmg = true;
            boolean mostMageDmg = true;
            boolean mostRangeDmg = true;
            boolean leastDamage = true;
            boolean mostDmgReceived = true;
            boolean mostDeaths = true;
            boolean mostHealedDmg = true;
            for (Player teamMate : party.getTeam()) {
                if (teamMate == player)
                    continue;
                DungeonController tmController = (DungeonController) teamMate.getControllerManager().getController();
                if (tmController.getMeleeDamage() >= controller.getMeleeDamage())
                    mostMeleeDmg = false;
                if (tmController.getMageDamage() >= controller.getMageDamage())
                    mostMageDmg = false;
                if (tmController.getRangeDamage() >= controller.getRangeDamage())
                    mostRangeDmg = false;
                if (controller.getDamage() >= tmController.getDamage())
                    leastDamage = false;
                if (controller.getDamageReceived() <= tmController.getDamageReceived())
                    mostDmgReceived = false;
                if (controller.getDeaths(player) <= tmController.getDeaths(teamMate))
                    mostDeaths = false;
                if (controller.getHealedDamage() <= tmController.getHealedDamage())
                    mostHealedDmg = false;
            }
            if (mostMeleeDmg && mostMageDmg && mostRangeDmg)
                achievements.add(1);
            if (leastDamage && mostDeaths) //leecher
                achievements.add(2);
            if (mostMeleeDmg)
                achievements.add(3);
            if (mostRangeDmg)
                achievements.add(4);
            if (mostMageDmg)
                achievements.add(5);
            if (leastDamage)
                achievements.add(7);
            if (mostDmgReceived)
                achievements.add(13);
            if (mostDeaths)
                achievements.add(15);
            if (mostHealedDmg)
                achievements.add(38);
        }
        if (achievements.size() == 0)
            achievements.add(33);
        return achievements.toArray(new Integer[achievements.size()]);

    }

    public void loadRewards() {
        stage = 2;
        for (Player player : party.getTeam()) {
            player.stopAll();
            double multiplier = 1;
            //player.getPacketSender().sendInterface(933);
            //player.getPacketSender().sendExecuteScriptReverse(2275); //clears interface data from last run
            for (int i = 0; i < 5; i++) {
                Player partyPlayer = i >= party.getTeam().size() ? null : party.getTeam().get(i);
                //player.getPacketSender().sendCSVarInteger(1198 + i, partyPlayer != null ? 1 : 0); //sets true that this player exists
                if (partyPlayer == null)
                    continue;
                //player.getPacketSender().sendCSVarString(2383 + i, partyPlayer.getDisplayName());
                Integer[] achievements = getAchievements(partyPlayer);
                //for (int i2 = 0; i2 < (achievements.length > 6 ? 6 : achievements.length); i2++)
                //player.getPacketSender().sendCSVarInteger(1203 + (i * 6) + i2, achievements[i2]);
            }
            //player.getPacketSender().sendIComponentText(933, 26, Misc.formatTime((Misc.currentWorldCycle() - time) * 600));
            //player.getPacketSender().sendCSVarInteger(1187, party.getFloor());
            //player.getPacketSender().sendCSVarInteger(1188, party.getSize() + 1); //dungeon size, sets bonus aswell
            multiplier += DungeonConstants.DUNGEON_SIZE_BONUS[party.getSize()];
            //player.getPacketSender().sendCSVarInteger(1191, party.getTeam().size() * 10 + party.getDifficulty()); //teamsize:dificulty
            multiplier += DungeonConstants.DUNGEON_DIFFICULTY_RATIO_BONUS[party.getTeam().size() - 1][party.getDifficulty() - 1];
            int v = 0;
            if (getVisibleBonusRoomsCount() != 0) { //no bonus rooms in c1, would be divide by 0
                v = getVisibleBonusRoomsCount() * 10000 / (dungeon.getRoomsCount() - dungeon.getCritCount());
            }
            //player.getPacketSender().sendCSVarInteger(1195, v); //dungeons rooms opened, sets bonus aswell
            multiplier += DungeonConstants.MAX_BONUS_ROOM * v / 10000;
            v = (getLevelModPerc() * 20) - 1000;
            //player.getPacketSender().sendCSVarInteger(1236, v); //sets level mod
            multiplier += ((double) v) / 10000;
            //player.getPacketSender().sendCSVarInteger(1196, party.isGuideMode() ? 1 : 0); //sets guidemode
            if (party.isGuideMode())
                multiplier -= 0.05;
            //player.getPacketSender().sendCSVarInteger(1319, DungeonUtils.getMaxFloor(player.getSkills().getMaxLevel(Skill.DUNGEONEERING)));
            //player.getPacketSender().sendCSVarInteger(1320, party.getComplexity());
            if (party.getComplexity() != 6)
                multiplier -= (DungeonConstants.COMPLEXIYY_PENALTY_BASE[party.getSize()] + (5 - party.getComplexity()) * 0.06);
            //player.getPacketSender().sendCSVarInteger(1321, (int) (levelDiffPenalty * 10000));
            multiplier -= party.getLevelDifferencePenalty(player);
            //double countedDeaths = Math.min(player.getVarsManager().getBitValue(7554), 6);
            //multiplier *= (1.0 - (countedDeaths * 0.1)); //adds FLAT 10% reduction per death, upto 6
            //base xp is based on a ton of factors, including opened rooms, resources harvested, ... but this is most imporant one
            double floorXP = getXPForFloor(party.getFloor(), party.getSize()) * getVisibleRoomsCount() / dungeon.getRoomsCount();
            boolean tickedOff = player.getDungManager().isTickedOff(party.getFloor());
            if (!tickedOff)
                player.getDungManager().tickOff(party.getFloor());
            else {
                int[] range = DungeonUtils.getFloorThemeRange(party.getFloor());
                for (int floor = range[0]; floor <= range[1]; floor++) {
                    if (player.getDungManager().getMaxFloor() < floor)
                        break;
                    if (!player.getDungManager().isTickedOff(floor)) {
                        player.message("Since you have previously completed this floor, floor " + floor + " was instead ticked-off.");
                        player.getDungManager().tickOff(floor);
                        floorXP = getXPForFloor(floor, party.getSize()) * getVisibleRoomsCount() / dungeon.getRoomsCount();
                        tickedOff = false;
                        break;
                    }
                }
            }
            double prestigeXP = tickedOff ? 0 : getXPForFloor(player.getDungManager().getPrestige(), party.getSize()) * getVisibleRoomsCount() / dungeon.getRoomsCount();
            double averageXP = (floorXP + prestigeXP) / 2;
            if (party.getTeam().size() == 1 && party.getSize() != DungeonConstants.LARGE_DUNGEON)
                averageXP *= 1.5;
            multiplier = Math.max(0.1, multiplier);
            double totalXp = (averageXP * multiplier) / 10;
            double realXP = totalXp * player.getGameMode().getSkillRate() * (GameSettings.BONUS_EXP ? 1.50 : 1);
            int tokens = (int) ((realXP) / 10);

            if (party.getComplexity() == 1)
                Achievements.finishAchievement(player, Achievements.AchievementData.COMPLETE_A_C1_DUNG);
            else if (DungeonUtils.getFloorType(party.getFloor()) == DungeonConstants.WARPED_FLOORS && party.getComplexity() == 6)
                Achievements.doProgress(player, Achievements.AchievementData.COMPLETE_5_C6_WARPED_DUNGEONS);
            if (getPartyDeaths().get(player.getUsername()) == null)
                Achievements.finishAchievement(player, Achievements.AchievementData.COMPLETE_A_DUNGEON_WITHOUT_DEATH);
            Achievements.doProgress(player, Achievements.AchievementData.COMPLETE_100_DUNGEONS);
            
            player.getSkills().addExperience(Skill.DUNGEONEERING, totalXp);
            player.getDungManager().addTokens(tokens);
            player.getPointsHandler().refreshPanel();
            //player.getMusicsManager().forcePlayMusic(770);
            if (!tickedOff) {
                if (DungeonUtils.getMaxFloor(player.getSkills().getMaxLevel(Skill.DUNGEONEERING)) < party.getFloor() + 1)
                    player.message("The next floor is not available at your Dungeoneering level. Consider resetting your progress to gain best ongoing rate of xp.");
            } else {
                player.message("Warning" + FontUtils.BLACK + ": You have already completed all the available floors of this theme" + FontUtils.COL_END, 0xd80000);
                player.message("and thus cannot be awarded prestige xp until you reset your progress or switch theme.");
            }
            //player.message("Pre-Share: <col=641d9e>" + (isKeyShare() ? "OFF" : "ON"));
            if (party.getFloor() == player.getDungManager().getMaxFloor() && party.getFloor() < DungeonUtils.getMaxFloor(player.getSkills().getMaxLevel(Skill.DUNGEONEERING)))
                player.getDungManager().increaseMaxFloor();
            if (player.getDungManager().getMaxComplexity() < 6)
                player.getDungManager().increaseMaxComplexity();
            if (player.getFamiliar() != null)
                player.getFamiliar().sendDeath(player);
        }
        clearGuardians();
    }

    public static int getXPForFloor(int floor, int type) {
        int points = 0;
        for (int i = 1; i <= floor; i++)
            points += Math.floor(i + 100.0 * Math.pow(1.3, i / 10));
        if (type == DungeonConstants.MEDIUM_DUNGEON)
            points *= 5;
        else if (type == DungeonConstants.LARGE_DUNGEON)
            points *= 10;
        return points * DungeonConstants.XP_RATE;
    }

    public void voteToMoveOn(Player player) {
        if (rewardsTimer == null)
            setRewardsTimer();
        rewardsTimer.increaseReadyCount();
    }

    public void ready(Player player) {
        int index = party.getIndex(player);
        rewardsTimer.increaseReadyCount();
        //for (Player p2 : party.getTeam())
        //p2.getPacketSender().sendCSVarInteger(1397 + index, 1);
    }

    public DungeonPartyManager getParty() {
        return party;
    }

    public void setRewardsTimer() {
        GameExecutorManager.fastExecutor.getExecutor().schedule(rewardsTimer = new RewardsTimer(), 1000, 5000);
    }

    public void setDestroyTimer() {
        //cant be already instanced before anyway, after all only instances if party is 0 and removes if party not 0
        GameExecutorManager.fastExecutor.getExecutor().schedule(destroyTimer = new DestroyTimer(), 1000, 5000);
    }

    public void setMark(Figure target, boolean mark) {
        if (mark) {
            for (Player player : party.getTeam())
                player.getPacketSender().sendEntityHint(target);
        } else
            removeMark();
        if (target instanceof DungeonMob)
            ((DungeonMob) target).setMarked(mark);
    }

    public void setGroupGatestone(Position groupGatestone) {
        this.groupGatestone = groupGatestone;
    }

    public Position getGroupGatestone() {
        if (groupGatestone == null) {
            Player player = party.getGateStonePlayer();
            if (player != null)
                return player;
        }
        return groupGatestone;
    }

    public void resetGatestone() {
        groupGatestone = null;
    }

    public void removeMark() {
        for (Player player : party.getTeam())
            removeMark(player);
    }

    public void removeMark(Player player) {
        player.getPacketSender().sendEntityHintRemoval(true);
        //player.getHintIconsManager().removeHintIcon(6);
    }

    public void endDestroyTimer() {
        if (destroyTimer != null) {
            destroyTimer.cancel();
            destroyTimer = null;
        }
    }

    public void endRewardsTimer() {
        if (rewardsTimer != null) {
            rewardsTimer.cancel();
            rewardsTimer = null;
        }
    }

    private class DestroyTimer extends TimerTask {
        private long timeLeft;

        public DestroyTimer() {
            timeLeft = 600; //10min
        }

        @Override
        public void run() {
            try {
                if (timeLeft > 0) {
                    timeLeft -= 5;
                    return;
                }
                destroy();
                cancel();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

    }

    private class RewardsTimer extends TimerTask {

        private long timeLeft;
        private boolean gaveRewards;

        public RewardsTimer() {
            timeLeft = party.getTeam().size() * 15;
        }

        public void increaseReadyCount() {
            int reduce = (int) (gaveRewards ? ((double) 45 / (double) party.getTeam().size()) : 60);
            timeLeft = timeLeft > reduce ? timeLeft - reduce : 0;
        }

        @Override
        public void run() {
            try {
                if (party.getTeam().size() == 1)
                    timeLeft = 0;
                if (timeLeft > 0) {
                    for (Player player : party.getTeam())
                        player.message(/*gaveRewards ? ("Time until next dungeon: " + timeLeft) :*/ (timeLeft + " seconds until dungeon ends."));
                    timeLeft -= 5;
                } else {
                    gaveRewards = true;
                    //timeLeft = 45;
                    loadRewards();
                    nextFloor();
                    cancel();
                }
            } catch (Throwable e) {
                Logger.handle(e);
            }
        }
    }

    public void setDungeon() {
        key = party.getLeader() + "_" + keyMaker.getAndIncrement();
        cachedDungeons.put(key, this);
        for (Player player : party.getTeam())
            player.getDungManager().setRejoinKey(key);
    }

    public static void checkRejoin(Player player) {
        Object key = player.getDungManager().getRejoinKey();
        if (key == null)
            return;
        DungeonManager dungeon = cachedDungeons.get(key);
        //either doesn't exit / ur m8s moving next floor(reward screen)
        if (dungeon == null || !dungeon.hasLoadedNoRewardScreen()) {
            player.getDungManager().setRejoinKey(null);
            return;
        }
        dungeon.rejoinParty(player);
    }

    public void load() {
        party.lockParty();
        visibleMap = new VisibleRoom[DungeonConstants.DUNGEON_RATIO[party.getSize()][0]][DungeonConstants.DUNGEON_RATIO[party.getSize()][1]];
        // slow executor loads dungeon as it may take up to few secs
        GameExecutorManager.slowExecutor.execute(() -> {
            clearKeyList();
            // generates dungeon structure
            dungeon = new Dungeon(DungeonManager.this, party.getFloor(), party.getComplexity(), party.getSize());
            if (!dungeon.isReady()) {
                for (Player member : party.getTeam()) {
                    member.getMovement().unlock();
                    member.getDungManager().finish();
                }
                return;
            }
            //time = Misc.currentWorldCycle();
            // finds an empty map area bounds
            boundChunks = MapBuilder.findEmptyChunkBound(dungeon.getMapWidth() * 2, (dungeon.getMapHeight() * 2));

            if (boundChunks == null)
                System.err.println("Error finding dungeoneering chunk region!");


            //reserves all map area
            //MapBuilder.cutMap(boundChunks[0], boundChunks[1], dungeon.getMapWidth() * 2, (dungeon.getMapHeight() * 2), 0);
            //set dungeon
            setDungeon();
            // loads start room
            loadRoom(dungeon.getStartRoomReference());
            stage = 1;
        });
    }

    public boolean hasStarted() {
        return stage != 0;
    }

    public boolean isAtRewardsScreen() {
        return stage == 2;
    }

    public boolean hasLoadedNoRewardScreen() {
        return stage == 1;
    }

    public void openMap(Player player) {
        if (party.getSize() == DungeonConstants.TEST_DUNGEON) {
            player.message("Dungeon is too big to be displayed.");
            return;
        }
        //player.getInterfaceManager().sendCentralInterface(942);
        //player.getPacketSender().sendExecuteScriptReverse(3277); //clear the map if theres any setted
        int protocol = party.getSize() == DungeonConstants.LARGE_DUNGEON ? 0 : party.getSize() == DungeonConstants.MEDIUM_DUNGEON ? 2 : 1;
        for (int x = 0; x < visibleMap.length; x++) {
            for (int y = 0; y < visibleMap[x].length; y++) {
                if (visibleMap[x][y] != null) { //means exists
                    Room room = getRoom(new RoomReference(x, y));
                    boolean highLight = party.isGuideMode() && room.isCritPath();
                    //player.getPacketSender().sendExecuteScriptReverse(3278, protocol, getMapIconSprite(room, highLight), worldY, worldX);
                    /*if (room.getRoom() instanceof StartRoom)
                        player.getPacketSender().sendExecuteScriptReverse(3280, protocol, worldY, worldX);
                    else if (room.getRoom() instanceof BossRoom)
                        player.getPacketSender().sendExecuteScriptReverse(3281, protocol, worldY, worldX);
                    if (room.hasNorthDoor() && visibleMap[worldX][worldY + 1] == null) {
                        Room unknownR = getRoom(new RoomReference(worldX, worldY + 1));
                        highLight = party.isGuideMode() && unknownR.isCritPath();
                        player.getPacketSender().sendExecuteScriptReverse(3278, protocol, getMapIconSprite(DungeonConstants.SOUTH_DOOR, highLight), worldY + 1, worldX);
                    }
                    if (room.hasSouthDoor() && visibleMap[worldX][worldY - 1] == null) {
                        Room unknownR = getRoom(new RoomReference(worldX, worldY - 1));
                        highLight = party.isGuideMode() && unknownR.isCritPath();
                        player.getPacketSender().sendExecuteScriptReverse(3278, protocol, getMapIconSprite(DungeonConstants.NORTH_DOOR, highLight), worldY - 1, worldX);
                    }
                    if (room.hasEastDoor() && visibleMap[worldX + 1][worldY] == null) {
                        Room unknownR = getRoom(new RoomReference(worldX + 1, worldY));
                        highLight = party.isGuideMode() && unknownR.isCritPath();
                        player.getPacketSender().sendExecuteScriptReverse(3278, protocol, getMapIconSprite(DungeonConstants.WEST_DOOR, highLight), worldY, worldX + 1);
                    }
                    if (room.hasWestDoor() && visibleMap[worldX - 1][worldY] == null) {
                        Room unknownR = getRoom(new RoomReference(worldX - 1, worldY));
                        highLight = party.isGuideMode() && unknownR.isCritPath();
                        player.getPacketSender().sendExecuteScriptReverse(3278, protocol, getMapIconSprite(DungeonConstants.EAST_DOOR, highLight), worldY, worldX - 1);
                    }*/
                }
            }
        }
        int index = 1;
        for (Player p2 : party.getTeam()) {
            // RoomReference reference = getCurrentRoomReference(p2);
            //player.getPacketSender().sendExecuteScriptReverse(3279, p2.getDisplayName(), protocol, index++, reference.getChunkY(), reference.getChunkX());
        }
    }

    public int getMapIconSprite(int direction, boolean highLight) {
        /*for (MapRoomIcon icon : MapRoomIcon.values()) {
            if (icon.isOpen())
                continue;
            if (icon.hasDoor(direction))
                return icon.getSpriteId() + (highLight ? MapRoomIcon.values().length : 0);
        }*/
        return 2879;
    }

    public int getMapIconSprite(Room room, boolean highLight) {
        /*for (MapRoomIcon icon : MapRoomIcon.values()) {
            if (!icon.isOpen())
                continue;
            if (icon.hasNorthDoor() == room.hasNorthDoor() && icon.hasSouthDoor() == room.hasSouthDoor() && icon.hasWestDoor() == room.hasWestDoor() && icon.hasEastDoor() == room.hasEastDoor())
                return icon.getSpriteId() + (highLight ? MapRoomIcon.values().length : 0);
        }*/
        return 2878;
    }

    public void openStairs(RoomReference reference) {
        Room room = getRoom(reference);
        int type = 0;

        if (room.getRoom().getChunkX() == 26 && room.getRoom().getChunkY() == 640) //unholy cursed
            type = 1;
        else if (room.getRoom().getChunkX() == 30 && room.getRoom().getChunkY() == 656) //stomp
            type = 2;
        else if ((room.getRoom().getChunkX() == 30 && room.getRoom().getChunkY() == 672) || (room.getRoom().getChunkX() == 24 && room.getRoom().getChunkY() == 690)) //necromancer)
            type = 3;
        else if (room.getRoom().getChunkX() == 26 && room.getRoom().getChunkY() == 690) //world-gorger
            type = 4;
        else if (room.getRoom().getChunkX() == 24 && room.getRoom().getChunkY() == 688) //blink
            type = 5;
        spawnObject(reference, DungeonConstants.LADDERS[party.getFloorType()], 10, (type == 2 || type == 3) ? 0 : 3, type == 4 ? 11 : type == 3 ? 15 : type == 2 ? 14 : 7, type == 5 ? 14 : (type == 3 || type == 2) ? 3 : type == 1 ? 11 : 15);
        //getVisibleRoom(reference).setNoMusic();
        for (Player player : party.getTeam()) {
            if (!isAtBossRoom(player))
                continue;
            //player.getPacketSender().sendMusicEffectOld(415);
            //playMusic(player, reference);
        }
    }

    public List<String> getFarmKeyList() {
        return farmKeyList;
    }

    public void addMastyxTrap(MastyxTrap mastyxTrap) {
        mastyxTraps.add(mastyxTrap);
    }

    public List<MastyxTrap> getMastyxTraps() {
        return mastyxTraps;
    }

    public void removeMastyxTrap(MastyxTrap mastyxTrap) {
        mastyxTraps.remove(mastyxTrap);
        mastyxTrap.deregister();
    }

    public void message(RoomReference reference, String message) {
        for (Player player : party.getTeam()) {
            if (reference.equals(getCurrentRoomReference(player))) {
                player.message(message);
            }
        }
    }

    public void showBar(RoomReference reference, String name, int percentage) {
        for (Player player : party.getTeam()) {
            RoomReference current = getCurrentRoomReference(player);
            if (reference.getX() == current.getX() && reference.getY() == current.getY() && player.getControllerManager().getController() instanceof DungeonController) {
                DungeonController c = (DungeonController) player.getControllerManager().getController();
                c.showBar(true, name);
                c.sendBarPercentage(percentage);
            }
        }
    }

    public void hideBar(RoomReference reference) {
        for (Player player : party.getTeam()) {
            RoomReference current = getCurrentRoomReference(player);
            if (reference.getX() == current.getX() && reference.getY() == current.getY() && player.getControllerManager().getController() instanceof DungeonController) {
                DungeonController c = (DungeonController) player.getControllerManager().getController();
                c.showBar(false, null);
            }
        }
    }

    public Map<String, Integer> getPartyDeaths() {
        return partyDeaths;
    }

    /*
     * Use forId npc instead
     * this being used because gravecreeper gets removed when using special :/
     */
    @Deprecated
    public DungeonBoss getTemporaryBoss() {
        return temporaryBoss;
    }

    public void setTemporaryBoss(DungeonBoss temporaryBoss) {
        this.temporaryBoss = temporaryBoss;
    }

    public void openAllRooms() {
        for (int x = 0; x < visibleMap.length; x++) {
            for (int y = 0; y < visibleMap[1].length; y++) {
                if (x == dungeon.getStartRoomReference().getX() && y == dungeon.getStartRoomReference().getY())
                    continue;
                loadRoom(x, y);
            }
        }
    }
}
