package com.fury.game.content.skill.member.construction;

import com.fury.cache.def.Loader;
import com.fury.cache.def.object.ObjectDefinition;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.engine.task.executor.GameExecutorManager;
import com.fury.game.GameSettings;
import com.fury.game.container.impl.equip.Equipment;
import com.fury.game.content.controller.Controller;
import com.fury.game.content.controller.impl.HouseController;
import com.fury.game.content.dialogue.impl.misc.SimpleMessageD;
import com.fury.game.content.dialogue.impl.skills.construction.*;
import com.fury.game.content.misc.items.StrangeRocks;
import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.member.construction.HouseConstants.*;
import com.fury.game.entity.character.combat.effects.Effects;
import com.fury.game.entity.character.npc.impl.construction.Guard;
import com.fury.game.entity.character.npc.impl.construction.ServantMob;
import com.fury.game.entity.character.player.info.DonorStatus;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.core.model.item.Item;
import com.fury.game.entity.object.GameObject;
import com.fury.game.node.entity.actor.figure.player.handles.Settings;
import com.fury.game.node.entity.actor.object.ObjectHandler;
import com.fury.game.node.entity.actor.object.ObjectManager;
import com.fury.game.world.GameWorld;
import com.fury.game.world.World;
import com.fury.game.world.map.Position;
import com.fury.game.world.map.build.DynamicRegion;
import com.fury.game.world.map.build.MapBuilder;
import com.fury.game.world.map.build.MapUtils;
import com.fury.game.world.map.region.Region;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.util.Misc;
import com.fury.util.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/*
 * House class only contains house data + support methods to change that data
 * HouseController provides support between player interaction inside house and housemanager
 * HouseConstants handles the constants such as existing rooms, builds, roofs
 */
public class House implements Serializable {

    public static int LOGGED_OUT = 0, KICKED = 1, TELEPORTED = 2;
    private static final long serialVersionUID = 8111719490432901786L;

    //dont name it rooms or it will null server
    private List<RoomReference> roomsR;

    public int getAmountOfRooms() {
        return roomsR.size();
    }

    private byte look;
    private HouseConstants.POHLocation location;
    private Servant servant;
    private boolean buildMode;
    private boolean arriveInPortal;
    private boolean doorsOpen;
    private byte paymentStage;

    private transient Player player;
    private transient boolean locked;
    private transient int challengeMode; //0 disabled,  1 - challenge method, 2 - pvp challenge method
    private transient int burnerCount;

    //house loaded datas
    private transient List<Player> players;
    private transient int[] boundChuncks;
    private transient boolean loaded;
    private transient ServantMob servantInstance;
    private transient List<GameObject> dungeonTraps;

    private byte build;

    private boolean isOwnerInside() {
        return players.contains(player);
    }

    public void expelGuests() {
        if (!isOwnerInside()) {
            player.message("You can only expel guests when you are in your own house.");
            return;
        }
        kickGuests();
    }

    public void kickGuests() {
        if (players == null) //still initing i guess
            return;
        for (Player player : new ArrayList<>(players)) {
            if (isOwner(player))
                continue;
            leaveHouse(player, KICKED);
        }
    }

    public boolean isOwner(Player player) {
        return this.player == player;
    }

    public boolean containsPlayer(Player player) {
        return players.contains(player);
    }

    public void enterMyHouse() {
        joinHouse(player);
    }

    public void openRoomCreationMenu(GameObject door) {
        int roomX = player.getChunkX() - boundChuncks[0]; //current room
        int roomY = player.getChunkY() - boundChuncks[1]; //current room
        int xInChunk = player.getXInChunk();
        int yInChunk = player.getYInChunk();
        if (xInChunk == 7)
            roomX += 1;
        else if (xInChunk == 0)
            roomX -= 1;
        else if (yInChunk == 7)
            roomY += 1;
        else if (yInChunk == 0)
            roomY -= 1;
        openRoomCreationMenu(roomX, roomY, door.getZ());
    }

    public boolean hasRoom(HouseConstants.Room room) {
        for (RoomReference r : roomsR)
            if (r.room == room)
                return true;
        return false;
    }

    public void removeRoom() {
        int roomX = player.getChunkX() - boundChuncks[0]; //current room
        int roomY = player.getChunkY() - boundChuncks[1]; //current room
        RoomReference room = getRoom(roomX, roomY, player.getZ());
        if (room == null)
            return;
        if (room.getPlane() != 1) {
            player.getDialogueManager().sendDialogue("You cannot remove a building that is supporting this room.");
            return;
        }
        RoomReference above = getRoom(roomX, roomY, 2);
        RoomReference below = getRoom(roomX, roomY, 0);

        RoomReference roomTo = room.room == Room.THRONE_ROOM ? below : above != null && above.getStaircaseSlot() != -1 ? above : below != null && below.getStaircaseSlot() != -1 ? below : null;
        if (roomTo == null) {
            player.getDialogueManager().sendDialogue("These stairs do not lead anywhere.");
            return;
        }
        openRoomCreationMenu(roomTo.getX(), roomTo.getY(), roomTo.getPlane());
    }

    /*
     * door used to calculate where player facing to create
     */
    public void openRoomCreationMenu(int roomX, int roomY, int plane) {
        if (!buildMode) {
            player.getDialogueManager().sendDialogue("You can only do that in building mode.");
            return;
        }
        RoomReference room = getRoom(roomX, roomY, plane);
        if (room != null) {
            if (room.plane == 1 && getRoom(roomX, roomY, room.plane + 1) != null) {
                player.getDialogueManager().sendDialogue("You can't remove a room that is supporting another room.");
                return;
            }
            if (room.room == Room.THRONE_ROOM && room.plane == 1) {
                RoomReference bellow = getRoom(roomX, roomY, room.plane - 1);
                if (bellow != null && bellow.room == Room.OUTBLIETTE) {
                    player.getDialogueManager().sendDialogue("You can't remove a throne room that is supporting a outbliette.");
                    return;
                }
            }
            if ((room.room == Room.GARDEN || room.room == Room.FORMAL_GARDEN) && getPortalCount() < 2) {
                if (room == getPortalRoom()) {
                    player.getDialogueManager().sendDialogue("Your house must have at least one exit portal.");
                    return;
                }
            }
            player.getDialogueManager().startDialogue(new RemoveRoomD(), room);
        } else {
            if (roomX == 0 || roomY == 0 || roomX == 7 || roomY == 7) {
                player.getDialogueManager().sendDialogue("You can't create a room here.");
                return;
            }
            if (plane == 2) {
                RoomReference r = getRoom(roomX, roomY, 1);
                if (r == null || (r.room == Room.GARDEN || r.room == Room.FORMAL_GARDEN || r.room == Room.MENAGERIE)) {
                    player.getDialogueManager().sendDialogue("You can't create a room here.");
                    return;
                }

            }

            player.getPacketSender().sendInterface(28643);
            player.getTemporaryAttributes().put("CreationRoom", new int[]
                    {roomX, roomY, plane});
            player.setCloseInterfacesEvent(() -> player.getTemporaryAttributes().remove("CreationRoom"));
        }
    }

