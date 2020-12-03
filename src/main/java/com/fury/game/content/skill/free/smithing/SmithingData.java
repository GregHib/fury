package com.fury.game.content.skill.free.smithing;

import com.fury.game.content.skill.Skill;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import com.fury.util.FontUtils;
import com.fury.util.Misc;

/*
 * Rewrote by Greg
 */
public class SmithingData {

	public static final Item HAMMER = new Item(2347), DUNG_HAMMER = new Item(17883);

	public static void showBronzeInterface(Player player) {
		String fiveb = GetForBars(2349, 5, player);
		String threeb = GetForBars(2349, 3, player);
		String twob = GetForBars(2349, 2, player);
		String oneb = GetForBars(2349, 1, player);
		player.getPacketSender().sendString(1112, fiveb + "5 Bars" + fiveb);
		player.getPacketSender().sendString(1109, threeb + "3 Bars" + threeb);
		player.getPacketSender().sendString(1110, threeb + "3 Bars" + threeb);
		player.getPacketSender().sendString(1118, threeb + "3 Bars" + threeb);
		player.getPacketSender().sendString(1111, threeb + "3 Bars" + threeb);
		player.getPacketSender().sendString(1095, threeb + "3 Bars" + threeb);
		player.getPacketSender().sendString(1115, threeb + "3 Bars" + threeb);
		player.getPacketSender().sendString(1090, threeb + "3 Bars" + threeb);
		player.getPacketSender().sendString(1113, twob + "2 Bars" + twob);
		player.getPacketSender().sendString(1116, twob + "2 Bars" + twob);
		player.getPacketSender().sendString(1114, twob + "2 Bars" + twob);
		player.getPacketSender().sendString(1089, twob + "2 Bars" + twob);
		player.getPacketSender().sendString(8428, twob + "2 Bars" + twob);
		player.getPacketSender().sendString(1124, oneb + "1 Bar" + oneb);
		player.getPacketSender().sendString(1125, oneb + "1 Bar" + oneb);
		player.getPacketSender().sendString(1126, oneb + "1 Bar" + oneb);
		player.getPacketSender().sendString(1127, oneb + "1 Bar" + oneb);
		player.getPacketSender().sendString(1128, oneb + "1 Bar" + oneb);
		player.getPacketSender().sendString(1129, oneb + "1 Bar" + oneb);
		player.getPacketSender().sendString(1130, oneb + "1 Bar" + oneb);
		player.getPacketSender().sendString(1131, oneb + "1 Bar" + oneb);
		player.getPacketSender().sendString(13357, oneb + "1 Bar" + oneb);
		player.getPacketSender().sendString(11459, oneb + "1 Bar" + oneb);
		player.getPacketSender().sendString(1101, GetForlvl(18, player) + "Plate Body" + GetForlvl(18, player));
		player.getPacketSender().sendString(1099, GetForlvl(16, player) + "Plate Legs" + GetForlvl(16, player));
		player.getPacketSender().sendString(1100, GetForlvl(16, player) + "Plate Skirt" + GetForlvl(16, player));
		player.getPacketSender().sendString(1088, GetForlvl(14, player) + "2 Hand Sword" + GetForlvl(14, player));
		player.getPacketSender().sendString(1105, GetForlvl(12, player) + "Kite Shield" + GetForlvl(12, player));
		player.getPacketSender().sendString(1098, GetForlvl(11, player) + "Chain Body" + GetForlvl(11, player));
		player.getPacketSender().sendString(1092, GetForlvl(10, player) + "Battle Axe" + GetForlvl(10, player));
		player.getPacketSender().sendString(1083, GetForlvl(9, player) + "Warhammer" + GetForlvl(9, player));
		player.getPacketSender().sendString(1104, GetForlvl(8, player) + "Square Shield" + GetForlvl(8, player));
		player.getPacketSender().sendString(1103, GetForlvl(7, player) + "Full Helm" + GetForlvl(7, player));
		player.getPacketSender().sendString(1106, GetForlvl(7, player) + "Throwing Knives" + GetForlvl(7, player));
		player.getPacketSender().sendString(1086, GetForlvl(6, player) + "Long Sword" + GetForlvl(6, player));
		player.getPacketSender().sendString(1087, GetForlvl(5, player) + "Scimitar" + GetForlvl(5, player));
		player.getPacketSender().sendString(1108, GetForlvl(5, player) + "Arrowtips" + GetForlvl(5, player));
		player.getPacketSender().sendString(1085, GetForlvl(4, player) + "Sword" + GetForlvl(4, player));
		player.getPacketSender().sendString(1107, GetForlvl(4, player) + "Bolts" + GetForlvl(4, player));
		player.getPacketSender().sendString(13358, GetForlvl(4, player) + "Nails" + GetForlvl(4, player));
		player.getPacketSender().sendString(1102, GetForlvl(3, player) + "Medium Helm" + GetForlvl(3, player));
		player.getPacketSender().sendString(1093, GetForlvl(2, player) + "Mace" + GetForlvl(2, player));
		player.getPacketSender().sendString(1094, GetForlvl(1, player) + "Dagger" + GetForlvl(1, player));
		player.getPacketSender().sendString(1091, GetForlvl(1, player) + "Hatchet" + GetForlvl(1, player));
		player.getPacketSender().sendString(8429, GetForlvl(8, player) + "Claws" + GetForlvl(8, player));
		player.getPacketSender().sendSmithingData(1205,0,1119,1);
		player.getPacketSender().sendSmithingData(1351,0,1120,1);
		player.getPacketSender().sendSmithingData(1103,0,1121,1);
		player.getPacketSender().sendSmithingData(1139,0,1122,1);
		player.getPacketSender().sendSmithingData(9375,0,1123,10);
		player.getPacketSender().sendSmithingData(1277,1,1119,1);
		player.getPacketSender().sendSmithingData(1422,1,1120,1);
		player.getPacketSender().sendSmithingData(1075,1,1121,1);
		player.getPacketSender().sendSmithingData(1155,1,1122,1);
		player.getPacketSender().sendSmithingData(39,1,1123,15);
		player.getPacketSender().sendSmithingData(1321,2,1119,1);
		player.getPacketSender().sendSmithingData(1337,2,1120,1);
		player.getPacketSender().sendSmithingData(1087,2,1121,1);
		player.getPacketSender().sendSmithingData(1173,2,1122,1);
		player.getPacketSender().sendSmithingData(864,2,1123,5);
		player.getPacketSender().sendSmithingData(1291,3,1119,1);
		player.getPacketSender().sendSmithingData(1375,3,1120,1);
		player.getPacketSender().sendSmithingData(1117,3,1121,1);
		player.getPacketSender().sendSmithingData(1189,3,1122,1);
		player.getPacketSender().sendSmithingData(1307,4,1119,1);
		player.getPacketSender().sendSmithingData(4819,4,1122,15);
		player.getPacketSender().sendSmithingData(3095,4,1120, 1);
		player.getPacketSender().sendSmithingData(-1,3,1123, 1);
		player.getPacketSender().sendString(1135, "");
		player.getPacketSender().sendString(1134, "");
		player.getPacketSender().sendString(11461, "");
		player.getPacketSender().sendString(11459, "");
		player.getPacketSender().sendString(1132, "");
		player.getPacketSender().sendString(1096, "");
		player.getPacketSender().sendInterface(994);
		player.setSmithingInterfaceType(1);
	}

