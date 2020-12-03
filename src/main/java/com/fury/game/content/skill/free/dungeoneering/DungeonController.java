package com.fury.game.content.skill.free.dungeoneering;

import com.fury.core.model.node.entity.Entity;
import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.task.Task;
import com.fury.game.container.impl.shop.ShopManager;
import com.fury.game.content.controller.Controller;
import com.fury.game.content.dialogue.impl.skills.dungeoneering.*;
import com.fury.game.content.global.Achievements;
import com.fury.game.content.global.thievingguild.ThievingGuild;
import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.free.cooking.Consumables;
import com.fury.game.content.skill.free.dungeoneering.DungeonConstants.KeyDoors;
import com.fury.game.content.skill.free.dungeoneering.DungeonConstants.SkillDoors;
import com.fury.game.content.skill.free.dungeoneering.rooms.PuzzleRoom;
import com.fury.game.content.skill.free.dungeoneering.skills.DungeoneeringFarming;
import com.fury.game.content.skill.free.dungeoneering.skills.DungeoneeringFarming.Harvest;
import com.fury.game.content.skill.free.dungeoneering.skills.DungeoneeringMining;
import com.fury.game.content.skill.free.dungeoneering.skills.DungeoneeringTraps;
import com.fury.game.content.skill.free.mining.Mining;
import com.fury.game.content.skill.free.mining.data.Pickaxe;
import com.fury.game.content.skill.free.smithing.SmeltingData;
import com.fury.game.content.skill.free.woodcutting.Hatchet;
import com.fury.game.content.skill.free.woodcutting.Trees;
import com.fury.game.content.skill.free.woodcutting.Woodcutting;
import com.fury.game.content.skill.member.summoning.Infusion;
import com.fury.game.content.skill.member.summoning.SummoningType;
import com.fury.game.content.skill.member.summoning.impl.Summoning;
import com.fury.game.content.skill.member.thieving.Thieving;
import com.fury.game.entity.character.combat.CombatIcon;
import com.fury.game.entity.character.combat.Hit;
import com.fury.game.entity.character.combat.effects.Effect;
import com.fury.game.entity.character.combat.effects.Effects;
import com.fury.game.entity.character.combat.magic.MagicSpells;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.character.npc.impl.dungeoneering.*;
import com.fury.game.entity.character.player.link.transportation.TeleportHandler;
import com.fury.game.entity.character.player.link.transportation.TeleportType;
import com.fury.core.model.item.FloorItem;
import com.fury.core.model.item.Item;
import com.fury.game.entity.object.GameObject;
import com.fury.game.node.entity.actor.object.ObjectHandler;
import com.fury.game.node.entity.actor.figure.player.handles.Settings;
import com.fury.game.node.entity.actor.object.ObjectManager;
import com.fury.game.npc.familiar.Familiar;
import com.fury.game.world.FloorItemManager;
import com.fury.game.world.GameWorld;
import com.fury.game.world.World;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.game.world.update.flag.block.Direction;
import com.fury.game.world.update.flag.block.HitMask;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.network.packet.impl.DropItemPacketListener;
import com.fury.util.Misc;
import com.fury.util.TempKeys;

import java.util.Collection;

public class DungeonController extends Controller {

    private DungeonManager dungeon;
    private Position gatestone;
    // private int deaths;
    private int voteStage;
    private boolean killedBossWithLessThan10HP;
    private int damageReceived;
    private int meleeDamage, rangeDamage, mageDamage, mixedDamage;
    private int healedDamage;
    private boolean showBar;

    public void start() {
        //this.dungeon = dungeon;
        dungeon = (DungeonManager) getArguments()[0];
        setArguments(null); // because arguments save on char and we don't want to save manager
        showDeaths();
        refreshDeaths();
    }

    public void showDeaths() {
        //player.getInterfaceManager().sendMinigameInterface(945);
    }

    public void showBar() {
        //player.getPacketSender().sendHideIComponent(945, 2, !showBar);
    }

    public void hideBar() {
        showBar(false, null);
    }

    public void showBar(boolean show, String name) {
        if (showBar == show)
            return;
        showBar = show;
        showBar();
        //if (show)
        //player.getPacketSender().sendCSVarString(315, name);
    }

    public void sendBarPercentage(int percentage) {
        //player.getPacketSender().sendCSVarInteger(1233, percentage * 2);
    }

    public void reset() {
        //deaths = 0;
        voteStage = 0;
        gatestone = null;
        killedBossWithLessThan10HP = false;
        damageReceived = 0;
        meleeDamage = 0;
        rangeDamage = 0;
        mageDamage = 0;
        healedDamage = 0;
        refreshDeaths();
        showDeaths();
        hideBar();
        //player.getAppearence().setRenderEmote(-1);
    }

    @Override
    public boolean canMove(int dir) {
        VisibleRoom vr = dungeon.getVisibleRoom(dungeon.getCurrentRoomReference(player));
        Position to = new Position(player.getX() + Misc.DIRECTION_DELTA_X[dir], player.getY() + Misc.DIRECTION_DELTA_Y[dir], 0);
        if (vr != null && !vr.canMove(player, to)) {
            return false;
        }

        Room room = dungeon.getRoom(dungeon.getCurrentRoomReference(player));
        if (room != null) {
            if (room.getRoom() == DungeonUtils.getBossRoomWithChunk(DungeonConstants.FROZEN_FLOORS, 26, 624)) {
                if (!player.getMovement().isLocked() && ObjectManager.getObjectWithType(new Position(player.getX() + Misc.DIRECTION_DELTA_X[dir], player.getY() + Misc.DIRECTION_DELTA_Y[dir], 0), 22) == null) {
                    //player.getAppearence().setRenderEmote(1429);
                    player.getSettings().set(Settings.RUNNING, true);
                    player.getMovement().lock();
                }
                if (player.getMovement().isLocked()) {
                    Position nextStep = new Position(player.getX() + Misc.DIRECTION_DELTA_X[dir] * 2, player.getY() + Misc.DIRECTION_DELTA_Y[dir] * 2, 0);
                    Mob boss = getNPC(player, 9929);
                    boolean collides = boss != null && Misc.colides(nextStep.getX(), nextStep.getY(), player.getSizeX(), player.getSizeY(), boss.getX(), boss.getY(), boss.getSizeX(), boss.getSizeY());
                    //player.reset();
                    GameObject object = ObjectManager.getObjectWithType(new Position(nextStep.getX(), nextStep.getY(), 0), 22);
                    if (collides || ((object != null && (object.getId() == 49331 || object.getId() == 49333)) /*|| !player.addWalkSteps(nextStep.getChunkX(), nextStep.getChunkY(), 1)*/)) {
                        player.getMovement().unlock();
                        //player.getAppearence().setRenderEmote(-1);
                    }
                }
            }
        }
        return dungeon != null && !dungeon.isAtRewardsScreen();
    }

