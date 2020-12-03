package com.fury.game.content.skill.member.summoning.impl;

import com.fury.cache.Revision;
import com.fury.game.GameSettings;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import com.fury.game.content.global.Achievements;
import com.fury.game.npc.familiar.Familiar;
import com.fury.game.world.GameWorld;
import com.fury.game.world.World;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.Flag;
import com.fury.util.Misc;
import com.fury.util.Utils;

public class Pet extends Mob {
    private final Player owner;

    /**
     * The "near" directions.
     */
    private final int[][] checkNearDirs;

    /**
     * The item id.
     */
    private final int itemId;

    /**
     * The pet details.
     */
    private final PetDetails details;

    /**
     * The growth rate of the pet.
     */
    private double growthRate;

    /**
     * The pets type.
     */
    private final Pets pet;

    /**
     * Constructs a new {@code Pet} {@code Object}.
     *
     * @param id
     *            The Npc id.
     * @param itemId
     *            The item id.
     * @param owner
     *            The owner.
     * @param tile
     *            The world tile.
     */
    public Pet(int id, int itemId, Player owner, Position tile, PetDetails details) {
        super(id, tile, Revision.RS2, false);
        this.owner = owner;
        this.itemId = itemId;
        this.checkNearDirs = Utils.getCoordOffsetsNear(super.getSize());
        this.details = details;
        this.pet = Pets.forId(itemId);
        setRun(true);
        if (pet == Pets.TROLL_BABY && owner.getPetManager().getTrollBabyName() != null)
            setName(owner.getPetManager().getTrollBabyName());
        sendMainConfigurations();
    }

    @Override
    public void processNpc() {
        unlockOrb();
        if (pet == Pets.TROLL_BABY || pet.getFood().length > 0) {
            details.updateHunger(0.025);
            owner.getPacketSender().sendString(54234, (int) details.getHunger() + "%");
        }
        if (details.getHunger() >= 90.0 && details.getHunger() < 90.025) {
            owner.getPacketSender().sendMessage("Your pet is starving, feed it before it runs off.", 0xff000);
        } else if (details.getHunger() == 100.0) {
            owner.getPetManager().setNpcId(-1);
            owner.getPetManager().setItemId(-1);
            owner.setPet(null);
            owner.getPetManager().removeDetails(itemId);
            owner.message("Your pet has ran away to find some food!");
            switchOrb(false);
            owner.getPacketSender().sendTabInterface(GameSettings.SUMMONING_TAB, -1);
            owner.getPacketSender().sendTab(GameSettings.INVENTORY_TAB);
            deregister();
            return;
        }
        if (growthRate > 0.000) {
            details.updateGrowth(growthRate);
            owner.getPacketSender().sendString(54234, (int) details.getGrowth() + "%");
            if (details.getGrowth() == 100.0) {
                growNextStage();
            }
        }
        if (!isWithinDistance(owner, 12)) {
            call();
            return;
        }
        sendFollow();
    }

    /**
     * Grows into the next stage of this pet (if any).
     */
    public void growNextStage() {
        if (details.getStage() == 3) {
            return;
        }
        if (pet == null) {
            return;
        }
        int npcId = pet.getNpcId(details.getStage() + 1);
        if (npcId < 1) {
            return;
        }
        details.setStage(details.getStage() + 1);
        int itemId = pet.getItemId(details.getStage());
        if (pet.getNpcId(details.getStage() + 1) > 0) {
            details.updateGrowth(-100.0);
        }
        owner.getPetManager().setItemId(itemId);
        owner.getPetManager().setNpcId(npcId);
        deregister();
        Pet newPet = new Pet(npcId, itemId, owner, owner, details);
        newPet.growthRate = growthRate;
        owner.setPet(newPet);
        owner.getPacketSender().sendMessage("Your pet has grown larger.", 0xff0000);
        Achievements.finishAchievement(owner, Achievements.AchievementData.GROW_A_PET);
    }

    /**
     * Picks up the pet.
     */
    public void pickup() {
        if (!owner.getInventory().hasRoom()) {
            owner.message("You have no inventory space to pick up your pet.");
            return;
        }
        owner.getInventory().add(new Item(itemId, 1));
        owner.setPet(null);
        owner.getPetManager().setNpcId(-1);
        owner.getPetManager().setItemId(-1);
        switchOrb(false);
        owner.getPacketSender().sendTabInterface(GameSettings.SUMMONING_TAB, -1);
        owner.getPacketSender().sendTab(GameSettings.INVENTORY_TAB);
//        owner.getInterfaceManager().removeFamiliarInterface();
//        owner.getPacketSender().sendIComponentSettings(747, 17, 0, 0, 0);
        deregister();
    }