	public static void makeIronInterface(Player player) {
		String fiveb = GetForBars(2351, 5, player);
		String threeb = GetForBars(2351, 3, player);
		String twob = GetForBars(2351, 2, player);
		String oneb = GetForBars(2351, 1, player);
		player.getPacketSender().sendString(1112, fiveb+"5 Bars"+fiveb);
		player.getPacketSender().sendString(1109, threeb+"3 Bars"+threeb);
		player.getPacketSender().sendString(1110, threeb+"3 Bars"+threeb);
		player.getPacketSender().sendString(1118, threeb+"3 Bars"+threeb);
		player.getPacketSender().sendString(1111, threeb+"3 Bars"+threeb);
		player.getPacketSender().sendString(1095, threeb+"3 Bars"+threeb);
		player.getPacketSender().sendString(1115, threeb+"3 Bars"+threeb);
		player.getPacketSender().sendString(1090, threeb+"3 Bars"+threeb);
		player.getPacketSender().sendString(1113, twob+"2 Bars"+twob);
		player.getPacketSender().sendString(1116, twob+"2 Bars"+twob);
		player.getPacketSender().sendString(1114, twob+"2 Bars"+twob);
		player.getPacketSender().sendString(1089, twob+"2 Bars"+twob);
		player.getPacketSender().sendString(8428, twob+"2 Bars"+twob);
		player.getPacketSender().sendString(1124, oneb+"1 Bar"+oneb);
		player.getPacketSender().sendString(1125, oneb+"1 Bar"+oneb);
		player.getPacketSender().sendString(1126, oneb+"1 Bar"+oneb);
		player.getPacketSender().sendString(1127, oneb+"1 Bar"+oneb);
		player.getPacketSender().sendString(1128, oneb+"1 Bar"+oneb);
		player.getPacketSender().sendString(1129, oneb+"1 Bar"+oneb);
		player.getPacketSender().sendString(1130, oneb+"1 Bar"+oneb);
		player.getPacketSender().sendString(1131, oneb+"1 Bar"+oneb);
		player.getPacketSender().sendString(13357, oneb+"1 Bar"+oneb);
		player.getPacketSender().sendString(11459, oneb+"1 Bar"+oneb);
		player.getPacketSender().sendString(1101, GetForlvl(33, player)+"Plate Body"+GetForlvl(18, player));
		player.getPacketSender().sendString(1099, GetForlvl(31, player)+"Plate Legs"+GetForlvl(16, player));
		player.getPacketSender().sendString(1100, GetForlvl(31, player)+"Plate Skirt"+GetForlvl(16, player));
		sendString(player, (GetForlvl(29, player)+"2 Hand Sword"+GetForlvl(14, player)), 1088);
		sendString(player, (GetForlvl(27, player)+"Kite Shield"+GetForlvl(12, player)), 1105);
		sendString(player, (GetForlvl(26, player)+"Chain Body"+GetForlvl(11, player)), 1098);
		sendString(player, (GetForlvl(26, player)+"Oil Lantern Frame"+GetForlvl(11, player)),11461);
		sendString(player, (GetForlvl(25, player)+"Battle Axe"+GetForlvl(10, player)), 1092);
		sendString(player, (GetForlvl(24, player)+"Warhammer"+GetForlvl(9, player)), 1083);
		sendString(player, (GetForlvl(23, player)+"Square Shield"+GetForlvl(8, player)), 1104);
		sendString(player, (GetForlvl(22, player)+"Full Helm"+GetForlvl(7, player)), 1103);
		sendString(player, (GetForlvl(21, player)+"Throwing Knives"+GetForlvl(7, player)), 1106);
		sendString(player, (GetForlvl(21, player)+"Long Sword"+GetForlvl(6, player)), 1086);
		sendString(player, (GetForlvl(20, player)+"Scimitar"+GetForlvl(5, player)), 1087);
		sendString(player, (GetForlvl(20, player)+"Arrowtips"+GetForlvl(5, player)), 1108);
		sendString(player, (GetForlvl(19, player)+"Sword"+GetForlvl(4, player)), 1085);
		sendString(player, (GetForlvl(19, player)+"Bolts"+GetForlvl(4, player)), 9377);
		sendString(player, (GetForlvl(19, player)+"Nails"+GetForlvl(4, player)), 13358);
		sendString(player, (GetForlvl(18, player)+"Medium Helm"+GetForlvl(3, player)), 1102);
		sendString(player, (GetForlvl(17, player)+"Mace"+GetForlvl(2, player)), 1093);
		sendString(player, (GetForlvl(15, player)+"Dagger"+GetForlvl(1, player)), 1094);
		sendString(player, (GetForlvl(16, player)+"Axe"+GetForlvl(1, player)), 1091);
		player.getPacketSender().sendSmithingData(1203,0,1119,1);
		player.getPacketSender().sendSmithingData(1349,0,1120,1);
		player.getPacketSender().sendSmithingData(1101,0,1121,1);
		player.getPacketSender().sendSmithingData(1137,0,1122,1);
		player.getPacketSender().sendSmithingData(9377,0,1123,10);
		player.getPacketSender().sendSmithingData(1279,1,1119,1);
		player.getPacketSender().sendSmithingData(1420,1,1120,1);
		player.getPacketSender().sendSmithingData(1067,1,1121,1);
		player.getPacketSender().sendSmithingData(1153,1,1122,1);
		player.getPacketSender().sendSmithingData(40,1,1123,15);
		player.getPacketSender().sendSmithingData(1323,2,1119,1);
		player.getPacketSender().sendSmithingData(1335,2,1120,1);
		player.getPacketSender().sendSmithingData(1081,2,1121,1);
		player.getPacketSender().sendSmithingData(1175,2,1122,1);
		player.getPacketSender().sendSmithingData(863,2,1123,5);
		player.getPacketSender().sendSmithingData(1293,3,1119,1);
		player.getPacketSender().sendSmithingData(1363,3,1120,1);
		player.getPacketSender().sendSmithingData(1115,3,1121,1);
		player.getPacketSender().sendSmithingData(1191,3,1122,1);
		player.getPacketSender().sendSmithingData(1309,4,1119,1);
		player.getPacketSender().sendSmithingData(4820,4,1122,15);
		player.getPacketSender().sendSmithingData(4540,4,1121,1);
		player.getPacketSender().sendSmithingData(3096,4,1120, 1);
		player.getPacketSender().sendSmithingData(-1,3,1123, 1);
		sendString(player, "",1135);
		sendString(player, "",1134);
		sendString(player, "",1132);
		sendString(player, "",1096);
		player.getPacketSender().sendInterface(994);
		player.setSmithingInterfaceType(2);
	}

