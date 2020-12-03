package com.fury.game.content.global.events.christmas;

import com.fury.cache.Revision;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.content.controller.Controller;
import com.fury.game.entity.character.player.link.transportation.TeleportHandler;
import com.fury.game.entity.character.player.link.transportation.TeleportType;
import com.fury.core.model.item.Item;
import com.fury.game.entity.object.GameObject;
import com.fury.game.node.entity.actor.object.ObjectHandler;
import com.fury.game.system.files.world.increment.timer.impl.DailySnowflake;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;
import com.fury.game.world.map.instance.MapInstance;

public class ChristmasEvent extends Controller {

    public static boolean active = false;
    private static MapInstance instance = new MapInstance(328, 705);
    private static final Position OUTSIDE = new Position(3085, 3499);

    @Override
    public void start() {
    }

    public static void enter(Player player) {
        if (player.getChristmasCharactersFound() == ChristmasPartyCharacters.values().length) {
            instance.load(() -> {
                player.graphic(1285);
                ObjectHandler.useStairs(player, 7534, instance.getTile(22, 19), 4, 4);
            });
        } else {
            player.graphic(1285);
            ObjectHandler.useStairs(player, 7534, new Position(2646, 5659), 4, 4);
        }
        player.getControllerManager().startController(new ChristmasEvent());
    }

    public void leave(boolean move) {
        if(move)
            player.moveTo(OUTSIDE);
        removeController();
    }

    @Override
    public boolean processObjectClick1(GameObject object) {
        switch (object.getId()) {
            case 47766://Christmas cupboard exit
                TeleportHandler.teleportPlayer(player, OUTSIDE, TeleportType.SNOW_TELEPORT);
                leave(false);
                return false;
            case 28296://Snow
                player.animate(7529);
                player.getInventory().add(new Item(11951, player.getInventory().getSpaces()));
                return false;
            default:
                return true;
        }
    }


    @Override
    public boolean sendDeath() {
        leave(false);
        return true;
    }

    @Override
    public boolean logout() {
        leave(true);
        return true;
    }

    @Override
    public void magicTeleported(int type) {
        leave(false);
    }

    public static void init() {
        for (ChristmasPartyCharacters character : ChristmasPartyCharacters.values())
            GameWorld.getMobs().spawn(character.getId(), character.getPosition());

        instance.load(() -> GameWorld.getMobs().spawn(8540, instance.getTile(30, 38)));
        instance.load(() -> GameWorld.getMobs().spawn(6744, instance.getTile(22, 26)));
        instance.load(() -> GameWorld.getMobs().spawn(6745, instance.getTile(30, 23)));
        instance.load(() -> GameWorld.getMobs().spawn(6746, instance.getTile(40, 20)));
        instance.load(() -> GameWorld.getMobs().spawn(9393, instance.getTile(40, 26)));
        instance.load(() -> GameWorld.getMobs().spawn(6743, instance.getTile(40, 10)));
        instance.load(() -> GameWorld.getMobs().spawn(9387, instance.getTile(31, 20)));
        instance.load(() -> GameWorld.getMobs().spawn(9384, instance.getTile(25, 20)));
        instance.load(() -> GameWorld.getMobs().spawn(6742, instance.getTile(20, 10)));


        instance.load(() -> GameWorld.getMobs().spawn(14209, Revision.PRE_RS3, instance.getTile(50, 20), true));
        instance.load(() -> GameWorld.getMobs().spawn(14208, Revision.PRE_RS3, instance.getTile(45, 18), true));
        instance.load(() -> GameWorld.getMobs().spawn(14208, Revision.PRE_RS3, instance.getTile(45, 24), true));
        instance.load(() -> GameWorld.getMobs().spawn(14207, Revision.PRE_RS3, instance.getTile(48, 28), true));
        instance.load(() -> GameWorld.getMobs().spawn(14209, Revision.PRE_RS3, instance.getTile(48, 16), true));
        instance.load(() -> GameWorld.getMobs().spawn(14207, Revision.PRE_RS3, instance.getTile(45, 14), true));
        instance.load(() -> GameWorld.getMobs().spawn(14208, Revision.PRE_RS3, instance.getTile(48, 13), true));
    }

    public static void giveSnowflake(Player player) {
        if (active && DailySnowflake.get().get(player.getLogger().getHardwareId()) < 750) {
            DailySnowflake.get().record(player.getLogger().getHardwareId());
            Item snowflake = new Item(33596, Revision.RS3);

            //Add inv/bank
            if(!player.getInventory().add(snowflake))
                if(!player.getBank().tab().add(snowflake))
                    player.getInventory().addSafe(snowflake);
        }
    }
}
