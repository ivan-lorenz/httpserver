package org.simple.server.application;

import com.sun.net.httpserver.HttpHandler;
import org.simple.server.controller.ServerHandler;
import org.simple.server.controller.action.ServerActionFactory;
import org.simple.server.model.repository.IServerRepository;
import org.simple.server.model.repository.ServerRepository;

/* RunContext is the dependency injection container for Production.
 * It manages the creation of an specific http handler and its dependencies.
 */
public class RunContext extends Context {

    // Server user store
    private IServerRepository repository = new ServerRepository("admin", "admin");

    // Server realm
    private String realm = "simple-server";

    @Override
    public ISupplyContext supplyContext() {
        return new ISupplyContext() {

            @Override
            public IServerRepository getRepository() {
                return repository;
            }

            @Override
            public String getRealm() {
                return realm;
            }
        };
    }
}
