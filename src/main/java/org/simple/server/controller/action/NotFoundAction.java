package org.simple.server.controller.action;

import org.simple.server.controller.IServerExchange;

import java.io.IOException;

import static org.simple.server.controller.action.ServerActionHelper.sendFile;

/* NotFoundAction is a IServerAction implementation for URI requests not mapped
 * in the router configuration.
 */
public class NotFoundAction implements IServerAction {

    @Override
    public void execute(IServerExchange exchange) throws IOException {
        sendFile(exchange, "/404.html", 404);
    }

}
