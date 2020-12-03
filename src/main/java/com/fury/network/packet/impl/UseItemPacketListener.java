package com.fury.network.packet.impl;

import com.fury.cache.Revision;
import com.fury.core.action.PlayerActionBus;
import com.fury.core.action.actions.ItemOnItemAction;
import com.fury.core.action.actions.ItemOnObjectAction;
import com.fury.core.action.actions.ItemOptionAction;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.content.dialogue.impl.items.AttachingOrbsD;
import com.fury.game.content.dialogue.impl.items.ItemMessageD;
import com.fury.game.content.dialogue.impl.misc.SimpleMessageD;
import com.fury.game.content.dialogue.impl.skills.cooking.CookingD;
import com.fury.game.content.dialogue.impl.skills.fletching.FletchingD;
import com.fury.game.content.dialogue.impl.skills.herblore.HerbloreD;
import com.fury.game.content.dialogue.impl.skills.prayer.BonesOnAlterD;
import com.fury.game.content.eco.ge.ItemSets;
import com.fury.game.content.global.Achievements;
import com.fury.game.content.global.minigames.impl.WarriorsGuild;
import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.free.cooking.Cookables;
import com.fury.game.content.skill.free.cooking.Cooking;
import com.fury.game.content.skill.free.cooking.CookingData;
import com.fury.game.content.skill.free.crafting.gems.GemCutting;
import com.fury.game.content.skill.free.crafting.glass.GlassBlowing;
import com.fury.game.content.skill.free.crafting.leather.LeatherMaking;
import com.fury.game.content.skill.free.firemaking.FireStarter;
import com.fury.game.content.skill.free.firemaking.Firemaking;
import com.fury.game.content.skill.free.firemaking.bonfire.Bonfire;
import com.fury.game.content.skill.free.prayer.Bone;
import com.fury.game.content.skill.free.prayer.BoneOffering;
import com.fury.game.content.skill.free.runecrafting.Runecrafting;
import com.fury.game.content.skill.free.smithing.Smithing;
import com.fury.game.content.skill.member.construction.SpellTablet;
import com.fury.game.content.skill.member.farming.ProductInfo;
import com.fury.game.content.skill.member.fletching.Fletching;
import com.fury.game.content.skill.member.fletching.FletchingData;
import com.fury.game.content.skill.member.herblore.Drinkables;
import com.fury.game.content.skill.member.herblore.Herblore;
import com.fury.game.content.skill.member.herblore.WaterFilling;
import com.fury.game.content.skill.member.herblore.WeaponPoison;
import com.fury.game.content.skill.member.summoning.Incubator;
import com.fury.game.content.skill.member.summoning.impl.Pet;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.character.player.content.OrnamentKits;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.core.model.item.FloorItem;
import com.fury.core.model.item.Item;
import com.fury.game.entity.item.content.ItemCreation;
import com.fury.game.entity.item.content.ItemForging;
import com.fury.game.entity.item.content.ItemRepair;
import com.fury.game.entity.object.GameObject;
import com.fury.game.node.entity.actor.object.ObjectManager;
import com.fury.game.npc.familiar.Familiar;
import com.fury.game.npc.familiar.impl.Pyrelord;
import com.fury.game.npc.slayer.ConditionalDeath;
import com.fury.game.npc.slayer.polypore.PolyporeCreature;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;
import com.fury.game.world.map.path.RouteEvent;
import com.fury.network.packet.Packet;
import com.fury.network.packet.PacketConstants;
import com.fury.network.packet.PacketListener;
import com.fury.util.Misc;

import java.util.Arrays;

/**
 * This packet listener is called when a player 'uses' an item on another
 * figure.
 *
 * @author relex lawl
 */

public class UseItemPacketListener implements PacketListener {

    /**
     * The PacketListener logger to debug information and print out errors.
     */
    // private final static Logger logger =
    // Logger.getLogger(UseItemPacketListener.class);
    private static void useItem(Player player, Packet packet) {
        if (player.getMovement().getTeleporting() || player.getHealth().getHitpoints() <= 0)
            return;
        int interfaceId = packet.readLEShortA();
        int slot = packet.readShortA();
        int id = packet.readLEShort();
    }

