package com.fury.game.content.dialogue;

import com.fury.cache.Revision;
import com.fury.cache.def.Loader;
import com.fury.cache.def.npc.NpcDefinition;
import com.fury.core.model.node.entity.actor.figure.player.Player;

import java.security.InvalidParameterException;

/**
 * Manages dialogues.
 *
 * @author Greg
 */

public class DialogueManager {

    public static final String DEFAULT_OPTIONS_TITLE = "Select an option";
    public static final int OPTION_CONTINUE = -1;
    public static final int OPTION_1 = 0;
    public static final int OPTION_2 = 1;
    public static final int OPTION_3 = 2;
    public static final int OPTION_4 = 3;
    public static final int OPTION_5 = 4;

    private Dialogue plugin;
    private Player player;

    public DialogueManager(Player player) {
        this.player = player;
    }

    private static final int[] NPC_DIALOGUE_ID = { 4885, 4890, 4896, 4903 };
    private static final int[] PLAYER_DIALOGUE_ID = { 971, 976, 982, 989 };
    private static final int[] OPTION_DIALOGUE_ID = { 13760, 2461, 2471, 2482, 2494 };
    private static final int[] STATEMENT_DIALOGUE_ID = { 356, 359, 363, 368, 374 };

    public void sendStatement(String statement) {
        player.getPacketSender().sendString(357, statement);
        player.getPacketSender().sendString(358, "Click here to continue");
        player.getPacketSender().sendChatboxInterface(356);
    }

    public void startDialogue(Dialogue plugin, Object... parameters) {
        if (!player.getControllerManager().useDialogueScript(plugin))
            return;
        if (this.plugin != null)
            this.plugin.finish();
        this.plugin = plugin;
        if (this.plugin == null)
            return;
        this.plugin.setParameters(parameters);
        this.plugin.setPlayer(player);
        this.plugin.start();
    }

    public void continueDialogue(int interfaceId) {
        if (plugin == null && interfaceId == -1) {
            player.getPacketSender().sendInterfaceRemoval();
            return;
        }
        if (plugin == null)
            return;
        //Would normally be the parentInterfaceId and the childPosition in parent array parameters
        //But that would require a decent client side rewrite
        int option;
        switch (interfaceId) {
            //First option of
            case 2494://Five
            case 2482://Four
            case 2471://Three
            case 2461://Two
                option = 0;
                break;
            //Second option of
            case 2495://Five
            case 2483://Four
            case 2472://Three
            case 2462://Two
                option = 1;
                break;
            //Third option of
            case 2496://Five
            case 2484://Four
            case 2473://Three
                option = 2;
                break;
            //Fourth option of
            case 2497://Five
            case 2485://Four
                option = 3;
                break;
            //Fifth option of
            case 2498://Five
                option = 4;
                break;
            default://Continue
                option = -1;
                break;
        }
        this.plugin.run(option);
    }

    public void continueCustomDialogue(int componentID) {
        if (plugin == null)
            return;
        this.plugin.run(componentID);
    }

    public void runDialogue(int option) {
        if (plugin == null)
            return;

        this.plugin.run(option);
    }

    public void finishDialogue() {
        if (plugin == null)
            return;
        plugin.finish();
        plugin = null;
        player.getPacketSender().sendInterfaceRemoval();
    }

    public void finishHouseDialogue() {
        if (plugin == null)
            return;
        plugin.finish();
        plugin = null;
    }

