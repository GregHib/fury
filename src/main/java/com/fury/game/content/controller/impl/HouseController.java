package com.fury.game.content.controller.impl;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.task.Task;
import com.fury.game.GameSettings;
import com.fury.game.container.impl.equip.Slot;
import com.fury.game.content.controller.Controller;
import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.impl.skills.construction.*;
import com.fury.game.content.dialogue.impl.skills.cooking.CookingD;
import com.fury.game.content.dialogue.impl.skills.prayer.BonesOnAlterD;
import com.fury.game.content.global.dnd.star.ShootingStar;
import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.free.cooking.Cookables;
import com.fury.game.content.skill.free.cooking.Cooking;
import com.fury.game.content.skill.free.prayer.Bone;
import com.fury.game.content.skill.free.prayer.BoneOffering;
import com.fury.game.content.skill.member.construction.House;
import com.fury.game.content.skill.member.construction.HouseConstants;
import com.fury.game.content.skill.member.construction.TabletMaking;
import com.fury.game.entity.character.combat.CombatIcon;
import com.fury.game.entity.character.combat.Hit;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.character.npc.impl.construction.ServantMob;
import com.fury.game.entity.character.player.actions.PlayerCombatAction;
import com.fury.game.entity.character.player.actions.SitChair;
import com.fury.game.entity.character.player.link.transportation.TeleportHandler;
import com.fury.game.entity.character.player.link.transportation.TeleportType;
import com.fury.core.model.item.Item;
import com.fury.game.entity.object.GameObject;
import com.fury.game.node.entity.actor.figure.player.PlayerDeath;
import com.fury.game.node.entity.actor.object.ObjectHandler;
import com.fury.game.node.entity.actor.object.TempObjectManager;
import com.fury.game.world.FloorItemManager;
import com.fury.game.world.GameWorld;
import com.fury.game.world.World;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.game.world.update.flag.block.HitMask;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.game.world.update.flag.block.graphic.GraphicHeight;
import com.fury.util.Misc;
import com.fury.util.Utils;

public class HouseController extends Controller {

    private transient byte ringType;
    private House house;

    @Override
    public void start() {
        this.house = (House) getArguments()[0];
        getArguments()[0] = null; //its was gonna be saved unless somehow in a server restart but lets be safe
    }

    /**
     * return process normaly
     */
    @Override
    public boolean processObjectClick5(GameObject object) {
        if (object.getDefinition().containsOption(4, "Build")) {
            if (!house.isOwner(player)) {
                player.message("You can only do that in your own house.");
                return false;
            }
            if (house.isDoor(object)) {
                house.openRoomCreationMenu(object);
            } else {
                for (HouseConstants.Builds build : HouseConstants.Builds.values()) {
                    if (build.containsId(object.getId())) {
                        house.openBuildInterface(object, build);
                        return false;
                    }

                }
            }
        } else if (object.getDefinition().containsOption("Remove")) {
            if (!house.isOwner(player)) {
                player.message("You can only do that in your own house.");
                return false;
            }
            house.openRemoveBuild(object);
        }
        return false;
    }

    @Override
    public boolean processNPCClick1(Mob mob) {
        if (mob instanceof ServantMob) {
            mob.getDirection().setDirection(player.getDirection().getDirection());
            if (!house.isOwner(player)) {
                player.getDialogueManager().sendNPCDialogue(mob.getId(), Expressions.NORMAL, "Sorry, I only serve my master.");
                return false;
            }
            player.getDialogueManager().startDialogue(new NewServantD(), mob, false);
            return false;
        }
        return true;
    }

    @Override
    public boolean processNPCClick2(Mob mob) {
        if (mob instanceof ServantMob) {
            mob.getDirection().setDirection(player.getDirection().getDirection());
            if (!house.isOwner(player)) {
                player.getDialogueManager().sendNPCDialogue(mob.getId(), Expressions.NORMAL, "The servant ignores your request.");
                return false;
            }
            player.getDialogueManager().startDialogue(new NewServantD(), mob, true);
            return false;
        }
        return true;
    }

