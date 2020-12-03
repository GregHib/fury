package com.fury.game.entity.character.player.link.transportation;

import com.fury.game.container.impl.equip.Slot;
import com.fury.game.content.global.Achievements;
import com.fury.game.world.map.Position;
import com.fury.game.content.dialogue.input.impl.EnterFairyRingCode;
import com.fury.core.model.node.entity.actor.figure.player.Player;

/**
 * Created by Greg on 25/10/2016.
 */
public class FairyRingTeleport {

    public static boolean fairyPort(Player player, String string) {
        if(string.length() != 3)
            player.message("Invalid location code.");

        for (Combination combination : Combination.values()) {
            if (string.equalsIgnoreCase(combination.getCode())) {
                TeleportHandler.teleportPlayer(player, combination.getPosition(), TeleportType.FAIRY_RING_TELE);
                Achievements.finishAchievement(player, Achievements.AchievementData.USE_FAIRY_RING_TELEPORT);
                return true;
            }
        }
        return true;
    }
    public static boolean handleObjectClick(Player player) {
        if(hasDramenStaff(player)) {
            player.setInputHandling(new EnterFairyRingCode());
            player.getPacketSender().sendEnterInputPrompt("Enter fairy ring code:");
        }
        return false;
    }

    private static boolean hasDramenStaff(Player player) {
        if (player.getEquipment().get(Slot.WEAPON).getId() != 772 && player.getEquipment().get(Slot.WEAPON).getId() != 9084) {
            player.message("You need a dramen or lunar staff to use the Fairy Ring!");
            return false;
        }
        return true;
    }

    public static void homeTeleport(Player player) {
        if(hasDramenStaff(player))
            TeleportHandler.teleportPlayer(player, homeRing, TeleportType.FAIRY_RING_TELE);
    }
    private static final Position homeRing = new Position(3129, 3496);

    private enum Combination {
        MAIN_LOCATION("", new Position(2412, 4434)),
        MUDSKIPPER("AIQ", new Position(2996, 3114)),
        WITCH("AIR", new Position(2700, 3247)),
        GOLDENAPPLE("AJR", new Position(2780, 3613)),
        PENGUIN("AJS", new Position(2500, 3896)),
        PICTORIS("AKQ", new Position(2319, 3619)),
        FELDIP("AKS", new Position(2571, 2956)),
        JIGJIG("ALP", new Position(2472, 3027)),
        MORYTANIA("ALQ", new Position(2472, 3027)),
        ABYSS("ALR", new Position(3059, 4875)),
        MCGRUBOR("ALS", new Position(2644, 3495)),
        POLYPORE("BIP", new Position(3410, 3324)),
        KALPHITE("BIQ", new Position(3251, 3095)),
        SPARSE("BIR", new Position(2455, 4396)),
        ARDOUGNE("BIS", new Position(2635, 3266)),
        ANCIENT("BJQ", new Position(1737, 5342)),//One way
        //FISHERREALM("BJR", new Position(2650, 4730)),
        CASTLEWARS("BKP", new Position(2385, 3035)),
        ENCHANTED("BKQ", new Position(3041, 4532)),
        MORTMYRE("BKR", new Position(3469, 3431)),
        //TZHAAR("BLP", new Position(4622, 5147)),
        //YUBIUSK("BLQ", new Position(2229, 4244, 1)),
        LEGENDS("BLR", new Position(2740, 3351)),
        ZANARIS("BLS", new Position(2412, 4434)),
        MISCELLANIA("CIP", new Position(2513, 3884)),
        YANILLE("CIQ", new Position(2528, 3127)),
        //BOB(C, I, S, new Position(, , )), Other realms: ScapeRune (Evil Bob's island): ScapeRune - The RuneScape Wiki
        SINCLAIR("CJR", new Position(2705, 3576)),
        COSMIC("CKP", new Position(2075, 4848)),
        WANNAI("CKR", new Position(2801, 3003)),
        CANIFIS("CKS", new Position(3447, 3470)),
        DRAYNOR("CLP", new Position(3082, 3206)),
        APEATOLL("CLR", new Position(2735, 2742)),
        ISLAND("CLS", new Position(2682, 3081)),
        MOSLE("DIP", new Position(3763, 2930)),
//        GORAK("DIR", new Position(3038, 5348)),
        WIZARDTOWER("DIS", new Position(3108, 3149)),
        TOL("DJP", new Position(2658, 3230)),
        SINCLAIR2("DJR", new Position(2676, 3587)),
        MUSAPOINT("DKP", new Position(2900, 3111)),
        GLACORS("DKQ", new Position(4183, 5726)),
        EXCHANGE("DKR", new Position(3129, 3496)),
        SNOWY("DKS", new Position(2744, 3719)),
        KHARIDIAN("DLQ", new Position(3423, 3016)),
        POISON("DLR", new Position(2213, 3099));

        Combination(String code, Position position) {
            this.code = code;
            this.position = position;
        }

        private String code;
        private Position position;

        public String getCode() {
            return code;
        }

        public Position getPosition() {
            return position;
        }
    }
}
