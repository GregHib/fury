package com.fury.game.system.files.loaders.item;

import com.fury.core.model.item.Item;
import com.fury.game.entity.character.player.content.Effigies;
import com.fury.game.entity.character.player.content.OrnamentKits;

public class ItemConstants {

    public static int getDegradeGearRate() {
        return 1;
    }

    public static int getItemDefaultCharges(int id) {
        // Pvp Armors
        if (id == 13910 || id == 13913 || id == 13916 || id == 13919 || id == 13922 || id == 13925 || id == 13928 || id == 13931 || id == 13934 || id == 13937 || id == 13940 || id == 13943 || id == 13946 || id == 13949 || id == 13952)
            return 1500 * getDegradeGearRate(); // 15minutes
        if (id == 13960 || id == 13963 || id == 13966 || id == 13969 || id == 13972 || id == 13975)
            return 2000 * getDegradeGearRate();// 20 min.
        if (id == 13860 || id == 13863 || id == 13866 || id == 13869 || id == 13872 || id == 13875 || id == 13878 || id == 13886 || id == 13889 || id == 13892 || id == 13895 || id == 13898 || id == 13901 || id == 13904 || id == 13907 || id == 13960)
            return 6000 * getDegradeGearRate(); // 1hour
        // Nex Armor
        if (id == 20137 || id == 20141 || id == 20145 || id == 20149 || id == 20153 || id == 20157 || id == 20161 || id == 20165 || id == 20169)
            return 60000 * getDegradeGearRate(); // 10 hour
        //Crystal bow
        if (id == 4214 || id == 4215 || id == 4216 || id == 4217 || id == 4218 || id == 4219 || id == 4220 || id == 4221 || id == 4222 || id == 4223)
            return 250;
        if (id == 4225 || id == 4226 || id == 4227 || id == 4228 || id == 4229 || id == 4230 || id == 4231 || id == 4232 || id == 4233 || id == 4234)
            return 250;
        //barrows degraded already
        if ((id >= 21762 && id <= 21765) || (id >= 21754 && id <= 21757) || (id >= 21746 && id <= 21749) || (id >= 21738 && id <= 21741) || (id >= 4856 && id <= 4859) || (id >= 4862 && id <= 4865) || (id >= 4868 && id <= 4871) || (id >= 4874 && id <= 4877) || (id >= 4880 && id <= 4883) || (id >= 4886 && id <= 4889) || (id >= 4892 && id <= 4895) || (id >= 4898 && id <= 4901) || (id >= 4904 && id <= 4907) || (id >= 4910 && id <= 4913) || (id >= 4916 && id <= 4919) || (id >= 4922 && id <= 4925) || (id >= 4928 && id <= 4931) || (id >= 4934 && id <= 4937) || (id >= 4940 && id <= 4943) || (id >= 4946 && id <= 4949) || (id >= 4952 && id <= 4955) || (id >= 4958 && id <= 4961) || (id >= 4964 && id <= 4967) || (id >= 4970 && id <= 4973) || (id >= 4976 && id <= 4979) || (id >= 4982 && id <= 4985) || (id >= 4988 && id <= 4991) || (id >= 4994 && id <= 4997))
            return 30000 * getDegradeGearRate();
        if (id >= 24450 && id <= 24454) // rouge gloves
            return 6000;
        if (id >= 22358 && id <= 22369) // dominion gloves
            return 24000 * getDegradeGearRate();
        if (id == 22444) //neem oil
            return 2000;
        //polypore armors
        if (id == 22460 || id == 22464 || id == 22468 || id == 22472 || id == 22476 || id == 22480 || id == 22484 || id == 22488 || id == 22492)
            return 60000 * getDegradeGearRate(); // 10 hour
        if (id == 18349 || id == 18351 || id == 18353 || id == 18355 || id == 18357 || id == 18359 || id == 18361 || id == 18363 || id == 18365 || id == 18367 || id == 18369 || id == 18371 || id == 18373)
            return 30000 * getDegradeGearRate(); // 5 hour
        if (id == 22496)
            return 3000;
        if (id == 20173)
            return 60000;
        if (id == 11283)
            return 50;
        return -1;
    }

    public static final int[] CLUE_SCROLLS = new int[]
            { 2677, 2801, 2722, 19043 };
    public static final int[] SCROLL_BOXES = new int[]
            { 19005, 19065, 18937, 19041 };
    public static final int[] PUZZLES = new int[]
            { 2798, 3565, 3576, 19042 };
    public static final int[] BASE_PIECES =
            { 2749, 3619, 3643, 18841 };
    public static final int[] POST_IMBUED_RINGS =
            { 15009, 15010, 15011, 15012, 15013, 15014, 15015, 15016, 15017, 15018, 15019, 15020, 15220 };


