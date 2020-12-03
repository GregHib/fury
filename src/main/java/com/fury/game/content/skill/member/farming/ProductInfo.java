package com.fury.game.content.skill.member.farming;

import com.fury.core.model.item.Item;

import java.util.HashMap;
import java.util.Map;

public enum ProductInfo {

    /**
     * Allotments
     */
    POTATO(5318, 1, 1942, 0, 8, 9, 10, FarmingConstants.ALLOTMENT),
    ONION(5319, 5, 1957, 1, 9.5, 10.5, 10, FarmingConstants.ALLOTMENT),
    CABBAGE(5324, 7, 1965, 2, 10, 11.5, 10, FarmingConstants.ALLOTMENT),
    TOMATO(5322, 12, 1982, 3, 12.5, 14, 10, FarmingConstants.ALLOTMENT),
    SWEETCORN(5320, 20, 5986, 4, 17, 19, 10, 6, FarmingConstants.ALLOTMENT),
    STRAWBERRY(5323, 31, 5504, 5, 26, 29, 10, 6, 2, FarmingConstants.ALLOTMENT),
    WATERMELON(5321, 47, 5982, 6, 48.5, 54.5, 10, 8, 4, FarmingConstants.ALLOTMENT),

    /**
     * Herbs
     */
    GUAM(5291, 9, 199, 0, 11, 12.5, 20, FarmingConstants.HERBS),
    MARRENTILL(5292, 14, 201, 1, 13.5, 15, 20, FarmingConstants.HERBS),
    TARROMIN(5293, 19, 203, 2, 16, 18, 20, FarmingConstants.HERBS),
    HARRALANDER(5294, 26, 205, 3, 21.5, 24, 20, FarmingConstants.HERBS),
    RANNAR(5295, 32, 207, 4, 27, 30.5, 20, FarmingConstants.HERBS),
    TOADFLAX(5296, 38, 3049, 5, 34, 38.5, 20, FarmingConstants.HERBS),
    IRIT(5297, 44, 209, 6, 43, 48.5, 20, FarmingConstants.HERBS),
    AVANTOE(5298, 50, 211, 7, 54.4, 61.5, 20, FarmingConstants.HERBS),
    KWUARM(5299, 56, 213, 6, 69, 78, 20, FarmingConstants.HERBS),
    SNAPDRAGON(5300, 62, 3051, 6, 87.5, 98.5, 20, FarmingConstants.HERBS),
    CADANTINE(5301, 67, 215, 6, 106.5, 120, 20, FarmingConstants.HERBS),
    LANTADYME(5302, 73, 2485, 6, 134.5, 151.5, 20, FarmingConstants.HERBS),
    DWARF(5303, 79, 217, 6, 170.5, 192, 20, FarmingConstants.HERBS),
    TORSTOL(5304, 85, 219, 6, 199.5, 224.5, 20, FarmingConstants.HERBS),
    FELLSTALK(21621, 91, 21626, 6, 225, 315.6, 20, FarmingConstants.HERBS),
    WERGALI(14870, 46, 213, 8, 52.8, 52.8, 20, FarmingConstants.HERBS),
    GOUT(6311, 65, 3261, 27, 105, 45, 20, FarmingConstants.HERBS),

    /**
     * Flowers
     */
    MARIGOLD(5096, 2, 6010, 0, 8.5, 47, 5, FarmingConstants.FLOWERS),
    ROSEMARY(5097, 11, 6014, 1, 12, 66.5, 5, FarmingConstants.FLOWERS),
    NASTURTIUM(5098, 24, 6012, 2, 19.5, 111, 5, FarmingConstants.FLOWERS),
    WOAD(5099, 25, 1793, 3, 20.5, 115.5, 5, FarmingConstants.FLOWERS),
    LIMPWURT(5100, 26, 225, 4, 21.5, 120, 5, FarmingConstants.FLOWERS),
    WHITE_LILY(14589, 52, 14583, 6, 70, 250, 20, 4, -1, FarmingConstants.FLOWERS),

    /**
     * Hops
     */
    BARLEY(5305, 3, 6006, 9, 8.5, 9.5, 10, 4, 1, FarmingConstants.HOPS),
    HAMMERSTONE(5307, 4, 5994, 0, 9, 10, 10, 4, 1, FarmingConstants.HOPS),
    ASGARNIAN(5308, 8, 5996, 1, 10.9, 12, 10, 5, 3, FarmingConstants.HOPS),
    JUTE(5306, 13, 5931, 10, 13, 14.5, 10, 5, 3, FarmingConstants.HOPS),
    YANILLIAN(5309, 16, 5998, 3, 14.5, 16, 10, 6, 1, FarmingConstants.HOPS),
    KRANDORIAN(5310, 21, 6000, 5, 17.5, 19.5, 10, 7, FarmingConstants.HOPS),
    WILDBOOD(5311, 28, 6002, 7, 23, 26, 10, 7, 1, FarmingConstants.HOPS),

