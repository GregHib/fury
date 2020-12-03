package com.fury.network.packet.impl;

import com.fury.game.content.controller.impl.FirstAdventureController;
import com.fury.game.content.dialogue.impl.misc.StartD;
import com.fury.game.content.dialogue.impl.misc.WelcomeD;
import com.fury.game.content.dialogue.impl.quests.tutorial.WrongDirectionD;
import com.fury.game.content.dialogue.input.impl.EnterFriendToRefer;
import com.fury.game.content.global.quests.Quests;
import com.fury.game.content.global.quests.impl.FirstAdventure;
import com.fury.game.content.skill.free.dungeoneering.DungeonController;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.entity.character.player.link.transportation.TeleportHandler;
import com.fury.game.npc.misc.FurySage;
import com.fury.game.world.map.Position;
import com.fury.game.world.map.path.FixedTileStrategy;
import com.fury.game.world.map.path.PathFinder;
import com.fury.network.packet.Packet;
import com.fury.network.packet.PacketConstants;
import com.fury.network.packet.PacketListener;
import com.fury.util.Utils;

import java.util.LinkedList;

/**
 * This packet listener is called when a player has clicked on
 * either the mini-map or the actual game map to move around.
 *
 * @author Gabriel Hannason
 */

public class MovementPacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        if (player.newPlayer() && !player.getQuestManager().hasStarted(Quests.FIRST_ADVENTURE)) {
            if (player.getQuestManager().hasFinished(Quests.FIRST_ADVENTURE)) {
                player.getDialogueManager().startDialogue(new StartD());
            } else if (player.getQuestManager().hasStarted(Quests.FIRST_ADVENTURE))
                player.getDialogueManager().startDialogue(new WelcomeD());
            else if(player.getInputHandling() instanceof EnterFriendToRefer)
                player.getPacketSender().sendEnterInputPrompt("Enter name of friend to refer:");
            return;
        }

        if (!checkReqs(player, packet.getOpcode()))
            return;

        player.getPacketSender().sendInterfaceRemoval();

//        if (packet.getOpcode() != PacketConstants.COMMAND_MOVEMENT_OPCODE) {
//            player.setCastSpell(null);
//            player.getCombat().resetCombat();
//        }

        player.setInactive(false);
        player.stopAll();

        final int toX = packet.readLEShortA();
        final int toY = packet.readLEShort();

        if(packet.getOpcode() == PacketConstants.COMMAND_MOVEMENT_OPCODE && player.getRights().isOrHigher(PlayerRights.ADMINISTRATOR)) {
            if(!(player.getControllerManager().getController() instanceof DungeonController) && (player.getRights() == PlayerRights.ADMINISTRATOR && !player.isInWilderness() || player.getRights().isOrHigher(PlayerRights.OWNER))) {
                player.moveTo(new Position(toX, toY, player.getZ()));
                TeleportHandler.checkControllers(player, player);
                return;
            }
        }

        if(player.getQuestManager().hasStarted(Quests.FIRST_ADVENTURE)) {
            FirstAdventure quest = (FirstAdventure) player.getQuestManager().getQuest(Quests.FIRST_ADVENTURE);
            if(player.getControllerManager().getController() instanceof FirstAdventureController && (quest.getStage() < FirstAdventureController.AGILITY_START || quest.getStage() >= FirstAdventureController.AGILITY_COMPLETE)) {
                FurySage sage = quest.sage;
                if(!new Position(toX, toY).isWithinDistance(sage, 10)) {
                    player.getDialogueManager().startDialogue(new WrongDirectionD(), sage);
                    return;
                }
            }
        }

        int steps = PathFinder.findRoute(player, player.getSizeX(), player.getSizeY(), new FixedTileStrategy(toX, toY), true);
        LinkedList<Integer> bufferX = PathFinder.getLastPathBufferX();
        LinkedList<Integer> bufferY = PathFinder.getLastPathBufferY();
        for (int i = steps - 1; i >= 0; i--) {
            if (!player.getMovement().addWalkSteps(bufferX.get(i), bufferY.get(i), 25, true))
                break;
        }
    }

    public boolean checkReqs(Player player, int opcode) {
        if (player.getCombat().getFreezeDelay() >= Utils.currentTimeMillis() && opcode != PacketConstants.COMMAND_MOVEMENT_OPCODE)
            return false;

        if (player.getTrade().inTrade() && System.currentTimeMillis() - player.getTrade().lastAction <= 1000) {
            return false;
        }

        /*if (Dueling.checkRule(player, DuelRule.NO_MOVEMENT)) {
            if (opcode != PacketConstants.COMMAND_MOVEMENT_OPCODE)
                player.getPacketSender().sendMessage("Movement has been turned off in this duel!");
            return false;
        }*/

        if(player.getEmotesManager().isDoingEmote())
            return false;

        if (player.getSettings().isResting()) {
            player.getSettings().setRestingState(0);
            return false;
        }
        if (player.isSitting()) {
            player.setSitting(false);
            return false;
        }

        if (player.isCrossingObstacle())
            return false;

        if (player.isNeedsPlacement())
            return false;

        return !player.getMovement().isLocked();
    }

}