    /**
     * Calls the pet.
     */
    public void call() {
        int sizeX = getSizeX();
        int sizeY = getSizeY();
        Position teleTile = null;
        for (int dir = 0; dir < checkNearDirs[0].length; dir++) {
            final Position tile = new Position(new Position(owner.getX() + checkNearDirs[0][dir], owner.getY() + checkNearDirs[1][dir], owner.getZ()));
            if (World.isTileFree(tile.getX(), tile.getY(), tile.getZ(), sizeX, sizeY)) {
                teleTile = tile;
                break;
            }
        }
        GameWorld.schedule(1, () -> getUpdateFlags().add(Flag.ENTITY_INTERACTION));
        if (teleTile == null)
            return;
        moveTo(teleTile);
    }

    private void sendFollow() {
        if (getDirection().getInteracting() != owner)
            getDirection().setInteracting(owner);
        if (getCombat().getFreezeDelay() >= System.currentTimeMillis())
            return;
        int sizeX = getSizeX();
        int sizeY = getSizeY();
        int targetSizeX = owner.getSizeX();
        int targetSizeY = owner.getSizeY();
        if (Misc.colides(getX(), getY(), sizeX, sizeY, owner.getX(), owner.getY(), targetSizeX, targetSizeY) && !owner.getMovement().hasWalkSteps()) {
            getMovement().reset();
            if (!getMovement().addWalkSteps(owner.getX() + targetSizeX, getY())) {
                getMovement().reset();
                if (!getMovement().addWalkSteps(owner.getX() - sizeX, getY())) {
                    getMovement().reset();
                    if (!getMovement().addWalkSteps(getX(), owner.getY() + targetSizeY)) {
                        getMovement().reset();
                        if (!getMovement().addWalkSteps(getX(), owner.getY() - sizeY)) {
                            return;
                        }
                    }
                }
            }
            return;
        }
        getMovement().reset();
        if (!getCombat().clippedProjectile(owner, true) || !Misc.isOnRange(getX(), getY(), sizeX, sizeY, owner.getX(), owner.getY(), targetSizeX, targetSizeY, 0))
            getMovement().addWalkStepsInteract(owner.getX(), owner.getY(), 2, sizeX, sizeY, true);

    }

    /**
     * Sends the main configurations for the Pet interface (+ summoning orb).
     */
    public void sendMainConfigurations() {
        switchOrb(true);
        String name = getDefinition().getName().replaceAll("_", " ").toLowerCase();
        owner.getPacketSender().sendString(54223, name.substring(0, 1).toUpperCase() + name.substring(1));
        owner.getPacketSender().sendTabInterface(GameSettings.SUMMONING_TAB, 54200);
        owner.getPacketSender().sendNpcHeadOnInterface(getId(), getRevision(), 54221);
//        owner.getVarsManager().sendVarOld(448, itemId);// configures
//        owner.getPacketSender().sendCSVarInteger(1436, 0);
        unlockOrb(); // temporary
    }

    /**
     * Sends the follower details.
     */
    public void sendFollowerDetails() {
//        owner.getVarsManager().sendVarBitOld(4285, (int) details.getGrowth());
//        owner.getVarsManager().sendVarBitOld(4286, (int) details.getHunger());
//        owner.getInterfaceManager().setFamiliarInterface(662);
        unlockInterface();
    }

    /**
     * Switch the Summoning orb state.
     *
     * @param enable
     *            If the orb should be enabled.
     */
    public void switchOrb(boolean enable) {
        owner.getPacketSender().sendOrb(3, enable);
        if (enable) {
            unlockInterface();
            return;
        }
        lockOrb();
    }

    /**
     * Unlocks the orb.
     */
    public void unlockOrb() {
//        owner.getPacketSender().sendHideIComponent(747, 9, false);
        Familiar.Companion.sendLeftClickOption(owner);
    }

    /**
     * Unlocks the interfaces.
     */
    public void unlockInterface() {
//        owner.getPacketSender().sendHideIComponent(747, 9, false);
    }

    /**
     * Locks the orb.
     */
    public void lockOrb() {
//        owner.getPacketSender().sendHideIComponent(747, 9, true);
    }

    /**
     * Gets the details.
     *
     * @return The details.
     */
    public PetDetails getDetails() {
        return details;
    }

    /**
     * Gets the growthRate.
     *
     * @return The growthRate.
     */
    public double getGrowthRate() {
        return growthRate;
    }

    /**
     * Sets the growthRate.
     *
     * @param growthRate
     *            The growthRate to set.
     */
    public void setGrowthRate(double growthRate) {
        this.growthRate = growthRate;
    }

    /**
     * Gets the item id of the pet.
     *
     * @return The item id.
     */
    public int getItemId() {
        return itemId;
    }
}
