package org.simple.server.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.simple.server.controller.action.IServerAction;
import org.simple.server.controller.action.ServerActionFactory;

import java.io.IOException;

/* ServerHandler implements the Mediator pattern to trigger different actions for different URLs.
 * It use a constructor injected action factory to instantiate the right action for the job and execute it.
 */
public class ServerHandler implements HttpHandler {

    // ServerAction factory
    private final ServerActionFactory actionFactory;

    public ServerHandler(ServerActionFactory actionFactory) {
        this.actionFactory = actionFactory;
    }

    // Implementation of HttpHandler.
    // This method encapsulates the Mediator pattern (or Front Controller for web applications)
    public void handle(HttpExchange httpExchange) throws IOException {
        IServerExchange exchange = new ServerExchange(httpExchange);

        IServerAction action = actionFactory.getAction(exchange);
        action.execute(exchange);
    }
}