    public int getHealedDamage() {
        return healedDamage;
    }

    /*
     *the dmg you receiving
     */
    public void processIncomingHit(Hit hit) {
        damageReceived += hit.getDamage();
    }

    public int getDamageReceived() {
        return damageReceived;
    }

    public int getMeleeDamage() {
        return meleeDamage;
    }

    public int getRangeDamage() {
        return rangeDamage;
    }

    public int getMageDamage() {
        return mageDamage;
    }

    public int getDamage() {
        return meleeDamage + rangeDamage + mageDamage + mixedDamage;
    }

    public void sendInterfaces() {
        if (dungeon != null && dungeon.isAtRewardsScreen())
            return;
        showDeaths();
    }

    @Override
    public void processNPCDeath(Mob mob) {
        if (mob instanceof DungeonBoss) {
            if (player.getHealth().getHitpoints() <= 10)
                killedBossWithLessThan10HP = true;
        }
        for (int i = 0; i < DungeonConstants.HUNTER_CREATURES.length; i++)
            if (mob.getId() == DungeonConstants.HUNTER_CREATURES[i]) {
                Achievements.finishAchievement(player, Achievements.AchievementData.EVERYBODY_WALK_THE_DINOSAUR);
                break;
            }
    }

    @Override
    public boolean sendDeath() {
        player.getMovement().lock(8);
        player.stopAll();
        //player.getPacketSender().sendMusicEffectOld(418);
        if (player.getInventory().contains(DungeonConstants.GROUP_GATESTONE)) {
            Position tile = player.copyPosition();
            dungeon.setGroupGatestone(player.copyPosition());
            FloorItemManager.addGroundItem(DungeonConstants.GROUP_GATESTONE, tile);
            player.getInventory().delete(DungeonConstants.GROUP_GATESTONE);
            player.message("Your group gatestone drops to the floor as you die.");
        }
        if (player.getInventory().contains(DungeonConstants.GATESTONE)) {
            Position tile = player.copyPosition();
            setGatestone(player.copyPosition());
            FloorItemManager.addGroundItem(DungeonConstants.GATESTONE, tile, player);
            player.getInventory().delete(DungeonConstants.GATESTONE);
            player.message("Your gatestone drops to the floor as you die.");
        }
        increaseDeaths(player);
        hideBar();
        GameWorld.schedule(new Task(true, 1) {
            private int loop;

            @Override
            public void run() {
                if (loop == 0) {
                    player.animate(836);
                } else if (loop == 1) {
                    player.message("Oh dear, you are dead!");
                    if (dungeon != null) {
                        for (Player p2 : dungeon.getParty().getTeam()) {
                            if (p2 == player)
                                continue;
                            p2.message(player.getUsername() + " has died.");
                        }
                    }
                } else if (loop == 3) {
                    Effect previousOvlEffect = player.getEffects().getEffectForType(Effects.OVERLOAD);
                    Effect previousRenewalEffect = player.getEffects().getEffectForType(Effects.OVERLOAD);
                    player.reset();
                    if (previousOvlEffect != null)
                        player.getEffects().startEffect(new Effect(Effects.OVERLOAD, previousOvlEffect.getCycle()));
                    if (previousRenewalEffect != null)
                        player.getEffects().startEffect(new Effect(Effects.OVERLOAD, previousRenewalEffect.getCycle()));
                    if (dungeon != null && dungeon.getParty().getTeam().contains(player)) {
                        if (dungeon.isAtBossRoom(player, 26, 672, true)) {
                            Mob mob = getNPC(player, 11872);
                            if (mob != null) {
                                mob.forceChat("Another kill for the Thunderous!");
//                                npc.playSoundEffect(1928);
                            }
                        }
                        Position startRoom = dungeon.getHomeTile();
                        player.moveTo(startRoom, 0, Direction.SOUTH);
                        dungeon.playMusic(player, dungeon.getCurrentRoomReference(startRoom));
                        increaseDeaths(player);
                    }
                    player.animate(-1);
                    hideBar();
                } else if (loop == 4) {
                    stop();
                }
                loop++;
            }
        });
        return false;
    }

    private void refreshDeaths() {
        //player.getVarsManager().sendVarBit(2365, getDeaths()); //deaths
    }

    public void increaseDeaths(Player player) {
        Integer deaths = dungeon.getPartyDeaths().get(player.getUsername());
        if (deaths == null)
            deaths = 0;
        else if (deaths == 15)
            return;
        dungeon.getPartyDeaths().put(player.getUsername(), deaths + 1);
        //deaths++;
        refreshDeaths();
    }

    public int getDeaths(Player player) {
        if (dungeon == null)
            return 0;
        Integer deaths = dungeon.getPartyDeaths().get(player.getUsername());
        return deaths == null ? 0 : deaths;//deaths;
    }

