package com.fury.game.content.dialogue.impl.skills.construction;

import com.fury.cache.def.Loader;
import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.core.model.item.Item;

import java.util.ArrayList;
import java.util.List;

public class ToolsD extends Dialogue {
    private static final int[][] ITEMS = {{8794, 2347, 1755, 1735}, {1925, 946, 952, 590}, {1757, 1785, 1733}, {1595, 1597, 1592, 1599, 11065, 5523}, {5341, 952, 5325, 5343, 5331, 5329}};
    private List<String> names = new ArrayList<>();
    private int index;

    @Override
    public void start() {
        int objectId = (int) this.parameters[0];
        index = objectId - 13699;
        int[] items = ITEMS[index];
        int length = items.length;
        for (int i = 0; i < length; i++)
            names.add(Loader.getItem(items[i]).getName());
        String[] options = new String[length > 5 ? 5 : length];
        System.arraycopy(names.toArray(new String[names.size()]), 0, options, 0, length > 5 ? 5 : length);
        if(length > 5) {
            options[4] = "Next Page";
            stage = 0;
        }
        player.getDialogueManager().sendOptionsDialogue("Please select an item", options);
    }

    @Override
    public void run(int optionId) {
        if(stage == -1) {
            if (optionId < ITEMS[index].length)
                player.getInventory().addSafe(new Item(ITEMS[index][optionId]));
            end();
        } else if(stage == 0) {
            if(optionId == DialogueManager.OPTION_5) {
                int length = ITEMS[index].length - 4;
                String[] options = new String[length + 1];
                System.arraycopy(names.toArray(new String[names.size()]), 4, options, 0, length > 5 ? 5 : length);
                options[length] = "Previous Page";
                player.getDialogueManager().sendOptionsDialogue("Please select an item", options);
                stage = 1;
            } else {
                if (optionId < ITEMS[index].length)
                    player.getInventory().addSafe(new Item(ITEMS[index][optionId]));
                end();
            }
        } else if(stage == 1) {
            if (optionId < ITEMS[index].length - 4) {
                player.getInventory().addSafe(new Item(ITEMS[index][optionId + 4]));
                end();
            } else
                player.getDialogueManager().startDialogue(new ToolsD(), parameters[0]);
        } else
            end();
    }
}
