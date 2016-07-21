package it;

import com.sun.net.httpserver.HttpHandler;
import org.simple.server.Context;
import org.simple.server.controller.ServerHandler;
import org.simple.server.controller.action.IServerAction;
import org.simple.server.controller.action.ServerActionFactory;

import java.util.Map;

public class TestContext extends Context{
    @Override
    public ISupplyContext supplyContext() {
        return new ISupplyContext() {
            @Override
            public HttpHandler getHandler(Map<String, IServerAction> router) {
                return new ServerHandler(new ServerActionFactory(router));
            }
        };
    }
}