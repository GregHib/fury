package com.fury.network.packet.impl;

import com.fury.core.action.PlayerActionBus;
import com.fury.core.action.actions.ObjectOptionAction;
import com.fury.core.model.item.Item;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.task.Task;
import com.fury.core.task.TickableTask;
import com.fury.game.GameSettings;
import com.fury.game.container.impl.equip.Slot;
import com.fury.game.container.impl.shop.ShopManager;
import com.fury.game.content.controller.Controller;
import com.fury.game.content.controller.impl.*;
import com.fury.game.content.dialogue.impl.misc.NexEnterD;
import com.fury.game.content.dialogue.impl.misc.Scoreboard;
import com.fury.game.content.dialogue.impl.misc.SimpleMessageD;
import com.fury.game.content.dialogue.impl.npcs.SpiritTreeD;
import com.fury.game.content.dialogue.impl.npcs.SpiritTreeTravelD;
import com.fury.game.content.dialogue.impl.objects.PrayerBookD;
import com.fury.game.content.dialogue.impl.objects.SpellBookD;
import com.fury.game.content.dialogue.impl.objects.WildernessDitchD;
import com.fury.game.content.dialogue.impl.skills.dungeoneering.DungeonLeaveParty;
import com.fury.game.content.dialogue.impl.transportation.DonorTeleportsD;
import com.fury.game.content.eco.ge.GrandExchange;
import com.fury.game.content.global.Achievements;
import com.fury.game.content.global.CrystalChest;
import com.fury.game.content.global.config.ConfigConstants;
import com.fury.game.content.global.dnd.eviltree.EvilTree;
import com.fury.game.content.global.dnd.star.ShootingStar;
import com.fury.game.content.global.events.christmas.ChristmasEvent;
import com.fury.game.content.global.minigames.impl.*;
import com.fury.game.content.global.thievingguild.ThievingGuild;
import com.fury.game.content.global.treasuretrails.TreasureTrailHandlers;
import com.fury.game.content.misc.objects.DwarfMultiCannon;
import com.fury.game.content.misc.objects.MuddyChest;
import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.free.crafting.spinning.Spinning;
import com.fury.game.content.skill.free.dungeoneering.DungeonController;
import com.fury.game.content.skill.free.firemaking.bonfire.Bonfire;
import com.fury.game.content.skill.free.mining.Mining;
import com.fury.game.content.skill.free.mining.MiningBase;
import com.fury.game.content.skill.free.mining.Prospecting;
import com.fury.game.content.skill.free.mining.data.Ores;
import com.fury.game.content.skill.free.mining.data.Pickaxe;
import com.fury.game.content.skill.free.mining.impl.GemMining;
import com.fury.game.content.skill.free.mining.impl.essence.Essence;
import com.fury.game.content.skill.free.mining.impl.essence.EssenceMining;
import com.fury.game.content.skill.free.prayer.Prayerbook;
import com.fury.game.content.skill.free.runecrafting.RuneData;
import com.fury.game.content.skill.free.runecrafting.Runecrafting;
import com.fury.game.content.skill.free.runecrafting.TalismanData;
import com.fury.game.content.skill.free.smithing.Smelting;
import com.fury.game.content.skill.free.smithing.Smithing;
import com.fury.game.content.skill.free.woodcutting.Hatchet;
import com.fury.game.content.skill.free.woodcutting.Trees;
import com.fury.game.content.skill.free.woodcutting.Vines;
import com.fury.game.content.skill.free.woodcutting.Woodcutting;
import com.fury.game.content.skill.member.agility.Agility;
import com.fury.game.content.skill.member.agility.GnomeCourse;
import com.fury.game.content.skill.member.agility.WildernessCourse;
import com.fury.game.content.skill.member.construction.House;
import com.fury.game.content.skill.member.hunter.TrapAction;
import com.fury.game.content.skill.member.summoning.Infusion;
import com.fury.game.content.skill.member.summoning.impl.Summoning;
import com.fury.game.content.skill.member.thieving.Thieving;
import com.fury.game.entity.character.combat.CombatIcon;
import com.fury.game.entity.character.combat.Hit;
import com.fury.game.entity.character.combat.effects.Effects;
import com.fury.game.entity.character.combat.magic.Autocasting;
import com.fury.game.entity.character.player.actions.ForceMovement;
import com.fury.game.content.combat.magic.MagicSpellBook;
import com.fury.game.entity.character.player.content.objects.PrivateObjectManager;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.entity.character.player.link.transportation.FairyRingTeleport;
import com.fury.game.entity.character.player.link.transportation.TeleportHandler;
import com.fury.game.entity.character.player.link.transportation.TeleportType;
import com.fury.game.entity.object.GameObject;
import com.fury.game.node.entity.actor.figure.player.handles.Settings;
import com.fury.game.node.entity.actor.object.ObjectHandler;
import com.fury.game.node.entity.actor.object.ObjectManager;
import com.fury.game.node.entity.actor.object.TempObjectManager;
import com.fury.game.npc.slayer.polypore.PolyporeCreature;
import com.fury.game.system.files.loaders.item.WeaponAnimations;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;
import com.fury.game.world.map.path.RouteEvent;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.game.world.update.flag.block.Direction;
import com.fury.game.world.update.flag.block.HitMask;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.network.packet.Packet;
import com.fury.network.packet.PacketConstants;
import com.fury.network.packet.PacketListener;
import com.fury.util.Misc;
import com.fury.util.RandomUtils;

import java.util.Arrays;
import java.util.Optional;

/**
 * This packet listener is called when a player clicked
 * on a game object.
 *
 * @author relex lawl
 */

public class ObjectActionPacketListener implements PacketListener {

    /**
     * The PacketListener logger to debug information and print out errors.
     */

