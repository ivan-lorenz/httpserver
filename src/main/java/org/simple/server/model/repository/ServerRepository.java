package org.simple.server.model.repository;

import org.simple.server.application.IClock;
import org.simple.server.model.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/* ServerRepository is a IServerRepository implementation backed by a
 * ConcurrentHashMap as a store for IRepositoryUser values.
 */
public class ServerRepository implements IServerRepository {

    private final ConcurrentHashMap<String, IRepositoryUser> userStore = new ConcurrentHashMap<>(8, 0.9f, 1);
    private final ConcurrentHashMap<String, IServerSession> sessionStore = new ConcurrentHashMap<>(8, 0.9f, 1);

    // System clock
    private final IClock clock;

    public ServerRepository(String adminUser, String adminPassword, IClock clock) {
        ArrayList<ServerRole> roles = new ArrayList<>();
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
        }

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
    public Optional<IServerUser> getUser(String user) {
        IRepositoryUser repoUser = userStore.get(user);
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
        IServerSession session = new ServerSession(user, UUID.randomUUID().toString(), clock.getTimestamp());
        sessionStore.put(session.getSession(), session);
        return session;
    }

    @Override
    public Optional<IServerSession> getSession(String session) {
        IServerSession success = sessionStore.get(session);
        return null != success ? Optional.of(success) : Optional.empty();
    }

    @Override
    public Optional<IServerSession> closeSession(String session) {
        IServerSession success = sessionStore.remove(session);
        return null != success ? Optional.of(success) : Optional.empty();
    }

    @Override
    public void keepAliveSession(IServerSession session) {
        sessionStore.put(session.getSession(), session.keepAlive(clock));
    }

    @Override
    public void deleteAll() {
        userStore.clear();
    }
}
