package com.fury.game.content.eco.ge;

import com.fury.cache.def.item.ItemDefinition;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;

/**
 * A Grand Exchange slot's state
 *
 * @author Gabriel Hannason
 */
public enum GrandExchangeSlotState {

    EMPTY {
        @Override
        public void update(Player p, int slot, int geData, int percent, Item item) {
            p.getPacketSender().sendGrandExchangeUpdate("resetslot <" + slot + ">");
        }
    },
    PENDING_PURCHASE {
        @Override
        public void update(Player p, int slot, int geData, int percent, Item item) {
            if(item == null) {
                p.getPacketSender().sendGrandExchangeUpdate("slotselected item #-1,0# slotsell <" + slot + "> [" + geData + "] slotpercent {" + percent + "}");
                return;
            }
            ItemDefinition def = item.getDefinition();
            if (def.noteId != -1 && def.noted)
                item = new Item(def.noteId, item);
            p.getPacketSender().sendGrandExchangeUpdate("slotselected item #" + item.getId() + "," + item.getRevision().ordinal() + "# slotbuy <" + slot + "> [" + geData + "] slotpercent {" + percent + "}");
        }
    },
    FINISHED_PURCHASE {
        @Override
        public void update(Player p, int slot, int geData, int percent, Item item) {
            if(item == null) {
                p.getPacketSender().sendGrandExchangeUpdate("slotselected item #-1,0# slotsell <" + slot + "> [" + geData + "] slotpercent {" + percent + "}");
                return;
            }
            ItemDefinition def = item.getDefinition();
            if (def.noteId != -1 && def.noted)
                item = new Item(def.noteId, item);
            p.getPacketSender().sendGrandExchangeUpdate("slotselected item #" + item.getId() + "," + item.getRevision().ordinal()  + "# slotbuy <" + slot + "> [" + geData + "] slotpercent {" + percent + "}");
        }
    },
    PENDING_SALE {
        @Override
        public void update(Player p, int slot, int geData, int percent, Item item) {
            if(item == null) {
                p.getPacketSender().sendGrandExchangeUpdate("slotselected item #-1,0# slotsell <" + slot + "> [" + geData + "] slotpercent {" + percent + "}");
                return;
            }
            ItemDefinition def = item.getDefinition();
            if (def.noteId != -1 && def.noted)
                item = new Item(def.noteId, item);
            p.getPacketSender().sendGrandExchangeUpdate("slotselected item #" + item.getId() + "," + item.getRevision().ordinal()  + "# slotsell <" + slot + "> [" + geData + "] slotpercent {" + percent + "}");
        }
    },
    FINISHED_SALE {
        @Override
        public void update(Player p, int slot, int geData, int percent, Item item) {
            if(item == null) {
                p.getPacketSender().sendGrandExchangeUpdate("slotselected item #-1,0# slotsell <" + slot + "> [" + geData + "] slotpercent {" + percent + "}");
                return;
            }
            ItemDefinition def = item.getDefinition();
            if (def.noteId != -1 && def.noted)
                item = new Item(def.noteId, item);
            p.getPacketSender().sendGrandExchangeUpdate("slotselected item #" + item.getId() + "," + item.getRevision().ordinal()  + "# slotsell <" + slot + "> [" + geData + "] slotpercent {" + percent + "}");
        }
    },
    ABORTED {
        @Override
        public void update(Player p, int slot, int geData, int percent, Item item) {
            p.getPacketSender().sendGrandExchangeUpdate("<" + slot + "> slotaborted");
        }
    };

    public abstract void update(Player p, int slot, int geData, int percent, Item item);

    public static GrandExchangeSlotState forId(int updateStateOrdinal) {
        for (GrandExchangeSlotState state : GrandExchangeSlotState.values()) {
            if (state.ordinal() == updateStateOrdinal) {
                return state;
            }
        }
        return null;
    }

}