    private static void itemOnItem(Player player, Packet packet) {
        int usedWithSlot = packet.readUnsignedShort();
        int itemUsedSlot = packet.readUnsignedShortA();
        int usedWithId = packet.readInt();
        int type = packet.readShort();
        int itemUsedId = packet.readInt();
        int widget = packet.readShort();

        if (!player.getInventory().validate(usedWithId, usedWithSlot))
            return;

        if (!player.getInventory().validate(itemUsedId, itemUsedSlot))
            return;

        Item itemUsed = player.getInventory().get(itemUsedSlot);
        Item usedWith = player.getInventory().get(usedWithSlot);

        if (player.getRights() == PlayerRights.DEVELOPER)
            player.getPacketSender().sendConsoleMessage("Item on item [id, slot]: [" + itemUsedId + ", " + itemUsedSlot + "]  [" + usedWithId + ", " + usedWithSlot + "]");


        if (!player.getControllerManager().canUseItemOnItem(usedWith, itemUsed))
            return;

        if (itemUsed != null && usedWith != null && PlayerActionBus.publish(player, new ItemOnItemAction(itemUsed, usedWith, itemUsedSlot, usedWithSlot)))
            return;

        if (AttachingOrbsD.isAttachingOrb(player, itemUsed, usedWith))
            return;

        if (itemUsed.getId() == 5733 || usedWith.getId() == 5733) {
            Item other = itemUsed.getId() == 5733 ? usedWith : itemUsed;
            if (player.getRights().isOrHigher(PlayerRights.DEVELOPER)) {
                player.message(other.getId() + " " + other.getName() + " " + other.getRevision());
                player.message("Tradable: " + other.tradeable() + " Stackable: " + other.getDefinition().stackable);
                player.message("Model: " + other.getDefinition().modelId);
                return;
            }
        }

        SpellTablet tablet = SpellTablet.forId(itemUsed.getId());
        if (tablet != null) {
            tablet.use(player, usedWith, usedWithSlot);
            return;
        }

        tablet = SpellTablet.forId(usedWith.getId());
        if (tablet != null) {
            tablet.use(player, itemUsed, itemUsedSlot);
            return;
        }

        if (itemUsed.getId() == 6573 || usedWith.getId() == 6573) {
            player.message("To make an Amulet of Fury, you need to put an onyx in a furnace.");
            return;
        }

        if (usedWith.getId() == 1775 || itemUsed.getId() == 1775)
            GlassBlowing.makeGlass(player);

        WeaponPoison.execute(player, usedWith, itemUsed);

        if (Firemaking.isFiremaking(player, itemUsed, usedWith))
            return;

        if (Drinkables.mixFlask(player, itemUsed, usedWith, itemUsedSlot, usedWithSlot))
            return;

        if (Drinkables.mixPot(player, itemUsed, usedWith, itemUsedSlot, usedWithSlot, true) != -1)
            return;

        if (itemUsed.isEqual(Herblore.PESTLE_AND_MORTAR) && Herblore.isRawIngredient(player, usedWith))
            return;
        else if (usedWith.isEqual(Herblore.PESTLE_AND_MORTAR) && Herblore.isRawIngredient(player, itemUsed))
            return;

        if (OrnamentKits.attachKit(player, itemUsed, usedWith, itemUsedSlot, usedWithSlot))
            return;

        Item herblore = Herblore.isHerbloreSkill(itemUsed, usedWith);
        if (herblore != null) {
            player.getDialogueManager().startDialogue(new HerbloreD(), herblore, itemUsed, usedWith, false);
            return;
        }
        if (itemUsed.getId() == 233 && usedWith.getId() == 9735 || itemUsed.getId() == 9735 && usedWith.getId() == 233) {
            player.getInventory().delete(new Item(9735));
            player.getInventory().add(new Item(9736));
            return;
        }

        if (ItemCreation.handleItemOnItem(player, itemUsed.getId(), usedWith.getId()))
            return;


        FletchingData fletch = Fletching.isFletchingCombination(usedWith, itemUsed);
        if (fletch != null) {
            player.getDialogueManager().startDialogue(new FletchingD(), fletch);
            return;
        }
        if (GemCutting.isCutting(player, itemUsed, usedWith))
            return;
        if (itemUsed.getId() == 1733 || usedWith.getId() == 1733)
            LeatherMaking.craftLeatherDialogue(player, itemUsed.getId(), usedWith.getId());
        ItemForging.forgeItem(player, usedWith, itemUsed);

        if (CookingData.handlePieItems(player, itemUsed.getId(), usedWith.getId()) || CookingData.handlePieItems(player, usedWith.getId(), itemUsed.getId()))
            return;
    }