    @Override
    public boolean processMagicTeleport(Position toTile) {
        return !(dungeon == null /*|| !player.getCombatDefinitions().isDungeonneringSpellBook()*/ || !dungeon.hasStarted() || dungeon.isAtRewardsScreen());
    }

    @Override
    public boolean processItemTeleport(Position toTile) {
        return false;
    }

    @Override
    public boolean canTakeItem(FloorItem item) {
        if (dungeon.isKeyShare()) {
            for (KeyDoors key : DungeonConstants.KeyDoors.values()) {
                if (item.getId() == key.getKeyId()) {
                    dungeon.setKey(key, true);
                    FloorItemManager.removeGroundItem(player, item, false);
                    return false;
                }
            }
        }
        if (item.isEqual(DungeonConstants.GROUP_GATESTONE)) {
            dungeon.setGroupGatestone(null);
            return true;
        } else if (item.isEqual(DungeonConstants.GATESTONE)) {
            if (item.getOwnerName().isEmpty()) {
                FloorItemManager.removeGroundItem(player, item);
                return false;
            } else if (!item.getOwnerName().equals(player.getUsername())) {
                player.message("This isn't your gatestone!");
                return false;
            }
            setGatestone(null);
            return true;
        } else if ((World.getMask(item.getTile().getX(), item.getTile().getY(), item.getTile().getZ()) & 0x1280120) != 0) {
            player.animate(833);
            player.getDirection().face(item.getTile());
            player.getMovement().lock(1);
        }
        return true;
    }

    private void openSkillDoor(Player player, final SkillDoors s, final GameObject object, final Room room, final int floorType) {
        final int index = room.getDoorIndexByRotation(object.getDirection());

        if (index == -1)
            return;

        final Door door = room.getDoor(index);

        if (door == null || door.getType() != DungeonConstants.SKILL_DOOR)
            return;
        if (!player.getSkills().hasRequirement(s.getSkill(), door.getLevel(), "remove this " + object.getDefinition().getName().toLowerCase()))
            return;
        int openAnim = -1;
        if (s.getSkill() == Skill.FIREMAKING) {
            if (!player.getInventory().contains(DungeonConstants.TINDERBOX)) {
                player.message("You need a tinderbox to do this.");
                return;
            }
        } else if (s.getSkill() == Skill.MINING) {
            Pickaxe defs = Mining.getPickaxe(player, true);
            if (defs == null) {
                player.message("You do not have a pickaxe that you can use.");
                return;
            }
            openAnim = defs.getAnimationId();
        } else if (s.getSkill() == Skill.WOODCUTTING) {
            Hatchet hatchet = Woodcutting.getHatchet(player, true);
            if (hatchet == null) {
                player.message("You don't have a hatchet that you can use.");
                return;
            }
            openAnim = hatchet.getEmoteId();
        }
        final boolean fail = Misc.random(100) <= 10;
        player.getMovement().lock(3);
        if (s.getOpenAnim() != -1)
            player.perform(new Animation(openAnim != -1 ? openAnim : fail && s.getFailAnim() != -1 ? s.getFailAnim() : s.getOpenAnim()));
        if (s.getOpenGfx() != -1 || s.getFailGfx() != -1)
            player.perform(new Graphic(fail ? s.getFailGfx() : s.getOpenGfx()));
        if (s.getOpenObjectAnim() != -1 && !fail)
            World.sendObjectAnimation(object, new Animation(s.getOpenObjectAnim()));

        GameWorld.schedule(2, () -> {
            if (s.getFailAnim() == -1)
                player.animate(-1);
            if (!fail) {
                if (room.getDoor(index) == null) //means someone else opened at same time
                    return;
                player.getSkills().addExperience(s.getSkill(), door.getLevel() * 5 + 10);
                room.setDoor(index, null);
                int openId = s.getOpenObject(floorType);
                if (openId == -1)
                    ObjectManager.removeObject(object);
                else
                    dungeon.setDoor(dungeon.getCurrentRoomReference(object), -1, openId, object.getDirection());
            } else {
                player.message(s.getFailMessage(), true);
                player.getCombat().applyHit(new Hit(door.getLevel() * 4, HitMask.RED, CombatIcon.NONE));
                if (room.getDoor(index) == null) //means someone else opened at same time
                    return;
                if (s.getFailObjectAnim() != -1)
                    World.sendObjectAnimation(object, new Animation(s.getFailObjectAnim()));
            }
        });
    }

    @Override
    public boolean processNPCClick1(final Mob mob) {
        if (dungeon == null || !dungeon.hasStarted() || dungeon.isAtRewardsScreen())
            return false;
        VisibleRoom vRoom = dungeon.getVisibleRoom(dungeon.getCurrentRoomReference(player));
        if (vRoom == null || !vRoom.processNPCClick1(player, mob)) {
            return false;
        }
        if (mob.getId() == DungeonConstants.SMUGGLER) {
            if (dungeon.getParty().getComplexity() > 1) {
                ShopManager.getShops().get(dungeon.getParty().getShop()).open(player);
            } else {
                player.getDialogueManager().startDialogue(new SmugglerD());
            }
            return false;
        } else if (mob.getId() == DungeonConstants.FISH_SPOT_NPC_ID) {
            //player.getActionManager().setAction(new DungeoneeringFishing((DungeonFishSpot) npc));
            return false;
        } else if (mob.getId() == 10023) {
            FrozenAdventurer adventurer = (FrozenAdventurer) mob;
            adventurer.getFrozenPlayer().resetTransformation();
            return false;
        } else if (mob.getId() == DungeonConstants.SMUGGLER) {
            mob.getDirection().face(player);
            //player.getDialogueManager().startDialogue(new SmugglerD", dungeon.getParty().getComplexity());
            return false;
        } else if (mob.getId() >= 11076 && mob.getId() <= 11085) {
            DungeoneeringTraps.removeTrap(player, (MastyxTrap) mob, dungeon);
            return false;
        } else if (mob.getId() >= 11096 && mob.getId() <= 11105) {
            DungeoneeringTraps.skinMastyx(player, mob);
            return false;
        } else if (mob instanceof DivineSkinweaver) {
            ((DivineSkinweaver) mob).talkTo(player);
            return false;
        }
        return true;
    }

