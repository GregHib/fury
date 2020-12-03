package com.fury.game.content.skill.free.fishing;

import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.world.map.Position;

/**
 * Created by Greg on 20/12/2016.
 */
public class FishingSpotsHandler {

    public enum Spots {
        SPOT_1(new Position(2836, 3431), new Position(2845, 3429)),
        SPOT_2(new Position(2853, 3423), new Position(2860, 3426)),
        SPOT_3(new Position(3110, 3432), new Position(3104, 3423)),
        SPOT_4(new Position(3104, 3424), new Position(3110, 3433)),
        SPOT_5(new Position(3632, 5082), new Position(3621, 5087)),
        SPOT_6(new Position(3625, 5083), new Position(3617, 5087)),
        SPOT_7(new Position(3621, 5119), new Position(3617, 5123)),
        SPOT_8(new Position(3628, 5136), new Position(3633, 5137)),
        SPOT_9(new Position(3637, 5139), new Position(3634, 5148)),
        SPOT_10(new Position(3652, 5141), new Position(3658, 5145)),
        SPOT_11(new Position(3680, 5110), new Position(3675, 5114))
        ;

        public Position getFirstSpot() {
            return firstSpot;
        }

        public Position getSecondSpot() {
            return secondSpot;
        }

        Position firstSpot, secondSpot;
        Spots(Position firstSpot, Position secondSpot) {
            this.firstSpot = firstSpot;
            this.secondSpot = secondSpot;
        }

        public static Spots get(Position pos) {
            for(Spots spot : Spots.values()) {
                if(pos.sameAs(spot.getFirstSpot()) || pos.sameAs(spot.getSecondSpot())) {
                    return spot;
                }
            }
            return null;
        }
    }

    public static boolean moveSpot(Mob mob) {
        if(true)//Disabled due to incorrect spot placements/spawns (needs syncing after npc spawning fixed)
            return true;
        Spots spot = Spots.get(mob);
        Position newPos = null;
        if(spot != null) {
            if(spot.getFirstSpot().sameAs(mob)) {
                newPos = spot.getSecondSpot();
            } else if(spot.getSecondSpot().sameAs(mob)) {
                newPos = spot.getFirstSpot();
            }
        }
        if(newPos == null)
            return false;
        mob.setPosition(newPos);
        return  true;
    }

}
