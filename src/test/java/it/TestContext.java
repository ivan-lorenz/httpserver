package it;

import com.sun.net.httpserver.HttpHandler;
import org.simple.server.application.Context;
import org.simple.server.application.IServerRouter;
import org.simple.server.application.RunContext;
import org.simple.server.application.ServerRouter;
import org.simple.server.controller.ServerHandler;
import org.simple.server.controller.action.ServerActionFactory;
import org.simple.server.model.repository.IServerRepository;

class TestContext extends RunContext {

    private IServerRepository repository;

    TestContext(IServerRepository repository) {
        this.repository = repository;
    }

    @Override
    public ISupplyContext supplyContext() {
        return new ISupplyContext() {

            @Override
            public IServerRepository getRepository() {
                return repository;
            }

            @Override
            public String getRealm() {
                return "test-realm";
            }
        };
    }
}
