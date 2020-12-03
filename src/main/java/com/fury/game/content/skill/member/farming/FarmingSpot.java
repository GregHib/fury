package com.fury.game.content.skill.member.farming;

import com.fury.game.node.entity.actor.figure.player.handles.Settings;
import com.fury.util.Misc;

public class FarmingSpot {

    private SpotInfo spotInfo;
    private ProductInfo productInfo;
    private int stage;
    private long cycleTime;
    private int harvestAmount;
    private boolean[] attributes;
    private FarmingManager manager;
    private boolean needRemoval;

    public FarmingSpot(FarmingManager manager, SpotInfo spotInfo) {
        this.spotInfo = spotInfo;
        cycleTime = Misc.currentTimeMillis();
        stage = 0;
        harvestAmount = 0;
        attributes = new boolean[10];
        renewCycle();
        this.manager = manager;
    }

    public void setActive(ProductInfo productInfo) {
        setProductInfo(productInfo);
        stage = -1;
        resetCycle();
    }

    private void resetCycle() {
        cycleTime = Misc.currentTimeMillis();
        harvestAmount = 0;
        for (int index = 0; index < attributes.length; index++) {
            if (index == 4)
                continue;
            attributes[index] = false;
        }
    }

    public void setCycleTime(long cycleTime) {
        setCycleTime(false, cycleTime);
    }

    public void setCycleTime(boolean reset, long cycleTime) {
        if (reset)
            this.cycleTime = 0;
        if (this.cycleTime == 0)
            this.cycleTime = Misc.currentTimeMillis();
        this.cycleTime += cycleTime;
    }

    public void setActualCycleTime(long cycleTime) {
        this.cycleTime = cycleTime;
    }

    public void setIdle() {
        stage = 3;
        setProductInfo(null);
        refresh();
        resetCycle();
    }

    public void process() {
        if (cycleTime == 0)
            return;
        while (cycleTime < Misc.currentTimeMillis()) {
            if (productInfo != null) {
                if (hasChecked() && (isEmpty() || !hasMaximumRegeneration())) {
                    if (isEmpty()) {
                        setEmpty(false);
                        if (productInfo.getType() == FarmingConstants.FRUIT_TREES)
                            setCycleTime(FarmingConstants.REGENERATION_CONSTANT);
                        else
                            cycleTime = 0;
                    } else if (!hasMaximumRegeneration()) {
                        if (harvestAmount == 5)
                            cycleTime = 0;
                        else
                            cycleTime += FarmingConstants.REGENERATION_CONSTANT;
                        harvestAmount++;
                    } else
                        cycleTime = 0;
                    refresh();
                    return;
                } else {
                    increaseStage();
                    if (reachedMaxStage() || isDead()) {
                        cycleTime = 0;
                        if(manager.getPlayer().getSettings().getBool(Settings.FARMING_MESSAGES) && spotInfo.getType() != FarmingConstants.COMPOST)
                            manager.getPlayer().getPacketSender().sendMessage("One of your farming spots " + (reachedMaxStage() ? "has finished growing.": "caught a disease and has died!"), 0x516534);
                        break;
                    }
                }
            } else {
                if (spotInfo.getType() != FarmingConstants.COMPOST) {
                    decreaseStage();
                    if (stage <= 0) {
                        remove();
                        break;
                    }
                }
            }
            renewCycle();
        }
    }

    public int getConfigBaseValue() {
        if (productInfo != null) {
            if (productInfo.getType() == FarmingConstants.ALLOTMENT)
                return 6 + (productInfo.getConfigIndex() * 7);
            else if (productInfo.getType() == FarmingConstants.HERBS)
                return 4 + (productInfo.getConfigIndex() * 7);
            else if (productInfo.getType() == FarmingConstants.FLOWERS)
                return 8 + (productInfo.getConfigIndex() * 5);
            else if (productInfo.getType() == FarmingConstants.HOPS)
                return 3 + (productInfo.getConfigIndex() * 5);
            else if (productInfo.getType() == FarmingConstants.TREES || productInfo.getType() == FarmingConstants.FRUIT_TREES || productInfo.getType() == FarmingConstants.BUSHES)
                return 8 + (productInfo.getConfigIndex() ^ 2 - 1);
            else if (productInfo.getType() == FarmingConstants.MUSHROOMS)
                return 4 + (productInfo.getConfigIndex() * 22);
            else if (productInfo.getType() == FarmingConstants.BELLADONNA)
                return 4;
        }
        return stage;
    }

