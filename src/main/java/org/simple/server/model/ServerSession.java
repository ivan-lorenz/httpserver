package org.simple.server.model;

import java.util.Date;

public class ServerSession implements IServerSession {

    private String session;
    private long timestamp;

    public ServerSession(String session, long timestamp) {
        this.session = session;
        this.timestamp = timestamp;
    }

    public String getSession() {
        return session;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