    public void climbTrapdoor(Player player, GameObject object, boolean up) {
        int roomX = object.getChunkX() - boundChuncks[0];
        int roomY = object.getChunkY() - boundChuncks[1];
        RoomReference room = getRoom(roomX, roomY, object.getZ());
        if (room == null)
            return;
        if(!up) {
            player.getDialogueManager().startDialogue(new SimpleMessageD(), "House dungeons have been disabled.", "Suggest on forums if you", "would like to see them added.");
            return;
        }
        if (!up && buildMode && room.plane != 1) {
            player.message("You cannot add a oubliette here.");
            return;
        }
        RoomReference roomTo = getRoom(roomX, roomY, room.plane + (up ? 1 : -1));
        if (roomTo == null) {
            if (buildMode && isOwner(player) && !up)
                player.getDialogueManager().startDialogue(new CreateOublietteD(), room);
            else
                player.message("This " + (up ? "ladder" : "trapdoor") + " does not lead anywhere.");
            //start dialogue
            return;
        }
        if (roomTo.room != Room.THRONE_ROOM && roomTo.room != Room.OUTBLIETTE) {
            player.message("This " + (up ? "ladder" : "trapdoor") + " does not lead anywhere.");
            return;
        }
        ObjectHandler.useStairs(player, up ? 828 : 827, new Position(player.getX(), player.getY(), player.getZ() + (up ? 1 : -1)), 0, 2);
    }

    public void climbStaircase(Player player, GameObject object, boolean up, boolean dungeonEntrance) {
        int roomX = object.getChunkX() - boundChuncks[0];
        int roomY = object.getChunkY() - boundChuncks[1];
        RoomReference room = getRoom(roomX, roomY, object.getZ());
        if (room == null)
            return;
        if(dungeonEntrance) {
            player.getDialogueManager().startDialogue(new SimpleMessageD(), "House dungeons have been disabled.", "If you'd like to see them", "added post a suggestion on the forums.");
            return;
        }
        if (buildMode && room.plane == (up ? 2 : 0)) {
            player.message("You are on the " + (up ? "highest" : "lowest") + " possible level so you cannot add a room " + (up ? "above" : "under") + " here.");
            return;
        }
        RoomReference roomTo = getRoom(roomX, roomY, room.plane + (up ? 1 : -1));
        if (roomTo == null) {
            if (buildMode && isOwner(player))
                player.getDialogueManager().startDialogue(new CreateRoomStairsD(), room, up, dungeonEntrance);
            else
                player.message((dungeonEntrance ? "This entrance does " : "These stairs do") + " not lead anywhere.");
            //start dialogue
            return;
        }
        if ((roomTo.room != Room.GARDEN && roomTo.room != Room.FORMAL_GARDEN) && roomTo.getStaircaseSlot() == -1) {
            player.message((dungeonEntrance ? "This entrance does " : "These stairs do") + " not lead anywhere.");
            return;
        }
        ObjectHandler.useStairs(player,-1, new Position(player.getX(), player.getY(), player.getZ() + (up ? 1 : -1)), 0, 2);
    }

    public void removeRoom(RoomReference room) {
        if (roomsR.remove(room)) {
            refreshNumberOfRooms();
            refreshHouse();
        }
    }

    public void createRoom(int slot) {
        Room[] rooms = HouseConstants.Room.values();
        if (slot >= rooms.length)
            return;
        int[] position = (int[]) player.getTemporaryAttributes().get("CreationRoom");
        player.getPacketSender().sendInterfaceRemoval();
        if (position == null)
            return;
        Room room = rooms[slot];
        if ((room == Room.TREASURE_ROOM || room == Room.DUNGEON_CORRIDOR || room == Room.DUNGEON_JUNCTION || room == Room.DUNGEON_PIT || room == Room.DUNGEON_STAIRS) && position[2] != 0) {
            player.message("That room can only be built underground.");
            return;
        }
        if (room == Room.THRONE_ROOM) {
            if (position[2] != 1) {
                player.message("This room cannot be built on a second level or underground.");
                return;
            }
        }
        if (room == Room.OUTBLIETTE) {
            player.message("That room can only be built using a throne room trapdoor.");
            return;
        }
        if ((room == Room.GARDEN || room == Room.FORMAL_GARDEN || room == Room.MENAGERIE) && position[2] != 1) {
            player.message("That room can only be built on ground.");
            return;
        }
        if (room == Room.MENAGERIE && hasRoom(Room.MENAGERIE)) {
            player.message("You can only build one menagerie.");
            return;
        }
        if (room == Room.GAMES_ROOM && hasRoom(Room.GAMES_ROOM)) {
            player.message("You can only build one game room.");
            return;
        }
        if (!player.getSkills().hasRequirement(Skill.CONSTRUCTION, room.getLevel(), "build this room"))
            return;

        if (!player.getInventory().hasCoins(room.getPrice())) {
            player.message("You don't have enough coins to build this room.");
            return;
        }

        if (roomsR.size() >= getMaxQuantityRooms()) {
            player.message("You have reached the maximum quantity of rooms.");
            return;
        }

        player.getDialogueManager().startDialogue(new CreateRoomD(), new RoomReference(room, position[0], position[1], position[2], 0));
    }

    private int getMaxQuantityRooms() {
        int consLvl = player.getSkills().getLevel(Skill.CONSTRUCTION);
        int maxRoom = 20;
        if (DonorStatus.isDonor(player, DonorStatus.EMERALD_DONOR))
            maxRoom += 10;
        if (player.isDonor())
            maxRoom += 10;
        if (consLvl >= 38) {
            maxRoom += (consLvl - 32) / 6;
            if (consLvl == 99)
                maxRoom++;
        }
        return maxRoom;
    }

    public void createRoom(RoomReference room) {
        if (!loaded)
            return;
        if (!player.getInventory().removeCoins(room.room.getPrice(), "build this room"))
            return;
        roomsR.add(room);
        refreshNumberOfRooms();
        refreshHouse();
    }

    public void addSavedRoom(RoomReference room) {
        if (!loaded)
            return;
        roomsR.add(room);
        refreshNumberOfRooms();
        refreshHouse();
    }

    //Used for inter 396
    private static final int[] BUILD_INDEXES = {0, 2, 4, 6, 1, 3, 5};