    private static void firstClick(final Player player, GameObject object) {
        if (!player.getControllerManager().processObjectClick1(object))
            return;

        if (player.getQuestManager().isObjectOption1(object))
            return;

        if (object != null && PlayerActionBus.publish(player, new ObjectOptionAction(object, 1)))
            return;

        if (PyramidPlunder.handleUrns(player, object))
            return;

        if (player.getFarmingManager().isFarming(object.getId(), null, 1))
            return;

        if (object.getId() == 68444) {
            FightKiln.enterFightKiln(player, false);
            return;
        }
        if (Wilderness.isDitch(object.getId()))
            if (!player.getSettings().getBool(Settings.WILDERNESS_WARNINGS))
                Wilderness.crossDitch(player, object);
            else
                player.getDialogueManager().startDialogue(new WildernessDitchD(), object);

        if (Runecrafting.runecraftingAltar(player, object.getId())) {
            RuneData rune = RuneData.forId(object.getId());
            if (rune == null)
                return;
            if (rune == RuneData.ZMI)
                Runecrafting.craftZMI(player);
            else
                Runecrafting.craftRunes(player, rune);
            return;
        }

        if (TrapAction.isTrap(player, object, object.getId()) || TrapAction.isTrap(player, object))
            return;

        if (Agility.handleObject(player, object))
            return;

        if (AllFiredUp.handleObject(player, object))
            return;

        if (EvilTree.handleObjectClick(player, object))
            return;

        if(player.getDungManager().enterResourceDungeon(object))
            return;

        if(MuddyChest.Companion.run(player, object))
            return;

        TalismanData data = TalismanData.forObjectId(object.getId());
        if (data != null) {
            if (player.hasItem(new Item(data.getTalismanId())) || player.getEquipment().get(Slot.HEAD).getId() == data.getTiaraId() || player.getEquipment().get(Slot.HEAD).getId() == 13655) {
                player.moveTo(data.getLocation());
            } else {
                player.message("A mysterious force prevents you from entering without a talisman or tiara.");
            }
            return;
        }

        if (TreasureTrailHandlers.handleObjectClick(player, object))
            return;

        switch (object.getId()) {
            case 25337:
                player.moveTo(new Position(1744, 5321, 1));
                break;
            case 39468:
                player.moveTo(new Position(1745, 5325, 0));
                break;
            case 1756://Water orb ladder
                ObjectHandler.useStairs(player, 827, new Position(2842, 9823), 2, 0, "You climb down the stairs...");
                break;
            case 29358://Air orb ladder
                ObjectHandler.useStairs(player, 827, new Position(3089, 9971), 2, 0, "You climb down the stairs...");
                break;
            case 377://Sinister chest
                if(player.getInventory().contains(new Item(993))) {
                    if(player.getInventory().getSpaces() < 6) {
                        player.message("You need at least 6 free inventory spaces.");
                    } else {
                        player.message("You unlock the chest with your key...", true);
                        player.getInventory().delete(new Item(993));
                        player.getInventory().addSafe(new Item(208, 3));
                        player.getInventory().addSafe(new Item(206, 2));
                        player.getInventory().addSafe(new Item(210));
                        player.getInventory().addSafe(new Item(212));
                        player.getInventory().addSafe(new Item(213));
                        player.getInventory().addSafe(new Item(219));
                        if(RandomUtils.success(0.8)) {
                            player.message("A foul gas seeps from the chest.");
                            player.getEffects().makePoisoned(30);
                        }
                        player.message("You find a lot of herbs in the chest.");
                    }
                } else
                    player.message("You do not have the correct key to open this chest.");
                break;
            case 2290://Yanille dungeon pipe
                if (object.getX() == 2577)
                    Agility.enterPipe(player, object, 49, 2572, 9506, ForceMovement.WEST);
                else
                    Agility.enterPipe(player, object, 49, 2578, 9506, ForceMovement.EAST);
                break;
            case 35969:
                Agility.walkLedge(player, object, false, 40, 2580, 9512, 14);
                break;
            case 2303://Yanille dungeon ledge
                Agility.walkLedge(player, object, true, 40, 2580, 9520, 14);
                break;
            case 32270:
                ObjectHandler.useStairs(player,-1, new Position(player.getX(), 3121), 0, 2);
                break;
            case 37023://Yanille dungeon entrance
                ObjectHandler.useStairs(player,-1, new Position(player.getX(), 9525), 0, 2);
                break;
            case 9311:
            case 9312://Edgeville/G.e shortcut
                if (!Agility.hasLevel(player, 21))
                    return;

                GameWorld.schedule(new TickableTask(true, 1) {
                    boolean withinGE = object.getId() == 9312;
                    Position tile = withinGE ? new Position(3139, 3516) : new Position(3143, 3514);

                    @Override
                    public void tick() {
                        switch (getTick()) {
                            case 0:
                                player.getMovement().lock();
                                break;
                            case 1:
                                player.animate(2589);
                                player.setForceMovement(new ForceMovement(object, 1, withinGE ? ForceMovement.WEST : ForceMovement.EAST));
                                break;
                            case 3:
                                player.moveTo(new Position(3141, 3515, 0));
                                player.animate(2590);
                                break;
                            case 5:
                                player.animate(2591);
                                player.moveTo(tile);
                                break;
                            case 6:
                                player.moveTo(new Position(tile.getX() + (withinGE ? -1 : 1), tile.getY(), tile.getZ()));
                                player.getMovement().unlock();
                                stop();
                                break;
                        }
                    }
                });
                break;
            case 11844://Falador west wall shortcut
                if (!Agility.hasLevel(player, 5))
                    return;
                GameWorld.schedule(new TickableTask(true, 1) {
                    boolean exit = player.getX() >= 2936;
                    Position tile = exit ? new Position(2935, 3355) : new Position(2936, 3355);

                    @Override
                    public void tick() {
                        switch (getTick()) {
                            case 0:
                                player.getMovement().lock();
                                break;
                            case 1:
                                player.animate(839);
                                player.setForceMovement(new ForceMovement(object, 1, exit ? ForceMovement.WEST : ForceMovement.EAST));
                                break;
                            case 2:
                                player.moveTo(tile);
                                player.getMovement().unlock();
                                stop();
                                break;
                        }
                    }
                });
                break;
            case 9310:
                if (!Agility.hasLevel(player, 26))
                    return;
                ObjectHandler.useStairs(player,827, new Position(2948, 3309), 1, 2, "You make you through the tunnel to the other side.");
                break;
            case 9309://Falador south-west tunnel
                if (!Agility.hasLevel(player, 26))
                    return;
                ObjectHandler.useStairs(player,827, new Position(2948, 3313), 1, 2, "You make you through the tunnel to the other side.");
                break;
            case 9302:
                if (!Agility.hasLevel(player, 16))
                    return;
                ObjectHandler.useStairs(player,827, new Position(2575, 3107), 2, 3, "You make you through the tunnel to the other side.");
                break;
            case 9301://Yannile tunnel shortcut
                if (!Agility.hasLevel(player, 16))
                    return;
                ObjectHandler.useStairs(player,827, new Position(2575, 3112), 2, 3, "You make you through the tunnel to the other side.");
                break;
            //Lighthouse
            case 4577://Door
                player.getMovement().lock(2);
                player.getMovement().addWalkSteps(2509, player.getY() == 3635 ? 3636 : 3635, 1, false);
                break;
            case 4383:
                ObjectHandler.useStairs(player,833, new Position(2519, 9993, 1), 1, 2);
                break;
            case 4412:
                ObjectHandler.useStairs(player,828, new Position(2510, 3644), 1, 2);
                break;
            case 4546:
                if (player.getY() <= 10002) {
                    player.message("This door cannot be opened from this side.");
                    return;
                }
                player.getMovement().lock(2);
                player.getMovement().addWalkSteps(2513, 10002, 1, false);
                break;
            case 4545:
                if (player.getY() >= 10003) {
                    player.message("This door cannot be opened from this side.");
                    return;
                }
                player.getMovement().lock(2);
                player.getMovement().addWalkSteps(2516, 10003, 1, false);
                break;
            case 4413:
                ObjectHandler.useStairs(player,828, new Position(2515, 10005, 1), 1, 2);
                break;
            //Waterbirth island dungeon
            case 10193:
                if (object.sameAs(1798, 4406, 3))
                    ObjectHandler.useStairs(player,-1, new Position(2545, 10143), 0, 1);
                break;
            case 8930:
                if (object.sameAs(2542, 3740))
                    ObjectHandler.useStairs(player,-1, new Position(2545, 10143), 0, 1);
                break;
            case 10195:
                if (object.sameAs(1808, 4405, 3))
                    ObjectHandler.useStairs(player,-1, new Position(1810, 4405, 2), 0, 1);
                break;
            case 10196:
                if (object.sameAs(1809, 4405, 2))
                    ObjectHandler.useStairs(player,-1, new Position(1807, 4405, 3), 0, 1);
                break;
            case 10198:
                if (object.sameAs(1823, 4404, 2))
                    ObjectHandler.useStairs(player,-1, new Position(1825, 4404, 3), 0, 1);
                break;
            case 10197:
                if (object.sameAs(1824, 4404, 3))
                    ObjectHandler.useStairs(player,-1, new Position(1823, 4404, 2), 0, 1);
                break;
            case 10199:
                if (object.sameAs(1834, 4389, 3))
                    ObjectHandler.useStairs(player,-1, new Position(1834, 4388, 2), 0, 1);
                break;
            case 10200:
                if (object.sameAs(1834, 4388, 2))
                    ObjectHandler.useStairs(player,-1, new Position(1834, 4390, 3), 0, 1);
                break;
            case 10201:
                if (object.sameAs(1811, 4394))
                    ObjectHandler.useStairs(player,-1, new Position(1810, 4394, 1), 0, 1);
                break;
            case 10202:
                if (object.sameAs(1810, 4394, 1))
                    ObjectHandler.useStairs(player,-1, new Position(1812, 4394, 2), 0, 1);
                break;
            case 10203:
                if (object.sameAs(1799, 4388, 1))
                    ObjectHandler.useStairs(player,-1, new Position(1799, 4386, 2), 0, 1);
                break;
            case 10204:
                if (object.sameAs(1799, 4387, 2))
                    ObjectHandler.useStairs(player,-1, new Position(1799, 4389, 1), 0, 1);
                break;
            case 10205:
                if (object.sameAs(1797, 4382, 2))
                    ObjectHandler.useStairs(player,-1, new Position(1797, 4382, 1), 0, 1);
                break;
            case 10206:
                if (object.sameAs(1798, 4382, 1))
                    ObjectHandler.useStairs(player,-1, new Position(1796, 4382, 2), 0, 1);
                break;
            case 10207:
                if (object.sameAs(1802, 4369, 1))
                    ObjectHandler.useStairs(player,-1, new Position(1800, 4369, 2), 0, 1);
                break;
            case 10208:
                if (object.sameAs(1802, 4369, 2))
                    ObjectHandler.useStairs(player,-1, new Position(1802, 4369, 1), 0, 1);
                break;
            case 10209:
                if (object.sameAs(1826, 4362, 2))
                    ObjectHandler.useStairs(player,-1, new Position(1828, 4362, 1), 0, 1);
                break;
            case 10210:
                if (object.sameAs(1827, 4362, 1))
                    ObjectHandler.useStairs(player,-1, new Position(1825, 4362, 2), 0, 1);
                break;
            case 10211:
                if (object.sameAs(1863, 4371, 1))
                    ObjectHandler.useStairs(player,-1, new Position(1863, 4373, 2), 0, 1);
                break;
            case 10212:
                if (object.sameAs(1863, 4372, 2))
                    ObjectHandler.useStairs(player,-1, new Position(1863, 4370, 1), 0, 1);
                break;
            case 10213:
                if (object.sameAs(1864, 4388, 2))
                    ObjectHandler.useStairs(player,-1, new Position(1864, 4389, 1), 0, 1);
                break;
            case 10214:
                if (object.sameAs(1864, 4389, 1))
                    ObjectHandler.useStairs(player,-1, new Position(1864, 4387, 2), 0, 1);
                break;
            case 10215:
                if (object.sameAs(1890, 4407, 1))
                    ObjectHandler.useStairs(player,-1, new Position(1890, 4408), 0, 1);
                break;
            case 10216:
                if (object.sameAs(1890, 4408))
                    ObjectHandler.useStairs(player,-1, new Position(1890, 4406, 1), 0, 1);
                break;
            case 10229:
                if (object.sameAs(2899, 4449, 0))
                    ObjectHandler.useStairs(player,828, new Position(1912, 4367), 1, 2);
                break;
            case 10217:
                if (object.sameAs(1957, 4371))
                    ObjectHandler.useStairs(player,-1, new Position(1957, 4373, 1), 0, 1);
                break;
            case 10218:
                if (object.sameAs(1957, 4372, 1))
                    ObjectHandler.useStairs(player,-1, new Position(1957, 4370), 0, 1);
                break;
            case 10226:
                if (object.sameAs(1932, 4378, 1))
                    ObjectHandler.useStairs(player,-1, new Position(1932, 4380, 2), 0, 1);
                break;
            case 10225:
                if (object.sameAs(1932, 4379, 2))
                    ObjectHandler.useStairs(player,-1, new Position(1932, 4377, 1), 0, 1);
                break;
            case 10228:
                if (object.sameAs(1961, 4391, 2))
                    ObjectHandler.useStairs(player,-1, new Position(1961, 4393, 3), 0, 1);
                break;
            case 10227:
                if (object.sameAs(1961, 4392, 3))
                    ObjectHandler.useStairs(player,-1, new Position(1961, 4392, 2), 0, 1);
                break;
            case 10194://TODO find correct location
                if (object.sameAs(1975, 4408, 3))
                    ObjectHandler.useStairs(player,-1, new Position(2503, 3636), 0, 1);
                break;
            case 10219:
                if (object.sameAs(1824, 4381, 2))
                    ObjectHandler.useStairs(player,-1, new Position(1824, 4379, 3), 0, 1);
                break;
            case 10220:
                if (object.sameAs(1824, 4380, 3))
                    ObjectHandler.useStairs(player,-1, new Position(1824, 4382, 2), 0, 1);
                break;
            case 10221:
                if (object.sameAs(1838, 4376, 3))
                    ObjectHandler.useStairs(player,-1, new Position(1838, 4374, 2), 0, 1);
                break;
            case 10222:
                if (object.sameAs(1838, 4375, 2))
                    ObjectHandler.useStairs(player,-1, new Position(1838, 4377, 3), 0, 1);
                break;
            case 10223:
                if (object.sameAs(1850, 4386, 2))
                    ObjectHandler.useStairs(player,-1, new Position(1850, 4385, 1), 0, 1);
                break;
            case 10224:
                if (object.sameAs(1850, 4385, 1))
                    ObjectHandler.useStairs(player,-1, new Position(1850, 4387, 2), 0, 1);
                break;
            case 48208://Al kharid strykewyrm stile
                if (player.getY() <= 3162 && player.getY() >= 3160) {
                    if (player.getX() == 3328)
                        player.moveTo(player.copyPosition().add(1, 0));
                    else if (player.getX() == 3329)
                        player.moveTo(player.copyPosition().add(-1, 0));
                }
                break;
            case 48627:
                player.getStatue().click();
                break;

            case 12322://Jadinko entrance
                ObjectHandler.useStairs(player,11042, new Position(3024, 9224), 2, 3);
                GameWorld.schedule(1, () -> {
                    player.getDirection().face(new Position(3024, 9225));
                    player.animate(11043);
                    player.getControllerManager().startController(new JadinkoLair());
                });
                break;
            case 56805://Climbable vines
                ObjectHandler.useStairs(player,3303, new Position(player.getX(), player.getY() + (player.getY() >= object.getY() ? -2 : 2), 0), 1, 2, null, true);
                break;
            case 12328://Jadinko Lair entrance
                ObjectHandler.useStairs(player,3527, new Position(3012, 9275), 5, 6);
                player.setForceMovement(new ForceMovement(player, 3, object, 2, ForceMovement.WEST));
                GameWorld.schedule(4, () -> {
                    player.getDirection().face(new Position(3012, 9274));
                    player.animate(11043);
                    player.getControllerManager().startController(new JadinkoLair());
                });
                break;
            case 16640:
                if (object.getX() == 2330 && object.getY() == 10353)
                    ObjectHandler.useStairs(player,828, new Position(2141, 3944), 1, 2);
                break;
            case 36306:
                if (object.getX() == 2142 && object.getY() == 3944)
                    ObjectHandler.useStairs(player,833, new Position(2329, 10353, 2), 1, 2);
                break;
            case 14315:
                if (Lander.canEnter(player, 0))
                    return;
                break;
            case 25631:
                if (Lander.canEnter(player, 1))
                    return;
                break;
            case 25632:
                if (Lander.canEnter(player, 2))
                    return;
                break;
            case 15477:
            case 15478:
            case 15479:
            case 15480:
            case 15481:
            case 15482://House portals
                House.enterHousePortal(player);
                break;
            case 47765://Christmas cupboard entrance
                ChristmasEvent.enter(player);
                break;
            case 16265://Enchanted valley tree
                Hatchet hatchet = Woodcutting.getHatchet(player, false);
                if (hatchet == null) {
                    player.message("You don't have a hatchet that you can use.");
                    return;
                }
                player.perform(new Animation(hatchet.getEmoteId()));

                Mob treeSpirit = GameWorld.getMobs().spawn(4470, player.copyPosition(), true);
                treeSpirit.setTarget(player);
                treeSpirit.perform(new Graphic(179));
                treeSpirit.forceChat("Leave these woods and never return!");
                player.getMovement().stepAway();
                break;
            case 42366://Enchanted valley rock
                Pickaxe pickaxe = Mining.getPickaxe(player, false);
                if (pickaxe == null) {
                    player.message("You don't have a pickaxe that you can use.");
                    return;
                }

                player.perform(new Animation(pickaxe.getAnimationId()));
                Mob rockGolem = GameWorld.getMobs().spawn(8648, player.copyPosition(), true);
                rockGolem.setTarget(player);
                player.getMovement().stepAway();
                break;
            case 47077://Donor portal
                player.getDialogueManager().startDialogue(new DonorTeleportsD());
                break;
            case 1528://Bandit camp curtain
                TempObjectManager.spawnObjectTemporary(new GameObject(1529, object, object.getType(), object.getDirection()), 60000, false, true);
                break;
            case 63093://Polypore dungeon entrance
                ObjectHandler.useStairs(player,-1, new Position(4620, 5458, 3), 0, 1);
                break;
            case 63094:
                ObjectHandler.useStairs(player,-1, new Position(3410, 3329), 0, 1);
                break;
            case 64294:
            case 64295:
                if (!Agility.hasLevel(player, 73)) {
                    player.message("You need an agility level of at least 73 to pass this obstacle.");
                    return;
                }
                player.animate(6132);
                player.getMovement().lock(3);
                final Position toTile;
                if (object.getId() == 64295 && object.getX() == 4661 && object.getY() == 5476)
                    toTile = new Position(4658, 5476, 3);
                else if (object.getId() == 64295 && object.getX() == 4682 && object.getY() == 5476)
                    toTile = new Position(4685, 5476, 3);
                else if (object.getId() == 64294 && object.getX() == 4684 && object.getY() == 5476)
                    toTile = new Position(4681, 5476, 3);
                else
                    toTile = new Position(4662, 5476, 3);
                player.setForceMovement(new ForceMovement(player, 0, toTile, 2, object.getDirection() == 2 ? ForceMovement.EAST : ForceMovement.WEST));
                GameWorld.schedule(1, () -> player.moveTo(toTile));
                break;
            case 64360:
                if (object.getX() == 4629 && object.getY() == 5453)
                    PolyporeCreature.Companion.useStairs(player, new Position(4629, 5451, 2), true);
                break;
            case 45846://Neem drupes
                int value = player.getConfig().get(ConfigConstants.NEEM_DRUPES);
                if (value == 7)
                    return;
                player.getMovement().lock(2);
                player.animate(15460);
                player.getInventory().add(new Item(22445, 1));
                if (value == 0) {
                    GameWorld.schedule(new Task(false, 9) {
                        @Override
                        public void run() {
                            int value = player.getConfig().get(ConfigConstants.NEEM_DRUPES);
                            player.getConfig().send(ConfigConstants.NEEM_DRUPES, value - 1);
                            if (value == 1)
                                stop();
                        }
                    });
                }
                player.getConfig().send(ConfigConstants.NEEM_DRUPES, value + 1);
                break;
            case 64125:
                value = player.getConfig().get(ConfigConstants.NEEM_DRUPE_2);
                if (value == 7)
                    return;
                player.getMovement().lock(2);
                player.animate(15460);
                player.getInventory().add(new Item(22445, 1));
                if (value == 0) {
                    GameWorld.schedule(new Task(false, 9) {
                        @Override
                        public void run() {
                            int value = player.getConfig().get(ConfigConstants.NEEM_DRUPE_2);
                            player.getConfig().send(ConfigConstants.NEEM_DRUPE_2, value - 1);
                            if (value == 1)
                                stop();
                        }
                    });
                }
                player.getConfig().send(ConfigConstants.NEEM_DRUPE_2, value + 1);
                break;
            case 64361:
                if (object.getX() == 4629 && object.getY() == 5452)
                    PolyporeCreature.Companion.useStairs(player, new Position(4629, 5454, 3), false);
                else if (object.getX() == 4632 && object.getY() == 5442)
                    PolyporeCreature.Companion.useStairs(player, new Position(4632, 5444, 2), false);
                else if (object.getX() == 4633 && object.getY() == 5409)
                    PolyporeCreature.Companion.useStairs(player, new Position(4631, 5409, 3), false);
                else if (object.getX() == 4643 && object.getY() == 5389)
                    PolyporeCreature.Companion.useStairs(player, new Position(4641, 5389, 2), false);
                else if (object.getX() == 4691 && object.getY() == 5468)
                    PolyporeCreature.Companion.useStairs(player, new Position(4691, 5470, 3), false);
                else if (object.getX() == 4689 && object.getY() == 5480)
                    PolyporeCreature.Companion.useStairs(player, new Position(4689, 5478, 2), false);
                else if (object.getX() == 4699 && object.getY() == 5459)
                    PolyporeCreature.Companion.useStairs(player, new Position(4697, 5459, 3), false);
                else if (object.getX() == 4705 && object.getY() == 5461)
                    PolyporeCreature.Companion.useStairs(player, new Position(4705, 5459, 2), false);
                else if (object.getX() == 4718 && object.getY() == 5466)
                    PolyporeCreature.Companion.useStairs(player, new Position(4718, 5468, 1), false);
                break;
            case 64359:
                if (object.getX() == 4632 && object.getY() == 5443)
                    PolyporeCreature.Companion.useStairs(player, new Position(4632, 5443, 1), true);
                else if (object.getX() == 4632 && object.getY() == 5409)
                    PolyporeCreature.Companion.useStairs(player, new Position(4632, 5409, 2), true);
                else if (object.getX() == 4642 && object.getY() == 5389)
                    PolyporeCreature.Companion.useStairs(player, new Position(4642, 5389, 1), true);
                else if (object.getX() == 4652 && object.getY() == 5388)
                    PolyporeCreature.Companion.useStairs(player, new Position(4652, 5388), true);
                else if (object.getX() == 4691 && object.getY() == 5469)
                    PolyporeCreature.Companion.useStairs(player, new Position(4691, 5469, 2), true);
                else if (object.getX() == 4689 && object.getY() == 5479)
                    PolyporeCreature.Companion.useStairs(player, new Position(4689, 5479, 1), true);
                else if (object.getX() == 4698 && object.getY() == 5459)
                    PolyporeCreature.Companion.useStairs(player, new Position(4698, 5459, 2), true);
                else if (object.getX() == 4705 && object.getY() == 5460)
                    PolyporeCreature.Companion.useStairs(player, new Position(4704, 5461, 1), true);
                else if (object.getX() == 4718 && object.getY() == 5467)
                    PolyporeCreature.Companion.useStairs(player, new Position(4718, 5467), true);
                break;
            case 64362:
                if (object.getX() == 4652 && object.getY() == 5387)
                    PolyporeCreature.Companion.useStairs(player, new Position(4652, 5389, 1), false);
                break;//End polypore dungeon
            case 5090:
                Agility.crossLog(player, 33, new Position(2682, 9506));
                break;
            case 5088:
                Agility.crossLog(player, 33, new Position(2687, 9506));
                break;
            case 74630://Slayer portal entrance
                ObjectHandler.useStairs(player,-1, new Position(1375, 5911), 0, 1);
                break;
            case 74625://Slayer portal exit
                ObjectHandler.useStairs(player,-1, new Position(3085, 3496), 0, 1);
                break;
            case 8966:
                ObjectHandler.useStairs(player,-1, new Position(2523, 3739), 1, 2);
                break;
            case 8929://Rock lobster dungeon entrance
                ObjectHandler.useStairs(player,-1, new Position(2442, 10147), 1, 2);
                break;
            case 6899://Lumbridge castle cellar hole
                player.animate(10578);
                ObjectHandler.useStairs(player,-1, object, 1, 2);
                ObjectHandler.useStairs(player,10579, new Position(3219, 9618), 1, 2);
                player.getControllerManager().forceStop();
                player.message("You squeeze through the hole.");
                break;
            case 6898:
                player.animate(10578);
                ObjectHandler.useStairs(player,-1, object, 1, 2);
                ObjectHandler.useStairs(player,10579, new Position(3221, 9618), 1, 2);
                player.getControllerManager().forceStop();
                player.message("You squeeze through the hole.");
                break;
            case 6912:
                player.animate(10578);
                ObjectHandler.useStairs(player,-1, object, 1, 2);
                ObjectHandler.useStairs(player,10579, new Position(player.getX(), player.getY() == 9601 ? player.getY() + 2 : player.getY() - 2, 0), 1, 2);
                break;
            case 5946://Caves exit
                ObjectHandler.useStairs(player,828, new Position(3168, 3171), 1, 2);
                player.getControllerManager().forceStop();
                break;
            case 5947://Caves entrance
                ObjectHandler.useStairs(player,828, new Position(3168, 9572), 1, 2);
                break;
            case 3832://Kalphite queen exit rope
                ObjectHandler.useStairs(player,828, new Position(3509, 9496, 2), 0, 1);
                break;
            case 48802:
                ObjectHandler.useStairs(player,828, new Position(3484, 9510, 2), 0, 1);
                break;
            case 3829:
                ObjectHandler.useStairs(player,828, new Position(3227, 3107), 0, 1);
                break;
            case 48803://Kalphite queen entrance
                ObjectHandler.useStairs(player,828, new Position(3507, 9494), 0, 1);
                break;
            case 35551://Lumbridge/Al kharid toll gate
            case 35549:
                if (player.getY() == 3228 || player.getY() == 3227)
                    player.moveTo(new Position(player.getX() <= 3267 ? 3268 : 3267, player.getY()));
                break;
            case 16154://Barbarian village entrance
                ObjectHandler.useStairs(player,-1, new Position(1859, 5243), 0, 1);
                break;
            case 16123:
            case 16124:
            case 16065:
            case 16066:
            case 16089:
            case 16090:
            case 16043:
            case 16044://Stronghold of security doors
                player.getMovement().lock(2);
                player.animate(4282);
                GameWorld.schedule(1, () -> {
                    Position tile;
                    switch (object.getDirection()) {
                        case 0:
                            tile = new Position(object.getX() == player.getX() ? object.getX() - 1 : object.getX(), player.getY(), 0);
                            break;
                        case 1:
                            tile = new Position(player.getX(), object.getY() == player.getY() ? object.getY() + 1 : object.getY(), 0);
                            break;
                        case 2:
                            tile = new Position(object.getX() == player.getX() ? object.getX() + 1 : object.getX(), player.getY(), 0);
                            break;
                        case 3:
                        default:
                            tile = new Position(player.getX(), object.getY() == player.getY() ? object.getY() - 1 : object.getY(), 0);
                            break;
                    }
                    player.moveTo(tile);
                    player.animate(4283);
                });
                break;
            case 16148:
            case 16146:
                ObjectHandler.useStairs(player,828, new Position(3081, 3421), 1, 2, "You climb up the ladder to the surface.");
                break;
            case 16149://First floor
                ObjectHandler.useStairs(player,828, new Position(2042, 5245), 1, 2, "You climb down the ladder to the next level.");
                break;
            case 16078:
            case 16080:
                ObjectHandler.useStairs(player,828, new Position(1859, 5243), 1, 2, "You climb up the ladder to the level above.");
                break;
            case 16081://Second floor
                ObjectHandler.useStairs(player,828, new Position(2123, 5252), 1, 2, "You climb down the ladder to the next level.");
                break;
            case 16114:
            case 16112://Third floor
                ObjectHandler.useStairs(player,828, new Position(2042, 5245), 1, 2, "You climb up the ladder to the level above.");
                break;
            case 16115:
                ObjectHandler.useStairs(player,828, new Position(2358, 5215), 1, 2, "You climb down the ladder to the next level.");
                break;
            case 16049:
            case 16048://Last floor
                ObjectHandler.useStairs(player,828, new Position(3081, 3421), 1, 2, "You climb up the ladder, which seems to twist wind in all directions.");
                break;
            case 16150://Portals
                ObjectHandler.useStairs(player,-1, new Position(1914, 5222), 0, 1);
                break;
            case 16082:
                ObjectHandler.useStairs(player,-1, new Position(2021, 5223), 0, 1);
                break;
            case 16116:
                ObjectHandler.useStairs(player,-1, new Position(2146, 5287), 0, 1);
                break;
            case 16050:
                ObjectHandler.useStairs(player,-1, new Position(2341, 5219), 0, 1);
                break;
            case 18341://Forinthry/revenant dungeon
                if (object.getX() == 3036 && object.getY() == 10172)
                    ObjectHandler.useStairs(player,-1, new Position(3039, 3765), 0, 1);
                break;
            case 18342:
                if (object.getX() == 3075 && object.getY() == 10057)
                    ObjectHandler.useStairs(player,-1, new Position(3071, 3649), 0, 1);
                break;
            case 20599:
                if (object.getX() == 3038 && object.getY() == 3761)
                    ObjectHandler.useStairs(player,-1, new Position(3037, 10171), 0, 1);
                break;
            case 20600:
                if (object.getX() == 3072 && object.getY() == 3648)
                    ObjectHandler.useStairs(player,-1, new Position(3077, 10058), 0, 1);
                break;
            case 30943://Falador mining guild ladders & stairs
                if ((player.getY() == 9777 || player.getY() == 9776) && player.getX() == 3058 && player.getZ() == 0)
                    player.moveTo(new Position(3061, player.getY() - 6400, 0));
                break;
            case 28742:
                ObjectHandler.useStairs(player,827, new Position(2333, 10015), 1, 2);
                break;
            case 28743:
                ObjectHandler.useStairs(player,828, new Position(3089, 3489), 1, 2);
                break;
            case 30942:
                if (object.getX() == 3019 && object.getY() == 3450)
                    ObjectHandler.useStairs(player,828, new Position(3020, 9850), 1, 2);
            case 6226:
                if (object.getX() == 3019 && object.getY() == 9850)
                    ObjectHandler.useStairs(player,828, new Position(3018, 3450), 1, 2);
                else if (object.getX() == 3019 && object.getY() == 9740)
                    ObjectHandler.useStairs(player,828, new Position(3019, 3341), 1, 2);
                else if (object.getX() == 3019 && object.getY() == 9738)
                    ObjectHandler.useStairs(player,828, new Position(3019, 3337), 1, 2);
                else if (object.getX() == 3018 && object.getY() == 9739)
                    ObjectHandler.useStairs(player,828, new Position(3017, 3339), 1, 2);
                else if (object.getX() == 3020 && object.getY() == 9739)
                    ObjectHandler.useStairs(player,828, new Position(3021, 3339), 1, 2);
            case 2113://Mining guild stairs
                ObjectHandler.useStairs(player,-1, new Position(3021, 9739), 0, 1);
                break;
            case 8742:
                player.getMovement().lock(2);
                if (player.getX() == 2306 && player.getY() == 3194)
                    player.getMovement().addWalkSteps(2304, 3194, -1, false);
                else if (player.getX() == 2306 && player.getY() == 3195)
                    player.getMovement().addWalkSteps(2304, 3195, -1, false);
                else if (player.getX() == 2304 && player.getY() == 3194)
                    player.getMovement().addWalkSteps(2306, 3194, -1, false);
                else if (player.getX() == 2304 && player.getY() == 3195)
                    player.getMovement().addWalkSteps(2306, 3195, -1, false);
                break;
            case 21309:
                player.getMovement().lock(5);
                player.getMovement().addWalkSteps(2343, 3820, -1, false);
                break;
            case 21308:
                player.getMovement().lock(5);
                player.getMovement().addWalkSteps(2343, 3829, -1, false);
                break;
            case 21307:
                if (object.getX() == 2317 && object.getY() == 3831) {

                    if (!Agility.hasLevel(player, 40))
                        return;
                    player.getMovement().lock(5);
                    player.getMovement().addWalkSteps(2317, 3823, -1, false);
                }
                break;
            case 21306://nezi bridge
                if (object.getX() == 2317 && object.getY() == 3824) {

                    if (!Agility.hasLevel(player, 40))
                        return;
                    player.getMovement().lock(5);
                    player.getMovement().addWalkSteps(2317, 3832, -1, false);
                }
                break;
            case 26342:
                int config = player.getConfig().get(ConfigConstants.GODWARS_ENTRANCE);
                if (config == 0) {
                    if (!player.getInventory().delete(new Item(954))) {
                        player.message("You don't have a rope to tie around the pillar.");
                        return;
                    }
                    GodWars.setConfigs(player);
                } else if (config == 1) {
                    ObjectHandler.useStairs(player,827, new Position(2882, 5311, 2), 2, 1, "You climb down the rope...");
                    player.getControllerManager().startController(new GodWars());
                }
                break;
            case 34877:
            case 34878:
            case 9303:
            case 9304:
            case 9305:
            case 9306://Death plateau
                if (!Agility.hasLevel(player, 61))
                    return;
                GameWorld.schedule(1, () -> {
                    int id = object.getId();
                    boolean isGoingDown = id == 34877 ? player.getY() >= 3620 : id == 34878 ? player.getY() >= 9587 : id == 9303 ? player.getX() >= 2856 : id == 9306 ? player.getX() <= 2909 : id == 9305 ? player.getX() <= 2894 : player.getY() >= 3662;
                    if (isGoingDown)
                        ObjectHandler.useStairs(player,3382, id == 34877 ? new Position(2877, 3618) : id == 34878 ? new Position(2875, 3594) : id == 9303 ? new Position(2854, 3664) : id == 9306 ? new Position(2912, 3687) : id == 9305 ? new Position(2897, 3674) : new Position(2875, 3659), 5, 7, null, true);
                    else
                        ObjectHandler.useStairs(player,3381, id == 34877 ? new Position(2876, 3622) : id == 34878 ? new Position(2875, 3598) : id == 9303 ? new Position(2858, 3664) : id == 9306 ? new Position(2908, 3686) : id == 9305 ? new Position(2893, 3673) : new Position(2874, 3663), 5, 7, null, true);

                });
                break;
            case 3803:
                if (!Agility.hasLevel(player, 64))
                    return;
                GameWorld.schedule(1, () -> {
                    boolean isGoingDown = player.getX() >= 2877;
                    if (isGoingDown)
                        ObjectHandler.useStairs(player,15239, new Position(2875, 3672), 4, 5, null, true);
                    else
                        ObjectHandler.useStairs(player,3378, new Position(2879, 3673), 4, 5, null, true);

                });
                break;
            case 3748:
            case 5847:
                if (!Agility.hasLevel(player, 41))
                    return;
                GameWorld.schedule(1, () -> {
                    boolean isFailed = Misc.random(4) == 0;
                    if (isFailed) {
                        player.getCombat().applyHit(new Hit(player, Misc.random(20, 50), HitMask.RED, CombatIcon.NONE));
                        player.message("You skid your knee across the rocks.");
                    }
                    boolean isTravelingEast = object.getId() == 5847 ? player.getX() >= 2760 : (object.getX() == 2817 && object.getY() == 3630) && player.getX() >= 2817;
                    boolean isTravelingNorth = isTravelingEast ? false : (object.getX() == 2846 && object.getY() == 3620) ? player.getY() >= 3620 : player.getY() >= 3675;

                    if (object.getX() == 2846 && object.getY() == 3620) {
                        if (player.getEquipment().get(Slot.FEET).getId() != 3105 && player.getEquipment().get(Slot.FEET).getId() != 6145) {
                            player.getDialogueManager().startDialogue(new SimpleMessageD(), "You need rock climbing boots in order to jump this ledge.");
                            return;
                        }
                    }
                    ObjectHandler.useStairs(player,3377, new Position(isTravelingNorth ? player.getX() : (isTravelingEast ? -1 : 1) + object.getX(), (isTravelingEast || (object.getX() == 2817 && object.getY() == 3630) || object.getId() == 5847 ? 0 : (isTravelingNorth ? -2 : 2)) + player.getY(), 0), 3, 5, null, true);
                });
                break;
            case 35390:
                GodWars.passGiantBoulder(player, object, true);
                break;
            case 15653://Warriors guild door
                if (WarriorsGuild.hasRequirements(player)) {
                    player.moveTo(2876, 3542);
                    player.getControllerManager().startController(new WarriorsGuild());
                }
                break;
            case 24991://Puro puro
                GameWorld.schedule(10, () -> player.getControllerManager().startController(new PuroPuro()));
                TeleportHandler.teleportPlayer(player, new Position(2591, 4320), TeleportType.PURO_PURO);
                return;
            case 70812:
                player.getDialogueManager().sendDialogue("You will be sent to the heart of this cave complex - alone.", "There is no way out other than victory, teleportation, or death.", "Only those who can endure dangerous encounters (combat level 120 or", "more) should proceed.");
                return;
            case 68444:
                FightKiln.enterFightKiln(player, false);
                break;
            case 7351:
                ShopManager.getShops().get(116).open(player);
                break;
            case 29375:
                if (object.sameAs(new Position(3119, 9964)))
                    player.moveTo(3120, 9970);
                else if (object.sameAs(new Position(3120, 9964)))
                    player.moveTo(3121, 9970);
                else if (object.sameAs(new Position(3119, 9969)))
                    player.moveTo(3120, 9963);
                else if (object.sameAs(new Position(3120, 9969)))
                    player.moveTo(3121, 9970);
                break;
            case 26118:
                player.animate(828);
                player.moveTo(new Position(2714, 3472, 3), 2);
                break;
            case 26119:
                player.animate(827);
                player.moveTo(new Position(2714, 3472, 1), 2);
                break;
            case 25939:
                player.animate(828);
                player.moveTo(new Position(2714, 3470), 2);
                break;
            case 25938:
                player.animate(828);
                player.moveTo(new Position(2714, 3470, 1), 2);
                break;
            case 2408:
                //Easy to smuggle using summoning bob but w/e
                if (player.getEquipment().getSpaces() != 15 || player.getInventory().getSpaces() != 28) {
                    player.message("You don't think you should bring anything down into this dungeon.");
                    return;
                }
                player.animate(828);
                player.moveTo(new Position(2822, 9774), 2);
                player.message("You climb down the... er wait what?");
                break;
            case 2407:
                player.moveTo(3086, 3248);
                break;
            case 61498:
            case 52679:
            case 52613:
            case 52609:
            case 16184:
            case 14160:
            case 14154:
            case 14151:
            case 14145:
            case 14142:
            case 14136:
            case 14133:
            case 14130:
            case 14127:
            case 14121:
            case 14118:
            case 14115:
            case 14112:
            case 14109:
            case 14106:
            case 14104:
            case 14103:
            case 14100:
            case 14091:
            case 14088:
            case 14085:
            case 14082:
            case 14079:
            case 14076:
            case 14073:
            case 14070:
            case 14067:
            case 14064:
            case 14061:
            case 14058:
            case 12128://Fairy ring returns
                FairyRingTeleport.homeTeleport(player);
                break;
            case 14097://Fairy ring
                FairyRingTeleport.handleObjectClick(player);
                break;
            case 20210:
                player.moveTo(new Position(2552, player.getY() <= 3559 ? 3561 : 3558, 0));
                break;
            case 19171:
                player.moveTo(new Position(player.getX() == 2523 ? 2522 : 2523, 3375, 0));
                break;
            case 2262:
            case 2261:
                player.moveTo(2880, 2952);
                break;
            case 2216:
                player.moveTo(2867, 2952);
                break;
            case 9470:
                if ((player.getY() == 3216 || player.getY() == 3215) && player.getX() == 2964 && player.getZ() == 0)
                    player.moveTo(new Position(2968, player.getY(), 1));
                break;
            case 9471:
                if ((player.getY() == 3216 || player.getY() == 3215) && player.getX() == 2968 && player.getZ() == 1)
                    player.moveTo(new Position(2964, player.getY(), 0));
                break;
            case 35781:
                if (player.sameAs(new Position(3035, 3362)))
                    player.moveTo(3036, 3363, 1);
                break;
            case 35782:
                if (player.sameAs(new Position(3036, 3363, 1)))
                    player.moveTo(3035, 3362);
                break;
            case 11742:
                if (player.sameAs(new Position(3035, 3345, 1))) {
                    player.animate(828);
                    player.moveTo(new Position(3035, 3345), 2);
                }
                break;
            case 11739:
                if (player.sameAs(new Position(3035, 3345))) {
                    player.animate(828);
                    player.moveTo(new Position(3035, 3345, 1), 2);
                }
                break;
            case 24349:
                if (player.sameAs(new Position(3256, 3420)))
                    player.moveTo(3257, 3421, 1);
                break;
            case 24353:
                if (player.sameAs(new Position(3257, 3421, 1)))
                    player.moveTo(3256, 3420);
                break;
            case 11732:
                if (player.sameAs(new Position(2973, 3386)))
                    player.moveTo(2972, 3385, 1);
                break;
            case 11733:
                if (player.sameAs(new Position(2972, 3385, 1)))
                    player.moveTo(2972, 3385);
                break;
            case 34498:
                if ((player.getY() == 3322 || player.getY() == 3321) && player.getX() == 2662 && player.getZ() == 0)
                    player.moveTo(new Position(2665, player.getY(), 1));
                break;
            case 34499:
                if ((player.getY() == 3322 || player.getY() == 3321) && player.getX() == 2665 && player.getZ() == 1)
                    player.moveTo(new Position(2662, player.getY(), 0));
                break;
            case 7258:
                if (player.sameAs(new Position(3061, 4985, 1)))
                    player.moveTo(2906, 3537);
                break;
            case 7257:
                if (player.sameAs(new Position(2906, 3537)))
                    player.moveTo(3061, 4985, 1);
                break;
            case 34550:
                if (player.sameAs(new Position(2572, 3326, 1)))
                    player.moveTo(2575, 3326);
                break;
            case 34548:
                if ((player.getY() == 3325 || player.getY() == 3326) && player.getX() == 2575 && player.getZ() == 0)
                    player.moveTo(2572, 3326, 1);
                break;
            case 24354:
                if (player.getX() == 3202 && player.getY() == 3417 && player.getZ() == 0) {
                    player.animate(828);
                    player.moveTo(new Position(3202, 3417, 1), 2);
                }
                break;
            case 24355:
                if (player.getX() == 3202 && player.getY() == 3417 && player.getZ() == 1) {
                    player.animate(828);
                    player.moveTo(new Position(3202, 3417), 2);
                }
                break;
            case 2617:
                if ((player.getX() == 3077 || player.getX() == 3078) && player.getY() == 9771 && player.getZ() == 0)
                    player.moveTo(new Position(player.getX() + 38, 3355, 0));
                break;
            case 47643:
                if ((player.getX() == 3115 || player.getX() == 3116) && player.getY() == 3355 && player.getZ() == 0)
                    player.moveTo(new Position(player.getX() - 38, 9771, 0));
                break;
            case 47657:
                if ((player.getX() == 3108 || player.getX() == 3109) && player.getY() == 3366 && player.getZ() == 1)
                    player.moveTo(new Position(player.getX(), 3361, 0));
                break;
            case 47364:
                if ((player.getX() == 3107 || player.getX() == 3108 || player.getX() == 3109 || player.getX() == 3110) && player.getY() == 3361 && player.getZ() == 0)
                    player.moveTo(new Position(player.getX(), 3366, 1));
                break;
            case 47575:
                if (object.sameAs(new Position(3105, 3363, 2))) {
                    player.animate(828);
                    player.moveTo(new Position(3105, 3364, 1), 2);
                }
                break;
            case 47574:
                if (object.sameAs(new Position(3105, 3363, 1))) {
                    player.animate(828);
                    player.moveTo(new Position(3105, 3364, 2), 2);
                }
                break;
            case 36770:
                if (object.sameAs(new Position(3229, 3213, 2))) {
                    player.animate(828);
                    player.moveTo(new Position(3229, 3214, 1), 2);
                } else if (object.sameAs(new Position(3229, 3224, 2))) {
                    player.animate(828);
                    player.moveTo(new Position(3229, 3223, 1), 2);
                }
                break;
            case 36768:
                if (object.sameAs(new Position(3229, 3213))) {
                    player.animate(828);
                    player.moveTo(new Position(3229, 3214, 1), 2);
                } else if (object.sameAs(new Position(3229, 3224))) {
                    player.animate(828);
                    player.moveTo(new Position(3229, 3223, 1), 2);
                }
                break;
            case 30944:
                if ((player.getY() == 3377 || player.getY() == 3376) && player.getX() == 3061 && player.getZ() == 0)
                    player.moveTo(new Position(3058, player.getY() + 6400, 0));
                break;
            case 25604:
                if ((player.getX() == 2750 || player.getX() == 2751) && player.getY() == 3513 && player.getZ() == 1)
                    player.moveTo(new Position(player.getX(), 3508, 0));
                break;
            case 26106:
                if ((player.getX() == 2750 || player.getX() == 2751) && player.getY() == 3508 && player.getZ() == 0)
                    player.moveTo(new Position(player.getX(), 3513, 1));
                break;
            case 26107://camelot ladder up
                if (object.sameAs(new Position(2749, 3491, 1))) {
                    player.animate(828);
                    player.moveTo(new Position(2749, 3492, 2), 2);
                } else if (object.sameAs(new Position(2747, 3493))) {
                    player.animate(828);
                    player.moveTo(new Position(2748, 3493, 1), 2);
                } else if (object.sameAs(new Position(2769, 3493))) {
                    player.animate(828);
                    player.moveTo(new Position(2768, 3493, 1), 2);
                } else if (object.sameAs(new Position(2767, 3491, 1))) {
                    player.animate(828);
                    player.moveTo(new Position(2767, 3492, 2), 2);
                }
                break;
            case 25606://camelot ladder down
                if (object.sameAs(new Position(2749, 3491, 2))) {
                    player.animate(828);
                    player.moveTo(new Position(2749, 3492, 1), 2);
                } else if (object.sameAs(new Position(2747, 3493, 1))) {
                    player.animate(828);
                    player.moveTo(new Position(2748, 3493), 2);
                } else if (object.sameAs(new Position(2769, 3493, 1))) {
                    player.animate(828);
                    player.moveTo(new Position(2768, 3493), 2);
                } else if (object.sameAs(new Position(2767, 3491, 2))) {
                    player.animate(828);
                    player.moveTo(new Position(2767, 3492, 1), 2);
                }
                break;
            case 40027:
                player.animate(828);
                player.moveTo(new Position(3013, 3204), 2);
                break;
            case 40026:
                player.animate(828);
                player.moveTo(new Position(3013, 3204, 1), 2);
                break;
            case 4615:
                player.animate(753);
                Agility.cross(player, 20, new Position(2599, 3608), new Animation(756));
                break;
            case 4616:
                Agility.cross(player, 20, new Position(2595, 3608), new Animation(754));
                break;
            case 51:
                if (player.getX() == 2661)
                    Agility.cross(player, 20, new Position(2662, 3500), new Animation(2240));
                else
                    Agility.cross(player, 20, new Position(2661, 3500), new Animation(2240));
                break;
            case 2296:
                if (object.getX() == 2599)
                    Agility.crossLog(player, 20, new Position(2603, 3477));
                else
                    Agility.crossLog(player, 20, new Position(2598, 3477));
                break;
            case 48496://Dungeoneering entrance
                player.getDungManager().enterDungeon(false);
                break;
            case 12389://Hill giant entrance
                player.animate(827);
                player.moveTo(new Position(3115, 9852), 2);
                break;
            case 55404://Taverly dungeon entrance
                player.animate(827);
                player.moveTo(new Position(2884, 9796), 2);
                break;
            case 2492://Esse mine portal
                TeleportHandler.teleportPlayer(player, new Position(3253, 3401), TeleportType.NORMAL);
                break;
            case 3932://Isafdar Hunter Log
                if (object.getX() == 2259)
                    Agility.crossLog(player, 70, new Position(2264, 3250));
                else
                    Agility.crossLog(player, 70, new Position(2258, 3250));
                break;
            case 5005://Mort Myre Swamp Tree Bridge
                if (object.getX() == 3502 && object.getY() == 3426) {
                    ObjectHandler.useStairs(player,828, new Position(3503, 3431), 1, 2);
                } else if (object.getX() == 3502 && object.getY() == 3431) {
                    ObjectHandler.useStairs(player,828, new Position(3502, 3425), 1, 2);
                }
                break;
            case 5002://Mort Myre Rope Bridge
                if (object.getX() == 3502 && object.getY() == 3430) {
                    if (player.getX() == 3502 && player.getY() == 3429) {
                        player.getMovement().addWalkSteps(0, 1);
                    }
                } else if (object.getX() == 3502 && object.getY() == 3429) {
                    if (player.getX() == 3502) {
                        if (player.getY() == 3430) {
                            player.getMovement().addWalkSteps(0, -1);
                        } else if (player.getY() == 3428) {
                            player.getMovement().addWalkSteps(0, 1);
                        }
                    }
                } else if (object.getX() == 3502 && object.getY() == 3428) {
                    if (player.getX() == 3502) {
                        if (player.getY() == 3429) {
                            player.getMovement().addWalkSteps(0, -1);
                        } else if (player.getY() == 3427) {
                            player.getMovement().addWalkSteps(0, 1);
                        }
                    }
                } else if (object.getX() == 3502 && object.getY() == 3427) {
                    if (player.getX() == 3502 && player.getY() == 3428) {
                        player.getMovement().addWalkSteps(0, -1);
                    }
                }
                break;
            /*case 19651://Net
            case 19678://Swamp lizard
            case 19650://Orange salamander
            case 19662://Red salamander
            case 19670://Black salamander
                NetTrapData trapData = NetTrapData.getTrapData(player, object);
                Trap t = Hunter.getTrapForPosition(trapData);
                if (t == null)
                    return;
                if (t.getOwner() != player) {
                    player.message("This is not your trap.");
                    return;
                }
                NetTrapData.dismantleTrap(t, true);
                break;
            case 19679://Swamp lizard
            case 19652://Orange salamander
            case 19663://Red salamander
            case 19671://Black salamander
                NetTrapData trap = NetTrapData.forPos(object);
                if (trap == null)
                    return;
                trap.layTrap(player);
                break;*/
            case 28714://Summoning ladder up
                player.animate(828);
                player.moveTo(new Position(2926, 3444), 2);
                break;
            case 28676://Summoning ladder down
                player.animate(827);
                player.moveTo(new Position(2209, 5348), 2);
                break;
            case 2147://Wizards tower basement
                player.moveTo(3104, 9576);
                break;
            case 12538://Wizards tower top floor stairs
            case 12536://Wizards tower stairs
                player.moveTo(3104, 3161, 1);
                break;
            case 38279://Wizards tower portal
                player.moveTo(1696, 5460, 2);
                break;
            case 38287://Runecrafting guild portal
                player.moveTo(3107, 3160, 1);
                break;
            case 36687://Lumbridge cellar trapdoor
                player.moveTo(3208, 9616);
                break;
            case 36878://Lumbridge windmill bin
                player.message("You gather up all the flour into a pot.");
                player.animate(833);
                int amount = player.getInventory().getAmount(new Item(1931));
                while (player.getMillFilled() > 0 && player.getInventory().contains(new Item(1931))) {
                    player.addMillFilled(-1);
                    player.getInventory().delete(new Item(1931));
                    player.getInventory().add(new Item(1933));
                }
                if (player.getMillFilled() <= 0)
                    ObjectManager.spawnObject(new GameObject(36879, new Position(3166, 3306)));
                break;
            case 36797://Lumbridge windmill ladder top floor
                player.animate(828);
                player.message("You climb down the ladder..", true);
                GameWorld.schedule(1, () -> {
                    if (object.getX() == 3164 && object.getY() == 3307 && object.getZ() == 2)
                        player.moveTo(3165, 3307, 1);
                });
                break;
            case 36795://Lumbridge windmill ladder
                player.animate(828);
                player.message("You climb up the ladder..", true);
                GameWorld.schedule(1, () -> {
                    if (object.getX() == 3164 && object.getY() == 3307 && object.getZ() == 0)
                        player.moveTo(3165, 3307, 1);
                });
                break;
            case 36775://Lumbridge Castle Stairs Second-Left
                player.moveTo(3205, 3209, 1);
                break;
            case 36778://Lumbridge Castle Stairs Second-Right
                player.moveTo(3205, 3228, 1);
                break;
            case 36773://Lumbridge Castle Stairs Ground-Left
                player.moveTo(3205, 3209, 1);
                break;
            case 36776://Lumbridge Castle Stairs Ground-Right
                player.moveTo(3205, 3228, 1);
                break;
            case 1161://Cabbages
                final String message = "You pick a cabbage.";
                final int itemId = 1965;
                if (player.getInventory().getSpaces() <= 0) {
                    player.message("You do not have enough space in your inventory.");
                } else {
                    GameWorld.schedule(new Task(true, 2) {
                        @Override
                        public void run() {
                            if (player.getInventory().getSpaces() <= 0) {
                                player.message("You do not have enough space in your inventory.");
                                stop();
                                return;
                            }

                            if (player.getInteractingObject() != null) {
                                player.getDirection().face(player.getInteractingObject().copyPosition());
                            }
                            player.getInventory().add(new Item(itemId));
                            player.animate(827);
                            player.message(message);
                        }
                    });
                }
                break;
            case 8689://Cows
                final Item empty_bucket = new Item(1925);
                if (!player.getInventory().contains(empty_bucket)) {
                    player.message("You have nothing to put the milk into.");
                } else {
                    final int a = player.getInventory().getAmount(empty_bucket);
                    GameWorld.schedule(new Task(true, 2) {
                        private int amountFilled = 0;

                        @Override
                        public void run() {
                            if (amountFilled >= a) {
                                stop();
                                return;
                            }
                            if (!player.getInventory().contains(empty_bucket)) {
                                stop();
                                return;
                            }

                            if (player.getInteractingObject() != null) {
                                player.getDirection().face(player.getInteractingObject().copyPosition());
                            }

                            amountFilled++;
                            player.getInventory().delete(empty_bucket);
                            player.getInventory().add(new Item(1927));
                            player.animate(2292);
                            player.message("You fill the bucket with milk.");
                        }
                    });
                }
                break;
            case 50552://Daemonheim stairs
                player.moveTo(3454, 3725);
                    /*if (player.getX() == 3454 && player.getY() == 3723 && player.getZ() == 1) {
                        player.setSkillAnimation(13760);
                        player.getUpdateFlags().flag(Flag.APPEARANCE);
                        TaskManager.submit(new Task(1, player, true) {
                            int tick = 0;

                            @Override
                            public void execute() {
                                tick++;
                                switch (tick) {
                                    case 3:
                                        player.getMovementQueue().walkStep(0, 1);
                                        break;
                                    case 5:
                                        player.moveTo(3454, 3725);
                                        break;
                                    case 6:
                                        player.setSkillAnimation(-1);
                                        player.getUpdateFlags().flag(Flag.APPEARANCE);
                                        stop();
                                        break;
                                }
                            }
                        });
                    }*/
                break;
            case 11211://Mos Le'harmless gangplank to Port Phasmatys
                player.moveTo(3709, 3496);
                break;
            case 11209://Port Phasmatys gangplank to Mos Le'harmless
                player.moveTo(3684, 2953);
                break;
            case 15767://Mos le'harmless Stairs Cave Entrance
                player.moveTo(3748, 9373);
                break;
            case 15811://Mos le'harmless Stairs Cave Exit
            case 15812:
                player.moveTo(3749, 2973);
                break;
            case 15790://Mos le'harmless Stairs Up
                if (object.getX() == 3829 && object.getY() == 9462) {
                    player.moveTo(3831, 3062);
                } else if (object.getX() == 3814 && object.getY() == 9462) {
                    player.moveTo(3816, 3062);
                }
                break;
            case 15791://Mos le'harmless Stairs Down
                if (object.getX() == 3829 && object.getY() == 3062) {
                    player.moveTo(3828, 9462);
                } else if (object.getX() == 3814 && object.getY() == 3062) {
                    player.moveTo(3813, 9462);
                }
                break;
            case 2465://Air
                player.moveTo(3129, 3406);
                break;
            case 2466://Mind
                player.moveTo(2984, 3516);
                break;
            case 2467://Water
                player.moveTo(3183, 3163);
                break;
            case 2468://Earth
                player.moveTo(3305, 3472);
                break;
            case 2469://Fire
                player.moveTo(3311, 3253);
                break;
            case 2470://Body
                player.moveTo(3051, 3444);
                break;
            case 2471://Cosmic
                //player.moveTo(2407, 4379);
                break;
            case 2474://Chaos
                player.moveTo(3059, 3589);
                break;
            case 2472://Law
                player.moveTo(2859, 3379);
                break;
            case 2475://Death
                //player.moveTo(1863, 4639);
                break;
            case 2477://Blood
                //player.moveTo(3560, 9779);
                break;
            case 2473://Nature
                player.moveTo(2867, 3020);
                break;
            case 26849://ZMI Stairs
                player.moveTo(3271, 4861);
                break;
            case 26850:
                player.moveTo(2453, 3231);
                break;
            case 26845://ZMI Crack
                player.moveTo(3308, 4819);
                break;
            case 26844:
                player.moveTo(3312, 4818);
                break;
            case 35997://Ardougne log bridge
                Agility.crossLog(player, 33, new Position(2602, 3336));
                break;
            case 35999://Ardougne log bridge
                Agility.crossLog(player, 33, new Position(2598, 3336));
                break;
            case 9321:
                if (!player.getSkills().hasRequirement(Skill.AGILITY, 62, "use this shortcut"))
                    return;

                boolean startX = player.getX() > 2734;
                player.getMovement().addWalkSteps((startX ? 2735 : 2730) - player.getX(), 10008 - player.getY());
                player.getDirection().face(new Position(startX ? 2730 : 2735, 10008));
                player.getDirection().face(new Position(startX ? 2730 : 2735, 10008));
                GameWorld.schedule(new TickableTask(false, 1) {
                    @Override
                    public void tick() {
                        if (getTick() == 1) {
                            player.animate(2240);
                        } else if (getTick() == 3) {
                            player.moveTo(new Position(startX ? 2730 : 2735, 10008));
                        } else if (getTick() == 4) {
                            player.message("You carefully squeeze your way through the crevice.");
                            stop();
                        }
                    }
                });
                break;
            case 44339:
                if (!Agility.hasLevel(player, 81))
                    return;
                boolean isEast = player.getX() > 2772;
                final Position tile = new Position(isEast ? 2768 : 2775, 10002, 0);
                GameWorld.schedule(new TickableTask(true) {
                    @Override
                    public void tick() {
                        if (getTick() == 0)
                            player.getDirection().face(object);
                        else if (getTick() == 1) {
                            player.animate(10738);
                            player.setForceMovement(new ForceMovement(player, 0, tile, 5, isEast ? 3 : 1));
                        } else if (getTick() == 3)
                            player.moveTo(tile);
                        else if (getTick() == 4) {
                            player.message("Your feet skid as you land floor.");
                            stop();
                        }
                    }
                });
                break;
            case 24359://Varrock museum
                if (object.getX() == 3253 && object.getY() == 3444 && object.getZ() == 2) {
                    player.moveTo(3253, 3442, 1);
                } else if (object.getX() == 3266 && object.getY() == 3453 && object.getZ() == 1) {
                    player.moveTo(3266, 3451);
                }
                break;
            case 24357://Varrock museum first floor going up
                if (object.getX() == 3253 && object.getY() == 3443 && object.getZ() == 1) {
                    player.moveTo(3253, 3446, 2);
                }
                break;
            case 24358://Varrock museum first floor
                if (object.getX() == 3226 && object.getY() == 3452 && object.getZ() == 0) {
                    player.moveTo(3266, 3455, 1);
                }
                break;
            case 24428://Varrock museum downstairs
                player.moveTo(1759, 4958);
                break;
            case 24427://Varrock museum downstairs
                player.moveTo(3258, 3452);
                break;
            case 4500://Fremennik slayer dungeon
                player.moveTo(2796, 3615);
                break;
            case 4499://Fremennik slayer dungeon
                player.moveTo(2808, 10002);
                break;
            case 1317://Spirit Tree
                player.getDialogueManager().startDialogue(new SpiritTreeD());
                break;
            case 36771:
                player.moveTo(3207, 3222, 3);
                break;
            case 36772:
                player.moveTo(3207, 3224, 2);
                break;
            /*case 36778://Lumbridge stairs
                if(object.getChunkX() == 3204 && object.getChunkY() == 3229 && object.getChunkZ() == 2) {
                    player.moveTo(new Position(player.getChunkX(), player.getChunkY(), 1));
                } else if(object.getChunkX() == 3204 && object.getChunkY() == 3207 && object.getChunkZ() == 2) {
                    player.moveTo(new Position(player.getChunkX(), player.getChunkY(), 1));
                }
                break;*/
            case 5100://Brimhaven moss giant tunnel
                if (!player.getSkills().hasRequirement(Skill.AGILITY, 30, "use this shortcut"))
                    return;
                boolean ret = player.getY() > 9568;
                player.moveTo(ret ? new Position(2655, 9566) : new Position(2655, 9573));
                break;
            case 5099://Brimhaven red dragon tunnel
                if (!player.getSkills().hasRequirement(Skill.AGILITY, 50, "use this shortcut"))
                    return;
                ret = player.getY() <= 9493;
                player.moveTo(ret ? new Position(2698, 9499) : new Position(2698, 9492));
                break;
            case 8987://Edgeville portal
                player.moveTo(3033, 2975);
                player.getDirection().face(new Position(3033, 2976));
                player.message("Use the door behind you to exit the prison.");
                break;
            case 31455://Jail exit door
                if (object.getX() == 3033 && object.getY() == 2974 && object.getZ() == 0 &&
                        player.getX() == 3033 && object.getY() == 2974 && player.getZ() == 0) {
                    player.moveTo(3109, 3514);
                }
                break;
            case 31539://Jail door
                if ((player.getX() == 3032 || player.getX() == 3033 || player.getX() == 3034) && player.getY() == 2979) {
                    player.moveTo(3033, 2980);
                } else if ((player.getX() == 3032 || player.getX() == 3033 || player.getX() == 3034) && player.getY() == 2980) {
                    player.moveTo(3033, 2979);
                }

                break;
            case 31529://Jail stairs up
                if (object.getX() == 3037 && object.getY() == 2981 && object.getZ() == 1 && player.isWithinDistance(object, 1)) {
                    player.moveTo(3036, 2981, 2);
                } else if (object.getX() == 3033 && object.getY() == 2981 && object.getZ() == 0 && (
                        (player.getX() == 3033 && player.getY() == 2980) ||
                                (player.getX() == 3034 && player.getY() == 2980) ||
                                (player.getX() == 3032 && player.getY() == 2980) ||
                                (player.getX() == 3032 && player.getY() == 2981) ||
                                (player.getX() == 3032 && player.getY() == 2981))) {
                    player.moveTo(3035, 2981, 1);
                }
                break;
            case 31530://Jail stairs down
                if (object.getX() == 3037 && object.getY() == 2981 && object.getZ() == 2 && player.isWithinDistance(object, 1)) {
                    player.moveTo(3036, 2981, 1);
                } else if (object.getX() == 3033 && object.getY() == 2981 && object.getZ() == 1 && (
                        (player.getX() == 3035 && player.getY() == 2981) ||
                                (player.getX() == 3035 && player.getY() == 2980) ||
                                (player.getX() == 3034 && player.getY() == 2980))) {
                    player.moveTo(3033, 2980);
                }
                break;
            case 5083:
                player.moveTo(2713, 9564);
                break;
            case 5084:
                player.moveTo(2744, 3152);
                break;
            case 11399:
                player.animate(828);
                player.message("You climb down the ladder..", true);
                GameWorld.schedule(1, () -> {
                    if (object.getX() == 3008 && object.getY() == 3150)
                        player.moveTo(3009, 9550);
                });
                break;
            case 9472://Ladder down to Asgarnia ice dungeon
                player.animate(828);
                player.message("You climb down the ladder..", true);
                GameWorld.schedule(1, () -> {
                    if (object.getX() == 3008 && object.getY() == 3150)
                        player.moveTo(3009, 9550);
                });
                break;
            case 33174:
                player.moveTo(3056, 9562);
                break;
            case 33173:
                if (!player.getSkills().hasRequirement(Skill.DUNGEONEERING, 85, "enter here"))
                    return;
                player.moveTo(3056, 9555);
                break;
            case 2874:
                if (player.getInventory().contains(new Item(2412)) || player.getInventory().contains(new Item(2413)) || player.getInventory().contains(new Item(2414))) {
                    player.message("It would be rude to ask for another cape");
                } else {
                    player.animate(645);
                    player.getInventory().add(new Item(2414));
                    player.message("Zamorak thanks you for your loyalty and provides you with a cape.");
                }
                break;
            case 2875:
                if (player.getInventory().contains(new Item(2412)) || player.getInventory().contains(new Item(2413)) || player.getInventory().contains(new Item(2414))) {
                    player.message("It would be rude to ask for another cape");
                } else {
                    player.animate(645);
                    player.getInventory().add(new Item(2413));
                    player.message("Guthix thanks you for your loyalty and provides you with a cape.");
                }
                break;
            case 2873:
                if (player.getInventory().contains(new Item(2412)) || player.getInventory().contains(new Item(2413)) || player.getInventory().contains(new Item(2414))) {
                    player.message("It would be rude to ask for another cape");
                } else {
                    player.animate(645);
                    player.getInventory().add(new Item(2412));
                    player.message("Saradomin thanks you for your loyalty and provides you with a cape.");
                }
                break;
            case 2879://Pool back
                player.moveTo(2542, 4718);
                break;
            case 2878://Pool entrance
                player.moveTo(2509, 4689);
                break;
            case 1816:
                player.animate(2140);
                player.message("You pull the lever..", true);
                GameWorld.schedule(1, () -> {
                    if (object.getX() == 3067 && object.getY() == 10252)
                        TeleportHandler.teleportPlayer(player, new Position(2273, 4680), TeleportType.NORMAL);
                });
                break;
            case 9706:
            case 9707:
                if (object.getX() == 3105 && object.getY() == 3952) {
                    TeleportHandler.pullLever(player, new Position(3105, 3956));
                } else if (object.getX() == 3104 && object.getY() == 3956) {
                    TeleportHandler.pullLever(player, new Position(3105, 3951));
                }
                break;
            case 1765://Ladder down to kbd
                player.animate(828);
                player.message("You climb down the stairs..", true);
                if (object.getX() == 3017 && object.getY() == 3849)
                    player.moveTo(new Position(3069, 10257), 1);
                break;
            case 30863://Monastery ladder
                player.animate(828);
                player.message("You climb down the ladder..", true);
                GameWorld.schedule(1, () -> {
                    if (object.getX() == 3046 && object.getY() == 3483 && object.getZ() == 1) {
                        player.moveTo(3045, 3483);
                    } else if (object.getX() == 3057 && object.getY() == 3483 && object.getZ() == 1) {
                        player.moveTo(3058, 3483);
                    }
                });
                break;
            case 2641://Monastery ladder
                player.animate(828);
                player.message("You climb up the ladder..", true);
                GameWorld.schedule(1, () -> {
                    if (object.getX() == 3046 && object.getY() == 3483 && object.getZ() == 0) {
                        player.moveTo(3045, 3483, 1);
                    } else if (object.getX() == 3057 && object.getY() == 3483 && object.getZ() == 0) {
                        player.moveTo(3058, 3483, 1);
                    }
                });
                break;
            case 42220:
                player.moveTo(3082, 3475);
                break;
            case 42219:
                player.moveTo(1886, 3178);
                break;
            case 27668:
                player.moveTo(3368, 3268);
                break;
            case 27669:
                player.moveTo(3231, 5091);
                break;
            case 28779:
                if (object.getX() == 3142 && object.getY() == 5545) {
                    BorkController.enterBork(player);
                }
                if (object.getX() == 3254 && object.getY() == 5451) {
                    player.moveTo(3250, 5448);
                }
                if (object.getX() == 3250 && object.getY() == 5448) {
                    player.moveTo(3254, 5451);
                }
                if (object.getX() == 3241 && object.getY() == 5445) {
                    player.moveTo(3233, 5445);
                }
                if (object.getX() == 3233 && object.getY() == 5445) {
                    player.moveTo(3241, 5445);
                }
                if (object.getX() == 3259 && object.getY() == 5446) {
                    player.moveTo(3265, 5491);
                }
                if (object.getX() == 3265 && object.getY() == 5491) {
                    player.moveTo(3259, 5446);
                }
                if (object.getX() == 3260 && object.getY() == 5491) {
                    player.moveTo(3266, 5446);
                }
                if (object.getX() == 3266 && object.getY() == 5446) {
                    player.moveTo(3260, 5491);
                }
                if (object.getX() == 3241 && object.getY() == 5469) {
                    player.moveTo(3233, 5470);
                }
                if (object.getX() == 3233 && object.getY() == 5470) {
                    player.moveTo(3241, 5469);
                }
                if (object.getX() == 3235 && object.getY() == 5457) {
                    player.moveTo(3229, 5454);
                }
                if (object.getX() == 3229 && object.getY() == 5454) {
                    player.moveTo(3235, 5457);
                }
                if (object.getX() == 3280 && object.getY() == 5460) {
                    player.moveTo(3273, 5460);
                }
                if (object.getX() == 3273 && object.getY() == 5460) {
                    player.moveTo(3280, 5460);
                }
                if (object.getX() == 3283 && object.getY() == 5448) {
                    player.moveTo(3287, 5448);
                }
                if (object.getX() == 3287 && object.getY() == 5448) {
                    player.moveTo(3283, 5448);
                }
                if (object.getX() == 3244 && object.getY() == 5495) {
                    player.moveTo(3239, 5498);
                }
                if (object.getX() == 3239 && object.getY() == 5498) {
                    player.moveTo(3244, 5495);
                }
                if (object.getX() == 3232 && object.getY() == 5501) {
                    player.moveTo(3238, 5507);
                }
                if (object.getX() == 3238 && object.getY() == 5507) {
                    player.moveTo(3232, 5501);
                }
                if (object.getX() == 3218 && object.getY() == 5497) {
                    player.moveTo(3222, 5488);
                }
                if (object.getX() == 3222 && object.getY() == 5488) {
                    player.moveTo(3218, 5497);
                }
                if (object.getX() == 3218 && object.getY() == 5478) {
                    player.moveTo(3215, 5475);
                }
                if (object.getX() == 3215 && object.getY() == 5475) {
                    player.moveTo(3218, 5478);
                }
                if (object.getX() == 3224 && object.getY() == 5479) {
                    player.moveTo(3222, 5474);
                }
                if (object.getX() == 3222 && object.getY() == 5474) {
                    player.moveTo(3224, 5479);
                }
                if (object.getX() == 3208 && object.getY() == 5471) {
                    player.moveTo(3210, 5477);
                }
                if (object.getX() == 3210 && object.getY() == 5477) {
                    player.moveTo(3208, 5471);
                }
                if (object.getX() == 3214 && object.getY() == 5456) {
                    player.moveTo(3212, 5452);
                }
                if (object.getX() == 3212 && object.getY() == 5452) {
                    player.moveTo(3214, 5456);
                }
                if (object.getX() == 3204 && object.getY() == 5445) {
                    player.moveTo(3197, 5448);
                }
                if (object.getX() == 3197 && object.getY() == 5448) {
                    player.moveTo(3204, 5445);
                }
                if (object.getX() == 3189 && object.getY() == 5444) {
                    player.moveTo(3187, 5460);
                }
                if (object.getX() == 3187 && object.getY() == 5460) {
                    player.moveTo(3189, 5444);
                }
                if (object.getX() == 3192 && object.getY() == 5472) {
                    player.moveTo(3186, 5472);
                }
                if (object.getX() == 3186 && object.getY() == 5472) {
                    player.moveTo(3192, 5472);
                }
                if (object.getX() == 3185 && object.getY() == 5478) {
                    player.moveTo(3191, 5482);
                }
                if (object.getX() == 3191 && object.getY() == 5482) {
                    player.moveTo(3185, 5478);
                }
                if (object.getX() == 3171 && object.getY() == 5473) {
                    player.moveTo(3167, 5471);
                }
                if (object.getX() == 3167 && object.getY() == 5471) {
                    player.moveTo(3171, 5473);
                }
                if (object.getX() == 3171 && object.getY() == 5478) {
                    player.moveTo(3167, 5478);
                }
                if (object.getX() == 3167 && object.getY() == 5478) {
                    player.moveTo(3171, 5478);
                }
                if (object.getX() == 3168 && object.getY() == 5456) {
                    player.moveTo(3178, 5460);
                }
                if (object.getX() == 3178 && object.getY() == 5460) {
                    player.moveTo(3168, 5456);
                }
                if (object.getX() == 3191 && object.getY() == 5495) {
                    player.moveTo(3194, 5490);
                }
                if (object.getX() == 3194 && object.getY() == 5490) {
                    player.moveTo(3191, 5495);
                }
                if (object.getX() == 3141 && object.getY() == 5480) {
                    player.moveTo(3142, 5489);
                }
                if (object.getX() == 3142 && object.getY() == 5489) {
                    player.moveTo(3141, 5480);
                }
                if (object.getX() == 3142 && object.getY() == 5462) {
                    player.moveTo(3154, 5462);
                }
                if (object.getX() == 3154 && object.getY() == 5462) {
                    player.moveTo(3142, 5462);
                }
                if (object.getX() == 3143 && object.getY() == 5443) {
                    player.moveTo(3155, 5449);
                }
                if (object.getX() == 3155 && object.getY() == 5449) {
                    player.moveTo(3143, 5443);
                }
                if (object.getX() == 3307 && object.getY() == 5496) {
                    player.moveTo(3317, 5496);
                }
                if (object.getX() == 3317 && object.getY() == 5496) {
                    player.moveTo(3307, 5496);
                }
                if (object.getX() == 3318 && object.getY() == 5481) {
                    player.moveTo(3322, 5480);
                }
                if (object.getX() == 3322 && object.getY() == 5480) {
                    player.moveTo(3318, 5481);
                }
                if (object.getX() == 3299 && object.getY() == 5484) {
                    player.moveTo(3303, 5477);
                }
                if (object.getX() == 3303 && object.getY() == 5477) {
                    player.moveTo(3299, 5484);
                }
                if (object.getX() == 3286 && object.getY() == 5470) {
                    player.moveTo(3285, 5474);
                }
                if (object.getX() == 3285 && object.getY() == 5474) {
                    player.moveTo(3286, 5470);
                }
                if (object.getX() == 3290 && object.getY() == 5463) {
                    player.moveTo(3302, 5469);
                }
                if (object.getX() == 3302 && object.getY() == 5469) {
                    player.moveTo(3290, 5463);
                }
                if (object.getX() == 3296 && object.getY() == 5455) {
                    player.moveTo(3299, 5450);
                }
                if (object.getX() == 3299 && object.getY() == 5450) {
                    player.moveTo(3296, 5455);
                }
                if (object.getX() == 3280 && object.getY() == 5501) {
                    player.moveTo(3285, 5508);
                }
                if (object.getX() == 3285 && object.getY() == 5508) {
                    player.moveTo(3280, 5501);
                }
                if (object.getX() == 3300 && object.getY() == 5514) {
                    player.moveTo(3297, 5510);
                }
                if (object.getX() == 3297 && object.getY() == 5510) {
                    player.moveTo(3300, 5514);
                }
                if (object.getX() == 3289 && object.getY() == 5533) {
                    player.moveTo(3288, 5536);
                }
                if (object.getX() == 3288 && object.getY() == 5536) {
                    player.moveTo(3289, 5533);
                }
                if (object.getX() == 3285 && object.getY() == 5527) {
                    player.moveTo(3282, 5531);
                }
                if (object.getX() == 3282 && object.getY() == 5531) {
                    player.moveTo(3285, 5527);
                }
                if (object.getX() == 3325 && object.getY() == 5518) {
                    player.moveTo(3323, 5531);
                }
                if (object.getX() == 3323 && object.getY() == 5531) {
                    player.moveTo(3325, 5518);
                }
                if (object.getX() == 3299 && object.getY() == 5533) {
                    player.moveTo(3297, 5536);
                }
                if (object.getX() == 3297 && object.getY() == 5536) {
                    player.moveTo(3299, 5533);
                }
                if (object.getX() == 3321 && object.getY() == 5554) {
                    player.moveTo(3315, 5552);
                }
                if (object.getX() == 3315 && object.getY() == 5552) {
                    player.moveTo(3321, 5554);
                }
                if (object.getX() == 3291 && object.getY() == 5555) {
                    player.moveTo(3285, 5556);
                }
                if (object.getX() == 3285 && object.getY() == 5556) {
                    player.moveTo(3291, 5555);
                }
                if (object.getX() == 3266 && object.getY() == 5552) {
                    player.moveTo(3262, 5552);
                }
                if (object.getX() == 3262 && object.getY() == 5552) {
                    player.moveTo(3266, 5552);
                }
                if (object.getX() == 3256 && object.getY() == 5561) {
                    player.moveTo(3253, 5561);
                }
                if (object.getX() == 3253 && object.getY() == 5561) {
                    player.moveTo(3256, 5561);
                }
                if (object.getX() == 3249 && object.getY() == 5546) {
                    player.moveTo(3252, 5543);
                }
                if (object.getX() == 3252 && object.getY() == 5543) {
                    player.moveTo(3249, 5546);
                }
                if (object.getX() == 3261 && object.getY() == 5536) {
                    player.moveTo(3268, 5534);
                }
                if (object.getX() == 3268 && object.getY() == 5534) {
                    player.moveTo(3261, 5536);
                }
                if (object.getX() == 3243 && object.getY() == 5526) {
                    player.moveTo(3241, 5529);
                }
                if (object.getX() == 3241 && object.getY() == 5529) {
                    player.moveTo(3243, 5526);
                }
                if (object.getX() == 3230 && object.getY() == 5547) {
                    player.moveTo(3226, 5553);
                }
                if (object.getX() == 3226 && object.getY() == 5553) {
                    player.moveTo(3230, 5547);
                }
                if (object.getX() == 3206 && object.getY() == 5553) {
                    player.moveTo(3204, 5546);
                }
                if (object.getX() == 3204 && object.getY() == 5546) {
                    player.moveTo(3206, 5553);
                }
                if (object.getX() == 3211 && object.getY() == 5533) {
                    player.moveTo(3214, 5533);
                }
                if (object.getX() == 3214 && object.getY() == 5533) {
                    player.moveTo(3211, 5533);
                }
                if (object.getX() == 3208 && object.getY() == 5527) {
                    player.moveTo(3211, 5523);
                }
                if (object.getX() == 3211 && object.getY() == 5523) {
                    player.moveTo(3208, 5527);
                }
                if (object.getX() == 3201 && object.getY() == 5531) {
                    player.moveTo(3197, 5529);
                }
                if (object.getX() == 3197 && object.getY() == 5529) {
                    player.moveTo(3201, 5531);
                }
                if (object.getX() == 3202 && object.getY() == 5515) {
                    player.moveTo(3196, 5512);
                }
                if (object.getX() == 3196 && object.getY() == 5512) {
                    player.moveTo(3202, 5515);
                }
                if (object.getX() == 3190 && object.getY() == 5515) {
                    player.moveTo(3190, 5519);
                }
                if (object.getX() == 3190 && object.getY() == 5519) {
                    player.moveTo(3190, 5515);
                }
                if (object.getX() == 3185 && object.getY() == 5518) {
                    player.moveTo(3181, 5517);
                }
                if (object.getX() == 3181 && object.getY() == 5517) {
                    player.moveTo(3185, 5518);
                }
                if (object.getX() == 3187 && object.getY() == 5531) {
                    player.moveTo(3182, 5530);
                }
                if (object.getX() == 3182 && object.getY() == 5530) {
                    player.moveTo(3187, 5531);
                }
                if (object.getX() == 3169 && object.getY() == 5510) {
                    player.moveTo(3159, 5501);
                }
                if (object.getX() == 3159 && object.getY() == 5501) {
                    player.moveTo(3169, 5510);
                }
                if (object.getX() == 3165 && object.getY() == 5515) {
                    player.moveTo(3173, 5530);
                }
                if (object.getX() == 3173 && object.getY() == 5530) {
                    player.moveTo(3165, 5515);
                }
                if (object.getX() == 3156 && object.getY() == 5523) {
                    player.moveTo(3152, 5520);
                }
                if (object.getX() == 3152 && object.getY() == 5520) {
                    player.moveTo(3156, 5523);
                }
                if (object.getX() == 3148 && object.getY() == 5533) {
                    player.moveTo(3153, 5537);
                }
                if (object.getX() == 3153 && object.getY() == 5537) {
                    player.moveTo(3148, 5533);
                }
                if (object.getX() == 3143 && object.getY() == 5535) {
                    player.moveTo(3147, 5541);
                }
                if (object.getX() == 3147 && object.getY() == 5541) {
                    player.moveTo(3143, 5535);
                }
                if (object.getX() == 3168 && object.getY() == 5541) {
                    player.moveTo(3171, 5542);
                }
                if (object.getX() == 3171 && object.getY() == 5542) {
                    player.moveTo(3168, 5541);
                }
                if (object.getX() == 3190 && object.getY() == 5549) {
                    player.moveTo(3190, 5554);
                }
                if (object.getX() == 3190 && object.getY() == 5554) {
                    player.moveTo(3190, 5549);
                }
                if (object.getX() == 3180 && object.getY() == 5557) {
                    player.moveTo(3174, 5558);
                }
                if (object.getX() == 3174 && object.getY() == 5558) {
                    player.moveTo(3180, 5557);
                }
                if (object.getX() == 3162 && object.getY() == 5557) {
                    player.moveTo(3158, 5561);
                }
                if (object.getX() == 3158 && object.getY() == 5561) {
                    player.moveTo(3162, 5557);
                }
                if (object.getX() == 3166 && object.getY() == 5553) {
                    player.moveTo(3162, 5545);
                }
                if (object.getX() == 3162 && object.getY() == 5545) {
                    player.moveTo(3166, 5553);
                }
                break;
            case 28891://Wilderness rifts
                if (player.getCombat().isInCombat())
                    player.message("You cannot enter the rift whilst your under attack.");
                else
                    ObjectHandler.useStairs(player,827, new Position(3183, 5470), 1, 2);
                break;
            case 28892:
                if (player.getCombat().isInCombat())
                    player.message("You cannot enter the rift whilst your under attack.");
                else if (object.getX() == 3165 && object.getY() == 3561)
                    ObjectHandler.useStairs(player,827, new Position(3292, 5479), 1, 2);
                else if (object.getX() == 3176 && object.getY() == 3585)
                    ObjectHandler.useStairs(player,827, new Position(3291, 5538), 1, 2);
                break;
            case 28893:
                if (player.getCombat().isInCombat())
                    player.message("You cannot enter the rift whilst your under attack.");
                else if (object.getX() == 3119 && object.getY() == 3571)
                    ObjectHandler.useStairs(player,827, new Position(3248, 5490), 1, 2);
                else if (object.getX() == 3130 && object.getY() == 3588)
                    ObjectHandler.useStairs(player,827, new Position(3234, 5559), 1, 2);
                break;
            case 28782:
                if (object.getX() == 3183 && object.getY() == 5470)
                    ObjectHandler.useStairs(player,828, new Position(3057, 3551), 1, 2);
                else if (object.getX() == 3248 && object.getY() == 5490)
                    ObjectHandler.useStairs(player,828, new Position(3120, 3571), 1, 2);
                else if (object.getX() == 3234 && object.getY() == 5559)
                    ObjectHandler.useStairs(player,828, new Position(3130, 3586), 1, 2);
                else if (object.getX() == 3291 && object.getY() == 5538)
                    ObjectHandler.useStairs(player,828, new Position(3177, 3585), 1, 2);
                else if (object.getX() == 3292 && object.getY() == 5479)
                    ObjectHandler.useStairs(player,828, new Position(3165, 3562), 1, 2);
                break;
            case 5259:
                if (player.getX() == 3659 || player.getX() == 3660) {
                    if (player.getY() == 3507)
                        player.moveTo(player.copyPosition().add(0, 2));
                    else if (player.getY() == 3509)
                        player.moveTo(player.copyPosition().add(0, -2));
                } else if (player.getY() == 3486 || player.getY() == 3485) {
                    if (player.getX() == 3653)
                        player.moveTo(player.copyPosition().add(-2, 0));
                    else if (player.getX() == 3651)
                        player.moveTo(player.copyPosition().add(2, 0));
                }
                break;
            case 10805:
            case 10806:
                GrandExchange.open(player);
                break;
            //Clan wars portals
            case 28213:
                player.message("The portal does not seem to be functioning properly.");
                break;
            case 38699:
                player.message("The portal does not seem to be functioning properly.");
                break;
            case 38698:
                player.getControllerManager().startController(new FreeForAllController(), false);
                player.moveTo(2815, 5511);
                break;
            case 45803:
            case 1767:
                player.getDialogueManager().startDialogue(new DungeonLeaveParty());
                break;
            case 7353:
                player.moveTo(new Position(2439, 4956, player.getZ()));
                break;
            case 7321:
                player.moveTo(new Position(2452, 4944, player.getZ()));
                break;
            case 7322:
                player.moveTo(new Position(2455, 4964, player.getZ()));
                break;
            case 7315:
                player.moveTo(new Position(2447, 4956, player.getZ()));
                break;
            case 7316:
                player.moveTo(new Position(2471, 4956, player.getZ()));
                break;
            case 7318:
                player.moveTo(new Position(2464, 4963, player.getZ()));
                break;
            //case 7319:
            //player.moveTo(new Position(2467, 4940, player.getChunkZ()));
            //break;
            case 7324:
                player.moveTo(new Position(2481, 4956, player.getZ()));
                break;

            case 7319:
                if (object.getX() == 2481 && object.getY() == 4956)
                    player.moveTo(new Position(2467, 4940, player.getZ()));
                break;

            case 11356:
                player.moveTo(2860, 9741);
                player.message("You step through the portal..");
                break;
            case 47180:
                player.message("You activate the device..");
                player.moveTo(2586, 3912);
                break;
            case 9319:
                if (!player.getSkills().hasRequirement(Skill.AGILITY, 61, "climb this"))
                    return;
                if (player.getZ() == 0)
                    player.moveTo(3422, 3549, 1);
                else if (player.getZ() == 1) {
                    if (object.getX() == 3447)
                        player.moveTo(3447, 3575, 2);
                    else
                        player.moveTo(3447, 3575);
                }
                break;

            case 9320:
                if (!player.getSkills().hasRequirement(Skill.AGILITY, 61, "climb this"))
                    return;
                if (player.getZ() == 1)
                    player.moveTo(3422, 3549);
                else if (player.getZ() == 0)
                    player.moveTo(3447, 3575, 1);
                else if (player.getZ() == 2)
                    player.moveTo(3447, 3575, 1);
                player.animate(828);
                break;
            case 7836:
            case 7808:
                int amt = player.getInventory().getAmount(new Item(6055));
                if (amt > 0) {
                    player.getInventory().delete(new Item(6055, amt));
                    player.message("You put the weed in the compost bin.");
                    player.getSkills().addExperience(Skill.FARMING, 1 * amt);
                } else {
                    player.message("You do not have any weeds in your inventory.");
                }
                break;
            case 5960: //Levers
            case 5959:
                if (player.getEffects().hasActiveEffect(Effects.TELEPORT_BLOCK)) {
                    player.message("You have been teleblocked.");
                    return;
                }
                if (object.getX() == 3090 && object.getY() == 3956) {//Mage bank out
                    player.getDirection().setDirection(Direction.WEST);
                    TeleportHandler.pullLever(player, new Position(2539, 4712));
                } else if (object.getX() == 2539 && object.getY() == 4712) {//Mage bank in
                    player.getDirection().setDirection(Direction.SOUTH);
                    TeleportHandler.pullLever(player, new Position(3090, 3956));
                } else {
                    player.getDirection().setDirection(Direction.WEST);
                    TeleportHandler.pullLever(player, new Position(3090, 3475));
                }
                break;
            case 5096:
                if (object.getX() == 2644 && object.getY() == 9593)
                    player.moveTo(2649, 9591);
                break;

            case 5094:
                if (object.getX() == 2648 && object.getY() == 9592)
                    player.moveTo(2643, 9594, 2);
                break;

            case 5098:
                if (object.getX() == 2635 && object.getY() == 9511)
                    player.moveTo(2637, 9517);
                break;

            case 5097:
                if (object.getX() == 2635 && object.getY() == 9514)
                    player.moveTo(2636, 9510, 2);
                break;
            case 4493:
                if (player.getX() >= 3432)
                    player.moveTo(3433, 3538, 1);
                break;
            case 4494:
                player.moveTo(3438, 3538);
                break;
            case 4495:
                player.moveTo(3417, 3541, 2);
                break;
            case 4496:
                player.moveTo(3412, 3541, 1);
                break;
            case 25339:
            case 25340:
                player.moveTo(new Position(1778, 5346, player.getZ() == 0 ? 1 : 0));
                break;
            case 10230:
                player.perform(new Animation(827));
                player.message("You climb " + "down" + " the ladder..", true);
                GameWorld.schedule(1, () -> player.moveTo(new Position(2900, 4449)));
                break;
            case 26933://Edge dungeon - 1568
                player.moveTo(3097, 9868);
                break;
            case 5103: //Brimhaven vines
            case 5104:
            case 5105:
            case 5106:
            case 5107:
                player.getActionManager().setAction(new Vines(object));
                break;
            case 57225:
                Optional<Controller> currentController = Optional.ofNullable(player.getControllerManager().getController());
                if(player.getX() < 2909) {
                    currentController.ifPresent(Controller::removeController);
                    player.getDialogueManager().startDialogue(new NexEnterD());
                } else {
                    currentController.ifPresent(controller -> {
                        if(controller instanceof ZarosGodwars) {
                            ((ZarosGodwars) controller).remove();
                            player.moveTo(2907, 5204);
                        }
                    });
                }
                break;
            /*case 26945:
                player.getDialogueManager().startDialogue(new GoodwillD());
                break;*/
            case 9294:
                if (!player.getSkills().hasRequirement(Skill.AGILITY, 80, "use this shortcut"))
                    return;
                player.animate(769);
                player.moveTo(new Position(player.getX() >= 2880 ? 2878 : 2880, 9813), 1);
                break;
            case 9293:
                boolean back = player.getX() > 2888;
                player.moveTo(back ? new Position(2886, 9799) : new Position(2892, 9799));
                Achievements.finishAchievement(player, Achievements.AchievementData.USE_TAVERLEY_SHORTCUT);
                break;
            case 1755:
            case 32015:
            case 29355:
                player.animate(828);
                player.message("You climb the stairs..", true);
                GameWorld.schedule(1, () -> {
                    if (object.getX() == 2547 && object.getY() == 9951) {
                        player.moveTo(2548, 3551);
                    } else if (object.getX() == 3005 && object.getY() == 10363) {
                        player.moveTo(3005, 3962);
                    } else if (object.getX() == 3084 && object.getY() == 9672) {
                        player.moveTo(3117, 3244);
                    } else if (object.getX() == 3097 && object.getY() == 9867) {
                        player.moveTo(3096, 3468);
                    } else if (object.getX() == 3069 && object.getY() == 10256) {
                        player.moveTo(3017, 3850);
                    } else if (object.getX() == 3008 && object.getY() == 9550) {//Asgarnia Ice Dungeon
                        player.moveTo(3009, 3150);
                    } else if (object.getX() == 3103 && object.getY() == 9576) {//Wizard tower basement
                        player.moveTo(3105, 3162);
                    } else if (object.getX() == 3209 && object.getY() == 9616) {//Lumbridge cellar
                        player.moveTo(3210, 3216);
                    } else if (object.getX() == 2884 && object.getY() == 9797) {//Taverley ladder
                        player.moveTo(2884, 3398);
                    } else if (object.getX() == 3116 && object.getY() == 9852) {//Hill giants ladder
                        player.moveTo(3115, 3452);
                    } else if(object.getX() == 3088 && object.getY() == 9971) {//Air obelisk
                        player.moveTo(3089, 3571);
                    } else if(object.getX() == 2842 && object.getY() == 9824) {//Water obelisk
                        player.moveTo(2842, 3423);
                    }
                });
                break;
            case 5110:
                player.moveTo(2647, 9557);
                player.message("You pass the stones..", true);
                break;
            case 5111:
                player.moveTo(2649, 9562);
                player.message("You pass the stones..", true);
                break;
            case 6434:
                player.animate(827);
                player.message("You enter the trapdoor..", true);
                player.moveTo(new Position(3085, 9672), 1);
                break;
            case 12356:
                if(!player.getTimers().getClickDelay().elapsed(3000))
                    return;
                if (!player.getMinigameAttributes().getRecipeForDisasterAttributes().hasFinishedPart(0)) {
                    player.message("Perhaps it would be a good idea to talk to the Gypsy first?");
                    return;
                }
                player.getMinigameAttributes().getRecipeForDisasterAttributes().setPartFinished(1, true);
                if (!(player.getControllerManager().getController() instanceof RecipeForDisaster))
                    player.getControllerManager().startController(new RecipeForDisaster());
                player.getTimers().getClickDelay().reset();
                break;
            case 9356:
                FightCavesController.enterFightCaves(player);
                break;
            case 3203:
                /*if (player.getLocation() == Location.DUEL_ARENA && player.getDueling().duelingStatus == 5) {
                    player.getDueling().forfeitDuel();
                }*/
                break;
            case 46542:
            case 30205:
                player.getDialogueManager().startDialogue(new Scoreboard());
                break;
            case 28734:
            case 28716:
                if (!player.busy()) {
                    player.getSkills().refresh(Skill.SUMMONING);
                    Infusion.openInfuseInterface(player, false, false);
                } else
                    player.message("Please finish what you're doing before opening this.");
                break;
            case 9:
            case 29402:
            case 29405:
                DwarfMultiCannon.pickupCannon(player, 3, object);
                break;
            case 8:
            case 29401:
            case 29404:
                DwarfMultiCannon.pickupCannon(player, 2, object);
                break;
            case 7:
            case 29398:
            case 29403:
                DwarfMultiCannon.pickupCannon(player, 1, object);
                break;
            case 6:
            case 29406:
            case 29408:
                if (PrivateObjectManager.isPlayerObject(player, object))
                    DwarfMultiCannon.fire(player, object);
                else
                    player.message("This is not your cannon!");
                break;
            case 2:
                player.moveTo(new Position(player.getX() > 2690 ? 2687 : 2694, 3714));
                player.message("You walk through the entrance..", true);
                break;
            case 12692:
            case 2783:
            case 4306:
                player.setInteractingObject(object);
                Smithing.handleAnvil(player);
                break;
            case 4859:
                player.animate(645);
                if (!player.getSkills().isFull(Skill.PRAYER)) {
                    player.getSkills().restore(Skill.PRAYER);
                    player.message("You recharge your Prayer points.");
                }
                break;
            case 47120:
                player.getPrayer().closeAllPrayers();
                player.animate(645);
                if (player.getPrayerbook() == Prayerbook.NORMAL) {
                    player.message("You sense a surge of power flow through your body!");
                    player.setPrayerbook(Prayerbook.CURSES);
                    Achievements.finishAchievement(player, Achievements.AchievementData.SWITCH_TO_CURSES);
                } else {
                    player.message("You sense a surge of purity flow through your body!");
                    player.setPrayerbook(Prayerbook.NORMAL);
                }

                if (!player.getSkills().isFull(Skill.PRAYER))
                    player.getSkills().restore(Skill.PRAYER);

                player.getPacketSender().sendTabInterface(GameSettings.PRAYER_TAB, player.getPrayerbook().getInterfaceId());
                break;
            case 37990://Chaos altar
                if (!player.getSkills().hasRequirement(Skill.DEFENCE, 30, "use this altar"))
                    return;
                player.getDialogueManager().startDialogue(new PrayerBookD(), true);
                break;
            case 6552://Ancient altar
                player.getDialogueManager().startDialogue(new SpellBookD(), true);
                break;
            case 28698:
                player.animate(645);
                player.setSpellBook(player.getSpellbook() == MagicSpellBook.LUNAR ? MagicSpellBook.NORMAL : MagicSpellBook.LUNAR, false);
                player.getPacketSender().sendTabInterface(GameSettings.MAGIC_TAB, player.getSpellbook().getInterfaceId());
                player.message("Your magic spellbook is changed..");
                Autocasting.resetAutoCast(player, true);
                break;
            case 172:
                CrystalChest.handleChest(player, object);
                break;
            case 2491:
            case 16684:
                player.getActionManager().setAction(new EssenceMining(object, player.getSkills().getLevel(Skill.MINING) < 30 ? Essence.RUNE_ESSENCE : Essence.PURE_ESSENCE));
                break;
            case 38660:
            case 38661:
            case 38662:
            case 38663:
            case 38664:
            case 38665:
            case 38666:
            case 38667:
            case 38668:
                ShootingStar.mine(player, object);
                break;
            case 9711:
            case 9712:
            case 9713:
            case 15503:
            case 15504:
            case 15505:
            case 5766:
            case 5767://CLAY
                if (object.getDefinition().containsOption(0, "Mine"))
                    player.getActionManager().setAction(new Mining(object, Ores.CLAY));
                break;
            case 18991:
            case 18992:
            case 18993:
            case 3042:
            case 9708:
            case 9709:
            case 9710:
            case 11936:
            case 11960:
            case 11961:
            case 11962:
            case 11189:
            case 11190:
            case 11191:
            case 29232:
            case 29231:
            case 29230:
            case 11937:
            case 11938:
            case 2090:
            case 5779:
            case 5780:
            case 5781://COPPER
                if (object.getDefinition().containsOption(0, "Mine"))
                    player.getActionManager().setAction(new Mining(object, Ores.COPPER));
                break;
            case 18994:
            case 18995:
            case 18996:
            case 3043:
            case 9714:
            case 9715:
            case 9716:
            case 11933:
            case 11934:
            case 11935:
            case 11957:
            case 11958:
            case 11959:
            case 11186:
            case 11187:
            case 11188:
            case 29227:
            case 29228:
            case 29229:
            case 2094:
            case 5776:
            case 5777:
            case 5778://TIN
                if (object.getDefinition().containsOption(0, "Mine"))
                    player.getActionManager().setAction(new Mining(object, Ores.TIN));
                break;
            case 9720:
            case 9721:
            case 9722:
            case 11951:
            case 11952:
            case 11953:
            case 11183:
            case 11184:
            case 11185:
            case 2099:
            case 5768:
            case 5769:
            case 37312:
            case 37310:
            case 45068:
            case 45067://GOLD
                if (object.getDefinition().containsOption(0, "Mine"))
                    player.getActionManager().setAction(new Mining(object, Ores.GOLD));
                break;
            case 19000:
            case 19001:
            case 19002:
            case 9717:
            case 9718:
            case 9719:
            case 2093:
            case 2092:
            case 11954:
            case 11955:
            case 11956:
            case 29221:
            case 29222:
            case 29223:
            case 14856:
            case 14857:
            case 14858:
            case 37309:
            case 37308:
            case 37307:
            case 5773:
            case 5774:
            case 5775://IRON
                if (object.getDefinition().containsOption(0, "Mine"))
                    player.getActionManager().setAction(new Mining(object, Ores.IRON));
                break;
            case 2100:
            case 2101:
            case 29226:
            case 29225:
            case 29224:
            case 37306:
            case 37305:
            case 37304:
            case 37670:
            case 11948:
            case 11949:
            case 11950://SILVER
                if (object.getDefinition().containsOption(0, "Mine"))
                    player.getActionManager().setAction(new Mining(object, Ores.SILVER));
                break;
            case 18997:
            case 18998:
            case 18999:
            case 29216:
            case 29215:
            case 29217:
            case 11965:
            case 11964:
            case 11963:
            case 11930:
            case 11931:
            case 11932:
            case 14850:
            case 14851:
            case 14852:
            case 21288:
            case 21287:
            case 21289:
            case 5770:
            case 5771:
            case 5772:
            case 2096:
            case 2097://COAL
                if (object.getDefinition().containsOption(0, "Mine"))
                    player.getActionManager().setAction(new Mining(object, Ores.COAL));
                break;
            case 33220:
            case 33221:
                if (object.getDefinition().containsOption(0, "Mine"))
                    player.getActionManager().setAction(new Mining(object, Ores.BLURITE));
                break;
            case 25370:
            case 25368:
            case 11942:
            case 11943:
            case 11944:
            case 11945:
            case 11946:
            case 11947:
            case 29236:
            case 29237:
            case 29238:
            case 14853:
            case 14854:
            case 14855:
            case 5784:
            case 5785:
            case 5786://Mithril
                if (object.getDefinition().containsOption(0, "Mine"))
                    player.getActionManager().setAction(new Mining(object, Ores.MITHRIL));
                break;
            case 11941:
            case 11939:
            case 29233:
            case 29234:
            case 29235:
            case 14862:
            case 14863:
            case 14864:
            case 5782:
            case 5783:
            case 2104://Adamant
                if (object.getDefinition().containsOption(0, "Mine"))
                    player.getActionManager().setAction(new Mining(object, Ores.ADAMANT));
                break;
            case 14859:
            case 14860:
            case 14861:
            case 2106:
            case 2107:
            case 33079:
            case 33078:
            case 45070:
            case 45069:
                if (object.getDefinition().containsOption(0, "Mine"))
                    player.getActionManager().setAction(new Mining(object, Ores.RUNITE));
                break;
            case 45076://Living rock caverns
                player.getActionManager().setAction(new Mining(object, Ores.LRC_GOLD));
                break;
            case 5999:
                player.getActionManager().setAction(new Mining(object, Ores.LRC_COAL));
                break;
            case 45078:
                ObjectHandler.useStairs(player,2413, new Position(3012, 9832), 2, 2);
                break;
            case 45077:
                player.getMovement().lock();
                if (player.getX() != object.getX() || player.getY() != object.getY())
                    player.getMovement().addWalkSteps(object.getX(), object.getY(), -1, false);
                GameWorld.schedule(new Task(false, 1) {
                    private int count;

                    @Override
                    public void run() {
                        if (count == 0) {
                            player.getDirection().face(new Position(object.getX() - 1, object.getY(), 0));
                            player.animate(12216);
                            player.getMovement().unlock();
                        } else if (count == 2) {
                            player.moveTo(3651, 5122);
                            player.getDirection().face(new Position(3651, 5121));
                            player.animate(12217);
                        } else if (count == 5) {
                            player.getMovement().unlock();
                            stop();
                        }
                        count++;
                    }
                });
                break;//End lrc
            case 17004:
            case 17005:
            case 17006:
            case 11194:
            case 11195:
            case 11364:
                if (object.getDefinition().containsOption(0, "Mine"))
                    player.getActionManager().setAction(new GemMining(object));
                break;
            case 10947:
                if (object.getDefinition().containsOption(0, "Mine"))
                    player.getActionManager().setAction(new Mining(object, Ores.GRANITE));
                break;
            case 10946:
                if (object.getDefinition().containsOption(0, "Mine"))
                    player.getActionManager().setAction(new Mining(object, Ores.SANDSTONE));
                break;
            default:
                if (object.getDefinition() != null)
                    switch (object.getDefinition().getName().toLowerCase()) {
                        case "altar":
                            player.animate(645);
                            if (!player.getSkills().isFull(Skill.PRAYER)) {
                                player.getSkills().restore(Skill.PRAYER);
                                player.message("You recharge your Prayer points.");
                                Achievements.finishAchievement(player, Achievements.AchievementData.PRAY_AT_AN_ALTAR);
                            }
                            break;
                        case "pulley lift":
                        case "bank deposit box":
                            if (object.getDefinition().containsOption(0, "Deposit"))
                                player.getBank().open();
                            break;
                        case "bank":
                        case "bank chest":
                        case "bank booth":
                        case "counter":
                        case "chest":
                            if (object.getDefinition().containsOption(0, "Bank") || object.getDefinition().containsOption(0, "Use"))
                                player.getBank().open();
                            break;
                        case "web":
                            if (object.getDefinition().containsOption(0, "Slash")) {
                                player.perform(new Animation(WeaponAnimations.getAttackAnimation(player.getEquipment().get(Slot.WEAPON), 0)));
                                ObjectHandler.slashWeb(player, object);
                            }
                            break;
                        case "gate":
                        case "large door":
                        case "metal door":
                            if (object.getDefinition().containsOption(0, "Open"))
                                //if (!ObjectHandler.handleGate(player, object))
                                ObjectHandler.handleDoor(object);
                            break;
                        case "mark 1":
                        case "mark 2":
                        case "practice chest":
                            if (object.getDefinition().containsOption(1, "Pickpocket") || object.getDefinition().containsOption(0, "Pickpocket") || object.getDefinition().containsOption(0, "Open"))
                                if (ThievingGuild.isInThievingGuild(object.copyPosition()))
                                    ThievingGuild.pickpocketObject(player, object);
                            break;
                        case "door":
                        case "colony gate":
                        case "guild door":
                        case "blacksmith's door":
                            if ((object.getDefinition().containsOption(0, "Open") || object.getDefinition().containsOption(0, "Unlock"))) {
                                if (ThievingGuild.isInThievingGuild(object.copyPosition())) {
                                    Thieving.pickDoor(player, object);
                                } else {
                                    ObjectHandler.handleDoor(object);
                                }
                                Achievements.finishAchievement(player, Achievements.AchievementData.OPEN_THE_DOOR);
                            }
                            break;
                        case "trapdoor":
                            ObjectHandler.useStairs(player, 828, new Position(4762, 5763, 0), 1, 2);
                            break;
                        case "ladder":
                            if (ThievingGuild.isInThievingGuild(object.copyPosition())) {
                                ObjectHandler.useStairs(player, 828, new Position(3223, 3269, 0), 1, 2);
                            } else {
                                ObjectHandler.handleLadder(player, object, 1);
                            }
                            break;
                        case "furnace":
                        case "lava furnace":
                        case "clay forge":
                            Smelting.openInterface(player);
                            break;
                        case "small obelisk":
                            Summoning.renewSummoningPoints(player);
                            break;
                        case "dramen tree":
                            if (object.getDefinition().containsOption(0, "Chop down"))
                                player.getActionManager().setAction(new Woodcutting(object, Trees.DRAMEN));
                            break;
                        case "tree":
                            if (object.getDefinition().containsOption(0, "Chop down"))
                                player.getActionManager().setAction(new Woodcutting(object, Trees.NORMAL));
                            break;
                        case "evergreen":
                            if (object.getDefinition().containsOption(0, "Chop down"))
                                player.getActionManager().setAction(new Woodcutting(object, Trees.EVERGREEN));
                            break;
                        case "dead tree":
                            if (object.getDefinition().containsOption(0, "Chop down"))
                                player.getActionManager().setAction(new Woodcutting(object, Trees.DEAD));
                            break;
                        case "oak":
                            if (object.getDefinition().containsOption(0, "Chop down"))
                                player.getActionManager().setAction(new Woodcutting(object, Trees.OAK));
                            break;
                        case "teak":
                            if (object.getDefinition().containsOption(0, "Chop down"))
                                player.getActionManager().setAction(new Woodcutting(object, Trees.TEAK));
                            break;
                        case "mahogany":
                            if (object.getDefinition().containsOption(0, "Chop down"))
                                player.getActionManager().setAction(new Woodcutting(object, Trees.MAHOGANY));
                            break;
                        case "willow":
                            if (object.getDefinition().containsOption(0, "Chop down"))
                                player.getActionManager().setAction(new Woodcutting(object, Trees.WILLOW));
                            break;
                        case "maple tree":
                            if (object.getDefinition().containsOption(0, "Chop down"))
                                player.getActionManager().setAction(new Woodcutting(object, Trees.MAPLE));
                            break;
                        case "ivy":
                            if (object.getDefinition().containsOption(0, "Chop"))
                                player.getActionManager().setAction(new Woodcutting(object, Trees.IVY));
                            break;
                        case "yew":
                            if (object.getDefinition().containsOption(0, "Chop down"))
                                player.getActionManager().setAction(new Woodcutting(object, Trees.YEW));
                            break;
                        case "magic tree":
                            if (object.getDefinition().containsOption(0, "Chop down"))
                                player.getActionManager().setAction(new Woodcutting(object, Trees.MAGIC));
                            break;
                        case "cursed magic tree":
                            if (object.getDefinition().containsOption(0, "Chop down"))
                                player.getActionManager().setAction(new Woodcutting(object, Trees.CURSED_MAGIC));
                            break;
                    }
                break;
        }
    }

