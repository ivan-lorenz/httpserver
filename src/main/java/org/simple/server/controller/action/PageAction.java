package org.simple.server.controller.action;

import org.simple.server.controller.IServerExchange;

import java.io.IOException;
import java.net.URI;

import static org.simple.server.controller.action.ServerActionHelper.sendFile;

/* PageAction is a IServerAction implementation to serve our static pages.
 *
 */
public class PageAction implements IServerAction {

    @Override
    public void execute(IServerExchange exchange) throws IOException {
        URI uri = exchange.getRequestURI();
        sendFile(exchange, uri.getPath(),200);
    }

}
