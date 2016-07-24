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

    @Override
    public String getUser() {
        return this.user;
    }

    @Override
    public List<ServerRole> getRoles() {
        return this.roles;
    }

    @Override
    public String toString() {
        StringBuilder listString = new StringBuilder();

        for (ServerRole s : getRoles())
            listString.append(s.getValue()+",");

        return "User: " + getUser() + "; Roles: " + listString;
    }
}
