package com.fury.network.packet.impl;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.GameSettings;
import com.fury.game.content.combat.magic.spell.Teleports;
import com.fury.game.content.dialogue.SkillDialogue;
import com.fury.game.content.dialogue.impl.misc.BankPinD;
import com.fury.game.content.dialogue.impl.skills.dungeoneering.PrestigeReset;
import com.fury.game.content.dialogue.impl.transportation.*;
import com.fury.game.content.dialogue.input.impl.EnterClanChatToJoin;
import com.fury.game.content.eco.ge.GrandExchange;
import com.fury.game.content.global.Achievements;
import com.fury.game.content.global.dnd.star.ShootingStar;
import com.fury.game.content.global.minigames.impl.PestControlShop;
import com.fury.game.content.global.minigames.impl.RecipeForDisaster;
import com.fury.game.content.global.treasuretrails.TreasureTrailHandlers;
import com.fury.game.content.misc.items.ItemsKeptOnDeath;
import com.fury.game.content.misc.items.random.impl.imps.MysteryBoxTimedGen;
import com.fury.game.content.misc.objects.GravestoneSelection;
import com.fury.game.content.skill.free.cooking.Consumables;
import com.fury.game.content.skill.free.crafting.glass.GlassBlowing;
import com.fury.game.content.skill.free.crafting.leather.LeatherMaking;
import com.fury.game.content.skill.free.crafting.leather.tanning.Tanning;
import com.fury.game.content.skill.free.dungeoneering.DungeoneeringRewards;
import com.fury.game.content.skill.member.construction.Sawmill;
import com.fury.game.content.skill.member.herblore.IngredientsBook;
import com.fury.game.content.skill.member.slayer.SlayerManager;
import com.fury.game.content.skill.member.summoning.Infusion;
import com.fury.game.content.skill.member.summoning.impl.Summoning;
import com.fury.game.entity.character.combat.effects.EffectManager;
import com.fury.game.entity.character.combat.equipment.weapon.CombatSpecial;
import com.fury.game.entity.character.combat.equipment.weapon.FightType;
import com.fury.game.entity.character.combat.equipment.weapon.WeaponInterface;
import com.fury.game.entity.character.combat.magic.Autocasting;
import com.fury.game.entity.character.combat.magic.MagicSpells;
import com.fury.game.entity.character.player.content.*;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.entity.character.player.info.SkillInfo;
import com.fury.game.node.entity.actor.figure.player.handles.Settings;
import com.fury.game.node.entity.actor.figure.player.misc.redo.DropLog;
import com.fury.game.node.entity.actor.figure.player.misc.redo.KillsTracker;
import com.fury.game.system.communication.clans.ClanChat;
import com.fury.game.system.communication.clans.ClanChatManager;
import com.fury.network.packet.Packet;
import com.fury.network.packet.PacketListener;
import com.fury.util.Misc;
import com.fury.util.NameUtils;

/**
 * This packet listener manages a button that the player has clicked upon.
 *
 * @author Gabriel Hannason
 */

