package org.simple.server.application;

import org.simple.server.controller.IServerExchange;
import org.simple.server.controller.action.ServerScope;

import java.util.Optional;

/* IServerRouter defines the behaviour for routing URls to server actions.
 *
 */
public interface IServerRouter {
    Optional<ServerScope> get(IServerExchange e);

    // Default methods
    default boolean isNotFound(IServerExchange e) {
        return !get(e).isPresent();
    }

    default boolean isPublic(IServerExchange exchange) {
        return get(exchange)
                .map(ServerScope::isPublic)
                .orElse(false);
    }

}
