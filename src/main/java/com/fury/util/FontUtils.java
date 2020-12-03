package com.fury.util;

/**
 * Created by Greg on 13/04/2017.
 */
public class FontUtils {
    private static final String COL = "<col=";
    private static final String COL_CLOSE = ">";
    private static final String IMG = "<img=";
    private static final String IMG_CLOSE = ">";
    public static final String SHAD = "<shad>";
    public static final String SHAD_END = "</shad>";
    public static final String COL_END = "</col>";

    public static final String RED = colourTags(Colours.RED);
    public static final String GREEN = colourTags(Colours.GREEN);
    public static final String BLUE = colourTags(Colours.BLUE);
    public static final String YELLOW = colourTags(Colours.YELLOW);
    public static final String CYAN = colourTags(Colours.CYAN);
    public static final String MAGENTA = colourTags(Colours.MAGENTA);
    public static final String WHITE = colourTags(Colours.WHITE);
    public static final String LRE = colourTags(Colours.LRE);
    public static final String DRE = colourTags(Colours.DRE);
    public static final String BLACK = colourTags(Colours.BLACK);
    public static final String ORANGE = colourTags(Colours.ORANGE);
    public static final String ORANGE_1 = colourTags(Colours.ORANGE_1);
    public static final String ORANGE_2 = colourTags(Colours.ORANGE_2);
    public static final String ORANGE_3 = colourTags(Colours.ORANGE_3);
    public static final String GREEN_1 = colourTags(Colours.GREEN_1);
    public static final String GREEN_2 = colourTags(Colours.GREEN_2);
    public static final String GREEN_3 = colourTags(Colours.GREEN_3);


    public static String add(String message, int colour) {
        return colourTags(colour) + message + COL_END;
    }

    public static String colourTags(int colour) {
        return COL + Integer.toHexString(colour) + COL_CLOSE;
    }

    public static String imageTags(int sprite) {
        return IMG + sprite + IMG_CLOSE;
    }
}
