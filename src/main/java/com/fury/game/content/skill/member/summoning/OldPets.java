/**
 * Credit: Greg
 */
package com.fury.game.content.skill.member.summoning;

public enum OldPets {
    KITTEN_DARK_GREY(761, 1555),
    KITTEN_WHITE(762, 1556),
    KITTEN_MUSK(763, 1557),
    KITTEN_BLACK(764, 1558),
    KITTEN_BROWN(765, 1559),
    KITTEN_LIGHT_GREY(766, 1560),
    CAT_DARK_GREY(768, 1561),
    CAT_WHITE(769, 1562),
    CAT_MUSK(770, 1563),
    CAT_BLACK(771, 1564),
    CAT_BROWN(772, 1565),
    CAT_LIGHT_GREY(773, 773, 1566),
    OVERGROWN_HELLCAT(3503, 7581),
    HELLCAT(3504, 7582),
    HELL_KITTEN(3505, 7583),
    LAZY_HELLCAT(3506, 7584),
    WILLY_HELLCAT(3507, 7585),
    BABY_RED_DRAGON(6901, 12470),
    BABY_BLUE_DRAGON(6903, 12472),
    BABY_GREEN_DRAGON(6905, 12474),
    BABY_BLACK_DRAGON(6907, 12476),
    TERRIER_PUPPY_TAN(6958, 12512),
    TERRIER_TAN(6959, 12513),
    TERRIER_PUPPY_GREY(7237, 12700),
    TERRIER_GREY(7238, 12701),
    TERRIER_PUPPY_BLACK(7239, 12702),
    TERRIER_BLACK(7240, 12703),
    BULLDOG_PUPPY(6969, 12522),
    BULLDOG(6968, 12523),
    BULLDOG_PUPPY_GREY(7259, 12720),
    BULLDOG_GREY(7257, 12721),
    BULLDOG_PUPPY_NAVY(7260, 12722),
    BULLDOG_NAVY(7258, 12723),
    DALMATIAN_PUPPY_BLACK(6964, 12518),
    DALMATIAN_BLACK(6965, 12519),
    DALMATIAN_PUPPY_BLUE(7249, 12712),
    DALMATIAN_BLUE(7250, 12713),
    DALMATIAN_PUPPY_RED(7251, 12714),
    DALMATIAN_RED(7252, 12715),
    GREYHOUND_PUPPY_BROWN(6960, 12514),
    GREYHOUND_BROWN(6961, 12515),
    GREYHOUND_PUPPY_GREY(7241, 12704),
    GREYHOUND_GREY(7242, 12705),
    GREYHOUND_PUPPY_DARK_BROWN(7243, 12706),
    GREYHOUND_DARK_BROWN(7244, 12707),
    LABRADOR_PUPPY_YELLOW(6962, 12516),
    LABRADOR_YELLOW(6963, 12517),
    LABRADOR_PUPPY_BLACK(7245, 12708),
    LABRADOR_BLACK(7246, 12709),
    LABRADOR_PUPPY_GREY(7247, 12710),
    LABRADOR_GREY(7248, 12711),
    SHEEPDOG_PUPPY_BLACK(6966, 12520),
    SHEEPDOG_BLACK(6967, 12521),
    SHEEPDOG_PUPPY_GREY(7253, 12716),
    SHEEPDOG_GREY(7254, 12717),
    SHEEPDOG_PUPPY_YELLOW(7255, 12718),
    SHEEPDOG_YELLOW(7256, 12719),
    BABY_PENGUIN_GREY(6908, 12481),
    PENGUIN_GREY(6909, 12482),
    BABY_PENGUIN_BROWN(7313, 12763),
    PENGUIN_BROWN(7314, 12762),
    BABY_PENGUIN_BLUE(7316, 12765),
    PENGUIN_BLUE(7317, 12764),
    SNEAKERPEEPER_SPAWN(13089, 19894),;

    OldPets(int npcId, int spawnNpcId, int itemId) {
        this.npcId = npcId;
        this.spawnNpcId = spawnNpcId;
        this.itemId = itemId;
    }

    OldPets(int npcId, int itemId) {
        this.npcId = npcId;
        this.spawnNpcId = npcId;
        this.itemId = itemId;
    }

    public int npcId, spawnNpcId, itemId;

    public static OldPets forId(int itemId) {
        for (OldPets p : OldPets.values()) {
            if (p != null && p.itemId == itemId) {
                return p;
            }
        }
        return null;
    }

    public static OldPets forSpawnId(int spawnNpcId) {
        for (OldPets p : OldPets.values()) {
            if (p != null && p.spawnNpcId == spawnNpcId) {
                return p;
            }
        }
        return null;
    }

}
