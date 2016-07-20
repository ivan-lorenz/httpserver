package org.simple.server.controller.action;

import org.simple.server.controller.IServerExchange;

import java.util.Map;

/* ServerActionFactory implements Factory Method pattern.
 * It provides a creational method which return an specific action implementation
 * for the needed job, depending on our router configuration.
 */
public class ServerActionFactory {

    // Router configuration
    private Map<String, IServerAction> router;

    // Router configuration is injected as a dependency
    public ServerActionFactory(Map<String, IServerAction> router) {
        this.router = router;
    }

    // Implementation of Factory Method
    public IServerAction getAction(IServerExchange exchange) {
        IServerAction action = router.get(exchange.getRequestURI().getPath());
        return null != action ? action : new NotFoundAction();
    }
}
