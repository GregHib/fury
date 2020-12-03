package com.fury.game.system.files.loaders.item;

import com.fury.cache.Revision;
import com.fury.cache.def.Loader;
import com.fury.game.GameSettings;
import com.fury.game.system.files.Resources;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * This file manages every item combatDefinition, which includes
 * their name, description, value, skill requirements, etc.
 *
 * @author relex lawl
 */

public class ItemDefinitions {

    /**
     * The directory in which item definitions are found.
     */
    private static final int MAX_AMOUNT_OF_ITEMS = Loader.getTotalItems(Revision.RS3);
    private static ItemDefinitions[] definitions = new ItemDefinitions[MAX_AMOUNT_OF_ITEMS];
    public static final String UTF8_BOM = "\uFEFF";

    public static int getMaxAmountOfItems() {
        return MAX_AMOUNT_OF_ITEMS;
    }

    /**
     * Loading all item definitions
     */
    public static void init() {
        long startup = System.currentTimeMillis();
        ItemDefinitions definition = definitions[0];

        try {
//            FileWriter out = new FileWriter("C:\\Users\\Greg\\Desktop\\fury\\fury-server\\src\\main\\test\\com\\fileserver\\items.txt");
            File file = new File(Resources.getFile("items"));
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("inish")) {
                    definitions[definition.id] = definition;
//                    out.write(line + "\n\n");
                    continue;
                }
                String[] args = line.split(": ");
                if (args.length <= 1)
                    continue;
                String token = args[0], value = args[1];
                switch (token.toLowerCase()) {
                    case "id":
                        int id = Integer.valueOf(value);
                        definition = new ItemDefinitions();
                        definition.setDefaults();
                        definition.id = id;
//                        out.write(line + "\n");
                        break;
                    case "name":
                        if (value == null)
                            continue;
                        definition.name = value;
//                        out.write(line + "\n");
                        break;
                    case "value":
                        int price = Integer.valueOf(value);
                        definition.value = price;
//                        out.write(line + "\n");
                        break;
                    case "type":
                        definition.equipmentType = EquipmentType.valueOf(value);
//                        out.write(line + "\n");
                        break;
                }
            }
//            out.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(GameSettings.DEBUG)
            System.out.println("Loaded item definitions " + (System.currentTimeMillis() - startup) + "ms");
    }


    private int id = 0;
    private String name = "None";
    private int value = 1;
    private EquipmentType equipmentType = EquipmentType.WEAPON;

    public void setDefaults() {
        id = 0;
        name = "";
        value = 1;
        equipmentType = EquipmentType.WEAPON;
    }

    public int getValue() {
        return value;
    }

    public boolean isFullBody() {
        return equipmentType.equals(EquipmentType.PLATEBODY);
    }

    public boolean isBody() {
        return equipmentType.equals(EquipmentType.BODY);
    }

    public boolean isFullHelm() {
        return equipmentType.equals(EquipmentType.FULL_HELMET);
    }

    public boolean isHelm() {
        return equipmentType.equals(EquipmentType.HELMET);
    }

    public enum EquipmentType {
        HAT,
        CAPE,
        SHIELD,
        GLOVES,
        BOOTS,
        AMULET,
        RING,
        ARROWS,
        FULL_MASK,
        HELMET,
        FULL_HELMET,
        BODY,
        PLATEBODY,
        LEGS,
        WEAPON,
        AURA;
    }

    public static ItemDefinitions forId(int id) {
        return (id < 0 || id >= definitions.length || definitions[id] == null) ? new ItemDefinitions() : definitions[id];
    }

    @Override
    public String toString() {
        return "[ItemDefinition(" + id + ")] - Name: " + name + "; value: " + value + ";";
    }
}
