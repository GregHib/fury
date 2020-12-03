package com.fury.network.packet.impl;

import com.fury.cache.Revision;
import com.fury.core.model.item.Item;
import com.fury.core.model.node.entity.actor.figure.combat.magic.CombatSpell;
import com.fury.core.model.node.entity.actor.figure.combat.magic.context.SpellOnFigureContext;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.container.impl.equip.Slot;
import com.fury.game.container.impl.shop.Shop;
import com.fury.game.container.impl.shop.ShopManager;
import com.fury.game.content.controller.Abyss;
import com.fury.game.content.controller.impl.PuroPuro;
import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.impl.minigames.pest.LanderSquireD;
import com.fury.game.content.dialogue.impl.minigames.pyramidplunder.SimonTempletonD;
import com.fury.game.content.dialogue.impl.minigames.warriorsguild.WarriorsGuildD;
import com.fury.game.content.dialogue.impl.misc.DragonFireShieldD;
import com.fury.game.content.dialogue.impl.misc.ElfTrackerD;
import com.fury.game.content.dialogue.impl.misc.ShardExchange;
import com.fury.game.content.dialogue.impl.misc.VeteranCapeD;
import com.fury.game.content.dialogue.impl.npcs.*;
import com.fury.game.content.dialogue.impl.quests.tutorial.SkipTutorialD;
import com.fury.game.content.dialogue.impl.skills.agility.AgilityTicketExchangeD;
import com.fury.game.content.dialogue.impl.skills.construction.EstateAgentD;
import com.fury.game.content.dialogue.impl.skills.dungeoneering.DungeoneeringTutorD;
import com.fury.game.content.dialogue.impl.skills.dungeoneering.RewardsTraderD;
import com.fury.game.content.dialogue.impl.skills.summoning.FamiliarInspectionD;
import com.fury.game.content.dialogue.input.impl.EnterPotionSizeToDecant;
import com.fury.game.content.eco.ge.GrandExchange;
import com.fury.game.content.global.events.christmas.ChristmasPartyCharacters;
import com.fury.game.content.global.minigames.impl.PestControlShop;
import com.fury.game.content.global.quests.Quests;
import com.fury.game.content.global.thievingguild.ThievingGuild;
import com.fury.game.content.global.treasuretrails.ClueConstants;
import com.fury.game.content.global.treasuretrails.TreasureTrailHandlers;
import com.fury.game.content.misc.objects.GravestoneSelection;
import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.free.crafting.leather.tanning.Tanning;
import com.fury.game.content.skill.free.dungeoneering.DungeoneeringRewards;
import com.fury.game.content.skill.free.fishing.Fishing;
import com.fury.game.content.skill.free.fishing.FishingSpots;
import com.fury.game.content.skill.free.mining.MiningBase;
import com.fury.game.content.skill.free.mining.impl.LivingMineralMining;
import com.fury.game.content.skill.member.herblore.Drinkables;
import com.fury.game.content.skill.member.slayer.SlayerMaster;
import com.fury.game.content.skill.member.summoning.impl.Pet;
import com.fury.game.content.skill.member.summoning.impl.Summoning;
import com.fury.game.content.skill.member.thieving.PickPocketAction;
import com.fury.game.content.skill.member.thieving.PickPocketableNpc;
import com.fury.game.entity.character.combat.effects.Effects;
import com.fury.game.entity.character.combat.equipment.weapon.CombatSpecial;
import com.fury.game.entity.character.combat.magic.CombatSpells;
import com.fury.game.entity.character.player.actions.PlayerCombatAction;
import com.fury.game.entity.character.player.actions.Snowball;
import com.fury.game.entity.character.player.content.EnergyHandler;
import com.fury.game.entity.character.player.content.LoyaltyProgramme;
import com.fury.game.entity.character.player.info.DonorStatus;
import com.fury.game.entity.character.player.info.GameMode;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.entity.character.player.link.transportation.TeleportHandler;
import com.fury.game.entity.character.player.link.transportation.TeleportType;
import com.fury.game.entity.item.content.ItemRepair;
import com.fury.game.node.entity.actor.figure.mob.extension.misc.Gravestone;
import com.fury.game.node.entity.actor.figure.player.PlayerCombat;
import com.fury.game.node.entity.actor.figure.player.Points;
import com.fury.game.node.entity.actor.figure.player.handles.Settings;
import com.fury.game.npc.familiar.Familiar;
import com.fury.game.npc.minigames.pest.PestPortal;
import com.fury.game.npc.misc.FireSpirit;
import com.fury.game.npc.slayer.ConditionalDeath;
import com.fury.game.npc.slayer.LivingRock;
import com.fury.game.npc.slayer.Strykewyrm;
import com.fury.game.system.mysql.impl.DonationStore;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;
import com.fury.game.world.map.path.RouteEvent;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.network.packet.Packet;
import com.fury.network.packet.PacketConstants;
import com.fury.network.packet.PacketListener;
import com.fury.util.Misc;

public class NpcOptionPacketListener implements PacketListener {

