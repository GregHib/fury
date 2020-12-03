package com.fury.game.system.communication.commands.impl.owner;

import com.fury.cache.Revision;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.system.communication.commands.Command;
import com.fury.game.world.update.flag.Flag;

import java.util.regex.Pattern;

public class PnpcCommand implements Command {

    static Pattern pattern = Pattern.compile("^(?:pnpc|npcid)\\s(-?\\d+)(?:\\s([0-3]))?$");

    @Override
    public Pattern pattern() {
        return pattern;
    }

    @Override
    public String prefix() {
        return null;
    }

    @Override
    public String format() {
        return "pnpc [npc id] (revision 0-3)";
    }

    @Override
    public void process(Player player, String... values) {
        int npc = Integer.parseInt(values[1]);
        Revision revision = values[2] != null ? Revision.values()[Integer.parseInt(values[2])] : Revision.RS2;
        if(npc < 0)
            player.resetTransformation();
        else
            player.setTransformation(npc, revision);
        player.getUpdateFlags().add(Flag.APPEARANCE);
    }

    @Override
    public boolean rights(Player player) {
        return player.getRights().isOrHigher(PlayerRights.OWNER);
    }
}
