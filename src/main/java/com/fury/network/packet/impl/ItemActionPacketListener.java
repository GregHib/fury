package com.fury.network.packet.impl;

import com.fury.core.action.PlayerActionBus;
import com.fury.core.action.actions.ItemOptionAction;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.content.controller.impl.Daemonheim;
import com.fury.game.content.dialogue.impl.items.ModPotatoD;
import com.fury.game.content.dialogue.impl.items.ModPotatoExtraD;
import com.fury.game.content.dialogue.impl.items.NeemDrupeSquish;
import com.fury.game.content.dialogue.impl.items.ShardsOfArmaD;
import com.fury.game.content.dialogue.impl.npcs.CharmingImpD;
import com.fury.game.content.dialogue.impl.skills.slayer.EnchantedGemD;
import com.fury.game.content.dialogue.impl.skills.slayer.SocialSlayerD;
import com.fury.game.content.global.Gambling;
import com.fury.game.content.global.WaterContainers;
import com.fury.game.content.global.treasuretrails.TreasureTrailHandlers;
import com.fury.game.content.misc.items.random.impl.MysteryBoxGen;
import com.fury.game.content.misc.items.random.impl.VoteRewardGen;
import com.fury.game.content.misc.items.random.impl.imps.MysteryBoxTimedGen;
import com.fury.game.content.misc.objects.DwarfMultiCannon;
import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.free.cooking.Consumables;
import com.fury.game.content.skill.free.prayer.Bone;
import com.fury.game.content.skill.free.runecrafting.Runecrafting;
import com.fury.game.content.skill.free.woodcutting.BirdNests;
import com.fury.game.content.skill.member.construction.SpellTablet;
import com.fury.game.content.skill.member.herblore.Drinkables;
import com.fury.game.content.skill.member.herblore.Herbs;
import com.fury.game.content.skill.member.herblore.IngredientsBook;
import com.fury.game.content.skill.member.hunter.TrapAction;
import com.fury.game.content.skill.member.summoning.CharmingImp;
import com.fury.game.content.skill.member.summoning.impl.Summoning;
import com.fury.game.entity.character.combat.CombatIcon;
import com.fury.game.entity.character.combat.Hit;
import com.fury.game.entity.character.combat.equipment.weapon.CombatSpecial;
import com.fury.game.entity.character.player.content.*;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.entity.character.player.link.transportation.JewelryTeleporting;
import com.fury.game.entity.character.player.link.transportation.TeleportHandler;
import com.fury.game.entity.character.player.link.transportation.TeleportTabs;
import com.fury.game.entity.character.player.link.transportation.TeleportType;
import com.fury.core.model.item.Item;
import com.fury.game.entity.item.content.ItemSplitting;
import com.fury.game.node.entity.actor.figure.player.handles.Settings;
import com.fury.game.npc.slayer.polypore.PolyporeCreature;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.HitMask;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.network.packet.Packet;
import com.fury.network.packet.PacketConstants;
import com.fury.network.packet.PacketListener;
import com.fury.util.Misc;


public class ItemActionPacketListener implements PacketListener {

