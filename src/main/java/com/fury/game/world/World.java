package com.fury.game.world;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.task.Task;
import com.fury.game.GameSettings;
import com.fury.game.content.controller.impl.JadinkoLair;
import com.fury.game.content.eco.ge.GrandExchangeOffers;
import com.fury.game.content.global.LivingRockCavern;
import com.fury.game.content.global.dnd.DistractionAndDiversions;
import com.fury.game.content.global.dnd.eviltree.EvilTree;
import com.fury.game.content.global.dnd.star.ShootingStar;
import com.fury.game.content.global.thievingguild.ThievingGuild;
import com.fury.game.entity.character.player.info.DonorStatus;
import com.fury.game.entity.object.GameObject;
import com.fury.game.node.entity.actor.figure.player.Points;
import com.fury.game.system.communication.clans.ClanChatManager;
import com.fury.game.system.files.logs.PlayerLogs;
import com.fury.game.system.files.world.WorldFileHandler;
import com.fury.game.system.files.world.increment.timer.impl.DailyDonor;
import com.fury.game.system.mysql.impl.Highscores;
import com.fury.game.world.map.Position;
import com.fury.game.world.map.build.MapBuilder;
import com.fury.game.world.map.clip.Flags;
import com.fury.game.world.map.region.Region;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.util.Colours;
import com.fury.util.FontUtils;
import com.fury.util.Misc;
import com.fury.util.Utils;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Optional;

/**
 * @author Gabriel Hannason
 * Thanks to lare96 for help with parallel updating system
 */
public class World {


    public static int updateTime = -1;

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public static Player getPlayerByName(String username) {
        String user = Misc.formatText(username);
        return GameWorld.getPlayers().stream().filter(p -> p != null && p.getUsername().equals(user)).findFirst().orElse(null);
    }

    public static Player getPlayerByLong(long encodedName) {
        Optional<Player> op = GameWorld.getPlayers().search(p -> p != null && p.getLongUsername().equals(encodedName));
        return op.orElse(null);
    }

    public static void updateServerTime(String time) {
        GameWorld.getPlayers().forEach(p -> p.getPacketSender().sendString(39161, "Server time: " + FontUtils.YELLOW + time + FontUtils.COL_END, Colours.ORANGE_2));
    }

    public static void updatePlayersOnline() {
        GameWorld.getPlayers().forEach(p -> {
            p.getPacketSender().sendString(39160, "Players online:  " + (GameWorld.getPlayers().size() > 20 ? FontUtils.GREEN : GameWorld.getPlayers().size() < 10 ? FontUtils.WHITE : FontUtils.YELLOW) + (GameWorld.getPlayers().size() > 0 ? (Math.round(GameWorld.getPlayers().size() * 1.2) + 5) : GameWorld.getPlayers().size()) + FontUtils.COL_END, Colours.ORANGE_2);
            p.getReferAFriend().refreshOnline();
        });
    }

    public static void updateHighscores() {
        GameWorld.getPlayers().forEach(Highscores::save);
    }

