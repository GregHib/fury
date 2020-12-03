package com.fury.game.system.files.loaders.item;

import com.fury.game.entity.character.combat.equipment.weapon.CombatSpecial;
import com.fury.game.entity.character.combat.equipment.weapon.FightType;
import com.fury.game.entity.character.combat.equipment.weapon.WeaponInterface;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import com.fury.util.JsonLoader;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A static utility class that displays holds and displays data for weapon
 * interfaces.
 *
 * @author lare96
 */
public final class WeaponInterfaces {

    /**
     * A map of items and their respective interfaces.
     */
    private static Map<Integer, WeaponInterface> interfaces = new HashMap<>(500);

    /**
     * Assigns an interface to the combat sidebar based on the argued weapon.
     *
     * @param player the player that the interface will be assigned for.
     * @param item   the item that the interface will be chosen for.
     */
    public static void assign(Player player, Item item) {
        WeaponInterface weapon;

        if (item == null || item.getId() == -1) {
            weapon = WeaponInterface.UNARMED;
        } else {
            weapon = interfaces.get(item.getId());
        }

        if (weapon == null)
            weapon = WeaponInterface.UNARMED;

        if (weapon == WeaponInterface.UNARMED) {
            player.getPacketSender().sendTabInterface(0, weapon.getInterfaceId());
            player.setWeapon(WeaponInterface.UNARMED);
        } else if (weapon == WeaponInterface.CROSSBOW) {
            player.getPacketSender().sendString(weapon.getNameLineId() - 1, "Weapon: ");
        } else if (weapon == WeaponInterface.WHIP) {
            player.getPacketSender().sendString(weapon.getNameLineId() - 1, "Weapon: ");
        }
        player.getPacketSender().sendItemOnInterface(weapon.getInterfaceId() + 1, 200, item.getId());
        player.getPacketSender().sendTabInterface(0, weapon.getInterfaceId());
        if(weapon == WeaponInterface.UNARMED)
            player.getPacketSender().sendString(weapon.getNameLineId(), "Unarmed");
        else
            player.getPacketSender().sendString(weapon.getNameLineId(), item.getName());
        player.setWeapon(weapon);
        CombatSpecial.assign(player);
        CombatSpecial.updateBar(player);

        for (FightType type : weapon.getFightTypes()) {
            if (type.getStyle() == player.getFightType().getStyle()) {
                player.setFightType(type);
                player.getCombatDefinitions().setAttackStyle(player.getFightType().getChildId());
                player.getPacketSender().sendConfig(player.getFightType().getParentId(), player.getFightType().getChildId());
                return;
            }
        }

        player.setFightType(player.getWeapon().getFightTypes()[0]);
        player.getCombatDefinitions().setAttackStyle(player.getFightType().getChildId());
        player.getPacketSender().sendConfig(player.getFightType().getParentId(), player.getFightType().getChildId());
    }

    /**
     * Prepares the dynamic json loader for loading weapon interfaces.
     *
     * @return the dynamic json loader.
     * @throws Exception if any errors occur while preparing for load.
     */
    public static JsonLoader parseInterfaces() {
        return new JsonLoader() {
            @Override
            public void load(JsonObject reader, Gson builder) {
                int id = reader.get("item-id").getAsInt();
                WeaponInterface animation = builder.fromJson(reader.get("interface"), WeaponInterface.class);
                interfaces.put(id, animation);
            }

            @Override
            public String filePath() {
                return "./data/def/json/weapon_interfaces.json";
            }
        };
    }
}