	public static void makeSteelInterface(Player player) {
		String fiveb = GetForBars(2353, 5, player);
		String threeb = GetForBars(2353, 3, player);
		String twob = GetForBars(2353, 2, player);
		String oneb = GetForBars(2353, 1, player);
		sendString(player, fiveb+"5 Bars"+fiveb, 1112);
		sendString(player, threeb+"3 Bars"+threeb, 1109);
		sendString(player, threeb+"3 Bars"+threeb, 1110);
		sendString(player, threeb+"3 Bars"+threeb, 1118);
		sendString(player, threeb+"3 Bars"+threeb, 1111);
		sendString(player, threeb+"3 Bars"+threeb, 1095);
		sendString(player, threeb+"3 Bars"+threeb, 1115);
		sendString(player, threeb+"3 Bars"+threeb, 1090);
		sendString(player, twob+"2 Bars"+twob, 1113);
		sendString(player, twob+"2 Bars"+twob, 1116);
		sendString(player, twob+"2 Bars"+twob, 1114);
		sendString(player, twob+"2 Bars"+twob, 1089);
		sendString(player, twob+"2 Bars"+twob, 8428);
		sendString(player, oneb+"1 Bar"+oneb, 1124);
		sendString(player, oneb+"1 Bar"+oneb, 1125);
		sendString(player, oneb+"1 Bar"+oneb, 1126);
		sendString(player, oneb+"1 Bar"+oneb, 1127);
		sendString(player, oneb+"1 Bar"+oneb, 1128);
		sendString(player, oneb+"1 Bar"+oneb, 1129);
		sendString(player, oneb+"1 Bar"+oneb, 1130);
		sendString(player, oneb+"1 Bar"+oneb, 1131);
		sendString(player, oneb+"1 Bar"+oneb, 13357);
		sendString(player, oneb+"1 Bar"+oneb,1132);
		sendString(player, oneb+"1 Bar"+oneb,1135);
		sendString(player, "", 11459);
		sendString(player, GetForlvl(48, player)+"Plate Body"+GetForlvl(18, player), 1101);
		sendString(player, GetForlvl(46, player)+"Plate Legs"+GetForlvl(16, player), 1099);
		sendString(player, GetForlvl(46, player)+"Plate Skirt"+GetForlvl(16, player), 1100);
		sendString(player, GetForlvl(44, player)+"2 Hand Sword"+GetForlvl(14, player), 1088);
		sendString(player, GetForlvl(42, player)+"Kite Shield"+GetForlvl(12, player), 1105);
		sendString(player, GetForlvl(41, player)+"Chain Body"+GetForlvl(11, player), 1098);
		sendString(player, "",11461);
		sendString(player, GetForlvl(40, player)+"Battle Axe"+GetForlvl(10, player), 1092);
		sendString(player, GetForlvl(39, player)+"Warhammer"+GetForlvl(9, player), 1083);
		sendString(player, GetForlvl(38, player)+"Square Shield"+GetForlvl(8, player), 1104);
		sendString(player, GetForlvl(37, player)+"Full Helm"+GetForlvl(7, player), 1103);
		sendString(player, GetForlvl(37, player)+"Throwing Knives"+GetForlvl(7, player), 1106);
		sendString(player, GetForlvl(36, player)+"Long Sword"+GetForlvl(6, player), 1086);
		sendString(player, GetForlvl(35, player)+"Scimitar"+GetForlvl(5, player), 1087);
		sendString(player, GetForlvl(35, player)+"Arrowtips"+GetForlvl(5, player), 1108);
		sendString(player, GetForlvl(34, player)+"Sword"+GetForlvl(4, player), 1085);
		sendString(player, GetForlvl(34, player)+"Bolts"+GetForlvl(4, player), 9378);
		sendString(player, GetForlvl(34, player)+"Nails"+GetForlvl(4, player), 13358);
		sendString(player, GetForlvl(33, player)+"Medium Helm"+GetForlvl(3, player), 1102);
		sendString(player, GetForlvl(32, player)+"Mace"+GetForlvl(2, player), 1093);
		sendString(player, GetForlvl(30, player)+"Dagger"+GetForlvl(1, player), 1094);
		sendString(player, GetForlvl(31, player)+"Axe"+GetForlvl(1, player), 1091);
		sendString(player, GetForlvl(35, player)+"Cannon Ball"+GetForlvl(35, player),1096);
		sendString(player, GetForlvl(36, player)+"Studs"+GetForlvl(36, player),1134);
		player.getPacketSender().sendSmithingData(1207,0,1119,1);
		player.getPacketSender().sendSmithingData(1353,0,1120,1);
		player.getPacketSender().sendSmithingData(1105,0,1121,1);
		player.getPacketSender().sendSmithingData(1141,0,1122,1);
		player.getPacketSender().sendSmithingData(9378,0,1123,10);
		player.getPacketSender().sendSmithingData(1281,1,1119,1);
		player.getPacketSender().sendSmithingData(1424,1,1120,1);
		player.getPacketSender().sendSmithingData(1069,1,1121,1);
		player.getPacketSender().sendSmithingData(1157,1,1122,1);
		player.getPacketSender().sendSmithingData(41,1,1123,15);
		player.getPacketSender().sendSmithingData(1325,2,1119,1);
		player.getPacketSender().sendSmithingData(1339,2,1120,1);
		player.getPacketSender().sendSmithingData(1083,2,1121,1);
		player.getPacketSender().sendSmithingData(1177,2,1122,1);
		player.getPacketSender().sendSmithingData(865,2,1123,5);
		player.getPacketSender().sendSmithingData(1295,3,1119,1);
		player.getPacketSender().sendSmithingData(1365,3,1120,1);
		player.getPacketSender().sendSmithingData(1119,3,1121,1);
		player.getPacketSender().sendSmithingData(1193,3,1122,1); //sec lazoh //ok
		player.getPacketSender().sendSmithingData(1311,4,1119,1);
		player.getPacketSender().sendSmithingData(1539,4,1122,15);
		player.getPacketSender().sendSmithingData(2,3,1123,40);
		player.getPacketSender().sendSmithingData(2370,4,1123,1);
		player.getPacketSender().sendSmithingData(3097,4,1120, 1);
		player.getPacketSender().sendInterface(994);
		player.setSmithingInterfaceType(3);
	}

