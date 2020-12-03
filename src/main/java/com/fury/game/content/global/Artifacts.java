package com.fury.game.content.global;

import com.fury.cache.def.Loader;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.info.GameMode;
import com.fury.core.model.item.Item;
import com.fury.game.world.FloorItemManager;
import com.fury.game.world.GameWorld;
import com.fury.util.FontUtils;
import com.fury.util.Misc;

public class Artifacts {

    public static Item[] artifacts = {new Item(14876), new Item(14877), new Item(14878), new Item(14879), new Item(14880), new Item(14881), new Item(14882), new Item(14883), new Item(14884), new Item(14885), new Item(14886), new Item(14887), new Item(14888), new Item(14889), new Item(14890), new Item(14891), new Item(14892)};

    public static void sellArtifacts(Player player) {
        player.getPacketSender().sendInterfaceRemoval();
        boolean artifact = false;
        for (int k = 0; k < artifacts.length; k++) {
            if (player.getInventory().contains(artifacts[k])) {
                artifact = true;
            }
        }
        if (!artifact) {
            player.message("You do not have any Artifacts in your inventory to sell to Mandrith.");
            return;
        }
        for (int i = 0; i < artifacts.length; i++) {
            for (Item item : player.getInventory().getItems()) {
                if (item != null && item.isEqual(artifacts[i])) {
                    player.getInventory().delete(artifacts[i]);
                    player.getInventory().add(new Item(995, artifacts[i].getDefinitions().getValue()));
                    player.getInventory().refresh();
                }
            }
        }
        player.message("You've sold your artifacts.");

    }

    /*
     * Artifacts
     */
    private final static int[] LOW_ARTIFACTS = {14888, 14889, 14890, 14891, 14892};
    private final static int[] MED_ARTIFACTS = {14883, 14884, 14885, 14886};
    private final static int[] HIGH_ARTIFACTS = {14878, 14879, 14880, 14881, 14882};
    private final static int[] EXR_ARTIFACTS = {14876, 14877};
    private final static int[] PVP_ARMORS = {13899, 13893, 13887, 13902, 13896, 13890, 13858, 13861};

    /**
     * Handles a target drop
     */
    public static void handleDrops(Player killer, Player death, boolean targetKill) {
        if (killer.getGameMode() != GameMode.REGULAR)
            return;
        if (Misc.getRandom(100) >= 85 || targetKill)
            FloorItemManager.addGroundItem(new Item(getRandomItem(LOW_ARTIFACTS)), death.copyPosition(), killer);
        if (Misc.getRandom(100) >= 90)
            FloorItemManager.addGroundItem(new Item(getRandomItem(MED_ARTIFACTS)), death.copyPosition(), killer);
        if (Misc.getRandom(100) >= 97)
            FloorItemManager.addGroundItem(new Item(getRandomItem(HIGH_ARTIFACTS)), death.copyPosition(), killer);
        if (Misc.getRandom(100) >= 99)
            FloorItemManager.addGroundItem(new Item(getRandomItem(PVP_ARMORS)), death.copyPosition(), killer);
        if (Misc.getRandom(100) >= 99) {
            int rareDrop = getRandomItem(EXR_ARTIFACTS);
            String itemName = Misc.formatText(Loader.getItem(rareDrop).getName());
            FloorItemManager.addGroundItem(new Item(rareDrop), death.copyPosition(), killer);
            GameWorld.sendBroadcast(FontUtils.imageTags(535) + " " + killer.getUsername() + " has just received " + Misc.anOrA(itemName) + " " + itemName + " from Bounty Hunter!", 0x009966);
        }
    }

    /**
     * Get's a random int from the array specified
     *
     * @param array The array specified
     * @return The random integer
     */
    public static int getRandomItem(int[] array) {
        return array[Misc.getRandom(array.length - 1)];
    }

}