    private static void firstClick(Player player, Mob mob) {
        if (player.getRights() == PlayerRights.DEVELOPER)
            player.message("First click npc id: " + mob.getId());

        if (!player.getControllerManager().processNPCDistanceClick1(mob))
            return;

        player.getDirection().setInteracting(mob);
        player.stopAll(false);
        player.setRouteEvent(new RouteEvent(mob, () -> {
            player.getDirection().face(mob);

            if (!player.getControllerManager().processNPCClick1(mob))
                return;

            if (player.getQuestManager().isNpcOption1(mob))
                return;

            ChristmasPartyCharacters character = ChristmasPartyCharacters.getCharacter(mob.getId());
            if (character != null) {
                player.getDialogueManager().sendNPCDialogue(mob.getId(), Expressions.PLAIN_TALKING, character.getTalk());
                return;
            }

            if (TreasureTrailHandlers.handleNpcOption(player, mob))
                return;

            FishingSpots spot = FishingSpots.forId(mob.getId() | 1 << 24);
            if (spot != null) {
                player.getActionManager().setAction(new Fishing(spot, mob));
                return; // its a spot, they wont face us
            }

            PickPocketableNpc pocket = PickPocketableNpc.get(mob.getId());
            if (pocket != null && mob.getId() == 11275) {
                if (!player.getTimers().getClickDelay().elapsed(3000))
                    return;
                player.getTimers().getClickDelay().reset();
                player.getActionManager().setAction(new PickPocketAction(mob, pocket));
                return;
            }

            if (mob instanceof Pet) {
                Pet pet = (Pet) mob;
                if (pet != player.getPet()) {
                    player.message("This isn't your pet.");
                    return;
                }
                player.animate(827);
                pet.pickup();
            }
            if (mob.isFamiliar()) {
                Familiar familiar = (Familiar) mob;
                if (player.getFamiliar() != familiar) {
                    player.message("That isn't your familiar.");
                    return;
                } else if (familiar.getDefinition().hasOption("interact")) {
                    Object[] params = new Object[2];
                    Summoning.Pouches pouch = player.getFamiliar().getPouch();
                    if (pouch == Summoning.Pouches.SPIRIT_GRAAHK) {
                        params[0] = "Karamja's Hunter Area";
                        params[1] = new Position(2787, 3000);
                    } else if (pouch == Summoning.Pouches.SPIRIT_KYATT) {
                        params[0] = "Piscatorius Hunter Area";
                        params[1] = new Position(2339, 3636);
                    } else if (pouch == Summoning.Pouches.SPIRIT_LARUPIA) {
                        params[0] = "Feldip Hills Hunter Area";
                        params[1] = new Position(2557, 2913);
                    } else if (pouch == Summoning.Pouches.ARCTIC_BEAR) {
                        params[0] = "Rellekka Hills Hunter Area";
                        params[1] = new Position(2721, 3779);
                    } else if (pouch == Summoning.Pouches.LAVA_TITAN) {
                        params[0] = "Lava Maze - *Deep Wilderness*";
                        params[1] = new Position(3028, 3840);
                    } else
                        return;
                    player.getDialogueManager().startDialogue(new FamiliarInspectionD(), params[0], params[1]);
                }
                return;
            } else if (SlayerMaster.startInteractionForId(player, mob.getId(), 1)) {
                return;
            } else if (mob instanceof LivingRock) {
                player.getActionManager().setAction(new LivingMineralMining((LivingRock) mob));
                return;
            } else if (mob.getId() == 15451 && mob instanceof FireSpirit) {
                FireSpirit spirit = (FireSpirit) mob;
                spirit.giveReward(player);
            } else if (mob instanceof Gravestone) {
                Gravestone grave = (Gravestone) mob;
                grave.sendGraveInscription(player);
                return;
            }

            //Musicians
            boolean musician = (mob.getId() >= 8698 && mob.getId() <= 8723) || (mob.getRevision() == Revision.PRE_RS3 && (mob.getId() == 3463 || mob.getId() == 14628 || mob.getId() == 14629 || mob.getId() == 15217 || mob.getId() == 15218));

            if (musician)
                EnergyHandler.rest(player, true);

            switch (mob.getId()) {
                case 1918:
                    if (!player.getGameMode().equals(GameMode.LEGEND)) {
                        player.getPacketSender().sendMessage("You need to be a legend to access this shop.", 255);
                        return;
                    }
                    ShopManager.getShops().get(129).open(player);
                    break;
                case 1152:
                    player.getDialogueManager().startDialogue(new WeirdOldManD());
                    break;
                case 5510://Yak hair guy
                    player.getDialogueManager().startDialogue(new MortenHoldstromD());
                    break;
                case 2253:
                    ShopManager.getShops().get(9).open(player);
                    break;
                case 418:
                    player.getDialogueManager().startDialogue(new LeprechaunD());
                    break;
                case 198:
                    player.getDialogueManager().startDialogue(new GuildMasterD());
                    break;
                case 1199:
                    player.getDialogueManager().startDialogue(new ElfTrackerD());
                    break;
                case 456://Father aereck
                    GravestoneSelection.open(player);
                    break;
                case 949://Fury Sage
                    player.getDialogueManager().startDialogue(new SkipTutorialD(), false);
                    break;
                case 36://Falador gardener
                    player.getDialogueManager().startDialogue(new WysonD());
                    break;
                case 13824:
                    player.getDialogueManager().startDialogue(new MaverickD());
                    break;
                case 3802:
                case 6140:
                case 6141://Pest control lander squire
                    player.getDialogueManager().startDialogue(new LanderSquireD(), mob.getId());
                    break;
                case 9400://2nd santa
                    player.getDialogueManager().startDialogue(new Santa2D());
                    break;
                case 4247:
                case 6715://Estate agent
                    player.getDialogueManager().startDialogue(new EstateAgentD(), mob.getId());
                    break;
                case 4250://Sawmill operator
                    ShopManager.getShops().get(6).open(player);
                    break;
                case 8540://Santa
                    player.getDialogueManager().startDialogue(new SantaD());
                    break;
                case 6524://Bob barter (herbs)
                    player.getDialogueManager().startDialogue(new BobBarterD());
                    break;
                case 792://Stark ironman guy
                    if (mob.getRegionId() == 12115)
                        player.getDialogueManager().startDialogue(new IronManD());
                    else
                        player.getDialogueManager().startDialogue(new IronManZoneD());
                    break;
                case 564://Jukat ironman shop
                    ShopManager.getShops().get(121).open(player);
                    break;
                case 7781://Jesmona
                    player.getDialogueManager().startDialogue(new JesmonaD(), mob);
                    break;
                case 657://Entrana monk
                    player.moveTo(2834, 3335);
                    break;
                case 4439://Female Centaur
                case 4438://Male Centaur
                    player.getDialogueManager().startDialogue(new CentaurD(), mob.getId());
                    break;
                case 4441://Wood dryad
                    player.getDialogueManager().startDialogue(new WoodDryadD());
                    break;
                case 6996://Enchanted Valley River Troll
                    if (!player.getInventory().contains(new Item(307))) {
                        player.message("You need a " + new Item(307).getName().toLowerCase() + " to fish here.");
                        return;
                    }

                    if (player.getInventory().getAmount(new Item(313)) < 1) {
                        player.message("You don't have " + new Item(313).getName().toLowerCase() + " to fish here.");
                        return;
                    }

                    player.animate(622);
                    Mob riverTroll = GameWorld.getMobs().spawn(8646, player.copyPosition(), true);
                    riverTroll.animate(10819);
                    riverTroll.setTarget(player);
                    player.getMovement().stepAway();
                    break;
                case 14620://Polypore dungeon merchant
                    ShopManager.getShops().get(119).open(player);
                    break;
                case 6201:
                    player.getDialogueManager().startDialogue(new SirGerryD());
                    break;
                case 5442:
                case 5439:
                case 29:
                case 30://More musicians
                    EnergyHandler.rest(player, true);
                    break;
                case 15460:
                    if (!player.hasItem(24303)) {
                        player.getDialogueManager().sendNPCDialogue(15460, Revision.PRE_RS3, Expressions.NORMAL, "Here take this, it might be off more use", "to you than it is for me...");
                        player.getInventory().add(new Item(24303));
                    } else {
                        player.getDialogueManager().sendNPCDialogue(15460, Revision.PRE_RS3, Expressions.NORMAL, "Sorry there's not a lot else I can do to help.");
                    }
                    break;
                case 8239:
                    player.getDialogueManager().startDialogue(new BoltEnchanterD());
                    break;
                case 961:
                    if (!player.getTimers().getClickDelay().elapsed(3000))
                        return;
                    player.getTimers().getClickDelay().reset();
                    if (DonorStatus.isDonor(player, DonorStatus.SAPPHIRE_DONOR))
                        for (Skill skill : Skill.values())
                            if(player.getSkills().getLevel(skill) < (skill == Skill.CONSTITUTION ? player.getMaxConstitution() :  player.getSkills().getMaxLevel(skill)))
                                player.getSkills().restore(skill);
                    player.perform(new Graphic(436));
                    player.getSkills().restore(Skill.CONSTITUTION);
                    if (player.getEffects().hasActiveEffect(Effects.POISON)) {
                        player.message("Your poison has been cured.");
                        player.getEffects().removeEffect(Effects.POISON);
                    }
                    player.message("Your hitpoints" + (player.isDonor() ? " and stats" : "") + " have been restored.");
                    break;
                case 2328:
                    player.getDialogueManager().startDialogue(new WizardCrompertyD());
                    break;
                case 7969:
                    ShopManager.getShops().get(117).open(player);
                    break;
                case 9711:
                    player.getDialogueManager().startDialogue(new RewardsTraderD());
                    break;
                case 9712:
                    player.getDialogueManager().startDialogue(new DungeoneeringTutorD());
                    break;
                case 3123:
                    player.getDialogueManager().startDialogue(new SimonTempletonD());
                    break;
                case 586:
                    ShopManager.getShops().get(116).open(player);
                    break;
                case 584:
                    ShopManager.getShops().get(114).open(player);
                    break;
                case 540:
                    if (mob.getX() == 2670)
                        ShopManager.getShops().get(113).open(player);
                    else
                        ShopManager.getShops().get(115).open(player);
                    break;
                case 682:
                    ShopManager.getShops().get(112).open(player);
                    break;
                case 209:
                    player.getDialogueManager().startDialogue(new NulodionD());
                    break;
                case 7143://Gambler
                    ShopManager.getShops().get(41).open(player);
                    break;
                case 3797:
                    ItemRepair.handleDialogue(player, mob.getId());
                    break;
                case 455:
                    if (!player.getSkills().hasRequirement(Skill.HERBLORE, 99, "access this shop"))
                        return;
                    ShopManager.getShops().get(110).open(player);
                    break;
                case 563://Arhein
                    ShopManager.getShops().get(109).open(player);
                    break;
                case 575://Hickton (catherby archery)
                    ShopManager.getShops().get(108).open(player);
                    break;
                case 1281:
                    ShopManager.getShops().get(79).open(player);
                    break;
                case 5113://Hunting expert
                    ShopManager.getShops().get(23).open(player);
                    break;
                case 6070://Elnock Inquisitor
                    ShopManager.getShops().get(18).open(player);
                    break;
                case 6970://Pikkupstix
                    ShopManager.getShops().get(22).open(player);
                    break;
                case 587://Jatix (Herblore shop)
                    ShopManager.getShops().get(107).open(player);
                    break;
                case 0://Hans
                    if (!player.getUnlockedLoyaltyTitles()[LoyaltyProgramme.LoyaltyTitles.VETERAN.getId()])
                        return;
                    player.getDialogueManager().startDialogue(new VeteranCapeD());
                    break;
                case 3705://Max
                    player.getDialogueManager().startDialogue(new MaxD());
                    break;
                case 592://Roachey fishing guild shop
                    ShopManager.getShops().get(106).open(player);
                    break;
                case 2290://Sir Tiffy
                    ShopManager.getShops().get(105).open(player);
                    break;
                case 5503:
                case 5504://Mawnis Burowgar (Neitiznot Helm)
                    ShopManager.getShops().get(104).open(player);
                    break;
                case 5508://Maria Gunnars
                    player.moveTo(2311, 3781);
                    break;
                case 5507://Maria Gunnars
                    player.moveTo(2645, 3709);
                    break;
                case 1282://Sigmund the Mechant
                    ShopManager.getShops().get(103).open(player);
                    break;
                case 1315://Fishmonger
                    ShopManager.getShops().get(102).open(player);
                    break;
                case 1303://Skullgrimen's Battle Gear
                    ShopManager.getShops().get(101).open(player);
                    break;
                case 576://Harry's fishing shop
                    ShopManager.getShops().get(100).open(player);
                    break;
                case 557://Wydin food shop
                    ShopManager.getShops().get(99).open(player);
                    break;
                case 4293://Lidio Warriors' Guild Food Shop
                    ShopManager.getShops().get(98).open(player);
                    break;
                case 4294://Lilly Warriors Guild Potion Shop
                    ShopManager.getShops().get(97).open(player);
                    break;
                case 638://Apothecary
                    ShopManager.getShops().get(19).open(player);
                    break;
                case 3798://Squire Mage
                    ShopManager.getShops().get(96).open(player);
                    break;
                case 3796://Squire Range
                    ShopManager.getShops().get(95).open(player);
                    break;
                case 903://Lundail
                    ShopManager.getShops().get(94).open(player);
                    break;
                case 550://Lowe
                    ShopManager.getShops().get(92).open(player);
                    break;
                case 5913://Aubury
                    ShopManager.getShops().get(91).open(player);
                    break;
                case 577://Cassie (Shield Shop)
                    ShopManager.getShops().get(90).open(player);
                    break;
                case 549://Horvik (Armour shop)
                    ShopManager.getShops().get(89).open(player);
                    break;
                case 537://Scavvo (Champ Guild)
                    ShopManager.getShops().get(88).open(player);
                    break;
                case 551://Varrock sword shop
                    ShopManager.getShops().get(87).open(player);
                    break;
                case 541://Zeke (Scimitars)
                    ShopManager.getShops().get(86).open(player);
                    break;
                case 8032://Elriss (Runecrafting)
                    if (!player.getSkills().hasRequirement(Skill.RUNECRAFTING, 65, "access this shop"))
                        return;
                    ShopManager.getShops().get(85).open(player);
                    break;
                case 13632://Larriar (Runecrafting)
                    ShopManager.getShops().get(84).open(player);
                    break;
                case 249://Merlin
                    ShopManager.getShops().get(83).open(player);
                    break;
                case 359://Naff
                    ShopManager.getShops().get(82).open(player);
                    break;
                case 546://Zaff
                    ShopManager.getShops().get(81).open(player);
                    break;
                case 2259://Zamorak Mage
                    mob.animate(811);
                    mob.forceChat("Veniens! Sallakar! Rinnesset!");
                    player.getControllerManager().startController(new Abyss());
                    break;
                case 572://Spice seller
                    ShopManager.getShops().get(48).open(player);
                    break;
                case 573://Fur Trader
                    ShopManager.getShops().get(29).open(player);
                    break;
                case 574://Silk merchant
                    ShopManager.getShops().get(28).open(player);
                    break;
                case 569://Silver merchant
                    ShopManager.getShops().get(16).open(player);
                    break;
                case 571://Baker
                    ShopManager.getShops().get(7).open(player);
                    break;
                case 6362://Eniola - ZMI Banker
                    player.getDialogueManager().startDialogue(new EniolaD());
                    break;
                case 8091:
                    player.getDialogueManager().startDialogue(new StarSpriteD());
                    break;
                case 9466:
                case 9464:
                case 9462:
                    Strykewyrm.handleStomping(player, mob);
                    break;
                case 13727:
                    player.getDialogueManager().startDialogue(new XuanD());
                    break;
                case 3321://Gatekeeper
                    player.getDialogueManager().startDialogue(new GatekeeperD());
                    break;
                case 7959://Master Smith
                    player.getDialogueManager().startDialogue(new DragonFireShieldD());
                    break;
                case 12179://Mage
                    ShopManager.getShops().get(0).open(player);
                    break;
                case 1694://Archer
                    ShopManager.getShops().get(1).open(player);
                    break;
                case 400://Warrior
                    if (player.getGameMode().isIronMan())
                        ShopManager.getShops().get(126).open(player);
                    else
                        ShopManager.getShops().get(2).open(player);
                    break;
                case 942://Cook
                    ShopManager.getShops().get(3).open(player);
                    break;
                case 519://Tools Shop
                    if (player.getGameMode().isIronMan())
                        ShopManager.getShops().get(118).open(player);
                    else
                        ShopManager.getShops().get(4).open(player);
                    break;
                case 3641://Donator Shop
                    ShopManager.getShops().get(5).open(player);
                    break;
                case 251:
                    player.getDialogueManager().startDialogue(new KingArthurD());
                    break;
                case 534:
                    ShopManager.getShops().get(78).open(player);
                    break;
                case 947:
                    if (player.getX() >= 3092) {
                        player.getMovement().reset();
                        GrandExchange.open(player);
                    }
                    break;
                case 2622:
                    ShopManager.getShops().get(43).open(player);
                    break;
                case 437:
                    player.getDialogueManager().startDialogue(new AgilityTicketExchangeD());
                    break;
                case 5109:
                    ShopManager.getShops().get(38).open(player);
                    break;
                case 3385:
                    if (player.getMinigameAttributes().getRecipeForDisasterAttributes().hasFinishedPart(0) && player.getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted() < 6) {
                        player.getDialogueManager().startDialogue(new RFDD());
                        return;
                    }
                    if (player.getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted() == 6) {
                        player.getDialogueManager().startDialogue(new RFDCompletion());
                        return;
                    }
                    player.getDialogueManager().startDialogue(new RFDReturnD());
                    break;
                case 6782:
                    player.getDialogueManager().startDialogue(new GuideD());
                    break;
                case 3789:
                case 3788:
                    player.getPacketSender().sendInterface(18730);
                    PestControlShop.updateCommendations(player);
                    break;
                case 2948:
                    player.getDialogueManager().startDialogue(new WarriorsGuildD());
                    break;
                case 650:
                    ShopManager.getShops().get(35).open(player);
                    break;
                case 6055:
                case 6056:
                case 6057:
                case 6058:
                case 6059:
                case 6060:
                case 6061:
                case 6062:
                case 6063:
                case 6064:
                case 7903:
                    PuroPuro.catchImpling(player, mob);
                    break;
                case 5082:
                case 5083:
                case 5084:
                case 5085:
                    PuroPuro.catchButterfly(player, mob);
                    break;
                case 6537:
                    player.getDialogueManager().startDialogue(new MandrithD());
                    break;
                case 4646:
                    player.getDialogueManager().startDialogue(new KingHealthorgD());
                    break;
                case 805:
                    ShopManager.getShops().get(player.getGameMode().isIronMan() ? 122 : 34).open(player);
                    break;
//                case 462:
//                    ShopManager.getShops().get(33).open(player);
//                    break;
                case 8459:
                    ShopManager.getShops().get(30).open(player);
                    break;
                case 3299:
                    ShopManager.getShops().get(21).open(player);
                    break;
                case 548:
                    ShopManager.getShops().get(20).open(player);
                    break;
                case 1685:
                    ShopManager.getShops().get(19).open(player);
                    break;
            /*case 308:
                ShopManager.getShops().forId(18).open(player);
                break;*/
                case 802:
                    ShopManager.getShops().get(17).open(player);
                    break;
                case 4946:
                    ShopManager.getShops().get(15).open(player);
                    break;
                case 948:
                    ShopManager.getShops().get(13).open(player);
                    break;
                case 4906:
                    ShopManager.getShops().get(14).open(player);
                    break;
                case 520:
                case 521:
                    ShopManager.getShops().get(12).open(player);
                    break;
                case 2292:
                    ShopManager.getShops().get(11).open(player);
                    break;
                case 2676:
                    player.getAppearance().openInterface();
                    break;
                case 494:
                case 7605:
                case 9710://Dungeoneering banker
                case 13455://nex banker
                    player.getBank().open();
                    break;
            }
            if (!musician && !(mob instanceof PestPortal)) {
                mob.getDirection().face(player);
            }
        }));
    }