    private int getConfigValue(int type) {
        if (type == FarmingConstants.HERBS)
            return isDead() ? stage + 169 : getConfigBaseValue() + ((isDiseased() && stage != 0) ? stage + 127 : stage);
        else if (type == FarmingConstants.TREES) {
            int baseValue = getConfigBaseValue() + (isDead() ? stage + 128 : (isDiseased() && stage != 0) ? stage + 64 : stage);
            if (hasChecked()) {
                baseValue += 2;
                if (!isEmpty())
                    baseValue--;
            }
            return baseValue;
        } else if (type == FarmingConstants.FRUIT_TREES) {
            int baseValue = stage + getConfigBaseValue();
            if (hasChecked())
                baseValue += getHarvestAmount();
            else if (isDead())
                baseValue += 20;
            else if (isDiseased())
                baseValue += 12;
            else if (!hasChecked() && reachedMaxStage())
                baseValue += 20;
            if (isEmpty())
                baseValue += 19;
            return baseValue;
        } else if (type == FarmingConstants.BUSHES) {
            int baseValue = stage + getConfigBaseValue();
            if (hasChecked())
                baseValue += getHarvestAmount();
            else if (isDead())
                baseValue += 128;
            else if (isDiseased())
                baseValue += 65;
            else if (!hasChecked() && reachedMaxStage())
                baseValue += 240;
            return baseValue;
        } else if (type == FarmingConstants.COMPOST) {
            return isCleared() ? harvestAmount + 16 + (hasChecked() ? -1 : 0) : productInfo != null && reachedMaxStage() ? 0 : harvestAmount - stage;
        } else if (type == FarmingConstants.FLOWERS || type == FarmingConstants.HOPS || type == FarmingConstants.ALLOTMENT)
            return getConfigBaseValue() + (isDead() ? stage + 192 : (isDiseased() && stage != 0) ? stage + 128 : isWatered() ? 64 + stage : stage);
        else if (type == FarmingConstants.MUSHROOMS) {
            int value = stage + getConfigBaseValue();
            if (isDead())
                value += productInfo.getConfigIndex() == 1 ? 19 : 16;
            else if (isDiseased() && stage != 0)
                value += productInfo.getConfigIndex() == 1 ? 14 : 11;
            return value;
        } else if (type == FarmingConstants.BELLADONNA) {
            int value = stage + getConfigBaseValue();
            if (isDead())
                value += 7;
            else if (isDiseased() && stage != 0)
                value += 4;
            return value;
        }
        return stage + getConfigBaseValue();
    }

    private void checkFactors() {
        if (isDiseased()) {
            if (reachedMaxStage()) {
                setDead(false);
                setDiseased(false);
            } else {
                if (isFirstCycle())
                    setFirstCycle(false);
                else
                    setDead(true);
            }
        }
        if (productInfo.getType() == FarmingConstants.FRUIT_TREES || productInfo.getType() == FarmingConstants.BUSHES) {
            if (reachedMaxStage())
                setHarvestAmount(productInfo.getType() == FarmingConstants.BUSHES || productInfo == ProductInfo.PALM ? 4 : 6);
        }
        setWatered(false);
        checkDisease();
    }

    public boolean reachedMaxStage() {
        return stage == productInfo.getMaxStage();
    }

    private boolean hasMaximumRegeneration() {
        if (spotInfo.getType() != FarmingConstants.FRUIT_TREES && spotInfo.getType() != FarmingConstants.BUSHES)
            return true;
        else if (getHarvestAmount() != FarmingConstants.HARVEST_AMOUNTS[productInfo.getType()][1])
            return false;
        return true;
    }