	public static void makeMithInterface(Player player) {
		String fiveb = GetForBars(2359, 5, player);
		String threeb = GetForBars(2359, 3, player);
		String twob = GetForBars(2359, 2, player);
		String oneb = GetForBars(2359, 1, player);
		sendString(player, fiveb+"5 Bars"+fiveb, 1112);
		sendString(player, threeb+"3 Bars"+threeb, 1109);
		sendString(player, threeb+"3 Bars"+threeb, 1110);
		sendString(player, threeb+"3 Bars"+threeb, 1118);
		sendString(player, threeb+"3 Bars"+threeb, 1111);
		sendString(player, threeb+"3 Bars"+threeb, 1095);
		sendString(player, threeb+"3 Bars"+threeb, 1115);
		sendString(player, threeb+"3 Bars"+threeb, 1090);
		sendString(player, twob+"2 Bars"+twob, 1113);
		sendString(player, twob+"2 Bars"+twob, 1116);
		sendString(player, twob+"2 Bars"+twob, 1114);
		sendString(player, twob+"2 Bars"+twob, 1089);
		sendString(player, twob+"2 Bars"+twob, 8428);
		sendString(player, oneb+"1 Bar"+oneb, 1124);
		sendString(player, oneb+"1 Bar"+oneb, 1125);
		sendString(player, oneb+"1 Bar"+oneb, 1126);
		sendString(player, oneb+"1 Bar"+oneb, 1127);
		sendString(player, oneb+"1 Bar"+oneb, 1128);
		sendString(player, oneb+"1 Bar"+oneb, 1129);
		sendString(player, oneb+"1 Bar"+oneb, 1130);
		sendString(player, oneb+"1 Bar"+oneb, 1131);
		sendString(player, oneb+"1 Bar"+oneb, 13357);
		sendString(player, oneb+"1 Bar"+oneb, 11459);
		sendString(player, GetForlvl(68, player)+"Plate Body"+GetForlvl(18, player), 1101);
		sendString(player, GetForlvl(66, player)+"Plate Legs"+GetForlvl(16, player), 1099);
		sendString(player, GetForlvl(66, player)+"Plate Skirt"+GetForlvl(16, player), 1100);
		sendString(player, GetForlvl(64, player)+"2 Hand Sword"+GetForlvl(14, player), 1088);
		sendString(player, GetForlvl(62, player)+"Kite Shield"+GetForlvl(12, player), 1105);
		sendString(player, GetForlvl(61, player)+"Chain Body"+GetForlvl(11, player), 1098);
		sendString(player, GetForlvl(60, player)+"Battle Axe"+GetForlvl(10, player), 1092);
		sendString(player, GetForlvl(59, player)+"Warhammer"+GetForlvl(9, player), 1083);
		sendString(player, GetForlvl(58, player)+"Square Shield"+GetForlvl(8, player), 1104);
		sendString(player, GetForlvl(57, player)+"Full Helm"+GetForlvl(7, player), 1103);
		sendString(player, GetForlvl(57, player)+"Throwing Knives"+GetForlvl(7, player), 1106);
		sendString(player, GetForlvl(56, player)+"Long Sword"+GetForlvl(6, player), 1086);
		sendString(player, GetForlvl(55, player)+"Scimitar"+GetForlvl(5, player), 1087);
		sendString(player, GetForlvl(55, player)+"Arrowtips"+GetForlvl(5, player), 1108);
		sendString(player, GetForlvl(54, player)+"Sword"+GetForlvl(4, player), 1085);
		sendString(player, GetForlvl(54, player)+"Bolts"+GetForlvl(4, player), 9379);
		sendString(player, GetForlvl(54, player)+"Nails"+GetForlvl(4, player), 13358);
		sendString(player, GetForlvl(53, player)+"Medium Helm"+GetForlvl(3, player), 1102);
		sendString(player, GetForlvl(52, player)+"Mace"+GetForlvl(2, player), 1093);
		sendString(player, GetForlvl(50, player)+"Dagger"+GetForlvl(1, player), 1094);
		sendString(player, GetForlvl(51, player)+"Axe"+GetForlvl(1, player), 1091);
		player.getPacketSender().sendSmithingData(1209,0,1119,1); //dagger
		player.getPacketSender().sendSmithingData(1355,0,1120,1); //axe
		player.getPacketSender().sendSmithingData(1109,0,1121,1); //chain body
		player.getPacketSender().sendSmithingData(1143,0,1122,1); //med helm
		player.getPacketSender().sendSmithingData(9379,0,1123,10); //Bolts
		player.getPacketSender().sendSmithingData(1285,1,1119,1); //s-sword
		player.getPacketSender().sendSmithingData(1428,1,1120,1); //mace
		player.getPacketSender().sendSmithingData(1071,1,1121,1); //platelegs
		player.getPacketSender().sendSmithingData(1159,1,1122,1); //full helm
		player.getPacketSender().sendSmithingData(42,1,1123,15); //arrowtips
		player.getPacketSender().sendSmithingData(1329,2,1119,1); //scimmy
		player.getPacketSender().sendSmithingData(1343,2,1120,1); //warhammer
		player.getPacketSender().sendSmithingData(1085,2,1121,1); //plateskirt
		player.getPacketSender().sendSmithingData(1181,2,1122,1); //Sq. Shield
		player.getPacketSender().sendSmithingData(866,2,1123,5); //throwing-knives
		player.getPacketSender().sendSmithingData(1299,3,1119,1); //longsword
		player.getPacketSender().sendSmithingData(1369,3,1120,1); //battleaxe
		player.getPacketSender().sendSmithingData(1121,3,1121,1); //platebody
		player.getPacketSender().sendSmithingData(1197,3,1122,1); //kiteshield
		player.getPacketSender().sendSmithingData(1315,4,1119,1); //2h sword
		player.getPacketSender().sendSmithingData(4822,4,1122,15); //nails
		player.getPacketSender().sendSmithingData(3099,4,1120, 1);
		player.getPacketSender().sendSmithingData(-1,3,1123, 1);
		sendString(player, "",1135);
		sendString(player, "",1134);
		sendString(player, "",11461);
		sendString(player, "",11459);
		sendString(player, "",1132);
		sendString(player, "",1096);
		player.getPacketSender().sendInterface(994);
		player.setSmithingInterfaceType(4);
	}

