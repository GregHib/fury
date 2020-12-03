package com.fury.game.world.map.path;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.game.world.World;
import com.fury.game.world.map.clip.Flags;

import java.util.LinkedList;

public class PathFinder {
    private static final int GRAPH_SIZE = 128;
    private static final int QUEUE_SIZE = (GRAPH_SIZE * GRAPH_SIZE) / 4; // we do /4 because each tile can only be accessed from single direction
    private static final int ALTERNATIVE_ROUTE_MAX_DISTANCE = 100;
    private static final int ALTERNATIVE_ROUTE_RANGE = 10;

    private static final int DIR_NORTH = 0x1;
    private static final int DIR_EAST = 0x2;
    private static final int DIR_SOUTH = 0x4;
    private static final int DIR_WEST = 0x8;
    private static int exitX = -1;
    private static int exitY = -1;
    final static LinkedList<Integer> tileQueueX = new LinkedList<>();
    final static LinkedList<Integer> tileQueueY = new LinkedList<>();
    final static int[][] directions = new int[GRAPH_SIZE][GRAPH_SIZE];
    final static int[][] distances = new int[GRAPH_SIZE][GRAPH_SIZE];
    private static boolean isAlternative;

    public static int findRoute(Figure figure, int width, int length, RouteStrategy strategy, boolean findAlternative) {
        try {
            isAlternative = false;
            final int height = figure.getZ() % 4;

            tileQueueX.clear();
            tileQueueY.clear();

            for (int x = 0; x < GRAPH_SIZE; x++)
                for (int y = 0; y < GRAPH_SIZE; y++) {
                    directions[x][y] = 0;
                    distances[x][y] = 99999999;
                }

            boolean found;
            switch (Math.max(width, length)) {
                case 1:
                    found = findSize1(figure, strategy, height);
                    break;
                default:
                    found = findSizeX(figure, strategy, height, width, length);
                    break;
            }

            if (!found && !findAlternative)
                return -1;

            int sourceX = figure.getX();
            int sourceY = figure.getY();

            int graphBaseX = sourceX - (GRAPH_SIZE / 2);
            int graphBaseY = sourceY - (GRAPH_SIZE / 2);
            int endX = exitX;
            int endY = exitY;

            if (!found) {
                isAlternative = true;
                int lowestCost = Integer.MAX_VALUE;
                int lowestDistance = Integer.MAX_VALUE;

                int approxDestX = strategy.getApproxDestinationX();
                int approxDestY = strategy.getApproxDestinationY();

                for (int checkX = approxDestX - ALTERNATIVE_ROUTE_RANGE; checkX <= approxDestX + ALTERNATIVE_ROUTE_RANGE; checkX++)
                    for (int checkY = approxDestY - ALTERNATIVE_ROUTE_RANGE; checkY <= approxDestY + ALTERNATIVE_ROUTE_RANGE; checkY++) {
                        int graphX = checkX - graphBaseX;
                        int graphY = checkY - graphBaseY;
                        if (graphX < 0 || graphY < 0 || graphX >= GRAPH_SIZE || graphY >= GRAPH_SIZE || distances[graphX][graphY] >= ALTERNATIVE_ROUTE_MAX_DISTANCE)
                            continue;

                        int deltaX;
                        int deltaY;
                        if (approxDestX <= checkX) {
                            deltaX = 1 - approxDestX - (strategy.getApproxDestinationSizeX() - checkX);
                        } else
                            deltaX = approxDestX - checkX;
                        if (approxDestY <= checkY) {
                            deltaY = 1 - approxDestY - (strategy.getApproxDestinationSizeY() - checkY);
                        } else
                            deltaY = approxDestY - checkY;

                        int cost = (deltaX * deltaX) + (deltaY * deltaY);
                        if (cost < lowestCost || (cost <= lowestCost && distances[graphX][graphY] < lowestDistance)) {
                            // if the cost is lower than the lowest one, or same as the lowest one, but less steps, we accept this position as alternate.
                            lowestCost = cost;
                            lowestDistance = distances[graphX][graphY];
                            endX = checkX;
                            endY = checkY;
                        }
                    }

                if (lowestCost == Integer.MAX_VALUE || lowestDistance == Integer.MAX_VALUE)
                    return -1;
            }

            if(endX == sourceX && endY == sourceY)
                return 0;

            int steps = 0;
            tileQueueX.set(steps, endX);
            tileQueueY.set(steps++, endY);
            int lastWritten;
            for (int direction = lastWritten = directions[endX - graphBaseX][endY - graphBaseY]; endX != sourceX || endY != sourceY; direction = directions[endX - graphBaseX][endY - graphBaseY]) {
                if (direction != lastWritten) {
                    lastWritten = direction;
                    tileQueueX.set(steps, endX);
                    tileQueueY.set(steps++, endY);
                }

                if ((direction & DIR_EAST) != 0)
                    endX++;
                else if ((direction & DIR_WEST) != 0)
                    endX--;

                if ((direction & DIR_NORTH) != 0)
                    endY++;
                else if ((direction & DIR_SOUTH) != 0)
                    endY--;
            }
            return steps;
        } catch (Exception e) {
            System.out.println("Error finding route");
//            System.out.println("Error finding route, destinationX: " + approxDestX + ", destinationY: " + approxDestY + ". Queue reset.");
            figure.getMovement().reset();
            return -1;
        }
    }

