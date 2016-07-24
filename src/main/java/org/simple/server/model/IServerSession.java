package org.simple.server.model;

import org.simple.server.application.IClock;

public interface IServerSession {
    IServerUser getServerUser();
    String getSession();
    long getTimestamp();
    boolean isValid(IClock clock);
}