    private static void attackNpc(Player player, Mob mob) {
        if (mob.getId() == ClueConstants.DOUBLE_AGENT_HIGH || mob.getId() == ClueConstants.DOUBLE_AGENT_LOW) {
            boolean allowedToAttack = DoubleAgentD.handleDoubleAgentAttack(player, mob);
            if (!allowedToAttack)
                return;
        }

        CombatSpell spell = player.getCastSpell();

        if(spell != null && !spell.canCast(player, new SpellOnFigureContext(mob)))
            return;
        else if (!mob.getDefinition().hasAttackOption())
            return;

        if (!player.getControllerManager().canAttack(mob))
            return;

        if (mob.isCantInteract())
            return;

        if (mob.getHealth().getHitpoints() <= 0) {
            player.getMovement().reset();
            return;
        }

        player.stopAll(false);

        int rangeType = PlayerCombatAction.isRanging(player);
        CombatSpell autoSpell = player.getAutoCastSpell();
        boolean ranged = rangeType != 0 || spell != null || autoSpell != null || player.getEquipment().get(Slot.WEAPON).getId()== 22494;//Ranged as in from a distance not Ranged combat skill
        int maxDistance = ranged ? 7 : 0;
        boolean lineOfSight = player.getCombat().clippedProjectile(mob, maxDistance == 0);

        player.getDirection().setInteracting(mob);
        if (ranged && lineOfSight) {
            if(player.getCombat() instanceof PlayerCombat)
                ((PlayerCombat) player.getCombat()).target(mob);
        } else {
            player.setRouteEvent(new RouteEvent(mob, () -> {
                if(player.getCombat() instanceof PlayerCombat)
                    ((PlayerCombat) player.getCombat()).target(mob);
            }, ranged && !lineOfSight));
        }
    }

