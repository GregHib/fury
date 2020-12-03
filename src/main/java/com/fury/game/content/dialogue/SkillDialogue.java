package com.fury.game.content.dialogue;

import com.fury.game.content.dialogue.input.impl.EnterAmountToMake;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;

/**
 * Created by Greg on 16/11/2016.
 */
public class SkillDialogue {

    public static final String DEFAULT_SKILLS_TITLE = "What would you like to make?";
    private static final int[] SKILL_DIALOGUE_ID = {4429, 8866, 8880, 8899, 8938};
    private static final int[][][] DEFAULT_OFFSETS = {
            {{207, 3}},
            {{92, 3}, {323, 3}},
            {{56, 3}, {207, 3}, {360, 3}},
            {{28, 3}, {148, 3}, {268, 3}, {388, 3}},
            {{14, 3}, {111, 3}, {208, 3}, {305, 3}, {402, 3}}
    };

    public static void sendSkillDialogue(final Player player, String title, Item[] items) {
        sendSkillDialogue(player, title, items, getNames(items), 0, 0, 180);
    }

    public static void sendSkillDialogue(final Player player, String title, Item[] items, int offsetX, int offsetY) {
        sendSkillDialogue(player, title, items, getNames(items), offsetX, offsetY, 180);
    }

    public static void sendSkillDialogue(final Player player, String title, Item[] items, String[] names) {
        sendSkillDialogue(player, title, items, names, 0, 0, 180);
    }

    public static void sendSkillDialogue(final Player player, String title, Item[] items, String[] names, int xOffset, int yOffset, int zoom) {
        int interfaceId = SKILL_DIALOGUE_ID[items.length - 1];

        for (int i = 0; i < items.length; i++) {
            player.getPacketSender().sendInterfaceModel(interfaceId == 4429 ? 1746 : interfaceId + 3 + i, items[i].getId(), items[i].getRevision(), zoom);
            player.getPacketSender().sendString(interfaceId == 4429 ? 2799 : (interfaceId + 6 + items.length) + (i * 4), names[i]);
        }
        player.getPacketSender().sendString(interfaceId + 2 + (items.length * 5) + 1, title);
        player.getPacketSender().sendChatboxInterface(interfaceId);
        for (int i = 0; i < items.length; i++) {
            int defaultX = DEFAULT_OFFSETS[items.length - 1][i][0];
            int defaultY = DEFAULT_OFFSETS[items.length - 1][i][1];
            player.getPacketSender().sendInterfaceComponentPosition(interfaceId, 2 + i, defaultX + xOffset, defaultY + yOffset);
        }

    }


    public static String[] getNames(Item[] items) {
        String[] names = new String[items.length];
        for (int i = 0; i < items.length; i++)
            names[i] = items[i].getName();
        return names;
    }

    public static int getOptionSelected(int button) {
        int option = -1;
        switch (button) {
            case 8946:
            case 8947:
            case 8948:
            case 8949:
            case 8906:
            case 8907:
            case 8908:
            case 8909:
            case 8886:
            case 8887:
            case 8888:
            case 8889:
            case 8871:
            case 8872:
            case 8873:
            case 8874:
            case 3987:
            case 3986:
            case 2807:
            case 2414:
            case 1747:
            case 1748:
            case 2798:
            case 2799:
                option = 0;
                break;
            case 8950:
            case 8951:
            case 8952:
            case 8953:
            case 8910:
            case 8911:
            case 8912:
            case 8913:
            case 8890:
            case 8891:
            case 8892:
            case 8893:
            case 8875:
            case 8876:
            case 8877:
            case 8878:
            case 3991:
            case 3990:
            case 3989:
            case 3988:
                option = 1;
                break;
            case 8954:
            case 8955:
            case 8956:
            case 8957:
            case 8914:
            case 8915:
            case 8916:
            case 8917:
            case 8894:
            case 8895:
            case 8896:
            case 8897:
            case 3995:
            case 3994:
            case 3993:
            case 3992:
                option = 2;
                break;
            case 8958:
            case 8959:
            case 8960:
            case 8961:
            case 8918:
            case 8919:
            case 8920:
            case 8921:
            case 3999:
            case 3998:
            case 3997:
            case 3996:
                option = 3;
                break;
            case 8962:
            case 8963:
            case 8964:
            case 8965:
            case 4003:
            case 4002:
            case 4001:
            case 4000:
                option = 4;
                break;
            case 7441:
            case 7440:
            case 6397:
            case 4158:
                option = 5;
                break;
            case 7446:
            case 7444:
            case 7443:
            case 7442:
                option = 6;
                break;
            case 7450:
            case 7449:
            case 7448:
            case 7447:
                option = 7;
                break;
        }
        return option;
    }

    private static int getAmountSelected(int button) {
        int amount = -1;
        switch (button) {
            case 1747:
                amount = 28;//All
                break;
            case 8946:
            case 8950:
            case 8954:
            case 8958:
            case 8962:
            case 8906:
            case 8910:
            case 8914:
            case 8918:
            case 8886:
            case 8890:
            case 8894:
            case 8871:
            case 8875:
            case 1748:
            case 2414:
            case 3988:
            case 3992:
            case 3996:
            case 4000:
            case 4158:
            case 7442:
            case 7447:
                amount = 25;//X
                break;
            case 8947:
            case 8951:
            case 8955:
            case 8959:
            case 8963:
            case 8907:
            case 8911:
            case 8915:
            case 8919:
            case 8887:
            case 8891:
            case 8895:
            case 8872:
            case 8876:
            case 2807:
            case 3989:
            case 3993:
            case 3997:
            case 4001:
            case 6397:
            case 7443:
            case 7448:
                amount = 10;
                break;
            case 8948:
            case 8952:
            case 8956:
            case 8960:
            case 8964:
            case 8908:
            case 8912:
            case 8916:
            case 8920:
            case 8888:
            case 8892:
            case 8896:
            case 8873:
            case 8877:
            case 2798:
            case 3986:
            case 3990:
            case 3994:
            case 3998:
            case 4002:
            case 7440:
            case 7444:
            case 7449:
                amount = 5;
                break;
            case 8949:
            case 8953:
            case 8957:
            case 8961:
            case 8965:
            case 8909:
            case 8913:
            case 8917:
            case 8921:
            case 8889:
            case 8893:
            case 8897:
            case 8874:
            case 8878:
            case 2799:
            case 3987:
            case 3991:
            case 3995:
            case 3999:
            case 4003:
            case 7441:
            case 7446:
            case 7450:
                amount = 1;
                break;
        }
        return amount;
    }

    public static boolean handleDialogueButtons(Player player, int button) {
        int amount = getAmountSelected(button);
        int option = getOptionSelected(button);
        if (amount == -1 || option == -1)
            return false;

        player.getDialogueManager().setParameters(new Object[]{amount, option});
        if (amount == 25) {
            player.getPacketSender().sendEnterAmountPrompt("How many would you like to make?");
            player.setInputHandling(new EnterAmountToMake());
        } else
            player.getDialogueManager().runDialogue(option);
        return true;
    }
}
