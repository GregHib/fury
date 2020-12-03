package com.fury.game.world.map.instance;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.world.map.instance.impl.CorporealBeastInstance;
import com.fury.game.world.map.instance.impl.DagannothKingsInstance;
import com.fury.game.world.map.instance.impl.KalphiteQueenInstance;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BossInstanceHandler {

    private static final Map<BossInstanceType, BossInstance> instances = Collections.synchronizedMap(new HashMap<BossInstanceType, BossInstance>());

    public enum BossInstanceType {
        KALPHITE_QUEEN(KalphiteQueenInstance.class),
        CORPOREAL_BEAST(CorporealBeastInstance.class),
        DAGANNOTH_KINGS(DagannothKingsInstance.class),
        ;

        private Class<? extends BossInstance> instance;

        BossInstanceType(Class<? extends BossInstance> instance) {
            this.instance = instance;
        }

        public Class<? extends BossInstance> getInstance() {
            return instance;
        }
    }

    public static void enter(Player player, BossInstanceType type) {

        BossInstance instance = instances.get(type);

        if(instance == null) {
            try {
                instance = type.instance.getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
            if(instance != null) {
                instances.put(type, instance);
                instance.setEnter(player);
                return;
            }
        }

        if(instance != null) {
            instance.enter(player);
        } else {
            System.err.println("Error making instance: " + type);
        }
    }
}