    @SuppressWarnings("unused")
    private static void itemOnObject(Player player, Packet packet) {
        @SuppressWarnings("unused")
        int interfaceType = packet.readShort();
        int objectId = packet.readInt();
        final int objectY = packet.readLEShortA();
        final int itemSlot = packet.readLEShort();
        final int objectX = packet.readLEShortA();
        final int itemId = packet.readInt();

        if (!player.getInventory().validate(itemId, itemSlot))
            return;

        Item item = player.getInventory().get(itemSlot);

        final Position position = new Position(objectX, objectY, player.getZ());
        final GameObject object = ObjectManager.getObjectWithId(objectId, position);

        if (object == null || object.getId() != objectId)
            return;

        if (player.getRights() == PlayerRights.DEVELOPER)
            player.getPacketSender().sendConsoleMessage("Item on object [id, slot]: [" + item.getId() + ", " + objectId + "]");

        switch (item.getId()) {
            case 5733:
                if (player.getRights().isOrHigher(PlayerRights.ADMINISTRATOR)) {
                    player.message("Object: " + object.getId() + " " + object.getDefinition().getName() + " " + object.getRevision());
                    player.message("Models: " + Arrays.deepToString(object.getDefinition().modelIds) + " Types: " + Arrays.toString(object.getDefinition().modelTypes));
                    player.message("Anim: " + object.getDefinition().animationId + " arr: " + Arrays.toString(object.getDefinition().animationArray));
                    player.message("Varbit: " + object.getDefinition().varbitIndex + " Varbit objs size: " + (object.getDefinition().configObjectIds == null ? null : object.getDefinition().configObjectIds.length));
                    player.message("Varbit objs: " + Arrays.toString(object.getDefinition().configObjectIds));
                    player.message("Idle animation: " + object.getDefinition().animationId + " Array:" + Arrays.toString(object.getDefinition().animationArray));
                    player.setInteractingObject(object);
                }
                return;
        }

        player.setInteractingObject(object);
        player.getDirection().face(object);
        player.stopAll(false);
        player.setRouteEvent(new RouteEvent(object, () -> {

            if (item != null && PlayerActionBus.publish(player, new ItemOnObjectAction(item, itemSlot, object)))
                return;

            if (!player.getControllerManager().handleItemOnObject(object, item))
                return;

            if (Runecrafting.talismanOnAlter(player, object, item))
                return;

            if (player.getFarmingManager().isFarming(object.getId(), item, 0))
                return;

        /*   if (objectId == 15621) { // Warriors guild animator
                if (!WarriorsGuild.itemOnAnimator(player,
                        item, object))
                    player.message("Nothing interesting happens..");
                return;
            }*/

            switch (objectId) {
                case 28352:
                case 28550://Incubator
                    Incubator.useEgg(player, itemId);
                    break;
                case 36881://Lumbridge windmill hopper
                    if (itemId == 1947) {
                        player.message("You place the grain into the hopper and it falls down into the mill.");
                        player.animate(833);
                        int am = player.getInventory().getAmount(new Item(1947));
                        player.getInventory().delete(new Item(1947, am));
                        player.addMillFilled(am);
                    }
                    break;
                case 7836:
                case 7808:
                    if (itemId == 6055) {
                        int amt = player.getInventory().getAmount(new Item(6055));
                        if (amt > 0) {
                            player.getInventory().delete(new Item(6055, amt));
                            player.message("You put the weed in the compost bin.");
                            player.getSkills().addExperience(Skill.FARMING, 1 * amt);
                        }
                    }
                    break;
            }

            if (object.getDefinition() != null)
                switch (object.getDefinition().getName().toLowerCase()) {
                    case "bank booth":
                        ItemSets.Sets set = ItemSets.getSet(item.getId());
                        if (set != null)
                            ItemSets.exchangeSets(player, set);
                        break;
                    case "fountain":
                    case "well":
                    case "sink":
                    case "pump and tub":
                    case "pump and drain":
                        if (WaterFilling.isFilling(player, itemId))
                            player.setInteractingObject(object);
                        break;
                    case "sand pit":
                        //player.getActionManager().setAction(new SandBucketFillAction());
                        break;
                    case "anvil":
                        Smithing.handleAnvil(player, itemId);
                        break;
                    case "furnace":
                    case "lava furnace":
                        if (itemId == 6573 || itemId == 1597 || itemId == 1759) {
                            if (player.getSkills().getLevel(Skill.CRAFTING) < 80) {
                                player.message("You need a Crafting level of at least 80 to make that item.");
                                return;
                            }
                            if (player.getInventory().contains(new Item(6573))) {
                                if (player.getInventory().contains(new Item(1597))) {
                                    if (player.getInventory().contains(new Item(1759))) {
                                        player.animate(896);
                                        player.getInventory().delete(new Item(1759), new Item(6573));
                                        player.getInventory().add(new Item(6585));
                                        player.message("You put the items into the furnace to forge an Amulet of Fury.");
                                        Achievements.finishAchievement(player, Achievements.AchievementData.CREATE_A_FURY);
                                    } else {
                                        player.message("You need some Ball of Wool to do this.");
                                    }
                                } else {
                                    player.message("You need a Necklace mould to do this.");
                                }
                            }
                        }
                        if (itemId == 2357 || itemId == 4155) {
                            if (!player.getSlayerManager().getLearnt()[1]) {
                                player.message("You must have unlocked the ability to make Ring of slaying.");
                            } else {
                                if (player.getSkills().getLevel(Skill.CRAFTING) < 75) {
                                    player.message("You need a Crafting level of at least 75 to make that item.");
                                    return;
                                }
                                if (player.getInventory().contains(new Item(2357))) {
                                    if (player.getInventory().contains(new Item(4155, Revision.PRE_RS3))) {
                                        player.animate(896);
                                        player.getInventory().delete(new Item(2357), new Item(4155, Revision.PRE_RS3));
                                        player.getInventory().add(new Item(13281));
                                        player.getSkills().addExperience(Skill.CRAFTING, 15, true);
                                        player.message("You put the items into the furnace to forge a Ring of slaying.");
                                    } else {
                                        player.message("You need a Enchanted gem to do this.");
                                    }
                                } else {
                                    player.message("You need a gold bar to do this.");
                                }
                            }
                        }
                        break;
                    case "altar":
                        Bone bone = Bone.forId(item.getId());
                        if (bone != null) {
                            player.getDirection().face(BoneOffering.calcPosition(player, object));
                            player.getDialogueManager().startDialogue(new BonesOnAlterD(), object, bone, 0);
                            return;
                        }
                        break;
                    case "fire":
                        if (object.getDefinition().options != null && object.getDefinition().options.length >= 4 && object.getDefinition().options[4] != null && object.getDefinition().options[4].equals("Add-logs") && Bonfire.addLog(player, object, item))
                            return;
                        Cookables cook = Cooking.isCookingSkill(item);
                        if (cook != null) {
                            player.getDialogueManager().startDialogue(new CookingD(), cook, object);
                            return;
                        }
                        return;
                    case "range":
                    case "cooking range":
                    case "stove":
                    case "firepit":
                    case "firepit with hook":
                    case "firepit with pot":
                    case "small oven":
                    case "large oven":
                    case "steel range":
                    case "fancy range":
                        cook = Cooking.isCookingSkill(item);
                        if (cook != null) {
                            player.getDialogueManager().startDialogue(new CookingD(), cook, object);
                            return;
                        }
                        player.getDialogueManager().startDialogue(new SimpleMessageD(), "You can't cook that on a " + (object.getDefinition().getName().equals("Fire") ? "fire" : "range") + ".");

                        break;
                    /*default:
						player.message("Nothing interesting happens.");
						break;*/
                }
        }));
    }