    private static void secondClick(final Player player, GameObject object) {
        if (!player.getControllerManager().processObjectClick2(object))
            return;

        if (object != null && PlayerActionBus.publish(player, new ObjectOptionAction(object, 2)))
            return;

        if (PyramidPlunder.handleUrnCheck(player, object))
            return;

        if (Prospecting.forRock(object.getId()) != null) {
            Prospecting.prospectOre(player, object.getId());
            return;
        }

        if (player.getFarmingManager().isFarming(object.getId(), null, 2))
            return;

        if (Thieving.isStall(object)) {
            Thieving.handleStalls(player, object);
            return;
        }

        if (EvilTree.handleSecondObjectClick(player, object))
            return;
        //if ((gameObject.getId() >= 15477 && gameObject.getId() <= 15482) || gameObject.getId() == 93284)
        //EnterHouseD.enterHouse(player, false);

        switch (object.getId()) {
            case 48627:
                player.getStatue().check();
                break;
            case 10177://
                ObjectHandler.useStairs(player,828, new Position(2544, 3741), 1, 2);
                break;
            case 35390:
                GodWars.passGiantBoulder(player, object, false);
                break;
            case 70812:
                if (player.getSkills().getCombatLevel() < 120) {
                    player.message("You need a combat level of 120 to enter here.");
                    break;
                }
                if (!player.getSkills().hasMaxRequirement(Skill.SUMMONING, 60, "enter here"))
                    return;
                player.getControllerManager().startController(new QueenBlackDragonController());
                break;
            case 68444:
                FightKiln.enterFightKiln(player, false);
                break;
            case 25824:
            case 2644:
            case 36970:
                player.getDirection().face(object);
                Spinning.openInterface(player, false);
                break;
            case 2491:
                MiningBase.prospect(player, "This rock contains unbound Rune Stone essence.");
                break;
            case 2216:
                player.moveTo(2867, 2952);
                break;
            case 36769:
                if (object.sameAs(new Position(3229, 3213, 1))) {
                    player.animate(828);
                    player.moveTo(new Position(3229, 3214, 2), 2);
                } else if (object.sameAs(new Position(3229, 3224, 1))) {
                    player.animate(828);
                    player.moveTo(new Position(3229, 3223, 2), 2);
                }
                break;
            case 5005://Mort Myre Swamp Tree Bridge
                if (object.getX() == 3502 && object.getY() == 3426) {
                    ObjectHandler.useStairs(player,828, new Position(3503, 3431), 1, 2);
                } else if (object.getX() == 3502 && object.getY() == 3431) {
                    ObjectHandler.useStairs(player,828, new Position(3502, 3425), 1, 2);
                }
                break;
            case 12537://Wizards tower stairs
                player.moveTo(3104, 3161, 2);
                break;
            case 15507://Wheat - for some reason it's second click
                final String message = "You pick some grain.";
                final int itemId = 1947;
                if (player.getInventory().getSpaces() <= 0) {
                    player.message("You do not have enough space in your inventory.");
                } else {
                    GameWorld.schedule(new Task(true, 2) {
                        @Override
                        public void run() {
                            if (player.getInventory().getSpaces() <= 0) {
                                player.message("You do not have enough space in your inventory.");
                                stop();
                                return;
                            }

                            if (player.getInteractingObject() != null) {
                                player.getDirection().face(player.getInteractingObject().copyPosition());
                            }
                            player.getInventory().add(new Item(itemId));
                            player.animate(827);
                            player.message(message);
                        }
                    });
                }
                break;
            case 36796://Lumbridge windmill ladder
                player.animate(828);
                player.message("You climb up the ladder..", true);
                GameWorld.schedule(1, () -> {
                    if (object.getX() == 3164 && object.getY() == 3307 && object.getZ() == 1)
                        player.moveTo(3165, 3307, 2);
                });
                break;
            case 36776://Lumbridge Castle Stairs First-Right
                player.moveTo(3205, 3228, 2);
                break;
            case 36774://Lumbridge Castle Stairs First-Left
                player.moveTo(3205, 3209, 2);
                break;
            case 12309://Gypsy chest food shop
                ShopManager.getShops().get(80).open(player);
                break;
            case 1317://Spirit Tree
                player.getDialogueManager().startDialogue(new SpiritTreeTravelD());
                break;
            case 36777://Lumbridge stairs
                if (object.getX() == 3204 && object.getY() == 3229 && object.getZ() == 1) {
                    player.moveTo(new Position(player.getX(), player.getY(), 2));
                } else if (object.getX() == 3204 && object.getY() == 3207 && object.getZ() == 1) {
                    player.moveTo(new Position(player.getX(), player.getY(), 2));
                }
                break;
            case 27663:
                player.getBank().open();
                break;
            /*case 26945:
                player.setDialogueActionId(41);
                player.setInputHandling(new DonateToWell());
                player.getPacketSender().sendInterfaceRemoval().sendEnterAmountPrompt("How much money would you like to contribute with?");
                break;*/

            case 2646:
            case 312:
                if (!player.getTimers().getClickDelay().elapsed(1200))
                    return;
                if (player.getInventory().getSpaces() == 0) {
                    player.message("You don't have enough free inventory space.");
                    return;
                }
                String type = object.getId() == 312 ? "a Potato" : "some Flax";
                player.animate(827);
                player.getInventory().add(new Item(object.getId() == 312 ? 1942 : 1779));
                player.message("You pick " + type + "..", true);
                if (Misc.getRandom(5) == 1) {
                    player.getPacketSender().sendClientRightClickRemoval();
                    ObjectHandler.removeObjectTemporary(object, 10000);
                }
                if (object.getId() == 2646)
                    Achievements.finishAchievement(player, Achievements.AchievementData.PICK_FLAX);
                player.getTimers().getClickDelay().reset();
                break;
            case 6:
            case 29406:
            case 29408:
                DwarfMultiCannon.pickupCannon(player, 4, object);
                break;
            case 2152:
                player.animate(8502);
                player.perform(new Graphic(1308));
                player.getSkills().restore(Skill.SUMMONING);
                player.message("You renew your Summoning points.");
                break;
            default:
                if (object.getDefinition() != null) {
                    switch (object.getDefinition().getName().toLowerCase()) {
                        case "bank":
                        case "bank chest":
                        case "bank booth":
                        case "counter":
                            if (object.getDefinition().containsOption(1, "Exchange"))
                                GrandExchange.open(player);
                            else if (object.getDefinition().containsOption(1, "Bank"))
                                player.getBank().open();
                            break;
                        case "ladder":
                            ObjectHandler.handleLadder(player, object, 1);
                            break;
                        case "furnace":
                        case "lava furnace":
                        case "clay forge":
                            Smelting.openInterface(player);
                            break;
                        case "obelisk":
                            Summoning.renewSummoningPoints(player);
                            break;
                    }
                }
                break;
        }
    }