    private void sendEntityDialogue(DialogueType type, String title, int entityId, Revision revision, int animationId, String... lines) {
        switch (type) {
            case NPC_STATEMENT:
                int startDialogueChildId = NPC_DIALOGUE_ID[lines.length - 1];
                int headChildId = startDialogueChildId - 2;
                player.getPacketSender().sendNpcHeadOnInterface(entityId, revision, headChildId);
                player.getPacketSender().sendInterfaceAnimation(headChildId, animationId, Revision.RS2);
                NpcDefinition npcDefinition = Loader.getNpc(entityId, revision);
                player.getPacketSender().sendString(startDialogueChildId - 1, npcDefinition.getName().replaceAll("_", " "));
                for (int i = 0; i < lines.length; i++)
                    player.getPacketSender().sendString(startDialogueChildId + i, lines[i]);
                player.getPacketSender().sendChatboxInterface(startDialogueChildId - 3);
                break;
            case PLAYER_STATEMENT:
                startDialogueChildId = PLAYER_DIALOGUE_ID[lines.length - 1];
                headChildId = startDialogueChildId - 2;
                player.getPacketSender().sendPlayerHeadOnInterface(headChildId);
                player.getPacketSender().sendInterfaceAnimation(headChildId, animationId, Revision.RS2);
                player.getPacketSender().sendString(startDialogueChildId - 1, player.getUsername());
                for (int i = 0; i < lines.length; i++)
                    player.getPacketSender().sendString(startDialogueChildId + i, lines[i]);
                player.getPacketSender().sendChatboxInterface(startDialogueChildId - 3);
                break;
            case ITEM_STATEMENT:
                startDialogueChildId = NPC_DIALOGUE_ID[lines.length - 1];//TODO need correct id's
                headChildId = startDialogueChildId - 2;
                player.getPacketSender().sendInterfaceModel(headChildId, entityId, revision, 180);
                player.getPacketSender().sendString(startDialogueChildId - 1, title);
                for (int i = 0; i < lines.length; i++)
                    player.getPacketSender().sendString(startDialogueChildId + i, lines[i]);
                player.getPacketSender().sendChatboxInterface(startDialogueChildId - 3);
                break;
            case STATEMENT:
                int firstChildId = STATEMENT_DIALOGUE_ID[lines.length - 1];
                for (int i = 0; i < lines.length; i++)
                    player.getPacketSender().sendString(firstChildId + i + 1, lines[i]);
                player.getPacketSender().sendString(firstChildId + lines.length + 1, "Click here to continue");
                player.getPacketSender().sendChatboxInterface(STATEMENT_DIALOGUE_ID[lines.length - 1]);
                break;
            case OPTION:
                firstChildId = OPTION_DIALOGUE_ID[lines.length - 1];
                player.getPacketSender().sendString(firstChildId - 1, title);
                for (int i = 0; i < lines.length; i++)
                    player.getPacketSender().sendString(firstChildId + i, lines[i]);
                player.getPacketSender().sendChatboxInterface(firstChildId - 2);
                if(title.length() > 16) {
                    player.getPacketSender().sendInterfaceDisplayState((firstChildId - 2) + 1 + lines.length + 3, true);
                    player.getPacketSender().sendInterfaceDisplayState((firstChildId - 2) + 1 + lines.length + 3 + (lines.length >= 4 ? 1 : 3), false);
                } else if(title.length() > 0){
                    player.getPacketSender().sendInterfaceDisplayState((firstChildId - 2) + 1 + lines.length + 3, false);
                    player.getPacketSender().sendInterfaceDisplayState((firstChildId - 2) + 1 + lines.length + 3 + (lines.length >= 4 ? 1 : 3), true);
                } else {
                    player.getPacketSender().sendInterfaceDisplayState((firstChildId - 2) + 1 + lines.length + 3, true);
                    player.getPacketSender().sendInterfaceDisplayState((firstChildId - 2) + 1 + lines.length + 3 + (lines.length >= 4 ? 1 : 3), true);
                }
                break;
        }
        if(player.getInterfaceId() <= 0)
            player.setInterfaceId(100);
    }

    public void sendOptionsDialogue(String title, String... options) {
        if (options.length > 5)
            throw new InvalidParameterException("The max options length is 5.");

        int length = options.length > 5 ? 5 : options.length;
        String[] actions = new String[length];
        System.arraycopy(options, 0, actions, 0, length);
        sendEntityDialogue(DialogueType.OPTION, title, -1, null, -1, actions);
    }

    public void sendNPCDialogue(int npcId, Expressions expression, String... text) {//TODO remove
        sendNPCDialogue(npcId, Revision.RS2, expression, text);
    }

    public void sendNPCDialogue(int npcId, Revision revision, Expressions expression, String... text) {//TODO replace with Npc/NpcDef
        sendEntityDialogue(DialogueType.NPC_STATEMENT, npcId, revision, expression, text);
    }

    public void sendItemDialogue(int itemId, String... text) {//TODO remove
        sendItemDialogue(itemId, Revision.RS2, text);
    }

    public void sendItemDialogue(int itemId, Revision revision, String... text) {//TODO replace with Item/itemdef
        sendEntityDialogue(DialogueType.ITEM_STATEMENT, itemId, revision, null, text);
    }

    public void sendPlayerDialogue(Expressions expression, String... text) {
        sendEntityDialogue(DialogueType.PLAYER_STATEMENT, -1, null, expression, text);
    }

    public void sendDialogue(String... texts) {
        sendEntityDialogue(DialogueType.STATEMENT, "", -1, null, -1, texts);
    }

    //TODO replace entityId with Npc/Item Definitions
    public void sendEntityDialogue(DialogueType type, int entityId, Revision revision, Expressions expression, String... text) {
        String title = "";
        if (type == DialogueType.PLAYER_STATEMENT)
            title = player.getUsername();
        else if (type == DialogueType.NPC_STATEMENT)
            title = Loader.forId(entityId).name;
        else if (type == DialogueType.ITEM_STATEMENT)
            title = Loader.getItem(entityId, revision).getName();

        sendEntityDialogue(type, title, entityId, revision, expression == null ? -1 : expression.getAnimationId(), text);
    }

    public void setParameters(Object[] params) {
        if (plugin == null)
            return;
        plugin.setParameters(params);
    }

    public Object[] getParameters() {
        if (plugin == null)
            return null;
        return plugin.getParameters();
    }
}
