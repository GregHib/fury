package com.fury.game.entity.character.player.content;

import com.fury.cache.def.npc.NpcDefinition;
import com.fury.game.container.impl.equip.Slot;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import com.fury.game.world.GameWorld;
import com.fury.util.Misc;

public class Sounds {

	public enum Sound {
		ROTATING_CANNON(new int[] {941}),
		FIRING_CANNON(new int[] {341}),
		LEVELUP(new int[] {51}),
		DRINK_POTION(new int[] {334}),
		EAT_FOOD(new int[] {317}),
		EQUIP_ITEM(new int[] {319, 320}),
		DROP_ITEM(new int[] {376}),
		PICKUP_ITEM(new int[] {358, 359}),
		SMITH_ITEM(new int[] {464, 468}),
		SMELT_ITEM(new int[] {352}),
		MINE_ITEM(new int[] {429, 431, 432}),
		FLETCH_ITEM(new int[] {375}),
		WOODCUT(new int[] {471, 472, 473}),
		LIGHT_FIRE(new int[] {811}),
		TELEPORT(new int[] {202, 201}),
		ACTIVATE_PRAYER_OR_CURSE(new int[] {433}),
		DEACTIVATE_PRAYER_OR_CURSE(new int[] {435}),
		RUN_OUT_OF_PRAYER_POINTS(new int[] {438}),
		BURY_BONE(new int[] {380});

		Sound(int[] sounds) {
			this.sounds = sounds;
		}

		private int[] sounds;

		public int[] getSounds() {
			return sounds;
		}

		public int getSound() {
			return sounds[Misc.getRandom(getSounds().length - 1)];
		}
	}

	public static void sendSound(Player player, int id) {
		if(player.soundsActive())
			player.getPacketSender().sendSound(id, 10, 0);
	}

	public static void sendSound(Player player, Sound sound) {
		sendSound(player, sound.getSound());
	}

	public static void sendGlobalSound(final Player player, final Sound sound) {
		sendGlobalSound(player, sound.getSound());
	}

	public static void sendGlobalSound(final Player player, final int sound) {
		for(Player p : GameWorld.getRegions().getLocalPlayers(player)) {
			if(p == null)
				continue;
			sendSound(p, sound);
		}
	}

	public static void sendGlobalSoundTask(final Player player, final Sound sound) {
		GameWorld.schedule(1, () -> sendGlobalSound(player, sound));
	}

	public static int getNpcAttackSounds(Mob mob) {
		String npcName = mob.getDefinition() != null ? mob.getName() : "";
		if (npcName.contains("bat")) {
			return 1;
		}
		if (npcName.contains("cow")) {
			return 4;
		}
		if (npcName.contains("imp"))
		{
			return 11;
		}
		if (npcName.contains("rat"))
		{
			return 17;
		}
		if (npcName.contains("duck"))
		{
			return 26;
		}
		if (npcName.contains("wolf") || npcName.contains("bear"))
		{
			return 28;
		}
		if (npcName.contains("dragon"))
		{
			return 47;
		}
		if (npcName.contains("ghost"))
		{
			return 57;
		}
		if (npcName.contains("goblin"))
		{
			return 88;
		}
		if (npcName.contains("skeleton") || npcName.contains("demon") || npcName.contains("ogre") || npcName.contains("giant") || npcName.contains("tz-") || npcName.contains("jad"))
		{
			return 48;
		}
		if (npcName.contains("zombie"))
		{
			return 1155;
		}
		if (npcName.contains("man") || npcName.contains("woman") || npcName.contains("monk"))
		{
			return 417;
		}
		return Misc.getRandom(6) > 3 ? 398 : 394;
	}