    @Override
    public boolean processItemOnNPC(Mob mob, Item item) {
        if (mob instanceof ServantMob) {
            mob.getDirection().setDirection(player.getDirection().getDirection());
            if (!house.isOwner(player)) {
                player.getDialogueManager().sendNPCDialogue(mob.getId(), Expressions.NORMAL, "The servant ignores your request.");
                return false;
            }
            player.getDialogueManager().startDialogue(new ItemOnServantD(), mob, item, house.getServant().isSawmill());
            return false;
        }
        return false;
    }

    @Override
    public boolean processObjectClick1(GameObject object) {
        if (object.getId() == HouseConstants.HObject.EXIT_PORTAL.getId())
            house.leaveHouse(player, House.KICKED);
        else if (object.getId() == HouseConstants.HObject.CLAY_FIREPLACE.getId() || object.getId() == HouseConstants.HObject.STONE_FIREPLACE.getId() || object.getId() == HouseConstants.HObject.MARBLE_FIREPLACE.getId()) {
            if (house.isBuildMode()) {
                player.message("You cannot do this while in building mode.");
                return false;
            } else if (!player.getInventory().contains(new Item(1511))) {
                player.message("You need some ordinary logs to light the fireplace.");
                return false;
            } else if (!player.getInventory().contains(new Item(590))) {
                player.message("You do not have the required items to light this.");
                return false;
            }
            player.getMovement().lock(2);
            player.animate(3658);
            player.getInventory().delete(new Item(1511, 1));
            player.getSkills().addExperience(Skill.FIREMAKING, 40);
            GameObject objectR = new GameObject(object);
            objectR.setId(object.getId() + 1);
            TempObjectManager.spawnObjectTemporary(objectR, 60000, true, true); //lights for 1min
        } else if (object.getId() == HouseConstants.HObject.OAK_LARDER.getId() || object.getId() == HouseConstants.HObject.TEAK_LARDER.getId() || object.getId() == HouseConstants.HObject.WOODEN_LARDER.getId()) {
            player.getDialogueManager().startDialogue(new LarderD(), object.getId());
        } else if (object.getId() == HouseConstants.HObject.OAK_CLOCK.getId() || object.getId() == HouseConstants.HObject.TEAK_CLOCK.getId() || object.getId() == HouseConstants.HObject.GILDED_CLOCK.getId()) {
            player.message("The clock reads: " + Misc.getCurrentServerTime());
        } else if (HouseConstants.Builds.TOOL_STORE.containsObject(object)) {
            player.getDialogueManager().startDialogue(new ToolsD(), object.getId());
        } else if (HouseConstants.Builds.SHELVES.containsObject(object) || HouseConstants.Builds.SHELVES_2.containsObject(object)) {
            player.getDialogueManager().startDialogue(new ShelvesD(), object.getId());
        } else if (HouseConstants.Builds.DRESSERS.containsObject(object)) {
            player.getAppearance().openInterface();
        } else if (HouseConstants.Builds.WARDROBE.containsObject(object)) {
            player.getAppearance().openInterface();
        } else if (object.getId() >= HouseConstants.HObject.CRUDE_WOODEN_CHAIR.getId() && object.getId() <= HouseConstants.HObject.MAHOGANY_ARMCHAIR.getId()) {
            int chair = object.getId() - HouseConstants.HObject.CRUDE_WOODEN_CHAIR.getId();
            player.getActionManager().setAction(new SitChair(player, chair, object));
        } else if (object.getId() >= HouseConstants.HObject.WOOD_BENCH.getId() && object.getId() <= HouseConstants.HObject.GILDED_BENCH.getId()) {
            int bench = object.getId() - HouseConstants.HObject.WOOD_BENCH.getId();
            player.getActionManager().setAction(new SitChair(player, bench + 6, object));
        } else if (HouseConstants.Builds.WEAPONS_RACK.containsObject(object)) {
            int length = object.getId() == 13383 ? 5 : object.getId() == 13382 ? 4 : 2;
            player.getDialogueManager().startDialogue(new WeaponsRackD(), length);
        } else if (object.getId() >= HouseConstants.HObject.OAK_THRONE.getId() && object.getId() <= HouseConstants.HObject.DEMONIC_THRONE.getId()) {
            int throne = object.getId() - HouseConstants.HObject.OAK_THRONE.getId();
            player.getActionManager().setAction(new SitChair(player, throne + 12, object));
            return false;
        } else if (HouseConstants.Builds.DUNGEON_DOOR1.containsObject(object) || HouseConstants.Builds.DUNGEON_DOOR2.containsObject(object)) {
            if (!house.isOwner(player) && house.isChallengeMode()) {
                player.message("The door is locked!");
                return false;
            }
            passDoor(object);
            return false;
        } else if(HouseConstants.Builds.OBELISK.containsObject(object)) {
            return true;
        } else if (HouseConstants.Builds.ALTAR.containsObject(object)) {
            return true; //do what it does to altars outside world
        } else if (HouseConstants.Builds.LEVER.containsObject(object)) {
            if (!house.isOwner(player)) {
                player.message("You can only do that in your own house.");
                return false;
            }
            if (house.isBuildMode()) {
                player.message("You cannot do this while in building mode.");
                return false;
            }
            house.leverEffect(object);
        } else if (HouseConstants.Builds.ROPE_BELL_PULL.containsObject(object)) {
            if (!house.isOwner(player)) {
                player.message("I'd better not do this...");
                return false;
            }
            player.message("Coming soon");
//            house.callServant(true);
            return false;
        } else if (object.getId() == 13523) {
            //ItemTransportation.transportationDialogue(player, new Item(1712, 1), false);
            return false;
        } else if (HouseConstants.Builds.TELESCOPE.containsObject(object)) {
            ShootingStar.openTelescope(player);
        } else if (HouseConstants.Builds.TRAPDOOR.containsObject(object)) {
            house.climbTrapdoor(player, object, false);
        } else if (HouseConstants.Builds.LADDER.containsObject(object)) {
            house.climbTrapdoor(player, object, true);
        } else if (HouseConstants.Builds.MUSIC_SPOT.containsObject(object)) {
            if (object.getId() == 13214) {
                //player.musicEffectOld(50);
                player.animate(3674);
            } else if (object.getId() == 13216) {
                //player.getPackets().sendMusicEffectOld(72);
                player.animate(3675);
            } else if (object.getId() == 13215) {
                //player.getPackets().sendMusicEffectOld(90);
                player.animate(3674);
            }
        } else if (HouseConstants.Builds.ALTAR_LAMP.containsObject(object)) {
            if (house.isBuildMode()) {
                player.message("You cannot do this while in building mode.");
                return false;
            } else if (!player.getInventory().contains(new Item(590)) || object.getId() >= HouseConstants.HObject.INSCENCE_BURNER.getId() && object.getId() <= HouseConstants.HObject.MARBLE_BURNER.getId() && !player.getInventory().contains(new Item(251))) {
                player.message("You do not have the required items to light this.");
                return false;
            }
            player.getDirection().faceObject(object);
            player.message("You use your tinderbox to light the torches.");
            player.getMovement().lock(2);
            player.animate(3687);
            player.getInventory().delete(new Item(251));
            GameObject objectR = new GameObject(object);
            objectR.setId(object.getId() + 1);
            int time = 1210000;
            TempObjectManager.spawnObjectTemporary(objectR, time, true, true); //lights for 2 min 10 seconds
            World.sendObjectAnimation(objectR, new Animation(13209));
            for (int burner : HouseConstants.INCENSE_BURNERS) {
                if (objectR.getId() == burner) {
                    house.setBurnerCount(house.getBurnerCount() + 1);
                    GameWorld.schedule(time / 600, () -> house.setBurnerCount(house.getBurnerCount() - 1));
                }
            }
        } else if (HouseConstants.Builds.LECTURN.containsObject(object)) {
            if (house.isBuildMode()) {
                player.message("You cannot do this while in building mode.");
                return false;
            }
            TabletMaking.openTabInterface(player, object);
        } else if (HouseConstants.Builds.BOOKCASE.containsObject(object))
            player.message("You search the bookcase but find nothing.");
        else if (HouseConstants.Builds.HEAD_TROPHY.containsObject(object)) {
            player.getDialogueManager().sendPlayerDialogue(Expressions.NORMAL, "Hello, having a nice day " + object.getDefinition().getName() + "?");
        } else if (object.getId() == HouseConstants.HObject.DUNGEON_ENTRACE.getId() || HouseConstants.Builds.STAIRCASE.containsObject(object) || HouseConstants.Builds.STAIRCASE_DOWN.containsObject(object)) {
            if (object.getDefinition().containsOption("Climb"))
                player.getDialogueManager().startDialogue(new ClimbHouseStairD(), object, house);
            else
                house.climbStaircase(player, object, object.getDefinition().containsOption("Climb-up"), object.getId() == HouseConstants.HObject.DUNGEON_ENTRACE.getId());
        } else if (object.getId() == 13137 || object.getId() == 13133 || object.getId() == 13129) { //combat ring
            if (!house.isBuildMode()) {
                byte ringType = (byte) (this.ringType > 0 ? -1 : (object.getId() == 13137 ? 3 : object.getId() == 13133 ? 2 : 1));
                if (!canEnterRing(ringType))
                    return false;
                this.ringType = ringType;
            }
            player.getDirection().face(object);
            if (object.getDirection() == 0)
                ObjectHandler.useStairs(player, 4853, new Position(player.getX() == object.getX() ? object.getX() - 1 : object.getX(), object.getY(), object.getZ()), 1, 2);
            else if (object.getDirection() == 2)
                ObjectHandler.useStairs(player, 4853, new Position(player.getX() == object.getX() ? object.getX() + 1 : object.getX(), object.getY(), object.getZ()), 1, 2);
            else if (object.getDirection() == 1)
                ObjectHandler.useStairs(player, 4853, new Position(object.getX(), player.getY() == object.getY() ? object.getY() + 1 : object.getY(), object.getZ()), 1, 2);
            else
                ObjectHandler.useStairs(player, 4853, new Position(object.getX(), player.getY() == object.getY() ? object.getY() - 1 : object.getY(), object.getZ()), 1, 2);
            if (!house.isBuildMode() && !house.isPVPMode())
                player.setCanPvp(!player.isCanPvp());
        } else if (HouseConstants.Builds.PORTAL_FOCUS.containsObject(object)) {
            if (!house.isOwner(player)) {
                player.message("You can only do that in your own house.");
                return false;
            } else if (!house.isBuildMode()) {
                player.message("Your house must be in build mode in order to change your portal's focus.");
                return false;
            }
            player.getDialogueManager().startDialogue(new PortalChamberD(), house.getRoom(player));
            return false;
        } else if (object.getId() >= 13615 && object.getId() <= 13635) {
            Position target = new Position(HouseConstants.PORTAL_COORDINATES[object.getId() - (13615 + 7 * (((object.getId() - 13615) / 7)))]);
            TeleportHandler.teleportPlayer(player, target, TeleportType.NORMAL);
            return false;
        }
        return false;
    }

