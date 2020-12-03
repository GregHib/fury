package com.fury.network.packet.impl;

import com.fury.game.content.global.Achievements;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.content.Music;
import com.fury.network.packet.Packet;
import com.fury.network.packet.PacketConstants;
import com.fury.network.packet.PacketListener;

import java.util.ArrayList;

/**
 * Created by Greg on 02/08/2016.
 */
public class PlaylistPacketListener implements PacketListener {
    @Override
    public void handleMessage(Player player, Packet packet) {
        int code = packet.getOpcode();
        switch (code) {
            case PacketConstants.PLAYLIST_COMMAND_OPCODE:
                handlePlaylist(player, packet);
                break;
        }
    }

    private void handlePlaylist(Player player, Packet packet) {
        int index = packet.readShort();
        int songId = packet.readShort();
        if(songId == -1) {
            player.setPlaylist(new ArrayList<>());
        } else if(songId == 0) {
            if(index >= 0 && index < 30)
                player.removePlaylistSong(index);
        } else if(songId <= Music.Songs.values().length) {
            player.addPlaylistSong(songId);
            Achievements.finishAchievement(player, Achievements.AchievementData.ADD_A_SONG_TO_PLAYLIST);
        }
    }
}