    public void openBuildInterface(GameObject object, final Builds build) {
        if (!buildMode) {
            player.getDialogueManager().startDialogue(new SimpleMessageD(), "You can only do that in building mode.");
            return;
        }
        int roomX = object.getChunkX() - boundChuncks[0];
        int roomY = object.getChunkY() - boundChuncks[1];
        RoomReference room = getRoom(roomX, roomY, object.getZ());
        if (room == null)
            return;
        Item[] itemArray = new Item[build.getPieces().length];
        int requirimentsValue = 0;
        for (int index = 0; index < build.getPieces().length; index++) {
            HObject piece = build.getPieces()[index];
            itemArray[index] = new Item(piece.getItemId(), 1);
            if (hasRequirementsToBuild(false, build, piece))
                requirimentsValue += Math.pow(2, index + 1);
        }
        player.getPacketSender().sendWidgetItems(38274, itemArray);
        handleInterfaceCrosses(itemArray, player, build);
        //player.getPackets().sendIComponentSettings(1306, 55, -1, -1, 1); //exit button
        //for(int i = 0; i < itemArray.length; i++)
        //player.getPackets().sendIComponentSettings(1306, 8 + 7*i, 4, 4, 1); //build options
        player.getPacketSender().sendInterface(38272);
        player.getTemporaryAttributes().put("OpenedBuild", build);
        player.getTemporaryAttributes().put("OpenedBuildObject", object);
        /*
         * rs now uses dialogue for building. but we keep support for old way
		 */
        player.getDialogueManager().startDialogue(new BuildD());
        player.setCloseInterfacesEvent(() -> {
            player.getTemporaryAttributes().remove("OpenedBuild");
            player.getTemporaryAttributes().remove("OpenedBuildObject");
        });
    }

    public static void handleInterfaceCrosses(Item[] items, Player c, Builds build) {
        int i = 1000;
        for (int l = 0; l < items.length; l++) {
            Item f = items[l];
            c.getPacketSender().sendString(38275 + (i - 1000) * 6, Misc.formatName(f.getName().toLowerCase().replaceAll("_", " ")));
            c.getPacketSender().sendString(38280 + (i - 1000) * 6, "Level: " + build.getPieces()[l].getLevel());
            int i2 = 0;
            boolean canMake = true;
            for (int i1 = 0; i1 < build.getPieces()[l].getRequirements().length; i1++) {
                c.getPacketSender().sendString((38276 + i2) + (i - 1000) * 6, build.getPieces()[l].getRequirements()[i1].getAmount() + " x " + Loader.getItem(build.getPieces()[l].getRequirements()[i1].getId()).getName());
                if (c.getInventory().getAmount(build.getPieces()[l].getRequirements()[i1]) < build.getPieces()[l].getRequirements()[i1].getAmount()) {
                    i2++;
                    canMake = false;
                    continue;
                }
                i2++;
            }
            if (build.getPieces()[l].getLevel() > c.getSkills().getLevel(Skill.CONSTRUCTION)) {
                canMake = false;
            }
            if (canMake)
                c.getPacketSender().sendConfig(i, 0);
            else
                c.getPacketSender().sendConfig(i, 1);
            for (i2 = i2; i2 < 4; i2++) {
                c.getPacketSender().sendString((38276 + i2) + (i - 1000) * 6, "");
            }
            i++;
        }
        for (i = i; i < 1008; i++) {
            c.getPacketSender().sendString(38275 + (i - 1000) * 6, "");
            c.getPacketSender().sendString(38276 + (i - 1000) * 6, "");
            c.getPacketSender().sendString(38277 + (i - 1000) * 6, "");
            c.getPacketSender().sendString(38278 + (i - 1000) * 6, "");
            c.getPacketSender().sendString(38279 + (i - 1000) * 6, "");
            c.getPacketSender().sendString(38280 + (i - 1000) * 6, "");
            c.getPacketSender().sendConfig(i, 0);
        }
    }

    private boolean hasRequirementsToBuild(boolean warn, Builds build, HObject piece) {
        int level = player.getSkills().getMaxLevel(Skill.CONSTRUCTION);
        if (!build.isWater() && player.getInventory().contains(new Item(9625)))
            level += 3;
        if (level < piece.getLevel()) {
            if (warn)
                player.message("Your level of construction is too low for this build.");
            return false;
        }
        if (!player.getRights().isOrHigher(PlayerRights.DEVELOPER)) {
            if (!player.getInventory().containsAmount(piece.getRequirements())) {
                if (warn)
                    player.message("You don't have the right materials.");
                return false;
            }
            if (build.isWater() ? !hasWaterCan() : (!player.getInventory().contains(HouseConstants.HAMMER) || (!player.getInventory().contains(HouseConstants.SAW) && !player.getInventory().contains(new Item(9625))))) {
                if (warn)
                    player.message(build.isWater() ? "You will need a watering can with some water in it instead of hammer and saw to build plants." : "You will need a hammer and saw to build furniture.");
                return false;
            }
        }
        return true;
    }


    public void build(int slot) {
        final Builds build = (Builds) player.getTemporaryAttributes().get("OpenedBuild");
        GameObject object = (GameObject) player.getTemporaryAttributes().get("OpenedBuildObject");
        if (build == null || object == null || build.getPieces().length <= slot)
            return;
        int roomX = object.getChunkX() - boundChuncks[0];
        int roomY = object.getChunkY() - boundChuncks[1];
        final RoomReference room = getRoom(roomX, roomY, object.getZ());
        if (room == null)
            return;
        final HObject piece = build.getPieces()[slot];
        if (!hasRequirementsToBuild(true, build, piece))
            return;
        if(build.isUpgrade()) {
            if(slot > 0) {
                if(object.getId() != build.getPieces()[slot - 1].getId()) {
                    player.message("You need a " + build.getPieces()[slot - 1].name().toLowerCase().replaceAll("_", " ") + " to build this.");
                    return;
                }
            }

            boolean contains = false;
            for(int id : build.getIds()) {
                if(id == object.getId()) {
                    contains = true;
                    break;
                }
            }

            if(!contains && object.getId() >= build.getPieces()[slot].getId())
                return;
        }

        final ObjectReference oref = room.addObject(build, slot);
        player.getPacketSender().sendInterfaceRemoval();
        player.getMovement().lock();
        player.animate(build.isWater() ? 2293 : 3683);

        if (!player.getRights().isOrHigher(PlayerRights.DEVELOPER)) {
            if(build.isWater()) {
                for(Item item : player.getInventory().getItems()) {
                    if(item == null)
                        continue;

                    if(item.getId() >= 5333 && item.getId() <= 5340) {
                        player.getInventory().delete(item);
                        player.getInventory().add(new Item(item.getId() == 5333 ? 5331 : item.getId() - 1));
                        break;
                    }
                }
            }
            player.getInventory().delete(piece.getRequirements());
        }
        player.getMovement().lock();
        GameWorld.schedule(2, () -> {
            player.getSkills().addExperience(Skill.CONSTRUCTION, piece.getXP());
            StrangeRocks.handleStrangeRocks(player, Skill.CONSTRUCTION);
            if (build.isWater())
                player.getSkills().addExperience(Skill.FARMING, piece.getXP());
            refreshObject(room, oref, false);
            player.getMovement().lock(1);
        });
    }

