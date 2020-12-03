package com.fury.game.content.controller.impl;

import com.fury.game.GameSettings;
import com.fury.game.content.controller.Controller;
import com.fury.game.content.skill.free.dungeoneering.DungeonManager;
import com.fury.game.world.map.Position;

public class Daemonheim extends Controller {

    private boolean showingOption;

    @Override
    public void start() {
        setInviteOption(true);
    }


    @Override
    public void magicTeleported(int type) {
        setInviteOption(false);
        player.getDungManager().leaveParty();
        removeController();
    }

    @Override
    public boolean login() {
        moved();
        DungeonManager.checkRejoin(player);
        return false;
    }

    @Override
    public boolean sendDeath() {
        setInviteOption(false);
        player.getDungManager().leaveParty();
        removeController();
        return true;
    }


    public boolean logout() {
        return false; // so doesnt remove script
    }

    @Override
    public void forceClose() {
        setInviteOption(false);
    }

    @Override
    public void moved() {
        if (player.getDungManager().isInside())
            return;
        if (player.getX() >= 3381 && player.getX() <= 3384 && player.getY() <= 3616 && player.getY() >= 3613) {
            setInviteOption(false);
            player.getDungManager().leaveParty();
            removeController();
            player.getControllerManager().startController(new Wilderness());
        } else {
            if (!isAtDaemonheim(player)) {
                setInviteOption(false);
                player.getDungManager().leaveParty();
                removeController();
            } else
                setInviteOption(true);
        }
    }

    public static boolean isAtDaemonheim(Position tile) {
        return tile.getX() >= 3385 && tile.getX() <= 3513 && tile.getY() >= 3605 && tile.getY() <= 3794;
    }

    public void setInviteOption(boolean show) {
        if (show == showingOption)
            return;
        showingOption = show;
        if(show) {
            if (!player.isDungeoneeringSpellbook())
                player.getDungManager().openPartyInterface();
        } else {
            if(player.getDungManager().getParty() == null || (player.getDungManager().getParty() != null && player.getDungManager().getParty().getDungeon() == null)) {
                player.getPacketSender().sendDungeoneeringTabIcon(false);
                player.getPacketSender().sendTabInterface(GameSettings.QUESTS_TAB, 639);
            }
        }
        player.getPacketSender().sendInteractionOption(show ? "Invite" : "null", 2, false);
    }
}
