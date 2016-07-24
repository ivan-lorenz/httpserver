package org.simple.server.application;

import org.simple.server.model.repository.IServerRepository;
import org.simple.server.model.repository.ServerRepository;

/* RunContext is the dependency injection container for Production.
 * It manages the creation of an specific http handler and its dependencies.
 */
public class RunContext extends Context {

    // Server user store
    protected IServerRepository repository = new ServerRepository("admin", "admin");

    // Server realm
    protected String realm = "simple-server";

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