    @Override
    public boolean processNPCClick2(final Mob mob) {
        if (dungeon == null || !dungeon.hasStarted() || dungeon.isAtRewardsScreen())
            return false;
        VisibleRoom room = dungeon.getVisibleRoom(dungeon.getCurrentRoomReference(player));
        if (room == null)
            return false;
        if (!room.processNPCClick2(player, mob))
            return false;

        if (mob.isFamiliar()) {
            Familiar familiar = (Familiar) mob;
            if (player.getFamiliar() != familiar) {
                player.message("That isn't your familiar.");
                return false;
            } else if (familiar.getDefinition().hasOption("Take")) {
                familiar.takeBob();
                return false;
            }
            return true;
        } else if (mob.getDefinition().hasOption("Mark")) {
            if (!dungeon.getParty().isLeader(player)) {
                player.message("Only your party's leader can mark a target!");
                return false;
            }

            dungeon.setMark(mob, true);
            return false;
        } else if (mob.getId() == DungeonConstants.SMUGGLER) {
            //DungeonResourceShop.openResourceShop(player, dungeon.getParty().getComplexity());
            return false;
        }
        return true;
    }

    @Override
    public boolean processNPCClick3(Mob mob) {
        if (dungeon == null || !dungeon.hasStarted() || dungeon.isAtRewardsScreen())
            return false;
        VisibleRoom room = dungeon.getVisibleRoom(dungeon.getCurrentRoomReference(player));
        if (room == null)
            return false;
        return room.processNPCClick3(player, mob);
    }

    public static Mob getNPC(Entity entity, int id) {
        for (Mob mob : GameWorld.getMobs().getMobs()) {
            if (mob != null) {
                if (mob.getId() == id) {
                    return mob;
                }
            }
        }
        return null;
    }

    public static Mob getNPC(Entity entity, String name) {
        Collection<Mob> mobs = GameWorld.getRegions().getRegion(entity.getRegionId()).getNpcs(entity.getZ());
        for (Mob mob : mobs)
            if (mob.getName().equals(name))
                return mob;
        return null;
    }