	public static int getNpcBlockSound(Mob mob) {
		NpcDefinition def = mob.getDefinition();
		String npcName = def == null ? "" : def.getName() == null ? "" : def.getName().toLowerCase();
		if (npcName.contains("bat")) {
			return 7;
		}
		if (npcName.contains("cow")) {
			return 5;
		}
		if (npcName.contains("imp")) {
			return 11;
		}
		if (npcName.contains("rat")) {
			return 16;
		}
		if (npcName.contains("duck")) {
			return 24;
		}
		if (npcName.contains("wolf") || npcName.contains("bear")) {
			return 34;
		}
		if (npcName.contains("dragon")) {
			return 45;
		}
		if (npcName.contains("ghost")) {
			return 53;
		}
		if (npcName.contains("goblin")) {
			return 87;
		}
		if (npcName.contains("skeleton") || npcName.contains("demon") || npcName.contains("ogre") || npcName.contains("giant") || npcName.contains("tz-") || npcName.contains("jad")) {
			return 1154;
		}
		if (npcName.contains("zombie")) {
			return 1151;
		}
		if (npcName.contains("man") && !npcName.contains("woman")) {
			return 816;
		}
		if (npcName.contains("monk")) {
			return 816;
		}

		if (!npcName.contains("man") && npcName.contains("woman")) {
			return 818;
		}
		return 791;
	}

	public static int getNpcDeathSounds(Mob mob) {
		NpcDefinition def = mob.getDefinition();
		String nameName = def == null ? "" : def.getName() == null ? "" : def.getName().toLowerCase();

		if (nameName.contains("bat")) {
			return 7;
		}
		if (nameName.contains("cow")) {
			return 3;
		}
		if (nameName.contains("imp")) {
			return 9;
		}
		if (nameName.contains("rat")) {
			return 15;
		}
		if (nameName.contains("duck")) {
			return 25;
		}
		if (nameName.contains("wolf") || nameName.contains("bear")) {
			return 35;
		}
		if (nameName.contains("dragon")) {
			return 44;
		}
		if (nameName.contains("ghost")) {
			return 60;
		}
		if (nameName.contains("goblin")) {
			return 125;
		}
		if (nameName.contains("skeleton") || nameName.contains("demon") || nameName.contains("ogre") || nameName.contains("giant") || nameName.contains("tz-") || nameName.contains("jad")) {
			return 70;
		}
		if (nameName.contains("zombie")) {
			return 1140;
		}
		return 70;
	}

	public static int getPlayerBlockSounds(Player player) {
		Item weapon = player.getEquipment().get(Slot.WEAPON);
		int weaponId = weapon == null ? -1 : weapon.getId();

		int blockSound = 511;

		if (weaponId == 2499 ||
				weaponId == 2501 ||
				weaponId == 2503 ||
				weaponId == 4746 ||
				weaponId == 4757 ||
				weaponId == 10330) {//Dragonhide sound
			blockSound = 24;
		}
		else if (weaponId == 10551 ||//Torso
				weaponId == 10438) {//3rd age
			blockSound = 32;//Weird sound
		}
		else if (weaponId == 10338 ||//3rd age
				weaponId == 7399 ||//Enchanted
				weaponId == 6107 ||//Ghostly
				weaponId == 4091 ||//Mystic
				weaponId == 4101 ||//Mystic
				weaponId == 4111 ||//Mystic
				weaponId == 1035 ||//Zamorak
				weaponId == 12971) {//Combat
			blockSound = 14;//Robe sound
		}
		else if (weaponId == 1101 ||//Chains
				weaponId == 1103||
				weaponId == 1105||
				weaponId == 1107||
				weaponId == 1109||
				weaponId == 1111||
				weaponId == 1113||
				weaponId == 1115|| //Plates
				weaponId == 1117||
				weaponId == 1119||
				weaponId == 1121||
				weaponId == 1123||
				weaponId == 1125||
				weaponId == 1127||
				weaponId == 4720|| //Barrows armour
				weaponId == 4728||
				weaponId == 4749||
				weaponId == 4712||
				weaponId == 11720||//Godwars armour
				weaponId == 11724||
				weaponId == 3140||//Dragon
				weaponId == 2615||//Fancy
				weaponId == 2653||
				weaponId == 2661||
				weaponId == 2669||
				weaponId == 2623||
				weaponId == 3841||
				weaponId == 1127) {//Metal armour sound
			blockSound = 511;
		}
		return blockSound;
	}