    private static void firstAction(final Player player, Packet packet) {
        int interfaceId = packet.readUnsignedShort();
        int slot = packet.readShort();
        int itemId = packet.readInt();

        if (player.getRights().isOrHigher(PlayerRights.DEVELOPER))
            player.getPacketSender().sendConsoleMessage("Item [option, id, slot] [" + 1 + ", " + itemId + ", " + slot + "]");

        if (!player.getInventory().validate(itemId, slot))
            return;

        Item item = player.getInventory().get(slot);
        player.setInteractingItem(item);

        if (item != null && PlayerActionBus.publish(player, new ItemOptionAction(item, slot, 1)))
            return;

        if (TeleportTabs.teleportTabs(player, item.getId()))
            return;

        if (Bone.forId(item.getId()) != null) {
            Bone.bury(player, item);
            return;
        }

        if (Consumables.handleFood(player, item, slot))
            return;

        if (Drinkables.drink(player, item, slot))
            return;

        if (BirdNests.isNest(item)) {
            BirdNests.searchNest(player, item);
            return;
        }

        if (Herbs.clean(player, item))
            return;

        if (TreasureTrailHandlers.handleItemClick(player, item))
            return;

        if (MemberScrolls.handleScroll(player, item))
            return;

        if (Effigies.isEffigy(item)) {
            Effigies.handleEffigy(player, item);
            return;
        }

        if (ExperienceLamps.handleLamp(player, item))
            return;

        if (WaterContainers.empty(player, item, true))
            return;

        if (TrapAction.isTrap(player, player.copyPosition(), item.getId()))
            return;

        if (player.getDungManager().handleScrollUnlocks(player, item))
            return;

        if (!player.getControllerManager().processItemClick1(item))
            return;

        if(player.getEmotesManager().handleItem(item.getId()))
            return;

        SpellTablet tablet = SpellTablet.forId(item.getId());
        if(tablet != null) {
            player.message("Use the tablet on an item to enchant it.");
            return;
        }

        switch (item.getId()) {
            case 14701://Herb pouch
                player.getHerbPouch().fill();
                break;
            case 16://Ironman whistle
                if (!player.getTimers().getClickDelay().elapsed(2000))
                    return;
                TeleportHandler.teleportPlayer(player, new Position(3038, 5346), TeleportType.IRON_MAN);
                player.getTimers().getClickDelay().reset();
                break;
            case 22444://Neem oil
                PolyporeCreature.Companion.sprinkleOil(player, null);
                break;
            case 22445://Polypore neem drupe
                player.getDialogueManager().startDialogue(new NeemDrupeSquish());
                break;
            case 4155://Enchanted gem
                player.getDialogueManager().startDialogue(new EnchantedGemD(), player.getSlayerManager().getCurrentMaster().getNpcId());
                break;
            case 21776://Shards of armadyl
                player.getDialogueManager().startDialogue(new ShardsOfArmaD());
                break;
            case 27996:
                if (player.getCombat().isInCombat()) {
                    player.message("You cannot configure this right now.");
                    return;
                }
                player.getPacketSender().sendInterfaceRemoval();
                player.getDialogueManager().startDialogue(new CharmingImpD());
                break;
            case 19650://dungeoneering toolkit
                if (player.getInventory().getSpaces() < 8) {
                    player.message("You need at least 8 empty inventory slots to open this.");
                    return;
                }

                player.getInventory().delete(slot);
                player.getInventory().add(new Item[]{new Item(17794), new Item(16295), new Item(16361), new Item(17490), new Item(17754), new Item(17883), new Item(17796, 50), new Item(17678)});
                player.getInventory().refresh();
                break;
            case 594://Torch extinguish
                if (player.busy()) {
                    player.message("You can not do this right now.");
                    return;
                }
                player.getInventory().delete(new Item(594));
                player.getInventory().add(new Item(596));
                break;
            case 11258://Jar Generator
                if (!player.getTimers().getClickDelay().elapsed(3800))
                    return;
                if (player.getJarGeneratorCharge() <= 0)
                    return;
                if (player.getInventory().getSpaces() < 1) {
                    player.message("You do not have enough space in your inventory.");
                    return;
                }
                if (player.busy()) {
                    player.message("You can not do this right now.");
                    return;
                }
                if (player.getJarGeneratorCharge() - 1 <= 0) {
                    player.message("The generator doesn't have enough charge to make that.");
                    return;
                }
                player.animate(6592);
                player.perform(new Graphic(1117));
                player.getInventory().add(new Item(10012));
                player.setJarGeneratorCharge(player.getJarGeneratorCharge() - 1);
                player.message("You pull a butterfly jar out of the generator.");
                player.getTimers().getClickDelay().reset();
                if (player.getJarGeneratorCharge() <= 0) {
                    player.getInventory().delete(new Item(11258));
                    player.message("The jar generator breaks after using up it's final charge.");
                    player.setJarGeneratorCharge(100);
                }
                break;
            case 21371:
                player.getInventory().delete(new Item(21371));
                player.getInventory().add(new Item(4151));
                player.getInventory().add(new Item(21369));
                break;
            case 21372:
                player.getInventory().delete(new Item(21372));
                player.getInventory().add(new Item(15441));
                player.getInventory().add(new Item(21369));
                break;
            case 21373:
                player.getInventory().delete(new Item(21373));
                player.getInventory().add(new Item(15442));
                player.getInventory().add(new Item(21369));
                break;
            case 21374:
                player.getInventory().delete(new Item(21374));
                player.getInventory().add(new Item(15443));
                player.getInventory().add(new Item(21369));
                break;
            case 21375:
                player.getInventory().delete(new Item(21375));
                player.getInventory().add(new Item(15444));
                player.getInventory().add(new Item(21369));
                break;
            case 15364://Eye of newt pack
                if (!player.getTimers().getClickDelay().elapsed(2000))
                    return;
                if (player.busy()) {
                    player.message("You can not do this right now.");
                    return;
                }
                if (player.getInventory().getSpaces() < 1) {
                    player.message("You do not have enough space in your inventory.");
                    return;
                }
                player.getInventory().delete(new Item(item, 1));
                player.getInventory().add(new Item(222, 50));
                break;
            case 15363://Vial of water pack
                if (!player.getTimers().getClickDelay().elapsed(2000))
                    return;
                if (player.busy()) {
                    player.message("You can not do this right now.");
                    return;
                }
                if (player.getInventory().getSpaces() < 1) {
                    player.message("You do not have enough space in your inventory.");
                    return;
                }
                player.getInventory().delete(new Item(item, 1));
                player.getInventory().add(new Item(228, 50));
                break;
            case 15362://Vial pack
                if (!player.getTimers().getClickDelay().elapsed(2000))
                    return;
                if (player.busy()) {
                    player.message("You can not do this right now.");
                    return;
                }
                if (player.getInventory().getSpaces() < 1) {
                    player.message("You do not have enough space in your inventory.");
                    return;
                }
                player.getInventory().delete(new Item(item, 1));
                player.getInventory().add(new Item(230, 50));
                break;
            case 7509:
                player.forceChat("Ow! I nearly broke a tooth!");
                for (int i = 0; i < 3; i++)
                    player.getCombat().applyHit(new Hit(10, HitMask.RED, CombatIcon.NONE));
                //player.message("You burn your mouth and hands on the hot rock cake and then drop it.");
                break;
            case 15707://Ring of Kinship
                if (player.getControllerManager().getController() instanceof Daemonheim || (player.getDungManager().getParty() != null && player.getDungManager().getParty().getDungeon() != null))
                    player.getDungManager().openPartyInterface();
                else
                    player.message("This can only be accessed at Daemonheim.");
                break;
            case 20712:
                if (player.busy()) {
                    player.message("You can not do this right now.");
                    return;
                }
                if (player.getInventory().getSpaces() < 3) {
                    player.message("You need at least 3 empty inventory slots to open this.");
                    return;
                }
                if (player.getInventory().contains(new Item(20712)) && player.getTimers().getClickDelay().elapsed(400)) {
                    VoteRewardGen.reward(player);
                    player.getTimers().getClickDelay().reset();
                }
                break;
            case 7956:
                player.getInventory().delete(new Item(7956));
                int[] rewards = {200, 202, 204, 206, 208, 210, 212, 214, 216, 218, 220, 2486, 3052, 1624, 1622, 1620, 1618, 1632, 1516, 1514, 454, 448, 450, 452, 378, 372, 7945, 384, 390, 15271, 533, 535, 537, 18831, 556, 558, 555, 554, 557, 559, 564, 562, 566, 9075, 563, 561, 560, 565, 888, 890, 892, 11212, 9142, 9143, 9144, 9341, 9244, 866, 867, 868, 2, 10589, 10564, 6809, 4131, 15126, 4153, 1704, 1149};
                int[] rewardsAmount = {50, 50, 50, 30, 20, 30, 30, 30, 30, 20, 10, 5, 4, 70, 40, 25, 10, 10, 100, 50, 100, 80, 25, 25, 250, 200, 125, 50, 30, 25, 50, 20, 20, 5, 500, 500, 500, 500, 500, 500, 500, 500, 200, 200, 200, 200, 200, 200, 1000, 750, 200, 100, 1200, 1200, 120, 50, 20, 1000, 500, 100, 100, 1, 1, 1, 1, 1, 1, 1, 1};
                int rewardPos = Misc.getRandom(rewards.length - 1);
                player.getInventory().add(new Item(rewards[rewardPos], (int) ((rewardsAmount[rewardPos] * 0.5) + (Misc.getRandom(rewardsAmount[rewardPos])))));
                break;
            case 15387:
                player.getInventory().delete(new Item(15387));
                rewards = new int[]{1377, 1149, 7158, 3000, 219, 5016, 6293, 6889, 2205, 3051, 269, 329, 3779, 6371, 2442, 347, 247};
                player.getInventory().add(new Item(rewards[Misc.getRandom(rewards.length - 1)]));
                break;
            case 407:
                player.getInventory().delete(new Item(407));
                if (Misc.getRandom(3) < 3) {
                    player.getInventory().add(new Item(409));
                } else if (Misc.getRandom(4) < 4) {
                    player.getInventory().add(new Item(411));
                } else
                    player.getInventory().add(new Item(413));
                break;
            case 405:
                player.getInventory().delete(new Item(405));
                if (Misc.getRandom(1) < 1) {
                    int coins = Misc.getRandom(30000);
                    player.getInventory().add(new Item(995, coins));
                    player.message("The casket contained " + coins + " coins!");
                } else
                    player.message("The casket was empty.");
                break;
            case 15084:
                //player.message("Dicing has temporarily been disabled. Sorry for the inconvenience.");
                Gambling.rollDice(player);
                break;
            case 299:
                Gambling.plantSeed(player);
                break;
            case 15104:
                player.message("Combine this with the three other missing parts...");
                return;
            case 15103:
                player.message("Combine this with the three other missing parts...");
                return;
            case 15105:
                player.message("Combine this with the three other missing parts...");
                return;
            case 15106:
                player.message("Combine this with the three other missing parts...");
                return;
            case 19580:
            case 11858:
            case 11860:
            case 11862:
            case 11848:
            case 11856:
            case 11850:
            case 11854:
            case 11852:
            case 11846:
                if (!player.getTimers().getClickDelay().elapsed(2000))
                    return;
                if (player.busy()) {
                    player.message("You cannot open this right now.");
                    return;
                }
                int[] ids = new int[]{11858, 19580, 11860, 11862, 11848, 11856, 11850, 11854, 11852, 11846};
                int[][] itemsArray = new int[][]{
                        {10350, 10348, 10346, 10352},
                        {19308, 19311, 19314, 19317, 19320},
                        {10334, 10330, 10332, 10336},
                        {10342, 10338, 10340, 10344},
                        {4716, 4720, 4722, 4718},
                        {4753, 4757, 4759, 4755},
                        {4724, 4728, 4730, 4726},
                        {4745, 4749, 4751, 4747},
                        {4732, 4734, 4736, 4738},
                        {4708, 4712, 4714, 4710}};

                int[] items = null;
                for (int i = 0; i < ids.length; i++) {
                    if (item.getId() == ids[i]) {
                        items = itemsArray[i];
                        break;
                    }
                }
                if (items == null)
                    items = new int[]{item.getId()};

                if (player.getInventory().getSpaces() < items.length) {
                    player.message("You do not have enough space in your inventory.");
                    return;
                }

                player.getInventory().delete(item);
                for (int i : items)
                    player.getInventory().add(new Item(i));
                player.message("You open the set and find items inside.");
                player.getTimers().getClickDelay().reset();
                break;
            case 952:
                Digging.dig(player);
                break;
            case 292:
                IngredientsBook.readBook(player, 0, false);
                break;
            case 6199:
                MysteryBoxGen.reward(player);
                break;
            case 18768:
                Item mbox = player.getInventory().get(slot);
                if(mbox == null)
                    break;

                MysteryBoxTimedGen.attemptToOpen(player, mbox);
                break;
            case 15501:
                int superiorRewards[][] = {
                        {11133, 15126, 10828, 3751, 3753, 10589, 10564, 6809, 4587, 1249, 3204, 1305, 1377, 1434, 6528, 7158, 4153, 6, 8, 10, 12, 4675, 6914, 6889}, //Uncommon, 0
                        {6739, 15259, 15332, 2579, 6920, 6922, 15241, 11882, 11884, 11906, 20084}, //Rare, 1
                        {6570, 15018, 15019, 15020, 15220, 11730, 18349, 18353, 13896, 18357, 13899, 10551, 4151, 2577}, //Epic, 2
                        {11235, 17273, 14484, 11696, 11698, 11700, 20072, 15486, 19336, 19337, 19338, 19339, 19340} //Legendary, 3
                };
                double superiorNumGen = Math.random();
                /** Chances
                 *  54% chance of Uncommon Items - various high-end coin-bought gear
                 *  30% chance of Rare Items - Highest-end coin-bought gear, Some poor voting-point/pk-point equipment
                 *  11% chance of Epic Items -Better voting-point/pk-point equipment
                 *  5% chance of Legendary Items - Only top-notch voting-point/pk-point equipment
                 */
                int superiorRewardGrade = superiorNumGen >= 0.46 ? 0 : superiorNumGen >= 0.16 ? 1 : superiorNumGen >= 0.05 ? 2 : 3;
                int superiorRewardPos = Misc.getRandom(superiorRewards[superiorRewardGrade].length - 1);
                player.getInventory().delete(new Item(15501));
                player.getInventory().add(new Item(superiorRewards[superiorRewardGrade][superiorRewardPos]));
                player.getInventory().refresh();
                break;
            case 11882:
                player.getInventory().delete(new Item(11882));
                player.getInventory().add(new Item(2595));
                player.getInventory().add(new Item(2591));
                player.getInventory().add(new Item(3473));
                player.getInventory().add(new Item(2597));
                break;
            case 11884:
                player.getInventory().delete(new Item(11884));
                player.getInventory().add(new Item(2595));
                player.getInventory().add(new Item(2591));
                player.getInventory().add(new Item(2593));
                player.getInventory().add(new Item(2597));
                break;
            case 11906:
                player.getInventory().delete(new Item(11906));
                player.getInventory().add(new Item(7394));
                player.getInventory().add(new Item(7390));
                player.getInventory().add(new Item(7386));
                break;
            case 15262:
                if (!player.getTimers().getClickDelay().elapsed(1000))
                    return;
                player.getInventory().delete(new Item(15262));
                player.getInventory().add(new Item(12183, 10000));
                player.getTimers().getClickDelay().reset();
                break;
            case 6:
                DwarfMultiCannon.setUp(player, DwarfMultiCannon.CannonType.NORMAL);
                break;
            case 20494:
                DwarfMultiCannon.setUp(player, DwarfMultiCannon.CannonType.GOLD);
                break;
            case 20498:
                DwarfMultiCannon.setUp(player, DwarfMultiCannon.CannonType.ROYAL);
                break;
        }
    }

