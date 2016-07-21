package org.simple.server.controller.action;

import com.sun.net.httpserver.Headers;
import org.simple.server.controller.IServerExchange;

import java.io.IOException;
import java.io.InputStream;

/* NotFoundAction is a IServerAction implementation for URI requests not mapped
 * in the router configuration.
 */
public class NotFoundAction implements IServerAction {
    public void execute(IServerExchange exchange) throws IOException {
        ActionHelper.sendFile.sendFile(exchange, "/404.html", 404);
    }
}
