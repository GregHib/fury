package com.fury.util;

import com.fury.core.model.node.entity.Entity;
import com.fury.game.world.World;
import com.fury.game.world.map.Position;

import java.io.*;
import java.math.BigInteger;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Greg on 14/11/2016.
 */
public class Utils {
    private static final Object ALGORITHM_LOCK = new Object();

    private static long timeCorrection;
    private static long lastTimeUpdate;

    private static final long INIT_MILLIS = System.currentTimeMillis();
    private static final long INIT_NANOS = System.nanoTime();

    private static Random random;

    public static final int getRandom(int maxValue) {
        return (int) (Math.random() * (maxValue + 1));
    }

    public static final Random getRandomGenerator() {
        return random == null ? random = new Random() : random;
    }

    private static SecureRandom SECURE_RANDOM;

    static {
        try {
            // native is too slow
            SECURE_RANDOM = SecureRandom.getInstance("SHA1PRNG", "SUN");
        } catch (Throwable e) {
            //Logger.handle(e);
        }
    }

    private static long millisSinceClassInit() {
        return (System.nanoTime() - INIT_NANOS) / 1000000;
    }

    public static synchronized long currentTimeMillis() {
        long l = System.currentTimeMillis();
        if (l < lastTimeUpdate)
            timeCorrection += lastTimeUpdate - l;
        lastTimeUpdate = l;
        return l + timeCorrection;
    }
    /*
     * private static long timeCorrection; private static long lastTimeUpdate;
     * 
     * public static synchronized long currentTimeMillis() { long l =
     * System.currentTimeMillis(); if (l < lastTimeUpdate) timeCorrection +=
     * lastTimeUpdate - l; lastTimeUpdate = l; return l + timeCorrection; }
     */

    private static final DecimalFormat dFormatter = new DecimalFormat("#,###,###,###");

    public static String getFormattedNumber(int amount) {
        return dFormatter.format(amount);
    }

    public static int getMapArchiveId(int regionX, int regionY) {
        return regionX | regionY << 7;
    }

    public static String formatTime(long time) {
        long seconds = time / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        seconds = seconds % 60;
        minutes = minutes % 60;
        hours = hours % 24;
        StringBuilder string = new StringBuilder();
        string.append(hours > 9 ? hours : ("0" + hours));
        string.append(":" + (minutes > 9 ? minutes : ("0" + minutes)));
        string.append(":" + (seconds > 9 ? seconds : ("0" + seconds)));
        return string.toString();
    }

    public static void shuffle(int[] array) {
        int count = array.length;
        for (int i = count; i > 1; i--)
            swap(array, i - 1, SECURE_RANDOM.nextInt(i));
    }

