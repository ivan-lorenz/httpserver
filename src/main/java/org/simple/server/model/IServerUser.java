package org.simple.server.model;

import java.util.List;

/*
 *
 */
public interface IServerUser {
    String getUser();
    List<ServerRole> getRoles();
    boolean isAdmin();
}
