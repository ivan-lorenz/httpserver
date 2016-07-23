package org.simple.server.model;

import java.util.List;

/*
 *
 */
public interface IServerUser {

    String getUser();
    List<ServerRole> getRoles();

    //Default methods
    default boolean isAdmin() {
        for (ServerRole role: this.getRoles()) {
            if (role == ServerRole.ADMIN)
                return true;
        }
        return false;
    }
}
