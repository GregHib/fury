package com.fury.network.login;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.fury.cache.Revision;
import com.fury.core.model.item.Item;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;

public class LoginUtils {

    public static String getHost(Channel channel) {
        return ((InetSocketAddress) channel.remoteAddress()).getAddress().getHostAddress();
    }

    public static void sendResponseCode(ChannelHandlerContext ctx, int response) {
        ByteBuf buffer = Unpooled.buffer(1);
        buffer.writeByte(response);
        ctx.writeAndFlush(buffer).addListener(ChannelFutureListener.CLOSE);
    }

    public static Item[] getItemContainer(JsonArray array) {
        return getItemContainer(array, false);
    }

    public static Item[] getItemContainer(JsonArray array, boolean toNew) {
        Item[] items = new Item[array.size()];
        for(int i = 0; i < array.size(); i++ ) {
            JsonObject object = array.get(i).getAsJsonObject();
            items[i] = getItem(object);
        }
        return toNew ? items : convertToOld(items);
    }

    public static Item[] convertToOld(Item[] in) {
        Item[] out = new Item[in.length];
        for(int i = 0; i < in.length; i++)
            out[i] = in[i] == null ? new Item(-1, 0) : in[i];
        return out;
    }

    public static Item getItem(JsonObject object) {
        if(object.has("revision")) {
            return new Item(object.get("id").getAsInt(), object.has("amount") ? object.get("amount").getAsInt() : -1, object.get("revision").getAsString().equals("OSRS") ? Revision.RS2 : Revision.valueOf(object.get("revision").getAsString()));
        } else if(object.has("id"))
            return new Item(object.get("id").getAsInt(), object.has("amount") ? object.get("amount").getAsInt() : -1);
        else
            return null;
    }
}
