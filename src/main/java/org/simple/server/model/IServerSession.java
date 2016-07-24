package org.simple.server.model;

import org.simple.server.application.IClock;

public interface IServerSession {
    IServerUser getServerUser();
    String getSession();
    boolean isValid(IClock clock);
}
