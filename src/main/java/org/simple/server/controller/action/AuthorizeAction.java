package org.simple.server.controller.action;

import org.simple.server.controller.IServerExchange;
import org.simple.server.model.IServerSession;
import org.simple.server.model.repository.IServerRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import static org.simple.server.controller.action.ServerActionHelper.*;

/* AuthorizeAction is the controller for user authorization.
 * It is triggered from the login.html form.
 */
class AuthorizeAction implements IServerAction {

    // Server user store
    private IServerRepository repository;

    // Names of mandatory parameters to authenticate a user.
    private static final String userParam = "user_name";
    private static final String passwordParam = "user_password";

    AuthorizeAction(IServerRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(IServerExchange exchange) throws IOException {

        if (isWwwFormUrlencoded(exchange)) {

            Map<String, String> params = getWwwFormUrlencodedBody(exchange);

            if (null != params) {
                String user = params.get(userParam);
                String password = params.get(passwordParam);

                if (null != user && null != password) {

                    if (!createSession(user, password, exchange))
                        exchange.setStatus(401,-1);
                }

            } else {
                sendErrorResponse(400,"Wrong parameters", exchange);
            }

        } else {
            sendErrorResponse(400,"Content type must be application/x-www-form-urlencoded", exchange);
        }

        exchange.close();
    }

    private boolean createSession(String user, String password, IServerExchange exchange) {
        return repository.getCredentials(user, password)
                .map( u -> setCookie(repository.createSession(u),exchange))
                .orElse(false);
    }

    private boolean setCookie(IServerSession session, IServerExchange exchange) {
        try {
            exchange.getResponseHeaders()
                    .put("Set-Cookie", new ArrayList<String>() {{
                        add("SESSION=" + session.getSession());
                    }});
            exchange.setStatus(200, -1);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
