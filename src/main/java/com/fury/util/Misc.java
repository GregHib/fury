package com.fury.util;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.GameSettings;
import com.fury.core.model.item.Item;
import com.fury.game.system.files.Resources;
import com.fury.game.world.World;
import com.fury.game.world.map.Position;
import com.fury.game.world.map.region.RegionMap;
import io.netty.buffer.ByteBuf;

import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.zip.GZIPInputStream;

public class Misc {

    /**
     * Random instance, used to generate pseudo-random primitive types.
     */
    public static final Random RANDOM = new Random(System.currentTimeMillis());

    private static ZonedDateTime zonedDateTime;
    public static final int HALF_A_DAY_IN_MILLIS = 43200000;

    public static String getCurrentServerTime() {
        zonedDateTime = ZonedDateTime.now();
        int hour = zonedDateTime.getHour();
        String hourPrefix = hour < 10 ? "0" + hour + "" : "" + hour + "";
        int minute = zonedDateTime.getMinute();
        String minutePrefix = minute < 10 ? "0" + minute + "" : "" + minute + "";
        return "" + hourPrefix + ":" + minutePrefix + "";
    }

    public static String getTimePlayed(long totalPlayTime) {
        final int sec = (int) (totalPlayTime / 1000), h = sec / 3600, m = sec / 60 % 60;
        return (h < 10 ? "0" + h : h) + "h " + (m < 10 ? "0" + m : m) + "m";
    }

    public static int getMinutesPlayed(Player p) {
        long totalPlayTime = p.getTotalPlayTime() + (p.getTimers().getLogin().elapsed());
        int sec = (int) (totalPlayTime / 1000), h = sec / 3600, m = sec / 60 % 60;
        for (int i = 0; i < h; i++)
            m += 60;
        return m;
    }

    public static String getHoursPlayed(long totalPlayTime) {
        final int sec = (int) (totalPlayTime / 1000), h = sec / 3600;
        return (h < 10 ? "0" + h : h) + "h";
    }

    public static int getMinutesPassed(long t) {
        int seconds = (int) ((t / 1000) % 60);
        int minutes = (int) (((t - seconds) / 1000) / 60);
        return minutes;
    }

