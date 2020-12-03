package com.fury.cache.def.npc;

import com.fileserver.cache.impl.CacheArchive;
import com.fury.cache.ByteBuffer;
import com.fury.cache.Revision;

import java.util.HashMap;

public class NpcDefinition659 extends NpcDefinition {
    public static NpcDefinition659[] cache;
    protected static ByteBuffer buffer;
    protected static int[] indices;
    public static int total;

    public NpcDefinition659(int id) {
        super(id);
        revision = Revision.RS2;
    }

    public void load(CacheArchive archive, String name) {
        buffer = new ByteBuffer(archive.get(name + ".dat"));
        ByteBuffer index = new ByteBuffer(archive.get(name + ".idx"));

        total = index.getUnsignedShort();
        indices = getIndices(index, total);

        cache = new NpcDefinition659[total];
    }

    public NpcDefinition659 forId(int id) {
        NpcDefinition659 definition = id < 0 || id >= cache.length ? null : cache[id];
        if(definition == null) {
            definition = new NpcDefinition659(id);

            if(id >= indices.length || id < 0)
                return definition;

            buffer.position = indices[id];
            definition.readValueLoop(buffer);

            definition.changeValues();

            cache[id] = definition;
        }
        return definition;
    }

    @Override
    public void changeValues() {
        switch (revision) {
            case RS2:
                if (options != null) {
                    int total = 0;
                    for (int i = 0; i < options.length; i++)
                        if (options[i] != null)
                            total++;
                    if (options[0] != null && options[0].equalsIgnoreCase("Talk-to")) {
                        if (options[2] != null && total == 2) {
                            options[0] = options[2];
                            options[2] = null;
                        }
                        if (revision == Revision.RS2 && options[3] != null && options[3] != null && total == 3) {
                            options[0] = options[3];
                            options[3] = null;
                        }
                    }
                }
                switch (id) {
                    case 456://Father Aereck
                        options = new String[] {"Talk-to", null, "Trade", null, null};
                        break;
                    case 949:
                        name = "Fury Sage";
                        runAnimation = 824;
                        options = new String[] {"Talk-to", "End-quest", null, null, null};
                        break;
                    case 13090://Sneaker peeper
                        idleAnimation = 15016;
                        break;
                    case 13089://Sneaker peeper spawn
                        idleAnimation = 13291;
                        break;
                    case 6742:
                    case 6744:
                    case 6745:
                    case 6746:
                    case 9393:
                    case 6743:
                    case 9387:
                    case 9384://Christmas characters
                        options = new String[] { "Talk-to", "Pelt", null, null, null };
                        if(id > 7000)
                            walkAnimation = 819;
                        break;
                    case 6524://Bob barter
                        options = new String[] {"Talk-to", null, "Decant", "Decant-X", null};
                        break;
                    case 792://Ironman
                        name = "Stark";
                        options = new String[] {"Talk-to", null, null, null, null};
                        combat = 0;
                        break;
                    case 7781://Jesmona
                        name = "Slayer teleporter";
                        break;
                    case 574:
                        options = new String[] {"Trade", null, null, null, null};
                        break;
                    case 7143:
                        name = "Gambler";
                        break;
                    case 6782:
                        name = "Fury Guide";
                        break;
                    case 400:
                        name = "Melee Shop";
                        options = new String[] {"Trade", null, null, null, null};
                        break;
                    case 12179:
                        name = "Mage Shop";
                        options = new String[] {"Trade", null, null, null, null};
                        break;
                    case 1694:
                        name = "Range Shop";
                        options = new String[] {"Trade", null, null, null, null};
                        break;
                    case 942:
                        name = "Food Shop";
                        options = new String[] {"Trade", null, null, null, null};
                        break;
                    case 6970:
                        options = new String[]{"Trade", null, "Exchange Shards", null, null};
                        break;
                    case 251:
                        options = new String[]{"Talk-to", null, "Claim Items", "Check Total", "Teleport"};
                        break;
                    case 4646:
                        options = new String[]{"Talk-to", null, "Vote Rewards", null, null};
                        break;
                    case 8591:
                        options = new String[]{"Talk-to", null, "Trade", null, null};
                        break;
                    case 805:
                        options = new String[]{"Trade", null, "Tan hide", null, null};
                        break;
                    case 650://Warriors guild
                    case 5109://Artimeus (Nardah hunter store)
                    case 3789://Void knight
                    case 802://Brother jered
                    case 520://General store shopkeeper
                    case 521://Shop assistant
                    case 571://Baker
                    case 569://Silver merchant
                    case 573://Fur Trader
                    case 572://Spice seller
                    case 546://Zaff
                    case 359://Naff
                    case 249://Merlin
                    case 13632://Larriar (Runecrafting)
                    case 8032://Elriss (Runecrafting)
                    case 541://Zeke (Scimitar shop)
                    case 551://Varrock sword shop
                    case 537://Scavvo (Champ Guild)
                    case 549://Horvik (Armour shop)
                    case 577://Cassie (Shield Shop)
                    case 550://Lowe
                    case 903://Lundail
                    case 3796://Squire (archery store)
                    case 3798://Squire (Magic store)
                    case 638://Apothecary
                    case 4294://Lilly (Potions)
                    case 4293://Lidio (Food)
                    case 557://Wydin food shop
                    case 576://Harry (fish)
                    case 1303://Skullgrimen (Gear)
                    case 1315://Fishmonger
                    case 1282://Sigmund the Merchant
                    case 5503://Mawnis Burowgar
                    case 5504://Mawnis Burowgar (Neitiznot Helm)
                    case 592://Roachey (Fish Guild)
                    case 587://Jatix (Herblore shop)
                    case 6070://Elnock (Puro-puro shop)
                    case 5113://Hunting expertn
                    case 4946://Ignatius Vulcan (Firemaking)
                    case 1281://Sigli the Huntsman (Hunter)
                    case 948://Mining instructor
                    case 575://Hickton (catherby archery)
                    case 563://Arhein
                    case 455://99 herblore druid
                    case 682://Armour salesman
                    case 540://Gem trader
                    case 584://Falador gem guy
                    case 586://2h sword shop
                    case 437://Cap izzy no beard
                    case 519://Bob
                    case 548://Thalissa
                    case 3299://Master gardner
                        options = new String[]{"Trade", null, null, null, null};
                        break;
                    case 5913://Aubury
                        options = new String[]{"Trade", null, "Teleport", null, null};
                        break;
                    case 2292:
                        options = new String[]{"Trade", "Sell-all", null, null, null};
                        name = "Merchant";
                        break;
                    case 2253:
                        options = new String[]{"Buy Skillcapes", null, "Buy Skillcapes (t)", "Buy Hoods", "Buy Mastercapes"};
                        break;
                    case 3641:
                        name = "Donor Salesman";
                        break;
                    case 494:
                    case 9710:
                        options = new String[]{"Bank", null, null, null, null};
                        break;
                }
                break;
            case PRE_RS3:
                switch (id) {
                    case 13955://The raptor
                    case 14386://Death
                    case 14385:// wildy death
                        options = new String[] {"Talk-to", null, "Get-task", "Trade", "Rewards"};
                        break;
                }
                break;
        }

        //TODO re-implement boss pets
        /*
        case 3030:
                definition.name = "King black dragon";
                definition.actions = new String[5];
                definition.actions[0] = "Pick-up";
                definition.modelIds = new int[]{17414, 17415, 17429, 17422};
                definition.combat = 276;
                definition.idleAnimation = 90;
                definition.walkAnimation = 4635;
                definition.scaleXY = 63;
                definition.scaleZ = 63;
                definition.size = 3;
                break;

            case 3031:
                definition.name = "General graardor";
                definition.actions = new String[5];
                definition.actions[0] = "Pick-up";
                definition.modelIds = new int[]{27785, 27789};
                definition.combat = 624;
                definition.idleAnimation = 7059;
                definition.walkAnimation = 7058;
                definition.scaleXY = 40;
                definition.scaleZ = 40;
                break;

            case 3032:
                definition.name = "TzTok-Jad";
                definition.actions = new String[5];
                definition.actions[0] = "Pick-up";
                definition.modelIds = new int[]{34131};
                definition.combat = 702;
                definition.idleAnimation = 9274;
                definition.walkAnimation = 9273;
                definition.scaleXY = 45;
                definition.scaleZ = 45;
                definition.size = 2;
                break;

            case 3033:
                definition.name = "Chaos elemental";
                definition.actions = new String[5];
                definition.actions[0] = "Pick-up";
                definition.modelIds = new int[]{11216};
                definition.combat = 305;
                definition.idleAnimation = 3144;
                definition.walkAnimation = 3145;
                definition.scaleXY = 62;
                definition.scaleZ = 62;
                break;
            case 3034:
                definition.name = "Corporeal beast";
                definition.actions = new String[5];
                definition.actions[0] = "Pick-up";
                definition.modelIds = new int[]{40955};
                definition.combat = 785;
                definition.idleAnimation = 10056;
                definition.walkAnimation = 10055;
                definition.scaleXY = 45;
                definition.scaleZ = 45;
                definition.size = 2;
                break;

            case 3035:
                definition.name = "Kree'arra";
                definition.actions = new String[5];
                definition.actions[0] = "Pick-up";
                definition.modelIds = new int[]{28003, 28004};
                definition.combat = 580;
                definition.idleAnimation = 6972;
                definition.walkAnimation = 6973;
                definition.scaleXY = 43;
                definition.scaleZ = 43;
                definition.size = 2;
                break;

            case 3036:
                definition.name = "K'ril tsutsaroth";
                definition.actions = new String[5];
                definition.actions[0] = "Pick-up";
                definition.modelIds = new int[]{27768, 27773, 27764, 27765, 27770};
                definition.combat = 650;
                definition.idleAnimation = 6943;
                definition.walkAnimation = 6942;
                definition.scaleXY = 43;
                definition.scaleZ = 43;
                definition.size = 2;
                break;
            case 3041:
                definition.name = "Commander zilyana";
                definition.actions = new String[5];
                definition.actions[0] = "Pick-up";
                definition.modelIds = new int[]{28057, 28071, 28078, 28056};
                definition.combat = 596;
                definition.idleAnimation = 6963;
                definition.walkAnimation = 6962;
                definition.scaleXY = 103;
                definition.scaleZ = 103;
                definition.size = 2;
                break;
            case 3038:
                definition.name = "Dagannoth supreme";
                definition.actions = new String[5];
                definition.actions[0] = "Pick-up";
                definition.modelIds = new int[]{9941, 9943};
                definition.combat = 303;
                definition.idleAnimation = 2850;
                definition.walkAnimation = 2849;
                definition.scaleXY = 105;
                definition.scaleZ = 105;
                definition.size = 2;
                break;
            case 3039:
                definition.name = "Dagannoth prime"; //9940, 9943, 9942
                definition.actions = new String[5];
                definition.actions[0] = "Pick-up";
                definition.modelIds = new int[]{9940, 9943, 9942};
                definition.originalColours = new int[]{11930, 27144, 16536, 16540};
                definition.replacementColours = new int[]{5931, 1688, 21530, 21534};
                definition.combat = 303;
                definition.idleAnimation = 2850;
                definition.walkAnimation = 2849;
                definition.scaleXY = 105;
                definition.scaleZ = 105;
                definition.size = 2;
                break;
            case 3040:
                definition.name = "Dagannoth rex";
                definition.actions = new String[5];
                definition.actions[0] = "Pick-up";
                definition.modelIds = new int[]{9941};
                definition.originalColours = new int[]{16536, 16540, 27144, 2477};
                definition.replacementColours = new int[]{7322, 7326, 10403, 2595};
                definition.combat = 303;
                definition.idleAnimation = 2850;
                definition.walkAnimation = 2849;
                definition.scaleXY = 105;
                definition.scaleZ = 105;
                definition.size = 2;
                break;
            case 3047:
                definition.name = "Frost dragon";
                definition.combat = 166;
                definition.idleAnimation = 13156;
                definition.walkAnimation = 13157;
                definition.halfTurnAnimation = -1;
                definition.rotateAntiClockwiseAnimation = -1;
                definition.rotateClockwiseAnimation = -1;
                //definition.type = 51;
                definition.rotation = 32;
                definition.modelIds = new int[]{56767, 55294};
                definition.actions = new String[5];
                definition.actions[0] = "Pick-up";
                definition.scaleXY = 72;
                definition.scaleZ = 72;
                definition.size = 2;
                break;

            case 3048:
                definition.modelIds = new int[]{44733};
                definition.name = "Tormented demon";
                definition.combat = 450;
                definition.idleAnimation = 10921;
                definition.walkAnimation = 10920;
                definition.halfTurnAnimation = -1;
                definition.rotateAntiClockwiseAnimation = -1;
                definition.rotateClockwiseAnimation = -1;
                //	definition.type = 8349;
                definition.rotation = 32;
                definition.actions = new String[5];
                definition.actions[0] = "Pick-up";
                definition.scaleXY = 60;
                definition.scaleZ = 60;
                definition.size = 2;
                break;
            case 3050:
                definition.modelIds = new int[]{24602, 24605, 24606};
                definition.name = "Kalphite queen";
                definition.actions = new String[5];
                definition.actions[0] = "Pick-up";
                definition.combat = 333;
                definition.idleAnimation = 6236;
                definition.walkAnimation = 6236;
                definition.scaleXY = 70;
                definition.scaleZ = 70;
                definition.size = 2;
                break;
            case 3051:
                definition.modelIds = new int[]{46141};
                definition.name = "Slash bash";
                definition.actions = new String[5];
                definition.actions[0] = "Pick-up";
                definition.combat = 111;
                definition.idleAnimation = 11460;
                definition.walkAnimation = 11461;
                definition.scaleXY = 65;
                definition.scaleZ = 65;
                definition.size = 2;
                break;
            case 3052:
                definition.modelIds = new int[]{45412};
                definition.name = "Phoenix";
                definition.actions = new String[5];
                definition.actions[0] = "Pick-up";
                definition.combat = 235;
                definition.idleAnimation = 11074;
                definition.walkAnimation = 11075;
                definition.scaleXY = 70;
                definition.scaleZ = 70;
                definition.size = 2;
                break;
            case 3053:
                definition.modelIds = new int[]{46058, 46057};
                definition.name = "Bandos avatar";
                definition.actions = new String[5];
                definition.actions[0] = "Pick-up";
                definition.combat = 299;
                definition.idleAnimation = 11242;
                definition.walkAnimation = 11255;
                definition.scaleXY = 70;
                definition.scaleZ = 70;
                definition.size = 2;
                break;
            case 3054:
                definition.modelIds = new int[]{62717};
                definition.name = "Nex";
                definition.actions = new String[5];
                definition.actions[0] = "Pick-up";
                definition.combat = 565;
                definition.idleAnimation = 6320;
                definition.walkAnimation = 6319;
                definition.scaleXY = 95;
                definition.scaleZ = 95;
                definition.size = 1;
                break;
            case 3055:
                definition.modelIds = new int[]{51852, 51853};
                definition.name = "Jungle strykewyrm";
                definition.actions = new String[5];
                definition.actions[0] = "Pick-up";
                definition.combat = 110;
                definition.idleAnimation = 12790;
                definition.walkAnimation = 12790;
                definition.scaleXY = 60;
                definition.scaleZ = 60;
                definition.size = 1;
                break;
            case 3056:
                definition.modelIds = new int[]{51848, 51850};
                definition.name = "Desert strykewyrm";
                definition.actions = new String[5];
                definition.actions[0] = "Pick-up";
                definition.combat = 130;
                definition.idleAnimation = 12790;
                definition.walkAnimation = 12790;
                definition.scaleXY = 60;
                definition.scaleZ = 60;
                definition.size = 1;
                break;
            case 3057:
                definition.modelIds = new int[]{51847, 51849};
                definition.name = "Ice strykewyrm";
                definition.actions = new String[5];
                definition.actions[0] = "Pick-up";
                definition.combat = 210;
                definition.idleAnimation = 12790;
                definition.walkAnimation = 12790;
                definition.scaleXY = 65;
                definition.scaleZ = 65;
                definition.size = 1;
                break;
            case 3058:
                definition.modelIds = new int[]{49142, 49144};
                definition.name = "Green dragon";
                definition.actions = new String[5];
                definition.actions[0] = "Pick-up";
                definition.combat = 79;
                definition.idleAnimation = 12248;
                definition.walkAnimation = 12246;
                definition.scaleXY = 70;
                definition.scaleZ = 70;
                definition.size = 2;
                break;
            case 3059:
                definition.modelIds = new int[]{57937};
                definition.name = "Baby blue dragon";
                definition.actions = new String[5];
                definition.actions[0] = "Pick-up";
                definition.combat = 48;
                definition.idleAnimation = 14267;
                definition.walkAnimation = 14268;
                definition.scaleXY = 85;
                definition.scaleZ = 85;
                definition.size = 1;
                break;
            case 3060:
                definition.modelIds = new int[]{49137, 49144};
                definition.name = "Blue dragon";
                definition.actions = new String[5];
                definition.actions[0] = "Pick-up";
                definition.combat = 111;
                definition.idleAnimation = 12248;
                definition.walkAnimation = 12246;
                definition.scaleXY = 70;
                definition.scaleZ = 70;
                definition.size = 2;
                break;
            case 3061:
                definition.modelIds = new int[]{14294, 49144};
                definition.name = "Black dragon";
                definition.actions = new String[5];
                definition.actions[0] = "Pick-up";
                definition.combat = 227;
                definition.idleAnimation = 12248;
                definition.walkAnimation = 12246;
                definition.scaleXY = 70;
                definition.scaleZ = 70;
                definition.size = 2;
                break;
                case 3390:
                definition.name = "Prince Black Dragon";
                definition.scaleZ = 30;
                definition.scaleXY = 30;
                definition.size = 1;
                definition.actions = new String[5];
                definition.actions[0] = "Talk-to";
                definition.actions[2] = "Pick-up";
                definition.modelIds = new int[4];
                definition.modelIds[0] = 17414;
                definition.modelIds[1] = 17415;
                definition.modelIds[2] = 17429;
                definition.modelIds[3] = 17422;
                definition.idleAnimation = 90;
                definition.walkAnimation = 4635;
                definition.combat = 0;
                definition.description = "A miniature King Black Dragon!".getBytes();
                definition.varbit = -1;
                break;
            case 3391:// stop before 5902
                definition.varbit = -1;
                definition.name = "Chaos Elemental Jr.";
                definition.scaleZ = 30;
                definition.scaleXY = 30;
                definition.size = 1;
                definition.actions = new String[5];
                definition.actions[0] = "Talk-to";
                definition.actions[2] = "Pick-up";
                definition.modelIds = new int[1];
                definition.modelIds[0] = 11216;
                definition.idleAnimation = 3144;
                definition.walkAnimation = 3145;
                definition.combat = 0;
                definition.description = "A miniature Chaos Elemental!".getBytes();
                break;
            case 3392:// stop before 5902
                definition.varbit = -1;
                definition.name = "Baby Mole";
                definition.scaleZ = 30;
                definition.scaleXY = 30;
                definition.size = 1;
                definition.actions = new String[5];
                definition.actions[0] = "Talk-to";
                definition.actions[2] = "Pick-up";
                definition.modelIds = new int[4];
                definition.modelIds[0] = 12076;
                definition.modelIds[1] = 12075;
                definition.modelIds[2] = 12074;
                definition.modelIds[3] = 12077;
                definition.idleAnimation = 3309;
                definition.walkAnimation = 3313;
                definition.combat = 0;
                definition.description = "A miniature Giant Mole!".getBytes();
                break;
            case 3393:// stop before 5902
                definition.varbit = -1;
                definition.name = "Baby Dagannoth Supreme";
                definition.scaleZ = 40;
                definition.scaleXY = 40;
                definition.size = 1;
                definition.actions = new String[5];
                definition.actions[0] = "Talk-to";
                definition.actions[2] = "Pick-up";
                definition.modelIds = new int[2];
                definition.modelIds[0] = 9941;
                definition.modelIds[1] = 9943;
                definition.idleAnimation = 2850;
                definition.walkAnimation = 2849;
                definition.combat = 0;
                definition.description = "A miniature Dagannoth Supreme!".getBytes();
                break;
            case 3394:// stop before 5902
                definition.varbit = -1;
                definition.name = "Dagannoth Prime Jr.";
                definition.scaleZ = 40;
                definition.scaleXY = 40;
                definition.size = 1;
                definition.actions = new String[5];
                definition.actions[0] = "Talk-to";
                definition.actions[2] = "Pick-up";
                definition.modelIds = new int[3];
                definition.modelIds[0] = 9940;
                definition.modelIds[1] = 9943;
                definition.modelIds[2] = 9942;
                definition.idleAnimation = 2850;
                definition.walkAnimation = 2849;
                definition.combat = 0;
                definition.description = "A miniature Dagannoth Prime!".getBytes();
                break;
            case 3395:// stop before 5902
                definition.varbit = -1;
                definition.name = "Baby Dagannoth Rex";
                definition.scaleZ = 40;
                definition.scaleXY = 40;
                definition.size = 1;
                definition.actions = new String[5];
                definition.actions[0] = "Talk-to";
                definition.actions[2] = "Pick-up";
                definition.modelIds = new int[1];
                definition.modelIds[0] = 9941;
                definition.idleAnimation = 2850;
                definition.walkAnimation = 2849;
                definition.combat = 0;
                definition.description = "A miniature Dagannoth Rex!".getBytes();
                break;
            case 3396:
                definition.varbit = -1;
                definition.actions = new String[5];
                definition.actions[0] = "Talk-to";
                definition.actions[2] = "Pick-up";
                definition.modelIds = new int[2];
                definition.modelIds[0] = 28003;
                definition.modelIds[1] = 28004;
                definition.scaleZ = 25;
                definition.scaleXY = 25;
                definition.idleAnimation = 6972;
                definition.walkAnimation = 6973;
                definition.name = "Kree'arra Jr.";
                definition.combat = 0;
                definition.description = "A mini Kree'arra!".getBytes();
                definition.size = 1;
                break;
            case 3397:// stop before 5902
                definition.varbit = -1;
                definition.name = "General Graardor Jr.";
                definition.scaleZ = 30;
                definition.scaleXY = 30;
                definition.size = 1;
                definition.actions = new String[5];
                definition.actions[0] = "Talk-to";
                definition.actions[2] = "Pick-up";
                definition.modelIds = new int[2];
                definition.modelIds[0] = 27785;
                definition.modelIds[1] = 27789;
                definition.idleAnimation = 7059;
                definition.walkAnimation = 7058;
                definition.combat = 0;
                definition.description = "A miniature General Graardor!".getBytes();
                break;
            case 3398:// stop before 5902
                definition.varbit = -1;
                definition.name = "Penance Pet";
                definition.scaleZ = 30;
                definition.scaleXY = 30;
                definition.size = 1;
                definition.actions = new String[5];
                definition.actions[0] = "Talk-to";
                definition.actions[2] = "Pick-up";
                definition.modelIds = new int[8];
                definition.modelIds[0] = 20717;
                definition.modelIds[1] = 20715;
                definition.modelIds[2] = 20714;
                definition.modelIds[3] = 20709;
                definition.modelIds[4] = 20713;
                definition.modelIds[5] = 20712;
                definition.modelIds[6] = 20711;
                definition.modelIds[7] = 20710;
                definition.idleAnimation = 5410;
                definition.walkAnimation = 5409;
                definition.combat = 0;
                definition.description = "A miniature Penance Queen!".getBytes();
                break;
            case 3400:// stop before 5902
                definition.varbit = -1;
                definition.name = "Zilyana Jr.";
                definition.scaleZ = 40;
                definition.scaleXY = 40;
                definition.size = 1;
                definition.actions = new String[5];
                definition.actions[0] = "Talk-to";
                definition.actions[2] = "Pick-up";
                definition.modelIds = new int[4];
                definition.modelIds[0] = 28057;
                definition.modelIds[1] = 28071;
                definition.modelIds[2] = 28078;
                definition.modelIds[3] = 28056;
                definition.idleAnimation = 6963;
                definition.walkAnimation = 6962;
                definition.combat = 0;
                definition.description = "A miniature Commander Zilyana!".getBytes();
                break;
         */
    }

