package org.simple.server.model.repository;

import org.simple.server.model.IServerUser;

/*
 *
 */
public interface IServerRepository {
    IServerUser getCredentials(String user, String password);
}
