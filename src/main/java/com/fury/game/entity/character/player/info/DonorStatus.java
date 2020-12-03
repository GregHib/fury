package com.fury.game.entity.character.player.info;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.node.entity.actor.figure.player.Points;
import com.fury.util.Misc;

/**
 * Created by Greg on 26/11/2016.
 */
public enum DonorStatus {
    NONE(0, 1, 0, -1, 0.0, 0, 0, 12, 1.0),
    SAPPHIRE_DONOR(10, 1.1, 0x0000ff, 5, 0.01, 5, 22, 8, 1.02),
    EMERALD_DONOR(50, 1.2, 0x007f0e, 4, 0.02, 8, 46, 7, 1.04),
    RUBY_DONOR(150, 1.3, 0xbf0000, 3, 0.03, 12, 66, 6, 1.06),
    DIAMOND_DONOR(500, 1.4, 0xffffff, 2, 0.04, 15, 88, 5, 1.08),
    DRAGONSTONE_DONOR(1500, 1.5, 0xb200ff, 1, 0.05, 18, 110, 4, 1.1),
    ONYX_DONOR(5000, 1.6, 0xffffff, -1, 0.055, 25, 128, 3, 1.12);

    private int amount;
    private double loyaltyPointsGainModifier;
    private int yellColour;
    private int yellDelay;
    private double dropRate;
    private double prayerDrain;
    private int dailyPoints;
    private int extraBankSpace;
    private int borkTime;

    DonorStatus(int amount, double loyaltyPointsGainModifier, int yellColour, int yellDelaySeconds, double dropRate, int dailyPoints, int extraBankSpace, int borkTime, double prayerDrain) {
        this.amount = amount;
        this.loyaltyPointsGainModifier = loyaltyPointsGainModifier;
        this.yellColour = yellColour;
        this.yellDelay = yellDelaySeconds;
        this.dropRate = dropRate;
        this.dailyPoints = dailyPoints;
        this.extraBankSpace = extraBankSpace;
        this.borkTime = borkTime;
        this.prayerDrain = prayerDrain;
    }

    public int getAmount() {
        return amount;
    }

    public double getLoyaltyPointsGainModifier() {
        return loyaltyPointsGainModifier;
    }

    public int getYellColour() {
        return yellColour;
    }

    public int getYellDelay() {
        return yellDelay;
    }

    public int getDailyPoints() {
        return dailyPoints;
    }

    public int getExtraBankSpace() {
        return extraBankSpace;
    }

    public static boolean isDonor(Player player, DonorStatus status) {
        return player.getPoints().get(Points.DONATED) >= status.getAmount();
    }

    //TODO convert to a player variable/function rather than all static calls
    public static DonorStatus get(Player player) {
        DonorStatus highest = DonorStatus.NONE;
        for(DonorStatus stat : values()) {
            if(player.getPoints().get(Points.DONATED) >= stat.getAmount())
                highest = stat;
            if(player.getPoints().get(Points.DONATED) < stat.getAmount())
                break;
        }
        return highest;
    }

    public double getDropRate() {
        return dropRate;
    }

    public int getBorkTime() {
        return borkTime;
    }

    public double getPrayerDrain() {
        return prayerDrain;
    }

    public String getName() {
        return Misc.uppercaseFirst(name().replaceAll("_DONOR", "").replaceAll("_", " "));
    }
}
