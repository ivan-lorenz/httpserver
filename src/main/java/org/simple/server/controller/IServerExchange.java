package org.simple.server.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;

/* IServerExchange is the interface for the Adapter pattern implemented in ServerExchange.
 *
 */
public interface IServerExchange {
    String getRequestMethod();
    URI getRequestURI();
    Map<String, List<String>> getRequestHeaders();
    Map<String, List<String>> getResponseHeaders();
    InputStream getRequestBody();
    OutputStream getResponseBody();
    void setStatus(int rCode, long responseLength)throws IOException;
    void close();
    String getUser();
}