	public static void makeAddyInterface(Player player) {
		String fiveb = GetForBars(2361, 5, player);
		String threeb = GetForBars(2361, 3, player);
		String twob = GetForBars(2361, 2, player);
		String oneb = GetForBars(2361, 1, player);
		sendString(player, fiveb+"5 Bars"+fiveb, 1112);
		sendString(player, threeb+"3 Bars"+threeb, 1109);
		sendString(player, threeb+"3 Bars"+threeb, 1110);
		sendString(player, threeb+"3 Bars"+threeb, 1118);
		sendString(player, threeb+"3 Bars"+threeb, 1111);
		sendString(player, threeb+"3 Bars"+threeb, 1095);
		sendString(player, threeb+"3 Bars"+threeb, 1115);
		sendString(player, threeb+"3 Bars"+threeb, 1090);
		sendString(player, twob+"2 Bars"+twob, 1113);
		sendString(player, twob+"2 Bars"+twob, 1116);
		sendString(player, twob+"2 Bars"+twob, 1114);
		sendString(player, twob+"2 Bars"+twob, 1089);
		sendString(player, twob+"2 Bars"+twob, 8428);
		sendString(player, oneb+"1 Bar"+oneb, 1124);
		sendString(player, oneb+"1 Bar"+oneb, 1125);
		sendString(player, oneb+"1 Bar"+oneb, 1126);
		sendString(player, oneb+"1 Bar"+oneb, 1127);
		sendString(player, oneb+"1 Bar"+oneb, 1128);
		sendString(player, oneb+"1 Bar"+oneb, 1129);
		sendString(player, oneb+"1 Bar"+oneb, 1130);
		sendString(player, oneb+"1 Bar"+oneb, 1131);
		sendString(player, oneb+"1 Bar"+oneb, 13357);
		sendString(player, oneb+"1 Bar"+oneb, 11459);
		sendString(player, GetForlvl(88, player)+"Plate Body"+GetForlvl(18, player), 1101);
		sendString(player, GetForlvl(86, player)+"Plate Legs"+GetForlvl(16, player), 1099);
		sendString(player, GetForlvl(86, player)+"Plate Skirt"+GetForlvl(16, player), 1100);
		sendString(player, GetForlvl(84, player)+"2 Hand Sword"+GetForlvl(14, player), 1088);
		sendString(player, GetForlvl(82, player)+"Kite Shield"+GetForlvl(12, player), 1105);
		sendString(player, GetForlvl(81, player)+"Chain Body"+GetForlvl(11, player), 1098);
		sendString(player, GetForlvl(80, player)+"Battle Axe"+GetForlvl(10, player), 1092);
		sendString(player, GetForlvl(79, player)+"Warhammer"+GetForlvl(9, player), 1083);
		sendString(player, GetForlvl(78, player)+"Square Shield"+GetForlvl(8, player), 1104);
		sendString(player, GetForlvl(77, player)+"Full Helm"+GetForlvl(7, player), 1103);
		sendString(player, GetForlvl(77, player)+"Throwing Knives"+GetForlvl(7, player), 1106);
		sendString(player, GetForlvl(76, player)+"Long Sword"+GetForlvl(6, player), 1086);
		sendString(player, GetForlvl(75, player)+"Scimitar"+GetForlvl(5, player), 1087);
		sendString(player, GetForlvl(75, player)+"Arrowtips"+GetForlvl(5, player), 1108);
		sendString(player, GetForlvl(74, player)+"Sword"+GetForlvl(4, player), 1085);
		sendString(player, GetForlvl(74, player)+"Bolts"+GetForlvl(4, player), 9380);
		sendString(player, GetForlvl(74, player)+"Nails"+GetForlvl(4, player), 13358);
		sendString(player, GetForlvl(73, player)+"Medium Helm"+GetForlvl(3, player), 1102);
		sendString(player, GetForlvl(72, player)+"Mace"+GetForlvl(2, player), 1093);
		sendString(player, GetForlvl(70, player)+"Dagger"+GetForlvl(1, player), 1094);
		sendString(player, GetForlvl(71, player)+"Axe"+GetForlvl(1, player), 1091);
		player.getPacketSender().sendSmithingData(1211,0,1119,1); //dagger
		player.getPacketSender().sendSmithingData(1357,0,1120,1); //axe
		player.getPacketSender().sendSmithingData(1111,0,1121,1); //chain body
		player.getPacketSender().sendSmithingData(1145,0,1122,1); //med helm
		player.getPacketSender().sendSmithingData(9380,0,1123,10); //Bolts
		player.getPacketSender().sendSmithingData(1287,1,1119,1); //s-sword
		player.getPacketSender().sendSmithingData(1430,1,1120,1); //mace
		player.getPacketSender().sendSmithingData(1073,1,1121,1); //platelegs
		player.getPacketSender().sendSmithingData(1161,1,1122,1); //full helm
		player.getPacketSender().sendSmithingData(43,1,1123,15); //arrowtips
		player.getPacketSender().sendSmithingData(1331,2,1119,1); //scimmy
		player.getPacketSender().sendSmithingData(1345,2,1120,1); //warhammer
		player.getPacketSender().sendSmithingData(1091,2,1121,1); //plateskirt
		player.getPacketSender().sendSmithingData(1183,2,1122,1); //Sq. Shield
		player.getPacketSender().sendSmithingData(867,2,1123,5); //throwing-knives
		player.getPacketSender().sendSmithingData(1301,3,1119,1); //longsword
		player.getPacketSender().sendSmithingData(1371,3,1120,1); //battleaxe
		player.getPacketSender().sendSmithingData(1123,3,1121,1); //platebody
		player.getPacketSender().sendSmithingData(1199,3,1122,1); //kiteshield
		player.getPacketSender().sendSmithingData(1317,4,1119,1); //2h sword
		player.getPacketSender().sendSmithingData(4823,4,1122,15); //nails
		player.getPacketSender().sendSmithingData(3100,4,1120, 1); // claws
		player.getPacketSender().sendSmithingData(-1,3,1123, 1);
		sendString(player, "",1135);
		sendString(player, "",1134);
		sendString(player, "",11461);
		sendString(player, "",11459);
		sendString(player, "",1132);
		sendString(player, "",1096);
		player.getPacketSender().sendInterface(994);
		player.setSmithingInterfaceType(5);
	}