    public static Item[] concat(Item[] a, Item[] b) {
        int aLen = a.length;
        int bLen = b.length;
        Item[] c = new Item[aLen + bLen];
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);
        return c;
    }

    public static int[] concat(int[]... a) {
        int[] b = a[0];
        for(int i = 1; i < a.length; i++)
            b = concat(b, a[i]);
        return b;
    }

    public static int[] concat(int[] a, int[] b) {
        int aLen = a.length;
        int bLen = b.length;
        int[] c = new int[aLen + bLen];
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);
        return c;
    }

    public static Player getCloseRandomPlayer(List<Player> plrs) {
        int index = Misc.getRandom(plrs.size() - 1);
        if (index > 0)
            return plrs.get(index);
        return null;
    }

    public static byte directionDeltaX[] = new byte[]{0, 1, 1, 1, 0, -1, -1, -1};
    public static byte directionDeltaY[] = new byte[]{1, 1, 0, -1, -1, -1, 0, 1};
    public static byte xlateDirectionToClient[] = new byte[]{1, 2, 4, 7, 6, 5, 3, 0};

    public static int random(int maxValue) {
        if (maxValue <= 0)
            return 0;
        return RANDOM.nextInt(maxValue);
    }

    public static int getRandom(int range) {
        return (int) (Math.random() * (range + 1));
    }

    public static int getRandomGaussianDistribution(int deviation, int mean) {
        return (int) (RANDOM.nextGaussian() * deviation + mean);
    }

    public static int direction(int srcX, int srcY, int x, int y) {
        double dx = (double) x - srcX, dy = (double) y - srcY;
        double angle = Math.atan(dy / dx);
        angle = Math.toDegrees(angle);
        if (Double.isNaN(angle))
            return -1;
        if (Math.signum(dx) < 0)
            angle += 180.0;
        return (int) ((((90 - angle) / 22.5) + 16) % 16);
        /*int changeX = worldX - srcX; int changeY = worldY - srcY;
		for (int j = 0; j < directionDeltaX.length; j++) {
			if (changeX == directionDeltaX[j] &&
				changeY == directionDeltaY[j])
				return j;

		}
		return -1;*/
    }

    public static String uppercaseFirst(String str) {
        str = str.toLowerCase();
        if (str.length() > 1) {
            str = str.substring(0, 1).toUpperCase() + str.substring(1);
        } else {
            return str.toUpperCase();
        }
        return str;
    }

    public static String format(int num) {
        return NumberFormat.getInstance().format(num);
    }

    public static String formatText(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (i == 0) {
                s = String.format("%s%s", Character.toUpperCase(s.charAt(0)),
                        s.substring(1));
            }
            if (!Character.isLetterOrDigit(s.charAt(i))) {
                if (i + 1 < s.length()) {
                    s = String.format("%s%s%s", s.subSequence(0, i + 1),
                            Character.toUpperCase(s.charAt(i + 1)),
                            s.substring(i + 2));
                }
            }
        }
        return s.replace("_", " ");
    }

    public static final String formatAmount(long amount) {
        String format = "Too high!";
        if (amount >= 0 && amount < 100000) {
            format = String.valueOf(amount);
        } else if (amount >= 100000 && amount < 1000000) {
            format = amount / 1000 + "K";
        } else if (amount >= 1000000 && amount < 1000000000L) {
            format = amount / 1000000 + "M";
        } else if (amount >= 1000000000L && amount < 1000000000000L) {
            format = amount / 1000000000 + "B";
        } else if (amount >= 10000000000000L && amount < 10000000000000000L) {
            format = amount / 1000000000000L + "T";
        } else if (amount >= 10000000000000000L && amount < 1000000000000000000L) {
            format = amount / 1000000000000000L + "QD";
        } else if (amount >= 1000000000000000000L && amount < Long.MAX_VALUE) {
            format = amount / 1000000000000000000L + "QT";
        }
        return format;
    }

    public static String formatName(String str) {
        String str1 = Misc.uppercaseFirst(str);
        str1.replaceAll("_", " ");
        return str1;
    }

    public static String insertCommasToNumber(long number) {
        return insertCommasToNumber("" + number);
    }

    public static String insertCommasToNumber(int number) {
        return insertCommasToNumber("" + number);
    }

    public static String insertCommasToNumber(String number) {
        return number.length() < 4 ? number : insertCommasToNumber(number.substring(0, number.length() - 3)) + "," + number.substring(number.length() - 3, number.length());
    }

    public static String insertCommas(long number) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(number);
    }

    public static String insertCommas(int number) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(number);
    }

    private static char decodeBuf[] = new char[4096];

    public static String textUnpack(byte packedData[], int size) {
        int idx = 0, highNibble = -1;
        for (int i = 0; i < size * 2; i++) {
            int val = packedData[i / 2] >> (4 - 4 * (i % 2)) & 0xf;
            if (highNibble == -1) {
                if (val < 13)
                    decodeBuf[idx++] = xlateTable[val];
                else
                    highNibble = val;
            } else {
                decodeBuf[idx++] = xlateTable[((highNibble << 4) + val) - 195];
                highNibble = -1;
            }
        }

        return new String(decodeBuf, 0, idx);
    }

    public static char xlateTable[] = {' ', 'e', 't', 'a', 'o', 'i', 'h', 'n',
            's', 'r', 'd', 'l', 'u', 'm', 'w', 'c', 'y', 'f', 'g', 'p', 'b',
            'v', 'k', 'x', 'j', 'q', 'z', '0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', ' ', '!', '?', '.', ',', ':', ';', '(', ')', '-',
            '&', '*', '\\', '\'', '@', '#', '+', '=', '\243', '$', '%', '"',
            '[', ']', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
            'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
            'Y', 'Z'};

    public static String anOrA(String s) {
        s = s.toLowerCase();
        if (s.endsWith("s") || s.equalsIgnoreCase("bread") || s.equalsIgnoreCase("soft clay") || s.equalsIgnoreCase("cheese") || s.equalsIgnoreCase("ball of wool") || s.equalsIgnoreCase("spice") || s.equalsIgnoreCase("coal"))
            return "some";
        if (s.startsWith("a") || s.startsWith("e") || s.startsWith("i") || s.startsWith("o") || s.startsWith("u"))
            return "an";
        return "a";
    }

    @SuppressWarnings("rawtypes")
    public static Class[] getClasses(String packageName) throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        //String path = packageName.replace('.', '/');
        File path = new File(Resources.getDirectory("dialogues"));
        Enumeration<URL> resources = ClassLoader.getSystemResources(path.toString());
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            System.out.println(resource);
        }
        List<File> dirs = new ArrayList<File>();
        Files.walk(Paths.get(path.getPath()))
                .filter(Files::isRegularFile)
                .forEach(t -> dirs.add(t.toFile()));
        System.out.println(Arrays.toString(dirs.toArray()));
		/*System.out.println(path.getPath());
		System.out.println(path.isDirectory());
		Enumeration<URL> resources = classLoader.getResources(path.toString());
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			System.out.println(resource);
			dirs.add(new File(resource.getFile()));
		}*/
        ArrayList<Class> classes = new ArrayList<Class>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes.toArray(new Class[classes.size()]);
    }

    @SuppressWarnings("rawtypes")
    private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }

    public static int randomMinusOne(int length) {
        return getRandom(length - 1);
    }

    public static String removeSpaces(String s) {
        return s.replace(" ", "");
    }

    public static int getMinutesElapsed(int minute, int hour, int day, int year) {
        Calendar i = Calendar.getInstance();

        if (i.get(1) == year) {
            if (i.get(6) == day) {
                if (hour == i.get(11)) {
                    return i.get(12) - minute;
                }
                return (i.get(11) - hour) * 60 + (59 - i.get(12));
            }

            int ela = (i.get(6) - day) * 24 * 60 * 60;
            return ela > 2147483647 ? 2147483647 : ela;
        }

        int ela = getElapsed(day, year) * 24 * 60 * 60;

        return ela > 2147483647 ? 2147483647 : ela;
    }

    public static int getDayOfYear() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int days = 0;
        int[] daysOfTheMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if ((year % 4 == 0) && (year % 100 != 0) || (year % 400 == 0)) {
            daysOfTheMonth[1] = 29;
        }
        days += c.get(Calendar.DAY_OF_MONTH);
        for (int i = 0; i < daysOfTheMonth.length; i++) {
            if (i < month) {
                days += daysOfTheMonth[i];
            }
        }
        return days;
    }

    public static int getYear() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR);
    }

    public static int getElapsed(int day, int year) {
        if (year < 2013) {
            return 0;
        }

        int elapsed = 0;
        int currentYear = Misc.getYear();
        int currentDay = Misc.getDayOfYear();

        if (currentYear == year) {
            elapsed = currentDay - day;
        } else {
            elapsed = currentDay;

            for (int i = 1; i < 5; i++) {
                if (currentYear - i == year) {
                    elapsed += 365 - day;
                    break;
                } else {
                    elapsed += 365;
                }
            }
        }

        return elapsed;
    }

    public static boolean isWeekend() {
        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        return !(day >= 2 && day <= 5);
    }

    public static boolean isStartOfDay() {
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_WEEK);
        if (day != GameSettings.LOYALTY_RESET_DAY)
            return false;

        int hour = cal.get(Calendar.HOUR);
        if (hour != GameSettings.LOYALTY_RESET_HOUR)
            return false;

        int min = cal.get(Calendar.MINUTE);
        return min == GameSettings.LOYALTY_RESET_MINUTE;
    }

    /**
     * Returns a pseudo-random {@code int} value between inclusive
     * <code>min</code> and exclusive <code>max</code>.
     * <p>
     * <br>
     * <br>
     * This method is thread-safe. </br>
     *
     * @param min The minimum inclusive number.
     * @param max The maximum exclusive number.
     * @return The pseudo-random {@code int}.
     * @throws IllegalArgumentException If the specified range is less <tt>0</tt>
     */
    public static int exclusiveRandom(int min, int max) {
        if (max <= min) {
            max = min + 1;
        }
        return RANDOM.nextInt((max - min)) + min;
    }

    /**
     * Returns a pseudo-random {@code int} value between inclusive <tt>0</tt>
     * and exclusive <code>range</code>.
     * <p>
     * <br>
     * <br>
     * This method is thread-safe. </br>
     *
     * @param range The exclusive range.
     * @return The pseudo-random {@code int}.
     * @throws IllegalArgumentException If the specified range is less <tt>0</tt>
     */
    public static int exclusiveRandom(int range) {
        return exclusiveRandom(0, range);
    }

    /**
     * Returns a pseudo-random {@code int} value between inclusive
     * <code>min</code> and inclusive <code>max</code>.
     *
     * @param min The minimum inclusive number.
     * @param max The maximum inclusive number.
     * @return The pseudo-random {@code int}.
     * @throws IllegalArgumentException If {@code max - min + 1} is less than <tt>0</tt>.
     * @see {@link #exclusiveRandom(int)}.
     */
    public static int inclusiveRandom(int min, int max) {
        if (max < min) {
            max = min + 1;
        }
        return exclusiveRandom((max - min) + 1) + min;
    }

    /**
     * Returns a pseudo-random {@code int} value between inclusive <tt>0</tt>
     * and inclusive <code>range</code>.
     *
     * @param range The maximum inclusive number.
     * @return The pseudo-random {@code int}.
     * @throws IllegalArgumentException If {@code max - min + 1} is less than <tt>0</tt>.
     * @see {@link #exclusiveRandom(int)}.
     */
    public static int inclusiveRandom(int range) {
        return inclusiveRandom(0, range);
    }

    public static byte[] readFile(File s) {
        try {
            FileInputStream fis = new FileInputStream(s);
            FileChannel fc = fis.getChannel();
            ByteBuffer buf = ByteBuffer.allocate((int) fc.size());
            fc.read(buf);
            buf.flip();
            fis.close();
            return buf.array();
        } catch (Exception e) {
            System.out.println("FILE : " + s.getName() + " missing.");
            return null;
        }
    }

    private static DataInputStream dis;

    private static byte[] gzipInputBuffer = new byte[125000];

    private static GZIPInputStream gzip;

    private static int bufferlength = 0;

    private static byte[] buffer;

    public static byte[] getBuffer(File f) throws Exception {
        if (!f.exists()) return null;
        buffer = new byte[(int) f.length()];
        dis = new DataInputStream(new FileInputStream(f));
        dis.readFully(buffer);
        dis.close();
        gzip = new GZIPInputStream(new ByteArrayInputStream(buffer));

        bufferlength = 0;
        do {
            if (bufferlength == gzipInputBuffer.length) {
                System.out.println("Error inflating data.\nGZIP buffer overflow.");
                break;
            }
            int readByte = gzip.read(gzipInputBuffer, bufferlength, gzipInputBuffer.length - bufferlength);
            if (readByte == -1)
                break;
            bufferlength += readByte;
        } while (true);
        byte[] inflated = new byte[bufferlength];
        System.arraycopy(gzipInputBuffer, 0, inflated, 0, bufferlength);
        buffer = inflated;
        if (buffer.length < 10)
            return null;
        return buffer;
    }

    private static final long INIT_MILLIS = System.currentTimeMillis();
    private static final long INIT_NANOS = System.nanoTime();

    private static long millisSinceClassInit() {
        return (System.nanoTime() - INIT_NANOS) / 1000000;
    }

    public static long currentTimeMillis() {
        return INIT_MILLIS + millisSinceClassInit();
    }

    public static final int[] ROTATION_DIR_X = {-1, 0, 1, 0};

    public static final int[] ROTATION_DIR_Y = {0, 1, 0, -1};

    public static int getTimeLeft(long start, int timeAmount, TimeUnit timeUnit) {
        start -= timeUnit.toMillis(timeAmount);
        long elapsed = System.currentTimeMillis() - start;
        int toReturn = timeUnit == TimeUnit.SECONDS ? (int) ((elapsed / 1000) % 60) - timeAmount : (int) ((elapsed / 1000) / 60) - timeAmount;
        if (toReturn <= 0)
            toReturn = 1;
        return timeAmount - toReturn;
    }

    /**
     * Converts an array of bytes to an integer.
     *
     * @param data the array of bytes.
     * @return the newly constructed integer.
     */
    public static int hexToInt(byte[] data) {
        int value = 0;
        int n = 1000;
        for (int i = 0; i < data.length; i++) {
            int num = (data[i] & 0xFF) * n;
            value += num;
            if (n > 1) {
                n = n / 1000;
            }
        }
        return value;
    }

    public static Position delta(Position a, Position b) {
        return new Position(b.getX() - a.getX(), b.getY() - a.getY());
    }

    /**
     * Picks a random element out of any array type.
     *
     * @param array the array to pick the element from.
     * @return the element chosen.
     */
    public static <T> T randomElement(T[] array) {
        return array[(int) (RANDOM.nextDouble() * array.length)];
    }

    /**
     * Picks a random element out of any list type.
     *
     * @param list the list to pick the element from.
     * @return the element chosen.
     */
    public static <T> T randomElement(List<T> list) {
        return list.get((int) (RANDOM.nextDouble() * list.size()));
    }

    /**
     * Reads string from a data input stream.
     *
     * @param buffer The input stream to read string from.
     * @return The String value.
     */
    public static String readString(ByteBuf buffer) {
        StringBuilder builder = null;
        try {
            byte data;
            builder = new StringBuilder();
            while ((data = buffer.readByte()) != 10) {
                builder.append((char) data);
            }
        } catch (IndexOutOfBoundsException e) {

        }
        return builder.toString();
    }

    private static final String[] BLOCKED_WORDS = new String[]{
            ".com", ".net", ".org", "<img", "@cr", "<img=", "<col=", "<shad="
    };


    public static boolean blockedWord(String string) {
        for (String s : BLOCKED_WORDS) {
            if (string.contains(s)) {
                return true;
            }
        }
        return false;
    }

    public static boolean colides(int x1, int y1, int size1, int x2, int y2, int size2) {
        int distanceX = x1 - x2;
        int distanceY = y1 - y2;
        return distanceX < size2 && distanceX > -size1 && distanceY < size2 && distanceY > -size1;
    }

    public static boolean colides(int x1, int y1, int sizeX1, int sizeY1, int x2, int y2, int sizeX2, int sizeY2) {
        int distanceX = x1 - x2;
        int distanceY = y1 - y2;
        return distanceX < sizeX2 && distanceX > -sizeX1 && distanceY < sizeY2 && distanceY > -sizeY1;
    }

    public static boolean isOnRange(int x1, int y1, int size1, int x2, int y2, int size2, int maxDistance) {
        int distanceX = x1 - x2;
        int distanceY = y1 - y2;
        return !(distanceX > size2 + maxDistance || distanceX < -size1 - maxDistance || distanceY > size2 + maxDistance
                || distanceY < -size1 - maxDistance);
    }

    public static boolean isOnRange(int x1, int y1, int sizeX, int sizeY, int x2, int y2, int size2, int maxDistance) {
        int distanceX = x1 - x2;
        int distanceY = y1 - y2;
        return !(distanceX > size2 + maxDistance || distanceX < -sizeX - maxDistance || distanceY > size2 + maxDistance
                || distanceY < -sizeY - maxDistance);
    }

    public static boolean isOnRange(int x1, int y1, int sizeX1, int sizeY1, int x2, int y2, int sizeX2, int sizeY2, int maxDistance) {
        int distanceX = x1 - x2;
        int distanceY = y1 - y2;
        return !(distanceX > sizeX2 + maxDistance || distanceX < -sizeX1 - maxDistance || distanceY > sizeY2 + maxDistance
                || distanceY < -sizeY1 - maxDistance);
    }

    public static final byte[] DIRECTION_DELTA_X = new byte[]{-1, 0, 1, -1, 1, -1, 0, 1};
    public static final byte[] DIRECTION_DELTA_Y = new byte[]{1, 1, 1, 0, 0, -1, -1, -1};

    public static final int getDistance(Position t1, Position t2) {
        return getDistance(t1.getX(), t1.getY(), t2.getX(), t2.getY());
    }
    public static final int getDistance(int coordX1, int coordY1, int coordX2, int coordY2) {
        int deltaX = coordX2 - coordX1;
        int deltaY = coordY2 - coordY1;
        return ((int) Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2)));
    }

    public static final int random(int min, int max) {
        final int n = Math.abs(max - min);
        return Math.min(min, max) + (n == 0 ? 0 : random(n));
    }

    public static final double random(double min, double max) {
        final double n = Math.abs(max - min);
        return Math.min(min, max) + (n == 0 ? 0 : random((int) n));
    }

    public static String formatPlayerNameForDisplay(String name) {
        if (name == null)
            return "";
        name = name.replaceAll("_", " ");
        name = name.toLowerCase();
        StringBuilder newName = new StringBuilder();
        boolean wasSpace = true;
        for (int i = 0; i < name.length(); i++) {
            if (wasSpace) {
                newName.append(("" + name.charAt(i)).toUpperCase());
                wasSpace = false;
            } else {
                newName.append(name.charAt(i));
            }
            if (name.charAt(i) == ' ') {
                wasSpace = true;
            }
        }
        return newName.toString();
    }

    /*public static final int getAngle(int vertexX, int vertexY) {
        return ((int) (Math.atan2(-vertexX, -vertexY) * 2607.5945876176133)) & 0x3fff;
    }*/
    public static Position getFreeTile(Position center, int distance) {
        Position tile = center;
        for (int i = 0; i < 10; i++) {
            tile = center.copyPosition().add(Misc.random(distance), Misc.random(distance));
            if ((World.getMask(tile.getX(), tile.getY(), tile.getZ()) & 0x1280120) == 0)
                return tile;
        }
        return center;
    }

    public static boolean inCircle(Position location, Position center, int radius) {
        return getDistance(center, location) < radius;
    }

    private static final byte[][] ANGLE_DIRECTION_DELTA = {{0, -1}, {-1, -1}, {-1, 0}, {-1, 1}, {0, 1},
            {1, 1}, {1, 0}, {1, -1}};

    public static byte[] getDirection(int angle) {
        int v = angle >> 11;
        return ANGLE_DIRECTION_DELTA[v];
    }

    public static void displayClipping(RegionMap map) {
        for (int y = 64; y >= 0; y--) {
            for (int x = 0; x < 64; x++) {
                int id = map.getMask(x, y, 0);
                if (id <= 0) System.out.print(" ");
                if (id > 0) System.out.print(1);

                System.out.print(" ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void displayClipping(int[][] roomClipping) {
        for (int y = roomClipping.length - 1; y >= 0; y--) {
            for (int x = 0; x < roomClipping.length; x++) {
                int id = -1;
                if (roomClipping[x][y] != -1) id = roomClipping[x][y];
                if (id <= 0) System.out.print(" ");
                if (id > 0) System.out.print(1);

                System.out.print(" ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public static void displayClipping(int localX, int localY, int[][] roomClipping) {
        for (int y = roomClipping.length - 1; y >= 0; y--) {
            for (int x = 0; x < roomClipping.length; x++) {
                if (localX == x && localY == y) {
                    System.out.println("P ");
                    continue;
                }
                int id = -1;
                if (roomClipping[x][y] != -1) id = roomClipping[x][y];
                if (id <= 0) System.out.print(" ");
                if (id > 0) System.out.print(1);

                System.out.print(" ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static int getHoursPassed(long since) {
        long current = System.currentTimeMillis();
        double hours = (since - current) / 1000 / 60 / 60;
        return (int) hours;
    }

    public static boolean hasDayPassedSince(long time) {
        return getHoursPassed(time) >= 24;
    }

    public static long stringToAmount(String value) {
        return Long.parseLong(value.trim().toLowerCase().replaceAll("k", "000").replaceAll("m", "000000").replaceAll("b", "000000000").replaceAll("t", "000000000000"));
    }

    public final static String[] getValues(Matcher matcher) {
        int matches = matcher.groupCount() + 1;
        String[] values = new String[matches];
        for (int i = 0; i < matches; i++)
            values[i] = matcher.group(i);
        return values;
    }

    public static boolean playerExists(String name) {
        name = formatName(name.toLowerCase());
        return new File(Resources.getSaveDirectory("characters") + name + ".json").exists();
    }
}