    private static void thirdClick(Player player, GameObject object) {

        if (!player.getControllerManager().processObjectClick3(object))
            return;

        if (object != null && PlayerActionBus.publish(player, new ObjectOptionAction(object, 3)))
            return;

        if (PyramidPlunder.handleSnakes(player, object))
            return;


        if (EvilTree.handleThirdObjectClick(player, object))
            return;
        //if ((gameObject.getId() >= 15477 && gameObject.getId() <= 15482) || gameObject.getId() == 93284)
        //EnterHouseD.enterHouse(player, true);

        switch (object.getId()) {
            case 48627:
                ShopManager.getShops().get(127).open(player);
                break;
            case 36769:
                if (object.sameAs(new Position(3229, 3213, 1))) {
                    player.animate(828);
                    player.moveTo(new Position(3229, 3214), 2);
                } else if (object.sameAs(new Position(3229, 3224, 1))) {
                    player.animate(828);
                    player.moveTo(new Position(3229, 3223), 2);
                }
                break;
            case 12537://Wizards tower stairs
                player.moveTo(3104, 3161);
                break;
            case 36796://Lumbridge windmill ladder
                player.animate(828);
                player.message("You climb down the ladder..", true);
                GameWorld.schedule(1, () -> {
                    if (object.getX() == 3164 && object.getY() == 3307 && object.getZ() == 1) {
                        player.moveTo(3165, 3307);
                        if (player.getMillFilled() > 0)
                            GameWorld.schedule(1, () -> ObjectManager.spawnObject(new GameObject(36878, new Position(3166, 3306))));
                    }
                });
                break;
            case 36777://Lumbridge Castle Stairs First-Right
                player.moveTo(3205, 3228);
                break;
            case 36774://Lumbridge Castle Stairs First-Left
                player.moveTo(3205, 3209);
                break;
            case 12309://Gypsy Chest
                if (!player.getMinigameAttributes().getRecipeForDisasterAttributes().hasFinishedPart(0)) {
                    player.message("You have no business with this chest. Talk to the Gypsy in varrock first!");
                    return;
                }
                RecipeForDisaster.openRFDShop(player);
                break;
            default:
                if (object.getDefinition() != null) {
                    switch (object.getDefinition().getName().toLowerCase()) {
                        case "ladder":
                            ObjectHandler.handleLadder(player, object, 3);
                            break;
                        case "bank":
                        case "bank chest":
                        case "bank booth":
                        case "counter":
                            if (object.getDefinition().containsOption(2, "Exchange"))
                                GrandExchange.open(player);
                            break;
                    }
                }
                break;
        }
    }

