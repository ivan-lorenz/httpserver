package org.simple.server.application;

import com.sun.net.httpserver.HttpHandler;
import org.simple.server.controller.ServerHandler;
import org.simple.server.controller.action.ServerActionFactory;

/* RunContext is the dependency injection container for Production.
 * It manages the creation of an specific http handler and its dependencies.
 */
public class RunContext extends Context {

    @Override
    public ISupplyContext supplyContext() {
        return new ISupplyContext() {
            @Override
            public HttpHandler getHandler(IServerRouter router) {
                return new ServerHandler(new ServerActionFactory(router));
            }
        };
    }
}
