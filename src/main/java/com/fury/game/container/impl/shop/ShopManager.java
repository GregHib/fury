package com.fury.game.container.impl.shop;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.fury.core.model.item.Item;
import com.fury.game.system.files.Resources;
import com.fury.network.login.LoginUtils;
import com.fury.util.JsonLoader;

import java.util.HashMap;
import java.util.Map;

public class ShopManager {

    public static final int GENERAL_STORE = 12;
    public static final int MERCHANT_STORE = 11;
    public static final int RECIPE_FOR_DISASTER_STORE = 36;

    public static final int VOTING_REWARDS_STORE = 27;
    public static final int IRONMAN_VOTING_REWARDS_STORE = 120;
    public static final int PKING_REWARDS_STORE = 26;
    public static final int ENERGY_FRAGMENT_STORE = 33;
    public static final int AGILITY_TICKET_STORE = 39;
    public static final int GRAVEYARD_STORE = 42;
    public static final int TOKKUL_EXCHANGE_STORE = 43;
    public static final int STARDUST_EXCHANGE_STORE = 78;
    public static final int SKILLCAPE_STORE_HOODS = 8;
    public static final int SKILLCAPE_STORE = 9;
    public static final int SKILLCAPE_STORE_T = 10;
    public static final int MASTER_CAPE_STORE = 128;
    public static final int GAMBLING_STORE = 41;
    public static final int DUNGEONEERING_STORE = 44;
    public static final int SLAYER_STORE = 47;
    public static final int DONATION_POINTS_STORE = 24;
    public static final int MEMBERS_STORE_II = 25;
    public static final int ACHIEVEMENTS_STORE = 117;
    public static final int SANTAS_SNOWFLAKE_SHOP = 123;
    public static final int SLAYER_REWARDS_SHOP = 124;
    public static final int STRANGE_SKILL_ROCK_SHOP = 127;
    public static final int LEGENDS_SHOP = 129;
    public static final int WILDERNESS_SLAYER_REWARDS_SHOP = 130;

    private static Map<Integer, Shop> shops = new HashMap<Integer, Shop>();

    public static void addShop(int shopId, Shop shop) {
        shops.put(shopId, shop);
    }

    public static Map<Integer, Shop> getShops() {
        return shops;
    }

    public static JsonLoader parseShops() {
        return new JsonLoader() {
            @Override
            public void load(JsonObject reader, Gson builder) {
                int id = reader.get("id").getAsInt();
                String name = reader.get("name").getAsString();
                Item[] items = LoginUtils.getItemContainer(reader.get("items").getAsJsonArray());
                Item currency = new Item(reader.get("currency").getAsInt());
                shops.put(id, new Shop(null, id, name, currency, items, false));
            }

            @Override
            public String filePath() {
                return Resources.getFile("world_shops");
            }
        };
    }

