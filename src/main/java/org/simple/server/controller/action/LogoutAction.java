package org.simple.server.controller.action;

import org.simple.server.controller.IServerExchange;
import org.simple.server.model.ServerSession;
import org.simple.server.model.repository.IServerRepository;

import java.io.IOException;
import java.util.List;

import static org.simple.server.controller.action.ServerActionHelper.replaceTokenAndSend;
import static org.simple.server.controller.action.ServerActionHelper.sendFile;

public class LogoutAction implements IServerAction {

    // User store
    private IServerRepository repository;

    LogoutAction(IServerRepository repository) {
        this.repository = repository;
    }

    // TODO: refactor arrow code.
    @Override
    public void execute(IServerExchange exchange) throws IOException {
        List<String> cookieList = exchange.getRequestHeaders().get("Cookie");

        if (null != cookieList) {
            String session = ServerSession.getSessionFromCookie(cookieList.iterator().next());

            if (null != session) {
                if (repository.closeSession(session).isPresent())
                    sendFile(exchange, "/logout.html", 200);
                else
                    replaceTokenAndSend(exchange, "/error.html", "%status%", "404", 404);
            } else {
                replaceTokenAndSend(exchange, "/error.html", "%status%", "404", 404);
            }
        } else {
            replaceTokenAndSend(exchange, "/error.html", "%status%", "404", 404);
        }

        exchange.close();;
    }
}