    public static void updateOnlineCount() {
        try {
            URLConnection url = new URL(GameSettings.WEBSITE + "/updatepc.php?p=LCwrRkRxLV(!)_:&c=" + GameWorld.getPlayers().size()).openConnection();
            url.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            url.connect();
            InputStream in = url.getInputStream();
            if (!Misc.convertStreamToString(in).equalsIgnoreCase("updated."))
                System.err.println("Error updating player count.");
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void checkDonorPoints() {
        GameWorld.getPlayers().forEach(player -> {
            if (DonorStatus.isDonor(player, DonorStatus.SAPPHIRE_DONOR)) {
                if (player.getTimers().getLogin().elapsed() > 300000) {//5 minutes
                    if (!DailyDonor.get().has(player.getUsername())) {//Once
                        player.message("You've received your daily " + DonorStatus.get(player).getDailyPoints() + " donor points!", Colours.ORANGE_3);
                        player.getPoints().add(Points.DONOR, DonorStatus.get(player).getDailyPoints());
                        DailyDonor.get().record(player.getUsername());
                        PlayerLogs.log(player.getUsername(), "Daily " + DonorStatus.get(player).getDailyPoints() + " donor points claimed.");
                    }
                }
            }
        });
    }

    public static void saveAll(boolean savePlayers) {
        WorldFileHandler.save();
        GrandExchangeOffers.save();
        ClanChatManager.save();
        if (savePlayers)
            GameWorld.getPlayers().forEach(Player::save);
    }

    public static void setUpdateTime(int updateTime) {
        World.updateTime = updateTime;
    }

    public static int getUpdateTime() {
        return updateTime;
    }

    public static boolean isUpdating() {
        return updateTime >= 0 && updateTime <= 60;
    }

    public static void executeAfterLoadRegion(final int regionId, final Runnable event) {
        executeAfterLoadRegion(regionId, 600, event);
    }

    public static void executeAfterLoadRegion(final int regionId, long startTime, final Runnable event) {
        executeAfterLoadRegion(regionId, startTime, 10000, event);
    }

    static void executeAfterLoadRegion(final int regionId, long startTime, final long expireTime, final Runnable event) {
        final long start = Utils.currentTimeMillis();
        GameWorld.getRegions().load(regionId);//force check load if not loaded
        GameWorld.schedule(new Task(false, 2) {
            @Override
            public void run() {
                Region region = GameWorld.getRegions().getActiveRegions().get(regionId);

                if ((region == null || region.getLoadMapStage() != 2) && Utils.currentTimeMillis() - start < expireTime)
                    return;

                event.run();
                stop();
            }
        });
    }

    public static void updateEntityRegion(Figure figure) {
        if (figure.getFinished()) {
            figure.getRegion().removeEntity(figure);
            return;
        }
        int regionId = figure.getRegionId();
        int lastRegionId = figure.getLastRegionId();
        if (lastRegionId != regionId) {
            if (lastRegionId > 0)
                GameWorld.getRegions().get(lastRegionId).removeEntity(figure);
            Region region = GameWorld.getRegions().get(regionId);
            region.addEntity(figure);
            figure.setLastRegionId(regionId);
        }

        if (figure.isPlayer())
            ((Player) figure).getControllerManager().moved();

        figure.checkMulti();
    }


    public static boolean hasGroundItem(int id, Player owner) {
        return owner.getRegion().getFloorItem(id, owner) != null;
    }

    public static boolean isTileFree(int x, int y, int plane, int size) {
        for (int tileX = x; tileX < x + size; tileX++)
            for (int tileY = y; tileY < y + size; tileY++)
                if (!isFloorFree(tileX, tileY, plane) || !isWallsFree(tileX, tileY, plane))
                    return false;
        return true;
    }

    public static boolean isTileFree(int x, int y, int plane, int sizeX, int sizeY) {
        for (int tileX = x; tileX < x + sizeX; tileX++)
            for (int tileY = y; tileY < y + sizeY; tileY++)
                if (!isFloorFree(tileX, tileY, plane) || !isWallsFree(tileX, tileY, plane))
                    return false;
        return true;
    }

    public static boolean isFloorFree(int x, int y, int plane, int size) {
        for (int tileX = x; tileX < x + size; tileX++)
            for (int tileY = y; tileY < y + size; tileY++)
                if (!isFloorFree(tileX, tileY, plane))
                    return false;
        return true;
    }

    public static boolean isFloorFree(int x, int y, int plane) {
        return (getMask(x, y, plane) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ)) == 0;
    }

    public static boolean isWallsFree(int x, int y, int plane) {
        return (getMask(x, y, plane) & (Flags.CORNEROBJ_NORTHEAST | Flags.CORNEROBJ_NORTHWEST | Flags.CORNEROBJ_SOUTHEAST | Flags.CORNEROBJ_SOUTHWEST | Flags.WALLOBJ_EAST | Flags.WALLOBJ_NORTH | Flags.WALLOBJ_SOUTH | Flags.WALLOBJ_WEST)) == 0;
    }

    public static int getMask(int x, int y, int plane) {
        Position tile = new Position(x, y, plane);
        Region region = GameWorld.getRegions().get(tile.getRegionId());
        if (region == null)
            return -1;
        return region.getMask(tile.getXInRegion(), tile.getYInRegion(), tile.getZ());
    }

    public static int getClippedOnlyMask(int x, int y, int plane) {
        Position tile = new Position(x, y, plane);
        Region region = GameWorld.getRegions().get(tile.getRegionId());
        if (region == null)
            return -1;
        return region.getMaskClippedOnly(tile.getXInRegion(), tile.getYInRegion(), tile.getZ());
    }

    public static boolean canMove(int startX, int startY, int endX, int endY, int height, int xLength, int yLength) {
        int diffX = endX - startX;
        int diffY = endY - startY;
        int max = Math.max(Math.abs(diffX), Math.abs(diffY));
        for (int ii = 0; ii < max; ii++) {
            int currentX = endX - diffX;
            int currentY = endY - diffY;
            for (int i = 0; i < xLength; i++) {
                for (int i2 = 0; i2 < yLength; i2++)
                    if (diffX < 0 && diffY < 0) {
                        if ((World.getMask((currentX + i) - 1,
                                (currentY + i2) - 1, height) & 0x128010e) != 0
                                || (World.getMask((currentX + i) - 1, currentY
                                + i2, height) & 0x1280108) != 0
                                || (World.getMask(currentX + i,
                                (currentY + i2) - 1, height) & 0x1280102) != 0)
                            return false;
                    } else if (diffX > 0 && diffY > 0) {
                        if ((World.getMask(currentX + i + 1, currentY + i2 + 1,
                                height) & 0x12801e0) != 0
                                || (World.getMask(currentX + i + 1,
                                currentY + i2, height) & 0x1280180) != 0
                                || (World.getMask(currentX + i,
                                currentY + i2 + 1, height) & 0x1280120) != 0)
                            return false;
                    } else if (diffX < 0 && diffY > 0) {
                        if ((World.getMask((currentX + i) - 1, currentY + i2 + 1,
                                height) & 0x1280138) != 0
                                || (World.getMask((currentX + i) - 1, currentY
                                + i2, height) & 0x1280108) != 0
                                || (World.getMask(currentX + i,
                                currentY + i2 + 1, height) & 0x1280120) != 0)
                            return false;
                    } else if (diffX > 0 && diffY < 0) {
                        if ((World.getMask(currentX + i + 1, (currentY + i2) - 1,
                                height) & 0x1280183) != 0
                                || (World.getMask(currentX + i + 1,
                                currentY + i2, height) & 0x1280180) != 0
                                || (World.getMask(currentX + i,
                                (currentY + i2) - 1, height) & 0x1280102) != 0)
                            return false;
                    } else if (diffX > 0 && diffY == 0) {
                        if ((World.getMask(currentX + i + 1, currentY + i2,
                                height) & 0x1280180) != 0)
                            return false;
                    } else if (diffX < 0 && diffY == 0) {
                        if ((World.getMask((currentX + i) - 1, currentY + i2,
                                height) & 0x1280108) != 0)
                            return false;
                    } else if (diffX == 0 && diffY > 0) {
                        if ((World.getMask(currentX + i, currentY + i2 + 1,
                                height) & 0x1280120) != 0)
                            return false;
                    } else if (diffX == 0
                            && diffY < 0
                            && (World.getMask(currentX + i, (currentY + i2) - 1,
                            height) & 0x1280102) != 0)
                        return false;

            }

            if (diffX < 0)
                diffX++;
            else if (diffX > 0)
                diffX--;
            if (diffY < 0)
                diffY++;
            else if (diffY > 0)
                diffY--;
        }

        return true;
    }

    public static final void sendObjectAnimation(GameObject object, Animation animation) {
        sendObjectAnimation(null, object, animation);
    }

    public static final void sendObjectAnimation(Player creator, GameObject object, Animation animation) {
        if (creator == null) {
            for (Player player : GameWorld.getPlayers()) {
                if (player == null || !player.hasStarted() || player.getFinished() || !object.isViewableFrom(player))
                    continue;
                player.getPacketSender().sendObjectAnimation(object, animation);
            }
        } else {
            creator.getPacketSender().sendObjectAnimation(object, animation);
            for (Player player : GameWorld.getRegions().getLocalPlayers(creator)) {
                if (player == null || !player.hasStarted() || player.getFinished() || !object.isViewableFrom(player))
                    continue;
                player.getPacketSender().sendObjectAnimation(object, animation);
            }
        }
    }

    public static boolean canMove(Position start, Position end, int size) {
        return canMove(start.getX(), start.getY(), end.getX(), end.getY(), start.getZ(), size, size);
    }

    public static boolean canMove(Position start, Position end, int sizeX, int sizeY) {
        return canMove(start.getX(), start.getY(), end.getX(), end.getY(), start.getZ(), sizeX, sizeY);
    }

    public static boolean blockedProjectile(Position position) {
        return (World.getMask(position.getX(), position.getY(), position.getZ()) & 0x20000) == 0;
    }

    public static boolean blocked(Position pos) {
        return (getMask(pos.getX(), pos.getY(), pos.getZ()) & 0x1280120) != 0;
    }

    public static boolean blockedNorth(Position pos) {
        return (getMask(pos.getX(), pos.getY() + 1, pos.getZ()) & 0x1280120) != 0;
    }

    public static boolean blockedEast(Position pos) {
        return (getMask(pos.getX() + 1, pos.getY(), pos.getZ()) & 0x1280180) != 0;
    }

    public static boolean blockedSouth(Position pos) {
        return (getMask(pos.getX(), pos.getY() - 1, pos.getZ()) & 0x1280102) != 0;
    }

    public static boolean blockedWest(Position pos) {
        return (getMask(pos.getX() - 1, pos.getY(), pos.getZ()) & 0x1280108) != 0;
    }

    public static boolean blockedNorthEast(Position pos) {
        return (getMask(pos.getX() + 1, pos.getY() + 1, pos.getZ()) & 0x12801e0) != 0;
    }

    public static boolean blockedNorthWest(Position pos) {
        return (getMask(pos.getX() - 1, pos.getY() + 1, pos.getZ()) & 0x1280138) != 0;
    }

    public static boolean blockedSouthEast(Position pos) {
        return (getMask(pos.getX() + 1, pos.getY() - 1, pos.getZ()) & 0x1280183) != 0;
    }

    public static boolean blockedSouthWest(Position pos) {
        return (getMask(pos.getX() - 1, pos.getY() - 1, pos.getZ()) & 0x128010e) != 0;
    }

    public static boolean canProjectileAttack(Figure a, Figure b) {
        if (!a.isPlayer()) {
            if (b.isPlayer()) {
                return canProjectileMove(b.getX(), b
                        .getY(), a.getX(), a
                        .getY(), a.getZ(), 1, 1);
            }
        }
        return canProjectileMove(a.getX(),
                a.getY(), b.getX(), b
                        .getY(), a.getZ(), 1, 1);
    }

    public static final boolean checkProjectileStep(int x, int y, int plane, int dir, int size) {
        int xOffset = Misc.DIRECTION_DELTA_X[dir];
        int yOffset = Misc.DIRECTION_DELTA_Y[dir];

        if (size == 1) {
            int mask = getClippedOnlyMask(x + Misc.DIRECTION_DELTA_X[dir], y + Misc.DIRECTION_DELTA_Y[dir], plane);
            if (xOffset == -1 && yOffset == 0)
                return (mask & 0x42240000) == 0;
            if (xOffset == 1 && yOffset == 0)
                return (mask & 0x60240000) == 0;
            if (xOffset == 0 && yOffset == -1)
                return (mask & 0x40a40000) == 0;
            if (xOffset == 0 && yOffset == 1)
                return (mask & 0x48240000) == 0;
            if (xOffset == -1 && yOffset == -1) {
                return (mask & 0x43a40000) == 0 && (getClippedOnlyMask(x - 1, y, plane) & 0x42240000) == 0 && (getClippedOnlyMask(x, y - 1, plane) & 0x40a40000) == 0;
            }
            if (xOffset == 1 && yOffset == -1) {
                return (mask & 0x60e40000) == 0 && (getClippedOnlyMask(x + 1, y, plane) & 0x60240000) == 0 && (getClippedOnlyMask(x, y - 1, plane) & 0x40a40000) == 0;
            }
            if (xOffset == -1 && yOffset == 1) {
                return (mask & 0x4e240000) == 0 && (getClippedOnlyMask(x - 1, y, plane) & 0x42240000) == 0 && (getClippedOnlyMask(x, y + 1, plane) & 0x48240000) == 0;
            }
            if (xOffset == 1 && yOffset == 1) {
                return (mask & 0x78240000) == 0 && (getClippedOnlyMask(x + 1, y, plane) & 0x60240000) == 0 && (getClippedOnlyMask(x, y + 1, plane) & 0x48240000) == 0;
            }
        } else if (size == 2) {
            if (xOffset == -1 && yOffset == 0)
                return (getClippedOnlyMask(x - 1, y, plane) & 0x43a40000) == 0 && (getClippedOnlyMask(x - 1, y + 1, plane) & 0x4e240000) == 0;
            if (xOffset == 1 && yOffset == 0)
                return (getClippedOnlyMask(x + 2, y, plane) & 0x60e40000) == 0 && (getClippedOnlyMask(x + 2, y + 1, plane) & 0x78240000) == 0;
            if (xOffset == 0 && yOffset == -1)
                return (getClippedOnlyMask(x, y - 1, plane) & 0x43a40000) == 0 && (getClippedOnlyMask(x + 1, y - 1, plane) & 0x60e40000) == 0;
            if (xOffset == 0 && yOffset == 1)
                return (getClippedOnlyMask(x, y + 2, plane) & 0x4e240000) == 0 && (getClippedOnlyMask(x + 1, y + 2, plane) & 0x78240000) == 0;
            if (xOffset == -1 && yOffset == -1)
                return (getClippedOnlyMask(x - 1, y, plane) & 0x4fa40000) == 0 && (getClippedOnlyMask(x - 1, y - 1, plane) & 0x43a40000) == 0 && (getClippedOnlyMask(x, y - 1, plane) & 0x63e40000) == 0;
            if (xOffset == 1 && yOffset == -1)
                return (getClippedOnlyMask(x + 1, y - 1, plane) & 0x63e40000) == 0 && (getClippedOnlyMask(x + 2, y - 1, plane) & 0x60e40000) == 0 && (getClippedOnlyMask(x + 2, y, plane) & 0x78e40000) == 0;
            if (xOffset == -1 && yOffset == 1)
                return (getClippedOnlyMask(x - 1, y + 1, plane) & 0x4fa40000) == 0 && (getClippedOnlyMask(x - 1, y + 1, plane) & 0x4e240000) == 0 && (getClippedOnlyMask(x, y + 2, plane) & 0x7e240000) == 0;
            if (xOffset == 1 && yOffset == 1)
                return (getClippedOnlyMask(x + 1, y + 2, plane) & 0x7e240000) == 0 && (getClippedOnlyMask(x + 2, y + 2, plane) & 0x78240000) == 0 && (getClippedOnlyMask(x + 1, y + 1, plane) & 0x78e40000) == 0;
        } else {
            if (xOffset == -1 && yOffset == 0) {
                if ((getClippedOnlyMask(x - 1, y, plane) & 0x43a40000) != 0 || (getClippedOnlyMask(x - 1, -1 + (y + size), plane) & 0x4e240000) != 0)
                    return false;
                for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++)
                    if ((getClippedOnlyMask(x - 1, y + sizeOffset, plane) & 0x4fa40000) != 0)
                        return false;
            } else if (xOffset == 1 && yOffset == 0) {
                if ((getClippedOnlyMask(x + size, y, plane) & 0x60e40000) != 0 || (getClippedOnlyMask(x + size, y - (-size + 1), plane) & 0x78240000) != 0)
                    return false;
                for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++)
                    if ((getClippedOnlyMask(x + size, y + sizeOffset, plane) & 0x78e40000) != 0)
                        return false;
            } else if (xOffset == 0 && yOffset == -1) {
                if ((getClippedOnlyMask(x, y - 1, plane) & 0x43a40000) != 0 || (getClippedOnlyMask(x + size - 1, y - 1, plane) & 0x60e40000) != 0)
                    return false;
                for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++)
                    if ((getClippedOnlyMask(plane, x + sizeOffset, y - 1) & 0x63e40000) != 0)
                        return false;
            } else if (xOffset == 0 && yOffset == 1) {
                if ((getClippedOnlyMask(x, y + size, plane) & 0x4e240000) != 0 || (getClippedOnlyMask(x + (size - 1), y + size, plane) & 0x78240000) != 0)
                    return false;
                for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++)
                    if ((getClippedOnlyMask(x + sizeOffset, y + size, plane) & 0x7e240000) != 0)
                        return false;
            } else if (xOffset == -1 && yOffset == -1) {
                if ((getClippedOnlyMask(x - 1, y - 1, plane) & 0x43a40000) != 0)
                    return false;
                for (int sizeOffset = 1; sizeOffset < size; sizeOffset++)
                    if ((getClippedOnlyMask(x - 1, y + (-1 + sizeOffset), plane) & 0x4fa40000) != 0 || (getClippedOnlyMask(sizeOffset - 1 + x, y - 1, plane) & 0x63e40000) != 0)
                        return false;
            } else if (xOffset == 1 && yOffset == -1) {
                if ((getClippedOnlyMask(x + size, y - 1, plane) & 0x60e40000) != 0)
                    return false;
                for (int sizeOffset = 1; sizeOffset < size; sizeOffset++)
                    if ((getClippedOnlyMask(x + size, sizeOffset + (-1 + y), plane) & 0x78e40000) != 0 || (getClippedOnlyMask(x + sizeOffset, y - 1, plane) & 0x63e40000) != 0)
                        return false;
            } else if (xOffset == -1 && yOffset == 1) {
                if ((getClippedOnlyMask(x - 1, y + size, plane) & 0x4e240000) != 0)
                    return false;
                for (int sizeOffset = 1; sizeOffset < size; sizeOffset++)
                    if ((getClippedOnlyMask(x - 1, y + sizeOffset, plane) & 0x4fa40000) != 0 || (getClippedOnlyMask(-1 + (x + sizeOffset), y + size, plane) & 0x7e240000) != 0)
                        return false;
            } else if (xOffset == 1 && yOffset == 1) {
                if ((getClippedOnlyMask(x + size, y + size, plane) & 0x78240000) != 0)
                    return false;
                for (int sizeOffset = 1; sizeOffset < size; sizeOffset++)
                    if ((getClippedOnlyMask(x + sizeOffset, y + size, plane) & 0x7e240000) != 0 || (getClippedOnlyMask(x + size, y + sizeOffset, plane) & 0x78e40000) != 0)
                        return false;
            }
        }
        return true;
    }

    public static final boolean checkProjectileStep(int x, int y, int plane, int dir, int sizeX, int sizeY) {
        int xOffset = Misc.DIRECTION_DELTA_X[dir];
        int yOffset = Misc.DIRECTION_DELTA_Y[dir];

        if (sizeX == 1 && sizeY == 1) {
            int mask = getClippedOnlyMask(x + Misc.DIRECTION_DELTA_X[dir], y + Misc.DIRECTION_DELTA_Y[dir], plane);
            if (xOffset == -1 && yOffset == 0)
                return (mask & 0x42240000) == 0;
            if (xOffset == 1 && yOffset == 0)
                return (mask & 0x60240000) == 0;
            if (xOffset == 0 && yOffset == -1)
                return (mask & 0x40a40000) == 0;
            if (xOffset == 0 && yOffset == 1)
                return (mask & 0x48240000) == 0;
            if (xOffset == -1 && yOffset == -1) {
                return (mask & 0x43a40000) == 0 && (getClippedOnlyMask(x - 1, y, plane) & 0x42240000) == 0 && (getClippedOnlyMask(x, y - 1, plane) & 0x40a40000) == 0;
            }
            if (xOffset == 1 && yOffset == -1) {
                return (mask & 0x60e40000) == 0 && (getClippedOnlyMask(x + 1, y, plane) & 0x60240000) == 0 && (getClippedOnlyMask(x, y - 1, plane) & 0x40a40000) == 0;
            }
            if (xOffset == -1 && yOffset == 1) {
                return (mask & 0x4e240000) == 0 && (getClippedOnlyMask(x - 1, y, plane) & 0x42240000) == 0 && (getClippedOnlyMask(x, y + 1, plane) & 0x48240000) == 0;
            }
            if (xOffset == 1 && yOffset == 1) {
                return (mask & 0x78240000) == 0 && (getClippedOnlyMask(x + 1, y, plane) & 0x60240000) == 0 && (getClippedOnlyMask(x, y + 1, plane) & 0x48240000) == 0;
            }
        } else if (sizeX == 2 && sizeY == 2) {
            if (xOffset == -1 && yOffset == 0)
                return (getClippedOnlyMask(x - 1, y, plane) & 0x43a40000) == 0 && (getClippedOnlyMask(x - 1, y + 1, plane) & 0x4e240000) == 0;
            if (xOffset == 1 && yOffset == 0)
                return (getClippedOnlyMask(x + 2, y, plane) & 0x60e40000) == 0 && (getClippedOnlyMask(x + 2, y + 1, plane) & 0x78240000) == 0;
            if (xOffset == 0 && yOffset == -1)
                return (getClippedOnlyMask(x, y - 1, plane) & 0x43a40000) == 0 && (getClippedOnlyMask(x + 1, y - 1, plane) & 0x60e40000) == 0;
            if (xOffset == 0 && yOffset == 1)
                return (getClippedOnlyMask(x, y + 2, plane) & 0x4e240000) == 0 && (getClippedOnlyMask(x + 1, y + 2, plane) & 0x78240000) == 0;
            if (xOffset == -1 && yOffset == -1)
                return (getClippedOnlyMask(x - 1, y, plane) & 0x4fa40000) == 0 && (getClippedOnlyMask(x - 1, y - 1, plane) & 0x43a40000) == 0 && (getClippedOnlyMask(x, y - 1, plane) & 0x63e40000) == 0;
            if (xOffset == 1 && yOffset == -1)
                return (getClippedOnlyMask(x + 1, y - 1, plane) & 0x63e40000) == 0 && (getClippedOnlyMask(x + 2, y - 1, plane) & 0x60e40000) == 0 && (getClippedOnlyMask(x + 2, y, plane) & 0x78e40000) == 0;
            if (xOffset == -1 && yOffset == 1)
                return (getClippedOnlyMask(x - 1, y + 1, plane) & 0x4fa40000) == 0 && (getClippedOnlyMask(x - 1, y + 1, plane) & 0x4e240000) == 0 && (getClippedOnlyMask(x, y + 2, plane) & 0x7e240000) == 0;
            if (xOffset == 1 && yOffset == 1)
                return (getClippedOnlyMask(x + 1, y + 2, plane) & 0x7e240000) == 0 && (getClippedOnlyMask(x + 2, y + 2, plane) & 0x78240000) == 0 && (getClippedOnlyMask(x + 1, y + 1, plane) & 0x78e40000) == 0;
        } else {
            if (xOffset == -1 && yOffset == 0) {
                if ((getClippedOnlyMask(x - 1, y, plane) & 0x43a40000) != 0 || (getClippedOnlyMask(x - 1, -1 + (y + sizeY), plane) & 0x4e240000) != 0)
                    return false;
                for (int sizeOffset = 1; sizeOffset < sizeY - 1; sizeOffset++)
                    if ((getClippedOnlyMask(x - 1, y + sizeOffset, plane) & 0x4fa40000) != 0)
                        return false;
            } else if (xOffset == 1 && yOffset == 0) {
                if ((getClippedOnlyMask(x + sizeX, y, plane) & 0x60e40000) != 0 || (getClippedOnlyMask(x + sizeX, y - (-sizeY + 1), plane) & 0x78240000) != 0)
                    return false;
                for (int sizeOffset = 1; sizeOffset < sizeY - 1; sizeOffset++)
                    if ((getClippedOnlyMask(x + sizeX, y + sizeOffset, plane) & 0x78e40000) != 0)
                        return false;
            } else if (xOffset == 0 && yOffset == -1) {
                if ((getClippedOnlyMask(x, y - 1, plane) & 0x43a40000) != 0 || (getClippedOnlyMask(x + sizeX - 1, y - 1, plane) & 0x60e40000) != 0)
                    return false;
                for (int sizeOffset = 1; sizeOffset < sizeX - 1; sizeOffset++)
                    if ((getClippedOnlyMask(plane, x + sizeOffset, y - 1) & 0x63e40000) != 0)
                        return false;
            } else if (xOffset == 0 && yOffset == 1) {
                if ((getClippedOnlyMask(x, y + sizeY, plane) & 0x4e240000) != 0 || (getClippedOnlyMask(x + (sizeX - 1), y + sizeY, plane) & 0x78240000) != 0)
                    return false;
                for (int sizeOffset = 1; sizeOffset < sizeX - 1; sizeOffset++)
                    if ((getClippedOnlyMask(x + sizeOffset, y + sizeY, plane) & 0x7e240000) != 0)
                        return false;
            } else if (xOffset == -1 && yOffset == -1) {
                if ((getClippedOnlyMask(x - 1, y - 1, plane) & 0x43a40000) != 0)
                    return false;
                for (int sizeOffset = 1; sizeOffset < sizeY; sizeOffset++)
                    if ((getClippedOnlyMask(x - 1, y + (-1 + sizeOffset), plane) & 0x4fa40000) != 0 || (getClippedOnlyMask(sizeOffset - 1 + x, y - 1, plane) & 0x63e40000) != 0)
                        return false;
            } else if (xOffset == 1 && yOffset == -1) {
                if ((getClippedOnlyMask(x + sizeX, y - 1, plane) & 0x60e40000) != 0)
                    return false;
                for (int sizeOffset = 1; sizeOffset < sizeY; sizeOffset++)
                    if ((getClippedOnlyMask(x + sizeX, sizeOffset + (-1 + y), plane) & 0x78e40000) != 0 || (getClippedOnlyMask(x + sizeOffset, y - 1, plane) & 0x63e40000) != 0)
                        return false;
            } else if (xOffset == -1 && yOffset == 1) {
                if ((getClippedOnlyMask(x - 1, y + sizeY, plane) & 0x4e240000) != 0)
                    return false;
                for (int sizeOffset = 1; sizeOffset < sizeY; sizeOffset++)
                    if ((getClippedOnlyMask(x - 1, y + sizeOffset, plane) & 0x4fa40000) != 0 || (getClippedOnlyMask(-1 + (x + sizeOffset), y + sizeY, plane) & 0x7e240000) != 0)
                        return false;
            } else if (xOffset == 1 && yOffset == 1) {
                if ((getClippedOnlyMask(x + sizeX, y + sizeY, plane) & 0x78240000) != 0)
                    return false;
                for (int sizeOffset = 1; sizeOffset < sizeX; sizeOffset++)
                    if ((getClippedOnlyMask(x + sizeOffset, y + sizeY, plane) & 0x7e240000) != 0 || (getClippedOnlyMask(x + sizeX, y + sizeOffset, plane) & 0x78e40000) != 0)
                        return false;
            }
        }
        return true;
    }

    public static final boolean checkWalkStep(int x, int y, int plane, int dir, int size) {
        return checkOffsetWalkStep(x, y, plane, Misc.DIRECTION_DELTA_X[dir], Misc.DIRECTION_DELTA_Y[dir], size);
    }

    public static final boolean checkWalkStep(int x, int y, int plane, int dir, int sizeX, int sizeY) {
        return checkWalkStep(x, y, plane, Misc.DIRECTION_DELTA_X[dir], Misc.DIRECTION_DELTA_Y[dir], sizeX, sizeY);
    }

    public static final boolean checkOffsetWalkStep(int x, int y, int plane, int xOffset, int yOffset, int size) {
        return checkWalkStep(x, y, plane, xOffset, yOffset, size, size);
    }

    public static final boolean checkWalkStep(int x, int y, int plane, int xOffset, int yOffset, int sizeX, int sizeY) {
        if (sizeX == 1 && sizeY == 1) {
            int mask = getMask(x + xOffset, y + yOffset, plane);
            if (xOffset == -1 && yOffset == 0)
                return (mask & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_EAST)) == 0;
            if (xOffset == 1 && yOffset == 0)
                return (mask & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_WEST)) == 0;
            if (xOffset == 0 && yOffset == -1)
                return (mask & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH)) == 0;
            if (xOffset == 0 && yOffset == 1)
                return (mask & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_SOUTH)) == 0;
            if (xOffset == -1 && yOffset == -1)
                return (mask & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH | Flags.WALLOBJ_EAST | Flags.CORNEROBJ_NORTHEAST)) == 0 && (getMask(x - 1, y, plane) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_EAST)) == 0 && (getMask(x, y - 1, plane) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH)) == 0;
            if (xOffset == 1 && yOffset == -1)
                return (mask & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH | Flags.WALLOBJ_WEST | Flags.CORNEROBJ_NORTHWEST)) == 0 && (getMask(x + 1, y, plane) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_WEST)) == 0 && (getMask(x, y - 1, plane) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH)) == 0;
            if (xOffset == -1 && yOffset == 1)
                return (mask & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_EAST | Flags.WALLOBJ_SOUTH | Flags.CORNEROBJ_SOUTHEAST)) == 0 && (getMask(x - 1, y, plane) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_EAST)) == 0 && (getMask(x, y + 1, plane) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_SOUTH)) == 0;
            if (xOffset == 1 && yOffset == 1)
                return (mask & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_SOUTH | Flags.WALLOBJ_WEST | Flags.CORNEROBJ_SOUTHWEST)) == 0 && (getMask(x + 1, y, plane) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_WEST)) == 0 && (getMask(x, y + 1, plane) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_SOUTH)) == 0;
        } else if (sizeX == 2 && sizeY == 2) {
            if (xOffset == -1 && yOffset == 0)
                return (getMask(x - 1, y, plane) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH | Flags.WALLOBJ_EAST | Flags.CORNEROBJ_NORTHEAST)) == 0 && (getMask(x - 1, y + 1, plane) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_EAST | Flags.WALLOBJ_SOUTH | Flags.CORNEROBJ_SOUTHEAST)) == 0;
            if (xOffset == 1 && yOffset == 0)
                return (getMask(x + 2, y, plane) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH | Flags.WALLOBJ_WEST | Flags.CORNEROBJ_NORTHWEST)) == 0 && (getMask(x + 2, y + 1, plane) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_SOUTH | Flags.WALLOBJ_WEST | Flags.CORNEROBJ_SOUTHWEST)) == 0;
            if (xOffset == 0 && yOffset == -1)
                return (getMask(x, y - 1, plane) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH | Flags.WALLOBJ_EAST | Flags.CORNEROBJ_NORTHEAST)) == 0 && (getMask(x + 1, y - 1, plane) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH | Flags.WALLOBJ_WEST | Flags.CORNEROBJ_NORTHWEST)) == 0;
            if (xOffset == 0 && yOffset == 1)
                return (getMask(x, y + 2, plane) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_EAST | Flags.WALLOBJ_SOUTH | Flags.CORNEROBJ_SOUTHEAST)) == 0 && (getMask(x + 1, y + 2, plane) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_SOUTH | Flags.WALLOBJ_WEST | Flags.CORNEROBJ_SOUTHWEST)) == 0;
            if (xOffset == -1 && yOffset == -1)
                return (getMask(x - 1, y, plane) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH | Flags.WALLOBJ_EAST | Flags.WALLOBJ_SOUTH | Flags.CORNEROBJ_NORTHEAST | Flags.CORNEROBJ_SOUTHEAST)) == 0 && (getMask(x - 1, y - 1, plane) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH | Flags.WALLOBJ_EAST | Flags.CORNEROBJ_NORTHEAST)) == 0 && (getMask(x, y - 1, plane) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH | Flags.WALLOBJ_EAST | Flags.WALLOBJ_WEST | Flags.CORNEROBJ_NORTHWEST | Flags.CORNEROBJ_NORTHEAST)) == 0;
            if (xOffset == 1 && yOffset == -1)
                return (getMask(x + 1, y - 1, plane) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH | Flags.WALLOBJ_EAST | Flags.WALLOBJ_WEST | Flags.CORNEROBJ_NORTHWEST | Flags.CORNEROBJ_NORTHEAST)) == 0 && (getMask(x + 2, y - 1, plane) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH | Flags.WALLOBJ_WEST | Flags.CORNEROBJ_NORTHWEST)) == 0 && (getMask(x + 2, y, plane) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH | Flags.WALLOBJ_SOUTH | Flags.WALLOBJ_WEST | Flags.CORNEROBJ_NORTHWEST | Flags.CORNEROBJ_SOUTHWEST)) == 0;
            if (xOffset == -1 && yOffset == 1)
                return (getMask(x - 1, y + 1, plane) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH | Flags.WALLOBJ_EAST | Flags.WALLOBJ_SOUTH | Flags.CORNEROBJ_NORTHEAST | Flags.CORNEROBJ_SOUTHEAST)) == 0 && (getMask(x - 1, y + 1, plane) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_EAST | Flags.WALLOBJ_SOUTH | Flags.CORNEROBJ_SOUTHEAST)) == 0 && (getMask(x, y + 2, plane) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_EAST | Flags.WALLOBJ_SOUTH | Flags.WALLOBJ_WEST | Flags.CORNEROBJ_SOUTHEAST | Flags.CORNEROBJ_SOUTHWEST)) == 0;
            if (xOffset == 1 && yOffset == 1)
                return (getMask(x + 1, y + 2, plane) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_EAST | Flags.WALLOBJ_SOUTH | Flags.WALLOBJ_WEST | Flags.CORNEROBJ_SOUTHEAST | Flags.CORNEROBJ_SOUTHWEST)) == 0 && (getMask(x + 2, y + 2, plane) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_SOUTH | Flags.WALLOBJ_WEST | Flags.CORNEROBJ_SOUTHWEST)) == 0 && (getMask(x + 1, y + 1, plane) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH | Flags.WALLOBJ_SOUTH | Flags.WALLOBJ_WEST | Flags.CORNEROBJ_NORTHWEST | Flags.CORNEROBJ_SOUTHWEST)) == 0;
        } else {
            if (xOffset == -1 && yOffset == 0) {
                if ((getMask(x - 1, y, plane) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH | Flags.WALLOBJ_EAST | Flags.CORNEROBJ_NORTHEAST)) != 0 || (getMask(x - 1, -1 + (y + sizeY), plane) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_EAST | Flags.WALLOBJ_SOUTH | Flags.CORNEROBJ_SOUTHEAST)) != 0)
                    return false;
                for (int sizeOffset = 1; sizeOffset < sizeY - 1; sizeOffset++)
                    if ((getMask(x - 1, y + sizeOffset, plane) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH | Flags.WALLOBJ_EAST | Flags.WALLOBJ_SOUTH | Flags.CORNEROBJ_NORTHEAST | Flags.CORNEROBJ_SOUTHEAST)) != 0)
                        return false;
            } else if (xOffset == 1 && yOffset == 0) {
                if ((getMask(x + sizeX, y, plane) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH | Flags.WALLOBJ_WEST | Flags.CORNEROBJ_NORTHWEST)) != 0 || (getMask(x + sizeX, y - (-sizeY + 1), plane) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_SOUTH | Flags.WALLOBJ_WEST | Flags.CORNEROBJ_SOUTHWEST)) != 0)
                    return false;
                for (int sizeOffset = 1; sizeOffset < sizeY - 1; sizeOffset++)
                    if ((getMask(x + sizeX, y + sizeOffset, plane) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH | Flags.WALLOBJ_SOUTH | Flags.WALLOBJ_WEST | Flags.CORNEROBJ_NORTHWEST | Flags.CORNEROBJ_SOUTHWEST)) != 0)
                        return false;
            } else if (xOffset == 0 && yOffset == -1) {
                if ((getMask(x, y - 1, plane) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH | Flags.WALLOBJ_EAST | Flags.CORNEROBJ_NORTHEAST)) != 0 || (getMask(x + sizeX - 1, y - 1, plane) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH | Flags.WALLOBJ_WEST | Flags.CORNEROBJ_NORTHWEST)) != 0)
                    return false;
                for (int sizeOffset = 1; sizeOffset < sizeX - 1; sizeOffset++)
                    if ((getMask(x + sizeOffset, y - 1, plane) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH | Flags.WALLOBJ_EAST | Flags.WALLOBJ_WEST | Flags.CORNEROBJ_NORTHWEST | Flags.CORNEROBJ_NORTHEAST)) != 0)
                        return false;
            } else if (xOffset == 0 && yOffset == 1) {
                if ((getMask(x, y + sizeY, plane) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_EAST | Flags.WALLOBJ_SOUTH | Flags.CORNEROBJ_SOUTHEAST)) != 0 || (getMask(x + (sizeX - 1), y + sizeY, plane) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_SOUTH | Flags.WALLOBJ_WEST | Flags.CORNEROBJ_SOUTHWEST)) != 0)
                    return false;
                for (int sizeOffset = 1; sizeOffset < sizeX - 1; sizeOffset++)
                    if ((getMask(x + sizeOffset, y + sizeY, plane) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_EAST | Flags.WALLOBJ_SOUTH | Flags.WALLOBJ_WEST | Flags.CORNEROBJ_SOUTHEAST | Flags.CORNEROBJ_SOUTHWEST)) != 0)
                        return false;
            } else if (xOffset == -1 && yOffset == -1) {
                if ((getMask(x - 1, y - 1, plane) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH | Flags.WALLOBJ_EAST | Flags.CORNEROBJ_NORTHEAST)) != 0)
                    return false;
                for (int sizeOffset = 1; sizeOffset < sizeY; sizeOffset++)
                    if ((getMask(x - 1, y + (-1 + sizeOffset), plane) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH | Flags.WALLOBJ_EAST | Flags.WALLOBJ_SOUTH | Flags.CORNEROBJ_NORTHEAST | Flags.CORNEROBJ_SOUTHEAST)) != 0 || (getMask(sizeOffset - 1 + x, y - 1, plane) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH | Flags.WALLOBJ_EAST | Flags.WALLOBJ_WEST | Flags.CORNEROBJ_NORTHWEST | Flags.CORNEROBJ_NORTHEAST)) != 0)
                        return false;
            } else if (xOffset == 1 && yOffset == -1) {
                if ((getMask(x + sizeX, y - 1, plane) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH | Flags.WALLOBJ_WEST | Flags.CORNEROBJ_NORTHWEST)) != 0)
                    return false;
                for (int sizeOffset = 1; sizeOffset < sizeX; sizeOffset++)
                    if ((getMask(x + sizeX, sizeOffset + (-1 + y), plane) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH | Flags.WALLOBJ_SOUTH | Flags.WALLOBJ_WEST | Flags.CORNEROBJ_NORTHWEST | Flags.CORNEROBJ_SOUTHWEST)) != 0 || (getMask(x + sizeOffset, y - 1, plane) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH | Flags.WALLOBJ_EAST | Flags.WALLOBJ_WEST | Flags.CORNEROBJ_NORTHWEST | Flags.CORNEROBJ_NORTHEAST)) != 0)
                        return false;
            } else if (xOffset == -1 && yOffset == 1) {
                if ((getMask(x - 1, y + sizeY, plane) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_EAST | Flags.WALLOBJ_SOUTH | Flags.CORNEROBJ_SOUTHEAST)) != 0)
                    return false;
                for (int sizeOffset = 1; sizeOffset < sizeY; sizeOffset++)
                    if ((getMask(x - 1, y + sizeOffset, plane) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH | Flags.WALLOBJ_EAST | Flags.WALLOBJ_SOUTH | Flags.CORNEROBJ_NORTHEAST | Flags.CORNEROBJ_SOUTHEAST)) != 0 || (getMask(-1 + (x + sizeOffset), y + sizeY, plane) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_EAST | Flags.WALLOBJ_SOUTH | Flags.WALLOBJ_WEST | Flags.CORNEROBJ_SOUTHEAST | Flags.CORNEROBJ_SOUTHWEST)) != 0)
                        return false;
            } else if (xOffset == 1 && yOffset == 1) {
                if ((getMask(x + sizeX, y + sizeY, plane) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_SOUTH | Flags.WALLOBJ_WEST | Flags.CORNEROBJ_SOUTHWEST)) != 0)
                    return false;
                for (int sizeOffset = 1; sizeOffset < sizeX; sizeOffset++)
                    if ((getMask(x + sizeOffset, y + sizeY, plane) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_EAST | Flags.WALLOBJ_SOUTH | Flags.WALLOBJ_WEST | Flags.CORNEROBJ_SOUTHEAST | Flags.CORNEROBJ_SOUTHWEST)) != 0 || (getMask(x + sizeX, y + sizeOffset, plane) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH | Flags.WALLOBJ_SOUTH | Flags.WALLOBJ_WEST | Flags.CORNEROBJ_NORTHWEST | Flags.CORNEROBJ_SOUTHWEST)) != 0)
                        return false;
            }
        }
        return true;
    }

    public static boolean canProjectileMove(int startX, int startY, int endX, int endY, int height, int xLength, int yLength) {
        int diffX = endX - startX;
        int diffY = endY - startY;
        // height %= 4;
        int max = Math.max(Math.abs(diffX), Math.abs(diffY));
        for (int ii = 0; ii < max; ii++) {
            int currentX = endX - diffX;
            int currentY = endY - diffY;
            for (int i = 0; i < xLength; i++) {
                for (int i2 = 0; i2 < yLength; i2++) {
                    if (diffX < 0 && diffY < 0) {
                        if ((World.getMask(currentX + i - 1, currentY + i2 - 1, height) & (UNLOADED_TILE
                                | /* BLOCKED_TILE | */UNKNOWN
                                | PROJECTILE_TILE_BLOCKED
                                | PROJECTILE_EAST_BLOCKED
                                | PROJECTILE_NORTH_EAST_BLOCKED | PROJECTILE_NORTH_BLOCKED)) != 0
                                || (World.getMask(currentX + i - 1, currentY + i2, height) & (UNLOADED_TILE
                                | /* BLOCKED_TILE | */UNKNOWN
                                | PROJECTILE_TILE_BLOCKED | PROJECTILE_EAST_BLOCKED)) != 0
                                || (World.getMask(currentX + i,
                                currentY + i2 - 1, height) & (UNLOADED_TILE
                                | /* BLOCKED_TILE | */UNKNOWN
                                | PROJECTILE_TILE_BLOCKED | PROJECTILE_NORTH_BLOCKED)) != 0) {
                            return false;
                        }
                    } else if (diffX > 0 && diffY > 0) {
                        if ((World.getMask(currentX + i + 1,
                                currentY + i2 + 1, height) & (UNLOADED_TILE
                                | /* BLOCKED_TILE | */UNKNOWN
                                | PROJECTILE_TILE_BLOCKED
                                | PROJECTILE_WEST_BLOCKED
                                | PROJECTILE_SOUTH_WEST_BLOCKED | PROJECTILE_SOUTH_BLOCKED)) != 0
                                || (World.getMask(currentX + i + 1, currentY
                                + i2, height) & (UNLOADED_TILE
                                | /* BLOCKED_TILE | */UNKNOWN
                                | PROJECTILE_TILE_BLOCKED | PROJECTILE_WEST_BLOCKED)) != 0
                                || (World.getMask(currentX + i,
                                currentY + i2 + 1, height) & (UNLOADED_TILE
                                | /* BLOCKED_TILE | */UNKNOWN
                                | PROJECTILE_TILE_BLOCKED | PROJECTILE_SOUTH_BLOCKED)) != 0) {
                            return false;
                        }
                    } else if (diffX < 0 && diffY > 0) {
                        if ((World.getMask(currentX + i - 1,
                                currentY + i2 + 1, height) & (UNLOADED_TILE
                                | /* BLOCKED_TILE | */UNKNOWN
                                | PROJECTILE_TILE_BLOCKED
                                | PROJECTILE_SOUTH_BLOCKED
                                | PROJECTILE_SOUTH_EAST_BLOCKED | PROJECTILE_EAST_BLOCKED)) != 0
                                || (World.getMask(currentX + i - 1, currentY
                                + i2, height) & (UNLOADED_TILE
                                | /* BLOCKED_TILE | */UNKNOWN
                                | PROJECTILE_TILE_BLOCKED | PROJECTILE_EAST_BLOCKED)) != 0
                                || (World.getMask(currentX + i,
                                currentY + i2 + 1, height) & (UNLOADED_TILE
                                | /* BLOCKED_TILE | */UNKNOWN
                                | PROJECTILE_TILE_BLOCKED | PROJECTILE_SOUTH_BLOCKED)) != 0) {
                            return false;
                        }
                    } else if (diffX > 0 && diffY < 0) {
                        if ((World.getMask(currentX + i + 1,
                                currentY + i2 - 1, height) & (UNLOADED_TILE
                                | /* BLOCKED_TILE | */UNKNOWN
                                | PROJECTILE_TILE_BLOCKED
                                | PROJECTILE_WEST_BLOCKED
                                | PROJECTILE_NORTH_BLOCKED | PROJECTILE_NORTH_WEST_BLOCKED)) != 0
                                || (World.getMask(currentX + i + 1, currentY
                                + i2, height) & (UNLOADED_TILE
                                | /* BLOCKED_TILE | */UNKNOWN
                                | PROJECTILE_TILE_BLOCKED | PROJECTILE_WEST_BLOCKED)) != 0
                                || (World.getMask(currentX + i,
                                currentY + i2 - 1, height) & (UNLOADED_TILE
                                | /* BLOCKED_TILE | */UNKNOWN
                                | PROJECTILE_TILE_BLOCKED | PROJECTILE_NORTH_BLOCKED)) != 0) {
                            return false;
                        }
                    } else if (diffX > 0 && diffY == 0) {
                        if ((World.getMask(currentX + i + 1,
                                currentY + i2, height) & (UNLOADED_TILE
                                | /* BLOCKED_TILE | */UNKNOWN
                                | PROJECTILE_TILE_BLOCKED | PROJECTILE_WEST_BLOCKED)) != 0) {
                            return false;
                        }
                    } else if (diffX < 0 && diffY == 0) {
                        if ((World.getMask(currentX + i - 1,
                                currentY + i2, height) & (UNLOADED_TILE
                                | /* BLOCKED_TILE | */UNKNOWN
                                | PROJECTILE_TILE_BLOCKED | PROJECTILE_EAST_BLOCKED)) != 0) {
                            return false;
                        }
                    } else if (diffX == 0 && diffY > 0) {
                        if ((World.getMask(currentX + i, currentY
                                + i2 + 1, height) & (UNLOADED_TILE | /*
                         * BLOCKED_TILE
                         * |
                         */UNKNOWN
                                | PROJECTILE_TILE_BLOCKED | PROJECTILE_SOUTH_BLOCKED)) != 0) {
                            return false;
                        }
                    } else if (diffX == 0 && diffY < 0) {
                        if ((World.getMask(currentX + i, currentY
                                + i2 - 1, height) & (UNLOADED_TILE | /*
                         * BLOCKED_TILE
                         * |
                         */UNKNOWN
                                | PROJECTILE_TILE_BLOCKED | PROJECTILE_NORTH_BLOCKED)) != 0) {
                            return false;
                        }
                    }
                }
            }
            if (diffX < 0) {
                diffX++;
            } else if (diffX > 0) {
                diffX--;
            }
            if (diffY < 0) {
                diffY++; // change
            } else if (diffY > 0) {
                diffY--;
            }
        }
        return true;
    }

    public final static boolean isInDiagonalBlock(Figure attacked, Figure attacker) {
        return attacked.getX() - 1 == attacker.getX() && attacked.getY() + 1 == attacker.getY() || attacker.getX() - 1 == attacked.getX() && attacker.getY() + 1 == attacked.getY() || attacked.getX() + 1 == attacker.getX() && attacked.getY() - 1 == attacker.getY() || attacker.getX() + 1 == attacked.getX() && attacker.getY() - 1 == attacked.getY() || attacked.getX() + 1 == attacker.getX() && attacked.getY() + 1 == attacker.getY() || attacker.getX() + 1 == attacked.getX() && attacker.getY() + 1 == attacked.getY();
    }


    public static final int PROJECTILE_NORTH_WEST_BLOCKED = 0x200;
    public static final int PROJECTILE_NORTH_BLOCKED = 0x400;
    public static final int PROJECTILE_NORTH_EAST_BLOCKED = 0x800;
    public static final int PROJECTILE_EAST_BLOCKED = 0x1000;
    public static final int PROJECTILE_SOUTH_EAST_BLOCKED = 0x2000;
    public static final int PROJECTILE_SOUTH_BLOCKED = 0x4000;
    public static final int PROJECTILE_SOUTH_WEST_BLOCKED = 0x8000;
    public static final int PROJECTILE_WEST_BLOCKED = 0x10000;
    public static final int PROJECTILE_TILE_BLOCKED = 0x20000;
    public static final int UNKNOWN = 0x80000;
    public static final int BLOCKED_TILE = 0x200000;
    public static final int UNLOADED_TILE = 0x1000000;
    public static final int OCEAN_TILE = 2097152;

    public static final void init() {
        MapBuilder.init();
        LivingRockCavern.init();
        //DistractionAndDiversions.init();
        JadinkoLair.init();
        ThievingGuild.init();
    }

    public static void handleScheduledEvents(String time) {
        String[] hoursAndMinutes = time.split(":");
        int hours = Integer.parseInt(hoursAndMinutes[0]);
        int minutes = Integer.parseInt(hoursAndMinutes[1]);

        ShootingStar.handleTimedEvent(hours, minutes);
        EvilTree.get().handleTimedEvent(hours, minutes);
    }
}
