package com.fury.network.security;

import com.fury.game.GameSettings;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.system.communication.punishment.Punishment;
import com.fury.game.system.files.Resources;
import com.fury.game.system.files.world.single.impl.Starters;
import com.fury.network.login.LoginDetailsMessage;
import com.fury.network.login.LoginResponses;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

/**
 * A lot of connection-related stuff.
 * Really messy.
 */

public class ConnectionHandler {

    public static void init() {
        long startup = System.currentTimeMillis();
        loadHostBlacklist();
        if (GameSettings.DEBUG)
            System.out.println("Loaded connection handler " + (System.currentTimeMillis() - startup) + "ms");
    }

    public static int getResponse(Player player, LoginDetailsMessage msg) {
        String host = msg.getHost();

        if (Punishment.isBanned(player.getUsername()))
            return LoginResponses.LOGIN_DISABLED_ACCOUNT;

        if (Punishment.isHardwareBanned(player.getLogger().getHardwareId()))
            return LoginResponses.LOGIN_DISABLED_COMPUTER;

        if (isBlocked(host))
            return LoginResponses.LOGIN_DISABLED_IP;

        if (!isLocal(player.getLogger().getHardwareId())) {
            if (CONNECTIONS.get(player.getLogger().getHardwareId()) != null) {
                if (CONNECTIONS.get(player.getLogger().getHardwareId()) >= GameSettings.CONNECTION_LIMIT) {
                    System.out.println("Connection limit reached : " + player.getUsername() + ". Host: " + player.getLogger().getHardwareId() + " " + CONNECTIONS.get(player.getLogger().getHardwareId()));
                    return LoginResponses.LOGIN_CONNECTION_LIMIT;
                }
            }
        }

        return LoginResponses.LOGIN_SUCCESSFUL;
    }

    /**
     * BLACKLISTED CONNECTIONS SUCH AS PROXIES
     **/
    private static List<String> BLACKLISTED_HOSTNAMES = new ArrayList<>();

    /**
     * The concurrent map of registered connections.
     */
    private static final Map<String, Integer> CONNECTIONS = Collections.synchronizedMap(new HashMap<>());

    private static void loadHostBlacklist() {
        String word;
        try {
            BufferedReader in = new BufferedReader(new FileReader(Resources.getSaveFile("blockedhosts")));
            while ((word = in.readLine()) != null)
                BLACKLISTED_HOSTNAMES.add(word.toLowerCase());
            in.close();
        } catch (final Exception e) {
            System.out.println("Could not load blacklisted hosts.");
        }
    }

    public static boolean isBlocked(String host) {
        return BLACKLISTED_HOSTNAMES.contains(host.toLowerCase());
    }


    public static void clearConnections() {
        CONNECTIONS.clear();
    }

    public static void add(String host) {
        if (!isLocal(host)) {
            if (CONNECTIONS.get(host) == null) {
                CONNECTIONS.put(host, 1);
            } else {
                int amt = CONNECTIONS.get(host) + 1;
                CONNECTIONS.put(host, amt);
            }
        }
    }

    public static void remove(String host) {
        if (!isLocal(host)) {
            if (CONNECTIONS.get(host) != null) {
                int amt = CONNECTIONS.get(host) - 1;
                if (amt == 0) {
                    CONNECTIONS.remove(host);
                } else {
                    CONNECTIONS.put(host, amt);
                }
            }
        }
    }

    public static int getStarters(String hwid) {
        if (hwid == null)
            return GameSettings.MAX_STARTERS_PER_IP;

        return ConnectionHandler.isLocal(hwid) ? 0 : Starters.get().get(hwid);
    }

    /**
     * Determines if the specified host is connecting locally.
     *
     * @param host the host to check if connecting locally.
     * @return {@code true} if the host is connecting locally, {@code false}
     * otherwise.
     */
    public static boolean isLocal(String host) {
        return host == null || host.equals("null") || host.equals("127.0.0.1") || host.equals("localhost");
    }
}
