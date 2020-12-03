package com.fury.game.system.communication.commands.impl.regular;

import com.fury.core.model.item.Item;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.content.global.promotion.Promotion;
import com.fury.game.content.misc.items.random.impl.imps.MysteryBoxTimedGen;
import com.fury.game.system.communication.commands.Command;
import com.fury.game.system.files.world.single.impl.HWID;

import java.net.InetSocketAddress;
import java.util.regex.Pattern;

public class ClaimPromo implements Command {

    @Override
    public Pattern pattern() {
        return null;
    }

    @Override
    public String prefix() {
        return "claim "+Promotion.getPromoCode();
    }

    @Override
    public String format() {
        return null;
    }

    @Override
    public void process(Player player, String... values) {
        HWID.PromotionClaims claims = HWID.PromotionClaims.get();
        String username = player.getUsername();
        String ip = ((InetSocketAddress) player.getSession().get().getChannel().remoteAddress()).getHostString();
        String hwid = player.getLogger().getHardwareId();

        if(claims.has(username, ip, hwid)) {
            player.message("You've already claimed this promotion!");
            return;
        };
        claims.record(1, username, ip, hwid);
        claims.save();

        Item item = new Item(18768, 1);
        player.getInventory().add(item);
        MysteryBoxTimedGen.newTimerFor(player, item);
    }

    @Override
    public boolean rights(Player player) {
        return true;
    }
}