	public static void makeRuneInterface(Player player) {
		String fiveb = GetForBars(2363, 5, player);
		String threeb = GetForBars(2363, 3, player);
		String twob = GetForBars(2363, 2, player);
		String oneb = GetForBars(2363, 1, player);
		sendString(player, fiveb+"5 Bars"+fiveb, 1112);
		sendString(player, threeb+"3 Bars"+threeb, 1109);
		sendString(player, threeb+"3 Bars"+threeb, 1110);
		sendString(player, threeb+"3 Bars"+threeb, 1118);
		sendString(player, threeb+"3 Bars"+threeb, 1111);
		sendString(player, threeb+"3 Bars"+threeb, 1095);
		sendString(player, threeb+"3 Bars"+threeb, 1115);
		sendString(player, threeb+"3 Bars"+threeb, 1090);
		sendString(player, twob+"2 Bars"+twob, 1113);
		sendString(player, twob+"2 Bars"+twob, 1116);
		sendString(player, twob+"2 Bars"+twob, 1114);
		sendString(player, twob+"2 Bars"+twob, 1089);
		sendString(player, twob+"2 Bars"+twob, 8428);
		sendString(player, oneb+"1 Bar"+oneb, 1124);
		sendString(player, oneb+"1 Bar"+oneb, 1125);
		sendString(player, oneb+"1 Bar"+oneb, 1126);
		sendString(player, oneb+"1 Bar"+oneb, 1127);
		sendString(player, oneb+"1 Bar"+oneb, 1128);
		sendString(player, oneb+"1 Bar"+oneb, 1129);
		sendString(player, oneb+"1 Bar"+oneb, 1130);
		sendString(player, oneb+"1 Bar"+oneb, 1131);
		sendString(player, oneb+"1 Bar"+oneb, 13357);
		sendString(player, oneb+"1 Bar"+oneb, 11459);
		sendString(player, GetForlvl(88, player)+"Plate Body"+GetForlvl(18, player), 1101);
		sendString(player, GetForlvl(99, player)+"Plate Legs"+GetForlvl(16, player), 1099);
		sendString(player, GetForlvl(99, player)+"Plate Skirt"+GetForlvl(16, player), 1100);
		sendString(player, GetForlvl(99, player)+"2 Hand Sword"+GetForlvl(14, player), 1088);
		sendString(player, GetForlvl(97, player)+"Kite Shield"+GetForlvl(12, player), 1105);
		sendString(player, GetForlvl(96, player)+"Chain Body"+GetForlvl(11, player), 1098);
		sendString(player, GetForlvl(95, player)+"Battle Axe"+GetForlvl(10, player), 1092);
		sendString(player, GetForlvl(94, player)+"Warhammer"+GetForlvl(9, player), 1083);
		sendString(player, GetForlvl(93, player)+"Square Shield"+GetForlvl(8, player), 1104);
		sendString(player, GetForlvl(92, player)+"Full Helm"+GetForlvl(7, player), 1103);
		sendString(player, GetForlvl(92, player)+"Throwing Knives"+GetForlvl(7, player), 1106);
		sendString(player, GetForlvl(91, player)+"Long Sword"+GetForlvl(6, player), 1086);
		sendString(player, GetForlvl(90, player)+"Scimitar"+GetForlvl(5, player), 1087);
		sendString(player, GetForlvl(90, player)+"Arrowtips"+GetForlvl(5, player), 1108);
		sendString(player, GetForlvl(89, player)+"Sword"+GetForlvl(4, player), 1085);
		sendString(player, GetForlvl(89, player)+"Bolts"+GetForlvl(4, player), 9381);
		sendString(player, GetForlvl(89, player)+"Nails"+GetForlvl(4, player), 13358);
		sendString(player, GetForlvl(88, player)+"Medium Helm"+GetForlvl(3, player), 1102);
		sendString(player, GetForlvl(87, player)+"Mace"+GetForlvl(2, player), 1093);
		sendString(player, GetForlvl(85, player)+"Dagger"+GetForlvl(1, player), 1094);
		sendString(player, GetForlvl(86, player)+"Axe"+GetForlvl(1, player), 1091);
		player.getPacketSender().sendSmithingData(1213,0,1119,1); //dagger
		player.getPacketSender().sendSmithingData(1359,0,1120,1); //axe
		player.getPacketSender().sendSmithingData(1113,0,1121,1); //chain body
		player.getPacketSender().sendSmithingData(1147,0,1122,1); //med helm
		player.getPacketSender().sendSmithingData(9381,0,1123,10); //Bolts
		player.getPacketSender().sendSmithingData(1289,1,1119,1); //s-sword
		player.getPacketSender().sendSmithingData(1432,1,1120,1); //mace
		player.getPacketSender().sendSmithingData(1079,1,1121,1); //platelegs
		player.getPacketSender().sendSmithingData(1163,1,1122,1); //full helm
		player.getPacketSender().sendSmithingData(44,1,1123,15); //arrowtips
		player.getPacketSender().sendSmithingData(1333,2,1119,1); //scimmy
		player.getPacketSender().sendSmithingData(1347,2,1120,1); //warhammer
		player.getPacketSender().sendSmithingData(1093,2,1121,1); //plateskirt
		player.getPacketSender().sendSmithingData(1185,2,1122,1); //Sq. Shield
		player.getPacketSender().sendSmithingData(868,2,1123,5); //throwing-knives
		player.getPacketSender().sendSmithingData(1303,3,1119,1); //longsword
		player.getPacketSender().sendSmithingData(1373,3,1120,1); //battleaxe
		player.getPacketSender().sendSmithingData(1127,3,1121,1); //platebody
		player.getPacketSender().sendSmithingData(1201,3,1122,1); //kiteshield
		player.getPacketSender().sendSmithingData(1319,4,1119,1); //2h sword
		player.getPacketSender().sendSmithingData(4824,4,1122,15); //nails
		player.getPacketSender().sendSmithingData(-1,3,1123, 1);
		player.getPacketSender().sendSmithingData(3101,4,1120, 1); // claws
		sendString(player, "",1135);
		sendString(player, "",1134);
		sendString(player, "",11461);
		sendString(player, "",11459);
		sendString(player, "",1132);
		sendString(player, "",1096);
		player.getPacketSender().sendInterface(994);
		player.setSmithingInterfaceType(6);
	}

