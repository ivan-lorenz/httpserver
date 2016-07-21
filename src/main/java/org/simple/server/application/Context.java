package org.simple.server.application;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

/* Context is the abstract class implementing Dependency Injection Container technique
 * DI containers controlling the injection of dependencies manually are appropriate
 * for TDD purposes, allowing developers to mock any dependency during the test phase.
 * This class aggregates common functionality for both, running and test context, implemented
 * in the subclasses RunContext and TestContext.
 */
public abstract class Context {

    // Our simple server
    private HttpServer server;

    // Our server router
    protected IServerRouter router = new ServerRouter();

    public interface ISupplyContext {
        HttpHandler getHandler(IServerRouter router);
    }

    // Context subclasses need to supply specific context, ex. Run or Test
    public abstract ISupplyContext supplyContext();

    // Start listening on a port
    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(8001), 0);
        server.createContext("/", supplyContext().getHandler(router));
        server.setExecutor(null);
        server.start();
    }

    // Stop the server
    // Basically for testing purposes.
    public void stop() {
        if (null != server) server.stop(0);
    }


}
