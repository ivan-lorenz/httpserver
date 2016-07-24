package org.simple.server.controller.action;

import org.simple.server.controller.IServerExchange;

import java.io.IOException;
import java.net.URI;

import static org.simple.server.controller.action.ServerActionHelper.replaceTokenAndSend;

/* PageAction is a IServerAction implementation to serve our static pages.
 *
 */
public class PageAction implements IServerAction {

    @Override
    public void execute(IServerExchange exchange) throws IOException {
        URI uri = exchange.getRequestURI();
        replaceTokenAndSend(exchange, uri.getPath(),exchange.getUser());
    }

}
