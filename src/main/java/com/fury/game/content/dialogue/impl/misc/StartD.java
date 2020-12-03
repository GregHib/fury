package com.fury.game.content.dialogue.impl.misc;

import com.fury.game.GameSettings;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.dialogue.impl.npcs.IronManZoneD;
import com.fury.game.entity.character.player.info.GameMode;
import com.fury.core.model.item.Item;
import com.fury.game.system.files.world.single.impl.Starters;
import com.fury.network.security.ConnectionHandler;

public class StartD extends Dialogue {
    @Override
    public void start() {
        sendNpc(949, "Here are some items to get you started...");
        player.getMovement().lock();
    }

    @Override
    public void run(int optionId) {
        if(stage == -1) {
            sendStatement("The sage hands you a collection of items.");
            if (ConnectionHandler.getStarters(player.getLogger().getHardwareId()) <= GameSettings.MAX_STARTERS_PER_IP) {
                if (player.getGameMode() == GameMode.IRONMAN) {
                    player.getInventory().add(new Item(15707), new Item(995, 100000), new Item(841, 1), new Item(882, 25), new Item(1321, 1), new Item(2552, 1), new Item(556, 100), new Item(557, 30), new Item(555, 50), new Item(558, 25), new Item(554, 20), new Item(562, 10), new Item(8007, 5), new Item(2310, 5), new Item(11261, 5), new Item(303, 1), new Item(1267, 1), new Item(1349, 1), new Item(590, 1), new Item(15017, 1));
                } else {
                    player.getInventory().add(new Item(15707), new Item(995, 500000), new Item(841), new Item(884, 250), new Item(861), new Item(1323), new Item(1333), new Item(3105), new Item(1704), new Item(11118), new Item(556, 1000), new Item(557, 500), new Item(555, 1000), new Item(558, 250), new Item(554, 500), new Item(562, 50), new Item(8007, 10), new Item(386, 100));
                }
                Starters.get().record(player.getLogger().getHardwareId());
                player.setReceivedStarter(true);
            } else {
                player.message("You have already received all of your allocated account starters.");
            }
            stage = 0;
        } else if(stage == 0) {
            if(player.getGameMode().isIronMan() && player.newPlayer()) {
                end();
                player.getDialogueManager().startDialogue(new IronManZoneD());
                player.message("This is the exclusive ironman zone. You can teleport here by...");
                player.message("using your magic whistle or talking with Stark at home.");
            } else
                end();
        }
    }

    @Override
    public void finish() {
        player.setNewPlayer(false);
        player.setConverted(true);
        player.getMovement().unlock();
    }
}
