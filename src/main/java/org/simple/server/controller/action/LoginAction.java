package org.simple.server.controller.action;

import org.simple.server.controller.IServerExchange;

import java.io.IOException;
import java.net.URI;

import static org.simple.server.controller.action.ServerActionHelper.replaceTokenAndSend;

/*
 *
 */
public class LoginAction implements IServerAction {

    @Override
    public void execute(IServerExchange exchange) throws IOException {
        URI uri = exchange.getRequestURI();
        String query = exchange.getRequestURI().getQuery();
        if (null == query || query.isEmpty())
            replaceTokenAndSend(exchange, uri.getPath(),"%referer%", "", 200);
        else
            replaceTokenAndSend(exchange, uri.getPath(),"%referer%", "?"+query, 200);
    }

}
