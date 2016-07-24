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

    // Actions pool
    private IServerAction loginAction;
    private IServerAction pageAction;
    private IServerAction authorizeAction;
    private IServerAction userApiAction;
    private IServerAction notFoundAction;

    // Router configuration is injected as a dependency
    public ServerActionFactory(IServerRouter router, IServerRepository repository) {
        this.router = router;
        this.repository = repository;

        // Create actions
        this.loginAction = new LoginAction();
        this.pageAction = new PageAction();
        this.authorizeAction = new AuthorizeAction();
        this.userApiAction = new UserApiAction(repository);
        this.notFoundAction = new NotFoundAction();
    }

    // Implementation of Factory Method.
    public IServerAction getAction(IServerExchange exchange) {
        return router
                .get(exchange)
                .map(serverScope -> {
                    IServerAction action;
                    switch (serverScope.action()) {
                        case LOGIN:
                            action = loginAction;
                            break;
                        case PAGE:
                            action = pageAction;
                            break;
                        case AUTHORIZE:
                            action = authorizeAction;
                            break;
                        case USERAPI:
                            action = userApiAction;
                            break;
                        default:
                            action = notFoundAction;
                            break;
                    }
                    return action;
                })
                .orElse(new NotFoundAction());
    }

}