    public void secondClick(Player player, Mob mob) {
        final int npcId = mob.getId();

        if (player.getRights() == PlayerRights.DEVELOPER)
            player.message("Second click npc id: " + npcId);

        if (!player.getControllerManager().processNPCDistanceClick2(mob))
            return;

        player.getDirection().setInteracting(mob);
        player.stopAll(false);
        player.setRouteEvent(new RouteEvent(mob, () -> {
            player.getDirection().face(mob);

            if (!player.getControllerManager().processNPCClick2(mob))
                return;

            if (mob.isFamiliar()) {
                Familiar familiar = (Familiar) mob;
                if (player.getFamiliar() != familiar) {
                    player.message("This isn't your familiar.");
                    return;
                }

                if (familiar.getDefinition().hasOption("store") || mob.getDefinition().hasOption("withdraw")) {
                    player.getFamiliar().store();
                } else if (familiar.getDefinition().hasOption("cure")) {
                    if (!player.getEffects().hasActiveEffect(Effects.POISON)) {
                        player.message("Your aren't poisoned or diseased.");
                        return;
                    } else {
                        player.getFamiliar().drainSpecial(2);
                        player.setAntipoisonDelay(200);
                    }
                } else if (familiar.getDefinition().hasOption("interact")) {
                    Object[] params = new Object[2];
                    Summoning.Pouches pouch = player.getFamiliar().getPouch();
                    if (pouch == Summoning.Pouches.SPIRIT_GRAAHK) {
                        params[0] = "Karamja's Hunter Area";
                        params[1] = new Position(2787, 3000);
                    } else if (pouch == Summoning.Pouches.SPIRIT_KYATT) {
                        params[0] = "Piscatorius Hunter Area";
                        params[1] = new Position(2339, 3636);
                    } else if (pouch == Summoning.Pouches.SPIRIT_LARUPIA) {
                        params[0] = "Feldip Hills Hunter Area";
                        params[1] = new Position(2557, 2913);
                    } else if (pouch == Summoning.Pouches.ARCTIC_BEAR) {
                        params[0] = "Rellekka Hills Hunter Area";
                        params[1] = new Position(2721, 3779);
                    } else if (pouch == Summoning.Pouches.LAVA_TITAN) {
                        params[0] = "Lava Maze - *Deep Wilderness*";
                        params[1] = new Position(3028, 3840);
                    } else
                        return;
                    player.getDialogueManager().startDialogue(new FamiliarInspectionD(), params[0], params[1]);
                }
            } else if (SlayerMaster.startInteractionForId(player, mob.getId(), 2))
                return;
            else if (mob instanceof Gravestone) {
                Gravestone grave = (Gravestone) mob;
                grave.repair(player, false);
                return;
            }

            PickPocketableNpc pocket = PickPocketableNpc.get(mob.getId());
            if (pocket != null) {
                if (!player.getTimers().getClickDelay().elapsed(3000))
                    return;
                player.getTimers().getClickDelay().reset();
                player.getActionManager().setAction(new PickPocketAction(mob, pocket));
                return;
            }

            FishingSpots spot = FishingSpots.forId(mob.getId() | 2 << 24);
            if (spot != null) {
                player.getActionManager().setAction(new Fishing(spot, mob));
                return; // its a spot, they wont face us
            }
            switch (mob.getId()) {
                case 209:
                    ShopManager.getShops().get(111).open(player);
                    break;
                case 456://Father aereck
                    ShopManager.getShops().get(125).open(player);
                    break;
                case 6524://Bob barter (herbs)
                    player.getDialogueManager().startDialogue(new SimpleNpcMessageD(), mob.getId(), "Her ya go chap.");
                    Drinkables.decantPotsInv(player);
                    break;
                case 14620://Polypore dungeon merchant
                    ShopManager.getShops().get(119).open(player);
                    break;
                case 14849:
                case 1610:
                    if (mob instanceof ConditionalDeath)
                        ((ConditionalDeath) mob).useHammer(player);
                    break;
                case 961:
                    if (player.getGameMode().isIronMan()) {
                        player.message("Ironmen cannot use this feature.");
                        return;
                    }
                    if (!player.getTimers().getClickDelay().elapsed(3000))
                        return;
                    player.getTimers().getClickDelay().reset();
                    if (DonorStatus.isDonor(player, DonorStatus.SAPPHIRE_DONOR))
                        for (Skill skill : Skill.values())
                            if(player.getSkills().getLevel(skill) < (skill == Skill.CONSTITUTION ? player.getMaxConstitution() :  player.getSkills().getMaxLevel(skill)))
                                player.getSkills().restore(skill);
                    player.perform(new Graphic(436));
                    player.getSkills().restore(Skill.CONSTITUTION);
                    if (player.getEffects().hasActiveEffect(Effects.POISON)) {
                        player.message("Your poison has been cured.");
                        player.getEffects().removeEffect(Effects.POISON);
                    }
                    player.message("Your hitpoints" + (player.isDonor() ? " and stats" : "") + " have been restored.");
                    break;
                case 9711:
                    DungeoneeringRewards.openInterface(player);
                    break;
                case 5913://Aubury
                    mob.getDirection().face(player);
                    mob.forceChat("Senventior disthine molenko!");
                    mob.animate(1818);
                    mob.perform(new Graphic(343));
                    TeleportHandler.teleportPlayer(player, new Position(2911, 4832), TeleportType.NORMAL);
                    break;
                case 2259://Zamorak Mage
                    ShopManager.getShops().get(93).open(player);
                    break;
                case 6362://Eniola - ZMI Banker
                    player.getDialogueManager().startDialogue(new ZMIBankerD());
                    break;
                case 13727:
                    LoyaltyProgramme.open(player);
                    break;
                case 4250:
                    player.getPacketSender().sendInterface(28800);
                    break;
                case 7605://rogues' den banker
                case 9710://Dungeoneering banker
                    player.getBank().open();
                    break;
            /*case 3777:
                ShopManager.getShops().forId(24).open(player);
                break;*/
                case 251:
                    DonationStore.checkDonation(player);
//                    MySQLController.getStore().claim(player);
                    break;
                case 457:
                    player.message("The ghost teleports you away.");
                    player.getPacketSender().sendInterfaceRemoval();
                    player.moveTo(3651, 3486);
                    break;
                case 462:
                    mob.perform(CombatSpells.CONFUSE.getSpell().getAnimation().get());
                    mob.forceChat("Off you go!");
                    TeleportHandler.teleportPlayer(player, new Position(2345, 10338, 2), player.getSpellbook().getTeleportType());
                    break;
                case 4646:
                    player.message("You currently have " + player.getPoints().get(Points.VOTING) + " Voting points.");
                    player.message("You can earn points and coins by voting. To do so, simply use the ::vote command.");
                    ShopManager.getShops().get(player.getGameMode().isIronMan() ? 120 : 27).open(player);
                    break;
                case 8591:
                    /*if (!player.getMinigameAttributes().getNomadAttributes().hasFinishedPart(1)) {
                        player.message("You must complete Nomad's quest before being able to use this shop.");
                        return;
                    }*/
                    ShopManager.getShops().get(37).open(player);
                    break;
                case 805:
                    Tanning.selectionInterface(player);
                    break;
                case 6970:
                    player.getDialogueManager().startDialogue(new ShardExchange());
                    break;
                case 2253:
                    ShopManager.getShops().get(10).open(player);
                    break;
            }
            mob.getDirection().face(player);
            player.getDirection().face(mob);
        }));
    }

