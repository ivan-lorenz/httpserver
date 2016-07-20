package org.simple.server.controller.action;

import org.simple.server.controller.IServerExchange;

import java.io.IOException;

/* NotFoundAction is a IServerAction implementation for URI requests not mapped
 * in the router configuration.
 */
public class NotFoundAction implements IServerAction {
    public void execute(IServerExchange exchange) throws IOException {
        exchange.setStatus(404, 0);
        exchange.getResponseBody().close();
    }
}
