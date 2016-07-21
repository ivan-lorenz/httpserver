package org.simple.server.model;

import java.util.ArrayList;
import java.util.List;

/*
 *
 */
public class ServerUser implements IServerUser {

    private final String user;
    private List<ServerRole> roles = new ArrayList<>();

    public ServerUser(String user, List<ServerRole> roles) {
        this.user = user;
        this.roles = roles;
    }

    public ServerUser(String user, ServerRole role) {
        this.user = user;
        this.roles.add(role);
    }

    public ServerUser addRole(ServerRole role) {
        this.roles.add(role);
        return this;
    }

    @Override
    public String getUser() {
        return this.user;
    }

    @Override
    public List<ServerRole> getRoles() {
        return this.roles;
    }

    @Override
    public boolean isAdmin() {
        for (ServerRole role: roles) {
            if (role == ServerRole.ADMIN)
                return true;
        }
        return false;
    }
}
