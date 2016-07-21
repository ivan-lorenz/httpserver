package org.simple.server.model.repository;

import org.simple.server.model.ServerRole;

import java.util.List;

/*
 *
 */
class RepositoryUser implements IRepositoryUSer {

    private final String user;
    private final List<ServerRole> roles;
    private final String password;

    RepositoryUser(String user, List<ServerRole> roles, String password) {
        this.user = user;
        this.roles = roles;
        this.password = password;
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
    public String getPassword() {
        return this.password;
    }
}
