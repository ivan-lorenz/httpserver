package org.simple.server;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.simple.server.controller.action.IServerAction;
import org.simple.server.controller.action.PageAction;

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

    // Router configuration for our server
    // We map a URI path with a concrete action controller
    private static Map<String, IServerAction> router = new HashMap<String, IServerAction>();
    static {
        router.put("/page1.html", new PageAction());
    }

    // Our simple server
    private HttpServer server;

    public interface ISupplyContext {
        HttpHandler getHandler(Map<String, IServerAction> router);
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
