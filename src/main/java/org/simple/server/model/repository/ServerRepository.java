package org.simple.server.model.repository;

import org.simple.server.model.IServerUser;
import org.simple.server.model.ServerRole;
import org.simple.server.model.ServerUser;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/* ServerRepository is a IServerRepository implementation backed by a
 * ConcurrentHashMap as a store for IRepositoryUser values.
 */
public class ServerRepository implements IServerRepository {

    public ConcurrentHashMap<String, IRepositoryUser> userStore = new ConcurrentHashMap<String,IRepositoryUser>(8, 0.9f, 1);

    public ServerRepository(String adminUser, String adminPassword) {
        ArrayList<ServerRole> roles = new ArrayList<ServerRole>();
        roles.add(ServerRole.ADMIN);
        IRepositoryUser admin = new RepositoryUser(adminUser, roles,adminPassword);
        userStore.put(adminUser, admin);
    }

    @Override
    public Optional<IServerUser> getCredentials(String user, String password) {

        for (Map.Entry<String, IRepositoryUser> u: userStore.entrySet()) {
            if (Objects.equals(u.getKey(), user) && Objects.equals(u.getValue().getPassword(), password))
                return Optional.of(new ServerUser(user, u.getValue().getRoles()));
        };

        return Optional.empty();
    }

    @Override
    public Optional<IServerUser> createUser(String user, String password, List<ServerRole> roles) {
        IRepositoryUser repUser = new RepositoryUser(user, roles, password);
        IRepositoryUser success = userStore.put(user, repUser);
        return null != success ? Optional.of(new ServerUser(user,roles)) : Optional.empty();
    }

    @Override
    public Optional<IServerUser> deleteUser(String user) {
        IRepositoryUser repoUser = userStore.remove(user);
        if (null == repoUser)
            return Optional.empty();

        return Optional.of(new ServerUser(user,repoUser.getRoles()));
    }

    @Override
    public Optional<IServerUser> updateUser(String user, List<ServerRole> roles) {
        IRepositoryUser repoUser = userStore.get(user);

        if (null == repoUser)
            return Optional.empty();

        userStore.put(user, new RepositoryUser(user, roles, repoUser.getPassword()));
        return Optional.of(new ServerUser(user, roles));
    }

    @Override
    public void deleteAll() {
        userStore.clear();
    }
}
