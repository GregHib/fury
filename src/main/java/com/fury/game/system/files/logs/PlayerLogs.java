package com.fury.game.system.files.logs;

import com.fury.Main;
import com.fury.game.system.files.Resources;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PlayerLogs {

	/**
	 * Fetches system time and formats it appropriately
	 * 
	 * @return Formatted time
	 */
	private static String getTime() {
		Date getDate = new Date();
		String timeFormat = "M/d/yy hh:mma";
		SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
		return "[" + sdf.format(getDate) + "]\t";
	}

	/**
	 * Writes formatted string to text file
	 * 
	 * @param file
	 *            - File to write data to
	 * @param writable
	 *            - Data to written
	 * @throws IOException
	 */
	public static void log(String file, String writable) {
		Main.getLoader().getEngine().submit(() -> {
			try {
				FileWriter fw = new FileWriter(Resources.getSaveDirectory("oldlogs") + file + ".txt", true);
				if(fw != null) {
					fw.write(getTime() + writable + "\t");
					fw.write(System.lineSeparator());
					fw.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public static void eraseFile(String name) {
		try {
			File file = new File(Resources.getSaveDirectory("oldlogs") + name + ".txt");
			file.delete();
			log(name,
					"\t <----------------- File automatically cleaned ----------------->");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
