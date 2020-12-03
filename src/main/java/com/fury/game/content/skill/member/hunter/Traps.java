package com.fury.game.content.skill.member.hunter;

import com.fury.core.model.item.Item;

public enum Traps {
    BOX(-1, 19187, 19192, 27, 5208, 5208, new Item(10008)),
    SNARE(-1, 19175, 19174, 1, 5208, 5207, new Item(10006)),
    SWAMP_NET(19679, 19678, 19677, 29, 827, 827, new Item(954), new Item(303)),
    ORANGE_NET(19652, 19650, -1, 47, 827, 827, new Item(954), new Item(303)),
    RED_NET(19663, 19662, -1, 59, 827, 827, new Item(954), new Item(303)),
    BLACK_NET(19671, 19670, -1, 67, 827, 827, new Item(954), new Item(303)),
    BOULDER_TRAP(19205, 19206, 19219, 23, 5208, 5208, new Item(1511));
    //PITFALL(0, 0, 0, 31, 0, 0, null);

    private int startObjectId;
    private int objectId;
    private int failObjectId;
    private int requirementLevel;
    private int startAnimId;
    private int endAnimId;
    private Item[] requiredItems;

    Traps(int startObjectId, int objectId, int failObjectId, int requirementLevel, int startAnim, int endAnim, Item... required) {
        this.startObjectId = startObjectId;
        this.objectId = objectId;
        this.failObjectId = failObjectId;
        this.requirementLevel = requirementLevel;
        this.requiredItems = required;
        this.startAnimId = startAnim;
        this.endAnimId = endAnim;
    }

    public int getStartObjectId() {
        return startObjectId;
    }

    public int getObjectId() {
        return objectId;
    }

    public int getFailObjectId() {
        return failObjectId;
    }

    public int getRequirementLevel() {
        return requirementLevel;
    }

    public boolean isItem() {
        return this == BOX || this == SNARE;
    }

    public Item[] getRequiredItems() {
        return requiredItems;
    }

    public int getStartAnimId() {
        return startAnimId;
    }

    public int getEndAnimId() {
        return endAnimId;
    }

    public boolean giveItemsBack() {
        return this == BOX || this == SNARE || isNet();
    }

    public boolean isNet() {
        return this == SWAMP_NET || this == ORANGE_NET || this == RED_NET || this == BLACK_NET;
    }
}
