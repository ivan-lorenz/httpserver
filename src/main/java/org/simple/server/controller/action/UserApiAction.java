package org.simple.server.controller.action;

import org.simple.server.controller.IServerExchange;
import org.simple.server.model.IServerUser;
import org.simple.server.model.ServerRole;
import org.simple.server.model.repository.IServerRepository;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.simple.server.controller.action.ServerActionHelper.*;

/* UserApiAction is the REST API controller. An authenticated admin user can create,
 * modify anddelete users from the user store.
 */
class UserApiAction implements IServerAction {

    // Server user store
    private IServerRepository repository;

    // Names of mandatoryParameters to update a user. Add "password" for user creation.
    private static final String passwordParam = "password";
    private static final String roleParam = "role";

    UserApiAction(IServerRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(IServerExchange exchange) throws IOException {
        switch (exchange.getRequestMethod()) {
            case "POST":
                createUser(exchange);
                break;
            case "DELETE":
                deleteUser(exchange);
                break;
            case "PUT":
                updateUser(exchange);
                break;
            case "GET":
                getUser(exchange);
                break;
            default:
                exchange.setStatus(405,-1);
                exchange.close();
        }
    }

    private void createUser(IServerExchange exchange) throws IOException {

        Map<String, String> params = getQueryParams(exchange);

        if (null !=  params) {

            List<ServerRole> roles = getRoles(params.get(roleParam));
            String user = getUserFromRequest(exchange);
            String password = params.get(passwordParam);

            if (roles.isEmpty() || null == user || user.isEmpty() || null == password || password.isEmpty())
                sendResponse(400, "Wrong parameters", exchange);
            else {
                repository.createUser(user, password, roles);
                exchange.setStatus(200, -1);
            }
        } else {
            sendResponse(400, "Wrong parameters", exchange);
        }
        exchange.close();
    }

    private void deleteUser(IServerExchange exchange) throws IOException {
        String user = getUserFromRequest(exchange);

        if (null == user)
            sendResponse(400,"Unable to find the user",exchange);
        else if (!repository.deleteUser(user).isPresent())
            sendResponse(404,"User not found",exchange);
        else
            exchange.setStatus(200, -1);

        exchange.close();
    }

    private void updateUser(IServerExchange exchange) throws IOException {
        String user = getUserFromRequest(exchange);

        Map<String, String> params = getQueryParams(exchange);

        if (null == user || null == params)
            sendResponse(400,"Wrong parameters",exchange);
        else {
            List<ServerRole> roles = getRoles(params.get(roleParam));
            if (null == roles || roles.isEmpty())
                sendResponse(400,"Wrong parameters",exchange);
            else if (!repository.updateUser(user,roles).isPresent()) {
                sendResponse(404,"User not found",exchange);
            } else
                exchange.setStatus(200, -1);
        }

        exchange.close();
    }

    private void getUser(IServerExchange exchange) throws IOException {
        String user = getUserFromRequest(exchange);

        if (null == user)
            sendResponse(400,"Unable to find the user",exchange);
        else {
            Optional<IServerUser> sucess = repository.getUser(user);
            if (!sucess.isPresent())
                sendResponse(404, "User not found", exchange);
            else {
                sendResponse(200, sucess.get().toString(), exchange);

            }
        }

        exchange.close();
    }

}