    public static void secondAction(Player player, Packet packet) {
        int interfaceId = packet.readLEShortA();
        int slot = packet.readLEShort();
        int itemId = packet.readInt();

        if (player.getRights().isOrHigher(PlayerRights.DEVELOPER))
            player.getPacketSender().sendConsoleMessage("Item [option, id, slot] [" + 2 + ", " + itemId + ", " + slot + "]");

        if (!player.getInventory().validate(itemId, slot))
            return;

        Item item = player.getInventory().get(slot);


        if (item != null && PlayerActionBus.publish(player, new ItemOptionAction(item, slot, 2)))
            return;

        Summoning.Pouches sumPouch = Summoning.Pouches.forId(itemId);
        if (sumPouch != null) {
            Summoning.spawnFamiliar(player, sumPouch);
            return;
        }

        if (WaterContainers.empty(player, item, false))
            return;

        if (item.getDefinition().isBindItem())
            player.getDungManager().bind(item, slot);


        if(player.getEmotesManager().handleItem(item.getId(), 3))
            return;

        switch (item.getId()) {
            case 14701://Herb pouch
                player.getHerbPouch().check();
                break;
            /*case 21776://Shards of armadyl
                if (Herblore.isRawIngredient(player, item))
                    return;
                break;*/
            case 5733:
                if (player.getRights().isOrHigher(PlayerRights.ADMINISTRATOR)) {
                    if (player.getRights() == PlayerRights.ADMINISTRATOR && (player.isInWilderness() || player.isDueling()))
                        return;

                    player.getDialogueManager().startDialogue(new ModPotatoD());
                }
                break;
            case 10014:
                player.getInventory().delete(item);
                if (Misc.random(7) == 0) {
                    player.message("The butterfly jar shatters as you release the creature inside.");
                    return;
                }
                player.message("You release the butterfly into the wild.");
                player.getInventory().add(new Item(10012));
                break;
            case 11258://Jar Generator
                player.message("The generator has " + player.getJarGeneratorCharge() + "% charge remaining.");
                break;
            case 8901:
            case 8903:
            case 8905:
            case 8907:
            case 8909:
            case 8911:
            case 8913:
            case 8915:
            case 8917:
            case 8919:
                player.getInventory().delete(item);
                player.getInventory().add(new Item(8921));
                break;
            case 1712://Glory
            case 1710:
            case 1708:
            case 1706:
            case 11118://Combat Bracelet
            case 11120:
            case 11122:
            case 11124:
            case 2552://Ring of dueling
            case 2554:
            case 2556:
            case 2558:
            case 2560:
            case 2562:
            case 2564:
            case 2566:
            case 3853://Games Necklace
            case 3855:
            case 3857:
            case 3859:
            case 3861:
            case 3863:
            case 3865:
            case 3867:
                JewelryTeleporting.rub(player, item);
                break;
            case 1704:
                player.message("Your amulet has run out of charges.");
                break;
            case 11126:
                player.message("Your bracelet has run out of charges.");
                break;
            case 13281:
            case 13282:
            case 13283:
            case 13284:
            case 13285:
            case 13286:
            case 13287:
            case 13288:
                player.getSlayerManager().slayerRingTeleport(item);
                break;
            case 995:
                if (!player.isCommandViewing())
                    player.getMoneyPouch().deposit(player.getInventory().getAmount(new Item(995)));
                break;
            case 1438:
            case 1448:
            case 1440:
            case 1442:
            case 1444:
            case 1446:
            case 1454:
            case 1452:
            case 1462:
            case 1458:
            case 1456:
            case 1450:
                Runecrafting.handleTalisman(player, item);
                break;
        }
    }

