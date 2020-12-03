package com.fury.game.content.dialogue;

import com.fury.cache.Revision;
import com.fury.cache.def.Loader;
import com.fury.cache.def.npc.NpcDefinition;
import com.fury.game.content.dialogue.chain.impl.*;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;

/**
 * Created by Greg on 14/11/2016.
 */
public abstract class Dialogue {

    protected Player player;
    protected int stage = -1;
    public Object[] parameters;
    public static final int OPTION_1 = 0;
    public static final int OPTION_2 = 1;
    public static final int OPTION_3 = 2;
    public static final int OPTION_4 = 3;
    public static final int OPTION_5 = 4;

    public Dialogue() {}

    public abstract void start();

    public abstract void run(int optionId);

    public void finish() {

    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    protected final void end() {
        player.getDialogueManager().finishDialogue();
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void sendInfo(String title, String...lines) {
        player.getDialogueFactory().sendInfoBox(new InfoDialogue(title, lines));
    }


    protected void start(Dialogue dialogue, Object... parameters) {
        player.getDialogueManager().startDialogue(dialogue, parameters);
    }

    public void sendNpc(int id, String...lines) {
        sendNpc(id, Revision.RS2, Expressions.NORMAL, lines);
    }

    public void sendNpc(int id, Expressions expression, String...lines) {
        sendNpc(id, Revision.RS2, expression, lines);
    }

    public void sendNpc(Mob mob, String...lines) {
        sendNpc(mob.getDefinition(), Expressions.NORMAL, lines);
    }

    public void sendNpc(Mob mob, Expressions expression, String...lines) {
        sendNpc(mob.getDefinition(), expression, lines);
    }

    public void sendNpc(int id, Revision revision, Expressions expression, String...lines) {
        sendNpc(Loader.getNpc(id, revision), expression, lines);
    }

    public void sendNpc(NpcDefinition definition, Expressions expression, String...lines) {
        player.getDialogueFactory().sendNpcChat(new NpcDialogue(definition, expression, lines));
    }

    public void sendItem(Item item, String title, String context) {
        player.getDialogueFactory().sendItem(new ItemDialogue(title, context, item));
    }

    public void sendStatement(String...options) {
        player.getDialogueFactory().sendStatement(new StatementDialogue(options));
    }

    public void sendOption(String title, String...options) {
        player.getDialogueFactory().sendOptions(new OptionDialogue(title, options));
    }

    public void sendOptions(String...options) {
        player.getDialogueFactory().sendOptions(new OptionDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, options));
    }

    public void sendPlayerChat(Expressions expression, String...text) {
        player.getDialogueFactory().sendPlayerChat(new PlayerDialogue(expression, text));
    }
}
