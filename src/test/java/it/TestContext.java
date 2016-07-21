package it;

import com.sun.net.httpserver.HttpHandler;
import org.simple.server.application.Context;
import org.simple.server.application.IServerRouter;
import org.simple.server.controller.ServerHandler;
import org.simple.server.controller.action.ServerActionFactory;

public class TestContext extends Context{
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