    public void thirdClickAction(Player player, Packet packet) {
        int itemId = packet.readInt();
        int slot = packet.readLEShortA();
        int interfaceId = packet.readLEShortA();

        if (player.getRights().isOrHigher(PlayerRights.DEVELOPER))
            player.getPacketSender().sendConsoleMessage("Item [option, id, slot] [" + 3 + ", " + itemId + ", " + slot + "]");

        if (!player.getInventory().validate(itemId, slot))
            return;

        Item item = player.getInventory().get(slot);

        if (item != null && PlayerActionBus.publish(player, new ItemOptionAction(item, slot, 3)))
            return;

        /*if (JarData.forJar(item.getId()) != null) {
            PuroPuro.lootJar(player, new Item(item, 1), JarData.forJar(item.getId()));
            return;
        }*/

        if ((item.getDefinition().containsOption(2, "Split") || item.getDefinition().containsOption(2, "Dismantle")) && ItemSplitting.handleItemSplit(player, item))
            return;

        if (item.getDefinition().isBindItem())
            player.getDungManager().bind(player.getInventory().get(slot), slot);

        if (OrnamentKits.splitKit(player, item))
            return;

        switch (item.getId()) {
            case 14701://Herb pouch
                player.getHerbPouch().empty();
                break;
            case 4155:
                player.getDialogueManager().startDialogue(new SocialSlayerD());
                break;
            case 5733:
                if (player.getRights().isUpperStaff())
                    player.getDialogueManager().startDialogue(new ModPotatoExtraD());
                break;
            case 11258://Jar Generator
                if (!player.getTimers().getClickDelay().elapsed(3800))
                    return;
                if (player.getJarGeneratorCharge() <= 0)
                    return;
                if (player.getInventory().getSpaces() < 1) {
                    player.message("You do not have enough space in your inventory.");
                    return;
                }
                if (player.busy()) {
                    player.message("You can not do this right now.");
                    return;
                }
                if (player.getJarGeneratorCharge() - 3 <= 0) {
                    player.message("The generator doesn't have enough charge to make that.");
                    return;
                }
                player.animate(6592);
                player.perform(new Graphic(1117));
                player.getInventory().add(new Item(11260));
                player.setJarGeneratorCharge(player.getJarGeneratorCharge() - 3);
                player.getTimers().getClickDelay().reset();
                player.message("You pull a impling jar out of the generator.");
                if (player.getJarGeneratorCharge() <= 0) {
                    player.getInventory().delete(new Item(11258));
                    player.message("The jar generator breaks after using up it's final charge.");
                    player.setJarGeneratorCharge(100);
                }
                break;
            case 20769://Comp cape
            case 20767://Max cape
                if (player.getInterfaceId() > 0) {
                    player.message("Please close the interface you have open before opening another one.");
                    return;
                }
                player.getPacketSender().sendInterface(19109);
                player.getPacketSender().sendCompCapeInterfaceColours();
                break;
            case 15707:
                TeleportHandler.teleportPlayer(player, new Position(3450, 3715), TeleportType.KINSHIP_TELE);
                break;
            case 4079://Yo-yo
                player.getEmotesManager().handleItem(4079, 2);
                break;
            case 4566://Rubber Chicken
                player.getEmotesManager().handleItem(4566);
                break;
            case 27996:
                CharmingImp.sendConfig(player);
                break;
            case 13281:
            case 13282:
            case 13283:
            case 13284:
            case 13285:
            case 13286:
            case 13287:
            case 13288:
                player.getPacketSender().sendInterfaceRemoval();
                player.message(player.getSlayerManager().getCurrentTask() == null ? "You do not have a Slayer task." : "Your current task is to kill another " + player.getSlayerManager().getCount() + " " + Misc.formatText(player.getSlayerManager().getCurrentTask().toString().toLowerCase().replaceAll("_", " ")) + "s.");
                break;
            case 6570:
                if (player.getInventory().contains(new Item(6570)) && player.getInventory().getAmount(new Item(6529)) >= 50000) {
                    player.getInventory().delete(new Item(6570));
                    player.getInventory().delete(new Item(6529, 50000));
                    player.getInventory().add(new Item(23659));
                    player.message("You have upgraded your Fire cape into a TokHaar-Kal cape!");
                } else {
                    player.message("You need at least 50.000 Tokkul to upgrade your Fire Cape into a TokHaar-Kal cape.");
                }
                break;
            case 11283: //DFS
                player.message("Your Dragonfire shield has " + player.getDfsCharges() + "/20 dragon-fire charges.");
                break;
        }
    }

