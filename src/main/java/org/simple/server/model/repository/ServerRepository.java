package org.simple.server.model.repository;

import org.simple.server.model.IServerUser;
import org.simple.server.model.ServerRole;
import org.simple.server.model.ServerUser;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/* ServerRepository is a IServerRepository implementation backed by a
 * ConcurrentHashMap as a store for IRepositoryUser values.
 */
public class ServerRepository implements IServerRepository {

    private ConcurrentHashMap<String, IRepositoryUSer> userStore = new ConcurrentHashMap<String,IRepositoryUSer>(8, 0.9f, 1);

    public ServerRepository(String adminUser, String adminPassword) {
        ArrayList<ServerRole> roles = new ArrayList<ServerRole>();
        roles.add(ServerRole.ADMIN);
        IRepositoryUSer admin = new RepositoryUser(adminUser, roles,adminPassword);
        userStore.put(adminUser, admin);
    }

    @Override
    public IServerUser getCredentials(String user, String password) {

        for (Map.Entry<String, IRepositoryUSer> u: userStore.entrySet()) {
            if (Objects.equals(u.getKey(), user) && Objects.equals(u.getValue().getPassword(), password))
                return new ServerUser(user, u.getValue().getRoles());
        };

        return null;
    }
}
