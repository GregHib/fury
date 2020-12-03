package com.fury.game.world.map.path;

import com.fury.core.model.node.entity.Entity;
import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.FloorItem;
import com.fury.game.entity.object.GameObject;
import com.fury.game.world.map.Position;

import java.util.LinkedList;

public class RouteEvent {

    private Object object;

    private Runnable event;

    private RouteStrategy[] last;

    private boolean combat;

    public RouteEvent(Object object, Runnable event, boolean combat) {
        this.object = object;
        this.event = event;
        this.combat = combat;
    }

    public RouteEvent(Object object, Runnable event) {
        this(object, event, false);
    }

    public boolean process(Player player) {
        if (!simpleCheck(player)) {
            player.message("You can't reach that.");
            return true;
        }

        RouteStrategy[] strategies = generateStrategies();
        if (last != null && match(strategies, last) && player.getMovement().hasWalkSteps())
            return false;
        else if (last != null && match(strategies, last) && !player.getMovement().hasWalkSteps()) {
            for (int i = 0; i < strategies.length; i++) {
                RouteStrategy strategy = strategies[i];

                int steps = PathFinder.findRoute(player, player.getSizeX(), player.getSizeY(), strategy, i == (strategies.length - 1));
                if (steps == -1)
                    continue;

                if ((!PathFinder.lastIsAlternative() && steps <= 0)) {
                    if(event != null)
                        event.run();
                    return true;
                }
            }

            player.message("You can't reach that.");
            return true;
        } else {
            last = strategies;

            for (int i = 0; i < strategies.length; i++) {
                RouteStrategy strategy = strategies[i];

                int steps = PathFinder.findRoute(player, player.getSizeX(), player.getSizeY(), strategy, i == (strategies.length - 1));
                if (steps == -1)
                    continue;

                if ((!PathFinder.lastIsAlternative() && steps <= 0)) {
                    if(event != null)
                        event.run();
                    return true;
                }

                LinkedList<Integer> bufferX = PathFinder.getLastPathBufferX();
                LinkedList<Integer> bufferY = PathFinder.getLastPathBufferY();

                player.getMovement().reset();
                if (/*player.isBound() || */player.isStunned() || player.getMovement().isLocked())
                    return false;

                for (int step = steps - 1; step >= 0; step--) {
                    if (!player.getMovement().addWalkSteps(bufferX.get(step), bufferY.get(step), 25, true))
                        break;
                }

                return false;
            }

            player.message("You can't reach that.");
            return true;
        }
    }

    private boolean simpleCheck(Player player) {
        if (object instanceof GameObject) {
            return player.getZ() == ((GameObject) object).getZ();
        } else if (object instanceof FloorItem) {
            return player.getZ() == ((FloorItem) object).getTile().getZ();
        } else if (object instanceof Entity) {
            return player.getZ() == ((Entity) object).getZ();
        } else if (object instanceof Position) {
            return player.getZ() == ((Position) object).getZ();//TODO fix to match matrix 830
        } else if(object == null) {
            return false;
        } else {
            throw new RuntimeException(object + " is not instanceof any reachable entity.");
        }
    }

    private RouteStrategy[] generateStrategies() {
        if (object instanceof GameObject) {
            return new RouteStrategy[]{new ObjectStrategy((GameObject) object)};
        } else if (object instanceof FloorItem) {
            FloorItem item = (FloorItem) object;
            return new RouteStrategy[] { new FixedTileStrategy(item.getTile().getX(), item.getTile().getY()), new FloorItemStrategy(item) };
        } else if (object instanceof Figure && combat) {
            return new RouteStrategy[] { new CombatStrategy((Figure) object) };
        } else if (object instanceof Entity) {
            return new RouteStrategy[] { new EntityStrategy((Entity) object) };
        } else if(object instanceof Position) {
            Position position = (Position) object;
            return new RouteStrategy[] { new FixedTileStrategy(position.getX(), position.getY()) };
        } else {
            throw new RuntimeException(object + " is not instanceof any reachable entity.");
        }
    }

    private boolean match(RouteStrategy[] a1, RouteStrategy[] a2) {
        if (a1.length != a2.length)
            return false;
        for (int i = 0; i < a1.length; i++)
            if (!a1[i].equals(a2[i]))
                return false;
        return true;
    }
}