    private static void itemOnNpc(final Player player, Packet packet) {
        final int id = packet.readInt();
        final int index = packet.readShortA();
        final int slot = packet.readLEShort();
        final int widget = packet.readShortA();
        if (index < 0 || index > GameWorld.getMobs().getMobs().capacity()) {
            return;
        }
        Mob mob = GameWorld.getMobs().getMobs().get(index);

        if (mob == null)
            return;

        if (!player.getInventory().validate(id, slot))
            return;

        Item item = player.getInventory().get(slot);

        switch (item.getId()) {
            case 5733:
                if (player.getRights().isOrHigher(PlayerRights.ADMINISTRATOR)) {
                    player.message("Npc: " + mob.getName() + " " + mob.getRevision());
                    player.message("Idle anim: " + mob.getDefinition().idleAnimation);
                    player.message("Walk anim: " + mob.getDefinition().walkAnimation);
                    player.message("Run anim: " + mob.getDefinition().runAnimation);
                    player.getDirection().setInteracting(mob);
                    return;
                }
                break;
        }

        player.stopAll(false);
        player.setRouteEvent(new RouteEvent(mob, () -> {
            player.getDirection().face(mob);

            if (!player.getControllerManager().processItemOnNPC(mob, item))
                return;

            if (mob.getDefinition().getName().equalsIgnoreCase("tool leprechaun")) {
                if ((Herblore.isIngredient(item) || ProductInfo.isProduct(item)) && !item.getDefinition().isNoted() && item.getDefinition().noteId != -1) {
                    int quantity = player.getInventory().getAmount(item);
                    player.getInventory().delete(new Item(item, quantity));
                    player.getInventory().add(new Item(item.getDefinition().noteId, quantity));
                    player.getDialogueManager().startDialogue(new ItemMessageD(), "The leprechaun exchanges your items for banknotes.", item.getId());
                } else
                    player.message("Nothing interesting happens.");

            }

            if (mob instanceof Pet) {
                player.getDirection().face(mob);
                player.getPetManager().eat(item.getId(), (Pet) mob);
                return;
            } else if (mob.isFamiliar()) {
                Familiar familiar = (Familiar) mob;
                if (familiar.getBeastOfBurden() != null) {
                    player.getInventory().move(item, familiar.getBeastOfBurden());
                } else if (mob.getId() == 7378 || mob.getId() == 7377) {
                    if (Pyrelord.lightLog(familiar, item))
                        return;
                } else if (mob.getId() == 7339 || mob.getId() == 7340) {
                    if ((item.getId() >= 1704 && item.getId() <= 1710 && item.getId() % 2 == 0) || (item.getId() >= 10356 && item.getId() <= 10366 && item.getId() % 2 == 0) || (item.getId() == 2572 || (item.getId() >= 20653 && item.getId() <= 20657 && item.getId() % 2 != 0))) {
                        for (Item i : player.getInventory().getItems()) {
                            if (i == null)
                                continue;
                            if (i.getId() >= 1704 && i.getId() <= 1710 && i.getId() % 2 == 0)
                                i.setId(1712);
                            else if (i.getId() >= 10356 && i.getId() <= 10366 && i.getId() % 2 == 0)
                                i.setId(10354);
                            else if (i.getId() == 2572 || (i.getId() >= 20653 && i.getId() <= 20657 && i.getId() % 2 != 0))
                                i.setId(20659);
                        }
                        player.getInventory().refresh();
                        player.getDialogueManager().startDialogue(new ItemMessageD(), "Your ring of wealth and amulet of glory have all been recharged.", 1712);
                    }
                }
            } else if (mob instanceof ConditionalDeath)
                ((ConditionalDeath) mob).useHammer(player);

            switch (item.getId()) {
                case 22444:
                    PolyporeCreature.Companion.sprinkleOil(player, mob);
                    break;
            }

            ItemRepair.handlePurchaseDialogue(player, mob.getId(), item.getId());
        }));
    }

