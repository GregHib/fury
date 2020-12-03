package com.fury.game.entity.character.player.link.transportation;

import com.fury.game.content.global.Achievements;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.world.map.Position;

public enum TeleportTabs {
    HOME(8013, new Position(3197, 3434)),
    WATCHTOWER(8012, new Position(2548, 3114)),
    ARDOGUNE(8011, new Position(2662, 3307)),
    CAMELOT(8010, new Position(2757, 3477)),
    FALADOR(8009, new Position(2965, 3379)),
    LUMBRIDGE(8008, new Position(3224, 3219)),
    VARROCK(8007, new Position(3213, 3424)),
    AIR(13599, new Position(2841, 4829)),
    MIND(13600, new Position(2793, 4828)),
    WATER(13601, new Position(3494, 4832)),
    EARTH(13602, new Position(2655, 4830)),
    FIRE(13603, new Position(2577, 4846)),
    BODY(13604, new Position(2521, 4834)),
    COSMIC(13605, new Position(2162, 4833)),
    CHAOS(13606, new Position(2281, 4837)),
    ASTRAL(13611, new Position(2153, 3864)),
    NATURE(13607, new Position(2400, 4835)),
    LAW(13608, new Position(2464, 4818)),
    DEATH(13609, new Position(2208, 4830)),
    BLOOD(13610, new Position(2468, 4889, 1));

    TeleportTabs(int tabItemId, Position destination) {
        this.tabItemId = tabItemId;
        this.destination = destination;
    }

    private Position destination;
    private int tabItemId;

    public Position getDestination() {
        return destination;
    }

    public int getItemId() {
        return tabItemId;
    }

    public static boolean teleportTabs(Player player, int itemId) {
        for (TeleportTabs t : TeleportTabs.values()) {
            if (t.getItemId() == itemId) {
                if (t == HOME) {
                    TeleportHandler.teleportPlayer(player, player.getHouse().getLocation().getTile(), TeleportType.TELE_TAB);
                } else {
                    Position destination = t.getDestination();
                    if (t == CAMELOT && Achievements.hasFinishedAll(player, Achievements.Difficulty.HARD))
                        destination = new Position(2725, 3491);
                    TeleportHandler.teleportPlayer(player, destination, TeleportType.TELE_TAB);
                    if (t == NATURE)
                        Achievements.finishAchievement(player, Achievements.AchievementData.TABLET_TELEPORT_TO_NATURE_ALTAR);
                }
                return true;
            }
        }
        return false;
    }
}