    public static void fourthAction(Player player, Item item) {

        if(player.getEmotesManager().handleItem(item.getId(), 1))
            return;

        switch (item.getId()) {
            case 4155://Enchanted gem
                player.getSlayerManager().checkKillsLeft();
                break;
            case 5733:
                if (player.getRights().isOrHigher(PlayerRights.ADMINISTRATOR)) {
                    if (player.getRights() == PlayerRights.ADMINISTRATOR && (player.isInWilderness() || player.isDueling()))
                        return;
                    player.forceChat("Potato! Om nom nom!");
                    player.getCombat().applyHit(new Hit(990, HitMask.PURPLE, CombatIcon.NONE));
                    player.getSkills().restore(Skill.PRAYER);
                    player.getSettings().set(Settings.SPECIAL_ENERGY, 100);
                    CombatSpecial.updateBar(player);
                }
                break;
            //Nex amour
            case 20138:
            case 20142:
            case 20146:
            case 20150:
            case 20154:
            case 20158:
            case 20162:
            case 20166:
            case 20170:
                //Ancient warrior's equip
            case 13860:
            case 13863:
            case 13866:
            case 13869:
            case 13872:
            case 13875:
            case 13878:
            case 13904:
            case 13886:
            case 13892:
            case 13898:
            case 13889:
            case 13895:
            case 13901:
            case 13907:
                player.message("This armour must be repaired before you are able to equip it.");
                break;
            case 15363://Vial of water pack
                if (!player.getTimers().getClickDelay().elapsed(1300))
                    break;
                if (player.busy()) {
                    player.message("You can not do this right now.");
                    break;
                }
                if (player.getInventory().getSpaces() < 1) {
                    player.message("You do not have enough space in your inventory.");
                    break;
                }
                int amount = player.getInventory().getAmount(new Item(15363));
                if (amount > 0) {
                    player.getInventory().delete(new Item(15363, amount));
                    player.getInventory().add(new Item(228, 50 * amount));
                }
                player.getTimers().getClickDelay().reset();
                break;
            case 15364://Eye of newt pack
                if (!player.getTimers().getClickDelay().elapsed(2000))
                    break;
                if (player.busy()) {
                    player.message("You can not do this right now.");
                    break;
                }
                if (player.getInventory().getSpaces() < 1) {
                    player.message("You do not have enough space in your inventory.");
                    break;
                }
                amount = player.getInventory().getAmount(new Item(15364));
                player.getInventory().delete(new Item(15364, amount));
                player.getInventory().add(new Item(222, 50 * amount));
                break;
            case 15362://Vial pack
                if (!player.getTimers().getClickDelay().elapsed(1300))
                    break;
                if (player.busy()) {
                    player.message("You can not do this right now.");
                    break;
                }
                if (player.getInventory().getSpaces() < 1) {
                    player.message("You do not have enough space in your inventory.");
                    break;
                }
                amount = player.getInventory().getAmount(new Item(15362));
                if (amount > 0) {
                    player.getInventory().delete(new Item(15362, amount));
                    player.getInventory().add(new Item(230, 50 * amount));
                }
                player.getTimers().getClickDelay().reset();
                break;
            case 24337:
                if (player.getInventory().getSpaces() >= 4) {
                    player.getInventory().delete(new Item(24337));
                    player.getInventory().add(new Item(24342), new Item(24346), new Item(24340), new Item(24344), new Item(24303));
                }
                break;
            case 15262://Spirit shard pack
                if (!player.getTimers().getClickDelay().elapsed(1300))
                    return;

                if (player.busy()) {
                    player.message("You can not do this right now.");
                    return;
                }

                if (player.getInventory().getSpaces() < 1) {
                    player.message("You do not have enough space in your inventory.");
                    return;
                }

                amount = player.getInventory().getAmount(new Item(15262));
                if (amount > 0)
                    player.getInventory().delete(new Item(15262, amount));
                player.getInventory().add(new Item(12183, 10000 * amount));
                player.getTimers().getClickDelay().reset();
                return;
        }
    }

    @Override
    public void handleMessage(Player player, Packet packet) {
        if (player.getHealth().getHitpoints() <= 0)
            return;
        int code = packet.getOpcode();
        switch (code) {
            case PacketConstants.FIRST_ITEM_ACTION_OPCODE:
                firstAction(player, packet);
                break;
            case PacketConstants.SECOND_ITEM_ACTION_OPCODE:
                secondAction(player, packet);
                break;
            case PacketConstants.THIRD_ITEM_ACTION_OPCODE:
                thirdClickAction(player, packet);
                break;
        }
    }
}