    private Region getRegion() {
        int[] regionPos = MapUtils.convert(MapUtils.Structure.CHUNK, MapUtils.Structure.REGION, boundChuncks);
        return GameWorld.getRegions().get(MapUtils.encode(MapUtils.Structure.REGION, regionPos), true);
    }

    /*
     * called when switching challenge mode
     */
    private void refreshChallengeMode() {
        boolean remove = !isChallengeMode();
        for (RoomReference reference : roomsR) {
            if (reference.plane != 0)
                continue;
            for (ObjectReference o : reference.objects) {
                if (o.getPiece().getNPCId() != -1)
                    refreshObject(reference, o, !remove, true);
            }
        }
        if (remove)
            clearChallengeNPCs();
        else {
            boolean pvp = isPVPMode();
            for (Player player : new ArrayList<>(players)) {
                player.message((pvp ? "PVP" : "Challenge") + " mode is now activated.");
                if (pvp)
                	player.setCanPvp(true);
            }
        }
    }

    /*
     * called when turning off challenge mode / switching to buildmode
     */
    private void clearChallengeNPCs() {
        for (RoomReference rref : roomsR) {
            if (rref.getGuardians() == null)
                continue;
            List<Guard> challengeModeNPCs = rref.getGuardians();
            for (Guard n : challengeModeNPCs)
                n.deregister();
            challengeModeNPCs.clear();
        }
    }

    private void refreshObject(RoomReference rref, ObjectReference oref, boolean remove) {
        refreshObject(rref, oref, remove, false);
    }

