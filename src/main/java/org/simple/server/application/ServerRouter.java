package org.simple.server.application;

import org.simple.server.controller.action.IServerAction;
import org.simple.server.controller.action.PageAction;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/* ServerRouter encapsulates the logic behind mapping a URL to a server action.
 *
 */
public class ServerRouter implements  IServerRouter {

    // Router configuration for our server
    // We map a URI path with a concrete action controller
    private static Map<String, IServerAction> router = new HashMap<String, IServerAction>();
    static {
        router.put("/page[1-3]{1}.html", new PageAction());
    }

    @Override
    public IServerAction get(String s) {

        for (Map.Entry<String, IServerAction> e: router.entrySet()) {
            if (Pattern.matches(e.getKey(), s))
                return e.getValue();
        }

        return null;
    }
}
