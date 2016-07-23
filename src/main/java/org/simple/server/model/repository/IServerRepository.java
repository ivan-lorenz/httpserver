package org.simple.server.model.repository;

import org.simple.server.model.IServerUser;
import org.simple.server.model.ServerRole;

import java.util.List;
import java.util.Optional;

/*
 *
 */
public interface IServerRepository {
    Optional<IServerUser> getCredentials(String user, String password);
    IServerUser createUser(String user, String password, List<ServerRole> roles);
    Optional<IServerUser> deleteUser(String user);
    Optional<IServerUser> updateUser(String user, List<ServerRole> roles);
    void deleteAll();
}
