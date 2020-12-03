package com.fury.network.login;

import com.fury.network.security.IsaacRandom;
import io.netty.channel.ChannelHandlerContext;

public final class LoginDetailsMessage {

    private final String username;

    private final String password;

    private final String host;

    private final String serialNumber;

    private final String hardwareId;

    private final int clientVersion;

    private final int uid;

    private final ChannelHandlerContext context;

    private final IsaacRandom encryptor;

    private final IsaacRandom decryptor;

    public LoginDetailsMessage(ChannelHandlerContext context, String username, String password, String host, String serialNumber, String hardwareId, int clientVersion, int uid, IsaacRandom encryptor, IsaacRandom decryptor) {
        this.context = context;
        this.username = username;
        this.password = password;
        this.host = host;
        this.serialNumber = serialNumber;
        this.hardwareId = hardwareId;
        this.clientVersion = clientVersion;
        this.uid = uid;
        this.encryptor = encryptor;
        this.decryptor = decryptor;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getHost() {
    	return host;
    }

    public String getSerialNumber() {
    	return serialNumber;
    }

    public String getHardwareId() {
        return hardwareId;
    }

    public int getClientVersion() {
    	return clientVersion;
    }

    public int getUid() {
    	return uid;
    }

    public ChannelHandlerContext getContext() {
        return context;
    }

    public IsaacRandom getEncryptor() {
        return encryptor;
    }

    public IsaacRandom getDecryptor() {
        return decryptor;
    }
}
