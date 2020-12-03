package com.fury.game.entity.character.player.content.objects;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.object.GameObject;

public interface PrivateObjectProcessEvent {

    void spawnObject(Player player, GameObject object);

    void process(Player player, GameObject currentObject);

    void destroyObject(Player player, GameObject object);
    
}
