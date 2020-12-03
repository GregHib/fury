package com.fury.game.content.controller.impl.duel;

import com.fury.game.content.controller.Controller;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.content.PlayerInteractingOption;
import com.fury.game.network.packet.out.WalkableInterface;
import com.fury.game.world.map.Position;

public class DuelController extends Controller {

    @Override
    public void start() {
        sendInterfaces();
        player.message("Dueling is disabled until the economy can properly sustain it. Thanks for your understanding :)");
//        if(player.getPlayerInteractingOption() != PlayerInteractingOption.CHALLENGE)
//            player.getPacketSender().sendInteractionOption("Challenge", 2, false);
        moved();
    }

    @Override
    public boolean login() {
        start();
        return false;
    }

    public boolean logout() {
        return false;
    }

    @Override
    public void forceClose() {
        remove();
    }

    public boolean processMagicTeleport(Position toTile) {
        return true;
    }


    public boolean processItemTeleport(Position toTile) {
        return true;
    }

    @Override
    public void magicTeleported(int type) {
        removeController();
        remove();
    }

    @Override
    public void moved() {
        if (!isAtDuelArena(player)) {
            removeController();
            remove();
        }
    }

    @Override
    public boolean canPlayerOption1(Player target) {
        player.stopAll(true);
        if (target.busy()) {
            player.message("The other player is busy.");
            return false;
        }
        if (target.getTemporaryAttributes().get("DuelChallenged") == player) {
            target.getTemporaryAttributes().remove("DuelChallenged");
            DuelConfigurations duelConfigurations = new DuelConfigurations(player, target, (Boolean) target.getTemporaryAttributes().remove("DuelFriendly"));
            player.setDuelConfigurations(duelConfigurations);
            target.setDuelConfigurations(duelConfigurations);
            return false;
        }
        player.getTemporaryAttributes().put("DuelTarget", target);
        player.getTemporaryAttributes().put("WillDuelFriendly", false);
        challenge(player);
        return false;
    }

    public static void challenge(Player player) {
        player.getPacketSender().sendInterfaceRemoval();
        Boolean friendly = (Boolean) player.getTemporaryAttributes().remove("WillDuelFriendly");
        if (friendly == null)
            return;
        Player target = (Player) player.getTemporaryAttributes().remove("DuelTarget");
        if (target == null
                || target.getFinished()
                || !target.isWithinDistance(player, 14)
                || !(target.getControllerManager().getController() instanceof DuelController)) {
            player.message("Unable to find " + (target == null ? "your target" : target.getUsername()));
            return;
        }
        player.getTemporaryAttributes().put("DuelChallenged", target);
        player.getTemporaryAttributes().put("DuelFriendly", friendly);
        player.message("Sending " + target.getUsername() + " a request...");
        target.getPacketSender().sendDuelChallengeRequestMessage(player, friendly);
    }

    public void remove() {
//        player.getPacketSender().closeInterface(
//                player.getInterfaceManager().hasRezizableScreen() ? 10 : 8);
//        player.getAppearence().generateAppearenceData();
//        player.getPacketSender().sendPlayerOption("null", 1, false);
//        player.getPacketSender().sendInteractionOption("null", 2, false);

        player.send(new WalkableInterface(-1));
        if(player.getPlayerInteractingOption() != PlayerInteractingOption.NONE)
            player.getPacketSender().sendInteractionOption("null", 2, false);
    }

    @Override
    public void sendInterfaces() {
        if (isAtDuelArena(player)) {
//            player.getInterfaceManager().sendTab(
//                    player.getInterfaceManager().hasRezizableScreen() ? 10 : 8,
//                    638);
            if(player.getWalkableInterfaceId() != 201)
                player.send(new WalkableInterface(201));
        }
    }

    public static boolean isAtDuelArena(Position position) {
        return (position.getX() >= 3355 && position.getX() <= 3360 && position.getY() >= 3267 && position.getY() <= 3279)
                || (position.getX() >= 3355 && position.getX() <= 3379 && position.getY() >= 3272 && position.getY() <= 3279)
                || (position.getX() >= 3374 && position.getX() <= 3379 && position.getY() >= 3267 && position.getY() <= 3271)
                || (position.getX() >= 3355 && position.getX() <= 3373 && position.getY() >= 3262 && position.getY() <= 3271)
                || (position.getX() >= 3329 && position.getX() <= 3392 && position.getY() >= 3203 && position.getY() <= 3261)
                || (position.getX() >= 3361 && position.getX() <= 3373 && position.getY() >= 3262 && position.getY() <= 3271);
    }

}