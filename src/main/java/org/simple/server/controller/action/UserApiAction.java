package org.simple.server.controller.action;

import org.simple.server.controller.IServerExchange;
import org.simple.server.model.ServerRole;
import org.simple.server.model.repository.IServerRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.simple.server.controller.action.ServerAction.*;

/*
 *
 */
public class UserApiAction implements IServerAction {

    // Server user store
    private IServerRepository repository;

    // Names of mandatoryParameters to update a user. Add "password" for user creation.
    private static final String userParam = "user_name";
    private static final String passwordParam = "user_password";
    private static final String roleParam = "role";
    private static List<String> mandatoryParameters = new ArrayList<String>() {{
        add(userParam);
        add(roleParam);
    }};

    public UserApiAction(IServerRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(IServerExchange exchange) throws IOException {
        switch (exchange.getRequestMethod()) {
            case "POST":
                createUser(exchange);
                break;
            default:
                exchange.setStatus(405,-1);
                exchange.close();
        }
    }

    private void createUser(IServerExchange exchange) throws IOException {
        if (!isWwwFormUrlencoded(exchange)) {
            sendBadRequestResponse("Invalid content type. Rest api must use application/x-www-form-urlencoded", exchange);
        } else {
            Map<String, String> params = getWwwFormUrlencodedBody(exchange);

            if (null == params || !(params.keySet().containsAll(mandatoryParameters) && params.containsKey(passwordParam))) {
                sendBadRequestResponse("Invalid parameters. Please supply user name, password and role.", exchange);
            } else {
                List<ServerRole> roles = getRoles(params.get(roleParam));
                String user = params.get(userParam);
                String password = params.get(passwordParam);

                if (roles.isEmpty() || null == user || user.isEmpty() || null == password || password.isEmpty())
                    sendBadRequestResponse("Missing parameters",exchange);
                else {
                    repository.createUser(user,password,roles);
                    exchange.setStatus(200,-1);
                }
            }
        }
        exchange.close();
    }

    @Override
    public boolean isPublic() {
        return false;
    }
}
