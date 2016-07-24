package org.simple.server.controller.action;

/*
 *
 */
public class ServerScope {

    private ServerAction action;
    private boolean publicScope;

    public ServerScope(ServerAction action, boolean publicScope) {
        this.action = action;
        this.publicScope = publicScope;
    }

    public boolean isPublic() {
        return this.publicScope;
    }

    public ServerAction action() {
        return this.action;
    }
}
