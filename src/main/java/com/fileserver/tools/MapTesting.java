package com.fileserver.tools;

import com.fury.cache.ByteBuffer;
import com.fury.game.GameLoader;
import com.fury.game.world.map.region.RegionIndex;
import com.fury.game.world.map.region.RegionIndexing;

public class MapTesting {
    public static void main(String[] args) throws Exception {
        GameLoader.getCache().init();
        RegionIndexing.init();
        int regionId = 11573;
        RegionIndex regionData = RegionIndexing.get(regionId);

        if (regionData == null)
            return;
//        Path path = Paths.get("C:\\Users\\Greg\\Documents\\Projects\\Games\\Runescape Private Servers\\Frontier V3\\Server\\src\\com\\fileserver\\tools\\768.dat");
//        byte[] data = Files.readAllBytes(path);
//        System.out.println(Arrays.toString(data));
        int mapObject = regionData.mapObject;
        byte[] mapSettingsData = GameLoader.getCache().getDecompressedFile(regionData.revision.getMapIndex(), mapObject);
        //if (mapSettings == null)
        //    return;

        int landscapes = regionData.landscape;
        byte[] landscapeData = GameLoader.getCache().getDecompressedFile(regionData.revision.getMapIndex(), landscapes);
        //if (landscapeData != null)
        //    decodeLandscapes(regionId, landscapeData, mapSettings);

        int dX = 0, dY = 8, regionX = 366, regionY = 429;

        decodeRegionMapData(mapSettingsData, dY, dX, ((regionX - 6) * 8), (regionY - 6) * 8);

        dX = 256;dY = 3016; regionX = 45; regionY = 53;
        decodeRegionMapData(regionY * 8 - 48, mapSettingsData, dY, regionX * 8 - 48, dX);


    }
    static final void decodeRegionMapData(int regY, byte[] is,
                                          int dy, int regX, int dx) {
        dy = 0;
        dx = 0;
        for (int i_8_ = 0; i_8_ < 4; i_8_++) {
            for (int i_9_ = 0; i_9_ < 64; i_9_++) {
                for (int i_10_ = 0; i_10_ < 64; i_10_++) {
                    if (i_9_ + dx > 0 && dx + i_9_ < 103
                            && dy + i_10_ > 0 && dy + i_10_ < 103) {

                    }
//                        class59s[i_8_].collisionFlags[dx + i_9_][dy + i_10_] &= -16777217;
                }
            }
        }
        ByteBuffer class3_sub12 = new ByteBuffer(is);
        for (int z = 0; z < 4; z++) {
            for (int localX = 0; localX < 64; localX++) {
                for (int localY = 0; localY < 64; localY++)
                    decodeMapData(regY, localY + dy, 0, z, regX, dx
                            + localX, class3_sub12);
            }
        }
    }
    static final void decodeMapData(int regionY, int y, int dummy, int z, int regionX, int x,
                                    ByteBuffer stream) {
        try {
            //System.out.println("Extra:" + regionX + " " + regionY + " " + x + " " + y);

            if (x >= 0 && y >= 0 && x < 103 && y < 103) {
                //System.out.println(x + "," + y);
//                tileSettings[z][x][y] = (byte) 0;
                for (; ; ) {
                    int opcode = stream.getUByte();
//					if(x == 50 && y == 52 && z == 0)
//                    System.out.println("T: " + opcode + " " + x + " ," + y);
                    if (opcode == 0) {
//                        heightMod[z][x][y] = true;
                        if (z == 0) {
//                            System.out.println(-calculateHeight(x + 932731 + regionX, regionY + y + 556238) * 8);
//                            System.out.println((regionX + x + 932731) + ", " + (regionY + y + 556238));
//                            tileHeight[0][x][y] = -Client.calculateHeight(regionY + y + 556238,
//                                    x + 932731 + regionX) * 8;
                        } else {

//                            tileHeight[z][x][y] = (tileHeight[z - 1][x][y]) - 240
                        }
                        break;
                    }
                    if (opcode == 1) {
//                        heightMod[z][x][y] = false;
                        int heightModifier = stream.getUByte();
						System.out.println("H2:" + heightModifier);
                        if (heightModifier == 1)
                            heightModifier = 0;
                        if (z != 0) {
//                            tileHeight[z][x][y] = -(heightModifier * 8) + (tileHeight[z - 1][x][y]);
                        } else {
//                            if(x == 50 && y == 52)
//                            	System.out.println(x + " " + y + " " + heightModifier + " " + (- heightModifier * 8));
//                            tileHeight[0][x][y] = -heightModifier * 8;
                        }
                        break;
                    }
                    if (opcode <= 49) {
                        stream.getByte();
//                        underlayIds[z][x][y] = stream.getByte();
//                        underlayShapes[z][x][y] = (byte) ((opcode - 2) / 4);
//                        underlayRotations[z][x][y] = (byte) ((opcode + (dummy - 2)) & 3);
                    } else if (opcode <= 81) {
//                        tileSettings[z][x][y] = (byte) (opcode - 49);
                        //System.out.println(z+":"+x+":"+y+" = "+tileSettings[z][x][y]);
                    } else {
//                        overlayIds[z][x][y] = (byte) (opcode - 81);
                    }
                }
            } else {
                for (; ; ) {
                    int i_10_ = stream.getUByte();
                    if (i_10_ == 0)
                        break;
                    if (i_10_ == 1) {
                        stream.getUByte();
                        break;
                    }
                    if (i_10_ <= 49)
                        stream.getUByte();
                }
            }
        } catch (Exception e) {
            System.out.println("Leanbow sucks");
        }
    }

