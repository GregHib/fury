package com.fury.cache.def.npc;

import com.fury.cache.Revision;
import com.fury.cache.def.Definition;
import com.fury.cache.def.Loader;

import java.util.HashMap;

/**
 * Created by Greg on 04/01/2017.
 */
public abstract class NpcDefinition extends Definition {
    public int id;
    public String name;
    public String description;
    public int size;
    public int combat;
    public String[] options;
    public int[] modelIds;
    public int[] dialogueModels;
    public HashMap<Integer, Object> clientScriptData;
    public Revision revision = Revision.RS2;
    public byte walkMask;
    public int rotation;
    public int respawnDirection;
    public int idleAnimation;
    public int walkAnimation;
    public int runAnimation;

    public NpcDefinition(int id) {
        idleAnimation = -1;
        walkAnimation = -1;
        runAnimation = -1;
        this.id = id;
        name = "";
        description = "";
        size = 1;
        combat = 1;
        options = new String[10];
        walkMask = (byte) 0;
        rotation = 32;
        respawnDirection = (byte) 7;
    }

    public static NpcDefinition get(String name) {
        for(Revision revision : Revision.values()) {
            if(revision == Revision.RS3)
                continue;

            int foundId = -1;
            for (int id = 0; id < Loader.getTotalNpcs(revision); id++) {
                NpcDefinition def = Loader.getNpc(id, revision);
                if (def == null || def.getName() == null || def.getName().isEmpty())
                    continue;
                if (def.getName().equalsIgnoreCase(name)) {
                    if (def.getDescription() != null && !def.getDescription().isEmpty() && def.getDescription().contains("pet"))
                        continue;
                    foundId = id;
                    switch (foundId) {
                        case 2541://Bandos Avatar
                            foundId = 4540;
                            break;
                        case 8776://Hand Cannoneer
                            foundId = 8777;
                            break;
                        case 655://Tree spirit
                            foundId = 4470;
                            break;
                        case 1338://Dagannoth
                            foundId = 1341;
                            break;
                    }
                    break;
                }
            }
            if (foundId != -1)
                return Loader.getNpc(foundId, revision);
        }
        return null;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        if(description.equals(""))
            return "It's a " + name.toLowerCase() + ".";
        return description;
    }

    public int getSize() {
        return size;
    }

    public int getCombat() {
        return combat;
    }

    public String[] getOptions() {
        return options;
    }

    public boolean hasMarkOption() {
        for (String option : options) {
            if (option != null && option.equalsIgnoreCase("mark"))
                return true;
        }
        return false;
    }

    public boolean hasOption(String op) {
        for (String option : options) {
            if (option != null && option.equalsIgnoreCase(op))
                return true;
        }
        return false;
    }

    public boolean hasAttackOption() {
        for (String option : options)
            if (option != null && option.equalsIgnoreCase("attack"))
                return true;
        return false;
    }
}