    @Override
    public boolean processObjectClick1(final GameObject object) {
        if (dungeon == null || !dungeon.hasStarted() || dungeon.isAtRewardsScreen())
            return false;

        RoomReference ref = dungeon.getCurrentRoomReference(player);
        Room room = dungeon.getRoom(ref);
        int oX = object.getX() - (dungeon.getDungeonTile().getX() + (ref.getX() * 16));
        int oY = object.getY() - (dungeon.getDungeonTile().getY() + (ref.getY() * 16));
        if (oX == 7 && oY == 15) object.setDirection(1);//North
        if (oX == 0 && oY == 7) object.setDirection(0);//West
        if (oX == 7 && oY == 0) object.setDirection(3);//South
        if (oX == 15 && oY == 7) object.setDirection(2);//East

        //System.out.println("Face: " + object.getFace());
        VisibleRoom vr = dungeon.getVisibleRoom(ref);
        if (vr == null || !vr.processObjectClick1(player, object))
            return false;
        int floorType = DungeonUtils.getFloorType(dungeon.getParty().getFloor());

        for (SkillDoors s : SkillDoors.values()) {
            if (s.getClosedObject(floorType) == object.getId()) {
                openSkillDoor(player, s, object, room, floorType);
                return false;
            }
        }
        if (object.getId() == 49268) {
            Pickaxe defs = Mining.getPickaxe(player, true);
            if (defs == null) {
                player.message("You do not have a pickaxe or do not have the required level to use the pickaxe.");
                return false;
            }
            player.perform(new Animation(defs.getAnimationId()));
            player.getMovement().lock(4);
            GameWorld.schedule(3, () -> {
                player.animate(-1);
                ObjectManager.removeObject(object);
            });
            return false;
        } else if (object.getId() >= 49286 && object.getId() <= 49288) {
            Mob boss = getNPC(player, 10058);
            if (boss != null)
                ((DivineSkinweaver) boss).blockHole(player, object);
            return false;
        } else if (object.getId() == 49297) {
            Integer value = (Integer) player.getTemporaryAttributes().get(TempKeys.UNHOLY_CURSEBEARER_ROT);
            if (value != null && value >= 6) {
                Mob boss = getNPC(player, 10111);
                if (boss != null) {
                    player.message("You restore your combat stats, and the skeletal archmage is healed in the process. The font lessens the effect of the rot within your body.");
                    player.getTemporaryAttributes().put(TempKeys.UNHOLY_CURSEBEARER_ROT, 1);
                    player.getSkills().reset();
                    boss.getHealth().heal(boss.getCombatDefinition().getHitpoints() / 10);
                }
            } else
                player.message("You can't restore your stats yet.");
            return false;
        } else if (object.getId() >= KeyDoors.getLowestObjectId() && object.getId() <= KeyDoors.getMaxObjectId()) {
            int index = room.getDoorIndexByRotation(object.getDirection());
            if (index == -1)
                return false;
            Door door = room.getDoor(index);
            if (door == null || door.getType() != DungeonConstants.KEY_DOOR)
                return false;
            KeyDoors key = KeyDoors.values()[door.getId()];
            if (!dungeon.hasKey(key) && !player.getInventory().contains(new Item(key.getKeyId()))) {
                player.message("You don't have the correct key.");
                return false;
            }
            player.getInventory().delete(new Item(key.getKeyId()));
            player.getMovement().lock(1);
            player.message("You unlock the door.", true);
            player.animate(13798);// unlock key
            dungeon.setKey(key, false);
            room.setDoor(index, null);
            ObjectManager.removeObject(object);
            return false;
        } else if (object.getId() == DungeonConstants.DUNGEON_DOORS[floorType] || object.getId() == DungeonConstants.DUNGEON_GUARDIAN_DOORS[floorType] || object.getId() == DungeonConstants.DUNGEON_BOSS_DOORS[floorType] || DungeonUtils.isOpenSkillDoor(object.getId(), floorType) || (object.getId() >= KeyDoors.getLowestDoorId(floorType) && object.getId() <= KeyDoors.getMaxDoorId(floorType)) || (object.getDefinition().getName().equals("Door") && object.getDefinition().containsOption(0, "Enter"))) {//theres many ids for challenge doors
            if (object.getId() == DungeonConstants.DUNGEON_BOSS_DOORS[floorType] && player.getCombat().isInCombat()) {
                player.message("This door is too complex to unlock while in combat.");
                return false;
            }
            Door door = room.getDoorByRotation(object.getDirection());
            //if ((room.getDoorDirections()[i] + this.rotation & 0x3) == rotation)
            if (door == null) {
                openDoor(player, object);
                return false;
            }
            if (door.getType() == DungeonConstants.GUARDIAN_DOOR)
                player.message("The door won't unlock until all of the guardians in the room have been slain.");
            else if (door.getType() == DungeonConstants.KEY_DOOR || door.getType() == DungeonConstants.SKILL_DOOR)
                player.message("The door is locked.");
            else if (door.getType() == DungeonConstants.CHALLENGE_DOOR)
                player.message(((PuzzleRoom) vr).getLockMessage());
            return false;
        } else if (object.getId() == DungeonConstants.THIEF_CHEST_LOCKED[floorType]) {
            room = dungeon.getRoom(dungeon.getCurrentRoomReference(player));
            int type = room.getThiefChest();
            if (type == -1)
                return false;
            int level = type == 0 ? 1 : (type * 10);
            if (!player.getSkills().hasRequirement(Skill.THIEVING, level, "open this chest"))
                return false;

            room.setThiefChest(-1);
            player.animate(536);
            player.getMovement().lock(2);
            GameObject openedChest = new GameObject(object);
            openedChest.setId(DungeonConstants.THIEF_CHEST_OPEN[floorType]);
            ObjectManager.spawnObject(openedChest);
            player.getInventory().add(new Item(DungeonConstants.RUSTY_COINS, Misc.random((type + 1) * 10000) + 1));
            if (Misc.random(2) == 0)
                player.getInventory().add(new Item(DungeonConstants.CHARMS[Misc.random(DungeonConstants.CHARMS.length)], Misc.random(5) + 1));
            if (Misc.random(3) == 0)
                player.getInventory().add(new Item(DungeoneeringFarming.getHerbForLevel(level), Misc.random(1) + 1));
            if (Misc.random(4) == 0)
                player.getInventory().add(new Item(DungeonUtils.getArrows(type + 1), Misc.random(100) + 1));
            if (Misc.random(5) == 0)
                player.getInventory().add(new Item(DungeonUtils.getRandomWeapon(type + 1), 1));
            player.getSkills().addExperience(Skill.THIEVING, DungeonConstants.THIEF_CHEST_XP[type]);
            return false;
        } else if (object.getId() == DungeonConstants.THIEF_CHEST_OPEN[floorType]) {
            player.message("You already looted this chest.");
            return false;
        } else if (DungeonUtils.isLadder(object.getId(), floorType)) {
            if (voteStage != 0) {
                player.message("You have already voted to move on.");
                return false;
            }
            if (player.getDungManager().getParty().getTeam().size() == 1)
                this.dungeon.voteToMoveOn(player);
            else
                player.getDialogueManager().startDialogue(new DungeonClimbLadder(), this);
            return false;
        } else if (object.getId() == 53977 || object.getId() == 53978 || object.getId() == 53979) {
            int type = object.getId() == 53977 ? 0 : object.getId() == 53979 ? 1 : 2;
            Mob boss = getNPC(player, "Runebound behemoth");
//            System.out.println("Boss: " + boss);
            if (boss != null)
                ((RuneboundBehemoth) boss).activateArtifact(object, type);
            return false;
        }
        String name = object.getDefinition().getName().toLowerCase();
        switch (name) {
            case "dungeon exit":
                player.getDialogueManager().startDialogue(new DungeonExit(), this);
                return false;
            case "water trough":
                //WaterFilling.isFilling(player, 17490, false);
                Item empty = new Item(17490), full = new Item(17492);
                if (player.getInventory().getAmount(empty) == 0) {
                    player.message("You don't have any " + empty.getName().toLowerCase() + " to fill.");
                } else if (player.getInventory().getAmount(empty) >= 1) {
                    while (player.getInventory().getAmount(empty) >= 1) {
                        player.getInventory().delete(empty);
                        player.getInventory().add(full);
                        player.animate(832);
                        String vialName = new Item(17490).getName();
                        if (name.contains(" ("))
                            name = name.substring(0, name.indexOf(" ("));
                        player.message("You fill the " + name + ".", true);
                    }
                }
                return false;
            case "salve nettles":
                DungeoneeringFarming.initHarvest(player, Harvest.SALVE_NETTLES, object);
                return false;
            case "wildercress":
                DungeoneeringFarming.initHarvest(player, Harvest.WILDERCRESS, object);
                return false;
            case "blightleaf":
                DungeoneeringFarming.initHarvest(player, Harvest.BLIGHTLEAF, object);
                return false;
            case "roseblood":
                DungeoneeringFarming.initHarvest(player, Harvest.ROSEBLOOD, object);
                return false;
            case "bryll":
                DungeoneeringFarming.initHarvest(player, Harvest.BRYLL, object);
                return false;
            case "duskweed":
                DungeoneeringFarming.initHarvest(player, Harvest.DUSKWEED, object);
                return false;
            case "soulbell":
                DungeoneeringFarming.initHarvest(player, Harvest.SOULBELL, object);
                return false;
            case "ectograss":
                DungeoneeringFarming.initHarvest(player, Harvest.ECTOGRASS, object);
                return false;
            case "runeleaf":
                DungeoneeringFarming.initHarvest(player, Harvest.RUNELEAF, object);
                return false;
            case "spiritbloom":
                DungeoneeringFarming.initHarvest(player, Harvest.SPIRITBLOOM, object);
                return false;
            case "tangle gum tree":
                player.getActionManager().setAction(new Woodcutting(object, Trees.TANGLE_GUM_VINE));
                return false;
            case "seeping elm tree":
                player.getActionManager().setAction(new Woodcutting(object, Trees.SEEPING_ELM_TREE));
                return false;
            case "blood spindle tree":
                player.getActionManager().setAction(new Woodcutting(object, Trees.BLOOD_SPINDLE_TREE));
                return false;
            case "utuku tree":
                player.getActionManager().setAction(new Woodcutting(object, Trees.UTUKU_TREE));
                return false;
            case "spinebeam tree":
                player.getActionManager().setAction(new Woodcutting(object, Trees.SPINEBEAM_TREE));
                return false;
            case "bovistrangler tree":
                player.getActionManager().setAction(new Woodcutting(object, Trees.BOVISTRANGLER_TREE));
                return false;
            case "thigat tree":
                player.getActionManager().setAction(new Woodcutting(object, Trees.THIGAT_TREE));
                return false;
            case "corpsethorn tree":
                player.getActionManager().setAction(new Woodcutting(object, Trees.CORPESTHORN_TREE));
                return false;
            case "entgallow tree":
                player.getActionManager().setAction(new Woodcutting(object, Trees.ENTGALLOW_TREE));
                return false;
            case "grave creeper tree":
                player.getActionManager().setAction(new Woodcutting(object, Trees.GRAVE_CREEPER_TREE));
                return false;
            case "novite ore":
                player.getActionManager().setAction(new DungeoneeringMining(object, DungeoneeringMining.DungeoneeringRocks.NOVITE_ORE));
                return false;
            case "bathus ore":
                player.getActionManager().setAction(new DungeoneeringMining(object, DungeoneeringMining.DungeoneeringRocks.BATHUS_ORE));
                return false;
            case "marmaros ore":
                player.getActionManager().setAction(new DungeoneeringMining(object, DungeoneeringMining.DungeoneeringRocks.MARMAROS_ORE));
                return false;
            case "kratonite ore":
                player.getActionManager().setAction(new DungeoneeringMining(object, DungeoneeringMining.DungeoneeringRocks.KRATONIUM_ORE));
                return false;
            case "fractite ore":
                player.getActionManager().setAction(new DungeoneeringMining(object, DungeoneeringMining.DungeoneeringRocks.FRACTITE_ORE));
                return false;
            case "zephyrium ore":
                player.getActionManager().setAction(new DungeoneeringMining(object, DungeoneeringMining.DungeoneeringRocks.ZEPHYRIUM_ORE));
                return false;
            case "argonite ore":
                player.getActionManager().setAction(new DungeoneeringMining(object, DungeoneeringMining.DungeoneeringRocks.AGRONITE_ORE));
                return false;
            case "katagon ore":
                player.getActionManager().setAction(new DungeoneeringMining(object, DungeoneeringMining.DungeoneeringRocks.KATAGON_ORE));
                return false;
            case "gorgonite ore":
                player.getActionManager().setAction(new DungeoneeringMining(object, DungeoneeringMining.DungeoneeringRocks.GORGONITE_ORE));
                return false;
            case "promethium ore":
                player.getActionManager().setAction(new DungeoneeringMining(object, DungeoneeringMining.DungeoneeringRocks.PROMETHIUM_ORE));
                return false;
            case "furnace":
                player.getDialogueManager().startDialogue(new DungSmeltingD());
                return false;
            case "runecrafting altar":
                player.getDialogueManager().startDialogue(new DungRunecraftingD(), 0);
                return false;
            case "spinning wheel":
                //player.getDialogueManager().startDialogue(new SpinningD", true);
                return false;
            case "summoning obelisk":
                Infusion.openInfuseInterface(player, false, SummoningType.DUNGEONEERING);
                return false;
            case "group gatestone portal":
                portalGroupStoneTeleport(player);
                return false;
            case "web":
                if (object.getDefinition().containsOption(0, "Slash")) {
                    player.animate(451);
                    ObjectHandler.slashWeb(player, object);
                }
                return false;
            case "gate":
            case "large door":
            case "metal door":
                if (object.getDefinition().containsOption(0, "Open"))
                    //if (!ObjectHandler.handleGate(player, gameObject))
                    ObjectHandler.handleDoor(object);
                return false;
            case "door":
                if ((object.getDefinition().containsOption(0, "Open") || object.getDefinition().containsOption(0, "Unlock"))) {
                    ObjectHandler.handleDoor(object);
                    Achievements.finishAchievement(player, Achievements.AchievementData.OPEN_THE_DOOR);
                }
                return false;
            default:
                return true;
        }
    }

