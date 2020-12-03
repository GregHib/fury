package com.fury.game.npc.misc;

import com.fury.cache.Revision;
import com.fury.cache.def.Loader;
import com.fury.engine.task.executor.GameExecutorManager;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.character.npc.drops.Drop;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import com.fury.game.node.entity.actor.figure.mob.drops.MobDropHandler;
import com.fury.game.node.entity.actor.figure.mob.drops.MobDrops;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.util.Misc;
import com.fury.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;

public class FireSpirit extends Mob {

    private Player target;
    private long createTime;
    private int reward = Misc.inclusiveRandom(1, 4);

    public FireSpirit(Position position, Player target) {
        super(15451, position, Revision.PRE_RS3, true);
        this.target = target;
        createTime = Misc.currentTimeMillis();
    }

    @Override
    public void processNpc() {
        if (target.getFinished() || createTime + 60000 < Misc.currentTimeMillis())
            deregister();
    }

    public void giveReward(final Player player) {
        if (player != target || player.getMovement().isLocked())
            return;

        if(player.getInventory().getSpaces() < reward) {
            player.message("The fire spirit has a reward for you, but you need more inventory space.");
            return;
        }

        player.getMovement().lock();
        player.perform(new Animation(16705, Revision.PRE_RS3));
        Animation animation = new Animation(15627, Revision.PRE_RS3);
        perform(animation);
        GameExecutorManager.fastExecutor.schedule(new TimerTask() {
            @Override
            public void run() {
                player.getMovement().unlock();
                Drop[] originalDrops = MobDrops.getDrops(15451, getRevision());

                if (originalDrops == null)
                    return;
                List<Drop> drops = new ArrayList<>();

                drops.addAll(Arrays.asList(originalDrops));
                for (Drop drop : drops) {
                    if (drop.getRate() == 100) {
                        Item item = new Item(drop.getItemId(), drop.getMinAmount() + Utils.getRandom(drop.getExtraAmount()), drop.getRevision());
                        player.getLogger().addNpcDrop(item);
                        player.getInventory().addSafe(item);
                    }
                }

                for(int i = 0; i < reward; i++) {
                    Drop drop = MobDropHandler.getDrop(originalDrops, player);
                    if (drop != null) {
                        Item item = new Item(drop.getItemId(), drop.getMinAmount() + Utils.getRandom(drop.getExtraAmount()), drop.getRevision());
                        player.getLogger().addNpcDrop(item);
                        player.getInventory().addSafe(item);
                    }
                }

                player.message("The fire spirit gives you a reward to say thank you for freeing it, before disappearing.");
                deregister();
            }
        }, Loader.getAnimation(animation.getId(), animation.getRevision()).getEmoteTime());
    }
}
