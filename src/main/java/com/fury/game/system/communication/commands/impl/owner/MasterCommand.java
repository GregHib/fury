package com.fury.game.system.communication.commands.impl.owner;

import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.Skills;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.system.communication.commands.Command;
import com.fury.game.world.update.flag.Flag;

import java.util.regex.Pattern;

public class MasterCommand implements Command {
    @Override
    public Pattern pattern() {
        return null;
    }

    @Override
    public String prefix() {
        return "master";
    }

    @Override
    public String format() {
        return null;
    }

    @Override
    public void process(Player player, String... values) {
        for (Skill skill : Skill.values()) {
            int level = Skills.getMaxAchievingLevel(skill);
            player.getSkills().setLevel(skill, level);
            player.getSkills().setMaxLevel(skill, level);
            player.getSkills().setExperience(skill, 200000000);
        }
        player.getDungManager().setMaxComplexity(6);
        player.getDungManager().setMaxFloor(60);
        player.message("You are now a master of all skills.");
        player.getUpdateFlags().add(Flag.APPEARANCE);
    }

    @Override
    public boolean rights(Player player) {
        return player.getRights().isOrHigher(PlayerRights.OWNER);
    }
}
