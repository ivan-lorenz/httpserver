package org.simple.server.application;

import org.simple.server.controller.IServerExchange;
import org.simple.server.controller.action.*;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/* ServerRouter encapsulates the logic behind mapping a URL to a server action.
 *
 */
public class ServerRouter implements IServerRouter {

    // Router configuration for our server
    // We map a URI path and method with a concrete action controller
    private static Map<String, IServerAction> router = new HashMap<String, IServerAction>();
    static {
        router.put("GET/login.html", new LoginAction());
        router.put("GET/page[1-3]{1}.html$", new PageAction());
        router.put("POST/authorize$", new AuthorizeAction());
        router.put("(POST|GET|PUT)/api/user$", new UserApiAction());
    }

    @Override
    public IServerAction get(IServerExchange exchange) {

        for (Map.Entry<String, IServerAction> e: router.entrySet()) {
            if (Pattern.matches(e.getKey(), setRouterKey(exchange)))
                return e.getValue();
        }

        return null;
    }

    @Override
    public boolean isNotFound(IServerExchange exchange) {
        return null == get(exchange);
    }

    @Override
    public boolean isPublic(IServerExchange exchange) {

        IServerAction action = get(exchange);

        if (null != action)
            return action.isPublic();

        return false;
    }

    // Router logic to build the key to map for an action
    private String setRouterKey(IServerExchange exchange) {
        return exchange.getRequestMethod() + exchange.getRequestURI().getPath();
    }
}
