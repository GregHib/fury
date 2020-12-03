package com.fury.game.content.skill.member.construction;


import com.fury.cache.Revision;
import com.fury.cache.def.Loader;
import com.fury.game.content.dialogue.impl.skills.construction.CraftTabletD;
import com.fury.game.content.skill.Skill;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import com.fury.game.entity.object.GameObject;
import com.fury.util.Misc;

public class TabletMaking {

    public static void openTabInterface(Player player, GameObject object) {
        HouseConstants.HObject hObject = null;
        for(HouseConstants.HObject hObj : HouseConstants.HObject.values()) {
            if(hObj.getId() == object.getId()) {
                hObject = hObj;
                break;
            }
        }

        if(hObject == null)
            return;

        SpellTablet[] tablets = SpellTablet.getTablets(hObject);

        player.animate(3652);
        player.getTemporaryAttributes().put("tablets", tablets);
        Item[] itemArray = new Item[tablets.length];
        for (int i = 0; i < (tablets.length > 8 ? 8 : tablets.length); i++)
            itemArray[i] = new Item(tablets[i].getItem());
        player.getPacketSender().sendWidgetItems(38274, itemArray);
        handleInterfaceCrosses(player, tablets);
        player.getPacketSender().sendInterface(38272);
        player.getDialogueManager().startDialogue(new CraftTabletD());
    }


    public static void handleInterfaceCrosses(Player player, SpellTablet[] tablets) {
        int frame = 0;
        for (int index = 0; index < tablets.length; index++) {
            SpellTablet tablet = tablets[index];
            player.getPacketSender().sendString(38275 + (frame * 6), Misc.formatName(Loader.getItem(tablet.getItem(), Revision.RS2).getName().toLowerCase().replaceAll("_", " ")));
            player.getPacketSender().sendString(38280 + (frame * 6), "Level: " + tablet.getSpell().getLevel());
            int count = 0;
            boolean canMake = true;
            if(tablet.getSpell().getItems().isPresent()) {
                Item[] req = tablet.getSpell().getItems().get();
                for (int i = 0; i < req.length; i ++) {
                    player.getPacketSender().sendString((38276 + count) + frame * 6, req[i].getAmount() + " x " + req[i].getName());
                    if (player.getInventory().getAmount(req[i]) < req[i].getAmount()) {
                        count++;
                        canMake = false;
                        continue;
                    }
                    count++;
                }
            }

            if (!player.getSkills().hasLevel(Skill.CONSTRUCTION, tablet.getSpell().getLevel()))
                canMake = false;

            if (canMake)
                player.getPacketSender().sendConfig(1000 + frame, 0);
            else
                player.getPacketSender().sendConfig(1000 + frame, 1);
            for (int i2 = count; i2 < 4; i2++) {
                player.getPacketSender().sendString((38276 + i2) + frame * 6, "");
            }
            frame++;
        }
        for (frame = tablets.length; frame < 8; frame++) {
            player.getPacketSender().sendString(38275 + frame * 6, "");
            player.getPacketSender().sendString(38276 + frame * 6, "");
            player.getPacketSender().sendString(38277 + frame * 6, "");
            player.getPacketSender().sendString(38278 + frame * 6, "");
            player.getPacketSender().sendString(38279 + frame * 6, "");
            player.getPacketSender().sendString(38280 + frame * 6, "");
            player.getPacketSender().sendConfig(frame + 1000, 0);
        }
    }

}
