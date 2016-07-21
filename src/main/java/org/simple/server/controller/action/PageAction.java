package org.simple.server.controller.action;

import com.sun.net.httpserver.Headers;
import org.simple.server.controller.IServerExchange;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

/* PageAction is a IServerAction implementation to serving our static pages.
 *
 */
public class PageAction implements IServerAction {

    public void execute(IServerExchange exchange) throws IOException {
        URI uri = exchange.getRequestURI();
        ActionHelper.sendFile.sendFile(exchange, uri.getPath(),200);
    }
}