    public static Object[] getCustomShopData(int shop, int item) {
        if(shop == SANTAS_SNOWFLAKE_SHOP) {
            switch (item) {
                case 22985:
                    return new Object[]{20000, "Snowflakes"};
                case 15422:
                    return new Object[]{1000, "Snowflakes"};
                case 15423:
                    return new Object[]{2000, "Snowflakes"};
                case 15425:
                    return new Object[]{1500, "Snowflakes"};
            }
        } else if(shop == SLAYER_REWARDS_SHOP) {
            switch (item) {
                case 10551:
                    return new Object[]{300, "Slayer Points"};
                case 2572:
                    return new Object[]{15, "Slayer Points"};
                case 15014:
                    return new Object[]{250, "Slayer Points"};
                case 8850:
                    return new Object[]{40, "Slayer Points"};
                case 11732:
                    return new Object[]{80, "Slayer Points"};
                case 13734:
                    return new Object[]{100, "Slayer Points"};
                case 989:
                    return new Object[]{10, "Slayer Points"};
                case 3140:
                    return new Object[]{25, "Slayer Points"};
                case 4087:
                    return new Object[]{15, "Slayer Points"};
                case 15220:
                    return new Object[]{100, "Slayer Points"};
                case 15019:
                    return new Object[]{75, "Slayer Points"};
                case 15018:
                    return new Object[]{100, "Slayer Points"};
                case 15020:
                    return new Object[]{75, "Slayer Points"};
                case 14478:
                    return new Object[]{350, "Slayer Points"};
            }
        } else if(shop == WILDERNESS_SLAYER_REWARDS_SHOP) {
            switch (item) {
                case 22370:
                    return new Object[]{2, "Slayer Points"};
                case 4151:
                    return new Object[]{30, "Slayer Points"};
                case 11732:
                    return new Object[]{25, "Slayer Points"};
                case 15300:
                    return new Object[]{10, "Slayer Points"};
                case 6737:
                    return new Object[]{90, "Slayer Points"};
                case 6733:
                    return new Object[]{85, "Slayer Points"};
                case 6731:
                    return new Object[]{80, "Slayer Points"};
                case 6735:
                    return new Object[]{75, "Slayer Points"};
                case 24133:
                    return new Object[]{15, "Slayer Points"};

            }
        } else if(shop == STRANGE_SKILL_ROCK_SHOP) {
            switch (item) {
                case 2949:
                    return new Object[]{5000, "Strange Skill Points"};
                case 775:
                    return new Object[]{8000, "Strange Skill Points"};
                case 776:
                    return new Object[]{4000, "Strange Skill Points"};
                case 10075:
                    return new Object[]{10000, "Strange Skill Points"};
                case 7409:
                    return new Object[]{7500, "Strange Skill Points"};
                case 13659:
                    return new Object[]{5000, "Strange Skill Points"};
                case 11259:
                    return new Object[]{6000, "Strange Skill Points"};
                case 15439:
                    return new Object[]{10000, "Strange Skill Points"};
            }
        } else if (shop == VOTING_REWARDS_STORE || shop == IRONMAN_VOTING_REWARDS_STORE) {
            switch (item) {
                case 4151:
                    return new Object[]{10, "Voting points"};
                case 18744:
                case 18745:
                case 18746:
                    return new Object[]{40, "Voting points"};
                case 19333:
                    return new Object[]{25, "Voting points"};
                case 15332:
                    return new Object[]{1, "Voting point"};
                case 27996:
                    return new Object[]{50, "Voting points"};
                case 19336:
                    return new Object[]{30, "Voting points"};
                case 19337:
                    return new Object[]{50, "Voting points"};
                case 19338:
                    return new Object[]{40, "Voting points"};
                case 19339:
                    return new Object[]{35, "Voting points"};
                case 19340:
                    return new Object[]{35, "Voting points"};
                case 25312:
                    return new Object[]{25, "Voting points"};
                case 20072:
                    return new Object[]{15, "Voting points"};
                case 14490:
                    return new Object[]{8, "Voting points"};
                case 14492:
                    return new Object[]{10, "Voting points"};
                case 14494:
                    return new Object[]{5, "Voting points"};
                case 11732:
                    return new Object[]{8, "Voting points"};
                case 18830:
                    return new Object[]{1, "Voting points"};
                case 12473:
                    return new Object[]{50, "Voting points"};
                case 12471:
                    return new Object[]{75, "Voting points"};
                case 12469:
                    return new Object[]{100, "Voting points"};
                case 12475:
                    return new Object[]{250, "Voting points"};
                case 13642:
                    return new Object[]{100, "Voting points"};
                case 9084:
                    return new Object[]{5, "Voting points"};
            }
        } else if (shop == PKING_REWARDS_STORE) {
            switch (item) {
                case 13858:
                case 13861:
                case 13870:
                case 13873:
                    return new Object[]{80, "Pk points"};
                case 13864:
                case 13876:
                    return new Object[]{40, "Pk points"};
                case 13867:
                case 13896:
                    return new Object[]{50, "Pk points"};
                case 13879:
                case 13883:
                    return new Object[]{1, "Pk point"};
                case 13884:
                case 13890:
                    return new Object[]{90, "Pk points"};
                case 13902:
                    return new Object[]{60, "Pk points"};
                case 13887:
                case 13893:
                    return new Object[]{100, "Pk points"};
                case 13899:
                    return new Object[]{75, "Pk points"};
                case 13905:
                    return new Object[]{70, "Pk points"};

            }
        } else if (shop == ENERGY_FRAGMENT_STORE) {
            switch (item) {
                case 5509:
                    return new Object[]{400, "energy fragments"};
                case 5510:
                    return new Object[]{750, "energy fragments"};
                case 5512:
                    return new Object[]{1100, "energy fragments"};
            }
        } else if (shop == AGILITY_TICKET_STORE) {
            switch (item) {
                case 14936:
                case 14938:
                    return new Object[]{200, "agility tickets"};
                case 10071:
                    return new Object[]{75, "agility tickets"};
                case 88:
                    return new Object[]{50, "agility tickets"};
            }
        } else if (shop == STARDUST_EXCHANGE_STORE) {
            switch (item) {
                case 6180:
                case 6181:
                case 6182:
                    return new Object[]{2500, "stardust"};
                case 7409:
                    return new Object[]{3500, "stardust"};
                case 13661:
                    return new Object[]{5000, "stardust"};
                case 453:
                    return new Object[]{5, "stardust"};
                case 9185:
                    return new Object[]{250, "stardust"};
            }
        } else if (shop == GRAVEYARD_STORE) {
            switch (item) {
                case 18337:
                    return new Object[]{350, "zombie fragments"};
                case 10551:
                    return new Object[]{500, "zombie fragments"};
                case 10548:
                case 10549:
                case 10550:
                    return new Object[]{200, "zombie fragments"};
                case 7592:
                case 7593:
                case 7594:
                case 7595:
                case 7596:
                    return new Object[]{25, "zombie fragments"};
                case 15241:
                    return new Object[]{500, "zombie fragments"};
                case 15243:
                    return new Object[]{2, "zombie fragments"};
            }
        } else if (shop == TOKKUL_EXCHANGE_STORE) {
            switch (item) {
                case 438:
                case 436:
                    return new Object[]{80, "tokkul"};
                case 440:
                    return new Object[]{350, "tokkul"};
                case 442:
                    return new Object[]{3500, "tokkul"};
                case 453:
                    return new Object[]{770, "tokkul"};
                case 444:
                    return new Object[]{3000, "tokkul"};
                case 447:
                    return new Object[]{2430, "tokkul"};
                case 449:
                    return new Object[]{3900, "tokkul"};
                case 451:
                    return new Object[]{5800, "tokkul"};
                case 1623:
                    return new Object[]{540, "tokkul"};
                case 1621:
                    return new Object[]{950, "tokkul"};
                case 1619:
                    return new Object[]{1600, "tokkul"};
                case 1617:
                    return new Object[]{3100, "tokkul"};
                case 1631:
                    return new Object[]{8900, "tokkul"};
                case 6571:
                    return new Object[]{200000, "tokkul"};
                case 6522:
                    return new Object[]{250, "tokkul"};
                case 6523:
                    return new Object[]{35000, "tokkul"};
                case 6525:
                    return new Object[]{20000, "tokkul"};
                case 6526:
                    return new Object[]{30000, "tokkul"};
                case 6527:
                    return new Object[]{25000, "tokkul"};
            }
        } else if (shop == DUNGEONEERING_STORE) {
            switch (item) {
                case 18351:
                case 18349:
                case 18353:
                case 18357:
                case 18355:
                case 18359:
                case 18361:
                case 18363:
                    return new Object[]{200000, "Dungeoneering tokens"};
                case 18344:
                    return new Object[]{153000, "Dungeoneering tokens"};
                case 18839:
                    return new Object[]{140000, "Dungeoneering tokens"};
                case 18335:
                    return new Object[]{75000, "Dungeoneering tokens"};
            }
        } else if (shop == SLAYER_STORE) {
            switch (item) {
                case 13263:
                    return new Object[]{250, "Slayer points"};
                case 13281:
                    return new Object[]{5, "Slayer points"};
                case 15403:
                case 11730:
                case 10887:
                case 15241:
                    return new Object[]{300, "Slayer points"};
                case 11235:
                case 4151:
                case 15486:
                    return new Object[]{250, "Slayer points"};
                case 15243:
                    return new Object[]{3, "Slayer points"};
                case 10551:
                    return new Object[]{200, "Slayer points"};
                case 2572:
                    return new Object[]{500, "Slayer points"};
                case 21787:
                case 21790:
                case 21793:
                    return new Object[]{450, "Slayer points"};
            }
        } else if (shop == DONATION_POINTS_STORE) {
            switch (item) {
                case 6199:
                    return new Object[]{199, "Donor Points"};
                case 25202:
                    return new Object[]{199, "Donor Points"};
                case 13661:
                    return new Object[]{2499, "Donor Points"};
                case 15272:
                    return new Object[]{1, "Donor Point"};
                case 15270:
                    return new Object[]{1, "Donor Point"};
                case 18830:
                    return new Object[]{8, "Donor Points"};
                case 2364:
                    return new Object[]{3, "Donor Points"};
                case 2362:
                    return new Object[]{2, "Donor Points"};
                case 23352:
                    return new Object[]{10, "Donor Points"};
                case 13740:
                    return new Object[]{3499, "Donor Points"};
                case 14484:
                    return new Object[]{1999, "Donor Points"};
                case 11694:
                    return new Object[]{1249, "Donor Points"};
                case 4151:
                    return new Object[]{199, "Donor Points"};
                case 962:
                    return new Object[]{14999, "Donor Points"};
                case 11848:
                    return new Object[]{749, "Donor Points"};
                case 22358:
                    return new Object[]{7999, "Donor Points"};
                case 13738:
                    return new Object[]{1499, "Donor Points"};
                case 15220:
                    return new Object[]{499, "Donor Points"};
                case 6585:
                    return new Object[]{199, "Donor Points"};
                case 21787:
                    return new Object[]{1199, "Donor Points"};
                case 21790:
                    return new Object[]{1199, "Donor Points"};
                case 21793:
                    return new Object[]{999, "Donor Points"};
                case 11724:
                    return new Object[]{999, "Donor Points"};
                case 11726:
                    return new Object[]{999, "Donor Points"};
                case 11283:
                    return new Object[]{1199, "Donor Points"};
                case 22346:
                    return new Object[]{11999, "Donor Points"};
                case 22348:
                    return new Object[]{13999, "Donor Points"};
                case 22347:
                    return new Object[]{9999, "Donor Points"};
                case 11235:
                    return new Object[]{299, "Donor Points"};
                case 15486:
                    return new Object[]{399, "Donor Points"};
                case 17291:
                    return new Object[]{2999, "Donor Points"};
                case 15398:
                    return new Object[]{4499, "Donor Points"};
                case 23643:
                    return new Object[]{14999, "Donor Points"};
                case 22494:
                    return new Object[]{2499, "Donor Points"};
            }
        } else if (shop == MEMBERS_STORE_II) {
            switch (item) {
                case 21371:
                    return new Object[]{10, "Member Points"};
                case 18744:
                case 18745:
                case 18746:
                    return new Object[]{15, "Member Points"};
                case 962:
                case 1048:
                case 1046:
                    return new Object[]{40, "Member Points"};
                case 1042:
                case 1038:
                    return new Object[]{35, "Member Points"};
                case 1044:
                case 1040:
                    return new Object[]{30, "Member Points"};
                case 1055:
                case 1053:
                case 1057:
                case 19293:
                    return new Object[]{25, "Member Points"};
                case 1050:
                    return new Object[]{60, "Member Points"};
                case 13744:
                case 13738:
                case 13742:
                case 13740:
                    return new Object[]{15, "Member Points"};
                case 2572:
                    return new Object[]{10, "Member Points"};
                case 12601:
                case 12603:
                case 12605:
                    return new Object[]{10, "Member Points"};
                case 15018:
                case 15020:
                case 15220:
                    return new Object[]{5, "Member Points"};

                case 15441:
                case 15442:
                case 15443:
                case 15444:
                    return new Object[]{5, "Member Points"};
                case 21787:
                case 21790:
                case 21793:
                    return new Object[]{10, "Member Points"};
                case 11995:
                case 11996:
                case 11997:
                case 12001:
                case 12002:
                case 11991:
                case 11992:
                case 11987:
                case 11989:
                case 12004:
                    return new Object[]{10, "Member Points"};
                case 20929:
                    return new Object[]{75, "Member Points"};
                case 11858:
                case 11860:
                case 11862:
                case 19580:
                    return new Object[]{40, "Member Points"};
            }
        }
        return null;
    }
}