    private static void fourthClick(Player player, GameObject object) {
        int id = object.getId();
        int x = object.getX();
        int y = object.getY();

        if (object == null || player == null)
            return;

        if (!player.getControllerManager().processObjectClick4(object))
            return;

        if (PlayerActionBus.publish(player, new ObjectOptionAction(object, 4)))
            return;

        if (player.getFarmingManager().isFarming(object.getId(), null, 3))
            return;

        //if (((id >= 15477 && id <= 15482) || id == 93284))
        //EnterHouseD.enterFriendsHouse(player);

        switch (id) {
            case 45076:
                MiningBase.prospect(player, "This rock contains a large concentration of gold.");
                break;
            case 5999:
                MiningBase.prospect(player, "This rock contains a large concentration of coal.");
                break;
        }
    }

    private static void fifthClick(final Player player, GameObject object) {
        if (!player.getControllerManager().processObjectClick5(object))
            return;

        if (object != null && PlayerActionBus.publish(player, new ObjectOptionAction(object, 5)))
            return;

        if (object.getDefinition() != null) {
            switch (object.getDefinition().getName().toLowerCase()) {
                case "fire":
                    if (object.getDefinition().options[4].equals("Add-logs"))
                        Bonfire.addLogs(player, object);
                    break;
            }
        }
    }