    public void thirdClick(Player player, Mob mob) {

        if (player.getRights() == PlayerRights.DEVELOPER)
            player.message("Third click npc id: " + mob.getId());

        if (!player.getControllerManager().processNPCDistanceClick3(mob))
            return;

        player.getDirection().setInteracting(mob);
        player.setRouteEvent(new RouteEvent(mob, () -> {
            player.getDirection().face(mob);

            if (!player.getControllerManager().processNPCClick3(mob))
                return;

            if (SlayerMaster.startInteractionForId(player, mob.getId(), 3))
                return;

            if (ThievingGuild.isInThievingGuild(player) && ThievingGuild.canDisturb()) {
                player.getPacketSender().sendMessage("You have " + ThievingGuild.getHealth() + " hanky points left to go!", true);
                return;
            }

            if (mob instanceof Gravestone) {
                Gravestone grave = (Gravestone) mob;
                grave.repair(player, true);
                return;
            }

            switch (mob.getId()) {
                case 6524://Bob barter (herbs)
                    player.getPacketSender().sendEnterAmountPrompt("Enter the potion doses to decant into:");
                    player.setInputHandling(new EnterPotionSizeToDecant());
                    break;
                case 8837:
                case 8838:
                case 8839:
                    MiningBase.propect(player, "You examine the remains...", "The remains contain traces of living minerals.");
                    break;
                case 9711:
                    player.getDialogueManager().sendNPCDialogue(9711, Expressions.NO_EXPRESSION, "No good! Take it away.");
                    break;
                case 13727:
                    LoyaltyProgramme.reset(player);
                    player.getPacketSender().sendInterfaceRemoval();
                    break;
                case 251:
                    player.getDialogueManager().startDialogue(new KingArthurDonatedD());
                    break;
                case 961:
                    if (player.getRights() == PlayerRights.PLAYER) {
                        player.message("This feature is currently only available for members.");
                        return;
                    }
                    boolean restore = player.getSettings().getInt(Settings.SPECIAL_ENERGY) < 100;
                    if (restore) {
                        player.getSettings().set(Settings.SPECIAL_ENERGY, 100);
                        CombatSpecial.updateBar(player);
                        player.message("Your special attack energy has been restored.");
                    }
                    for (Skill skill : Skill.values()) {
                        if (!player.getSkills().isFull(skill)) {
                            player.getSkills().restore(skill);
                            restore = true;
                        }
                    }
                    if (restore) {
                        player.perform(new Graphic(1302));
                        player.message("Your stats have been restored.");
                    } else
                        player.message("Your stats do not need to be restored at the moment.");
                    break;
                case 2253:
                    ShopManager.getShops().get(8).open(player);
                    break;
            }
            mob.getDirection().face(player);
            player.getDirection().face(mob);
        }));
    }