    @Override
    public boolean processObjectClick2(final GameObject object) {
        if (dungeon == null || !dungeon.hasStarted() || dungeon.isAtRewardsScreen())
            return false;

        VisibleRoom vr = dungeon.getVisibleRoom(dungeon.getCurrentRoomReference(player));
        if (vr == null || !vr.processObjectClick2(player, object))
            return false;
        if (object.getId() >= DungeonConstants.FARMING_PATCH_BEGINING && object.getId() <= DungeonConstants.FARMING_PATCH_END) {
            if (object.getDefinition().containsOption("Inspect"))
                return false;
            Harvest harvest = Harvest.values()[((object.getId() - 50042) / 3)];
            if (harvest == null)
                return false;
            DungeoneeringFarming.initHarvest(player, harvest, object);
            return false;
        }
        String name = object.getDefinition().getName().toLowerCase();
        switch (name) {
            case "runecrafting altar":
                player.getDialogueManager().startDialogue(new DungRunecraftingD(), 1);
                return false;
            case "summoning obelisk":
                Summoning.renewSummoningPoints(player);
                return false;
        }
        return true;
    }

    @Override
    public boolean processObjectClick3(GameObject object) {
        if (dungeon == null || !dungeon.hasStarted() || dungeon.isAtRewardsScreen())
            return false;
        VisibleRoom vr = dungeon.getVisibleRoom(dungeon.getCurrentRoomReference(player));
        if (vr == null || !vr.processObjectClick4(player, object))
            return false;
        if (object.getId() >= DungeonConstants.FARMING_PATCH_BEGINING && object.getId() <= DungeonConstants.FARMING_PATCH_END) {
            DungeoneeringFarming.clearHarvest(player, object);
            return false;
        }
        String name = object.getDefinition().getName().toLowerCase();
        switch (name) {
            case "runecrafting altar":
                player.getDialogueManager().startDialogue(new DungRunecraftingD(), 2);
                return false;
        }
        return true;
    }