    private static boolean untouchables(final Player player, GameObject object) {
        if (object == null)
            return false;

        if (!player.getControllerManager().processUntouchableObjectClick1(object))
            return true;

        if (GnomeCourse.handleObstacles(player, object, false))
            return true;

        if (WildernessCourse.handleObstacles(player, object, false))
            return true;

        return false;
    }

    @Override
    public void handleMessage(Player player, Packet packet) {
        if (player.getMovement().getTeleporting() || player.getMovement().isLocked() || player.getEmotesManager().isDoingEmote())
            return;

        int opcode = packet.getOpcode();
        final int x = packet.readShort();
        final int id = packet.readInt();
        final int y = packet.readShort();

        final Position position = new Position(x, y, player.getZ() % 4);

        final GameObject object = PrivateObjectManager.isPrivateObject(player, id) ? PrivateObjectManager.getObject(player, id) : ObjectManager.getObjectWithId(id, position);

        if (object == null || !object.validate(id)) {
            if (player.getRights() == PlayerRights.DEVELOPER)
                player.getPacketSender().sendConsoleMessage("Unregistered Object[" + opcode + ", " + id + ", " + position.toString() + "]");

            if (player.getControllerManager().getController() instanceof DungeonController)
                player.message("Error verifying object, please relog to fix.");
            return;
        }
        if (object.getId() != id)
            object.setId(id);

        if (player.getRights() == PlayerRights.DEVELOPER)
            player.getPacketSender().sendConsoleMessage("Object[" + opcode + ", " + id + ", " + position.toString() + "]");


        player.stopAll(false);
        if (opcode == PacketConstants.FIRST_CLICK && untouchables(player, object))
            return;

        if (!player.getControllerManager().processObjectDistanceClick(object, opcode))
            return;

        player.setRouteEvent(new RouteEvent(object, () -> {
            // unreachable objects exception
            player.getDirection().faceObject(object);

            switch (opcode) {
                case PacketConstants.FIRST_CLICK:
                    firstClick(player, object);
                    break;
                case PacketConstants.SECOND_CLICK:
                    secondClick(player, object);
                    break;
                case PacketConstants.THIRD_CLICK:
                    thirdClick(player, object);
                    break;
                case PacketConstants.FOURTH_CLICK:
                    fourthClick(player, object);
                    break;
                case PacketConstants.FIFTH_CLICK:
                    fifthClick(player, object);
                    break;
            }
        }));
    }
}
