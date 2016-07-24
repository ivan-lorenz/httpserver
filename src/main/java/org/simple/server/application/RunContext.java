package org.simple.server.application;

import org.simple.server.model.repository.IServerRepository;
import org.simple.server.model.repository.ServerRepository;

/* RunContext is the dependency injection container for Production.
 * It manages the creation of an specific http handler and its dependencies.
 */
public class RunContext extends Context {

    // System clock
    protected IClock systemClock = new SystemClock();

    // Server user store
    protected IServerRepository repository = new ServerRepository("admin", "admin", systemClock);

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

            @Override
            public IClock getClock() { return systemClock; }
        };
    }
}
