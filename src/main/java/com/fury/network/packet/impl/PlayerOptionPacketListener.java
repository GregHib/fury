
package com.fury.network.packet.impl;

import com.fury.core.model.node.entity.actor.figure.combat.magic.CombatSpell;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.content.controller.impl.FirstAdventureController;
import com.fury.game.entity.character.player.actions.PlayerCombatAction;
import com.fury.game.entity.character.player.actions.PlayerFollow;
import com.fury.game.entity.character.player.actions.Snowball;
import com.fury.game.node.entity.actor.figure.player.PlayerCombat;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.path.RouteEvent;
import com.fury.network.packet.Packet;
import com.fury.network.packet.PacketListener;
import com.fury.util.Utils;

/**
 * This packet listener is called when a player has clicked on another player's
 * menu actions.
 *
 * @author relex lawl
 */

public class PlayerOptionPacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        if (player.getHealth().getHitpoints() <= 0 || player.getMovement().getTeleporting() || player.getControllerManager().getController() instanceof FirstAdventureController)
            return;

        int index = packet.readShort();
        int type = packet.readByte();

        if (index < 0 || index > GameWorld.getPlayers().capacity())
            return;

        Player target = GameWorld.getPlayers().get(index);
        if (target == null)
            return;

        switch (type) {
            case 0:
                handleFirstOption(player, target);
                break;
            case 1:
                handleSecondOption(player, target);
                break;
            case 2:
                handleThirdOption(player, target);
                break;
            case 3:
                handleFourthOption(player, target);
                break;
            case 4:
                handleFifthOption(player, target);
                break;
        }
    }

    private void handleFirstOption(Player player, Player target) {

    }

    private void handleSecondOption(Player player, Player target) {
        switch (player.getPlayerInteractingOption()) {
            case NONE:
                break;
            case CHALLENGE:
                player.setRouteEvent(new RouteEvent(target, () -> attack(player, target)));
                break;
            case INVITE:
                player.stopAll(false);
                player.setRouteEvent(new RouteEvent(target, () -> {
                    player.getPacketSender().sendInterfaceRemoval();
                    player.getDungManager().invite(target);
                }));
                break;
            case ATTACK:
                attack(player, target);
                break;
            case PELT:
                Snowball.pelt(player, target);
                break;
        }
    }

    private void handleThirdOption(Player player, Player target) {
        follow(player, target);
    }

    private void handleFourthOption(Player player, Player target) {
        if (player.getGameMode().isIronMan()) {
            player.message("You cannot trade other players because you are an Ironman");
            return;
        }

        if (!player.getControllerManager().canTrade()) {
            player.message("You cannot trade other players here.");
            return;
        }

        if (!player.isWithinDistance(target, 13))
            return;

        player.stopAll(false);
        player.setRouteEvent(new RouteEvent(target, () -> {
            if (target.getIndex() != player.getIndex())
                player.getTrade().requestTrade(target);
        }));
    }

    private void handleFifthOption(Player player, Player target) {

    }

    private void follow(Player player, Player leader) {
        if (leader.getHealth().getHitpoints() <= 0 || player.getHealth().getHitpoints() <= 0) {
            player.message("You cannot follow other players right now.");
            return;
        }
        player.getDirection().setInteracting(leader);
        player.getActionManager().setAction(new PlayerFollow(leader));
    }

    private void attack(Player player, Player target) {
        if(target == null || target.isDead() || target.getFinished() || !GameWorld.getRegions().getLocalPlayers(player).contains(target))
            return;

        if(!player.getControllerManager().canPlayerOption1(target))
            return;

        if (player.getStopAttackDelay() > Utils.currentTimeMillis())
            return;

        if (!player.isCanPvp())
            return;

        if (!player.getControllerManager().canAttack(target))
            return;

        if (!player.isCanPvp() || !target.isCanPvp()) {
            player.message("You can only attack players in a player-vs-player area.");
            return;
        }

        if (target.getCombat().getAttackedBy() != player && target.getCombat().getAttackedByDelay() > Utils.currentTimeMillis()) {
            if (target.getCombat().getAttackedBy() != null && target.getCombat().getAttackedBy().isNpc()) {
                target.getCombat().setAttackedBy(player); // changes enemy to player,  player has priority over  npc on single areas
            } else {
                player.message("That player is already in combat.");
                return;
            }
        }

        player.stopAll(false);
        if(player.getCombat() instanceof PlayerCombat)
            ((PlayerCombat) player.getCombat()).target(target);

        int rangeType = PlayerCombatAction.isRanging(player);
        CombatSpell spell = player.getCastSpell();
        CombatSpell autoSpell = player.getAutoCastSpell();
        boolean ranged = rangeType != 0 || spell != null || autoSpell != null;//Ranged as in from a distance not Ranged combat skill
        int maxDistance = ranged ? 7 : 0;
        boolean lineOfSight = player.getCombat().clippedProjectile(target, maxDistance == 0);

        player.getDirection().setInteracting(target);
        if(ranged && lineOfSight) {
            if(player.getCombat() instanceof PlayerCombat)
                ((PlayerCombat) player.getCombat()).target(target);
        } else {
            player.setRouteEvent(new RouteEvent(target,() -> {
                if(player.getCombat() instanceof PlayerCombat)
                    ((PlayerCombat) player.getCombat()).target(target);
            }, ranged && !lineOfSight));
        }
    }
}
