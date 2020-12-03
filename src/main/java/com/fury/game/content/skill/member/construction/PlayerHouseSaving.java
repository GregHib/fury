package com.fury.game.content.skill.member.construction;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.system.files.Resources;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Chris on 2/10/2017.
 */
public class PlayerHouseSaving {

    public static void saveHouse(Player player) {
        String playerName = player.getUsername();
        File houseFile = new File(Resources.getSaveDirectory("construction") + playerName + ".house");

        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(houseFile));
            out.writeObject(player.getHouse());
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
