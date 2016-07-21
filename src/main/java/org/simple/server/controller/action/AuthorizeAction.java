package org.simple.server.controller.action;

import org.simple.server.controller.IServerExchange;

import java.io.IOException;
import java.io.InputStream;

/*
 *
 */
public class AuthorizeAction implements IServerAction {

    @Override
    public void execute(IServerExchange exchange) throws IOException {
        InputStream body = exchange.getRequestBody();
        exchange.setStatus(401, -1);
        exchange.close();
    }

    @Override
    public boolean isPublic() {
        return true;
    }
}