    public static  void decodeRegionMapData(byte[] data, int dY, int dX, int regionX, int regionY/*, CollisionMap[] maps*/) {
        for (int z = 0; z < 4; z++) {
            for (int localX = 0; localX < 64; localX++) {
                for (int localY = 0; localY < 64; localY++) {
                    if (dX + localX > 0 && dX + localX < 103 && dY + localY > 0 && dY + localY < 103) {
                        //maps[z].clipData[dX + localX][dY + localY] &= 0xfeffffff;
                    }
                }
            }
        }
        ByteBuffer stream = new ByteBuffer(data);
        for (int z = 0; z < 4; z++) {
            for (int localX = 0; localX < 64; localX++) {
                for (int localY = 0; localY < 64; localY++) {
                    decodeMapData(localY + dY, localY + dY + regionY, stream, localX + dX, z, 0, localX + dX + regionX);
                }
            }
        }
    }

    private static void decodeMapData(int y, int regionY, ByteBuffer stream, int x, int z, int i1, int regionX) {
        if (x >= 0 && x < 103 && y >= 0 && y < 103) {
//            System.out.println(x + "," + y);
            //tileFlags[z][x][y] = 0;
            do {
                int type = stream.getUByte();
//                if(x == 50 && y == 52 && z == 0)
                //System.out.println("T: " + type + " " + x + " ," + y);
                if (type == 0) {
                    if (z == 0) {
//                        System.out.println(-calculateHeight(regionX + 932731, regionY + 556238) * 8);
//                        System.out.println((regionX + 932731) + ", " + (regionY + 556238));
                        //System.out.println();
                        //tileHeights[0][x][y] = -calculateHeight(regionX + 932731, regionY + 556238) * 8;
                    } else {
                        //tileHeights[z][x][y] = tileHeights[z - 1][x][y] - 240;
                    }
                    break;
                }

                if (type == 1) {
                    int height = stream.getUByte();
                    System.out.println(height);
                    if (height == 1)
                        height = 0;
                    if (z == 0) {
                        //tileHeights[0][x][y] = -height * 8;
//                        if(x == 50 && y == 52)
//                        System.out.println(x + "," + y + " " + height + " " + (-height * 8));
                    } else {
                        //tileHeights[z][x][y] = tileHeights[z - 1][x][y] - height * 8;
                    }
                    break;
                }

                if (type <= 49) {
                    stream.getByte();
                    //overlays[z][x][y ] = stream.getByte();
                    //overlayTypes[z][x][y ] = (byte) ((type - 2) >> 2);
                    //overlayOrientations[z][x][y ] = (byte) (type - 2 + i1 & 3);
                } else if (type <= 81) {
                    //tileFlags[z][x][y] = (byte) (type - 49);
                } else {
                    //underlays[z][x][y] = (byte) (type - 81);
                }
            } while (true);
        } else {
            do {
                int in = stream.getUByte();

                if (in == 0) {
                    break;
                } else if (in == 1) {
                    stream.getUByte();
                    break;
                } else if (in <= 49) {
                    stream.getUByte();
                }
            } while (true);
        }
    }