    private static boolean findSizeX(Figure figure, RouteStrategy strategy, int height, int width, int length) {
        int[][] directions = PathFinder.directions;
        int[][] distances = PathFinder.distances;
        LinkedList<Integer> tileQueueX = PathFinder.tileQueueX;
        LinkedList<Integer> tileQueueY = PathFinder.tileQueueY;

        int sourceX = figure.getX();
        int sourceY = figure.getY();

        int graphBaseX = sourceX - (GRAPH_SIZE / 2);
        int graphBaseY = sourceY - (GRAPH_SIZE / 2);
        int currentX = sourceX;
        int currentY = sourceY;
        int currentGraphX = sourceX - graphBaseX;
        int currentGraphY = sourceY - graphBaseY;

        directions[currentGraphX][currentGraphY] = 99;
        distances[currentGraphX][currentGraphY] = 0;

        int read = 0;
        tileQueueX.add(currentX);
        tileQueueY.add(currentY);
        final int pathLength = 4096;
        int count = 0;
        while (read != tileQueueX.size()) {
            currentX = tileQueueX.get(read);
            currentY = tileQueueY.get(read);
            read = (read + 1) % (pathLength - 1);

            currentGraphX = currentX - graphBaseX;
            currentGraphY = currentY - graphBaseY;

            if (strategy.canExit(currentX, currentY, height, width, length, graphBaseX, graphBaseY)) {
                exitX = currentX;
                exitY = currentY;
                return true;
            }

            int nextDistance = distances[currentGraphX][currentGraphY] + 1;
            if (currentGraphX > 0 && directions[currentGraphX - 1][currentGraphY] == 0 && (World.getMask(currentGraphX + graphBaseX - 1, currentGraphY + graphBaseY, height) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_EAST_BLOCKSWALK_ALTERNATIVE)) == 0 && (World.getMask(currentGraphX + graphBaseX - 1, currentGraphY + graphBaseY + (length - 1), height) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_EAST_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_SOUTH_BLOCKSWALK_ALTERNATIVE | Flags.CORNEROBJ_SOUTHEAST_BLOCKSWALK_ALTERNATIVE)) == 0) {
                exit: do {
                    for (int y = 1; y < (length - 1); y++) {
                        if ((World.getMask(currentGraphX + graphBaseX - 1, currentGraphY + graphBaseY + y, height) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_NORTH_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_EAST_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_SOUTH_BLOCKSWALK_ALTERNATIVE | Flags.CORNEROBJ_NORTHEAST_BLOCKSWALK_ALTERNATIVE | Flags.CORNEROBJ_SOUTHEAST_BLOCKSWALK_ALTERNATIVE)) != 0)
                            break exit;
                    }
                    tileQueueX.add(currentX - 1);
                    tileQueueY.add(currentY);

                    directions[currentGraphX - 1][currentGraphY] = DIR_EAST;
                    distances[currentGraphX - 1][currentGraphY] = nextDistance;
                } while (false);
            }

            if (currentGraphX < GRAPH_SIZE - width && directions[currentGraphX + 1][currentGraphY] == 0 && (World.getMask(currentGraphX + graphBaseX + 1, currentGraphY + graphBaseY, height) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_WEST_BLOCKSWALK_ALTERNATIVE)) == 0 && (World.getMask(currentGraphX + graphBaseX + length, currentGraphY + graphBaseY + (length - 1), height) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_SOUTH_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_WEST_BLOCKSWALK_ALTERNATIVE | Flags.CORNEROBJ_SOUTHWEST_BLOCKSWALK_ALTERNATIVE)) == 0) {
                exit: do {
                    for (int y = 1; y < (length - 1); y++) {
                        if ((World.getMask(currentGraphX + graphBaseX + width, currentGraphY + graphBaseY + y, height) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_NORTH_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_SOUTH_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_WEST_BLOCKSWALK_ALTERNATIVE | Flags.CORNEROBJ_NORTHWEST_BLOCKSWALK_ALTERNATIVE | Flags.CORNEROBJ_SOUTHWEST_BLOCKSWALK_ALTERNATIVE)) != 0)
                            break exit;
                    }
                    tileQueueX.add(currentX + 1);
                    tileQueueY.add(currentY);

                    directions[currentGraphX + 1][currentGraphY] = DIR_WEST;
                    distances[currentGraphX + 1][currentGraphY] = nextDistance;
                } while (false);
            }

            if (currentGraphY > 0 && directions[currentGraphX][currentGraphY - 1] == 0 && (World.getMask(currentGraphX + graphBaseX, currentGraphY + graphBaseY - 1, height) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_NORTH_BLOCKSWALK_ALTERNATIVE)) == 0 && (World.getMask(currentGraphX + graphBaseX + (length - 1), currentGraphY + graphBaseY - 1, height) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_NORTH_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_WEST_BLOCKSWALK_ALTERNATIVE | Flags.CORNEROBJ_NORTHWEST_BLOCKSWALK_ALTERNATIVE)) == 0) {
                exit: do {
                    for (int x = 1; x < (width - 1); x++) {
                        if ((World.getMask(currentGraphX + graphBaseX + x, currentGraphY + graphBaseY - 1, height) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_NORTH_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_EAST_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_WEST_BLOCKSWALK_ALTERNATIVE | Flags.CORNEROBJ_NORTHWEST_BLOCKSWALK_ALTERNATIVE | Flags.CORNEROBJ_NORTHEAST_BLOCKSWALK_ALTERNATIVE)) != 0)
                            break exit;
                    }
                    tileQueueX.add(currentX);
                    tileQueueY.add(currentY - 1);

                    directions[currentGraphX][currentGraphY - 1] = DIR_NORTH;
                    distances[currentGraphX][currentGraphY - 1] = nextDistance;
                } while (false);
            }

            if (currentGraphY < GRAPH_SIZE - length && directions[currentGraphX][currentGraphY + 1] == 0 && (World.getMask(currentGraphX + graphBaseX, currentGraphY + graphBaseY + 1, height) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_SOUTH_BLOCKSWALK_ALTERNATIVE)) == 0 && (World.getMask(currentGraphX + graphBaseX + (length - 1), currentGraphY + graphBaseY + length, height) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_SOUTH_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_WEST_BLOCKSWALK_ALTERNATIVE | Flags.CORNEROBJ_SOUTHWEST_BLOCKSWALK_ALTERNATIVE)) == 0) {
                exit: do {
                    for (int x = 1; x < (width - 1); x++) {
                        if ((World.getMask(currentGraphX + graphBaseX + x, currentGraphY + graphBaseY + length, height) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_EAST_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_SOUTH_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_WEST_BLOCKSWALK_ALTERNATIVE | Flags.CORNEROBJ_SOUTHEAST_BLOCKSWALK_ALTERNATIVE | Flags.CORNEROBJ_SOUTHWEST_BLOCKSWALK_ALTERNATIVE)) != 0)
                            break exit;
                    }
                    tileQueueX.add(currentX);
                    tileQueueY.add(currentY + 1);

                    directions[currentGraphX][currentGraphY + 1] = DIR_SOUTH;
                    distances[currentGraphX][currentGraphY + 1] = nextDistance;
                } while (false);
            }

            //Diagon alley
            if (currentGraphX > 0 && currentGraphY > 0 && directions[currentGraphX - 1][currentGraphY - 1] == 0
                    && (World.getMask(currentGraphX + graphBaseX - 1, currentGraphY + graphBaseY - 1, height) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_NORTH_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_EAST_BLOCKSWALK_ALTERNATIVE | Flags.CORNEROBJ_NORTHEAST_BLOCKSWALK_ALTERNATIVE)) == 0) {
                exit: do {
                    for (int y = 1; y < length; y++) {
                        if ((World.getMask(currentGraphX + graphBaseX - 1, currentGraphY + graphBaseY + (y - 1), height) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_NORTH_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_EAST_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_SOUTH_BLOCKSWALK_ALTERNATIVE | Flags.CORNEROBJ_NORTHEAST_BLOCKSWALK_ALTERNATIVE | Flags.CORNEROBJ_SOUTHEAST_BLOCKSWALK_ALTERNATIVE)) != 0 || (World.getMask(currentGraphX + graphBaseX + (y - 1), currentGraphY + graphBaseY - 1, height) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_NORTH_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_EAST_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_WEST_BLOCKSWALK_ALTERNATIVE | Flags.CORNEROBJ_NORTHWEST_BLOCKSWALK_ALTERNATIVE | Flags.CORNEROBJ_NORTHEAST_BLOCKSWALK_ALTERNATIVE)) != 0)
                            break exit;
                    }
                    tileQueueX.add(currentX - 1);
                    tileQueueY.add(currentY - 1);

                    directions[currentGraphX - 1][currentGraphY - 1] = DIR_NORTH | DIR_EAST;
                    distances[currentGraphX - 1][currentGraphY - 1] = nextDistance;
                } while (false);
            }

            if (currentGraphX < GRAPH_SIZE - width && currentGraphY > 0 && directions[currentGraphX + 1][currentGraphY - 1] == 0
                    && (World.getMask(currentGraphX + graphBaseX + width, currentGraphY + graphBaseY - 1, height) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_NORTH_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_WEST_BLOCKSWALK_ALTERNATIVE | Flags.CORNEROBJ_NORTHWEST_BLOCKSWALK_ALTERNATIVE)) == 0) {
                exit: do {
                    for (int y = 1; y < length; y++) {
                        if ((World.getMask(currentGraphX + graphBaseX + width, currentGraphY + graphBaseY + (y - 1), height) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_NORTH_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_SOUTH_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_WEST_BLOCKSWALK_ALTERNATIVE | Flags.CORNEROBJ_NORTHWEST_BLOCKSWALK_ALTERNATIVE | Flags.CORNEROBJ_SOUTHWEST_BLOCKSWALK_ALTERNATIVE)) != 0 || (World.getMask(currentGraphX + graphBaseX + y, currentGraphY + graphBaseY - 1, height) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_NORTH_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_EAST_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_WEST_BLOCKSWALK_ALTERNATIVE | Flags.CORNEROBJ_NORTHWEST_BLOCKSWALK_ALTERNATIVE | Flags.CORNEROBJ_NORTHEAST_BLOCKSWALK_ALTERNATIVE)) != 0)
                            break exit;
                    }
                    tileQueueX.add(currentX + 1);
                    tileQueueY.add(currentY - 1);

                    directions[currentGraphX + 1][currentGraphY - 1] = DIR_NORTH | DIR_WEST;
                    distances[currentGraphX + 1][currentGraphY - 1] = nextDistance;
                } while (false);
            }

            if (currentGraphX > 0 && currentGraphY < GRAPH_SIZE - length && directions[currentGraphX - 1][currentGraphY + 1] == 0
                    && (World.getMask(currentGraphX + graphBaseX - 1, currentGraphY + graphBaseY + length, height) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_EAST_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_SOUTH_BLOCKSWALK_ALTERNATIVE | Flags.CORNEROBJ_SOUTHEAST_BLOCKSWALK_ALTERNATIVE)) == 0) {
                exit: do {
                    for (int y = 1; y < length; y++) {
                        if ((World.getMask(currentGraphX + graphBaseX - 1, currentGraphY + graphBaseY + y, height) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_NORTH_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_EAST_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_SOUTH_BLOCKSWALK_ALTERNATIVE | Flags.CORNEROBJ_NORTHEAST_BLOCKSWALK_ALTERNATIVE | Flags.CORNEROBJ_SOUTHEAST_BLOCKSWALK_ALTERNATIVE)) != 0 || (World.getMask(currentGraphX + graphBaseX + (y - 1), currentGraphY + graphBaseY + length, height) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_EAST_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_SOUTH_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_WEST_BLOCKSWALK_ALTERNATIVE | Flags.CORNEROBJ_SOUTHEAST_BLOCKSWALK_ALTERNATIVE | Flags.CORNEROBJ_SOUTHWEST_BLOCKSWALK_ALTERNATIVE)) != 0)
                            break exit;
                    }
                    tileQueueX.add(currentX - 1);
                    tileQueueY.add(currentY + 1);

                    directions[currentGraphX - 1][currentGraphY + 1] = DIR_SOUTH | DIR_EAST;
                    distances[currentGraphX - 1][currentGraphY + 1] = nextDistance;
                } while (false);
            }

            if (currentGraphX < GRAPH_SIZE - width && currentGraphY < GRAPH_SIZE - length && directions[currentGraphX + 1][currentGraphY + 1] == 0
                    && (World.getMask(currentGraphX + graphBaseX + width, currentGraphY + graphBaseY + length, height) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_SOUTH_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_WEST_BLOCKSWALK_ALTERNATIVE | Flags.CORNEROBJ_SOUTHWEST_BLOCKSWALK_ALTERNATIVE)) == 0) {
                exit: do {
                    for (int x = 1; x < width; x++) {
                        if ((World.getMask(currentGraphX + graphBaseX + x, currentGraphY + graphBaseY + length, height) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_EAST_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_SOUTH_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_WEST_BLOCKSWALK_ALTERNATIVE | Flags.CORNEROBJ_SOUTHEAST_BLOCKSWALK_ALTERNATIVE | Flags.CORNEROBJ_SOUTHWEST_BLOCKSWALK_ALTERNATIVE)) != 0 || (World.getMask(currentGraphX + graphBaseX + length, currentGraphY + graphBaseY + x, height) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_NORTH_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_SOUTH_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_WEST_BLOCKSWALK_ALTERNATIVE | Flags.CORNEROBJ_NORTHWEST_BLOCKSWALK_ALTERNATIVE | Flags.CORNEROBJ_SOUTHWEST_BLOCKSWALK_ALTERNATIVE)) != 0)
                            break exit;
                    }
                    tileQueueX.add(currentX + 1);
                    tileQueueY.add(currentY + 1);

                    directions[currentGraphX + 1][currentGraphY + 1] = DIR_SOUTH | DIR_WEST;
                    distances[currentGraphX + 1][currentGraphY + 1] = nextDistance;
                } while (false);
            }
            count++;

            if(count >= 15) {
                System.out.println("Npc path find canceled after " + count + " size:" + width + "x" + length);
                break;
            }
        }

        exitX = currentX;
        exitY = currentY;
        return false;
    }

    private static boolean findSize1(Figure figure, RouteStrategy strategy, int height) {
        int[][] directions = PathFinder.directions;
        int[][] distances = PathFinder.distances;
        LinkedList<Integer> tileQueueX = PathFinder.tileQueueX;
        LinkedList<Integer> tileQueueY = PathFinder.tileQueueY;

        int sourceX = figure.getX();
        int sourceY = figure.getY();

        int graphBaseX = sourceX - (GRAPH_SIZE / 2);
        int graphBaseY = sourceY - (GRAPH_SIZE / 2);
        int currentX = sourceX;
        int currentY = sourceY;
        int currentGraphX = sourceX - graphBaseX;
        int currentGraphY = sourceY - graphBaseY;

        directions[currentGraphX][currentGraphY] = 99;
        distances[currentGraphX][currentGraphY] = 0;

        int read = 0, write = 0;
        tileQueueX.add(currentX);
        tileQueueY.add(currentY);
        write++;
        final int pathLength = 4096;
        while (read != write) {
            currentX = tileQueueX.get(read);
            currentY = tileQueueY.get(read);
            read = (read + 1) % (pathLength - 1);

            currentGraphX = currentX - graphBaseX;
            currentGraphY = currentY - graphBaseY;

            if (strategy.canExit(currentX, currentY, height, 1, 1, graphBaseX, graphBaseY)) {
                exitX = currentX;
                exitY = currentY;
                return true;
            }

            int nextDistance = distances[currentGraphX][currentGraphY] + 1;
            if (currentGraphX > 0 && directions[currentGraphX - 1][currentGraphY] == 0 && (World.getMask(currentGraphX + graphBaseX - 1, currentGraphY + graphBaseY, height) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_EAST_BLOCKSWALK_ALTERNATIVE)) == 0) {
                tileQueueX.add(currentX - 1);
                tileQueueY.add(currentY);
                write = (write + 1) & (QUEUE_SIZE - 1);

                directions[currentGraphX - 1][currentGraphY] = DIR_EAST;
                distances[currentGraphX - 1][currentGraphY] = nextDistance;
            }

            if (currentGraphX < GRAPH_SIZE - 1 && directions[currentGraphX + 1][currentGraphY] == 0 && (World.getMask(currentGraphX + graphBaseX + 1, currentGraphY + graphBaseY, height) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_WEST_BLOCKSWALK_ALTERNATIVE)) == 0) {
                tileQueueX.add(currentX + 1);
                tileQueueY.add(currentY);
                write = (write + 1) & (QUEUE_SIZE - 1);

                directions[currentGraphX + 1][currentGraphY] = DIR_WEST;
                distances[currentGraphX + 1][currentGraphY] = nextDistance;
            }

            if (currentGraphY > 0 && directions[currentGraphX][currentGraphY - 1] == 0 && (World.getMask(currentGraphX + graphBaseX, currentGraphY + graphBaseY - 1, height) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_NORTH_BLOCKSWALK_ALTERNATIVE)) == 0) {
                tileQueueX.add(currentX);
                tileQueueY.add(currentY - 1);
                write = (write + 1) & (QUEUE_SIZE - 1);

                directions[currentGraphX][currentGraphY - 1] = DIR_NORTH;
                distances[currentGraphX][currentGraphY - 1] = nextDistance;
            }

            if (currentGraphY < GRAPH_SIZE - 1 && directions[currentGraphX][currentGraphY + 1] == 0 && (World.getMask(currentGraphX + graphBaseX, currentGraphY + graphBaseY + 1, height) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_SOUTH_BLOCKSWALK_ALTERNATIVE)) == 0) {
                tileQueueX.add(currentX);
                tileQueueY.add(currentY + 1);
                write = (write + 1) & (QUEUE_SIZE - 1);

                directions[currentGraphX][currentGraphY + 1] = DIR_SOUTH;
                distances[currentGraphX][currentGraphY + 1] = nextDistance;
            }

            //Diagon alley
            if (currentGraphX > 0 && currentGraphY > 0 && directions[currentGraphX - 1][currentGraphY - 1] == 0
                    && (World.getMask(currentGraphX + graphBaseX - 1, currentGraphY + graphBaseY - 1, height) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_NORTH_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_EAST_BLOCKSWALK_ALTERNATIVE | Flags.CORNEROBJ_NORTHEAST_BLOCKSWALK_ALTERNATIVE)) == 0
                    && (World.getMask(currentGraphX + graphBaseX - 1, currentGraphY + graphBaseY, height) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_EAST_BLOCKSWALK_ALTERNATIVE)) == 0
                    && (World.getMask(currentGraphX + graphBaseX, currentGraphY + graphBaseY - 1, height) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_NORTH_BLOCKSWALK_ALTERNATIVE)) == 0) {
                tileQueueX.add(currentX - 1);
                tileQueueY.add(currentY - 1);
                write = (write + 1) & (QUEUE_SIZE - 1);

                directions[currentGraphX - 1][currentGraphY - 1] = DIR_NORTH | DIR_EAST;
                distances[currentGraphX - 1][currentGraphY - 1] = nextDistance;
            }

            if (currentGraphX < GRAPH_SIZE - 1 && currentGraphY > 0 && directions[currentGraphX + 1][currentGraphY - 1] == 0
                    && (World.getMask(currentGraphX + graphBaseX + 1, currentGraphY + graphBaseY - 1, height) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_NORTH_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_WEST_BLOCKSWALK_ALTERNATIVE | Flags.CORNEROBJ_NORTHWEST_BLOCKSWALK_ALTERNATIVE)) == 0
                    && (World.getMask(currentGraphX + graphBaseX + 1, currentGraphY + graphBaseY, height) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_WEST_BLOCKSWALK_ALTERNATIVE)) == 0
                    && (World.getMask(currentGraphX + graphBaseX, currentGraphY + graphBaseY - 1, height) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_NORTH_BLOCKSWALK_ALTERNATIVE)) == 0) {
                tileQueueX.add(currentX + 1);
                tileQueueY.add(currentY - 1);
                write = (write + 1) & (QUEUE_SIZE - 1);

                directions[currentGraphX + 1][currentGraphY - 1] = DIR_NORTH | DIR_WEST;
                distances[currentGraphX + 1][currentGraphY - 1] = nextDistance;
            }

            if (currentGraphX > 0 && currentGraphY < GRAPH_SIZE - 1 && directions[currentGraphX - 1][currentGraphY + 1] == 0
                    && (World.getMask(currentGraphX + graphBaseX - 1, currentGraphY + graphBaseY + 1, height) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_EAST_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_SOUTH_BLOCKSWALK_ALTERNATIVE | Flags.CORNEROBJ_SOUTHEAST_BLOCKSWALK_ALTERNATIVE)) == 0
                    && (World.getMask(currentGraphX + graphBaseX - 1, currentGraphY + graphBaseY, height) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_EAST_BLOCKSWALK_ALTERNATIVE)) == 0
                    && (World.getMask(currentGraphX + graphBaseX, currentGraphY + graphBaseY + 1, height) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_SOUTH_BLOCKSWALK_ALTERNATIVE)) == 0) {
                tileQueueX.add(currentX - 1);
                tileQueueY.add(currentY + 1);
                write = (write + 1) & (QUEUE_SIZE - 1);

                directions[currentGraphX - 1][currentGraphY + 1] = DIR_SOUTH | DIR_EAST;
                distances[currentGraphX - 1][currentGraphY + 1] = nextDistance;
            }

            if (currentGraphX < GRAPH_SIZE - 1 && currentGraphY < GRAPH_SIZE - 1 && directions[currentGraphX + 1][currentGraphY + 1] == 0
                    && (World.getMask(currentGraphX + graphBaseX + 1, currentGraphY + graphBaseY + 1, height) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_SOUTH_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_WEST_BLOCKSWALK_ALTERNATIVE | Flags.CORNEROBJ_SOUTHWEST_BLOCKSWALK_ALTERNATIVE)) == 0
                    && (World.getMask(currentGraphX + graphBaseX + 1, currentGraphY + graphBaseY, height) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_WEST_BLOCKSWALK_ALTERNATIVE)) == 0
                    && (World.getMask(currentGraphX + graphBaseX, currentGraphY + graphBaseY + 1, height) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_SOUTH_BLOCKSWALK_ALTERNATIVE)) == 0) {
                tileQueueX.add(currentX + 1);
                tileQueueY.add(currentY + 1);
                write = (write + 1) & (QUEUE_SIZE - 1);

                directions[currentGraphX + 1][currentGraphY + 1] = DIR_SOUTH | DIR_WEST;
                distances[currentGraphX + 1][currentGraphY + 1] = nextDistance;
            }
        }

        exitX = currentX;
        exitY = currentY;
        return false;
    }

    public static LinkedList<Integer> getLastPathBufferX() {
        return tileQueueX;
    }

    public static LinkedList<Integer> getLastPathBufferY() {
        return tileQueueY;
    }

    protected static boolean lastIsAlternative() {
        return isAlternative;
    }
}