    public static boolean isSlayerHelmet(Item item) {
        return item.getDefinition().getName().toLowerCase().contains("slayer helm");
    }

    public static boolean isTradeable(Item item) {
        if(item.getId() == 11283)//Dfs
            return true;
        if(item.getId() == 18648)//chalice
            return true;
        if(item.getId() >= 22346 && item.getId() <= 22348)
            return true;
        //Impling jars
        if((item.getId() >= 11238 && item.getId() <= 11257) || item.getId() == 15517 || item.getId() == 15518)
            return true;
        if (item.getDefinition().isDestroyItem() || item.getDefinition().lended)
            return false;
        int charges = ItemConstants.getItemDefaultCharges(item.getId());
        if (charges != -1 && charges != 0)
            return false;
        if(Effigies.isEffigy(item))
            return false;
        for (int id : CLUE_SCROLLS)
            if (item.getId() == id)
                return false;
        for (int id : SCROLL_BOXES)
            if (item.getId() == id)
                return false;
        for (int id : PUZZLES)
            if (item.getId() == id)
                return false;
        for (int id : POST_IMBUED_RINGS)
            if (item.getId() == id)
                return true;
        if (isSlayerHelmet(item))
            return false;
        //1st dung update
        if (item.getId() >= 18330 && item.getId() <= 18374)
            return false;
        //2nd dung update, warped
        if (item.getId() >= 19669 && item.getId() <= 19675)
            return false;
        //3rd dung update, warped
        if (item.getId() >= 19886 && item.getId() <= 19895)
            return false;
        //Castle wars particle rewards
        if (item.getId() >= 18744 && item.getId() <= 18747)
            return false;
        //Elite void
        if(item.getId() >= 19785 && item.getId() <= 19790)
            return false;
        //RDF gloves
        if(item.getId() >= 7453 && item.getId() <= 7462)
            return false;
        if (OrnamentKits.getKit(item) != null)
            return false;

        //Skillcapes
        if(item.getId() >= 9747 && item.getId() <= 9815)
            return false;
        //Summoning
        if(item.getId() >= 12169 && item.getId() <= 12171)
            return false;
        //Dungeoneering
        if(item.getId() >= 18508 && item.getId() <= 18510)
            return false;
        //Mastercapes
        if(item.getId() >= 31268 && item.getId() <= 31292)
            return false;

        switch (item.getId()) {
            case 1052: //Legends Cape
            case 6279: //Broodoo Shield
            case 6339: // Tribal Mask
            case 20857: //Sir Owen's Longsword
            case 19709://Dung master cape
            case 5509:
            case 5510:
            case 5512:
            case 5514://Runecrafting pouches
            case 8851://Warriors guild tokens
            case 19467: //biscuits
                //prayer books
            case 3839:
            case 3840:
            case 3841:
            case 3842:
            case 3843:
            case 3844:
            case 19612:
            case 19613:
            case 19614:
            case 19615:
            case 19616:
            case 19617:
                // void
            case 8839:
            case 8840:
            case 8841:
            case 8842:
            case 10611:
            case 11663:
            case 11664:
            case 11665:
            case 11674:
            case 11675:
            case 11676:
            case 19711:
            case 19712:
                //vinewhip
            case 2677:
            case 2801:
            case 2722:
            case 19043:
            case 23193:
            case 23194:
            case 20763: //veteran cape
            case 20767: //max cape and hood
            case 20768:
            case 10844: //sq'irk
            case 10845:
            case 10846:
            case 10847:
            case 10848:
            case 10849:
            case 10850:
            case 10581:
            case 23044: //mindspike
            case 23045:
            case 23046:
            case 23047:
            case 35: // excalibur
            case 22496: //polypore degraded gear
            case 22492:
            case 22488:
            case 22484:
            case 22480:
            case 22476:
            case 22472:
            case 22468:
            case 22464:
            case 22460:
            case 11283: //dragonfire shield
            case 24444: //neem drupe stuff
            case 24445:
            case 10588: // Salve amulet (e)
            case 772: // dramen staff
            case 6570: // firecape
            case 6529: // tokkul
            case 7462: // barrow gloves
            case 23659: // tookhaar-kal
            case 19784: // korasi
            case 24455:// crucible weapons
            case 24456:
            case 24457:
            case 15433:// red and blue cape from nomad
            case 15435:
            case 15432:
            case 15434:
            case 12158:
            case 12159:
            case 12160:
            case 12163:
                // bird nests with search
            case 5070:
            case 5071:
            case 5072:
            case 5073:
            case 5074:
            case 7413:
            case 11966:
                // stealing creation capes
            case 14387:
            case 14389:
            case 20072: // defenders
            case 8844:
            case 8845:
            case 8846:
            case 8847:
            case 8848:
            case 8849:
            case 8850:
                return false;
            default:
                return true;
        }
    }
}