	public static void sendString(Player player, String s, int i) {
		player.getPacketSender().sendString(i, s);
	}

	private static String GetForlvl(int i, Player player) {
		if (player.getSkills().hasLevel(Skill.SMITHING, i))
			return FontUtils.WHITE;
		return FontUtils.BLACK;
	}

	private static String GetForBars(int i, int j, Player player) {
		if (player.getInventory().getAmount(new Item(i)) >= j)
			return FontUtils.GREEN;
		return FontUtils.RED;
	}

	public static int getItemAmount(Item item) {
		String name = item.getName().toLowerCase();
		if(name.contains("cannon")) {
			return 40;
		} else if(name.contains("knife")) {
			return 5;
		} else if(name.contains("arrowtips") || name.contains("nails")) {
			return 15;
		} else if(name.contains("dart tip") || name.contains("bolts")) {
			return 10;
		}
		return 1;
	}

	public static int getBarAmount(Item item) {
		String name = item.getName().toLowerCase();
		if(name.contains("scimitar") || name.contains("claws") || name.contains("longsword") || name.contains("sq shield") || name.contains("full helm")) {
			return 2;
		} else if(name.contains("2h sword") || name.contains("warhammer") || name.contains("battleaxe") || name.contains("chainbody") || name.contains("platelegs") || name.contains("plateskirt") || name.contains("kiteshield")) {
			return 3;
		} else if(name.contains("platebody")) {
			return 5;
		}
		return 1;
	}

