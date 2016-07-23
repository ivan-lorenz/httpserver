package org.simple.server.application;

import com.sun.net.httpserver.HttpHandler;
import org.simple.server.controller.ServerHandler;
import org.simple.server.controller.action.*;
import org.simple.server.model.repository.IServerRepository;
import org.simple.server.model.repository.ServerRepository;

import java.util.HashMap;
import java.util.Map;

/* RunContext is the dependency injection container for Production.
 * It manages the creation of an specific http handler and its dependencies.
 */
public class RunContext extends Context {

    // Server user store
    protected IServerRepository repository = new ServerRepository("admin", "admin");

    // Router configuration for our server
    // We map a URI path and method with a concrete action controller
    protected Map<String, IServerAction> router = new HashMap<String, IServerAction>() {{
        put("GET/login.html", new LoginAction());
        put("GET/page[1-3]{1}.html$", new PageAction());
        put("POST/authorize$", new AuthorizeAction());
        put("(POST|GET|PUT)/api/user$", new UserApiAction(repository));
    }};


    @Override
    public ISupplyContext supplyContext() {
        return new ISupplyContext() {
            @Override
            public HttpHandler getHandler(IServerRouter router) {
                return new ServerHandler(new ServerActionFactory(router));
            }

            @Override
            public IServerRouter getRouter() {
                return new ServerRouter(router);
            }
        };
    }
}