    private void passDoor(GameObject object) {//TODO: Makes you run to some insane location
        int x = 0;
        int y = 0;
        if (object.getDirection() == 2)
            x = player.getX() == object.getX() ? 1 : -1;
        else if (object.getDirection() == 0)
            x = player.getX() == object.getX() ? -1 : 1;
        else if (object.getDirection() == 3)
            y = player.getY() == object.getY() ? -1 : 1;
        else if (object.getDirection() == 1)
            y = player.getY() == object.getY() ? 1 : -1;
        player.getMovement().addWalkSteps(player.getX() + x, player.getY() + y, 1, false);
        player.getMovement().lock(1);
    }

    private final static int[] RESTRICTED_SLOTS =
            {0, 1, 2, 4, 5, 7, 9, 10, 12, 13, 14};

    private boolean canEnterRing(byte ringType) {
        if (ringType == 1 || ringType == 2) {
            for (int slot : RESTRICTED_SLOTS) {
                Item item = player.getEquipment().get(slot);
                if (item != null) {
                    player.message("You need to remove your " + item.getName() + " before entering the ring.");
                    return false;
                }
            }
            if (ringType == 1) {
                int weapon = player.getEquipment().get(Slot.WEAPON).getId();
                if (weapon == 7671 || weapon == 7673)
                    return true;
                player.message("You need to have boxing gloves equipped in order to box.");
                return false;
            }
        }
        return true;
    }

