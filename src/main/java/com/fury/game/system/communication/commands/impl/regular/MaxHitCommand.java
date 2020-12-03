package com.fury.game.system.communication.commands.impl.regular;

import com.fury.core.model.node.entity.actor.figure.combat.magic.CombatSpell;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.container.impl.equip.Slot;
import com.fury.game.content.combat.magic.Magic;
import com.fury.game.entity.character.combat.magic.CombatSpells;
import com.fury.game.entity.character.player.actions.PlayerCombatAction;
import com.fury.game.system.communication.commands.Command;

import java.util.regex.Pattern;

public class MaxHitCommand implements Command {

    @Override
    public Pattern pattern() {
        return null;
    }

    @Override
    public String prefix() {
        return "maxhit";
    }

    @Override
    public String format() {
        return null;
    }

    @Override
    public void process(Player player, String... values) {
        CombatSpell spell = player.getCastSpell() != null ? player.getCastSpell() : player.getAutoCastSpell() != null ? player.getAutoCastSpell() : player.getEquipment().get(Slot.WEAPON).getId() == 22494 ? CombatSpells.POLYPORE.spell : null;
        boolean range = PlayerCombatAction.isRanging(player) != 0;
        boolean magic = spell != null;
        int max = PlayerCombatAction.getMaxHit(player, player.getFightType(), range, 1);
        if(magic)
            max = Magic.getMaxHit(player, null, spell);
        player.message((magic ? "Magic" : range? "Ranged" : "Melee") + ": " + max);
    }

    @Override
    public boolean rights(Player player) {
        return true;
    }
}
