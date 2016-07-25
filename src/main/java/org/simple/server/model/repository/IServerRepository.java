package org.simple.server.model.repository;

import org.simple.server.model.IServerSession;
import org.simple.server.model.IServerUser;
import org.simple.server.model.ServerRole;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*
 *
 */
public interface IServerRepository {
    Optional<IServerUser> getCredentials(String user, String password);
    IServerUser createUser(String user, String password, List<ServerRole> roles);
    Optional<IServerUser> deleteUser(String user);
    Optional<IServerUser> getUser(String user);
    Optional<IServerUser> updateUser(String user, List<ServerRole> roles);
    IServerSession createSession(IServerUser user);
    Optional<IServerSession> getSession(String session);
    Optional<IServerSession> closeSession(String session);
    void keepAliveSession(IServerSession session);
    void deleteAll();

    // Default methods
    default IServerUser createUser(String user, String password, ServerRole role) {
        return createUser(user, password, new ArrayList<ServerRole>(){{ add(role); }});
    }
}
