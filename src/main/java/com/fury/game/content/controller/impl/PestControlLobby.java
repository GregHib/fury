package com.fury.game.content.controller.impl;

import com.fury.game.content.controller.Controller;
import com.fury.game.content.dialogue.impl.minigames.pest.LanderD;
import com.fury.game.content.global.minigames.impl.Lander;
import com.fury.game.entity.object.GameObject;
import com.fury.game.network.packet.out.WalkableInterface;
import com.fury.game.node.entity.actor.figure.player.Points;
import com.fury.util.Misc;

public class PestControlLobby extends Controller {

    private Lander lander;

    @Override
    public void start() {
        this.lander = Lander.getLanders()[(Integer) getArguments()[0]];
    }

    @Override
    public void sendInterfaces() {
        int remainingTime = lander.getTimer().getSeconds();
        player.getPacketSender().sendString(21006,"Next Departure: " + (remainingTime/60) + " minutes " + (remainingTime % 60) + " seconds");
        player.getPacketSender().sendString(21007,"Players Ready: " + lander.getByStanders().size());
        player.getPacketSender().sendString(21009,"Commendations: " + player.getPoints().get(Points.COMMENDATIONS));
        player.getPacketSender().sendString(21010, Misc.formatName(lander.toString()));
        if(player.getWalkableInterfaceId() == -1)
            player.send(new WalkableInterface(21005));
    }

    @Override
    public void magicTeleported(int teleType) {
        player.getControllerManager().forceStop();
    }

    @Override
    public boolean sendDeath() {
        return true;
    }

    @Override
    public void forceClose() {
        player.send(new WalkableInterface(-1));
        if(lander != null)
            lander.exit(player, false);
    }

    @Override
    public boolean logout() {
        lander.exit(player, true);
        return true;
    }

    @Override
    public boolean canSummonFamiliar() {
        player.message("You feel it's best to keep your Familiar away during this game.");
        return false;
    }

    @Override
    public boolean processObjectClick1(GameObject object) {
        switch (object.getId()) {
            case 14314:
            case 25629:
            case 25630:
                player.getDialogueManager().startDialogue(new LanderD());
                return true;
        }
        return true;
    }
}
