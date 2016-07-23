package org.simple.server.application;

import org.simple.server.controller.IServerExchange;
import org.simple.server.controller.action.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

/* ServerRouter encapsulates the logic behind mapping a URL to a server action.
 *
 */
public class ServerRouter implements IServerRouter {

    private Map<String, IServerAction> router;

    public ServerRouter(Map<String, IServerAction> router) {
        this.router = router;
    }

    @Override
    public Optional<IServerAction> get(IServerExchange exchange) {

        for (Map.Entry<String, IServerAction> e: router.entrySet()) {
            if (Pattern.matches(e.getKey(), setRouterKey(exchange)))
                return Optional.of(e.getValue());
        }

        return Optional.empty();
    }

    @Override
    public boolean isNotFound(IServerExchange exchange) {
        return !get(exchange).isPresent();
    }

    @Override
    public boolean isPublic(IServerExchange exchange) {
        return get(exchange)
                .map(IServerAction::isPublic)
                .orElse(false);
    }

    // Router logic to build the key to map for an action
    private String setRouterKey(IServerExchange exchange) {
        return exchange.getRequestMethod() + exchange.getRequestURI().getPath();
    }
}
