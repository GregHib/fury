package com.fury.game.content.skill.member.farming;

import com.fury.cache.Revision;
import com.fury.cache.def.Loader;

import java.util.HashMap;
import java.util.Map;

public enum SpotInfo {

    TALVERY_TREE(8388, FarmingConstants.TREES),
    FALADOR_GARDEN_TREE(8389, FarmingConstants.TREES),
    VARROCK_TREE(8390, FarmingConstants.TREES),
    LUMBRIDGE_TREE(8391, FarmingConstants.TREES),
    GNOME_TREE(19147, FarmingConstants.TREES),

    GNOME_STRONG_FRUIT_TREE(7962, FarmingConstants.FRUIT_TREES),
    GNOME_FRUIT_TREE(7963, FarmingConstants.FRUIT_TREES),
    BRIMHAVEN_FRUIT_TREE(7964, FarmingConstants.FRUIT_TREES),
    CATHERBY_FRUIT_TREE(7965, FarmingConstants.FRUIT_TREES),
    LLETYA_FRUIT_TREE(28919, FarmingConstants.FRUIT_TREES),

    FALADOR_ALLOTMENT_NORTH(8550, FarmingConstants.ALLOTMENT),
    FALADOR_ALLOTMENT_SOUTH(8551, FarmingConstants.ALLOTMENT),
    CATHERBY_ALLOTMENT_NORTH(8552, FarmingConstants.ALLOTMENT),
    CATHERBY_ALLOTMENT_SOUTH(8553, FarmingConstants.ALLOTMENT),
    ARDOUGNE_ALLOTMENT_NORTH(8554, FarmingConstants.ALLOTMENT),
    ARDOUGNE_ALLOTMENT_SOUTH(8555, FarmingConstants.ALLOTMENT),
    CANFIS_ALLOTMENT_NORTH(8556, FarmingConstants.ALLOTMENT),
    CANFIS_ALLOTMENT_SOUTH(8557, FarmingConstants.ALLOTMENT),

    YANNILE_HOPS_PATCH(8173, FarmingConstants.HOPS),
    TALVERY_HOPS_PATCH(8174, FarmingConstants.HOPS),
    LUMBRIDGE_HOPS_PATCH(8175, FarmingConstants.HOPS),
    MCGRUBOR_HOPS_PATCH(8176, FarmingConstants.HOPS),

    FALADOR_FLOWER(7847, FarmingConstants.FLOWERS),
    CATHERBY_FLOWER(7848, FarmingConstants.FLOWERS),
    ARDOUGNE_FLOWER(7849, FarmingConstants.FLOWERS),
    CANFIS_FLOWER(7850, FarmingConstants.FLOWERS),

    CHAMPIONS_BUSH(7577, FarmingConstants.BUSHES),
    RIMMINGTON_BUSH(7578, FarmingConstants.BUSHES),
    ETCETERIA_BUSH(7579, FarmingConstants.BUSHES),
    SOUTH_ARDDOUGNE_BUSH(7580, FarmingConstants.BUSHES),

    FALADOR_HERB_PATCH(8150, FarmingConstants.HERBS),
    CATHERBY_HERB_PATCH(8151, FarmingConstants.HERBS),
    ARDOUGNE_HERB_PATCH(8152, FarmingConstants.HERBS),
    CANFIS_HERB_PATCH(8153, FarmingConstants.HERBS),

    FALADOR_COMPOST_BIN(7836, FarmingConstants.COMPOST),
    CATHERBY_BIN(7837, FarmingConstants.COMPOST),
    PORT_PHASYMATIS_BIN(7838, FarmingConstants.COMPOST),
    ARDOUGN_BIN(7839, FarmingConstants.COMPOST),
    TAVERLY_BIN(66577, FarmingConstants.COMPOST),

    MUSHROOM_SPECIAL(8337, FarmingConstants.MUSHROOMS),

    BELLADONNA(7572, FarmingConstants.BELLADONNA);

    private static Map<Short, SpotInfo> informations = new HashMap<>();

    public static SpotInfo getInfo(int objectId) {
        return informations.get((short) objectId);
    }

    static {
        for (SpotInfo information : SpotInfo.values())
            informations.put((short) information.objectId, information);
    }

    private int objectId;
    private int type;

    SpotInfo(int objectId, int type) {
        this.objectId = objectId;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public int getConfigFileId() {
        return Loader.getObject(objectId, Revision.PRE_RS3).varbitIndex;
    }
}