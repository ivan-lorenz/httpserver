package org.simple.server.controller;

import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.BasicAuthenticator;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;
import org.simple.server.application.IClock;
import org.simple.server.application.IServerRouter;
import org.simple.server.model.IServerUser;
import org.simple.server.model.ServerSession;
import org.simple.server.model.repository.IServerRepository;

import java.util.ArrayList;
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
            return repository
                    .getCredentials(s, s1)
                    .map(IServerUser::isAdmin)
                    .orElse(false);
        }
    }

    // Basic authentication.
    private ServerBasicAuthenticator basicAuthenticator;

    // Server router.
    private IServerRouter router;

    // Server realm
    private String realm;

    // System clock
    private IClock clock;

    // Guest user used to bypass authentication for not found pages and login page.
    private HttpPrincipal guest;

    // Server repository for users and sessions
    private IServerRepository repository;

    public ServerAuthenticator(IServerRouter router, String realm, IServerRepository repository, IClock clock) {
        this.router = router;
        this.realm = realm;
        this.basicAuthenticator = new ServerBasicAuthenticator(realm, repository);
        this.guest = new HttpPrincipal("guest", realm);
        this.repository = repository;
        this.clock = clock;
    }

    @Override
    public Result authenticate(HttpExchange httpExchange) {

        IServerExchange exchange = new ServerExchange(httpExchange);

        if (router.isPublic(exchange) || router.isNotFound(exchange))
            return new Success(guest);

        return authenticationStrategy(httpExchange);
    }

    private Authenticator.Result authenticationStrategy(HttpExchange httpExchange) {

        for (String header: httpExchange.getRequestHeaders().keySet()) {

            if (Objects.equals(header,"Authorization"))
                return basicAuthenticator.authenticate(httpExchange);

            if (Objects.equals(header,"Cookie"))
                return cookieAuthenticate(httpExchange, httpExchange.getRequestHeaders().getFirst("Cookie"));
        }

        // Login redirect
        httpExchange.getResponseHeaders().set("Location","login.html");
        return new Retry(301);
    }

    private Authenticator.Result cookieAuthenticate(HttpExchange exchange, String cookie) {
        String sessionString = ServerSession.getSessionFromCookie(cookie);

        if (null != sessionString) {
            return repository.getSession(sessionString)
                    .map(session -> {
                        IServerExchange e = new ServerExchange(exchange);
                        boolean validRole = router.get(e)
                                .map(scope -> scope.isValidRole(session.getServerUser().getRoles()))
                                .orElse(false);
                        return session.isValid(clock) && validRole ? new Success(new HttpPrincipal(session.getServerUser().getUser(), realm)) : new Failure(403);
                    }).orElse(new Failure(403));
        }

        exchange.getResponseHeaders().put("WWW-Authenticate", new ArrayList<String>(){{ add("Basic realm=" + realm);}});
        return new Failure(401);
    }

}