    private static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    public static String getFormattedNumber(double amount, char seperator) {
        String str = new DecimalFormat("#,###,###").format(amount);
        char[] rebuff = new char[str.length()];
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c >= '0' && c <= '9')
                rebuff[i] = c;
            else
                rebuff[i] = seperator;
        }
        return new String(rebuff);
    }

    public static byte[] cryptRSA(byte[] data, BigInteger exponent, BigInteger modulus) {
        return new BigInteger(data).modPow(exponent, modulus).toByteArray();
    }

    public static final byte[] encryptUsingMD5(byte[] buffer) {
        // prevents concurrency problems with the algorithm
        synchronized (ALGORITHM_LOCK) {
            try {
                MessageDigest algorithm = MessageDigest.getInstance("MD5");
                algorithm.update(buffer);
                byte[] digest = algorithm.digest();
                algorithm.reset();
                return digest;
            } catch (Throwable e) {
                //Logger.handle(e);
            }
            return null;
        }
    }

    public static boolean inCircle(Position location, Position center, int radius) {
        return getDistance(center, location) < radius;
    }

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;
        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }

    @SuppressWarnings({"rawtypes"})
    public static Class[] getClasses(String packageName) throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile().replaceAll("%20", " ")));
        }
        ArrayList<Class> classes = new ArrayList<>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes.toArray(new Class[classes.size()]);
    }

    public static File getFile(String packageName) {
        String path = packageName.replace('.', '/');
        return new File(System.getProperty("user.dir") + "/src/" + path);
    }

    @SuppressWarnings("rawtypes")
    private static List<Class> findClasses(File directory, String packageName) {
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
                try {
                    classes.add(Class
                            .forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
                } catch (Throwable e) {

                }
            }
        }
        return classes;
    }

    public static final int getDistance(Position t1, Position t2) {
        return getDistance(t1.getX(), t1.getY(), t2.getX(), t2.getY());
    }

    public static final int getDistance(int coordX1, int coordY1, int coordX2, int coordY2) {
        int deltaX = coordX2 - coordX1;
        int deltaY = coordY2 - coordY1;
        return ((int) Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2)));
    }

    public static Position getFreeTile(Position center, int distance) {
        Position tile = center;
        for (int i = 0; i < 10; i++) {
            tile = new Position(center, distance);
            if (World.isTileFree(tile.getX(), tile.getY(), tile.getZ(), 1))
                return tile;
        }
        return center;
    }

    public static final int getFaceDirection(int xOffset, int yOffset) {
        return ((int) (Math.atan2(-xOffset, -yOffset) * 2607.5945876176133)) & 0x3fff;
    }

    public static final int getMoveDirection(int xOffset, int yOffset) {
        if (xOffset < 0) {
            if (yOffset < 0)
                return 5;
            else if (yOffset > 0)
                return 0;
            else
                return 3;
        } else if (xOffset > 0) {
            if (yOffset < 0)
                return 7;
            else if (yOffset > 0)
                return 2;
            else
                return 4;
        } else {
            if (yOffset < 0)
                return 6;
            else if (yOffset > 0)
                return 1;
            else
                return -1;
        }
    }

    public static final String[] NUMBERS = {"one", "two", "three", "four", "five"};

    public static final byte[] DIRECTION_DELTA_X = new byte[]{-1, 0, 1, -1, 1, -1, 0, 1};
    public static final byte[] DIRECTION_DELTA_Y = new byte[]{1, 1, 1, 0, 0, -1, -1, -1};

    private static final byte[][] ANGLE_DIRECTION_DELTA = {{0, -1}, {-1, -1}, {-1, 0}, {-1, 1}, {0, 1},
            {1, 1}, {1, 0}, {1, -1}};

    public static int getNpcMoveDirection(int dd) {
        return getNpcMoveDirection(DIRECTION_DELTA_X[dd], DIRECTION_DELTA_Y[dd]);
    }

    public static byte[] getDirection(int angle) {
        int v = angle >> 11;
        return ANGLE_DIRECTION_DELTA[v];
    }

    public static final int getAngle(int xOffset, int yOffset) {
        return ((int) (Math.atan2(-xOffset, -yOffset) * 2607.5945876176133)) & 0x3fff;
    }

    public static int getNpcMoveDirection(int dx, int dy) {
        if (dx == 0 && dy > 0)
            return 0;
        if (dx > 0 && dy > 0)
            return 1;
        if (dx > 0 && dy == 0)
            return 2;
        if (dx > 0 && dy < 0)
            return 3;
        if (dx == 0 && dy < 0)
            return 4;
        if (dx < 0 && dy < 0)
            return 5;
        if (dx < 0 && dy == 0)
            return 6;
        if (dx < 0 && dy > 0)
            return 7;
        return -1;
    }

    public static final int[][] getCoordOffsetsNear(int size) {
        int[] xs = new int[4 + (4 * size)];
        int[] xy = new int[xs.length];
        xs[0] = -size;
        xy[0] = 1;
        xs[1] = 1;
        xy[1] = 1;
        xs[2] = -size;
        xy[2] = -size;
        xs[3] = 1;
        xy[2] = -size;
        for (int fakeSize = size; fakeSize > 0; fakeSize--) {
            xs[(4 + ((size - fakeSize) * 4))] = -fakeSize + 1;
            xy[(4 + ((size - fakeSize) * 4))] = 1;
            xs[(4 + ((size - fakeSize) * 4)) + 1] = -size;
            xy[(4 + ((size - fakeSize) * 4)) + 1] = -fakeSize + 1;
            xs[(4 + ((size - fakeSize) * 4)) + 2] = 1;
            xy[(4 + ((size - fakeSize) * 4)) + 2] = -fakeSize + 1;
            xs[(4 + ((size - fakeSize) * 4)) + 3] = -fakeSize + 1;
            xy[(4 + ((size - fakeSize) * 4)) + 3] = -size;
        }
        return new int[][]{xs, xy};
    }

    public static final int random(int min, int max) {
        final int n = Math.abs(max - min);
        return Math.min(min, max) + (n == 0 ? 0 : random(n));
    }

    public static final double random(double min, double max) {
        final double n = Math.abs(max - min);
        return Math.min(min, max) + (n == 0 ? 0 : random((int) n));
    }

    public static final int next(int max, int min) {
        return min + (int) (SECURE_RANDOM.nextDouble() * ((max - min) + 1));
    }

    public static final int random(int maxValue) {
        if (maxValue <= 0)
            return 0;
        return SECURE_RANDOM.nextInt(maxValue);
    }

    public static final double random(double maxValue) {
        return SECURE_RANDOM.nextDouble() * maxValue;
    }

    public static final double randomDouble() {
        return SECURE_RANDOM.nextDouble();
    }

    public static final double randomDouble(double maxValue) {
        return SECURE_RANDOM.nextDouble() * (maxValue + 1);
    }

    public static final char[] VALID_CHARS = {'_', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9'};

    public static boolean invalidAccountName(String name) {
        return name.length() < 2 || name.length() > 12 || name.startsWith("_") || name.endsWith("_")
                || name.contains("__") || containsInvalidCharacter(name);
    }

    public static boolean containsInvalidCharacter(char c) {
        for (char vc : VALID_CHARS) {
            if (vc == c)
                return false;
        }
        return true;
    }

    public static boolean containsInvalidCharacter(String name) {
        for (char c : name.toCharArray()) {
            if (containsInvalidCharacter(c))
                return true;
        }
        return false;
    }

    public static final long stringToLong(String s) {
        long l = 0L;
        for (int i = 0; i < s.length() && i < 12; i++) {
            char c = s.charAt(i);
            l *= 37L;
            if (c >= 'A' && c <= 'Z')
                l += (1 + c) - 65;
            else if (c >= 'a' && c <= 'z')
                l += (1 + c) - 97;
            else if (c >= '0' && c <= '9')
                l += (27 + c) - 48;
        }
        while (l % 37L == 0L && l != 0L) {
            l /= 37L;
        }
        return l;
    }

    public static final String longToString(long l) {
        if (l <= 0L || l >= 0x5b5b57f8a98a5dd1L)
            return null;
        if (l % 37L == 0L)
            return null;
        int i = 0;
        char ac[] = new char[12];
        while (l != 0L) {
            long l1 = l;
            l /= 37L;
            ac[11 - i++] = VALID_CHARS[(int) (l1 - l * 37L)];
        }
        return new String(ac, 12 - i, i);
    }

    /*
     * dont use as it blocks
     */
    public static String getExternalIP() {
        try {
            URL whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
            String ip = in.readLine(); // you get the IP as a String
            in.close();
            return ip;
        } catch (Throwable e) {
            return "127.0.0.1";
        }
    }

    public static final int getNameHash(String name) {
        name = name.toLowerCase();
        int hash = 0;
        for (int index = 0; index < name.length(); index++)
            hash = method1258(name.charAt(index)) + ((hash << 5) - hash);
        return hash;
    }

    public static final byte method1258(char c) {
        byte charByte;
        if (c > 0 && c < '\200' || c >= '\240' && c <= '\377') {
            charByte = (byte) c;
        } else if (c != '\u20AC') {
            if (c != '\u201A') {
                if (c != '\u0192') {
                    if (c == '\u201E') {
                        charByte = -124;
                    } else if (c != '\u2026') {
                        if (c != '\u2020') {
                            if (c == '\u2021') {
                                charByte = -121;
                            } else if (c == '\u02C6') {
                                charByte = -120;
                            } else if (c == '\u2030') {
                                charByte = -119;
                            } else if (c == '\u0160') {
                                charByte = -118;
                            } else if (c == '\u2039') {
                                charByte = -117;
                            } else if (c == '\u0152') {
                                charByte = -116;
                            } else if (c != '\u017D') {
                                if (c == '\u2018') {
                                    charByte = -111;
                                } else if (c != '\u2019') {
                                    if (c != '\u201C') {
                                        if (c == '\u201D') {
                                            charByte = -108;
                                        } else if (c != '\u2022') {
                                            if (c == '\u2013') {
                                                charByte = -106;
                                            } else if (c == '\u2014') {
                                                charByte = -105;
                                            } else if (c == '\u02DC') {
                                                charByte = -104;
                                            } else if (c == '\u2122') {
                                                charByte = -103;
                                            } else if (c != '\u0161') {
                                                if (c == '\u203A') {
                                                    charByte = -101;
                                                } else if (c != '\u0153') {
                                                    if (c == '\u017E') {
                                                        charByte = -98;
                                                    } else if (c != '\u0178') {
                                                        charByte = 63;
                                                    } else {
                                                        charByte = -97;
                                                    }
                                                } else {
                                                    charByte = -100;
                                                }
                                            } else {
                                                charByte = -102;
                                            }
                                        } else {
                                            charByte = -107;
                                        }
                                    } else {
                                        charByte = -109;
                                    }
                                } else {
                                    charByte = -110;
                                }
                            } else {
                                charByte = -114;
                            }
                        } else {
                            charByte = -122;
                        }
                    } else {
                        charByte = -123;
                    }
                } else {
                    charByte = -125;
                }
            } else {
                charByte = -126;
            }
        } else {
            charByte = -128;
        }
        return charByte;
    }

    public static String formatPlayerNameForProtocol(String name) {
        if (name == null)
            return "";
        name = name.replaceAll(" ", "_");
        name = name.toLowerCase();
        return name;
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

    private static final char[] UNICODE_TABLE = {'\u20ac', '\0', '\u201a', '\u0192', '\u201e', '\u2026', '\u2020',
            '\u2021', '\u02c6', '\u2030', '\u0160', '\u2039', '\u0152', '\0', '\u017d', '\0', '\0', '\u2018', '\u2019',
            '\u201c', '\u201d', '\u2022', '\u2013', '\u2014', '\u02dc', '\u2122', '\u0161', '\u203a', '\u0153', '\0',
            '\u017e', '\u0178'};

    public static char method2782(byte value) {
        int byteChar = 0xff & value;
        if (byteChar == 0)
            throw new IllegalArgumentException(
                    "Non cp1252 character 0x" + Integer.toString(byteChar, 16) + " provided");
        if ((byteChar ^ 0xffffffff) <= -129 && byteChar < 160) {
            int i_4_ = UNICODE_TABLE[-128 + byteChar];
            if ((i_4_ ^ 0xffffffff) == -1)
                i_4_ = 63;
            byteChar = i_4_;
        }
        return (char) byteChar;
    }

    public static int getHashMapSize(int size) {
        size--;
        size |= size >>> -1810941663;
        size |= size >>> 2010624802;
        size |= size >>> 10996420;
        size |= size >>> 491045480;
        size |= size >>> 1388313616;
        return 1 + size;
    }

    /**
     * Walk dirs 0 - South-West 1 - South 2 - South-East 3 - West 4 - East 5 -
     * North-West 6 - North 7 - North-East
     */
    public static int getPlayerWalkingDirection(int dx, int dy) {
        if (dx == -1 && dy == -1) {
            return 0;
        }
        if (dx == 0 && dy == -1) {
            return 1;
        }
        if (dx == 1 && dy == -1) {
            return 2;
        }
        if (dx == -1 && dy == 0) {
            return 3;
        }
        if (dx == 1 && dy == 0) {
            return 4;
        }
        if (dx == -1 && dy == 1) {
            return 5;
        }
        if (dx == 0 && dy == 1) {
            return 6;
        }
        if (dx == 1 && dy == 1) {
            return 7;
        }
        return -1;
    }

    public static int getPlayerRunningDirection(int dx, int dy) {
        if (dx == -2 && dy == -2)
            return 0;
        if (dx == -1 && dy == -2)
            return 1;
        if (dx == 0 && dy == -2)
            return 2;
        if (dx == 1 && dy == -2)
            return 3;
        if (dx == 2 && dy == -2)
            return 4;
        if (dx == -2 && dy == -1)
            return 5;
        if (dx == 2 && dy == -1)
            return 6;
        if (dx == -2 && dy == 0)
            return 7;
        if (dx == 2 && dy == 0)
            return 8;
        if (dx == -2 && dy == 1)
            return 9;
        if (dx == 2 && dy == 1)
            return 10;
        if (dx == -2 && dy == 2)
            return 11;
        if (dx == -1 && dy == 2)
            return 12;
        if (dx == 0 && dy == 2)
            return 13;
        if (dx == 1 && dy == 2)
            return 14;
        if (dx == 2 && dy == 2)
            return 15;
        return -1;
    }

    public static String fixChatMessage(String message) {
        StringBuilder newText = new StringBuilder();
        boolean wasSpace = true;
        boolean exception = false;
        for (int i = 0; i < message.length(); i++) {
            if (!exception) {
                if (wasSpace) {
                    newText.append(("" + message.charAt(i)).toUpperCase());
                    if (!String.valueOf(message.charAt(i)).equals(" "))
                        wasSpace = false;
                } else {
                    newText.append(("" + message.charAt(i)).toLowerCase());
                }
            } else {
                newText.append(("" + message.charAt(i)));
            }
            if (String.valueOf(message.charAt(i)).contains(":"))
                exception = true;
            else if (String.valueOf(message.charAt(i)).contains(".") || String.valueOf(message.charAt(i)).contains("!")
                    || String.valueOf(message.charAt(i)).contains("?"))
                wasSpace = true;
        }
        return newText.toString();
    }

    public static final int[] ROTATION_DIR_X = {-1, 0, 1, 0};

    public static final int[] ROTATION_DIR_Y = {0, 1, 0, -1};

    private Utils() {

    }

    public static String currentTime(String dateFormat) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(cal.getTime());
    }

    public static boolean colides(Entity entity, Entity target) {
        return entity.getZ() == target.getZ() && Misc.colides(entity.getX(), entity.getY(), entity.getSizeX(), entity.getSizeY(), target.getX(), target.getY(), target.getSizeX(), target.getSizeY());
    }

    public static boolean isOnRange(Entity entity, Entity target, int rangeRatio) {
        return entity.getZ() == target.getZ() && Misc.isOnRange(entity.getX(), entity.getY(), entity.getSizeX(), entity.getSizeY(), target.getX(), target.getY(), target.getSizeX(), target.getSizeY(), rangeRatio);
    }

    public static boolean isOnRange(Position entity, Position target, int rangeRatio, int s1, int s2) {
        return entity.getZ() == target.getZ()
                && Misc.isOnRange(entity.getX(), entity.getY(), s1, target.getX(), target.getY(), s2, rangeRatio);
    }

    public static int getProjectileTime(Position startTile, Position endTile, int startHeight, int endHeight,
                                        int speed, int delay, int curve, int startDistanceOffset, int creatorSize) {
        int distance = Utils.getDistance(startTile, endTile) + 1;
        if (speed == 0) // cant be 0, happens cuz method wrong and so /10 needed
            // so may round to 0
            speed = 1;
        return (int) ((delay * 10) + (distance
                * ((30 / speed) * 10) /** Math.cos(Math.toRadians(curve)) */
        ));
    }

    public static int getProjectileTimeNew(Position from, int fromSizeX, int fromSizeY, Position to, int toSizeX,
                                           int toSizeY, double speed) {
        int fromX = from.getX() * 2 + fromSizeX;
        int fromY = from.getY() * 2 + fromSizeY;

        int toX = to.getX() * 2 + toSizeX;
        int toY = to.getY() * 2 + toSizeY;

        fromX /= 2;
        fromY /= 2;
        toX /= 2;
        toY /= 2;

        int deltaX = fromX - toX;
        int deltaY = fromY - toY;
        int sqrt = (int) Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));
        return (int) (sqrt * (10 / speed));
    }

    public static int getProjectileTimeSoulsplit(Position from, int fromSizeX, int fromSizeY, Position to,
                                                 int toSizeX, int toSizeY) {
        int fromX = from.getX() * 2 + fromSizeX;
        int fromY = from.getY() * 2 + fromSizeY;

        int toX = to.getX() * 2 + toSizeX;
        int toY = to.getY() * 2 + toSizeY;

        fromX /= 2;
        fromY /= 2;
        toX /= 2;
        toY /= 2;

        int deltaX = fromX - toX;
        int deltaY = fromY - toY;
        int sqrt = (int) Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));
        sqrt *= 15;
        sqrt -= sqrt % 30;
        return Math.max(30, sqrt);
    }

    public static int projectileTimeToCycles(int time) {
        return (time + 29) / 30;
    /* return Math.max(1, (time+14)/30); */
    }

    private static final String[] NUMBER_NAMES = {"Zero", "One", "Two", "Three", "Four"};

    public static String toNumString(int i) {
        return NUMBER_NAMES[i];
    }
}
