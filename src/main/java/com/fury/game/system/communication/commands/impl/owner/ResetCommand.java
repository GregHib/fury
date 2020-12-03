package com.fury.game.system.communication.commands.impl.owner;

import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.Skills;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.system.communication.commands.Command;
import com.fury.game.world.update.flag.Flag;

import java.util.regex.Pattern;

public class ResetCommand implements Command {
    @Override
    public Pattern pattern() {
        return null;
    }

    @Override
    public String prefix() {
        return "reset";
    }

    @Override
    public String format() {
        return null;
    }

    @Override
    public void process(Player player, String... values) {
        for (Skill skill : Skill.values()) {
            int level = skill.equals(Skill.CONSTITUTION) ? 100 : skill.equals(Skill.PRAYER) ? 10 : 1;
            player.getSkills().setLevel(skill, level);
            player.getSkills().setMaxLevel(skill, level);
            player.getSkills().setExperience(skill, Skills.getExperienceForLevel(skill == Skill.CONSTITUTION ? 10 : 1));
        }
        player.message("Your skill levels have now been reset.");
        player.getUpdateFlags().add(Flag.APPEARANCE);
    }

    @Override
    public boolean rights(Player player) {
        return player.getRights().isOrHigher(PlayerRights.OWNER);
    }
}
