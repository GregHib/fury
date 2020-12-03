package com.fury.game.content.skill.member.hunter;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.content.skill.Skill;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.character.player.content.objects.PrivateObjectManager;
import com.fury.game.entity.object.GameObject;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.util.Misc;

import java.util.List;

public class HunterTrapMob extends Mob {

    private Traps trap;
    private HunterData hunterData;

    private GameObject o;
    private int captureTicks;

    public HunterTrapMob(HunterData hunter, int id, Position position, boolean spawned) {
        super(id, position, spawned);
        this.hunterData = hunter;
        this.trap = hunterData.getTrap();
    }

    @Override
    public void processNpc() {
        super.processNpc();
        if (captureTicks > 0) {
            captureTicks++;
            if(captureTicks == 4) {
                Player player = PrivateObjectManager.getObjectPlayer(o);
                if (player == null || player.getSkills().getLevel(Skill.HUNTER) < hunterData.getLureLevel()) {
                    setCantInteract(false);
                    o = null;
                    captureTicks = 0;
                    return;
                }
            } else if (captureTicks == 5) {
                if (hunterData.equals(HunterData.CRIMSON_SWIFT) || hunterData.equals(HunterData.GOLDEN_WARBLER) || hunterData.equals(HunterData.COPPER_LONGTAIL) || hunterData.equals(HunterData.CERULEAN_TWITCH) || hunterData.equals(HunterData.TROPICAL_WAGTAIL) || hunterData.equals(HunterData.WIMPY_BIRD) || hunterData.equals(HunterData.PAWYA) || hunterData.equals(HunterData.GRENWALL))
                    getMovement().addWalkSteps(o.getX(), o.getY(), -1, false);
            } else if (captureTicks == 6) {
                perform(new Animation(hunterData.getCatchAnimId()));
            } else if (captureTicks == 7) {// up to five
                if (!PrivateObjectManager.convertIntoObject(o, new GameObject(hunterData.getObjectId(), o.copyPosition(), o.getType(), o.getDirection()), p -> {
                    if (p == null || isDead())
                        return false;
                    int currentLevel = p.getSkills().getLevel(Skill.HUNTER), lureLevel = hunterData.getLureLevel();
                    double ratio = ((double) (trap.getRequirementLevel() + 20) / lureLevel) * currentLevel;
                    if (currentLevel < lureLevel || ratio < Misc.random(100))
                        return false;
                    return true;
                })) {
                    if(trap.isNet()) {
                        o.animate(5270);
                        Player player = PrivateObjectManager.getObjectPlayer(o);
                        if(player != null)
                            TrapAction.sendTrapRewards(player, o, trap, hunterData, false);
                    } else {
                        int anim = hunterData.getFailAnimId();
                        if (anim != -1)
                            perform(new Animation(anim));
                        PrivateObjectManager.convertIntoObject(o, new GameObject(trap.getFailObjectId(), o.copyPosition(), o.getType(), o.getDirection()), null);
                    }
                } else {
                    if (trap.isNet()) {
                        Player player = PrivateObjectManager.getObjectPlayer(o);
                        if (player != null)
                            TrapAction.sendTrapRewards(player, o, trap, hunterData, true);
                    }
                    setRespawnTask();
                }
            } else if (captureTicks == 8) {
                setCantInteract(false);
            } else if (captureTicks == 10) {
                o = null;
                captureTicks = 0;
            }
            return;
        }

        if (o != null || getFinished())
            return;
        List<GameObject> objects = GameWorld.getRegions().get(getRegionId()).getSpawnedObjects();
        if (objects == null)
            return;
        for (final GameObject object : objects) {
            Player player = PrivateObjectManager.getObjectPlayer(object);

            if (player == null || player.getSkills().getLevel(Skill.HUNTER) < hunterData.getLureLevel())
                continue;

            if (object.getId() != trap.getObjectId() || !isWithinDistance(object, 4) || Misc.random(50) != 0)
                continue;
            this.o = object;
            this.captureTicks = 1;
            setCantInteract(true);
            getMovement().reset();
            calcFollow(object, true);
            getDirection().faceObject(object);
            break;
        }
    }
}