    @SuppressWarnings("unused")
    private static void itemOnPlayer(Player player, Packet packet) {
        int interfaceId = packet.readUnsignedShortA();
        int targetIndex = packet.readUnsignedShort();
        int itemId = packet.readInt();
        int slot = packet.readLEShort();
        if (!player.getInventory().validate(itemId, slot))
            return;

        Item item = player.getInventory().get(slot);

        Player target = GameWorld.getPlayers().get(targetIndex);
        if (target == null)
            return;

        player.stopAll(false);
        player.setRouteEvent(new RouteEvent(target, () -> {
            player.getDirection().face(target);

            if (!player.getControllerManager().processItemOnPlayer(target, item, slot))
                return;

            switch (item.getId()) {
                case 962:
                    if (!player.getInventory().contains(new Item(962)) || player.getRights() == PlayerRights.ADMINISTRATOR)
                        return;

                    if (player.getInventory().getSpaces() < 1) {
                        player.message("You do no have enough free inventory slots.");
                        return;
                    }
                    if (target.getInventory().getSpaces() < 1) {
                        player.message("That player does not have enough free inventory slots.");
                        return;
                    }

                    player.getDirection().face(target);
                    target.getDirection().face(player);

                    player.animate(15153);
                    target.animate(15153);
                    player.message("You pull the Christmas cracker...");
                    target.message("" + player.getUsername() + " pulls a Christmas cracker on you..");
                    player.getInventory().delete(new Item(962));
                    if (Misc.getRandom(1) == 1) {
                        target.message("The cracker explodes and you receive a Party hat!");
                        target.getInventory().add(new Item(1038 + Misc.getRandom(5) * 2));
                        player.message("" + target.getUsername() + " has received a Party hat!");
                    } else {
                        player.message("The cracker explodes and you receive a Party hat!");
                        player.getInventory().add(new Item(1038 + Misc.getRandom(5) * 2));
                        target.message("" + player.getUsername() + " has received a Party hat!");
                    }
                    break;
                case 4155:
                    player.getSlayerManager().invitePlayer(target);
                    break;
            }
        }));
    }