	public enum SmithData {
		BRONZE_DAGGER(1205, 12.5, 1),
		BRONZE_HATCHET(1351, 12.5, 1),
		BRONZE_MACE(1422, 12.5, 2),
		BRONZE_MED_HELM(1139, 12.5, 3),
		BRONZE_BOLTS_UNF(9375, 12.5, 3),
		BRONZE_SWORD(1277, 12.5, 4),
		BRONZE_NAILS(4819, 12.5, 4),
		BRONZE_WIRE(1794,12.5,4),
		BRONZE_DART_TIPS(819, 12.5, 4),
		BRONZE_ARROW_TIPS(39,12.5,5),
		BRONZE_SCIMITAR(1321,25,5),
		BRONZE_PICKAXE(1265,25,5),
		BRONZE_LONGSWORD(1291,12.5,6),
		BRONZE_LIMBS(9420,12.5,6),
		BRONZE_FULL_HELM(1155,25,7),
		BRONZE_THROWING_KNIVES(864,12.5,7),
		BRONZE_SQUARE_SHIELD(1173,25,8),
		BRONZE_WARHAMMER(1337,37.5,9),
		BRONZE_BATTLEAXE(1375,37.5,10),
		BRONZE_CHAINBODY(1103,37.5,11),
		BRONZE_KITESHIELD(1189,37.5,12),
		BRONZE_CLAWS(3095,25,13),
		BRONZE_2H_SWORD(1307,37.5,14),
		BRONZE_PLATESKIRT(1087,37.5,16),
		BRONZE_PLATELEGS(1075,37.5,16),
		BRONZE_PLATEBODY(1117,37.5,18),
		IRON_DAGGER(1203,25,15),
		IRON_HATCHET(1349,25,16),
		IRON_MACE(1420,25,17),
		IRON_SPIT(7225,25,17),
		IRON_MED_HELM(1137,25,18),
		IRON_BOLTS_UNF(9377,25,18),
		IRON_SWORD(1279,25,19),
		IRON_NAILS(4820,25,19),
		IRON_DART_TIPS(820,25,20),
		IRON_ARROW_TIPS(40,25,20),
		IRON_SCIMITAR(1323,50,20),
		IRON_PACKAXE(1267,50,20),
		IRON_LONGSWORD(1293,50,21),
		IRON_FULLHELM(1153,50,22),
		IRON_THROWING_KNIVES(863,25,22),
		IRON_SQUARE_SHIELD(1175,50,23),
		IRON_LIMBS(9423,25,23),
		IRON_WARHAMMER(1335,75,24),
		IRON_BATTLEAXE(1363,75,25),
		IRON_CHAINBODY(1101,75,26),
		IRON_OIL_lATERN_FRAME(4540,25,26),
		IRON_KITESHIELD(1191,75,27),
		IRON_CLAWS(3096,50,28),
		IRON_2H_SWORD(1309,75,29),
		IRON_PLATESKIRT(1081,75,31),
		IRON_PLATELEGS(1067,75,31),
		IRON_PLATE_BODY(1115,125,33),
		STEEL_DAGGER(1207,37.5,30),
		STEEL_HATCHET(1353,37.5,31),
		STEEL_MACE(1424,37.5,32),
		STEEL_MED_HELM(1141,37.5,33),
		STEEL_BOLTS_UNF(9378,37.5,33),
		STEEL_NAILS(1539,37.5,34),
		STEEL_SWORD(1281,37.5,34),
		STEEL_DART_TIPS(821,37.5,34),
		STEEL_ARROW_TIPS(41,37.5,35),
		STEEL_SCIMITAR(1325,75,35),
		STEEL_PICKAXE(1269,75,35),
		STEEL_LONGSWORD(1295,75,36),
		STEEL_STUDS(2370,37.5,36),
		STEEL_CROSSBOW_LIMBS(9425,37.5,36),
		STEEL_FULL_HELM(1157,75,37),
		STEEL_KNIFES(865,37.5,37),
		STEEL_SQUARE_SHIELD(1177,75,38),
		STEEL_WARHAMMER(1339,112.5,39),
		STEEL_BATTLEAXE(1365,112.5,40),
		STEEL_CHAIN_BODY(1105,112.5,41),
		STEEL_KITESHIELD(1193,112.5,42),
		CANNON_BALL(2,25.6,35),
		STEEL_CLAWS(3097,75,43),
		STEEL_2H_SWORD(1311,112.5,44),
		STEEL_PLATESKIRT(1084,112.5,46),
		STEEL_PLATELEGS(1069,112.5,46),
		STEEL_PLATEBODY(1119,187.5,48),
		MITHRIL_DAGGER(1209,50,50),
		MITHRIL_HATCHET(1355,50,51),
		MITHRIL_MACE(1428,50,52),
		MITHRIL_MED_HELM(1143,50,53),
		MITHRIL_BOLTS_UNF(9379,50,53),
		MITHRIL_SWORD(1285,50,54),
		MITHRIL_NAILS(4822,50,54),
		MITHRIL_DART_TIPS(822,50,54),
		MITHRIL_ARROW_TIPS(42,50,55),
		MITHRIL_SCIMITAR(1329,100,55),
		MITHRIL_PICKAXE(1273,100,55),
		MITHRIL_LONGSWORD(1299,100,56),
		MITHRIL_LIMBS(9427,50,56),
		MITHRIL_FULLHELM(1159,100,57),
		MITHRIL_KNIVES(866,50,57),
		MITHRIL_SQUARE_SHIELD(1181,100,58),
		MITHRIL_WAR_HAMMER(1343,150,59),
		MITHRIL_GRAPPLE_TIP(9416,50,59),
		MITHRIL_BATTLEAXE(1369,150,60),
		MITHRIL_CHAIN_BODY(1109,150,61),
		MITHRIL_KITE_SHIELD(1197,150,62),
		MITHRIL_CLAWS(3099,100,63),
		MITHRIL_2H_SWORD(1315,150,64),
		MITHRIL_PLATESKIRT(1085,150,66),
		MITHRIL_PLATELEGS(1071,150,66),
		MITHRIL_PLATEBODY(1121,250,68),
		ADAMANT_DAGGER(1211,62.5,70),
		ADAMANT_HATCHET(1357,62.5,71),
		ADAMANT_MACE(1430,62.5,72),
		ADAMANT_MED_HELM(1145,62.5,73),
		ADAMANT_BOLTS_UNF(9380,62.5,73),
		ADAMANT_SWORD(1287,62.5,74),
		ADAMANT_NAILS(4823,62.5,74),
		ADAMANT_DART_TIPS(823,62.5,74),
		ADAMANT_ARROW_TIPS(43,62.5,75),
		ADAMANT_SCIMITAR(1331,125,75),
		ADAMANT_PICKAXE(1271,125,75),
		ADAMANT_LONGSWORD(1301,125,76),
		ADAMANT_LIMBS(9429,62.5,76),
		ADAMANT_FULL_HELM(1161,125,77),
		ADAMANT_KNIVES(867,62.5,77),
		ADAMANT_SQUARE_SHIELD(1183,125,78),
		ADAMANT_WAR_HAMMER(1345,187.5,79),
		ADAMANT_BATTLEAXE(1371,187.5,80),
		ADAMANT_CHAINBODY(1111,187.5,81),
		ADAMANT_KITESHIELD(1199,187.5,82),
		ADAMANT_CLAWS(3100,125,83),
		ADAMANT_2H_SWORD(1317,187.5,84),
		ADAMANT_PLATESKIRT(1091,187.5,86),
		ADAMANT_PLATELEGS(1073,187.5,86),
		ADAMANT_PLATEBODY(1123,312.5,88),
		RUNE_DAGGER(1213,75,51),
		RUNE_AXE(1359,75,85),
		RUNE_MACE(1432,75,86),
		RUNE_MED_HELM(1147,75,87),
		RUNE_BOLTS_UNF(9381,75,88),
		RUNE_SWORD(1289,75,89),
		RUNE_NAILS(4824,75,89),
		RUNE_DART_TIPS(824,75,90),
		RUNE_ARROW_TIPS(44,75,90),
		RUNE_SCIMITAR(1333,150,90),
		RUNE_PICKAXE(1275,150,91),
		RUNE_LONGSWORD(1303,150,91),
		RUNE_LIMBS(9431,75,91),
		RUNE_FULL_HELM(1163,150,92),
		RUNE_RUNE_KNIVES(868,75,92),
		RUNE_SQUARE_SHIELD(1185,150,93),
		RUNE_WARHAMMER(1347,225,94),
		RUNE_BATTLEAXE(1373,225,95),
		RUNE_CHAINBODY(1113,225,96),
		RUNE_KITESHIELD(1201,225,97),
		RUNE_CLAWS(3101,150,98),
		RUNE_2H_SWORD(1319,225,99),
		RUNE_PLATESKIRT(1093,225,99),
		RUNE_PLATELEGS(1079,225,99),
		RUNE_PLATEBODY(1127,375,99);
		
		SmithData(int id, double experience, int levelReq) {
			this.id = id;
			this.experience = experience;
			this.levelReq = levelReq;
		}
		
		private int id, levelReq;
		private double experience;
		
		public int getId() {
			return this.id;
		}
		public int getLevelReq() {
			return this.levelReq;
		}
		public double getExperience() {
			return experience;
		}
		public static SmithData forId(Item item) {
			return forId(item.getId());
		}
		public static SmithData forId(int id) {
			for(SmithData data : SmithData.values()) {
				if(data.getId() == id) {
					return data;
				}
			}
			return null;
		}
	}

	public static boolean ironOreSuccess(Player player) {
		return Misc.getRandom((int) (1 + player.getSkills().getLevel(Skill.SMITHING) / 0.5)) > 5;
	}
}