    @Override
    public boolean processObjectClick4(final GameObject object) {
        if (dungeon == null || !dungeon.hasStarted() || dungeon.isAtRewardsScreen())
            return false;
        if (!dungeon.getVisibleRoom(dungeon.getCurrentRoomReference(player)).processObjectClick4(player, object)) {
            return false;
        }
        String name = object.getDefinition().getName().toLowerCase();
        switch (name) {
            case "runecrafting altar":
                player.getDialogueManager().startDialogue(new DungRunecraftingD(), 3);
                return false;
        }
        return true;
    }

    @Override
    public boolean processObjectClick5(final GameObject object) {
        if (dungeon == null || !dungeon.hasStarted() || dungeon.isAtRewardsScreen())
            return false;
        VisibleRoom vr = dungeon.getVisibleRoom(dungeon.getCurrentRoomReference(player));
        if (vr == null || !vr.processObjectClick5(player, object))
            return false;
        String name = object.getDefinition().getName().toLowerCase();
        switch (name) {
            case "runecrafting altar":
                player.getDialogueManager().startDialogue(new DungRunecraftingD(), 4);
                return false;
        }
        return true;
    }

    public void leaveDungeon(Player player) {
        if (dungeon == null || !dungeon.hasStarted())
            return;
        dungeon.getParty().leaveParty(player, false, true);
    }

    public void voteToMoveOn(Player player) {
        if (dungeon == null || !dungeon.hasStarted() || voteStage != 0)
            return;
        //voteStage = 1;
        dungeon.voteToMoveOn(player);
    }


    @Override
    public boolean handleItemOnObject(GameObject object, Item item) {
        if (dungeon == null || !dungeon.hasStarted() || dungeon.isAtRewardsScreen())
            return false;
        VisibleRoom room = dungeon.getVisibleRoom(dungeon.getCurrentRoomReference(player));
        if (room == null)
            return false;
        if (!room.handleItemOnObject(player, object, item)) {
            return false;
        }
        String name = object.getDefinition().getName().toLowerCase();
        switch (name) {
            case "farming patch":
                DungeoneeringFarming.plantHarvest(item, player, object, dungeon);
                return true;
            case "furnace":
                for (SmeltingData bar : DungeonConstants.SMELTING_BARS) {
                    if (bar.getOre1() == item.getId()) {
                        player.getDialogueManager().startDialogue(new DungSmeltingD(), bar);
                        break;
                    }
                }
                return true;
            case "anvil":
				/*for (int index = 0; index < Smithing.BARS[1].length; index++) {
					if (Smithing.BARS[1][index] == item.getScrollId()) {
						Smithing.sendForgingInterface(player, index, true);
						break;
					}
				}*/
                return true;
        }
        return true;
    }

    @Override
    public boolean canUseItemOnItem(Item itemUsed, Item usedWith) {
        if (dungeon == null || !dungeon.hasStarted() || dungeon.isAtRewardsScreen())
            return false;
		/*if (itemUsed.getScrollId() == LeatherCraftingD.DUNG_NEEDLE || usedWith.getScrollId() == LeatherCraftingD.DUNG_NEEDLE) {
			int leatherIndex = LeatherCraftingD.getIndex(itemUsed.getScrollId());
			if (leatherIndex == -1)
				leatherIndex = LeatherCraftingD.getIndex(usedWith.getScrollId());
			if (leatherIndex != -1) {
				player.getDialogueManager().startDialogue(new LeatherCraftingD", leatherIndex, true);
				return true;
			}
		} else if (WeaponPoison.poison(player, itemUsed, usedWith, true))*/
        return false;
    }

    public void openDoor(Player player, GameObject object) {
        RoomReference roomReference = dungeon.getCurrentRoomReference(player);
        if (dungeon.enterRoom(player, object, roomReference.getX() + Misc.ROTATION_DIR_X[object.getDirection()], roomReference.getY() + Misc.ROTATION_DIR_Y[object.getDirection()]))
            hideBar();
    }

    /**
     * called once teleport is performed
     */
    public void magicTeleported(TeleportType type) {
        //dungeon.playMusic(player, dungeon.getCurrentRoomReference(player.getNextPosition()));
        hideBar();
    }

