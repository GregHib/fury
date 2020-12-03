package com.fury.network.packet.impl;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.content.dialogue.impl.quests.tutorial.SkipTutorialD;
import com.fury.game.entity.character.player.content.PlayerPanel;
import com.fury.game.world.GameWorld;
import com.fury.game.world.update.flag.Flag;
import com.fury.game.world.update.flag.block.Appearance;
import com.fury.game.world.update.flag.block.Gender;
import com.fury.network.packet.Packet;
import com.fury.network.packet.PacketListener;
import com.fury.util.FontUtils;

public class ChangeAppearancePacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        try {
            boolean isMale = packet.readByte() == 0;
            final int[] appearances = new int[7];
            final int[] colours = new int[ALLOWED_COLORS.length];
            int currentPartIndex = 1;
            for (int i = 0; i < appearances.length; i++, currentPartIndex++)
                appearances[i] = packet.readShort();
            for (int i = 0; i < colours.length; i++, currentPartIndex++) {
                int value = packet.readShort();
                if (value < ALLOWED_COLORS[i][0] || value > ALLOWED_COLORS[i][1])
                    value = ALLOWED_COLORS[i][0];
                colours[i] = value;
            }

            if (player.getAppearance().canChangeAppearance() && player.getInterfaceId() > 0) {
                //Appearance looks
                player.getAppearance().set(Appearance.GENDER, isMale ? 0 : 1);
                player.getAppearance().setGender(isMale ? Gender.MALE : Gender.FEMALE);
                player.getAppearance().set(Appearance.HEAD, appearances[0]);
                player.getAppearance().set(Appearance.CHEST, appearances[2]);
                player.getAppearance().set(Appearance.ARMS, appearances[3]);
                player.getAppearance().set(Appearance.HANDS, appearances[4]);
                player.getAppearance().set(Appearance.LEGS, appearances[5]);
                player.getAppearance().set(Appearance.FEET, appearances[6]);
                player.getAppearance().set(Appearance.BEARD, appearances[1]);

                //Colors
                player.getAppearance().set(Appearance.HAIR_COLOUR, colours[0]);
                player.getAppearance().set(Appearance.SKIN_COLOUR, colours[1]);
                player.getAppearance().set(Appearance.TORSO_COLOUR, colours[2]);
                player.getAppearance().set(Appearance.LEG_COLOUR, colours[3]);
                player.getAppearance().set(Appearance.FEET_COLOUR, colours[4]);

                player.getUpdateFlags().add(Flag.APPEARANCE);
            }
        } catch (Exception e) {
            player.getAppearance().set();
            e.printStackTrace();
        }
        player.getPacketSender().sendInterfaceRemoval();
        player.getAppearance().setCanChangeAppearance(false);
        if (player.newPlayer()) {
            if (player.getSave().isDisabled()) {
                player.getSave().setDisabled(false);
                GameWorld.sendBroadcast(FontUtils.imageTags(535) + " " + player.getUsername() + " has just joined, please give them a warm welcome! ", 0x008fb2);
            }
            PlayerPanel.refreshPanel(player);
            player.getDialogueManager().startDialogue(new SkipTutorialD(), true);
        }
        player.save();
    }

    private static final int[][] ALLOWED_COLORS = {
            {0, 11}, // hair color
            {0, 7}, // skin color
            {0, 15}, // torso color
            {0, 15}, // legs color
            {0, 5} // feet color
    };
}
