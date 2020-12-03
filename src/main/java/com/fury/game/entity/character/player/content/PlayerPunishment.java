package com.fury.game.entity.character.player.content;

import com.fury.Main;
import com.fury.game.GameSettings;
import com.fury.game.system.files.Resources;
import com.fury.util.Misc;

import java.io.*;
import java.util.ArrayList;


public class PlayerPunishment {

	public static ArrayList<String> IPSBanned = new ArrayList<>();
	public static ArrayList<String> IPSMuted = new ArrayList<>();
	public static ArrayList<String> AccountsBanned = new ArrayList<>();
	public static ArrayList<String> AccountsMuted = new ArrayList<>();


	public static void init() {
		long startup = System.currentTimeMillis();
		initializeList(Resources.getSaveFile("ipbans"), IPSBanned);
		initializeList(Resources.getSaveFile("bans"), AccountsBanned);
		initializeList(Resources.getSaveFile("ipmutes"), IPSMuted);
		initializeList(Resources.getSaveFile("mutes"), AccountsMuted);
		if(GameSettings.DEBUG)
			System.out.println("Loaded player punishment " + (System.currentTimeMillis() - startup) + "ms");
	}

	public static void initializeList(String directory, ArrayList<String> list) {
		try {
			BufferedReader in = new BufferedReader(new FileReader(directory));
			String data;
			while ((data = in.readLine()) != null) {
				list.add(data);
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void addBannedIP(String IP) {
		if(!IPSBanned.contains(IP)) {
			addToFile(Resources.getSaveFile("ipbans"), IP);
			IPSBanned.add(IP);
		}
	}

	public static void addMutedIP(String IP) {
		if(!IPSMuted.contains(IP)) {
			addToFile(Resources.getSaveFile("ipmutes"), IP);
			IPSMuted.add(IP);
		}
	}

	public static void ban(String p) {
		p = Misc.formatName(p.toLowerCase());
		if(!AccountsBanned.contains(p)) {
			addToFile(Resources.getSaveFile("bans"), p);
			AccountsBanned.add(p);
		}
	}

	public static void mute(String p) {
		p = Misc.formatName(p.toLowerCase());
		if(!AccountsMuted.contains(p)) {
			addToFile(Resources.getSaveFile("mutes"), p);
			AccountsMuted.add(p);
		}
	}

	public static boolean banned(String player) {
		player = Misc.formatName(player.toLowerCase());
		return AccountsBanned.contains(player);
	}

	public static boolean muted(String player) {
		player = Misc.formatName(player.toLowerCase());
		return AccountsMuted.contains(player);
	}

	public static boolean IPBanned(String IP) {
		return IPSBanned.contains(IP);
	}

	public static boolean IPMuted(String IP) {
		return IPSMuted.contains(IP);
	}

	public static void unban(String player) {
		player = Misc.formatName(player.toLowerCase());
		deleteFromFile(Resources.getSaveFile("bans"), player);
		AccountsBanned.remove(player);
	}

	public static void unmute(String player) {
		player = Misc.formatName(player.toLowerCase());
		deleteFromFile(Resources.getSaveFile("mutes"), player);
		AccountsMuted.remove(player);
	}

	public static void reloadIPBans() {
		IPSBanned.clear();
		initializeList(Resources.getSaveFile("ipbans"), IPSBanned);
	}

	public static void reloadIPMutes() {
		IPSMuted.clear();
		initializeList(Resources.getSaveFile("ipmutes"), IPSMuted);
	}

	public static void deleteFromFile(String file, String name) {
		Main.getLoader().getEngine().submit(() -> {
			try {
				BufferedReader r = new BufferedReader(new FileReader(file));
				ArrayList<String> contents = new ArrayList<String>();
				while(true) {
					String line = r.readLine();
					if(line == null) {
						break;
					} else {
						line = line.trim();
					}
					if(!line.equalsIgnoreCase(name)) {
						contents.add(line);
					}
				}
				r.close();
				BufferedWriter w = new BufferedWriter(new FileWriter(file));
				for(String line : contents) {
					w.write(line, 0, line.length());
					w.newLine();
				}
				w.flush();
				w.close();
			} catch (Exception e) {}
		});
	}

	public static void addToFile(String file, String data) {
		Main.getLoader().getEngine().submit(() -> {
			try {
				BufferedWriter out = new BufferedWriter(new FileWriter(file, true));
				try {
					out.newLine();
					out.write(data);
				} finally {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

}
