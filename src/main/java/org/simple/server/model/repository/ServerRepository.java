package org.simple.server.model.repository;

import org.simple.server.application.IClock;
import org.simple.server.model.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/* ServerRepository is a IServerRepository implementation backed by a
 * ConcurrentHashMap as a store for IRepositoryUser values.
 */
public class ServerRepository implements IServerRepository {

    private ConcurrentHashMap<String, IRepositoryUser> userStore = new ConcurrentHashMap<>(8, 0.9f, 1);
    private ConcurrentHashMap<String, IServerSession> sessionStore = new ConcurrentHashMap<>(8, 0.9f, 1);

    // System clock
    private IClock clock;

    public ServerRepository(String adminUser, String adminPassword, IClock clock) {
        ArrayList<ServerRole> roles = new ArrayList<ServerRole>();
        roles.add(ServerRole.ADMIN);
        IRepositoryUser admin = new RepositoryUser(adminUser, roles,adminPassword);
        userStore.put(adminUser, admin);

        this.clock = clock;
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
    public IServerUser createUser(String user, String password, List<ServerRole> roles) {
        IRepositoryUser repUser = new RepositoryUser(user, roles, password);
        userStore.put(user, repUser);
        return new ServerUser(user,roles);
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
    public IServerSession createSession(IServerUser user) {
        IServerSession session = new ServerSession(UUID.randomUUID().toString(), clock.getTimestamp());
        sessionStore.put(user.getUser(), session);
        return session;
    }

    @Override
    public void deleteAll() {
        userStore.clear();
    }
}
