package com.fury.game.entity.character.player.content;

import com.fury.core.model.node.entity.actor.figure.combat.magic.Spell;
import com.fury.game.content.combat.magic.spell.modern.teleport.*;

public enum TeleportSpells {
    VARROCK_TELEPORT(new VarrockTeleport()),
    LUMBRIDGE_TELEPORT(new LumbridgeTeleport()),
    FALADOR_TELEPORT(new FaladorTeleport()),
    CAMELOT_TELEPORT(new CamelotTeleport()),
    ARDOUGNE_TELEPORT(new ArdougneTeleport()),
    WATCHTOWER_TELEPORT(new WatchtowerTeleport()),
    TELEPORT_TO_HOUSE(new TeleportToHouse());

    TeleportSpells(Spell spell) {
        this.spell = spell;
    }

    private Spell spell;

    public Spell getSpell() {
        return spell;
    }
}