    /**
     * Trees
     */
    OAK(5370, 15, 6043, 1, 467.3, 14, 40, FarmingConstants.TREES),
    WILLOW(5371, 30, 6045, 6, 1456.5, 25, 40, 6, FarmingConstants.TREES),
    MAPLE(5372, 45, 6047, 17, 3403.4, 45, 40, 8, FarmingConstants.TREES),
    YEW(5373, 60, 6049, 26, 7069.9, 81, 40, 10, FarmingConstants.TREES),
    MAGIC(5374, 75, 6051, 41, 13768.3, 145.5, 40, 12, FarmingConstants.TREES),

    /**
     * Trees of the fruits :)
     */
    APPLE(5496, 27, 1955, 1, 1199.5, 22, 160, 6, FarmingConstants.FRUIT_TREES),
    BANANA(5497, 33, 1963, 26, 1841.5, 28, 160, 6, FarmingConstants.FRUIT_TREES),
    ORANGE(5498, 39, 2108, 65, 2470.2, 35.5, 160, 6, FarmingConstants.FRUIT_TREES),
    CURRY(5499, 42, 5970, 90, 2906.9, 40, 160, 6, FarmingConstants.FRUIT_TREES),
    PINEAPPLE(5500, 51, 2114, 129, 4605.7, 57, 160, 6, FarmingConstants.FRUIT_TREES),
    PAPAYA(5501, 57, 5972, 154, 6146.4, 72, 160, 6, FarmingConstants.FRUIT_TREES),
    PALM(5502, 68, 5974, 193, 10150.1, 110.5, 160, 6, FarmingConstants.FRUIT_TREES),

    /**
     * Bushes of the bush
     */
    REDBERRY(5101, 10, 1951, -4, 64, 11.5, 20, 5, FarmingConstants.BUSHES),
    CADAVABERRY(5102, 22, 753, 6, 102.5, 18, 20, 6, FarmingConstants.BUSHES),
    DWELLBERRY(5103, 36, 2126, 19, 177.5, 31.5, 20, 7, FarmingConstants.BUSHES),
    JANGERBERRY(5104, 48, 247, 31, 284.5, 50.5, 20, 8, FarmingConstants.BUSHES),
    WHITEBERRY(5105, 59, 239, 42, 437.5, 78, 20, 8, FarmingConstants.BUSHES),
    POISON_IVY(5106, 70, 6018, 188, 675, 120, 20, 8, FarmingConstants.BUSHES),

    COMPOST_BIN(7836, 1, -1, 0, 8, 14, 2, 15, FarmingConstants.COMPOST),

    BITTERCAP(17825, 53, 17821, 0, 61.5, 57.7, 40, 6, 0, FarmingConstants.MUSHROOMS),
    MORCHELLA(21620, 74, 21622, 1, 22, 77.7, 25, 6, 0, FarmingConstants.MUSHROOMS),

    BELLADONNA(5281, 63, 2398, 0, 91, 512, 80, FarmingConstants.BELLADONNA);

    private static Map<Short, ProductInfo> products = new HashMap<>();

    public static ProductInfo getProduct(int itemId) {
        return products.get((short) itemId);
    }

    static {
        for (ProductInfo product : ProductInfo.values()) {
            products.put((short) product.seedId, product);
        }
    }

    private int seedId;
    private int level;
    private int productId;
    private int configIndex;
    private int type;
    private int maxStage;
    private int stageSkip;
    private double experience;
    private double plantingExperience;
    private int cycleTime;

    ProductInfo(int seedId, int level, int productId, int configIndex, double plantingExperience, double experience, int cycleTime, int maxStage, int stageSkip, int type) {
        this.seedId = seedId;
        this.level = level;
        this.productId = productId;
        this.configIndex = configIndex;
        this.plantingExperience = plantingExperience;
        this.experience = experience;
        this.cycleTime = cycleTime;
        this.maxStage = maxStage;
        this.stageSkip = stageSkip;
        this.type = type;
    }

    private ProductInfo(int seedId, int level, int productId, int configIndex, double plantingExperience, double experience, int cycleTime, int maxStage, int type) {
        this(seedId, level, productId, configIndex, plantingExperience, experience, cycleTime, maxStage, 0, type);
    }

    private ProductInfo(int seedId, int level, int productId, int configIndex, double plantingExperience, double experience, int cycleTime, int type) {
        this(seedId, level, productId, configIndex, plantingExperience, experience, cycleTime, 4, 0, type);
    }

    public static boolean isProduct(Item item) {
        for (ProductInfo info : ProductInfo.values()) {
            if (info.productId == item.getId()) {
                return true;
            }
        }
        return false;
    }

    public int getType() {
        return type;
    }

    public int getMaxStage() {
        return maxStage;
    }

    public int getCycleTime() {
        return cycleTime;
    }

    public int getStageSkip() {
        return stageSkip;
    }

    public double getExperience() {
        return experience;
    }

    public double getPlantingExperience() {
        return plantingExperience;
    }

    public int getSeedId() {
        return seedId;
    }

    public int getLevel() {
        return level;
    }

    public int getProductId() {
        return productId;
    }

    public int getConfigIndex() {
        return configIndex;
    }
}