    /**
     * RingTypes : 1= boxing, 2= fencing, 3=combat
     *
     * @return
     */
    private boolean canAttack() {
        if (ringType == 1 || ringType == 2) {
            boolean distanceAttack = PlayerCombatAction.isRanging(player) > 1;
            if (player.getEquipment().get(Slot.WEAPON).getId() == 22496 || distanceAttack) {
                player.message("You may only use melee in this ring!");
                return false;
            }
            int weaponId = player.getEquipment().get(Slot.WEAPON).getId();
            if (ringType == 1) {
                return weaponId == 7671 || weaponId == 7673;
            }
        }
        return !house.isPVPMode() || player.getZ() == 0;
    }

    @Override
    public boolean canEquip(Item item, Slot slot) {
        if (ringType == 1) {
            player.message("You may only use boxing gloves in a boxing ring.");
            return false;
        } else if (ringType == 2) {
            if (slot.ordinal() != 3) {
                player.message("You may only equip weapons in a fencing ring.");
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean canRemoveEquip(int slot, int itemId) {
        if (ringType == 1) {
            if (slot == Slot.WEAPON.ordinal()) {
                player.message("You can't remove your boxing gloves.");
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean processButtonClick(int componentId) {
        if (componentId == 920) {
            player.getPacketSender().sendTabInterface(GameSettings.OPTIONS_TAB, 19261);
            player.getPacketSender().sendConfig(2504, 0);
            player.getPacketSender().sendString(19285, Integer.toString(house.getAmountOfRooms()));
            return false;
        }

        if (componentId == 19279) {
            House.leaveHouse(player);
            return false;
        }

        if (componentId == 19276) {
            house.expelGuests();
            return false;
        }

        if (componentId == 19267) {
            if (player.getTimers().getClickDelay().elapsed(4000)) {
                house.setBuildMode(true);
            }
            return false;
        }

        if (componentId == 19268) {
            if (player.getTimers().getClickDelay().elapsed(4000)) {
                house.setBuildMode(false);
            }
            return false;
        }

        if (componentId == 19272) {
            player.getPacketSender().sendConfig(221, 1);
//            house.setArriveInPortal(false);
            return false;
        }

        if (componentId == 19273) {
//            house.setArriveInPortal(true);
            return false;
        }

        /*if (interfaceId == 400) {)
            if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
                TabletMaking.handleTabletCreation(player, componentId, 1);
            else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
                TabletMaking.handleTabletCreation(player, componentId, 5);
            else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
                TabletMaking.handleTabletCreation(player, componentId, 10);
            else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
                //player.getTemporaryAttributes().put("create_tab_X_component", componentId);
                //player.getPackets().sendExecuteScriptReverse(108, new Object[]
                        //{ "Enter Amount:" });
                player.message("Coming soon!");
            } else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET)
                TabletMaking.handleTabletCreation(player, componentId, player.getInventory().getAmount(1761));
        }*/
        return true;
    }

    @Override
    public boolean keepCombating(Figure victim) {
        return canAttack();
    }

    public boolean handleItemOnObject(GameObject object, Item item) {
        if (object.getId() == HouseConstants.HObject.CLAY_FIREPLACE.getId() || object.getId() == HouseConstants.HObject.STONE_FIREPLACE.getId() || object.getId() == HouseConstants.HObject.MARBLE_FIREPLACE.getId()) {
            if (house.isBuildMode()) {
                player.message("You cannot do this while in building mode.");
                return false;
            } else if (item.getId() != 1511) {
                player.message("Only ordinary logs can be used to light the fireplace.");
                return false;
            } else if (!player.getInventory().contains(new Item(590))) {
                player.message("You do not have the required items to light this.");
                return false;
            }
            player.getMovement().lock(2);
            player.animate(3658);
            player.getInventory().delete(new Item(1511, 1));
            player.getSkills().addExperience(Skill.FIREMAKING, 40);
            GameObject objectR = new GameObject(object);
            objectR.setId(object.getId() + 1);
            TempObjectManager.spawnObjectTemporary(objectR, 60000, true, true); //lights for 1min
            return false;
        } else if (HouseConstants.HObject.FIREPIT.getId() >= object.getId() && HouseConstants.HObject.FANCY_RANGE.getId() <= object.getId()) {
            Cookables cook = Cooking.isCookingSkill(item);
            if (cook != null) {
                player.getDialogueManager().startDialogue(new CookingD(), cook, object);
                return false;
            }
            player.getDialogueManager().sendDialogue("You can't cook that on a " + (object.getDefinition().getName().equals("Fire") ? "fire" : "range") + ".");
            return false;
        } else if (HouseConstants.Builds.DINING_TABLE.containsObject(object) || HouseConstants.Builds.KITCHEN_TABLE.containsObject(object)) {
            if (house.isBuildMode()) {
                player.message("You cannot place items on the table while in building mode.");
                return false;
            }
            player.getDirection().faceObject(object);
            player.message("You place the " + item.getName() + " on the table.", true);
            player.animate(833);
            player.getMovement().lock(1);
            player.getInventory().delete(item);
            FloorItemManager.addGroundItem(item, object, player);
            return false;
        } else if (HouseConstants.Builds.BARRELS.containsObject(object) && item.getId() == 1919) {
            int beer = object.getId() - 13568;
            player.getInventory().delete(item);
            player.getInventory().addSafe(new Item(HouseConstants.BEERS[beer]));
            player.animate(3661 + beer);
            player.message("You fill the glass with " + object.getDefinition().getName() + ".", true);
            return false;
        } else if (HouseConstants.Builds.ALTAR.containsObject(object)) {
            Bone bone = Bone.forId(item.getId());
            if (bone != null) {
                if (house.isBuildMode()) {
                    player.message("You cannot do this while in building mode.");
                    return false;
                }
                player.getDirection().face(BoneOffering.calcPosition(player, object));
                player.getDialogueManager().startDialogue(new BonesOnAlterD(),object, bone, house.getBurnerCount());
                return false;
            }
        }
        return true;
    }

    public boolean canDropItem(Item item) {
        if (house.isBuildMode()) {
            player.message("You cannot drop items while in building mode.");
            return false;
        }
        return true;
    }

    @Override
    public boolean processObjectClick2(final GameObject object) {
        if (object.getId() == HouseConstants.HObject.EXIT_PORTAL.getId())
            house.switchLock(player);
        else if (object.getId() == HouseConstants.HObject.DUNGEON_ENTRACE.getId() || HouseConstants.Builds.STAIRCASE.containsObject(object) || HouseConstants.Builds.STAIRCASE_DOWN.containsObject(object))
            house.climbStaircase(player, object, true, false);
        else if (HouseConstants.Builds.DUNGEON_DOOR1.containsObject(object) || HouseConstants.Builds.DUNGEON_DOOR2.containsObject(object)) {
            if (!house.isChallengeMode()) {
                player.message("The doors are only locked during challenge mode.");
                return false;
            }
            player.animate(3692);
            player.message("You attempt to pick the lock...", true);
            final int slot = HouseConstants.Builds.DUNGEON_DOOR1.getHObjectSlot(object.getId());
            GameWorld.schedule(1, () -> {
                if (Utils.random(player.getSkills().getMaxLevel(Skill.THIEVING)) < Utils.random(HouseConstants.DUNGEON_DOOR_LEVELS[slot])) {
                    player.message("and fail to pick the lock.", true);
                    return;
                }
                player.message("and successfully pick the lock.", true);
                passDoor(object);
            });
            return false;
        }/* else if (HouseConstants.Builds.LEVER.containsObject(object)) {
            if (!house.isOwner(player)) {
                player.message("You can only do that in your own house.");
                return false;
            } else if (house.isBuildMode()) {
                player.message("You cannot do this while in building mode.");
                return false;
            }
            house.pullLeverChallengeMode(object);
        }*/
        return false;
    }

    @Override
    public boolean processObjectClick3(final GameObject object) {
        if (HouseConstants.Builds.STAIRCASE.containsObject(object) || HouseConstants.Builds.STAIRCASE_DOWN.containsObject(object))
            house.climbStaircase(player, object, false, false);
        else if (HouseConstants.Builds.DUNGEON_DOOR1.containsObject(object) || HouseConstants.Builds.DUNGEON_DOOR2.containsObject(object)) {
            if (!house.isChallengeMode()) {
                player.message("The doors are only locked during challenge mode.");
                return false;
            }
            player.animate(3693);
            player.message("You attempt to force the lock...", true);
            final int slot = HouseConstants.Builds.DUNGEON_DOOR1.getHObjectSlot(object.getId());
            GameWorld.schedule(1, () -> {
                if (Utils.random(player.getSkills().getMaxLevel(Skill.STRENGTH)) < Utils.random(HouseConstants.DUNGEON_DOOR_LEVELS[slot])) {
                    player.message("You fail to force the lock.", true);
                    return;
                }
                passDoor(object);
            });

            return false;
        }
        return false;
    }

    @Override
    public boolean processObjectClick4(GameObject object) {
        if (HouseConstants.Builds.LADDER.containsObject(object) || HouseConstants.Builds.TRAPDOOR.containsObject(object) || object.getId() == HouseConstants.HObject.DUNGEON_ENTRACE.getId() || HouseConstants.Builds.STAIRCASE.containsObject(object) || HouseConstants.Builds.STAIRCASE_DOWN.containsObject(object)) {
            if (!house.isOwner(player)) {
                player.message("You can only do that in your own house.");
                return false;
            }
            house.removeRoom();
        } else if (object.getDefinition().containsOption(3, "Upgrade")) {
            if (!house.isOwner(player)) {
                player.message("You can only do that in your own house.");
                return false;
            }
            for (HouseConstants.Builds build : HouseConstants.Builds.values()) {
                if(build.getHObjectSlot(object.getId()) != -1) {
                    house.openBuildInterface(object, build);
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public boolean checkWalkStep(int lastX, int lastY, int nextX, int nextY) {
        return !house.isSky(nextX, nextY, player.getZ());
    }

    @Override
    public boolean canMove(final int dir) {
        if (!house.isChallengeMode() || player.getMovement().isLocked())
            return true;

        final GameObject trap = house.getDungeonTrap(new Position(player.getX() + Utils.DIRECTION_DELTA_X[dir], player.getY() + Utils.DIRECTION_DELTA_Y[dir], player.getZ()));
        final boolean containsTrap = trap != null;

        if (containsTrap) {

            final int slot = HouseConstants.Builds.TRAP_SPACE_1.getHObjectSlot(trap.getId());
            final int agilityLevel = player.getSkills().getMaxLevel(Skill.AGILITY);
            final boolean isFailure = Utils.randomDouble((agilityLevel / 100.0)) < Math.random();
            final boolean isTeleportTrap = slot == 4;

            if (agilityLevel >= 50) {
                if (Utils.random(3) == 0) {// 33% chance
                    return true;// Completely skip the trap.
                }
            }
            final Position previousTile = player.getMovement().getLastPosition();
            int transformX = previousTile.getX() - trap.getX();
            int transformY = previousTile.getY() - trap.getY();
            if (transformX > 1)
                transformX = 1;
            if (transformY > 1)
                transformY = 1;
            final Position successTile = trap.transform(-transformX, -transformY, 0);

            player.getMovement().lock();
            player.getDirection().faceObject(trap);

            GameWorld.schedule(new Task(true) {
                private int count = 0;
                @Override
                public void run() {
                    count++;
                    if (count == 1)
                        player.getMovement().addWalkSteps(trap.getX(), trap.getY(), -1, false);
                    else if (count == 2) {
                        if (!isFailure) {
                            if (isTeleportTrap)
                                return;
                        }
                        player.animate(HouseConstants.TRAP_ANIMATIONS[slot]);
                        player.getPacketSender().sendGraphic(new Graphic(HouseConstants.TRAP_GRAPHICS[slot]), trap);
                    } else if (count == (isTeleportTrap ? 10 : 4)) {
                        player.getMovement().lock(slot == 2 ? 2 : 0);
                        stop();
                        if (isFailure) {
                            if (isTeleportTrap)
                                return;
                            player.getMovement().addWalkSteps(previousTile.getX(), previousTile.getY());
                        } else
                            player.setPosition(successTile);
                    }

                    if (isFailure) {
                        if (count == (isTeleportTrap ? 8 : 3)) {
                            if (slot == 0 || slot == 1) {
                                player.getCombat().applyHit(new Hit(100, HitMask.CRITICAL, CombatIcon.NONE));
                                player.forceChat("*Ouch*!");
                            } else if (slot == 3) {
                                player.getSkills().drain(Skill.AGILITY, 4);
                            } else if (slot == 4) {
                                House.RoomReference obliette = house.getRoom(HouseConstants.Room.OUTBLIETTE);
                                if (obliette == null)
                                    obliette = house.getRoom(HouseConstants.Room.GARDEN);
                                if (obliette != null) {
                                    boolean isPool = obliette.containsHObject(HouseConstants.HObject.TENTACLE_POOL);
                                    if (isPool) {
                                        player.perform(new Graphic(622, 6, GraphicHeight.LOW));
                                        //player.getAppearance().setRenderEmote(152);
                                    }
                                    ObjectHandler.useStairs(player, isPool ? 3641 : 3640, Utils.getFreeTile(house.getCenterTile(obliette), 1), 0, 2, null, true);
                                }
                            }
                        }
                    } else {
                        if (count == 3) {
                            //player.setNextForceMovement(new NewForceMovement(player, 0, successTile, 1, Utils.getAngle(trap.getX() - successTile.getX(), trap.getY() - successTile.getY())));
                            player.animate(3627);
                        }
                    }
                }
            });
        }
        return !containsTrap;
    }

    @Override
    public boolean logout() {
        house.leaveHouse(player, House.LOGGED_OUT);
        return false; //leave house method removes controller already
    }

    //shouldnt happen but lets imagine somehow in a server restart
    @Override
    public boolean login() {
        player.setPosition(GameSettings.DEFAULT_POSITION);
        removeController();
        return false; //remove controller manualy since i dont want to call forceclose
    }

    @Override
    public boolean sendDeath() {
        player.getMovement().lock(8);
        player.stopAll();
        GameWorld.schedule(new PlayerDeath(player, player.copyPosition(), house.isLoaded() ? house.getPortal() : GameSettings.DEFAULT_POSITION, false, new Runnable() {
            @Override
            public void run() {
                if (ringType > 0)
                    ringType = 0;
                if (player.isCanPvp() && !house.isPVPMode())
                    player.setCanPvp(false);
            }
        }));
        return false;
    }

    @Override
    public void magicTeleported(int type) {
        house.leaveHouse(player, House.TELEPORTED);
    }

    //shouldnt happen
    @Override
    public void forceClose() {
        house.leaveHouse(player, House.TELEPORTED);
    }

    public House getHouse() {
        return house;
    }
}