    @Override
    public boolean keepCombating(Figure target) {
        if (target instanceof DungeonSlayerMob) {
            DungeonSlayerMob npc = (DungeonSlayerMob) target;

            for (int index = 0; index < DungeonConstants.SLAYER_CREATURES.length; index++) {
                if (npc.getId() == DungeonConstants.SLAYER_CREATURES[index]) {
                    int level = DungeonConstants.SLAYER_LEVELS[index];

                    if (!player.getSkills().hasRequirement(Skill.SLAYER, level, "attack this monster"))
                        return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean processButtonClick(int interfaceId) {
        switch (interfaceId) {
            case 63080:
                stoneTeleport(player, false);
                return false;
            case 27132:
                player.getDungManager().openResetProgress();
                return false;
            case 26244:
                player.getDungManager().changeFloor();
                break;
            case 26247:
                player.getDungManager().changeComplexity();
                break;
            case 26229:
                player.getDungManager().checkLeaveParty();
                break;
            case 62901:
                DungeonManager dungeon = player.getDungManager().getParty().getDungeon();
                TeleportHandler.teleportPlayer(player, dungeon.getHomeTile().copyPosition(), player.getSpellbook().getTeleportType(), true);
                return false;
        }
        return true;
    }

    @Override
    public boolean canDropItem(Item item) {
        if (item.getId() == 15707) {
            player.message("You cannot destroy that here.");
            return false;
        }
        if (item.getName().contains("(b)")) {
            DropItemPacketListener.destroyItemInterface(player, item);
            return false;
        }
        if (!item.tradeable())
            return true;
        Item dropped = new Item(item.getId(), item.getAmount());
        player.getInventory().delete(item);
        Position currentTile = player.copyPosition();
        player.stopAll(false);
        if (dropped.isEqual(DungeonConstants.GROUP_GATESTONE))
            dungeon.setGroupGatestone(currentTile);
        else if (dropped.isEqual(DungeonConstants.GATESTONE)) {
            setGatestone(currentTile);
            FloorItemManager.addGroundItem(dropped, player.copyPosition(), player, true, -1, true, -1);
            player.message("You place the gatestone. You can teleport back to it at any time.");
            return false;
        }
        FloorItemManager.addGroundItem(dropped, player.copyPosition(), player, true, -1, false, -1);
        return false;
    }

    public void stoneTeleport(Player player, boolean group) {
        Position tile = group ? dungeon.getGroupGatestone() : gatestone;

        if (tile == null) { // Shouldn't happen for group gatestone
            if (group)//Temp fix
                player.getInventory().add(new Item(17792, 4));
            player.message("You currently do not have an active gatestone.");
            return;
        }
        if (!group) {
            FloorItem item = GameWorld.getRegions().get(gatestone.getRegionId()).getFloorItem(DungeonConstants.GATESTONE.getId(), tile, player);
            if (item == null)
                return;
            FloorItemManager.removeGroundItem(player, item, false);
            player.getInventory().delete(DungeonConstants.GATESTONE.getId());
            setGatestone(null);
        }
        TeleportHandler.teleportPlayer(player, tile, TeleportType.DUNGEONEERING_TELE, true);
    }

    private void portalGroupStoneTeleport(Player player) {
        Position tile = dungeon.getGroupGatestone();
        if (tile == null) //cant happen
            return;
        TeleportHandler.teleportPlayer(player, tile, TeleportType.DUNGEONEERING_GATESTONE_PORTAL, true);
    }

    public boolean canCreateGatestone(Player player) {
        if (player.getInventory().contains(DungeonConstants.GATESTONE)) {
            player.message("You already have a gatestone in your pack. Making another would be pointless.");
            return false;
        } else if (gatestone != null) {
            player.message("You already have an active gatestone.");
            return false;
        }
        return true;
    }

    public void leaveDungeonPermanently(Player player) {
        int index = dungeon.getParty().getIndex(player);
        leaveDungeon(player);
        //for (Player p2 : dungeon.getParty().getTeam())
        //p2.getPacketSender().sendCSVarInteger(1397 + index, 2);
    }

    @Override
    public boolean processItemOnPlayer(Player target, Item item, int slot) {
        Consumables.handleHealOtherAction(player, target, item, slot);
        return false;
    }

    @Override
    public void forceClose() {
        if (dungeon != null)
            dungeon.getParty().leaveParty(player, false, false);
        else { //rollback prevent taking items
            player.getInventory().clear();
            player.getEquipment().clear();
            if (player.getFamiliar() != null)
                player.getFamiliar().sendDeath(player);
        }
    }

    @Override
    public boolean logout() {
        return false; //else removes controller
    }

    @Override
    public boolean handleMagicSpells(MagicSpells spell) {
        switch (spell) {
            case CREATE_GATESTONE:
                if (player.getInventory().getSpaces() <= 0) {
                    player.getInventory().full();
                    return false;
                }

                if (!player.getTimers().getClickDelay().elapsed(1000))
                    return false;
                player.getTimers().getClickDelay().reset();

                if (!player.getDungManager().isInside())
                    return false;

                if (!canCreateGatestone(player))
                    return false;

                Item[] items = spell.getSpell().getItems().orElse(new Item[0]);
                if (player.getDungManager().isInside())
                    items = player.getDungManager().handleDungRunes(player, items);
                player.getInventory().delete(items);

                Achievements.finishAchievement(player, Achievements.AchievementData.CREATE_A_GATESTONE);
                player.animate(713);
                player.graphic(113);
                player.getSkills().addExperience(Skill.MAGIC, spell.getSpell().getExperience());
                player.getInventory().add(DungeonConstants.GATESTONE);
                break;
            case GROUP_GATESTONE_TELEPORT:
                if (!player.getTimers().getClickDelay().elapsed(1000))
                    return false;
                player.getTimers().getClickDelay().reset();

                if (!player.getDungManager().isInside())
                    return false;

                items = spell.getSpell().getItems().orElse(new Item[0]);
                if (player.getDungManager().isInside())
                    items = player.getDungManager().handleDungRunes(player, items);
                player.getInventory().delete(items);
                stoneTeleport(player, true);
                break;
        }
        return false;
    }

    private void setGatestone(Position gatestone) {
        this.gatestone = gatestone;
    }

    public boolean isKilledBossWithLessThan10HP() {
        return killedBossWithLessThan10HP;
    }
}
