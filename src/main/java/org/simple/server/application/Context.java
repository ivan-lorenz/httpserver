package org.simple.server.application;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.simple.server.controller.ServerAuthenticator;
import org.simple.server.controller.ServerHandler;
import org.simple.server.controller.action.*;
import org.simple.server.model.repository.IServerRepository;
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

    // Router configuration for our server
    // We map a URI path and method with a concrete action controller
    private Map<String, ServerScope> routerConfiguration =
        new HashMap<String, ServerScope>() {{
            put("GET/login.html", new ServerScope(ServerAction.LOGIN, true));
            put("GET/page[1-3]{1}.html$", new ServerScope(ServerAction.PAGE, false));
            put("POST/authorize$", new ServerScope(ServerAction.AUTHORIZE, true));
            put("(POST|DELETE|PUT)/api/user$", new ServerScope(ServerAction.USERAPI, false));
        }};

    // Interface to supply context that subclasses must implement.
    protected interface ISupplyContext {
        IServerRepository getRepository();
        String getRealm();
    }

    // Context subclasses need to supply specific context, ex. Run or Test
    protected abstract ISupplyContext supplyContext();


    // Start listening on a port
    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(8001), 0);
        IServerRouter router = new ServerRouter(routerConfiguration);
        HttpContext context = server.createContext("/", new ServerHandler(new ServerActionFactory(router,supplyContext().getRepository())));
        context.setAuthenticator(new ServerAuthenticator(router,supplyContext().getRealm(), supplyContext().getRepository()));
        server.setExecutor(null);
        server.start();
    }

    // Stop the server
    public void stop(int delay) {
        if (null != server) server.stop(delay);
    }


}
