package com.fury.game.content.dialogue.impl.npcs;

import com.fury.game.content.global.Achievements;
import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.skill.Skill;
import com.fury.game.entity.character.player.link.transportation.TeleportHandler;
import com.fury.game.entity.character.player.link.transportation.TeleportType;
import com.fury.game.world.map.Position;

/**
 * Created by Greg on 03/12/2016.
 */
public class SpiritTreeTravelD extends Dialogue {
    @Override
    public void start() {
        player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Strykewyrms", "Chaos Dwarf Battlefield", "Mos Le'Harmless", "Edimmu City", "Cancel");
    }

    @Override
    public void run(int optionId) {
        if (stage == -1) {
            if (optionId == DialogueManager.OPTION_1) {
                stage = 0;
                player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Jungle Strykewyrm", "Desert Strykewyrm", "Ice Strykewyrm", "Return");
            } else if (optionId == DialogueManager.OPTION_2) {
                TeleportHandler.teleportPlayer(player, new Position(1520, 4704), TeleportType.SPIRIT_TREE);
                Achievements.finishAchievement(player, Achievements.AchievementData.TELEPORT_SPIRIT_TREE);
                end();
            } else if (optionId == DialogueManager.OPTION_3) {
                TeleportHandler.teleportPlayer(player, new Position(3746, 9374), TeleportType.SPIRIT_TREE);
                Achievements.finishAchievement(player, Achievements.AchievementData.TELEPORT_SPIRIT_TREE);
                end();
            } else if (optionId == DialogueManager.OPTION_4) {
                if (player.getSkills().hasRequirement(Skill.DUNGEONEERING, 95, "fight a Edimmu."))
                    TeleportHandler.teleportPlayer(player, new Position(3655, 3349), TeleportType.SPIRIT_TREE);

                end();
            } else
                end();
        } else if (stage == 0) {
            end();
            if (optionId == DialogueManager.OPTION_1) {
                TeleportHandler.teleportPlayer(player, new Position(2463, 2907), TeleportType.SPIRIT_TREE);
                Achievements.finishAchievement(player, Achievements.AchievementData.TELEPORT_SPIRIT_TREE);
            } else if (optionId == DialogueManager.OPTION_2) {
                TeleportHandler.teleportPlayer(player, new Position(3373, 3157), TeleportType.SPIRIT_TREE);
                Achievements.finishAchievement(player, Achievements.AchievementData.TELEPORT_SPIRIT_TREE);
            } else if (optionId == DialogueManager.OPTION_3) {
                TeleportHandler.teleportPlayer(player, new Position(3435, 5646), TeleportType.SPIRIT_TREE);
                Achievements.finishAchievement(player, Achievements.AchievementData.TELEPORT_SPIRIT_TREE);
            } else if (optionId == DialogueManager.OPTION_4) {
                player.getDialogueManager().startDialogue(new SpiritTreeTravelD());
            }
        }
    }
}