    @Override
    public void readValues(ByteBuffer buffer, int opcode) {
        if (opcode == 1) {
            int size = buffer.readUnsignedByte();
            modelIds = new int[size];
            for (int index = 0; index < size; index++) {
                modelIds[index] = buffer.readUnsignedShort();
                if (modelIds[index] == 65535)
                    modelIds[index] = -1;
            }
        } else if (opcode == 2) {
            name = buffer.readString();
        } else if (opcode == 12) {
            size = (byte) buffer.readUnsignedByte();
        } else if (opcode == 13) {
            idleAnimation = buffer.readShort();
        } else if (opcode == 14) {
            walkAnimation = buffer.readShort();
        } else if (opcode == 15) {
            runAnimation = buffer.readShort();
        } else if (opcode == 17) {
            int temp = buffer.readShort();//walkUp not walk
            int walkAnimation = temp;
            int halfTurnAnimation = buffer.readShort();
            int rotateClockwiseAnimation = buffer.readShort();
            int rotateAntiClockwiseAnimation = buffer.readShort();
        } else if (opcode >= 30 && opcode < 35) {
            options[opcode - 30] = buffer.readString();
            if (options[opcode - 30].equalsIgnoreCase("Hidden"))
                options[opcode - 30] = null;
        } else if (opcode == 40) {
            int i = buffer.readUnsignedByte();
            short[] replacementColours = new short[i];
            short[] originalColours = new short[i];
            for (int index = 0; index < i ; index++) {
                originalColours[index] = (short) buffer.readUnsignedShort();
                replacementColours[index] = (short) buffer.readUnsignedShort();
            }
        } else if (opcode == 41) {
            int i = buffer.readUnsignedByte();
            short[] originalTextureColours = new short[i];
            short[] replacementTextureColours = new short[i];
            for (int index = 0; index < i; index++) {
                originalTextureColours[index] = (short) buffer.readUnsignedShort();
                replacementTextureColours[index] = (short) buffer.readUnsignedShort();
            }
        } else if (opcode == 44) {
            int count = (short) buffer.readUnsignedShort();
        } else if (opcode == 45) {
            int size = (short) buffer.readUnsignedShort();
        } else if (opcode == 42) {
            int i = buffer.readUnsignedByte();
            byte[] aByteArray861 = new byte[i];
            for (int index = 0; i > index; index++)
                aByteArray861[index] = (byte) buffer.readByte();
        } else if (opcode == 60) {
            int i = buffer.readUnsignedByte();
            int[] dialogueModels = new int[i];
            for (int index = 0; index < i; index++)
                dialogueModels[index] = buffer.readUnsignedShort();
        } else if (opcode == 93) {
            boolean drawMinimapDot = false;
        } else if (opcode == 95) {
            combat = buffer.readUnsignedShort();
        } else if (opcode == 97) {
            int scaleXY = buffer.readUnsignedShort();
        } else if (opcode == 98) {
            int scaleZ = buffer.readUnsignedShort();
        } else if (opcode == 99) {
            boolean priorityRender = true;
        } else if (opcode == 100) {
            int lightModifier = buffer.readByte();
        } else if (opcode == 101) {
            int shadowModifier = buffer.readByte() * 5;
        } else if (opcode == 102) {
            int headIcon = buffer.readUnsignedShort();
        } else if (opcode == 103) {
            rotation = buffer.readUnsignedShort();
        } else if (opcode == 106 || opcode == 118) {
            int varbit = buffer.readUnsignedShort();
            if (varbit == 65535)
                varbit = -1;
            int varp = buffer.readUnsignedShort();
            if (varp == 65535)
                varp = -1;
            int i = -1;
            if (opcode == 118) {
                i = buffer.readUnsignedShort();
                if (i == 65535)
                    i = -1;
            }
            int length = buffer.readUnsignedByte();
            int[] morphisms = new int[length + 2];
            for (int index = 0; length >= index; index++) {
                morphisms[index] = buffer.readUnsignedShort();
                if (morphisms[index] == 65535)
                    morphisms[index] = -1;
            }
            morphisms[length + 1] = i;
        } else if (opcode == 107) {
            boolean clickable = false;
        } else if (opcode == 109) {
            boolean aBoolean852 = false;
        } else if (opcode == 111) {
            boolean aBoolean857 = false;
        } else if (opcode == 113) {
            short aShort862 = (short) buffer.readUnsignedShort();
            short aShort894 = (short) buffer.readUnsignedShort();
        } else if (opcode == 114) {
            byte aByte851 = (byte) buffer.readByte();
            byte aByte854 = (byte) buffer.readByte();
        } else if (opcode == 115) {
            buffer.readUnsignedByte();
            buffer.readUnsignedByte();
        } else if (opcode == 119) {
            walkMask = (byte) buffer.readByte();
        } else if (opcode == 121) {
            int[][] anIntArrayArray840 = new int[modelIds.length][];
            int i = buffer.readUnsignedByte();
            for (int loop = 0; loop < i; loop++) {
                int index = buffer.readUnsignedByte();
                int[] is = anIntArrayArray840[index] = new int[3];
                is[0] = buffer.readByte();
                is[1] = buffer.readByte();
                is[2] = buffer.readByte();
            }
        } else if (opcode == 122) {
            int anInt836 = buffer.readUnsignedShort();
        } else if (opcode == 123) {
            int anInt846 = buffer.readUnsignedShort();
        } else if (opcode == 125) {
            respawnDirection = (byte) buffer.readByte();
        } else if (opcode == 127) {
            int renderEmote = buffer.readUnsignedShort();
        } else if (opcode == 128) {
            buffer.readUnsignedByte();
        } else if (opcode == 134) {
            int anInt876 = buffer.readUnsignedShort();
            if (anInt876 == 65535)
                anInt876 = -1;
            int anInt842 = buffer.readUnsignedShort();
            if (anInt842 == 65535)
                anInt842 = -1;
            int anInt884 = buffer.readUnsignedShort();
            if (anInt884 == 65535)
                anInt884 = -1;
            int anInt871 = buffer.readUnsignedShort();
            if (anInt871 == 65535)
                anInt871 = -1;
            int anInt875 = buffer.readUnsignedByte();
        } else if (opcode == 135) {
            int anInt833 = buffer.readUnsignedByte();
            int anInt874 = buffer.readUnsignedShort();
        } else if (opcode == 136) {
            int anInt837 = buffer.readUnsignedByte();
            int anInt889 = buffer.readUnsignedShort();
        } else if (opcode == 137) {
            int anInt872 = buffer.readUnsignedShort();
        } else if (opcode == 138) {
            int anInt901 = buffer.readBigSmart();
        } else if (opcode == 139) {
            int anInt879 = buffer.readBigSmart();
        } else if (opcode == 140) {
            int anInt850 = buffer.readUnsignedByte();
        } else if (opcode == 141) {
            boolean aBoolean849 = true;
        } else if (opcode == 142) {
            int anInt870 = buffer.readUnsignedShort();
        } else if (opcode == 143) {
            boolean aBoolean856 = true;
        } else if (opcode >= 150 && opcode < 155) {
            options[opcode - 150] = buffer.readString();
            if (options[opcode - 150].equalsIgnoreCase("Hidden"))
                options[opcode - 150] = null;
        } else if (opcode == 155) {
            int aByte821 = buffer.readByte();
            int aByte824 = buffer.readByte();
            int aByte843 = buffer.readByte();
            int aByte855 = buffer.readByte();
        } else if (opcode == 158) {
            byte aByte833 = (byte) 1;
        } else if (opcode == 159) {
            byte aByte833 = (byte) 0;
        } else if (opcode == 160) {
            int i = buffer.readUnsignedByte();
            int[] anIntArray885 = new int[i];
            for (int i_58_ = 0; i > i_58_; i_58_++)
                anIntArray885[i_58_] = buffer.readUnsignedShort();
        } else if (opcode == 162) {
            boolean aBoolean3190 = true;
        } else if (opcode == 163) {
            int anInt864 = buffer.readUnsignedByte();
        } else if (opcode == 164) {
            int anInt848 = buffer.readUnsignedShort();
            int anInt837 = buffer.readUnsignedShort();
        } else if (opcode == 165) {
            int anInt847 = buffer.readUnsignedByte();
        } else if (opcode == 168) {
            int anInt828 = buffer.readUnsignedByte();
        } else if (opcode >= 170 && opcode < 176) {//More options?
            /*int[] anIntArray2930 = null;
            if (anIntArray2930 == null) {
                anIntArray2930 = new int[6];
                Arrays.fill(anIntArray2930, -1);
            }*/
            int i_44_ = (short) buffer.readUnsignedShort();
            /*if (i_44_ == 65535)
                i_44_ = -1;
            anIntArray2930[opcode - 170] = i_44_;*/
        } else if (opcode == 249) {
            int i = buffer.readUnsignedByte();
            if (clientScriptData == null)
                clientScriptData = new HashMap<>(i);
            for (int i_60_ = 0; i > i_60_; i_60_++) {
                boolean stringInstance = buffer.readUnsignedByte() == 1;
                int key = buffer.read24BitInt();
                Object value;
                if (stringInstance)
                    value = buffer.readString();
                else
                    value = buffer.readInt();
                clientScriptData.put(key, value);
            }
        }
    }
}