    private static int calculateHeight(int x, int y) {
//        int height = interpolatedNoise(x + 45365, y + 0x16713, 4) - 128 + (interpolatedNoise(x + 10294, y + 37821, 2) - 128 >> 1) + (interpolatedNoise(x, y, 1) - 128 >> 2);
//        height = (int) ((double) height * 0.29999999999999999D) + 35;
        int noise1 = interpolatedNoise(x + 45365, 4, y + 91923);
        int noise2 = (interpolatedNoise(x + 10294, 2, y + 37821) - 128 >> 1);
        int noise3 = noise2 + noise1;
        int noise4 = interpolatedNoise(x, 1, y) - 128 >> 2;
        int height = noise4 + noise3 - 128;
        height = (int) ((double) height * 0.3) + 35;
        if (height < 10) {
            height = 10;
        } else if (height > 60) {
            height = 60;
        }

        return height;
    }
    private static int interpolatedNoise(int x, int y, int frequencyReciprocal) {
        int adj_x = x / frequencyReciprocal;
        int i1 = x & frequencyReciprocal - 1;
        int adj_y = y / frequencyReciprocal;
        int k1 = y & frequencyReciprocal - 1;
        int l1 = smoothNoise(adj_x, adj_y);
        int i2 = smoothNoise(adj_x + 1, adj_y);
        int j2 = smoothNoise(adj_x, adj_y + 1);
        int k2 = smoothNoise(adj_x + 1, adj_y + 1);
        int l2 = interpolate(l1, i2, i1, frequencyReciprocal);
        int i3 = interpolate(j2, k2, i1, frequencyReciprocal);
        return interpolate(l2, i3, k1, frequencyReciprocal);
    }
    private static int smoothNoise(int x, int y) {
        int corners = perlinNoise(x - 1, y - 1) + perlinNoise(x + 1, y - 1) + perlinNoise(x - 1, y + 1) + perlinNoise(x + 1, y + 1);
        int sides = perlinNoise(x - 1, y) + perlinNoise(x + 1, y) + perlinNoise(x, y - 1) + perlinNoise(x, y + 1);
        int center = perlinNoise(x, y);
        return sides / 8 + (corners / 16 + center / 4);
    }
    private static int perlinNoise(int x, int y) {
        int n = x * 57 + y;
        n = n << 13 ^ n;
        int l = n * (n * n * 15731 + 0xc0ae5) + 0x5208dd0d & 0x7fffffff;
        return l >> 19 & 0xff;
    }
    private static int interpolate(int a, int b, int angle, int frequencyReciprocal) {
        int cosine = 0x10000 - COSINE[angle * 1024 / frequencyReciprocal] >> 1;
        return (a * (0x10000 - cosine) >> 16) + (b * cosine >> 16);
    }

    public static int COSINE[];
    static {
        COSINE = new int[2048];
        for (int k = 0; k < 2048; k++) {
            COSINE[k] = (int) (65536D * Math.cos(k * 0.0030679614999999999D));
        }
    }
}
