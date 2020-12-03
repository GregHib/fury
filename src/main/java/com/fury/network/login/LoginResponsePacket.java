package com.fury.network.login;

public class LoginResponsePacket {

    /**
     * The login response that was indicated.
     */
    private final int response;

    /**
     * The rights of the player logging in.
     */
    private final int rights;

    /**
     * The rights of the player logging in.
     */
    private final boolean flagged;

    /**
     * Creates a new {@link LoginResponsePacket}.
     *
     * @param response The response that was indicated.
     *
     * @param rights The rights of the player logging in.
     *
     * @param flagged The flag that indicates a player was flagged.
     */
    public LoginResponsePacket(int response, int rights, boolean flagged) {
        this.response = response;
        this.rights = rights;
        this.flagged = flagged;
    }

    public int getResponse() {
        return response;
    }

    public int getRights() {
        return rights;
    }

    public int getFlagged() {
        return rights;
    }
}
