package com.fury.game.content.global.config;

import com.fury.core.model.node.entity.actor.figure.player.Player;

public class ConfigManager {

    private static final int SIZE = 12000;
    private final Player player;
    private int[] configs = new int[SIZE];

    public ConfigManager(Player player) {
        this.player = player;
    }

    public void reset() {
        configs = new int[SIZE];
    }

    public void init() {
        for (int i = 0; i < configs.length; i++) {
            int config = configs[i];
            if(config != 0)
                send(i, config, false);
        }
        if(get(ConfigConstants.AIR_ALTAR) == 0) {
            send(ConfigConstants.AIR_ALTAR, 1, true);
            send(ConfigConstants.MIND_ALTAR, 1, true);
            send(ConfigConstants.WATER_ALTAR, 1, true);
            send(ConfigConstants.EARTH_ALTAR, 1, true);
            send(ConfigConstants.FIRE_ALTAR, 1, true);
            send(ConfigConstants.BODY_ALTAR, 1, true);
            send(ConfigConstants.CHAOS_ALTAR, 1, true);
            send(ConfigConstants.NATURE_ALTAR, 1, true);
            send(ConfigConstants.LAW_ALTAR, 1, true);
        }
        if(get(ConfigConstants.KALPHITE_ENTRANCE) == 0) {
            send(ConfigConstants.KALPHITE_ENTRANCE, 1, true);
            send(ConfigConstants.KALPHITE_QUEEN_ENTRANCE, 1, true);
        }

        if(get(ConfigConstants.LUMBRIDGE_CELLAR_WALL) == 0)
            send(ConfigConstants.LUMBRIDGE_CELLAR_WALL, 11, true);

        if(get(ConfigConstants.TAVERLEY_BALLOON) == 0)
            send(ConfigConstants.TAVERLEY_BALLOON, 3, true);
    }

    public void send(int config, int state) {
        send(config, state, false);
    }

    public void send(int config, int state, boolean save) {
        player.getPacketSender().sendConfigByFile(config, state);
        if(save)
            setConfig(config, state);
    }

    private void setConfig(int config, int state) {
        if(config < configs.length)
            configs[config] = state;
    }

    public int[] configs() {
        return configs;
    }

    public void setConfigs(int[] configs) {
        this.configs = configs;
    }

    public int get(int config) {
        if(config < 0 || config >= configs.length)
            return 0;
        return configs[config];
    }
}