    public void refreshObject(RoomReference rref, ObjectReference oref, boolean remove, boolean challengeMode) {
        int boundX = rref.x * 8;
        int boundY = rref.y * 8;
        final Region region = getRegion();
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                GameObject[] objects = region.getAllObjects(boundX + x, boundY + y, rref.plane);
                if (objects != null) {
                    for (GameObject object : objects) {
                        if (object == null)
                            continue;
                        int slot = oref.getBuild().getIdSlot(object.getId());
                        if (slot == -1)
                            continue;
                        if (remove) {
                            if (challengeMode) {
                                GameObject objectR = new GameObject(object);
                                objectR.setId(HouseConstants.EMPTY_SPACE_ID);
                                ObjectManager.spawnObject(objectR);
                                List<Guard> guardians = rref.getGuardians();
                                if (guardians != null)
                                    guardians.add(new Guard(oref.getPiece().getNPCId(), this, object));
                            } else {
                                ObjectManager.spawnObject(object);
                            }
                        } else {
                            GameObject objectR = new GameObject(object);
                            objectR.setId(oref.getId(slot));
                            ObjectManager.spawnObject(objectR);
                        }
                    }
                }
            }
        }
    }

    public boolean hasWaterCan() {
        for (int id = 5333; id <= 5340; id++)
            if (player.getInventory().contains(new Item(id)))
                return true;
        return false;
    }

    public void openRemoveBuild(GameObject object) {
        if (!buildMode) {
            player.getDialogueManager().sendDialogue("You can only do that in building mode.");
            return;
        }
        if (object.getId() == HouseConstants.HObject.EXIT_PORTAL.getId() && getPortalCount() <= 1) {
            player.getDialogueManager().sendDialogue("Your house must have at least one exit portal.");
            return;
        }
        int roomX = object.getChunkX() - boundChuncks[0];
        int roomY = object.getChunkY() - boundChuncks[1];
        RoomReference room = getRoom(roomX, roomY, object.getZ());
        if (room == null)
            return;
        ObjectReference ref = room.getObject(object);
        if (ref != null) {
            if (ref.getBuild().toString().contains("STAIRCASE")) {
                if (object.getZ() != 1) {
                    RoomReference above = getRoom(roomX, roomY, 2);
                    RoomReference below = getRoom(roomX, roomY, 0);
                    if ((above != null && above.getStaircaseSlot() != -1) || (below != null && below.getStaircaseSlot() != -1))
                        player.getDialogueManager().sendDialogue("You cannot remove a building that is supporting this room.");
                    return;
                }
            }
            player.getDialogueManager().startDialogue(new RemoveBuildD(), object);
        }
    }

    public void removeBuild(final GameObject object) {
        if (!buildMode) {
            player.getDialogueManager().sendDialogue("You can only do that in building mode.");
            return;
        }
        int roomX = object.getChunkX() - boundChuncks[0];
        int roomY = object.getChunkY() - boundChuncks[1];
        final RoomReference room = getRoom(roomX, roomY, object.getZ());
        if (room == null)
            return;
        final ObjectReference oref = room.removeObject(object);
        if (oref == null)
            return;
        player.getMovement().lock();
        player.animate(3685);
        GameWorld.schedule(1, () -> {
            refreshObject(room, oref, true);
            player.getMovement().lock(1);
        });
    }

    public boolean isDoor(GameObject object) {
        return object.getDefinition().getName().equalsIgnoreCase("Door hotspot");
    }

    public boolean isBuildMode() {
        return buildMode;
    }

    public boolean isDoorSpace(GameObject object) {
        return object.getDefinition().getName().equalsIgnoreCase("Door space");
    }

    public boolean isWindowSpace(GameObject object) {
        return object.getDefinition().getName().equalsIgnoreCase("Window space");
    }

    public void switchLock(Player player) {
        if (!isOwner(player)) {
            player.message("You can only lock your own house.");
            return;
        }
        locked = !locked;
        if (locked)
            player.getDialogueManager().sendDialogue("Your house is now locked to all visitors.");
        else if (buildMode)
            player.getDialogueManager().sendDialogue("Visitors will be able to enter your house once you leave building mode.");
        else
            player.getDialogueManager().sendDialogue("Visitors can now enter your house.");
    }

    public static void enterHouse(Player player) {
        enterHouse(player, player.getUsername(), false);
    }

    public static void enterHouse(Player player, boolean override) {
        enterHouse(player, player.getUsername(), override);
    }

    public static void enterHouse(Player player, String displayname) {
        enterHouse(player, displayname, false);
    }

    public static void enterHouse(Player player, String displayname, boolean override) {
        if (player.getMovement().isLocked() && !override) {
            //players could enter friends house while using things like home tele which teleports you out when stepping off lodestone
            return;
        }

        Player owner = World.getPlayerByName(displayname);
        if (owner != player) {
            if (owner == null || !owner.getSettings().getBool(Settings.RUNNING) || owner.getHouse().locked) {
                player.message("That player is offline, or has privacy mode enabled.");
                return;
            }
            if (!owner.getRelations().isFriendWith(player.getUsername())) {
                player.message("That player is offline, or has privacy mode enabled.");
                return;
            }
        }
        if (!player.getRights().isOrHigher(PlayerRights.DEVELOPER) && !player.isWithinDistance(owner.getHouse().location.getTile(), 16)) {
            player.message("This house is at " + Utils.formatPlayerNameForDisplay(owner.getHouse().location.name()) + ".");
            return;
        }
        owner.getHouse().joinHouse(player);
    }

    public boolean joinHouse(final Player player) {
        if (!isOwner(player)) { //not owner
            if (!isOwnerInside() || !loaded) {
                player.message("That player is offline, or has privacy mode enabled."); //TODO message
                return false;
            }
            if (buildMode) {
                player.message("The owner currently has build mode turned on.");
                return false;
            }
        } else {
            if (false) {
                //System.out.println("starting nope");//we dont need bank pin checks
                return false;
            }
    }
        sendStartInterface(player);
        players.add(player);
        player.getControllerManager().startController(new HouseController(), this);
        if (loaded) {
            GameWorld.schedule(1, () -> teleportPlayer(player));
            GameWorld.schedule(3, () -> {
                player.getPacketSender().sendInterfaceRemoval();
                player.getMovement().lock(1);
            });
        } else {
            GameExecutorManager.slowExecutor.execute(() -> {
                boundChuncks = MapBuilder.findEmptyChunkBound(8, 8);
                createHouse(true);
            });
        }
        return true;
    }

    public static void leaveHouse(Player player) {
        Controller controller = player.getControllerManager().getController();
        if (controller == null || !(controller instanceof HouseController)) {
            player.message("You're not in a house.");
            return;
        }
        ((HouseController) controller).getHouse().leaveHouse(player, KICKED);
    }

    /*
     * 0 - logout, 1 kicked/tele outside outside, 2 tele somewhere else
     */
    public void leaveHouse(Player player, int type) {
        player.getPacketSender().sendTabInterface(GameSettings.OPTIONS_TAB, 904);
        player.getControllerManager().removeControllerWithoutCheck();
        if (type == LOGGED_OUT)
            player.moveTo(location.getTile());
        else if (type == KICKED)
            ObjectHandler.useStairs(player,-1, location.getTile(), 0, 2);
        if (player.getEffects().hasActiveEffect(Effects.POISON))
            player.getEffects().removeEffect(Effects.POISON);
        if (player.isCanPvp())
            player.setCanPvp(false);
		if (isOwner(player) && servantInstance != null)
			servantInstance.setFollowing(false);
        if (player.getFamiliar() != null)
            player.getFamiliar().respawnFamiliar(player);
        else
            player.getPetManager().init();
        removeItems(player);
        players.remove(player);
        if (players.size() == 0)
            destroyHouse();
    }


    private void removeItems(Player player) {
        for(int id : HouseConstants.WEAPON_RACK) {
            while (player.getInventory().contains(new Item(id)))
                player.getInventory().delete(new Item(id));
            while (player.getEquipment().contains(new Item(id)))
                player.getEquipment().delete(new Item(id));
        }
        Equipment.resetWeapon(player);
    }

    /*
     * refers to logout
     */
    public void finish() {
        kickGuests();
        //no need to leavehouse for owner, controler does that itself
    }

    public void refreshHouse() {
        loaded = false;
        sendStartInterface(player);
        createHouse(false);
    }

    public void sendStartInterface(Player player) {
        player.getPacketSender().sendInterface(28640);
        player.getMovement().lock();
        //player.getMusicsManager().playMusic(385);
        //player.getMusicsManager().playMusicEffect(67);
    }

    public void teleportPlayer(Player player) {
        player.moveTo(getPortal());
        if (isPVPMode())
            player.setCanPvp(true);
    }

    public boolean isLoaded() {
        return loaded;
    }

    public Position getPortal() {
        for (RoomReference room : roomsR) {
            if (room.room == HouseConstants.Room.GARDEN || room.room == HouseConstants.Room.FORMAL_GARDEN) {
                for (ObjectReference o : room.objects)
                    if (o.getPiece() == HouseConstants.HObject.EXIT_PORTAL)
                        return getCenterTile(room);
            }
        }
        //shouldnt happen
        int[] xyp = MapUtils.convert(MapUtils.Structure.CHUNK, MapUtils.Structure.TILE, boundChuncks);
        return new Position(xyp[0] + 32, xyp[1] + 32, 0);
    }

    public int getPortalCount() {
        int count = 0;
        for (RoomReference room : roomsR) {
            if (room.room == HouseConstants.Room.GARDEN || room.room == HouseConstants.Room.FORMAL_GARDEN) {
                for (ObjectReference o : room.objects)
                    if (o.getPiece() == HouseConstants.HObject.EXIT_PORTAL)
                        count++;
            }
        }
        return count;
    }

    public RoomReference getPortalRoom() {
        for (RoomReference room : roomsR) {
            if (room.room == HouseConstants.Room.GARDEN || room.room == HouseConstants.Room.FORMAL_GARDEN) {
                for (ObjectReference o : room.objects)
                    if (o.getPiece() == HouseConstants.HObject.EXIT_PORTAL)
                        return room;
            }
        }
        return null;
    }

    public House() {
        build = 4;
        buildMode = false;
        roomsR = new ArrayList<>();
        location = POHLocation.EDGEVILLE;
        addRoom(HouseConstants.Room.GARDEN, 3, 3, 1, 0);
        getRoom(3, 3, 1).addObject(Builds.CENTREPIECE, 0);
    }

    public boolean addRoom(HouseConstants.Room room, int x, int y, int plane, int rotation) {
        return roomsR.add(new RoomReference(room, x, y, plane, rotation));
    }

    public void reset() {
        build = 4;
        look = 0;
        buildMode = false;
        doorsOpen = true;
        roomsR = new ArrayList<>();
        location = POHLocation.EDGEVILLE;
        addRoom(HouseConstants.Room.GARDEN, 3, 3, 1, 0);
        getRoom(3, 3, 1).addObject(Builds.CENTREPIECE, 0);
    }

    public void init() {
        if (build != 4)
            reset();
        players = new ArrayList<>();
        dungeonTraps = new ArrayList<>();
        refreshBuildMode();
        //refreshArriveInPortal();
        //refreshDoorsOpen();
        //refreshNumberOfRooms();
    }

    public void refreshNumberOfRooms() {
        player.getPacketSender().sendString(19285, Integer.toString(roomsR.size()));
    }

    //TODO
    public void setDoorsOpen(boolean doorsOpen) {
        this.doorsOpen = doorsOpen;
        refreshDoorsOpen();
    }

    public void refreshDoorsOpen() {
        player.getPacketSender().sendConfig(1553, doorsOpen ? 1 : 0);
    }

    public void setArriveInPortal(boolean arriveInPortal) {
        this.arriveInPortal = arriveInPortal;
        //refreshArriveInPortal();
    }

    public void refreshArriveInPortal() {
        player.getPacketSender().sendConfig(1552, arriveInPortal ? 1 : 0);
    }

    public void setBuildMode(boolean buildMode) {
        if (this.buildMode == buildMode)
            return;
        this.buildMode = buildMode;
        if (loaded) {
            expelGuests();
            if (isOwnerInside()) { //since it expels all guests no point in refreshing if owner not inside
                player.stopAll();
                if (player.isCanPvp())
                    player.setCanPvp(false);
                refreshHouse();
            }
        }
        refreshBuildMode();
    }

    public void refreshBuildMode() {
        player.getPacketSender().sendConfig(220, buildMode ? 0 : 1);
        player.getPacketSender().sendConfig(1537, buildMode ? 1 : 0);
    }

    public RoomReference getRoom(Player player) {
        int roomX = player.getChunkX() - boundChuncks[0]; //current room
        int roomY = player.getChunkY() - boundChuncks[1]; //current room
        RoomReference room = getRoom(roomX, roomY, player.getZ());
        if (room == null)
            return null;
        return room;
    }

    public RoomReference getRoom(Room room) {
        for (RoomReference roomR : roomsR) {
            if (room == roomR.getRoom())
                return roomR;
        }
        return null;
    }

    public RoomReference getRoom(int x, int y, int plane) {
        for (RoomReference room : roomsR)
            if (room.x == x && room.y == y && room.plane == plane)
                return room;
        return null;
    }

    public boolean isSky(int x, int y, int plane) {
        return buildMode && plane == 2 && getRoom((x / 8) - boundChuncks[0], (y / 8) - boundChuncks[1], plane) == null;
    }

    public void previewRoom(RoomReference reference, boolean remove) {
        if (!loaded)
            return;
        int boundX = boundChuncks[0] * 8 + reference.x * 8;
        int boundY = boundChuncks[1] * 8 + reference.y * 8;
        int realChunkX = reference.room.getChunkX() + (look >= 4 ? 8 : 0);
        int realChunkY = reference.room.getChunkY();
        Region region = GameWorld.getRegions().get(MapUtils.encode(MapUtils.Structure.REGION, realChunkX / 8, realChunkY / 8));
        if (reference.plane == 0) {
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    GameObject objectR = new GameObject(HouseConstants.EMPTY_SPACE_ID, new Position(boundX + x, boundY + y, reference.plane), 10, reference.rotation);
                    if (remove)
                        ObjectManager.removeObject(objectR);
                    else
                        ObjectManager.spawnObject(objectR);
                }
            }
        }
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                GameObject[] objects = region.getAllObjects((realChunkX & 0x7) * 8 + x, (realChunkY & 0x7) * 8 + y, look & 0x3);
                if (objects != null) {
                    for (GameObject object : objects) {
                        if (object == null)
                            continue;
                        ObjectDefinition defs = object.getDefinition();
                        if (reference.plane == 0 || defs.containsOption(4, "Build")) {
                            GameObject objectR = new GameObject(object);
                            int[] coords = DynamicRegion.translate(x, y, reference.rotation, defs.sizeX, defs.sizeY, object.getDirection());
                            objectR.setPosition(new Position(boundX + coords[0], boundY + coords[1], reference.plane));
                            objectR.setDirection((object.getDirection() + reference.rotation) & 0x3);
                            if (remove)
                                ObjectManager.removeObject(objectR);
                            else
                                ObjectManager.spawnObject(objectR);
                        }
                    }
                }
            }
        }
    }

    public void destroyHouse() {
        final int[] boundChunksCopy = boundChuncks;
        //this way a new house can be created while current house being destroyed
        loaded = false;
        boundChuncks = null;
        removeServant();
        dungeonTraps.clear();
        if (isChallengeMode()) {
            clearChallengeNPCs();
            challengeMode = 0;
        }
        GameExecutorManager.slowExecutor.schedule(() -> MapBuilder.destroyMap(boundChunksCopy[0], boundChunksCopy[1], 8, 8), 1200, TimeUnit.MILLISECONDS);
    }

    public void createHouse(final boolean tp) {
        Object[][][][] data = new Object[4][8][8][];
        //sets rooms data
        for (RoomReference reference : roomsR)
            data[reference.plane][reference.x][reference.y] = new Object[]
                    {reference.room.getChunkX(), reference.room.getChunkY(), reference.rotation, reference.room.isShowRoof(), reference.room.getDoorsCount()};
        //sets roof data
        if (!buildMode) { // construct roof
            for (int x = 1; x < 7; x++) {
                skipY:
                for (int y = 1; y < 7; y++) {
                    for (int plane = 2; plane >= 1; plane--) {
                        if (data[plane][x][y] != null) {
                            boolean hasRoof = (boolean) data[plane][x][y][3];
                            if (hasRoof) {
                                byte rotation = (byte) data[plane][x][y][2];
                                // TODO find best Roof
                                int doorsCount = (int) data[plane][x][y][4];
                                Roof roof = doorsCount == 4 ? HouseConstants.Roof.ROOF3 : doorsCount == 3 ? HouseConstants.Roof.ROOF2 : HouseConstants.Roof.ROOF1;
                                data[plane + 1][x][y] = new Object[]
                                        {roof.getChunkX(), roof.getChunkY(), rotation, true, doorsCount};
                                continue skipY;
                            }
                        }
                    }
                }
            }
        }
        //builds data
        for (int plane = 0; plane < data.length; plane++) {
            for (int x = 0; x < data[plane].length; x++) {
                for (int y = 0; y < data[plane][x].length; y++) {
                    if (data[plane][x][y] != null)
                        MapBuilder.copyChunk((int) data[plane][x][y][0] + (look >= 4 ? 8 : 0), (int) data[plane][x][y][1], look & 0x3, boundChuncks[0] + x, boundChuncks[1] + y, plane, (byte) data[plane][x][y][2]);
                    else if ((x == 0 || x == 7 || y == 0 || y == 7) && plane == 1)
                        MapBuilder.copyChunk(HouseConstants.BLACK[0], HouseConstants.BLACK[1], 0, boundChuncks[0] + x, boundChuncks[1] + y, plane, 0);
                    else if (plane == 1)
                        MapBuilder.copyChunk(HouseConstants.LAND[0] + (look >= 4 ? 8 : 0), HouseConstants.LAND[1], look & 0x3, boundChuncks[0] + x, boundChuncks[1] + y, plane, 0);
                    else if (plane == 0)
                        MapBuilder.copyChunk(HouseConstants.DUNGEON[0] + (look >= 4 ? 8 : 0), HouseConstants.DUNGEON[1], look & 0x3, boundChuncks[0] + x, boundChuncks[1] + y, plane, 0);
                    else
                        MapBuilder.cutChunk(boundChuncks[0] + x, boundChuncks[1] + y, plane);
                }
            }
        }
        int[] regionPos = MapUtils.convert(MapUtils.Structure.CHUNK, MapUtils.Structure.REGION, boundChuncks);
        final Region region = GameWorld.getRegions().get(MapUtils.encode(MapUtils.Structure.REGION, regionPos), true);
        List<GameObject> spawnedObjects = region.getSpawnedObjects();
        if (spawnedObjects != null) {
            for (GameObject object : spawnedObjects)
                if (object != null)
                    ObjectManager.removeObject(object);
        }
        List<GameObject> removedObjects = region.getRemovedOriginalObjects();
        if (removedObjects != null) {
            for (GameObject object : removedObjects)
                ObjectManager.spawnObject(object);
        }
        if (isChallengeMode()) {
            clearChallengeNPCs();
            if (isPVPMode())
                player.setCanPvp(false);
            challengeMode = 0;
        }
        dungeonTraps.clear();
        World.executeAfterLoadRegion(region.getRegionId(), 2400, new Runnable() {

            @Override
            public void run() {
                if (boundChuncks == null) //shouldnt unless shutdown command force kicks
                    return;
                for (RoomReference reference : roomsR) {
                    int boundX = reference.x * 8;
                    int boundY = reference.y * 8;
                    for (int x = 0; x < 8; x++) {
                        for (int y = 0; y < 8; y++) {
                            GameObject[] objects = region.getAllObjects(boundX + x, boundY + y, reference.plane);
                            if (objects != null) {
                                skip: for (GameObject object : objects) {
                                    if (object == null)
                                        continue;
                                    if (object.getDefinition().containsOption(4, "Build") || object.getDefinition().getName().equals("Habitat space")) {
                                        if (isDoor(object)) {
                                            if (!buildMode && object.getZ() == 2 && getRoom(((object.getX() / 8) - boundChuncks[0]) + Misc.ROTATION_DIR_X[object.getDirection()], ((object.getY() / 8) - boundChuncks[1]) + Misc.ROTATION_DIR_Y[object.getDirection()], object.getZ()) == null) {
                                                GameObject objectR = new GameObject(object);
                                                objectR.setId(HouseConstants.WALL_IDS[look]);
                                                ObjectManager.spawnObject(objectR);
                                                continue;
                                            }
                                        } else {
                                            for (ObjectReference o : reference.objects) {
                                                int slot = o.getBuild().getIdSlot(object.getId());
                                                if (slot != -1) {
                                                    GameObject objectR = new GameObject(object);
                                                    if (!buildMode && (o.getBuild() == Builds.PORTAL_1 || o.getBuild() == Builds.PORTAL_2 || o.getBuild() == Builds.PORTAL_3)) {
                                                        int portal = o.getBuild() == Builds.PORTAL_1 ? 0 : o.getBuild() == Builds.PORTAL_2 ? 1 : 2;
                                                        if (reference.directedPortals[portal] != 0) {
                                                            int type = o.getId(slot) - 13636;
                                                            objectR.setId(13614 + reference.directedPortals[portal] + type * 7);
                                                            ObjectManager.spawnObject(objectR);
                                                            continue skip;
                                                        }
                                                    }
                                                    objectR.setId(o.getId(slot));
                                                    if (!buildMode && (o.getBuild() == Builds.TRAP_SPACE_1 || o.getBuild() == Builds.TRAP_SPACE_2)) {
                                                        dungeonTraps.add(objectR);
                                                        ObjectManager.removeObject(object);
                                                        continue skip;
                                                    }
                                                    ObjectManager.spawnObject(objectR);
                                                    continue skip;
                                                }
                                            }
                                            if (!buildMode && isWindowSpace(object)) {
                                                object = new GameObject(object);
                                                object.setId(reference.plane == 0 ? HouseConstants.WALL_IDS[look] : HouseConstants.WINDOW_IDS[look]);
                                                ObjectManager.spawnObject(object);
                                                continue;
                                            }
                                        }
                                        if (!buildMode)
                                            ObjectManager.removeObject(object);
                                    } else if (object.getId() == HouseConstants.WINDOW_SPACE_ID) {
                                        object = new GameObject(object);
                                        object.setId(reference.plane == 0 ? HouseConstants.WALL_IDS[look] : HouseConstants.WINDOW_IDS[look]);
                                        ObjectManager.spawnObject(object);
                                    } else if (isDoorSpace(object)) //yes it does
                                        ObjectManager.removeObject(object);
                                }
                            }
                        }
                    }
                }
                refreshBuildMode();
                player.loadMapRegions();
                player.getTimers().getClickDelay().reset();
                player.getMovement().lock(4);
                if (player.getFamiliar() != null)
                    player.getFamiliar().respawnFamiliar(player);
                else
                    player.getPetManager().init();
                GameWorld.schedule(2, () -> player.getPacketSender().sendInterfaceRemoval());
                if (tp) {
                    teleportPlayer(player);
                    refreshServant();
                }
                loaded = true;
            }
        });
    }

    public boolean isWindow(int id) {
        return id == 13830;
    }

    public static class ObjectReference implements Serializable {

        /**
         *
         */
        private static final long serialVersionUID = -22245200911725426L;
        private int slot;
        private Builds build;

        public ObjectReference(Builds build, int slot) {
            this.setBuild(build);
            this.slot = slot;
        }

        public HObject getPiece() {
            return getBuild().getPieces()[slot];
        }

        public int getId() {
            return getBuild().getPieces()[slot].getId();
        }

        public int[] getIds() {
            return getBuild().getPieces()[slot].getIds();
        }

        public int getId(int slot2) {
            return getIds()[slot2];
        }

        public Builds getBuild() {
            return build;
        }

        public void setBuild(Builds build) {
            this.build = build;
        }

        public int getSlot() {
            return slot;
        }
    }

    public static class RoomReference implements Serializable {

        private static final long serialVersionUID = 4000732770611956015L;

        public RoomReference(HouseConstants.Room room, int x, int y, int plane, int rotation) {
            this.room = room;
            this.x = (byte) x;
            this.y = (byte) y;
            this.plane = (byte) plane;
            this.rotation = (byte) rotation;
            objects = new ArrayList<>();
            if (room == Room.PORTAL_CHAMBER)
                directedPortals = new byte[3];
            if (isDungeon(room))
                guardians = new ArrayList<>();
        }

        public HouseConstants.Room room;
        private byte x, y, plane, rotation;
        public List<ObjectReference> objects;
        private byte[] directedPortals;
        private transient List<Guard> guardians;

        public int getStaircaseSlot() {
            for (ObjectReference object : objects) {
                if (object.getBuild().toString().contains("STAIRCASE") || object.getBuild().toString().contains("CENTREPEICE"))
                    return object.slot;
            }
            return -1;
        }

        public int getTrapdoorSlot() {
            for (ObjectReference object : objects) {
                if (object.getBuild() == Builds.TRAPDOOR)
                    return object.slot;
            }
            return -1;
        }

        public boolean isStaircaseDown() {
            for (ObjectReference object : objects) {
                if (object.getBuild().toString().contains("STAIRCASE_DOWN"))
                    return true;
            }
            return false;
        }

        /*
         * x,y inside the room chunk
         */
        public ObjectReference addObject(Builds build, int slot) {
            ObjectReference ref = new ObjectReference(build, slot);
            objects.add(ref);
            return ref;
        }

        public ObjectReference getObject(GameObject object) {
            GameObject real = ObjectManager.getRealObject(object, object.getType());
            if (real != null) {
                for (ObjectReference o : objects) {
                    for (int id : o.getBuild().getIds()) {
                        if (real.getId() == id)
                            return o;
                    }
                }
            }
            return null;
        }

        public int getHObjectSlot(HObject hObject) {
            for (int index = 0; index < objects.size(); index++) {
                ObjectReference o = objects.get(index);
                if (o == null)
                    continue;
                if (hObject.getId() == o.getPiece().getId())
                    return o.getSlot();
            }
            return -1;
        }

        public boolean containsHObject(HObject hObject) {
            return getHObjectSlot(hObject) != -1;
        }

        public int getBuildSlot(Builds build) {
            for (int index = 0; index < objects.size(); index++) {
                ObjectReference o = objects.get(index);
                if (o == null)
                    continue;
                if (o.getBuild() == build)
                    return o.getSlot();
            }
            return -1;
        }

        public boolean containsBuild(Builds build) {
            return getBuildSlot(build) != -1;
        }

        public ObjectReference removeObject(GameObject object) {
            ObjectReference r = getObject(object);
            if (r != null) {
                objects.remove(r);
                return r;
            }
            return null;
        }

        public void setRotation(int rotation) {
            this.rotation = (byte) rotation;
        }

        public byte getRotation() {
            return rotation;
        }

        public Room getRoom() {
            return room;
        }

        public int getPlane() {
            return plane;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public List<ObjectReference> getObjects() {
            return objects;
        }

        public byte[] getDirectedPortals() {
            return directedPortals;
        }

        public void setDirectedPortals(byte[] directedPortals) {
            this.directedPortals = directedPortals;
        }

        public List<Guard> getGuardians() {
            if (isDungeon(room) && guardians == null)
                guardians = new ArrayList<Guard>();
            return guardians;
        }
    }

    public GameObject getWorldObjectForBuild(RoomReference reference, Builds build) {
        int boundX = boundChuncks[0] * 8 + reference.x * 8;
        int boundY = boundChuncks[1] * 8 + reference.y * 8;
        for (int x = -1; x < 8; x++) {
            for (int y = -1; y < 8; y++) {
                for (HObject piece : build.getPieces()) {
                    GameObject object = ObjectManager.getObjectWithId(piece.getId(), new Position(boundX + x, boundY + y, reference.plane));
                    if (object != null) {
                        return object;
                    }
                }
            }
        }
        return null;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public static boolean isDungeon(Room room) {
        for (Room dungeon : HouseConstants.DUNGEON_ROOMS) {
            if (room == dungeon)
                return true;
        }
        return false;
    }

    public Player getPlayer() {
        return player;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public POHLocation getLocation() {
        return location;
    }

    public void setLocation(POHLocation location) {
        this.location = location;
    }

    public boolean isArriveInPortal() {
        return arriveInPortal;
    }

    public byte getBuild() {
        return build;
    }

    public byte getLook() {
        return look;
    }

    public int getBurnerCount() {
        return burnerCount;
    }

    public void setBurnerCount(int burnerCount) {
        this.burnerCount = burnerCount;
    }

    public void redecorateHouse(int look) {
        this.look = (byte) look;
    }

    public void setServantOrdinal(byte ordinal) {
        if (ordinal == -1) {
            removeServant();
            servant = null;
            return;
        }
        this.servant = Servant.values()[ordinal];
    }

    public boolean hasServant() {
        return servant != null;
    }

    public static void enterHousePortal(Player player) {
        player.getDialogueManager().startDialogue(new EnterHouseD());
    }

    private void removeServant() {
        if (servantInstance != null) {
            servantInstance.deregister();
            servantInstance = null;
        }
    }

    private void addServant() {
        if (servantInstance == null && servant != null)
            servantInstance = new ServantMob(this);
    }

    /*
     * when switching from modes
     */
    private void refreshServant() {
        removeServant();
        addServant();
    }

    public Servant getServant() {
        return servant;
    }

    public void callServant(boolean bellPull) {
        if (bellPull) {
            player.animate(3668);
            player.getMovement().lock(2);
        }
        if (servantInstance == null)
            player.message("The house has no servant.");
        else {
            servantInstance.setFollowing(true);
            servantInstance.setPosition(Utils.getFreeTile(player, 1));
            servantInstance.animate(858);
            player.getDialogueManager().startDialogue(new ServantD(), servantInstance);
        }
    }

    public ServantMob getServantInstance() {
        return servantInstance;
    }

    public void switchChallengeMode(boolean pvp) {
        if (isBuildMode())
            return;
        //if (isPVPMode()) {
        //	for (Player player : new ArrayList<Player>(players))
        //		player.setCanPvp(false);
        //}
        challengeMode = isChallengeMode() ? 0 : pvp ? 2 : 1;
        refreshChallengeMode();
        player.getMovement().lock(2);
    }

    public boolean isChallengeMode() {
        return challengeMode != 0;
    }

    public boolean isPVPMode() {
        return challengeMode == 2;
    }

    public void pullLeverChallengeMode(GameObject object) {
        if (isChallengeMode()) {
            player.message("You turn off " + (isPVPMode() ? "pvp" : "challenge") + " mode.");
            switchChallengeMode(false);
            sendPullLeverEmote(object);
        } else {
            player.getDialogueManager().startDialogue(new ChallengeModeLeverD(), object);
        }
    }

    public void sendPullLeverEmote(GameObject object) {
        player.getMovement().lock(2);
        player.animate(3611);
        World.sendObjectAnimation(object, new Animation(3612));
    }

    public void leverEffect(GameObject object) {
        sendPullLeverEmote(object);
        player.message("Nothing interesting happens hehe.");
    }

    public GameObject getDungeonTrap(Position toTile) {
        for (GameObject dungeonTrap : dungeonTraps) {
            if (dungeonTrap.sameAs(toTile))
                return dungeonTrap;
        }
        return null;
    }

    public Position getCenterTile(RoomReference rRef) {
        if (boundChuncks == null || rRef == null)
            return null;
        return new Position(boundChuncks[0] * 8 + rRef.x * 8 + 3, boundChuncks[1] * 8 + rRef.y * 8 + 3, rRef.getPlane());
    }

    public int getPaymentStage() {
        return paymentStage;
    }

    public void resetPaymentStage() {
        paymentStage = 0;
    }

    public void incrementPaymentStage() {
        paymentStage++;
    }
}
