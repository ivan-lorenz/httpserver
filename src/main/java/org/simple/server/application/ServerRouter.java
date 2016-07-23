package org.simple.server.application;

import org.simple.server.controller.IServerExchange;
import org.simple.server.controller.action.ServerScope;

import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

/* ServerRouter encapsulates the logic behind mapping a URL to a server action.
 *
 */
public class ServerRouter implements IServerRouter {

    private Map<String, ServerScope> router;

    public ServerRouter(Map<String,ServerScope> router) {
        this.router = router;
    }

    @Override
    public Optional<ServerScope> get(IServerExchange exchange) {

        for (Map.Entry<String, ServerScope> e: router.entrySet()) {
            if (Pattern.matches(e.getKey(), setRouterKey(exchange)))
                return Optional.of(e.getValue());
        }

        return Optional.empty();
    }

    // Router logic to build the key to map for an action
    private String setRouterKey(IServerExchange exchange) {
        return exchange.getRequestMethod() + exchange.getRequestURI().getPath();
    }
}
