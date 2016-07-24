package org.simple.server.model;

import org.simple.server.application.IClock;

import java.time.Duration;

public class ServerSession implements IServerSession {

    // Session expiration timeout.
    private static final long expirationTime = Duration.ofMinutes(5).toMillis();

    private final String session;
    private final long timestamp;
    private final IServerUser user;

    public ServerSession(IServerUser user, String session, long timestamp) {
        this.session = session;
        this.timestamp = timestamp;
        this.user = user;
    }

    @Override
    public String getSession() {
        return session;
    }

    @Override
    public IServerUser getServerUser() { return user; }

    public static String getSessionFromCookie(String cookie) {
        return cookie.split("=")[1];
    }

    @Override
    public boolean isValid(IClock clock) { return clock.getTimestamp() - this.timestamp < expirationTime; }
}
