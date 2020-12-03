package com.fury.game.entity.character.player.actions;

import com.fury.game.container.impl.equip.Slot;
import com.fury.game.content.global.action.Action;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.system.files.loaders.item.WeaponAnimations;
import com.fury.game.world.update.flag.Flag;

public class ItemMorph extends Action {

    private int id;

    public ItemMorph(int npcId) {
        id = npcId;
    }

    @Override
    public boolean start(Player player) {
        player.getMovement().reset();
        player.setTransformation(id);
        player.getPacketSender().sendInterfaceSet(-1, 6014);
        player.getCharacterAnimations().setStandingAnimation(-1);
        player.getCharacterAnimations().setRunningAnimation(-1);
        player.getCharacterAnimations().setWalkingAnimation(-1);
        player.getMovement().lock();
        return true;
    }

    @Override
    public boolean process(Player player) {
        return true;
    }

    @Override
    public int processWithDelay(Player player) {
        return 0;
    }

    @Override
    public void stop(Player player) {
        player.resetTransformation();
        player.getEquipment().unequip(player.getEquipment().get(Slot.HEAD), Slot.HEAD.ordinal());
        player.getPacketSender().sendInterfaceRemoval();
        player.getCharacterAnimations().reset();
        WeaponAnimations.update(player);
        player.getUpdateFlags().add(Flag.APPEARANCE);
        player.getMovement().unlock();
    }
}