	public static int getPlayerAttackSound(Player player)	{
		Item weapon = player.getEquipment().get(Slot.WEAPON);
		String weaponName = weapon == null ? "" : weapon.getName().toLowerCase();
		int weaponId = weapon == null ? -1 : weapon.getId();
		if(weaponName.contains("bow"))
			return 370;

		if (weaponId == 772
				|| weaponId == 1379
				|| weaponId == 1381
				|| weaponId == 1383
				|| weaponId == 1385
				|| weaponId == 1387
				|| weaponId == 1389
				|| weaponId == 1391
				|| weaponId == 1393
				|| weaponId == 1395
				|| weaponId == 1397
				|| weaponId == 1399
				|| weaponId == 1401
				|| weaponId == 1403
				|| weaponId == 1405
				|| weaponId == 1407
				|| weaponId == 1409
				|| weaponId == 9100) { //Staff wack
			return 394;
		}
		if (weaponId == 839
				|| weaponId == 841
				|| weaponId == 843
				|| weaponId == 845
				|| weaponId == 847
				|| weaponId == 849
				|| weaponId == 851
				|| weaponId == 853
				|| weaponId == 855
				|| weaponId == 857
				|| weaponId == 859
				|| weaponId == 861
				|| weaponId == 4734
				|| weaponId == 2023 //RuneC'Bow
				|| weaponId == 4212
				|| weaponId == 4214
				|| weaponId == 4934
				|| weaponId == 9104
				|| weaponId == 9107) { //Bows/Crossbows
			return 370;
		}
		if (weaponId == 1363
				|| weaponId == 1365
				|| weaponId == 1367
				|| weaponId == 1369
				|| weaponId == 1371
				|| weaponId == 1373
				|| weaponId == 1375
				|| weaponId == 1377
				|| weaponId == 1349
				|| weaponId == 1351
				|| weaponId == 1353
				|| weaponId == 1355
				|| weaponId == 1357
				|| weaponId == 1359
				|| weaponId == 1361
				|| weaponId == 9109) { //BattleAxes/Axes
			return 399;
		}
		if (weaponId == 4718 || weaponId == 7808)
		{ //Dharok GreatAxe
			return 400;
		}
		if (weaponId == 6609
				|| weaponId == 1307
				|| weaponId == 1309
				|| weaponId == 1311
				|| weaponId == 1313
				|| weaponId == 1315
				|| weaponId == 1317
				|| weaponId == 1319) { //2h
			return 425;
		}
		if (weaponId == 1321
				|| weaponId == 1323
				|| weaponId == 1325
				|| weaponId == 1327
				|| weaponId == 1329
				|| weaponId == 1331
				|| weaponId == 1333
				|| weaponId == 4587) { //Scimitars
			return 396;
		}
		if (weaponName.contains("halberd"))
		{
			return 420;
		}
		if (weaponName.contains("long"))
		{
			return 396;
		}
		if (weaponName.contains("knife"))
		{
			return 368;
		}
		if (weaponName.contains("javelin"))
		{
			return 364;
		}

		if (weaponId == 1215 || weaponId == 5698) {
			return 401;
		}
		if (weaponId == 4755) {
			return 1059;
		}
		if (weaponId == 4153) {
			return 1079;
		}
		if (weaponId == -1) { // fists
			return 417;
		}
		if (weaponId == 2745 || weaponId == 2746 || weaponId == 2747 || weaponId == 2748) { // Godswords
			return 390;
		}
		if (weaponId == 4151 || weaponId == 15441 || weaponId == 15442 || weaponId == 15443 || weaponId == 15444 || weaponId == 21371) {
			return 1080;
		} else {
			return 398; //Daggers(this is enything that isn't added)
		}
	}

	public static int specialSounds(Player player) {
		Item weapon = player.getEquipment().get(Slot.WEAPON);
		int id = weapon == null ? -1 : weapon.getId();
		if (id == 4151) { //whip
			return 1081;
		}
		if (id == 5698) { //dds
			return 385;
		}
		if (id == 1434) {//Mace
			return 387;
		}
		if (id == 3204) { //halberd
			return 420;
		}
		if (id == 4153) { //gmaul
			return 1082;
		}
		if (id == 7158) { //d2h
			return 426;
		}
		if (id == 4587) { //dscim
			return 1305;
		}
		if (id == 1215) { //Dragon dag
			return 1082;
		}
		if (id == 1305) { //D Long
			return 390;
		}
		if (id == 861) { //MSB
			return 386;
		}
		if (id == 11235) { //DBow
			return 386;
		}
		if (id == 6739) { //D Axe
		}
		if (id == 1377) { //DBAxe
			return 389;
		}
		return -1;
	}
}
