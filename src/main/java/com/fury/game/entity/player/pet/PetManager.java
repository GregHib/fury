package com.fury.game.entity.player.pet;

import com.fury.cache.def.Loader;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.content.skill.member.summoning.impl.Pet;
import com.fury.game.content.skill.member.summoning.impl.PetDetails;
import com.fury.game.content.skill.member.summoning.impl.Pets;
import com.fury.core.model.item.Item;
import com.fury.game.system.files.loaders.item.ItemConstants;

import java.util.HashMap;
import java.util.Map;

public class PetManager {
    private final Map<Integer, PetDetails> petDetails = new HashMap<>();

    /**
     * The player.
     */
    private Player player;

    /**
     * The current Npc id.
     */
    private int npcId;

    /**
     * The current item id.
     */
    private int itemId;

    /**
     * The troll baby's name (if any).
     */
    private String trollBabyName;

    /**
     * Constructs a new {@code PetManager} {@code Object}.
     */
    public PetManager(Player player) {
		this.player = player;
    }

    /**
     * Spawns a pet.
     *
     * @param itemId
     *            The item id.
     * @param deleteItem
     *            If the item should be removed.
     * @return {@code True} if we were dealing with a pet item id.
     */
    public boolean spawnPet(int itemId, boolean deleteItem) {
        Pets pets = Pets.forId(itemId);
        if (pets == null)
            return false;
        else if (player.getPet() != null || player.getFamiliar() != null) {
            player.message("You already have a follower.");
            return true;
        } else if (!hasRequirements(pets)) {
            return true;
        }
        int baseItemId = pets.getBabyItemId();
        PetDetails details = petDetails.get(baseItemId);
        if (details == null) {
            details = new PetDetails(pets.getGrowthRate() == 0.0 ? 100.0 : 0.0);
            petDetails.put(baseItemId, details);
        }
        int id = pets.getItemId(details.getStage());
        if (itemId != id) {
            player.message("This is not the right pet, grow the pet correctly.");
            return true;
        }
        int npcId = pets.getNpcId(details.getStage());
        if (npcId > 0) {
            Pet pet = new Pet(npcId, itemId, player, player, details);
            this.npcId = npcId;
            this.itemId = itemId;
            pet.setGrowthRate(pets.getGrowthRate());
            player.setPet(pet);
            if (deleteItem) {
                player.animate(827);
                player.getInventory().delete(new Item(itemId));
            }
            return true;
        }
        return true;
    }

    /**
     * Checks if the player has the requirements for the pet.
     *
     * @param pet
     *            The pet.
     * @return {@code True} if so.
     */
    private boolean hasRequirements(Pets pet) {
        switch (pet) {
            case TZREK_JAD:
                /*if (!player.isCompletedFightCaves()) {
                    player.message("You need to complete at least one fight cave minigame to use this pet.");
                    return false;
                }*/
                /*if (!player.isWonFightPits()) {
                    player.message("You need to win at least one fight pits minigame to use this pet.");
                    return false;
                }*/
                return true;
            case SARADOMIN_OWL:
            case GUTHIX_RAPTOR:
            case ZAMORAK_HAWK:
            case VULTURE_1:
            case VULTURE_2:
            case VULTURE_3:
            case VULTURE_4:
            case VULTURE_5:
            case CHAMELEON:
                return true;
            case BABY_DRAGON_1:
            case BABY_DRAGON_2:
            case BABY_DRAGON_3:
            case SEARING_FLAME:
            case GLOWING_EMBER:
            case TWISTED_FIRESTARTER:
            case WARMING_FLAME:
                return true;
            default:
                return true;
        }
    }

    /**
     * Initializes the pet manager.
     */
    public void init() {
        if (npcId > 0 && itemId > 0) {
            spawnPet(itemId, false);
        }
    }

    /**
     * Makes the pet eat.
     *
     * @param foodId
     *            The food item id.
     * @param npc
     *            The pet Npc.
     */
    public void eat(int foodId, Pet npc) {
        Pets pets = Pets.forId(itemId);
        if (pets == null) {
            return;
        }
        if (pets == Pets.TROLL_BABY) {
            if (!ItemConstants.isTradeable(new Item(foodId))) {
                npc.forceChat("I no like that!");
                return;
            }
            if (trollBabyName == null) {
                trollBabyName = Loader.getItem(foodId).getName();
                npc.setName(trollBabyName);
                npc.forceChat("YUM! Me likes " + trollBabyName + "!");
            }
            player.getInventory().delete(new Item(foodId, 1));
            npc.getDetails().updateHunger(-15.0);
            player.message("Your pet happily eats the " + Loader.getItem(foodId).getName() + ".");
            return;
        }
        for (int food : pets.getFood()) {
            if (food == foodId) {
                player.getInventory().delete(new Item(food, 1));
                player.message("Your pet happily eats the " + Loader.getItem(food).getName() + ".");
                player.animate(827);
                npc.getDetails().updateHunger(-15.0);
                return;
            }
        }
        player.message("Nothing interesting happens.");
    }

    /**
     * Removes the details for this pet.
     *
     */
    public void removeDetails(int itemId) {
        Pets pets = Pets.forId(itemId);
        if (pets == null) {
            return;
        }
        petDetails.remove(pets.getBabyItemId());
    }

    public int getNpcId() {
        return npcId;
    }

    public void setNpcId(int npcId) {
        this.npcId = npcId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    /**
     * Gets the trollBabyName.
     *
     * @return The trollBabyName.
     */
    public String getTrollBabyName() {
        return trollBabyName;
    }

    /**
     * Sets the trollBabyName.
     *
     * @param trollBabyName
     *            The trollBabyName to set.
     */
    public void setTrollBabyName(String trollBabyName) {
        this.trollBabyName = trollBabyName;
    }

    public Map<Integer, PetDetails> getPetDetails() {
        return petDetails;
    }
}
