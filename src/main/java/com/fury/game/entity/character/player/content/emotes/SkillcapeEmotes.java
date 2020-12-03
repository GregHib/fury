package com.fury.game.entity.character.player.content.emotes;

import com.fury.cache.Revision;
import com.fury.core.model.item.Item;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.game.world.update.flag.block.graphic.Graphic;

public enum SkillcapeEmotes {
    ATTACK(new Item[]{new Item(9747), new Item(9748), new Item(10639), new Item(31268, Revision.RS3)}, 4959, 823, 40),
    DEFENCE(new Item[]{new Item(9753), new Item(9754), new Item(10641), new Item(31270, Revision.RS3)}, 4961, 824, 40),
    STRENGTH(new Item[]{new Item(9750), new Item(9751), new Item(10640), new Item(31269, Revision.RS3)}, 4981, 828, 100),
    CONSTITUTION(new Item[]{new Item(9768), new Item(9769), new Item(10647), new Item(31276, Revision.RS3)}, 14242, 2745, 70),
    RANGED(new Item[]{new Item(9756), new Item(9757), new Item(10642), new Item(31271, Revision.RS3)}, 4973, 832, 65),
    PRAYER(new Item[]{new Item(9759), new Item(9760), new Item(10643), new Item(31272, Revision.RS3)}, 4979, 829, 70),
    MAGIC(new Item[]{new Item(9762), new Item(9763), new Item(10644), new Item(31273, Revision.RS3)}, 4939, 813, 45),
    COOKING(new Item[]{new Item(9801), new Item(9802), new Item(10658), new Item(31288, Revision.RS3)}, 4955, 821, 150),
    WOODCUTTING(new Item[]{new Item(9807), new Item(9808), new Item(10660), new Item(31290, Revision.RS3)}, 4957, 822, 120),
    FLETCHING(new Item[]{new Item(9783), new Item(9784), new Item(10652), new Item(31281, Revision.RS3)}, 4937, 812, 90),
    FISHING(new Item[]{new Item(9798), new Item(9799), new Item(10657), new Item(31287, Revision.RS3)}, 4951, 819, 80),
    FIREMAKING(new Item[]{new Item(9804), new Item(9805), new Item(10659), new Item(31289, Revision.RS3)}, 4975, 831, 60),
    CRAFTING(new Item[]{new Item(9780), new Item(9781), new Item(10651), new Item(31280, Revision.RS3)}, 4949, 818, 90),
    SMITHING(new Item[]{new Item(9795), new Item(9796), new Item(10656), new Item(31286, Revision.RS3)}, 4943, 815, 100),
    MINING(new Item[]{new Item(9792), new Item(9793), new Item(10655), new Item(31285, Revision.RS3)}, 4941, 814, 45),
    HERBLORE(new Item[]{new Item(9774), new Item(9775), new Item(10649), new Item(31278, Revision.RS3)}, 4969, 835, 85),
    AGILITY(new Item[]{new Item(9771), new Item(9772), new Item(10648), new Item(31277, Revision.RS3)}, 4977, 830, 55),
    THIEVING(new Item[]{new Item(9777), new Item(9778), new Item(10650), new Item(31279, Revision.RS3)}, 4965, 826, 90),
    SLAYER(new Item[]{new Item(9786), new Item(9787), new Item(10653), new Item(31282, Revision.RS3)}, 4967, 1656, 60),
    FARMING(new Item[]{new Item(9810), new Item(9811), new Item(10661), new Item(31291, Revision.RS3)}, 4963, 825, 80),
    RUNECRAFTING(new Item[]{new Item(9765), new Item(9766), new Item(10645), new Item(31274, Revision.RS3)}, 4947, 817, 75),
    HUNTER(new Item[]{new Item(9948), new Item(9949), new Item(10646), new Item(31275, Revision.RS3)}, 5158, 907, 95),
    CONSTRUCTION(new Item[]{new Item(9789), new Item(9790), new Item(10654), new Item(31283, Revision.RS3)}, 4953, 820, 80),
    SUMMONING(new Item[]{new Item(12169), new Item(12170), new Item(12524), new Item(31292, Revision.RS3)}, 8525, 1515, 60),
    DUNGEONEERING(new Item[]{new Item(18508),new Item(18509)}, -1, -1, 100),
    DUNGEONEERING_MASTER(new Item[]{new Item(19709), new Item(19710)}, -1, -1, 85),
    QUEST_POINT(new Item[]{new Item(9813), new Item(9814), new Item(10662), new Item(36166, Revision.RS3)}, 4945, 816, 100),
    MAX_CAPE(new Item[]{new Item(20767)}, -1, -1, 5),
    COMPLETIONIST_CAPE(new Item[]{new Item(20769), new Item(20771)}, -1, -1, 40),
    VETERAN_CAPE(new Item[]{new Item(20763)}, 352, 1446, 90),
    CLASSIC_CAPE(new Item[]{new Item(20765)}, 122, 1466, 55);

    SkillcapeEmotes(Item[] items, int animationId, int graphicId, int delay) {
        this.items = items;
        animation = new Animation(animationId);
        graphic = new Graphic(graphicId);
        this.delay = delay;
    }

    private final Item[] items;
    private final Animation animation;
    private final Graphic graphic;
    private final int delay;

    public static SkillcapeEmotes forItem(Item skillcape) {
        for (SkillcapeEmotes data : values()) {
            for (Item item : data.items)
                if (item.isEqual(skillcape))
                    return data;
        }
        return null;
    }

    public int getDelay() {
        return delay;
    }

    public Item[] getItems() {
        return items;
    }

    public Animation getAnimation() {
        return animation;
    }

    public Graphic getGraphic() {
        return graphic;
    }
}