    public void fourthClick(Player player, Mob mob) {

        if (player.getRights() == PlayerRights.DEVELOPER)
            player.message("Fourth click npc id: " + mob.getId());

        if (!player.getControllerManager().processNPCDistanceClick4(mob))
            return;

        player.getDirection().setInteracting(mob);
        player.stopAll(false);
        player.setRouteEvent(new RouteEvent(mob, () -> {
            player.getDirection().face(mob);

            if (SlayerMaster.startInteractionForId(player, mob.getId(), 4))
                return;

            if (mob instanceof Gravestone) {
                Gravestone grave = (Gravestone) mob;
                grave.demolish(player);
                return;
            }

            switch (mob.getId()) {
                case 251:
                    player.getPacketSender().sendInterfaceRemoval();
                    if (!player.isDonor()) {
                        player.message("You need to be a donor to teleport to this zone.");
                        player.message("To become a member, use the command ::store and browse our store.");
                        return;
                    }
                    TeleportHandler.teleportPlayer(player, new Position(3186 + Misc.random(2), 5721 + Misc.random(4)), player.getSpellbook().getTeleportType());
                    break;
                case 2253:
                    ShopManager.getShops().get(128).open(player);
                    break;
            }
            mob.getDirection().face(player);
            player.getDirection().face(mob);
        }));
    }