public class ButtonClickPacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        int id = packet.readInt();

        if (player.getRights().isUpperStaff())
            player.getPacketSender().sendConsoleMessage("Clicked button: " + id);

        if (!player.getControllerManager().processButtonClick(id))
            return;

        if (player.getQuestManager().isButtonPressed(id))
            return;

        if (checkHandlers(player, id))
            return;

        if (id >= 28656 && id <= 28766) {
            int offset = (id - 28656) / 5;
            player.getHouse().createRoom(offset);
        }

        if (id >= 38333 && id <= 38340) {
            player.getDialogueManager().continueCustomDialogue(id);
        }

        if (player.getBank() != null && player.getBank().handleButton(id))
            return;

        switch (id) {
            case 26046://Examine drop logs
                player.getSettings().set(Settings.EXAMINE_DROP_TABLES, !player.getSettings().getBool(Settings.EXAMINE_DROP_TABLES));
                player.message("Examine to open drop logs is: " + (player.getSettings().getBool(Settings.EXAMINE_DROP_TABLES) ? "Enabled" : "Disabled"));
                break;
            case 26050:
                player.getSettings().set(Settings.WILDERNESS_WARNINGS, !player.getSettings().getBool(Settings.WILDERNESS_WARNINGS));
                player.message("Wilderness warning: " + (player.getSettings().getBool(Settings.WILDERNESS_WARNINGS) ? "Enabled" : "Disabled"));
                break;
            case 6020://Unmorph
                player.getActionManager().forceStop();
                break;
            case 7455://Trollheim teleport
            case 13087:
            case 30226:
                player.getDialogueManager().startDialogue(new TrainingTeleportsD());
                break;
            case 15284:
                DungeoneeringRewards.buy(player);
                break;
            case 41006:
                Infusion.openInfuseInterface(player, false);
                break;
            case 41017:
                Infusion.openInfuseInterface(player, true);
                break;
            case 41011:
                player.getSummoning().setDisplayOnlyCreatable(!player.getSummoning().displayOnlyCreatable());
                if (player.getInterfaceId() == Infusion.SCROLL_INTERFACE_ID) {
                    Infusion.openInfuseInterface(player, true);
                } else if (player.getInterfaceId() == Infusion.POUCH_INTERFACE_ID) {
                    Infusion.openInfuseInterface(player, false);
                }
                break;
            case 11001:
                player.setDefensiveCasting(!player.isDefensiveCasting());
                break;
            case 907://Graphics settings
                player.openInterface(26000);
                break;
            case 909://Audio options
                player.openInterface(19310);
                break;
            case 920://House options
                player.getPacketSender().sendButtonToggle(id, false);
                player.getPacketSender().sendConfig(221, 1);
                break;
            case 911://Profanity
            case 914://Accept aid
                player.getPacketSender().sendButtonToggle(id, false);
                break;
            case 6824://Duel arena claim button
            case 10162://Ingredients book close
                player.getPacketSender().sendInterfaceRemoval();
                break;
            case 15004:
            case 714://Price checker
                if (!player.busy() && !player.getCombat().isInCombat()) {
                    player.stopAll(false, false);
                    player.getPriceChecker().open();
                } else {
                    player.message("You cannot open this right now.");
                }
                break;
//            case 28177:
//                TeleportHandler.teleportPlayer(player, new Position(3197, 3434), TeleportType.NORMAL);
//                break;
            case 38082:
            case 38002:
            case 5384:
            case 26003:
                if (player.getInterfaceId() == 5292)
                    player.getBank().getSearch().stop();
                player.getPacketSender().sendInterfaceRemoval();
                break;
            case 1036:
                EnergyHandler.rest(player);
                break;
            case 276132:
                player.getDialogueManager().startDialogue(new PrestigeReset());
                break;
            case 27229:
                player.getDungManager().formParty();
                break;
            case 26244:
                player.getDungManager().changeFloor();
                break;
            case 26247:
                player.getDungManager().changeComplexity();
                break;
            case 26229:
                player.getDungManager().checkLeaveParty();
                break;
            case 26250:
                player.getDungManager().invite();
                break;
            case 27326:
            case 26226:
                player.getPacketSender().sendTabInterface(GameSettings.QUESTS_TAB, 639);
                player.getPacketSender().sendDungeoneeringTabIcon(false);
                break;
            case 19263:
                player.getPacketSender().sendTabInterface(GameSettings.OPTIONS_TAB, 904);
                break;
//            case 28180:
//                TeleportHandler.teleportPlayer(player, new Position(3446 + Misc.inclusiveRandom(3), 3697 + Misc.inclusiveRandom(3)), TeleportType.KINSHIP_TELE);
//                break;
            case 14176:
                player.setUntradeableDropItem(null);
                player.getPacketSender().sendInterfaceRemoval();
                break;
            case 14175:
                player.getPacketSender().sendInterfaceRemoval();
                if (player.getUntradeableDropItem() != null && player.getInventory().contains(player.getUntradeableDropItem())) {
                    if (player.getDungManager().isInside())
                        player.getDungManager().unbind(player.getUntradeableDropItem());
                    TreasureTrailHandlers.handleItemDestruction(player, player.getUntradeableDropItem().getId());
                    player.getInventory().delete(player.getUntradeableDropItem());
                    player.message("Your item vanishes as it hits the floor.");
                    Sounds.sendSound(player, Sounds.Sound.DROP_ITEM);

                    if(player.getUntradeableDropItem().getId() == 18768) {
                        MysteryBoxTimedGen.removeTimerFor(player);
                    }
                }
                player.setUntradeableDropItem(null);
                break;
            case 1013:
                player.getSkills().setTotalGainedExp(0);
                break;
            case 39170:
                player.forceChat("[QUICK CHAT] I am currently playing on the " + Misc.formatText(player.getGameMode().toString().toLowerCase().replaceAll("_", " ")) + " game mode.");
                break;
            case 39164:
                player.getDialogueManager().startDialogue(new ThievingTeleportsD());
                break;
            case 39162:
                if (!ShootingStar.hasCrashed()) {
                    player.message("A star has not crashed yet.");
                } else {
                    player.message("The current star is located at " + NameUtils.capitalize(ShootingStar.getLocation().name().toLowerCase().replaceAll("_", " ")) + " " + ShootingStar.getPercentage() + "%");
                }
                break;
            case 39163:
                player.message("Talk to the spirit tree west of the general store in edgeville.");
                break;
            case 39185:
                player.message("You have earned a total of " + (int) player.getAchievementAttributes().getTotalLoyaltyPointsEarned() + " loyalty points.");
                break;
            case 39178:
                if (player.getInterfaceId() == -1)
                    KillsTracker.open(player);
                break;
            case 39179:
                if (player.getInterfaceId() == -1)
                    DropLog.open(player);
                break;
            case 30106:
            case 13061:
            case 1174://Boss Teleports
                player.getDialogueManager().startDialogue(new BossTeleportsD());
                break;
            case 39197:
                if (player.getInterfaceId() == -1)
                    RecipeForDisaster.openQuestLog(player);
                break;
            case 350:
                player.message("To autocast a spell, please right-click it and choose the autocast option.");
                player.getPacketSender().sendTab(GameSettings.MAGIC_TAB).sendConfig(108, player.getAutoCastSpell() == null ? 3 : 1);
                break;
            case 10001://Hitpoint orb heal
                if (player.getInterfaceId() == -1) {
                    Consumables.handleHealAction(player);
                } else {
                    player.message("You cannot heal yourself right now.");
                }
                break;
            case 10002://Hitpoint orb cure
                EffectManager.healPoison(player);
                break;
            case 3546:
            case 3420:
                if (System.currentTimeMillis() - player.getTrade().lastAction <= 300)//TODO why is the not using click delay?
                    return;
                player.getTrade().lastAction = System.currentTimeMillis();
                if (player.getTrade().inTrade()) {
                    player.getTrade().acceptTrade(id == 3546 ? 2 : 1);
                } else {
                    player.getPacketSender().sendInterfaceRemoval();
                }
                break;
            case 841:
                IngredientsBook.readBook(player, player.getCurrentBookPage() + 2, true);
                break;
            case 839:
                IngredientsBook.readBook(player, player.getCurrentBookPage() - 2, true);
                break;
            case 14922:
                player.getPacketSender().sendClientRightClickRemoval().sendInterfaceRemoval();
                break;
            case 14921:
                player.message("Please visit the forums and ask for help in the support section.");
                break;
            case 5294:
                player.getPacketSender().sendClientRightClickRemoval().sendInterfaceRemoval();
                player.setDialogueActionId(player.getBankPinAttributes().hasBankPin() ? 8 : 7);
                player.getDialogueManager().startDialogue(new BankPinD());
                break;
            case 18500:
                if (player.getPriceChecker().isOpen())
                    player.getPriceChecker().withdrawAll();
                break;
            case 18501:
                if (player.getPriceChecker().isOpen())
                    player.getPriceChecker().depositInventory();
                break;
            case 2704:
                if (player.getFamiliar() != null && player.getFamiliar().getBeastOfBurden() != null) {
                    player.getFamiliar().depositInventory();
                } else {
                    player.message("You do not have a familiar who can hold items.");
                }
                break;
            case 2735:
            case 1511:
                if (player.getFamiliar() != null && player.getFamiliar().getBeastOfBurden() != null) {
                    player.getFamiliar().takeBob();
                    player.getPacketSender().sendInterfaceRemoval();
                } else {
                    player.message("You do not have a familiar who can hold items.");
                }
                break;
            case 30064:
            case 13035:
            case 1164://Skilling Teleport
                player.getDialogueManager().startDialogue(new SkillTeleportsD());
                break;
            case 30075:
            case 13045:
            case 1167://City Teleport
                player.getDialogueManager().startDialogue(new CityTeleportsD());
                break;
            case 30138:
            case 13069:
            case 1540://Minigame Teleport
                player.getDialogueManager().startDialogue(new MinigameTeleportD());
                break;
            case 30083:
            case 13053:
            case 1170://Dungeon Teleport
                player.getDialogueManager().startDialogue(new DungeonTeleportsD());
                break;
            case 30162:
            case 13079:
            case 1541://Wilderness Teleport
                player.getDialogueManager().startDialogue(new WildernessTeleportsD());
                break;
            case 14873:
            case 14874:
            case 14875:
            case 14876:
            case 14877:
            case 14878:
            case 14879:
            case 14880:
            case 14881:
            case 14882:
                BankPin.clickedButton(player, id);
                break;
            case 21000:
                if (!player.isBanking() || player.getInterfaceId() != 5292)
                    return;
                player.getBank().setSwapMode(!player.getBank().swapMode());
                player.getPacketSender().sendConfig(304, player.getBank().swapMode() ? 0 : 1);
                break;
            case 22845:
            case 24115:
            case 24010:
            case 24041:
            case 150:
                player.setAutoRetaliate(!player.isAutoRetaliate());
                break;
            case 29329:
                if (player.getInterfaceId() > 0) {
                    player.message("Please close the interface you have open before opening another one.");
                    return;
                }

                ClanChat clan = player.getCurrentClanChat();
                if (clan == null) {
                    player.setInputHandling(new EnterClanChatToJoin());
                    player.getPacketSender().sendEnterInputPrompt("Enter the player name whose channel you wish to join:");
                    player.getPacketSender().sendButtonToggle(29329, false);
                    player.getPacketSender().sendTooltip(29329, "Join Clan");
                } else {
                    ClanChatManager.leave(player, false);
                    player.setClanChatName(null);
                }
                break;
            case 29330:
                ClanChat clan2 = player.getCurrentClanChat();
                if (clan2 == null) {
                    player.message("You are not in a clanchat channel.");
                    return;
                }
                ClanChatManager.leave(player, false);
                player.setClanChatName(null);
                break;
            case 29331:
                if (player.getInterfaceId() > 0) {
                    player.message("Please close the interface you have open before opening another one.");
                    return;
                }
                if (player.getCurrentClanChat() == null) {
                    player.message("You must be in a clan chat to do this.");
                    return;
                }
                if (ClanChatManager.getClanChatChannel(player) == player.getCurrentClanChat())
                    ClanChatManager.clanChatSetupInterface(player, true);
                else {
                    player.message("Only the clan owner can access these settings.");
                }

                break;
            case 29455:
                if (player.getInterfaceId() > 0) {
                    player.message("Please close the interface you have open before opening another one.");
                    return;
                }
                ClanChatManager.toggleLootShare(player);
                break;
            case 48042:
                ClanChatManager.toggleCoinShare(player);
                break;
            case 152:
                if (player.getSettings().getInt(Settings.RUN_ENERGY) <= 1) {
                    player.message("You do not have enough energy to do this.");
                    player.getSettings().set(Settings.RUNNING, false);
                } else
                    player.getSettings().set(Settings.RUNNING, !player.getSettings().getBool(Settings.RUNNING));
                player.getPacketSender().sendRunStatus();
                break;
            case 27651:
            case 15001:
                if (player.getInterfaceId() == -1) {
                    player.stopAll();
                    player.getPacketSender().sendInterface(21172);
                    BonusManager.update(player);
                } else
                    player.message("Please close the interface you have open before doing this.");
                break;
            case 15007:
                if (player.getInterfaceId() > 0) {
                    player.message("Please close the interface you have open before opening another one.");
                    return;
                }
                player.stopAll();
                ItemsKeptOnDeath.open(player);
                break;
            case 2458: //Logout
                player.logout();
                break;
            //	case 10003:
            case 29138:
            case 29038:
            case 29063:
            case 29113:
            case 29163:
            case 29188:
            case 29213:
            case 29238:
            case 30007:
            case 48023:
            case 33033:
            case 30108:
            case 7473:
            case 7562:
            case 7487:
            case 7712:
            case 7788:
            case 8481:
            case 7612:
            case 7587:
            case 7662:
            case 7462:
            case 7548:
            case 7687:
            case 7537:
            case 12322:
            case 7637:
            case 12311:
                CombatSpecial.activate(player);
                break;
            case 1772: // shortbow & longbow
                if (player.getWeapon() == WeaponInterface.SHORTBOW) {
                    player.setFightType(FightType.SHORTBOW_ACCURATE);
                } else if (player.getWeapon() == WeaponInterface.LONGBOW) {
                    player.setFightType(FightType.LONGBOW_ACCURATE);
                } else if (player.getWeapon() == WeaponInterface.CROSSBOW) {
                    player.setFightType(FightType.CROSSBOW_ACCURATE);
                }
                player.getCombatDefinitions().setAttackStyle(0);
                break;
            case 1771:
                if (player.getWeapon() == WeaponInterface.SHORTBOW) {
                    player.setFightType(FightType.SHORTBOW_RAPID);
                } else if (player.getWeapon() == WeaponInterface.LONGBOW) {
                    player.setFightType(FightType.LONGBOW_RAPID);
                } else if (player.getWeapon() == WeaponInterface.CROSSBOW) {
                    player.setFightType(FightType.CROSSBOW_RAPID);
                }
                player.getCombatDefinitions().setAttackStyle(1);
                break;
            case 1770:
                if (player.getWeapon() == WeaponInterface.SHORTBOW) {
                    player.setFightType(FightType.SHORTBOW_LONGRANGE);
                } else if (player.getWeapon() == WeaponInterface.LONGBOW) {
                    player.setFightType(FightType.LONGBOW_LONGRANGE);
                } else if (player.getWeapon() == WeaponInterface.CROSSBOW) {
                    player.setFightType(FightType.CROSSBOW_LONGRANGE);
                }
                player.getCombatDefinitions().setAttackStyle(2);
                break;
            case 2282: // dagger & sword
                if (player.getWeapon() == WeaponInterface.DAGGER) {
                    player.setFightType(FightType.DAGGER_STAB);
                } else if (player.getWeapon() == WeaponInterface.SWORD) {
                    player.setFightType(FightType.SWORD_STAB);
                }
                player.getCombatDefinitions().setAttackStyle(0);
                break;
            case 2285:
                if (player.getWeapon() == WeaponInterface.DAGGER) {
                    player.setFightType(FightType.DAGGER_LUNGE);
                } else if (player.getWeapon() == WeaponInterface.SWORD) {
                    player.setFightType(FightType.SWORD_LUNGE);
                }
                player.getCombatDefinitions().setAttackStyle(1);
                break;
            case 2284:
                if (player.getWeapon() == WeaponInterface.DAGGER) {
                    player.setFightType(FightType.DAGGER_SLASH);
                } else if (player.getWeapon() == WeaponInterface.SWORD) {
                    player.setFightType(FightType.SWORD_SLASH);
                }
                player.getCombatDefinitions().setAttackStyle(2);
                break;
            case 2283:
                if (player.getWeapon() == WeaponInterface.DAGGER) {
                    player.setFightType(FightType.DAGGER_BLOCK);
                } else if (player.getWeapon() == WeaponInterface.SWORD) {
                    player.setFightType(FightType.SWORD_BLOCK);
                }
                player.getCombatDefinitions().setAttackStyle(3);
                break;
            case 2429: // scimitar & longsword & chinchompa & salamandar
                if (player.getWeapon() == WeaponInterface.SCIMITAR) {
                    player.setFightType(FightType.SCIMITAR_CHOP);
                } else if (player.getWeapon() == WeaponInterface.LONGSWORD) {
                    player.setFightType(FightType.LONGSWORD_CHOP);
                } else if (player.getWeapon() == WeaponInterface.CHINCHOMPA) {
                    player.setFightType(FightType.CHINCHOMPA_SHORTFUSE);
                } else if (player.getWeapon() == WeaponInterface.SALAMANDER) {
                    player.setFightType(FightType.SALAMANDER_SCORCH);
                }
                player.getCombatDefinitions().setAttackStyle(0);
                break;
            case 2432:
                if (player.getWeapon() == WeaponInterface.SCIMITAR) {
                    player.setFightType(FightType.SCIMITAR_SLASH);
                } else if (player.getWeapon() == WeaponInterface.LONGSWORD) {
                    player.setFightType(FightType.LONGSWORD_SLASH);
                } else if (player.getWeapon() == WeaponInterface.CHINCHOMPA) {
                    player.setFightType(FightType.CHINCHOMPA_MEDIUMFUSE);
                } else if (player.getWeapon() == WeaponInterface.SALAMANDER) {
                    player.setFightType(FightType.SALAMANDER_FLARE);
                }
                player.getCombatDefinitions().setAttackStyle(1);
                break;
            case 2431:
                if (player.getWeapon() == WeaponInterface.SCIMITAR) {
                    player.setFightType(FightType.SCIMITAR_LUNGE);
                } else if (player.getWeapon() == WeaponInterface.LONGSWORD) {
                    player.setFightType(FightType.LONGSWORD_LUNGE);
                } else if (player.getWeapon() == WeaponInterface.CHINCHOMPA) {
                    player.setFightType(FightType.CHINCHOMPA_LONGFUSE);
                } else if (player.getWeapon() == WeaponInterface.SALAMANDER) {
                    player.setFightType(FightType.SALAMANDER_BLAZE);
                }
                player.getCombatDefinitions().setAttackStyle(2);
                break;
            case 2430:
                if (player.getWeapon() == WeaponInterface.SCIMITAR) {
                    player.setFightType(FightType.SCIMITAR_BLOCK);
                } else if (player.getWeapon() == WeaponInterface.LONGSWORD) {
                    player.setFightType(FightType.LONGSWORD_BLOCK);
                }
                player.getCombatDefinitions().setAttackStyle(3);
                break;
            case 3802: // mace
                player.setFightType(FightType.MACE_POUND);
                player.getCombatDefinitions().setAttackStyle(0);
                break;
            case 3805:
                player.setFightType(FightType.MACE_PUMMEL);
                player.getCombatDefinitions().setAttackStyle(1);
                break;
            case 3804:
                player.setFightType(FightType.MACE_SPIKE);
                player.getCombatDefinitions().setAttackStyle(2);
                break;
            case 3803:
                player.setFightType(FightType.MACE_BLOCK);
                player.getCombatDefinitions().setAttackStyle(3);
                break;
            case 4454: // knife, thrownaxe, dart & javelin
                if (player.getWeapon() == WeaponInterface.KNIFE) {
                    player.setFightType(FightType.KNIFE_ACCURATE);
                } else if (player.getWeapon() == WeaponInterface.THROWNAXE) {
                    player.setFightType(FightType.THROWNAXE_ACCURATE);
                } else if (player.getWeapon() == WeaponInterface.DART) {
                    player.setFightType(FightType.DART_ACCURATE);
                } else if (player.getWeapon() == WeaponInterface.JAVELIN) {
                    player.setFightType(FightType.JAVELIN_ACCURATE);
                }
                player.getCombatDefinitions().setAttackStyle(0);
                break;
            case 4453:
                if (player.getWeapon() == WeaponInterface.KNIFE) {
                    player.setFightType(FightType.KNIFE_RAPID);
                } else if (player.getWeapon() == WeaponInterface.THROWNAXE) {
                    player.setFightType(FightType.THROWNAXE_RAPID);
                } else if (player.getWeapon() == WeaponInterface.DART) {
                    player.setFightType(FightType.DART_RAPID);
                } else if (player.getWeapon() == WeaponInterface.JAVELIN) {
                    player.setFightType(FightType.JAVELIN_RAPID);
                }
                player.getCombatDefinitions().setAttackStyle(1);
                break;
            case 4452:
                if (player.getWeapon() == WeaponInterface.KNIFE) {
                    player.setFightType(FightType.KNIFE_LONGRANGE);
                } else if (player.getWeapon() == WeaponInterface.THROWNAXE) {
                    player.setFightType(FightType.THROWNAXE_LONGRANGE);
                } else if (player.getWeapon() == WeaponInterface.DART) {
                    player.setFightType(FightType.DART_LONGRANGE);
                } else if (player.getWeapon() == WeaponInterface.JAVELIN) {
                    player.setFightType(FightType.JAVELIN_LONGRANGE);
                }
                player.getCombatDefinitions().setAttackStyle(2);
                break;
            case 4685: // spear
                player.setFightType(FightType.SPEAR_LUNGE);
                player.getCombatDefinitions().setAttackStyle(0);
                break;
            case 4688:
                player.setFightType(FightType.SPEAR_SWIPE);
                player.getCombatDefinitions().setAttackStyle(1);
                break;
            case 4687:
                player.setFightType(FightType.SPEAR_POUND);
                player.getCombatDefinitions().setAttackStyle(2);
                break;
            case 4686:
                player.setFightType(FightType.SPEAR_BLOCK);
                player.getCombatDefinitions().setAttackStyle(3);
                break;
            case 4711: // 2h sword
                player.setFightType(FightType.TWOHANDEDSWORD_CHOP);
                player.getCombatDefinitions().setAttackStyle(0);
                break;
            case 4714:
                player.setFightType(FightType.TWOHANDEDSWORD_SLASH);
                player.getCombatDefinitions().setAttackStyle(1);
                break;
            case 4713:
                player.setFightType(FightType.TWOHANDEDSWORD_SMASH);
                player.getCombatDefinitions().setAttackStyle(2);
                break;
            case 4712:
                player.setFightType(FightType.TWOHANDEDSWORD_BLOCK);
                player.getCombatDefinitions().setAttackStyle(3);
                break;
            case 5576: // pickaxe
                player.setFightType(FightType.PICKAXE_SPIKE);
                player.getCombatDefinitions().setAttackStyle(0);
                break;
            case 5579:
                player.setFightType(FightType.PICKAXE_IMPALE);
                player.getCombatDefinitions().setAttackStyle(1);
                break;
            case 5578:
                player.setFightType(FightType.PICKAXE_SMASH);
                player.getCombatDefinitions().setAttackStyle(2);
                break;
            case 5577:
                player.setFightType(FightType.PICKAXE_BLOCK);
                player.getCombatDefinitions().setAttackStyle(3);
                break;
            case 7768: // claws
                player.setFightType(FightType.CLAWS_CHOP);
                player.getCombatDefinitions().setAttackStyle(0);
                break;
            case 7771:
                player.setFightType(FightType.CLAWS_SLASH);
                player.getCombatDefinitions().setAttackStyle(1);
                break;
            case 7770:
                player.setFightType(FightType.CLAWS_LUNGE);
                player.getCombatDefinitions().setAttackStyle(2);
                break;
            case 7769:
                player.setFightType(FightType.CLAWS_BLOCK);
                player.getCombatDefinitions().setAttackStyle(3);
                break;
            case 8466: // halberd
                player.setFightType(FightType.HALBERD_JAB);
                player.getCombatDefinitions().setAttackStyle(0);
                break;
            case 8468:
                player.setFightType(FightType.HALBERD_SWIPE);
                player.getCombatDefinitions().setAttackStyle(1);
                break;
            case 8467:
                player.setFightType(FightType.HALBERD_FEND);
                player.getCombatDefinitions().setAttackStyle(2);
                break;
            case 5862: // unarmed
                player.setFightType(FightType.UNARMED_PUNCH);
                player.getCombatDefinitions().setAttackStyle(0);
                break;
            case 5861:
                player.setFightType(FightType.UNARMED_KICK);
                player.getCombatDefinitions().setAttackStyle(1);
                break;
            case 5860:
                player.setFightType(FightType.UNARMED_BLOCK);
                player.getCombatDefinitions().setAttackStyle(2);
                break;
            case 12298: // whip
                player.setFightType(FightType.WHIP_FLICK);
                player.getCombatDefinitions().setAttackStyle(0);
                break;
            case 12297:
                player.setFightType(FightType.WHIP_LASH);
                player.getCombatDefinitions().setAttackStyle(1);
                break;
            case 12296:
                player.setFightType(FightType.WHIP_DEFLECT);
                player.getCombatDefinitions().setAttackStyle(2);
                break;
            case 336: // staff
                player.setFightType(FightType.STAFF_BASH);
                player.getCombatDefinitions().setAttackStyle(0);
                break;
            case 335:
                player.setFightType(FightType.STAFF_POUND);
                player.getCombatDefinitions().setAttackStyle(1);
                break;
            case 334:
                player.setFightType(FightType.STAFF_FOCUS);
                player.getCombatDefinitions().setAttackStyle(2);
                break;
            case 433: // warhammer
                player.setFightType(FightType.WARHAMMER_POUND);
                player.getCombatDefinitions().setAttackStyle(0);
                break;
            case 432:
                player.setFightType(FightType.WARHAMMER_PUMMEL);
                player.getCombatDefinitions().setAttackStyle(1);
                break;
            case 431:
                player.setFightType(FightType.WARHAMMER_BLOCK);
                player.getCombatDefinitions().setAttackStyle(2);
                break;
            case 782: // scythe
                player.setFightType(FightType.SCYTHE_REAP);
                player.getCombatDefinitions().setAttackStyle(0);
                break;
            case 784:
                player.setFightType(FightType.SCYTHE_CHOP);
                player.getCombatDefinitions().setAttackStyle(1);
                break;
            case 785:
                player.setFightType(FightType.SCYTHE_JAB);
                player.getCombatDefinitions().setAttackStyle(2);
                break;
            case 783:
                player.setFightType(FightType.SCYTHE_BLOCK);
                player.getCombatDefinitions().setAttackStyle(3);
                break;
            case 1704: // battle axe
                player.setFightType(FightType.BATTLEAXE_CHOP);
                player.getCombatDefinitions().setAttackStyle(0);
                break;
            case 1707:
                player.setFightType(FightType.BATTLEAXE_HACK);
                player.getCombatDefinitions().setAttackStyle(1);
                break;
            case 1706:
                player.setFightType(FightType.BATTLEAXE_SMASH);
                player.getCombatDefinitions().setAttackStyle(2);
                break;
            case 1705:
                player.setFightType(FightType.BATTLEAXE_BLOCK);
                player.getCombatDefinitions().setAttackStyle(3);
                break;
        }
    }

    private boolean checkHandlers(Player player, int id) {
        if (GravestoneSelection.buttonPressed(player, id))
            return true;

        if (Summoning.handleButtons(player, id))
            return true;

        if (MagicSpells.handle(player, id))
            return true;

        if (Teleports.INSTANCE.handle(player, id))
            return true;

        if (SkillInfo.handleButtons(player, id))
            return true;

        if (SkillDialogue.handleDialogueButtons(player, id))
            return true;

        if (Sawmill.handleButtonClick(player, id))
            return true;

        switch (player.getInterfaceId()) {
            case 6575:
            case 6412://Confirm
            case 6733://Victory
                if(player.getDuelConfigurations().processButtonClick(player, player.getInterfaceId(), id))
                    return true;
                break;
        }
        switch (id) {
            case 2494:
            case 2495:
            case 2496:
            case 2497:
            case 2498:
            case 2471:
            case 2472:
            case 2473:
            case 2461:
            case 2462:
            case 2482:
            case 2483:
            case 2484:
            case 2485:
                if (player.getRights() == PlayerRights.DEVELOPER) {
                    player.message("Dialogue button id: " + id + ", action id: " + player.getDialogueActionId());
                    player.getPacketSender().sendConsoleMessage("Dialogue button id: " + id + ", action id: " + player.getDialogueActionId());
                }

                player.getDialogueManager().continueDialogue(id);
                return true;
        }
        if (player.getPrayer().handleButton(id))
            return true;

        //Why is this check here and not at start?
        if (player.getMovement().isLocked() && id != 2458 && id != 6020)
            return true;

        if (Achievements.handleButton(player, id)) {
            return true;
        }
        if (Music.handleButton(player, id)) {
            return true;
        }

        if (Autocasting.handleAutoCast(player, id)) {
            return true;
        }
        if (LoyaltyProgramme.handleButton(player, id)) {
            return true;
        }
        if (LeatherMaking.handleButton(player, id) || Tanning.handleButton(player, id)) {
            return true;
        }
        if (GlassBlowing.createGlass(player, id)) {
            return true;
        }

        if (player.getEmotesManager().handleButton(id))
            return true;

        if (PestControlShop.handleInterface(player, id)) {
            return true;
        }
        if (SlayerManager.handleRewardsInterface(player, id)) {
            return true;
        }
        if (ExperienceLamps.handleButton(player, id)) {
            return true;
        }
        if (GrandExchange.handleButton(player, id)) {
            return true;
        }
        return ClanChatManager.handleClanChatSetupButton(player, id);
    }
}
