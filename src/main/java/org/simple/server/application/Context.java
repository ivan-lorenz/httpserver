package org.simple.server.application;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.simple.server.controller.ServerAuthenticator;
import org.simple.server.controller.action.*;
import org.simple.server.model.repository.ServerRepository;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/* Context is the abstract class implementing Dependency Injection Container technique
 * DI containers controlling the injection of dependencies manually are appropriate
 * for TDD purposes, allowing developers to mock any dependency during the test phase.
 * This class aggregates common functionality for both, running and test context, implemented
 * in the subclasses RunContext and TestContext.
 */
public abstract class Context {

    // Our simple server
    private HttpServer server;

    protected interface ISupplyContext {
        HttpHandler getHandler(IServerRouter router);
        IServerRouter getRouter();
    }

    // Context subclasses need to supply specific context, ex. Run or Test
    protected abstract ISupplyContext supplyContext();

    // Start listening on a port
    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(8001), 0);
        HttpContext context = server.createContext("/", supplyContext().getHandler(supplyContext().getRouter()));
        context.setAuthenticator(new ServerAuthenticator(supplyContext().getRouter(),"simple-server", new ServerRepository("admin","admin")));
        server.setExecutor(null);
        server.start();
    }

    // Stop the server
    public void stop() {
        if (null != server) server.stop(0);
    }


}