    public void fifthClick(Player player, Mob mob) {
        if (player.getRights() == PlayerRights.DEVELOPER)
            player.message("Fifth click npc id: " + mob.getId());

        switch (mob.getId()) {
            case 6742:
            case 6744:
            case 6745:
            case 6746:
            case 9393:
            case 6743:
            case 9387:
            case 9384://Christmas characters
                Snowball.pelt(player, mob);
                return;
        }

        player.getDirection().setInteracting(mob);
        player.stopAll(false);
        player.setRouteEvent(new RouteEvent(mob, () -> {
            player.getDirection().face(mob);

            switch (mob.getId()) {
                case 2292://Merchant
                    player.setShopping(true);
                    Shop shop = ShopManager.getShops().get(11);
                    player.setShop(shop);
                    for(Item item : shop.getOriginalStock()) {
                        if(player.getInventory().contains(item)) {
                            int slot = player.getInventory().indexOf(item);
                            player.getShop().sellItem(player, slot, player.getInventory().getAmount(item), false);
                        }
                    }
                    player.setShop(null);
                    player.setShopping(false);
                    break;
                case 251:
                    ShopManager.getShops().get(24).open(player);
                    break;
            }
        }));
    }

    @Override
    public void handleMessage(Player player, Packet packet) {
        if (player.getMovement().getTeleporting() || player.getEmotesManager().isDoingEmote() || (player.newPlayer() && !player.getQuestManager().hasStarted(Quests.FIRST_ADVENTURE)))
            return;
        int opcode = packet.getOpcode();
        switch (opcode) {
            case PacketConstants.ATTACK_NPC:
                int index = packet.readShortA();
                if (index < 0 || index > GameWorld.getMobs().getMobs().capacity())
                    return;

                final Mob mob = GameWorld.getMobs().getMobs().get(index);
                if (mob == null)
                    return;

                if (mob.getDefinition().hasAttackOption())
                    attackNpc(player, mob);
                else
                    fifthClick(player, mob);
                break;

            case PacketConstants.MAGE_NPC:
                int npcIndex = packet.readLEShortA();
                int spellId = packet.readInt();

                if (npcIndex < 0 || spellId < 0 || npcIndex > GameWorld.getMobs().getMobs().capacity())
                    return;

                Mob n = GameWorld.getMobs().getMobs().get(npcIndex);

                CombatSpell spell = CombatSpells.getSpell(spellId);

                if (n == null || spell == null)
                    return;

                player.setCastSpell(spell);

                attackNpc(player, n);
                break;
        }

        int index = -1;
        switch (opcode) {
            case PacketConstants.FIRST_CLICK_OPCODE:
                index = packet.readLEShort();
                break;
            case PacketConstants.SECOND_CLICK_OPCODE:
                index = packet.readLEShortA();
                break;
            case PacketConstants.THIRD_CLICK_OPCODE:
                index = packet.readShort();
                break;
            case PacketConstants.FOURTH_CLICK_OPCODE:
                index = packet.readLEShort();
                break;
        }

        if (index < 0 || index > GameWorld.getMobs().getMobs().capacity())
            return;
        final Mob mob = GameWorld.getMobs().getMobs().get(index);

        if (mob == null)
            return;

        switch (opcode) {
            case PacketConstants.FIRST_CLICK_OPCODE:
                firstClick(player, mob);
                break;
            case PacketConstants.SECOND_CLICK_OPCODE:
                secondClick(player, mob);
                break;
            case PacketConstants.THIRD_CLICK_OPCODE:
                thirdClick(player, mob);
                break;
            case PacketConstants.FOURTH_CLICK_OPCODE:
                fourthClick(player, mob);
                break;
        }
    }
}
