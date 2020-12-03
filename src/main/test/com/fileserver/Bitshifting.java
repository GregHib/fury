package com.fileserver;

public class Bitshifting {
    public static void main(String[] args) {

        System.out.println((100 << 16) + (0 & 0xffff));
        System.out.println(6553600 & 0xffff);
        System.out.println(6553600 >> 16);
        long a = 4; long b = 11110;

        long res = a | (b << 3);

        a = res & 4;
        b = (res >> 3) & 0xFFFF;
        System.out.println(Long.toBinaryString(res));
        System.out.println(Long.toBinaryString((res >> 3)));
        System.out.println(Long.toBinaryString(0x9F));
        System.out.println(Long.toBinaryString((res >> 3) & 0xFFFF));
        System.out.println(Long.toBinaryString(34567));

        System.out.println(a + " " + b);


        fourParts();

        keyTest();
    }

    private static void keyTest() {
        int rotation = 100;
        int type = 80000;
        int regionY = 120;
        int regionX = 110;

        long key = (long) (rotation << 20 | (type << 14 | (regionY << 7 | regionX)) | 0x40000000);
    }

    private static void fourParts() {
        long a = 3; long b = 125; long c = 127; long d = 80000;
        //int res = a | (b << 2) | (c << 9) | (d << 16);

        long res = a | (b << 2);

        a = res & 3;
        b = (res >> 2) & 0x7F;
//        c = (res >> 9) & 0x7F;
//        d = (res >> 16) & 0x1FFFF
    }

    public static String binaryToHex(String bin) {
        return String.format("%21X", Long.parseLong(bin,2)) ;
    }

    static int combine(int a, int b) {
        return a << 8 | b;
    }

    static int getA(int c) {
        return c >> 8;
    }

    static int getB(int c) {
        return c & 0xff;
    }
}
