package org.simple.server.application;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import org.simple.server.controller.ServerAuthenticator;
import org.simple.server.controller.ServerHandler;
import org.simple.server.controller.action.ServerAction;
import org.simple.server.controller.action.ServerActionFactory;
import org.simple.server.controller.action.ServerScope;
import org.simple.server.model.ServerRole;
import org.simple.server.model.repository.IServerRepository;

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
    private final Map<String, ServerScope> routerConfiguration =
        new HashMap<String, ServerScope>() {{
            put("GET/login.html", new ServerScope(ServerAction.LOGIN, true, ServerRole.all));
            put("GET/logout.html", new ServerScope(ServerAction.LOGOUT, false, ServerRole.all));
            put("GET/page1.html$", new ServerScope(ServerAction.PAGE, false, ServerRole.PAGE1));
            put("GET/page2.html$", new ServerScope(ServerAction.PAGE, false, ServerRole.PAGE2));
            put("GET/page3.html$", new ServerScope(ServerAction.PAGE, false, ServerRole.PAGE3));
            put("POST/authorize$", new ServerScope(ServerAction.AUTHORIZE, true, ServerRole.all));
            put("(POST|DELETE|PUT)/api/user/(.+)", new ServerScope(ServerAction.USERAPI, false, ServerRole.ADMIN));
            put("GET/api/user/(.+)", new ServerScope(ServerAction.USERAPI, false, ServerRole.all));
        }};

    // Interface to supply context that subclasses must implement.
    protected interface ISupplyContext {
        IServerRepository getRepository();
        String getRealm();
        IClock getClock();
    }

    // Context subclasses need to supply specific context, ex. Run or Test
    protected abstract ISupplyContext supplyContext();


    // Start listening on a port
    public void start(int port) throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        IServerRouter router = new ServerRouter(routerConfiguration);
        HttpContext context = server.createContext("/", new ServerHandler(new ServerActionFactory(router,supplyContext().getRepository())));
        context.setAuthenticator(new ServerAuthenticator(router,supplyContext().getRealm(), supplyContext().getRepository(), supplyContext().getClock()));
        server.setExecutor(null);
        server.start();
    }

    // Stop the server
    public void stop(int delay) {
        if (null != server) server.stop(delay);
    }


}
