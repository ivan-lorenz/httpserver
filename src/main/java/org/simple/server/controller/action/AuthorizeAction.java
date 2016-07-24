package org.simple.server.controller.action;

import com.sun.net.httpserver.Authenticator;
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

    // TODO: refactor arrow code.
    @Override
    public void execute(IServerExchange exchange) throws IOException {

        if (isWwwFormUrlencoded(exchange)) {

            Map<String, String> params = getWwwFormUrlencodedBody(exchange);

            if (null != params) {
                String user = params.get(userParam);
                String password = params.get(passwordParam);

                if (null != user && null != password) {

                    if (!createSession(user, password, exchange))
                        replaceTokenAndSend(exchange, "/error.html", "%status%","401 - Unauthorized", 401);
                }

            } else {
                replaceTokenAndSend(exchange, "/error.html", "%status%","400 - Wrong parameters", 400);
            }

        } else {
            replaceTokenAndSend(exchange, "/error.html", "%status%","400 - Content type must be application/x-www-form-urlencode", 400);
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
            doRedirect(exchange);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private void doRedirect(IServerExchange exchange) throws IOException {
        Map<String, String> params =  getQueryParams(exchange);

        if (null == params|| null == params.get("from"))
            exchange.setStatus(200, -1);
        else {
            exchange.getResponseHeaders().put("Location", new ArrayList<String>(){{ add( params.get("from"));}});
            exchange.setStatus(303, -1);
        }
    }
}