    private void itemOnGroundItem(Player player, Packet packet) {
        int itemId = packet.readInt();
        int slot = packet.readShort();
        int x = packet.readShort();
        int y = packet.readShort();
        int groundItemId = packet.readInt();
        Revision itemRevision = Revision.values()[packet.readUnsignedByte()];
        int type = packet.readShort();

        if (!player.getInventory().validate(itemId, slot))
            return;

        final Position position = new Position(x, y, player.getZ());

        final FloorItem floorItem = player.getRegion().getFloorItem(groundItemId, position, player);
        if (floorItem == null)
            return;

        if (!player.getTimers().getItemPickup().elapsed(500))
            return;

        player.stopAll(false);
        player.setRouteEvent(new RouteEvent(floorItem, () -> {
            FireStarter starter = FireStarter.forId(itemId);
            if (starter != null && Firemaking.isFiremaking(player, starter, floorItem, floorItem.getId()))
                return;
        }));
    }

    @Override
    public void handleMessage(Player player, Packet packet) {
        if (player.getHealth().getHitpoints() <= 0)
            return;
        switch (packet.getOpcode()) {
            case PacketConstants.ITEM_ON_ITEM:
                itemOnItem(player, packet);
                break;
            case PacketConstants.USE_ITEM:
                useItem(player, packet);
                break;
            case PacketConstants.ITEM_ON_OBJECT:
                itemOnObject(player, packet);
                break;
            case PacketConstants.ITEM_ON_GROUND_ITEM:
                itemOnGroundItem(player, packet);
                break;
            case PacketConstants.ITEM_ON_NPC:
                itemOnNpc(player, packet);
                break;
            case PacketConstants.ITEM_ON_PLAYER:
                itemOnPlayer(player, packet);
                break;
        }
    }
}
