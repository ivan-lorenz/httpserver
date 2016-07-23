package org.simple.server.application;

import org.simple.server.controller.IServerExchange;
import org.simple.server.controller.action.IServerAction;

import java.util.Optional;

/* IServerRouter defines the behaviour for routing URls to server actions.
 *
 */
public interface IServerRouter {
    Optional<IServerAction> get(IServerExchange e);
    boolean isPublic(IServerExchange e);
    boolean isNotFound(IServerExchange e);
}
