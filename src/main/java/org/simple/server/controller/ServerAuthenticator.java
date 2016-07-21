package org.simple.server.controller;

import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.BasicAuthenticator;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;
import org.simple.server.application.IServerRouter;
import org.simple.server.model.IServerUser;
import org.simple.server.model.repository.IServerRepository;

import java.util.Objects;

/* ServerAuthenticator encapsulates all the logic behind server authorization scheme.
 * This class support Basic Authentication and Session validation.
 */
public class ServerAuthenticator extends Authenticator {


    /* ServerBasicAuthenticator is a BasicAuthenticator only used for REST API calls
     * to our server. These requests should be made by some user having ServerRole.ADMIN role,
     * so we check credentials for being an admin.
     */
    private class ServerBasicAuthenticator extends BasicAuthenticator {

        private IServerRepository repository;

        ServerBasicAuthenticator(String realm, IServerRepository repository) {
            super(realm);
            this.repository = repository;
        }

        @Override
        public boolean checkCredentials(String s, String s1) {
            IServerUser user = repository.getCredentials(s, s1);
            if (null != user && user.isAdmin())
                return true;
            return false;
        }
    }

    // Basic authentication.
    private ServerBasicAuthenticator basicAuthenticator;

    // Server router.
    private IServerRouter router;

    // Guest user used to bypass authentication for not found pages and login page.
    private HttpPrincipal guest;

    // Server repository for users and sessions
    private IServerRepository repository;

    public ServerAuthenticator(IServerRouter router, String realm, IServerRepository repository) {
        this.router = router;
        this.basicAuthenticator = new ServerBasicAuthenticator(realm, repository);
        this.guest = new HttpPrincipal("guest", realm);
        this.repository = repository;
    }

    @Override
    public Result authenticate(HttpExchange httpExchange) {

        IServerExchange exchange = new ServerExchange(httpExchange);

        if (router.isPublic(exchange) || router.isNotFound(exchange))
            return new Success(guest);

        if (isAuthenticated(httpExchange)) {

            // Manage BasicAuthorization for API requests
            return basicAuthenticator.authenticate(httpExchange);

        } else {

            // Login redirect
            httpExchange.getResponseHeaders().set("Location","login.html");
            return new Retry(301);
        }
    }

    private boolean isAuthenticated(HttpExchange httpExchange) {
        for (String header: httpExchange.getRequestHeaders().keySet()) {
            if (Objects.equals(header,"Authorization") || Objects.equals(header,"Set-Cookie"))
                return true;
        }
        return false;
    }
}
