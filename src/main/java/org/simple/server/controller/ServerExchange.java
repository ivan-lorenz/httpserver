package org.simple.server.controller;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;

/* ServerExchange implements Adapter pattern for class HttpExchange.
 * We use it to decouple dependency to HttpExchange from ours Actions,
 * easing the migration of our actions to others server libraries (like Jetty).
 */
public class ServerExchange implements IServerExchange {

    private final HttpExchange exchange;

    public ServerExchange(HttpExchange exchange) {
        this.exchange = exchange;
    }

    public String getRequestMethod() {
        return exchange.getRequestMethod();
    }

    public URI getRequestURI() {
        return exchange.getRequestURI();
    }

    public Map<String, List<String>> getRequestHeaders() {
        return exchange.getRequestHeaders();
    }

    public Map<String, List<String>> getResponseHeaders() {
        return exchange.getResponseHeaders();
    }

    public InputStream getRequestBody() throws IOException {
        return exchange.getRequestBody();
    }

    public OutputStream getResponseBody() throws IOException{
        return exchange.getResponseBody();
    }

    public void setStatus(int rCode, long responseLength) throws IOException {
        exchange.sendResponseHeaders(rCode, responseLength);
    }
}
