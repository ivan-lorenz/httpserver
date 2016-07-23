package org.simple.server.controller.action;

import org.simple.server.application.IServerRouter;
import org.simple.server.controller.IServerExchange;
import org.simple.server.model.repository.IServerRepository;

/* ServerActionFactory implements Factory Method pattern.
 * It provides a creational method which return an specific action implementation
 * for the needed job, depending on our router configuration.
 */
public class ServerActionFactory {

    // Router configuration
    private IServerRouter router;

    // User store
    private IServerRepository repository;

    // Router configuration is injected as a dependency
    public ServerActionFactory(IServerRouter router, IServerRepository repository) {
        this.router = router;
        this.repository = repository;
    }

    // Implementation of Factory Method
    public IServerAction getAction(IServerExchange exchange) {
        return router
                .get(exchange)
                .map(serverScope -> {
                    IServerAction action;
                    switch (serverScope.action()) {
                        case LOGIN:
                            action = new LoginAction();
                            break;
                        case PAGE:
                            action = new PageAction();
                            break;
                        case AUTHORIZE:
                            action = new AuthorizeAction();
                            break;
                        case USERAPI:
                            action = new UserApiAction(repository);
                            break;
                        default:
                            action = new NotFoundAction();
                            break;
                    }
                    return action;
                })
                .orElse(new NotFoundAction());
    }

}
