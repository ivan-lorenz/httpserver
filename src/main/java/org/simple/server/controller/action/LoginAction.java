package org.simple.server.controller.action;

import org.simple.server.controller.IServerExchange;

import java.io.IOException;
import java.net.URI;

import static org.simple.server.controller.action.ServerAction.sendFile;

/*
 *
 */
public class LoginAction implements IServerAction {

    @Override
    public void execute(IServerExchange exchange) throws IOException {
        URI uri = exchange.getRequestURI();
        sendFile(exchange, uri.getPath(),200);
    }

    @Override
    public boolean isPublic() {
        return true;
    }
}
