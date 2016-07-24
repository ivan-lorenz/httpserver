package org.simple.server.controller.action;

import org.simple.server.model.ServerRole;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 *
 */
public class ServerScope {

    private ServerAction action;
    private boolean publicScope;
    private List<ServerRole> allowedRoles;

    public ServerScope(ServerAction action, boolean publicScope, List<ServerRole> allowedRoles) {
        this.action = action;
        this.publicScope = publicScope;
        this.allowedRoles = allowedRoles;
    }

    public ServerScope(ServerAction action, boolean publicScope,ServerRole allowedRole) {
        this.action = action;
        this.publicScope = publicScope;
        this.allowedRoles = new ArrayList<ServerRole>(){{ add(allowedRole); }};
    }

    public boolean isPublic() {
        return this.publicScope;
    }

    public ServerAction action() {
        return this.action;
    }

    public boolean isValidRole(List<ServerRole> roles) {
        return !Collections.disjoint(roles, allowedRoles);
    }
}
