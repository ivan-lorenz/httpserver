package org.simple.server.controller.action;

import org.simple.server.controller.IServerExchange;

import java.io.IOException;

public interface IServerAction {
    public void execute(IServerExchange exchange) throws IOException;
    public boolean isPublic();
}