    public void renewCycle() {
        long constant = 10000L;
        if (productInfo != null) {
            cycleTime += constant * productInfo.getCycleTime();
        } else
            cycleTime += constant * 3;
    }

    public boolean canBeDiseased() {
        if (stage == 0 && productInfo.getType() != FarmingConstants.BUSHES || reachedMaxStage() || isDiseased() || productInfo == ProductInfo.WHITE_LILY || productInfo == ProductInfo.POISON_IVY || productInfo.getType() == FarmingConstants.COMPOST)
            return false;
        return true;
    }

    private void checkDisease() {
        if (canBeDiseased()) {
            int baseValue = 35;
            if (isWatered())
                baseValue += 10;
            if (getCompost())
                baseValue += 10;
            else if (getSuperCompost())
                baseValue += 20;
            if (Misc.random(baseValue) == 0) {
                setDiseased(true);
                refresh();
            }
        }
    }

    public void increaseStage() {
        stage++;
        if (productInfo != null)
            checkFactors();
        refresh();
    }

    public void decreaseStage() {
        setCleared(false);
        stage--;
        refresh();
    }

    public void remove() {
        needRemoval = true;
    }

    public boolean needsRemoval() {
        return needRemoval;
    }

    public void refresh() {
        int value = spotInfo.getType() == FarmingConstants.COMPOST ? getConfigValue(spotInfo.getType()) : productInfo != null ? getConfigValue(spotInfo.getType()) + productInfo.getStageSkip() : stage;
        manager.getPlayer().getPacketSender().sendConfigByFile(spotInfo.getConfigFileId(), value);
    }

    public void setProductInfo(ProductInfo productInfo) {
        this.productInfo = productInfo;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public void setAttributes(boolean[] attributes) {
        this.attributes = attributes;
    }

    public SpotInfo getSpotInfo() {
        return spotInfo;
    }

    public ProductInfo getProductInfo() {
        return productInfo;
    }

    public int getStage() {
        return stage;
    }

    public long getCycleTime() {
        return cycleTime;
    }

    public boolean[] getAttributes() {
        return attributes;
    }

    public boolean isDiseased() {
        return attributes[0];
    }

    public void setDiseased(boolean diseased) {
        this.attributes[0] = diseased;
    }

    public boolean isWatered() {
        return attributes[1];
    }

    public void setWatered(boolean watered) {
        this.attributes[1] = watered;
    }

    public boolean isDead() {
        return attributes[2];
    }

    public void setDead(boolean dead) {
        this.attributes[2] = dead;
        if (dead)
            setDiseased(false);
    }

    public boolean isFirstCycle() {
        return attributes[3];
    }

    public void setFirstCycle(boolean firstCycle) {
        this.attributes[3] = firstCycle;
    }

    public boolean isCleared() {
        return attributes[4];
    }

    public void setCleared(boolean cleared) {
        this.attributes[4] = cleared;
    }

    public boolean hasChecked() {
        return attributes[5];
    }

    public void setChecked(boolean checked) {
        this.attributes[5] = checked;
    }

    public boolean isEmpty() {
        return attributes[6];
    }

    public void setEmpty(boolean empty) {
        this.attributes[6] = empty;
    }

    public boolean hasCompost() {
        return attributes[7] || attributes[8];
    }

    public boolean getSuperCompost() {
        return attributes[8];
    }

    public void setSuperCompost(boolean superCompost) {
        this.attributes[8] = superCompost;
    }

    public boolean hasGivenAmount() {
        return attributes[9];
    }

    public void setHasGivenAmount(boolean amount) {
        this.attributes[9] = amount;
    }

    public boolean getCompost() {
        return attributes[7];
    }

    public void setCompost(boolean compost) {
        this.attributes[7] = compost;
    }

    public void setHarvestAmount(int harvestAmount) {
        this.harvestAmount = harvestAmount;
    }

    public boolean hasEmptyHarvestAmount() {
        return harvestAmount == 0;
    }

    public int getHarvestAmount() {
        return harvestAmount;
    }
}
